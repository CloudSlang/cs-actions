package org.openscore.content.ssh.services.impl;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.openscore.content.ssh.entities.CommandResult;
import org.openscore.content.ssh.entities.ConnectionDetails;
import org.openscore.content.ssh.entities.ExpectCommandResult;
import org.openscore.content.ssh.entities.KeyFile;
import org.openscore.content.ssh.entities.KnownHostsFile;
import org.openscore.content.ssh.services.SSHService;
import org.openscore.content.ssh.services.impl.SSHServiceImpl;
import org.openscore.content.ssh.utils.simulator.ShellSimulator;
import org.openscore.content.ssh.utils.simulator.visualization.IShellVisualizer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

/**
 * @author ioanvranauhp
 * @since 1.0.128-SNAPSHOT
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SSHServiceImpl.class})
public class SSHServiceImplTest {

    private static final String SHELL_PATH = "path";
    private static final String PASS_PHRASE = "passPhrase";
    private static final Path KNOWN_HOSTS_PATH = Paths.get(System.getProperty("user.home"), ".ssh", "known_hosts");;
    private static final String KNOWN_HOSTS_POLICY = "allow";
    private static final String OUTPUT = "output";
    private static final String HOST = "host";
    private static final int PORT = 0;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final int CONNECT_TIMEOUT = 10000;
    private static final int COMMAND_TIMEOUT = 200;
    private static final ConnectionDetails CONNECTION_DETAILS = new ConnectionDetails(HOST, PORT, USERNAME, PASSWORD);
    private static String XML_SUMMARY = "XML_SUMMARY";

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

    @Mock
    private JSch jSchMock;

    @Before
    public void setUp() throws Exception {
        PowerMockito.whenNew(JSch.class).withNoArguments().thenReturn(jSchMock);
        PowerMockito.when(jSchMock.getSession(USERNAME, HOST, PORT)).thenReturn(sessionMock);
        Mockito.doNothing().when(jSchMock).addIdentity(SHELL_PATH);
        PowerMockito.when(sessionMock.openChannel("shell")).thenReturn(channelShellMock);
        Mockito.doNothing().when(channelShellMock).connect(CONNECT_TIMEOUT);

        PowerMockito.whenNew(ShellSimulator.class).withAnyArguments().thenReturn(simulatorMock);

        PowerMockito.when(simulatorMock.getOutput()).thenReturn(OUTPUT);
        PowerMockito.when(simulatorMock.addVisualizer("basic")).thenReturn(visualizerMock);
        PowerMockito.when(visualizerMock.getXMLSummary()).thenReturn(XML_SUMMARY);
    }

    @Test
    public void testConstructors() throws Exception {
        SSHServiceImpl sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        assertEquals(sshService.getShellChannel(), channelShellMock);
        assertEquals(sshService.getSSHSession(), sessionMock);

        sshService = new SSHServiceImpl(CONNECTION_DETAILS, null, new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY), CONNECT_TIMEOUT, false);
        assertEquals(null, sshService.getShellChannel());
        assertEquals(sessionMock, sshService.getSSHSession());

        sshService = new SSHServiceImpl(CONNECTION_DETAILS, null, new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY), CONNECT_TIMEOUT, true);
        assertEquals(channelShellMock, sshService.getShellChannel());
        assertEquals(sessionMock, sshService.getSSHSession());

        sshService = new SSHServiceImpl(CONNECTION_DETAILS, new KeyFile(SHELL_PATH), new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY), CONNECT_TIMEOUT, true);
        assertEquals(channelShellMock, sshService.getShellChannel());
        assertEquals(sessionMock, sshService.getSSHSession());

        sshService = new SSHServiceImpl(CONNECTION_DETAILS, new KeyFile(SHELL_PATH, PASS_PHRASE), new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY), CONNECT_TIMEOUT, true);
        assertEquals(channelShellMock, sshService.getShellChannel());
        assertEquals(sessionMock, sshService.getSSHSession());

        sshService = new SSHServiceImpl(CONNECTION_DETAILS, new KeyFile(SHELL_PATH, PASS_PHRASE), new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY), CONNECT_TIMEOUT);
        assertEquals(null, sshService.getShellChannel());
        assertEquals(sessionMock, sshService.getSSHSession());
    }

    @Test
    public void testRunShellCommand() throws Exception {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        CommandResult commandResult = sshService.runShellCommand("ls", "UTF-8", true, CONNECT_TIMEOUT, COMMAND_TIMEOUT);
        assertEquals(commandResult.getExitCode(), 0);
        assertEquals(commandResult.getStandardError(), "");
        assertEquals(commandResult.getStandardOutput(), "");

        commandResult = sshService.runShellCommand("ls", "UTF-8", false, CONNECT_TIMEOUT, COMMAND_TIMEOUT);
        assertEquals(commandResult.getExitCode(), 0);
        assertEquals(commandResult.getStandardError(), "");
        assertEquals(commandResult.getStandardOutput(), "");

        commandResult = sshService.runShellCommand("", "UTF-8", false, CONNECT_TIMEOUT, COMMAND_TIMEOUT);
        assertEquals(commandResult.getExitCode(), 0);
        assertEquals(commandResult.getStandardError(), "");
        assertEquals(commandResult.getStandardOutput(), "");
    }

    @Test(expected = RuntimeException.class)
    public void testRunShellCommandInvalidEncoding() throws Exception {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        sshService.runShellCommand("", "test", true, CONNECT_TIMEOUT, COMMAND_TIMEOUT);
    }

    @Test
    public void testRunExpectCommand() throws Exception {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        ExpectCommandResult expectCommandResult = sshService.runExpectCommand("ls", "UTF-8", "\\n", 1000, CONNECT_TIMEOUT, COMMAND_TIMEOUT);
        assertEquals(expectCommandResult.getExitCode(), 0);
        assertEquals(expectCommandResult.getStandardError(), null);
        assertEquals(expectCommandResult.getStandardOutput(), OUTPUT);
        assertEquals(expectCommandResult.getExpectXmlOutputs(), XML_SUMMARY);

        sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        expectCommandResult = sshService.runExpectCommand("ls", "UTF-8", "\\n", 1000, 0, 0);
        assertEquals(expectCommandResult.getExitCode(), 0);
        assertEquals(expectCommandResult.getStandardError(), null);
        assertEquals(expectCommandResult.getStandardOutput(), OUTPUT);
        assertEquals(expectCommandResult.getExpectXmlOutputs(), XML_SUMMARY);

        sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        expectCommandResult = sshService.runExpectCommand("ls", "UTF-8", "test123!@#", 1000, 0, 0);
        assertEquals(expectCommandResult.getExitCode(), 0);
        assertEquals(expectCommandResult.getStandardError(), null);
        assertEquals(expectCommandResult.getStandardOutput(), OUTPUT);
        assertEquals(expectCommandResult.getExpectXmlOutputs(), XML_SUMMARY);

        sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        expectCommandResult = sshService.runExpectCommand("", "UTF-8", "\\n", 1000, 0, 0);
        assertEquals(expectCommandResult.getExitCode(), 0);
        assertEquals(expectCommandResult.getStandardError(), null);
        assertEquals(expectCommandResult.getStandardOutput(), OUTPUT);
        assertEquals(expectCommandResult.getExpectXmlOutputs(), XML_SUMMARY);

    }

    @Test
    public void testCreateLocalTunnel() throws JSchException {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        sshService.createLocalTunnel(PORT, HOST, PORT);

        verify(sessionMock).setPortForwardingL(PORT, HOST, PORT);
    }

    @Test
    public void testIsConnected() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        final boolean connected = sshService.isConnected();
        assertEquals(false, connected);

        verify(sessionMock).isConnected();
    }

    @Test
    public void testIsExpectChannelConnected() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        final boolean connected = sshService.isExpectChannelConnected();
        assertEquals(false, connected);

        verify(channelShellMock).isConnected();
    }

    @Test
    public void testClose() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelShellMock);
        sshService.close();
        verify(channelShellMock).disconnect();
        Mockito.verifyNoMoreInteractions(channelShellMock);
        verify(sessionMock).disconnect();

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
