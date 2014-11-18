package org.eclipse.score.content.ssh.services.impl;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import org.eclipse.score.content.ssh.entities.CommandResult;
import org.eclipse.score.content.ssh.entities.ConnectionDetails;
import org.eclipse.score.content.ssh.entities.ExpectCommandResult;
import org.eclipse.score.content.ssh.entities.KeyFile;
import org.eclipse.score.content.ssh.services.SSHService;
import org.eclipse.score.content.ssh.utils.simulator.ShellSimulator;
import org.eclipse.score.content.ssh.utils.simulator.visualization.IShellVisualizer;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author ioanvranauhp
 * @since 1.0.128-SNAPSHOT
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SSHServiceImpl.class})
public class SSHServiceImplTest {

    private final String shellPath = "path";
    private final String passPhrase = "passPhrase";
    private final String output = "output";
    private final String host = "host";
    private final int port = 0;
    private final String username = "username";
    private final String password = "password";
    private final int connectTimeout = 10000;
    private final int commandTimeout = 200;
    private final ConnectionDetails connectionDetails = new ConnectionDetails(host, port, username, password);
    private String xmlSumary = "xmlSumary";

    @Mock
    private Session sessionMock;
    @Mock
    private ChannelShell channelShellMock;
    @Mock
    private CommandResult commandResultMock;
    @Mock
    private ExpectCommandResult expectCommandResultMock;
    @Mock
    private ShellSimulator simulatorMock;
    @Mock
    private IShellVisualizer visualizerMock;
    private SSHService sshServiceSpy;

    @Mock
    private JSch jSchMock;

    @Before
    public void setUp() throws Exception {
        PowerMockito.whenNew(JSch.class).withNoArguments().thenReturn(jSchMock);
        PowerMockito.when(jSchMock.getSession(username, host, port)).thenReturn(sessionMock);
        Mockito.doNothing().when(jSchMock).addIdentity(shellPath);
        PowerMockito.when(sessionMock.openChannel("shell")).thenReturn(channelShellMock);
        Mockito.doNothing().when(channelShellMock).connect(connectTimeout);

        PowerMockito.whenNew(ShellSimulator.class).withAnyArguments().thenReturn(simulatorMock);

        PowerMockito.when(simulatorMock.getOutput()).thenReturn(output);
        PowerMockito.when(simulatorMock.addVisualizer("basic")).thenReturn(visualizerMock);
        PowerMockito.when(visualizerMock.getXMLSummary()).thenReturn(xmlSumary);
    }

    @Test
    public void testConstructors() throws Exception {
        SSHServiceImpl sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        assertEquals(sshService.getShellChannel(), channelShellMock);
        assertEquals(sshService.getSSHSession(), sessionMock);

        sshService = new SSHServiceImpl(connectionDetails, null, connectTimeout, false);
        assertEquals(null, sshService.getShellChannel());
        assertEquals(sessionMock, sshService.getSSHSession());

        sshService = new SSHServiceImpl(connectionDetails, null, connectTimeout, true);
        assertEquals(channelShellMock, sshService.getShellChannel());
        assertEquals(sessionMock, sshService.getSSHSession());

        sshService = new SSHServiceImpl(connectionDetails, new KeyFile(shellPath), connectTimeout, true);
        assertEquals(channelShellMock, sshService.getShellChannel());
        assertEquals(sessionMock, sshService.getSSHSession());

        sshService = new SSHServiceImpl(connectionDetails, new KeyFile(shellPath, passPhrase), connectTimeout, true);
        assertEquals(channelShellMock, sshService.getShellChannel());
        assertEquals(sessionMock, sshService.getSSHSession());

        sshService = new SSHServiceImpl(connectionDetails, new KeyFile(shellPath, passPhrase), connectTimeout);
        assertEquals(null, sshService.getShellChannel());
        assertEquals(sessionMock, sshService.getSSHSession());
    }

