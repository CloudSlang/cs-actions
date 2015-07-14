package io.cloudslang.content.jclouds.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.ServerIdentificationInputs;
import io.cloudslang.content.jclouds.entities.outputs.Outputs;
import io.cloudslang.content.jclouds.execute.RemoveServerExecutor;
import io.cloudslang.content.jclouds.utilities.ExceptionProcessor;

import java.util.Map;

/**
 * Created by persdana on 6/23/2015.
 */
public class RemoveServerAction {
    @Action(name = "Remove Server",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = CommonInputs.PROVIDER, required = true) String provider,
            @Param(value = CommonInputs.ENDPOINT, required = true) String identityEndpoint,
            @Param(value = CommonInputs.IDENTITY) String identity,
            @Param(value = CommonInputs.CREDENTIAL) String credential,
            @Param(value = ServerIdentificationInputs.REGION) String region,
            @Param(value = ServerIdentificationInputs.SERVER_ID) String serverId,
            @Param(value = CommonInputs.PROXY_HOST) String proxyHost,
            @Param(value = CommonInputs.PROXY_PORT) String proxyPort
    ) {

        ServerIdentificationInputs serverIdentificationInputs = new ServerIdentificationInputs(provider, identity, credential, identityEndpoint, proxyHost, proxyPort, region, serverId);

        try {
            return new RemoveServerExecutor().execute(serverIdentificationInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e.getMessage(), e);
        }
    }
}
