/*
 * (c) Copyright 2019 Micro Focus, L.P.
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

package io.cloudslang.content.hashicorp.terraform.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.hashicorp.terraform.entities.CreateRunInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.hashicorp.terraform.services.CreateRunImpl.createRunClient;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Common.AUTH_TOKEN_DESC;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.ListOAuthClient.*;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getOperationResults;
import static io.cloudslang.content.hashicorp.terraform.utils.Outputs.AuthorizationOutputs.AUTH_TOKEN;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.DELIMITER;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.STATUS_CODE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.ListOAuthClientConstants.OAUTH_TOKEN_LIST_JSON_PATH;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.*;

public class CreateRun {
    @Action(name = "Create Run",
            outputs = {
                    @Output(value = OutputNames.RETURN_RESULT, description = RETURN_RESULT_DESC),
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = AUTH_TOKEN, required = true, description = AUTH_TOKEN_DESC) String authToken,
                                       @Param(value = "workspaceId") String workspaceId,
                                       @Param(value = "runMessage") String runMessage,
                                       @Param(value = "isDestroy") String isDestroy,
                                       @Param( value = "body") String body) {
        workspaceId = defaultIfEmpty(workspaceId, EMPTY);
        runMessage = defaultIfEmpty(runMessage, EMPTY);
        isDestroy = defaultIfEmpty(isDestroy, EMPTY);
        try {
            final Map<String, String> result = createRunClient(CreateRunInputs.builder()
                    .body(body)
                    .commonInputs(TerraformCommonInputs.builder()
                            .authToken(authToken)
                            .build())
                    .build());
            final String returnMessage = result.get(RETURN_RESULT);
            System.out.println("RETURN RESULT" + returnMessage);

            final Map<String, String> results = getOperationResults(result, returnMessage, returnMessage, returnMessage);
            final int statusCode = Integer.parseInt(result.get(STATUS_CODE));

            if (statusCode >= 200 && statusCode < 300) {
                final List<String> outhTokenIdList = JsonPath.read(returnMessage, OAUTH_TOKEN_LIST_JSON_PATH);
                System.out.println("TokenID" + outhTokenIdList);
                if (!outhTokenIdList.isEmpty()) {
                    final String mouthTokenIdListAsString = join(outhTokenIdList.toArray(), DELIMITER);
                    results.put("oauthTokenId", mouthTokenIdListAsString);
                } else {
                    results.put("oauthTokenId", EMPTY);
                }
            }

            return results;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
}
