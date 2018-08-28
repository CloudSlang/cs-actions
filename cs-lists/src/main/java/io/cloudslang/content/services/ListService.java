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
        returnResult.put("result", "failed");

        try {
            iterator.init(list, separator, globalSessionObject);
            if (iterator.hasNext()) {
                returnResult.put("index", Integer.toString(iterator.getIndex()));
                returnResult.put("resultString", iterator.getNext(globalSessionObject));
                if (iterator.hasNext()) {
                    returnResult.put("result", "has more");
                } else {
                    returnResult.put("resultString", "");
                    iterator.setStepSessionEnd(globalSessionObject);
                    returnResult.put("result", "no more");
                }
            }
        } catch (IteratorProcessorException e) {
            returnResult.put("result", "failed");
            returnResult.put("resultString", e.getMessage());
        }
        return returnResult;
    }
}
