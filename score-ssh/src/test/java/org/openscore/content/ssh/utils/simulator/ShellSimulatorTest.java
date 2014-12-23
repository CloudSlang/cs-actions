package org.openscore.content.ssh.utils.simulator;

import org.openscore.content.ssh.utils.simulator.ScriptRunner;
import org.openscore.content.ssh.utils.simulator.ShellSimulator;
import org.openscore.content.ssh.utils.simulator.visualization.IShellVisualizer;
import org.openscore.content.ssh.utils.simulator.visualization.ScreenEmulator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by vranau on 11/24/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ShellSimulator.class, ScriptRunner.class})
public class ShellSimulatorTest {

    private String script = "send ls \nexpect ls";
    private long matchTimeout = 1000L;
    private long readTimeout = 1000L;
    private long sleepTimeout = 1000L;
    private long characterDelay = 1000L;
    private String newLine = "\n";
    final String errorMessage = "Script did not fully finish, had: 0 commands left ";

    ShellSimulator shellSimulator;

    @Mock
    private ScriptRunner simulatorMock;

    @Mock
    private ScreenEmulator screenEmulatorMock;

    @Mock
    private InputStream inputStreamMock;

    @Mock
    private OutputStream outputStreamMock;

    @Before
    public void setUp() throws Exception {
        whenNew(ScriptRunner.class).withAnyArguments().thenReturn(simulatorMock);
        when(simulatorMock.getException()).thenReturn("");
        whenNew(ScreenEmulator.class).withAnyArguments().thenReturn(screenEmulatorMock);


        shellSimulator = new ShellSimulator(script, matchTimeout, readTimeout, sleepTimeout, characterDelay);
    }

    @Test
    public void testConstructors() throws Exception {
        final String defaultCharset = "UTF-8";

        ShellSimulator shellSimulator = new ShellSimulator(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, newLine.toCharArray());
        assertEquals(shellSimulator.getException(), errorMessage);

        shellSimulator = new ShellSimulator(script, matchTimeout, readTimeout, sleepTimeout, characterDelay);
        assertEquals(shellSimulator.getException(), errorMessage);

        shellSimulator = new ShellSimulator(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, newLine.toCharArray(), defaultCharset);
        assertEquals(shellSimulator.getException(), errorMessage);

        shellSimulator = new ShellSimulator(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, newLine.toCharArray(), "");
        assertEquals(shellSimulator.getException(), errorMessage);

        shellSimulator = new ShellSimulator(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, defaultCharset, true);
        assertEquals(shellSimulator.getException(), errorMessage);

        shellSimulator = new ShellSimulator(script, matchTimeout, readTimeout, sleepTimeout, characterDelay, newLine.toCharArray(), defaultCharset, false);
        assertEquals(shellSimulator.getException(), errorMessage);
    }

    @Test
    public void testAddVisualizer() throws Exception {

        IShellVisualizer iShellVisualizer = shellSimulator.addVisualizer("basic");
        assertEquals(true, iShellVisualizer instanceof ScreenEmulator);

        iShellVisualizer = shellSimulator.addVisualizer("");
        assertEquals(true, iShellVisualizer instanceof ScreenEmulator);

        String name = null;
        iShellVisualizer = shellSimulator.addVisualizer(name);
        assertEquals(true, iShellVisualizer instanceof ScreenEmulator);

        iShellVisualizer = shellSimulator.addVisualizer("none");
        assertNull(iShellVisualizer);

        shellSimulator.addVisualizer(screenEmulatorMock);
    }

    @Test
    public void testAddVisualizerWithException() throws Exception {

        String exceptionMessage = "";
        try {
            shellSimulator.addVisualizer("test");
        } catch (Exception ex) {
            exceptionMessage = ex.getMessage();
        }
        assertEquals("The specified visualizer is of unknown type", exceptionMessage);
    }

    @Test
    public void testSetStreams() throws Exception {

        shellSimulator.setStreams(inputStreamMock, outputStreamMock);
        assertEquals(inputStreamMock, simulatorMock.in);
        assertEquals(outputStreamMock, simulatorMock.out);
    }

    @Test
    public void testGetOutput() {
        when(simulatorMock.getOutput()).thenReturn("output");
        final String output = shellSimulator.getOutput();
        assertEquals("output", output);
    }

    @Test
    public void testGetException() throws Exception {

        when(simulatorMock.noMoreCommandsLeft()).thenReturn(true);
        String exception = shellSimulator.getException();
        assertEquals("", exception);

        when(simulatorMock.noMoreCommandsLeft()).thenReturn(false);
        exception = shellSimulator.getException();
        assertEquals(errorMessage, exception);
    }

    @Test
    public void testNoMoreCommandsLeft() {
        assertEquals(false, shellSimulator.noMoreCommandsLeft());
        when(simulatorMock.noMoreCommandsLeft()).thenReturn(true);
        assertEquals(true, shellSimulator.noMoreCommandsLeft());
    }

    @Test
    public void testRun() throws Exception {

        when(simulatorMock.isAlive()).thenReturn(false);
        shellSimulator.run(-1L);
    }
}
