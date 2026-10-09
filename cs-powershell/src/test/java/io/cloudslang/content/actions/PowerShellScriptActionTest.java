/*
 * Copyright 2019-2024 Open Text
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




package io.cloudslang.content.actions;

import io.cloudslang.content.entities.WSManRequestInputs;
import io.cloudslang.content.services.PowerShellScriptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by giloan on 5/6/2016.
 */
public class PowerShellScriptActionTest {

    private static final String LOCALHOST = "localhost";
    private static final String PORT = "5986";
    private static final String HTTPS = "https";
    private static final String USER = "user";
    private static final String PROXY_HOST = "proxy1";
    private static final String PROXY_PORT = "8081";
    private static final String PROXY_USER = "proxyUser";
    private static final String X_509_HOSTNAME_VERIFIER_STRICT = "strict";
    private static final String TRUST_KEYSTORE = "trustKeystorePath";
    private static final String PASS = "pass";
    private static final String KEYSTORE = "keystorePath";
    private static final String MAX_ENVELOPE_SIZE = "153600";
    private static final String SCRIPT = "Get-Host";
    private static final String MODULES = "Storage";
    private static final String WINRM_LOCALE_EN_US = "en-US";
    private static final String OPERATION_TIMEOUT = "60";
    private static final String RETURN_CODE = "returnCode";
    private static final String RETURN_CODE_SUCCESS = "0";
    private static final String SCRIPT_EXIT_CODE = "scriptExitCode";
    private static final String EMPTY_STRING = "";
    private static final String EXCEPTION_MESSAGE = "exceptionMessage";
    private static final String EXCEPTION = "exception";
    private static final String RETURN_CODE_FAILURE = "-1";
    private static final String BASIC_AUTH_TYPE = "Basic";
    private static final String KERBEROS_CONF_FILE = "/kerberosConfFile";
    private static final String KERBEROS_LOGIN_CONF_FILE = "/kerberosLoginConfFile";
    private static final String KERBEROS_SKIP_PORT_FOR_LOOKUP = "true";

    private PowerShellScriptAction powerShellScriptAction;

    private final Map<String, String> resultMock = mock(Map.class);

    @BeforeEach
    public void setUp() {
        powerShellScriptAction = new PowerShellScriptAction();
    }

    @Test
    public void testExecute() throws Exception {
        try (MockedConstruction<PowerShellScriptService> construction = mockConstruction(PowerShellScriptService.class,
                (service, context) -> configureSuccess(service))) {

            Map<String, String> result = powerShellScriptAction.execute(LOCALHOST, PORT, HTTPS, USER, PASS, BASIC_AUTH_TYPE, PROXY_HOST, PROXY_PORT,
                    PROXY_USER, PASS, Boolean.TRUE.toString(), X_509_HOSTNAME_VERIFIER_STRICT, TRUST_KEYSTORE, PASS, KERBEROS_CONF_FILE, KERBEROS_LOGIN_CONF_FILE, KERBEROS_SKIP_PORT_FOR_LOOKUP, KEYSTORE, PASS,
                    MAX_ENVELOPE_SIZE, SCRIPT, EMPTY_STRING, MODULES, WINRM_LOCALE_EN_US, OPERATION_TIMEOUT);

            verify(construction.constructed().get(0)).execute(any(WSManRequestInputs.class));
            verify(resultMock).put(RETURN_CODE, RETURN_CODE_SUCCESS);
            verify(resultMock).get(SCRIPT_EXIT_CODE);
            assertEquals(resultMock, result);
        }
    }

