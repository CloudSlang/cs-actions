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
import io.cloudslang.content.json.exceptions.JsonSchemaValidationException;
import io.cloudslang.content.json.services.JsonService;

import java.util.Map;

import static io.cloudslang.content.json.services.JsonService.*;
import static io.cloudslang.content.json.utils.Constants.Description.*;
import static io.cloudslang.content.json.utils.Constants.InputNames.*;
import static io.cloudslang.content.json.utils.Constants.ValidationMessages.EMPTY_JSON_PROVIDED;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

/**
 * Created by Daniel Manciu
 * Date 20/07/2018.
 */

public class ValidateJson {

    /**
     * Checks if a string is a valid JSON object and, optionally, if it validates against a JSON schema.
     */

    @Action(name = "Validate JSON", description = VALIDATE_JSON_DESC
            , outputs = {
            @Output(OutputNames.RETURN_RESULT),
            @Output(OutputNames.RETURN_CODE),
            @Output(OutputNames.EXCEPTION)
    },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = VALIDATE_JSON_SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true, description = VALIDATE_JSON_FAILURE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = JSON_OBJECT, required = true, description = JSON_OBJECT_DESC) String jsonObject,
            @Param(value = JSON_SCHEMA, description = JSON_SCHEMA_DESC) String jsonSchema,
            @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
            @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
            @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword
    ) {
        if (jsonObject == null || jsonObject.trim().equals(OtherValues.EMPTY_STRING)) {
            return getFailureResultsMap(new Exception(EMPTY_JSON_PROVIDED));
        }

        jsonObject = JsonService.replaceSingleQuotesWithDoubleQuotes(jsonObject);

        if (jsonSchema == null || jsonSchema.trim().equals(OtherValues.EMPTY_STRING)) {
            return validateJsonResultMap(jsonObject);
        }

        String jsonSchemaObject;

        try {
            jsonSchemaObject = getJsonSchemaFromResource(jsonSchema, proxyHost, proxyPort, proxyUsername, proxyPassword);
        } catch (JsonSchemaValidationException exception) {
            return getFailureResultsMap(exception.getMessage());
        }

        return validateJsonAgainstSchemaResultMap(jsonObject, jsonSchemaObject);
    }

}
