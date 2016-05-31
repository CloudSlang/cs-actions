package io.cloudslang.content.datetime.actions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by stcu on 25.04.2016.
 */
public class GetCurrentDateTimeTest {
    private GetCurrentDateTime getCurrentDateTime;
    private Map<String, String> result;

    @Before
    public void init() {
        getCurrentDateTime = new GetCurrentDateTime();
        result = new HashMap<>();
    }

    @After
    public void tearDown() {
        getCurrentDateTime = null;
        result = null;
    }

    @Test
    public void testExecuteAllValid() {
        result = getCurrentDateTime.execute("fr", "FR");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
    }

    @Test
    public void testLocaleLangNull() {
        result = getCurrentDateTime.execute(null, "DK");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
    }

    @Test
    public void testLocaleCountryNull() {
        result = getCurrentDateTime.execute("da", null);

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
    }

    @Test
    public void testExecuteTimestamp() {
        result = getCurrentDateTime.execute("unix", "DK");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
        assertTrue(result.get("returnResult").startsWith("146"));
    }
}