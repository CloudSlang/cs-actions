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

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.abbyy.constants.MiscConstants;
import io.cloudslang.content.abbyy.constants.OutputNames;
import io.cloudslang.content.abbyy.entities.*;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.validators.AbbyyResultValidator;
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@PrepareForTest({ProcessTextFieldService.class})
public class ProcessTextFieldServiceTest extends AbstractPostRequestServiceTest<ProcessTextFieldInput> {

    private static final LocationId LOCATION_ID = LocationId.EU;
    private static final String APPLICATION_ID = "dummy";
    private static final String PASSWORD = "dummy";
    private static final Region REGION = new Region(-1, -1, -1, -1);
    private static final List<String> LANGUAGES = Collections.singletonList("English");
    private static final String LETTER_SET = "dummy";
    private static final String REG_EXP = "dummy";
    private static final TextType TEXT_TYPE = TextType.NORMAL;
    private static final boolean ONE_TEXT_LINE = false;
    private static final boolean ONE_WORD_PER_TEXT_LINE = false;
    private static final MarkingType MARKING_TYPE = MarkingType.SIMPLE_TEXT;
    private static final int PLACEHOLDERS_COUNT = 1;
    private static final WritingStyle WRITING_STYLE = WritingStyle.DEFAULT;
    private static final String DESCRIPTION = "dummy";
    private static final String PDF_PASSWORD = "dummy";


    @Mock
    private AbbyyResultValidator resultValidatorMock;


    @Override
    protected AbstractPostRequestService<ProcessTextFieldInput> newSutInstance() throws ParserConfigurationException, SAXException {
        return new ProcessTextFieldService(this.responseParserMock, this.httpClientMock, this.requestValidator, this.resultValidatorMock);
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockRequest();
    }


    @Override
    public void execute_validInput_correspondingUrlCalled() throws Exception {
        final String expectedUrl = "https://cloud-eu.ocrsdk.com/processTextField?region=-1%2C-1%2C-1%2C-1&language=English" +
                "&letterSet=dummy&regExp=dummy&textType=normal&oneTextLine=false&oneWordPerTextLine=false&markingType=simpleText" +
                "&placeholdersCount=1&writingStyle=default&description=dummy&pdfPassword=dummy";
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
    public void execute_xmlResultCouldNotBeRetrieved_AbbyySdkException() throws Exception {
        //Arrange
        final String taskId = "taskId";
        final int credits = 1;
        final long estimatedProcessingTime = 5L;
        final List<String> resultUrls = Arrays.asList("url1", "url2", "url3");
        final String statusCode = "123";

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
        final String xmlResult = "txtResult";

        Map<String, String> rawResponse = new HashMap<>();
        rawResponse.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, statusCode);
        rawResponse.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xmlResult);
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
        assertEquals(xmlResult, results.get(OutputNames.XML_RESULT));
        assertEquals(ReturnCodes.SUCCESS, results.get(io.cloudslang.content.constants.OutputNames.RETURN_CODE));
        assertEquals(StringUtils.EMPTY, results.get(io.cloudslang.content.constants.OutputNames.EXCEPTION));
    }


    @Test
    public void execute_destinationFileIsNotNull_resultIsWrittenToCorrespondingFile() throws Exception {
        //Arrange
        final String taskId = "taskId";
        final int credits = 1;
        final long estimatedProcessingTime = 5L;
        final List<String> resultUrls = Arrays.asList("url1", "url2", "url3");
        final String statusCode = "200";
        final String txtResult = "txtResult";
        final String sourceFileName = "sourceFile";
        final String destinationFilePath = "destDir";

        File destinationFileMock = mock(File.class);
        when(destinationFileMock.getAbsolutePath()).thenReturn(destinationFilePath);
        when(this.requestMock.getDestinationFile()).thenReturn(destinationFileMock);
        File sourceFileMock = mock(File.class);
        when(sourceFileMock.getName()).thenReturn(sourceFileName);
        when(this.requestMock.getSourceFile()).thenReturn(sourceFileMock);

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

        FileWriter fileWriterMock = PowerMockito.mock(FileWriter.class);
        PowerMockito.whenNew(FileWriter.class).withAnyArguments().thenReturn(fileWriterMock);

        //Act
        this.sut.execute(this.requestMock);

        //Assert
        PowerMockito.verifyNew(FileWriter.class).withArguments(eq(destinationFileMock));
        verify(fileWriterMock).write(eq(txtResult));
    }


    private void mockRequest() {
        this.requestMock = mock(ProcessTextFieldInput.class);
        when(this.requestMock.getLocationId()).thenReturn(LOCATION_ID);
        when(this.requestMock.getApplicationId()).thenReturn(APPLICATION_ID);
        when(this.requestMock.getPassword()).thenReturn(PASSWORD);
        when(this.requestMock.getRegion()).thenReturn(REGION);
        when(this.requestMock.getLanguages()).thenReturn(LANGUAGES);
        when(this.requestMock.getLetterSet()).thenReturn(LETTER_SET);
        when(this.requestMock.getRegExp()).thenReturn(REG_EXP);
        when(this.requestMock.getTextType()).thenReturn(TEXT_TYPE);
        when(this.requestMock.isOneTextLine()).thenReturn(ONE_TEXT_LINE);
        when(this.requestMock.isOneWordPerTextLine()).thenReturn(ONE_WORD_PER_TEXT_LINE);
        when(this.requestMock.getMarkingType()).thenReturn(MARKING_TYPE);
        when(this.requestMock.getPlaceholdersCount()).thenReturn(PLACEHOLDERS_COUNT);
        when(this.requestMock.getWritingStyle()).thenReturn(WRITING_STYLE);
        when(this.requestMock.getDescription()).thenReturn(DESCRIPTION);
        when(this.requestMock.getPdfPassword()).thenReturn(PDF_PASSWORD);
        when(this.requestMock.getSourceFile()).thenReturn(mock(File.class));
    }
}
