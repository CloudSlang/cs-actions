package org.eclipse.score.content.ssh.utils.simulator.elements;

import junit.framework.Assert;
import org.eclipse.score.content.ssh.utils.simulator.IScriptRunner;
import org.eclipse.score.content.ssh.utils.simulator.ScriptModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;

/**
 * Created by vranau on 11/25/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SimulatorModificationCommand.class})
public class SimulatorModificationCommandTest {

    private ScriptModel scriptModel;
    private String script = "send ls \nexpect ls";
    private String newLine = "\n";

    @Mock
    private IScriptRunner simulator;

    @Test
    public void testGetInstance() throws Exception {
        final SimulatorModificationCommand simulatorModificationCommand = SimulatorModificationCommand.getInstance("captureOutput", simulator);
        assertNotNull(simulatorModificationCommand);

    }

}