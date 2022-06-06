package io.cloudslang.content.google.javasdk.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;

public class GetCurrentTime {

    @Action(name = "Get Current Time",
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
    public Map<String, String> execute() {

        Map<String, String> results = new HashMap<>();

        try {
            results.put("returnResult", LocalDateTime.now().toString());
        } catch (Exception e) {

            return getFailureResultsMap(e);
        }

        return results;

    }
}
