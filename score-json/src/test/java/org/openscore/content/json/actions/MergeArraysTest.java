package org.openscore.content.json.actions;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by ioanvranauhp
 * Date on 2/12/2015.
 */
public class MergeArraysTest {
    public static final String EXCEPTION = "exception";
    private static final String RETURN_RESULT = "returnResult";
    public static final String RETURN_CODE = "returnCode";

    private final MergeArrays mergeArrays = new MergeArrays();

    @Test
    public void testArrayWithJsonInside() throws Exception {
        String array1 = " [{\"one\":1, \"two\":2}, 3, \"four\"]";
        String array2 = "[{\"one\":1, \"two\":2}, 3, \"four\"]";
        final Map<String, String> returnResult = mergeArrays.execute(array1, array2);
        assertEquals("[{\"one\":1,\"two\":2},3,\"four\",{\"one\":1,\"two\":2},3,\"four\"]", returnResult.get(RETURN_RESULT));
    }

    @Test
    public void testSimpleIntegerArray() throws Exception {
        String array1 = "[1,2,3]";
        String array2 = "[1,2,3]";
        final Map<String, String> returnResult = mergeArrays.execute(array1, array2);
        assertEquals("[1,2,3,1,2,3]", returnResult.get(RETURN_RESULT));
    }

    @Test
    public void testSimpleStringArray() throws Exception {
        String array1 = "[\"one\",\"two\",\"three\"]";
        String array2 = "[\"one\",\"two\",\"three\"]";
        final Map<String, String> returnResult = mergeArrays.execute(array1, array2);
        assertEquals("[\"one\",\"two\",\"three\",\"one\",\"two\",\"three\"]", returnResult.get(RETURN_RESULT));
    }

    @Test
    public void testEmptyArray1() throws Exception {
        String array1 = "";
        String array2 = "[1,2,3]";
        final Map<String, String> returnResult = mergeArrays.execute(array1, array2);
        assertEquals("The input value is not a valid JavaScript array array1", returnResult.get(RETURN_RESULT));
        assertEquals("The input value is not a valid JavaScript array array1", returnResult.get(EXCEPTION));
        assertEquals("-1", returnResult.get(RETURN_CODE));
    }

    @Test
    public void testNullArray1() throws Exception {
        String array2 = "[1,2,3]";
        final Map<String, String> returnResult = mergeArrays.execute(null, array2);
        assertEquals("The input value is not a valid JavaScript array array1", returnResult.get(RETURN_RESULT));
        assertEquals("The input value is not a valid JavaScript array array1", returnResult.get(EXCEPTION));
        assertEquals("-1", returnResult.get(RETURN_CODE));
    }

    @Test
    public void testEmptyArray2() throws Exception {
        String array1 = "[1,2,3]";
        String array2 = "";
        final Map<String, String> returnResult = mergeArrays.execute(array1, array2);
        assertEquals("The input value is not a valid JavaScript array array2", returnResult.get(RETURN_RESULT));
        assertEquals("The input value is not a valid JavaScript array array2", returnResult.get(EXCEPTION));
        assertEquals("-1", returnResult.get(RETURN_CODE));
    }

    @Test
    public void testNullArray2() throws Exception {
        String array1 = "[1,2,3]";
        final Map<String, String> returnResult = mergeArrays.execute(array1, null);
        assertEquals("The input value is not a valid JavaScript array array2", returnResult.get(RETURN_RESULT));
        assertEquals("The input value is not a valid JavaScript array array2", returnResult.get(EXCEPTION));
        assertEquals("-1", returnResult.get(RETURN_CODE));
    }

    @Test
    public void testInvalidArray1() throws Exception {
        String array1 = "[dfgs][!@##$%^&*";
        String array2 = "[1,2,3]";
        final Map<String, String> returnResult = mergeArrays.execute(array1, array2);
        assertEquals("Invalid jsonObject provided!  array1=[dfgs][!@##$%^&*", returnResult.get(RETURN_RESULT));
        assertTrue(returnResult.get(EXCEPTION).toLowerCase().startsWith("unrecognized token 'dfgs'"));
        assertEquals("-1", returnResult.get(RETURN_CODE));
    }

    @Test
    public void testInvalidArray2() throws Exception {
        String array1 = "[1,2,3]";
        String array2 = "[dfgs][!@##$%^&*";
        final Map<String, String> returnResult = mergeArrays.execute(array1, array2);
        assertEquals("Invalid jsonObject provided!  array2=[dfgs][!@##$%^&*", returnResult.get(RETURN_RESULT));
        assertTrue(returnResult.get(EXCEPTION).toLowerCase().startsWith("unrecognized token 'dfgs'"));
        assertEquals("-1", returnResult.get(RETURN_CODE));
    }

    @Test
    public void testMergeEmptyArray1() throws Exception {
        String array1 = "[]";
        String array2 = "[1,2,3]";
        final Map<String, String> returnResult = mergeArrays.execute(array1, array2);
        assertEquals("[1,2,3]", returnResult.get(RETURN_RESULT));
        assertEquals("0", returnResult.get(RETURN_CODE));
    }

    @Test
    public void testMergeEmptyArray2() throws Exception {
        String array1 = "[1,2,3]";
        String array2 = "[]";
        final Map<String, String> returnResult = mergeArrays.execute(array1, array2);
        assertEquals("[1,2,3]", returnResult.get(RETURN_RESULT));
        assertEquals("0", returnResult.get(RETURN_CODE));
    }

    @Test
    public void testMergeEmptyBothArrays() throws Exception {
        String array1 = "[]";
        String array2 = "[]";
        final Map<String, String> returnResult = mergeArrays.execute(array1, array2);
        assertEquals("[]", returnResult.get(RETURN_RESULT));
        assertEquals("0", returnResult.get(RETURN_CODE));
    }

    @Test
    public void testMergeLargeArrays() throws Exception {
        StringBuilder array = new StringBuilder("[");
        final int size = 30000;
        for (int i = 0; i < size; i++) {
            array.append(i).append(",");
        }
        array.append(size).append("]");

        StringBuilder result = new StringBuilder(array);
        result.append(array);
        final String arraysSeparators = "][";
        final int start = result.indexOf(arraysSeparators);
        result.replace(start, start + arraysSeparators.length(), ",");

        final Map<String, String> returnResult = mergeArrays.execute(array.toString(), array.toString());
        assertEquals(result.toString(), returnResult.get(RETURN_RESULT));
        assertEquals("0", returnResult.get(RETURN_CODE));
    }
}
