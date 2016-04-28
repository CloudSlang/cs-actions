package io.cloudslang.content.datetime.services;

import io.cloudslang.content.datetime.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by stcu on 28.04.2016.
 */
public class DateTimeService {

    public Map<String, String> getCurrentDateTime(String localeLang, String localeCountry) {

        Locale locale;
        DateTimeFormatter formatter;
        DateTime datetime = DateTime.now();
        Map<String, String> returnResult = new HashMap<>();

        if (StringUtils.isNotEmpty(localeLang)) {
            if (LocaleUtils.isUnix(localeLang)) {
                long timestamp = Math.round(datetime.getMillis() / 1000);
                addReturnValues(returnResult, "" + timestamp);

                return returnResult;
            }

            if (StringUtils.isNotEmpty(localeCountry)) {
                locale = new Locale(localeLang, localeCountry);
            } else {
                locale = new Locale(localeLang);
            }

            formatter = DateTimeFormat.fullDateTime().withLocale(locale);
        } else {
            formatter = DateTimeFormat.fullDateTime();
        }
        addReturnValues(returnResult, formatter.print(datetime));

        return returnResult;
    }

    public Map<String, String> offsetTimeBy(String date, String offset, String localeLang, String localeCountry) throws ParseException {

        Map<String, String> resultMap = new HashMap<String, String>();
        Locale locale;
        DateFormat dateFormatter;
        Date parsedDate;
        int parsedOffset = Integer.parseInt(offset);
        int offsetTimestamp;

        if(LocaleUtils.isUnix(localeLang)) {

            offsetTimestamp = Integer.parseInt(date) + parsedOffset;
            addReturnValues(resultMap, "" + offsetTimestamp);
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
