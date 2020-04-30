/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.hashicorp.terraform.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformVariableInputs;
import io.cloudslang.content.hashicorp.terraform.services.models.variables.CreateVariableRequestBody;
import io.cloudslang.content.hashicorp.terraform.services.models.variables.UpdateVariableRequestBody;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.utils.StringUtilities;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateVariableConstants.VARIABLE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateVariableConstants.VARIABLE_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspaceConstants.WORKSPACE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getUriBuilder;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class WorkspaceVariableImpl {

    @NotNull
    public static Map<String, String> createVariable(@NotNull TerraformVariableInputs terraformVariableInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = terraformVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(createVariableUrl(terraformVariableInputs));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        if (commonInputs.getRequestBody().isEmpty() & terraformVariableInputs.getSensitiveVariableRequestBody().isEmpty()) {
            httpClientInputs.setBody(createVariableRequestBody(terraformVariableInputs));
        } else if (!terraformVariableInputs.getSensitiveVariableRequestBody().isEmpty()) {
            httpClientInputs.setBody(terraformVariableInputs.getSensitiveVariableRequestBody());
            httpClientInputs.setUrl(createVariableUrl(terraformVariableInputs));
        } else {
            httpClientInputs.setBody(commonInputs.getRequestBody());
        }
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());

        return new HttpClientService().execute(httpClientInputs);
    }


    @NotNull
    public static Map<String, Map<String, String>> createVariables(@NotNull TerraformVariableInputs terraformVariableInputs) throws Exception {
        String variableName;
        String variableValue;
        String hcl;
        String catagory;
        Map<String, Map<String, String>> createVariableMap = new HashMap<>();
        JSONParser parser = new JSONParser();
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = terraformVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(createVariableUrl(terraformVariableInputs));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        if (!terraformVariableInputs.getVariableJson().isEmpty() & !terraformVariableInputs.getSensitiveVariableJson().isEmpty()) {
            String sensitiveVariableValue;

            JSONArray createVariableJsonArray = (JSONArray) parser.parse(terraformVariableInputs.getVariableJson());
            JSONArray createSensitiveVariableJsonArray = (JSONArray) parser.parse(terraformVariableInputs.getSensitiveVariableJson());
            JSONObject createVariableJson;
            JSONObject createSensitiveVariableJson;
            for (int i = 0; i < createVariableJsonArray.size(); i++) {
                createVariableJson = (JSONObject) createVariableJsonArray.get(i);
                variableName = (String) ((JSONObject) ((JSONObject) createVariableJson.get("data")).get("attributes")).get("propertyName");
                variableValue = (String) ((JSONObject) ((JSONObject) createVariableJson.get("data")).get("attributes")).get("propertyValue");
                hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) createVariableJson.get("data")).get("attributes")).get("HCL"));
                catagory = (String) ((JSONObject) ((JSONObject) createVariableJson.get("data")).get("attributes")).get("Category");


                terraformVariableInputs = TerraformVariableInputs.builder()
                        .variableName(variableName)
                        .variableValue(variableValue)
                        .variableCategory(catagory)
                        .hcl(hcl)
                        .workspaceId(terraformVariableInputs.getWorkspaceId())
                        .sensitive("false").build();
                httpClientInputs.setBody(createVariableRequestBody(terraformVariableInputs));

                createVariableMap.put(variableName, new HttpClientService().execute(httpClientInputs));

            }
            for (int i = 0; i < createSensitiveVariableJsonArray.size(); i++) {
                createSensitiveVariableJson = (JSONObject) createSensitiveVariableJsonArray.get(i);
                variableName = (String) ((JSONObject) ((JSONObject) createSensitiveVariableJson.get("data")).get("attributes")).get("propertyName");
                sensitiveVariableValue = (String) ((JSONObject) ((JSONObject) createSensitiveVariableJson.get("data")).get("attributes")).get("propertyValue");
                hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) createSensitiveVariableJson.get("data")).get("attributes")).get("HCL"));
                catagory = (String) ((JSONObject) ((JSONObject) createSensitiveVariableJson.get("data")).get("attributes")).get("Category");


                terraformVariableInputs = TerraformVariableInputs.builder()
                        .variableName(variableName)
                        .sensitiveVariableValue(sensitiveVariableValue)
                        .variableValue(EMPTY)
                        .variableCategory(catagory)
                        .hcl(hcl)
                        .workspaceId(terraformVariableInputs.getWorkspaceId())
                        .sensitive("true").build();
                httpClientInputs.setBody(createVariableRequestBody(terraformVariableInputs));
                createVariableMap.put(variableName, new HttpClientService().execute(httpClientInputs));

            }
            return createVariableMap;

        } else if (!terraformVariableInputs.getVariableJson().isEmpty()) {
            JSONArray createVariableJsonArray = (JSONArray) parser.parse(terraformVariableInputs.getVariableJson());
            JSONObject createVariableJson;
            for (int i = 0; i < createVariableJsonArray.size(); i++) {
                createVariableJson = (JSONObject) createVariableJsonArray.get(i);
                variableName = (String) ((JSONObject) ((JSONObject) createVariableJson.get("data")).get("attributes")).get("propertyName");
                variableValue = (String) ((JSONObject) ((JSONObject) createVariableJson.get("data")).get("attributes")).get("propertyValue");
                hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) createVariableJson.get("data")).get("attributes")).get("HCL"));
                catagory = (String) ((JSONObject) ((JSONObject) createVariableJson.get("data")).get("attributes")).get("Category");


                terraformVariableInputs = TerraformVariableInputs.builder()
                        .variableName(variableName)
                        .variableValue(variableValue)
                        .variableCategory(catagory)
                        .hcl(hcl)
                        .workspaceId(terraformVariableInputs.getWorkspaceId())
                        .sensitive("false").build();
                httpClientInputs.setBody(createVariableRequestBody(terraformVariableInputs));
                createVariableMap.put(variableName, new HttpClientService().execute(httpClientInputs));

            }

            return createVariableMap;
        } else {
            JSONArray createSensitiveVariableJsonArray = (JSONArray) parser.parse(terraformVariableInputs.getSensitiveVariableJson());
            JSONObject createSensitiveVariableJson;
            for (int i = 0; i < createSensitiveVariableJsonArray.size(); i++) {
                createSensitiveVariableJson = (JSONObject) createSensitiveVariableJsonArray.get(i);
                variableName = (String) ((JSONObject) ((JSONObject) createSensitiveVariableJson.get("data")).get("attributes")).get("propertyName");
                variableValue = (String) ((JSONObject) ((JSONObject) createSensitiveVariableJson.get("data")).get("attributes")).get("propertyValue");
                hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) createSensitiveVariableJson.get("data")).get("attributes")).get("HCL"));
                catagory = (String) ((JSONObject) ((JSONObject) createSensitiveVariableJson.get("data")).get("attributes")).get("Category");


                terraformVariableInputs = TerraformVariableInputs.builder()

                        .variableName(variableName)
                        .sensitiveVariableValue(variableValue)
                        .variableValue(EMPTY)
                        .variableCategory(catagory)
                        .hcl(hcl)
                        .workspaceId(terraformVariableInputs.getWorkspaceId())
                        .sensitive("true").build();
                httpClientInputs.setBody(createVariableRequestBody(terraformVariableInputs));
                createVariableMap.put(variableName, new HttpClientService().execute(httpClientInputs));

            }

            return createVariableMap;
        }


    }

    @NotNull
    public static Map<String, Map<String, String>> updateVariables(@NotNull TerraformVariableInputs terraformVariableInputs) throws Exception {
        String variableName;
        String variableValue;
        String sensitiveVariableValue;
        String variableId;
        String hcl = null;
        String sensitive = null;
        Map<String, Map<String, String>> updateVariableMap = new HashMap<>();
        Map<String, String> listVariablesResult = listVariables(terraformVariableInputs);
        JSONParser parser = new JSONParser();
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = terraformVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(createVariableUrl(terraformVariableInputs));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PATCH);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        if (!terraformVariableInputs.getVariableJson().isEmpty() & !terraformVariableInputs.getSensitiveVariableJson().isEmpty()) {

            JSONArray updateVariableJsonArray = (JSONArray) parser.parse(terraformVariableInputs.getVariableJson());
            JSONArray updateSensitiveVariableJsonArray = (JSONArray) parser.parse(terraformVariableInputs.getSensitiveVariableJson());
            JSONObject updateVariableJson;
            JSONObject updateSensitiveVariableJson;
            for (int i = 0; i < updateVariableJsonArray.size(); i++) {
                updateVariableJson = (JSONObject) updateVariableJsonArray.get(i);
                variableName = (String) ((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).get("propertyName");
                variableValue = (String) ((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).get("propertyValue");
                variableId = (String) (((JSONObject) updateVariableJson.get("data")).get("id"));
                if (((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).containsKey("HCL")) {
                    hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).get("HCL"));
                }
                if (((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).containsKey("sensitive")) {
                    sensitive = Boolean.toString((boolean) ((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).get("sensitive"));
                }
                JSONObject variableJsonObj = (JSONObject) parser.parse(listVariablesResult.get("returnResult"));
                JSONArray variableJsonArray = (JSONArray) variableJsonObj.get("data");
                for (int j = 0; j < variableJsonArray.size(); j++) {
                    final String variableID = JsonPath.read(variableJsonArray.get(j), "id");
                    if (variableId.equals(variableID)) {
                        terraformVariableInputs = TerraformVariableInputs.builder()
                                .variableName(variableName)
                                .variableValue(variableValue)
                                .hcl(hcl)
                                .sensitive(sensitive)
                                .variableId(variableId)
                                .workspaceId(terraformVariableInputs.getWorkspaceId())
                                .build();
                        httpClientInputs.setBody(updateVariableRequestBody(terraformVariableInputs));
                        httpClientInputs.setUrl(updateVariableUrl(terraformVariableInputs));
                        updateVariableMap.put(variableName, new HttpClientService().execute(httpClientInputs));
                    }
                }
            }
            for (int i = 0; i < updateSensitiveVariableJsonArray.size(); i++) {
                updateSensitiveVariableJson = (JSONObject) updateSensitiveVariableJsonArray.get(i);
                variableName = (String) updateSensitiveVariableJson.get("propertyName");
                sensitiveVariableValue = (String) updateSensitiveVariableJson.get("propertyValue");
                variableId = (String) (((JSONObject) updateSensitiveVariableJson.get("data")).get("id"));
                JSONObject sensitiveVariableJsonObj = (JSONObject) parser.parse(listVariablesResult.get("returnResult"));
                JSONArray sensitiveVariableJsonArray = (JSONArray) sensitiveVariableJsonObj.get("data");
                for (int j = 0; j < sensitiveVariableJsonArray.size(); j++) {
                    final String variableID = JsonPath.read(sensitiveVariableJsonArray.get(j), "id");
                    if (variableId.equals(variableID)) {
                        terraformVariableInputs = TerraformVariableInputs.builder()
                                .variableName(variableName)
                                .variableValue(EMPTY)
                                .sensitiveVariableValue(sensitiveVariableValue)
                                .variableId(variableId)
                                .workspaceId(terraformVariableInputs.getWorkspaceId())
                                .build();
                        httpClientInputs.setBody(updateVariableRequestBody(terraformVariableInputs));
                        httpClientInputs.setUrl(updateVariableUrl(terraformVariableInputs));
                        updateVariableMap.put(variableName, new HttpClientService().execute(httpClientInputs));
                    }
                }
            }
            return updateVariableMap;
        } else if (!terraformVariableInputs.getVariableJson().isEmpty()) {
            JSONArray updateVariableJsonArray = (JSONArray) parser.parse(terraformVariableInputs.getVariableJson());
            JSONObject updateVariableJson;
            for (int i = 0; i < updateVariableJsonArray.size(); i++) {
                updateVariableJson = (JSONObject) updateVariableJsonArray.get(i);
                variableName = (String) ((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).get("propertyName");
                variableValue = (String) ((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).get("propertyValue");
                variableId = (String) (((JSONObject) updateVariableJson.get("data")).get("id"));
                if (((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).containsKey("HCL")) {
                    hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).get("HCL"));
                }
                if (((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).containsKey("sensitive")) {
                    sensitive = Boolean.toString((boolean) ((JSONObject) ((JSONObject) updateVariableJson.get("data")).get("attributes")).get("sensitive"));
                }
                JSONObject variableJsonObj = (JSONObject) parser.parse(listVariablesResult.get("returnResult"));
                JSONArray variableJsonArray = (JSONArray) variableJsonObj.get("data");
                for (int j = 0; j < variableJsonArray.size(); j++) {
                    final String variableID = JsonPath.read(variableJsonArray.get(j), "id");
                    if (variableId.equals(variableID)) {
                        terraformVariableInputs = TerraformVariableInputs.builder()
                                .variableName(variableName)
                                .variableValue(variableValue)
                                .hcl(hcl)
                                .sensitive(sensitive)
                                .variableId(variableId)
                                .workspaceId(terraformVariableInputs.getWorkspaceId())
                                .build();
                        httpClientInputs.setBody(updateVariableRequestBody(terraformVariableInputs));
                        httpClientInputs.setUrl(updateVariableUrl(terraformVariableInputs));
                        updateVariableMap.put(variableName, new HttpClientService().execute(httpClientInputs));
                    }
                }
            }
            return updateVariableMap;
        } else {
            JSONArray updateSensitiveVariableJsonArray = (JSONArray) parser.parse(terraformVariableInputs.getSensitiveVariableJson());
            JSONObject updateSensitiveVariableJson;
            for (int i = 0; i < updateSensitiveVariableJsonArray.size(); i++) {
                updateSensitiveVariableJson = (JSONObject) updateSensitiveVariableJsonArray.get(i);
                variableName = (String) ((JSONObject) ((JSONObject) updateSensitiveVariableJson.get("data")).get("attributes")).get("propertyName");
                sensitiveVariableValue = (String) ((JSONObject) ((JSONObject) updateSensitiveVariableJson.get("data")).get("attributes")).get("propertyValue");
                variableId = (String) (((JSONObject) updateSensitiveVariableJson.get("data")).get("id"));
                JSONObject sensitiveVariableJsonObj = (JSONObject) parser.parse(listVariablesResult.get("returnResult"));
                JSONArray sensitiveVariableJsonArray = (JSONArray) sensitiveVariableJsonObj.get("data");
                for (int j = 0; j < sensitiveVariableJsonArray.size(); j++) {
                    final String variableID = JsonPath.read(updateSensitiveVariableJson.get(j), "id");
                    if (variableId.equals(variableID)) {
                        terraformVariableInputs = TerraformVariableInputs.builder()
                                .variableName(variableName)
                                .variableValue(EMPTY)
                                .sensitiveVariableValue(sensitiveVariableValue)
                                .variableId(variableId)
                                .workspaceId(terraformVariableInputs.getWorkspaceId())
                                .build();
                        httpClientInputs.setBody(updateVariableRequestBody(terraformVariableInputs));
                        httpClientInputs.setUrl(updateVariableUrl(terraformVariableInputs));
                        updateVariableMap.put(variableName, new HttpClientService().execute(httpClientInputs));
                    }
                }
            }

            return updateVariableMap;
        }


    }

    @NotNull
    public static Map<String, String> getVariablesOperationOutput(String variablesJson, String sensitiveVariablesJson, Map<String, Map<String, String>> result) {
        try {
            final Map<String, String> results = new HashMap<>();
            JSONParser parser = new JSONParser();
            if (!variablesJson.isEmpty() & !sensitiveVariablesJson.isEmpty()) {
                JSONArray createVariableJsonArray = (JSONArray) parser.parse(variablesJson);
                JSONArray createSensitiveVariableJsonArray = (JSONArray) parser.parse(sensitiveVariablesJson);
                JSONObject createVariableJson;
                JSONObject createSensitiveVariableJson;
                String variableName = EMPTY;
                String sensitiveVariableName = EMPTY;

                for (int i = 0; i < createVariableJsonArray.size(); i++) {
                    createVariableJson = (JSONObject) createVariableJsonArray.get(i);
                    variableName = (String) ((JSONObject) ((JSONObject) createVariableJson.get("data")).get("attributes")).get("propertyName");


                    for (String variableResult : result.keySet()) {

                        if (variableName.equals(variableResult)) {

                            results.put(RETURN_CODE, result.get(variableResult).get(RETURN_CODE));
                            results.put(variableName, result.get(variableResult).get("returnResult"));
                            results.put(STATUS_CODE, result.get(variableResult).get(STATUS_CODE));
                        }
                    }
                }
                for (int i = 0; i < createSensitiveVariableJsonArray.size(); i++) {
                    createSensitiveVariableJson = (JSONObject) createSensitiveVariableJsonArray.get(i);
                    sensitiveVariableName = (String) ((JSONObject) ((JSONObject) createSensitiveVariableJson.get("data")).get("attributes")).get("propertyName");


                    for (String variableResult : result.keySet()) {

                        if(sensitiveVariableName.equals(variableResult)) {

                            results.put(RETURN_CODE, result.get(variableResult).get(RETURN_CODE));
                            results.put(sensitiveVariableName, result.get(variableResult).get("returnResult"));
                            results.put(STATUS_CODE, result.get(variableResult).get(STATUS_CODE));
                        }
                    }
                }
                return results;

            } else if (!sensitiveVariablesJson.isEmpty()) {
                JSONArray createSensitiveVariableJsonArray = (JSONArray) parser.parse(sensitiveVariablesJson);
                JSONObject createSensitiveVariableJson;
                String sensitiveVariableName = EMPTY;
                for (int i = 0; i < createSensitiveVariableJsonArray.size(); i++) {
                    createSensitiveVariableJson = (JSONObject) createSensitiveVariableJsonArray.get(i);
                    sensitiveVariableName = (String) ((JSONObject) ((JSONObject) createSensitiveVariableJson.get("data")).get("attributes")).get("propertyName");


                    for (String variableResult : result.keySet()) {

                        if(sensitiveVariableName.equals(variableResult)) {

                            results.put(RETURN_CODE, result.get(variableResult).get(RETURN_CODE));
                            results.put(sensitiveVariableName, result.get(variableResult).get("returnResult"));
                            results.put(STATUS_CODE, result.get(variableResult).get(STATUS_CODE));
                        }

                    }
                }
                return results;

            } else {
                JSONArray createVariableJsonArray = (JSONArray) parser.parse(variablesJson);
                JSONObject createVariableJson;
                String variableName = EMPTY;


                for (int i = 0; i < createVariableJsonArray.size(); i++) {
                    createVariableJson = (JSONObject) createVariableJsonArray.get(i);
                    variableName = (String) ((JSONObject) ((JSONObject) createVariableJson.get("data")).get("attributes")).get("propertyName");


                    for (String variableResult : result.keySet()) {

                        if(variableName.equals(variableResult)) {

                            results.put(RETURN_CODE, result.get(variableResult).get(RETURN_CODE));
                            results.put(variableName, result.get(variableResult).get("returnResult"));
                            results.put(STATUS_CODE, result.get(variableResult).get(STATUS_CODE));
                        }
                    }
                }
                return results;
            }

        } catch (Exception e) {
            return getFailureResultsMap(StringUtilities.join(e, NEW_LINE));

        }
    }

    @NotNull
    public static Map<String, String> listVariables(@NotNull final TerraformVariableInputs terraformVariableInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listVariablesUrl(terraformVariableInputs));
        setCommonHttpInputs(httpClientInputs, terraformVariableInputs.getCommonInputs());
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setResponseCharacterSet(terraformVariableInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(terraformVariableInputs.getCommonInputs().getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }


    @NotNull
    public static String listVariablesUrl(@NotNull final TerraformVariableInputs terraformVariableInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(WORKSPACE_PATH)
                .append(PATH_SEPARATOR)
                .append(terraformVariableInputs.getWorkspaceId())
                .append(VARIABLE_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String createVariableUrl(@NotNull final TerraformVariableInputs terraformVariableInputs) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getVariablePath(terraformVariableInputs));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getVariablePath(@NotNull final TerraformVariableInputs terraformVariableInputs) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(WORKSPACE_PATH)
                .append(PATH_SEPARATOR)
                .append(terraformVariableInputs.getWorkspaceId())
                .append(VARIABLE_PATH);
        return pathString.toString();
    }


    public static Map<String, String> updateVariable(@NotNull final TerraformVariableInputs updateVariableInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = updateVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(updateVariableUrl(updateVariableInputs));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PATCH);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setBody(commonInputs.getRequestBody());
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> deleteVariable(@NotNull final TerraformVariableInputs deleteVariableInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = deleteVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(deleteVariableUrl(deleteVariableInputs));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String updateVariableUrl(@NotNull final TerraformVariableInputs updateVariableInputs) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getVariablesPath(updateVariableInputs));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String deleteVariableUrl(@NotNull final TerraformVariableInputs deleteVariableInputs) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getVariablesPath(deleteVariableInputs));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getVariablesPath(@NotNull final TerraformVariableInputs updateVariableInputs) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(WORKSPACE_PATH)
                .append(PATH_SEPARATOR)
                .append(updateVariableInputs.getWorkspaceId())
                .append(VARIABLE_PATH)
                .append(PATH_SEPARATOR)
                .append(updateVariableInputs.getVariableId());
        return pathString.toString();
    }

    @NotNull
    public static String createVariableRequestBody(TerraformVariableInputs terraformVariableInputs) {
        String requestBody = EMPTY;
        ObjectMapper createVariableMapper = new ObjectMapper();
        CreateVariableRequestBody createVariableRequestBody = new CreateVariableRequestBody();
        CreateVariableRequestBody.CreateVariableData createVariableData = createVariableRequestBody.new CreateVariableData();
        createVariableData.setType(VARIABLE_TYPE);

        CreateVariableRequestBody.Attributes attributes = createVariableRequestBody.new Attributes();
        attributes.setKey(terraformVariableInputs.getVariableName());
        if (terraformVariableInputs.getVariableValue().isEmpty()) {
            attributes.setValue(terraformVariableInputs.getSensitiveVariableValue());
        } else {
            attributes.setValue(terraformVariableInputs.getVariableValue());
        }
        attributes.setCategory(terraformVariableInputs.getVariableCategory());
        attributes.setHcl(terraformVariableInputs.getHcl());
        attributes.setSensitive(terraformVariableInputs.getSensitive());
        createVariableData.setAttributes(attributes);
        createVariableRequestBody.setData(createVariableData);

        try {
            requestBody = createVariableMapper.writeValueAsString(createVariableRequestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;

    }

    @NotNull
    public static String updateVariableRequestBody(TerraformVariableInputs terraformVariableInputs) {
        String requestBody = EMPTY;
        ObjectMapper updateVariableMapper = new ObjectMapper();
        UpdateVariableRequestBody updateVariableRequestBody = new UpdateVariableRequestBody();
        UpdateVariableRequestBody.UpdateVariableData updateVariableData = updateVariableRequestBody.new UpdateVariableData();
        updateVariableData.setType(VARIABLE_TYPE);
        updateVariableData.setId(terraformVariableInputs.getVariableId());

        UpdateVariableRequestBody.Attributes attributes = updateVariableRequestBody.new Attributes();

        attributes.setKey(terraformVariableInputs.getVariableName());
        attributes.setHcl(terraformVariableInputs.getHcl());
        attributes.setSensitive(terraformVariableInputs.getSensitive());

        if (terraformVariableInputs.getVariableValue().isEmpty()) {
            attributes.setValue(terraformVariableInputs.getSensitiveVariableValue());
        } else {
            attributes.setValue(terraformVariableInputs.getVariableValue());
        }
        updateVariableData.setAttributes(attributes);
        updateVariableRequestBody.setData(updateVariableData);

        try {
            requestBody = updateVariableMapper.writeValueAsString(updateVariableRequestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;


    }
}
