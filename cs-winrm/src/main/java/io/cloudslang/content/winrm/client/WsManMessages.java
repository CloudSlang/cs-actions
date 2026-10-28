/*
 * Copyright 2026-2027 Open Text
 * This program and the accompanying materials
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

package io.cloudslang.content.winrm.client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Builds and parses the WS-Management (MS-WSMV) SOAP messages used by the Windows Remote Shell.
 */
final class WsManMessages {

    static final String NS_SOAP = "http://www.w3.org/2003/05/soap-envelope";
    static final String NS_ADDRESSING = "http://schemas.xmlsoap.org/ws/2004/08/addressing";
    static final String NS_WSMAN = "http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd";
    static final String NS_SHELL = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell";

    static final String RESOURCE_URI = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/cmd";
    static final int MAX_ENVELOPE_SIZE = 153600;

    static final String ACTION_CREATE = "http://schemas.xmlsoap.org/ws/2004/09/transfer/Create";
    static final String ACTION_DELETE = "http://schemas.xmlsoap.org/ws/2004/09/transfer/Delete";
    static final String ACTION_COMMAND = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Command";
    static final String ACTION_RECEIVE = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Receive";
    static final String ACTION_SIGNAL = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/Signal";

    static final String SIGNAL_TERMINATE = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/signal/terminate";
    static final String COMMAND_STATE_DONE = "http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Done";

    private static final String ANONYMOUS = "http://schemas.xmlsoap.org/ws/2004/08/addressing/role/anonymous";

    private final String endpoint;
    private final String locale;
    private final String operationTimeout;

    WsManMessages(final String endpoint, final String locale, final long operationTimeoutMillis) {
        this.endpoint = endpoint;
        this.locale = locale;
        this.operationTimeout = toDuration(operationTimeoutMillis);
    }

    String create(final String workingDirectory, final Map<String, String> environment) {
        final Map<String, String> options = new LinkedHashMap<>();
        options.put("WINRS_NOPROFILE", "FALSE");
        options.put("WINRS_CODEPAGE", "437");

        final StringBuilder body = new StringBuilder("<rsp:Shell>");
        if (environment != null && !environment.isEmpty()) {
            body.append("<rsp:Environment>");
            for (Map.Entry<String, String> variable : environment.entrySet()) {
                body.append("<rsp:Variable Name=\"").append(escape(variable.getKey())).append("\">")
                        .append(escape(variable.getValue())).append("</rsp:Variable>");
            }
            body.append("</rsp:Environment>");
        }
        if (workingDirectory != null) {
            body.append("<rsp:WorkingDirectory>").append(escape(workingDirectory)).append("</rsp:WorkingDirectory>");
        }
        body.append("<rsp:InputStreams>stdin</rsp:InputStreams>")
                .append("<rsp:OutputStreams>stdout stderr</rsp:OutputStreams>")
                .append("</rsp:Shell>");
        return envelope(ACTION_CREATE, null, options, body.toString());
    }

    String command(final String shellId, final String command, final List<String> arguments, final Boolean skipCommandShell) {
        final Map<String, String> options = new LinkedHashMap<>();
        options.put("WINRS_CONSOLEMODE_STDIN", "TRUE");
        if (skipCommandShell != null) {
            options.put("WINRS_SKIP_CMD_SHELL", skipCommandShell.toString().toUpperCase(java.util.Locale.ROOT));
        }

        final StringBuilder body = new StringBuilder("<rsp:CommandLine><rsp:Command>").append(escape(command)).append("</rsp:Command>");
        if (arguments != null) {
            for (String argument : arguments) {
                body.append("<rsp:Arguments>").append(escape(argument)).append("</rsp:Arguments>");
            }
        }
        body.append("</rsp:CommandLine>");
        return envelope(ACTION_COMMAND, shellId, options, body.toString());
    }

    String receive(final String shellId, final String commandId) {
        return envelope(ACTION_RECEIVE, shellId, null,
                "<rsp:Receive><rsp:DesiredStream CommandId=\"" + escape(commandId) + "\">stdout stderr</rsp:DesiredStream></rsp:Receive>");
    }

    String signal(final String shellId, final String commandId, final String code) {
        return envelope(ACTION_SIGNAL, shellId, null,
                "<rsp:Signal CommandId=\"" + escape(commandId) + "\"><rsp:Code>" + escape(code) + "</rsp:Code></rsp:Signal>");
    }

    String delete(final String shellId) {
        return envelope(ACTION_DELETE, shellId, null, "");
    }

