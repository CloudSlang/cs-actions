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

import org.apache.hc.core5.net.InetAddressUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Client executing commands on a remote Windows host through the WinRM (WS-Management) Windows Remote Shell.
 * <p>
 * Each execution opens a shell, runs the command, collects its output and exit code, then closes the shell.
 */
public final class WinRmClient {

    public static final long DEFAULT_OPERATION_TIMEOUT = 60_000L;
    public static final long DEFAULT_CONNECTION_TIMEOUT = 30_000L;
    public static final long DEFAULT_CONNECTION_REQUEST_TIMEOUT = 60_000L;
    public static final int DEFAULT_RETRIES_FOR_CONNECTION_FAILURES = 1;
    public static final long DEFAULT_PAUSE_BETWEEN_RETRIES = 5_000L;

    static final String FAULT_CODE_OPERATION_TIMEOUT_EXPIRED = "2150858793";
    static final String FAULT_CODE_SHELL_WAS_NOT_FOUND = "2150858843";

    private static final String DEFAULT_LOCALE = "en-US";
    private static final Pattern PORT_SUFFIX = Pattern.compile(".*:(\\d+)$");

    private final Builder settings;

    private WinRmClient(final Builder builder) {
        this.settings = builder;
    }

    public static Builder builder(final String host, final String username, final String password) {
        return new Builder(host, null, username, password);
    }

    public static Builder builder(final String host, final String domain, final String username, final String password) {
        return new Builder(host, domain, username, password);
    }

    public WinRmResponse executeCommand(final String command) {
        return executeCommand(command, null);
    }

    public WinRmResponse executeCommand(final String command, final List<String> arguments) {
        if (command == null) {
            throw new NullPointerException("command must not be null");
        }
        final String endpoint = settings.getEndpointUrl();
        final WsManMessages messages = new WsManMessages(endpoint, DEFAULT_LOCALE, settings.operationTimeout);
        try (WinRmTransport transport = new WinRmTransport(transportSettings(endpoint))) {
            return execute(transport, messages, command, arguments);
        } catch (IOException e) {
            // raised only when closing the transport
            throw new WinRmException("Unable to close the WinRM connection: " + e.getMessage(), e);
        }
    }

    /**
     * Executes a PowerShell script, passing it encoded as {@code powershell -encodedcommand <base64 UTF-16LE script>}.
     */
    public WinRmResponse executePs(final String psScript) {
        return executeCommand("powershell", Arrays.asList("-encodedcommand", encodePs(psScript)));
    }

    public static String encodePs(final String psScript) {
        return Base64.getEncoder().encodeToString(psScript.getBytes(StandardCharsets.UTF_16LE));
    }

    private WinRmResponse execute(final WinRmTransport transport, final WsManMessages messages,
                                  final String command, final List<String> arguments) {
        final Document createResponse = invokeWithRetry("create", transport,
                messages.create(settings.workingDirectory, settings.environment));
        final String shellId = WsManMessages.getShellId(createResponse);

        Throwable failure = null;
        try {
            return executeInShell(transport, messages, shellId, command, arguments);
        } catch (RuntimeException | Error e) {
            failure = e;
            throw e;
        } finally {
            try {
                invokeIgnoringFault("delete", transport, messages.delete(shellId), FAULT_CODE_SHELL_WAS_NOT_FOUND);
            } catch (RuntimeException e) {
                if (failure == null) {
                    throw e;
                }
                failure.addSuppressed(e);
            }
        }
    }

    private WinRmResponse executeInShell(final WinRmTransport transport, final WsManMessages messages, final String shellId,
                                         final String command, final List<String> arguments) {
        final Document commandResponse;
        try {
            // the command is never retried, to avoid running it more than once
            commandResponse = transport.send(messages.command(shellId, command, arguments, Boolean.FALSE));
        } catch (IOException e) {
            throw new WinRmException("Exception occurred while making winrm call: " + e.getMessage(), e);
        }
        final String commandId = WsManMessages.getCommandId(commandResponse);

        Throwable failure = null;
        try {
            return receive(transport, messages, shellId, commandId);
        } catch (RuntimeException | Error e) {
            failure = e;
            throw e;
        } finally {
            try {
                invokeIgnoringFault("signal", transport,
                        messages.signal(shellId, commandId, WsManMessages.SIGNAL_TERMINATE), FAULT_CODE_SHELL_WAS_NOT_FOUND);
            } catch (RuntimeException e) {
                if (failure == null) {
                    throw e;
                }
                failure.addSuppressed(e);
            }
        }
    }

