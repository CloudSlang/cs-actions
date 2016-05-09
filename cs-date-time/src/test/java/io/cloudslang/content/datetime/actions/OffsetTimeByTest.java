package io.cloudslang.content.datetime.actions;

import io.cloudslang.content.datetime.utils.Constants;
import org.junit.Test;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by cadm on 4/26/2016.
 */
public class OffsetTimeByTest {
    private final OffsetTimeBy offsetTimeBy = new OffsetTimeBy();
    private final String RETURN_CODE = "returnCode";
    private final String RETURN_RESULT = "returnResult";

    @Test
    public void testLocaleEnglish() {
        String date = "April 26, 2016 1:32:20 PM EEST";
        String offset = "30";
        String lang = "en";
        String country = "US";

        final Map<String, String> result = offsetTimeBy.execute(date, offset, lang, country);
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(RETURN_CODE));
    }

    @Test
    public void testLocaleFrench() {
        String date = "4 mai 2016 18:09:41";
        String offset = "12";
        String lang = "fr";
        String country = "FR";
        final Map<String, String> result = offsetTimeBy.execute(date, offset, lang, country);
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(RETURN_CODE));
    }

    @Test
    public void testLocaleUnix() {
        String date = "1000";
        String offset = "12";
        String lang = "unix";
        String country = "US";

        final Map<String, String> result = offsetTimeBy.execute(date, offset, lang, country);
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(RETURN_CODE));
    }

    @Test
    public void testLocaleLangNull() {
        String date = "April 26, 2016 1:32:20 PM EEST";
        String offset = "20";
        String country = "US";

        final Map<String, String> result = offsetTimeBy.execute(date, offset, null, country);
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(RETURN_CODE));
    }

    @Test
    public void testLocaleCountryNull() {
        String date = "April 26, 2016 1:32:20 PM EEST";
        String offset = "15";
        String lang = "en";

        final Map<String, String> result = offsetTimeBy.execute(date, offset, lang, null);
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(RETURN_CODE));
    }
}
