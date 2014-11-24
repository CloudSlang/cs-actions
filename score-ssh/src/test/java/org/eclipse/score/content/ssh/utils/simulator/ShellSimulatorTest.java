package org.eclipse.score.content.ssh.utils.simulator;

import org.eclipse.score.content.ssh.utils.simulator.visualization.IShellVisualizer;
import org.eclipse.score.content.ssh.utils.simulator.visualization.ScreenEmulator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by vranau on 11/24/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ShellSimulator.class, ScriptRunner.class, System.class})
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
    private ScriptRunner simulator;

    @Mock
    private ScreenEmulator screenEmulator;

    @Mock
    private InputStream inputStream;

    @Mock
    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        PowerMockito.whenNew(ScriptRunner.class).withAnyArguments().thenReturn(simulator);
        PowerMockito.when(simulator.getException()).thenReturn("");
        PowerMockito.whenNew(ScreenEmulator.class).withAnyArguments().thenReturn(screenEmulator);


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

        shellSimulator.addVisualizer(screenEmulator);
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

        shellSimulator.setStreams(inputStream, outputStream);
        assertEquals(inputStream, simulator.in);
        assertEquals(outputStream, simulator.out);
    }

    @Test
    public void testGetOutput() {
        PowerMockito.when(simulator.getOutput()).thenReturn("output");
        final String output = shellSimulator.getOutput();
        assertEquals("output", output);
    }

    @Test
    public void testGetException() throws Exception {

        PowerMockito.when(simulator.noMoreCommandsLeft()).thenReturn(true);
        String exception = shellSimulator.getException();
        assertEquals("", exception);

        PowerMockito.when(simulator.noMoreCommandsLeft()).thenReturn(false);
        exception = shellSimulator.getException();
        assertEquals(errorMessage, exception);
    }

    @Test
    public void testNoMoreCommandsLeft() {
        assertEquals(false, shellSimulator.noMoreCommandsLeft());
        PowerMockito.when(simulator.noMoreCommandsLeft()).thenReturn(true);
        assertEquals(true, shellSimulator.noMoreCommandsLeft());
    }

    @Test
    public void testRun() throws Exception {

        PowerMockito.when(simulator.isAlive()).thenReturn(false);
        shellSimulator.run(-1L);
    }
}
