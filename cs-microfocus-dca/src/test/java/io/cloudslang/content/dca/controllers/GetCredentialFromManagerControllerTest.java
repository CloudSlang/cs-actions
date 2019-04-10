/*
 * (c) Copyright 2019 Micro Focus, L.P.
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


package io.cloudslang.content.dca.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static io.cloudslang.content.dca.controllers.GetCredentialFromManagerController.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.Assert.assertEquals;

public class GetCredentialFromManagerControllerTest {
    private static final String INVALID_KEY = "invalid_key";
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String JSON_ARRAY =
            "[{\"key\":\"username\", \"value\":\"username\"},{\"key\":\"password\", \"value\":\"password\"}]";
    private static final String JSON_ARRAY_WITHOUT_PASSWORD =
            "[{\"key\":\"username\", \"value\":\"username\"}]";
    private static final String JSON_ARRAY_WITHOUT_USERNAME =
            "[{\"key\":\"password\", \"value\":\"password\"}]";
    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testGetValueFromDataArray() throws Exception {
        assertEquals(USERNAME, getValueFromDataArray(mapper.readTree(JSON_ARRAY), USERNAME));
        assertEquals(PASSWORD, getValueFromDataArray(mapper.readTree(JSON_ARRAY), PASSWORD));

        assertEquals(EMPTY, getValueFromDataArray(mapper.readTree(JSON_ARRAY), INVALID_KEY));
    }

    @Test
    public void testGetUsernameFromDataArray() throws Exception {
        assertEquals(USERNAME, getUsernameFromDataArray(mapper.readTree(JSON_ARRAY)));

        assertEquals(EMPTY, getUsernameFromDataArray(mapper.readTree(JSON_ARRAY_WITHOUT_USERNAME)));
    }

    @Test
    public void testGetPasswordFromDataArray() throws Exception {
        assertEquals(PASSWORD, getPasswordFromDataArray(mapper.readTree(JSON_ARRAY)));

        assertEquals(EMPTY, getPasswordFromDataArray(mapper.readTree(JSON_ARRAY_WITHOUT_PASSWORD)));
    }
}
