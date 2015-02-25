/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package org.openscore.content.json.utils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.internal.JsonReader;
import com.jayway.jsonpath.internal.spi.json.AbstractJsonProvider;

import java.util.Map;

import static org.openscore.content.json.utils.ActionsEnum.insert;

/**
 * Created by ioanvranauhp
 * Date 2/9/2015.
 */
public class JsonUtils {

    public static Map<String, String> populateResult(Map<String, String> returnResult, String value, Exception exception) {
        returnResult.put(Constants.OutputNames.RETURN_RESULT, value);
        if (exception != null) {
            returnResult.put(Constants.OutputNames.EXCEPTION, exception.getMessage());
            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
        } else {
            returnResult.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
        }
        return returnResult;
    }

    public static Map<String, String> populateResult(Map<String, String> returnResult, Exception exception) {
        if (exception != null) {
            return populateResult(returnResult, exception.getMessage(), exception);
        } else {
            return populateResult(returnResult, Constants.EMPTY_STRING, null);
        }
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().equals(Constants.EMPTY_STRING);
    }

    public static void validateEditJsonInputs(String jsonObject, String jsonPath, String action, String name, String value) throws Exception {
        if (isBlank(jsonObject)) {
            throw new Exception("Empty jsonObject provided!");
        }
        if (isBlank(jsonPath)) {
            throw new Exception("Empty jsonPath provided!");
        }
        if (isBlank(action)) {
            throw new Exception("Empty action provided!");
        }

        final String actionString = action.toLowerCase().trim();

        boolean exists = false;
        String actionEnumValues = "";
        for (ActionsEnum actionsEnum : ActionsEnum.values()) {
            final String actionEnumValue = actionsEnum.getValue();
            actionEnumValues += actionEnumValue + " ";
            if (actionString.equals(actionEnumValue)) {
                exists = true;
            }
        }
        if (!exists) {
            throw new Exception("Invalid action provided! Action should be one of the values: " + actionEnumValues);
        }

        if (actionString.equals(insert.getValue())) {
            if (isBlank(name)) {
                throw new Exception("Empty name provided for insert action!");
            }
        }

        checkForNullValue(actionString, value);
    }

    private static void checkForNullValue(String actionString, String value) throws Exception {
        final ActionsEnum actionEnum = ActionsEnum.valueOf(actionString);
        if (actionEnum.getNeedValue()) {
            if (value == null) {
                throw new Exception("Null value provided for " + actionEnum.getValue() + " action!");
            }
        }
    }

    public static JsonReader getJsonReader(String jsonObject, final AbstractJsonProvider provider) {
        final Configuration configuration = Configuration.defaultConfiguration().jsonProvider(provider);
        JsonReader jsonReader = new JsonReader(configuration);
        jsonReader.parse(jsonObject);
        return jsonReader;
    }
}
