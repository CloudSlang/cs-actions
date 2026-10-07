/*
 * Copyright 2020-2024 Open Text
 * This program and the accompanying materials
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
import io.cloudslang.content.abbyy.entities.inputs.AbbyyInput;
import io.cloudslang.content.abbyy.entities.inputs.ProcessImageInput;
import io.cloudslang.content.abbyy.entities.others.*;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.TimeoutException;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.entities.responses.AbbyyResponse;
import io.cloudslang.content.abbyy.validators.AbbyyResultValidator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessImageServiceTest extends AbbyyServiceTest<ProcessImageInput> {

    @TempDir
    private Path tempDir;

    @Mock
    private AbbyyResultValidator xmlResultValidatorMock;
    @Mock
    private AbbyyResultValidator txtResultValidatorMock;
    @Mock
    private AbbyyResultValidator pdfResultValidatorMock;


    @Test
    public void handleTaskCompleted_exportFormatsListDoesNotMatchResultUrls_exceptionThrown() throws Exception {
        //Arrange
        ProcessImageInput requestMock = mockAbbyyRequest();
        when(requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.TXT, ExportFormat.XML, ExportFormat.PDF_SEARCHABLE));

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Collections.singletonList("txt"));

        Map<String, String> resultsDummy = new HashMap<>();

        //Act
        AbbyySdkException ex = assertThrows(AbbyySdkException.class,
                () -> this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy));

        //Assert
        assertTrue(ex.getMessage().contains(ExceptionMsgs.EXPORT_FORMAT_AND_RESULT_URLS_DO_NOT_MATCH));
        assertTrue(ex.getMessage().contains(ExportFormat.TXT.toString()));
        assertTrue(ex.getMessage().contains(ExportFormat.XML.toString()));
        assertTrue(ex.getMessage().contains(ExportFormat.PDF_SEARCHABLE.toString()));
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

        //Act
        AbbyySdkException ex = assertThrows(AbbyySdkException.class,
                () -> this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy));

        //Assert
        assertTrue(ex.getMessage().contains(ValidationException.class.getSimpleName()));
        assertTrue(ex.getMessage().contains(errMsg));
        assertTrue(ex.getMessage().contains(ExportFormat.TXT.toString()));
        assertTrue(ex.getMessage().contains(ExportFormat.XML.toString()));
        assertTrue(ex.getMessage().contains(ExportFormat.PDF_SEARCHABLE.toString()));
    }


    @Test
    public void handleTaskCompleted_validationAfterDownloadFails_exceptionThrown() throws Exception {
        //Arrange
        final String errMsg = "This is the error message";

        ProcessImageInput requestMock = mockAbbyyRequest();
        when(requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.TXT, ExportFormat.XML, ExportFormat.PDF_SEARCHABLE));
        Path destinationFileMock = mock(Path.class);
        when(destinationFileMock.toAbsolutePath()).thenReturn(tempDir);
        when(requestMock.getDestinationFile()).thenReturn(destinationFileMock);
        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Arrays.asList("txt", "xml", "pdf"));

        Map<String, String> resultsDummy = new HashMap<>();

        when(this.txtResultValidatorMock.validateAfterDownload(any(AbbyyInput.class), anyString()))
                .thenReturn(new ValidationException(errMsg));
        when(this.xmlResultValidatorMock.validateAfterDownload(any(AbbyyInput.class), anyString()))
                .thenReturn(new ValidationException(errMsg));
        when(this.pdfResultValidatorMock.validateAfterDownload(any(AbbyyInput.class), anyString()))
                .thenReturn(new ValidationException(errMsg));
        when(this.abbyyApiMock.getResult(eq(requestMock), eq("pdf"), eq(ExportFormat.PDF_SEARCHABLE),
                anyString(), eq(false))).thenAnswer(invocation -> {
            Files.createFile(Paths.get(invocation.getArgument(3, String.class)));
            return null;
        });


        //Act
        AbbyySdkException ex = assertThrows(AbbyySdkException.class,
                () -> this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy));

        //Assert
        assertTrue(ex.getMessage().contains(ValidationException.class.getSimpleName()));
        assertTrue(ex.getMessage().contains(errMsg));
        assertTrue(ex.getMessage().contains(ExportFormat.TXT.toString()));
        assertTrue(ex.getMessage().contains(ExportFormat.XML.toString()));
        assertTrue(ex.getMessage().contains(ExportFormat.PDF_SEARCHABLE.toString()));
    }


    @Test
    public void handleTaskCompleted_abbyyApiCallFails_exceptionThrown() throws Exception {
        //Arrange
        final String errMsg = "This is the error message";

        ProcessImageInput requestMock = mockAbbyyRequest();
        when(requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.TXT, ExportFormat.XML));
        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Arrays.asList("txt", "xml"));

        Map<String, String> resultsDummy = new HashMap<>();

        when(this.abbyyApiMock.getResult(eq(requestMock), anyString(), any(ExportFormat.class), isNull(), anyBoolean()))
                .thenThrow(new TimeoutException(errMsg));

        //Act
        AbbyySdkException ex = assertThrows(AbbyySdkException.class,
                () -> this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy));

        //Assert
        assertTrue(ex.getMessage().contains(TimeoutException.class.getSimpleName()), ex.getMessage());
        assertTrue(ex.getMessage().contains(errMsg));
        assertTrue(ex.getMessage().contains(ExportFormat.TXT.toString()));
        assertTrue(ex.getMessage().contains(ExportFormat.XML.toString()));
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

        when(this.abbyyApiMock.getResult(eq(requestMock), anyString(), any(ExportFormat.class), isNull(), anyBoolean()))
                .thenReturn(result);

        //Act
        this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy);
    }


    @Override
    AbbyyService<ProcessImageInput> newSutInstance() {
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
        Path sourceFileMock = Paths.get("source.pdf");
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
