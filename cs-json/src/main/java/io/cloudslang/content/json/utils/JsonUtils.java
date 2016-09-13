/*******************************************************************************
 * (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.json.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonContext;
import com.jayway.jsonpath.spi.json.AbstractJsonProvider;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import io.cloudslang.content.constants.OtherValues;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.StringUtilities;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.json.utils.ActionsEnum.insert;
import static io.cloudslang.content.json.utils.JsonExceptionValues.INVALID_JSONOBJECT;
import static io.cloudslang.content.json.utils.JsonExceptionValues.INVALID_JSONPATH;

/**
 * Created by ioanvranauhp
 * Date 2/9/2015.
 */
public class JsonUtils {

    public static Map<String, String> populateResult(Map<String, String> returnResult, String value, Exception exception) {
        returnResult.put(OutputNames.RETURN_RESULT, value);
        if (exception != null) {
            returnResult.put(OutputNames.EXCEPTION, exception.getMessage());
            returnResult.put(OutputNames.RETURN_CODE, ReturnCodes.FAILURE);
        } else {
            returnResult.put(OutputNames.RETURN_CODE, ReturnCodes.SUCCESS);
        }
        return returnResult;
    }

    public static Map<String, String> populateResult(Map<String, String> returnResult, Exception exception) {
        if (exception != null) {
            return populateResult(returnResult, exception.getMessage(), exception);
        } else {
            return populateResult(returnResult, OtherValues.EMPTY_STRING, null);
        }
    }

    public static void validateEditJsonInputs(String jsonObject, String jsonPath, String action, String name, String value) throws Exception {
        if (StringUtilities.isBlank(jsonObject)) {
            throw new Exception("Empty jsonObject provided!");
        }
        if (StringUtilities.isBlank(jsonPath)) {
            throw new Exception("Empty jsonPath provided!");
        }
        if (StringUtilities.isBlank(action)) {
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
            if (StringUtilities.isBlank(name)) {
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

    public static JsonPath getValidJsonPath(final String jsonPath) {
        try {
            return JsonPath.compile(jsonPath);
        } catch (IllegalArgumentException iae) {
            throw illegalArgumentExceptionWithMessage(INVALID_JSONPATH, iae);
        }
    }

    @NotNull
    public static JsonContext getValidJsonContext(final String jsonObject) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            final AbstractJsonProvider provider = new JacksonJsonNodeJsonProvider(objectMapper);
            final Configuration configuration = Configuration.defaultConfiguration()
                    .jsonProvider(provider);
            final JsonContext jsonContext = new JsonContext(configuration);
            jsonContext.parse(jsonObject);
            return jsonContext;
        } catch (IllegalArgumentException iae) {
            throw illegalArgumentExceptionWithMessage(INVALID_JSONOBJECT, iae);
        }
    }

    public static boolean parseBooleanWithDefault(String booleanValue, boolean defaultValue) {
        if (StringUtilities.isBlank(booleanValue)) {
            return defaultValue;
        } else {
            return Boolean.valueOf(booleanValue);
        }
    }

    @NotNull
    public static IllegalArgumentException illegalArgumentExceptionWithMessage(@NotNull final String message, @NotNull final Throwable throwable) {
        final IllegalArgumentException iae = new IllegalArgumentException(message);
        iae.setStackTrace(throwable.getStackTrace());
        return iae;
    }
}
