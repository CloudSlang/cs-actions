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
import io.cloudslang.content.amazon.utils.InputsUtil;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.LOAD_BALANCING_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.LoadBalancingQueryApiActions.DELETE_LOAD_BALANCER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;

import static io.cloudslang.content.amazon.entities.constants.Inputs.LoadBalancerInputs.LOAD_BALANCER_ARN;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;

import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;

/**
 * Created by TusaM
 * 12/5/2016.
 */
public class DeleteLoadBalancer {
    private static final String LATEST_DELETE_LOAD_BALANCER_API_VERSION = "2015-12-01";

    /**
     * Deletes the specified Application Load Balancer and its attached listeners.
     * Note: You can't delete a load balancer if deletion protection is enabled. If the load balancer does not exist or
     * has already been deleted, the call succeeds. Deleting a load balancer does not affect its registered targets.
     * For example, your EC2 instances continue to run and are still registered to their target groups. If you no longer
     * need these EC2 instances, you can stop or terminate them.
     *
     * @param endpoint        Optional - Endpoint to which request will be sent.
     *                        Example: "https://elasticloadbalancing.amazonaws.com"
     *                        Default: "https://ec2.amazonaws.com"
     * @param identity        ID of the secret access key associated with your Amazon AWS or IAM account.
     *                        Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential      Secret access key associated with your Amazon AWS or IAM account.
     *                        Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost       Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                        Default: ""
     * @param proxyPort       Optional - proxy server port. You must either specify values for both <proxyHost> and
     *                        <proxyPort> inputs or leave them both empty.
     *                        Default: ""
     * @param proxyUsername   Optional - proxy server user name.
     *                        Default: ""
     * @param proxyPassword   Optional - proxy server password associated with the <proxyUsername> input value.
     * @param headers         Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                        The header name-value pair will be separated by ":"
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
     *                        Example: "2015-12-01"
     *                        Default: "2015-12-01"
     * @param loadBalancerArn Amazon Resource Name (ARN) of the load balancer.
     *                        Example: "arn:aws:elasticloadbalancing:us-west-2:123456789012:loadbalancer/app/my-load-balancer/50dc6c495c0c9188"
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Delete Load Balancer",
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
                                       @Param(value = LOAD_BALANCER_ARN, required = true) String loadBalancerArn) {
        try {
            version = InputsUtil.getDefaultStringInput(version, LATEST_DELETE_LOAD_BALANCER_API_VERSION);

            final CommonInputs commonInputs = new CommonInputs.Builder()
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
                    .withAction(DELETE_LOAD_BALANCER)
                    .withApiService(LOAD_BALANCING_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final LoadBalancerInputs loadBalancerInputs = new LoadBalancerInputs.Builder()
                    .withLoadBalancerArn(loadBalancerArn)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, loadBalancerInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}