/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.amazon.actions.eks;

import com.amazonaws.services.eks.AmazonEKS;
import com.amazonaws.services.eks.model.CreateClusterResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.validators.Validator;
import io.cloudslang.content.amazon.factory.EKSClientBuilder;
import io.cloudslang.content.amazon.services.AmazonEKSService;
import io.cloudslang.content.constants.DefaultValues;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Descriptions.Common.*;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.DBInstanceCommon.ACCESS_KEY_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.DBInstanceCommon.ACCESS_KEY_ID_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.DescribeEKSClusterAction.DESCRIBE_EKS_CLUSTER_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.EKSClusterCommon.EKS_CLUSTER_NAME_DESC;
import static io.cloudslang.content.amazon.entities.constants.Descriptions.ProvisionProductAction.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CreateEKSClusterInputs.VERSION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CreateEKSClusterInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EKSCommonInputs.EKS_CLUSTER_NAME;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class CreateEKSClusterAction {
    @Action(name = CREATE_EKS_CLUSTER, description = DESCRIBE_EKS_CLUSTER_DESC,
            outputs = {
                    @Output(value = Outputs.RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = Outputs.RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = Outputs.EXCEPTION, description = EXCEPTION_DESC)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = SUCCESS_DESC),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, description = FAILURE_DESC)
            }
    )
    public Map<String, String> execute(@Param(value = ACCESS_KEY_ID, required = true, description = ACCESS_KEY_ID_DESC) final String accessKeyID,
                                       @Param(value = ACCESS_KEY, required = true, encrypted = true, description = ACCESS_KEY_DESC) final String accessKey,
                                       @Param(value = REGION, required = true, description = REGION_DESC) String region,
                                       @Param(value = EKS_CLUSTER_NAME, required = true, description = EKS_CLUSTER_NAME_DESC) String eksClusterName,
                                       @Param(value = ROLE_ARN, required = true, description = EKS_CLUSTER_NAME_DESC) String roleArn,
                                       @Param(value = SUBNET_IDS, required = true, description = EKS_CLUSTER_NAME_DESC) String subnetIds,
                                       @Param(value = SECURITY_GROUP_IDS, required = true, description = EKS_CLUSTER_NAME_DESC) String securityGroupIds,
                                       @Param(value = ENDPOINT_PUBLIC_ACCESS, description = EKS_CLUSTER_NAME_DESC) String endpointPublicAccess,
                                       @Param(value = ENDPOINT_PRIVATE_ACCESS, description = EKS_CLUSTER_NAME_DESC) String endpointPrivateAccess,
                                       @Param(value = VERSION, description = EKS_CLUSTER_NAME_DESC) String version,
                                       @Param(value = SERVICE_IPV4_CIDR, description = EKS_CLUSTER_NAME_DESC) String serviceIpv4Cidr,
                                       @Param(value = TAG_KEY, description = EKS_CLUSTER_NAME_DESC) String tagKeys,
                                       @Param(value = TAG_VALUES, description = EKS_CLUSTER_NAME_DESC) String tagValues,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) final String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) final String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) final String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) final String proxyPassword,
                                       @Param(value = CONNECT_TIMEOUT, description = CONNECT_TIMEOUT_DESC) String connectTimeout,
                                       @Param(value = EXECUTION_TIMEOUT, description = EXECUTION_TIMEOUT_DESC) String execTimeout,
                                       @Param(value = ASYNC, description = ASYNC_DESC) String async
    ) {
        final String proxyPortVal = defaultIfEmpty(proxyPort, DefaultValues.PROXY_PORT);
        final String connectTimeoutVal = defaultIfEmpty(connectTimeout, DefaultValues.CONNECT_TIMEOUT);
        final String execTimeoutVal = defaultIfEmpty(execTimeout, DefaultValues.EXEC_TIMEOUT);
        final String asyncVal = defaultIfEmpty(async, DefaultValues.ASYNC);

        Validator validator = new Validator()
                .validatePort(proxyPortVal, PROXY_PORT)
                .validateInt(connectTimeoutVal, CONNECT_TIMEOUT)
                .validateInt(execTimeoutVal, EXECUTION_TIMEOUT)
                .validateBoolean(asyncVal, ASYNC);

        if (validator.hasErrors()) {
            return getFailureResultsMap(validator.getErrors());
        }

        final Integer proxyPortImp = Integer.valueOf(proxyPortVal);
        final Integer connectTimeoutImp = Integer.valueOf(connectTimeoutVal);
        final Integer execTimeoutImp = Integer.valueOf(execTimeoutVal);
        final boolean asyncImp = Boolean.parseBoolean(asyncVal);

        Map<String, String> tags = new HashMap<>();

        if (isNotEmpty(tagKeys) && isNotEmpty(tagValues)) {
            List<String> keyList = Arrays.asList(tagKeys.split(","));
            List<String> valueList = Arrays.asList(tagValues.split(","));
            for (int i = 0; i < keyList.size(); i++) {
                tags.put(keyList.get(i), valueList.get(i));
            }
        }

        try {
            final AmazonEKS amazonEKS = EKSClientBuilder.getEKSClientBuilder(accessKeyID, accessKey,
                    proxyHost, proxyPortImp, proxyUsername, proxyPassword, connectTimeoutImp, execTimeoutImp, region, asyncImp);

            final CreateClusterResult result = AmazonEKSService.createEKSCluster(
                    eksClusterName, roleArn, Arrays.asList(subnetIds.split(",")), Arrays.asList(securityGroupIds.split(","))
                    , Boolean.parseBoolean(endpointPublicAccess), Boolean.parseBoolean(endpointPrivateAccess), version, serviceIpv4Cidr,
                    tags, amazonEKS);

            ObjectMapper objectMapper = new ObjectMapper();
            return getSuccessResultsMap(objectMapper.writeValueAsString(result.getCluster()));

        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
