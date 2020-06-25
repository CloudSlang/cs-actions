package io.cloudslang.content.nutanix.prism.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.nutanix.prism.services.CounterImpl;

import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.nutanix.prism.utils.Constants.Common.BOOLEAN_FALSE;
import static io.cloudslang.content.nutanix.prism.utils.Constants.CounterConstants.*;
import static io.cloudslang.content.nutanix.prism.utils.Descriptions.Common.FAILURE_DESC;
import static io.cloudslang.content.nutanix.prism.utils.Descriptions.Common.SUCCESS_DESC;
import static io.cloudslang.content.nutanix.prism.utils.Descriptions.Counter.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;


public class Counter {

    @Action(name = COUNTER_OPERATION_NAME, description = COUNTER_DESC,
            outputs = {
                    @Output(value = RESULT_STRING, description = RESULT_STRING_DESC),
                    @Output(value = RESULT, description = RESULT_DESC)},
            responses = {
                    @Response(text = HASMORE, field = RESULT, value = HASMORE, matchType = MatchType.COMPARE_EQUAL,
                            responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = NOMORE, field = RESULT, value = NOMORE, matchType = MatchType.COMPARE_EQUAL,
                            responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RESULT, value = FAILURE, matchType = MatchType.COMPARE_EQUAL,
                            isDefault = true, isOnFail = true, responseType = ERROR, description = FAILURE_DESC)})
    public Map<String, String> execute(@Param(value = FROM, required = true, description = FROM_DESC) String from,
                                       @Param(value = TO, required = true, description = TO_DESC) String to,
                                       @Param(value = INCREMENT_BY, description = INCREMENT_BY_DESC) String incrementBy,
                                       @Param(value = RESET, description = RESET_DESC) String reset,
                                       @Param("globalSessionObject") GlobalSessionObject<Map<String, Object>>
                                               globalSessionObject) {

        reset = defaultIfEmpty(reset, BOOLEAN_FALSE);
        incrementBy = defaultIfEmpty(incrementBy, INCREMENT_BY_DEFAULT_VALUE);


        return CounterImpl.counter(to, from, incrementBy, Boolean.valueOf(reset), globalSessionObject);

    }

}
