/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.json.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.utils.StringUtilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.json.utils.JsonUtils.populateResult;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public class ArraySize {

    public static final String NOT_A_VALID_JSON_ARRAY_MESSAGE = "The input value is not a valid JavaScript array!";

    /**
     * This operation determines the number of elements in the given JSON array.  If an element
     * is itself another JSON array, it only counts as 1 element; in other
     * words, it will not expand and count embedded arrays.  Null values are also
     * considered to be an element.
     *
     * @param array The string representation of a JSON array object.
     * @return a map containing the output of the operation. Keys present in the map are:
     * <p/>
     * <br><br><b>returnResult</b> - This will contain size of the json array given in the input.
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */
    @Action(name = "Array Size",
            outputs = {
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = Constants.InputNames.ARRAY, required = true) String array) {

        Map<String, String> returnResult = new HashMap<>();

        if (StringUtilities.isBlank(array)) {
            return populateResult(returnResult, new Exception(NOT_A_VALID_JSON_ARRAY_MESSAGE));
        }
        JsonNode jsonNode;
        try {
            ObjectMapper mapper = new ObjectMapper();
            jsonNode = mapper.readTree(array);
        } catch (IOException exception) {
            final String value = "Invalid jsonObject provided! " + exception.getMessage();
            return populateResult(returnResult, value, exception);
        }
        final String result;
        if (jsonNode instanceof ArrayNode) {
            final ArrayNode asJsonArray = (ArrayNode) jsonNode;
            result = Integer.toString(asJsonArray.size());
        } else {
            return populateResult(returnResult, new Exception(NOT_A_VALID_JSON_ARRAY_MESSAGE));
        }
        return populateResult(returnResult, result, null);
    }
}
