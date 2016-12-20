/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
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
import io.cloudslang.content.utils.ListProcessor;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.utils.Constants.OutputNames.RESPONSE;
import static io.cloudslang.content.utils.Constants.OutputNames.RESULT_TEXT;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utils.Constants.ResponseNames.FAILURE;
import static io.cloudslang.content.utils.Constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_FAILURE;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_SUCCESS;

/**
 * Created by giloan on 7/8/2016.
 */
public class ListItemGrabberAction {

    private static final String WHILE_PARSING_INDEX = "\n While parsing index.";
    private static final String LIST = "list";
    private static final String DELIMITER = "delimiter";
    private static final String INDEX = "index";

    /**
     * This operation is used to retrieve a value from a list. When the index of an element from a list is known,
     * this operation can be used to extract the element.
     *
     * @param list      The list to get the value from.
     * @param delimiter The delimiter that separates values in the list.
     * @param index     The index of the value (starting with 0) to retrieve from the list.
     * @return It returns the value found at the specified index in the list, if the value specified for
     * the @index parameter is positive and less than the size of the list. Otherwise, it returns
     * the value specified for @index.
     */
    @Action(name = "List Item Grabber",
            outputs = {
                    @Output(RESULT_TEXT),
                    @Output(RESPONSE),
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = FAILURE, field = RESPONSE, value = RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, isOnFail = true, isDefault = true)
            })
    public Map<String, String> grabItemFromList(@Param(value = LIST, required = true) String list,
                                                @Param(value = DELIMITER, required = true) String delimiter,
                                                @Param(value = INDEX, required = true) String index) {
        Map<String, String> result = new HashMap<>();
        try {
            String[] table = ListProcessor.toArray(list, delimiter);
            int resolvedIndex;
            try {
                resolvedIndex = ListProcessor.getIndex(index, table.length);
            } catch (NumberFormatException e) {
                throw new NumberFormatException(e.getMessage() + WHILE_PARSING_INDEX);
            }

            String value = table[resolvedIndex];
            result.put(RESULT_TEXT, value);
            result.put(RESPONSE, SUCCESS);
            result.put(RETURN_RESULT, value);
            result.put(RETURN_CODE, RETURN_CODE_SUCCESS);
        } catch (Exception e) {
            result.put(RESULT_TEXT, e.getMessage());
            result.put(RESPONSE, FAILURE);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(RETURN_CODE, RETURN_CODE_FAILURE);
        }
        return result;
    }
}
