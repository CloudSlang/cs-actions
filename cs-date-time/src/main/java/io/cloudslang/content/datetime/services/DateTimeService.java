package io.cloudslang.content.datetime.services;

import io.cloudslang.content.datetime.utils.Constants;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
            if ("unix".equals(localeLang)) {
                long timestamp = Math.round(datetime.getMillis() / 1000);
                addReturnValues(returnResult, Constants.OutputNames.RETURN_RESULT, "" + timestamp);

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
        addReturnValues(returnResult, Constants.OutputNames.RETURN_RESULT, formatter.print(datetime));

        return returnResult;
    }

    private void addReturnValues(Map<String, String> resultMap , String name, String value) {
        resultMap.put(name, value);
        resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
    }
}
