package io.cloudslang.content.datetime.actions;

import io.cloudslang.content.datetime.utils.Constants;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
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
        result = getCurrentDateTime.execute("fr", "FR", "PST", null);
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
    }

    @Test
    public void testLocaleLangNull() {
        result = getCurrentDateTime.execute(null, "DK", "GMT+1", null);
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
    }

    @Test
    public void testLocaleCountryNull() {
        result = getCurrentDateTime.execute("da", null, null, null);
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
    }

    @Test
    public void testExecuteTimestamp() {
        result = getCurrentDateTime.execute("unix", "DK", null, null);
        String currentTimeUnix = String.valueOf(DateTime.now().getMillis() / Constants.Miscellaneous.THOUSAND_MULTIPLIER);
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertTrue(result.get(RETURN_RESULT).startsWith(currentTimeUnix.substring(0, 6)));
    }
}