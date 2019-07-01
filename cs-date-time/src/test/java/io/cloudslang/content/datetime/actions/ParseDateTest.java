/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



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
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
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
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals(result.get(RETURN_RESULT), "2001-07-04");
    }

    @Test
    public void testExecuteDateFormatValid() {
        result = parseDate
                .execute("2001-07-04T12:08:56.235+0700", "yyyy-MM-dd'T'HH:mm:ss.SSSZ", null, null, "yyyy-MM-dd HH:mm:ss", null, null);
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals(result.get(RETURN_RESULT), "2001-07-04 05:08:56");
    }

    @Test
    public void testExecuteUnixValid() {
        result = parseDate.execute("1467976783", "unix", null, null, "MM/dd/yyyy HH:mm", "en", "US");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertTrue(result.get(RETURN_RESULT).contains("07/08/2016 11:19"));
    }

    @Test
    public void testExecuteUnixFailed() {
        result = parseDate.execute("2001-07-04T12:08:56.235+0700", "unix", null, null, null, null, null);
        assertEquals(FAILURE, result.get(RETURN_CODE));
    }

    @Test
    public void testDateFormatFailed() {
        result = parseDate.execute("2001-07-04T12:08:56.235+0700", "222", null, null, null, null, null);
        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
    }

    @Test
    public void testSimpleDateFormatValid() {
        result = parseDate.execute("Wed, Jul 4, '01", "EEE, MMM d, ''yy", "en", "US", "yyyy-MM-dd", "fr", "FR");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals(result.get(RETURN_RESULT), "2001-07-04");
    }

    @Test
    public void testDateNull() {
        result = parseDate.execute(null, "EEE, MMM d, ''yy", "en", "US", "yyyy-MM-dd", "fr", "FR");

        assertEquals(FAILURE, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
    }

    @Test
    public void testGetCurrentDateValid() {
        final String date = new GetCurrentDateTime().execute("en", "US", "GMT", null).get(RETURN_RESULT);
        result = parseDate.execute(date, "", "", "", "", "fr", "FR");
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
    }

    @Test
    public void testFormatsNullValid() throws Exception {
        result = parseDate.execute("2001-07-04T12:08:56.235", "", "en", "US", "", "fr", "FR");
        DateTimeFormatter dateTimeFormatter = DateTimeUtils.getDateFormatter("dd-MM-yyyy HH:mm:ss", "fr", "FR");
        DateTime dateTime = DateTimeUtils.getJodaOrJavaDate(dateTimeFormatter, result.get(RETURN_RESULT));
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals(dateTimeFormatter.print(dateTime), "04-07-2001 12:08:56");
    }

    @Test
    public void testUnixBug() {
        result = parseDate.execute("1000", "S", "en", "US", "HH:mm:ss", "en", "US");
        assertFalse(result.get(RETURN_RESULT).isEmpty());
        assertEquals(SUCCESS, result.get(RETURN_CODE));
        assertEquals("00:00:01", result.get(RETURN_RESULT));
    }
}