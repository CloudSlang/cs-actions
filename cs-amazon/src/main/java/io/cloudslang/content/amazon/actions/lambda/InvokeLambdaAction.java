/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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
/*
 * Copyright 2019-2024 Open Text
 * This program and the accompanying materials
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
 


package io.cloudslang.content.amazon.actions.lambda;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import io.cloudslang.content.annotations.Action;
import io.cloudslang.content.annotations.Output;
import io.cloudslang.content.annotations.Param;
import io.cloudslang.content.annotations.Response;
import io.cloudslang.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.utils.AmazonWebServiceClientUtil;
import io.cloudslang.content.amazon.utils.DefaultValues;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.*;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class InvokeLambdaAction {
    /**
     * Invokes an AWS Lambda Function in sync mode using AWS Java SDK
     *
     * @param identity          Access key associated with your Amazon AWS or IAM account.
     *                          Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param credential        Secret access key ID associated with your Amazon AWS or IAM account.
     * @param proxyHost         Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                          Default: ""
     * @param proxyPort         Optional - proxy server port. You must either specify values for both proxyHost and
     *                          proxyPort inputs or leave them both empty.
     *                          Default: ""
     * @param proxyUsername     Optional - proxy server user name.
     *                          Default: ""
     * @param proxyPassword     Optional - proxy server password associated with the proxyUsername input value.
     *                          Default: ""
     * @param function          Lambda function name to call
     *                          Example: "helloWord"
     * @param qualifier         Optional - Lambda function version or alias
     *                          Example: ":1"
     *                          Default: "$LATEST"
     * @param payload           Optional - Lambda function payload in JSON format
     *                          Example: "{"key1":"value1", "key1":"value2"}"
     *                          Default: null
     * @return                  A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     *                          operation, or failure message and the exception if there is one
     */
    @Action(name = "Invoke AWS Lambda Function",
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
            @Param(value = IDENTITY,   required = true)  String identity,
            @Param(value = CREDENTIAL, required = true, encrypted = true)  String credential,
            @Param(value = REGION,     required = true)  String region,
            @Param(value = PROXY_HOST)                   String proxyHost,
            @Param(value = PROXY_PORT)                   String proxyPort,
            @Param(value = PROXY_USERNAME)               String proxyUsername,
            @Param(value = PROXY_PASSWORD)               String proxyPassword,
            @Param(value = FUNCTION_NAME, required = true) String function,
            @Param(value = FUNCTION_QUALIFIER)           String qualifier,
            @Param(value = FUNCTION_PAYLOAD)             String payload,
            @Param(value = CONNECT_TIMEOUT)              String connectTimeoutMs,
            @Param(value = EXECUTION_TIMEOUT)            String execTimeoutMs) {

        proxyPort = defaultIfEmpty(proxyPort, DefaultValues.PROXY_PORT);
        connectTimeoutMs = defaultIfEmpty(connectTimeoutMs, DefaultValues.CONNECT_TIMEOUT);
        execTimeoutMs = defaultIfBlank(execTimeoutMs, DefaultValues.EXEC_TIMEOUT);
        qualifier = defaultIfBlank(qualifier, DefaultValues.DEFAULT_FUNCTION_QUALIFIER);

        try {
            try (LambdaClient client = LambdaClient.builder()
                    .region(Region.of(region))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(identity, credential)))
                    .httpClientBuilder(AmazonWebServiceClientUtil.getHttpClientBuilder(
                            proxyHost, proxyPort, proxyUsername, proxyPassword, connectTimeoutMs))
                    .overrideConfiguration(AmazonWebServiceClientUtil.getClientOverrideConfiguration(execTimeoutMs))
                    .build()) {
                InvokeRequest.Builder request = InvokeRequest.builder().functionName(function).qualifier(qualifier);
                if (payload != null) {
                    request.payload(SdkBytes.fromUtf8String(payload));
                }
                return OutputUtilities.getSuccessResultsMap(client.invoke(request.build()).payload().asUtf8String());
            }
        } catch (Exception e) {
            return OutputUtilities.getFailureResultsMap(e);
        }
    }
}
