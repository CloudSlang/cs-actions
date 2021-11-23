package io.cloudslang.content.json.actions;

import com.hp.oo.sdk.content.annotations.*;
import com.hp.oo.sdk.content.plugin.ActionMetadata.*;
import com.hp.oo.sdk.content.plugin.*;
import io.cloudslang.content.constants.*;
import io.cloudslang.content.json.services.*;
import io.cloudslang.content.json.utils.*;

import java.util.*;

import static io.cloudslang.content.constants.ResponseNames.*;
import static io.cloudslang.content.json.utils.Constants.ArrayIteratorAction.*;
import static io.cloudslang.content.json.utils.Descriptions.ArrayIteratorDescription.*;

public class ArrayIteratorAction {

    @Action(name = JSON_ARRAY_OP, description = ARRAY_ITERATOR_DESCRIPTION,
            outputs = {
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = HAS_MORE, field = RESULT_TEXT, value = HAS_MORE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = HAS_MORE_DESC),
                    @Response(text = NO_MORE, field = RESULT_TEXT, value = NO_MORE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED, description = NO_MORE_DESC),
                    @Response(text = FAILURE, field = RESULT_TEXT, value = FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true, description = FAILURE_DESC)})

    public Map<String, String> execute(@Param(value = Constants.InputNames.ARRAY, required = true) String array,
                                       @Param(value = GLOBAL_SESSION_OBJECT) GlobalSessionObject<Map<String, Object>> globalSessionObject) {

        return ArrayListService.iterate(array, globalSessionObject);
    }
}
