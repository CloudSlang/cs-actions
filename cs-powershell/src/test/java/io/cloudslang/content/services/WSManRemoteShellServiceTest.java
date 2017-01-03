/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.services;

import io.cloudslang.content.entities.OutputStream;
import io.cloudslang.content.entities.WSManRequestInputs;
import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.httpclient.CSHttpClient;
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
    private static final String EXECUTE_REQUEST_METHOD = "executeRequest";
    private static final String DELETE_SHELL_METHOD = "deleteShell";
    private static final String DELETE_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2004/09/transfer/DeleteResponse";

    private WSManRequestInputs wsManRequestInputs;
    @Mock
    private CSHttpClient csHttpClientMock;
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
        csHttpClientMock = null;
        httpClientInputsMock = null;
        urlMock = null;
        resultMock = null;
        wsManRequestInputs = null;
        wsManRemoteShellServiceSpy = null;
    }

    @Test
    public void testRunCommand() throws Exception {
        PowerMockito.doReturn(SHELL_UUID).when(wsManRemoteShellServiceSpy, CREATE_SHELL_METHOD, any(CSHttpClient.class), any(HttpClientInputs.class),
                any(WSManRequestInputs.class));

        PowerMockito.doReturn(COMMAND_UUID).when(wsManRemoteShellServiceSpy, EXECUTE_COMMAND_METHOD, any(CSHttpClient.class),
                any(HttpClientInputs.class), any(String.class), any(WSManRequestInputs.class), any(String.class));

        PowerMockito.doReturn(resultMock).when(wsManRemoteShellServiceSpy, RECEIVE_COMMAND_RESULT_METHOD, any(CSHttpClient.class), any(HttpClientInputs.class),
                any(String.class), any(String.class), any(WSManRequestInputs.class));

        PowerMockito.doNothing().when(wsManRemoteShellServiceSpy, DELETE_SHELL_METHOD, any(CSHttpClient.class), any(HttpClientInputs.class),
                any(String.class), any(WSManRequestInputs.class));

        PowerMockito.whenNew(CSHttpClient.class).withNoArguments().thenReturn(csHttpClientMock);
        PowerMockito.whenNew(HttpClientInputs.class).withNoArguments().thenReturn(httpClientInputsMock);
        PowerMockito.mockStatic(WSManUtils.class);
        PowerMockito.doNothing().when(WSManUtils.class);
        WSManUtils.validateUUID(SHELL_UUID, SHELL_ID);
        WSManUtils.validateUUID(COMMAND_UUID, COMMAND_ID);

        Map<String, String> result = wsManRemoteShellServiceSpy.runCommand(wsManRequestInputs);

        PowerMockito.verifyNew(CSHttpClient.class).withNoArguments();
        PowerMockito.verifyNew(HttpClientInputs.class).withNoArguments();
        verifyStatic();
        WSManUtils.validateUUID(SHELL_UUID, SHELL_ID);
        WSManUtils.validateUUID(COMMAND_UUID, COMMAND_ID);
        assertEquals(resultMock, result);
    }

    @Test
    public void testRunCommandThrowsException() throws Exception {
        PowerMockito.doThrow(new RuntimeException(SHELL_ID_NOT_RETRIEVED)).when(wsManRemoteShellServiceSpy,
                CREATE_SHELL_METHOD, any(CSHttpClient.class), any(HttpClientInputs.class), any(WSManRequestInputs.class));

        thrownException.expectMessage(SHELL_ID_NOT_RETRIEVED);
        wsManRemoteShellServiceSpy.runCommand(wsManRequestInputs);

        PowerMockito.verifyNew(CSHttpClient.class).withNoArguments();
        PowerMockito.verifyNew(HttpClientInputs.class).withNoArguments();
    }

    @Test
    public void testExecuteRequest() throws Exception {
        doNothing().when(httpClientInputsMock).setBody(RESPONSE_BODY);
        doReturn(resultMock).when(csHttpClientMock).execute(httpClientInputsMock);

        Map<String, String> result = Whitebox.invokeMethod(new WSManRemoteShellService(), EXECUTE_REQUEST_METHOD, csHttpClientMock, httpClientInputsMock, RESPONSE_BODY);

        verify(httpClientInputsMock).setBody(RESPONSE_BODY);
        verify(csHttpClientMock).execute(httpClientInputsMock);
        assertEquals(resultMock, result);
    }

    @Test
    public void testExecuteRequestThrowsException() throws Exception {
        doNothing().when(httpClientInputsMock).setBody(RESPONSE_BODY);
        doReturn(resultMock).when(csHttpClientMock).execute(httpClientInputsMock);
        doReturn(UNAUTHORIZED_STATUS_CODE).when(resultMock).get(STATUS_CODE);

        thrownException.expectMessage(UNAUTHORIZED_EXCEPTION_MESSAGE);
        Whitebox.invokeMethod(new WSManRemoteShellService(), EXECUTE_REQUEST_METHOD, csHttpClientMock, httpClientInputsMock, RESPONSE_BODY);

        verify(httpClientInputsMock).setBody(RESPONSE_BODY);
        verify(csHttpClientMock).execute(httpClientInputsMock);
        verify(resultMock).get(STATUS_CODE);
    }

    @Test
    public void testCreateShell() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(SHELL_UUID);

        String result = Whitebox.invokeMethod(new WSManRemoteShellService(), CREATE_SHELL_METHOD, csHttpClientMock, httpClientInputsMock, wsManRequestInputs);

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
        Whitebox.invokeMethod(new WSManRemoteShellService(), CREATE_SHELL_METHOD, csHttpClientMock, httpClientInputsMock, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION);
        XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
        verify(csHttpClientMock).execute(httpClientInputsMock);
    }

    @Test
    public void testCreateShellThrowsFaultException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, CREATE_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        Mockito.when(WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);

        thrownException.expectMessage(FAULT_MESSAGE);
        Whitebox.invokeMethod(new WSManRemoteShellService(), CREATE_SHELL_METHOD, csHttpClientMock, httpClientInputsMock, wsManRequestInputs);

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
        Whitebox.invokeMethod(new WSManRemoteShellService(), CREATE_SHELL_METHOD, csHttpClientMock, httpClientInputsMock, wsManRequestInputs);

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

        String result = Whitebox.invokeMethod(new WSManRemoteShellService(), EXECUTE_COMMAND_METHOD, csHttpClientMock, httpClientInputsMock,
                SHELL_UUID, wsManRequestInputs, COMMAND);

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
        Whitebox.invokeMethod(new WSManRemoteShellService(), EXECUTE_COMMAND_METHOD, csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs, COMMAND);

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
        Whitebox.invokeMethod(new WSManRemoteShellService(), EXECUTE_COMMAND_METHOD, csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs, COMMAND);

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
        Whitebox.invokeMethod(new WSManRemoteShellService(), EXECUTE_COMMAND_METHOD, csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs, COMMAND);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, COMMAND_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
    }

    @Test
    public void testReceiveCommandResult() throws Exception {
        mockExecuteRequest();
        PowerMockito.doReturn(false).when(wsManRemoteShellServiceSpy, EXECUTION_IS_TIMED_OUT_METHOD, anyLong(), anyInt());
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        Mockito.when(WSManUtils.commandExecutionIsDone(RESPONSE_BODY)).thenReturn(true);
        PowerMockito.doReturn(resultMock).when(wsManRemoteShellServiceSpy, PROCESS_COMMAND_EXECUTION_RESPONSE_METHOD, any(String.class));

        Map<String, String> result = Whitebox.invokeMethod(wsManRemoteShellServiceSpy, RECEIVE_COMMAND_RESULT_METHOD, csHttpClientMock, httpClientInputsMock,
                SHELL_UUID, COMMAND_UUID, wsManRequestInputs);

        assertEquals(resultMock, result);
        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        WSManUtils.commandExecutionIsDone(RESPONSE_BODY);
        PowerMockito.verifyPrivate(wsManRemoteShellServiceSpy).invoke(PROCESS_COMMAND_EXECUTION_RESPONSE_METHOD, any(String.class));
    }

    @Test
    public void testReceiveCommandResultThrowsFaultException() throws Exception {
        mockExecuteRequest();
        PowerMockito.doReturn(false).when(wsManRemoteShellServiceSpy, EXECUTION_IS_TIMED_OUT_METHOD, anyLong(), anyInt());
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        Mockito.when(WSManUtils.commandExecutionIsDone(RESPONSE_BODY)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        Mockito.when(WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);

        thrownException.expectMessage(FAULT_MESSAGE);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, RECEIVE_COMMAND_RESULT_METHOD, csHttpClientMock, httpClientInputsMock,
                SHELL_UUID, COMMAND_UUID, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        WSManUtils.commandExecutionIsDone(RESPONSE_BODY);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
        WSManUtils.getResponseFault(RESPONSE_BODY);
    }

    @Test
    public void testReceiveCommandResultThrowsTimeoutException() throws Exception {
        mockExecuteRequest();
        PowerMockito.doReturn(true).when(wsManRemoteShellServiceSpy, EXECUTION_IS_TIMED_OUT_METHOD, anyLong(), anyInt());

        thrownException.expectMessage(EXECUTION_TIMED_OUT);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, RECEIVE_COMMAND_RESULT_METHOD, csHttpClientMock, httpClientInputsMock,
                SHELL_UUID, COMMAND_UUID, wsManRequestInputs);
    }

    @Test
    public void testGetResourceId() throws Exception {
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(SHELL_UUID);

        String result = Whitebox.invokeMethod(wsManRemoteShellServiceSpy, GET_RESOURCE_ID_METHOD, RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
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
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, GET_RESOURCE_ID_METHOD, RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        XMLUtils.parseXml(RESPONSE_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
    }

    @Test
    public void testGetResourceIdThrowsFaultException() throws Exception {
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        Mockito.when(WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);

        thrownException.expectMessage(FAULT_MESSAGE);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, GET_RESOURCE_ID_METHOD, RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
        WSManUtils.getResponseFault(RESPONSE_BODY);
    }

    @Test
    public void testGetResourceIdThrowsUnexpectedResponseException() throws Exception {
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(false);

        thrownException.expectMessage(UNEXPECTED_SERVICE_RESPONSE);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, GET_RESOURCE_ID_METHOD, RESPONSE_BODY, RECEIVE_RESPONSE_ACTION,
                CREATE_RESPONSE_SHELL_ID_XPATH, SHELL_ID_NOT_RETRIEVED);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, RECEIVE_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
    }

    @Test
    public void testProcessCommandExecutionResponse() throws Exception {
        doReturn(RECEIVE_RESULT).when(resultMock).get(RETURN_RESULT);
        PowerMockito.doReturn(STDOUT_VALUE).when(wsManRemoteShellServiceSpy, BUILD_RESULT_FROM_RESPONSE_STREAMS_METHOD, RECEIVE_RESULT, OutputStream.STDOUT);
        PowerMockito.doReturn(STDERR_VALUE).when(wsManRemoteShellServiceSpy, BUILD_RESULT_FROM_RESPONSE_STREAMS_METHOD, RECEIVE_RESULT, OutputStream.STDERR);
        PowerMockito.mockStatic(WSManUtils.class);
        PowerMockito.when(WSManUtils.getScriptExitCode(RECEIVE_RESULT)).thenReturn(SCRIPT_EXIT_CODE_ZERO);

        Map<String, String> result = Whitebox.invokeMethod(wsManRemoteShellServiceSpy, PROCESS_COMMAND_EXECUTION_RESPONSE_METHOD, resultMock);

        assertEquals(STDOUT_VALUE, result.get(RETURN_RESULT));
        assertEquals(STDERR_VALUE, result.get(STDERR));
        assertEquals(SCRIPT_EXIT_CODE_ZERO, result.get(SCRIPT_EXIT_CODE));
        verify(resultMock, times(3)).get(RETURN_RESULT);
        verifyStatic();
        WSManUtils.getScriptExitCode(RECEIVE_RESULT);
    }

    @Test
    public void testDeleteShell() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION)).thenReturn(true);

        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, WSManRemoteShellServiceTest.DELETE_SHELL_METHOD, csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION);
    }

    @Test
    public void testDeleteShellThrowsFaultException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(true);
        Mockito.when(WSManUtils.getResponseFault(RESPONSE_BODY)).thenReturn(FAULT_MESSAGE);

        thrownException.expectMessage(FAULT_MESSAGE);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, WSManRemoteShellServiceTest.DELETE_SHELL_METHOD, csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
        WSManUtils.getResponseFault(RESPONSE_BODY);
    }

    @Test
    public void testDeleteShellThrowsUnexpectedServiceResponseException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(RESPONSE_BODY)).thenReturn(false);

        thrownException.expectMessage(UNEXPECTED_SERVICE_RESPONSE);
        Whitebox.invokeMethod(wsManRemoteShellServiceSpy, WSManRemoteShellServiceTest.DELETE_SHELL_METHOD, csHttpClientMock, httpClientInputsMock, SHELL_UUID, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(RESPONSE_BODY, DELETE_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(RESPONSE_BODY);
    }

    @Test
    public void testBuildResultFromResponseStreams() throws Exception {
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.countStreamElements(RECEIVE_RESULT)).thenReturn(2);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(anyString(), anyString())).thenReturn("c3RyZWFtX3ZhbA==");

        String result = Whitebox.invokeMethod(wsManRemoteShellServiceSpy, "buildResultFromResponseStreams", RECEIVE_RESULT, OutputStream.STDOUT);

        assertEquals("stream_val" + "stream_val", result);
        verifyStatic();
        WSManUtils.countStreamElements(RECEIVE_RESULT);
        XMLUtils.parseXml(anyString(), anyString());
    }

    private void mockExecuteRequest() {
        Map<String, String> result = new HashMap<>();
        result.put(RETURN_RESULT, RESPONSE_BODY);
        result.put(STATUS_CODE, OK_STATUS_CODE);
        doReturn(result).when(csHttpClientMock).execute(httpClientInputsMock);
    }
}
