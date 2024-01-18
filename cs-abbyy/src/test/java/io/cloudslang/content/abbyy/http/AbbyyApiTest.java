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


package io.cloudslang.content.abbyy.http;

import io.cloudslang.content.abbyy.constants.Headers;
import io.cloudslang.content.abbyy.entities.others.ExportFormat;
import io.cloudslang.content.abbyy.entities.others.LocationId;
import io.cloudslang.content.abbyy.entities.requests.HttpClientRequest;
import io.cloudslang.content.abbyy.entities.responses.HttpClientResponse;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ClientSideException;
import io.cloudslang.content.abbyy.entities.inputs.AbbyyInput;
import io.cloudslang.content.constants.ReturnCodes;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbbyyApi.class, HttpClient.class})
public class AbbyyApiTest {

    private AbbyyApi sut;
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Mock
    private AbbyyResponseParser responseParserMock;


    @Before
    public void setUp() throws ParserConfigurationException {
        this.sut = new AbbyyApi(responseParserMock);
    }


    @Test
    public void postRequest_httpClientCallReturns401_ClientSideException() throws Exception {
        //Arrange
        final String statusCode = "401";

        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        when(abbyyInput.getSourceFile()).thenReturn(mock(Path.class));

        HttpClientRequest.Builder builderSpy = new HttpClientRequest.Builder();
        PowerMockito.spy(builderSpy);

        HttpClientResponse responseMock = mock(HttpClientResponse.class);
        when(responseMock.getReturnCode()).thenReturn(ReturnCodes.SUCCESS);
        when(responseMock.getStatusCode()).thenReturn(Short.parseShort(statusCode));

        PowerMockito.mockStatic(HttpClient.class);
        PowerMockito.when(HttpClient.class, "execute", any(HttpClientRequest.class)).thenReturn(responseMock);

        //Assert
        this.exception.expect(ClientSideException.class);

        //Act
        this.sut.request(abbyyInput);
        assertEquals(statusCode, this.sut.getLastStatusCode());
    }


    @Test
    public void postRequest_httpClientCallReturnsSuccess_Success() throws Exception {
        //Arrange
        final String statusCode = "200";
        final String url = "url";

        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        when(abbyyInput.getSourceFile()).thenReturn(mock(Path.class));

        HttpClientRequest.Builder builderSpy = new HttpClientRequest.Builder();
        PowerMockito.spy(builderSpy);

        HttpClientResponse responseMock = mock(HttpClientResponse.class);
        when(responseMock.getReturnCode()).thenReturn(ReturnCodes.SUCCESS);
        when(responseMock.getStatusCode()).thenReturn(Short.parseShort(statusCode));

        PowerMockito.mockStatic(HttpClient.class);
        PowerMockito.when(HttpClient.class, "execute", any(HttpClientRequest.class)).thenReturn(responseMock);

        //Act
        this.sut.request(abbyyInput);

        //Assert
        verify(this.responseParserMock).parseResponse(responseMock);
        assertEquals(statusCode, this.sut.getLastStatusCode());
    }


    @Test
    public void getTaskStatus_httpClientCallSucceeds_Success() throws Exception {
        //Arrange
        final String statusCode = "203";
        final String taskId = "taskid";

        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        when(abbyyInput.getLocationId()).thenReturn(LocationId.EU);
        when(abbyyInput.getSourceFile()).thenReturn(mock(Path.class));

        HttpClientRequest.Builder builderSpy = new HttpClientRequest.Builder();
        PowerMockito.spy(builderSpy);

        HttpClientResponse responseMock = mock(HttpClientResponse.class);
        when(responseMock.getReturnCode()).thenReturn(ReturnCodes.SUCCESS);
        when(responseMock.getStatusCode()).thenReturn(Short.parseShort(statusCode));

        PowerMockito.mockStatic(HttpClient.class);
        PowerMockito.when(HttpClient.class, "execute", any(HttpClientRequest.class)).thenReturn(responseMock);

        //Act
        this.sut.getTaskStatus(abbyyInput, taskId);

        //Assert
        verify(responseParserMock).parseResponse(responseMock);
        assertEquals(statusCode, this.sut.getLastStatusCode());
    }


