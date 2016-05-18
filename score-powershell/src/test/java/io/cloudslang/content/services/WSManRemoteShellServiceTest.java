package io.cloudslang.content.services;

import io.cloudslang.content.entities.WSManRequestInputs;
import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.httpclient.ScoreHttpClient;
import io.cloudslang.content.utils.WSManUtils;
import io.cloudslang.content.utils.XMLUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created by giloan on 5/9/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({WSManRemoteShellService.class, WSManUtils.class, XMLUtils.class})
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
    private static final String URL_STR = "http://winrmserver:5985/wsman";
    private static final String RESPONSE_BODY = "request body";
    private static final String RETURN_RESULT = "returnResult";
    private static final String STATUS_CODE = "statusCode";
    private static final String OK_STATUS_CODE = "200";
    private static final String SHELL_ID_NOT_RETRIEVED = "The shell id could not be retrieved.";
    private static final String CREATE_SHELL_REQUEST_XML = "templates/CreateShell.xml";
    private static final String CREATE_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2004/09/transfer/CreateResponse";
    private static final String CREATE_RESPONSE_SHELL_ID_XPATH = "/Envelope/Body/ResourceCreated/ReferenceParameters/SelectorSet/Selector[@Name='ShellId']/text()";
    private static final String FAULT_MESSAGE = "fault message";
    private static final String COMMAND_RESPONSE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandResponse";
    private static final String COMMAND_RESULT_COMMAND_ID_XPATH = "/Envelope/Body/CommandResponse/CommandId";
    private static final String EXECUTE_COMMAND_REQUEST_XML = "templates/ExecuteCommand.xml";
    private static final String COMMAND = "get-host";
    private static final String COMMAND_ID_NOT_RETRIEVED = "The command id could not be retrieved.";
    private static final String UNEXPECTED_SERVICE_RESPONSE = "Unexpected service response: ";
    private static final String RECEIVE_RESPONSE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/ReceiveResponse";
    private static final String RECEIVE_REQUEST_XML = "templates/Receive.xml";
    private static final String EXECUTION_TIMED_OUT = "The script execution timed out!";

    private WSManRequestInputs wsManRequestInputs;
    @Mock
    private ScoreHttpClient scoreHttpClientMock;
    @Mock
    private HttpClientInputs httpClientInputsMock;
    @Mock
    private URL urlMock;
    @Mock
    private Map<String, String> resultMock;
    @Rule
    private ExpectedException thrownException = ExpectedException.none();
    private WSManRemoteShellService wsManRemoteShellServiceSpy;

    @Before
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
        wsManRemoteShellServiceSpy = PowerMockito.spy(new WSManRemoteShellService());
    }

    @After
    public void tearDown() {
        scoreHttpClientMock = null;
        httpClientInputsMock = null;
        urlMock = null;
        resultMock = null;
        wsManRequestInputs = null;
        wsManRemoteShellServiceSpy = null;
    }

    @Test
    public void testRunCommand() throws Exception {
        PowerMockito.doReturn(SHELL_UUID).when(wsManRemoteShellServiceSpy, "createShell", any(ScoreHttpClient.class), any(HttpClientInputs.class),
                any(String.class), any(String.class),
                any(WSManRequestInputs.class));

        PowerMockito.doReturn(COMMAND_UUID).when(wsManRemoteShellServiceSpy, "executeCommand", any(ScoreHttpClient.class),
                any(HttpClientInputs.class), any(String.class), any(String.class),
                any(String.class), any(WSManRequestInputs.class), any(String.class));

        PowerMockito.doReturn(resultMock).when(wsManRemoteShellServiceSpy, "receiveCommandResult", any(ScoreHttpClient.class), any(HttpClientInputs.class),
                any(String.class), any(String.class), any(String.class),
                any(String.class), any(WSManRequestInputs.class));

        PowerMockito.doNothing().when(wsManRemoteShellServiceSpy, "deleteShell", any(ScoreHttpClient.class), any(HttpClientInputs.class),
                any(String.class), any(String.class), any(String.class), any(WSManRequestInputs.class));

        PowerMockito.whenNew(ScoreHttpClient.class).withNoArguments().thenReturn(scoreHttpClientMock);
        PowerMockito.whenNew(HttpClientInputs.class).withNoArguments().thenReturn(httpClientInputsMock);
        PowerMockito.mockStatic(WSManUtils.class);
        PowerMockito.doNothing().when(WSManUtils.class);
        WSManUtils.validateUUID(SHELL_UUID, SHELL_ID);
        WSManUtils.validateUUID(COMMAND_UUID, COMMAND_ID);

        Map<String, String> result = wsManRemoteShellServiceSpy.runCommand(wsManRequestInputs);

        PowerMockito.verifyNew(ScoreHttpClient.class).withNoArguments();
        PowerMockito.verifyNew(HttpClientInputs.class).withNoArguments();
        verifyStatic();
        WSManUtils.validateUUID(SHELL_UUID, SHELL_ID);
        WSManUtils.validateUUID(COMMAND_UUID, COMMAND_ID);
        assertEquals(resultMock, result);
    }

    @Test
    public void testRunCommandThrowsException() throws Exception {
        PowerMockito.doThrow(new RuntimeException(SHELL_ID_NOT_RETRIEVED)).when(wsManRemoteShellServiceSpy, "createShell", any(ScoreHttpClient.class), any(HttpClientInputs.class),
                any(String.class), any(String.class),
                any(WSManRequestInputs.class));

        thrownException.expectMessage(SHELL_ID_NOT_RETRIEVED);
        wsManRemoteShellServiceSpy.runCommand(wsManRequestInputs);

        PowerMockito.verifyNew(ScoreHttpClient.class).withNoArguments();
        PowerMockito.verifyNew(HttpClientInputs.class).withNoArguments();
    }

    @Test
    public void testExecuteRequest() throws Exception {
        doNothing().when(httpClientInputsMock).setBody(RESPONSE_BODY);
        doReturn(resultMock).when(scoreHttpClientMock).execute(httpClientInputsMock);

        Map<String, String> result = Whitebox.invokeMethod(new WSManRemoteShellService(), "executeRequest", scoreHttpClientMock, httpClientInputsMock, RESPONSE_BODY);

        verify(httpClientInputsMock).setBody(RESPONSE_BODY);
        verify(scoreHttpClientMock).execute(httpClientInputsMock);
        assertEquals(resultMock, result);
    }

    @Test
    public void testCreateShell() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(SHELL_UUID);

        String result = Whitebox.invokeMethod(new WSManRemoteShellService(), "createShell", scoreHttpClientMock, httpClientInputsMock, CREATE_SHELL_REQUEST_XML, URL_STR, wsManRequestInputs);

        assertEquals(SHELL_UUID, result);
        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION);
        XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
    }


    @Test
    public void testCreateShellThrowsShellIdNotRetrievedException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(null);

        thrownException.expectMessage(SHELL_ID_NOT_RETRIEVED);
        Whitebox.invokeMethod(new WSManRemoteShellService(), "createShell", scoreHttpClientMock, httpClientInputsMock, CREATE_SHELL_REQUEST_XML, URL_STR, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION);
        XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
        verify(scoreHttpClientMock).execute(httpClientInputsMock);
    }

    @Test
    public void testCreateShellThrowsFaultException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        Mockito.when(WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);

        thrownException.expectMessage(FAULT_MESSAGE);
        Whitebox.invokeMethod(new WSManRemoteShellService(), "createShell", scoreHttpClientMock, httpClientInputsMock, CREATE_SHELL_REQUEST_XML, URL_STR, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
        WSManUtils.getResponseFault(RESPONSE_BODY);
    }

    @Test
    public void testCreateShellThrowsUnexpectedResponseException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(false);

        thrownException.expectMessage(UNEXPECTED_SERVICE_RESPONSE);
        Whitebox.invokeMethod(new WSManRemoteShellService(), "createShell", scoreHttpClientMock, httpClientInputsMock, CREATE_SHELL_REQUEST_XML, URL_STR, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
    }

    @Test
    public void testExecuteCommand() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(RESPONSE_BODY, COMMAND_RESULT_COMMAND_ID_XPATH)).thenReturn(COMMAND_UUID);

        String result = Whitebox.invokeMethod(new WSManRemoteShellService(), "executeCommand", scoreHttpClientMock, httpClientInputsMock, EXECUTE_COMMAND_REQUEST_XML, URL_STR, SHELL_UUID, wsManRequestInputs, COMMAND);

        assertEquals(COMMAND_UUID, result);
        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION);
        XMLUtils.parseXml(RESPONSE_BODY, COMMAND_RESULT_COMMAND_ID_XPATH);
    }

    @Test
    public void testExecuteCommandThrowsFaultException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        Mockito.when(WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);

        thrownException.expectMessage(FAULT_MESSAGE);
        Whitebox.invokeMethod(new WSManRemoteShellService(), "executeCommand", scoreHttpClientMock, httpClientInputsMock, EXECUTE_COMMAND_REQUEST_XML, URL_STR, SHELL_UUID, wsManRequestInputs, COMMAND);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION);
    }

    @Test
    public void testExecuteCommandThrowsCommandIdNotRetrievedException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(RESPONSE_BODY, COMMAND_RESULT_COMMAND_ID_XPATH)).thenReturn("");

        thrownException.expectMessage(COMMAND_ID_NOT_RETRIEVED);
        Whitebox.invokeMethod(new WSManRemoteShellService(), "executeCommand", scoreHttpClientMock, httpClientInputsMock, EXECUTE_COMMAND_REQUEST_XML, URL_STR, SHELL_UUID, wsManRequestInputs, COMMAND);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION);
        XMLUtils.parseXml(RESPONSE_BODY, COMMAND_RESULT_COMMAND_ID_XPATH);
    }

    @Test
    public void testExecuteCommandThrowsUnexpectedResponseException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(false);

        thrownException.expectMessage(UNEXPECTED_SERVICE_RESPONSE);
        Whitebox.invokeMethod(new WSManRemoteShellService(), "executeCommand", scoreHttpClientMock, httpClientInputsMock, EXECUTE_COMMAND_REQUEST_XML, URL_STR, SHELL_UUID, wsManRequestInputs, COMMAND);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
    }

    @Test
    public void testReceiveCommandResult() throws Exception {
        mockExecuteRequest();
        PowerMockito.doReturn(false).when(wsManRemoteShellServiceSpy, "executionIsTimedOut", anyLong(), anyInt());
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        Mockito.when(WSManUtils.commandExecutionIsDone(RESPONSE_BODY)).thenReturn(true);
        PowerMockito.doReturn(resultMock).when(wsManRemoteShellServiceSpy, "processCommandExecutionResponse", any(String.class));

        Map<String, String> result = Whitebox.invokeMethod(wsManRemoteShellServiceSpy, "receiveCommandResult", scoreHttpClientMock, httpClientInputsMock,
                RECEIVE_REQUEST_XML, URL_STR, SHELL_UUID, COMMAND_UUID, wsManRequestInputs);

        assertEquals(resultMock, result);
        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        WSManUtils.commandExecutionIsDone(RESPONSE_BODY);
        PowerMockito.verifyPrivate(wsManRemoteShellServiceSpy).invoke("processCommandExecutionResponse", any(String.class));
    }

    @Test
    public void testReceiveCommandResultThrowsFaultException() throws Exception {
        mockExecuteRequest();
        PowerMockito.doReturn(false).when(wsManRemoteShellServiceSpy, "executionIsTimedOut", anyLong(), anyInt());
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        Mockito.when(WSManUtils.commandExecutionIsDone(RESPONSE_BODY)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        Mockito.when(WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);

        thrownException.expectMessage(FAULT_MESSAGE);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, "receiveCommandResult", scoreHttpClientMock, httpClientInputsMock,
                RECEIVE_REQUEST_XML, URL_STR, SHELL_UUID, COMMAND_UUID, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        WSManUtils.commandExecutionIsDone(RESPONSE_BODY);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
        WSManUtils.getResponseFault(RESPONSE_BODY);
    }

    @Test
    public void testReceiveCommandResultThrowsTimeoutException() throws Exception {
        mockExecuteRequest();
        PowerMockito.doReturn(true).when(wsManRemoteShellServiceSpy, "executionIsTimedOut", anyLong(), anyInt());

        thrownException.expectMessage(EXECUTION_TIMED_OUT);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, "receiveCommandResult", scoreHttpClientMock, httpClientInputsMock,
                RECEIVE_REQUEST_XML, URL_STR, SHELL_UUID, COMMAND_UUID, wsManRequestInputs);
    }

    @Test
    public void testGetResourceId() throws Exception {
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(SHELL_UUID);

        String result = Whitebox.invokeMethod(wsManRemoteShellServiceSpy, "getResourceId", RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED);

        assertEquals(result, SHELL_UUID);
        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
    }

    @Test
    public void testGetResourceIdThrowsShellIdNotRetrieved() throws Exception {
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn("");

        thrownException.expectMessage(SHELL_ID_NOT_RETRIEVED);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, "getResourceId", RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
    }

    @Test
    public void testGetResourceIdThrowsFaultException() throws Exception {
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn("");

        thrownException.expectMessage(SHELL_ID_NOT_RETRIEVED);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, "getResourceId", RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
    }

    @Test
    public void testGetResourceIdThrowsUnexpectedResponseException() throws Exception {
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(false);

        thrownException.expectMessage(UNEXPECTED_SERVICE_RESPONSE);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, "getResourceId", RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
    }

    private void mockExecuteRequest() {
        Map<String, String> result = new HashMap<>();
        result.put(RETURN_RESULT, RESPONSE_BODY);
        result.put(STATUS_CODE, OK_STATUS_CODE);
        doReturn(result).when(scoreHttpClientMock).execute(httpClientInputsMock);
    }
}
