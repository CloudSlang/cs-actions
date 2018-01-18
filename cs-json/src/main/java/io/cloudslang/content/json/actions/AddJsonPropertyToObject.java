
package io.cloudslang.content.json.actions;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.OtherValues;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.JsonUtils;
import io.cloudslang.content.utils.StringUtilities;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.json.utils.JsonUtils.populateResult;

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
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.JSON_OBJECT, required = true) String jsonObject,
            @Param(value = Constants.InputNames.NEW_PROPERTY_NAME, required = true) String newPropertyName,
            @Param(value = Constants.InputNames.NEW_PROPERTY_VALUE, required = true) String newPropertyValue,
            @Param(value = Constants.InputNames.VALIDATE_VALUE) String validateValue) {

        Map<String, String> returnResult = new HashMap<>();
        if (jsonObject == null || jsonObject.trim().equals(OtherValues.EMPTY_STRING)) {
            return populateResult(returnResult, new Exception("Empty jsonObject provided!"));
        }

        ObjectMapper mapper = new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        final boolean validateValueBoolean = JsonUtils.parseBooleanWithDefault(validateValue, true);

        if (StringUtilities.isBlank(newPropertyValue)) {
            final String exceptionValue = "The value for the property " + newPropertyName + " it is not a valid JSON object!";
            return populateResult(returnResult, new Exception(exceptionValue));
        }

        if (newPropertyName == null) {
            return populateResult(returnResult, new Exception("Null newPropertyName provided!"));
        }

        JsonNode jsonRoot;
        try {
            jsonRoot = mapper.readTree(jsonObject);
        } catch (Exception exception) {
            final String exceptionValue = "Invalid jsonObject provided! " + exception.getMessage();
            return populateResult(returnResult, exceptionValue, exception);
        }

        ContainerNode jsonNodes = null;
        JsonNode jsonNodeValueWrapper;
        try {
            jsonNodeValueWrapper = mapper.readTree(newPropertyValue);
        } catch (IOException exception) {
            if (!validateValueBoolean) {
                jsonNodeValueWrapper = mapper.valueToTree(newPropertyValue);
            } else {
                final String exceptionValue = "The value for the property " + newPropertyName + " it is not a valid JSON object!";
                return populateResult(returnResult, exceptionValue, exception);
            }
        }
        if (jsonRoot instanceof ObjectNode) {
            jsonNodes = ((ObjectNode) jsonRoot).putPOJO(newPropertyName, jsonNodeValueWrapper);
        }
        if (jsonRoot instanceof ArrayNode) {
            jsonNodes = ((ArrayNode) jsonRoot).add(jsonNodeValueWrapper);
        }

        if (jsonNodes == null) {
            return populateResult(returnResult, new Exception("The value cannot be added!"));
        }
        return populateResult(returnResult, jsonNodes.toString(), null);
    }
}
