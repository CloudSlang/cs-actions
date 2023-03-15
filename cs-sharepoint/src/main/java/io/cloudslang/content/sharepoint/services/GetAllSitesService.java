package io.cloudslang.content.sharepoint.services;

import com.google.gson.*;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.sharepoint.utils.Outputs.SITE_IDS;
import static io.cloudslang.content.sharepoint.utils.Outputs.SITE_URLS;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetAllSitesService {
    private static String DISPLAY_NAME = "displayName";
    private static String ID = "id";
    private static String WEB_URL = "webUrl";
    public static void addAllSitesResult(Map<String, String> result){
        JsonObject jsonResponse = JsonParser.parseString(result.get(RETURN_RESULT)).getAsJsonObject();
        JsonArray elementArray = jsonResponse.getAsJsonArray("value");

        // arrays that store the pairs
        JsonArray siteIds = new JsonArray();
        JsonArray siteUrls = new JsonArray();

        for(JsonElement jsonElement : elementArray){
            //create objects to be added in array
            JsonObject siteId = new JsonObject();
            JsonObject siteUrl = new JsonObject();

            //add fields to object and add object to array
            siteId.add(DISPLAY_NAME, jsonElement.getAsJsonObject().get(DISPLAY_NAME) );
            siteId.add(ID, jsonElement.getAsJsonObject().get(ID) );
            siteIds.add(siteId);

            //add fields to object and add object to array
            siteUrl.add(DISPLAY_NAME, jsonElement.getAsJsonObject().get(DISPLAY_NAME) );
            siteUrl.add(WEB_URL, jsonElement.getAsJsonObject().get(WEB_URL) );
            siteUrls.add(siteUrl);
        }
        //put the arrays as strings in the final result
        result.put(SITE_IDS, siteIds.toString());
        result.put(SITE_URLS, siteUrls.toString());

    }
    public static void setFailureCustomResults(Map<String, String> httpResults, String... inputs) {

        for (String input : inputs)
            httpResults.put(input, EMPTY);
    }
}
