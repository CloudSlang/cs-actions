package org.openscore.content.ssh.utils.simulator.visualization;

import com.jcraft.jcterm.Connection;
import com.jcraft.jcterm.EmulatorVT100;
import org.dom4j.IllegalAddException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openscore.content.ssh.utils.simulator.visualization.ScreenEmulator;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by vranau on 11/25/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ScreenEmulator.class})
public class ScreenEmulatorTest {

    private ScreenEmulator screenEmulator;
    @Mock
    private InputStream inputStream;
    @Mock
    private Connection connection;
    @Mock
    private EmulatorVT100 emulator;

    @Before
    public void setUp() throws Exception {
        screenEmulator = new ScreenEmulator();
    }

    @Test
    public void testRun() throws Exception {
        whenNew(EmulatorVT100.class).withAnyArguments().thenReturn(emulator);
        screenEmulator.run(inputStream);
        verify(emulator).reset();
        verify(emulator).start();
    }

    @Test
    public void testGetXmlSummary() {
        assertEquals("<Frames>\n</Frames>", screenEmulator.getXMLSummary());
        screenEmulator.beep();
        assertEquals("<Frames>\n" +
                "<Beep lastFrameIndex=\"0\"/>\n" +
                "</Frames>", screenEmulator.getXMLSummary());
        ScreenEmulator screenEmulatorNew = new ScreenEmulator();
        screenEmulatorNew.snapshotBuffer(true);
        assertEquals("<Frames>\n</Frames>", screenEmulatorNew.getXMLSummary());
    }

    @Test
    public void testBufferAsString() {
        String bufferAsString = screenEmulator.bufferAsString(true);
        assertEquals("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", bufferAsString);

        bufferAsString = screenEmulator.bufferAsString(false);
        assertEquals("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", bufferAsString);
    }

    @Test
    public void testSnapshotBuffer() {
        String snapshotBuffer = screenEmulator.snapshotBuffer(true);
        assertEquals("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", snapshotBuffer);

        snapshotBuffer = screenEmulator.snapshotBuffer(false);
        assertEquals("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", snapshotBuffer);

        ScreenEmulator screenEmulatorNew = new ScreenEmulator() {
            @Override
            public String bufferAsString(boolean clearChanges) {
                return "Frame";
            }
        };
        snapshotBuffer = screenEmulatorNew.snapshotBuffer(true);
        assertEquals("Frame", snapshotBuffer);
        snapshotBuffer = screenEmulatorNew.snapshotBuffer(true);
        assertEquals("Frame", snapshotBuffer);
    }

    @Test
    public void testBeep() {
        screenEmulator.beep();
        assertEquals("<Frames>\n<Beep lastFrameIndex=\"0\"/>\n</Frames>", screenEmulator.getXMLSummary());
    }

    @Test(expected = IllegalAddException.class)
    public void testBeepWithException() {
        screenEmulator.beep();
        assertEquals("<Frames>\n<Beep lastFrameIndex=\"0\"/>\n</Frames>", screenEmulator.getXMLSummary());
        screenEmulator.beep();
        assertEquals("<Frames>\n<Beep lastFrameIndex=\"0\"/>\n</Frames>", screenEmulator.getXMLSummary());

    }

    @Test
    public void testClear() {
        assertEquals("<Frames>\n</Frames>", screenEmulator.getXMLSummary());
        screenEmulator.clear();
        assertEquals("<Frames>\n</Frames>", screenEmulator.getXMLSummary());
    }

    @Test
    public void testClearArea() {
        screenEmulator.clear_area(2, 1, 10, 10);
    }

    @Test
    public void testDrawString() {
        screenEmulator.drawString("string", 2, 3);
    }

    @Test
    public void testGetters() {
        assertEquals(1, screenEmulator.getCharHeight());
        assertEquals(1, screenEmulator.getCharWidth());
        assertEquals(null, screenEmulator.getColor(333));
        assertEquals(80, screenEmulator.getColumnCount());
        assertEquals(24, screenEmulator.getRowCount());
        assertEquals(24, screenEmulator.getTermHeight());
        assertEquals(80, screenEmulator.getTermWidth());
    }

    @Test
    public void testScrollArea() {
        screenEmulator.scroll_area(0, 0, 0, 0, 0, 0);
    }
}