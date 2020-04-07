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
import io.cloudslang.content.abby.entities.AbbyyRequest;
import io.cloudslang.content.abby.entities.AbbyyResponse;
import io.cloudslang.content.abby.exceptions.AbbyySdkException;
import io.cloudslang.content.abby.exceptions.ServerSideException;
import io.cloudslang.content.abby.utils.AbbyyResponseParser;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.httpclient.actions.HttpClientAction;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public abstract class AbstractPostRequestServiceTest<R extends AbbyyRequest> {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    protected AbstractPostRequestService<R> sut;
    protected R requestMock;
    @Mock
    protected HttpClientAction httpClientMock;
    @Mock
    protected AbbyyResponseParser responseParserMock;


    protected abstract AbstractPostRequestService<R> newSutInstance() throws ParserConfigurationException;


    @Before
    public void setUp() throws Exception {
        this.sut = newSutInstance();
    }


    @Test
    public void execute_requestIsNull_IllegalArgumentException() throws Exception {
        //Arrange
        this.requestMock = null;
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        this.sut.execute(requestMock);
    }


    @Test
    public abstract void execute_validInput_correspondingUrlCalled() throws Exception;


    @Test
    public void execute_notEnoughCredits_AbbyySdkException() throws Exception {
        //Arrange
        Map<String, String> rawResponse = new HashMap<>();
        rawResponse.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "123");
        when(this.httpClientMock.execute(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                any(SerializableSessionObject.class), any(GlobalSessionObject.class))).thenReturn(rawResponse);

        AbbyyResponse responseMock = mock(AbbyyResponse.class);
        when(responseMock.getTaskStatus()).thenReturn(AbbyyResponse.TaskStatus.NOT_ENOUGH_CREDITS);
        when(responseMock.getCredits()).thenReturn(1);
        when(this.responseParserMock.parseResponse(anyMapOf(String.class, String.class))).thenReturn(responseMock);

        //Assert
        exception.expect(AbbyySdkException.class);

        //Act
        this.sut.execute(this.requestMock);
    }


    @Test
    public void execute_taskDoesNotFinishInTime_TimeoutException() throws Exception {
        //Arrange
        Map<String, String> rawResponse = new HashMap<>();
        rawResponse.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "123");
        when(this.httpClientMock.execute(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString(),
                any(SerializableSessionObject.class), any(GlobalSessionObject.class))).thenReturn(rawResponse);

        AbbyyResponse responseMock = mock(AbbyyResponse.class);
        when(responseMock.getTaskStatus()).thenReturn(AbbyyResponse.TaskStatus.IN_PROGRESS);
        when(responseMock.getCredits()).thenReturn(1);
        when(responseMock.getEstimatedProcessingTime()).thenReturn(5L);
        when(this.responseParserMock.parseResponse(anyMapOf(String.class, String.class))).thenReturn(responseMock);

        try {
            //Act
            this.sut.execute(this.requestMock);
            fail();
        } catch (AbbyySdkException ex) {
            //Assert
            assertTrue(ex.getCause() instanceof TimeoutException);
        }
    }


    @Test
    public void execute_taskDeletedMeanwhile_AbbyySdkException() throws Exception {
        //Arrange
        Map<String, String> rawResponse = new HashMap<>();
        rawResponse.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "123");
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
                .thenReturn(AbbyyResponse.TaskStatus.DELETED);
        when(responseMock.getCredits()).thenReturn(1);
        when(responseMock.getEstimatedProcessingTime()).thenReturn(5L);
        when(this.responseParserMock.parseResponse(anyMapOf(String.class, String.class))).thenReturn(responseMock);

        //Assert
        exception.expect(AbbyySdkException.class);

        //Act
        this.sut.execute(this.requestMock);
    }


    @Test
    public void execute_processingFailed_ServerSideException() throws Exception {
        //Arrange
        final String serverSideErrorMessage = "serverSideErrorMessage";

        Map<String, String> rawResponse = new HashMap<>();
        rawResponse.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "123");
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
                .thenReturn(AbbyyResponse.TaskStatus.PROCESSING_FAILED);
        when(responseMock.getCredits()).thenReturn(1);
        when(responseMock.getEstimatedProcessingTime()).thenReturn(5L);
        when(responseMock.getErrorMessage()).thenReturn(serverSideErrorMessage);
        when(this.responseParserMock.parseResponse(anyMapOf(String.class, String.class))).thenReturn(responseMock);

        try {
            //Act
            this.sut.execute(this.requestMock);
            fail();
        } catch (ServerSideException ex) {
            //Assert
            assertTrue(ex.getMessage().contains(serverSideErrorMessage));
        }
    }


    @Test
    public void execute_processingCompleted_Success() throws Exception {
        //Arrange
        final String taskId = "taskId";
        final int credits = 1;
        final long estimatedProcessingTime = 5L;
        final List<String> resultUrls = Arrays.asList("url1", "url2", "url3");
        final String statusCode = "200";

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
    }
}