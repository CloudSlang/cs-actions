package io.cloudslang.content.services;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import io.cloudslang.content.exceptions.IteratorProcessorException;
import io.cloudslang.content.utils.IteratorProcessor;

import java.util.HashMap;
import java.util.Map;

public class ListService {
    public static Map<String, String> iterate(String list, String separator, GlobalSessionObject<Map<String, Object>> globalSessionObject) {

        IteratorProcessor iterator = new IteratorProcessor();
        Map<String, String> returnResult = new HashMap<String, String>();
        returnResult.put("result", "Failed");

        try {
            iterator.init(list, separator, globalSessionObject);
            if (iterator.hasNext()) {
                returnResult.put("Index", Integer.toString(iterator.getIndex()));
                returnResult.put("ResultString", iterator.getNext(globalSessionObject));
                if (iterator.hasNext()) {
                    returnResult.put("result", "HasMore");
                } else {
                    returnResult.put("ResultString", "");
                    iterator.setStepSessionEnd(globalSessionObject);
                    returnResult.put("result", "NoMore");
                }
            }
        } catch (IteratorProcessorException e) {
            returnResult.put("result", "Failed");
            returnResult.put("ResultString", e.getMessage());
        }
        return returnResult;
    }
}
