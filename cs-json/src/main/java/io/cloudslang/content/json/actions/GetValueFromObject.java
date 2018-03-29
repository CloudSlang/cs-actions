/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
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

/**
 * Created by ioanvranauhp
 * Date 2/5/2015.
 */
public class GetValueFromObject {

    private static final String ESCAPED_SLASH = "\\";

    /**
     * This operation accepts an object in the JavaScript Object Notation format (JSON) and returns a value for the specified key.
     *
     * @param object The string representation of a JSON object.
     *               Objects in JSON are a collection of name value pairs, separated by a colon and surrounded with curly brackets {}.
     *               The name must be a string value, and the value can be a single string or any valid JSON object or array.
     *               Examples: {"one":1, "two":2}, {"one":{"a":"a","B":"B"}, "two":"two", "three":[1,2,3.4]}
     * @param key    The key in the object to get the value of.
     *               Examples: city, location[0].city
     * @return a map containing the output of the operation. Keys present in the map are:
     * <p/>
     * <br><br><b>returnResult</b> - This will contain the value for the specified key in the object.
     * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * this result contains the java stack trace of the runtime exception.
     * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */
    @Action(name = "Get Value from Object",
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
            @Param(value = Constants.InputNames.OBJECT, required = true) String object,
            @Param(value = Constants.InputNames.KEY, required = true) String key) {

        Map<String, String> returnResult = new HashMap<>();

        if (StringUtilities.isBlank(object)) {
            return populateResult(returnResult, new Exception("Empty object provided!"));
        }
        if (key == null) {
            return populateResult(returnResult, new Exception("Null key provided!"));
        }

        final JsonNode jsonRoot;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            jsonRoot = objectMapper.readTree(object);
        } catch (Exception exception) {
            final String value = "Invalid object provided! " + exception.getMessage();
            return populateResult(returnResult, value, exception);
        }

        int startIndex = 0;
        final JsonNode valueFromObject;
        try {
            valueFromObject = getObject(jsonRoot, key.split(ESCAPED_SLASH + "."), startIndex);
        } catch (Exception exception) {
            return populateResult(returnResult, exception);
        }
        if (valueFromObject.isValueNode()) {
            return populateResult(returnResult, valueFromObject.asText(), null);
        } else {
            return populateResult(returnResult, valueFromObject.toString(), null);
        }

    }

    private JsonNode getObject(JsonNode jsonObject, String[] keys, int startIndex) throws Exception {
        if (startIndex >= keys.length) {
            return jsonObject;
        }
        String aKey = keys[startIndex];
        if (jsonObject instanceof ObjectNode) {
            //base case
            if (keys.length == 0)
                return getValue(jsonObject, aKey);
                //recursive call
            else {
                final ObjectNode newJsonObject;
                final JsonNode valueFromKey = getValue(jsonObject, aKey);
                if (valueFromKey instanceof ObjectNode) {
                    newJsonObject = (ObjectNode) valueFromKey;
                } else {
                    return valueFromKey;
                }
                return getObject(newJsonObject, keys, ++startIndex);
            }
        } else {
            throw new Exception("The key does not exist in JavaScript object!");
        }
    }

    private JsonNode getValue(JsonNode jsonElement, String aKey) throws Exception {

        //non JSON array object
        if (!aKey.matches(".*" + ESCAPED_SLASH + "[[0-9]+]$")) {
            if (jsonElement.get(aKey) != null) {
                return jsonElement.get(aKey);
            } else
                throw new Exception("The " + aKey + " key does not exist in JavaScript object!");
        }
        //JSON array object
        else {
            int startIndex = aKey.indexOf("[");
            int endIndex = aKey.indexOf("]");
            String oneKey = aKey.substring(0, startIndex);
            int index = 0;
            try {
                index = Integer.parseInt(aKey.substring(startIndex + 1, endIndex));
            } catch (NumberFormatException e) {
                throw new Exception("Invalid index provided: " + index);
            }
            JsonNode subObject;
            subObject = jsonElement.get(oneKey);
            if (jsonElement.get(oneKey) != null) {
                if (subObject instanceof ArrayNode) {
                    final ArrayNode asJsonArray = (ArrayNode) subObject;
                    if ((index >= asJsonArray.size()) || (index < 0)) {
                        throw new Exception("The provided " + index + " index is out of range! Provide a valid index value in the provided JSON!");
                    } else {
                        return asJsonArray.get(index);
                    }
                } else {
                    throw new Exception("Invalid json array provided: " + subObject.toString() + " ");
                }
            } else {
                throw new Exception("The " + aKey + " key does not exist in JavaScript object!");
            }
        }
    }
}
