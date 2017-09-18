/*
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
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
import static io.cloudslang.content.datetime.utils.DatetimeInputs.DATE_LOCALE_COUNTRY;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.DATE_LOCALE_LANG;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.LOCALE_DATE;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.OUT_FORMAT;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.OUT_LOCALE_COUNTRY;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.OUT_LOCALE_LANG;

/**
 * Created by stcu on 27.04.2016.
 */
public class ParseDate {
    /**
     * This operation converts the date input value from one date/time format (specified by dateFormat)
     * to another date/time format (specified by outFormat) using locale settings (language and country).
     * You can use the flow "Get Current Date and Time" to check upon the default date/time format from
     * the Java environment.
     *
     * @param date              the date to parse/convert
     * @param dateFormat        the format of the input date
     * @param dateLocaleLang    the locale language for input dateFormat string. It will be ignored if
     *                          dateFormat is empty. default locale language from the Java environment
     *                          (which is dependent on the OS locale language)
     * @param dateLocaleCountry the locale country for input dateFormat string. It will be ignored
     *                          if dateFormat is empty or dateLocaleLang is empty. Default locale country
     *                          from the Java environment (which is dependent on the OS locale country)
     * @param outFormat         The format of the output date/time. Default date/time format from the Java
     *                          environment (which is dependent on the OS date/time format)
     * @param outLocaleLang     The locale language for output string. It will be ignored if outFormat is
     *                          empty.
     * @param outLocaleCountry  The locale country for output string. It will be ignored if outFormat
     *                          is empty or outLocaleLang is empty.
     * @return The date in the new format
     */
    @Action(name = "Parse Date",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true)})

    public Map<String, String> execute(@Param(value = LOCALE_DATE, required = true) String date,
                                       @Param(value = DATE_FORMAT) String dateFormat,
                                       @Param(value = DATE_LOCALE_LANG) String dateLocaleLang,
                                       @Param(value = DATE_LOCALE_COUNTRY) String dateLocaleCountry,
                                       @Param(value = OUT_FORMAT) String outFormat,
                                       @Param(value = OUT_LOCALE_LANG) String outLocaleLang,
                                       @Param(value = OUT_LOCALE_COUNTRY) String outLocaleCountry) {
        try {
            return OutputUtilities.getSuccessResultsMap(
                    DateTimeService.parseDate(date, dateFormat, dateLocaleLang, dateLocaleCountry, outFormat, outLocaleLang, outLocaleCountry));
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }
}