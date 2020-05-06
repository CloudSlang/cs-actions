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

import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class HttpClientResponseTest {

    @RunWith(PowerMockRunner.class)
    public static class BuilderTest {

        @Rule
        public ExpectedException exception = ExpectedException.none();


        @Test
        public void build_noFieldSet_pojoSuccessfullyBuilt() throws IOException {
            //Act
            HttpClientResponse httpResponse = new HttpClientResponse.Builder().build();
            //Assert
            assertEquals(StringUtils.EMPTY, httpResponse.getReturnResult());
            assertEquals(StringUtils.EMPTY, httpResponse.getReturnCode());
            assertEquals(StringUtils.EMPTY, httpResponse.getException());
            assertNull(httpResponse.getStatusCode());
            assertEquals(new Properties(), httpResponse.getResponseHeaders());
        }


        @Test
        public void build_statusCodeIsNaN_Exception() throws IOException {
            //Arrange
            final String returnResult = "return result";
            final String returnCode = "return code";
            final String exception = "exception";
            final String statusCode = "not a number";

            final String name1 = "name1";
            final String name2 = "name2";
            final String value1 = "value1";
            final String value2 = "value2";
            final String responseHeaders = String.format("%s:%s\n%s:%s", name1, value1, name2, value2);

            //Assert
            this.exception.expect(NumberFormatException.class);

            //Act
            new HttpClientResponse.Builder()
                    .returnResult(returnResult)
                    .returnCode(returnCode)
                    .exception(exception)
                    .statusCode(statusCode)
                    .responseHeaders(responseHeaders)
                    .build();
        }


        @Test
        public void build_allFieldsSet_pojoSuccessfullyBuilt() throws IOException {
            //Arrange
            final String returnResult = "return result";
            final String returnCode = "return code";
            final String exception = "exception";
            final String statusCode = "202";

            final String name1 = "name1";
            final String name2 = "name2";
            final String value1 = "value1";
            final String value2 = "value2";
            final String responseHeaders = String.format("%s:%s\n%s:%s", name1, value1, name2, value2);

            //Act
            HttpClientResponse httpResponse = new HttpClientResponse.Builder()
                    .returnResult(returnResult)
                    .returnCode(returnCode)
                    .exception(exception)
                    .statusCode(statusCode)
                    .responseHeaders(responseHeaders)
                    .build();

            //Assert
            assertEquals(returnResult, httpResponse.getReturnResult());
            assertEquals(returnCode, httpResponse.getReturnCode());
            assertEquals(exception, httpResponse.getException());
            assertEquals(Short.parseShort(statusCode), (Object) httpResponse.getStatusCode());
            assertEquals(value1, httpResponse.getResponseHeaders().getProperty(name1));
            assertEquals(value2, httpResponse.getResponseHeaders().getProperty(name2));
        }
    }
}
