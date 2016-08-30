package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.utils.ListProcessor;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.utils.Constants.OutputNames.RESPONSE;
import static io.cloudslang.content.utils.Constants.OutputNames.RESULT_TEXT;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.utils.Constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.utils.Constants.ResponseNames.FAILURE;
import static io.cloudslang.content.utils.Constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_FAILURE;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_SUCCESS;

/**
 * Created by giloan on 7/8/2016.
 */
public class ListSortAction {

    private static final String LIST = "list";
    private static final String DELIMITER = "delimiter";
    private static final String REVERSE = "reverse";

    /**
     * This method sorts a list of strings. If the list contains only numerical strings, it is sorted in numerical order.
     * Otherwise it is sorted alphabetically.
     *
     * @param list      The list to be sorted.
     * @param delimiter The list delimiter.
     * @param reverse   A boolean value for sorting the list in reverse order.
     * @return The sorted list.
     */
    @Action(name = "List Sort",
            outputs = {
                    @Output(RESULT_TEXT),
                    @Output(RESPONSE),
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = FAILURE, field = RETURN_CODE, value = RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, isOnFail = true, isDefault = true)
            })
    public Map<String, String> sortList(@Param(value = LIST, required = true) String list,
                                        @Param(value = DELIMITER, required = true) String delimiter,
                                        @Param(value = REVERSE) String reverse) {

        Map<String, String> result = new HashMap<>();
        try {
            String sortedList = sort(list, Boolean.parseBoolean(reverse), delimiter);
            result.put(RESULT_TEXT, sortedList);
            result.put(RESPONSE, SUCCESS);
            result.put(RETURN_RESULT, sortedList);
            result.put(RETURN_CODE, RETURN_CODE_SUCCESS);
        } catch (Exception e) {
            result.put(RESULT_TEXT, e.getMessage());
            result.put(RESPONSE, FAILURE);
            result.put(RETURN_RESULT, e.getMessage());
            result.put(RETURN_CODE, RETURN_CODE_FAILURE);
        }
        return result;
    }

    private String sort(String list, boolean reverse, String delimiter) {
        try {
            int[] unsorted = ListProcessor.toIntArray(list, delimiter);
            if (reverse) {
                return ListProcessor.toString(ListProcessor.reverse(ListProcessor.sort(unsorted)), delimiter);
            } else {
                return ListProcessor.toString(ListProcessor.sort(unsorted), delimiter);
            }
        } catch (NumberFormatException e) {
            try {
                double[] unsorted = ListProcessor.toDoubleArray(list, delimiter);
                if (reverse) {
                    return ListProcessor.toString(ListProcessor.reverse(ListProcessor.sort(unsorted)), delimiter);
                } else {
                    return ListProcessor.toString(ListProcessor.sort(unsorted), delimiter);
                }
            } catch (NumberFormatException f) {
                String[] unsorted = ListProcessor.toArray(list, delimiter);
                if (reverse) {
                    return ListProcessor.toString(ListProcessor.reverse(ListProcessor.sort(unsorted)), delimiter);
                } else {
                    return ListProcessor.toString(ListProcessor.sort(unsorted), delimiter);
                }
            }
        }
    }
}
