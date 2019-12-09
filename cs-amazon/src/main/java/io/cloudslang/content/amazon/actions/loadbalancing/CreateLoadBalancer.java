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
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.IamInputs;
import io.cloudslang.content.amazon.entities.inputs.LoadBalancerInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;

import java.util.Map;

import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.LOAD_BALANCING_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.LOAD_BALANCER_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.LoadBalancingQueryApiActions.CREATE_LOAD_BALANCER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KEY_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VALUE_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.SECURITY_GROUP_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.SUBNET_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.LoadBalancerInputs.LOAD_BALANCER_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.LoadBalancerInputs.SCHEME;
import static io.cloudslang.content.amazon.entities.constants.Outputs.SUCCESS;
import static io.cloudslang.content.amazon.entities.constants.Outputs.SUCCESS_RETURN_CODE;
import static io.cloudslang.content.amazon.entities.constants.Outputs.FAILURE;
import static io.cloudslang.content.amazon.entities.constants.Outputs.FAILURE_RETURN_CODE;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public class CreateLoadBalancer {
    /**
     * Creates an Application Load Balancer.
     * To create listeners for your load balancer, use CreateListener. You can add security groups, subnets, and tags when
     * you create your load balancer, or you can add them later using SetSecurityGroups, SetSubnets, and AddTags. To describe
     * your current load balancers, see DescribeLoadBalancers. When you are finished with a load balancer, you can delete
     * it using DeleteLoadBalancer. You can create up to 20 load balancers per region per account. You can request an
     * increase for the number of load balancers for your account. For more information, see Limits for Your Application
     * Load Balancer in the Application Load Balancers Guide.
     * For more information, see http://docs.aws.amazon.com/elasticloadbalancing/latest/application/application-load-balancers.html
     *
     * @param endpoint               Optional - Endpoint to which request will be sent.
     *                               Default: "https://elasticloadbalancing.amazonaws.com"
     * @param identity               ID of the secret access key associated with your Amazon AWS or IAM account.
     *                               Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential             Secret access key associated with your Amazon AWS or IAM account.
     *                               Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost              Optional - proxy server used to connect to Amazon API. If empty no proxy will
     *                               be used.
     * @param proxyPort              Optional - proxy server port. You must either specify values for both proxyHost
     *                               and proxyPort inputs or leave them both empty.
     * @param proxyUsername          Optional - proxy server user name.
     *                               Default: ""
     * @param proxyPassword          Optional - proxy server password associated with the proxyUsername input value.
     * @param headers                Optional - string containing the headers to use for the request separated by new
     *                               line (CRLF). The header name-value pair will be separated by ":"
     *                               Format: Conforming with HTTP standard for headers (RFC 2616)
     *                               Examples: "Accept:text/plain"
     *                               Default: ""
     * @param queryParams            Optional - string containing query parameters that will be appended to the URL.
     *                               The names and the values must not be URL encoded because if they are encoded then
     *                               a double encoded will occur. The separator between name-value pairs is "&" symbol.
     *                               The query name will be separated from query value by "="
     *                               Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                               Default: ""
     * @param version                Optional - Version of the web service to made the call against it.
     *                               Example: "2015-12-01"
     *                               Default: "2015-12-01"
     * @param loadBalancerName       Name of the load balancer. This name must be unique within your AWS account, can have
     *                               a maximum of 32 characters, must contain only alphanumeric characters or hyphens, and
     *                               must not begin or end with a hyphen.
     * @param subnetIdsString        String that contains one or more IDs of the subnets to attach to the load balancer.
     *                               You can specify only one subnet per Availability Zone. You must specify subnets from
     *                               at least two Availability Zones.
     * @param scheme                 Optional - The nodes of an Internet-facing load balancer have public IP addresses.
     *                               The DNS name of an Internet-facing load balancer is publicly resolvable to the public
     *                               IP addresses of the nodes. Therefore, Internet-facing load balancers can route requests
     *                               from clients over the Internet. The nodes of an internal load balancer have only private
     *                               IP addresses. The DNS name of an internal load balancer is publicly resolvable to the
     *                               private IP addresses of the nodes. Therefore, internal load balancers can only route
     *                               requests from clients with access to the VPC for the load balancer. The default is
     *                               an Internet-facing load balancer.
     *                               Valid values: "internet-facing", "internal"
     *                               Default: "internet-facing"
     * @param securityGroupIdsString Optional - String that contains one or more IDs of the security groups to assign to
     *                               the load balancer.
     *                               Default: ""
     * @param keyTagsString          String that contains one or more key tags separated by delimiter.
     *                               Length constraint tag key: minimum length of 1, maximum length of 128.
     *                               Pattern: ^([\p{L}\p{Z}\p{N}_.:/=+\-@]*)$
     *                               Default: ""
     * @param valueTagsString        String that contains one or more tag values separated by delimiter. The value parameter
     *                               Length constraint tag value: minimum length of 0, maximum length of 256.
     *                               Pattern: ^([\p{L}\p{Z}\p{N}_.:/=+\-@]*)$
     *                               Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     *         and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Create Load Balancer",
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = OutputNames.RETURN_CODE, value = SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = OutputNames.RETURN_CODE, value = FAILURE_RETURN_CODE,
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
                                       @Param(value = LOAD_BALANCER_NAME, required = true) String loadBalancerName,
                                       @Param(value = SUBNET_IDS_STRING, required = true) String subnetIdsString,
                                       @Param(value = SCHEME) String scheme,
                                       @Param(value = SECURITY_GROUP_IDS_STRING) String securityGroupIdsString,
                                       @Param(value = KEY_TAGS_STRING) String keyTagsString,
                                       @Param(value = VALUE_TAGS_STRING) String valueTagsString) {
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
                    .withAction(CREATE_LOAD_BALANCER)
                    .withApiService(LOAD_BALANCING_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder()
                    .withKeyTagsString(keyTagsString)
                    .withValueTagsString(valueTagsString)
                    .build();

            final IamInputs iamInputs = new IamInputs.Builder().withSecurityGroupIdsString(securityGroupIdsString).build();

            final LoadBalancerInputs loadBalancerInputs = new LoadBalancerInputs.Builder()
                    .withLoadBalancerName(loadBalancerName)
                    .withScheme(scheme)
                    .build();

            final NetworkInputs networkInputs = new NetworkInputs.Builder().withSubnetIdsString(subnetIdsString).build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, iamInputs, loadBalancerInputs, networkInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}
