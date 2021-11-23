package io.cloudslang.content.json.services;

import com.hp.oo.sdk.content.plugin.*;
import io.cloudslang.content.json.utils.*;

import java.util.*;

import static io.cloudslang.content.json.utils.Constants.ArrayIteratorAction.INDEX;
import static io.cloudslang.content.json.utils.Constants.ArrayIteratorAction.RESULT_TEXT;

public class ArrayListService {
    public static Map<String, String> iterate(String array, GlobalSessionObject<Map<String, Object>> globalSessionObject){
        IteratorProcessor iterate = new IteratorProcessor();
        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(RESULT_TEXT, "failed");

        try {
            iterate.init(array,  globalSessionObject);
            if (iterate.hasNext()) {
                returnResult.put(INDEX, Integer.toString(iterate.getIndex()));
                returnResult.put("resultString", iterate.getNext(globalSessionObject));
                if (iterate.hasNext()) {
                    returnResult.put(RESULT_TEXT, "has more");
                    returnResult.put("returnCode", "0");
                } else {
                    returnResult.put("resultString", "");
                    returnResult.put("returnCode", "1");
                    iterate.setStepSessionEnd(globalSessionObject);
                    returnResult.put(RESULT_TEXT, "no more");
                }
            }
        } catch (Exception e) {
            returnResult.put(RESULT_TEXT, "failed");
            returnResult.put("resultString", e.getMessage());
            returnResult.put("returnCode", "-1");
        }
        return returnResult;
    }
}
