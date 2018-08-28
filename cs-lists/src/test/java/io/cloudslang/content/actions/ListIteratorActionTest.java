package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

public class ListIteratorActionTest {

    private static final String LIST_STRING = "Maria, Ioana, Matei, Gicu, George";
    private static final String LIST_INTEGER = "12, 11, 10, 154, 22, 1";
    private static final String EMPTY_LIST = ",,,";
    private static final String RETURN_STRING = "returnResult";

    @Test
    public void testListIteratorSuccess() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute(LIST_STRING, ",", globalSessionObject);
        assertEquals("has more", result.get("result"));
        assertEquals("Maria", result.get(RETURN_STRING));
        assertEquals("0", result.get("index"));
    }

    @Test
    public void testListIteratorFails() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute(LIST_STRING, "", globalSessionObject);
        assertEquals("failed", result.get("result"));
        assertEquals("delimiter has null or 0 length", result.get(RETURN_STRING));
    }

    @Test
    public void testListIteratorEmptyList() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute("", ";", globalSessionObject);
        assertEquals("failed", result.get("result"));
        assertEquals("list has null or 0 length", result.get(RETURN_STRING));
    }

    @Test
    public void testListIteratorEmptyListAndSeparator() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute("", "", globalSessionObject);
        assertEquals("failed", result.get("result"));
        assertEquals("delimiter has null or 0 length", result.get(RETURN_STRING));
    }

    @Test
    public void testListIteratorEmptyList2() {
        final GlobalSessionObject<Map<String, Object>> globalSessionObject = new GlobalSessionObject<>();
        Map<String, String> result = new ListIteratorAction().execute(EMPTY_LIST, ",", globalSessionObject);
        assertEquals("no more", result.get("result"));
    }

}
