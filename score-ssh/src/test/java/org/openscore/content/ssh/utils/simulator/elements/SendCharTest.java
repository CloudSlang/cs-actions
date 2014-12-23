package org.openscore.content.ssh.utils.simulator.elements;

import org.junit.Test;
import org.openscore.content.ssh.utils.simulator.elements.SendChar;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by vranau on 11/25/2014.
 */
public class SendCharTest {

    @Test
    public void testSet() {
        SendChar sendChar = new SendChar();
        sendChar.set("ls");
        assertEquals("[\u0000]", Arrays.toString(sendChar.get()));

        sendChar.set("0");
        assertEquals("[\u0000]", Arrays.toString(sendChar.get()));

        sendChar.set("32");
        assertEquals("[ ]", Arrays.toString(sendChar.get()));

        sendChar.set("89");
        assertEquals("[Y]", Arrays.toString(sendChar.get()));

        sendChar.set(null);
        assertEquals("[\u0000]", Arrays.toString(sendChar.get()));
    }

}