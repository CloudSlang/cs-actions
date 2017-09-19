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

import io.cloudslang.content.constants.OutputNames;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Created by victor on 05.09.2016.
 */
public class OutputUtilitiesTest {

    @Test
    public void getSuccessResultsMap() throws Exception {
        Map<String, String> result = OutputUtilities.getSuccessResultsMap("a");
        assertEquals(result.get(OutputNames.RETURN_RESULT), "a");
        assertEquals(result.get(OutputNames.RETURN_CODE), "0");
    }

    @Test
    public void getFailureResultsMapString() throws Exception {
        Map<String, String> result = OutputUtilities.getFailureResultsMap("a");
        assertEquals(result.get(OutputNames.RETURN_RESULT), "a");
        assertEquals(result.get(OutputNames.EXCEPTION), "a");
        assertEquals(result.get(OutputNames.RETURN_CODE), "-1");
    }

    @Test
    public void getFailureResultsMapThrowable() throws Exception {
        Map<String, String> result = OutputUtilities.getFailureResultsMap(new Exception("b"));
        assertEquals(result.get(OutputNames.RETURN_RESULT), "b");
        assertTrue(result.get(OutputNames.EXCEPTION).startsWith("java.lang.Exception: b"));
        assertEquals(result.get(OutputNames.RETURN_CODE), "-1");
    }

}