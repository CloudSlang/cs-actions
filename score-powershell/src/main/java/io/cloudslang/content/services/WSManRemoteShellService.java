package io.cloudslang.content.services;

import io.cloudslang.content.entities.EncoderDecoder;
import io.cloudslang.content.entities.WSManRequestInputs;
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
    private static final String STATUS_CODE = "statusCode";
    private static final String UNAUTHORIZED_STATUS_CODE = "401";

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
    private static final String POWERSHELL_SCRIPT_PREFIX = "PowerShell -NonInteractive -EncodedCommand";
    private static final String UNAUTHORIZED_EXCEPTION_MESSAGE = "Unauthorized! Service responded with 401 status code!";

    private long commandExecutionStartTime;

    /**
     * Executes a command on a remote shell by communicating with the WinRM server from the remote host.
     * Method creates a shell, runs a command on the shell, waits for the command execution to finnish, retrieves the result then deletes the shell.
     *
     * @param wsManRequestInputs
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
    public Map<String, String> runCommand(WSManRequestInputs wsManRequestInputs) throws RuntimeException, IOException, InterruptedException, ParserConfigurationException, TransformerException, XPathExpressionException, TimeoutException, URISyntaxException, SAXException {
        ScoreHttpClient scoreHttpClient = new ScoreHttpClient();
        HttpClientInputs httpClientInputs = new HttpClientInputs();
        URL url = buildURL(wsManRequestInputs, WSMAN_RESOURCE_URI);
        httpClientInputs = setCommonHttpInputs(httpClientInputs, url, wsManRequestInputs);
        String shellId = createShell(scoreHttpClient, httpClientInputs, CREATE_SHELL_REQUEST_XML, url.toString(), wsManRequestInputs);
        WSManUtils.validateUUID(shellId, SHELL_ID);
        String commandStr = POWERSHELL_SCRIPT_PREFIX + " " + EncoderDecoder.encodeStringInBase64(wsManRequestInputs.getScript(), Charsets.UTF_16LE);
        String commandId = executeCommand(scoreHttpClient, httpClientInputs, EXECUTE_COMMAND_REQUEST_XML, url.toString(), shellId, wsManRequestInputs, commandStr);
        WSManUtils.validateUUID(commandId, COMMAND_ID);
        Map<String, String> scriptResults = receiveCommandResult(scoreHttpClient, httpClientInputs, RECEIVE_REQUEST_XML, url.toString(), shellId, commandId, wsManRequestInputs);
        deleteShell(scoreHttpClient, httpClientInputs, DELETE_SHELL_REQUEST_XML, url.toString(), shellId, wsManRequestInputs);
        return scriptResults;
    }

    /**
     * Configures the HttpClientInputs object with the most common http parameters.
     *
     * @param httpClientInputs
     * @param url
     * @param wsManRequestInputs
     * @return the configured HttpClientInputs object.
     * @throws MalformedURLException
     */
    private static HttpClientInputs setCommonHttpInputs(HttpClientInputs httpClientInputs, URL url, WSManRequestInputs wsManRequestInputs) throws MalformedURLException {
        httpClientInputs.setUrl(url.toString());
        httpClientInputs.setAuthType(BASIC_AUTH_TYPE);
        httpClientInputs.setUsername(wsManRequestInputs.getUsername());
        httpClientInputs.setPassword(wsManRequestInputs.getPassword());
        httpClientInputs.setTrustAllRoots(wsManRequestInputs.getTrustAllRoots());
        httpClientInputs.setX509HostnameVerifier(wsManRequestInputs.getX509HostnameVerifier());
        httpClientInputs.setProxyHost(wsManRequestInputs.getProxyHost());
        httpClientInputs.setProxyPort(wsManRequestInputs.getProxyPort());
        httpClientInputs.setProxyUsername(wsManRequestInputs.getProxyUsername());
        httpClientInputs.setProxyPassword(wsManRequestInputs.getProxyPassword());
        httpClientInputs.setKeystore(wsManRequestInputs.getKeystore());
        httpClientInputs.setKeystorePassword(wsManRequestInputs.getKeystorePassword());
        httpClientInputs.setTrustKeystore(wsManRequestInputs.getTrustKeystore());
        httpClientInputs.setTrustPassword(wsManRequestInputs.getTrustPassword());
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
    private Map<String, String> executeRequest(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String requestMessage) {
        httpClientInputs.setBody(requestMessage);
        Map<String, String> requestResponse = scoreHttpClient.execute(httpClientInputs);
        if (UNAUTHORIZED_STATUS_CODE.equals(requestResponse.get(STATUS_CODE))) {
            throw new RuntimeException(UNAUTHORIZED_EXCEPTION_MESSAGE);
        }
        return requestResponse;
    }

    /**
     * Creates a shell on the remote server and returns the shell id.
     *
     * @param scoreHttpClient
     * @param httpClientInputs
     * @param resourceName
     * @param url
     * @param wsManRequestInputs
     * @return the id of the created shell.
     * @throws RuntimeException
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private String createShell(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName, String url,
                               WSManRequestInputs wsManRequestInputs) throws RuntimeException, IOException, URISyntaxException,
            TransformerException, XPathExpressionException, SAXException, ParserConfigurationException {
        String document = ResourceLoader.loadAsString(resourceName);
        document = createCreateShellRequestBody(document, url, String.valueOf(wsManRequestInputs.getMaxEnvelopeSize()),
                wsManRequestInputs.getWinrmLocale(), String.valueOf(wsManRequestInputs.getOperationTimeout()));
        Map<String, String> createShellResult = executeRequest(scoreHttpClient, httpClientInputs, document);
        return getResourceId(createShellResult.get(RETURN_RESULT), CREATE_RESPONSE_ACTION, CREATE_RESPONSE_SHELL_ID_XPATH,
                SHELL_ID_NOT_RETRIEVED);
    }

    /**
     * Executes a command on the given shell.
     *
     * @param scoreHttpClient
     * @param httpClientInputs
     * @param resourceName
     * @param url
     * @param shellId
     * @param wsManRequestInputs
     * @return the command id.
     * @throws RuntimeException
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private String executeCommand(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName,
                                  String url, String shellId, WSManRequestInputs wsManRequestInputs, String command) throws RuntimeException,
            IOException, URISyntaxException, TransformerException, XPathExpressionException, SAXException, ParserConfigurationException {
        String documentStr = ResourceLoader.loadAsString(resourceName);
        documentStr = createExecuteCommandRequestBody(documentStr, url, shellId, command, String.valueOf(wsManRequestInputs.getMaxEnvelopeSize()),
                wsManRequestInputs.getWinrmLocale(), String.valueOf(wsManRequestInputs.getOperationTimeout()));
        commandExecutionStartTime = System.currentTimeMillis() / 1000;
        Map<String, String> executeCommandResult = executeRequest(scoreHttpClient, httpClientInputs, documentStr);
        return getResourceId(executeCommandResult.get(RETURN_RESULT), COMMAND_RESPONSE_ACTION, COMMAND_RESULT_COMMAND_ID_XPATH,
                COMMAND_ID_NOT_RETRIEVED);
    }

    /**
     * Waits for a specific command that is running on a remote shell to finnish it's execution.
     *
     * @param scoreHttpClient
     * @param httpClientInputs
     * @param resourceName
     * @param url
     * @param shellId
     * @param commandId
     * @param wsManRequestInputs
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
    private Map<String, String> receiveCommandResult(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs,
                                                     String resourceName, String url, String shellId, String commandId,
                                                     WSManRequestInputs wsManRequestInputs) throws RuntimeException,
            IOException, URISyntaxException, TransformerException, TimeoutException, XPathExpressionException, SAXException,
            ParserConfigurationException, InterruptedException {
        String documentStr = ResourceLoader.loadAsString(resourceName);
        documentStr = createReceiveRequestBody(documentStr, url, shellId, commandId, String.valueOf(wsManRequestInputs.getMaxEnvelopeSize()), wsManRequestInputs.getWinrmLocale(), String.valueOf(wsManRequestInputs.getOperationTimeout()));
        Map<String, String> receiveResult;
        while (true) {
            receiveResult = executeRequest(scoreHttpClient, httpClientInputs, documentStr);
            if (executionIsTimedOut(commandExecutionStartTime, wsManRequestInputs.getOperationTimeout())) {
                throw new TimeoutException(EXECUTION_TIMED_OUT);
            } else if (WSManUtils.isSpecificResponseAction(receiveResult.get(RETURN_RESULT), RECEIVE_RESPONSE_ACTION) &&
                    WSManUtils.commandExecutionIsDone(receiveResult.get(RETURN_RESULT))) {
                return processCommandExecutionResponse(receiveResult);
            } else if (WSManUtils.isFaultResponse(receiveResult.get(RETURN_RESULT))) {
                throw new RuntimeException(WSManUtils.getResponseFault(receiveResult.get(RETURN_RESULT)));
            }

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw e;
            }
        }

    }

    /**
     * This method retrieves the resource id from the create resource request response and throws the appropriate exceptions in case of failure.
     *
     * @param response
     * @param resourceResponseAction
     * @param resourceIdXpath
     * @param resourceIdExceptionMessage
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws IOException
     */
    private String getResourceId(String response, String resourceResponseAction, String resourceIdXpath, String resourceIdExceptionMessage) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        if (WSManUtils.isSpecificResponseAction(response, resourceResponseAction)) {
            String shellId = XMLUtils.parseXml(response, resourceIdXpath);
            if (StringUtils.isNotBlank(shellId)) {
                return shellId;
            } else {
                throw new RuntimeException(resourceIdExceptionMessage);
            }
        } else if (WSManUtils.isFaultResponse(response)) {
            throw new RuntimeException(WSManUtils.getResponseFault(response));
        } else {
            throw new RuntimeException(UNEXPECTED_SERVICE_RESPONSE + response);
        }
    }

    private Map<String, String> processCommandExecutionResponse(Map<String, String> receiveResult) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        Map<String, String> scriptResults = new HashMap<>();
        scriptResults.put(RETURN_RESULT, buildResultFromResponseStreams(receiveResult.get(RETURN_RESULT)));
        scriptResults.put(Constants.OutputNames.SCRIPT_EXIT_CODE, WSManUtils.getScriptExitCode(receiveResult.get(RETURN_RESULT)));
        return scriptResults;
    }

    /**
     * Deletes the remote shell.
     *
     * @param scoreHttpClient
     * @param httpClientInputs
     * @param resourceName
     * @param url
     * @param shellId
     * @param wsManRequestInputs
     * @throws RuntimeException
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    private void deleteShell(ScoreHttpClient scoreHttpClient, HttpClientInputs httpClientInputs, String resourceName, String url,
                             String shellId, WSManRequestInputs wsManRequestInputs)
            throws RuntimeException, IOException, URISyntaxException, TransformerException, XPathExpressionException, SAXException, ParserConfigurationException {
        String documentStr = ResourceLoader.loadAsString(resourceName);
        documentStr = createDeleteShellRequestBody(documentStr, url, shellId, String.valueOf(wsManRequestInputs.getMaxEnvelopeSize()), wsManRequestInputs.getWinrmLocale(), String.valueOf(wsManRequestInputs.getOperationTimeout()));
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
     *
     * @param response
     * @return the decoded result of the command in a string.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws IOException
     */
    private String buildResultFromResponseStreams(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        StringBuilder commandResult = new StringBuilder();
        int noOfStreams = WSManUtils.countStreamElements(response);
        for (int streamNo = 1; streamNo <= noOfStreams; streamNo++) {
            String stream = XMLUtils.parseXml(response, String.format(RECEIVE_RESPONSE_XPATH, streamNo));
            if (!"DQo=".equals(stream)) {
                commandResult.append(EncoderDecoder.decodeBase64String(stream));
            }
        }
        return commandResult.toString();
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

    private URL buildURL(WSManRequestInputs wsManRequestInputs, String resource) throws MalformedURLException {
        return new URL(wsManRequestInputs.getProtocol(), wsManRequestInputs.getHost(), Integer.parseInt(wsManRequestInputs.getPort()), resource);
    }
}
