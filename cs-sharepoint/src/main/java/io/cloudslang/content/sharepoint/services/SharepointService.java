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
import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.httpclient.actions.HttpClientGetAction;
import io.cloudslang.content.sharepoint.utils.Constants;
import io.cloudslang.content.sharepoint.utils.Descriptions;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.stream.StreamSupport;

import static io.cloudslang.content.constants.BooleanValues.FALSE;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.sharepoint.utils.Constants.*;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetDriveIdByName.NO_DRIVE_FOUND;
import static io.cloudslang.content.sharepoint.utils.Descriptions.GetSiteNameById.EXCEPTION_DESC;
import static io.cloudslang.content.sharepoint.utils.Outputs.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

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
    }

    public static void processHttpGetItemShareLink(Map<String, String> httpResults, String exceptionMessage) throws JsonProcessingException {

        processHttpResult(httpResults, exceptionMessage);

        if (!httpResults.get(STATUS_CODE).equals("200") && !httpResults.get(STATUS_CODE).equals("201"))
            return;

        JsonNode json = new ObjectMapper().readTree(httpResults.get(RETURN_RESULT));

        httpResults.put(SHARE_LINK, json.get(LINK).get(WEB_URL).asText());
        httpResults.put(SHARE_ID, json.get(ID).asText());
    }

    public static Map<String, String> getEntitiesFromDrive(
            String authToken,
            String driveId,
            String path,
            String entitiesType,
            String proxyHost,
            String proxyPort,
            String proxyUsername,
            String proxyPassword,
            String trustAllRoots,
            String x509HostnameVerifier,
            String trustKeystore,
            String trustPassword,
            String tlsVersion,
            String allowedCiphers,
            String connectTimeout,
            String executionTimeout,
            SerializableSessionObject sessionCookies,
            GlobalSessionObject sessionConnectionPool) {

        try {

            String endpoint = path == null || path.isEmpty() ? ROOT_CHILDREN_ENDPOINT : ROOT_PATH_ENDPOINT + path + CHILDREN_PATH_ENDPOINT;

            Map<String, String> result = new HttpClientGetAction().execute(
                    GRAPH_API_ENDPOINT + DRIVES_ENDPOINT + driveId + endpoint,
                    ANONYMOUS,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    proxyHost,
                    proxyPort,
                    proxyUsername,
                    proxyPassword,
                    tlsVersion,
                    allowedCiphers,
                    trustAllRoots,
                    x509HostnameVerifier,
                    trustKeystore,
                    trustPassword,
                    EMPTY,
                    EMPTY,
                    FALSE,
                    CONNECTIONS_MAX_PER_ROUTE_CONST,
                    CONNECTIONS_MAX_TOTAL_CONST,
                    EMPTY,
                    EMPTY,
                    AUTHORIZATION_BEARER + authToken,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    EMPTY,
                    connectTimeout,
                    EMPTY,
                    executionTimeout,
                    sessionCookies,
                    sessionConnectionPool
            );

            processHttpResult(result, Descriptions.GetEntitiesFromDrive.EXCEPTION_DESC);

            if (result.get(RETURN_CODE).equals(NEGATIVE_RETURN_CODE))
                return result;

            JsonObject returnResult = JsonParser.parseString(result.get(RETURN_RESULT)).getAsJsonObject();

            StreamSupport
                    .stream(returnResult.get(VALUE).getAsJsonArray().spliterator(), true)
                    .filter(entity -> entity.getAsJsonObject().has(FOLDER))
                    .forEach(folder -> {

                        Map<String, String> folderResult = getEntitiesFromDrive(
                                authToken,
                                driveId,
                                path + SLASH + folder.getAsJsonObject().get(Constants.NAME).getAsString(),
                                entitiesType,
                                proxyHost,
                                proxyPort,
                                proxyUsername,
                                proxyPassword,
                                trustAllRoots,
                                x509HostnameVerifier,
                                trustKeystore,
                                trustPassword,
                                tlsVersion,
                                allowedCiphers,
                                connectTimeout,
                                executionTimeout,
                                sessionCookies,
                                sessionConnectionPool
                        );

                        try {
                            returnResult
                                    .get(VALUE)
                                    .getAsJsonArray()
                                    .addAll(JsonParser.parseString(folderResult.get(RETURN_RESULT)).getAsJsonObject().get(VALUE).getAsJsonArray());
                        }catch (Exception ignored) {}
                    });

            result.put(RETURN_RESULT, returnResult.toString());

            processHttpGetEntitiesFromDrive(result, Descriptions.GetEntitiesFromDrive.EXCEPTION_DESC, entitiesType);
            return result;
        } catch (Exception exception) {
            return getFailureResultsMap(exception);
        }
    }
    public static class GetAllSitesService {
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

        private static String DISPLAY_NAME = "displayName";
        private static String ID = "id";
        private static String WEB_URL = "webUrl";

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

        public static void setFailureCustomResults(Map<String, String> httpResults, String... inputs) {

            for (String input : inputs)
                httpResults.put(input, EMPTY);
        }
    }

}
