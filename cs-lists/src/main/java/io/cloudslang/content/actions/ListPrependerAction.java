package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.utils.Constants.OutputNames.RESPONSE;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utils.Constants.ResponseNames.FAILURE;
import static io.cloudslang.content.utils.Constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_FAILURE;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_SUCCESS;

/**
 * Created by giloan on 7/8/2016.
 */
public class ListPrependerAction {

    private static final String LIST = "list";
    private static final String DELIMITER = "delimiter";
    private static final String ELEMENT = "element";

    /**
     * This method pre-pends an element to a list of strings.
     *
     * @param list      The list to pre-pend to.
     * @param element   The element to pre-pend to the list.
     * @param delimiter The list delimiter. Delimiter can be empty string.
     * @return The new list.
     */
    @Action(name = "List Prepender",
            outputs = {
                    @Output(RESPONSE),
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = FAILURE, field = RETURN_CODE, value = RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, isOnFail = true, isDefault = true)
            })
    public Map<String, String> prependElement(@Param(value = LIST, required = true) String list,
                                              @Param(value = ELEMENT, required = true) String element,
                                              @Param(value = DELIMITER) String delimiter) {
        Map<String, String> result = new HashMap<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb = StringUtils.isEmpty(list) ? sb.append(element) : sb.append(element).append(delimiter).append(list);
            result.put(RESPONSE, SUCCESS);
            result.put(RETURN_RESULT, sb.toString());
            result.put(RETURN_CODE, RETURN_CODE_SUCCESS);
        } catch (Exception e) {
            result.put(RESPONSE, FAILURE);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(RETURN_CODE, RETURN_CODE_FAILURE);
        }
        return result;
    }
}
