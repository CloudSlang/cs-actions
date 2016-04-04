package io.cloudslang.content.services;

import io.cloudslang.content.entities.EncoderDecoder;
import io.cloudslang.content.httpclient.HttpClientInputs;
import io.cloudslang.content.httpclient.ScoreHttpClient;
import io.cloudslang.content.utils.Constants;
import io.cloudslang.content.utils.ResourceLoader;
import io.cloudslang.content.utils.WSManUtils;
import io.cloudslang.content.utils.XMLUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpPost;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.TransformerException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by giloan on 3/27/2016.
 */
public class WSManService {

    private static final String CONTENT_TYPE_HEADER = "Content-Type:application/soap+xml;charset=UTF-8";
    private static final String BASIC_AUTH_TYPE = "Basic";
    private static final String WSMAN_RESOURCE_URI = "/wsman";
    private static final String NEW_LINE_SEPARATOR = "\\n";
    private static final String CREATE_RESPONSE_SHELL_ID_XPATH = "/Envelope/Body/ResourceCreated/ReferenceParameters/SelectorSet/Selector[@Name='ShellId']/text()";
    private static final String SERVICE_ADDRESS_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_SERVICE_NETWORK_ADDRESS";
    private static final String SHELL_ID_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_SHELL_ID";
    private static final String MESSAGE_ID_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_MESSAGE_ID";
    private static final String COMMAND_ID_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_COMMAND_ID";
    private static final String COMMAND_PLACEHOLDER_NAME = "$PLACEHOLDER_FOR_COMMAND";
    private static final String COMMAND_RESULT_COMMAND_ID_XPATH = "/Envelope/Body/CommandResponse/CommandId";
    private static final String UUID_LABEL = "uuid:";
    private static final String PLACEHOLDER_NOT_FOUND = "Resource does not contain the expected placeholder name: ";
    private static final String RECEIVE_RESPONSE_XPATH = "/Envelope/Body/ReceiveResponse/Stream[2]/text()";

    private ScoreHttpClient scoreHttpClient;

    public String runCommand(String host, String port, String protocol, String username, String password, String command) throws Exception {
        Map<String, String> resultMap = new HashMap<String, String>();
        scoreHttpClient = new ScoreHttpClient();
        HttpClientInputs httpClientInputs = new HttpClientInputs();
        URL url = buildURL(host, port, protocol, WSMAN_RESOURCE_URI);
        httpClientInputs = setCommonHttpInputs(httpClientInputs, url, username, password);
        String shellId = createShell(httpClientInputs, "CreateShell.xml", url.toString());
//        String encodedCommand = "powershell" + " " + "-NoProfile" + " " + "-NonInteractive" + " " + "-EncodedCommand" + " " + EncoderDecoder.encodeStringInBase64(command);
        String commandStr = "powershell" + " " + command;
        String commandId = executeCommand(httpClientInputs, "ExecuteCommand.xml", url.toString(), shellId, commandStr);
        String commandResult = receiveCommandResult(httpClientInputs, "Receive.xml", url.toString(), shellId, commandId);
        return EncoderDecoder.decodeBase64String(commandResult);
    }

