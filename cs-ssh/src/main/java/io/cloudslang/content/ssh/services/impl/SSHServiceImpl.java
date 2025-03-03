/*
 * Copyright 2021-2024 Open Text
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





package io.cloudslang.content.ssh.services.impl;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.jcraft.jsch.*;
import io.cloudslang.content.ssh.entities.*;
import io.cloudslang.content.ssh.exceptions.SSHException;
import io.cloudslang.content.ssh.exceptions.TimeoutException;
import io.cloudslang.content.ssh.services.SSHService;
import io.cloudslang.content.ssh.utils.CacheUtils;
import io.cloudslang.content.ssh.utils.IdentityKeyUtils;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 * @author octavian-h
 */
public class SSHServiceImpl implements SSHService {
    private static final String SHELL_CHANNEL = "shell";
    private static final int POLLING_INTERVAL = 10;
    private static final String EXEC_CHANNEL = "exec";
    private static final String KNOWN_HOSTS_ALLOW = "allow";
    private static final String KNOWN_HOSTS_STRICT = "strict";
    private static final String KNOWN_HOSTS_ADD = "add";
    private static final String ALLOWED_CIPHERS = "aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-ctr,aes192-cbc,aes256-ctr,aes256-cbc";
    private static final String KEX = ",ecdh-sha2-nistp256,ecdh-sha2-nistp384,ecdh-sha2-nistp521,diffie-hellman-group14-sha1,diffie-hellman-group-exchange-sha256,diffie-hellman-group1-sha1,diffie-hellman-group-exchange-sha1";
    public static final String EXIT_COMMAND = "exit";
    private Session session;
    private Channel execChannel;

    public SSHServiceImpl(Session session, Channel channel) {
        this.session = session;
        this.execChannel = channel;
    }

    /**
     * Open SSH session.
     *
     * @param details                     The connection details.
     * @param identityKey                 The private key file or string.
     * @param knownHostsFile              The known_hosts file and policy.
     * @param connectTimeout              The open SSH session timeout.
     * @param keepContextForExpectCommand Use the same channel for the expect command.
     * @param proxyHTTP                   The proxy settings, parse it as null if no proxy settings required
     * @param allowedCiphers              The list of allowed ciphers. If not empty, it will be used to overwrite the default list.
     */
    public SSHServiceImpl(ConnectionDetails details, IdentityKey identityKey, KnownHostsFile knownHostsFile,
                          int connectTimeout, boolean keepContextForExpectCommand, ProxyHTTP proxyHTTP, String allowedCiphers) throws SSHException {
        JSch jsch = new JSch();
        String finalListOfAllowedCiphers = StringUtilities.isNotBlank(allowedCiphers) ? allowedCiphers : ALLOWED_CIPHERS;
        JSch.setConfig("cipher.s2c", finalListOfAllowedCiphers);
        JSch.setConfig("cipher.c2s", finalListOfAllowedCiphers);
        JSch.setConfig("PreferredAuthentications", "publickey,password,keyboard-interactive");
        // Check if the configuration already contains the value before appending
        if (!JSch.getConfig("server_host_key").contains("ssh-rsa,ssh-dss")) {
            JSch.setConfig("server_host_key", JSch.getConfig("server_host_key") + ",ssh-rsa,ssh-dss");
        }
        if (!JSch.getConfig("PubkeyAcceptedAlgorithms").contains("ssh-rsa,ssh-dss")) {
            JSch.setConfig("PubkeyAcceptedAlgorithms", JSch.getConfig("PubkeyAcceptedAlgorithms") + ",ssh-rsa,ssh-dss");
        }
        if (!JSch.getConfig("kex").contains(KEX)) {
            JSch.setConfig("kex", JSch.getConfig("kex") + KEX);
        }
        try {
            session = jsch.getSession(details.getUsername(), details.getHost(), details.getPort());
        } catch (JSchException e) {
            throw new SSHException(e);
        }

        try {
            String policy = knownHostsFile.getPolicy();
            Path knownHostsFilePath = knownHostsFile.getPath();
            switch (policy.toLowerCase(Locale.ENGLISH)) {
                case KNOWN_HOSTS_ALLOW:
                    session.setConfig("StrictHostKeyChecking", "no");
                    break;
                case KNOWN_HOSTS_STRICT:
                    jsch.setKnownHosts(knownHostsFilePath.toString());
                    session.setConfig("StrictHostKeyChecking", "yes");
                    break;
                case KNOWN_HOSTS_ADD:
                    if (!knownHostsFilePath.isAbsolute()) {
                        throw new SSHException("The known_hosts file path should be absolute.");
                    }
                    if (!Files.exists(knownHostsFilePath)) {
                        Path parent = knownHostsFilePath.getParent();
                        if (parent != null) {
                            Files.createDirectories(parent);
                        }
                        Files.createFile(knownHostsFilePath);
                    }
                    jsch.setKnownHosts(knownHostsFilePath.toString());
                    session.setConfig("StrictHostKeyChecking", "no");
                    break;
                default:
                    throw new SSHException("Unknown known_hosts file policy.");
            }
        } catch (JSchException e) {
            throw new SSHException("The known_hosts file couldn't be set.", e);
        } catch (IOException e) {
            throw new SSHException("The known_hosts file couldn't be created.", e);
        }

        if (identityKey == null) {
            // use the password
            session.setPassword(details.getPassword());
        } else {
            // or use the OpenSSH private key file or string
            IdentityKeyUtils.setIdentity(jsch, identityKey);
        }

        if (proxyHTTP != null) {
            session.setProxy(proxyHTTP);
        }

        try {
            session.connect(connectTimeout);

            if (keepContextForExpectCommand) {
                // create exec channel
                execChannel = session.openChannel(EXEC_CHANNEL);

                // connect to the channel and run the command(s)
                execChannel.connect(connectTimeout);
            }
        } catch (JSchException e) {
            throw new SSHException(e);
        }
    }

