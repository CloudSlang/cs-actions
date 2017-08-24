/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.utilities.actions;

import org.junit.Test;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static junit.framework.Assert.assertEquals;

/**
 * Created by moldovai on 8/22/2017.
 */
public class DefaultIfEmptyTest {

    private final DefaultIfEmptyAction d = new DefaultIfEmptyAction();

    @Test
    public void testWithBlankString() {
        final Map<String, String> result = d.execute("   ", "string", "  ");
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithEmptyString() {
        final Map<String, String> result = d.execute("", "string", "");
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithTrimString() {
        final Map<String, String> result = d.execute("", "string", "astring");
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("Failure", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithTrimInitialString() {
        final Map<String, String> result = d.execute("initial", "string", "astring");
        assertEquals("-1", result.get(RETURN_CODE));
        assertEquals("Failure", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingInitialString() {
        final Map<String, String> result = d.execute("initialstr", "string", "");
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithTrimTrue() {
        final Map<String, String> result = d.execute("   ", "string", "true");
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithTrimFalse() {
        final Map<String, String> result = d.execute("", "string", "false");
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingInitialStringAndTrimTrue() {
        final Map<String, String> result = d.execute("initialstr", "string", "true");
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingInitialStringAndTrimFalse() {
        final Map<String, String> result = d.execute("initialstr", "string", "false");
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingTrimNull() {
        final Map<String, String> result = d.execute("", "string", null);
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingInitialStringNull() {
        final Map<String, String> result = d.execute(null, "string", "false");
        assertEquals("0", result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }
}
