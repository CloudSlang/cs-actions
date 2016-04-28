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

import io.cloudslang.content.datetime.services.DateTimeService;
import io.cloudslang.content.datetime.utils.Constants;
import java.util.HashMap;
import java.util.Map;

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

    public Map<String, String> execute(
            @Param(value = Constants.InputNames.LOCALE_DATE,   required = true) String date,
            @Param(value = Constants.InputNames.LOCALE_OFFSET, required = true) String offset,
            @Param(value = Constants.InputNames.LOCALE_LANG,   required = true) String localeLang,
            @Param(value = Constants.InputNames.LOCALE_COUNTRY,required = true) String localeCountry) {

        Map<String, String> resultMap = new HashMap<>();
        try {
            resultMap = new DateTimeService().offsetTimeBy(date, offset, localeLang, localeCountry);
        }
        catch(Exception e) {
            resultMap.put(Constants.OutputNames.EXCEPTION, e.getMessage());
            resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        }

        return resultMap;
    }

    public static void main(String[] args) {

        OffsetTimeBy offset = new OffsetTimeBy();
        Map<String, String> result = offset.execute("April 26, 2016 1:32:20 PM EEST", "5", "en", "US");
        //Map<String, String> result2 = offset.execute("2300", "15", "unix", "");
        System.out.println("main() over");
    }
}
