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
import io.cloudslang.content.jclouds.execute.images.ResetLaunchPermissionsOnImageExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 5/18/2016.
 */
public class ResetLaunchPermissionsOnImageAction {
    /**
     * Resets the launch permission attribute of a specified AMI to its default value.
     * Note:
     * The productCodes attribute can't be reset.
     *
     * @param provider          Cloud provider on which you have the instance.
     *                          Default: "amazon"
     * @param endpoint          Endpoint to which first request will be sent.
     *                          Example: "https://ec2.amazonaws.com"
     * @param identity          Optional - Username of your account or the Access Key ID.
     * @param credential        Optional - Password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost         Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort         Optional - Proxy server port.
     * @param withExecutionLogs Optional - If "true" then the execution logs will be shown in CLI console.
     * @param region            Optional - Region where image, to resets the launch permission attribute for, reside.
     *                          ListRegionAction can be used in order to get all regions. For further details check:
     *                          http://docs.aws.amazon.com/general/latest/gr/rande.html#s3_region
     *                          Default: "us-east-1".
     * @param imageId           ID of the image to resets the launch permission attribute for
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Reset Launch Permissions On Image",
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
                                       @Param(Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                       @Param(Inputs.CommonInputs.WITH_EXECUTION_LOGS) String withExecutionLogs,

                                       @Param(Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.IMAGE_ID, required = true) String imageId) throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(endpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withExecutionLogs(withExecutionLogs)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .withImageId(imageId)
                .build();

        try {
            return new ResetLaunchPermissionsOnImageExecutor().execute(inputs, customInputs);
        } catch (Exception exception) {
            return ExceptionProcessor.getExceptionResult(exception);
        }
    }
}
