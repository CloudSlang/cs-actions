/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

package io.cloudslang.content.datetime.services;

import io.cloudslang.content.datetime.utils.Constants;
import io.cloudslang.content.datetime.utils.DateTimeUtils;
import io.cloudslang.content.utils.StringUtilities;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.TimeZone;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by stcu on 28.04.2016.
 */
public class DateTimeService {

    /**
     * Check the current date and time, and returns a java DateAndTime formatted string of it.
     * If locale is specified, it will return the date and time string based on the locale.
     * Otherwise, default locale will be used.
     *
     * @param localeLang    The locale language for date and time string. If localeLang is 'unix' the
     *                      localeCountry input is ignored and the result will be the current UNIX
     *                      timestamp. Examples:  en, ja, unix.
     * @param localeCountry The locale country for date and time string. For example, US or JP.
     *                      If localeLang is not specified, this input will be ignored.
     * @param timezone      The timezone you want the current datetime to be.
     *                      Examples: GMT, GMT+1, PST
     * @param dateFormat    The format of the output date/time.The Default date/time format is from the Java
     *                      environment (which is dependent on the OS date/time format)
     * @return Current date and time.
     */
    public static String getCurrentDateTime(final String localeLang, final String localeCountry, final String timezone, final String dateFormat) throws Exception {
        final DateTimeFormatter formatter = getDateTimeFormatter(localeLang, localeCountry, timezone, dateFormat);
        final DateTime datetime = DateTime.now();
        if (DateTimeUtils.isUnix(localeLang)) {
            Long unixSeconds = (long) Math.round(datetime.getMillis() / Constants.Miscellaneous.THOUSAND_MULTIPLIER);
            return unixSeconds.toString();
        }
        return formatter.print(datetime);
    }

    private static DateTimeFormatter getDateTimeFormatter(final String localeLang, final String localeCountry, final String timezone, final String dateFormat) {
        DateTimeFormatter formatter;
        if (StringUtilities.isNoneBlank(dateFormat)) {
            formatter = DateTimeFormat.forPattern(dateFormat);
        } else {
            formatter = DateTimeFormat.longDateTime();
        }

        if (isNotBlank(timezone)) {
            formatter = formatter.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(timezone)));
        }

        if (isNotBlank(localeLang)) {
            formatter = formatter.withLocale(DateTimeUtils.getLocaleByCountry(localeLang, localeCountry));
        }
        return formatter;
    }

    /**
     * This operation converts the date input value from one date/time format (specified by dateFormat)
     * to another date/time format (specified by outFormat) using locale settings (language and country).
     * You can use the flow "Get Current Date and Time" to check upon the default date/time format from
     * the Java environment.
     *
     * @param date              the date to parse/convert
     * @param dateFormat        the format of the input date
     * @param dateLocaleLang    the locale language for input dateFormat string. It will be ignored if
     *                          dateFormat is empty. default locale language from the Java environment
     *                          (which is dependent on the OS locale language)
     * @param dateLocaleCountry the locale country for input dateFormat string. It will be ignored
     *                          if dateFormat is empty or dateLocaleLang is empty. Default locale country
     *                          from the Java environment (which is dependent on the OS locale country)
     * @param outFormat         The format of the output date/time. Default date/time format from the Java
     *                          environment (which is dependent on the OS date/time format)
     * @param outLocaleLang     The locale language for output string. It will be ignored if outFormat is
     *                          empty.
     * @param outLocaleCountry  The locale country for output string. It will be ignored if outFormat
     *                          is empty or outLocaleLang is empty.
     * @return The date in the new format
     */
    public static String parseDate(final String date, final String dateFormat, final String dateLocaleLang,
                                   final String dateLocaleCountry, final String outFormat, final String outLocaleLang,
                                   final String outLocaleCountry) throws Exception {
        if (StringUtils.isBlank(date)) {
            throw new RuntimeException(Constants.ErrorMessages.DATE_NULL_OR_EMPTY);
        }
        DateTimeZone timeZone = DateTimeZone.forID(Constants.Miscellaneous.GMT);
        DateTime inputDateTime = parseInputDate(date, dateFormat, dateLocaleLang, dateLocaleCountry, timeZone);

        return changeFormatForDateTime(inputDateTime, outFormat, outLocaleLang, outLocaleCountry);
    }

    private static DateTime parseInputDate(final String date, final String dateFormat, final String dateLocaleLang, final String dateLocaleCountry,
                                           final DateTimeZone timeZone) throws Exception {
        if (DateTimeUtils.isUnix(dateFormat)) {
            return new DateTime(Long.parseLong(date) * Constants.Miscellaneous.THOUSAND_MULTIPLIER).withZone(timeZone);
        }
        if (DateTimeUtils.isMilliseconds(dateFormat)) { // can be removed if not needed
            return new DateTime(new Date(Long.parseLong(date))).withZone(timeZone);
        }
        DateTimeFormatter dateFormatter = DateTimeUtils.getDateFormatter(dateFormat, dateLocaleLang, dateLocaleCountry);
        if (isNotBlank(dateFormat)) {
            dateFormatter = dateFormatter.withZone(timeZone);
            return dateFormatter.parseDateTime(date);
        }
        return DateTimeUtils.getJodaOrJavaDate(dateFormatter, date);
    }

    private static String changeFormatForDateTime(final DateTime inputDateTime, final String outFormat, final String outLocaleLang,
                                                  final String outLocaleCountry) {
        if (DateTimeUtils.isUnix(outFormat)) {
            Long timestamp = (long) Math.round(inputDateTime.getMillis() / Constants.Miscellaneous.THOUSAND_MULTIPLIER);
            return timestamp.toString();
        }
        DateTimeFormatter outFormatter = DateTimeUtils.getDateFormatter(outFormat, outLocaleLang, outLocaleCountry);
        return outFormatter.print(inputDateTime);
    }

    public static String offsetTimeBy(final String date, final String offset, final String localeLang, final String localeCountry) throws Exception {
        Long parsedOffset = Long.parseLong(offset);
        if (DateTimeUtils.isUnix(localeLang)) {
            Long offsetTimestamp = Long.parseLong(date) + parsedOffset;
            return offsetTimestamp.toString();
        }
        DateTimeFormatter dateFormatter = DateTimeUtils.formatWithDefault(localeLang, localeCountry);
        DateTime dateTime = DateTimeUtils.getJodaOrJavaDate(dateFormatter, date);
        return dateFormatter.print(dateTime.plusSeconds(parsedOffset.intValue()));
    }
}
