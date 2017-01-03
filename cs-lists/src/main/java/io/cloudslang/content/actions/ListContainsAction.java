/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.utils.Constants;
import io.cloudslang.content.utils.InputsUtils;
import io.cloudslang.content.utils.ListProcessor;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.utils.Constants.EMPTY_STRING;
import static io.cloudslang.content.utils.Constants.FALSE;
import static io.cloudslang.content.utils.Constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.utils.Constants.OutputNames.RESPONSE;
import static io.cloudslang.content.utils.Constants.OutputNames.RESPONSE_TEXT;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utils.Constants.ResponseNames.FAILURE;
import static io.cloudslang.content.utils.Constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_FAILURE;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_SUCCESS;
import static io.cloudslang.content.utils.Constants.TRUE;

/**
 * Created by moldovas on 7/12/2016.
 */

public class ListContainsAction {
    private static final String SUBLIST = "sublist";
    private static final String DELIMITER = "delimiter";
    private static final String CONTAINER = "container";
    private static final String IGNORE_CASE = "ignoreCase";

    /**
     * This method checks to see if a list contains every element in another list.
     *
     * @param sublist      The contained list.
     * @param container    The containing list.
     * @param delimiter    A delimiter separating elements in the two lists. Default is a comma.
     * @param ignoreCase   If set to 'True' then the compare is not case sensitive. Default is True.
     * @return sublist is contained in container or not.
     */

    @Action(name = "List Contains All",
            outputs = {
                    @Output(RESPONSE_TEXT),
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = FAILURE, field = RETURN_CODE, value = RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, isOnFail = true, isDefault = true)
            })
    public Map<String, String> containsElement(@Param(value = SUBLIST, required = true) String sublist,
                                               @Param(value = CONTAINER, required = true) String container,
                                               @Param(value = DELIMITER) String delimiter,
                                               @Param(value = IGNORE_CASE) String ignoreCase
                                               ) {
        Map<String, String> result = new HashMap<>();
        try {
            delimiter = InputsUtils.getInputDefaultValue(delimiter, Constants.DEFAULT_DELIMITER);
            String[] subArray = sublist.split(delimiter);
            String[] containerArray = container.split(delimiter);
            String[] uncontainedArray = ListProcessor.getUncontainedArray(subArray, containerArray, InputsUtils.toBoolean(ignoreCase, true, IGNORE_CASE));

            if (ListProcessor.arrayElementsAreNull(uncontainedArray)) {
                result.put(RESPONSE_TEXT, TRUE);
                result.put(RETURN_RESULT, EMPTY_STRING);
                result.put(RETURN_CODE, RETURN_CODE_SUCCESS);
                result.put(EXCEPTION, EMPTY_STRING);
            } else {
                result.put(RESPONSE, FALSE);
                result.put(RETURN_CODE, RETURN_CODE_FAILURE);
                result.put(RETURN_RESULT, StringUtils.join(uncontainedArray, delimiter));
                result.put(EXCEPTION, EMPTY_STRING);
            }

        } catch (Exception e) {
            result.put(RESPONSE, FAILURE);
            result.put(RETURN_RESULT, EMPTY_STRING);
            result.put(RETURN_CODE, RETURN_CODE_FAILURE);
            result.put(EXCEPTION, e.getMessage());
        }
        return result;
    }

}