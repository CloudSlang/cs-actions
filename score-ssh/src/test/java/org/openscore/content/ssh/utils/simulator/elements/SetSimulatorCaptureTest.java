package org.openscore.content.ssh.utils.simulator.elements;

import org.junit.Test;
import org.openscore.content.ssh.utils.simulator.elements.SetSimulatorCapture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by vranau on 11/25/2014.
 */
public class SetSimulatorCaptureTest {

    @Test
    public void testSet() {
        SetSimulatorCapture setSimulatorCapture = new SetSimulatorCapture();
        setSimulatorCapture.set("test");
        assertFalse(setSimulatorCapture.enable);

        setSimulatorCapture.set("");
        assertFalse(setSimulatorCapture.enable);

        setSimulatorCapture.set(null);
        assertFalse(setSimulatorCapture.enable);

        setSimulatorCapture.set("1");
        assertFalse(setSimulatorCapture.enable);

        setSimulatorCapture.set("true");
        assertEquals(true, setSimulatorCapture.enable);

        setSimulatorCapture.execute();
    }

}