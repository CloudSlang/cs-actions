package io.cloudslang.content.amazon.actions.loadbalancing;

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
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.LOAD_BALANCING_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.LoadBalancingQueryApiActions.CREATE_LOAD_BALANCER;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
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
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ZONE_NAMES_STRING;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public class CreateLoadBalancer {
    /**
     * Creates a Classic Load Balancer.
     * You can add listeners, security groups, subnets, and tags when you create your load balancer, or you can add them
     * later using CreateLoadBalancerListeners, ApplySecurityGroupsToLoadBalancer, AttachLoadBalancerToSubnets, and AddTags.
     * To describe your current load balancers, see DescribeLoadBalancers. When you are finished with a load balancer, you
     * can delete it using DeleteLoadBalancer.
     * You can create up to 20 load balancers per region per account. You can request an increase for the number of load
     * balancers for your account. For more information, see Limits for Your Classic Load Balancer in the Classic Load
     * Balancer Guide.
     *
     * @param endpoint        Optional - Endpoint to which request will be sent.
     *                        Default: "https://ec2.amazonaws.com"
     * @param identity        ID of the secret access key associated with your Amazon AWS or IAM account.
     *                        Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential      Secret access key associated with your Amazon AWS or IAM account.
     *                        Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost       Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     * @param proxyPort       Optional - proxy server port. You must either specify values for both <proxyHost> and
     *                        <proxyPort> inputs or leave them both empty.
     * @param proxyUsername   Optional - proxy server user name.
     *                        Default: ""
     * @param proxyPassword   Optional - proxy server password associated with the <proxyUsername> input value.
     * @param headers         Optional - string containing the headers to use for the request separated by new line
     *                        (CRLF). The header name-value pair will be separated by ":"
     *                        Format: Conforming with HTTP standard for headers (RFC 2616)
     *                        Examples: "Accept:text/plain"
     *                        Default: ""
     * @param queryParams     Optional - string containing query parameters that will be appended to the URL. The names
     *                        and the values must not be URL encoded because if they are encoded then a double encoded
     *                        will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                        separated from query value by "="
     *                        Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                        Default: ""
     * @param version         Optional - Version of the web service to made the call against it.
     *                        Example: "2012-06-01"
     *                        Default: "2012-06-01"
     * @param zoneNamesString Optional - String that contains names of one or more Availability Zones.
     *                        Example: "us-east-1a,us-east-1d"
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Create Load Balancer",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
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
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = VERSION) String version,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = ZONE_NAMES_STRING) String zoneNamesString) {
        try {
            version = InputsUtil.getDefaultStringInput(version, "2012-06-01");
            CommonInputs commonInputs = new CommonInputs.Builder()
                    .withEndpoint(endpoint)
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

            CustomInputs customInputs = new CustomInputs.Builder()
                    .withAvailabilityZonesString(zoneNamesString)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}