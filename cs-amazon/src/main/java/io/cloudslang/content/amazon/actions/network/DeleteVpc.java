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



package io.cloudslang.content.amazon.actions.network;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.OutputNames;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.NETWORK_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.DELETE_VPC;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VPC_ID;
import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

/**
 * Created by TusaM
 * 3/14/2017.
 */
public class DeleteVpc {
    /**
     * Deletes the specified VPC.
     * Note: You must detach or delete all gateways and resources that are associated with the VPC before you can delete
     * it. For example, you must terminate all instances running in the VPC, delete all security groups associated with
     * the VPC (except the default one), delete all route tables associated with the VPC (except the default one), and
     * so on.
     *
     * @param endpoint      Optional - Endpoint to which request will be sent.
     *                      Default: "https://ec2.amazonaws.com"
     * @param identity      ID of the secret access key associated with your Amazon AWS or IAM account.
     *                      Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential    Secret access key associated with your Amazon AWS or IAM account.
     *                      Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost     Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     * @param proxyPort     Optional - proxy server port. You must either specify values for both proxyHost and proxyPort
     *                      inputs or leave them both empty.
     * @param proxyUsername Optional - proxy server user name.
     * @param proxyPassword Optional - proxy server password associated with the proxyUsername input value.
     * @param headers       Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                      The header name-value pair will be separated by ":".
     *                      Format: Conforming with HTTP standard for headers (RFC 2616)
     *                      Examples: "Accept:text/plain"
     * @param queryParams   Optional - string containing query parameters that will be appended to the URL.
     *                      The names and the values must not be URL encoded because if they are encoded then a double
     *                      encoded will occur. The separator between name-value pairs is "&" symbol. The query name will
     *                      be separated from query value by "=".
     *                      Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     * @param version       Optional - Version of the web service to made the call against it.
     *                      Example: "2016-11-15"
     *                      Default: "2016-11-15"
     * @param vpcId         ID of the VPC to be deleted.
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Delete Vpc",
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = ENDPOINT) String endpoint,
                                       @Param(value = IDENTITY, required = true) String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = VERSION) String version,
                                       @Param(value = VPC_ID) String vpcId) {
        try {
            version = getDefaultStringInput(version, NETWORK_DEFAULT_API_VERSION);

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
                    .withAction(DELETE_VPC)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder().withVpcId(vpcId).build();

            return new QueryApiExecutor().execute(commonInputs, customInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}