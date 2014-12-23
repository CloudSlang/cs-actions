package org.openscore.content.ssh.utils.simulator;

import org.junit.Before;
import org.junit.Test;
import org.openscore.content.ssh.utils.simulator.ScriptLines;

import static org.junit.Assert.*;

/**
 * Created by vranau on 11/25/2014.
 */
public class ScriptLinesTest {

    private ScriptLines scriptLines;
    private String script = "send ls \nexpect ls";

    @Before
    public void setUp() throws Exception {
        scriptLines = new ScriptLines(script);
    }

    @Test
    public void testPeekNext() {
        assertEquals("expect ls", scriptLines.peekNext());
        ScriptLines scriptLinesNew = new ScriptLines(script) {
            @Override
            public boolean hasNext() {
                return false;
            }
        };
        assertNull(scriptLinesNew.peekNext());
    }

    @Test
    public void testToString() {
        assertEquals("Script:currently on line: 1\n" +
                "1 send ls2 expect ls", scriptLines.toString());
    }

    @Test
    public void testNextLine() {
        assertEquals(true, scriptLines.nextLine());
        ScriptLines scriptLinesNew = new ScriptLines(script) {
            @Override
            public boolean hasNext() {
                return false;
            }
        };
        assertFalse(scriptLinesNew.nextLine());
    }
}