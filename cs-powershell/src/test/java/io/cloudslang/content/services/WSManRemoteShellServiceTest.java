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




package io.cloudslang.content.services;

import io.cloudslang.content.entities.OutputStream;
import io.cloudslang.content.entities.WSManRequestInputs;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.utils.WSManUtils;
import io.cloudslang.content.utils.XMLUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by giloan on 5/9/2016.
 */
@ExtendWith(MockitoExtension.class)
public class WSManRemoteShellServiceTest {

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
    private static final String WINRM_LOCALE_EN_US = "en-US";
    private static final String OPERATION_TIMEOUT = "60";
    private static final String SHELL_UUID = "19034e02-69a7-46e2-9da9-7d95d8096054";
    private static final String SHELL_ID = "shellId";
    private static final String COMMAND_ID = "commandId";
    private static final String COMMAND_UUID = "C0DE9575-6E2D-4C79-9367-676071BDE404";
    private static final String RESPONSE_BODY = "request body";
    private static final String RETURN_RESULT = "returnResult";
    private static final String OK_STATUS_CODE = "200";
    private static final String SHELL_ID_NOT_RETRIEVED = "The shell id could not be retrieved.";
    private static final String CREATE_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2004/09/transfer/CreateResponse";
    private static final String CREATE_RESPONSE_SHELL_ID_XPATH = "/Envelope/Body/ResourceCreated/ReferenceParameters/SelectorSet/Selector[@Name='ShellId']/text()";
    private static final String FAULT_MESSAGE = "fault message";
    private static final String COMMAND_RESPONSE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandResponse";
    private static final String COMMAND_RESULT_COMMAND_ID_XPATH = "/Envelope/Body/CommandResponse/CommandId";
    private static final String COMMAND = "get-host";
    private static final String COMMAND_ID_NOT_RETRIEVED = "The command id could not be retrieved.";
    private static final String UNEXPECTED_SERVICE_RESPONSE = "Unexpected service response: ";
    private static final String RECEIVE_RESPONSE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/ReceiveResponse";
    private static final String EXECUTION_TIMED_OUT = "The script execution timed out!";
    private static final String STATUS_CODE = "statusCode";
    private static final String UNAUTHORIZED_STATUS_CODE = "401";
    private static final String UNAUTHORIZED_EXCEPTION_MESSAGE = "Unauthorized! Service responded with 401 status code!";
    private static final String STDOUT_VALUE = "stdout stream value";
    private static final String STDERR_VALUE = "stderr stream value";
    private static final String STDERR = "stderr";
    private static final String RECEIVE_RESULT = "script execution result containing stdout and stderr streams";
    private static final String SCRIPT_EXIT_CODE_ZERO = "0";
    private static final String SCRIPT_EXIT_CODE = "scriptExitCode";
    private static final String BUILD_RESULT_FROM_RESPONSE_STREAMS_METHOD = "buildResultFromResponseStreams";
    private static final String PROCESS_COMMAND_EXECUTION_RESPONSE_METHOD = "processCommandExecutionResponse";
    private static final String GET_RESOURCE_ID_METHOD = "getResourceId";
    private static final String RECEIVE_COMMAND_RESULT_METHOD = "receiveCommandResult";
    private static final String EXECUTION_IS_TIMED_OUT_METHOD = "executionIsTimedOut";
    private static final String EXECUTE_COMMAND_METHOD = "executeCommand";
    private static final String CREATE_SHELL_METHOD = "createShell";
    private static final String EXECUTE_REQUEST_METHOD = "executeRequestWithBody";
    private static final String DELETE_SHELL_METHOD = "deleteShell";
    private static final String DELETE_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2004/09/transfer/DeleteResponse";

    private WSManRequestInputs wsManRequestInputs;
    @Mock
    private HttpClientService csHttpClientMock;
    @Mock
    private HttpClientInputs httpClientInputsMock;
    @Mock
    private Map<String, String> resultMock;
    private WSManRemoteShellService wsManRemoteShellServiceSpy;
    private MockedStatic<WSManUtils> wsManUtilsStatic;
    private MockedStatic<XMLUtils> xmlUtilsStatic;
    private MockedStatic<HttpClientService> httpClientServiceStatic;
    private MockedConstruction<HttpClientService> httpClientConstruction;
    private MockedConstruction<HttpClientInputs> httpClientInputsConstruction;

    @RegisterExtension
    static final StaticMockExtension staticMockExtension = new StaticMockExtension();

