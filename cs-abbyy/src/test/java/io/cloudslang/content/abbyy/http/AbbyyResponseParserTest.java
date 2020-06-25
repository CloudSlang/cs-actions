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
package io.cloudslang.content.abbyy.http;

import io.cloudslang.content.abbyy.entities.responses.AbbyyResponse;
import io.cloudslang.content.abbyy.entities.responses.HttpClientResponse;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.validators.AbbyyResponseValidator;
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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AbbyyResponseParser.class})
public class AbbyyResponseParserTest {

    private AbbyyResponseParser sut;
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Mock
    private AbbyyResponseValidator validatorMock;


    @Before
    public void setUp() throws ParserConfigurationException {
        this.sut = new AbbyyResponseParser(validatorMock);
    }


    @Test
    public void parseResponse_responseHasNullStatusCode_IllegalArgumentException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn(null);
        //Assert
        this.exception.expect(IllegalArgumentException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_responseHasNullReturnResult_IllegalArgumentException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn(null);
        //Assert
        this.exception.expect(IllegalArgumentException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_responseIsInvalidXml_Exception() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<invalid");
        //Assert
        this.exception.expect(Exception.class);
        //Act
        this.sut.parseResponse(response);
    }

    @Test
    public void parseResponse_responseIsFailureButInvalidResponseFormat_AbbyySdkException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 0);
        when(response.getReturnResult()).thenReturn("<error>\n</error>");
        //Assert
        this.exception.expect(AbbyySdkException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_responseIsFailure_AbbyySdkException() throws Exception {
        //Arrange
        final String errMsg = "This is the error";
        final String xml = String.format("<error><message>%s</message></error>", errMsg);
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 0);
        when(response.getReturnResult()).thenReturn(xml);
        //Assert
        this.exception.expect(AbbyySdkException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_responseIsSuccessButInvalidResponseFormat_AbbyySdkException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<response></response>");
        //Assert
        this.exception.expect(AbbyySdkException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_responseIsSuccessButValidationFails_ValidationException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<response><task/></response>");

        AbbyyResponse.Builder abbyyResponseBuilderMock = mockAbbyyResponseBuilder();
        PowerMockito.whenNew(AbbyyResponse.Builder.class).withAnyArguments().thenReturn(abbyyResponseBuilderMock);

        when(this.validatorMock.validate(any(AbbyyResponse.class))).thenReturn(new ValidationException("asd"));

        //Assert
        this.exception.expect(ValidationException.class);

        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_responseIsSuccess_ValidationException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<response><task/></response>");

        AbbyyResponse.Builder abbyyResponseBuilderMock = mockAbbyyResponseBuilder();
        PowerMockito.whenNew(AbbyyResponse.Builder.class).withAnyArguments().thenReturn(abbyyResponseBuilderMock);

        //Act
        this.sut.parseResponse(response);
    }

    private AbbyyResponse.Builder mockAbbyyResponseBuilder() {
        AbbyyResponse.Builder abbyyResponseBuilderMock = PowerMockito.mock(AbbyyResponse.Builder.class);
        when(abbyyResponseBuilderMock.taskId(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.credits(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.taskStatus(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.errorMessage(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.resultUrl(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.resultUrl2(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.resultUrl3(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.estimatedProcessingTime(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.build()).thenReturn(mock(AbbyyResponse.class));
        return abbyyResponseBuilderMock;
    }
}
