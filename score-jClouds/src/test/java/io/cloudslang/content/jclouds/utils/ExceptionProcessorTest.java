package io.cloudslang.content.jclouds.utils;

import org.junit.Test;

import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by persdana on 7/13/2015.
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