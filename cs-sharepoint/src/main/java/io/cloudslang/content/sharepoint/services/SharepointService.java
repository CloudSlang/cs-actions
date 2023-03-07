package io.cloudslang.content.sharepoint.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.sharepoint.utils.Constants.ID;

public class SharepointService {
    public static void processHttpGetSideIdByName(Map<String, String> httpResults) {

        //Process the return result output
        String returnResult = httpResults.get(RETURN_RESULT);
        try {
            if (!(returnResult.isEmpty())) {
                httpResults.put(RETURN_RESULT, returnResult);

                JsonObject jsonResponse = JsonParser.parseString(httpResults.get(RETURN_RESULT)).getAsJsonObject();

                //Kind output
                JsonPrimitive tmpResponse = (JsonPrimitive) jsonResponse.get(ID);
                httpResults.put(RETURN_RESULT, tmpResponse.toString());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
