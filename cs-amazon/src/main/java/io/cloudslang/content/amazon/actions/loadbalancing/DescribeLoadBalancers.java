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



package io.cloudslang.content.amazon.actions.loadbalancing;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.LoadBalancerInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.LOAD_BALANCING_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.LOAD_BALANCER_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.LoadBalancingQueryApiActions.DESCRIBE_LOAD_BALANCERS;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.LoadBalancerInputs.*;
import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;
import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;

/**
 * Created by TusaM
 * 3/8/2017.
 */
public class DescribeLoadBalancers {
    /**
     * Describes the specified Application Load Balancers or all of your Application Load Balancers.
     * Note: To describe the listeners for a load balancer, use DescribeListeners:
     * http://docs.aws.amazon.com/elasticloadbalancing/latest/APIReference/API_DescribeListeners.html
     * To describe the attributes for a load balancer, use DescribeLoadBalancerAttributes:
     * http://docs.aws.amazon.com/elasticloadbalancing/latest/APIReference/API_DescribeLoadBalancerAttributes.html
     *
     * @param endpoint          Optional - Endpoint to which request will be sent.
     *                          Default: "https://elasticloadbalancing.amazonaws.com"
     * @param identity          ID of the secret access key associated with your Amazon AWS or IAM account.
     *                          Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential        Secret access key associated with your Amazon AWS or IAM account.
     *                          Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost         Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     * @param proxyPort         Optional - proxy server port. You must either specify values for both proxyHost and proxyPort
     *                          inputs or leave them both empty.
     * @param proxyUsername     Optional - proxy server user name.
     * @param proxyPassword     Optional - proxy server password associated with the proxyUsername input value.
     * @param headers           Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                          The header name-value pair will be separated by ":"
     *                          Format: Conforming with HTTP standard for headers (RFC 2616)
     *                          Examples: "Accept:text/plain"
     * @param queryParams       Optional - string containing query parameters that will be appended to the URL.
     *                          The names and the values must not be URL encoded because if they are encoded then a double
     *                          encoded will occur. The separator between name-value pairs is "&" symbol. The query name
     *                          will be separated from query value by "="
     *                          Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     * @param version           Optional - Version of the web service to made the call against it.
     *                          Example: "2015-12-01"
     *                          Default: "2015-12-01"
     * @param delimiter         Optional - Delimiter that will be used.
     *                          Excluded (invalid) chars: ":", "-", "/"
     *                          Default: ","
     * @param arnsString        Optional - String that contains one or more values that represents Amazon Resource Names
     *                          (ARN) of the load balancers separated by the delimiter. You can specify up to 20 load
     *                          balancers in a single call.
     *                          Example: "arn:aws:elasticloadbalancing:us-west-2:123456789012:loadbalancer/app/my-load-balancer/50dc6c495c0c9188,arn:aws:elasticloadbalancing:us-east-1:123456789012:loadbalancer/app/my-load-balancer/50dc6c495c0c9189"
     * @param marker            Optional - marker for the next set of results. This marker is received from a previous call.
     * @param memberNamesString Optional - names of the load balancers to retrieve details for.
     * @param pageSize          Optional - maximum number of results to return with this call.
     *                          Valid range: Minimum value of 1. Maximum value of 400.
     *                          Default: "1"
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Describe Load Balancers",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
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
                                       @Param(value = ARNS_STRING) String arnsString,
                                       @Param(value = MARKER) String marker,
                                       @Param(value = MEMBER_NAMES_STRING) String memberNamesString,
                                       @Param(value = PAGE_SIZE) String pageSize) {
        try {
            version = getDefaultStringInput(version, LOAD_BALANCER_DEFAULT_API_VERSION);

            final CommonInputs commonInputs = new CommonInputs.Builder()
                    .withEndpoint(endpoint, LOAD_BALANCING_API, EMPTY)
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
                    .withAction(DESCRIBE_LOAD_BALANCERS)
                    .withApiService(LOAD_BALANCING_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final LoadBalancerInputs loadBalancerInputs = new LoadBalancerInputs.Builder()
                    .withArnsString(arnsString)
                    .withMarker(marker)
                    .withMemberNamesString(memberNamesString)
                    .withPageSize(pageSize)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, loadBalancerInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}