package io.cloudslang.content.json.services;

import com.google.gson.*;
import java.util.Map;
import java.util.Set;

public class GetObjectKeysImpl {

    public static String getObjectKeys(String jsonString) {

        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries = jsonObject.entrySet();
        JsonArray keyList = new JsonArray();
        for (Map.Entry<String, JsonElement> entry: entries) {
            keyList.add(entry.getKey());
        }

        return keyList.toString();
    }
}
