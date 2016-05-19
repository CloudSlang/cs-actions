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
import io.cloudslang.content.jclouds.execute.images.DescribeImagesInRegionExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 5/6/2016.
 */
public class DescribeImagesInRegionAction {
    /**
     * Describes one or more of the images (AMIs, AKIs, and ARIs) available to you. Images available to you include
     * public images, private images that you own, and private images owned by other AWS accounts but for which you have
     * explicit launch permissions.
     * Note:
     * De-registered images are included in the returned results for an unspecified interval after de-registration.
     *
     * @param provider         Cloud provider on which you have the instance.
     *                         Default: "amazon"
     * @param identityEndpoint Endpoint to which first request will be sent.
     *                         Example: "https://ec2.amazonaws.com"
     * @param identity         Username of your account or the Access Key ID.
     * @param credential       Password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost        Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort        Proxy server port.
     * @param region           Optional - Region where image will be created. ListRegionAction can be used in order to
     *                         get all regions. For further details check: http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region
     *                         Default: "us-east-1".
     * @param identityId       Scopes the images by users with explicit launch permissions. Specify an AWS account ID,
     *                         "self" (the sender of the request), or "all" (public AMIs).
     *                         Valid: "self", "all" or AWS account ID
     *                         Default: "self"
     * @param imageIdsString   Optional - A string that contains: none, one or more image IDs separated by delimiter.
     *                         Default: ""
     * @param ownersString     Optional - Filters the images by the owner. Specify an AWS account ID, "amazon" (owner is Amazon),
     *                         "aws-marketplace" (owner is AWS Marketplace), "self" (owner is the sender of the request).
     *                         Omitting this option returns all images for which you have launch permissions, regardless
     *                         of ownership.
     *                         Valid: "", "amazon", "aws-marketplace", or "self"
     *                         Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Describe Images In Region",
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
                                       @Param(Inputs.CustomInputs.IDENTITY_ID) String identityId,
                                       @Param(Inputs.ImageInputs.IMAGE_IDS_STRING) String imageIdsString,
                                       @Param(Inputs.ImageInputs.OWNERS_STRING) String ownersString) throws Exception {

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
                .withIdentityId(identityId)
                .build();

        ImageInputs imageInputs = new ImageInputs.ImageInputsBuilder()
                .withCustomInputs(customInputs)
                .withImageIdsString(imageIdsString)
                .withOwnersString(ownersString)
                .build();

        try {
            return new DescribeImagesInRegionExecutor().execute(inputs, imageInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}
