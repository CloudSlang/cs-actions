package org.eclipse.score.content.ssh.utils.simulator.elements;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by vranau on 11/25/2014.
 */
public class ExpectEndsWithTest {

    @Test
    public void testMatch() throws Exception {
        ExpectEndsWith expectEndsWith = new ExpectEndsWith();
        expectEndsWith.set("ls");
        assertEquals(true, expectEndsWith.match("ls", 10));

        expectEndsWith.set("");
        assertEquals(true, expectEndsWith.match("", 10));
        expectEndsWith.set(null);
        assertEquals(true, expectEndsWith.match("", 10));

        expectEndsWith.set("ls");
        assertEquals(false, expectEndsWith.match("", 10));

        expectEndsWith.set("ls");
        assertEquals(false, expectEndsWith.match(null, 10));

        expectEndsWith.set("ls");
        assertEquals(false, expectEndsWith.match(null, 0));

        expectEndsWith.set("ls");
        assertEquals(false, expectEndsWith.match(null, -3453));
    }
}