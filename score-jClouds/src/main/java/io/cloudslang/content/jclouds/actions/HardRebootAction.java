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
import io.cloudslang.content.jclouds.execute.HardRebootExecutor;
import io.cloudslang.content.jclouds.utilities.ExceptionProcessor;

import java.util.Map;

/**
 * Created by persdana on 6/22/2015.
 */
public class HardRebootAction {
    /**
     * Perform a hard reboot of a server.  A hard reboot (HARD) is equivalent to power cycling the server.
     *
     * @param provider         The cloud provider on which you have the instance. Supported values: "amazon" or "openstack".
     * @param identityEndpoint The endpoint to which first request is sent. Example: "https://ec2.amazonaws.com" or "http://hostOrIp:5000/v2.0"
     * @param identity         The username of your account. For openstack provider the username must be after a authTokenAlias which will be create ex: "demo:admin"
     * @param credential
     * @param region
     * @param serverId
     * @param proxyHost
     * @param proxyPort
     * @return
     */
    @Action(name = "Hard Reboot",
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
            return new HardRebootExecutor().execute(serverIdentificationInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }

}