    @Test
    public void getResult_httpClientCallNotOk_AbbyySdkException() throws Exception {
        //Arrange
        final String resultUrl = "resultUrl";
        final ExportFormat exportFormat = ExportFormat.XML;
        final String downloadPath = null;
        final boolean useSpecificCharSet = false;

        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        when(abbyyInput.getLocationId()).thenReturn(LocationId.EU);
        when(abbyyInput.getSourceFile()).thenReturn(mock(Path.class));

        HttpClientRequest.Builder builderSpy = new HttpClientRequest.Builder();
        PowerMockito.spy(builderSpy);

        HttpClientResponse responseMock = mock(HttpClientResponse.class);
        when(responseMock.getReturnCode()).thenReturn(ReturnCodes.SUCCESS);
        when(responseMock.getStatusCode()).thenReturn((short) 0);

        PowerMockito.mockStatic(HttpClient.class);
        PowerMockito.when(HttpClient.class, "execute", any(HttpClientRequest.class)).thenReturn(responseMock);

        //Assert
        this.exception.expect(AbbyySdkException.class);

        //Act
        this.sut.getResult(abbyyInput, resultUrl, exportFormat, downloadPath, useSpecificCharSet);
    }


    @Test
    public void getResult_httpClientCallSucceeds_Success() throws Exception {
        //Arrange
        final String statusCode = "200";
        final String expectedReturnResult = "expected";

        final String resultUrl = "resultUrl";
        final ExportFormat exportFormat = ExportFormat.XML;
        final String downloadPath = null;
        final boolean useSpecificCharSet = false;

        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        when(abbyyInput.getLocationId()).thenReturn(LocationId.EU);
        when(abbyyInput.getSourceFile()).thenReturn(mock(Path.class));

        HttpClientRequest.Builder builderSpy = new HttpClientRequest.Builder();
        PowerMockito.spy(builderSpy);

        HttpClientResponse responseMock = mock(HttpClientResponse.class);
        when(responseMock.getReturnCode()).thenReturn(ReturnCodes.SUCCESS);
        when(responseMock.getStatusCode()).thenReturn(Short.parseShort(statusCode));
        when(responseMock.getReturnResult()).thenReturn(expectedReturnResult);

        PowerMockito.mockStatic(HttpClient.class);
        PowerMockito.when(HttpClient.class, "execute", any(HttpClientRequest.class)).thenReturn(responseMock);

        //Act
        String returnResult = this.sut.getResult(abbyyInput, resultUrl, exportFormat, downloadPath, useSpecificCharSet);

        //Assert
        assertEquals(expectedReturnResult, returnResult);
        assertEquals(statusCode, this.sut.getLastStatusCode());
    }


    @Test
    public void getResultSize_httpClientCallIsNotOk_AbbyySdkException() throws Exception {
        //Arrange
        final String statusCode = "0";

        final String resultUrl = "resultUrl";
        final ExportFormat exportFormat = ExportFormat.XML;

        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        when(abbyyInput.getLocationId()).thenReturn(LocationId.EU);
        when(abbyyInput.getSourceFile()).thenReturn(mock(Path.class));

        HttpClientRequest.Builder builderSpy = new HttpClientRequest.Builder();
        PowerMockito.spy(builderSpy);

        HttpClientResponse responseMock = mock(HttpClientResponse.class);
        when(responseMock.getReturnCode()).thenReturn(ReturnCodes.SUCCESS);
        when(responseMock.getStatusCode()).thenReturn(Short.parseShort(statusCode));

        PowerMockito.mockStatic(HttpClient.class);
        PowerMockito.when(HttpClient.class, "execute", any(HttpClientRequest.class)).thenReturn(responseMock);

        try {
            //Act
            this.sut.getResultSize(abbyyInput, resultUrl, exportFormat);

            //Assert
            fail();
        } catch (AbbyySdkException ex) {
            assertEquals(statusCode, this.sut.getLastStatusCode());
        }
    }


