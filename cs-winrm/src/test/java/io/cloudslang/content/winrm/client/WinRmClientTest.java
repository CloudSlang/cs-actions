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

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.cloudslang.content.winrm.client.ntlm.NtlmTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Runs the client against a local fake WinRM server.
 */
public class WinRmClientTest {

    private static final String ENCRYPTED_CONTENT_TYPE =
            "multipart/encrypted;protocol=\"application/HTTP-SPNEGO-session-encrypted\";boundary=\"Encrypted Boundary\"";
    private static final Pattern ACTION = Pattern.compile("<a:Action[^>]*>([^<]+)</a:Action>");

    private HttpServer server;
    private FakeWinRm winRm;

    @BeforeEach
    public void startServer() throws IOException {
        winRm = new FakeWinRm();
        server = HttpServer.create(new InetSocketAddress(InetAddress.getLoopbackAddress(), 0), 0);
        server.createContext("/wsman", winRm::handle);
        server.start();
    }

    @AfterEach
    public void stopServer() {
        server.stop(0);
    }

    @Test
    public void basicAuthentication() {
        winRm.mode = "basic";
        final WinRmResponse response = client(AuthenticationScheme.BASIC, "Password").executeCommand("echo hello");

        assertEquals("hello world", response.getStdOut());
        assertEquals("warning", response.getStdErr());
        assertEquals(3, response.getStatusCode());
        assertEquals(Arrays.asList("Create", "Command", "Receive", "Receive", "Receive", "Signal", "Delete"), winRm.actions);
        assertTrue(winRm.requests.get(0).contains("<rsp:WorkingDirectory>C:\\Temp</rsp:WorkingDirectory>"));
        assertTrue(winRm.requests.get(0).contains("<w:OperationTimeout>PT120S</w:OperationTimeout>"));
        assertTrue(winRm.requests.get(1).contains("<rsp:Command>echo hello</rsp:Command>"));
        for (String authorization : winRm.authorizations) {
            assertEquals("Basic " + Base64.getEncoder().encodeToString("User:Password".getBytes(StandardCharsets.UTF_8)), authorization);
        }
    }

    @Test
    public void powerShellIsEncoded() {
        winRm.mode = "basic";
        client(AuthenticationScheme.BASIC, "Password").executePs("Get-Date");
        final String expected = Base64.getEncoder().encodeToString("Get-Date".getBytes(StandardCharsets.UTF_16LE));
        assertTrue(winRm.requests.get(1).contains("<rsp:Command>powershell</rsp:Command>" +
                "<rsp:Arguments>-encodedcommand</rsp:Arguments><rsp:Arguments>" + expected + "</rsp:Arguments>"));
    }

    @Test
    public void ntlmAuthenticationWithEncryption() {
        winRm.mode = "ntlm";
        final WinRmResponse response = client(AuthenticationScheme.NTLM, "Password").executeCommand("echo hello");

        assertEquals("hello world", response.getStdOut());
        assertEquals("warning", response.getStdErr());
        assertEquals(3, response.getStatusCode());
        assertEquals(Arrays.asList("Create", "Command", "Receive", "Receive", "Receive", "Signal", "Delete"), winRm.actions);
        assertEquals(winRm.actions.size(), winRm.encryptedRequests);
        assertEquals(1, winRm.handshakes);
        assertEquals(1, winRm.remotePorts.size(), "all requests must use the authenticated connection");
    }

    @Test
    public void ntlmReauthenticatesWhenConnectionIsReset() {
        winRm.mode = "ntlm";
        winRm.forgetAuthenticationAfter = 2;
        final WinRmResponse response = client(AuthenticationScheme.NTLM, "Password").executeCommand("echo hello");
        assertEquals("hello world", response.getStdOut());
        assertEquals(2, winRm.handshakes);
        assertEquals(Arrays.asList("Create", "Command", "Receive", "Receive", "Receive", "Signal", "Delete"), winRm.actions);
    }

    @Test
    public void ntlmInvalidCredentials() {
        winRm.mode = "ntlm";
        try {
            client(AuthenticationScheme.NTLM, "wrong").executeCommand("echo hello");
            fail("expected an authentication failure");
        } catch (WinRmException e) {
            assertTrue(e.getMessage().contains("Invalid credentials or incompatible authentication schemes"), e.getMessage());
        }
    }

    @Test
    public void basicInvalidCredentials() {
        winRm.mode = "basic";
        try {
            client(AuthenticationScheme.BASIC, "wrong").executeCommand("echo hello");
            fail("expected an authentication failure");
        } catch (WinRmException e) {
            assertTrue(e.getMessage().contains("401"), e.getMessage());
        }
    }

