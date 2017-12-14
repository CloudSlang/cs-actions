
package io.cloudslang.content.utils;

import org.junit.Test;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by moldovas on 7/13/2016.
 */
public class ListProcessorTest {
    @Test
    public void arrayElementsAreNull() throws Exception {
        assertTrue(ListProcessor.arrayElementsAreNull(new String[3]));
        String[] arrayOfWords = {"a","b","c"};
        assertFalse(ListProcessor.arrayElementsAreNull(arrayOfWords));
    }

    @Test
    public void getEmptyUncontainedArray() throws Exception {
        String[] subArray = {"e"};
        String[] containerArray = {"d","e","f"};
        String[] result = ListProcessor.getUncontainedArray(subArray,containerArray, true);
        List<String> listResult =  Arrays.asList(result);
        assertEquals(listResult, Collections.emptyList());
    }

    @Test
    public void getPopulatedUncontainedArray() throws Exception {
        String[] subArray = {"a","e"};
        String[] containerArray = {"d","e","f"};
        String[] result = ListProcessor.getUncontainedArray(subArray,containerArray, true);
        List<String> listResult =  Arrays.asList(result);
        assertEquals(listResult, Collections.singletonList("a"));
    }

    @Test
    public void elementsAreEqual() throws Exception {
        String ignoreCaseTrue = "true";
        String ignoreCaseFalse = "false";
        assertTrue(ListProcessor.elementsAreEqual("A", "a", Boolean.parseBoolean(ignoreCaseTrue)));
        assertFalse(ListProcessor.elementsAreEqual("A", "a", Boolean.parseBoolean(ignoreCaseFalse)));

    }

}