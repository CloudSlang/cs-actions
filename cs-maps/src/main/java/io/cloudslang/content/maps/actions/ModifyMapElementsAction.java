package io.cloudslang.content.maps.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.maps.constants.InputNames;
import io.cloudslang.content.maps.entities.ModifyMapElementsInput;
import io.cloudslang.content.maps.services.ModifyMapElementsService;
import io.cloudslang.content.utils.OutputUtilities;

import java.util.Map;


public class ModifyMapElementsAction {

    private final ModifyMapElementsService service = new ModifyMapElementsService();


    @Action(name = "Modify Map Elements",
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

    public Map<String, String> execute(@Param(value = InputNames.MAP, required = true) String map,
                                       @Param(value = InputNames.METHOD, required = true) String method,
                                       @Param(value = InputNames.ELEMENTS, required = true) String elements,
                                       @Param(value = InputNames.METHOD_VALUE, required = true) String value,
                                       @Param(value = InputNames.PAIR_DELIMITER, required = true) String pairDelimiter,
                                       @Param(value = InputNames.ENTRY_DELIMITER, required = true) String entryDelimiter,
                                       @Param(value = InputNames.MAP_START) String mapStart,
                                       @Param(value = InputNames.MAP_END) String mapEnd,
                                       @Param(value = InputNames.ELEMENT_WRAPPER) String elementWrapper,
                                       @Param(value = InputNames.STRIP_WHITESPACES) String stripWhitespaces) {
        try {
            ModifyMapElementsInput input = new ModifyMapElementsInput.Builder()
                    .map(map)
                    .elements(elements)
                    .method(method)
                    .value(value)
                    .pairDelimiter(pairDelimiter)
                    .entryDelimiter(entryDelimiter)
                    .mapStart(mapStart)
                    .mapEnd(mapEnd)
                    .elementWrapper(elementWrapper)
                    .stripWhitespaces(stripWhitespaces)
                    .build();
            return service.execute(input);
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
