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




package io.cloudslang.content.datetime.services;

import io.cloudslang.content.datetime.utils.Constants;
import io.cloudslang.content.datetime.utils.DateTimeUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by ursan on 7/8/2016.
 */
public class DateTimeServiceTest {

    @Test
    public void getCurrentDateTime() throws Exception {
        final String result = DateTimeService.getCurrentDateTime("unix", "DK", null, null);
        String currentTimeUnix =  String.valueOf(DateTime.now().getMillis() / Constants.Miscellaneous.THOUSAND_MULTIPLIER);
        assertFalse(result.isEmpty());
        assertTrue(result.startsWith(currentTimeUnix.substring(0,6)));
    }

    @Test
    public void parseDate() throws Exception {
        final String result = DateTimeService.parseDate("Wed, Jul 4, '01", "EEE, MMM d, ''yy", "en", "US", "yyyy-MM-dd", "fr", "FR");
        assertFalse(result.isEmpty());
        assertEquals(result, "2001-07-04");
    }

    @Test
    public void offsetTimeBy() throws Exception {
        final String result = DateTimeService.offsetTimeBy("April 26, 2016 1:32:20 PM", "20", "en", "US");
        DateTimeFormatter dateTimeFormatter = DateTimeUtils.getDateFormatter("dd-MM-yyyy HH:mm:ss", "en", "us");
        DateTime dateTime = DateTimeUtils.getJodaOrJavaDate(dateTimeFormatter, result);
        assertFalse(result.isEmpty());
        assertEquals(dateTimeFormatter.print(dateTime), "26-04-2016 13:32:40");
    }

}
