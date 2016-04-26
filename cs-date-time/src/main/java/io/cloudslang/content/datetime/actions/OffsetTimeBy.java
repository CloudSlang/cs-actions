package io.cloudslang.content.datetime.actions;

/**
 * Created by cadm on 4/25/2016.
 */
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;

import io.cloudslang.content.datetime.utils.Constants;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.Date;

public class OffsetTimeBy {

    /**
     * Changes the time represented by a date by the specified number of seconds.
     * If locale is specified, it will return the date and time string based on the locale.
     * Otherwise, default locale will be used.
     * @param date
     * @param offset
     * @param localeLang
     * @param localeCountry
     */
    @Action(name = "Offset Time By",
            description = "Changes the time represented by a date by the specified number of seconds",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })

    public Map<String, String> OffsetTimeBy(
            @Param(value = Constants.InputNames.LOCALE_DATE,   required = true) String date,
            @Param(value = Constants.InputNames.LOCALE_OFFSET, required = true) int offset,
            @Param(value = Constants.InputNames.LOCALE_LANG,      required = true) String localeLang,
            @Param(value = Constants.InputNames.LOCALE_COUNTRY,   required = true) String localeCountry) {

        Map<String, String> resultMap = new HashMap<String, String>();
        Locale locale;
        DateFormat dateFormatter;
        Date parsedDate;

        try {

            if(localeLang != null && localeLang.toLowerCase() == "unix") {

                int offsetedTimestamp = Integer.parseInt(date) + offset;
                resultMap.put(Constants.OutputNames.RETURN_RESULT, "" + offsetedTimestamp);
                resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
            }
            else
            {
                if(localeLang != null && localeLang.length() > 0)
                {
                    if(localeCountry != null && localeCountry.length() > 0)
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
                offset *= 1000;
                parsedDate.setTime(parsedDate.getTime() + offset);

                resultMap.put(Constants.OutputNames.RETURN_RESULT, "" + dateFormatter.format(parsedDate));
                resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
            }
        }
        catch(Exception e) {
            resultMap.put(Constants.OutputNames.EXCEPTION, e.getMessage());
            resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        }

        return resultMap;
    }

    public static void main(String[] args) {

        OffsetTimeBy offset = new OffsetTimeBy();
        Map<String, String> result = offset.OffsetTimeBy("April 26, 2016 1:32:20 PM EEST", 5, "en", "US");
        System.out.println("main() over");
    }
}
