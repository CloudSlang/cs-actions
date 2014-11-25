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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
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
    private PipedInputStream pipedInputStream;

    @Mock
    private ShellSimulator shellSimulator;
    @Mock
    private PipedOutputStream pipedOutputStream;
    @Mock
    private ScriptModel parser;

    @Mock
    private AlwaysOn alwaysOn;
    @Mock
    private Expect expect;
    @Mock
    private Send send;
    @Mock
    private InputStream inputStream;

    @Before
    public void setUp() throws Exception {
        whenNew(ScriptModel.class).withAnyArguments().thenReturn(parser);
        scriptRunner = new ScriptRunner(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, newLine.toCharArray());
    }

    @Test
    public void testAddPipe() throws Exception {

        whenNew(PipedOutputStream.class).withArguments(pipedInputStream).thenReturn(pipedOutputStream);
        scriptRunner.addPipe(pipedInputStream);
        shellSimulator.getException();
        verifyNew(PipedOutputStream.class).withArguments(pipedInputStream);
        scriptRunner.addPipe(null);
        verifyZeroInteractions(pipedInputStream, pipedOutputStream);
    }

    @Test
    public void testGetOutput() {
        String output = scriptRunner.getOutput();
        assertEquals("", output);
    }

    @Test
    public void testSetCaptureOutput(){
        scriptRunner.setCaptureOutput(true);
        scriptRunner.setCaptureOutput(false);
        boolean captureOutput = Boolean.parseBoolean(null);
        scriptRunner.setCaptureOutput(captureOutput);
    }

    @Test
    public void testGetCommandsLeft() {
        when(parser.getCommandsLeft()).thenReturn(10);
        assertEquals(10, scriptRunner.getCommandsLeft());
    }

    @Test
    public void testNoMoreCommandsLeft() {
        when(parser.getCommandsLeft()).thenReturn(10);
        assertEquals(false, scriptRunner.noMoreCommandsLeft());

        when(parser.getCommandsLeft()).thenReturn(0);
        assertEquals(true, scriptRunner.noMoreCommandsLeft());
    }

    @Test
    public void testTerminate() {
        scriptRunner.terminate();
    }

    @Test
    public void testGetDeltaT(){
        final long deltaT = scriptRunner.getDeltaT();
        assertTrue(deltaT > 0);
    }
    @Test
    public void testRun() throws Exception {

        scriptRunner = new ScriptRunner(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, newLine.toCharArray());
        when(parser.getCommandsLeft()).thenReturn(10);
        scriptRunner.run();
        final String exception = scriptRunner.getException().toLowerCase();
        assertTrue(exception.contains("readTimeout at:".toLowerCase()));
    }

    @Test
    public void checkAlwaysHandlers() throws Exception {
        boolean checkAlwaysHandlers = scriptRunner.checkAlwaysHandlers();
        assertEquals(false, checkAlwaysHandlers);

        when(parser.checkAlwaysHandlers("", matchTimeout)).thenReturn(alwaysOn);
        checkAlwaysHandlers = scriptRunner.checkAlwaysHandlers();
        assertEquals(true, checkAlwaysHandlers);

        when(parser.checkAlwaysHandlers("", matchTimeout)).thenThrow(new Exception("test exception"));
        checkAlwaysHandlers = scriptRunner.checkAlwaysHandlers();
        assertEquals(false, checkAlwaysHandlers);
        assertTrue(scriptRunner.getException().contains("test exception"));
    }

    @Test
    public void testSendable() throws Exception {
        boolean sendable = scriptRunner.sendable();
        assertFalse(sendable);

        when(parser.getCommandsLeft()).thenReturn(10);
        sendable = scriptRunner.sendable();
        assertFalse(sendable);

        when(parser.IsExceptAllowed("", scriptRunner, matchTimeout)).thenReturn(expect);
        when(parser.IsSendAllowed()).thenReturn(send);
        sendable = scriptRunner.sendable();
        assertTrue(sendable);
    }

    @Test
    public void testProcess() throws Exception {
        boolean process = scriptRunner.process();
        assertFalse(process);
        scriptRunner.in = inputStream;
        process = scriptRunner.process();
        assertFalse(process);
        scriptRunner.in = null;
    }

}
