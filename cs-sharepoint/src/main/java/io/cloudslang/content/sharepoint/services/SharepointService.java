package io.cloudslang.content.sharepoint.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.sharepoint.utils.Constants.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetSiteNameById.EXCEPTION_DESC;
import static io.cloudslang.content.sharepoint.utils.Outputs.*;

public class SharepointService {

    public static void processHttpResult(Map<String, String> httpResults, String exceptionMessage) {

        String statusCode = httpResults.get(STATUS_CODE);

        if (StringUtils.isEmpty(statusCode) || Integer.parseInt(statusCode) < 200 || Integer.parseInt(statusCode) >= 300) {
            if (StringUtils.isEmpty(httpResults.get(EXCEPTION))) {
                httpResults.put(EXCEPTION, httpResults.get(RETURN_RESULT));
                httpResults.put(RETURN_RESULT, exceptionMessage);
            }
            httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
        }
    }

    public static void processHttpGetSiteIdByName(Map<String, String> httpResults) {

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

    public static void processHttpGetSiteNameById(Map<String, String> httpResults) {

        //Process the return result output
        String returnResult = httpResults.get(RETURN_RESULT);
        try {
            if (!(returnResult.isEmpty())) {
                JsonObject jsonResponse = JsonParser.parseString(httpResults.get(RETURN_RESULT)).getAsJsonObject();

                try {
                    String displayNamePath = "$.displayName";
                    String displayNamePathResponse = JsonPath.read(jsonResponse.toString(), displayNamePath);
                    httpResults.put(SITE_DISPLAY_NAME, displayNamePathResponse);

                    String namePath = "$.name";
                    String namePathResponse = JsonPath.read(jsonResponse.toString(), namePath);
                    httpResults.put(SITE_NAME, namePathResponse);
                } catch (Exception e) {
                    httpResults.put(SITE_NAME, EXCEPTION_DESC);
                    httpResults.put(RETURN_CODE,NEGATIVE_RETURN_CODE);
                }
            }
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void processHttpGetRootDrive(Map<String, String> httpResults, String exceptionMessage) throws JsonProcessingException {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("200"))
            return;

        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));

        httpResults.put(WEB_URL, json.get(WEB_URL).asText());
        httpResults.put(DRIVE_NAME, json.get(NAME).asText());
        httpResults.put(DRIVE_TYPE, json.get(PARENT_REFERENCE).get(DRIVE_TYPE).asText());
        httpResults.put(DRIVE_ID, json.get(PARENT_REFERENCE).get(DRIVE_ID).asText());
    }
}
