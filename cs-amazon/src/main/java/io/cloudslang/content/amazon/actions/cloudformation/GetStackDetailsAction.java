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


package io.cloudslang.content.amazon.actions.cloudformation;

import com.amazonaws.services.cloudformation.AmazonCloudFormation;
import com.amazonaws.services.cloudformation.model.DescribeStackResourcesRequest;
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest;
import com.amazonaws.services.cloudformation.model.Stack;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.factory.CloudFormationClientBuilder;
import io.cloudslang.content.amazon.utils.DefaultValues;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CloudFormationInputs.STACK_NAME;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class GetStackDetailsAction {
    /**
     * Get AWS Cloud Formation Stack Details
     *
     * @param identity      Access key associated with your Amazon AWS or IAM account.
     *                      Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param credential    Secret access key ID associated with your Amazon AWS or IAM account.
     * @param proxyHost     Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                      Default: ""
     * @param proxyPort     Optional - proxy server port. You must either specify values for both proxyHost and
     *                      proxyPort inputs or leave them both empty.
     *                      Default: ""
     * @param proxyUsername Optional - proxy server user name.
     *                      Default: ""
     * @param proxyPassword Optional - proxy server password associated with the proxyUsername input value.
     *                      Default: ""
     * @param region        AWS region name
     *                      Example: "eu-central-1"
     * @param stackName     CloudFormation stack name
     *                      Example: "mystack"
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Get AWS Cloud Formation Stack Details",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.STACK_NAME_RESULT),
                    @Output(Outputs.STACK_ID_RESULT),
                    @Output(Outputs.STACK_STATUS_RESULT),
                    @Output(Outputs.STACK_STATUS_RESULT_REASON),
                    @Output(Outputs.STACK_CREATION_TIME_RESULT),
                    @Output(Outputs.STACK_DESCRIPTION_RESULT),
                    @Output(Outputs.STACK_OUTPUTS_RESULT),
                    @Output(Outputs.STACK_RESOURCES_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = IDENTITY, required = true) String identity,
            @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
            @Param(value = REGION, required = true) String region,
            @Param(value = PROXY_HOST) String proxyHost,
            @Param(value = PROXY_PORT) String proxyPort,
            @Param(value = PROXY_USERNAME) String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
            @Param(value = CONNECT_TIMEOUT) String connectTimeoutMs,
            @Param(value = EXECUTION_TIMEOUT) String execTimeoutMs,
            @Param(value = STACK_NAME, required = true) String stackName) {

        proxyPort = defaultIfEmpty(proxyPort, DefaultValues.PROXY_PORT);
        connectTimeoutMs = defaultIfEmpty(connectTimeoutMs, DefaultValues.CONNECT_TIMEOUT);
        execTimeoutMs = defaultIfEmpty(execTimeoutMs, DefaultValues.EXEC_TIMEOUT);

        AmazonCloudFormation stackBuilder = CloudFormationClientBuilder.getCloudFormationClient(identity, credential, proxyHost, proxyPort, proxyUsername, proxyPassword, connectTimeoutMs, execTimeoutMs, region);

        final Map<String, String> results = new HashMap();

        try {
            final DescribeStacksRequest describeStacksRequest = new DescribeStacksRequest();
            describeStacksRequest.withStackName(stackName);

            final DescribeStackResourcesRequest stackResourceRequest = new DescribeStackResourcesRequest();

            for (Stack stack : stackBuilder.describeStacks(describeStacksRequest).getStacks()) {
                results.put(Outputs.RETURN_RESULT, stack.getStackName() + "[" + stack.getStackStatus() + "]" );
                results.put(Outputs.STACK_NAME_RESULT, stack.getStackName());
                results.put(Outputs.STACK_ID_RESULT,stack.getStackId());
                results.put(Outputs.STACK_STATUS_RESULT,stack.getStackStatus());
                results.put(Outputs.STACK_STATUS_RESULT_REASON,stack.getStackStatusReason());
                results.put(Outputs.STACK_CREATION_TIME_RESULT,stack.getCreationTime().toString());
                results.put(Outputs.STACK_DESCRIPTION_RESULT,stack.getDescription());
                results.put(Outputs.STACK_OUTPUTS_RESULT,stack.getOutputs().toString());

                stackResourceRequest.setStackName(stack.getStackName());
                results.put(Outputs.STACK_RESOURCES_RESULT,stackBuilder.describeStackResources(stackResourceRequest).getStackResources().toString());

                results.put(Outputs.RETURN_CODE, Outputs.SUCCESS_RETURN_CODE);
                results.put(Outputs.EXCEPTION, StringUtils.EMPTY);
            }
        } catch (Exception e) {
            results.put(Outputs.RETURN_RESULT, e.getMessage());
            results.put(Outputs.STACK_ID_RESULT, StringUtils.EMPTY);
            results.put(Outputs.STACK_STATUS_RESULT,StringUtils.EMPTY);
            results.put(Outputs.STACK_STATUS_RESULT_REASON,StringUtils.EMPTY);
            results.put(Outputs.STACK_CREATION_TIME_RESULT,StringUtils.EMPTY);
            results.put(Outputs.STACK_DESCRIPTION_RESULT,StringUtils.EMPTY);
            results.put(Outputs.STACK_OUTPUTS_RESULT,StringUtils.EMPTY);
            results.put(Outputs.STACK_RESOURCES_RESULT,StringUtils.EMPTY);
            results.put(Outputs.RETURN_CODE, Outputs.FAILURE_RETURN_CODE);
            results.put(Outputs.EXCEPTION, e.getStackTrace().toString());
        }

        return results;
    }
}