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

import static io.cloudslang.content.alibaba.services.InstanceService.startInstance;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.Common.*;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.StartInstance.INIT_LOCAL_DISK_DESC;
import static io.cloudslang.content.alibaba.utils.constants.Descriptions.StartInstance.START_INSTANCE_DESC;
import static io.cloudslang.content.alibaba.utils.constants.ExceptionMessages.START_INSTANCE_EXCEPTION;
import static io.cloudslang.content.alibaba.utils.constants.Inputs.*;
import static io.cloudslang.content.alibaba.utils.constants.Outputs.REQUEST_ID;
import static io.cloudslang.content.alibaba.utils.constants.SuccessMessages.START_INSTANCE_SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.Boolean.valueOf;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class StartInstance {
    @Action(name = "Start Instance",
            description = START_INSTANCE_DESC,
            outputs = {
                    @Output(value = Outputs.RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = Outputs.RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = Outputs.REQUEST_ID, description = REQUEST_ID_DESC),
                    @Output(value = Outputs.EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(@Param(value = ACCESS_KEY_ID, required = true, description = ACCESS_KEY_ID_DESC) final String accessKeyId,
                                       @Param(value = ACCESS_KEY_SECRET, required = true, encrypted = true, description = ACCESS_KEY_SECRET_ID_DESC) final String accessKeySecret,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) final String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) final String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) final String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) final String proxyPassword,
                                       @Param(value = REGION_ID, required = true, description = REGION_ID_DESC) final String regionId,
                                       @Param(value = INSTANCE_ID, required = true, description = INSTANCE_ID_DESC) final String instanceId,
                                       @Param(value = INIT_LOCAL_DISK, description = INIT_LOCAL_DISK_DESC) final String initLocalDisk) {
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
        final Boolean initLocalDiskImp = valueOf(initLocalDisk);

        try {
            final IAcsClient client = ClientUtil.getClient(regionId, accessKeyId, accessKeySecret);
            final String requestId = startInstance(proxyHostImp, proxyPortImp, proxyUsernameImp, proxyPasswordImp, instanceId, initLocalDiskImp, client);

            final Map<String, String> resultMap = getSuccessResultsMap(START_INSTANCE_SUCCESS);
            resultMap.put(REQUEST_ID, requestId);

            return resultMap;
        } catch (Exception e) {
            return getFailureResultsMap(START_INSTANCE_EXCEPTION, e);
        }
    }
}
