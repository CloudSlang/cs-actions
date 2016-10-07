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
import io.cloudslang.content.jclouds.execute.images.CreateImageInRegionExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class CreateImageInRegionAction {
    /**
     * Creates an Amazon EBS-backed AMI from an Amazon EBS-backed instance that is either running or stopped.
     *
     * @param provider    Cloud provider on which you have the instance.
     *                    Default: "amazon"
     * @param endpoint    Endpoint to which request will be sent.
     *                    Example: "https://ec2.amazonaws.com"
     * @param identity    Optional - Username of your account or the Access Key ID.
     * @param credential  Optional - Password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost   Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort   Optional - Proxy server port.
     * @param debugMode   Optional - If "true" then the execution logs will be shown in CLI console.
     * @param region      Optional - Region where image will be created. ListRegionAction can be used in order to
     *                    get all regions. For further details check: http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region
     *                    Default: "us-east-1".
     * @param instanceId  ID of the server (instance) to be used to create image.
     * @param name        A name for the new image.
     * @param description Optional - A description for the new image.
     *                    Default: ""
     * @param noReboot    Optional - By default, Amazon EC2 attempts to shut down and reboot the instance before
     *                    creating the image. If the 'No Reboot' option is set, Amazon EC2 doesn't shut down the
     *                    instance before creating the image. When this option is used, file system integrity on
     *                    the created image can't be guaranteed.
     *                    Default: "true"
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Create Image in Region",
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
                                       @Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String endpoint,
                                       @Param(value = Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(value = Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(value = Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                       @Param(value = Inputs.CommonInputs.DEBUG_MODE) String debugMode,

                                       @Param(value = Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.INSTANCE_ID, required = true) String instanceId,

                                       @Param(value = Inputs.ImageInputs.IMAGE_DESCRIPTION) String description,
                                       @Param(value = Inputs.ImageInputs.NAME, required = true) String name,
                                       @Param(value = Inputs.ImageInputs.NO_REBOOT) String noReboot) throws Exception {

        CommonInputs inputs = new CommonInputs.Builder()
                .withProvider(provider)
                .withEndpoint(endpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDebugMode(debugMode)
                .build();

        CustomInputs customInputs = new CustomInputs.Builder()
                .withRegion(region)
                .withInstanceId(instanceId)
                .build();

        ImageInputs imageInputs = new ImageInputs.Builder()
                .withCustomInputs(customInputs)
                .withImageName(name)
                .withImageDescription(description)
                .withImageNoReboot(noReboot)
                .build();

        try {
            return new CreateImageInRegionExecutor().execute(inputs, imageInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}