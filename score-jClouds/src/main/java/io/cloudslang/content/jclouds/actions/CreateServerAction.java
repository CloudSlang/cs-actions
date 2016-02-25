package io.cloudslang.content.jclouds.actions;

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
import io.cloudslang.content.jclouds.execute.CreateServerExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class CreateServerAction {
    @Action(name = "Create Server",
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
    public Map<String, String> execute(@Param(value = Inputs.PROVIDER, required = true) String provider,
                                       @Param(value = Inputs.ENDPOINT, required = true) String identityEndpoint,
                                       @Param(Inputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(Inputs.PROXY_HOST) String proxyHost,
                                       @Param(Inputs.PROXY_PORT) String proxyPort,

                                       @Param(Inputs.REGION) String region,
                                       @Param(Inputs.AVAILABILITY_ZONE) String availabilityZone,
                                       @Param(value = Inputs.IMAGE_REF, required = true) String imageRef,
                                       @Param(Inputs.MIN_COUNT) String minCount,
                                       @Param(Inputs.MAX_COUNT) String maxCount) throws Exception {

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
                .withAvailabilityZone(availabilityZone)
                .withImageRef(imageRef)
                .withMinCount(minCount)
                .withMaxCount(maxCount)
                .build();

        try {
            return new CreateServerExecutor().execute(inputs, customInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}