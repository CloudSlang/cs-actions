package io.cloudslang.content.actions;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ListSizeActionTest {

    @Test
    public void testSize() {
        String list = "ana,ion,vasile,marian,george";
        Map<String, String> result = new ListSizeAction().getListSize(list, ",");
        assertEquals("5", result.get("result"));
    }
}
