/*
 * (c) Copyright 2023 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.cloudslang.content.sharepoint.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.sharepoint.utils.Descriptions;
import io.cloudslang.content.sharepoint.utils.Utils;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.httpclient.utils.Constants.COLON;
import static io.cloudslang.content.sharepoint.utils.Constants.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.CreateFolder.HOST_EXCEPTION_DESC;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetDriveIdByName.NO_DRIVE_FOUND;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetSiteNameById.EXCEPTION_DESC;
import static io.cloudslang.content.sharepoint.utils.Outputs.*;
import static io.cloudslang.content.sharepoint.utils.Utils.getFirstAvailableFileName;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SharepointService {

    public static String encodeFileName(String fileName) throws URISyntaxException {
        return new URI(null, null, fileName, null).toASCIIString();
    }

    public static void setFailureCustomResults(Map<String, String> httpResults, String... inputs) {

        for (String input : inputs)
            httpResults.put(input, EMPTY);
    }

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
                    httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
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
                    httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
                }
            }
        } catch (
                Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void processHttpGetRootSite(Map<String, String> result, String exceptionMessage) {
        processHttpResult(result, exceptionMessage);
        if (!exceptionMessage.equals(Descriptions.GetRootSite.EXCEPTION_DESC)) {
            JSONObject jsonObject = (JSONObject) JSONValue.parse(result.get(RETURN_RESULT));
            result.put(SITE_ID, jsonObject.getAsString("id"));
            result.put(SITE_NAME, jsonObject.getAsString("name"));
            result.put(SITE_DISPLAY_NAME, jsonObject.getAsString("displayName"));
            result.put(WEB_URL, jsonObject.getAsString("webUrl"));
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

    public static void processHttpDownloadFile(Map<String, String> httpResults, String exceptionMessage, String path, boolean overwrite) throws IOException {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("200"))
            return;

        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));

        httpResults.put(SIZE, json.get(SIZE).asText());
        httpResults.put(CREATED_DATE_TIME, json.get(FILE_SYSTEM_INFO).get(CREATED_DATE_TIME).asText());
        httpResults.put(LAST_MODIFIED_DATE_TIME, json.get(FILE_SYSTEM_INFO).get(LAST_MODIFIED_DATE_TIME).asText());
        httpResults.put(LAST_MODIFIED_BY, json.get(LAST_MODIFIED_BY).get(USER).get(DISPLAY_NAME).asText());
        httpResults.put(FILE_TYPE, json.get(FILE).get(MIME_TYPE).asText());
        httpResults.put(FILE_NAME, json.get(NAME).asText());

        String downloadUrl = json.get(MICROSOFT_GRAPH_DOWNLOAD_URL).asText();
        String fileName = path + "\\" + json.get(NAME).asText();

        if (!overwrite)
            fileName = getFirstAvailableFileName(fileName);

        FileUtils.copyURLToFile(new URL(downloadUrl), new File(fileName));
    }

    public static void processHttpGetAllDrives(Map<String, String> httpResults, String exceptionMessage) throws JsonProcessingException {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("200"))
            return;

        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));

        JsonArray driveIds = new JsonArray();
        JsonArray driveUrls = new JsonArray();

        StreamSupport
                .stream(json.get(VALUE).spliterator(), true)
                .forEach(drive -> {

                    JsonObject driveId = new JsonObject();
                    driveId.addProperty(NAME, drive.get(NAME).asText());
                    driveId.addProperty(ID, drive.get(ID).asText());
                    driveIds.add(driveId);

                    JsonObject driveUrl = new JsonObject();
                    driveUrl.addProperty(NAME, drive.get(NAME).asText());
                    driveUrl.addProperty(WEB_URL, drive.get(WEB_URL).asText());
                    driveUrls.add(driveUrl);
                });

        httpResults.put(DRIVE_IDS, driveIds.toString());
        httpResults.put(DRIVE_URLS, driveUrls.toString());
    }

    public static void processHttpGetSiteDetails(Map<String, String> httpResults, String exceptionMessage) throws JsonProcessingException {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("200"))
            return;

        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));

        httpResults.put(WEB_URL, json.get(WEB_URL).asText());
        httpResults.put(SITE_ID, json.get(ID).asText());
        httpResults.put(SITE_NAME, json.get(NAME).asText());
        httpResults.put(SITE_DISPLAY_NAME, json.get(DISPLAY_NAME).asText());
    }

    public static void processHttpGetDriveNameById(Map<String, String> httpResults, String exceptionMessage) throws JsonProcessingException {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("200"))
            return;

        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));
        httpResults.put(DRIVE_NAME, json.get(NAME).asText());
    }

    public static void processHttpGetDriveIdByName(Map<String, String> httpResults, String driveName, String exceptionMessage) {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("200"))
            return;

        JsonObject jsonResponse = JsonParser.parseString(httpResults.get(RETURN_RESULT)).getAsJsonObject();
        JsonArray elementArray = jsonResponse.getAsJsonArray("value");
        for (JsonElement jsonElement : elementArray)
            if (jsonElement.getAsJsonObject().get("name").getAsString().equals(driveName)) {
                httpResults.put(DRIVE_ID, jsonElement.getAsJsonObject().get("id").getAsString());
                return;
            }
        httpResults.put(RETURN_RESULT, NO_DRIVE_FOUND);
        httpResults.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
    }

    public static void processHttpGetEntitiesFromDrive(Map<String, String> httpResults, String exceptionMessage, String entitiesType) throws JsonProcessingException {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("200"))
            return;

        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));

        JsonArray entityIds = new JsonArray();
        JsonArray entityUrls = new JsonArray();
        JsonArray entityTypes = new JsonArray();
        JsonArray entityPaths = new JsonArray();

        StreamSupport
                .stream(json.get(VALUE).spliterator(), true)
                .filter(entity -> {

                    if (entitiesType.equals(FILES))
                        return entity.has(FILE);

                    if (entitiesType.equals(FOLDERS))
                        return entity.has(FOLDER);

                    return true;
                })
                .forEach(entity -> {

                    JsonObject entityId = new JsonObject();
                    entityId.addProperty(NAME, entity.get(NAME).asText());
                    entityId.addProperty(ID, entity.get(ID).asText());
                    entityIds.add(entityId);

                    JsonObject entityUrl = new JsonObject();
                    entityUrl.addProperty(NAME, entity.get(NAME).asText());
                    entityUrl.addProperty(WEB_URL, entity.get(WEB_URL).asText());
                    entityUrls.add(entityUrl);

                    JsonObject entityPath = new JsonObject();
                    entityPath.addProperty(NAME, entity.get(NAME).asText());
                    String path = entity.get(PARENT_REFERENCE).get(PATH).asText();
                    entityPath.addProperty(PATH, path.substring(path.indexOf(COLON) + 1));
                    entityPaths.add(entityPath);

                    JsonObject entityType = new JsonObject();
                    entityType.addProperty(NAME, entity.get(NAME).asText());
                    if (entity.has(FOLDER))
                        entityType.addProperty(TYPE, FOLDER);
                    else
                        entityType.addProperty(TYPE, entity.get(FILE).get(MIME_TYPE).asText());
                    entityTypes.add(entityType);
                });

        httpResults.put(ENTITY_IDS, entityIds.toString());
        httpResults.put(ENTITY_URLS, entityUrls.toString());
        httpResults.put(ENTITY_TYPES, entityTypes.toString());
        httpResults.put(ENTITY_PATHS, entityPaths.toString());
    }

    public static void processHttpGetEntityShareLink(Map<String, String> httpResults, String exceptionMessage) throws JsonProcessingException {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("200") && !httpResults.get(STATUS_CODE).equals("201"))
            return;

        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));

        httpResults.put(SHARE_LINK, json.get(LINK).get(WEB_URL).asText());
        httpResults.put(SHARE_ID, json.get(ID).asText());
    }

    public static void processHttpGetEntityIdByName(Map<String, String> httpResults, String exceptionMessage) throws JsonProcessingException {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("200") && !httpResults.get(STATUS_CODE).equals("201"))
            return;

        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));

        httpResults.put(SHARE_LINK, json.get(LINK).get(WEB_URL).asText());
        httpResults.put(SHARE_ID, json.get(ID).asText());
    }

    public static void processHttpAllSites(Map<String, String> result) {
        processHttpResult(result, Descriptions.GetAllSites.EXCEPTION_DESC);

        if (Integer.parseInt(result.get(RETURN_CODE)) != -1) {
            if (Integer.parseInt(result.get(STATUS_CODE)) >= 200 && Integer.parseInt(result.get(STATUS_CODE)) < 300)
                addAllSitesResult(result);
            else {
                setFailureCustomResults(result, SITE_IDS, SITE_URLS);
                result.put(RETURN_CODE, NEGATIVE_RETURN_CODE);
                result.put(EXCEPTION, result.get(RETURN_RESULT));
                result.put(RETURN_RESULT, Descriptions.GetRootSite.EXCEPTION_DESC);
            }

        } else
            setFailureCustomResults(result, SITE_IDS, SITE_URLS);
    }

    public static void addAllSitesResult(Map<String, String> result) {
        JsonObject jsonResponse = JsonParser.parseString(result.get(RETURN_RESULT)).getAsJsonObject();
        JsonArray elementArray = jsonResponse.getAsJsonArray("value");

        // arrays that store the pairs
        JsonArray siteIds = new JsonArray();
        JsonArray siteUrls = new JsonArray();

        for (JsonElement jsonElement : elementArray) {
            //create objects to be added in array
            JsonObject siteId = new JsonObject();
            JsonObject siteUrl = new JsonObject();

            //add fields to object and add object to array
            siteId.add(DISPLAY_NAME, jsonElement.getAsJsonObject().get(DISPLAY_NAME));
            siteId.add(ID, jsonElement.getAsJsonObject().get(ID));
            siteIds.add(siteId);

            //add fields to object and add object to array
            siteUrl.add(DISPLAY_NAME, jsonElement.getAsJsonObject().get(DISPLAY_NAME));
            siteUrl.add(WEB_URL, jsonElement.getAsJsonObject().get(WEB_URL));
            siteUrls.add(siteUrl);
        }
        //put the arrays as strings in the final result
        result.put(SITE_IDS, siteIds.toString());
        result.put(SITE_URLS, siteUrls.toString());
    }

    public static void processHttpResultUploadFile(Map<String, String> httpResults, String exceptionMessage) throws JsonProcessingException {

        processHttpResult(httpResults, exceptionMessage);

        if (StringUtils.isEmpty(httpResults.get(STATUS_CODE)) || Integer.parseInt(httpResults.get(STATUS_CODE)) < 200 || Integer.parseInt(httpResults.get(STATUS_CODE)) >= 300)
            return;
        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));
        httpResults.put(FILE_ID, json.get(ID).asText());
        httpResults.put(WEB_URL, json.get(WEB_URL).asText());
    }

    public static String populateEndpointUploadFile(@NotNull final String siteId,
                                                    @NotNull final String driveId,
                                                    @NotNull final String folderId,
                                                    @NotNull final String fileName) throws URISyntaxException {
        StringBuilder endpoint = new StringBuilder(GRAPH_API_ENDPOINT);
        if (!siteId.isEmpty())
            endpoint.append(SITES_ENDPOINT).append(siteId);
        endpoint.append(!driveId.isEmpty() ? DRIVES_ENDPOINT + driveId : DRIVE_ENDPOINT).append(ITEMS_ENDPOINT);
        endpoint.append(!folderId.isEmpty() ? folderId + PATH_ENDPOINT : ROOT_PATH_ENDPOINT_2);
        endpoint.append(encodeFileName(fileName)).append(CONTENT_ENDPOINT);
        return endpoint.toString();
    }

    public static String processHost(List<String> ids, String parentItemId) throws Utils.HostException {
        boolean found = false;
        for(String id:ids){
            if (!id.isEmpty()){
                if(!found)
                    found=true;
                else
                    throw new Utils.HostException(HOST_EXCEPTION_DESC);
            }
        }

        // case when no ids were provided
        int pos = ids.size();

        for (String id : ids) {
            if (!id.isEmpty()) {
                // when an input was found, store it and leave
                pos = ids.indexOf(id);
                break;
            }
        }
        // create the list of endpoints
        List<String> endpoints = Arrays.asList(DRIVES_ENDPOINT, GROUPS_ENDPOINT, SITES_ENDPOINT, USERS_ENDPOINT, ME_ENDPOINT);

        // start building the host
        StringBuilder hostBuilder = new StringBuilder(GRAPH_API_ENDPOINT);

        // append the corresponding parts of the host
        hostBuilder.append(endpoints.get(pos));

        // if all ids are empty, no id to append
        if (pos < ids.size())
            hostBuilder.append(ids.get(pos));

        hostBuilder.append(pos == 0 ? ITEMS_ENDPOINT : DRIVE_ITEMS_ENDPOINT);

        // append common part
        hostBuilder.append(parentItemId).append(CHILDREN_PATH_ENDPOINT);

        return hostBuilder.toString();

    }

    public static void processHttpCreateFolder(Map<String, String> httpResults, String exceptionMessage) throws JsonProcessingException {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("201"))
            return;

        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));

        httpResults.put(WEB_URL, json.get(WEB_URL).asText());
        httpResults.put(ID, json.get(ID).asText());
    }

}