    @Test
    public void testRunShellCommand() throws Exception {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        CommandResult commandResult = sshService.runShellCommand("ls", "UTF-8", true, connectTimeout, commandTimeout);
        assertEquals(commandResult.getExitCode(), 0);
        assertEquals(commandResult.getStandardError(), "");
        assertEquals(commandResult.getStandardOutput(), "");

        commandResult = sshService.runShellCommand("ls", "UTF-8", false, connectTimeout, commandTimeout);
        assertEquals(commandResult.getExitCode(), 0);
        assertEquals(commandResult.getStandardError(), "");
        assertEquals(commandResult.getStandardOutput(), "");

        commandResult = sshService.runShellCommand("", "UTF-8", false, connectTimeout, commandTimeout);
        assertEquals(commandResult.getExitCode(), 0);
        assertEquals(commandResult.getStandardError(), "");
        assertEquals(commandResult.getStandardOutput(), "");
    }

    @Test(expected = RuntimeException.class)
    public void testRunShellCommandInvalidEncoding() throws Exception {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        sshService.runShellCommand("", "test", true, connectTimeout, commandTimeout);
    }

    @Test
    public void testRunExpectCommand() throws Exception {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        ExpectCommandResult expectCommandResult = sshService.runExpectCommand("ls", "UTF-8", "\\n", 1000, connectTimeout, commandTimeout);
        assertEquals(expectCommandResult.getExitCode(), 0);
        assertEquals(expectCommandResult.getStandardError(), null);
        assertEquals(expectCommandResult.getStandardOutput(), output);
        assertEquals(expectCommandResult.getExpectXmlOutputs(), xmlSumary);

        sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        expectCommandResult = sshService.runExpectCommand("ls", "UTF-8", "\\n", 1000, 0, 0);
        assertEquals(expectCommandResult.getExitCode(), 0);
        assertEquals(expectCommandResult.getStandardError(), null);
        assertEquals(expectCommandResult.getStandardOutput(), output);
        assertEquals(expectCommandResult.getExpectXmlOutputs(), xmlSumary);

        sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        expectCommandResult = sshService.runExpectCommand("ls", "UTF-8", "test123!@#", 1000, 0, 0);
        assertEquals(expectCommandResult.getExitCode(), 0);
        assertEquals(expectCommandResult.getStandardError(), null);
        assertEquals(expectCommandResult.getStandardOutput(), output);
        assertEquals(expectCommandResult.getExpectXmlOutputs(), xmlSumary);

        sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        expectCommandResult = sshService.runExpectCommand("", "UTF-8", "\\n", 1000, 0, 0);
        assertEquals(expectCommandResult.getExitCode(), 0);
        assertEquals(expectCommandResult.getStandardError(), null);
        assertEquals(expectCommandResult.getStandardOutput(), output);
        assertEquals(expectCommandResult.getExpectXmlOutputs(), xmlSumary);

    }

    @Test
    public void testCreateLocalTunnel() throws JSchException {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        sshService.createLocalTunnel(port, host, port);

        Mockito.verify(sessionMock).setPortForwardingL(port, host, port);
    }

    @Test
    public void testIsConnected() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        final boolean connected = sshService.isConnected();
        assertEquals(false, connected);

        Mockito.verify(sessionMock).isConnected();
    }

    @Test
    public void testIsExpectChannelConnected() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        final boolean connected = sshService.isExpectChannelConnected();
        assertEquals(false, connected);

        Mockito.verify(channelShellMock).isConnected();
    }

    @Test
    public void testClose() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        sshService.close();
        Mockito.verify(channelShellMock).disconnect();
        Mockito.verifyNoMoreInteractions(channelShellMock);
        Mockito.verify(sessionMock).disconnect();

        sshService = new SSHServiceImpl(sessionMock, null);
        sshService.close();
    }

    @Test
    public void testGetSSHSession() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        assertEquals(sessionMock, sshService.getSSHSession());
        assertEquals(channelShellMock, sshService.getShellChannel());
    }

    @Test
    public void testSaveToCache() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        final boolean savedToCache = sshService.saveToCache(Mockito.any(GlobalSessionObject.class), "sessionId");
        assertEquals(false, savedToCache);
    }

    @Test
    public void testRemoveFromCache() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        sshService.removeFromCache(Mockito.any(GlobalSessionObject.class), "sessionId");
    }
}
