package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.utils.Constants;
import io.cloudslang.content.utils.ListProcessor;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.HashMap;
import static io.cloudslang.content.utils.Constants.OutputNames.RESPONSE_TEXT;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.utils.Constants.ResponseNames.FAILURE;
import static io.cloudslang.content.utils.Constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_FAILURE;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_SUCCESS;

/**
 * Created by moldovas on 7/12/2016.
 */

public class ListContainsAction {
    private static final String SUBLIST = "sublist";
    private static final String DELIMITER = "delimiter";
    private static final String CONTAINER = "container";
    private static final String IGNORE_CASE = "ignoreCase";

    /**
     * This method checks to see if a list contains every element in another list.
     *
     * @param sublist      The contained list.
     * @param container    The containing list.
     * @param delimiter    A delimiter separating elements in the two lists. Default is a comma.
     * @param ignoreCase   If set to 'True' then the compare is not case sensitive. Default is True.
     * @return sublist is contained in container or not.
     */

    @Action(name = "List Contains",
            outputs = {
                    @Output(RESPONSE_TEXT),
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = FAILURE, field = RETURN_CODE, value = RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, isOnFail = true, isDefault = true)
            })
    public Map<String, String> containsElement(@Param(value = SUBLIST, required = true) String sublist,
                                               @Param(value = CONTAINER, required = true) String container,
                                               @Param(value = DELIMITER) String delimiter,
                                               @Param(value = IGNORE_CASE) String ignoreCase
                                               ) {
        Map<String, String> result = new HashMap<>();
        ListProcessor.listContainsElements(sublist, container, getInputDefaultValue(delimiter, Constants.DEFAULT_DELIMITER), result,
                getInputDefaultValue(ignoreCase, Constants.IGNORE_CASE_DEFAULT));
        return result;
    }

    private static String getInputDefaultValue(String input, String defaultValue) {
        return (StringUtils.isEmpty(input)) ? defaultValue : input;
    }
}