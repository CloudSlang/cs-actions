/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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
import io.cloudslang.content.ssh.entities.CommandResult;
import io.cloudslang.content.ssh.entities.ConnectionDetails;
import io.cloudslang.content.ssh.entities.KeyFile;
import io.cloudslang.content.ssh.entities.KnownHostsFile;
import io.cloudslang.content.ssh.exceptions.SSHException;
import io.cloudslang.content.ssh.services.SSHService;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author ioanvranauhp
 * @since 1.0.128-SNAPSHOT
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SSHServiceImpl.class, IOUtils.class})
public class SSHServiceImplTest {

    public static final boolean AGENT_FORWARDING_FALSE = false;
    public static final boolean AGENT_FORWARDING_TRUE = true;
    private static final String SHELL_PATH = "path";
    private static final String PASS_PHRASE = "passPhrase";
    private static final Path KNOWN_HOSTS_PATH = Paths.get(System.getProperty("user.home"), ".ssh", "known_hosts");
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
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Mock
    private Session sessionMock;
    @Mock
    private ChannelExec channelExecMock;
    @Mock
    private ChannelShell channelShellMock;
    @Mock
    private CommandResult commandResultMock;
    @Mock
    private JSch jSchMock;
    @Mock
    private InputStream inputStreamMock;
    @Mock
    private OutputStream outputStreamMock;

    @Before
    public void setUp() throws Exception {
        PowerMockito.whenNew(JSch.class).withNoArguments().thenReturn(jSchMock);
        PowerMockito.when(jSchMock.getSession(USERNAME, HOST, PORT)).thenReturn(sessionMock);
        Mockito.doNothing().when(jSchMock).addIdentity(SHELL_PATH);
        PowerMockito.when(sessionMock.openChannel("exec")).thenReturn(channelExecMock);
        PowerMockito.when(sessionMock.openChannel("shell")).thenReturn(channelShellMock);
        Mockito.doNothing().when(channelExecMock).connect(CONNECT_TIMEOUT);
        PowerMockito.when(channelExecMock.isClosed()).thenReturn(true);
    }

    @Test
    public void testConstructors() {
        SSHServiceImpl sshService = new SSHServiceImpl(sessionMock, channelExecMock);
        assertEquals(sshService.getExecChannel(), channelExecMock);
        assertEquals(sshService.getSSHSession(), sessionMock);

        ProxyHTTP proxyHTTP = null;
        try {
            sshService = new SSHServiceImpl(CONNECTION_DETAILS, null, new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY), CONNECT_TIMEOUT, false, proxyHTTP, "");
            assertEquals(null, sshService.getExecChannel());
            assertEquals(sessionMock, sshService.getSSHSession());

            sshService = new SSHServiceImpl(CONNECTION_DETAILS, null, new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY), CONNECT_TIMEOUT, true, proxyHTTP, "");
            assertEquals(channelExecMock, sshService.getExecChannel());
            assertEquals(sessionMock, sshService.getSSHSession());