    public HttpClientInputs setCommonHttpInputs(HttpClientInputs httpClientInputs, URL url, String username, String password) throws MalformedURLException {
        httpClientInputs.setUrl(url.toString());
        httpClientInputs.setAuthType(BASIC_AUTH_TYPE);
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);
        String headers = httpClientInputs.getHeaders();
        if (StringUtils.isEmpty(headers)) {
            httpClientInputs.setHeaders(CONTENT_TYPE_HEADER);
        } else {
            httpClientInputs.setHeaders(headers + NEW_LINE_SEPARATOR + CONTENT_TYPE_HEADER);
        }
        return httpClientInputs;
    }

    public Map<String, String> executeRequest(HttpClientInputs httpClientInputs, String requestMessage) throws TransformerException {
        httpClientInputs.setBody(requestMessage);
        return scoreHttpClient.execute(httpClientInputs);
    }

    public String createShell(HttpClientInputs httpClientInputs, String resourceName, String url) throws Exception {
        String document = ResourceLoader.loadAsString(resourceName);
        document = getCreateShellRequestBody(document, url);
        httpClientInputs.setMethod(HttpPost.METHOD_NAME);
        Map<String, String> createShellResult = executeRequest(httpClientInputs, document);
        if (WSManUtils.isCreateResponse(createShellResult.get(Constants.OutputNames.RETURN_RESULT))) {
            return XMLUtils.parseXml(createShellResult.get(Constants.OutputNames.RETURN_RESULT), CREATE_RESPONSE_SHELL_ID_XPATH);
        } else {
            throw new Exception(WSManUtils.getResponseFault(createShellResult.get(Constants.OutputNames.RETURN_RESULT)));
        }
    }

    public String executeCommand(HttpClientInputs httpClientInputs, String resourceName, String url, String shellId, String command) throws Exception {
        String documentStr = ResourceLoader.loadAsString(resourceName);
        documentStr = getExecuteCommandRequestBody(documentStr, url, shellId, command);
        httpClientInputs.setMethod(HttpPost.METHOD_NAME);
        Map<String, String> executeCommandResult = executeRequest(httpClientInputs, documentStr);
        if (WSManUtils.isCommandResponse(executeCommandResult.get(Constants.OutputNames.RETURN_RESULT))) {
            return XMLUtils.parseXml(executeCommandResult.get(Constants.OutputNames.RETURN_RESULT), COMMAND_RESULT_COMMAND_ID_XPATH);
        } else {
            throw new Exception(WSManUtils.getResponseFault(executeCommandResult.get(Constants.OutputNames.RETURN_RESULT)));
        }
    }

    private String receiveCommandResult(HttpClientInputs httpClientInputs, String resourceName, String url, String shellId, String commandId) throws Exception {
        String documentStr = ResourceLoader.loadAsString(resourceName);
        documentStr = getReceiveRequestBody(documentStr, url, shellId, commandId);
        httpClientInputs.setMethod(HttpPost.METHOD_NAME);
        Map<String, String> receiveResult;
        do {
            //TODO insert sync behaviour
            receiveResult = executeRequest(httpClientInputs, documentStr);
        } while (WSManUtils.isReceiveResult(receiveResult.get(Constants.OutputNames.RETURN_RESULT)) &&
                WSManUtils.commandExecutionIsInProgress(receiveResult.get(Constants.OutputNames.RETURN_RESULT)));

        if (WSManUtils.isReceiveResult(receiveResult.get(Constants.OutputNames.RETURN_RESULT))) { //todo parse all streams from the receiveResponse and concatente them
            return XMLUtils.parseXml(receiveResult.get(Constants.OutputNames.RETURN_RESULT), RECEIVE_RESPONSE_XPATH);
        } else {
            throw new Exception(WSManUtils.getResponseFault(receiveResult.get(Constants.OutputNames.RETURN_RESULT)));
        }
    }

    public String getCreateShellRequestBody(String doc, String url) throws Exception {
        String document = replacePlaceholder(doc, SERVICE_ADDRESS_PLACEHOLDER_NAME, url);
        document = replacePlaceholder(document, MESSAGE_ID_PLACEHOLDER_NAME, UUID_LABEL + UUID.randomUUID().toString());
        return document;
    }

    public String getExecuteCommandRequestBody(String doc, String url, String shellId, String command) throws Exception {
        String document = replacePlaceholder(doc, SERVICE_ADDRESS_PLACEHOLDER_NAME, url);
        document = replacePlaceholder(document, SHELL_ID_PLACEHOLDER_NAME, shellId);
        document = replacePlaceholder(document, MESSAGE_ID_PLACEHOLDER_NAME, UUID_LABEL + UUID.randomUUID().toString());
        document = replacePlaceholder(document, COMMAND_PLACEHOLDER_NAME, command);
        return document;
    }

    private String getReceiveRequestBody(String doc, String url, String shellId, String commandId) throws Exception {
        String document = replacePlaceholder(doc, SERVICE_ADDRESS_PLACEHOLDER_NAME, url);
        document = replacePlaceholder(document, SHELL_ID_PLACEHOLDER_NAME, shellId);
        document = replacePlaceholder(document, MESSAGE_ID_PLACEHOLDER_NAME, UUID_LABEL + UUID.randomUUID().toString());
        document = replacePlaceholder(document, COMMAND_ID_PLACEHOLDER_NAME, commandId);
        return document;
    }

    private String replacePlaceholder(String document, String placeholderName, String placeholderValue) throws Exception {
        if (StringUtils.containsIgnoreCase(document, placeholderName)) {
            return StringUtils.replace(document, placeholderName, placeholderValue);
        } else {
            throw new Exception(PLACEHOLDER_NOT_FOUND + placeholderName);
        }
    }

    public URL buildURL(String host, String port, String protocol, String resource) throws MalformedURLException {
        return new URL(protocol, host, Integer.parseInt(port), resource);
    }
}
