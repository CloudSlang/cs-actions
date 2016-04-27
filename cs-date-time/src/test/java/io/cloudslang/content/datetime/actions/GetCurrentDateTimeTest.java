package io.cloudslang.content.datetime.actions;

import io.cloudslang.content.datetime.utils.Constants;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by stcu on 25.04.2016.
 */
public class GetCurrentDateTimeTest {
    private final GetCurrentDateTime getCurrentDateTime = new GetCurrentDateTime();

    @Test
    public void testExecuteAllValid() {
        String localeLang = "fr";
        String localeCountry = "FR";

        final Map<String, String> result = getCurrentDateTime.execute(localeLang, localeCountry);
        assertTrue(!result.get(Constants.OutputNames.RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(Constants.OutputNames.RETURN_CODE));
    }

    @Test
    public void testLocaleLangNull() {
        String localeLang = null;
        String localeCountry = "DK";

        final Map<String, String> result = getCurrentDateTime.execute(localeLang, localeCountry);
        assertTrue(!result.get(Constants.OutputNames.RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(Constants.OutputNames.RETURN_CODE));
    }

    @Test
    public void testLocaleCountryNull() {
        String localeLang = "da";
        String localeCountry = null;

        final Map<String, String> result = getCurrentDateTime.execute(localeLang, localeCountry);
        assertTrue(!result.get(Constants.OutputNames.RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(Constants.OutputNames.RETURN_CODE));
    }

    @Test
    public void testExecuteAllInvalid() {
        String localeLang = "asdassssssasd";
        String localeCountry = "asdasdasdasdasdasd";

        final Map<String, String> result = getCurrentDateTime.execute(localeLang, localeCountry);
        assertTrue(!result.get(Constants.OutputNames.RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(Constants.OutputNames.RETURN_CODE));
    }

    @Test
    public void testExecuteTimestamp() {
        String localeLang = "unix";
        String localeCountry = "DK";

        final Map<String, String> result = getCurrentDateTime.execute(localeLang, localeCountry);
        assertTrue(!result.get(Constants.OutputNames.RETURN_RESULT).isEmpty());
        assertTrue(result.get(Constants.OutputNames.RETURN_RESULT).startsWith("1461"));
        assertEquals("0", result.get(Constants.OutputNames.RETURN_CODE));
    }
}
