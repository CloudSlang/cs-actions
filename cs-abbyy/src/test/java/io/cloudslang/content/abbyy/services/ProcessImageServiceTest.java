/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.abbyy.services;

import io.cloudslang.content.abbyy.constants.ExceptionMsgs;
import io.cloudslang.content.abbyy.entities.*;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.TimeoutException;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.http.AbbyyResponse;
import io.cloudslang.content.abbyy.validators.AbbyyResultValidator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest({ProcessImageService.class})
public class ProcessImageServiceTest extends AbstractPostRequestServiceTest<ProcessImageInput> {


    @Mock
    private AbbyyResultValidator xmlResultValidatorMock;
    @Mock
    private AbbyyResultValidator txtResultValidatorMock;
    @Mock
    private AbbyyResultValidator pdfResultValidatorMock;


    @Test
    public void buildUrl_allFieldsSet_correspondingUrlReturned() throws Exception {
        //Arrange
        final String expectedUrl = "https://cloud-eu.ocrsdk.com/processImage?language=English&profile=textExtraction&textType=" +
                "normal&imageSource=auto&correctOrientation=true&correctSkew=true&readBarcodes=false&" +
                "exportFormat=pdfSearchable%2Cxml%2Ctxt&description=dummy&pdfPassword=dummy&xml%3AwriteFormatting=true&" +
                "xml%3AwriteRecognitionVariants=true&pdf%3AwriteTags=auto";

        ProcessImageInput requestMock = mockAbbyyRequest();

        when(requestMock.getLocationId()).thenReturn(LocationId.EU);
        when(requestMock.getLanguages()).thenReturn(Collections.singletonList("English"));
        when(requestMock.getTextTypes()).thenReturn(Collections.singletonList(TextType.NORMAL));
        when(requestMock.getImageSource()).thenReturn(ImageSource.AUTO);
        when(requestMock.isCorrectOrientation()).thenReturn(true);
        when(requestMock.isCorrectSkew()).thenReturn(true);
        when(requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.PDF_SEARCHABLE, ExportFormat.XML, ExportFormat.TXT));
        when(requestMock.getDescription()).thenReturn("dummy");
        when(requestMock.getPdfPassword()).thenReturn("dummy");

        when(requestMock.getProfile()).thenReturn(Profile.TEXT_EXTRACTION);
        when(requestMock.getWriteTags()).thenReturn(WriteTags.AUTO);
        when(requestMock.isWriteFormatting()).thenReturn(true);
        when(requestMock.isWriteRecognitionVariants()).thenReturn(true);

        //Act
        String url = this.sut.buildUrl(requestMock);

