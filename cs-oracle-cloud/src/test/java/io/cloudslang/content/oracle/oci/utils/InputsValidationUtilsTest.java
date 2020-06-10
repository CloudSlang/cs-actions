/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.oracle.oci.utils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.oracle.oci.utils.InputsValidation.verifyCommonInputs;
import static org.junit.Assert.assertEquals;

public class InputsValidationUtilsTest {

    public static final String CONNECT_TIMEOUT = "2";
    public static final String SOCKET_TIMEOUT = "10";
    public static final String CONNECTIONS_MAX_PER_ROUTE = "5";
    public static final String CONNECTIONS_MAX_TOTAL = "20";
    private static final String EMPTY = "";
    private static final String PROXY_PORT = "8080";
    private static final String TRUST_ALL_ROOTS = "true";
    private static final String KEEP_ALIVE = "false";
    private static final String NUMBER_VALIDATOR_EXCEPTION = "The invalid for socketTimeout input is not a valid number value.";
    private static final String BOOLEAN_VALIDATOR = "The invalid for trustAllRoots input is not a valid boolean value.";
    private static final String INVALID = "invalid";
    private List<String> exceptionMessages = new ArrayList<>();

    @Test
    public void verifyCommonInputsValid() {
        exceptionMessages = verifyCommonInputs(PROXY_PORT, TRUST_ALL_ROOTS,
                CONNECT_TIMEOUT, SOCKET_TIMEOUT, KEEP_ALIVE, CONNECTIONS_MAX_PER_ROUTE, CONNECTIONS_MAX_TOTAL);
        assertEquals(exceptionMessages.size(), 0);
    }

    @Test
    public void verifyCommonInputsInvalidBooleanAndNumber() {
        exceptionMessages = verifyCommonInputs(PROXY_PORT, INVALID,
                CONNECT_TIMEOUT, INVALID, KEEP_ALIVE, CONNECTIONS_MAX_PER_ROUTE, CONNECTIONS_MAX_TOTAL);
        assertEquals(exceptionMessages.size(), 2);
        assertEquals(exceptionMessages.get(0), BOOLEAN_VALIDATOR);
        assertEquals(exceptionMessages.get(1), NUMBER_VALIDATOR_EXCEPTION);
    }


}