    @Test
    public void connectionFailuresAreRetried() {
        winRm.mode = "basic";
        winRm.failFirstCreate = true;
        final WinRmResponse response = client(AuthenticationScheme.BASIC, "Password").executeCommand("echo hello");
        assertEquals(3, response.getStatusCode());
        assertEquals(Arrays.asList("Create", "Create", "Command", "Receive", "Receive", "Receive", "Signal", "Delete"), winRm.actions);
    }

    @Test
    public void commandFaultStillDeletesShell() {
        winRm.mode = "basic";
        winRm.commandFault = true;
        try {
            client(AuthenticationScheme.BASIC, "Password").executeCommand("echo hello");
            fail("expected a fault");
        } catch (WinRmFaultException e) {
            assertEquals("2150858770", e.getFaultCode());
            assertEquals("The command failed.", e.getMessage());
        }
        assertEquals(Arrays.asList("Create", "Command", "Delete"), winRm.actions);
    }

    private WinRmClient client(final AuthenticationScheme scheme, final String password) {
        return WinRmClient.builder("http://127.0.0.1:" + server.getAddress().getPort() + "/wsman", "Domain", "User", password)
                .authenticationScheme(scheme)
                .workingDirectory("C:\\Temp")
                .operationTimeout(120_000)
                .retriesForConnectionFailures(1, 10)
                .build();
    }

    /**
     * Minimal WinRM server: supports Basic and NTLM (Negotiate) authentication with message encryption.
     */
    private static final class FakeWinRm {
        private static final byte[] NT_HASH = HexFormat.of().parseHex("a4f49c406510bdcab6824ee7c30fd852"); // "Password"

        volatile String mode;
        volatile boolean failFirstCreate;
        volatile boolean commandFault;
        volatile int forgetAuthenticationAfter = -1;

        final List<String> actions = Collections.synchronizedList(new ArrayList<>());
        final List<String> requests = Collections.synchronizedList(new ArrayList<>());
        final List<String> authorizations = Collections.synchronizedList(new ArrayList<>());
        final java.util.Set<Integer> remotePorts = Collections.synchronizedSet(new java.util.HashSet<>());
        int encryptedRequests;
        int handshakes;

        private NtlmTest.ServerSide security;
        private int clientSequence;
        private int serverSequence;
        private int receives;
        private int authenticatedRequests;

        synchronized void handle(final HttpExchange exchange) throws IOException {
            try {
                final byte[] body = exchange.getRequestBody().readAllBytes();
                final String authorization = exchange.getRequestHeaders().getFirst("Authorization");
                remotePorts.add(exchange.getRemoteAddress().getPort());
                if ("basic".equals(mode)) {
                    authorizations.add(authorization);
                    final String expected = "Basic " + Base64.getEncoder().encodeToString("User:Password".getBytes(StandardCharsets.UTF_8));
                    if (!expected.equals(authorization)) {
                        send(exchange, 401, null, new byte[0]);
                        return;
                    }
                    reply(exchange, new String(body, StandardCharsets.UTF_8), false);
                    return;
                }

                if (authorization != null && authorization.startsWith("Negotiate ")) {
                    final byte[] token = Base64.getDecoder().decode(authorization.substring("Negotiate ".length()));
                    if (token[8] == 1) {
                        final int flags = NtlmTest.readUint32(token, 12) | 0x00800000;
                        exchange.getResponseHeaders().add("WWW-Authenticate",
                                "Negotiate " + Base64.getEncoder().encodeToString(NtlmTest.challengeMessage(flags)));
                        send(exchange, 401, null, new byte[0]);
                        return;
                    }
                    if (!authenticate(token)) {
                        exchange.getResponseHeaders().add("WWW-Authenticate", "Negotiate");
                        send(exchange, 401, null, new byte[0]);
                        return;
                    }
                    handshakes++;
                } else if (security == null || authenticatedRequests == forgetAuthenticationAfter) {
                    security = null;
                    forgetAuthenticationAfter = -1;
                    exchange.getResponseHeaders().add("WWW-Authenticate", "Negotiate");
                    exchange.getResponseHeaders().add("WWW-Authenticate", "Kerberos");
                    send(exchange, 401, null, new byte[0]);
                    return;
                }
                authenticatedRequests++;
                final String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
                if (contentType == null || !contentType.toLowerCase(Locale.ROOT).startsWith("multipart/encrypted")) {
                    throw new IllegalStateException("unencrypted request: " + contentType);
                }
                encryptedRequests++;
                reply(exchange, new String(security.decrypt(body, clientSequence++), StandardCharsets.UTF_8), true);
            } catch (Exception e) {
                e.printStackTrace();
                send(exchange, 418, null, new byte[0]);
            }
        }

