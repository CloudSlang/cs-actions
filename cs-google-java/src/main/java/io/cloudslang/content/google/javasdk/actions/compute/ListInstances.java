package io.cloudslang.content.google.javasdk.actions.compute;

import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.InstanceList;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.google.javasdk.actions.utils.GoogleComputeImpl;
import io.cloudslang.content.google.javasdk.actions.utils.Outputs;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.google.javasdk.actions.utils.Inputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

public class ListInstances {
    @Action(name = "List Instances",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.RETURN_CODE_SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.RETURN_CODE_FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = PROJECT_ID, required = true) String projectId,
                                       @Param(value = ZONE, required = true) String zone,
                                       @Param(value = JSON_TOKEN, required = true) String jsonToken,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD) String proxyPassword) {

        Map<String, String> results = new HashMap<>();

        try {
            Compute computeService = GoogleComputeImpl.createComputeService(jsonToken, proxyHost, Integer.parseInt(proxyPort));
            Compute.Instances.List request = computeService.instances().list(projectId, zone);

            InstanceList response = request.execute();


            results.put("returnResult", response.toString());
        } catch (Exception e) {

            return getFailureResultsMap(e);
        }

        return results;

    }
}
