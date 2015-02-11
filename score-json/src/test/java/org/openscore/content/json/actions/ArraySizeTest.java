package org.openscore.content.json.actions;

import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

/**
 * Created by vranau
 * Date 2/11/2015.
 */
public class ArraySizeTest {
    private static final String RETURN_RESULT = "returnResult";

    private final ArraySize arraySize = new ArraySize();

    @Test
    public void testWithBasicArray() {
        String array = "[ \"apple\", \"banana\" ]";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("2", execute.get(RETURN_RESULT));
    }

    @Test
    public void testWithArrayWithArray() {
        String array = "[ \"apple\", \"pencil\", [ 0, 3, 5, -199 ] ]";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("3", execute.get(RETURN_RESULT));
    }

    @Test
    public void testWithArrayWithNullElementsInArray() {
        String array = "[ \"apple\", null, \"banana\", null ] ";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("4", execute.get(RETURN_RESULT));
    }

    @Test
    public void testWithEmptyArray() {
        String array = "[]";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("0", execute.get(RETURN_RESULT));
    }

    @Test
    public void testWithBadFormatArray() {
        String array = "[";
        Map<String, String> execute = arraySize.execute(array);
        assertEquals("Invalid jsonObject provided! java.io.EOFException: End of input at line 1 column 2", execute.get(RETURN_RESULT));

        array = "[,`~!@#$%^&*()_+}{-=[]:|<>?<>]";
        execute = arraySize.execute(array);
        assertEquals("Invalid jsonObject provided! java.io.EOFException: End of input at line 1 column 31", execute.get(RETURN_RESULT));
        assertEquals("java.io.EOFException: End of input at line 1 column 31", execute.get("exception"));
        assertEquals("-1", execute.get("returnCode"));
    }

    @Test
    public void testWithEmptyInput() {
        String array = "";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("The input value is not a valid JavaScript array!", execute.get(RETURN_RESULT));
        assertEquals("The input value is not a valid JavaScript array!", execute.get("exception"));
        assertEquals("-1", execute.get("returnCode"));
    }

    @Test
    public void testWithNullInput() {
        final Map<String, String> execute = arraySize.execute(null);
        assertEquals("The input value is not a valid JavaScript array!", execute.get(RETURN_RESULT));
        assertEquals("The input value is not a valid JavaScript array!", execute.get("exception"));
        assertEquals("-1", execute.get("returnCode"));
    }

    @Test
    public void testWithJsonObject() {
        String array = " { \"city\" : \"Palo Alto\", \n" +
                "     \"state\" : \"CA\" }";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("The input value is not a valid JavaScript array!", execute.get(RETURN_RESULT));
    }

    @Test
    public void testWithJsonObjectWithArrayInside() {
        String array = " { \"city\" : \"[Palo Alto, CA]\", \n" +
                "     \"state\" : \"CA\" }";
        final Map<String, String> execute = arraySize.execute(array);
        assertEquals("The input value is not a valid JavaScript array!", execute.get(RETURN_RESULT));
    }
}
