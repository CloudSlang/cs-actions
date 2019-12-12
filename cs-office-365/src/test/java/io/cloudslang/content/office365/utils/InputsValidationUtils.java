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

package io.cloudslang.content.office365.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.office365.utils.InputsValidation.verifyCommonInputs;
import static io.cloudslang.content.office365.utils.InputsValidation.verifyGetMessageInputs;
import static org.junit.Assert.assertEquals;

public class InputsValidationUtils {

    public static final String CONNECT_TIMEOUT = "2";
    public static final String SOCKET_TIMEOUT = "10";
    public static final String CONNECTIONS_MAX_PER_ROUTE = "5";
    public static final String CONNECTIONS_MAX_TOTAL = "20";
    private static final String USER_PRINCIPAL_NAME = "userPrincipalName";
    private static final String USER_ID = "userId";
    private static final String EMPTY = "";
    private static final String PROXY_PORT = "8080";
    private static final String TRUST_ALL_ROOTS = "true";
    private static final String KEEP_ALIVE = "false";
    private static final String MESSAGE_ID = "messageId";
    private static final String MESSAGE_ID_EXCEPTION = "The messageId can't be null or empty.";
    private static final String NUMBER_VALIDATOR_EXCEPTION = "The invalid for socketTimeout input is not a valid number value.";
    private static final String BOOLEAN_VALIDATOR = "The invalid for trustAllRoots input is not a valid boolean value.";
    private static final String INVALID = "invalid";
    private static final String LOGIN_VALIDATOR = "The userPrincipalName or userId is required for login.";
    private List<String> exceptionMessages = new ArrayList<>();

    @Test
    public void verifyCommonInputsValid() {
        exceptionMessages = verifyCommonInputs(USER_PRINCIPAL_NAME, USER_ID, PROXY_PORT, TRUST_ALL_ROOTS,
                CONNECT_TIMEOUT, SOCKET_TIMEOUT, KEEP_ALIVE, CONNECTIONS_MAX_PER_ROUTE, CONNECTIONS_MAX_TOTAL);
        assertEquals(exceptionMessages.size(), 0);
    }

    @Test
    public void verifyCommonInputsEmptyUserAuthInputs() {
        exceptionMessages = verifyCommonInputs(EMPTY, EMPTY, PROXY_PORT, TRUST_ALL_ROOTS,
                CONNECT_TIMEOUT, SOCKET_TIMEOUT, KEEP_ALIVE, CONNECTIONS_MAX_PER_ROUTE, CONNECTIONS_MAX_TOTAL);
        assertEquals(exceptionMessages.size(), 1);
        assertEquals(exceptionMessages.get(0), LOGIN_VALIDATOR);
    }

    @Test
    public void verifyCommonInputsInvalidBooleanAndNumber() {
        exceptionMessages = verifyCommonInputs(USER_PRINCIPAL_NAME, USER_ID, PROXY_PORT, INVALID,
                CONNECT_TIMEOUT, INVALID, KEEP_ALIVE, CONNECTIONS_MAX_PER_ROUTE, CONNECTIONS_MAX_TOTAL);
        assertEquals(exceptionMessages.size(), 2);
        assertEquals(exceptionMessages.get(0), BOOLEAN_VALIDATOR);
        assertEquals(exceptionMessages.get(1), NUMBER_VALIDATOR_EXCEPTION);
    }

    @Test
    public void verifyCommonInputsEmptyAuthInputUserId() {
        exceptionMessages = verifyCommonInputs(USER_PRINCIPAL_NAME, EMPTY, PROXY_PORT, TRUST_ALL_ROOTS,
                CONNECT_TIMEOUT, SOCKET_TIMEOUT, KEEP_ALIVE, CONNECTIONS_MAX_PER_ROUTE, CONNECTIONS_MAX_TOTAL);
        assertEquals(exceptionMessages.size(), 0);
    }

    @Test
    public void verifyCommonInputsEmptyAuthInputUserPrincipalName() {
        exceptionMessages = verifyCommonInputs(EMPTY, USER_ID, PROXY_PORT, TRUST_ALL_ROOTS,
                CONNECT_TIMEOUT, SOCKET_TIMEOUT, KEEP_ALIVE, CONNECTIONS_MAX_PER_ROUTE, CONNECTIONS_MAX_TOTAL);
        assertEquals(exceptionMessages.size(), 0);
    }

    @Test
    public void verifyGetMessageInputsInvalid() {
        exceptionMessages = verifyGetMessageInputs(EMPTY, EMPTY, USER_ID, PROXY_PORT, TRUST_ALL_ROOTS,
                CONNECT_TIMEOUT, SOCKET_TIMEOUT, KEEP_ALIVE, CONNECTIONS_MAX_PER_ROUTE, CONNECTIONS_MAX_TOTAL);
        assertEquals(exceptionMessages.size(), 1);
        assertEquals(exceptionMessages.get(0), MESSAGE_ID_EXCEPTION);
    }


    @Test
    public void verifyGetMessageInputsValid() {
        exceptionMessages = verifyGetMessageInputs(MESSAGE_ID, EMPTY, USER_ID, PROXY_PORT, TRUST_ALL_ROOTS,
                CONNECT_TIMEOUT, SOCKET_TIMEOUT, KEEP_ALIVE, CONNECTIONS_MAX_PER_ROUTE, CONNECTIONS_MAX_TOTAL);
        assertEquals(exceptionMessages.size(), 0);
    }

}
