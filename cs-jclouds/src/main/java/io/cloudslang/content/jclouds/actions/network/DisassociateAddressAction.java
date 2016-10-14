package io.cloudslang.content.jclouds.actions.network;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.ElasticIpInputs;
import io.cloudslang.content.jclouds.execute.queries.QueryApiExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.jclouds.entities.constants.Inputs.CommonInputs.VERSION;

import static io.cloudslang.content.jclouds.entities.constants.Inputs.CustomInputs.ASSOCIATION_ID;

import static io.cloudslang.content.jclouds.entities.constants.Inputs.ElasticIpInputs.PUBLIC_IP;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Apis.AMAZON_EC2_API;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.jclouds.entities.constants.Constants.QueryApiActions.DISASSOCIATE_ADDRESS;

/**
 * Created by Mihai Tusa.
 * 9/13/2016.
 */
public class DisassociateAddressAction {
    /**
     * Disassociates an Elastic IP address from the instance or network interface it's associated with.
     * Note: An Elastic IP address is for use in either the EC2-Classic platform or in a VPC. For more information, see
     * Elastic IP Addresses in the Amazon Elastic Compute Cloud User Guide.
     * <p>
     * Important: This is an idempotent operation. If you perform the operation more than once, Amazon EC2 doesn't return
     * an error.
     *
     * @param endpoint      Endpoint to which request will be sent.
     *                      Default: "https://ec2.amazonaws.com"
     * @param identity      ID of the secret access key associated with your Amazon AWS or IAM account.
     *                      Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential    Secret access key associated with your Amazon AWS or IAM account.
     *                      Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost     Optional - proxy server used to connect to Amazon API. If empty no proxy will be used.
     *                      Default: ""
     * @param proxyPort     Optional - proxy server port. You must either specify values for both <proxyHost> and <proxyPort>
     *                      inputs or leave them both empty.
     *                      Default: ""
     * @param proxyUsername Optional - proxy server user name.
     *                      Default: ""
     * @param proxyPassword Optional - proxy server password associated with the <proxyUsername> input value.
     *                      Default: ""
     * @param version       Version of the web service to made the call against it.
     *                      Example: "2016-04-01"
     * @param headers       Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                      The header name-value pair will be separated by ":".
     *                      Format: Conforming with HTTP standard for headers (RFC 2616)
     *                      Examples: "Accept:text/plain"
     *                      Default: ""
     * @param queryParams   Optional - string containing query parameters that will be appended to the URL. The names and
     *                      the values must not be URL encoded because if they are encoded then a double encoded will occur.
     *                      The separator between name-value pairs is "&" symbol. The query name will be separated from
     *                      query value by "=".
     *                      Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                      Default: ""
     * @param associationId Optional - [EC2-VPC] Association ID. Required for EC2-VPC.
     *                      Default: ""
     * @param publicIp      Optional - Elastic IP address. This is required for EC2-Classic.
     *                      Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Disassociate Address",
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
    public Map<String, String> disassociateAddress(@Param(value = ENDPOINT, required = true) String endpoint,
                                                   @Param(value = IDENTITY, required = true) String identity,
                                                   @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                                   @Param(value = PROXY_HOST) String proxyHost,
                                                   @Param(value = PROXY_PORT) String proxyPort,
                                                   @Param(value = PROXY_USERNAME) String proxyUsername,
                                                   @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                                   @Param(value = QUERY_PARAMS) String queryParams,
                                                   @Param(value = HEADERS) String headers,
                                                   @Param(value = VERSION, required = true) String version,
                                                   @Param(value = ASSOCIATION_ID) String associationId,
                                                   @Param(value = PUBLIC_IP) String publicIp) {
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
                    .withAction(DISASSOCIATE_ADDRESS)
                    .withApiService(AMAZON_EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.Builder().withAssociationId(associationId).build();
            ElasticIpInputs elasticIpInputs = new ElasticIpInputs.Builder().withPublicIp(publicIp).build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, elasticIpInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}