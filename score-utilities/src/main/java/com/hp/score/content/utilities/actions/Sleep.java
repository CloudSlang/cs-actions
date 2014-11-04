package com.hp.score.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.score.content.utilities.utils.Constants;
import com.hp.score.content.utilities.utils.StringUtils;
import com.hp.score.content.utilities.utils.UnresolvedExpressionException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by persdana on 10/30/2014.
 */
public class Sleep {

    private static final String SUCCESS_MESSAGE = "completed sleep";
    public static final String SECONDS = "seconds";
    public static final String NULL_SECONDS_EXCEPTION_MESSAGE = "seconds is null";

    @Action(name="Sleep",
            outputs = {
                    @Output(Constants.OutputNames.RESULT_STRING),
                    @Output(Constants.OutputNames.RESULT_TEXT)},
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RESULT_TEXT, value = Constants.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RESULT_TEXT, value = Constants.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> sleep(@Param(SECONDS) String seconds) {
        Map<String, String> result = new HashMap<>();

        try {
            if ((seconds == null) || (seconds.length() == 0))
                throw new UnresolvedExpressionException(NULL_SECONDS_EXCEPTION_MESSAGE);

            Thread.sleep( Integer.valueOf(seconds) *1000);

            result.put(Constants.OutputNames.RESULT_TEXT, Constants.SUCCESS);
            result.put(Constants.OutputNames.RESULT_STRING, SUCCESS_MESSAGE);
        }
        catch (Exception e) {
            result.put(Constants.OutputNames.RESULT_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RESULT_STRING, StringUtils.toString(e));
        }

        return result;
    }
}
