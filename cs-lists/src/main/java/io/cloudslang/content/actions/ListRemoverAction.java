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
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.utils.Constants.OutputNames.RESPONSE;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utils.Constants.ResponseNames.FAILURE;
import static io.cloudslang.content.utils.Constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_FAILURE;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_SUCCESS;

/**
 * Created by giloan on 7/8/2016.
 */
public class ListRemoverAction {

    private static final String LIST = "list";
    private static final String DELIMITER = "delimiter";
    private static final String ELEMENT = "element";
    private static final String EMPTY_INPUT_EXCEPTION = "One of the operation inputs is empty. All inputs are required!";

    /**
     * This method removes an element from a list of strings.
     *
     * @param list      The list to remove from.
     * @param index     The index of the element to remove from the list.
     * @param delimiter The list delimiter.
     * @return The new list.
     */
    @Action(name = "List Remover",
            outputs = {
                    @Output(RESPONSE),
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = FAILURE, field = RETURN_CODE, value = RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, isOnFail = true, isDefault = true)
            })
    public Map<String, String> removeElement(@Param(value = LIST, required = true) String list,
                                             @Param(value = ELEMENT, required = true) String index,
                                             @Param(value = DELIMITER, required = true) String delimiter) {
        Map<String, String> result = new HashMap<>();
        try {
            if (StringUtils.isEmpty(list) || StringUtils.isEmpty(index) || StringUtils.isEmpty(delimiter)) {
                throw new RuntimeException(EMPTY_INPUT_EXCEPTION);
            } else {
                String[] elements = StringUtils.split(list, delimiter);
                elements = ArrayUtils.remove(elements, Integer.parseInt(index));
                result.put(RESPONSE, SUCCESS);
                result.put(RETURN_RESULT, StringUtils.join(elements, delimiter));
                result.put(RETURN_CODE, RETURN_CODE_SUCCESS);
            }
        } catch (Exception e) {
            result.put(RESPONSE, FAILURE);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(RETURN_CODE, RETURN_CODE_FAILURE);
        }
        return result;
    }
}
