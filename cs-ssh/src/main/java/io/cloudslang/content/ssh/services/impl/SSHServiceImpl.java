package io.cloudslang.content.ssh.services.impl;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.cloudslang.content.ssh.entities.CommandResult;
import io.cloudslang.content.ssh.entities.ConnectionDetails;
import io.cloudslang.content.ssh.entities.KeyFile;
import io.cloudslang.content.ssh.entities.KnownHostsFile;
import io.cloudslang.content.ssh.entities.SSHConnection;
import io.cloudslang.content.ssh.exceptions.TimeoutException;
import io.cloudslang.content.ssh.services.SSHService;
import io.cloudslang.content.ssh.utils.CacheUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
    private static final int POLLING_INTERVAL = 10;
    private static final String EXEC_CHANNEL = "exec";
    private static final String KNOWN_HOSTS_ALLOW = "allow";
    private static final String KNOWN_HOSTS_STRICT = "strict";
    private static final String KNOWN_HOSTS_ADD = "add";
    private Session session;
    private Channel execChannel;

    public SSHServiceImpl(Session session, Channel channel) {
        this.session = session;
        this.execChannel = channel;
    }

    /**
     * Open SSH session.
     *
     * @param details        The connection details.
     * @param keyFile        The private key file.
     * @param knownHostsFile The known_hosts file and policy.
     * @param connectTimeout The open SSH session timeout.
     */
    public SSHServiceImpl(ConnectionDetails details, KeyFile keyFile, KnownHostsFile knownHostsFile, int connectTimeout) {
        this(details, keyFile, knownHostsFile, connectTimeout, false);
    }

    /**
     * Open SSH session.
     *
     * @param details                     The connection details.
     * @param keyFile                     The private key file.
     * @param knownHostsFile              The known_hosts file and policy.
     * @param connectTimeout              The open SSH session timeout.
     * @param keepContextForExpectCommand Use the same channel for the expect command.
     */
    public SSHServiceImpl(ConnectionDetails details, KeyFile keyFile, KnownHostsFile knownHostsFile, int connectTimeout, boolean keepContextForExpectCommand) {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(details.getUsername(), details.getHost(), details.getPort());
            session.setConfig("PreferredAuthentications", "publickey,password");

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
                        throw new RuntimeException("The known_hosts file path should be absolute.");
                    }
                    if (!Files.exists(knownHostsFilePath)) {
                        Files.createDirectories(knownHostsFilePath.getParent());
                        Files.createFile(knownHostsFilePath);
                    }
                    jsch.setKnownHosts(knownHostsFilePath.toString());
                    session.setConfig("StrictHostKeyChecking", "no");
                    break;
                default:
                    throw new RuntimeException("Unknown known_hosts file policy.");
            }

            if (keyFile == null) {
                // use the password
                session.setPassword(details.getPassword());
            } else {
                // or use the OpenSSH private key file
                String keyFilePath = keyFile.getKeyFilePath();
                String passPhrase = keyFile.getPassPhrase();
                if (passPhrase != null) {
                    jsch.addIdentity(keyFilePath, passPhrase);
                } else {
                    jsch.addIdentity(keyFilePath);
                }
            }

            session.connect(connectTimeout);

            if (keepContextForExpectCommand) {
                // create exec channel
                execChannel = session.openChannel(EXEC_CHANNEL);

                // connect to the channel and run the command(s)
                execChannel.connect(connectTimeout);
            }
        } catch (JSchException | IOException e) {
            throw new RuntimeException(e);
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
        try {

            if (!isConnected()) {
                session.connect(connectTimeout);
            }
            ChannelExec channel = (ChannelExec) session.openChannel(EXEC_CHANNEL);
            channel.setCommand(command.getBytes(characterSet));
            channel.setPty(usePseudoTerminal);
            channel.setAgentForwarding(agentForwarding);
            channel.setInputStream(null);
            OutputStream out = new ByteArrayOutputStream();
            channel.setOutputStream(out);
            OutputStream err = new ByteArrayOutputStream();
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
            CommandResult result = new CommandResult();
            result.setStandardOutput(((ByteArrayOutputStream) out).toString(characterSet));
            if( usePseudoTerminal && channel.getExitStatus() != 0){
                result.setStandardError(((ByteArrayOutputStream) out).toString(characterSet));
            }
            else {
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

    public Channel getExecChannel() {
        return execChannel;
    }
}
