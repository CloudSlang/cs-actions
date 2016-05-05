package io.cloudslang.content.datetime.services;

import io.cloudslang.content.datetime.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
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
    public Map<String, String> getCurrentDateTime(String localeLang, String localeCountry) throws Exception {
        DateTimeFormatter formatter;
        DateTime datetime = DateTime.now();
        Map<String, String> returnResult = new HashMap<>();

        if (StringUtils.isNotEmpty(localeLang)) {
            if (LocaleUtils.isUnix(localeLang)) {
                long timestamp = (long)Math.floor(datetime.getMillis()/ 1000);
                addReturnValues(returnResult, "" + timestamp);

                return returnResult;
            }

            formatter = DateTimeFormat.longDateTime().withLocale(getLocaleByCountry(localeLang, localeCountry));
        } else {
            formatter = DateTimeFormat.longDateTime();
        }
        addReturnValues(returnResult, formatter.print(datetime));

        return returnResult;
    }

    /**
     * This operation converts the date input value from one date/time format (specified by dateFormat)
     * to another date/time format (specified by outFormat) using locale settings (language and country).
     * You can use the flow "Get Current Date and Time" to check upon the default date/time format from
     * the Java environement.
     *
     * @param date the date to parse/convert
     * @param dateFormat the format of the input date
     * @param dateLocaleLang the locale language for input dateFormat string. It will be ignored if
     *                       dateFormat is empty. default locale language from the Java environement
     *                       (which is dependent on the OS locale language)
     * @param dateLocaleCountry the locale country for input dateFormat string. It will be ignored
     *                          if dateFormat is empty or dateLocaleLang is empty. Default locale country
     *                          from the Java environement (which is dependent on the OS locale country)
     * @param outFormat The format of the output date/time. Default date/time format from the Java
     *                  environement (which is dependent on the OS date/time format)
     * @param outLocaleLang The locale language for output string. It will be ignored if outFormat is
     *                      empty.
     * @param outLocaleCountry The locale country for output string. It will be ignored if outFormat
     *                         is empty or outLocaleLang is empty.
     * @return The date in the new format
     */
    public Map<String, String> parseDate(String date, String dateFormat, String dateLocaleLang,
                                         String dateLocaleCountry, String outFormat, String outLocaleLang,
                                         String outLocaleCountry) throws Exception {
        DateTime inputDateTime;
        DateTimeFormatter dateFormatter, outFormatter;
        Map<String, String> returnResult = new HashMap<>();
        DateTimeZone timeZone = DateTimeZone.forID("GMT");

        if (StringUtils.isEmpty(date))
            throw new NullPointerException("Date is either Null or Empty");

        if (StringUtils.isNotEmpty(dateFormat)) {
            if (LocaleUtils.isUnix(dateFormat)) {
                inputDateTime = new DateTime(Long.parseLong(date) * 1000);
            } else {
                dateFormatter = formatWithPattern(dateFormat, dateLocaleLang, dateLocaleCountry);

                if (LocaleUtils.isUnix(outFormat))
                    dateFormatter.withZone(DateTimeZone.getDefault());

                inputDateTime = dateFormatter.parseDateTime(date);
            }
        } else {
            dateFormatter = formatWithDefault(dateLocaleLang, dateLocaleCountry);

            if (LocaleUtils.isUnix(dateFormat))
                dateFormatter.withZone(timeZone);

            inputDateTime = getJodaOrJavaDate(dateFormatter, date);
        }

        if (StringUtils.isNotEmpty(outFormat)) {
            if (LocaleUtils.isUnix(outFormat)) {
                long timestamp = (long)Math.floor(inputDateTime.toDateTime().getMillis() / 1000);
                addReturnValues(returnResult, "" + timestamp);
                return returnResult;
            } else {
                outFormatter = formatWithPattern(outFormat, outLocaleLang, outLocaleCountry);

                if (StringUtils.isNotEmpty(dateFormat) && LocaleUtils.isUnix(outFormat))
                    outFormatter.withZone(timeZone);

                addReturnValues(returnResult, outFormatter.print(inputDateTime));
            }
        } else {
            outFormatter = formatWithDefault(outLocaleLang, outLocaleCountry);

            if (StringUtils.isNotEmpty(dateFormat) && LocaleUtils.isUnix(outFormat))
                outFormatter.withZone(DateTimeZone.getDefault());

            addReturnValues(returnResult, outFormatter.print(inputDateTime));
        }

        return returnResult;
    }

    public Map<String, String> offsetTimeBy(String date, String offset, String localeLang, String localeCountry) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        DateTime dateTime;
        DateTimeFormatter dateFormatter;
        int parsedOffset = Integer.parseInt(offset);
        int offsetTimestamp;

        if(LocaleUtils.isUnix(localeLang)) {
            offsetTimestamp = Integer.parseInt(date) + parsedOffset;
            addReturnValues(resultMap, "" + offsetTimestamp);
        }
        else {
            dateFormatter = formatWithDefault(localeLang, localeCountry);
            dateTime = getJodaOrJavaDate(dateFormatter, date);
            addReturnValues(resultMap, dateFormatter.print(dateTime.plusSeconds(parsedOffset)));
        }

        return resultMap;
    }

    private void addReturnValues(Map<String, String> resultMap , String value) {
        resultMap.put(Constants.OutputNames.RETURN_RESULT, value);
        resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
    }

    /**
     * Generates the locale
     * @param lang the language
     * @param country the country
     * @return the locale
     */
    private Locale getLocaleByCountry(String lang, String country) {
        Locale locale;
        if (StringUtils.isNotEmpty(country)) {
            locale = new Locale(lang, country);
        } else {
            locale = new Locale(lang);
        }

        return locale;
    }

    /**
     * Generates a DateTimeFormatter using a custom pattern with the default locale or a new one
     * according to what language and country are provided as params.
     * @param format
     * @param lang
     * @param country
     * @return the DateTimeFormatter generated
     */
    private DateTimeFormatter formatWithPattern(String format, String lang, String country) {
        DateTimeFormatter dateFormatter;
        if (StringUtils.isNotEmpty(lang)) {
            dateFormatter = DateTimeFormat.forPattern(format)
                    .withLocale(getLocaleByCountry(lang, country));
        } else {
            dateFormatter = DateTimeFormat.forPattern(format);
        }

        return dateFormatter;
    }

    /**
     * Generates a DateTimeFormatter using full date pattern with the default locale or a new one
     * according to what language and country are provided as params.
     * @param lang
     * @param country
     * @return the DateTimeFormatter generated
     */
    private DateTimeFormatter formatWithDefault(String lang, String country) {
        DateTimeFormatter dateFormatter;
        if (StringUtils.isNotEmpty(lang)) {
            dateFormatter = DateTimeFormat.longDateTime()
                    .withLocale(getLocaleByCountry(lang, country));
        } else {
            dateFormatter = DateTimeFormat.longDateTime()
                    .withLocale(Locale.getDefault());
        }

        return dateFormatter;
    }

    /**
     * Because the date passed as argument can be in java format, and because not all formats are compatible
     * with joda-time, this method checks if the date string is valid with java. In this way we can use the
     * proper DateTime without alterating the output.
     * @param date date passed as argument
     * @return true if is a java date
     */
    private boolean isDateValid(String date, Locale locale) {
        try {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
            format.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Returns a LocalDateTime depending on how the date passes as argument is formatted.
     * @see {@link #isDateValid}
     * @param date date passed as argument
     * @return true if is a java date
     */
    private DateTime getJodaOrJavaDate(DateTimeFormatter dateFormatter, String date) throws Exception {

        DateTime datetime;
        if (!isDateValid(date, dateFormatter.getLocale())) {
            datetime = new DateTime(date);
        } else {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT,
                    dateFormatter.getLocale());

            Calendar dateCalendar = GregorianCalendar.getInstance();
            dateCalendar.setTime(dateFormat.parse(date));

            datetime = new DateTime(dateCalendar.getTime());
        }

        return datetime;
    }
}

