/*
 * (c) Copyright 2018 Micro Focus
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
package io.cloudslang.content.alibaba.actions.instances;

import com.aliyuncs.IAcsClient;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.alibaba.utils.ClientUtil;
import io.cloudslang.content.alibaba.utils.Validator;
import io.cloudslang.content.alibaba.utils.constants.Outputs;

import java.util.Map;

import static io.cloudslang.content.alibaba.services.InstanceService.deleteInstance;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.ACCESS_KEY_ID_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.ACCESS_KEY_SECRET_ID_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.EXCEPTION_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.INSTANCE_ID_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.PROXY_HOST_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.PROXY_PASSWORD_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.PROXY_PORT_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.PROXY_USERNAME_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.REGION_ID_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.REQUEST_ID_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.RETURN_CODE_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.RETURN_RESULT_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.DeleteInstance.DELETE_INSTANCE_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.DeleteInstance.FAILURE_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.DeleteInstance.SUCCESS_DESC;
import static io.cloudslang.content.alibaba.utils.constants.ExceptionMessages.DELETE_INSTANCE_EXCEPTION;
import static io.cloudslang.content.alibaba.utils.constants.Inputs.ACCESS_KEY_ID;
import static io.cloudslang.content.alibaba.utils.constants.Inputs.ACCESS_KEY_SECRET;
import static io.cloudslang.content.alibaba.utils.constants.Inputs.INSTANCE_ID;
import static io.cloudslang.content.alibaba.utils.constants.Inputs.PROXY_HOST;
import static io.cloudslang.content.alibaba.utils.constants.Inputs.PROXY_PASSWORD;
import static io.cloudslang.content.alibaba.utils.constants.Inputs.PROXY_PORT;
import static io.cloudslang.content.alibaba.utils.constants.Inputs.PROXY_USERNAME;
import static io.cloudslang.content.alibaba.utils.constants.Inputs.REGION_ID;
import static io.cloudslang.content.alibaba.utils.constants.Outputs.REQUEST_ID;
import static io.cloudslang.content.alibaba.utils.constants.SuccessMessages.DELETE_INSTANCE_SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class DeleteInstance {

    @Action(name = "Delete Instance",
            description = DELETE_INSTANCE_DESC,
            outputs = {
                    @Output(value = Outputs.RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = Outputs.RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = Outputs.REQUEST_ID, description = REQUEST_ID_DESC),
                    @Output(value = Outputs.EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            }
    )
    public Map<String, String> execute(@Param(value = ACCESS_KEY_ID, required = true, description = ACCESS_KEY_ID_DESC) final String accessKeyId,
                                       @Param(value = ACCESS_KEY_SECRET, required = true, encrypted = true, description = ACCESS_KEY_SECRET_ID_DESC) final String accessKeySecret,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) final String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) final String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) final String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) final String proxyPassword,
                                       @Param(value = REGION_ID, required = true, description = REGION_ID_DESC) final String regionId,
                                       @Param(value = INSTANCE_ID, required = true, description = INSTANCE_ID_DESC) final String instanceId) {
        //Validate Inputs
        Validator validator = new Validator()
                .validatePort(proxyPort, PROXY_PORT);

        if (validator.hasErrors()) {
            return getFailureResultsMap(validator.getErrors());
        }

        //Get default values and String conversions
        final String proxyHostImp = defaultIfEmpty(proxyHost, EMPTY);
        final String proxyPortImp = defaultIfEmpty(proxyPort, EMPTY);
        final String proxyUsernameImp = defaultIfEmpty(proxyUsername, EMPTY);
        final String proxyPasswordImp = defaultIfEmpty(proxyPassword, EMPTY);

        try {
            final IAcsClient client = ClientUtil.getClient(regionId, accessKeyId, accessKeySecret);
            final String requestId = deleteInstance(proxyHostImp, proxyPortImp, proxyUsernameImp, proxyPasswordImp, instanceId, client);

            final Map<String, String> resultMap = getSuccessResultsMap(DELETE_INSTANCE_SUCCESS);
            resultMap.put(REQUEST_ID, requestId);

            return resultMap;
        } catch (Exception e) {
            return getFailureResultsMap(DELETE_INSTANCE_EXCEPTION, e);
        }
    }
}