        private boolean authenticate(final byte[] type3) throws Exception {
            final String user = new String(NtlmTest.securityBuffer(type3, 36), StandardCharsets.UTF_16LE);
            final String domain = new String(NtlmTest.securityBuffer(type3, 28), StandardCharsets.UTF_16LE);
            final byte[] ntResponse = NtlmTest.securityBuffer(type3, 20);
            final byte[] proof = Arrays.copyOf(ntResponse, 16);
            final byte[] blob = Arrays.copyOfRange(ntResponse, 16, ntResponse.length);
            final byte[] ntOwfV2 = NtlmTest.hmac(NT_HASH, (user.toUpperCase(Locale.ROOT) + domain).getBytes(StandardCharsets.UTF_16LE));
            if (!Arrays.equals(proof, NtlmTest.hmac(ntOwfV2, NtlmTest.concat(NtlmTest.serverChallenge(), blob)))) {
                return false;
            }
            final Cipher rc4 = Cipher.getInstance("ARCFOUR");
            rc4.init(Cipher.DECRYPT_MODE, new SecretKeySpec(NtlmTest.hmac(ntOwfV2, proof), "ARCFOUR"));
            security = new NtlmTest.ServerSide(rc4.doFinal(NtlmTest.securityBuffer(type3, 52)));
            clientSequence = 0;
            serverSequence = 0;
            return true;
        }

        private void reply(final HttpExchange exchange, final String request, final boolean encrypt) throws Exception {
            requests.add(request);
            final Matcher matcher = ACTION.matcher(request);
            if (!matcher.find()) {
                throw new IllegalStateException("no action in " + request);
            }
            final String action = matcher.group(1).substring(matcher.group(1).lastIndexOf('/') + 1);
            actions.add(action);

            int status = 200;
            final String response;
            switch (action) {
                case "Create":
                    if (failFirstCreate) {
                        failFirstCreate = false;
                        send(exchange, 503, null, new byte[0]);
                        return;
                    }
                    response = envelope("<x:ResourceCreated xmlns:x=\"http://schemas.xmlsoap.org/ws/2004/09/transfer\">" +
                            "<a:ReferenceParameters><w:SelectorSet><w:Selector Name=\"ShellId\">SHELL-1</w:Selector>" +
                            "</w:SelectorSet></a:ReferenceParameters></x:ResourceCreated>");
                    break;
                case "Command":
                    if (commandFault) {
                        status = 500;
                        response = WsManMessagesTest.fault("2150858770", "The command failed.");
                    } else {
                        response = envelope("<rsp:CommandResponse><rsp:CommandId>CMD-1</rsp:CommandId></rsp:CommandResponse>");
                    }
                    break;
                case "Receive":
                    receives++;
                    if (receives == 1) {
                        status = 500;
                        response = WsManMessagesTest.fault(WinRmClient.FAULT_CODE_OPERATION_TIMEOUT_EXPIRED, "timeout");
                    } else if (receives == 2) {
                        response = WsManMessagesTest.receive("hello ", "", false, 0);
                    } else {
                        response = WsManMessagesTest.receive("world", "warning", true, 3);
                    }
                    break;
                case "Signal":
                    response = envelope("<rsp:SignalResponse/>");
                    break;
                case "Delete":
                    response = envelope("");
                    break;
                default:
                    throw new IllegalStateException("unexpected action " + action);
            }
            final byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            if (encrypt) {
                send(exchange, status, ENCRYPTED_CONTENT_TYPE, security.encrypt(bytes, serverSequence++));
            } else {
                send(exchange, status, "application/soap+xml;charset=UTF-8", bytes);
            }
        }

        private static String envelope(final String body) {
            return "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" " +
                    "xmlns:a=\"http://schemas.xmlsoap.org/ws/2004/08/addressing\" " +
                    "xmlns:w=\"http://schemas.dmtf.org/wbem/wsman/1/wsman.xsd\" " +
                    "xmlns:rsp=\"http://schemas.microsoft.com/wbem/wsman/1/windows/shell\"><s:Header/><s:Body>" + body +
                    "</s:Body></s:Envelope>";
        }

        private static void send(final HttpExchange exchange, final int status, final String contentType, final byte[] body) throws IOException {
            if (contentType != null) {
                exchange.getResponseHeaders().add("Content-Type", contentType);
            }
            exchange.sendResponseHeaders(status, body.length == 0 ? -1 : body.length);
            if (body.length > 0) {
                exchange.getResponseBody().write(body);
            }
            exchange.close();
        }
    }
}
