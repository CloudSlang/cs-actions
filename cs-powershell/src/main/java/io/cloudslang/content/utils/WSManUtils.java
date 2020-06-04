/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



package io.cloudslang.content.utils;

import io.cloudslang.content.entities.EncoderDecoder;
import io.cloudslang.content.entities.WSManRequestInputs;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static io.cloudslang.content.constants.OtherValues.COMMA_DELIMITER;
import static io.cloudslang.content.utils.Constants.Others.*;
import static io.cloudslang.content.utils.Constants.OutputNames.*;
import static io.cloudslang.content.utils.Constants.ReturnCodes.*;
import static org.apache.commons.lang3.StringUtils.*;

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

    public static String constructCommand(final WSManRequestInputs wsManRequestInputs) {
        final StringBuilder command = new StringBuilder()
                .append(POWERSHELL_SCRIPT_PREFIX)
                .append(SPACE)
                .append(NON_INTERACTIVE_PARAMETER)
                .append(SPACE);

        if(StringUtils.isNotBlank(wsManRequestInputs.getConfigurationName())) {
            command.append(CONFIGURATION_NAME_PARAMETER)
                    .append(SPACE)
                    .append(wsManRequestInputs.getConfigurationName())
                    .append(SPACE);
        }

        command.append(ENCODED_COMMAND_PARAMETER)
                .append(SPACE);

        if (StringUtilities.isNotBlank(wsManRequestInputs.getModules())) {
            command.append(EncoderDecoder.encodeStringInBase64(
                    IMPORT_MODULE_PARAMETER +
                            SPACE +
                            getEscapedModules(wsManRequestInputs) +
                            LF +
                            wsManRequestInputs.getScript(), Charsets.UTF_16LE));
        } else {
            command.append(EncoderDecoder.encodeStringInBase64(wsManRequestInputs.getScript(), Charsets.UTF_16LE));
        }
        return command.toString();
    }

    private static String getEscapedModules(final WSManRequestInputs wsManRequestInputs) {
        final String[] modules = StringUtilities.split(wsManRequestInputs.getModules(), COMMA_DELIMITER);
        final List<String> escapedModules = new ArrayList<>();
        for (String module : modules) {
            module = StringUtilities.trim(module);
            module = StringUtilities.prependIfMissing(module, SINGLE_QUOTE);
            module = StringUtilities.appendIfMissing(module, SINGLE_QUOTE);
            escapedModules.add(module);
        }
        return StringUtilities.join(escapedModules, COMMA_DELIMITER);
    }

    /**
     * Checks the scriptExitCode value of the script execution and fails the operation if exit code is different than zero.
     * @param resultMap
     */
    public static void verifyScriptExecutionStatus(final Map<String, String> resultMap) {
        if (ZERO_SCRIPT_EXIT_CODE.equals(resultMap.get(SCRIPT_EXIT_CODE))) {
            resultMap.put(RETURN_CODE, RETURN_CODE_SUCCESS);
        } else {
            resultMap.put(RETURN_CODE, RETURN_CODE_FAILURE);
        }
    }
}
