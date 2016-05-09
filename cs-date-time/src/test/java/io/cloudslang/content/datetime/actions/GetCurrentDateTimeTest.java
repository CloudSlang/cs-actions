package io.cloudslang.content.datetime.actions;

import io.cloudslang.content.datetime.utils.Constants;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by stcu on 25.04.2016.
 */
public class GetCurrentDateTimeTest {
    private final GetCurrentDateTime getCurrentDateTime = new GetCurrentDateTime();
    private final String RETURN_CODE = "returnCode";
    private final String RETURN_RESULT = "returnResult";

    @Test
    public void testExecuteAllValid() {
        String localeLang = "fr";
        String localeCountry = "FR";

        final Map<String, String> result = getCurrentDateTime.execute(localeLang, localeCountry);
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(RETURN_CODE));
    }

    @Test
    public void testLocaleLangNull() {
        String localeLang = null;
        String localeCountry = "DK";

        final Map<String, String> result = getCurrentDateTime.execute(localeLang, localeCountry);
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(RETURN_CODE));
    }

    @Test
    public void testLocaleCountryNull() {
        String localeLang = "da";
        String localeCountry = null;

        final Map<String, String> result = getCurrentDateTime.execute(localeLang, localeCountry);
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(RETURN_CODE));
    }

    @Test
    public void testExecuteTimestamp() {
        String localeLang = "unix";
        String localeCountry = "DK";

        final Map<String, String> result = getCurrentDateTime.execute(localeLang, localeCountry);
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertTrue(result.get(RETURN_RESULT).startsWith("146"));
        assertEquals("0", result.get(RETURN_CODE));
    }
}
