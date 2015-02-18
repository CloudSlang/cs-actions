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

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.internal.JsonReader;
import com.jayway.jsonpath.internal.spi.json.AbstractJsonProvider;
import com.jayway.jsonpath.internal.spi.json.GsonJsonProvider;
import org.openscore.content.json.utils.ActionsEnum;
import org.openscore.content.json.utils.Constants;
import org.openscore.content.json.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

import static org.openscore.content.json.utils.JsonUtils.populateResult;

/**
 * Created by ioanvranauhp
 * Date 1/12/2015.
 */
public class EditJson {

    /**
     * This operation determines the number of elements in the given JSON array.  If an element
     * is itself another JSON array, it only counts as 1 element; in other
     * words, it will not expand and count embedded arrays.  Null values are also
     * considered to be an element.
     *
     * @return a map containing the output of the operation. Keys present in the map are:
     * <p/>
     * <br><br><b>returnResult</b> - This will contain size of the json array given in the input.
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */
    @Action(name = "Array Size",
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
                                       @Param(value = Constants.InputNames.PROPERTY_NAME) String propertyName,
                                       @Param(value = Constants.InputNames.PROPERTY_VALUE) String propertyValue) {

        Map<String, String> returnResult = new HashMap<>();
        JsonReader jsonReader;
        try {
            JsonUtils.validateEditJsonInputs(jsonObject, jsonPath, action, propertyName, propertyValue);
            jsonReader = getJsonReader(jsonObject);
        } catch (Exception e) {
            return populateResult(returnResult, e);
        }
        if (jsonReader == null) {
            Exception e = new Exception("Could not parse the jsonPath input!");
            return populateResult(returnResult, e);
        }

        String result;
        try {
            final Object jsonPropertyValueObject;
            if (propertyValue.trim().equals(Constants.EMPTY_STRING)) {
                jsonPropertyValueObject = propertyValue;
            } else {
                JsonReader propertyJsonReader = getJsonReader(propertyValue);
                jsonPropertyValueObject = propertyJsonReader.json();
            }
            Object json = editJson(jsonPath, action, propertyName, jsonPropertyValueObject, jsonReader);
            result = json.toString();
        } catch (Exception e) {
            return populateResult(returnResult, e);
        }
        return populateResult(returnResult, result, null);
    }

    private JsonReader getJsonReader(String jsonObject) {
        final AbstractJsonProvider provider = new GsonJsonProvider();
        final Configuration configuration = Configuration.defaultConfiguration().jsonProvider(provider);
        JsonReader jsonReader = new JsonReader(configuration);
        jsonReader.parse(jsonObject);
        return jsonReader;
    }

    private Object editJson(String jsonPath, String action, String propertyName, Object propertyValue, JsonReader jsonReader) {
        ActionsEnum myAction = getAction(action);
        Object json = null;

        switch (myAction) {
            case get:
                json = jsonReader.read(jsonPath);
                break;
            case insert:
                json = jsonReader.put(jsonPath, propertyName, propertyValue).json();
                break;
            case add:
                json = jsonReader.add(jsonPath, propertyValue).json();
                break;
            case update:
                json = jsonReader.set(jsonPath, propertyValue).json();
                break;
            case delete:
                json = jsonReader.delete(jsonPath).json();
                break;
        }
        return json;
    }

    private ActionsEnum getAction(String action) {
        return ActionsEnum.valueOf(action.toLowerCase());
    }
}