        //Assert
        assertEquals(expectedUrl, url);
    }


    @Test
    public void buildUrl_someFieldsSet_correspondingUrlReturned() throws Exception {
        //Arrange
        final String expectedUrl = "https://cloud-eu.ocrsdk.com/processImage?textType=" +
                "normal&imageSource=auto&correctOrientation=false&correctSkew=true&readBarcodes=false&" +
                "exportFormat=txt&description=dummy";

        ProcessImageInput requestMock = mockAbbyyRequest();

        when(requestMock.getLocationId()).thenReturn(LocationId.EU);
        when(requestMock.getLanguages()).thenReturn(null);
        when(requestMock.getTextTypes()).thenReturn(Collections.singletonList(TextType.NORMAL));
        when(requestMock.getImageSource()).thenReturn(ImageSource.AUTO);
        when(requestMock.isCorrectOrientation()).thenReturn(false);
        when(requestMock.isCorrectSkew()).thenReturn(true);
        when(requestMock.getExportFormats()).thenReturn(Collections.singletonList(ExportFormat.TXT));
        when(requestMock.getDescription()).thenReturn("dummy");
        when(requestMock.getPdfPassword()).thenReturn(StringUtils.EMPTY);

        when(requestMock.getProfile()).thenReturn(null);
        when(requestMock.getWriteTags()).thenReturn(WriteTags.AUTO);
        when(requestMock.isWriteFormatting()).thenReturn(true);
        when(requestMock.isWriteRecognitionVariants()).thenReturn(true);

        //Act
        String url = this.sut.buildUrl(requestMock);

        //Assert
        assertEquals(expectedUrl, url);
    }


    @Test
    public void handleTaskCompleted_exportFormatsListDoesNotMatchResultUrls_exceptionThrown() throws Exception {
        //Arrange
        ProcessImageInput requestMock = mockAbbyyRequest();
        when(requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.TXT, ExportFormat.XML, ExportFormat.PDF_SEARCHABLE));

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Collections.singletonList("txt"));

        Map<String, String> resultsDummy = new HashMap<>();

        try {
            //Act
            this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy);

            //Assert
            fail();
        } catch (AbbyySdkException ex) {
            assertTrue(ex.getMessage().contains(ExceptionMsgs.EXPORT_FORMAT_AND_RESULT_URLS_DO_NOT_MATCH));
            assertTrue(ex.getMessage().contains(ExportFormat.TXT.toString()));
            assertTrue(ex.getMessage().contains(ExportFormat.XML.toString()));
            assertTrue(ex.getMessage().contains(ExportFormat.PDF_SEARCHABLE.toString()));
        }
    }


    @Test
    public void handleTaskCompleted_validationBeforeDownloadFails_exceptionThrown() throws Exception {
        //Arrange
        final String errMsg = "This is the error message";

        ProcessImageInput requestMock = mockAbbyyRequest();
        when(requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.TXT, ExportFormat.XML, ExportFormat.PDF_SEARCHABLE));

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Arrays.asList("txt", "xml", "pdf"));

        Map<String, String> resultsDummy = new HashMap<>();

        when(this.txtResultValidatorMock.validateBeforeDownload(eq(requestMock), anyString()))
                .thenReturn(new ValidationException(errMsg));
        when(this.xmlResultValidatorMock.validateBeforeDownload(eq(requestMock), anyString()))
                .thenReturn(new ValidationException(errMsg));
        when(this.pdfResultValidatorMock.validateBeforeDownload(eq(requestMock), anyString()))
                .thenReturn(new ValidationException(errMsg));

        try {
            //Act
            this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy);

            //Assert
            fail();
        } catch (AbbyySdkException ex) {
            assertTrue(ex.getMessage().contains(ValidationException.class.getSimpleName()));
            assertTrue(ex.getMessage().contains(errMsg));
            assertTrue(ex.getMessage().contains(ExportFormat.TXT.toString()));
            assertTrue(ex.getMessage().contains(ExportFormat.XML.toString()));
            assertTrue(ex.getMessage().contains(ExportFormat.PDF_SEARCHABLE.toString()));
        }
    }


    @Test
    public void handleTaskCompleted_validationAfterDownloadFails_exceptionThrown() throws Exception {
        //Arrange
        final String errMsg = "This is the error message";

        ProcessImageInput requestMock = mockAbbyyRequest();
        when(requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.TXT, ExportFormat.XML, ExportFormat.PDF_SEARCHABLE));
        File destinationFileMock = mock(File.class);
        when(destinationFileMock.getAbsolutePath()).thenReturn(StringUtils.EMPTY);
        when(requestMock.getDestinationFile()).thenReturn(destinationFileMock);

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Arrays.asList("txt", "xml", "pdf"));

        Map<String, String> resultsDummy = new HashMap<>();

        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(mock(File.class));
        PowerMockito.whenNew(FileWriter.class).withAnyArguments().thenReturn(mock(FileWriter.class));

        when(this.txtResultValidatorMock.validateAfterDownload(anyString()))
                .thenReturn(new ValidationException(errMsg));
        when(this.xmlResultValidatorMock.validateAfterDownload(anyString()))
                .thenReturn(new ValidationException(errMsg));
        when(this.pdfResultValidatorMock.validateAfterDownload(anyString()))
                .thenReturn(new ValidationException(errMsg));


        try {
            //Act
            this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy);

            //Assert
            fail();
        } catch (AbbyySdkException ex) {
            assertTrue(ex.getMessage().contains(ValidationException.class.getSimpleName()));
            assertTrue(ex.getMessage().contains(errMsg));
            assertTrue(ex.getMessage().contains(ExportFormat.TXT.toString()));
            assertTrue(ex.getMessage().contains(ExportFormat.XML.toString()));
            assertTrue(ex.getMessage().contains(ExportFormat.PDF_SEARCHABLE.toString()));
        }
    }


    @Test
    public void handleTaskCompleted_abbyyApiCallFails_exceptionThrown() throws Exception {
        //Arrange
        final String errMsg = "This is the error message";

        ProcessImageInput requestMock = mockAbbyyRequest();
        when(requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.TXT, ExportFormat.XML, ExportFormat.PDF_SEARCHABLE));
        File destinationFileMock = mock(File.class);
        when(destinationFileMock.getAbsolutePath()).thenReturn(StringUtils.EMPTY);
        when(requestMock.getDestinationFile()).thenReturn(destinationFileMock);

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Arrays.asList("txt", "xml", "pdf"));

        Map<String, String> resultsDummy = new HashMap<>();

        PowerMockito.whenNew(File.class).withAnyArguments().thenReturn(mock(File.class));
        PowerMockito.whenNew(FileWriter.class).withAnyArguments().thenReturn(mock(FileWriter.class));

        when(this.abbyyApiMock.getResult(eq(requestMock), anyString(), any(ExportFormat.class), anyString(), anyBoolean()))
                .thenThrow(new TimeoutException(errMsg));

        try {
            //Act
            this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy);

            //Assert
            fail();
        } catch (AbbyySdkException ex) {
            assertTrue(ex.getMessage().contains(TimeoutException.class.getSimpleName()));
            assertTrue(ex.getMessage().contains(errMsg));
            assertTrue(ex.getMessage().contains(ExportFormat.TXT.toString()));
            assertTrue(ex.getMessage().contains(ExportFormat.XML.toString()));
            assertTrue(ex.getMessage().contains(ExportFormat.PDF_SEARCHABLE.toString()));
        }
    }


    @Test
    public void handleTaskCompleted_abbyyApiCallSucceeds_noExceptionThrown() throws Exception {
        //Arrange
        final String result = "result";

        ProcessImageInput requestMock = mockAbbyyRequest();
        when(requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.TXT, ExportFormat.XML, ExportFormat.PDF_SEARCHABLE));

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Arrays.asList("txt", "xml", "pdf"));

        Map<String, String> resultsDummy = new HashMap<>();

        when(this.abbyyApiMock.getResult(eq(requestMock), anyString(), any(ExportFormat.class), anyString(), anyBoolean()))
                .thenReturn(result);

        //Act
        this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy);
    }


    @Override
    AbstractPostRequestService<ProcessImageInput> newSutInstance() {
        return new ProcessImageService(requestValidatorMock, xmlResultValidatorMock, txtResultValidatorMock,
                pdfResultValidatorMock, abbyyApiMock);
    }


    @Override
    ProcessImageInput mockAbbyyRequest() {
        ProcessImageInput requestMock = mock(ProcessImageInput.class);

        when(requestMock.getLocationId()).thenReturn(LocationId.EU);
        when(requestMock.getApplicationId()).thenReturn("dummy");
        when(requestMock.getPassword()).thenReturn("dummy");
        when(requestMock.getLanguages()).thenReturn(Collections.singletonList("English"));
        File sourceFileMock = mock(File.class);
        when(sourceFileMock.exists()).thenReturn(true);
        when(sourceFileMock.isFile()).thenReturn(true);
        when(requestMock.getSourceFile()).thenReturn(sourceFileMock);
        when(requestMock.getDestinationFile()).thenReturn(null);
        when(requestMock.getProfile()).thenReturn(Profile.TEXT_EXTRACTION);
        when(requestMock.getTextTypes()).thenReturn(Collections.singletonList(TextType.NORMAL));
        when(requestMock.getImageSource()).thenReturn(ImageSource.AUTO);
        when(requestMock.isCorrectOrientation()).thenReturn(true);
        when(requestMock.isCorrectSkew()).thenReturn(true);
        when(requestMock.getExportFormats()).thenReturn(Collections.singletonList(ExportFormat.PDF_SEARCHABLE));
        when(requestMock.getWriteTags()).thenReturn(WriteTags.AUTO);
        when(requestMock.getDescription()).thenReturn("dummy");
        when(requestMock.getPdfPassword()).thenReturn("dummy");

        when(requestMock.getProxyPort()).thenReturn((short) 8080);
        when(requestMock.getConnectTimeout()).thenReturn(0);
        when(requestMock.getSocketTimeout()).thenReturn(0);
        when(requestMock.getConnectionsMaxPerRoute()).thenReturn(20);
        when(requestMock.getConnectionsMaxTotal()).thenReturn(0);

        return requestMock;
    }
}