    private WinRmResponse receive(final WinRmTransport transport, final WsManMessages messages,
                                  final String shellId, final String commandId) {
        final ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
        final ByteArrayOutputStream stdErr = new ByteArrayOutputStream();
        final String receiveMessage = messages.receive(shellId, commandId);
        while (true) {
            final Document response;
            try {
                response = invokeWithRetry("receive", transport, receiveMessage);
            } catch (WinRmFaultException fault) {
                // no output was available before the operation timeout expired: receive again
                if (FAULT_CODE_OPERATION_TIMEOUT_EXPIRED.equals(fault.getFaultCode())) {
                    continue;
                }
                throw fault;
            }
            final Integer exitCode = readReceiveResponse(response, stdOut, stdErr);
            if (exitCode != null) {
                return new WinRmResponse(decode(stdOut), decode(stdErr), exitCode);
            }
        }
    }

    /**
     * Appends the streams of a Receive response to the given buffers.
     *
     * @return the command exit code when the command is done, {@code null} otherwise
     */
    static Integer readReceiveResponse(final Document response, final ByteArrayOutputStream stdOut, final ByteArrayOutputStream stdErr) {
        for (Element stream : WsManMessages.elements(response, WsManMessages.NS_SHELL, "Stream")) {
            final String content = stream.getTextContent().trim();
            if (content.isEmpty()) {
                continue;
            }
            final byte[] value = Base64.getMimeDecoder().decode(content);
            final String name = stream.getAttribute("Name");
            if ("stdout".equals(name)) {
                stdOut.write(value, 0, value.length);
            } else if ("stderr".equals(name)) {
                stdErr.write(value, 0, value.length);
            }
        }
        final List<Element> states = WsManMessages.elements(response, WsManMessages.NS_SHELL, "CommandState");
        if (!states.isEmpty() && WsManMessages.COMMAND_STATE_DONE.equals(states.get(0).getAttribute("State"))) {
            final Element exitCode = WsManMessages.firstChild(states.get(0), WsManMessages.NS_SHELL, "ExitCode");
            if (exitCode == null) {
                throw new WinRmException("Exit code not found in the WinRM Receive response");
            }
            return Integer.valueOf((int) Long.parseLong(exitCode.getTextContent().trim()));
        }
        return null;
    }

    private static String decode(final ByteArrayOutputStream stream) {
        // the platform default charset is used, as by previous winrm4j based implementation
        return new String(stream.toByteArray());
    }

    private Document invokeIgnoringFault(final String operation, final WinRmTransport transport, final String message,
                                         final String ignoredFaultCode) {
        try {
            return invokeWithRetry(operation, transport, message);
        } catch (WinRmFaultException fault) {
            if (ignoredFaultCode.equals(fault.getFaultCode())) {
                return null;
            }
            throw fault;
        }
    }

    private Document invokeWithRetry(final String operation, final WinRmTransport transport, final String message) {
        IOException firstException = null;
        int attempt = 0;
        while (true) {
            attempt++;
            try {
                return transport.send(message);
            } catch (IOException e) {
                if (firstException == null) {
                    firstException = e;
                }
                if (attempt > settings.retriesForConnectionFailures) {
                    break;
                }
                try {
                    Thread.sleep(settings.pauseBetweenRetries);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new WinRmException("Exception occurred while making winrm call", e);
                }
            }
        }
        throw new WinRmException("failed task \"" + operation + "\" after " + attempt + " attempt(s)", firstException);
    }

    private WinRmTransport.Settings transportSettings(final String endpoint) {
        final WinRmTransport.Settings transport = new WinRmTransport.Settings();
        transport.endpoint = endpoint;
        transport.authenticationScheme = settings.authenticationScheme;
        transport.username = settings.username;
        transport.password = settings.password;
        transport.domain = settings.domain;
        transport.requestNewKerberosTicket = settings.requestNewKerberosTicket;
        transport.sslContext = settings.sslContext;
        transport.hostnameVerifier = settings.hostnameVerifier;
        transport.disableCertificateChecks = settings.disableCertificateChecks;
        transport.proxyHost = settings.proxyHost;
        transport.proxyPort = settings.proxyPort;
        transport.proxyUsername = settings.proxyUsername;
        transport.proxyPassword = settings.proxyPassword;
        transport.connectionTimeout = settings.connectionTimeout;
        transport.connectionRequestTimeout = settings.connectionRequestTimeout;
        // the socket must stay open longer than the server side operation timeout
        transport.receiveTimeout = settings.operationTimeout + 60_000L;
        return transport;
    }

