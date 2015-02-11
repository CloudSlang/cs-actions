/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/

package org.openscore.content.json.actions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import org.openscore.content.json.utils.Constants;

import java.util.HashMap;
import java.util.Map;

import static org.openscore.content.json.utils.JsonUtils.populateResult;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public class AddJsonPropertyToObject {

    /**
     * Inserts a new name/value property into a JSON object, where the value is a valid JSON string.
     * If the <b>newPropertyValue</b> input is not a valid string representation of a JSON object, the operation fails.
     * This operation can be used to add a property with a simple string value.
     *
     * @param jsonObject       The string representation of a JSON object.
     *                         Objects in JSON are a collection of name value pairs, separated by a colon and surrounded with curly brackets {}.
     *                         The name must be a string value, and the value can be a single string or any valid JSON object or array.
     *                         Examples: {"one":1, "two":2}, {"one":{"a":"a","B":"B"}, "two":"two", "three":[1,2,3.4]}
     * @param newPropertyName  The name of the new property that will be added to the JSON object.
     *                         Examples: property1, some_property, another property
     * @param newPropertyValue The value for the new property. This must be a valid JSON object.
     *                         Examples: 1, {"A":"A"}, [1,2,3,4]
     * @return a map containing the output of the operation. Keys present in the map are:
     * <p/>
     * <br><br><b>returnResult</b> - This will contain the JSON with the new property/value added.
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */
    @Action(name = "Add JSON Property to Object",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.JSON_OBJECT, required = true) String jsonObject,
            @Param(value = Constants.InputNames.NEW_PROPERTY_NAME, required = true) String newPropertyName,
            @Param(value = Constants.InputNames.NEW_PROPERTY_VALUE, required = true) String newPropertyValue) {

        Map<String, String> returnResult = new HashMap<>();
        if (jsonObject == null || jsonObject.trim().equals(Constants.EMPTY_STRING)) {
            final String exceptionValue = "Empty jsonObject provided!";
            return populateResult(returnResult, exceptionValue, new Exception(exceptionValue));
        }

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement;
        JsonObject jsonRoot;

        if (newPropertyValue == null || newPropertyValue.trim().equals(Constants.EMPTY_STRING)) {
            final String exceptionValue = "The value for the property " + newPropertyName + " it is not a valid JSON object!";
            return populateResult(returnResult, exceptionValue, new Exception(exceptionValue));
        }

        if (newPropertyName == null) {
            final String exceptionValue = "Null newPropertyName provided!";
            return populateResult(returnResult, exceptionValue, new Exception(exceptionValue));
        }

        try {
            jsonElement = jsonParser.parse(jsonObject);
            jsonRoot = jsonElement.getAsJsonObject();
        } catch (Exception exception) {
            final String exceptionValue = "Invalid jsonObject provided! " + exception.getMessage();
            return populateResult(returnResult, exceptionValue, exception);
        }

        JsonElement jsonElementWrapper;
        try {
            jsonElementWrapper = jsonParser.parse(newPropertyValue);
            jsonRoot.add(newPropertyName, jsonElementWrapper);
        } catch (JsonSyntaxException exception) {
            final String exceptionValue = "The value for the property " + newPropertyName + " it is not a valid JSON object!";
            return populateResult(returnResult, exceptionValue, exception);
        }

        return populateResult(returnResult, jsonRoot.toString(), null);
    }
}
