/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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




package io.cloudslang.content.datetime.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by ursan on 7/8/2016.
 */
public class DateTimeUtilsTest {
    @Test
    public void isUnix() throws Exception {
        assertTrue(DateTimeUtils.isUnix("unix"));
        assertFalse(DateTimeUtils.isUnix(""));
    }

    @Test
    public void isMilliseconds() throws Exception {
        assertTrue(DateTimeUtils.isMilliseconds("S"));
        assertFalse(DateTimeUtils.isMilliseconds("unix"));
    }

    @Test
    public void isDateValid() throws Exception {
        assertTrue(DateTimeUtils.isDateValid("4 juillet 2001 12:08:56 EEST", Locale.FRENCH));
        assertFalse(DateTimeUtils.isDateValid("Wed, Jul 4, '01", Locale.ENGLISH));
    }

    @Test
    public void getLocaleByCountry() throws Exception {
        assertTrue(DateTimeUtils.getLocaleByCountry("en", "us").equals(Locale.US));
    }

    @Test
    public void getJodaOrJavaDate() throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeUtils.getDateFormatter("HH:MM", "fr", "FR");
        DateTime dateTime = DateTimeUtils
                .getJodaOrJavaDate(dateTimeFormatter, "4 juillet 2001 12:08:56 EEST");
        assertEquals("12:07", dateTime.toString(dateTimeFormatter));
    }

    @Test
    public void getDateFormatter() throws Exception {
        assertEquals(DateTimeUtils.getDateFormatter("HH:MM", "en", "us").getLocale(), Locale.US);
        assertEquals(DateTimeUtils.getDateFormatter("", "en", "us").getLocale(),Locale.US);
    }

    @Test
    public void formatWithDefault() throws Exception {
        assertEquals(DateTimeUtils.formatWithDefault("en", "us").getLocale(), Locale.US);
    }
}
