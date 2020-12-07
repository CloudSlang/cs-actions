package io.cloudslang.content.filesystem.services;

import io.cloudslang.content.filesystem.entities.GetModifiedDateInputs;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.filesystem.constants.Constants.*;
import static io.cloudslang.content.filesystem.constants.ExceptionMsgs.DOES_NOT_EXIST;
import static io.cloudslang.content.filesystem.constants.ResultsName.DATE;

public class GetModifiedDateService {

    public static Map<String, String> execute(GetModifiedDateInputs getModifiedDateInputs) throws ParseException {

        Map<String, String> result = new HashMap<>();
        result.put(RETURN_RESULT, String.format(GET_MODIFIED_DATE_SUCCESS, getModifiedDateInputs.getSource()));

        File file = new File(getModifiedDateInputs.getSource());

        if (file.exists()) {
            Date modified = new Date(file.lastModified());

            DateFormat f = createDateFormatLocalizedFromUserInputs(getModifiedDateInputs.getLocaleLang(), getModifiedDateInputs.getLocaleCountry());

            result.put(DATE, f.format(modified));
            Date threshold = parseDate(getModifiedDateInputs.getThreshold());

            modified = parseDate((f.format(modified)));
            int compared = modified.compareTo(threshold);
            if (compared > 0)
                result.put(RETURN_CODE, GREATER);
            else if (compared < 0)
                result.put(RETURN_CODE, LESS);
            else
                result.put(RETURN_CODE, EQUALS_VALUE);
        } else {
            throw new RuntimeException(String.format(DOES_NOT_EXIST, getModifiedDateInputs.getSource()));
        }

        return result;

    }

    private static DateFormat createDateFormatLocalizedFromUserInputs(String localeLanguage, String localeCountry) {
        DateFormat f = null;
        if ((localeLanguage != null && !localeLanguage.isEmpty()) && (localeCountry != null && !localeCountry.isEmpty())) {
            Locale dateLocale = new Locale(localeLanguage, localeCountry);
            if (isLocaleValid(dateLocale)) {
                f = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, dateLocale);
            }
        }
        if (f == null) {
            f = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        }

        return f;
    }

    private static boolean isLocaleValid(Locale locale) {
        boolean isLocaleValid = false;
        List<Locale> locales = Arrays.asList(DateFormat.getAvailableLocales());
        if (locales.contains(locale)) {
            isLocaleValid = true;
        }
        return isLocaleValid;
    }

    private static Date parseDate(String toParse) throws ParseException {
        Date decoded = null;
        try {
            decoded = parseDate(toParse, DateFormat.SHORT);
        } catch (ParseException a) {
            Locale[] locales = DateFormat.getAvailableLocales();
            for (int count = 0; count < locales.length && decoded == null; count++) {
                try {
                    decoded = parseDate(toParse, DateFormat.MEDIUM, locales[count]);
                } catch (ParseException b) {
                    if (count + 1 >= locales.length)
                        throw b;
                }
            }
        }
        return decoded;
    }

    private static Date parseDate(String toParse, int style, Locale locale) throws ParseException {
        Date d;
        try {
            DateFormat f = DateFormat.getDateTimeInstance(style, DateFormat.MEDIUM, locale);
            d = f.parse(toParse);
        } catch (ParseException e) {
            if (style == DateFormat.SHORT)
                d = parseDate(toParse, DateFormat.MEDIUM, locale);
            else if (style == DateFormat.MEDIUM)
                d = parseDate(toParse, DateFormat.LONG, locale);
            else if (style == DateFormat.LONG)
                d = parseDate(toParse, DateFormat.FULL, locale);
            else
                throw e;
        }
        return d;
    }

    private static Date parseDate(String toParse, int style) throws ParseException {
        Date d;
        try {
            DateFormat f = DateFormat.getDateTimeInstance(style, DateFormat.MEDIUM);
            d = f.parse(toParse);
        } catch (ParseException e) {
            if (style == DateFormat.SHORT)
                d = parseDate(toParse, DateFormat.MEDIUM);
            else if (style == DateFormat.MEDIUM)
                d = parseDate(toParse, DateFormat.LONG);
            else if (style == DateFormat.LONG)
                d = parseDate(toParse, DateFormat.FULL);
            else
                throw e;
        }
        return d;
    }

}
