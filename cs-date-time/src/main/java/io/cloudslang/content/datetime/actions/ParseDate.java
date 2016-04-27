package io.cloudslang.content.datetime.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.datetime.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by stcu on 27.04.2016.
 */
public class ParseDate {

    /**
     * This operation converts the date input value from one date/time format (specified by dateFormat)
     * to another date/time format (specified by outFormat) using locale settings (language and country).
     * You can use the flow "Get Current Date and Time" to check upon the default date/time format from
     * the Java environement.
     *
     * @return
     */
    @Action(name = "Parse Date",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE,
                            value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL,
                            responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE,
                            value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL,
                            responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = Constants.InputNames.LOCALE_DATE, required = true) String date,
                                       @Param(value = Constants.InputNames.DATE_FORMAT, required = true) String dateFormat,
                                       @Param(value = Constants.InputNames.DATE_LOCALE_LANG, required = true) String dateLocaleLang,
                                       @Param(value = Constants.InputNames.DATE_LOCALE_COUNTRY, required = true) String dateLocaleCountry,
                                       @Param(value = Constants.InputNames.OUT_FORMAT, required = true) String outFormat,
                                       @Param(value = Constants.InputNames.OUT_LOCALE_LANG, required = true) String outLocaleLang,
                                       @Param(value = Constants.InputNames.OUT_LOCALE_COUNTRY, required = true) String outLocaleCountry
    ) {
        Map<String, String> returnResult = new HashMap<>();

        return returnResult;
    }
}
