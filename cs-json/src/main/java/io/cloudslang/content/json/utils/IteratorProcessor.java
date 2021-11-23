package io.cloudslang.content.json.utils;

import com.google.gson.*;
import com.hp.oo.sdk.content.plugin.*;
import io.cloudslang.content.utils.*;

import java.util.*;

import static io.cloudslang.content.json.utils.Constants.ArrayIteratorAction.*;
import static io.cloudslang.content.json.utils.ExceptionMsgs.INVALID_JSON_ARRAY;

public class IteratorProcessor {

    private String arrayElement;
    private int index;
    private JsonArray jsonElements;

//    private boolean initialized(GlobalSessionObject<Map<String, Object>> session) {
//        return (session.get().get(INDEX) != null);
//    }

    public void init(String array, GlobalSessionObject<Map<String, Object>> session) throws Exception {

        if (session.get() == null) {
            Map<String, Object> initialIndex = new HashMap<String, Object>();
            initialIndex.put(INDEX, 0);
            session.setResource(new IteratorSessionResource(initialIndex));
        }

        Map<String, Object> sessionMap = session.get();
        if (StringUtilities.isBlank(array)) {
            throw new Exception(String.format(ExceptionMsgs.NULL_OR_EMPTY_INPUT, Constants.InputNames.ARRAY));
        }

        index = Integer.parseInt(String.valueOf(sessionMap.get(INDEX)));

        try {
        jsonElements = new Gson().fromJson(array, JsonArray.class);
        }catch (Exception ex) {
            throw new Exception(INVALID_JSON_ARRAY);
        }
    }

    public boolean hasNext() {
        return index < jsonElements.size();
    }

    public int getIndex() {
        return index;
    }

    public String getNext(GlobalSessionObject<Map<String, Object>> session) {
        if (index < jsonElements.size()) {
            String ret = String.valueOf(jsonElements.get(index));
            index += 1;
            session.get().put(INDEX, index);

            return ret;
        } else {
            this.index += 1;
            session.get().put(INDEX, index);

            return null;
        }
    }

    public void setStepSessionEnd(GlobalSessionObject<Map<String, Object>> session) {
        session.get().put(INDEX, Integer.toString(0));
    }

}
