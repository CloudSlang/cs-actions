

package io.cloudslang.content.vmware.utils;

import com.google.gson.Gson;

public class JsonConverter {

    private static Gson gson = new Gson();

    private JsonConverter() {
    }

    public static <T> T convertFromJson(String jsonString, Class<T> classOfT) {
        return gson.fromJson(jsonString, classOfT);
    }
}
