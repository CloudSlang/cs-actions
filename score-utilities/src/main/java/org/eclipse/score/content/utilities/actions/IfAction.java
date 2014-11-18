package org.eclipse.score.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import org.eclipse.score.content.utilities.utils.Constants;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by persdana on 10/31/2014.
 */
public class IfAction {
    public static final String CONDITION = "condition";


    @Action(name = "If",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.EXCEPTION)},
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_RESULT, value = "true", matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_RESULT, value = "false", matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> ifAction(@Param(CONDITION) String condition) {
        Map<String, String> result = new HashMap<>();

        try {
            ScriptEngineManager sem = new ScriptEngineManager();
            ScriptEngine se = sem.getEngineByName("JavaScript");

            String resultStr = se.eval(condition).toString();
            Boolean resultBool = Boolean.parseBoolean(resultStr);

            result.put(Constants.OutputNames.RETURN_RESULT, resultBool.toString());
            result.put(Constants.OutputNames.EXCEPTION, Constants.EMPTY_STRING);
        } catch (Exception e) {
            result.put(Constants.OutputNames.RETURN_RESULT, Boolean.FALSE.toString());
            result.put(Constants.OutputNames.EXCEPTION, e.toString());
        }

        return result;
    }
}