    @Test
    public void testExecuteWithInputDefaultValues() throws Exception {
        try (MockedConstruction<PowerShellScriptService> construction = mockConstruction(PowerShellScriptService.class,
                (service, context) -> configureSuccess(service))) {

            Map<String, String> result = powerShellScriptAction.execute(LOCALHOST, EMPTY_STRING, EMPTY_STRING, USER, PASS, BASIC_AUTH_TYPE, PROXY_HOST, PROXY_PORT,
                    PROXY_USER, PASS, EMPTY_STRING, EMPTY_STRING, TRUST_KEYSTORE, PASS, KERBEROS_CONF_FILE, KERBEROS_LOGIN_CONF_FILE, KERBEROS_SKIP_PORT_FOR_LOOKUP, KEYSTORE, PASS,
                    EMPTY_STRING, SCRIPT, EMPTY_STRING, MODULES, EMPTY_STRING, EMPTY_STRING);

            verify(construction.constructed().get(0)).execute(any(WSManRequestInputs.class));
            verify(resultMock).put(RETURN_CODE, RETURN_CODE_SUCCESS);
            verify(resultMock).get(SCRIPT_EXIT_CODE);
            assertEquals(resultMock, result);
        }
    }

    @Test
    public void testExecuteThrowsException() throws Exception {
        try (MockedConstruction<PowerShellScriptService> construction = mockConstruction(PowerShellScriptService.class,
                (service, context) -> doThrow(new RuntimeException(EXCEPTION_MESSAGE)).when(service).execute(any(WSManRequestInputs.class)))) {

            Map<String, String> result = powerShellScriptAction.execute(LOCALHOST, EMPTY_STRING, EMPTY_STRING, USER, BASIC_AUTH_TYPE, PASS, PROXY_HOST, PROXY_PORT,
                    PROXY_USER, PASS, EMPTY_STRING, EMPTY_STRING, TRUST_KEYSTORE, PASS, KERBEROS_CONF_FILE, KERBEROS_LOGIN_CONF_FILE, KERBEROS_SKIP_PORT_FOR_LOOKUP, KEYSTORE, PASS,
                    EMPTY_STRING, SCRIPT, EMPTY_STRING, MODULES, EMPTY_STRING, EMPTY_STRING);

            assertTrue(result.get(EXCEPTION).contains(EXCEPTION_MESSAGE));
            assertEquals(RETURN_CODE_FAILURE, result.get(RETURN_CODE));
            assertEquals(1, construction.constructed().size());
        }
    }

    @Test
    public void testExecuteWithFailureScriptExitCode() throws Exception {
        when(resultMock.get(SCRIPT_EXIT_CODE)).thenReturn(RETURN_CODE_FAILURE);
        try (MockedConstruction<PowerShellScriptService> construction = mockConstruction(PowerShellScriptService.class,
                (service, context) -> when(service.execute(any(WSManRequestInputs.class))).thenReturn(resultMock))) {

            Map<String, String> result = powerShellScriptAction.execute(LOCALHOST, EMPTY_STRING, EMPTY_STRING, USER, PASS, BASIC_AUTH_TYPE, PROXY_HOST, PROXY_PORT,
                    PROXY_USER, PASS, EMPTY_STRING, EMPTY_STRING, TRUST_KEYSTORE, PASS, KERBEROS_CONF_FILE, KERBEROS_LOGIN_CONF_FILE, KERBEROS_SKIP_PORT_FOR_LOOKUP, KEYSTORE, PASS,
                    EMPTY_STRING, SCRIPT, EMPTY_STRING, MODULES, EMPTY_STRING, EMPTY_STRING);

            verify(construction.constructed().get(0)).execute(any(WSManRequestInputs.class));
            verify(resultMock).put(RETURN_CODE, RETURN_CODE_FAILURE);
            verify(resultMock).get(SCRIPT_EXIT_CODE);
            assertEquals(resultMock, result);
        }
    }

    private void configureSuccess(PowerShellScriptService service) throws Exception {
        when(service.execute(any(WSManRequestInputs.class))).thenReturn(resultMock);
        when(resultMock.put(RETURN_CODE, RETURN_CODE_SUCCESS)).thenReturn(null);
        when(resultMock.get(SCRIPT_EXIT_CODE)).thenReturn(RETURN_CODE_SUCCESS);
    }


}
