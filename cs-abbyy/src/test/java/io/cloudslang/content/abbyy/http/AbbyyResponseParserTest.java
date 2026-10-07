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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
        assertThrows(IllegalArgumentException.class, () -> this.sut.parseResponse(response));
    }


    @Test
    public void parseResponse_responseHasNullReturnResult_IllegalArgumentException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> this.sut.parseResponse(response));
    }


    @Test
    public void parseResponse_responseIsInvalidXml_Exception() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<invalid");
        assertThrows(SAXException.class, () -> this.sut.parseResponse(response));
    }

    @Test
    public void parseResponse_responseIsFailureButInvalidResponseFormat_AbbyySdkException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 0);
        when(response.getReturnResult()).thenReturn("<error>\n</error>");
        assertThrows(AbbyySdkException.class, () -> this.sut.parseResponse(response));
    }


    @Test
    public void parseResponse_responseIsFailure_AbbyySdkException() throws Exception {
        //Arrange
        final String errMsg = "This is the error";
        final String xml = String.format("<error><message>%s</message></error>", errMsg);
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 0);
        when(response.getReturnResult()).thenReturn(xml);
        assertThrows(AbbyySdkException.class, () -> this.sut.parseResponse(response));
    }


    @Test
    public void parseResponse_responseIsSuccessButInvalidResponseFormat_AbbyySdkException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<response></response>");
        assertThrows(AbbyySdkException.class, () -> this.sut.parseResponse(response));
    }


    @Test
    public void parseResponse_responseIsSuccessButValidationFails_ValidationException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<response><task/></response>");

        when(this.validatorMock.validate(any(AbbyyResponse.class))).thenReturn(new ValidationException("asd"));

        try (MockedConstruction<AbbyyResponse.Builder> responseBuilders =
                     mockConstruction(AbbyyResponse.Builder.class, (builder, context) -> stubBuilder(builder))) {
            assertThrows(ValidationException.class, () -> this.sut.parseResponse(response));
        }
    }


    @Test
    public void parseResponse_responseIsSuccess_ValidationException() throws Exception {
        //Arrange
        final HttpClientResponse response = mock(HttpClientResponse.class);
        when(response.getStatusCode()).thenReturn((short) 200);
        when(response.getReturnResult()).thenReturn("<response><task/></response>");

        try (MockedConstruction<AbbyyResponse.Builder> responseBuilders =
                     mockConstruction(AbbyyResponse.Builder.class, (builder, context) -> stubBuilder(builder))) {
            AbbyyResponse parsedResponse = this.sut.parseResponse(response);
            assertSame(responseBuilders.constructed().get(0).build(), parsedResponse);
        }
    }

    private void stubBuilder(AbbyyResponse.Builder abbyyResponseBuilderMock) {
        when(abbyyResponseBuilderMock.taskId(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.credits(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.taskStatus(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.errorMessage(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.resultUrl(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.resultUrl2(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.resultUrl3(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.estimatedProcessingTime(anyString())).thenReturn(abbyyResponseBuilderMock);
        when(abbyyResponseBuilderMock.build()).thenReturn(mock(AbbyyResponse.class));
    }
}
