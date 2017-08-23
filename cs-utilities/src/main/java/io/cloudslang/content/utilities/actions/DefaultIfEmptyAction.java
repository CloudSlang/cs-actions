/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.BooleanUtilities;
import io.cloudslang.content.utils.StringUtilities;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ReturnCodes.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;


/**
 * Created by moldovai on 8/21/2017.
 */
public class DefaultIfEmptyAction {

    /**
     * This operation checks if a string is empty and if it's true a default value will
     * be assigned instead of the initial string.
     *
     * @param initial_value The initial string where the default value will be assigned.
     * @param default_value The default value used to replace the empty string.
     * @param trim A variable used to check if the initial string is blank or empty.
     * @return a map containing the output of the operation. Keys present in the map are:
     * <p/>
     * <br><br><b>returnResult</b> - This will contain the replaced string with the default value.
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */

    private static final String INITIAL_VALUE = "initial_value";
    private static final String DEFAULT_VALUE = "default_value";
    private static final String TRIM = "trim";

    @Action(name = "Default value if string is empty",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION),
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(
            @Param(value = INITIAL_VALUE) String initial_value,
            @Param(value = DEFAULT_VALUE, required = true) String default_value,
            @Param(value = TRIM) String trim) {

        try {
            trim = StringUtilities.defaultIfBlank(trim, "true");

            boolean validTrim = BooleanUtilities.toBoolean(trim);

            String resultString;
            if (validTrim) {
                resultString = StringUtilities.defaultIfBlank(initial_value, default_value);
            } else {
                resultString = StringUtilities.defaultIfEmpty(initial_value, default_value);
            }
            return getSuccessResultsMap(resultString);

        } catch (Exception e) {
            final Map<String, String> result = getFailureResultsMap(e);
            result.put(RETURN_RESULT, "Failure");
            return result;
        }
    }
}