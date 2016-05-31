package io.cloudslang.content.datetime.actions;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by stcu on 29.04.2016.
 */
public class ParseDateTest {
    private ParseDate parseDate;
    private Map<String, String> result;

    @Before
    public void init() {
        parseDate = new ParseDate();
        result = new HashMap<>();
    }

    @After
    public void tearDown() {
        parseDate = null;
        result = null;
    }


    @Test
    public void testExecuteAllValid() {
        result = parseDate
                .execute("2001-07-04T12:08:56.235+0700", "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "en", "US", "yyyy-MM-dd", "fr", "FR");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
    }

    @Test
    public void testExecuteDateFormatValid() {
        result = parseDate
                .execute("2001-07-04T12:08:56.235+0700", "yyyy-MM-dd'T'HH:mm:ss.SSSZ", null, null, "yyyy-MM-dd", null, null);

        assertEquals("0", result.get("returnCode"));
        assertEquals("2001-07-04", result.get("returnResult"));
    }

    @Test
    public void testExecuteUnixValid() {
        result = parseDate.execute("1462254848", "unix", null, null, "yyyy-MM-dd", "en", "US");

        assertEquals("0", result.get("returnCode"));
        assertTrue(result.get("returnResult").contains("2016-05-03"));
    }

    @Test
    public void testExecuteUnixFailed() {
        result = parseDate.execute("2001-07-04T12:08:56.235+0700", "unix", null, null, null, null, null);

        assertEquals("-1", result.get("returnCode"));
    }

    @Test
    public void testDateFormatFailed() {
        result = parseDate.execute("2001-07-04T12:08:56.235+0700", "222", null, null, null, null, null);

        assertEquals("-1", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
    }

    @Test
    public void testSimpleDateFormatValid() {
        result = parseDate.execute("Wed, Jul 4, '01", "EEE, MMM d, ''yy", "en", "US", "yyyy-MM-dd", "fr", "FR");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
    }

    @Test
    public void testDateNull() {
        result = parseDate.execute(null, "EEE, MMM d, ''yy", "en", "US", "yyyy-MM-dd", "fr", "FR");

        assertEquals("-1", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
    }

    @Test
    public void testGetCurrentDateValid() {
        String date = new GetCurrentDateTime().execute("en", "US").get("returnResult");

        result = parseDate.execute(date, "", "", "", "", "fr", "FR");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
    }

    @Test
    public void testFormatsNullValid() {
        result = parseDate.execute("2001-07-04T12:08:56.235+0700", "", "en", "US", "", "fr", "FR");

        assertEquals("0", result.get("returnCode"));
        assertFalse(result.get("returnResult").isEmpty());
    }

    @Test
    public void testUnixBug() {
        result = parseDate.execute("1000", "S", "en", "US", "HH:mm:ss", "en", "US");

        assertFalse(result.get("returnResult").isEmpty());
        assertEquals("00:00:01", result.get("returnResult"));
        assertEquals("0", result.get("returnCode"));
    }
}