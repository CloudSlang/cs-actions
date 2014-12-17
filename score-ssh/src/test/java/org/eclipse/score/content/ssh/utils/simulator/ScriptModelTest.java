package org.eclipse.score.content.ssh.utils.simulator;

import org.eclipse.score.content.ssh.utils.simulator.elements.AlwaysOn;
import org.eclipse.score.content.ssh.utils.simulator.elements.Expect;
import org.eclipse.score.content.ssh.utils.simulator.elements.Send;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by vranau on 11/25/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ScriptModel.class, AlwaysOn.class})
public class ScriptModelTest {


    @Mock
    private AlwaysOn alwaysOnMock;
    @Mock
    private ScriptRunner scriptRunnerMock;

    private ScriptModel scriptModel;
    private String script = "wait 1000\nexpect 1000\nsend ls \n always";
    private String newLine = "\n";
    private long matchTimeout = 1000L;


    @Before
    public void setUp() throws Exception {
        mockStatic(AlwaysOn.class);
        when(AlwaysOn.getInstance(Matchers.<ScriptLines>anyObject(), Matchers.<char[]>anyObject())).thenReturn(alwaysOnMock);
        scriptModel = new ScriptModel(script, newLine.toCharArray());
    }

    @Test
    public void testCheckAlwaysHandlers() throws Exception {

        AlwaysOn alwaysOn = scriptModel.checkAlwaysHandlers("", matchTimeout);
        assertNull(alwaysOn);
        when(alwaysOnMock.match("", matchTimeout)).thenReturn(true);
        alwaysOn = scriptModel.checkAlwaysHandlers("", matchTimeout);
        assertEquals(this.alwaysOnMock, alwaysOn);
    }

    @Test
    public void testIsExceptAllowed() throws Exception {
        Expect expect = scriptModel.IsExceptAllowed("1000", scriptRunnerMock, matchTimeout);
        assertNotNull(expect);
        expect = scriptModel.IsExceptAllowed("", scriptRunnerMock, matchTimeout);
        assertNull(expect);
    }

    @Test
    public void testIsSendAllowed() throws Exception {
        Send send = scriptModel.IsSendAllowed();
        assertNull(send);
        ScriptModel scriptModelnew = new ScriptModel("send ls", newLine.toCharArray());
        send = scriptModelnew.IsSendAllowed();
        assertNotNull(send);
        assertFalse(send.waitIfNeeded());
        assertEquals("ls" + newLine, String.valueOf(send.get()));
    }

    @Test
    public void testGetCommandsLeft() {
        assertEquals(2, scriptModel.getCommandsLeft());
    }
}
