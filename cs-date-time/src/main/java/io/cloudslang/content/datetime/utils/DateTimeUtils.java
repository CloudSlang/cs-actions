/***********************************************************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 ***********************************************************************************************************************/
package io.cloudslang.content.datetime.utils;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by ursan on 7/6/2016.
 */
public class DateTimeUtils {
    public static boolean isUnix(String locale) {
        return Constants.Miscellaneous.UNIX.equals(locale);
    }

    public static boolean isMilliseconds(String locale) {
        return Constants.Miscellaneous.MILLISECONDS.equals(locale);
    }

    /**
     * Because the date passed as argument can be in java format, and because not all formats are compatible
     * with joda-time, this method checks if the date string is valid with java. In this way we can use the
     * proper DateTime without changing the output.
     *
     * @param date date passed as argument
     * @return true if is a java date
     */
    public static boolean isDateValid(String date, Locale locale) {
        try {
            DateFormat format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
            format.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Generates the locale
     *
     * @param lang    the language
     * @param country the country
     * @return the locale
     */
    public static Locale getLocaleByCountry(String lang, String country) {
        return StringUtils.isNotBlank(country) ? new Locale(lang, country) : new Locale(lang);
    }

    /**
     * Returns a LocalDateTime depending on how the date passes as argument is formatted.
     *
     * @param date date passed as argument
     * @return the DateTime if it could parse it
     */
    public static DateTime getJodaOrJavaDate(final DateTimeFormatter dateFormatter, final String date) throws Exception {
        if (DateTimeUtils.isDateValid(date, dateFormatter.getLocale())) {
            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, dateFormatter.getLocale());

            Calendar dateCalendar = GregorianCalendar.getInstance();
            dateCalendar.setTime(dateFormat.parse(date));

            return new DateTime(dateCalendar.getTime());
        }

        return new DateTime(date);
    }


    /**
     * Generates a DateTimeFormatter using a custom pattern with the default locale or a new one
     * according to what language and country are provided as params.
     *
     * @param format  the pattern
     * @param lang    the language
     * @param country the country
     * @return the DateTimeFormatter generated
     */
    public static DateTimeFormatter getDateFormatter(String format, String lang, String country) {
        if (StringUtils.isNotBlank(format)) {
            DateTimeFormatter dateFormat = DateTimeFormat.forPattern(format);
            if (StringUtils.isNotBlank(lang)) {
                return dateFormat.withLocale(DateTimeUtils.getLocaleByCountry(lang, country));
            }
            return dateFormat;
        }
        return formatWithDefault(lang, country);
    }

    /**
     * Generates a DateTimeFormatter using full date pattern with the default locale or a new one
     * according to what language and country are provided as params.
     *
     * @param lang    the language
     * @param country the country
     * @return the DateTimeFormatter generated
     */
    public static DateTimeFormatter formatWithDefault(String lang, String country) {
        return (StringUtils.isNotBlank(lang)) ? DateTimeFormat.longDateTime().withLocale(DateTimeUtils.getLocaleByCountry(lang, country)) :
                DateTimeFormat.longDateTime().withLocale(Locale.getDefault());
    }

}
