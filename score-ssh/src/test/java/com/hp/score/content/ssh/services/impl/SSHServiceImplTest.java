package com.hp.score.content.ssh.services.impl;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SessionParam;
import com.hp.score.content.ssh.actions.SSHShellCommandAction;
import com.hp.score.content.ssh.entities.CommandResult;
import com.hp.score.content.ssh.entities.ExpectCommandResult;
import com.hp.score.content.ssh.entities.SSHConnection;
import com.hp.score.content.ssh.services.SSHService;
import com.hp.score.content.ssh.utils.CacheUtils;
import com.hp.score.content.ssh.utils.Constants;
import com.hp.score.content.ssh.utils.simulator.ShellSimulator;
import com.hp.score.content.ssh.utils.simulator.visualization.IShellVisualizer;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author ioanvranauhp
 * @since 1.0.128-SNAPSHOT
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({CommandResult.class, CacheUtils.class})
public class SSHServiceImplTest {

    @Mock
    private Session sessionMock;
    @Mock
    private ChannelShell channelShellMock;
    @Mock
    private Channel channelMock;
    @Mock
    private CommandResult commandResultMock;
    @Mock
    private ExpectCommandResult expectCommandResultMock;
    @Mock
    private ShellSimulator simulatorMock;
    @Mock
    private IShellVisualizer visualizerMock;
    private SSHService sshServiceSpy;

//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//
//        doReturn(true).when(sessionMock).isConnected();
//
//        PowerMockito.mockStatic(CacheUtils.class);
//        when(CacheUtils.getSshSession(any(SessionParam.class), anyString())).thenReturn(sessionMock);
//
//        sshServiceSpy = CacheUtils.getFromCache(any(SessionParam.class), anyString());
//        sshServiceSpy = SSHServiceImpl.getFromCache(any(SessionParam.class), anyString());
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        sessionMock = null;
//        channelShellMock = null;
//        commandResultMock = null;
//        simulatorMock = null;
//        expectCommandResultMock = null;
//        sshServiceSpy = null;
//    }
//
//    @Test
//    public void testGetFromCache() throws Exception {
////        CacheUtils.getFromCache(any(SessionParam.class), anyString());
//        SSHServiceImpl.getFromCache(any(SessionParam.class), anyString());
//        PowerMockito.verifyStatic(times(1));
//    }
//
//    @Test
//    public void testRunShellCommand() throws Exception {
//        doReturn(channelShellMock).when(sessionMock).openChannel(anyString());
////        doNothing().when(channelShellMock).setPty(anyBoolean());
//        doNothing().when(channelShellMock).connect(anyInt());
//        doReturn(true).when(channelShellMock).isEOF();
//        PowerMockito.whenNew(CommandResult.class).withNoArguments().thenReturn(commandResultMock);
//        doNothing().when(commandResultMock).setStandardOutput(anyString());
//        doNothing().when(commandResultMock).setStandardError(anyString());
//        doNothing().when(channelShellMock).disconnect();
//        doReturn(5).when(channelShellMock).getExitStatus();
//        doNothing().when(commandResultMock).setExitCode(anyInt());
//
//        sshServiceSpy.runShellCommand("", "UTF-8", false, 0, 0);
////        verify(channelShellMock).setPty(anyBoolean());
//        verify(channelShellMock).connect(anyInt());
//        verify(commandResultMock).setStandardOutput(anyString());
//        verify(commandResultMock).setStandardError(anyString());
//        verify(channelShellMock).disconnect();
//        verify(commandResultMock).setExitCode(5);
//    }
//
//    @Test
//    public void testRunShellExpectCommands() throws Exception {
//        doReturn(channelShellMock).when(sessionMock).openChannel(anyString());
//
//        PowerMockito.whenNew(ShellSimulator.class).withAnyArguments().thenReturn(simulatorMock);
//        doNothing().when(simulatorMock).setStreams(any(InputStream.class), any(OutputStream.class));
//        doReturn(visualizerMock).when(simulatorMock).addVisualizer(anyString());
//
//        doNothing().when(channelShellMock).connect(anyInt());
//        doNothing().when(simulatorMock).run(anyInt());
//
//        doReturn(null).when(simulatorMock).getException();
//
//        PowerMockito.whenNew(ExpectCommandResult.class).withNoArguments().thenReturn(expectCommandResultMock);
//        doNothing().when(expectCommandResultMock).setStandardOutput(anyString());
//        doNothing().when(expectCommandResultMock).setExpectXmlOutputs(anyString());
//        doNothing().when(channelShellMock).disconnect();
//        doReturn(5).when(channelShellMock).getExitStatus();
//        doNothing().when(expectCommandResultMock).setExitCode(anyInt());
//
//        sshServiceSpy.runExpectCommand("", "UTF-8", "\n", 0, 0, 0);
//        verify(channelShellMock).connect(anyInt());
//        verify(simulatorMock).run(anyInt());
//        verify(simulatorMock).getException();
//        verify(visualizerMock).getXMLSummary();
//        verify(expectCommandResultMock).setStandardOutput(anyString());
//        verify(expectCommandResultMock).setExpectXmlOutputs(anyString());
//        verify(expectCommandResultMock).setExitCode(5);
//    }
//
//    @Test
//    public void testCreateLocalTunnel() throws Exception {
//        doReturn(0).when(sessionMock).setPortForwardingL(anyInt(), anyString(), anyInt());
//        sshServiceSpy.createLocalTunnel(0, "", 0);
//        verify(sessionMock).setPortForwardingL(anyInt(), anyString(), anyInt());
//    }
//
//    @Test
//    public void testIsConnected() throws Exception {
//        assertEquals(sshServiceSpy.isConnected(), true);
//    }
//
//    @Test
//    public void testClose() throws Exception {
//        sshServiceSpy.close();
//        verify(sessionMock).disconnect();
//    }
//
//    @Test
//    public void testSaveToCache() throws Exception {
//        sshServiceSpy.saveToCache(new GlobalSessionObject<>(), "");
//        PowerMockito.verifyStatic(times(1));
//    }
//
//    @Test
//    public void testRemoveFromCache() throws Exception {
//        sshServiceSpy.removeFromCache(new GlobalSessionObject<>(), "");
//        PowerMockito.verifyStatic(times(1));
//    }

    @Test
    public void testBasic() {
        String user = "demo";
//        String user = "root";
//        String user = "Administrator";
        String host = "16.84.193.91";
//        String host = "16.77.58.180";
//        String host = "16.77.42.143";
//        String host = "16.77.45.100";
        String password = "hpinvent";
//        String password = "B33f34t3r";
        String strCommand = "show network\n";
        SSHShellCommandAction command = new SSHShellCommandAction();
        Map<String, String> results = command.runSshShellCommand(
                host,
                "",
                user,
                password,
                "",
                strCommand,
                "",
                "utf-8",
                "",
                "10000",
                new GlobalSessionObject<Map<String, SSHConnection>>(),
                new GlobalSessionObject<Map<String, SSHConnection>>(),
                "true",
                "true"
        );
        System.out.println(results.get(Constants.OutputNames.RETURN_RESULT));
    }
}