    private String envelope(final String action, final String shellId, final Map<String, String> options, final String body) {
        final StringBuilder xml = new StringBuilder(1024)
                .append("<s:Envelope xmlns:s=\"").append(NS_SOAP).append("\" xmlns:a=\"").append(NS_ADDRESSING)
                .append("\" xmlns:w=\"").append(NS_WSMAN).append("\" xmlns:rsp=\"").append(NS_SHELL).append("\">")
                .append("<s:Header>")
                .append("<a:To>").append(escape(endpoint)).append("</a:To>")
                .append("<a:ReplyTo><a:Address s:mustUnderstand=\"true\">").append(ANONYMOUS).append("</a:Address></a:ReplyTo>")
                .append("<a:Action s:mustUnderstand=\"true\">").append(action).append("</a:Action>")
                .append("<a:MessageID>uuid:").append(UUID.randomUUID().toString().toUpperCase()).append("</a:MessageID>")
                .append("<w:ResourceURI s:mustUnderstand=\"true\">").append(RESOURCE_URI).append("</w:ResourceURI>")
                .append("<w:MaxEnvelopeSize s:mustUnderstand=\"true\">").append(MAX_ENVELOPE_SIZE).append("</w:MaxEnvelopeSize>")
                .append("<w:OperationTimeout>").append(operationTimeout).append("</w:OperationTimeout>")
                .append("<w:Locale xml:lang=\"").append(escape(locale)).append("\" s:mustUnderstand=\"false\"/>");
        if (shellId != null) {
            xml.append("<w:SelectorSet><w:Selector Name=\"ShellId\">").append(escape(shellId)).append("</w:Selector></w:SelectorSet>");
        }
        if (options != null && !options.isEmpty()) {
            xml.append("<w:OptionSet>");
            for (Map.Entry<String, String> option : options.entrySet()) {
                xml.append("<w:Option Name=\"").append(escape(option.getKey())).append("\">")
                        .append(escape(option.getValue())).append("</w:Option>");
            }
            xml.append("</w:OptionSet>");
        }
        xml.append("</s:Header><s:Body>").append(body).append("</s:Body></s:Envelope>");
        return xml.toString();
    }

    static String getShellId(final Document response) {
        for (Element selector : elements(response, NS_WSMAN, "Selector")) {
            if ("ShellId".equals(selector.getAttribute("Name")) && !selector.getTextContent().trim().isEmpty()) {
                return selector.getTextContent().trim();
            }
        }
        final List<Element> shellIds = elements(response, NS_SHELL, "ShellId");
        if (!shellIds.isEmpty() && !shellIds.get(0).getTextContent().trim().isEmpty()) {
            return shellIds.get(0).getTextContent().trim();
        }
        throw new IllegalStateException("Shell ID not found in the WinRM Create response");
    }

    static String getCommandId(final Document response) {
        final List<Element> commandIds = elements(response, NS_SHELL, "CommandId");
        if (commandIds.isEmpty() || commandIds.get(0).getTextContent().trim().isEmpty()) {
            throw new IllegalStateException("Command ID not found in the WinRM Command response");
        }
        return commandIds.get(0).getTextContent().trim();
    }

    static List<Element> elements(final Document document, final String namespace, final String localName) {
        final NodeList nodes = document.getElementsByTagNameNS(namespace, localName);
        final List<Element> result = new ArrayList<>(nodes.getLength());
        for (int i = 0; i < nodes.getLength(); i++) {
            result.add((Element) nodes.item(i));
        }
        return result;
    }

    static Element firstChild(final Element parent, final String namespace, final String localName) {
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element && localName.equals(child.getLocalName()) && namespace.equals(child.getNamespaceURI())) {
                return (Element) child;
            }
        }
        return null;
    }

    /**
     * Returns the SOAP fault contained in the document, or {@code null} if the document is not a fault.
     */
    static WinRmFaultException getFault(final Document document) {
        final List<Element> faults = elements(document, NS_SOAP, "Fault");
        if (faults.isEmpty()) {
            return null;
        }
        final Element fault = faults.get(0);
        String code = null;
        String message = null;
        final NodeList reasons = fault.getElementsByTagNameNS(NS_SOAP, "Text");
        if (reasons.getLength() > 0) {
            message = reasons.item(0).getTextContent().trim();
        }
        final NodeList wsManFaults = fault.getElementsByTagNameNS("*", "WSManFault");
        if (wsManFaults.getLength() > 0) {
            final Element wsManFault = (Element) wsManFaults.item(0);
            code = wsManFault.hasAttribute("Code") ? wsManFault.getAttribute("Code") : null;
            final NodeList messages = wsManFault.getElementsByTagNameNS("*", "Message");
            if ((message == null || message.isEmpty()) && messages.getLength() > 0) {
                message = messages.item(0).getTextContent().trim();
            }
        }
        if (message == null || message.isEmpty()) {
            message = "WinRM SOAP fault";
        }
        return new WinRmFaultException(message, code);
    }

    static Document parse(final byte[] xml) {
        try {
            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            final DocumentBuilder builder = factory.newDocumentBuilder();
            // report problems through exceptions only, instead of printing them to stderr
            builder.setErrorHandler(new DefaultHandler());
            return builder.parse(new InputSource(new ByteArrayInputStream(xml)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new WinRmException("Unable to parse the WinRM response: " + e.getMessage(), e);
        }
    }

    static String toDuration(final long operationTimeoutMillis) {
        final BigDecimal seconds = BigDecimal.valueOf(operationTimeoutMillis).divide(BigDecimal.valueOf(1000));
        return new DecimalFormat("PT#.###S", new DecimalFormatSymbols(java.util.Locale.ROOT)).format(seconds);
    }

    static String escape(final String value) {
        final StringBuilder escaped = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            final char c = value.charAt(i);
            switch (c) {
                case '&':
                    escaped.append("&amp;");
                    break;
                case '<':
                    escaped.append("&lt;");
                    break;
                case '>':
                    escaped.append("&gt;");
                    break;
                case '"':
                    escaped.append("&quot;");
                    break;
                case '\'':
                    escaped.append("&apos;");
                    break;
                case '\r':
                    escaped.append("&#xD;");
                    break;
                default:
                    escaped.append(c);
            }
        }
        return escaped.toString();
    }
}
