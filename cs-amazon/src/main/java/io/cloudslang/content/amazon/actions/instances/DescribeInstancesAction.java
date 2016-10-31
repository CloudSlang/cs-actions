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

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.AMAZON_EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.QueryApiActions.DESCRIBE_INSTANCES;

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
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.INSTANCE_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.MAX_RESULTS;

/**
 * Created by Mihai Tusa.
 * 6/1/2016.
 */
public class DescribeInstancesAction {
    /**
     * Describes one or more instances.
     * Note: If you specify one or more instance IDs, Amazon EC2 returns information for those instances.
     * If you do not specify instance IDs, Amazon EC2 returns information for all relevant instances.
     * If you specify an instance that you do not own, it's not included in the output.
     * Recently terminated instances might appear in the output. This interval is usually less than one hour.
     *
     * @param endpoint          Endpoint to which request will be sent.
     *                          Default: "https://ec2.amazonaws.com"
     * @param identity          ID of the secret access key associated with your Amazon AWS or IAM account.
     *                          Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential        Secret access key associated with your Amazon AWS or IAM account.
     *                          Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost         Optional - proxy server used to connect to Amazon API. If empty no proxy
     *                          will be used.
     *                          Default: ""
     * @param proxyPort         Optional - proxy server port. You must either specify values for both
     *                          <proxyHost> and <proxyPort> inputs or leave them both empty.
     *                          Default: ""
     * @param proxyUsername     Optional - proxy server user name.
     *                          Default: ""
     * @param proxyPassword     Optional - proxy server password associated with the <proxyUsername>
     *                          input value.
     *                          Default: ""
     * @param headers           Optional - string containing the headers to use for the request separated
     *                          by new line (CRLF). The header name-value pair will be separated by ":".
     *                          Format: Conforming with HTTP standard for headers (RFC 2616)
     *                          Examples: "Accept:text/plain"
     *                          Default: ""
     * @param queryParams       Optional - string containing query parameters that will be appended to
     *                          the URL. The names and the values must not be URL encoded because if
     *                          they are encoded then a double encoded will occur. The separator between
     *                          name-value pairs is "&" symbol. The query name will be separated from
     *                          query value by "=".
     *                          Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                          Default: ""
     * @param version           Version of the web service to made the call against it.
     *                          Example: "2016-09-15"
     * @param delimiter         Optional - Delimiter that will be used.
     * @param instanceIdsString Optional - String that contains one or more values that represents instance IDs.
     *                          Example: "i-12345678,i-abcdef12,i-12ab34cd"
     *                          Default: ""
     * @param maxResults        Optional - The maximum number of results to return in a single call. To retrieve the
     *                          remaining results, make another call with the returned NextToken value. This value can
     *                          be between 5 and 1000. You cannot specify this parameter and the instance IDs parameter
     *                          or tag filters in the same call.
     *                          Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Describe Instances",
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
                                       @Param(value = VERSION, required = true) String version,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = INSTANCE_IDS_STRING) String instanceIdsString,
                                       @Param(value = MAX_RESULTS) String maxResults) {

        try {
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
                    .withAction(DESCRIBE_INSTANCES)
                    .withApiService(AMAZON_EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            InstanceInputs instanceInputs = new InstanceInputs.Builder()
                    .withInstanceIdsString(instanceIdsString)
                    .withMaxResults(maxResults)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, instanceInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}