package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by giloan on 3/29/2016.
 */
public class WSManUtils {

    private static final String HEADER_XPATH = "/Envelope/Header";
    private static final String FAULT_DETAIL_XPATH = "/Envelope/Body/Fault/Detail/WSManFault/Message";
    private static final String FAULT_REASON_XPATH = "/Envelope/Body/Fault/Reason";
    private static final String COMMAND_STATE_XPATH = "/Envelope/Body/ReceiveResponse/CommandState/@State";
    private static final String COUNT_STREAMS_XPATH = "count(//Envelope/Body/ReceiveResponse/Stream)";
    private static final String SCRIPT_EXIT_CODE_XPATH = "/Envelope/Body/ReceiveResponse/CommandState/ExitCode";

    private static final String DONE_COMMAND_STATE_ACTION = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Done";
    private static final String WSMAN_FAULT_RESPONSE_ACTION = "http://schemas.dmtf.org/wbem/wsman/1/wsman/fault";
    private static final String ADDRESSING_FAULT_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2004/08/addressing/fault";

    private WSManUtils() {
    }

    public static boolean isSpecificResponseAction(String response, String responseType) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return StringUtils.isNotEmpty(response) && StringUtils.containsIgnoreCase(XMLUtils.parseXml(response, HEADER_XPATH), responseType);
    }

    public static String getResponseFault(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return getResponseFaultReason(response) + getResponseFaultDetail(response);
    }

    private static String getResponseFaultDetail(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return XMLUtils.parseXml(response, FAULT_DETAIL_XPATH);
    }

    private static String getResponseFaultReason(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return XMLUtils.parseXml(response, FAULT_REASON_XPATH);
    }

    public static boolean commandExecutionIsDone(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return StringUtils.containsIgnoreCase(XMLUtils.parseXml(response, COMMAND_STATE_XPATH), DONE_COMMAND_STATE_ACTION);
    }

    public static int countStreamElements(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return Integer.parseInt(XMLUtils.parseXml(response, COUNT_STREAMS_XPATH));
    }

    public static boolean isFaultResponse(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return StringUtils.isNotEmpty(response) &&
                (isSpecificResponseAction(response, WSMAN_FAULT_RESPONSE_ACTION) || isSpecificResponseAction(response, ADDRESSING_FAULT_RESPONSE_ACTION));
    }

    public static String getScriptExitCode(String response) throws ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        return XMLUtils.parseXml(response, SCRIPT_EXIT_CODE_XPATH);
    }

    /**
     * Checks if a string is a valid UUID or not.
     *
     * @param string The UUID value.
     * @return true if input is a valid UUID or false if input is empty or an invalid UUID format.
     */
    public static boolean isUUID(String string) {
        try {
            if (string != null) {
                UUID.fromString(string);
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Validates a UUID value and throws a specific exception if UUID is invalid.
     *
     * @param uuid        The UUID value to validate.
     * @param uuidValueOf The property associated to the given UUID value.
     * @throws RuntimeException
     */
    public static void validateUUID(String uuid, String uuidValueOf) throws RuntimeException {
        if (!WSManUtils.isUUID(uuid)) {
            throw new RuntimeException("The returned " + uuidValueOf + " is not a valid UUID value! " + uuidValueOf + ": " + uuid);
        }
    }
}