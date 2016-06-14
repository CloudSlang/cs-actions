package io.cloudslang.content.jclouds.actions.images;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.execute.images.RemoveLaunchPermissionsFromImageExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 5/18/2016.
 */
public class RemoveLaunchPermissionsFromImageAction {
    /**
     * Removes launch permission of the specified AMI.
     * Note:
     * AWS Marketplace product codes cannot be modified. Images with an AWS Marketplace product code cannot be made public.
     *
     * @param provider         Cloud provider on which you have the image.
     *                         Default: "amazon"
     * @param identityEndpoint Endpoint to which first request will be sent.
     *                         Example: "https://ec2.amazonaws.com"
     * @param identity         Username of your account or the Access Key ID.
     * @param credential       Password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost        Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort        Proxy server port.
     * @param region           Optional - Region where the targeted image reside. ListRegionAction can be used in order to
     *                         get all regions. For further details check: http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region
     *                         Default: "us-east-1".
     * @param delimiter        The delimiter that will be used - Default: ","
     * @param imageId          ID of the specified image to remove launch permission for.
     * @param userIdsString    Optional - A string that contains: none, one or more user IDs separated by delimiter.
     *                         Default: ""
     * @param userGroupsString Optional - A string that contains: none, one or more user groups separated by delimiter.
     *                         Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Remove Launch Permissions To Image In Region",
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
    public Map<String, String> execute(@Param(value = Inputs.CommonInputs.PROVIDER, required = true) String provider,
                                       @Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String identityEndpoint,
                                       @Param(Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                       @Param(Inputs.CommonInputs.DELIMITER) String delimiter,

                                       @Param(Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.IMAGE_ID, required = true) String imageId,
                                       @Param(Inputs.ImageInputs.USER_IDS_STRING) String userIdsString,
                                       @Param(Inputs.ImageInputs.USER_GROUPS_STRING) String userGroupsString) throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(identityEndpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDelimiter(delimiter)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .withImageId(imageId)
                .build();

        ImageInputs imageInputs = new ImageInputs.ImageInputsBuilder()
                .withCustomInputs(customInputs)
                .withUserIdsString(userIdsString)
                .withUserGroupsString(userGroupsString)
                .build();

        try {
            return new RemoveLaunchPermissionsFromImageExecutor().execute(inputs, imageInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}