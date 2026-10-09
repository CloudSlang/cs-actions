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

import io.cloudslang.content.abbyy.entities.responses.AbbyyResponse;
import io.cloudslang.content.abbyy.entities.responses.HttpClientResponse;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ValidationException;
import io.cloudslang.content.abbyy.validators.AbbyyResponseValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.xml.parsers.ParserConfigurationException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@org.junit.jupiter.api.extension.ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
public class AbbyyResponseParserTest {

    private AbbyyResponseParser sut;
    @Mock
    private AbbyyResponseValidator validatorMock;


    @BeforeEach
    public void setUp() throws ParserConfigurationException {
        this.sut = new AbbyyResponseParser(validatorMock);
    }


    @Test
    public void parseResponse_responseHasNullStatusCode_IllegalArgumentException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn(null);
        //Assert
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> this.sut.parseResponse(response));
    }


    @Test
    public void parseResponse_responseHasNullReturnResult_IllegalArgumentException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn(null);
        //Assert
        org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class, () -> this.sut.parseResponse(response));
    }


    @Test
    public void parseResponse_responseIsInvalidXml_Exception() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<invalid");
        //Assert
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> this.sut.parseResponse(response));
    }

    @Test
    public void parseResponse_responseIsFailureButInvalidResponseFormat_AbbyySdkException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 0);
        when(response.getReturnResult()).thenReturn("<error>\n</error>");
        //Assert
        org.junit.jupiter.api.Assertions.assertThrows(AbbyySdkException.class, () -> this.sut.parseResponse(response));
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
        org.junit.jupiter.api.Assertions.assertThrows(AbbyySdkException.class, () -> this.sut.parseResponse(response));
    }


    @Test
    public void parseResponse_responseIsSuccessButInvalidResponseFormat_AbbyySdkException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<response></response>");
        //Assert
        org.junit.jupiter.api.Assertions.assertThrows(AbbyySdkException.class, () -> this.sut.parseResponse(response));
    }


    @Test
    public void parseResponse_responseIsSuccessButValidationFails_ValidationException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<response><task id=\"task-id\" credits=\"1\" status=\"Completed\" estimatedProcessingTime=\"1\"/></response>");

        when(this.validatorMock.validate(any(AbbyyResponse.class))).thenReturn(new ValidationException("asd"));

        //Assert
        org.junit.jupiter.api.Assertions.assertThrows(ValidationException.class, () -> this.sut.parseResponse(response));
    }


    @Test
    public void parseResponse_responseIsSuccess_ValidationException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<response><task id=\"task-id\" credits=\"1\" status=\"Completed\" estimatedProcessingTime=\"1\"/></response>");

        //Act
        this.sut.parseResponse(response);
    }
}
