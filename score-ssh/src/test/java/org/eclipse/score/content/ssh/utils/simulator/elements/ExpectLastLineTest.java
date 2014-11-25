package org.eclipse.score.content.ssh.utils.simulator.elements;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by vranau on 11/25/2014.
 */
public class ExpectLastLineTest {

    @Test
    public void testMatch() throws Exception {
        ExpectLastLine expectLastLine = new ExpectLastLine();
        expectLastLine.set("ls");
        assertEquals(true, expectLastLine.match("ls", 10));

        expectLastLine.set("");
        assertEquals(true, expectLastLine.match("", 10));
        expectLastLine.set(null);
        assertEquals(true, expectLastLine.match("", 10));

        expectLastLine.set("ls");
        assertEquals(false, expectLastLine.match("", 10));

        expectLastLine.set("ls");
        assertEquals(false, expectLastLine.match(null, 10));

        expectLastLine.set("ls");
        assertEquals(false, expectLastLine.match(null, 0));

        expectLastLine.set("ls");
        assertEquals(false, expectLastLine.match(null, -3453));
    }

}