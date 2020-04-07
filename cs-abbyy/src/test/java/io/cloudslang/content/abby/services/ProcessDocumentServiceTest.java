/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.abby.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.abby.constants.MiscConstants;
import io.cloudslang.content.abby.constants.OutputNames;
import io.cloudslang.content.abby.entities.*;
import io.cloudslang.content.abby.exceptions.AbbyySdkException;
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class ProcessDocumentServiceTest extends AbstractPostRequestServiceTest<ProcessDocumentInput> {

    private static final LocationId LOCATION_ID = LocationId.EU;
    private static final String APPLICATION_ID = "dummy";
    private static final String PASSWORD = "dummy";
    private static final List<String> LANGUAGES = Collections.singletonList("English");
    private static final Profile PROFILE = Profile.TEXT_EXTRACTION;
    private static final List<TextType> TEXT_TYPES = Collections.singletonList(TextType.NORMAL);
    private static final ImageSource IMAGE_SOURCE = ImageSource.AUTO;
    private static final boolean CORRECT_ORIENTATION = true;
    private static final boolean CORRECT_SKEW = true;
    private static final boolean READ_BARCODES = false;
    private static final List<ExportFormat> EXPORT_FORMATS = Arrays.asList(ExportFormat.PDF_A, ExportFormat.XML);
    private static final boolean WRITE_FORMATTING = false;
    private static final boolean WRITE_RECOGNITION_VARIANTS = false;
    private static final WriteTags WRITE_TAGS = WriteTags.AUTO;
    private static final String DESCRIPTION = "dummy";
    private static final String PDF_PASSWORD = "dummy";
    private static final short PROXY_PORT = 20;
    private static final boolean TRUST_ALL_ROOTS = false;
    private static final int CONNECT_TIMEOUT = 1;
    private static final int SOCKET_TIMEOUT = 1;
    private static final boolean KEEP_ALIVE = false;
    private static final int CONNECTIONS_MAX_PER_ROUTE = 1;
    private static final int CONNECTIONS_MAX_TOTAL = 1;


    @Override
    protected AbstractPostRequestService<ProcessDocumentInput> newSutInstance() throws ParserConfigurationException {
        return new ProcessDocumentService(this.responseParserMock, this.httpClientMock);
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockRequest();
    }


    @Override
    public void execute_validInput_correspondingUrlCalled() throws Exception {
        final String expectedUrl = "https://cloud-eu.ocrsdk.com/processImage?language=English&profile=textExtraction&textType=" +
                "normal&imageSource=auto&correctOrientation=true&correctSkew=true&readBarcodes=false&" +
                "exportFormat=pdfa%2Cxml&description=dummy&pdfPassword=dummy&xml%3AwriteFormatting=false&" +
                "xml%3AwriteRecognitionVariants=false&pdf%3AwriteTags=auto";
        mockRequest();
        when(this.httpClientMock.execute(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                any(SerializableSessionObject.class), any(GlobalSessionObject.class))).thenReturn(null);

        try {
            this.sut.execute(this.requestMock);
            fail();
        } catch (Exception ex) {
            verify(this.httpClientMock).execute(eq(expectedUrl), anyString(), anyString(), anyString(), anyString(), anyString(),
                    anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                    anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                    anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                    anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                    anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                    any(SerializableSessionObject.class), any(GlobalSessionObject.class));
        }
    }


    @Test
    public void execute_txtResultCouldNotBeRetrieved_AbbyySdkException() throws Exception {
        //Arrange
        final String taskId = "taskId";
        final int credits = 1;
        final long estimatedProcessingTime = 5L;
        final List<String> resultUrls = Arrays.asList("url1", "url2", "url3");
        final String statusCode = "123";

        when(this.requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.DOCX, ExportFormat.TXT, ExportFormat.TXT_UNSTRUCTURED));

        Map<String, String> rawResponse = new HashMap<>();
        rawResponse.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, statusCode);
        when(this.httpClientMock.execute(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                any(SerializableSessionObject.class), any(GlobalSessionObject.class))).thenReturn(rawResponse);

        AbbyyResponse responseMock = mock(AbbyyResponse.class);
        when(responseMock.getTaskStatus())
                .thenReturn(AbbyyResponse.TaskStatus.QUEUED)
                .thenReturn(AbbyyResponse.TaskStatus.COMPLETED);
        when(responseMock.getTaskId()).thenReturn(taskId);
        when(responseMock.getCredits()).thenReturn(credits);
        when(responseMock.getEstimatedProcessingTime()).thenReturn(estimatedProcessingTime);
        when(responseMock.getResultUrls()).thenReturn(resultUrls);
        when(this.responseParserMock.parseResponse(anyMapOf(String.class, String.class))).thenReturn(responseMock);

        //Assert
        exception.expect(AbbyySdkException.class);

        //Act
        this.sut.execute(this.requestMock);
    }


    @Test
    public void execute_txtResultRetrieved_Success() throws Exception {
        //Arrange
        final String taskId = "taskId";
        final int credits = 1;
        final long estimatedProcessingTime = 5L;
        final List<String> resultUrls = Arrays.asList("url1", "url2", "url3");
        final String statusCode = "200";
        final String txtResult = "txtResult";

        when(this.requestMock.getExportFormats())
                .thenReturn(Arrays.asList(ExportFormat.DOCX, ExportFormat.TXT, ExportFormat.TXT_UNSTRUCTURED));

        Map<String, String> rawResponse = new HashMap<>();
        rawResponse.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, statusCode);
        rawResponse.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, txtResult);
        when(this.httpClientMock.execute(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                any(SerializableSessionObject.class), any(GlobalSessionObject.class))).thenReturn(rawResponse);

        AbbyyResponse responseMock = mock(AbbyyResponse.class);
        when(responseMock.getTaskStatus())
                .thenReturn(AbbyyResponse.TaskStatus.QUEUED)
                .thenReturn(AbbyyResponse.TaskStatus.COMPLETED);
        when(responseMock.getTaskId()).thenReturn(taskId);
        when(responseMock.getCredits()).thenReturn(credits);
        when(responseMock.getEstimatedProcessingTime()).thenReturn(estimatedProcessingTime);
        when(responseMock.getResultUrls()).thenReturn(resultUrls);
        when(this.responseParserMock.parseResponse(anyMapOf(String.class, String.class))).thenReturn(responseMock);

        //Act
        Map<String, String> results = this.sut.execute(this.requestMock);

        //Assert
        assertEquals(taskId, results.get(OutputNames.TASK_ID));
        assertEquals(String.valueOf(credits), results.get(OutputNames.CREDITS));
        assertEquals(statusCode, results.get(OutputNames.STATUS_CODE));
        assertTrue(results.get(OutputNames.RESULT_URL).contains(resultUrls.get(0)));
        assertTrue(results.get(OutputNames.RESULT_URL).contains(resultUrls.get(1)));
        assertTrue(results.get(OutputNames.RESULT_URL).contains(resultUrls.get(2)));
        assertEquals(ReturnCodes.SUCCESS, results.get(io.cloudslang.content.constants.OutputNames.RETURN_CODE));
        assertEquals(StringUtils.EMPTY, results.get(io.cloudslang.content.constants.OutputNames.EXCEPTION));
        assertEquals(txtResult, results.get(io.cloudslang.content.constants.OutputNames.RETURN_RESULT));
    }


    private void mockRequest() {
        this.requestMock = mock(ProcessDocumentInput.class);
        when(this.requestMock.getLocationId()).thenReturn(LOCATION_ID);
        when(this.requestMock.getApplicationId()).thenReturn(APPLICATION_ID);
        when(this.requestMock.getPassword()).thenReturn(PASSWORD);
        when(this.requestMock.getLanguages()).thenReturn(LANGUAGES);
        when(this.requestMock.getProfile()).thenReturn(PROFILE);
        when(this.requestMock.getTextTypes()).thenReturn(TEXT_TYPES);
        when(this.requestMock.getImageSource()).thenReturn(IMAGE_SOURCE);
        when(this.requestMock.isCorrectOrientation()).thenReturn(CORRECT_ORIENTATION);
        when(this.requestMock.isCorrectSkew()).thenReturn(CORRECT_SKEW);
        when(this.requestMock.isReadBarcodes()).thenReturn(READ_BARCODES);
        when(this.requestMock.getExportFormats()).thenReturn(EXPORT_FORMATS);
        when(this.requestMock.isWriteFormatting()).thenReturn(WRITE_FORMATTING);
        when(this.requestMock.isWriteRecognitionVariants()).thenReturn(WRITE_RECOGNITION_VARIANTS);
        when(this.requestMock.getWriteTags()).thenReturn(WRITE_TAGS);
        when(this.requestMock.getDescription()).thenReturn(DESCRIPTION);
        when(this.requestMock.getPdfPassword()).thenReturn(PDF_PASSWORD);
    }
}