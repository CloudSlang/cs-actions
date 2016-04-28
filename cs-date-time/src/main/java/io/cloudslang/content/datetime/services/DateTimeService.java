package io.cloudslang.content.datetime.services;

import io.cloudslang.content.datetime.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by stcu on 28.04.2016.
 */
public class DateTimeService {

    public Map<String, String> getCurrentDateTime(String localeLang, String localeCountry) {

        Locale locale;
        DateFormat formatter;
        Calendar calendar = Calendar.getInstance();
        Map<String, String> returnResult = new HashMap<>();

        if (StringUtils.isNotEmpty(localeLang)) {
            if (LocaleUtils.isUnix(localeLang)) {
                long timestamp = Math.round(calendar.getTimeInMillis() / 1000);
                addReturnValues(returnResult, "" + timestamp);

                return returnResult;
            }

            if (StringUtils.isNotEmpty(localeCountry)) {
                locale = new Locale(localeLang, localeCountry);
            } else {
                locale = new Locale(localeLang);
            }

            formatter = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale);
        } else {
            formatter = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
        }
        addReturnValues(returnResult, formatter.format(calendar.getTime()));

        return returnResult;
    }

    public Map<String, String> offsetTimeBy(String date, String offset, String localeLang, String localeCountry) throws ParseException {

        Map<String, String> resultMap = new HashMap<String, String>();
        Locale locale;
        DateFormat dateFormatter;
        Date parsedDate;
        int parsedOffset = Integer.parseInt(offset);
        int offsetedTimestamp = 0;

        if(LocaleUtils.isUnix(localeLang)) {

            offsetedTimestamp = Integer.parseInt(date) + parsedOffset;
            addReturnValues(resultMap, "" + offsetedTimestamp);
        }
        else {
            if(StringUtils.isNotEmpty(localeLang))
            {
                if(StringUtils.isNotEmpty(localeCountry))
                {
                    locale = new Locale(localeLang, localeCountry);
                }
                else
                {
                    locale = new Locale(localeLang);
                }

                dateFormatter = DateFormat.getDateTimeInstance(java.text.DateFormat.LONG, java.text.DateFormat.LONG, locale);
            }
            else // use the default locale
            {
                dateFormatter = DateFormat.getDateTimeInstance();
            }

            parsedDate = dateFormatter.parse(date);
            parsedOffset *= 1000;
            parsedDate.setTime(parsedDate.getTime() + parsedOffset);

            addReturnValues(resultMap, "" + dateFormatter.format(parsedDate));
        }

        return resultMap;
    }

    private void addReturnValues(Map<String, String> resultMap , String value) {
        resultMap.put(Constants.OutputNames.RETURN_RESULT, value);
        resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
    }
}
