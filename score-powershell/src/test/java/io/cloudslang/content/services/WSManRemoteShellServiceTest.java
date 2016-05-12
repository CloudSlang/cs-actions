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
    private static final String URL_STR = "http://winrmserver:5985/wsman";
    private static final String REQUEST_BODY = "request body";
    private static final String RETURN_RESULT = "returnResult";
    private static final String SHELL_ID_NOT_RETRIEVED = "The shell id could not be retrieved.";
    private static final String CREATE_SHELL_REQUEST_XML = "templates/CreateShell.xml";
    private static final String CREATE_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2004/09/transfer/CreateResponse";
    private static final String CREATE_RESPONSE_SHELL_ID_XPATH = "/Envelope/Body/ResourceCreated/ReferenceParameters/SelectorSet/Selector[@Name='ShellId']/text()";
    private static final String FAULT_MESSAGE = "fault message";


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
    }

    @After
    public void tearDown() {
        scoreHttpClientMock = null;
        httpClientInputsMock = null;
        urlMock = null;
        resultMock = null;
        wsManRequestInputs = null;
    }

    @Test
    public void testExecuteRequest() throws Exception {
        doNothing().when(httpClientInputsMock).setBody(REQUEST_BODY);
        doReturn(resultMock).when(scoreHttpClientMock).execute(httpClientInputsMock);

        Map<String, String> result = Whitebox.invokeMethod(new WSManRemoteShellService(), "executeRequest", scoreHttpClientMock, httpClientInputsMock, REQUEST_BODY);

        verify(httpClientInputsMock).setBody(REQUEST_BODY);
        verify(scoreHttpClientMock).execute(httpClientInputsMock);
        assertEquals(resultMock, result);
    }

    @Test
    public void testCreateShell() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(REQUEST_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(SHELL_UUID);

        String result = Whitebox.invokeMethod(new WSManRemoteShellService(), "createShell", scoreHttpClientMock, httpClientInputsMock, CREATE_SHELL_REQUEST_XML, URL_STR, wsManRequestInputs);

        assertEquals(SHELL_UUID, result);
        verifyStatic();
        WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION);
        XMLUtils.parseXml(REQUEST_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
    }


    @Test
    public void testCreateShellThrowsNullShellIdException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(REQUEST_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(null);

        thrownException.expectMessage(SHELL_ID_NOT_RETRIEVED);
        Whitebox.invokeMethod(new WSManRemoteShellService(), "createShell", scoreHttpClientMock, httpClientInputsMock, CREATE_SHELL_REQUEST_XML, URL_STR, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION);
        XMLUtils.parseXml(REQUEST_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
        verify(scoreHttpClientMock).execute(httpClientInputsMock);
    }

    @Test
    public void testCreateShellThrowsFaultException() throws Exception {
        mockExecuteRequest();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(REQUEST_BODY)).thenReturn(true);
        Mockito.when(WSManUtils.getResponseFault(REQUEST_BODY)).thenReturn(FAULT_MESSAGE);

        thrownException.expectMessage(FAULT_MESSAGE);
        Whitebox.invokeMethod(new WSManRemoteShellService(), "createShell", scoreHttpClientMock, httpClientInputsMock, CREATE_SHELL_REQUEST_XML, URL_STR, wsManRequestInputs);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(REQUEST_BODY);
        WSManUtils.getResponseFault(REQUEST_BODY);
    }

    private void mockExecuteRequest() {
        Map<String, String> result = new HashMap<>();
        result.put(RETURN_RESULT, REQUEST_BODY);
        doReturn(result).when(scoreHttpClientMock).execute(httpClientInputsMock);
    }
}
