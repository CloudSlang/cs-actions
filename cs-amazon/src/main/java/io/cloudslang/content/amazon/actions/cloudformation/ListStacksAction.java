/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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
import com.amazonaws.services.cloudformation.model.DescribeStacksRequest;
import com.amazonaws.services.cloudformation.model.Stack;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Constants;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.factory.CloudFormationClientBuilder;
import io.cloudslang.content.amazon.utils.DefaultValues;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class ListStacksAction {
    /**
     * List AWS Cloud Formation Stacks
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
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "List AWS Cloud Formation stacks",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
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
            @Param(value = PROXY_PASSWORD) String proxyPassword,
            @Param(value = CONNECT_TIMEOUT) String connectTimeoutMs,
            @Param(value = EXECUTION_TIMEOUT) String execTimeoutMs) {

        proxyPort = defaultIfEmpty(proxyPort, DefaultValues.PROXY_PORT);
        connectTimeoutMs = defaultIfEmpty(connectTimeoutMs, DefaultValues.CONNECT_TIMEOUT);
        execTimeoutMs = defaultIfEmpty(execTimeoutMs, DefaultValues.EXEC_TIMEOUT);

        try {
            AmazonCloudFormation stackBuilder = CloudFormationClientBuilder.getCloudFormationClient(identity, credential, proxyHost, proxyPort, proxyUsername, proxyPassword, connectTimeoutMs, execTimeoutMs, region);

            StringBuilder listOfStacksResult = new StringBuilder(EMPTY);
            // Show all the stacks for this account along with the resources for each stack
            for (Stack stack : stackBuilder.describeStacks(new DescribeStacksRequest()).getStacks()) {
                listOfStacksResult.append(stack.getStackName() + Constants.Miscellaneous.COMMA_DELIMITER);
            }

            if (listOfStacksResult.length() > 0) {
                listOfStacksResult.deleteCharAt(listOfStacksResult.length() - 1); //remove last comma delimiter
            }

            return OutputUtilities.getSuccessResultsMap(listOfStacksResult.toString());
        } catch (Exception e) {
            return OutputUtilities.getFailureResultsMap(e);
        }
    }
}
