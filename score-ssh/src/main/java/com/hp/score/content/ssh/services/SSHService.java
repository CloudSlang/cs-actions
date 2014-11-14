package com.hp.score.content.ssh.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionParam;
import com.hp.score.content.ssh.entities.CommandResult;
import com.hp.score.content.ssh.entities.ExpectCommandResult;
import com.hp.score.content.ssh.entities.SSHConnection;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.Session;

import java.util.Map;

/**
 * @author ioanvranauhp
 *         Date: 10/29/14
 */
public interface SSHService extends AutoCloseable {

    /**
     * Run a Shell command(s) using SSH protocol.
     *
     * @param command           The Shell command(s).
     * @param characterSet      The character set for the command and for the output of the command.
     * @param usePseudoTerminal If true the result will be formatted like in a terminal.
     * @param connectTimeout    The channel connection timeout.
     * @param commandTimeout    The command timeout.
     * @return the command result.
     */
    CommandResult runShellCommand(String command, String characterSet, boolean usePseudoTerminal, int connectTimeout, int commandTimeout);

    /**
     * Checks the SSH session.
     *
     * @return true if the SSH session is opened, otherwise false.
     */
    boolean isConnected();

    /**
     * Checks the SSH channel for the Expect command.
     *
     * @return true if the SSH channel is opened, otherwise false.
     */
    boolean isExpectChannelConnected();

    /**
     * Close the SSH session.
     */
    void close();

    /**
     * Save the SSH session in the cache (Operation Orchestration session).
     *
     * @param sessionParam The cache (Operation Orchestration session).
     * @param sessionId    The key of the Operation Orchestration session.
     */
    boolean saveToCache(GlobalSessionObject<Map<String, SSHConnection>> sessionParam, String sessionId);

    /**
     * Remove SSH session from the cache (Operation Orchestration session).
     *
     * @param sessionParam The cache (Operation Orchestration session).
     * @param sessionId    The key of the Operation Orchestration session.
     */
    void removeFromCache(GlobalSessionObject<Map<String, SSHConnection>>  sessionParam, String sessionId);

    /**
     * Run a expect command through SSH protocol.
     *
     * @param command               The list of commands (send and expect).
     * @param characterSet          The character set for the command and for the output of the command.
     * @param newline               The character(s) used to separate send and expect commands and also the enter key pressed.
     * @param writeCharacterTimeout The write character delay.
     * @param connectTimeout        The channel connection timeout.
     * @param commandTimeout        The command timeout.   @return the result of the expect commands
     */
    ExpectCommandResult runExpectCommand(String command, String characterSet, String newline, int writeCharacterTimeout, int connectTimeout, int commandTimeout);

    /**
     * Create a local SSH tunnel (connect to a local port on the engine, which is then forwarded to the remote end of the tunnel.
     *
     * @param localPort  The local port.
     * @param remoteHost The remote host.
     * @param remotePort The remote port.
     */
    void createLocalTunnel(int localPort, String remoteHost, int remotePort);

    Session getSSHSession();

    Channel getShellChannel();
}
