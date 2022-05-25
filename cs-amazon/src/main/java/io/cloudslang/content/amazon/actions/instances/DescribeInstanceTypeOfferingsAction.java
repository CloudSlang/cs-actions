package io.cloudslang.content.amazon.actions.instances;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.INSTANCES_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.DESCRIBE_INSTANCE_TYPE_OFFERINGS;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.*;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.MAX_RESULTS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.NEXT_TOKEN;
import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;

/**
 * Created by Amey Dwivedi.
 * 22/5/2022.
 */
public class DescribeInstanceTypeOfferingsAction {
    /**
     * Returns a list of the available instance type offerings available in region.
     *
     * @param endpoint           Endpoint to which request will be sent.
     *                           Default: "https://ec2.amazonaws.com"
     * @param identity           ID of the secret access key associated with your Amazon AWS or IAM account.
     *                           Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential         Secret access key associated with your Amazon AWS or IAM account.
     *                           Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
//     * @param region             The region where to deploy the instance.
//     *                           Example: "us-west-1"
//     *                           Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost          Optional - proxy server used to connect to Amazon API. If empty no proxy
     *                           will be used.
     *                           Default: ""
     * @param proxyPort          Optional - proxy server port. You must either specify values for both
     *                           proxyHost and proxyPort inputs or leave them both empty.
     *                           Default: ""
     * @param proxyUsername      Optional - proxy server user name.
     *                           Default: ""
     * @param proxyPassword      Optional - proxy server password associated with the proxyUsername
     *                           input value.
     *                           Default: ""
     * @param headers            Optional - string containing the headers to use for the request separated
     *                           by new line (CRLF). The header name-value pair will be separated by ":".
     *                           Format: Conforming with HTTP standard for headers (RFC 2616)
     *                           Examples: "Accept:text/plain"
     *                           Default: ""
     * @param queryParams        Optional - string containing query parameters that will be appended to
     *                           the URL. The names and the values must not be URL encoded because if
     *                           they are encoded then a double encoded will occur. The separator between
     *                           name-value pairs is "&" symbol. The query name will be separated from
     *                           query value by "=".
     *                           Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                           Default: ""
     * @param version            Version of the web service to made the call against it.
     *                           Example: "2016-11-15"
     *                           Default: "2016-11-15"
     * @param connectTimeout     The time to wait for a connection to be established.
     *                           Default: "10000"
     * @param executionTimeout   The amount of time (in milliseconds) to allow the client.
     *                           Default: "60000"
     * @param maxResults         Optional - The maximum number of results to return in a single call. To retrieve the
     *                           remaining results, make another call with the returned NextToken value. This value can
     *                           be between 5 and 1000. You cannot specify this parameter and the instance IDs parameter
     *                           or tag filters in the same call.
     *                           Default: ""
     * @param nextToken          Optional - The token to use to retrieve the next page of results. This value is null when
     *                           there are no more results to return.
     *                           Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     *         operation, or failure message and the exception if there is one
     */
    @Action(name = "Describe Instances",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(@Param(value = ENDPOINT) String endpoint,
                                       @Param(value = IDENTITY, required = true) String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                       //@Param(value = REGION) String region,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = VERSION) String version,
                                       @Param(value = CONNECT_TIMEOUT) String connectTimeout,
                                       @Param(value = EXECUTION_TIMEOUT) String executionTimeout,
                                       @Param(value = MAX_RESULTS) String maxResults,
                                       @Param(value = NEXT_TOKEN) String nextToken) {

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
                    .withAction(DESCRIBE_INSTANCE_TYPE_OFFERINGS)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final InstanceInputs instanceInputs = new InstanceInputs.Builder()
                    .withConnectTimeout(connectTimeout)
                    .withExecutionTimeout(executionTimeout)
                    .withMaxResults(maxResults)
                    .withNextToken(nextToken)
                    .build();

            return  new QueryApiExecutor().execute(commonInputs, instanceInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}
