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
import io.cloudslang.content.jclouds.execute.images.CreateImageInRegionExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 5/4/2016.
 */
public class CreateImageInRegionAction {
    @Action(name = "Create Image In Region",
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

                                       @Param(Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.SERVER_ID, required = true) String serverId,
                                       @Param(value = Inputs.CustomInputs.IMAGE_NAME, required = true) String imageName,
                                       @Param(Inputs.CustomInputs.IMAGE_DESCRIPTION) String imageDescription,
                                       @Param(Inputs.CustomInputs.IMAGE_NO_REBOOT) String imageNoReboot) throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(identityEndpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .withServerId(serverId)
                .withImageName(imageName)
                .withImageDescription(imageDescription)
                .withImageNoReboot(imageNoReboot)
                .build();

        try {
            return new CreateImageInRegionExecutor().execute(inputs, customInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}