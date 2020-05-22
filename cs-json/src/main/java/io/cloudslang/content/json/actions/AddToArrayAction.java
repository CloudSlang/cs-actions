package io.cloudslang.content.json.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.json.entities.AddToArrayInput;
import io.cloudslang.content.json.services.AddToArrayService;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;

public class AddToArrayAction {

    private final AddToArrayService service = new AddToArrayService();


    /**
     * Adds an element to a JavaScript array. The resulting array, with the element
     * added, will be returned in the Result output. The given element will be added
     * as a string, which is what most flow variables contain.
     *
     * @param array   The JavaScript array that will be added to.
     *                The operation will return a failure if the array is not a JavaScript array.
     * @param element The element to add into the array. The element will be interpreted as a string and added into the array.
     *                That means that even if the value appears to be another valid JavaScript
     *                object or array, it will be added as a simple string.
     * @param index   The index (-n < i < n-1) in the array in which the value will be inserted.
     *                If an index is not specified, then the element will be appended to the end of the array.
     * @return a map containing the output of the operations. Keys present in the map are:
     * <br><b>returnResult</b> - The element of the array at specified index.
     * If the operation failed, this field will contain an error message.
     * <br><b>returnCode</b> - The returnCode of the operation: 0 for success, -1 for failure.
     * <br><b>exception</b> - The exception message if the operation goes to failure.
     */
    @Action(name = "Add to Array",
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> execute(@Param(value = Constants.InputNames.ARRAY, required = true) String array,
                                       @Param(value = Constants.InputNames.ELEMENT, required = true) String element,
                                       @Param(value = Constants.InputNames.INDEX) String index) {
        try {
            AddToArrayInput input = new AddToArrayInput.Builder()
                    .array(array)
                    .element(element)
                    .index(index)
                    .build();
            return this.service.execute(input);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
