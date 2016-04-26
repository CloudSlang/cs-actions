package io.cloudslang.content.datetime.actions;

import io.cloudslang.content.datetime.utils.Constants;
import org.junit.Test;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by cadm on 4/26/2016.
 */
public class OffsetTimeByTest {
    private final OffsetTimeBy offsetTimeBy = new OffsetTimeBy();

    @Test
    public void test_localeEnglish() {

        final Map<String, String> result = offsetTimeBy.OffsetTimeBy("April 26, 2016 1:32:20 PM EEST", 5, "en", "US");
        assertTrue(!result.get(Constants.OutputNames.RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(Constants.OutputNames.RETURN_CODE));
    }

    @Test
    public void test_localeSpanish() {

        final Map<String, String> result = offsetTimeBy.OffsetTimeBy("26 de abril de 2016 13:32:20 EEST", 5, "es", "SP");
        assertTrue(!result.get(Constants.OutputNames.RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(Constants.OutputNames.RETURN_CODE));
    }

    @Test
    public void test_localeUnix() {

        final Map<String, String> result = offsetTimeBy.OffsetTimeBy("1000", 5, "unix", "US");
        assertTrue(!result.get(Constants.OutputNames.RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(Constants.OutputNames.RETURN_CODE));
    }

    @Test
    public void test_LocaleLangNull() {

        final Map<String, String> result = offsetTimeBy.OffsetTimeBy("April 26, 2016 1:32:20 PM EEST", 5, null, "US");
        assertTrue(!result.get(Constants.OutputNames.RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(Constants.OutputNames.RETURN_CODE));
    }

    @Test
    public void test_LocaleCountryNull() {

        final Map<String, String> result = offsetTimeBy.OffsetTimeBy("April 26, 2016 1:32:20 PM EEST", 5, "en", null);
        assertTrue(!result.get(Constants.OutputNames.RETURN_RESULT).isEmpty());
        assertEquals("0", result.get(Constants.OutputNames.RETURN_CODE));
    }
}
