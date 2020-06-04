/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.json.entities.AddPropertyToObjectInputs;
import io.cloudslang.content.json.services.AddPropertyToObjectImpl;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.json.utils.Constants.AddPropertyToObject.NEW_LINE;
import static io.cloudslang.content.json.utils.Constants.InputNames.*;
import static io.cloudslang.content.json.utils.Descriptions.AddPropertyToObject.*;
import static io.cloudslang.content.json.utils.InputsValidation.verifyJsonObject;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class AddPropertyToObject {

    private final ObjectMapper objectMapper = new ObjectMapper();


    public AddPropertyToObject() {
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }


    /**
     * Inserts a new name/value property into a JSON object, where the value is a string.
     * This operation forces the value of the name/vale pair to be a string, no matter what data type it actually is.
     * This means that the newPropertyValue input does not need to be wrapped in quotes as any quotes entered in this
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
     * returnResult - This will contain the JSON with the new property/value added.
     * exception - In case of success response, this result is empty. In case of failure response this result contains
     * the java stack trace of the runtime exception.
     * returnCode - The returnCode of the operation: 0 for success, -1 for failure.
     */
    @Action(name = ADD_PROPERTY_TO_OBJECT,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC, isOnFail = true)
            }
    )
    public Map<String, String> execute(
            @Param(value = JSON_OBJECT, description = JSON_OBJECT_DESC, required = true) String jsonObject,
            @Param(value = NEW_PROPERTY_NAME, description = NEW_PROPERTY_NAME_DESC) String newPropertyName,
            @Param(value = NEW_PROPERTY_VALUE, description = NEW_PROPERTY_VALUE_DESC) String newPropertyValue) {

        newPropertyName = defaultIfEmpty(newPropertyName, EMPTY);
        newPropertyValue = defaultIfEmpty(newPropertyValue, EMPTY);

        final List<String> exceptionMessages = verifyJsonObject(jsonObject, objectMapper);

        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        try {
            final AddPropertyToObjectInputs addPropertyToObjectInputs = AddPropertyToObjectInputs.builder()
                    .jsonObject(jsonObject)
                    .newPropertyName(newPropertyName)
                    .newPropertyValue(newPropertyValue)
                    .build();

            final String resultJson = AddPropertyToObjectImpl.addPropertyToObject(addPropertyToObjectInputs, objectMapper);

            return getSuccessResultsMap(resultJson);
        } catch (Exception exception) {
            return getFailureResultsMap(exception);

        }
    }
}
