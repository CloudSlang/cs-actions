package io.cloudslang.content.datetime.actions;

import io.cloudslang.content.datetime.utils.DateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.datetime.utils.DateTimeUtils.getDateFormatter;
import static io.cloudslang.content.datetime.utils.DateTimeUtils.getJodaOrJavaDate;
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
    public void testLocaleEnglish() throws Exception {
        result = offsetTimeBy.execute("April 26, 2016 1:32:20 PM EEST", "30", "en", "US");
        final DateTimeFormatter dateTimeFormatter = getDateFormatter("dd-MM-yyyy HH:mm:ss", "en", "US");
        final DateTime dateTime = getJodaOrJavaDate(dateTimeFormatter, result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals(dateTimeFormatter.print(dateTime), "26-04-2016 13:32:50");

    }

    @Test
    public void testLocaleFrench() throws Exception {
        result = offsetTimeBy.execute("4 mai 2016 18:09:41", "12", "fr", "FR");
        final DateTimeFormatter dateTimeFormatter = getDateFormatter("dd-MM-yyyy HH:mm:ss", "fr", "FR");
        final DateTime dateTime = getJodaOrJavaDate(dateTimeFormatter, result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals(dateTimeFormatter.print(dateTime), "04-05-2016 18:09:53");
    }

    @Test
    public void testLocaleUnix() {
        result = offsetTimeBy.execute("1000", "12", "unix", "US");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals(result.get(RETURN_RESULT), "1012");
    }

    @Test
    public void testLocaleLangNull() throws Exception {
        result = offsetTimeBy.execute("April 26, 2016 1:32:20 PM EEST", "20", null, "US");
        final DateTimeFormatter dateTimeFormatter = getDateFormatter("dd-MM-yyyy HH:mm:ss", "en", "US");
        final DateTime dateTime = getJodaOrJavaDate(dateTimeFormatter, result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals(dateTimeFormatter.print(dateTime), "26-04-2016 13:32:40");
    }

    @Test
    public void testLocaleCountryNull() throws Exception {
        result = offsetTimeBy.execute("April 26, 2016 1:32:20 PM EEST", "15", "en", null);
        final DateTimeFormatter dateTimeFormatter = getDateFormatter("dd-MM-yyyy HH:mm:ss", "en", "US");
        final DateTime dateTime = getJodaOrJavaDate(dateTimeFormatter, result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals(dateTimeFormatter.print(dateTime), "26-04-2016 13:32:35");
    }
}
