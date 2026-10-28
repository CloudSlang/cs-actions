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

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WsManMessagesTest {

    private static final String NAMESPACES = "xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" " +
            "xmlns:w=\"http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd\" " +
            "xmlns:rsp=\"http://schemas.microsoft.com/wbem/wsman/1/windows/shell\" " +
            "xmlns:f=\"http://schemas.microsoft.com/wbem/wsman/1/wsmanfault\"";

    @Test
    public void operationTimeout() {
        assertEquals("PT60S", WsManMessages.toDuration(60_000));
        assertEquals("PT1.5S", WsManMessages.toDuration(1_500));
        assertEquals("PT0.001S", WsManMessages.toDuration(1));
        assertEquals("PT3600S", WsManMessages.toDuration(3_600_000));
    }

    @Test
    public void createMessage() {
        final Map<String, String> environment = new LinkedHashMap<>();
        environment.put("A", "1&2");
        final String xml = new WsManMessages("http://host:5985/wsman", "en-US", 60_000)
                .create("C:\\Temp <dir>", environment);
        final Document document = WsManMessages.parse(xml.getBytes(StandardCharsets.UTF_8));

        assertEquals(WsManMessages.ACTION_CREATE, text(document, WsManMessages.NS_ADDRESSING, "Action"));
        assertEquals("http://host:5985/wsman", text(document, WsManMessages.NS_ADDRESSING, "To"));
        assertTrue(text(document, WsManMessages.NS_ADDRESSING, "MessageID").startsWith("uuid:"));
        assertEquals(WsManMessages.RESOURCE_URI, text(document, WsManMessages.NS_WSMAN, "ResourceURI"));
        assertEquals("153600", text(document, WsManMessages.NS_WSMAN, "MaxEnvelopeSize"));
        assertEquals("PT60S", text(document, WsManMessages.NS_WSMAN, "OperationTimeout"));
        assertEquals("en-US", WsManMessages.elements(document, WsManMessages.NS_WSMAN, "Locale").get(0)
                .getAttributeNS("http://www.w3.org/XML/1998/namespace", "lang"));
        assertEquals("C:\\Temp <dir>", text(document, WsManMessages.NS_SHELL, "WorkingDirectory"));
        assertEquals("1&2", text(document, WsManMessages.NS_SHELL, "Variable"));
        assertEquals("stdin", text(document, WsManMessages.NS_SHELL, "InputStreams"));
        assertEquals("stdout stderr", text(document, WsManMessages.NS_SHELL, "OutputStreams"));
        assertEquals("WINRS_NOPROFILE", WsManMessages.elements(document, WsManMessages.NS_WSMAN, "Option").get(0).getAttribute("Name"));
        assertEquals("437", WsManMessages.elements(document, WsManMessages.NS_WSMAN, "Option").get(1).getTextContent());
        assertTrue(WsManMessages.elements(document, WsManMessages.NS_WSMAN, "SelectorSet").isEmpty());
    }

    @Test
    public void createMessageWithoutWorkingDirectory() {
        final String xml = new WsManMessages("http://host:5985/wsman", "en-US", 60_000).create(null, Collections.emptyMap());
        final Document document = WsManMessages.parse(xml.getBytes(StandardCharsets.UTF_8));
        assertTrue(WsManMessages.elements(document, WsManMessages.NS_SHELL, "WorkingDirectory").isEmpty());
        assertTrue(WsManMessages.elements(document, WsManMessages.NS_SHELL, "Environment").isEmpty());
    }

    @Test
    public void commandMessage() {
        final String xml = new WsManMessages("http://host:5985/wsman", "en-US", 60_000)
                .command("SHELL-1", "powershell", Arrays.asList("-encodedcommand", "a\"b<c>"), Boolean.FALSE);
        final Document document = WsManMessages.parse(xml.getBytes(StandardCharsets.UTF_8));
        assertEquals(WsManMessages.ACTION_COMMAND, text(document, WsManMessages.NS_ADDRESSING, "Action"));
        assertEquals("SHELL-1", text(document, WsManMessages.NS_WSMAN, "Selector"));
        assertEquals("powershell", text(document, WsManMessages.NS_SHELL, "Command"));
        assertEquals("a\"b<c>", WsManMessages.elements(document, WsManMessages.NS_SHELL, "Arguments").get(1).getTextContent());
        assertEquals("FALSE", WsManMessages.elements(document, WsManMessages.NS_WSMAN, "Option").get(1).getTextContent());
    }

    @Test
    public void commandLineBreaksArePreserved() {
        final String xml = new WsManMessages("http://host:5985/wsman", "en-US", 60_000)
                .command("SHELL-1", "echo 1\r\necho 2", null, Boolean.FALSE);
        final Document document = WsManMessages.parse(xml.getBytes(StandardCharsets.UTF_8));
        assertEquals("echo 1\r\necho 2", text(document, WsManMessages.NS_SHELL, "Command"));
    }

    @Test
    public void parseShellAndCommandIds() {
        final Document created = WsManMessages.parse(("<s:Envelope " + NAMESPACES + "><s:Body><x:ResourceCreated " +
                "xmlns:x=\"http://schemas.xmlsoap.org/ws/2004/09/transfer\"><w:SelectorSet><w:Selector Name=\"ShellId\">" +
                " 11-22 </w:Selector></w:SelectorSet></x:ResourceCreated></s:Body></s:Envelope>").getBytes(StandardCharsets.UTF_8));
        assertEquals("11-22", WsManMessages.getShellId(created));

        final Document command = WsManMessages.parse(("<s:Envelope " + NAMESPACES + "><s:Body><rsp:CommandResponse>" +
                "<rsp:CommandId>CMD-1</rsp:CommandId></rsp:CommandResponse></s:Body></s:Envelope>").getBytes(StandardCharsets.UTF_8));
        assertEquals("CMD-1", WsManMessages.getCommandId(command));
    }

    @Test
    public void parseFault() {
        final Document document = WsManMessages.parse(fault("2150858793", "The WS-Management service cannot complete the operation.")
                .getBytes(StandardCharsets.UTF_8));
        final WinRmFaultException fault = WsManMessages.getFault(document);
        assertEquals("2150858793", fault.getFaultCode());
        assertEquals("The WS-Management service cannot complete the operation.", fault.getMessage());

        assertNull(WsManMessages.getFault(WsManMessages.parse(("<s:Envelope " + NAMESPACES + "><s:Body/></s:Envelope>")
                .getBytes(StandardCharsets.UTF_8))));
    }

    @Test
    public void doctypeIsRejected() {
        assertThrows(WinRmException.class, () ->
                WsManMessages.parse(("<!DOCTYPE x [<!ENTITY e SYSTEM \"file:///etc/passwd\">]><x>&e;</x>")
                        .getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    public void readReceiveResponse() {
        final ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
        final ByteArrayOutputStream stdErr = new ByteArrayOutputStream();

        assertNull(WinRmClient.readReceiveResponse(WsManMessages.parse(receive("hello ", "", false, 0)
                .getBytes(StandardCharsets.UTF_8)), stdOut, stdErr));
        assertEquals(Integer.valueOf(-1), WinRmClient.readReceiveResponse(WsManMessages.parse(receive("world", "oops", true, 4294967295L)
                .getBytes(StandardCharsets.UTF_8)), stdOut, stdErr));

        assertEquals("hello world", stdOut.toString());
        assertEquals("oops", stdErr.toString());
    }

    @Test
    public void endpointUrl() {
        assertEquals("http://host:5985/wsman", WinRmClient.builder("host", "u", "p").port(5985).getEndpointUrl());
        assertEquals("https://host:5986/wsman", WinRmClient.builder("host", "u", "p").port(5986).useHttps(true).getEndpointUrl());
        assertEquals("https://host:1234/wsman", WinRmClient.builder("host:1234", "u", "p").port(5986).useHttps(true).getEndpointUrl());
        assertEquals("http://[fe80::1]:5985/wsman", WinRmClient.builder("fe80::1", "u", "p").port(5985).getEndpointUrl());
        assertEquals("http://10.0.0.1:5985/wsman", WinRmClient.builder("10.0.0.1", "u", "p").port(5985).getEndpointUrl());
        assertEquals("https://host/custom", WinRmClient.builder("https://host/custom", "u", "p").useHttps(true).getEndpointUrl());
    }

    @Test
    public void endpointUrlProtocolMismatch() {
        assertThrows(IllegalArgumentException.class,
                () -> WinRmClient.builder("http://host/wsman", "u", "p").useHttps(true).getEndpointUrl());
    }

    @Test
    public void authenticationSchemes() {
        assertEquals(AuthenticationScheme.BASIC, AuthenticationScheme.fromString("basic"));
        assertEquals(AuthenticationScheme.NTLM, AuthenticationScheme.fromString("NTLM"));
        assertEquals(AuthenticationScheme.KERBEROS, AuthenticationScheme.fromString("Kerberos"));
    }

    @Test
    public void unknownAuthenticationScheme() {
        assertThrows(UnsupportedOperationException.class, () -> AuthenticationScheme.fromString("Digest"));
    }

    static String fault(final String code, final String message) {
        return "<s:Envelope " + NAMESPACES + "><s:Body><s:Fault><s:Code><s:Value>s:Receiver</s:Value></s:Code>" +
                "<s:Reason><s:Text xml:lang=\"en-US\">" + message + "</s:Text></s:Reason><s:Detail>" +
                "<f:WSManFault Code=\"" + code + "\" Machine=\"host\"><f:Message>" + message + "</f:Message></f:WSManFault>" +
                "</s:Detail></s:Fault></s:Body></s:Envelope>";
    }

    static String receive(final String stdOut, final String stdErr, final boolean done, final long exitCode) {
        final Base64.Encoder encoder = Base64.getEncoder();
        return "<s:Envelope " + NAMESPACES + "><s:Body><rsp:ReceiveResponse>" +
                "<rsp:Stream Name=\"stdout\" CommandId=\"CMD-1\">" + encoder.encodeToString(stdOut.getBytes()) + "</rsp:Stream>" +
                "<rsp:Stream Name=\"stderr\" CommandId=\"CMD-1\">" + encoder.encodeToString(stdErr.getBytes()) + "</rsp:Stream>" +
                (done
                        ? "<rsp:Stream Name=\"stdout\" CommandId=\"CMD-1\" End=\"true\"></rsp:Stream>" +
                        "<rsp:CommandState CommandId=\"CMD-1\" State=\"" + WsManMessages.COMMAND_STATE_DONE + "\">" +
                        "<rsp:ExitCode>" + exitCode + "</rsp:ExitCode></rsp:CommandState>"
                        : "<rsp:CommandState CommandId=\"CMD-1\" State=\"http://schemas.microsoft.com/wbem/wsman/1/windows/shell/CommandState/Running\"/>") +
                "</rsp:ReceiveResponse></s:Body></s:Envelope>";
    }

    private static String text(final Document document, final String namespace, final String localName) {
        return WsManMessages.elements(document, namespace, localName).get(0).getTextContent();
    }
}
