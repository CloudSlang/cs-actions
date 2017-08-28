/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.utilities;

import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utilities.actions.DefaultIfEmptyAction;
import org.junit.Test;
import java.util.Map;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by moldovai on 8/22/2017.
 */
public class DefaultIfEmptyTest {

    private final DefaultIfEmptyAction d = new DefaultIfEmptyAction();

    @Test
    public void testWithBlankString() {
        final Map<String, String> result = d.execute("   ", "string", "  ");
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithEmptyString() {
        final Map<String, String> result = d.execute("", "string", "");
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithTrimString() {
        final Map<String, String> result = d.execute("", "string", "astring");
        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertTrue(result.get(RETURN_RESULT).contains("The provided string cannot be converted to a boolean value"));
    }

    @Test
    public void testWithTrimInitialString() {
        final Map<String, String> result = d.execute("initial", "string", "astring");
        assertEquals(ReturnCodes.FAILURE, result.get(RETURN_CODE));
        assertTrue(result.get(RETURN_RESULT).contains("The provided string cannot be converted to a boolean value"));
    }

    @Test
    public void testHavingInitialString() {
        final Map<String, String> result = d.execute("initialstr", "string", "");
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithTrimTrue() {
        final Map<String, String> result = d.execute("   ", "string", "true");
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testWithTrimFalse() {
        final Map<String, String> result = d.execute("", "string", "false");
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingInitialStringAndTrimTrue() {
        final Map<String, String> result = d.execute("initialstr", "string", "true");
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingInitialStringAndTrimFalse() {
        final Map<String, String> result = d.execute("initialstr", "string", "false");
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingTrimNull() {
        final Map<String, String> result = d.execute("", "string", null);
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingInitialStringNull() {
        final Map<String, String> result = d.execute(null, "string", "false");
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingTrimVariableWrittenInLowerAndUpperCase() {
        final Map<String, String> result = d.execute(null, "string", "    fAlSE  ");
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("string", result.get(RETURN_RESULT));
    }

    @Test
    public void testHavingTrimVariableInUpperCase() {
        final Map<String, String> result = d.execute("initialstr", "string", "  TRUE     ");
        assertEquals(ReturnCodes.SUCCESS, result.get(RETURN_CODE));
        assertEquals("initialstr", result.get(RETURN_RESULT));
    }
}