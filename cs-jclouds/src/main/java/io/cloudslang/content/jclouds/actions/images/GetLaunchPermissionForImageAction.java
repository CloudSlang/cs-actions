package io.cloudslang.content.jclouds.actions.images;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
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

import static io.cloudslang.content.jclouds.entities.constants.Inputs.CustomInputs.IMAGE_ID;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Apis.AMAZON_EC2_API;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.LAUNCH_PERMISSION;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.jclouds.entities.constants.Constants.QueryApiActions.DESCRIBE_IMAGE_ATTRIBUTE;

/**
 * Created by Mihai Tusa.
 * 5/10/2016.
 */
public class GetLaunchPermissionForImageAction {
    /**
     * Describes the launch permission of the specified AMI.
     *
     * @param endpoint      Endpoint to which request will be sent.
     *                      Example: "https://ec2.amazonaws.com"
     * @param identity      Optional - Username of your account or the Access Key ID.
     * @param credential    Optional - Password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost     Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort     Optional - Proxy server port.
     * @param proxyUsername Optional - proxy server user name.
     * @param proxyPassword Optional - proxy server password associated with the <proxyUsername> input value.
     * @param headers       Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                      The header name-value pair will be separated by ":".
     *                      Format: Conforming with HTTP standard for headers (RFC 2616)
     *                      Examples: "Accept:text/plain"
     * @param queryParams   Optional - string containing query parameters that will be appended to the URL. The names
     *                      and the values must not be URL encoded because if they are encoded then a double encoded
     *                      will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                      separated from query value by "=".
     *                      Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     * @param version       Version of the web service to made the call against it.
     *                      Example: "2016-04-01"
     *                      Default: ""
     * @param imageId       ID of the specified image to retrieve launch permission for.
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Get Launch Permission for Image",
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
    public Map<String, String> execute(@Param(value = ENDPOINT, required = true) String endpoint,
                                       @Param(value = IDENTITY, required = true) String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = VERSION, required = true) String version,
                                       @Param(value = IMAGE_ID, required = true) String imageId) {
        try {
            CommonInputs inputs = new CommonInputs.Builder()
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
                    .withAction(DESCRIBE_IMAGE_ATTRIBUTE)
                    .withApiService(AMAZON_EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.Builder().withAttribute(LAUNCH_PERMISSION).withImageId(imageId).build();

            return new QueryApiExecutor().execute(inputs, customInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}