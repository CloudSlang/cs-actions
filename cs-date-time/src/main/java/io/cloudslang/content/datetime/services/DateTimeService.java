package io.cloudslang.content.datetime.services;

import io.cloudslang.content.datetime.utils.Constants;
import io.cloudslang.content.datetime.utils.DateTimeUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;

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
     * @return Current date and time.
     */
    public String getCurrentDateTime(String localeLang, String localeCountry) throws Exception {
        DateTimeFormatter formatter = DateTimeFormat.longDateTime();
        DateTime datetime = DateTime.now();
        if (DateTimeUtils.isUnix(localeLang)) {
            Long unixSeconds = (long) Math.round(datetime.getMillis() / Constants.Miscellaneous.THOUSAND_MULTIPLIER);
            return unixSeconds.toString();
        }
        if (StringUtils.isNotBlank(localeLang)) {
            formatter = formatter.withLocale(DateTimeUtils.getLocaleByCountry(localeLang, localeCountry));
        }
        return formatter.print(datetime);
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
    public String parseDate(String date, String dateFormat, String dateLocaleLang,
                                         String dateLocaleCountry, String outFormat, String outLocaleLang,
                                         String outLocaleCountry) throws Exception {
        if (StringUtils.isBlank(date)) {
            throw new RuntimeException(Constants.ErrorMessages.DATE_NULL_OR_EMPTY);
        }
        DateTimeZone timeZone = DateTimeZone.forID(Constants.Miscellaneous.GMT);
        DateTime inputDateTime = parseInputDate(date, dateFormat, dateLocaleLang, dateLocaleCountry, timeZone);

        return changeFormatForDateTime(inputDateTime, outFormat, outLocaleLang, outLocaleCountry);
    }

    private DateTime parseInputDate(String date, String dateFormat, String dateLocaleLang, String dateLocaleCountry,
                                    DateTimeZone timeZone) throws Exception {
        if (DateTimeUtils.isUnix(dateFormat)) {
            return new DateTime(Long.parseLong(date) * Constants.Miscellaneous.THOUSAND_MULTIPLIER).withZone(timeZone);
        }
        if (DateTimeUtils.isMilliseconds(dateFormat)) { // can be removed if not needed
            return new DateTime(new Date(Long.parseLong(date))).withZone(timeZone);
        }
        DateTimeFormatter dateFormatter = DateTimeUtils.getDateFormatter(dateFormat, dateLocaleLang, dateLocaleCountry);
        if (StringUtils.isNotBlank(dateFormat)) {
            dateFormatter = dateFormatter.withZone(timeZone);
            return dateFormatter.parseDateTime(date);
        }
        return DateTimeUtils.getJodaOrJavaDate(dateFormatter, date);
    }

    private String changeFormatForDateTime(DateTime inputDateTime, String outFormat, String outLocaleLang,
                                                        String outLocaleCountry) {
        if (DateTimeUtils.isUnix(outFormat)) {
            Long timestamp = (long) Math.round(inputDateTime.getMillis() / Constants.Miscellaneous.THOUSAND_MULTIPLIER);
            return timestamp.toString();
        }
        DateTimeFormatter outFormatter = DateTimeUtils.getDateFormatter(outFormat, outLocaleLang, outLocaleCountry);
        return outFormatter.print(inputDateTime);
    }

    public String offsetTimeBy(String date, String offset, String localeLang, String localeCountry)
            throws Exception {
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
