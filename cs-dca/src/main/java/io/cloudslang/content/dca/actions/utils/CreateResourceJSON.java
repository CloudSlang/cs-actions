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
package io.cloudslang.content.dca.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.dca.models.DcaResourceModel;
import io.cloudslang.content.dca.utils.Validator;

import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.dca.controllers.CreateResourceJSONController.getDcaBaseResourceModels;
import static io.cloudslang.content.dca.controllers.CreateResourceJSONController.getDcaDeploymentParameterModels;
import static io.cloudslang.content.dca.utils.DefaultValues.COMMA;
import static io.cloudslang.content.dca.utils.Descriptions.Common.*;
import static io.cloudslang.content.dca.utils.Descriptions.CreateResourceJSON.*;
import static io.cloudslang.content.dca.utils.InputNames.*;
import static io.cloudslang.content.dca.utils.Utilities.splitToList;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.join;

public class CreateResourceJSON {

    @Action(name = "Create Resource JSON",
            description = CREATE_RESOURCE_JSON_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_RESPONSE_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ERROR, description = FAILURE_RESPONSE_DESC)
            })
    public Map<String, String> execute(
            @Param(value = TYPE_UUID, required = true, description = TYPE_UUID_DESC) final String typeUuid,
            @Param(value = DEPLOY_SEQUENCE, required = true, description = DEPLOY_SEQ_DESC) final String deploySequence,
            @Param(value = BASE_RESOURCE_UUID_LIST, required = true, description = BR_UUID_DESC) final String baseResourceUuidList,
            @Param(value = BASE_RESOURCE_CI_TYPE_LIST, required = true, description = BR_CITYPE_DESC) final String baseResourceCiTypeList,
            @Param(value = BASE_RESOURCE_TYPE_UUID_LIST, required = true, description = BR_TYPE_UUID_DESC) final String baseResourceTypeUuidList,
            @Param(value = DEPLOYMENT_PARAMETER_NAME_LIST, description = DP_NAME_DESC) final String deploymentParameterNameList,
            @Param(value = DEPLOYMENT_PARAMETER_VALUE_LIST, description = DP_VALUE_DESC) final String deploymentParameterValueList,
            @Param(value = DELIMITER, description = DELIMITER_DESC) final String delimiterInp
    ) {
        final String delimiter = defaultIfEmpty(delimiterInp, COMMA);

        final List<String> baseResourceUUIDs = splitToList(baseResourceUuidList, delimiter);
        final List<String> baseResourceCiTypes = splitToList(baseResourceCiTypeList, delimiter);
        final List<String> baseResourceTypeUUIDs = splitToList(baseResourceTypeUuidList, delimiter);

        final List<String> deploymentParameterNames = splitToList(deploymentParameterNameList, delimiter);
        final List<String> deploymentParameterValues = splitToList(deploymentParameterValueList, delimiter);

        final Validator validator = new Validator();
        validator.validateInt(deploySequence);
        validator.validateSameLength(baseResourceUUIDs, baseResourceCiTypes, baseResourceTypeUUIDs);
        validator.validateSameLength(deploymentParameterNames, deploymentParameterValues);

        if (!validator.getValidationErrorList().isEmpty()) {
            return getFailureResultsMap(join(validator.getValidationErrorList(), lineSeparator()));
        }

        final DcaResourceModel dcaResourceModel = new DcaResourceModel(typeUuid, Integer.valueOf(deploySequence));

        dcaResourceModel.addBaseResources(getDcaBaseResourceModels(baseResourceUUIDs, baseResourceCiTypes,
                baseResourceTypeUUIDs));

        dcaResourceModel.addDeploymentParameters(getDcaDeploymentParameterModels(deploymentParameterNames,
                deploymentParameterValues));

        try {
            return getSuccessResultsMap(dcaResourceModel.toJson());
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }

}
