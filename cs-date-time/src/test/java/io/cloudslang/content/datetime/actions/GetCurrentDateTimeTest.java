/***********************************************************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 ***********************************************************************************************************************/
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
    public void testExecuteAllValidWithFormat() {
        result = getCurrentDateTime.execute("fr", "FR", "GMT+3", "dd-M-yyyy HH:mm:ss");
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