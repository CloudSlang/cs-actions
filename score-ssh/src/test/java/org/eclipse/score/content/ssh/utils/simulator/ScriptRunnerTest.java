package org.eclipse.score.content.ssh.utils.simulator;

import org.eclipse.score.content.ssh.utils.simulator.elements.AlwaysOn;
import org.eclipse.score.content.ssh.utils.simulator.elements.Expect;
import org.eclipse.score.content.ssh.utils.simulator.elements.Send;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import static junit.framework.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * Created by vranau on 11/24/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ScriptRunner.class})
public class ScriptRunnerTest {
    private String script = "send ls \nexpect ls";
    private long matchTimeout = 1000L;
    private long readTimeout = 1000L;
    private long sleepTimeout = 1000L;
    private long characterDelay = 1000L;
    private String newLine = "\n";
    private ScriptRunner scriptRunner;

    @Mock
    private PipedInputStream pipedInputStreamMock;
    @Mock
    private ShellSimulator shellSimulatorMock;
    @Mock
    private PipedOutputStream pipedOutputStreamMock;
    @Mock
    private ScriptModel parserMock;
    @Mock
    private AlwaysOn alwaysOnMock;
    @Mock
    private Expect expectMock;
    @Mock
    private Send sendMock;
    @Mock
    private InputStream inputStreamMock;

    @Before
    public void setUp() throws Exception {
        whenNew(ScriptModel.class).withAnyArguments().thenReturn(parserMock);
        scriptRunner = new ScriptRunner(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, newLine.toCharArray());
    }

    @Test
    public void testAddPipe() throws Exception {

        whenNew(PipedOutputStream.class).withArguments(pipedInputStreamMock).thenReturn(pipedOutputStreamMock);
        scriptRunner.addPipe(pipedInputStreamMock);
        shellSimulatorMock.getException();
        verifyNew(PipedOutputStream.class).withArguments(pipedInputStreamMock);
        scriptRunner.addPipe(null);
        verifyZeroInteractions(pipedInputStreamMock, pipedOutputStreamMock);
    }

    @Test
    public void testGetOutput() {
        String output = scriptRunner.getOutput();
        assertEquals("", output);
    }

    @Test
    public void testSetCaptureOutput() {
        scriptRunner.setCaptureOutput(true);
        scriptRunner.setCaptureOutput(false);
        boolean captureOutput = Boolean.parseBoolean(null);
        scriptRunner.setCaptureOutput(captureOutput);
    }

    @Test
    public void testGetCommandsLeft() {
        when(parserMock.getCommandsLeft()).thenReturn(10);
        assertEquals(10, scriptRunner.getCommandsLeft());
    }

    @Test
    public void testNoMoreCommandsLeft() {
        when(parserMock.getCommandsLeft()).thenReturn(10);
        assertEquals(false, scriptRunner.noMoreCommandsLeft());

        when(parserMock.getCommandsLeft()).thenReturn(0);
        assertEquals(true, scriptRunner.noMoreCommandsLeft());
    }

    @Test
    public void testTerminate() {
        scriptRunner.terminate();
    }

    @Test
    public void testGetDeltaT() {
        final long deltaT = scriptRunner.getDeltaT();
        assertTrue(deltaT > 0);
    }

    @Test
    public void testRun() throws Exception {

        scriptRunner = new ScriptRunner(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, newLine.toCharArray());
        when(parserMock.getCommandsLeft()).thenReturn(10);
        scriptRunner.run();
        final String exception = scriptRunner.getException().toLowerCase();
        assertTrue(exception.contains("readTimeout at:".toLowerCase()));
    }

    @Test
    public void checkAlwaysHandlers() throws Exception {
        boolean checkAlwaysHandlers = scriptRunner.checkAlwaysHandlers();
        assertEquals(false, checkAlwaysHandlers);

        when(parserMock.checkAlwaysHandlers("", matchTimeout)).thenReturn(alwaysOnMock);
        checkAlwaysHandlers = scriptRunner.checkAlwaysHandlers();
        assertEquals(true, checkAlwaysHandlers);

        when(parserMock.checkAlwaysHandlers("", matchTimeout)).thenThrow(new Exception("test exception"));
        checkAlwaysHandlers = scriptRunner.checkAlwaysHandlers();
        assertEquals(false, checkAlwaysHandlers);
        assertTrue(scriptRunner.getException().contains("test exception"));
    }

    @Test
    public void testSendable() throws Exception {
        boolean sendable = scriptRunner.sendable();
        assertFalse(sendable);

        when(parserMock.getCommandsLeft()).thenReturn(10);
        sendable = scriptRunner.sendable();
        assertFalse(sendable);

        when(parserMock.IsExceptAllowed("", scriptRunner, matchTimeout)).thenReturn(expectMock);
        when(parserMock.IsSendAllowed()).thenReturn(sendMock);
        sendable = scriptRunner.sendable();
        assertTrue(sendable);
    }

    @Test
    public void testProcess() throws Exception {
        boolean process = scriptRunner.process();
        assertFalse(process);
        scriptRunner.in = inputStreamMock;
        process = scriptRunner.process();
        assertFalse(process);
        scriptRunner.in = null;
    }

}
