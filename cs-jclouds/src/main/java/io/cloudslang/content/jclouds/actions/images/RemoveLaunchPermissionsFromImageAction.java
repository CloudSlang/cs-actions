package io.cloudslang.content.jclouds.actions.images;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.execute.queries.QueryApiExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 5/18/2016.
 */
public class RemoveLaunchPermissionsFromImageAction {
    private static final String REMOVE_OPERATION = "remove";
    private static final String LAUNCH_PERMISSION = "launchPermission";

    /**
     * Removes launch permission of the specified AMI.
     * Note:
     * AWS Marketplace product codes cannot be modified. Images with an AWS Marketplace product code cannot be made public.
     *
     * @param endpoint         Endpoint to which request will be sent.
     *                         Example: "https://ec2.amazonaws.com"
     * @param identity         Optional - Username of your account or the Access Key ID.
     * @param credential       Optional - Password of the user or the Secret Access Key that correspond to the identity input.
     * @param version          Version of the web service to made the call against it.
     *                         Example: "2016-04-01"
     *                         Default: ""
     * @param proxyHost        Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort        Optional - Proxy server port.
     * @param debugMode        Optional - If "true" then the execution logs will be shown in CLI console.
     * @param delimiter        Optional - The delimiter that will be used - Default: ","
     * @param imageId          ID of the specified image to remove launch permission for.
     * @param userIdsString    Optional - A string that contains: none, one or more user IDs separated by delimiter.
     *                         Default: ""
     * @param userGroupsString Optional - A string that contains: none, one or more user groups separated by delimiter.
     *                         Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Remove Launch Permissions To Image",
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
    public Map<String, String> execute(@Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String endpoint,
                                       @Param(value = Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(value = Inputs.CommonInputs.VERSION, required = true) String version,
                                       @Param(value = Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(value = Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                       @Param(value = Inputs.CommonInputs.DELIMITER) String delimiter,
                                       @Param(value = Inputs.CommonInputs.DEBUG_MODE) String debugMode,

                                       @Param(value = Inputs.CustomInputs.IMAGE_ID, required = true) String imageId,
                                       @Param(value = Inputs.ImageInputs.USER_IDS_STRING) String userIdsString,
                                       @Param(value = Inputs.ImageInputs.USER_GROUPS_STRING) String userGroupsString) {
        try {
            CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                    .withEndpoint(endpoint)
                    .withIdentity(identity)
                    .withCredential(credential)
                    .withProxyHost(proxyHost)
                    .withProxyPort(proxyPort)
                    .withDelimiter(delimiter)
                    .withDebugMode(debugMode)
                    .withVersion(version)
                    .withAction(Constants.QueryApiActions.MODIFY_IMAGE_ATTRIBUTE)
                    .withApiService(Constants.Apis.AMAZON_EC2_API)
                    .withRequestUri(Constants.Miscellaneous.EMPTY)
                    .withRequestPayload(Constants.Miscellaneous.EMPTY)
                    .withHttpClientMethod(Constants.AwsParams.HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                    .withAttribute(LAUNCH_PERMISSION)
                    .withOperationType(REMOVE_OPERATION)
                    .withImageId(imageId)
                    .build();

            ImageInputs imageInputs = new ImageInputs.ImageInputsBuilder()
                    .withCustomInputs(customInputs)
                    .withUserIdsString(userIdsString)
                    .withUserGroupsString(userGroupsString)
                    .build();

            return new QueryApiExecutor().execute(inputs, imageInputs);
        } catch (Exception exception) {
                return ExceptionProcessor.getExceptionResult(exception);
            }
        }
    }