package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.InputNames;
import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.utils.StringMethod;
import io.cloudslang.content.utils.Validator;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


public class ModifyListElementsAction {

    /**
     * This method checks to see if a list contains every element in another list.
     *
     * @param list              - The list to modify elements.
     * @param delimiter         A delimiter separating elements in the two lists.
     *                          Default value : comma.
     * @param method            The method for modifying the elements.
     *                          Valid values: to_uppercase, to_lowercase, add_prefix, add_suffix.
     * @param value             Optional - The value for suffix or prefix.
     *                          Default value: empty string.
     * @param stripWhitespaces: Optional - True if leading and trailing whitespaces should be removed from the list.
     *                          #!                           Default: false.
     *                          #!                           Valid values: true, false.
     * @return a map with following entries:
     * return_result: The modified list if operation succeeded. Otherwise it will contain the message of the exception.
     * return_code: 0 if operation succeeded, -1 otherwise.
     * exception: The exception's stack trace if operation failed. Empty otherwise.
     */

    @Action(name = "Modify List Elements",
            outputs = {
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_RESULT),
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_CODE),
                    @Output(io.cloudslang.content.constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = io.cloudslang.content.constants.ResponseNames.SUCCESS,
                            field = io.cloudslang.content.constants.OutputNames.RETURN_CODE,
                            value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = io.cloudslang.content.constants.ResponseNames.FAILURE,
                            field = io.cloudslang.content.constants.OutputNames.RETURN_CODE,
                            value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL,
                            isOnFail = true, isDefault = true)
            })
    public @NotNull Map<String, String> execute(@Param(value = InputNames.LIST, required = true) String list,
                                                @Param(value = InputNames.DELIMITER, required = true) String delimiter,
                                                @Param(value = InputNames.METHOD, required = true) String method,
                                                @Param(value = InputNames.STRIP_WHITESPACES) String stripWhitespaces,
                                                @Param(value = InputNames.VALUE) String value) {
        //Get default values

        delimiter = defaultIfEmpty(delimiter, StringUtils.EMPTY);

        boolean stripWhitespace;
        String methods;
        try {
            stripWhitespace = Validator.parseStripWhitespaces(stripWhitespaces);
            methods = Validator.parseMethod(method);
        } catch (Exception e) {
            return OutputUtilities.getFailureResultsMap(e.getMessage());
        }

        try {
            String test = StringMethod.execute(list, methods, value, stripWhitespace, delimiter);
            return OutputUtilities.getSuccessResultsMap(test);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex.getMessage());
        }
    }
}
