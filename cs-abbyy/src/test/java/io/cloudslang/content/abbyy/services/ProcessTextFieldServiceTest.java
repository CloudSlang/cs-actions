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
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@PrepareForTest({ProcessTextFieldService.class})
public class ProcessTextFieldServiceTest extends AbstractPostRequestServiceTest<ProcessTextFieldInput> {

    @Mock
    private AbbyyResultValidator xmlResultValidatorMock;


    @Test
    public void buildUrl_allFieldsSet_correspondingUrlReturned() throws Exception {
        //Arrange
        final String expectedUrl = "https://cloud-eu.ocrsdk.com/processTextField?language=English&letterSet=dummy" +
                "&regExp=dummy&textType=normal&oneTextLine=true&oneWordPerTextLine=true" +
                "&markingType=simpleText&placeholdersCount=1&writingStyle=default&description=dummy&pdfPassword=dummy";

        ProcessTextFieldInput requestMock = mockAbbyyRequest();

        when(requestMock.getLocationId()).thenReturn(LocationId.EU);
        when(requestMock.getLanguages()).thenReturn(Collections.singletonList("English"));
        when(requestMock.getLetterSet()).thenReturn("dummy");
        when(requestMock.getRegExp()).thenReturn("dummy");
        when(requestMock.getTextType()).thenReturn(TextType.NORMAL);
        when(requestMock.isOneTextLine()).thenReturn(true);
        when(requestMock.isOneWordPerTextLine()).thenReturn(true);
        when(requestMock.getMarkingType()).thenReturn(MarkingType.SIMPLE_TEXT);
        when(requestMock.getPlaceholdersCount()).thenReturn(1);
        when(requestMock.getDescription()).thenReturn("dummy");
        when(requestMock.getPdfPassword()).thenReturn("dummy");

        when(requestMock.getWritingStyle()).thenReturn(WritingStyle.DEFAULT);

        //Act
        String url = this.sut.buildUrl(requestMock);

        //Assert
        assertEquals(expectedUrl, url);
    }


    @Test
    public void buildUrl_someFieldsSet_correspondingUrlReturned() throws Exception {
        //Arrange
        final String expectedUrl = "https://cloud-eu.ocrsdk.com/processTextField?language=English&letterSet=dummy" +
                "&regExp=dummy&textType=normal&oneTextLine=true&oneWordPerTextLine=true" +
                "&markingType=simpleText&placeholdersCount=1";

        ProcessTextFieldInput requestMock = mockAbbyyRequest();

        when(requestMock.getLocationId()).thenReturn(LocationId.EU);
        when(requestMock.getLanguages()).thenReturn(Collections.singletonList("English"));
        when(requestMock.getLetterSet()).thenReturn("dummy");
        when(requestMock.getRegExp()).thenReturn("dummy");
        when(requestMock.getTextType()).thenReturn(TextType.NORMAL);
        when(requestMock.isOneTextLine()).thenReturn(true);
        when(requestMock.isOneWordPerTextLine()).thenReturn(true);
        when(requestMock.getMarkingType()).thenReturn(MarkingType.SIMPLE_TEXT);
        when(requestMock.getPlaceholdersCount()).thenReturn(1);
        when(requestMock.getDescription()).thenReturn(null);
        when(requestMock.getPdfPassword()).thenReturn(null);

        when(requestMock.getWritingStyle()).thenReturn(null);

        //Act
        String url = this.sut.buildUrl(requestMock);

        //Assert
        assertEquals(expectedUrl, url);
    }


    @Test
    public void handleTaskCompleted_exportFormatsListDoesNotMatchResultUrls_exceptionThrown() throws Exception {
        //Arrange
        ProcessTextFieldInput requestMock = mockAbbyyRequest();

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Collections.<String>emptyList());

        Map<String, String> resultsDummy = new HashMap<>();

        try {
            //Act
            this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy);

            //Assert
            fail();
        } catch (AbbyySdkException ex) {
            assertTrue(ex.getMessage().contains(ExceptionMsgs.EXPORT_FORMAT_AND_RESULT_URLS_DO_NOT_MATCH));
        }
    }


    @Test
    public void handleTaskCompleted_validationBeforeDownloadFails_exceptionThrown() throws Exception {
        //Arrange
        final String errMsg = "This is the error message";

        ProcessTextFieldInput requestMock = mockAbbyyRequest();

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Collections.singletonList("txt"));

        Map<String, String> resultsDummy = new HashMap<>();

        when(this.xmlResultValidatorMock.validateBeforeDownload(eq(requestMock), anyString()))
                .thenReturn(new ValidationException(errMsg));

        try {
            //Act
            this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy);

            //Assert
            fail();
        } catch (AbbyySdkException ex) {
            assertTrue(ex.toString().contains(ValidationException.class.getSimpleName()));
            assertTrue(ex.getMessage().contains(errMsg));
        }
    }


    @Test
    public void handleTaskCompleted_abbyyApiCallFails_exceptionThrown() throws Exception {
        //Arrange
        final String errMsg = "This is the error message";

        ProcessTextFieldInput requestMock = mockAbbyyRequest();

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Collections.singletonList("txt"));

        Map<String, String> resultsDummy = new HashMap<>();

        when(this.abbyyApiMock.getResult(eq(requestMock), anyString(), eq(ExportFormat.XML), anyString(), anyBoolean()))
                .thenThrow(new TimeoutException(errMsg));

        try {
            //Act
            this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy);

            //Assert
            fail();
        } catch (TimeoutException ex) {
            assertEquals(errMsg, ex.getMessage());
        }
    }


    @Test
    public void handleTaskCompleted_abbyyApiCallSucceeds_noExceptionThrown() throws Exception {
        //Arrange
        ProcessTextFieldInput requestMock = mockAbbyyRequest();

        AbbyyResponse responseMock = mockAbbyyResponse();
        when(responseMock.getResultUrls()).thenReturn(Collections.singletonList("txt"));

        Map<String, String> resultsDummy = new HashMap<>();

        //Act
        this.sut.handleTaskCompleted(requestMock, responseMock, resultsDummy);
    }


    @Override
    AbstractPostRequestService<ProcessTextFieldInput> newSutInstance() {
        return new ProcessTextFieldService(this.requestValidatorMock, this.xmlResultValidatorMock, this.abbyyApiMock);
    }


    @Override
    ProcessTextFieldInput mockAbbyyRequest() {
        ProcessTextFieldInput requestMock = mock(ProcessTextFieldInput.class);

        when(requestMock.getLocationId()).thenReturn(LocationId.EU);
        when(requestMock.getApplicationId()).thenReturn("dummy");
        when(requestMock.getPassword()).thenReturn("dummy");
        when(requestMock.getLanguages()).thenReturn(Collections.singletonList("English"));
        File sourceFileMock = mock(File.class);
        when(sourceFileMock.exists()).thenReturn(true);
        when(sourceFileMock.isFile()).thenReturn(true);
        when(requestMock.getSourceFile()).thenReturn(sourceFileMock);
        when(requestMock.getDestinationFile()).thenReturn(null);
        when(requestMock.getLetterSet()).thenReturn("dummy");
        when(requestMock.getRegExp()).thenReturn("dummy");
        when(requestMock.getTextType()).thenReturn(TextType.NORMAL);
        when(requestMock.isOneTextLine()).thenReturn(true);
        when(requestMock.isOneWordPerTextLine()).thenReturn(true);
        when(requestMock.getMarkingType()).thenReturn(MarkingType.SIMPLE_TEXT);
        when(requestMock.getPlaceholdersCount()).thenReturn(1);
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
