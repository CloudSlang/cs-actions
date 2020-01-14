package io.cloudslang.content.hashicorp.terraform.actions.utils;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.StepSerializableSessionObject;
import io.cloudslang.content.hashicorp.terraform.services.CounterImpl;
import io.cloudslang.content.hashicorp.terraform.services.models.CounterImplException;

import java.util.HashMap;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CounterConstants.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Counter.*;
import static org.apache.commons.lang3.StringUtils.*;


public class Counter {

    public static final String RESULT_DBG_INDEX = "index";
    StepSerializableSessionObject sessionObject=new StepSerializableSessionObject("name");

    @Action(name = OPERATION_NAME, description = COUNTER_DESC,
            outputs = {
                    @Output(value = RESULT_TEXT, description = RESULT_TEXT_DESC),
                    @Output(value = RESULT, description = RESULT_DESC)},
            responses = {
                    @Response(text = HASMORE, field = RESULT, value = HASMORE, matchType = MatchType.COMPARE_EQUAL, responseType = RESOLVED),
                    @Response(text = NOMORE, field = RESULT, value = NOMORE, matchType = MatchType.COMPARE_EQUAL,responseType = RESOLVED),
                    @Response(text = FAILURE, field = RESULT, value = FAILURE, matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true,responseType = ERROR)})
    public Map<String, String> execute(@Param(value = FROM , required = true, description = FROM_DESC) String from,
                                       @Param(value = TO, required = true, description = TO_DESC) String to,
                                       @Param(value = INCREMENT_BY, description = INCREMENT_BY_DESC) String incrementBy,
                                       @Param(value = RESET, description = RESET_DESC) String reset) {

        from = defaultIfEmpty(from, EMPTY);
        to = defaultIfEmpty(to, EMPTY);
        reset = defaultIfEmpty(reset, EMPTY);
        incrementBy = defaultIfEmpty(incrementBy, EMPTY);

        CounterImpl counter = new CounterImpl();
        Map<String, String> returnResult = new HashMap<String, String>();
        returnResult.put(RESULT, FAILURE_MESSAGE);

        try {
            counter.init(to, from, incrementBy, Boolean.valueOf(reset), sessionObject);
            if (counter.hasNext()) {
                returnResult.put(RESULT_DBG_INDEX, Long.toString(counter.getIndex()));
                returnResult.put(RESULT_TEXT, counter.getNext(sessionObject));
                returnResult.put(RESULT, HASMORE);
            } else {
                counter.setStepSessionEnd(sessionObject);
                returnResult.put(RESULT_TEXT, "");
                returnResult.put(RESULT, NOMORE);
            }
        } catch (CounterImplException e) {
            returnResult.put(EXCEPTION, e.toString());
        }
        return returnResult;
    }
    }
