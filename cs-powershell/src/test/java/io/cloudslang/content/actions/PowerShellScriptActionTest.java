/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.actions;

import io.cloudslang.content.entities.WSManRequestInputs;
import io.cloudslang.content.services.WSManRemoteShellService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by giloan on 5/6/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PowerShellScriptAction.class)
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

    @Mock
    private WSManRemoteShellService serviceMock;
    @Mock
    private Map<String, String> resultMock;
    @Mock
    private WSManRequestInputs wsManRequestInputsMock;
    @Mock
    private WSManRequestInputs.WSManRequestInputsBuilder wsManRequestInputsBuilderMock;

    @Before
    public void setUp() {
        powerShellScriptAction = new PowerShellScriptAction();
    }

    @After
    public void tearDown() {
        powerShellScriptAction = null;
        serviceMock = null;
        resultMock = null;
        wsManRequestInputsMock = null;
        wsManRequestInputsBuilderMock = null;
    }

    @Test
    public void testExecute() throws Exception {
        configureMocksForSuccessTests();

        Map<String, String> result = powerShellScriptAction.execute(LOCALHOST, PORT, HTTPS, USER, PASS, BASIC_AUTH_TYPE, PROXY_HOST, PROXY_PORT,
                PROXY_USER, PASS, Boolean.TRUE.toString(), X_509_HOSTNAME_VERIFIER_STRICT, TRUST_KEYSTORE, PASS, KERBEROS_CONF_FILE, KERBEROS_LOGIN_CONF_FILE, KERBEROS_SKIP_PORT_FOR_LOOKUP, KEYSTORE, PASS,
                MAX_ENVELOPE_SIZE, SCRIPT, WINRM_LOCALE_EN_US, OPERATION_TIMEOUT);

        verifyNew(WSManRemoteShellService.class).withNoArguments();
        verifyMockInteractions();
        assertEquals(resultMock, result);
    }

    @Test
    public void testExecuteWithInputDefaultValues() throws Exception {
        configureMocksForSuccessTests();

        Map<String, String> result = powerShellScriptAction.execute(LOCALHOST, EMPTY_STRING, EMPTY_STRING, USER, PASS, BASIC_AUTH_TYPE, PROXY_HOST, PROXY_PORT,
                PROXY_USER, PASS, EMPTY_STRING, EMPTY_STRING, TRUST_KEYSTORE, PASS, KERBEROS_CONF_FILE, KERBEROS_LOGIN_CONF_FILE, KERBEROS_SKIP_PORT_FOR_LOOKUP, KEYSTORE, PASS,
                EMPTY_STRING, SCRIPT, EMPTY_STRING, EMPTY_STRING);

        verifyNew(WSManRemoteShellService.class).withNoArguments();
        verifyMockInteractions();
        assertEquals(resultMock, result);
    }

    @Test
    public void testExecuteThrowsException() throws Exception {
        whenNew(WSManRemoteShellService.class).withNoArguments().thenReturn(serviceMock);
        doThrow(new RuntimeException(EXCEPTION_MESSAGE)).when(serviceMock).runCommand(any(WSManRequestInputs.class));

        Map<String, String> result = powerShellScriptAction.execute(LOCALHOST, EMPTY_STRING, EMPTY_STRING, USER, BASIC_AUTH_TYPE, PASS, PROXY_HOST, PROXY_PORT,
                PROXY_USER, PASS, EMPTY_STRING, EMPTY_STRING, TRUST_KEYSTORE, PASS, KERBEROS_CONF_FILE, KERBEROS_LOGIN_CONF_FILE, KERBEROS_SKIP_PORT_FOR_LOOKUP, KEYSTORE, PASS,
                EMPTY_STRING, SCRIPT, EMPTY_STRING, EMPTY_STRING);

        assertTrue(result.get(EXCEPTION).contains(EXCEPTION_MESSAGE));
        assertEquals(RETURN_CODE_FAILURE, result.get(RETURN_CODE));
    }

    @Test
    public void testExecuteWithFailureScriptExitCode() throws Exception {
        whenNew(WSManRemoteShellService.class).withNoArguments().thenReturn(serviceMock);
        doReturn(resultMock).when(serviceMock).runCommand(any(WSManRequestInputs.class));
        doReturn(null).when(resultMock).put(RETURN_CODE, RETURN_CODE_SUCCESS);
        doReturn(RETURN_CODE_FAILURE).when(resultMock).get(SCRIPT_EXIT_CODE);

        Map<String, String> result = powerShellScriptAction.execute(LOCALHOST, EMPTY_STRING, EMPTY_STRING, USER, PASS, BASIC_AUTH_TYPE, PROXY_HOST, PROXY_PORT,
                PROXY_USER, PASS, EMPTY_STRING, EMPTY_STRING, TRUST_KEYSTORE, PASS, KERBEROS_CONF_FILE, KERBEROS_LOGIN_CONF_FILE, KERBEROS_SKIP_PORT_FOR_LOOKUP, KEYSTORE, PASS,
                EMPTY_STRING, SCRIPT, EMPTY_STRING, EMPTY_STRING);

        verifyNew(WSManRemoteShellService.class).withNoArguments();
        verify(serviceMock, times(1)).runCommand(any(WSManRequestInputs.class));
        verify(resultMock, times(1)).put(RETURN_CODE, RETURN_CODE_FAILURE);
        verify(resultMock, times(1)).get(SCRIPT_EXIT_CODE);
        assertEquals(resultMock, result);
    }

    private void configureMocksForSuccessTests() throws Exception {
        whenNew(WSManRemoteShellService.class).withNoArguments().thenReturn(serviceMock);
        doReturn(resultMock).when(serviceMock).runCommand(any(WSManRequestInputs.class));
        doReturn(null).when(resultMock).put(RETURN_CODE, RETURN_CODE_SUCCESS);
        doReturn(RETURN_CODE_SUCCESS).when(resultMock).get(SCRIPT_EXIT_CODE);
    }

    private void verifyMockInteractions() throws IOException, InterruptedException, ParserConfigurationException, TransformerException, XPathExpressionException, TimeoutException, URISyntaxException, SAXException {
        verify(serviceMock, times(1)).runCommand(any(WSManRequestInputs.class));
        verify(resultMock, times(1)).put(RETURN_CODE, RETURN_CODE_SUCCESS);
        verify(resultMock, times(1)).get(SCRIPT_EXIT_CODE);
    }


}
