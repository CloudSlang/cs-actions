package io.cloudslang.content.sharepoint.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.sharepoint.utils.Constants.EXCEPTION_SITE_ID;
import static io.cloudslang.content.sharepoint.utils.Constants.NEGATIVE_RETURN_CODE;
import static io.cloudslang.content.sharepoint.utils.Outputs.SITE_ID;

public class SharepointService {
    public static void processHttpGetSideIdByName(Map<String, String> httpResults) {

        //Process the return result output
        String returnResult = httpResults.get(RETURN_RESULT);
        try {
            if (!(returnResult.isEmpty())) {
                httpResults.put(RETURN_RESULT, returnResult);

                JsonObject jsonResponse = JsonParser.parseString(httpResults.get(RETURN_RESULT)).getAsJsonObject();

                try {
                    String namespacePath = "$.value[0].id";
                    String namespacePathResponse = JsonPath.read(jsonResponse.toString(), namespacePath);
                    httpResults.put(SITE_ID, namespacePathResponse);
                } catch (Exception e) {
                    httpResults.put(SITE_ID, EXCEPTION_SITE_ID);
                    httpResults.put(RETURN_CODE,NEGATIVE_RETURN_CODE);
                }
            }
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }
}
