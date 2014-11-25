package org.eclipse.score.content.ssh.utils.simulator.visualization;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by vranau on 11/25/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ScreenEmulator.class})
public class ByteWrapperTest {

    private ByteWrapper byteWrapper;

    @Mock
    private ScreenEmulator screenEmulator;

    @Before
    public void setUp() throws Exception {
        byteWrapper = new ByteWrapper(screenEmulator);
    }

    @Test
    public void testGetValue() {
        assertEquals(32, byteWrapper.getValue());
    }
    @Test
    public void testSetValue() {
        byte byteValue = 3;
        byteWrapper.setValue(byteValue);
        assertEquals(3, byteWrapper.getValue());
        byteWrapper.setValue(byteValue);
        assertEquals(3, byteWrapper.getValue());
        Mockito.verify(screenEmulator).snapshotBuffer(true);
    }
    @Test
    public void testClearChanged() {
        byte byteValue = 3;
        byteWrapper.setValue(byteValue);
        assertEquals(3, byteWrapper.getValue());
        byteWrapper.clearChanged();
        byteWrapper.setValue(byteValue);
        assertEquals(3, byteWrapper.getValue());
        Mockito.verifyZeroInteractions(screenEmulator);
    }

    @Test
    public void testClear() {
        assertEquals(32, byteWrapper.getValue());
        byteWrapper.clear();
        assertEquals(32, byteWrapper.getValue());
        byte byteValue = 3;
        byteWrapper.setValue(byteValue);
        assertEquals(3, byteWrapper.getValue());
        byteWrapper.clear();
        assertEquals(32, byteWrapper.getValue());
        byteWrapper.setValue(byteValue);
        assertEquals(3, byteWrapper.getValue());
        byteWrapper.clearChanged();
        assertEquals(3, byteWrapper.getValue());
        byteWrapper.clear();
        assertEquals(32, byteWrapper.getValue());
    }
    @Test
    public void t() {

    }
}