    @Test
    public void getResultSize_contentLengthHeaderIsMissing_AbbyySdkException() throws Exception {
        //Arrange
        final String statusCode = "200";

        final String resultUrl = "resultUrl";
        final ExportFormat exportFormat = ExportFormat.XML;

        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        when(abbyyInput.getLocationId()).thenReturn(LocationId.EU);
        when(abbyyInput.getSourceFile()).thenReturn(mock(Path.class));

        HttpClientRequest.Builder builderSpy = new HttpClientRequest.Builder();
        PowerMockito.spy(builderSpy);

        HttpClientResponse responseMock = mock(HttpClientResponse.class);
        when(responseMock.getReturnCode()).thenReturn(ReturnCodes.SUCCESS);
        when(responseMock.getStatusCode()).thenReturn(Short.parseShort(statusCode));
        Properties responseHeaders = mock(Properties.class);
        when(responseHeaders.getProperty(eq(Headers.CONTENT_LENGTH))).thenReturn(StringUtils.EMPTY);
        when(responseMock.getResponseHeaders()).thenReturn(responseHeaders);

        PowerMockito.mockStatic(HttpClient.class);
        PowerMockito.when(HttpClient.class, "execute", any(HttpClientRequest.class)).thenReturn(responseMock);

        try {
            //Act
            this.sut.getResultSize(abbyyInput, resultUrl, exportFormat);

            //Assert
            fail();
        } catch (AbbyySdkException ex) {
            assertEquals(statusCode, this.sut.getLastStatusCode());
        }
    }


    @Test
    public void getResultSize_httpClientCallSucceeds_Success() throws Exception {
        //Arrange
        final String statusCode = "200";
        final String expectedSize = "123";

        final String resultUrl = "resultUrl";
        final ExportFormat exportFormat = ExportFormat.XML;

        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        when(abbyyInput.getLocationId()).thenReturn(LocationId.EU);
        when(abbyyInput.getSourceFile()).thenReturn(mock(Path.class));

        HttpClientRequest.Builder builderSpy = new HttpClientRequest.Builder();
        PowerMockito.spy(builderSpy);

        HttpClientResponse responseMock = mock(HttpClientResponse.class);
        when(responseMock.getReturnCode()).thenReturn(ReturnCodes.SUCCESS);
        when(responseMock.getStatusCode()).thenReturn(Short.parseShort(statusCode));
        Properties responseHeaders = mock(Properties.class);
        when(responseHeaders.getProperty(eq(Headers.CONTENT_LENGTH))).thenReturn(expectedSize);
        when(responseMock.getResponseHeaders()).thenReturn(responseHeaders);

        PowerMockito.mockStatic(HttpClient.class);
        PowerMockito.when(HttpClient.class, "execute", any(HttpClientRequest.class)).thenReturn(responseMock);

        //Act
        long size = this.sut.getResultSize(abbyyInput, resultUrl, exportFormat);

        //Assert
        assertEquals(expectedSize, String.valueOf(size));
        assertEquals(statusCode, this.sut.getLastStatusCode());
    }


