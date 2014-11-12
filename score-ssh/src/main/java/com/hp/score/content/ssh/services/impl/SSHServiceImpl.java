package com.hp.score.content.ssh.services.impl;

import com.hp.oo.sdk.content.plugin.SessionParam;
import com.hp.score.content.ssh.entities.CommandResult;
import com.hp.score.content.ssh.entities.ConnectionDetails;
import com.hp.score.content.ssh.entities.ExpectCommandResult;
import com.hp.score.content.ssh.entities.KeyFile;
import com.hp.score.content.ssh.services.SSHService;
import com.hp.score.content.ssh.utils.CacheUtils;
import com.hp.score.content.ssh.utils.Constants;
import com.hp.score.content.ssh.utils.simulator.ShellSimulator;
import com.hp.score.content.ssh.utils.simulator.visualization.IShellVisualizer;
import com.jcraft.jsch.*;

import java.io.*;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public class SSHServiceImpl implements SSHService {
    private static final int POLLING_INTERVAL = 1000;
    private static final String SHELL_CHANNEL = "shell";
    private static final String BASIC_VISUALIZER = "basic";
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
     * @param connectTimeout The open SSH session timeout.
     */
    public SSHServiceImpl(ConnectionDetails details, KeyFile keyFile, int connectTimeout) {
        this(details, keyFile, connectTimeout, false);
    }

    /**
     * Open SSH session.
     *
     * @param details                     The connection details.
     * @param keyFile                     The private key file.
     * @param connectTimeout              The open SSH session timeout.
     * @param keepContextForExpectCommand Use the same channel for the expect command.
     */
    public SSHServiceImpl(ConnectionDetails details, KeyFile keyFile, int connectTimeout, boolean keepContextForExpectCommand) {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(details.getUsername(), details.getHost(), details.getPort());
            session.setConfig("StrictHostKeyChecking", "no");

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
            result.setExpectXmlOutputs(visualizer.getXMLSummary());

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
    public void saveToCache(SessionParam sessionParam, String sessionId) {
        if (shellChannel == null) {
            CacheUtils.saveSshSession(session, sessionParam, sessionId);
        } else {
            CacheUtils.saveSshSessionAndChannel(session, shellChannel, sessionParam, sessionId);
        }
    }

    @Override
    public void removeFromCache(SessionParam sessionParam, String sessionId) {
        CacheUtils.removeSshSession(sessionParam, sessionId);
    }
}
