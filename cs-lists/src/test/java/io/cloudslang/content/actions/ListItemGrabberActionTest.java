
package io.cloudslang.content.actions;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ListItemGrabberActionTest {

    private static final String LIST = "Ana,Ion,Vasile,Maria,George";

    @Test
    public void testGetItem() {
        Map<String, String> result = new ListItemGrabberAction().grabItemFromList(LIST, ",", "1");
        assertEquals("success", result.get("response"));
        assertEquals("Ion", result.get("result"));
    }
}