    @Override
    public CommandResult runShell(
            final String command,
            final String characterSet,
            boolean usePseudoTerminal,
            int connectTimeout,
            int commandTimeout,
            boolean agentForwarding) {
        ChannelShell channelShell = null;
        OutputStream out = null;
        OutputStream err = null;
        try {
            if (!isConnected()) {
                session.connect(connectTimeout);
            }

            channelShell = (ChannelShell) session.openChannel(SHELL_CHANNEL);
            channelShell.setPty(usePseudoTerminal);
            channelShell.setAgentForwarding(agentForwarding);

            final OutputStream shellIn = channelShell.getOutputStream();
            final InputStream shellOut = channelShell.getInputStream();

            channelShell.connect(connectTimeout);

            final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(shellIn, characterSet));

            printWriter.println(command);
            printWriter.flush();

            try {
                Thread.sleep(commandTimeout);
            } catch (InterruptedException ignored) {
            }

            printWriter.println(EXIT_COMMAND);
            printWriter.flush();

            final String result = IOUtils.toString(shellOut, characterSet);

            final CommandResult commandResult = new CommandResult();
            commandResult.setStandardOutput(result);

            return commandResult;
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
        }finally {
            if (channelShell != null && channelShell.isConnected()) {
                channelShell.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (err != null) {
                    err.close();
                }
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }

    @Override
    public CommandResult runShellCommand(
            String command,
            String characterSet,
            boolean usePseudoTerminal,
            int connectTimeout,
            int commandTimeout,
            boolean agentForwarding) {
        ChannelExec channel = null;
        OutputStream out = null;
        OutputStream err = null;
        try {
            if (!isConnected()) {
                session.connect(connectTimeout);
            }
            // create exec channel
            channel = (ChannelExec) session.openChannel(EXEC_CHANNEL);
            channel.setCommand(command.getBytes(characterSet));
            channel.setPty(usePseudoTerminal);
            channel.setAgentForwarding(agentForwarding);
            out = new ByteArrayOutputStream();
            channel.setOutputStream(out);
             err = new ByteArrayOutputStream();
            channel.setErrStream(err);

            // connect to the channel and run the command(s)
            channel.connect(connectTimeout);

            // wait for response
            long currentTime = System.currentTimeMillis();
            long timeLimit = currentTime + commandTimeout;
            while (!channel.isClosed() && currentTime < timeLimit) {
                try {
                    Thread.sleep(POLLING_INTERVAL);
                } catch (InterruptedException ignore) {
                }
                currentTime = System.currentTimeMillis();
            }
            boolean timedOut = !channel.isClosed();

            // save the response
            CommandResult result = new CommandResult();
            result.setStandardOutput(((ByteArrayOutputStream) out).toString(characterSet));
            if (usePseudoTerminal && channel.getExitStatus() != 0) {
                result.setStandardError(((ByteArrayOutputStream) out).toString(characterSet));
            } else {
                result.setStandardError(((ByteArrayOutputStream) err).toString(characterSet));
            }

            channel.disconnect();
            // The exit status is only available after the channel was closed (more exactly, just before the channel is closed).
            result.setExitCode(channel.getExitStatus());

            if (timedOut) {
                throw new TimeoutException(String.valueOf(result));
            }

            return result;
        } catch (JSchException | UnsupportedEncodingException | TimeoutException e) {
            throw new RuntimeException(e);
        }finally {
            if (channel != null && channel.isConnected()) {
                channel.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (err != null) {
                    err.close();
                }
            } catch (IOException ioException) {
                throw new RuntimeException(ioException);
            }
        }
    }

    @Override
    public void createLocalTunnel(int localPort, String remoteHost, int remotePort) {
        try {
            session.setPortForwardingL(localPort, remoteHost, remotePort);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isConnected() {
        return session.isConnected();
    }

    @Override
    public void close() {
        if (execChannel != null) {
            execChannel.disconnect();
            execChannel = null;
        }
        session.disconnect();
        session = null;
    }

    @Override
    public boolean saveToCache(GlobalSessionObject<Map<String, SSHConnection>> sessionParam, String sessionId) {
        return CacheUtils.saveSshSessionAndChannel(session, execChannel, sessionParam, sessionId);

    }

    @Override
    public void removeFromCache(GlobalSessionObject<Map<String, SSHConnection>> sessionParam, String sessionId) {
        CacheUtils.removeSshSession(sessionParam, sessionId);
    }

    @Override
    public Session getSSHSession() {
        return session;
    }

    @Override
    public Channel getExecChannel() {
        return execChannel;
    }
}
