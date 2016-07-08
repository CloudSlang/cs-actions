package io.cloudslang.content.datetime.actions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by cadm on 4/26/2016.
 */
public class OffsetTimeByTest {
    private OffsetTimeBy offsetTimeBy;
    private Map<String, String> result;

    @Before
    public void init() {
        offsetTimeBy = new OffsetTimeBy();
        result = new HashMap<>();
    }

    @After
    public void tearDown() {
        offsetTimeBy = null;
        result = null;
    }

    @Test
    public void testLocaleEnglish() {
        result = offsetTimeBy.execute("April 26, 2016 1:32:20 PM EEST", "30", "en", "US");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
        assertEquals(result.get("returnResult"), "April 26, 2016 1:32:50 PM EEST");

    }

    @Test
    public void testLocaleFrench() {
        result = offsetTimeBy.execute("4 mai 2016 18:09:41", "12", "fr", "FR");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
        assertEquals(result.get("returnResult"), "4 mai 2016 18:09:53 EEST");
    }

    @Test
    public void testLocaleUnix() {
        result = offsetTimeBy.execute("1000", "12", "unix", "US");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
        assertEquals(result.get("returnResult"), "1012");
    }

    @Test
    public void testLocaleLangNull() {
        result = offsetTimeBy.execute("April 26, 2016 1:32:20 PM EEST", "20", null, "US");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
        assertEquals(result.get("returnResult"), "April 26, 2016 1:32:40 PM EEST");
    }

    @Test
    public void testLocaleCountryNull() {
        result = offsetTimeBy.execute("April 26, 2016 1:32:20 PM EEST", "15", "en", null);

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
        assertEquals(result.get("returnResult"), "April 26, 2016 1:32:35 PM EEST");
    }
}
