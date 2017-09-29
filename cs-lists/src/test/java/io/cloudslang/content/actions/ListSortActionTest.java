/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.actions;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ListSortActionTest {

    private static final String LIST_STRING = "z,a,g,b,s";
    private static final String LIST_INTEGER = "10,5,8,1,6";

    @Test
    public void testSortLettersReverse() {
        Map<String, String> result = new ListSortAction().sortList(LIST_STRING, ",", "true");
        assertEquals("success", result.get("response"));
        assertEquals("z,s,g,b,a", result.get("result"));
    }

    @Test
    public void testSortLetters() {
        Map<String, String> result1 = new ListSortAction().sortList(LIST_STRING, ",", "false");
        assertEquals("success", result1.get("response"));
        assertEquals("a,b,g,s,z", result1.get("result"));
    }

    @Test
    public void testSortIntegers() {
        Map<String, String> result2 = new ListSortAction().sortList(LIST_INTEGER, ",", "true");
        assertEquals("success", result2.get("response"));
        assertEquals("10,8,6,5,1", result2.get("result"));
    }

    @Test
    public void testSortIntegersInvalidReverse() {
        Map<String, String> result2 = new ListSortAction().sortList(LIST_INTEGER, ",", "sdfsd");
        assertEquals("success", result2.get("response"));
        assertEquals("1,5,6,8,10", result2.get("result"));
    }
}
