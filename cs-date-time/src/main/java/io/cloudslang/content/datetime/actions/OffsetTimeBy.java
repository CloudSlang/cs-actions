/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */




package io.cloudslang.content.datetime.actions;

/**
 * Created by cadm on 4/25/2016.
 */

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
import static io.cloudslang.content.datetime.utils.DatetimeInputs.LOCALE_COUNTRY;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.LOCALE_DATE;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.LOCALE_LANG;
import static io.cloudslang.content.datetime.utils.DatetimeInputs.LOCALE_OFFSET;

public class OffsetTimeBy {

    /**
     * Changes the time represented by a date by the specified number of seconds.
     * If locale is specified, it will return the date and time string based on the locale.
     * Otherwise, default locale will be used.
     *
     * @param date          Current time.
     * @param offset        The offset value specified number of seconds.
     * @param localeLang    The locale language for date and time string. If localeLang is 'unix' the
     *                      localeCountry input is ignored and the result will be the current UNIX
     *                      timestamp. Examples:  en, ja, unix.
     * @param localeCountry The locale country for date and time string. For example, US or JP.
     *                      If localeLang is not specified, this input will be ignored.
     */
    @Action(name = "Offset Time By",
            description = "Changes the time represented by a date by the specified number of seconds",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, isOnFail = true)})

    public Map<String, String> execute(
            @Param(value = LOCALE_DATE, required = true) String date,
            @Param(value = LOCALE_OFFSET, required = true) String offset,
            @Param(value = LOCALE_LANG) String localeLang,
            @Param(value = LOCALE_COUNTRY) String localeCountry) {
        try {
            return OutputUtilities.getSuccessResultsMap(DateTimeService.offsetTimeBy(date, offset, localeLang, localeCountry));
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }
}