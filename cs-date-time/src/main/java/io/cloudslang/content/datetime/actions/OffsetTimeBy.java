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
            @Param(Constants.InputNames.LOCALE_OFFSET) String offset,
            @Param(Constants.InputNames.LOCALE_LANG) String localeLang,
            @Param(Constants.InputNames.LOCALE_COUNTRY) String localeCountry) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            resultMap = new DateTimeService().offsetTimeBy(date, offset, localeLang, localeCountry);
        }
        catch(Exception exception) {
            resultMap.put(Constants.OutputNames.EXCEPTION, exception.toString());
            resultMap.put(Constants.OutputNames.RETURN_RESULT, exception.getMessage());
            resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        }

        return resultMap;
    }
}
