package io.cloudslang.content.datetime.services;

import io.cloudslang.content.datetime.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

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
            if ("unix".equals(localeLang)) {
                long timestamp = Math.round(calendar.getTimeInMillis() / 1000);
                addReturnValues(returnResult, Constants.OutputNames.RETURN_RESULT, "" + timestamp);

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
        addReturnValues(returnResult, Constants.OutputNames.RETURN_RESULT, formatter.format(calendar.getTime()));

        return returnResult;
    }

    private void addReturnValues(Map<String, String> resultMap , String name, String value) {
        resultMap.put(name, value);
        resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
    }
}