            sshService = new SSHServiceImpl(CONNECTION_DETAILS, new KeyFile(SHELL_PATH), new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY), CONNECT_TIMEOUT, true, proxyHTTP, "");
            assertEquals(channelExecMock, sshService.getExecChannel());
            assertEquals(sessionMock, sshService.getSSHSession());

            sshService = new SSHServiceImpl(CONNECTION_DETAILS, new KeyFile(SHELL_PATH, PASS_PHRASE), new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY), CONNECT_TIMEOUT, true, proxyHTTP, "");
            assertEquals(channelExecMock, sshService.getExecChannel());
            assertEquals(sessionMock, sshService.getSSHSession());

            sshService = new SSHServiceImpl(CONNECTION_DETAILS, new KeyFile(SHELL_PATH, PASS_PHRASE), new KnownHostsFile(KNOWN_HOSTS_PATH, KNOWN_HOSTS_POLICY), CONNECT_TIMEOUT, false, proxyHTTP, "");
            assertEquals(null, sshService.getExecChannel());
            assertEquals(sessionMock, sshService.getSSHSession());
        } catch (SSHException e) {
            assert (false);
        }
    }

    @Test
    public void testRunShellCommand() throws Exception {
        SSHService sshService = prepareRunShellCommandTest();
        CommandResult commandResult = sshService.runShellCommand("ls", "UTF-8", true, CONNECT_TIMEOUT, COMMAND_TIMEOUT, AGENT_FORWARDING_FALSE);
        assertEquals(commandResult.getExitCode(), 0);
        assertEquals(commandResult.getStandardError(), "");
        assertEquals(commandResult.getStandardOutput(), "");
    }

    private SSHService prepareRunShellCommandTest() throws IOException {
        when(channelExecMock.getInputStream()).thenReturn(inputStreamMock);
        when(inputStreamMock.available()).thenReturn(1).thenReturn(0);
        return new SSHServiceImpl(sessionMock, channelExecMock);
    }

    @Test
    public void testRunShell() throws Exception {
        SSHService sshService = prepareRunShellTest();
        CommandResult commandResult = sshService.runShell("ls", "UTF-8", true, CONNECT_TIMEOUT, COMMAND_TIMEOUT, AGENT_FORWARDING_FALSE);
        assertEquals(0, commandResult.getExitCode());
        assertEquals(null, commandResult.getStandardError());
        assertEquals("", commandResult.getStandardOutput());
    }

    private SSHService prepareRunShellTest() throws IOException {
        when(channelShellMock.getInputStream()).thenReturn(inputStreamMock);
        when(channelShellMock.getOutputStream()).thenReturn(outputStreamMock);
        when(inputStreamMock.available()).thenReturn(1).thenReturn(0);
        mockStatic(IOUtils.class);
        when(IOUtils.toString(inputStreamMock, "UTF-8")).thenReturn("");
        return new SSHServiceImpl(sessionMock, channelExecMock);
    }

    @Test
    public void testRunShellCommand2() throws Exception {
        SSHService sshService = prepareRunShellCommandTest();

        CommandResult commandResult = sshService.runShellCommand("ls", "UTF-8", false, CONNECT_TIMEOUT, COMMAND_TIMEOUT, AGENT_FORWARDING_TRUE);
        assertEquals(commandResult.getExitCode(), 0);
        assertEquals(commandResult.getStandardError(), "");
        assertEquals(commandResult.getStandardOutput(), "");
    }

    @Test
    public void testRunShellCommand3() throws Exception {
        SSHService sshService = prepareRunShellCommandTest();

        CommandResult commandResult = sshService.runShellCommand("", "UTF-8", false, CONNECT_TIMEOUT, COMMAND_TIMEOUT, AGENT_FORWARDING_FALSE);
        assertEquals(commandResult.getExitCode(), 0);
        assertEquals(commandResult.getStandardError(), "");
        assertEquals(commandResult.getStandardOutput(), "");
    }

    @Test
    public void testRunShellCommandInvalidEncoding() throws Exception {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelExecMock);

        exception.expect(RuntimeException.class);

        sshService.runShellCommand("", "test", true, CONNECT_TIMEOUT, COMMAND_TIMEOUT, AGENT_FORWARDING_TRUE);
    }

    @Test
    public void testCreateLocalTunnel() throws JSchException {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelExecMock);
        sshService.createLocalTunnel(PORT, HOST, PORT);

        verify(sessionMock).setPortForwardingL(PORT, HOST, PORT);
    }

    @Test
    public void testClose() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelExecMock);
        sshService.close();
        verify(channelExecMock).disconnect();
        Mockito.verifyNoMoreInteractions(channelExecMock);
        verify(sessionMock).disconnect();

        sshService = new SSHServiceImpl(sessionMock, null);
        sshService.close();
    }

    @Test
    public void testGetSSHSession() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelExecMock);
        assertEquals(sessionMock, sshService.getSSHSession());
        assertEquals(channelExecMock, sshService.getExecChannel());
    }

    @Test
    public void testSaveToCache() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelExecMock);
        final boolean savedToCache = sshService.saveToCache(Mockito.any(GlobalSessionObject.class), "sessionId");
        assertEquals(false, savedToCache);
    }

    @Test
    public void testRemoveFromCache() {
        SSHService sshService = new SSHServiceImpl(sessionMock, channelExecMock);
        sshService.removeFromCache(Mockito.any(GlobalSessionObject.class), "sessionId");
    }

    @Test
    public void testTimeoutExceptionIsThrown() throws Exception {
        PowerMockito.when(channelExecMock.isClosed()).thenReturn(false);
        SSHService sshService = new SSHServiceImpl(sessionMock, channelExecMock);

        exception.expect(RuntimeException.class);
        exception.expectMessage("Timeout");

        sshService.runShellCommand("ls", "UTF-8", true, CONNECT_TIMEOUT, 0, AGENT_FORWARDING_FALSE);
    }

}