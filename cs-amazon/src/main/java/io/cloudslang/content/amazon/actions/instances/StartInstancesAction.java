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
 



package io.cloudslang.content.amazon.actions.instances;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;

import java.util.Map;

import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.INSTANCES_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.START_INSTANCES;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.INSTANCE_IDS_STRING;

/**
 * Created by persdana
 * 5/25/2015.
 */
public class StartInstancesAction {
    /**
     * Starts an Amazon EBS-backed AMI that you've previously stopped.
     * Note: Instances that use Amazon EBS volumes as their root devices can be quickly stopped and started. When an
     * instance is stopped, the compute resources are released and you are not billed for hourly instance usage.
     * However, your root partition Amazon EBS volume remains, continues to persist your data, and you are charged
     * for Amazon EBS volume usage. You can restart your instance at any time. Each time you transition an instance
     * from stopped to started, Amazon EC2 charges a full instance hour, even if transitions happen multiple times
     * within a single hour.
     * Before stopping an instance, make sure it is in a state from which it can be restarted. Stopping an instance
     * does not preserve data stored in RAM. Performing this operation on an instance that uses an instance store
     * as its root device returns an error.
     * For more information, see Stopping Instances in the Amazon Elastic Compute Cloud User Guide.
     *
     * @param endpoint          Optional - Endpoint to which request will be sent.
     *                          Default: "https://ec2.amazonaws.com"
     * @param identity          ID of the secret access key associated with your Amazon AWS or IAM account.
     *                          Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential        Secret access key associated with your Amazon AWS or IAM account.
     *                          Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost         Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                          Default: ""
     * @param proxyPort         Optional - proxy server port. You must either specify values for both proxyHost and
     *                          proxyPort inputs or leave them both empty.
     *                          Default: ""
     * @param proxyUsername     Optional - proxy server user name.
     *                          Default: ""
     * @param proxyPassword     Optional - proxy server password associated with the proxyUsername input value.
     *                          Default: ""
     * @param headers           Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                          The header name-value pair will be separated by ":".
     *                          Format: Conforming with HTTP standard for headers (RFC 2616)
     *                          Examples: "Accept:text/plain"
     *                          Default: ""
     * @param queryParams       Optional - string containing query parameters that will be appended to the URL. The names
     *                          and the values must not be URL encoded because if they are encoded then a double encoded
     *                          will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                          separated from query value by "=".
     *                          Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                          Default: ""
     * @param version           Optional - Version of the web service to made the call against it.
     *                          Example: "2016-11-15"
     *                          Default: "2016-11-15"
     * @param delimiter         Optional - delimiter that will be used.
     *                          Default: ","
     * @param instanceIdsString String that contains one or more values that represents instance IDs.
     *                          Example: "i-12345678,i-abcdef12,i-12ab34cd"
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     *         and the exception if there is one), returnCode of the operation and the ID of the request.
     */
    @Action(name = "Start Instances",
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
    public Map<String, String> execute(@Param(value = ENDPOINT) String endpoint,
                                       @Param(value = IDENTITY, required = true) String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = VERSION) String version,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = INSTANCE_IDS_STRING, required = true) String instanceIdsString) {
        try {
            version = getDefaultStringInput(version, INSTANCES_DEFAULT_API_VERSION);

            final CommonInputs commonInputs = new CommonInputs.Builder()
                    .withEndpoint(endpoint, EC2_API, EMPTY)
                    .withIdentity(identity)
                    .withCredential(credential)
                    .withProxyHost(proxyHost)
                    .withProxyPort(proxyPort)
                    .withProxyUsername(proxyUsername)
                    .withProxyPassword(proxyPassword)
                    .withHeaders(headers)
                    .withQueryParams(queryParams)
                    .withVersion(version)
                    .withDelimiter(delimiter)
                    .withAction(START_INSTANCES)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final InstanceInputs instanceInputs = new InstanceInputs.Builder().withInstanceIdsString(instanceIdsString).build();

            return new QueryApiExecutor().execute(commonInputs, instanceInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}
