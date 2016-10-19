package io.cloudslang.content.datetime.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.datetime.services.DateTimeService;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.DATE_FORMAT;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.LOCALE_COUNTRY;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.LOCALE_LANG;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.TIMEZONE;

/**
 * Created by stcu on 21.04.2016.
 */
public class GetCurrentDateTime {
    /**
     * Check the current date and time, and returns a java DateAndTime formatted string of it.
     * If locale is specified, it will return the date and time string based on the locale.
     * Otherwise, default locale will be used.
     *
     * @param localeLang    The locale language for date and time string. If localeLang is 'unix' the
     *                      localeCountry input is ignored and the result will be the current UNIX
     *                      timestamp. Examples:  en, ja, unix.
     * @param localeCountry The locale country for date and time string. For example, US or JP.
     *                      If localeLang is not specified, this input will be ignored.
     * @param timezone      The timezone you want the current datetime to be.
     *                      Examples: GMT, GMT+1, PST
     * @param dateFormat    The format of the output date/time.The Default date/time format is from the Java
     *                      environment (which is dependent on the OS date/time format)
     * @return Current date and time.
     */
    @Action(name = "Get Current Date And Time",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true)})

    public Map<String, String> execute(
            @Param(LOCALE_LANG) String localeLang,
            @Param(LOCALE_COUNTRY) String localeCountry,
            @Param(TIMEZONE) String timezone,
            @Param(DATE_FORMAT) String dateFormat) {

        try {
            return OutputUtilities.getSuccessResultsMap(DateTimeService.getCurrentDateTime(localeLang, localeCountry, timezone, dateFormat));
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }
}