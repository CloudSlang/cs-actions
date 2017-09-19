/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.utils;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertFalse;


/**
 * Created by victor on 04.09.2016.
 */
public class OtherUtilitiesTest {
    @Test
    public void isValidEmail() throws Exception {
        assertTrue(OtherUtilities.isValidEmail("a.b@g.com"));
        assertTrue(OtherUtilities.isValidEmail("aaa.bbb@ggg.com"));
        assertTrue(OtherUtilities.isValidEmail("qweqwe@a.com"));
        assertFalse(OtherUtilities.isValidEmail(""));
        assertFalse(OtherUtilities.isValidEmail("a.b@c"));
        assertFalse(OtherUtilities.isValidEmail("qw..eqwe@."));
    }


    @Test
    public void isValidIpPort() throws Exception {
        assertTrue(OtherUtilities.isValidIpPort("9000"));
        assertTrue(OtherUtilities.isValidIpPort("5000"));
        assertFalse(OtherUtilities.isValidIpPort("-1"));
        assertFalse(OtherUtilities.isValidIpPort("9999999999999999999"));
        assertFalse(OtherUtilities.isValidIpPort("65539"));
        assertFalse(OtherUtilities.isValidIpPort("65536"));

    }

    @Test
    public void isValidIp() throws Exception {
        assertTrue(OtherUtilities.isValidIp("1.1.1.1"));
        assertTrue(OtherUtilities.isValidIp("255.255.255.255"));
        assertTrue(OtherUtilities.isValidIp("192.168.1.1"));
        assertTrue(OtherUtilities.isValidIp("::1"));
        assertTrue(OtherUtilities.isValidIp("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
        assertFalse(OtherUtilities.isValidIp(".1.1.1"));
        assertFalse(OtherUtilities.isValidIp("2001:0db8:85a3:0000:0000:8a2e:0370:73349999999"));
        assertFalse(OtherUtilities.isValidIp("2001:0db8:80123---5a3:0000:0000:8a2e:0370:7334"));
    }

    @Test
    public void changeNewLineFromWindowsToUnix() throws Exception {
        assertEquals(OtherUtilities.changeNewLineFromWindowsToUnix("abc\r\ndef"), "abc\ndef");
        assertEquals(OtherUtilities.changeNewLineFromWindowsToUnix("\r\n"), "\n");
        assertEquals(OtherUtilities.changeNewLineFromWindowsToUnix(""), "");
    }

    @Test
    public void changeNewLineFromUnixToWindows() throws Exception {
        assertEquals(OtherUtilities.changeNewLineFromUnixToWindows("abc\ndef"), "abc\r\ndef");
        assertEquals(OtherUtilities.changeNewLineFromUnixToWindows("\n"), "\r\n");
        assertEquals(OtherUtilities.changeNewLineFromUnixToWindows(""), "");
    }

}