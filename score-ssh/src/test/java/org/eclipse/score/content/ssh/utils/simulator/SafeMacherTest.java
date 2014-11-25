package org.eclipse.score.content.ssh.utils.simulator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by vranau on 11/25/2014.
 */
public class SafeMacherTest {

    @Test
    public void testMatch() throws Exception {
        assertEquals(false, SafeMatcher.match("toTest", "regex", 100));
    }
}