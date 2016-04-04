package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;

/**
 * Created by giloan on 3/29/2016.
 */
public class WSManUtils {

    private static final String HEADER_XPATH = "/Envelope/Header";
    private static final String CREATE_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2004/09/transfer/CreateResponse";
    private static final String COMMAND_RESPONSE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandResponse";
    private static final String FAULT_DETAIL = "/Envelope/Body/Fault/Detail/FaultDetail";
    private static final String FAULT_REASON = "/Envelope/Body/Fault/Reason";
    private static final String RECEIVE_RESPONSE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/ReceiveResponse";
    private static final String COMMAND_STATE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Done";
    private static final String COMMAND_STATE_XPATH = "/Envelope/Body/ReceiveResponse/CommandState";

    public static boolean isCreateResponse(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return StringUtils.containsIgnoreCase(XMLUtils.parseXml(response, HEADER_XPATH), CREATE_RESPONSE_ACTION);
    }

    public static boolean isCommandResponse(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return StringUtils.containsIgnoreCase(XMLUtils.parseXml(response, HEADER_XPATH), COMMAND_RESPONSE_ACTION);
    }

    public static boolean isReceiveResult(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return StringUtils.containsIgnoreCase(XMLUtils.parseXml(response, HEADER_XPATH), RECEIVE_RESPONSE_ACTION);
    }

    public static String getResponseFault(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return getResponseFaultReason(response) + getResponseFaultDetail(response);
    }

    private static String getResponseFaultDetail(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return XMLUtils.parseXml(response, FAULT_DETAIL);
    }

    private static String getResponseFaultReason(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return XMLUtils.parseXml(response, FAULT_REASON);
    }

    public static boolean commandExecutionIsInProgress(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return !StringUtils.containsIgnoreCase(XMLUtils.parseXml(response, COMMAND_STATE_XPATH), COMMAND_STATE_ACTION);
    }
}