    @Test
    public void getResultChunk_startByteIndexIsNegative_IllegalArgumentException() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String resultUrl = "resultUrl";
        final ExportFormat exportFormat = ExportFormat.XML;
        final int startByteIndex = -1;
        final int endByteIndex = startByteIndex + 1;
        //Assert
        this.exception.expect(IllegalArgumentException.class);
        //Act
        this.sut.getResultChunk(abbyyInput, resultUrl, exportFormat, startByteIndex, endByteIndex);
    }


    @Test
    public void getResultChunk_endByteIndexIsNegative_IllegalArgumentException() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String resultUrl = "resultUrl";
        final ExportFormat exportFormat = ExportFormat.XML;
        final int startByteIndex = 0;
        final int endByteIndex = -1;
        //Assert
        this.exception.expect(IllegalArgumentException.class);
        //Act
        this.sut.getResultChunk(abbyyInput, resultUrl, exportFormat, startByteIndex, endByteIndex);
    }


    @Test
    public void getResultChunk_illegalInterval_IllegalArgumentException() throws Exception {
        //Arrange
        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        final String resultUrl = "resultUrl";
        final ExportFormat exportFormat = ExportFormat.XML;
        final int startByteIndex = 2;
        final int endByteIndex = startByteIndex - 1;
        //Assert
        this.exception.expect(IllegalArgumentException.class);
        //Act
        this.sut.getResultChunk(abbyyInput, resultUrl, exportFormat, startByteIndex, endByteIndex);
    }


    @Test
    public void getResultChunk_httpClientCallIsNotOk_AbbyySdkException() throws Exception {
        //Arrange
        final String statusCode = "0";

        final String resultUrl = "resultUrl";
        final ExportFormat exportFormat = ExportFormat.XML;
        final int startByteIndex = 2;
        final int endByteIndex = startByteIndex + 1;

        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        when(abbyyInput.getLocationId()).thenReturn(LocationId.EU);
        when(abbyyInput.getSourceFile()).thenReturn(mock(Path.class));

        HttpClientRequest.Builder builderSpy = new HttpClientRequest.Builder();
        PowerMockito.spy(builderSpy);

        HttpClientResponse responseMock = mock(HttpClientResponse.class);
        when(responseMock.getReturnCode()).thenReturn(ReturnCodes.SUCCESS);
        when(responseMock.getStatusCode()).thenReturn(Short.parseShort(statusCode));

        PowerMockito.mockStatic(HttpClient.class);
        PowerMockito.when(HttpClient.class, "execute", any(HttpClientRequest.class)).thenReturn(responseMock);

        try {
            //Act
            this.sut.getResultChunk(abbyyInput, resultUrl, exportFormat, startByteIndex, endByteIndex);

            //Assert
            fail();
        } catch (AbbyySdkException ex) {
            assertEquals(statusCode, this.sut.getLastStatusCode());
        }
    }


    @Test
    public void getResultChunk_httpClientCallSucceeds_Success() throws Exception {
        //Arrange
        final String statusCode = "206";
        final String expectedReturnResult = "expected";

        final String resultUrl = "resultUrl";
        final ExportFormat exportFormat = ExportFormat.XML;
        final int startByteIndex = 2;
        final int endByteIndex = startByteIndex + 1;

        final AbbyyInput abbyyInput = mock(AbbyyInput.class);
        when(abbyyInput.getLocationId()).thenReturn(LocationId.EU);
        when(abbyyInput.getSourceFile()).thenReturn(mock(Path.class));

        HttpClientRequest.Builder builderSpy = new HttpClientRequest.Builder();
        PowerMockito.spy(builderSpy);

        HttpClientResponse responseMock = mock(HttpClientResponse.class);
        when(responseMock.getReturnCode()).thenReturn(ReturnCodes.SUCCESS);
        when(responseMock.getStatusCode()).thenReturn(Short.parseShort(statusCode));
        when(responseMock.getReturnResult()).thenReturn(expectedReturnResult);

        PowerMockito.mockStatic(HttpClient.class);
        PowerMockito.when(HttpClient.class, "execute", any(HttpClientRequest.class)).thenReturn(responseMock);

        //Act
        String returnResult = this.sut.getResultChunk(abbyyInput, resultUrl, exportFormat, startByteIndex, endByteIndex);

        //Assert
        assertEquals(expectedReturnResult, returnResult);
        assertEquals(statusCode, this.sut.getLastStatusCode());
    }
}
