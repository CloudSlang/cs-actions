package io.cloudslang.content.datetime.actions;

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
     * @param date the date to parse/convert
     * @param dateFormat the format of the input date
     * @param dateLocaleLang the locale language for input dateFormat string. It will be ignored if
     *                       dateFormat is empty. default locale language from the Java environement
     *                       (which is dependent on the OS locale language)
     * @param dateLocaleCountry the locale country for input dateFormat string. It will be ignored
     *                          if dateFormat is empty or dateLocaleLang is empty. Default locale country
     *                          from the Java environement (which is dependent on the OS locale country)
     * @param outFormat The format of the output date/time. Default date/time format from the Java
     *                  environement (which is dependent on the OS date/time format)
     * @param outLocaleLang The locale language for output string. It will be ignored if outFormat is
     *                      empty.
     * @param outLocaleCountry The locale country for output string. It will be ignored if outFormat
     *                         is empty or outLocaleLang is empty.
     * @return The date in the new format
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
                                       @Param(Constants.InputNames.DATE_FORMAT) String dateFormat,
                                       @Param(Constants.InputNames.DATE_LOCALE_LANG) String dateLocaleLang,
                                       @Param(Constants.InputNames.DATE_LOCALE_COUNTRY) String dateLocaleCountry,
                                       @Param(Constants.InputNames.OUT_FORMAT) String outFormat,
                                       @Param(Constants.InputNames.OUT_LOCALE_LANG) String outLocaleLang,
                                       @Param(Constants.InputNames.OUT_LOCALE_COUNTRY) String outLocaleCountry
    ) {
        Map<String, String> returnResult = new HashMap<>();

        try {
            returnResult = new DateTimeService().parseDate(date, dateFormat, dateLocaleLang, dateLocaleCountry,
                    outFormat, outLocaleLang, outLocaleCountry);
        } catch (Exception exception) {
            returnResult.put(Constants.OutputNames.EXCEPTION, exception.toString());
            returnResult.put(Constants.OutputNames.RETURN_RESULT, exception.getMessage());
            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        }

        return returnResult;
    }
}
