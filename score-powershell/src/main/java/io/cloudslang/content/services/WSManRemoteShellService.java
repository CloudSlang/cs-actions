package io.cloudslang.content.services;

import io.cloudslang.content.entities.EncoderDecoder;
import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.httpclient.ScoreHttpClient;
import io.cloudslang.content.utils.Constants;
import io.cloudslang.content.utils.ResourceLoader;
import io.cloudslang.content.utils.WSManUtils;
import io.cloudslang.content.utils.XMLUtils;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_RESULT;

/**
 * Created by giloan on 3/27/2016.
 */
public class WSManRemoteShellService {

    private static final String SERVICE_ADDRESS_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_SERVICE_NETWORK_ADDRESS";
    private static final String SHELL_ID_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_SHELL_ID";
    private static final String MESSAGE_ID_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_MESSAGE_ID";
    private static final String COMMAND_ID_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_COMMAND_ID";
    private static final String COMMAND_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_COMMAND";
    private static final String MAX_ENVELOPE_SIZE_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_MAX_ENVELOPE_SIZE";
    private static final String OPERATION_TIMEOUT_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_OPERATION_TIMEOUT";
    private static final String WINRM_LOCALE_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_LOCALE";

    private static final String CREATE_RESPONSE_SHELL_ID_XPATH = "/Envelope/Body/ResourceCreated/ReferenceParameters/SelectorSet/Selector[@Name='ShellId']/text()";
    private static final String COMMAND_RESULT_COMMAND_ID_XPATH = "/Envelope/Body/CommandResponse/CommandId";
    private static final String RECEIVE_RESPONSE_XPATH = "/Envelope/Body/ReceiveResponse/Stream[%d]/text()";
    private static final String CREATE_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2004/09/transfer/CreateResponse";

    private static final String COMMAND_RESPONSE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandResponse";
    private static final String RECEIVE_RESPONSE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/ReceiveResponse";
    private static final String DELETE_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2004/09/transfer/DeleteResponse";
    private static final String CONTENT_TYPE_HEADER = "Content-Type:application/soap+xml;charset=UTF-8";

    private static final String BASIC_AUTH_TYPE = "Basic";
    private static final String WSMAN_RESOURCE_URI = "/wsman";
    private static final String NEW_LINE_SEPARATOR = "\\n";
    private static final String UUID_LABEL = "uuid:";
    private static final String PLACEHOLDER_NOT_FOUND = "Resource does not contain the expected placeholder name: ";
    private static final String CREATE_SHELL_REQUEST_XML = "templates/CreateShell.xml";
    private static final String EXECUTE_COMMAND_REQUEST_XML = "templates/ExecuteCommand.xml";
    private static final String RECEIVE_REQUEST_XML = "templates/Receive.xml";
    private static final String DELETE_SHELL_REQUEST_XML = "templates/DeleteShell.xml";
    private static final String UNEXPECTED_SERVICE_RESPONSE = "Unexpected service response: ";
    private static final String SHELL_ID = "shellId";
    private static final String COMMAND_ID = "commandId";
    private static final String EXECUTION_TIMED_OUT = "The script execution timed out!";
    private static final String COMMAND_ID_NOT_RETRIEVED = "The command id could not be retrieved.";
    private static final String SHELL_ID_NOT_RETRIEVED = "The shell id could not be retrieved.";

    private long commandExecutionStartTime;

