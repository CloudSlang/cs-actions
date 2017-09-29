/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.amazon.utils;

import org.junit.Test;

import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by persdana
 * 7/13/2015.
 */
public class ExceptionProcessorTest {
    private static final String STACK_TRACE = "java.lang.Exception: abc";

    @Test
    public void testGetExceptionResult() {
        Map<String, String> result = ExceptionProcessor.getExceptionResult(new Exception("abc"));
        String exception = result.get("exception");

        assertEquals("abc", result.get("returnResult"));
        assertEquals("-1", result.get("returnCode"));
        assertTrue(exception.contains(STACK_TRACE));
    }
}
