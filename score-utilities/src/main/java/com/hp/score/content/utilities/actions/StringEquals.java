package com.hp.score.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.score.content.utilities.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by persdana on 10/30/2014.
 */
public class StringEquals {

    public static final String MATCHES = "Matches";
    public static final String DO_NOT_MATCH = "Does not Match";

    public static final String STRING1 = "string1";
    public static final String STRING2 = "string2";
    public static final String IGNORE_CASE = "ignoreCase";

    @Action(name="String Equals",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.RESPONSE_TEXT)},
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RESPONSE_TEXT, value = Constants.SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RESPONSE_TEXT, value = Constants.FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> compareStrings(@Param(STRING1) String string1, @Param(STRING2) String string2, @Param(IGNORE_CASE) boolean ignoreCase) {

        Map<String, String> result = new HashMap<>();
        result.put(Constants.OutputNames.RESPONSE_TEXT, Constants.FAILURE);

        try {
            if (ignoreCase) {
                if (string1.equalsIgnoreCase(string2)) {
                    result.put(Constants.OutputNames.RESPONSE_TEXT, Constants.SUCCESS);
                    result.put(Constants.OutputNames.RETURN_RESULT, MATCHES);
                } else {
                    result.put(Constants.OutputNames.RESPONSE_TEXT, Constants.FAILURE);
                    result.put(Constants.OutputNames.RETURN_RESULT, DO_NOT_MATCH);
                }
            } else {
                if (string1.equals(string2)) {
                    result.put(Constants.OutputNames.RESPONSE_TEXT, Constants.SUCCESS);
                    result.put(Constants.OutputNames.RETURN_RESULT, MATCHES);
                } else {
                    result.put(Constants.OutputNames.RESPONSE_TEXT, Constants.FAILURE);
                    result.put(Constants.OutputNames.RETURN_RESULT, DO_NOT_MATCH);
                }
            }
        } catch (Exception e) {
            result.put(Constants.OutputNames.RESPONSE_TEXT, Constants.FAILURE);
            result.put(Constants.OutputNames.RETURN_RESULT, e.fillInStackTrace().toString());
        }

        return result;
    }
}
