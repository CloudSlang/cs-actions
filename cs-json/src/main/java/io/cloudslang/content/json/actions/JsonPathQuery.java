/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
import io.cloudslang.content.json.services.JsonService;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

import static io.cloudslang.content.constants.OtherValues.NULL_STRING;

/**
 * Created by victor on 06.09.2016.
 */
public class JsonPathQuery {

    /**
     * This operation takes a reference to JSON (in the form of a string) and runs a specified JSON Path query on it.
     * It returns the results as a JSON Object.
     *
     * @param jsonObject The JSON in the form of a string.
     * @param jsonPath   The JSON Path query to run.
     * @return A map which contains the resulted JSON from the given path.
     */
    @Action(name = "JSON Path Query",
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
            @Param(value = Constants.InputNames.JSON_PATH, required = true) String jsonPath) {
        try {
            final JsonNode jsonNode = JsonService.evaluateJsonPathQuery(jsonObject, jsonPath);
            if (!jsonNode.isNull()) {
                return OutputUtilities.getSuccessResultsMap(jsonNode.toString());
            }
            return OutputUtilities.getSuccessResultsMap(NULL_STRING);
        } catch (Exception exception) {
            return OutputUtilities.getFailureResultsMap(exception);
        }
    }
}
