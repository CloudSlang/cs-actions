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
import io.cloudslang.content.jclouds.execute.UpdateServerTypeExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class UpdateServerTypeAction {
    @Action(name = "Update Server Type",
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
                                       @Param(value = Inputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(Inputs.PROXY_HOST) String proxyHost,
                                       @Param(Inputs.PROXY_PORT) String proxyPort,

                                       @Param(Inputs.REGION) String region,
                                       @Param(value = Inputs.SERVER_ID, required = true) String serverId,
                                       @Param(Inputs.SERVER_TYPE) String serverType,
                                       @Param(Inputs.OPERATION_TIMEOUT) String operationTimeout,
                                       @Param(Inputs.POOLING_INTERVAl) String poolingInterval) throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(identityEndpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .build();

        CustomInputs customInputs = new CustomInputs.SpecificInputsBuilder()
                .withRegion(region)
                .withServerId(serverId)
                .withInstanceType(serverType)
                .withOperationTimeout(operationTimeout)
                .withPoolingInterval(poolingInterval)
                .build();

        try {
            return new UpdateServerTypeExecutor().execute(inputs, customInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}