    static class StaticMockExtension implements InvocationInterceptor {
        @Override
        public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext,
                                        ExtensionContext extensionContext) throws Throwable {
            WSManRemoteShellServiceTest test = (WSManRemoteShellServiceTest) extensionContext.getRequiredTestInstance();
            try (MockedStatic<WSManUtils> wsManUtils = mockStatic(WSManUtils.class);
                 MockedStatic<XMLUtils> xmlUtils = mockStatic(XMLUtils.class);
                 MockedStatic<HttpClientService> httpClientService = mockStatic(HttpClientService.class);
                 MockedConstruction<HttpClientService> httpClientConstruction = mockConstruction(HttpClientService.class,
                         (mock, context) -> test.csHttpClientMock = mock);
                 MockedConstruction<HttpClientInputs> httpClientInputsConstruction = mockConstruction(HttpClientInputs.class,
                         (mock, context) -> test.httpClientInputsMock = mock)) {
                test.wsManUtilsStatic = wsManUtils;
                test.xmlUtilsStatic = xmlUtils;
                test.httpClientServiceStatic = httpClientService;
                test.httpClientConstruction = httpClientConstruction;
                test.httpClientInputsConstruction = httpClientInputsConstruction;
                invocation.proceed();
            }
        }
    }

    @BeforeEach
    public void setUp() {
        wsManRequestInputs = new WSManRequestInputs.WSManRequestInputsBuilder()
                .withHost(LOCALHOST)
                .withPort(PORT)
                .withProtocol(HTTPS)
                .withUsername(USER)
                .withPassword(PASS)
                .withProxyHost(PROXY_HOST)
                .withProxyPort(PROXY_PORT)
                .withProxyUsername(PROXY_USER)
                .withProxyPassword(PASS)
                .withMaxEnvelopeSize(MAX_ENVELOPE_SIZE)
                .withTrustAllRoots(Boolean.TRUE.toString())
                .withX509HostnameVerifier(X_509_HOSTNAME_VERIFIER_STRICT)
                .withKeystore(KEYSTORE)
                .withKeystorePassword(PASS)
                .withTrustKeystore(TRUST_KEYSTORE)
                .withTrustPassword(PASS)
                .withScript(SCRIPT)
                .withWinrmLocale(WINRM_LOCALE_EN_US)
                .withOperationTimeout(OPERATION_TIMEOUT)
                .build();
        wsManRemoteShellServiceSpy = spy(new WSManRemoteShellService());
    }

    @Test
    public void testRunCommand() throws Exception {
        doReturn(SHELL_UUID).when(wsManRemoteShellServiceSpy).createShell(any(HttpClientService.class), any(HttpClientInputs.class),
                any(WSManRequestInputs.class));
        doReturn(COMMAND_UUID).when(wsManRemoteShellServiceSpy).executeCommand(any(HttpClientService.class),
                any(HttpClientInputs.class), any(), any(WSManRequestInputs.class), any());
        doReturn(resultMock).when(wsManRemoteShellServiceSpy).receiveCommandResult(any(HttpClientService.class), any(HttpClientInputs.class),
                any(), any(), any(WSManRequestInputs.class));
        doNothing().when(wsManRemoteShellServiceSpy).deleteShell(any(HttpClientService.class), any(HttpClientInputs.class),
                any(), any(WSManRequestInputs.class));

        Map<String, String> result = wsManRemoteShellServiceSpy.runCommand(wsManRequestInputs);

        wsManUtilsStatic.verify(() -> WSManUtils.validateUUID(SHELL_UUID, SHELL_ID));
        wsManUtilsStatic.verify(() -> WSManUtils.validateUUID(COMMAND_UUID, COMMAND_ID));
        assertEquals(1, httpClientConstruction.constructed().size());
        assertEquals(1, httpClientInputsConstruction.constructed().size());
        assertEquals(resultMock, result);
    }

    @Test
    public void testRunCommandThrowsException() throws Exception {
        doThrow(new RuntimeException(SHELL_ID_NOT_RETRIEVED)).when(wsManRemoteShellServiceSpy)
                .createShell(any(HttpClientService.class), any(HttpClientInputs.class), any(WSManRequestInputs.class));
        assertRuntimeException(SHELL_ID_NOT_RETRIEVED, () -> wsManRemoteShellServiceSpy.runCommand(wsManRequestInputs));
    }

    @Test
    public void testExecuteRequest() throws Exception {
        httpClientServiceStatic.when(() -> HttpClientService.execute(any(HttpClientInputs.class))).thenReturn(resultMock);
        Map<String, String> result = new WSManRemoteShellService().executeRequestWithBody(csHttpClientMock, httpClientInputsMock, RESPONSE_BODY);
        httpClientServiceStatic.verify(() -> HttpClientService.execute(any(HttpClientInputs.class)));
        assertEquals(resultMock, result);
    }

    @Test
    public void testExecuteRequestThrowsException() throws Exception {
        doReturn(UNAUTHORIZED_STATUS_CODE).when(resultMock).get(STATUS_CODE);
        httpClientServiceStatic.when(() -> HttpClientService.execute(any(HttpClientInputs.class))).thenReturn(resultMock);
        assertRuntimeException(UNAUTHORIZED_EXCEPTION_MESSAGE,
                () -> new WSManRemoteShellService().executeRequestWithBody(csHttpClientMock, httpClientInputsMock, RESPONSE_BODY));
        verify(resultMock).get(STATUS_CODE);
    }

    @Test
    public void testCreateShell() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION)).thenReturn(true);
        xmlUtilsStatic.when(() -> XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(SHELL_UUID);

        String result = new WSManRemoteShellService().createShell(csHttpClientMock, httpClientInputsMock, wsManRequestInputs);

        assertEquals(SHELL_UUID, result);
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION));
        xmlUtilsStatic.verify(() -> XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH));
    }


    @Test
    public void testCreateShellThrowsShellIdNotRetrievedException() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION)).thenReturn(true);
        xmlUtilsStatic.when(() -> XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(null);

        assertRuntimeException(SHELL_ID_NOT_RETRIEVED,
                () -> new WSManRemoteShellService().createShell(csHttpClientMock, httpClientInputsMock, wsManRequestInputs));

        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION));
        xmlUtilsStatic.verify(() -> XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH));
        httpClientServiceStatic.verify(() -> HttpClientService.execute(any(HttpClientInputs.class)));
    }

    @Test
    public void testCreateShellThrowsFaultException() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION)).thenReturn(false);
        wsManUtilsStatic.when(() -> WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        wsManUtilsStatic.when(() -> WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);

        assertRuntimeException(FAULT_MESSAGE,
                () -> new WSManRemoteShellService().createShell(csHttpClientMock, httpClientInputsMock, wsManRequestInputs));

        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION));
        wsManUtilsStatic.verify(() -> WSManUtils.isFaultResponse(RESPONSE_BODY));
        wsManUtilsStatic.verify(() -> WSManUtils.getResponseFault(RESPONSE_BODY));
    }

    @Test
    public void testCreateShellThrowsUnexpectedResponseException() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION)).thenReturn(false);
        wsManUtilsStatic.when(() -> WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(false);

        assertRuntimeException(UNEXPECTED_SERVICE_RESPONSE,
                () -> new WSManRemoteShellService().createShell(csHttpClientMock, httpClientInputsMock, wsManRequestInputs));

        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION));
        wsManUtilsStatic.verify(() -> WSManUtils.isFaultResponse(RESPONSE_BODY));
    }

    @Test
    public void testExecuteCommand() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION)).thenReturn(true);
        xmlUtilsStatic.when(() -> XMLUtils.parseXml(RESPONSE_BODY, COMMAND_RESULT_COMMAND_ID_XPATH)).thenReturn(COMMAND_UUID);

        String result = new WSManRemoteShellService().executeCommand(csHttpClientMock, httpClientInputsMock,
                SHELL_UUID, wsManRequestInputs, COMMAND);

        assertEquals(COMMAND_UUID, result);
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION));
        xmlUtilsStatic.verify(() -> XMLUtils.parseXml(RESPONSE_BODY, COMMAND_RESULT_COMMAND_ID_XPATH));
    }

    @Test
    public void testExecuteCommandThrowsFaultException() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION)).thenReturn(false);
        wsManUtilsStatic.when(() -> WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        wsManUtilsStatic.when(() -> WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);

        assertRuntimeException(FAULT_MESSAGE,
                () -> new WSManRemoteShellService().executeCommand(csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs, COMMAND));

        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION));
    }

    @Test
    public void testExecuteCommandThrowsCommandIdNotRetrievedException() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION)).thenReturn(true);
        xmlUtilsStatic.when(() -> XMLUtils.parseXml(RESPONSE_BODY, COMMAND_RESULT_COMMAND_ID_XPATH)).thenReturn("");

        assertRuntimeException(COMMAND_ID_NOT_RETRIEVED,
                () -> new WSManRemoteShellService().executeCommand(csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs, COMMAND));

        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION));
        xmlUtilsStatic.verify(() -> XMLUtils.parseXml(RESPONSE_BODY, COMMAND_RESULT_COMMAND_ID_XPATH));
    }

    @Test
    public void testExecuteCommandThrowsUnexpectedResponseException() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION)).thenReturn(false);
        wsManUtilsStatic.when(() -> WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(false);

        assertRuntimeException(UNEXPECTED_SERVICE_RESPONSE,
                () -> new WSManRemoteShellService().executeCommand(csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs, COMMAND));

        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION));
        wsManUtilsStatic.verify(() -> WSManUtils.isFaultResponse(RESPONSE_BODY));
    }

    @Test
    public void testReceiveCommandResult() throws Exception {
        mockExecuteRequest();
        doReturn(false).when(wsManRemoteShellServiceSpy).executionIsTimedOut(anyLong(), anyInt());
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        wsManUtilsStatic.when(() -> WSManUtils.commandExecutionIsDone(RESPONSE_BODY)).thenReturn(true);
        doReturn(resultMock).when(wsManRemoteShellServiceSpy).processCommandExecutionResponse(anyMap());

        Map<String, String> result = wsManRemoteShellServiceSpy.receiveCommandResult(csHttpClientMock, httpClientInputsMock,
                SHELL_UUID, COMMAND_UUID, wsManRequestInputs);

        assertEquals(resultMock, result);
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION));
        wsManUtilsStatic.verify(() -> WSManUtils.commandExecutionIsDone(RESPONSE_BODY));
        verify(wsManRemoteShellServiceSpy).processCommandExecutionResponse(anyMap());
    }

    @Test
    public void testReceiveCommandResultThrowsFaultException() throws Exception {
        mockExecuteRequest();
        doReturn(false).when(wsManRemoteShellServiceSpy).executionIsTimedOut(anyLong(), anyInt());
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        wsManUtilsStatic.when(() -> WSManUtils.commandExecutionIsDone(RESPONSE_BODY)).thenReturn(false);
        wsManUtilsStatic.when(() -> WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        wsManUtilsStatic.when(() -> WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);

        assertRuntimeException(FAULT_MESSAGE,
                () -> wsManRemoteShellServiceSpy.receiveCommandResult(csHttpClientMock, httpClientInputsMock,
                        SHELL_UUID, COMMAND_UUID, wsManRequestInputs));
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION));
        wsManUtilsStatic.verify(() -> WSManUtils.commandExecutionIsDone(RESPONSE_BODY));
        wsManUtilsStatic.verify(() -> WSManUtils.isFaultResponse(RESPONSE_BODY));
        wsManUtilsStatic.verify(() -> WSManUtils.getResponseFault(RESPONSE_BODY));
    }

    @Test
    public void testReceiveCommandResultThrowsTimeoutException() throws Exception {
        mockExecuteRequest();
        doReturn(true).when(wsManRemoteShellServiceSpy).executionIsTimedOut(anyLong(), anyInt());
        java.util.concurrent.TimeoutException exception = assertThrows(java.util.concurrent.TimeoutException.class,
                () -> wsManRemoteShellServiceSpy.receiveCommandResult(csHttpClientMock, httpClientInputsMock,
                        SHELL_UUID, COMMAND_UUID, wsManRequestInputs));
        assertTrue(exception.getMessage().contains(EXECUTION_TIMED_OUT));
    }

    @Test
    public void testGetResourceId() throws Exception {
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        xmlUtilsStatic.when(() -> XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(SHELL_UUID);

        String result = wsManRemoteShellServiceSpy.getResourceId(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED);

        assertEquals(result, SHELL_UUID);
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION));
        xmlUtilsStatic.verify(() -> XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH));
    }

    @Test
    public void testGetResourceIdThrowsShellIdNotRetrieved() throws Exception {
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        xmlUtilsStatic.when(() -> XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn("");

        assertRuntimeException(SHELL_ID_NOT_RETRIEVED, () -> wsManRemoteShellServiceSpy.getResourceId(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED));
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION));
        xmlUtilsStatic.verify(() -> XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH));
    }

    @Test
    public void testGetResourceIdThrowsFaultException() throws Exception {
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(false);
        wsManUtilsStatic.when(() -> WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        wsManUtilsStatic.when(() -> WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);
        assertRuntimeException(FAULT_MESSAGE, () -> wsManRemoteShellServiceSpy.getResourceId(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED));
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION));
        wsManUtilsStatic.verify(() -> WSManUtils.isFaultResponse(RESPONSE_BODY));
        wsManUtilsStatic.verify(() -> WSManUtils.getResponseFault(RESPONSE_BODY));
    }

    @Test
    public void testGetResourceIdThrowsUnexpectedResponseException() throws Exception {
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(false);
        wsManUtilsStatic.when(() -> WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(false);
        assertRuntimeException(UNEXPECTED_SERVICE_RESPONSE, () -> wsManRemoteShellServiceSpy.getResourceId(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED));
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION));
        wsManUtilsStatic.verify(() -> WSManUtils.isFaultResponse(RESPONSE_BODY));
    }

    @Test
    public void testProcessCommandExecutionResponse() throws Exception {
        doReturn(RECEIVE_RESULT).when(resultMock).get(RETURN_RESULT);
        doReturn(STDOUT_VALUE).when(wsManRemoteShellServiceSpy).buildResultFromResponseStreams(RECEIVE_RESULT, OutputStream.STDOUT);
        doReturn(STDERR_VALUE).when(wsManRemoteShellServiceSpy).buildResultFromResponseStreams(RECEIVE_RESULT, OutputStream.STDERR);
        wsManUtilsStatic.when(() -> WSManUtils.getScriptExitCode(RECEIVE_RESULT)).thenReturn(SCRIPT_EXIT_CODE_ZERO);

        Map<String, String> result = wsManRemoteShellServiceSpy.processCommandExecutionResponse(resultMock);

        assertEquals(STDOUT_VALUE, result.get(RETURN_RESULT));
        assertEquals(STDERR_VALUE, result.get(STDERR));
        assertEquals(SCRIPT_EXIT_CODE_ZERO, result.get(SCRIPT_EXIT_CODE));
        verify(resultMock, times(3)).get(RETURN_RESULT);
        wsManUtilsStatic.verify(() -> WSManUtils.getScriptExitCode(RECEIVE_RESULT));
    }

    @Test
    public void testDeleteShell() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION)).thenReturn(true);
        wsManRemoteShellServiceSpy.deleteShell(csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs);
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION));
    }

    @Test
    public void testDeleteShellThrowsFaultException() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION)).thenReturn(false);
        wsManUtilsStatic.when(() -> WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        wsManUtilsStatic.when(() -> WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);
        assertRuntimeException(FAULT_MESSAGE,
                () -> wsManRemoteShellServiceSpy.deleteShell(csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs));
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION));
        wsManUtilsStatic.verify(() -> WSManUtils.isFaultResponse(RESPONSE_BODY));
        wsManUtilsStatic.verify(() -> WSManUtils.getResponseFault(RESPONSE_BODY));
    }

    @Test
    public void testDeleteShellThrowsUnexpectedServiceResponseException() throws Exception {
        mockExecuteRequest();
        wsManUtilsStatic.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION)).thenReturn(false);
        wsManUtilsStatic.when(() -> WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(false);
        assertRuntimeException(UNEXPECTED_SERVICE_RESPONSE,
                () -> wsManRemoteShellServiceSpy.deleteShell(csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs));
        wsManUtilsStatic.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION));
        wsManUtilsStatic.verify(() -> WSManUtils.isFaultResponse(RESPONSE_BODY));
    }

    @Test
    public void testBuildResultFromResponseStreams() throws Exception {
        wsManUtilsStatic.when(() -> WSManUtils.countStreamElements(RECEIVE_RESULT)).thenReturn(2);
        xmlUtilsStatic.when(() -> XMLUtils.parseXml(anyString(), anyString())).thenReturn("c3RyZWFtX3ZhbA==");

        String result = wsManRemoteShellServiceSpy.buildResultFromResponseStreams(RECEIVE_RESULT, OutputStream.STDOUT);

        assertEquals("stream_val" + "stream_val", result);
        wsManUtilsStatic.verify(() -> WSManUtils.countStreamElements(RECEIVE_RESULT));
        xmlUtilsStatic.verify(() -> XMLUtils.parseXml(anyString(), anyString()), times(2));
    }

    private void mockExecuteRequest() throws Exception {
        Map<String, String> result = new HashMap<>();
        result.put(RETURN_RESULT, RESPONSE_BODY);
        result.put(STATUS_CODE, OK_STATUS_CODE);
        httpClientServiceStatic.when(() -> HttpClientService.execute(any(HttpClientInputs.class))).thenReturn(result);
    }

    private void assertRuntimeException(String message, org.junit.jupiter.api.function.Executable action) {
        RuntimeException exception = assertThrows(RuntimeException.class, action);
        assertTrue(exception.getMessage().contains(message));
    }
}
