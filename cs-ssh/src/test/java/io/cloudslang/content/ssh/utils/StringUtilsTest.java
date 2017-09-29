/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.ssh.utils;

import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {

    @Test
    public void testToBoolean() {
        boolean result = StringUtils.toBoolean("true", false);
        assertEquals(result, true);

        result = StringUtils.toBoolean("false", false);
        assertEquals(result, false);

        result = StringUtils.toBoolean("", false);
        assertEquals(result, false);

        result = StringUtils.toBoolean("", true);
        assertEquals(result, true);

        result = StringUtils.toBoolean(null, false);
        assertEquals(result, false);

        result = StringUtils.toBoolean("", true);
        assertEquals(result, true);
    }

    @Test
    public void testToInt() {
        int result = StringUtils.toInt("10", 11);
        assertEquals(result, 10);

        result = StringUtils.toInt("", 11);
        assertEquals(result, 11);

        result = StringUtils.toInt(null, 11);
        assertEquals(result, 11);
    }

    @Test(expected = NumberFormatException.class)
    public void testToIntException() {
        int result = StringUtils.toInt("1kuhasd", 11);
        assertEquals(result, 10);
    }

    @Test
    public void testToNotEmptyString() {
        String result = StringUtils.toNotEmptyString("test", "default");
        assertEquals(result, "test");

        result = StringUtils.toNotEmptyString("", "default");
        assertEquals(result, "default");

        result = StringUtils.toNotEmptyString(null, "default");
        assertEquals(result, "default");
    }

    @Test(expected = java.lang.RuntimeException.class)
    public void testToNewlineException() throws Exception {
        String result = StringUtils.toNewline("test");
        assertEquals(result, "");
    }

    @Test
    public void testToNewline() {
        String result = StringUtils.toNewline("\\n");
        assertEquals(result, "\n");

        result = StringUtils.toNewline("\\r");
        assertEquals(result, "\r");

        result = StringUtils.toNewline("");
        assertEquals(result, "\n");

        result = StringUtils.toNewline(",,");
        assertEquals(result, "");

        result = StringUtils.toNewline("0,,");
        assertEquals(result, "\u0000");

        result = StringUtils.toNewline("0,0,0");
        assertEquals(result, "\u0000\u0000\u0000");

        result = StringUtils.toNewline("0,38,64,116");
        assertEquals(result, "\u0000&@t");
    }

    @Test
    public void testGetStackTraceAsString() {
        Exception exception = new Exception("Test exception");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        assertEquals(sw.toString(), StringUtils.getStackTraceAsString(exception));
        try {
            sw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw.close();

        exception = new Exception();
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        assertEquals(sw.toString(), StringUtils.getStackTraceAsString(exception));
        try {
            sw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw.close();
        assertEquals("", StringUtils.getStackTraceAsString(null));
    }
}