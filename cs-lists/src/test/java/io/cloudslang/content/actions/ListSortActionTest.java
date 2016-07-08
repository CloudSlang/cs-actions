package io.cloudslang.content.actions;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ListSortActionTest {

    @Test
    public void testSort() {
        String listString = "z,a,g,b,s";
        String listInteger = "10,5,8,1,6";

        Map<String, String> result = new ListSortAction().sortList(listString, ",", true);
        assertEquals("success", result.get("response"));
        assertEquals("z,s,g,b,a", result.get("result"));

        Map<String, String> result2 = new ListSortAction().sortList(listInteger, ",", true);
        assertEquals("success", result2.get("response"));
        assertEquals("10,8,6,5,1", result2.get("result"));
    }
}
