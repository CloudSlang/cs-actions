package org.eclipse.score.content.ssh.services.impl;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.eclipse.score.content.ssh.entities.CommandResult;
import org.eclipse.score.content.ssh.entities.ConnectionDetails;
import org.eclipse.score.content.ssh.entities.ExpectCommandResult;
import org.eclipse.score.content.ssh.entities.KeyFile;
import org.eclipse.score.content.ssh.entities.KnownHostsFile;
import org.eclipse.score.content.ssh.entities.SSHConnection;
import org.eclipse.score.content.ssh.services.SSHService;
import org.eclipse.score.content.ssh.utils.CacheUtils;
import org.eclipse.score.content.ssh.utils.Constants;
import org.eclipse.score.content.ssh.utils.simulator.ShellSimulator;
import org.eclipse.score.content.ssh.utils.simulator.visualization.IShellVisualizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 * @author octavian-h
 */
public class SSHServiceImpl implements SSHService {
    private static final int POLLING_INTERVAL = 1000;
    private static final String SHELL_CHANNEL = "shell";
    private static final String BASIC_VISUALIZER = "basic";
    private static final String KNOWN_HOSTS_ALLOW = "allow";
    private static final String KNOWN_HOSTS_STRICT = "strict";
    private static final String KNOWN_HOSTS_ADD = "add";
    private Session session;
    private Channel shellChannel;

    public SSHServiceImpl(Session session, Channel channel) {
        this.session = session;
        this.shellChannel = channel;
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

            String policy = knownHostsFile.getPolicy();
            switch (policy.toLowerCase(Locale.ENGLISH)) {
                case KNOWN_HOSTS_ALLOW:
                    session.setConfig("StrictHostKeyChecking", "no");
                    break;
                case KNOWN_HOSTS_STRICT:
                    jsch.setKnownHosts(knownHostsFile.getPath().toString());
                    session.setConfig("StrictHostKeyChecking", "yes");
                    break;
                case KNOWN_HOSTS_ADD:
                    jsch.setKnownHosts(knownHostsFile.getPath().toString());
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
                // create shell channel
                shellChannel = session.openChannel(SHELL_CHANNEL);

                // connect to the channel and run the command(s)
                shellChannel.connect(connectTimeout);
            }
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public CommandResult runShellCommand(String command, String characterSet, boolean usePseudoTerminal, int connectTimeout, int commandTimeout) {
        try {
            // create shell channel
            Channel channel = session.openChannel(SHELL_CHANNEL);
            ((ChannelShell) channel).setPty(usePseudoTerminal);
            InputStream in = new ByteArrayInputStream(command.getBytes(characterSet));
            channel.setInputStream(in);
            OutputStream out = new ByteArrayOutputStream();
            channel.setOutputStream(out);
            OutputStream err = new ByteArrayOutputStream();
            channel.setExtOutputStream(err);

            // connect to the channel and run the command(s)
            channel.connect(connectTimeout);

            // wait for response
            do {
                try {
                    Thread.sleep(POLLING_INTERVAL);
                } catch (InterruptedException ignore) {
                }
                commandTimeout -= POLLING_INTERVAL;
            } while (!channel.isEOF() && commandTimeout > 0);

            // save the response
            CommandResult result = new CommandResult();
            result.setStandardOutput(((ByteArrayOutputStream) out).toString(characterSet));
            result.setStandardError(((ByteArrayOutputStream) err).toString(characterSet));

            channel.disconnect();
            // The exit status is only available after the channel was closed (more exactly, just before the channel is closed).
            result.setExitCode(channel.getExitStatus());
            return result;
        } catch (JSchException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ExpectCommandResult runExpectCommand(String commandList, String characterSet, String newline, int writeCharacterTimeout, int connectTimeout, int commandTimeout) {
        try {
            if (shellChannel == null || !shellChannel.isConnected()) {
                // create shell channel
                shellChannel = session.openChannel(SHELL_CHANNEL);

                // connect to the channel and run the command(s)
                shellChannel.connect(connectTimeout);
            }

            ShellSimulator simulator = new ShellSimulator(
                    commandList,
                    Constants.DEFAULT_MATCH_TIMEOUT,
                    Constants.DEFAULT_READ_TIMEOUT,
                    Constants.DEFAULT_IDLE_TIMEOUT,
                    writeCharacterTimeout,
                    newline.toCharArray(),
                    characterSet,
                    false);
            simulator.setStreams(shellChannel.getInputStream(), shellChannel.getOutputStream());
            IShellVisualizer visualizer = simulator.addVisualizer(BASIC_VISUALIZER);

            simulator.run(commandTimeout);

            String exception = simulator.getException();
            if (exception != null && exception.length() > 0) {
                throw new RuntimeException(exception);
            }

            // save the response
            ExpectCommandResult result = new ExpectCommandResult();
            result.setStandardOutput(simulator.getOutput());
            if (visualizer != null) {
                result.setExpectXmlOutputs(visualizer.getXMLSummary());
            }

            // The exit status is only available after the channel was closed (more exactly, just before the channel is closed).
            result.setExitCode(shellChannel.getExitStatus());
            return result;
        } catch (AssertionError | Exception e) {
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
    public boolean isExpectChannelConnected() {
        return shellChannel != null && shellChannel.isConnected();
    }

    @Override
    public void close() {
        if (shellChannel != null) {
            shellChannel.disconnect();
            shellChannel = null;
        }
        session.disconnect();
        session = null;
    }

    @Override
    public boolean saveToCache(GlobalSessionObject<Map<String, SSHConnection>> sessionParam, String sessionId) {
        return CacheUtils.saveSshSessionAndChannel(session, shellChannel, sessionParam, sessionId);

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
    public Channel getShellChannel() {
        return shellChannel;
    }
}
