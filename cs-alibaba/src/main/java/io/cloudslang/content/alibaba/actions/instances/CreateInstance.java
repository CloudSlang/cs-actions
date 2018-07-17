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

package io.cloudslang.content.alibaba.actions.instances;

import io.cloudslang.content.alibaba.entities.inputs.CommonInputs;
import io.cloudslang.content.alibaba.services.CreateInstanceInAlibabaService;
import io.cloudslang.content.alibaba.entities.CreateInstanceInputs;
import java.util.HashMap;
import java.util.Map;


import static io.cloudslang.content.alibaba.entities.constants.Constants.EcsQueryApiActions.CREATE_INSTANCES;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.alibaba.utils.CreateInstanceInputNames.REGIONID;
import static io.cloudslang.content.alibaba.entities.constants.Inputs.CommonInputs.ACCESSKEYID;
import static io.cloudslang.content.alibaba.entities.constants.Inputs.CommonInputs.ACCESSKEYSECRET;
import static io.cloudslang.content.alibaba.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.alibaba.utils.CreateInstanceInputNames.IMAGEID;
import static io.cloudslang.content.alibaba.utils.CreateInstanceInputNames.INSTANCENAME;
import static io.cloudslang.content.alibaba.utils.CreateInstanceInputNames.SECURITYGOUPID;
import static io.cloudslang.content.alibaba.utils.CreateInstanceInputNames.INSTANCETYPE;
import static io.cloudslang.content.alibaba.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.alibaba.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.alibaba.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.alibaba.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.alibaba.utils.CreateInstanceInputNames.INTERNETMAXBANDWIDTHOUT;
import static io.cloudslang.content.alibaba.entities.constants.Inputs.CommonInputs.ECS_API;
import static io.cloudslang.content.alibaba.entities.constants.Constants.AlibabaParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.alibaba.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;


public class CreateInstance {

    /**
     * Launches instance using Image that you have specified.

     *
     * @param endpoint                                  Optional - Endpoint to which request will be sent.
     *                                                  Default: "http://ecs.aliyuncs.com"
     * @param accessKeyId                               ID of the secret access key associated with your Alibaba account.
     *                                                  Example: "LTAIjeVEXAMPLE"
     *
     * @param accessKeySecret                           Secret access key associated with your Alibaba account.
     *                                                  Example: "w5DKxAxPsxRfiCYEXAMPLEKEY"
     * @param proxyHost                                 Optional - proxy server used to connect to Alibaba API. If empty no
     *                                                  proxy will be used.
     *                                                  Default: ""
     * @param proxyPort                                 Optional - proxy server port. You must either specify values for both
     *                                                  proxyHost and proxyPort inputs or leave them both empty.
     *                                                  Default: ""
     * @param proxyUsername                             Optional - proxy server user name.
     *                                                  Default: ""
     * @param proxyPassword                             Optional - proxy server password associated with the proxyUsername
     *                                                  input value.
     * @param imageId                                   ID of an image file. An image is a running environment template for ECS instances.
     *                                                  Example: "ubuntu_16_0402_32_20G_alibase_20180409.vhd"
     * @param instanceType                              Instance type for the new ECS instance.
     *                                                  Example: "ecs.t5-lc1m1.small"
     * @param regionId                                  Region ID of the instance.
     *                                                  Example: "us-west-1"
     * @param version                                   Optional - Version of the web service to made the call against it.
     *                                                  Example: "2014-05-26"
     *                                                  Default: "2014-05-26"
     * @param securityGroupId                           ID of the security group to which an ECS instance belongs. A security
     *                                                  group is a logical group that groups instances in the same region with
     *                                                  the same security requirements and mutual trust.
     * @param internetMaxBandwidthOut                   Maximum outbound bandwidth to the Internet, its unit of measurement is
     *                                                  Mbit/s. Value range:[0, 100]. If this parameter is notspecified, an error
     *                                                  is returned.
     * @Param instanceName                              Name of an ECS instance.
     *
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     *         and the exception if there is one), returnCode of the operation and the ID of the request
     */


    @Action(name = "Create Instance",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR)
            })
    public Map<String, String> execute(@Param(value = ECS_API) String endpoint,
                                       @Param(value = REGIONID, required = true) String regionId,
                                       @Param(value = ACCESSKEYID, required = true, encrypted = true) String accessKeyId,
                                       @Param(value = ACCESSKEYSECRET) String accessKeySecret,
                                       @Param(value = VERSION) String version,
                                       @Param(value = IMAGEID) String imageId,
                                       @Param(value = INSTANCENAME) String instanceName,
                                       @Param(value = SECURITYGOUPID) String securityGroupId,
                                       @Param(value = INSTANCETYPE) String instanceType,
                                       @Param(value = INTERNETMAXBANDWIDTHOUT) String internetMaxBandwidthOut,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword) {


        try {

            final CommonInputs commonInputs = new CommonInputs.Builder().withEndpoint(endpoint, ECS_API, EMPTY)
                    .withAccessKeyId(accessKeyId)
                    .withAccessKeySecret(accessKeySecret)
                    .withAction(CREATE_INSTANCES)
                    .withProxyHost(proxyHost)
                    .withProxyPort(proxyPort)
                    .withVersion(version)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .withProxyUsername(proxyUsername)
                    .withApiService(ECS_API)
                    .withProxyPassword(proxyPassword).build();
            CreateInstanceInputs createInstanceInputs = CreateInstanceInputs.builder()
                    .regionId(regionId)
                    .imageId(imageId)
                    .instanceName(instanceName)
                    .instanceType(instanceType)
                    .internetMaxBandwidthOut(internetMaxBandwidthOut)
                    .securityGroupId(securityGroupId).build();
            Map<String,String> parameters=new HashMap<>();
            parameters.put("RegionId",regionId);
            parameters.put("ImageId",imageId);
            parameters.put("InstanceType",instanceType);
            parameters.put("InternetMaxBandwidthOut",internetMaxBandwidthOut);
            parameters.put("InstanceName",instanceName);
            parameters.put("SecurityGroupId",securityGroupId);

            return new CreateInstanceInAlibabaService().createInstance(parameters,commonInputs,createInstanceInputs);
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }

    }
}