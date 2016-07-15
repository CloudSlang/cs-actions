package io.cloudslang.content.json.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.json.services.JsonService;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Folea Ilie Cristian on 2/3/2016.
 */
public class RemoveEmptyElementAction {
    @Action(name = "Remove Empty Elements",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.RETURN_RESULT)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> removeEmptyElements(@Param(value = Constants.InputNames.JSON_OBJECT, required = true) String json) {
        Map<String, String> resultMap = new HashMap<String, String>();

        try {
            JsonService jsonService = new JsonService();
            String result = jsonService.removeEmptyElementsJson(json);

            resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_SUCCESS);
            resultMap.put(Constants.OutputNames.RETURN_RESULT, result);
        } catch (Exception ex) {
            resultMap.put(Constants.OutputNames.EXCEPTION, StringUtils.getStackTraceAsString(ex));
            resultMap.put(Constants.OutputNames.ERROR_MESSAGE, ex.getMessage());
            resultMap.put(Constants.OutputNames.RETURN_CODE, Constants.ReturnCodes.RETURN_CODE_FAILURE);
            return resultMap;
        }

        return resultMap;
    }
}
