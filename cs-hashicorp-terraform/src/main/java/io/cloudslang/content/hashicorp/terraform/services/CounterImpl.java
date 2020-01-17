package io.cloudslang.content.hashicorp.terraform.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.hashicorp.terraform.exceptions.CounterImplException;
import io.cloudslang.content.hashicorp.terraform.utils.CounterProcessor;

import java.util.HashMap;
import java.util.Map;

public class CounterImpl {
    public static Map<String, String> counter(String to, String from, String by, boolean reset, GlobalSessionObject<Map<String, Object>> globalSessionObject) {

        CounterProcessor counter = new CounterProcessor();
        Map<String, String> returnResult = new HashMap<String, String>();
        returnResult.put("result", "failed");

        try {
            counter.init(to,from,by,reset, globalSessionObject);
            if (counter.hasNext()) {
                returnResult.put("index", Integer.toString(counter.getIndex()));
                returnResult.put("resultString", counter.getNext(globalSessionObject));
                if (counter.hasNext()) {
                    returnResult.put("result", "has more");
                    returnResult.put("returnCode", "0");
                } else {
                    returnResult.put("resultString", "");
                    returnResult.put("returnCode", "1");
                    counter.setStepSessionEnd(globalSessionObject);
                    returnResult.put("result", "no more");
                }
            }
        } catch (CounterImplException e) {
            returnResult.put("result", "failed");
            returnResult.put("resultString", e.getMessage());
            returnResult.put("returnCode", "-1");
        }
        return returnResult;
    }







}
