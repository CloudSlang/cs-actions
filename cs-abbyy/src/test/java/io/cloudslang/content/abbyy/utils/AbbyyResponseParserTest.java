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
package io.cloudslang.content.abbyy.utils;

import io.cloudslang.content.abbyy.constants.MiscConstants;
import io.cloudslang.content.abbyy.exceptions.AbbyySdkException;
import io.cloudslang.content.abbyy.exceptions.ServerSideException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

@RunWith(PowerMockRunner.class)
public abstract class AbbyyResponseParserTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();
    protected AbbyyResponseParser sut;


    @Before
    public void setUp() throws Exception {
        this.sut = this.newSutInstance();
    }


    protected abstract AbbyyResponseParser newSutInstance() throws Exception;


    @Test
    public void parseResponse_responseIsNull_IllegalArgumentException() throws Exception {
        //Arrange
        final Map<String, String> response = null;
        //Assert
        exception.expect(IllegalArgumentException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_responseContainsException_ExceptionWithCorrespondingMessage() throws Exception {
        //Arrange
        final String httpErrMsg = "http client exception";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_EXCEPTION_OUTPUT, httpErrMsg);

        try {
            //Act
            this.sut.parseResponse(response);
            fail();
        } catch (Exception ex) {
            //Assert
            assertTrue(ex.getMessage().contains(httpErrMsg));
        }
    }


    @Test
    public void parseResponse_invalidFailureResponseReceived_Exception() throws Exception {
        //Arrange
        final String xml = "<invalid></invalid>";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "123");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);
        //Assert
        exception.expect(Exception.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_validFailureResponseReceived_ServerSideExceptionWithCorrespondingMessage() throws Exception {
        //Arrange
        final String abbyServerSideErrMsg = "ABBY Server Side Error Message";
        final String xml = "<error><message>" + abbyServerSideErrMsg + "</message></error>";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "123");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);

        try {
            //Act
            this.sut.parseResponse(response);
            fail();
        } catch (ServerSideException ex) {
            //Assert
            assertTrue(ex.getMessage().contains(abbyServerSideErrMsg));
        }
    }


    @Test
    public void parseResponse_invalidSuccessResponseReceived_Exception() throws Exception {
        //Arrange
        final String xml = "<response></response>";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "200");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);
        //Assert
        exception.expect(Exception.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_idAttrIsMissing_AbbyySdkException() throws Exception {
        //Arrange
        final String xml = "<response><task credits='2' status='Queued' /></response>";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "200");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);
        //Assert
        exception.expect(AbbyySdkException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_creditsAttrIsMissing_AbbyySdkException() throws Exception {
        //Arrange
        final String xml = "<response><task id='2' status='Queued' /></response>";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "200");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);
        //Assert
        exception.expect(AbbyySdkException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_creditsAttrIsInvalid_AbbyySdkException() throws Exception {
        //Arrange
        final String xml = "<response><task id='2' credits='a' status='Queued' /></response>";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "200");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);
        //Assert
        exception.expect(AbbyySdkException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_creditsAttrIsNegative_AbbyySdkException() throws Exception {
        //Arrange
        final String xml = "<response><task id='2' credits='-1' status='Queued' /></response>";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "200");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);
        //Assert
        exception.expect(AbbyySdkException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_statusAttrIsEmpty_AbbyySdkException() throws Exception {
        //Arrange
        final String xml = "<response><task id='2' credits='2' status='' /></response>";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "200");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);
        //Assert
        exception.expect(AbbyySdkException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_estimatedProcessingTimeAttrIsInvalid_AbbyySdkException() throws Exception {
        //Arrange
        final String xml = "<response><task id='2' credits='2' status='Queued' estimatedProcessingTime='a' /></response>";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "200");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);
        //Assert
        exception.expect(AbbyySdkException.class);
        //Act
        this.sut.parseResponse(response);
    }


    @Test
    public void parseResponse_estimatedProcessingTimeAttrIsNegative_AbbyySdkException() throws Exception {
        //Arrange
        final String xml = "<response><task id='2' credits='2' status='Queued' estimatedProcessingTime='-2' /></response>";
        final Map<String, String> response = new HashMap<>();
        response.put(MiscConstants.HTTP_STATUS_CODE_OUTPUT, "200");
        response.put(MiscConstants.HTTP_RETURN_RESULT_OUTPUT, xml);
        //Assert
        exception.expect(AbbyySdkException.class);
        //Act
        this.sut.parseResponse(response);
    }
}