    /**
     * Executes a command on a remote shell by communicating with the WinRM server from the remote host.
     * Method creates a shell, runs a command on the shell, waits for the command execution to finnish, retrieves the result then deletes the shell.
     *
     * @param host
     * @param port
     * @param protocol
     * @param username
     * @param password
     * @param proxyHost
     * @param proxyPort
     * @param proxyUsername
     * @param proxyPassword
     * @param maxEnvelopeSize
     * @param trustAllRoots
     * @param x509HostnameVerifier
     * @param keystore
     * @param keystorePassword
     * @param trustKeystore
     * @param trustPassword
     * @param command
     * @param winrmLocale
     * @param operationTimeout
     * @return a map with the result of the command and the exit code of the command execution.
     * @throws RuntimeException
     * @throws IOException
     * @throws InterruptedException
     * @throws ParserConfigurationException
     * @throws TransformerException
     * @throws XPathExpressionException
     * @throws TimeoutException
     * @throws URISyntaxException
     * @throws SAXException
     */
    public Map<String, String> runCommand(String host, String port, String protocol, String username, String password, String proxyHost, String proxyPort, String proxyUsername,
                                          String proxyPassword, String maxEnvelopeSize, String trustAllRoots, String x509HostnameVerifier, String keystore,
                                          String keystorePassword, String trustKeystore, String trustPassword, String command, String winrmLocale, int operationTimeout) throws RuntimeException, IOException, InterruptedException, ParserConfigurationException, TransformerException, XPathExpressionException, TimeoutException, URISyntaxException, SAXException {
        ScoreHttpClient scoreHttpClient = new ScoreHttpClient();
        HttpClientInputs httpClientInputs = new HttpClientInputs();
        URL url = buildURL(host, port, protocol, WSMAN_RESOURCE_URI);
        httpClientInputs = setCommonHttpInputs(httpClientInputs, url, username, password, proxyHost, proxyPort, proxyUsername, proxyPassword, trustAllRoots, x509HostnameVerifier, keystore, keystorePassword, trustKeystore, trustPassword);
        String shellId = createShell(scoreHttpClient, httpClientInputs, CREATE_SHELL_REQUEST_XML, url.toString(), maxEnvelopeSize, winrmLocale, String.valueOf(operationTimeout));
        WSManUtils.validateUUID(shellId, SHELL_ID);
        String commandStr = "PowerShell" + " " + "-NonInteractive" + " " + "-EncodedCommand" + " " + EncoderDecoder.encodeStringInBase64(command, Charsets.UTF_16LE);
        String commandId = executeCommand(scoreHttpClient, httpClientInputs, EXECUTE_COMMAND_REQUEST_XML, url.toString(), shellId, commandStr, maxEnvelopeSize, winrmLocale, String.valueOf(operationTimeout));
        WSManUtils.validateUUID(commandId, COMMAND_ID);
        Map<String, String> scriptResults = receiveCommandResult(scoreHttpClient, httpClientInputs, RECEIVE_REQUEST_XML, url.toString(), shellId, commandId, maxEnvelopeSize, winrmLocale, operationTimeout);
        deleteShell(scoreHttpClient, httpClientInputs, DELETE_SHELL_REQUEST_XML, url.toString(), shellId, maxEnvelopeSize, winrmLocale, String.valueOf(operationTimeout));
        return scriptResults;
    }

