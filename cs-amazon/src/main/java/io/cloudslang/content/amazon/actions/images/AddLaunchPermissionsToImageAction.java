package io.cloudslang.content.amazon.actions.images;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.ImageInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.ADD_OPERATION_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.LAUNCH_PERMISSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.MODIFY_IMAGE_ATTRIBUTE;
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
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.IMAGE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.USER_GROUPS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ImageInputs.USER_IDS_STRING;

/**
 * Created by Mihai Tusa.
 * 5/10/2016.
 */
public class AddLaunchPermissionsToImageAction {
    /**
     * Adds launch permission to the specified AMI.
     * Note:
     * AWS Marketplace product codes cannot be modified. Images with an AWS Marketplace product code cannot be made public.
     *
     * @param endpoint         Optional - Endpoint to which request will be sent.
     *                         Example: "https://ec2.amazonaws.com"
     * @param identity         Username of your account or the Access Key ID.
     * @param credential       Password of the user or the Secret Access Key that correspond to the identity
     *                         input.
     * @param proxyHost        Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort        Optional - Proxy server port.
     * @param proxyUsername    Optional - proxy server user name.
     * @param proxyPassword    Optional - proxy server password associated with the <proxyUsername> input value.
     * @param headers          Optional - string containing the headers to use for the request separated by new line (CRLF).
     *                         The header name-value pair will be separated by ":".
     *                         Format: Conforming with HTTP standard for headers (RFC 2616)
     *                         Examples: "Accept:text/plain"
     * @param queryParams      Optional - string containing query parameters that will be appended to the URL. The names
     *                         and the values must not be URL encoded because if they are encoded then a double encoded
     *                         will occur. The separator between name-value pairs is "&" symbol. The query name will be
     *                         separated from query value by "=".
     *                         Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     * @param version          Optional - Version of the web service to made the call against it.
     *                         Example: "2016-04-01"
     *                         Default: "2016-04-01"
     * @param imageId          ID of the specified image to add launch permission for.
     * @param userIdsString    Optional - A string that contains: none, one or more user IDs separated by delimiter.
     *                         Default: ""
     * @param userGroupsString Optional - A string that contains: none, one or more user groups separated by delimiter.
     *                         Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Add Launch Permissions to Image",
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
                                       @Param(value = IMAGE_ID, required = true) String imageId,
                                       @Param(value = USER_IDS_STRING) String userIdsString,
                                       @Param(value = USER_GROUPS_STRING) String userGroupsString) {
        try {
            version = InputsUtil.getDefaultStringInput(version, "2016-04-01");
            final CommonInputs inputs = new CommonInputs.Builder()
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
                    .withAction(MODIFY_IMAGE_ATTRIBUTE)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder()
                    .withAttribute(LAUNCH_PERMISSION)
                    .withOperationType(ADD_OPERATION_TYPE)
                    .withImageId(imageId)
                    .build();

            final ImageInputs imageInputs = new ImageInputs.Builder()
                    .withUserIdsString(userIdsString)
                    .withUserGroupsString(userGroupsString)
                    .build();

            return new QueryApiExecutor().execute(inputs, customInputs, imageInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}