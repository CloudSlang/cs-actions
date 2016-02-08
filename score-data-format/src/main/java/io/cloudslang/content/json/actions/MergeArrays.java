/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
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
import io.cloudslang.content.json.utils.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.json.utils.JsonUtils.isBlank;
import static io.cloudslang.content.json.utils.JsonUtils.populateResult;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public class MergeArrays {

    public static final String NOT_A_VALID_JSON_ARRAY_MESSAGE = "The input value is not a valid JavaScript array";
    public static final String INVALID_JSON_OBJECT_PROVIDED_EXCEPTION_MESSAGE = "Invalid jsonObject provided! ";
    public static final String ARRAY1_MESSAGE = " array1=";
    public static final String ARRAY2_MESSAGE = " array2=";

    /**
     * This operation merge the contents of two JSON arrays. This operation does not modify either of the input arrays.
     * The result is the contents or array1 and array2, merged into a single array. The merge operation add into the result
     * the first array and then the second array.
     *
     * @param array1 The string representation of a JSON array object.
     *               Arrays in JSON are comma separated lists of objects, enclosed in square brackets [ ].
     *               Examples: [1,2,3] or ["one","two","three"] or [{"one":1, "two":2}, 3, "four"]
     * @param array2 The string representation of a JSON array object.
     *               Arrays in JSON are comma separated lists of objects, enclosed in square brackets [ ].
     *               Examples: [1,2,3] or ["one","two","three"] or [{"one":1, "two":2}, 3, "four"]
     * @return a map containing the output of the operation. Keys present in the map are:
     * <p/>
     * <br><br><b>returnResult</b> - This will contain the string representation of the new JSON array with the contents
     * of array1 and array2.
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */
    @Action(name = "Merge Arrays",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = Constants.InputNames.ARRAY, required = true) String array1,
                                       @Param(value = Constants.InputNames.ARRAY, required = true) String array2) {

        Map<String, String> returnResult = new HashMap<>();
        if (isBlank(array1)) {
            final String exceptionValue = NOT_A_VALID_JSON_ARRAY_MESSAGE + ARRAY1_MESSAGE.replaceFirst("=", "");
            return populateResult(returnResult, exceptionValue, new Exception(exceptionValue));
        }

        if (isBlank(array2)) {
            final String exceptionValue = NOT_A_VALID_JSON_ARRAY_MESSAGE + ARRAY2_MESSAGE.replaceFirst("=", "");
            return populateResult(returnResult, new Exception(exceptionValue));
        }

        JsonNode jsonNode1;
        JsonNode jsonNode2;
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonNode1 = mapper.readTree(array1);
        } catch (IOException exception) {
            final String value = INVALID_JSON_OBJECT_PROVIDED_EXCEPTION_MESSAGE + ARRAY1_MESSAGE + array1;
            return populateResult(returnResult, value, exception);
        }
        try {
            jsonNode2 = mapper.readTree(array2);
        } catch (IOException exception) {
            final String value = INVALID_JSON_OBJECT_PROVIDED_EXCEPTION_MESSAGE + ARRAY2_MESSAGE + array2;
            return populateResult(returnResult, value, exception);
        }

        final String result;
        if (jsonNode1 instanceof ArrayNode && jsonNode2 instanceof ArrayNode) {
            final ArrayNode asJsonArray1 = (ArrayNode) jsonNode1;
            final ArrayNode asJsonArray2 = (ArrayNode) jsonNode2;
            final ArrayNode asJsonArrayResult = new ArrayNode(mapper.getNodeFactory());

            asJsonArrayResult.addAll(asJsonArray1);
            asJsonArrayResult.addAll(asJsonArray2);
            result = asJsonArrayResult.toString();
        } else {
            result = NOT_A_VALID_JSON_ARRAY_MESSAGE + ARRAY1_MESSAGE + array1 + ARRAY2_MESSAGE + array2;
            return populateResult(returnResult, new Exception(result));
        }
        return populateResult(returnResult, result, null);
    }
}
