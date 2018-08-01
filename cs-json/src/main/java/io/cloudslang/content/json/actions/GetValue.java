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
/*
 * (c) Copyright 2018 EntIT Software LLC, a Micro Focus company, L.P.
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
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static io.cloudslang.content.constants.OtherValues.NULL_STRING;
import static io.cloudslang.content.json.services.JsonService.evaluateJsonPathQueryWithoutDup;
import static io.cloudslang.content.json.utils.Constants.InputNames.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

/**
 * created by Alexandra Boicu
 * 30/07/2018
 */
public class GetValue {
    /**
     * This operation accepts an object in the JavaScript Object Notation format (JSON) and returns a value for the specified key.
     *
     * @param jsonInput The string representation of a JSON object.
     *                  *      *               Objects in JSON are a collection of name value pairs, separated by a colon and surrounded with curly brackets {}.
     *                  *      *               The name must be a string value, and the value can be a single string or any valid JSON object or array.
     *                  *      *               Examples: {"one":1, "two":2}, {"one":{"a":"a","B":"B"}, "two":"two", "three":[1,2,3.4]}
     * @param jsonPath  The JSON Path qury to run
     *                  Examples: city, location[0].city
     * @return a map containing the output of the operation. Keys present in the map are:
     * * <p/>
     * * <br><br><b>returnResult</b> - This will contain the value for the specified json path.
     * * <br><b>exception</b> - In case of success response, this result is empty. In case of failure response,
     * * this result contains the java stack trace of the runtime exception.
     * * <br><br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     */


    @Action(name = "Get Value",
            outputs = {
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.EXCEPTION),
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)

            })

    public Map<String, String> execute(
            @Param(value = JSON_INPUT, description = JSON_INPUT_DESC, required = true) String jsonInput,
            @Param(value = JSON_PATH, description = JSON_PATH_DESC) String jsonPath) {
        try {
            final JsonNode jsonNode = evaluateJsonPathQueryWithoutDup(jsonInput, jsonPath);
            if (!jsonNode.isNull()) {
                if (jsonNode.toString().startsWith("\"") && jsonNode.toString().endsWith("\"")) {
                    final String result = jsonNode.toString().substring(1, jsonNode.toString().length() - 1);
                    return getSuccessResultsMap(result);
                }
                return getSuccessResultsMap(jsonNode.toString());
            }
            return getSuccessResultsMap(NULL_STRING);
        } catch (Exception exception) {
            return getFailureResultsMap(exception);

        }

    }
}