    /**
     * Configures the HttpClientInputs object with the most common http parameters.
     *
     * @param httpClientInputs
     * @param url
     * @param username
     * @param password
     * @param proxyHost
     * @param proxyPort
     * @param proxyUsername
     * @param proxyPassword
     * @param trustAllRoots
     * @param x509HostnameVerifier
     * @param keystore
     * @param keystorePassword
     * @param trustKeystore
     * @param trustPassword
     * @return the configured HttpClientInputs object.
     * @throws MalformedURLException
     */
    public HttpClientInputs setCommonHttpInputs(HttpClientInputs httpClientInputs, URL url, String username, String password, String proxyHost, String proxyPort,
                                                String proxyUsername, String proxyPassword, String trustAllRoots, String x509HostnameVerifier, String keystore,
                                                String keystorePassword, String trustKeystore, String trustPassword) throws MalformedURLException {
        httpClientInputs.setUrl(url.toString());
        httpClientInputs.setAuthType(BASIC_AUTH_TYPE);
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);
        httpClientInputs.setTrustAllRoots(trustAllRoots);
        httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        httpClientInputs.setProxyHost(proxyHost);
        httpClientInputs.setProxyPort(proxyPort);
        httpClientInputs.setProxyUsername(proxyUsername);
        httpClientInputs.setProxyPassword(proxyPassword);
        httpClientInputs.setKeystore(keystore);
        httpClientInputs.setKeystorePassword(keystorePassword);
        httpClientInputs.setTrustKeystore(trustKeystore);
        httpClientInputs.setTrustPassword(trustPassword);
        String headers = httpClientInputs.getHeaders();
        if (StringUtils.isEmpty(headers)) {
            httpClientInputs.setHeaders(CONTENT_TYPE_HEADER);
        } else {
            httpClientInputs.setHeaders(headers + NEW_LINE_SEPARATOR + CONTENT_TYPE_HEADER);
        }
        httpClientInputs.setMethod(HttpPost.METHOD_NAME);
        return httpClientInputs;
    }

    /**
     * This method executes a request with the given ScoreHttpClient, HttpClientInputs and body.
     *
     * @param scoreHttpClient
     * @param httpClientInputs
     * @param requestMessage
     * @return the result of the request execution.
     */
    protected Map<String, String> executeRequest(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String requestMessage) {
        httpClientInputs.setBody(requestMessage);
        return scoreHttpClient.execute(httpClientInputs);
    }

    /**
     * Creates a shell on the remote server and returns the shell id.
     * @param scoreHttpClient
     * @param httpClientInputs
     * @param resourceName
     * @param url
     * @param maxEnvelopeSize
     * @param winrmLocale
     * @param operationTimeout
     * @return  the id of the created shell.
     * @throws RuntimeException
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    protected String createShell(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName, String url, String maxEnvelopeSize, String winrmLocale, String operationTimeout) throws RuntimeException, IOException, URISyntaxException, TransformerException, XPathExpressionException, SAXException, ParserConfigurationException {
        String document = ResourceLoader.loadAsString(resourceName);
        document = createCreateShellRequestBody(document, url, maxEnvelopeSize, winrmLocale, operationTimeout);
        Map<String, String> createShellResult = executeRequest(scoreHttpClient, httpClientInputs, document);
        if (WSManUtils.isSpecificResponseAction(createShellResult.get(RETURN_RESULT), CREATE_RESPONSE_ACTION)) {
            String shellId = XMLUtils.parseXml(createShellResult.get(RETURN_RESULT), CREATE_RESPONSE_SHELL_ID_XPATH);
            if (StringUtils.isNotBlank(shellId)) {
                return shellId;
            } else {
                throw new RuntimeException(SHELL_ID_NOT_RETRIEVED);
            }
        } else if (WSManUtils.isFaultResponse(createShellResult.get(RETURN_RESULT))) {
            throw new RuntimeException(WSManUtils.getResponseFault(createShellResult.get(RETURN_RESULT)));
        } else {
            throw new RuntimeException(UNEXPECTED_SERVICE_RESPONSE + createShellResult.get(RETURN_RESULT));
        }
    }

    /**
     * Executes a command on the given shell.
     * @param scoreHttpClient
     * @param httpClientInputs
     * @param resourceName
     * @param url
     * @param shellId
     * @param command
     * @param maxEnvelopeSize
     * @param winrmLocale
     * @param operationTimeout
     * @return the command id.
     * @throws RuntimeException
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    protected String executeCommand(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName, String url, String shellId, String command, String maxEnvelopeSize, String winrmLocale, String operationTimeout) throws RuntimeException, IOException, URISyntaxException, TransformerException, XPathExpressionException, SAXException, ParserConfigurationException {
        String documentStr = ResourceLoader.loadAsString(resourceName);
        documentStr = createExecuteCommandRequestBody(documentStr, url, shellId, command, maxEnvelopeSize, winrmLocale, operationTimeout);
        commandExecutionStartTime = System.currentTimeMillis() / 1000;
        Map<String, String> executeCommandResult = executeRequest(scoreHttpClient, httpClientInputs, documentStr);
        if (WSManUtils.isSpecificResponseAction(executeCommandResult.get(RETURN_RESULT), COMMAND_RESPONSE_ACTION)) {
            String commandId = XMLUtils.parseXml(executeCommandResult.get(RETURN_RESULT), COMMAND_RESULT_COMMAND_ID_XPATH);
            if (StringUtils.isNotBlank(commandId)) {
                return commandId;
            } else {
                throw new RuntimeException(COMMAND_ID_NOT_RETRIEVED);
            }
        } else if (WSManUtils.isFaultResponse(executeCommandResult.get(RETURN_RESULT))) {
            throw new RuntimeException(WSManUtils.getResponseFault(executeCommandResult.get(RETURN_RESULT)));
        } else {
            throw new RuntimeException(UNEXPECTED_SERVICE_RESPONSE + executeCommandResult.get(RETURN_RESULT));
        }
    }

    /**
     * Waits for a specific command that is running on a remote shell to finnish it's execution.
     * @param scoreHttpClient
     * @param httpClientInputs
     * @param resourceName
     * @param url
     * @param shellId
     * @param commandId
     * @param maxEnvelopeSize
     * @param winrmLocale
     * @param operationTimeout
     * @return the command execution result and exit code.
     * @throws RuntimeException
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     * @throws TimeoutException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws InterruptedException
     */
    protected Map<String, String> receiveCommandResult(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName, String url, String shellId, String commandId, String maxEnvelopeSize, String winrmLocale, int operationTimeout) throws RuntimeException, IOException, URISyntaxException, TransformerException, TimeoutException, XPathExpressionException, SAXException, ParserConfigurationException, InterruptedException {
        String documentStr = ResourceLoader.loadAsString(resourceName);
        documentStr = createReceiveRequestBody(documentStr, url, shellId, commandId, maxEnvelopeSize, winrmLocale, String.valueOf(operationTimeout));
        Map<String, String> receiveResult;
        while (true) {
            receiveResult = executeRequest(scoreHttpClient, httpClientInputs, documentStr);
            if (executionIsTimedOut(commandExecutionStartTime, operationTimeout)) {
                throw new TimeoutException(EXECUTION_TIMED_OUT);
            } else if (WSManUtils.isSpecificResponseAction(receiveResult.get(RETURN_RESULT), RECEIVE_RESPONSE_ACTION) &&
                    WSManUtils.commandExecutionIsDone(receiveResult.get(RETURN_RESULT))) {
                break;
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw e;
            }
        }

        if (WSManUtils.isSpecificResponseAction(receiveResult.get(RETURN_RESULT), RECEIVE_RESPONSE_ACTION)) {
            Map<String, String> scriptResults = new HashMap<>();
            scriptResults.put(RETURN_RESULT, buildResultFromResponseStreams(receiveResult.get(RETURN_RESULT)));
            scriptResults.put(Constants.OutputNames.SCRIPT_EXIT_CODE, WSManUtils.getScriptExitCode(receiveResult.get(RETURN_RESULT)));
            return scriptResults;
        } else {
            throw new RuntimeException(WSManUtils.getResponseFault(receiveResult.get(RETURN_RESULT)));
        }
    }

    /**
     * Deletes the remote shell.
     * @param scoreHttpClient
     * @param httpClientInputs
     * @param resourceName
     * @param url
     * @param shellId
     * @param maxEnvelopeSize
     * @param winrmLocale
     * @param operationTimeout
     * @throws RuntimeException
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    protected void deleteShell(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName, String url, String shellId, String maxEnvelopeSize, String winrmLocale, String operationTimeout) throws RuntimeException, IOException, URISyntaxException, TransformerException, XPathExpressionException, SAXException, ParserConfigurationException {
        String documentStr = ResourceLoader.loadAsString(resourceName);
        documentStr = createDeleteShellRequestBody(documentStr, url, shellId, maxEnvelopeSize, winrmLocale, operationTimeout);
        Map<String, String> deleteShellResult = executeRequest(scoreHttpClient, httpClientInputs, documentStr);
        if (WSManUtils.isSpecificResponseAction(deleteShellResult.get(RETURN_RESULT), DELETE_RESPONSE_ACTION)) {
            return;
        } else if (WSManUtils.isFaultResponse(deleteShellResult.get(RETURN_RESULT))) {
            throw new RuntimeException(WSManUtils.getResponseFault(deleteShellResult.get(RETURN_RESULT)));
        } else {
            throw new RuntimeException(UNEXPECTED_SERVICE_RESPONSE + deleteShellResult.get(RETURN_RESULT));
        }
    }

    /**
     * Constructs the executed command response from multiple streams of data containing the encoded result of the execution.
     * @param response
     * @return the decoded result of the command in a string.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws IOException
     */
    protected String buildResultFromResponseStreams(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        StringBuilder commandResult = new StringBuilder();
        int noOfStreams = WSManUtils.countStreamElements(response);
        for (int streamNo = 1; streamNo <= noOfStreams; streamNo++) {
            String stream = XMLUtils.parseXml(response, String.format(RECEIVE_RESPONSE_XPATH, streamNo));
            if (!"DQo=".equals(stream)) {
                commandResult.append(stream);
            }
        }
        return EncoderDecoder.decodeBase64String(commandResult.toString());
    }

    /**
     * Check whether or not the command execution reach the timeout value.
     *
     * @param aStartTime A start time in seconds.
     * @param aTimeout   A timeout value in seconds.
     * @return true if it reaches timeout.
     */
    private boolean executionIsTimedOut(long aStartTime, int aTimeout) {
        if (aTimeout != 0) {
            long now = System.currentTimeMillis() / 1000;
            if ((now - aStartTime) >= aTimeout) {
                return true;
            }
        }
        return false;
    }

    private String createCreateShellRequestBody(String doc, String url, String maxEnvelopeSize, String winrmLocale, String operationTimeout) throws RuntimeException {
        doc = replaceCommonPlaceholders(doc, url, maxEnvelopeSize, winrmLocale, operationTimeout);
        return replacePlaceholder(doc, MESSAGE_ID_PLACEHOLDER_NAME, UUID_LABEL + UUID.randomUUID().toString());
    }

    private String createExecuteCommandRequestBody(String doc, String url, String shellId, String command, String maxEnvelopeSize, String winrmLocale, String operationTimeout) throws RuntimeException {
        doc = replaceCommonPlaceholders(doc, url, maxEnvelopeSize, winrmLocale, operationTimeout);
        doc = replacePlaceholder(doc, SHELL_ID_PLACEHOLDER_NAME, shellId);
        doc = replacePlaceholder(doc, MESSAGE_ID_PLACEHOLDER_NAME, UUID_LABEL + UUID.randomUUID().toString());
        return replacePlaceholder(doc, COMMAND_PLACEHOLDER_NAME, command);
    }

    private String createReceiveRequestBody(String doc, String url, String shellId, String commandId, String maxEnvelopeSize, String winrmLocale, String operationTimeout) throws RuntimeException {
        doc = replaceCommonPlaceholders(doc, url, maxEnvelopeSize, winrmLocale, operationTimeout);
        doc = replacePlaceholder(doc, SHELL_ID_PLACEHOLDER_NAME, shellId);
        doc = replacePlaceholder(doc, MESSAGE_ID_PLACEHOLDER_NAME, UUID_LABEL + UUID.randomUUID().toString());
        return replacePlaceholder(doc, COMMAND_ID_PLACEHOLDER_NAME, commandId);
    }

    private String createDeleteShellRequestBody(String doc, String url, String shellId, String maxEnvelopeSize, String winrmLocale, String operationTimeout) throws RuntimeException {
        doc = replaceCommonPlaceholders(doc, url, maxEnvelopeSize, winrmLocale, operationTimeout);
        doc = replacePlaceholder(doc, SHELL_ID_PLACEHOLDER_NAME, shellId);
        return replacePlaceholder(doc, MESSAGE_ID_PLACEHOLDER_NAME, UUID_LABEL + UUID.randomUUID().toString());
    }

    private String replaceCommonPlaceholders(String doc, String url, String maxEnvelopeSize, String winrmLocale, String operationTimeout) {
        doc = replacePlaceholder(doc, SERVICE_ADDRESS_PLACEHOLDER_NAME, url);
        doc = replacePlaceholder(doc, MAX_ENVELOPE_SIZE_PLACEHOLDER_NAME, maxEnvelopeSize);
        doc = replacePlaceholder(doc, WINRM_LOCALE_PLACEHOLDER_NAME, winrmLocale);
        return replacePlaceholder(doc, OPERATION_TIMEOUT_PLACEHOLDER_NAME, operationTimeout);
    }

    private String replacePlaceholder(String document, String placeholderName, String placeholderValue) throws RuntimeException {
        if (StringUtils.containsIgnoreCase(document, placeholderName)) {
            return StringUtils.replace(document, placeholderName, placeholderValue);
        } else {
            throw new RuntimeException(PLACEHOLDER_NOT_FOUND + placeholderName);
        }
    }

    public URL buildURL(String host, String port, String protocol, String resource) throws MalformedURLException {
        return new URL(protocol, host, Integer.parseInt(port), resource);
    }
}
