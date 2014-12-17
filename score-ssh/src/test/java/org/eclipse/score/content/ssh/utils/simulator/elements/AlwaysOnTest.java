package org.eclipse.score.content.ssh.utils.simulator.elements;

import junit.framework.Assert;
import org.eclipse.score.content.ssh.utils.simulator.ScriptLines;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by vranau on 11/25/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({AlwaysOn.class, Send.class})
public class AlwaysOnTest {

    private String script = "send ls always\n on expect";
    private String newLine = "\n";
    private ScriptLines scriptLines = new ScriptLines(script);
    @Mock
    private Send sendMock;

    @Test
    public void testGetInstance() throws Exception {
        mockStatic(Send.class);
        when(Send.getInstance(anyString(), Matchers.<char[]>anyObject())).thenReturn(sendMock);
        AlwaysOn instance = AlwaysOn.getInstance(scriptLines, newLine.toCharArray());
        Assert.assertNotNull(instance);

        instance = AlwaysOn.getInstance(null, null);
        Assert.assertNotNull(instance);
    }

    @Test
    public void testMatch() throws Exception {
        AlwaysOn alwaysOn = new AlwaysOn();
        final Expect command = new Expect();
        command.set("ls");
        alwaysOn.setExpect(command);
        assertEquals(true, alwaysOn.match("ls", 10));
        alwaysOn = new AlwaysOn();
        assertEquals(false, alwaysOn.match("ls", 10));
    }

    @Test
    public void testGet() {
        AlwaysOn alwaysOn = new AlwaysOn();
        assertEquals("[]", Arrays.toString(alwaysOn.get()));
        final Send command = new Send();
        command.set("ls");
        alwaysOn.setCommand(command);
        assertEquals("[l, s, \n]", Arrays.toString(alwaysOn.get()));

        alwaysOn.setCommand(null);
        assertEquals("[]", Arrays.toString(alwaysOn.get()));
    }


}