package io.cloudslang.content.utils;

import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

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