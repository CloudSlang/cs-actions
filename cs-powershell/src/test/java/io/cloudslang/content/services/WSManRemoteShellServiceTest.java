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
import io.cloudslang.content.powershell.entities.PowerShellHttpClientInputs;
import io.cloudslang.content.utils.WSManUtils;
import io.cloudslang.content.utils.XMLUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WSManRemoteShellServiceTest {
    private static final String RESPONSE = "response body";
    private static final String SHELL_ID = "19034e02-69a7-46e2-9da9-7d95d8096054";
    private static final String COMMAND_ID = "C0DE9575-6E2D-4C79-9367-676071BDE404";
    private static final String FAULT = "fault message";
    private static final String SHELL_ID_NOT_RETRIEVED = "The shell id could not be retrieved.";
    private static final String UNEXPECTED_RESPONSE = "Unexpected service response: ";
    private static final String RECEIVE_ACTION =
            "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/ReceiveResponse";
    private static final String RETURN_RESULT = "returnResult";
    private static final String STATUS_CODE = "statusCode";

    private final WSManRemoteShellService service = new WSManRemoteShellService();

    @Test
    void executeRequestSetsBodyAndReturnsResponse() throws Exception {
        PowerShellHttpClientInputs inputs = spy(new PowerShellHttpClientInputs());
        Map<String, String> response = Map.of(STATUS_CODE, "200");
        try (MockedStatic<HttpClientService> client = mockStatic(HttpClientService.class)) {
            client.when(() -> HttpClientService.execute(any(HttpClientInputs.class))).thenReturn(response);
            Map<String, String> result = invoke("executeRequestWithBody",
                    new Class<?>[]{PowerShellHttpClientInputs.class, String.class}, inputs, RESPONSE);

            assertEquals(response, result);
            verify(inputs).setBody(RESPONSE);
            client.verify(() -> HttpClientService.execute(any(HttpClientInputs.class)));
        }
    }

    @Test
    void executeRequestRejectsUnauthorizedResponse() throws Exception {
        PowerShellHttpClientInputs inputs = spy(new PowerShellHttpClientInputs());
        try (MockedStatic<HttpClientService> client = mockStatic(HttpClientService.class)) {
            client.when(() -> HttpClientService.execute(any(HttpClientInputs.class))).thenReturn(Map.of(STATUS_CODE, "401"));

            Exception exception = assertThrows(Exception.class, () -> invoke("executeRequestWithBody",
                    new Class<?>[]{PowerShellHttpClientInputs.class, String.class}, inputs, RESPONSE));

            assertTrue(rootCause(exception).getMessage().contains("Unauthorized! Service responded with 401 status code!"));
        }
    }

    @Test
    void getResourceIdReturnsParsedIdForExpectedResponse() throws Exception {
        try (MockedStatic<WSManUtils> wsman = mockStatic(WSManUtils.class);
             MockedStatic<XMLUtils> xml = mockStatic(XMLUtils.class)) {
            wsman.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE, "create")).thenReturn(true);
            xml.when(() -> XMLUtils.parseXml(RESPONSE, "/id")).thenReturn(SHELL_ID);

            String result = invoke("getResourceId",
                    new Class<?>[]{String.class, String.class, String.class, String.class},
                    RESPONSE, "create", "/id", SHELL_ID_NOT_RETRIEVED);

            assertEquals(SHELL_ID, result);
            wsman.verify(() -> WSManUtils.isSpecificResponseAction(RESPONSE, "create"));
            xml.verify(() -> XMLUtils.parseXml(RESPONSE, "/id"));
        }
    }

    @Test
    void getResourceIdRejectsMissingIdFaultAndUnexpectedResponse() throws Exception {
        try (MockedStatic<WSManUtils> wsman = mockStatic(WSManUtils.class);
             MockedStatic<XMLUtils> xml = mockStatic(XMLUtils.class)) {
            wsman.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE, "create")).thenReturn(true);
            xml.when(() -> XMLUtils.parseXml(RESPONSE, "/id")).thenReturn(" ");
            Exception missingId = assertThrows(Exception.class, () -> invoke("getResourceId",
                    new Class<?>[]{String.class, String.class, String.class, String.class},
                    RESPONSE, "create", "/id", SHELL_ID_NOT_RETRIEVED));
            assertTrue(rootCause(missingId).getMessage().contains(SHELL_ID_NOT_RETRIEVED));

            wsman.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE, "create")).thenReturn(false);
            wsman.when(() -> WSManUtils.isFaultResponse(RESPONSE)).thenReturn(true);
            wsman.when(() -> WSManUtils.getResponseFault(RESPONSE)).thenReturn(FAULT);
            Exception fault = assertThrows(Exception.class, () -> invoke("getResourceId",
                    new Class<?>[]{String.class, String.class, String.class, String.class},
                    RESPONSE, "create", "/id", SHELL_ID_NOT_RETRIEVED));
            assertTrue(rootCause(fault).getMessage().contains(FAULT));

            wsman.when(() -> WSManUtils.isFaultResponse(RESPONSE)).thenReturn(false);
            Exception unexpected = assertThrows(Exception.class, () -> invoke("getResourceId",
                    new Class<?>[]{String.class, String.class, String.class, String.class},
                    RESPONSE, "create", "/id", SHELL_ID_NOT_RETRIEVED));
            assertTrue(rootCause(unexpected).getMessage().contains(UNEXPECTED_RESPONSE + RESPONSE));
        }
    }

    @Test
    void receiveCommandResultProcessesCompletedResponse() throws Exception {
        PowerShellHttpClientInputs inputs = spy(new PowerShellHttpClientInputs());
        WSManRequestInputs request = requestInputs("60");
        inputs.setUrl("https://host:5986/wsman");
        setField(service, "commandExecutionStartTime", System.currentTimeMillis() / 1000);
        try (MockedStatic<HttpClientService> client = mockStatic(HttpClientService.class);
             MockedStatic<WSManUtils> wsman = mockStatic(WSManUtils.class);
             MockedStatic<XMLUtils> xml = mockStatic(XMLUtils.class)) {
            client.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                    .thenReturn(Map.of(RETURN_RESULT, RESPONSE, STATUS_CODE, "200"));
            wsman.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE, RECEIVE_ACTION)).thenReturn(true);
            wsman.when(() -> WSManUtils.commandExecutionIsDone(RESPONSE)).thenReturn(true);
            wsman.when(() -> WSManUtils.countStreamElements(RESPONSE)).thenReturn(1);
            wsman.when(() -> WSManUtils.getScriptExitCode(RESPONSE)).thenReturn("0");
            xml.when(() -> XMLUtils.parseXml(eq(RESPONSE), anyString())).thenAnswer(invocation ->
                    invocation.<String>getArgument(1).contains("stdout") ? "aGVsbG8=" : "ZXJyb3I=");

            Map<String, String> result = invoke("receiveCommandResult",
                    new Class<?>[]{PowerShellHttpClientInputs.class, String.class, String.class, WSManRequestInputs.class},
                    inputs, SHELL_ID, COMMAND_ID, request);

            assertEquals("hello", result.get(RETURN_RESULT));
            assertEquals("error", result.get("stderr"));
            assertEquals("0", result.get("scriptExitCode"));
        }
    }

    @Test
    void receiveCommandResultTimesOut() throws Exception {
        PowerShellHttpClientInputs inputs = spy(new PowerShellHttpClientInputs());
        inputs.setUrl("https://host:5986/wsman");
        setField(service, "commandExecutionStartTime", 0L);

        try (MockedStatic<HttpClientService> client = mockStatic(HttpClientService.class)) {
            client.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                    .thenReturn(Map.of(RETURN_RESULT, RESPONSE, STATUS_CODE, "200"));
            Exception exception = assertThrows(Exception.class, () -> invoke("receiveCommandResult",
                    new Class<?>[]{PowerShellHttpClientInputs.class, String.class, String.class, WSManRequestInputs.class},
                    inputs, SHELL_ID, COMMAND_ID, requestInputs("1")));

            assertTrue(rootCause(exception) instanceof TimeoutException);
            assertTrue(rootCause(exception).getMessage().contains("The script execution timed out!"));
        }
    }

    @Test
    void deleteShellAcceptsExpectedActionAndRejectsFault() throws Exception {
        PowerShellHttpClientInputs inputs = spy(new PowerShellHttpClientInputs());
        inputs.setUrl("https://host:5986/wsman");
        try (MockedStatic<HttpClientService> client = mockStatic(HttpClientService.class);
             MockedStatic<WSManUtils> wsman = mockStatic(WSManUtils.class)) {
            client.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                    .thenReturn(Map.of(RETURN_RESULT, RESPONSE, STATUS_CODE, "200"));
            wsman.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE,
                    "http://schemas.xmlsoap.org/ws/2004/09/transfer/DeleteResponse")).thenReturn(true);
            invoke("deleteShell", new Class<?>[]{PowerShellHttpClientInputs.class,
                    String.class, WSManRequestInputs.class}, inputs, SHELL_ID, requestInputs("60"));
            client.verify(() -> HttpClientService.execute(any(HttpClientInputs.class)));

            wsman.when(() -> WSManUtils.isSpecificResponseAction(RESPONSE,
                    "http://schemas.xmlsoap.org/ws/2004/09/transfer/DeleteResponse")).thenReturn(false);
            wsman.when(() -> WSManUtils.isFaultResponse(RESPONSE)).thenReturn(true);
            wsman.when(() -> WSManUtils.getResponseFault(RESPONSE)).thenReturn(FAULT);
            Exception exception = assertThrows(Exception.class, () -> invoke("deleteShell",
                    new Class<?>[]{PowerShellHttpClientInputs.class, String.class, WSManRequestInputs.class},
                    inputs, SHELL_ID, requestInputs("60")));
            assertTrue(rootCause(exception).getMessage().contains(FAULT));
        }
    }

    @Test
    void processCommandResponseSeparatesStreamsAndExitCode() throws Exception {
        Map<String, String> response = new HashMap<>();
        response.put(RETURN_RESULT, RESPONSE);
        try (MockedStatic<WSManUtils> wsman = mockStatic(WSManUtils.class);
             MockedStatic<XMLUtils> xml = mockStatic(XMLUtils.class)) {
            wsman.when(() -> WSManUtils.countStreamElements(RESPONSE)).thenReturn(1);
            wsman.when(() -> WSManUtils.getScriptExitCode(RESPONSE)).thenReturn("7");
            xml.when(() -> XMLUtils.parseXml(eq(RESPONSE), anyString())).thenAnswer(invocation ->
                    invocation.<String>getArgument(1).contains(OutputStream.STDOUT.getValue()) ? "b3V0" : "ZXJy");

            Map<String, String> result = invoke("processCommandExecutionResponse",
                    new Class<?>[]{Map.class}, response);

            assertEquals("out", result.get(RETURN_RESULT));
            assertEquals("err", result.get("stderr"));
            assertEquals("7", result.get("scriptExitCode"));
        }
    }

    @Test
    void executionTimeoutIsDisabledWhenTimeoutIsZero() throws Exception {
        boolean timedOut = invoke("executionIsTimedOut", new Class<?>[]{long.class, int.class}, 0L, 0);
        assertFalse(timedOut);
    }

    @Test
    void runCommandPropagatesHttpClientFailure() throws Exception {
        WSManRequestInputs request = requestInputs("60");
        try (MockedStatic<HttpClientService> client = mockStatic(HttpClientService.class)) {
            client.when(() -> HttpClientService.execute(any(HttpClientInputs.class)))
                    .thenThrow(new IllegalStateException("transport failed"));
            IllegalStateException exception = assertThrows(IllegalStateException.class, () -> service.runCommand(request));
            assertEquals("transport failed", exception.getMessage());
            client.verify(() -> HttpClientService.execute(any(HttpClientInputs.class)));
        }
    }

    @Test
    void runCommandCreatesExecutesReceivesAndDeletesShell() throws Exception {
        WSManRequestInputs request = requestInputs("60");
        Map<String, String> createResponse = Map.of(RETURN_RESULT, "create-response", STATUS_CODE, "200");
        Map<String, String> commandResponse = Map.of(RETURN_RESULT, "command-response", STATUS_CODE, "200");
        Map<String, String> receiveResponse = Map.of(RETURN_RESULT, "receive-response", STATUS_CODE, "200");
        Map<String, String> deleteResponse = Map.of(RETURN_RESULT, "delete-response", STATUS_CODE, "200");

        try (MockedStatic<HttpClientService> client = mockStatic(HttpClientService.class);
             MockedStatic<WSManUtils> wsman = mockStatic(WSManUtils.class);
             MockedStatic<XMLUtils> xml = mockStatic(XMLUtils.class)) {
            client.when(() -> HttpClientService.execute(any(HttpClientInputs.class))).thenReturn(
                    createResponse, commandResponse, receiveResponse, deleteResponse);
            wsman.when(() -> WSManUtils.isSpecificResponseAction(anyString(), anyString()))
                    .thenAnswer(invocation -> {
                        String response = invocation.getArgument(0);
                        String action = invocation.getArgument(1);
                        return ("create-response".equals(response) && action.contains("transfer/CreateResponse"))
                                || ("command-response".equals(response) && action.endsWith("/CommandResponse"))
                                || ("receive-response".equals(response) && action.equals(RECEIVE_ACTION))
                                || ("delete-response".equals(response) && action.contains("transfer/DeleteResponse"));
                    });
            wsman.when(() -> WSManUtils.constructCommand(request, io.cloudslang.content.entities.PSEdition.WINDOWS))
                    .thenReturn("Get-Host");
            wsman.when(() -> WSManUtils.commandExecutionIsDone("receive-response")).thenReturn(true);
            wsman.when(() -> WSManUtils.countStreamElements("receive-response")).thenReturn(1);
            wsman.when(() -> WSManUtils.getScriptExitCode("receive-response")).thenReturn("0");
            xml.when(() -> XMLUtils.parseXml(anyString(), anyString())).thenAnswer(invocation -> {
                String path = invocation.getArgument(1);
                if (path.endsWith("/CommandId")) {
                    return COMMAND_ID;
                }
                if (path.contains("stdout")) {
                    return "aGVsbG8=";
                }
                if (path.contains("stderr")) {
                    return "ZXJyb3I=";
                }
                return SHELL_ID;
            });

            Map<String, String> result = service.runCommand(request);

            assertEquals("hello", result.get(RETURN_RESULT));
            assertEquals("error", result.get("stderr"));
            assertEquals("0", result.get("scriptExitCode"));
            client.verify(() -> HttpClientService.execute(any(HttpClientInputs.class)), times(4));
        }
    }

    private WSManRequestInputs requestInputs(String timeout) {
        return new WSManRequestInputs.WSManRequestInputsBuilder()
                .withHost("host").withPort("5986").withProtocol("https")
                .withUsername("user").withPassword("pass").withAuthType("Basic")
                .withTrustAllRoots("true").withX509HostnameVerifier("strict")
                .withMaxEnvelopeSize("153600").withScript("Get-Host")
                .withWinrmLocale("en-US").withOperationTimeout(timeout).build();
    }

    @SuppressWarnings("unchecked")
    private <T> T invoke(String name, Class<?>[] parameterTypes, Object... arguments) throws Exception {
        Method method = WSManRemoteShellService.class.getDeclaredMethod(name, parameterTypes);
        method.setAccessible(true);
        try {
            return (T) method.invoke(service, arguments);
        } catch (java.lang.reflect.InvocationTargetException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            throw exception;
        }
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private static Throwable rootCause(Throwable throwable) {
        while (throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        return throwable;
    }
}
