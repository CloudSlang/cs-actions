package io.cloudslang.content.jclouds.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.outputs.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.ServerIdentificationInputs;
import io.cloudslang.content.jclouds.execute.StartServerExecutor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by persdana on 5/25/2015.
 */
public class StartServerAction {
    @Action(name = "Start Server",
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

        ServerIdentificationInputs startServerInputs = new ServerIdentificationInputs();
        startServerInputs.setProvider(provider);
        startServerInputs.setEndpoint(identityEndpoint);
        startServerInputs.setIdentity(identity);
        startServerInputs.setCredential(credential);
        startServerInputs.setServerId(serverId);
        startServerInputs.setRegion(region);
        startServerInputs.setProxyHost(proxyHost);
        startServerInputs.setProxyPort(proxyPort);

        try {
            return new StartServerExecutor().execute(startServerInputs);

        } catch (Exception e) {
            return exceptionResult(e.getMessage(), e);
        }
    }

    private Map<String, String> exceptionResult(String message, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String eStr = writer.toString().replace("" + (char) 0x00, "");

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(Outputs.RETURN_RESULT, message);
        returnResult.put(Outputs.RETURN_CODE, Outputs.FAILURE_RETURN_CODE);
        returnResult.put(Outputs.EXCEPTION, eStr);
        return returnResult;
    }

}
