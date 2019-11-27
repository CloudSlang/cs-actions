/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.utils.StringUtilities;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.json.utils.JsonUtils.populateResult;

public class AddPropertyToObject {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AddPropertyToObject() {
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }

    /**
     * Inserts a new name/value property into a JSON object, where the value is a string.
     * This operation forces the value of the name/vale pair to be a string, no matter what data type it actually is.
     * This means that the <b>newPropertyValue</b> input does not need to be wrapped in quotes as any quotes entered in this
     * input will be escaped, ending up in the properties' value.
     * In case that a new property with the same name as an existing one is added, the old property's value will be overwritten.
     * The 'Add JSON Property to Object' operation should be used to add values of type other than string,
     * for example: another object, an array or a number.
     *
     * @param jsonObject       String representation of a JSON object. Objects in JSON are a collection of name value pairs,
     *                         separated by a colon and surrounded with curly brackets {}. The name must be a string value,
     *                         and the value can be a single string or any valid JSON object or array.
     *                         Examples: {"one":1, "two":2}, {"one":{"a":"a","B":"B"}, "two":"two", "three":[1,2,3.4]}
     * @param newPropertyName  The name of the new property to add to the JSON object. There is no rule as to which character to use.
     *                         Examples: property1, some_property, another property
     * @param newPropertyValue The value for the new property. This is interpreted as a string, no matter what the contents of the input.
     *                         Examples: value, 1, [1,2,3]
     * @return a map containing the output of the operation. Keys present in the map are:
     * <p/>
     * <br><br><b>returnResult</b> - This will contain the JSON with the new property/value added.
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */
    @Action(name = "Add Property to Object",
            outputs = {
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            }
    )
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.JSON_OBJECT, required = true) String jsonObject,
            @Param(value = Constants.InputNames.NEW_PROPERTY_NAME) String newPropertyName,
            @Param(value = Constants.InputNames.NEW_PROPERTY_VALUE) String newPropertyValue) {

        Map<String, String> returnResult = new HashMap<>();
        if (!validateInput(jsonObject, newPropertyName, newPropertyValue, returnResult)) {
            return returnResult;
        }
        doExecute(jsonObject, newPropertyName, newPropertyValue, returnResult);
        return returnResult;
    }


    /**
     * Validates the input for this action.
     *
     * @param jsonObject       The value of the <b>jsonObject</b> input.
     * @param newPropertyName  The value of the <b>newPropertyName</b> input.
     * @param newPropertyValue The value of the <b>newPropertyValue</b> input.
     * @param returnResult     The map that will be used as return result for this action.
     * @return True if input is valid, false otherwise.
     */
    private boolean validateInput(String jsonObject, String newPropertyName, String newPropertyValue,
                                  Map<String, String> returnResult) {
        return validateJsonObject(jsonObject, returnResult);
    }

    /**
     * Validates the <b>jsonObject</b> input.
     *
     * @param jsonObject   The value of the <b>jsonObject</b> input.
     * @param returnResult The map that will be used as return result for this action.
     * @return True if value of <b>jsonObject</b> is valid, false otherwise.
     */
    private boolean validateJsonObject(String jsonObject, Map<String, String> returnResult) {
        if (StringUtilities.isBlank(jsonObject)) {
            final String exMsg = "Empty JSON string";
            populateResult(returnResult, new Exception(exMsg));
            return false;
        }

        try {
            JsonNode jsonRoot = objectMapper.readTree(jsonObject);
            if (jsonRoot == null || !(jsonRoot.isContainerNode() && jsonRoot.isObject())) {
                throw new Exception();
            }
        } catch (Exception exception) {
            final String exMsg = Constants.InputNames.JSON_OBJECT + " is not a valid JSON Object";
            populateResult(returnResult, new Exception(exMsg));
            return false;
        }

        return true;
    }

    /**
     * Does the actual logic for this Action
     *
     * @param jsonObject       The value of the <b>jsonObject</b> input.
     * @param newPropertyName  The value of the <b>newPropertyName</b> input.
     * @param newPropertyValue The value of the <b>newPropertyValue</b> input.
     * @param returnResult     The map that will be used as return result for this action.
     */
    private void doExecute(String jsonObject, String newPropertyName, String newPropertyValue,
                           Map<String, String> returnResult) {
        try {
            JsonNode jsonRoot = objectMapper.readTree(jsonObject);
            if(!(jsonRoot instanceof ObjectNode)){
                throw new Exception("The value could not be added!");
            }
            ContainerNode jsonResult = ((ObjectNode) jsonRoot).put(newPropertyName, newPropertyValue);
            if (jsonResult == null) {
                throw new Exception("The value could not be added!");
            }
            populateResult(returnResult, jsonResult.toString(), null);
        } catch (Exception ex) {
            populateResult(returnResult, ex);
        }
    }

}
