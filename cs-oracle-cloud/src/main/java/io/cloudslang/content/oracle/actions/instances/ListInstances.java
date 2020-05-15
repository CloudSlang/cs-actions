/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.oracle.actions.instances;

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
//import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.oracle.entities.inputs.OCICommonInputs;
import io.cloudslang.content.oracle.entities.inputs.OCIInstanceInputs;
import io.cloudslang.content.utils.StringUtilities;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.oracle.utils.Constants.Common.STATUS_CODE;
import static io.cloudslang.content.oracle.utils.Constants.ListInstancesConstants.LIST_INSTANCES_OPERATION_NAME;
import static io.cloudslang.content.oracle.utils.Descriptions.ListInstances.COMPARTMENT_OCID_DESC;
import static io.cloudslang.content.oracle.utils.Descriptions.ListInstances.INSTANCE_LIST_DESC;
import static io.cloudslang.content.oracle.utils.Descriptions.ListInstances.LIST_INSTANCES_OPERATION_DESC;
import static io.cloudslang.content.oracle.utils.Descriptions.Common.*;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.oracle.utils.Inputs.ListInstancesInputs.COMPARTMENT_OCID;
import static io.cloudslang.content.oracle.utils.Outputs.ListInstancesOutputs.INSTANCE_LIST;
import static io.cloudslang.content.oracle.utils.Inputs.CommonInputs.*;
import static io.cloudslang.content.oracle.services.models.instances.InstanceImpl.listInstances;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

public class ListInstances {

    @Action(name =LIST_INSTANCES_OPERATION_NAME ,
            description = LIST_INSTANCES_OPERATION_DESC,
            outputs = {
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = INSTANCE_LIST, description = INSTANCE_LIST_DESC),
                    @Output(value = STATUS_CODE, description = STATUS_CODE_DESC)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            })
    public Map<String, String> execute(@Param(value = TENANCY_OCID, encrypted = true, required = true, description = TENANCY_OCID_DESC) String tenancyOcid,
                                       @Param(value = USER_OCID, required = true, description = USER_OCID_DESC) String userOcid,
                                       @Param(value = FINGER_PRINT, description = FINGER_PRINT_DESC) String fingerPrint,
                                       @Param(value = PRIVATE_KEY_FILE, description = PRIVATE_KEY_FILE_DESC) String privateKeyFile,
                                       @Param(value = COMPARTMENT_OCID, description = COMPARTMENT_OCID_DESC) String compartmentOcid){

        try {
            final Map<String, String> result = listInstances(OCIInstanceInputs.builder()
                    .compartmentOcid(compartmentOcid)
                    .commonInputs(OCICommonInputs.builder()
                        .tenancyOcId(tenancyOcid)
                        .userOcid(userOcid)
                        .fingerPrint(fingerPrint)
                        .privateKeyFilename(privateKeyFile)
                        .build())
                    .build());
            final String returnMessage = result.get(RETURN_RESULT);

            return result;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
        }
    }