    public static final class Builder {
        private final String host;
        private final String domain;
        private final String username;
        private final String password;
        private AuthenticationScheme authenticationScheme = AuthenticationScheme.NTLM;
        private Integer port;
        private boolean useHttps;
        private String workingDirectory;
        private Map<String, String> environment = Collections.emptyMap();
        private boolean requestNewKerberosTicket;
        private SSLContext sslContext;
        private HostnameVerifier hostnameVerifier;
        private boolean disableCertificateChecks;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private long operationTimeout = DEFAULT_OPERATION_TIMEOUT;
        private long connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
        private long connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
        private int retriesForConnectionFailures = DEFAULT_RETRIES_FOR_CONNECTION_FAILURES;
        private long pauseBetweenRetries = DEFAULT_PAUSE_BETWEEN_RETRIES;

        private Builder(final String host, final String domain, final String username, final String password) {
            if (host == null) {
                throw new NullPointerException("host must not be null");
            }
            this.host = host;
            this.domain = domain;
            this.username = username;
            this.password = password;
        }

        public Builder authenticationScheme(final AuthenticationScheme authenticationScheme) {
            if (authenticationScheme == null) {
                throw new NullPointerException("authenticationScheme must not be null");
            }
            this.authenticationScheme = authenticationScheme;
            return this;
        }

        public Builder port(final int port) {
            this.port = port;
            return this;
        }

        public Builder useHttps(final boolean useHttps) {
            this.useHttps = useHttps;
            return this;
        }

        public Builder workingDirectory(final String workingDirectory) {
            this.workingDirectory = workingDirectory;
            return this;
        }

        public Builder environment(final Map<String, String> environment) {
            if (environment == null) {
                throw new NullPointerException("environment must not be null");
            }
            this.environment = environment;
            return this;
        }

        public Builder requestNewKerberosTicket(final boolean requestNewKerberosTicket) {
            this.requestNewKerberosTicket = requestNewKerberosTicket;
            return this;
        }

        public Builder sslContext(final SSLContext sslContext) {
            this.sslContext = sslContext;
            return this;
        }

        public Builder hostnameVerifier(final HostnameVerifier hostnameVerifier) {
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }

        /**
         * Skips certificate trust and host name checks, unless an SSL context or host name verifier is provided.
         */
        public Builder disableCertificateChecks(final boolean disableCertificateChecks) {
            this.disableCertificateChecks = disableCertificateChecks;
            return this;
        }

        public Builder proxy(final String proxyHost, final String proxyPort) {
            this.proxyHost = proxyHost;
            this.proxyPort = proxyPort;
            return this;
        }

        public Builder proxyCredentials(final String proxyUsername, final String proxyPassword) {
            this.proxyUsername = proxyUsername;
            this.proxyPassword = proxyPassword;
            return this;
        }

        /**
         * @param operationTimeout WS-Management operation timeout, in milliseconds
         */
        public Builder operationTimeout(final long operationTimeout) {
            this.operationTimeout = operationTimeout;
            return this;
        }

        public Builder connectionTimeout(final long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public Builder connectionRequestTimeout(final long connectionRequestTimeout) {
            this.connectionRequestTimeout = connectionRequestTimeout;
            return this;
        }

        public Builder retriesForConnectionFailures(final int retriesForConnectionFailures, final long pauseBetweenRetries) {
            if (retriesForConnectionFailures < 0) {
                throw new IllegalArgumentException("retriesForConnectionFailures should be zero or more");
            }
            this.retriesForConnectionFailures = retriesForConnectionFailures;
            this.pauseBetweenRetries = pauseBetweenRetries;
            return this;
        }

        public WinRmClient build() {
            return new WinRmClient(this);
        }

        String getEndpointUrl() {
            if (host.startsWith("http:") || host.startsWith("https:")) {
                if (useHttps && host.startsWith("http:")) {
                    throw new IllegalArgumentException("Invalid setting useHttps and address starting http://");
                }
                if (!useHttps && host.startsWith("https:")) {
                    throw new IllegalArgumentException("Invalid setting useHttp and address starting https://");
                }
                return host;
            }
            final String address = InetAddressUtils.isIPv6(host) ? "[" + host + "]" : host;
            final String protocol = useHttps ? "https" : "http";
            if (PORT_SUFFIX.matcher(address).matches()) {
                return protocol + "://" + address + "/wsman";
            }
            final int effectivePort = port != null ? port : (useHttps ? 5986 : 5985);
            return protocol + "://" + address + ":" + effectivePort + "/wsman";
        }
    }
}
