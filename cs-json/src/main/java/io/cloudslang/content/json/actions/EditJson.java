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

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.jsonpath.spi.json.AbstractJsonProvider;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import io.cloudslang.content.json.utils.ActionsEnum;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.json.utils.JsonUtils.populateResult;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public class EditJson {

    /**
     * * This operation edits a JSON object given in the form of a string using a jsonPath and returns the edited json
     *
     * @param jsonObject The JSON object in a form of a string
     * @param jsonPath   The JSON Path query used for editing the json object
     * @param action     The action used for editing the json. Valid values are: get, insert, add, update and delete.
     *                   The difference between insert and add action is that add is used for adding data into an array
     *                   based on the jsonPath provided and insert action inserts an new property and a new value in the json
     *                   based on the jsonPath provided.
     * @param name       The property name used for insert operation
     * @param value      The property value used for insert, add and update operations.
     * @return a map containing the output of the operation. Keys present in the map are:
     * <p/>
     * <br><br><b>returnResult</b> - This will contain the edited json based on the action type, jsonPath and
     * name and value inputs
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception or just the error message.
     * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */
    @Action(name = "Edit Json",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = Constants.InputNames.JSON_OBJECT, required = true) String jsonObject,
                                       @Param(value = Constants.InputNames.JSON_PATH, required = true) String jsonPath,
                                       @Param(value = Constants.InputNames.ACTION, required = true) String action,
                                       @Param(value = Constants.InputNames.NAME) String name,
                                       @Param(value = Constants.InputNames.VALUE) String value,
                                       @Param(value = Constants.InputNames.VALIDATE_VALUE) String validateValue) {

        Map<String, String> returnResult = new HashMap<>();
        JsonContext jsonContext;
        final AbstractJsonProvider provider = new JacksonJsonNodeJsonProvider();
        boolean validateValueBoolean = JsonUtils.parseBooleanWithDefault(validateValue, true);
        try {
            JsonUtils.validateEditJsonInputs(jsonObject, jsonPath, action, name, value);
            jsonContext = JsonUtils.getJsonContext(jsonObject, provider);
        } catch (Exception e) {
            return populateResult(returnResult, e);
        }
        final ActionsEnum actionEnum = ActionsEnum.valueOf(action);
        String result;
        try {
            Object valueObject;
            JsonContext valueJsonContext;
            try {
                valueJsonContext = JsonUtils.getJsonContext(value, provider);
                valueObject = valueJsonContext.json();
            } catch (Exception e) {
                if (!validateValueBoolean || !actionEnum.getNeedValue()) {
                    valueObject = value;
                } else {
                    throw e;
                }
            }
            Object json = editJson(jsonPath, action, name, valueObject, jsonContext);
            result = json.toString();
        } catch (Exception e) {
            return populateResult(returnResult, e);
        }
        return populateResult(returnResult, result, null);
    }

    private Object editJson(String jsonPath, String action, String name, Object value, JsonContext jsonContext) {
        ActionsEnum myAction = ActionsEnum.valueOf(action.toLowerCase());
        Object json = null;

        switch (myAction) {
            case get:
                json = jsonContext.read(jsonPath);
                break;
            case insert:
                json = jsonContext.put(jsonPath, name, value).json();
                break;
            case add:
                json = jsonContext.add(jsonPath, value).json();
                break;
            case update:
                json = jsonContext.set(jsonPath, value).json();
                break;
            case delete:
                json = jsonContext.delete(jsonPath).json();
                break;
        }
        return json;
    }
}
