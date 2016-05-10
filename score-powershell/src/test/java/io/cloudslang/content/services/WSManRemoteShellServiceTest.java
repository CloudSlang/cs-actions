package io.cloudslang.content.services;

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
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.*;

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
    private static final String COMMAND_UUID = "C0DE9575-6E2D-4C79-9367-676071BDE404";
    private static final String COMMAND_ID = "commandId";
    private static final String URL_STR = "http://winrmserver:5985/wsman";
    private static final String REQUEST_BODY = "request body";
    private static final String RETURN_RESULT = "returnResult";
    private static final String SHELL_ID_NOT_RETRIEVED = "The shell id could not be retrieved.";
    private static final String CREATE_SHELL_REQUEST_XML = "templates/CreateShell.xml";
    private static final String CREATE_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2004/09/transfer/CreateResponse";
    private static final String CREATE_RESPONSE_SHELL_ID_XPATH = "/Envelope/Body/ResourceCreated/ReferenceParameters/SelectorSet/Selector[@Name='ShellId']/text()";
    private static final String FAULT_MESSAGE = "fault message";


    private WSManRemoteShellService wsManRemoteShellService;
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
        wsManRemoteShellService = new WSManRemoteShellService();
    }

    @After
    public void tearDown() {
        wsManRemoteShellService = null;
        scoreHttpClientMock = null;
        httpClientInputsMock = null;
        urlMock = null;
        resultMock = null;
    }

    @Test
    public void testRunCommand() throws Exception {
        wsManRemoteShellService = new WSManRemoteShellService() {
            @Override
            protected String createShell(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName, String url, String maxEnvelopeSize, String winrmLocale, String operationTimeout) throws RuntimeException, IOException, URISyntaxException, TransformerException, XPathExpressionException, SAXException, ParserConfigurationException {
                return SHELL_UUID;
            }

            @Override
            protected String executeCommand(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName, String url, String shellId, String command, String maxEnvelopeSize, String winrmLocale, String operationTimeout) throws RuntimeException, IOException, URISyntaxException, TransformerException, XPathExpressionException, SAXException, ParserConfigurationException {
                return COMMAND_UUID;
            }

            @Override
            protected Map<String, String> receiveCommandResult(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName, String url, String shellId, String commandId, String maxEnvelopeSize, String winrmLocale, int operationTimeout) throws RuntimeException, IOException, URISyntaxException, TransformerException, TimeoutException, XPathExpressionException, SAXException, ParserConfigurationException, InterruptedException {
                return resultMock;
            }

            @Override
            protected void deleteShell(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName, String url, String shellId, String maxEnvelopeSize, String winrmLocale, String operationTimeout) throws RuntimeException, IOException, URISyntaxException, TransformerException, XPathExpressionException, SAXException, ParserConfigurationException {

            }
        };
        whenNew(ScoreHttpClient.class).withNoArguments().thenReturn(scoreHttpClientMock);
        whenNew(HttpClientInputs.class).withNoArguments().thenReturn(httpClientInputsMock);
        mockStatic(WSManUtils.class);
        PowerMockito.doNothing().when(WSManUtils.class);
        WSManUtils.validateUUID(SHELL_UUID, SHELL_ID);
        WSManUtils.validateUUID(COMMAND_UUID, COMMAND_ID);

        Map<String, String> result = wsManRemoteShellService.runCommand(LOCALHOST, PORT, HTTPS, USER, PASS, PROXY_HOST, PROXY_PORT,
                PROXY_USER, PASS, MAX_ENVELOPE_SIZE, Boolean.TRUE.toString(), X_509_HOSTNAME_VERIFIER_STRICT, KEYSTORE, PASS,
                TRUST_KEYSTORE, PASS, SCRIPT, WINRM_LOCALE_EN_US, Integer.parseInt(OPERATION_TIMEOUT));

        verifyNew(ScoreHttpClient.class).withNoArguments();
        verifyNew(HttpClientInputs.class).withNoArguments();
        verifyStatic();
        WSManUtils.validateUUID(SHELL_UUID, SHELL_ID);
        WSManUtils.validateUUID(COMMAND_UUID, COMMAND_ID);
        assertEquals(resultMock, result);
    }

    @Test
    public void testExecuteRequest() throws TransformerException {
        doNothing().when(httpClientInputsMock).setBody(REQUEST_BODY);
        doReturn(resultMock).when(scoreHttpClientMock).execute(httpClientInputsMock);

        Map<String, String> result = wsManRemoteShellService.executeRequest(scoreHttpClientMock, httpClientInputsMock, REQUEST_BODY);

        verify(httpClientInputsMock).setBody(REQUEST_BODY);
        verify(scoreHttpClientMock).execute(httpClientInputsMock);
        assertEquals(resultMock, result);
    }

    @Test
    public void testCreateShell() throws ParserConfigurationException, TransformerException, IOException, XPathExpressionException, URISyntaxException, SAXException {
        configureServiceForTesting();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(REQUEST_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(SHELL_UUID);

        String result = wsManRemoteShellService.createShell(scoreHttpClientMock, httpClientInputsMock, CREATE_SHELL_REQUEST_XML, URL_STR, MAX_ENVELOPE_SIZE, WINRM_LOCALE_EN_US, OPERATION_TIMEOUT);

        assertEquals(SHELL_UUID, result);
        verifyStatic();
        WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION);
        XMLUtils.parseXml(REQUEST_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
    }

    @Test
    public void testCreateShellThrowsNullShellIdException() throws ParserConfigurationException, TransformerException, IOException, XPathExpressionException, URISyntaxException, SAXException {
        configureServiceForTesting();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION)).thenReturn(true);
        PowerMockito.mockStatic(XMLUtils.class);
        Mockito.when(XMLUtils.parseXml(REQUEST_BODY, CREATE_RESPONSE_SHELL_ID_XPATH)).thenReturn(null);

        thrownException.expectMessage(SHELL_ID_NOT_RETRIEVED);
        wsManRemoteShellService.createShell(scoreHttpClientMock, httpClientInputsMock, CREATE_SHELL_REQUEST_XML, URL_STR, MAX_ENVELOPE_SIZE, WINRM_LOCALE_EN_US, OPERATION_TIMEOUT);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION);
        XMLUtils.parseXml(REQUEST_BODY, CREATE_RESPONSE_SHELL_ID_XPATH);
    }

    @Test
    public void testCreateShellThrowsFaultException() throws ParserConfigurationException, SAXException, XPathExpressionException, IOException, TransformerException, URISyntaxException {
        configureServiceForTesting();
        PowerMockito.mockStatic(WSManUtils.class);
        Mockito.when(WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION)).thenReturn(false);
        Mockito.when(WSManUtils.isFaultResponse(REQUEST_BODY)).thenReturn(true);
        Mockito.when(WSManUtils.getResponseFault(REQUEST_BODY)).thenReturn(FAULT_MESSAGE);

        thrownException.expectMessage(FAULT_MESSAGE);
        wsManRemoteShellService.createShell(scoreHttpClientMock, httpClientInputsMock, CREATE_SHELL_REQUEST_XML, URL_STR, MAX_ENVELOPE_SIZE, WINRM_LOCALE_EN_US, OPERATION_TIMEOUT);

        verifyStatic();
        WSManUtils.isSpecificResponseAction(REQUEST_BODY, CREATE_RESPONSE_ACTION);
        WSManUtils.isFaultResponse(REQUEST_BODY);
        WSManUtils.getResponseFault(REQUEST_BODY);
    }

    private void configureServiceForTesting() {
        wsManRemoteShellService = new WSManRemoteShellService() {
            @Override
            protected Map<String, String> executeRequest(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String requestMessage) {
                Map<String, String> result = new HashMap<>();
                result.put(RETURN_RESULT, REQUEST_BODY);
                return result;
            }
        };
    }
}
