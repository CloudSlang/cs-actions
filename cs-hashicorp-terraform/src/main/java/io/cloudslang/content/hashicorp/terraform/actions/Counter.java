package io.cloudslang.content.hashicorp.terraform.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.StepSerializableSessionObject;
import io.cloudslang.content.hashicorp.terraform.services.CounterImpl;
import io.cloudslang.content.hashicorp.terraform.services.CounterImplException;


import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CounterConstants.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Descriptions.Counter.FAILURE_MESSAGE;


public class Counter {

    public static final String RESULT_DBG_INDEX = "index";

    @Action(name = OPERATION_NAME,
            outputs = {
                    @Output(RESULT_TEXT),
                    @Output(RESULT)},
            responses = {
                    @Response(text = HASMORE, field = RESULT, value = "has more", matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = NOMORE, field = RESULT, value = "no more", matchType = MatchType.COMPARE_EQUAL),
                    @Response(text = FAILURE, field = RESULT, value = "failure", matchType = MatchType.COMPARE_EQUAL, isDefault = true, isOnFail = true)})
    public Map<String, String> execute(@Param("from") String from, @Param("to") String to, @Param("incrementBy") String incrementBy, @Param("reset") boolean reset,
                                       @Param("sessionCounter") StepSerializableSessionObject sessionCounter) {
        CounterImpl counter = new CounterImpl();
        Map<String, String> returnResult = new HashMap<String, String>();
        returnResult.put(RESULT, FAILURE_MESSAGE);

        try {
            counter.init(to, from, incrementBy, reset, sessionCounter);
            if (counter.hasNext()) {
                returnResult.put(RESULT_DBG_INDEX, Long.toString(counter.getIndex()));
                returnResult.put(RESULT_TEXT, counter.getNext(sessionCounter));
                returnResult.put(RESULT, HASMORE);
            } else {
                counter.setStepSessionEnd(sessionCounter);
                returnResult.put(RESULT_TEXT, "");
                returnResult.put(RESULT, NOMORE);
            }
        } catch (CounterImplException e) {
            returnResult.put(EXCEPTION, e.toString());
        }
        return returnResult;
    }
    }
