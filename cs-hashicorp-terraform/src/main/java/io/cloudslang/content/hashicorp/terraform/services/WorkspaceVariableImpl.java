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
import io.cloudslang.content.hashicorp.terraform.entities.TerraformWorkspaceVariableInputs;
import io.cloudslang.content.hashicorp.terraform.services.models.workspace.CreateWorkspaceVariableRequestBody;
import io.cloudslang.content.hashicorp.terraform.services.models.workspace.UpdateWorkspaceVariableRequestBody;
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
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspaceVariableConstants.WORKSPACE_VARIABLE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspaceVariableConstants.WORKSPACE_VARIABLE_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspaceConstants.WORKSPACE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getUriBuilder;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class WorkspaceVariableImpl {

    @NotNull
    public static Map<String, String> createWorkspaceVariable(@NotNull TerraformWorkspaceVariableInputs terraformWorkspaceVariableInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = terraformWorkspaceVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(createWorkspaceVariableUrl(terraformWorkspaceVariableInputs));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));

        if (commonInputs.getRequestBody().isEmpty() & terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableRequestBody().isEmpty()) {
            httpClientInputs.setBody(createWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs));
        } else if (!terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableRequestBody().isEmpty()) {
            httpClientInputs.setBody(terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableRequestBody());
            httpClientInputs.setUrl(createWorkspaceVariableUrl(terraformWorkspaceVariableInputs));
        } else {
            httpClientInputs.setBody(commonInputs.getRequestBody());
        }
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());

        return new HttpClientService().execute(httpClientInputs);
    }


    @NotNull
    public static Map<String, Map<String, String>> createWorkspaceVariables(@NotNull TerraformWorkspaceVariableInputs terraformWorkspaceVariableInputs) throws Exception {
        String workspaceVariableName;
        String workspaceVariableValue;
        String hcl;
        String workspaceVariableCategory;
        Map<String, Map<String, String>> createWorkspaceVariableMap = new HashMap<>();
        JSONParser parser = new JSONParser();
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = terraformWorkspaceVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(createWorkspaceVariableUrl(terraformWorkspaceVariableInputs));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        if (!terraformWorkspaceVariableInputs.getWorkspaceVariableJson().isEmpty() & !terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableJson().isEmpty()) {
            String sensitiveWorkspaceVariableValue;

            JSONArray createWorkspaceVariableJsonArray = (JSONArray) parser.parse(terraformWorkspaceVariableInputs.getWorkspaceVariableJson());
            JSONArray createSensitiveWorkspaceVariableJsonArray = (JSONArray) parser.parse(terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableJson());
            JSONObject createWorkspaceVariableJson;
            JSONObject createSensitiveWorkspaceVariableJson;
            for (int i = 0; i < createWorkspaceVariableJsonArray.size(); i++) {
                createWorkspaceVariableJson = (JSONObject) createWorkspaceVariableJsonArray.get(i);
                workspaceVariableName = (String) ((JSONObject) ((JSONObject) createWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");
                workspaceVariableValue = (String) ((JSONObject) ((JSONObject) createWorkspaceVariableJson.get("data")).get("attributes")).get("propertyValue");
                hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) createWorkspaceVariableJson.get("data")).get("attributes")).get("HCL"));
                workspaceVariableCategory = (String) ((JSONObject) ((JSONObject) createWorkspaceVariableJson.get("data")).get("attributes")).get("Category");


                terraformWorkspaceVariableInputs = terraformWorkspaceVariableInputs.builder()
                        .workspaceVariableName(workspaceVariableName)
                        .workspaceVariableValue(workspaceVariableValue)
                        .workspaceVariableCategory(workspaceVariableCategory)
                        .hcl(hcl)
                        .workspaceId(terraformWorkspaceVariableInputs.getWorkspaceId())
                        .sensitive("false").build();
                httpClientInputs.setBody(createWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs));

                createWorkspaceVariableMap.put(workspaceVariableName, new HttpClientService().execute(httpClientInputs));

            }
            for (int i = 0; i < createSensitiveWorkspaceVariableJsonArray.size(); i++) {
                createSensitiveWorkspaceVariableJson = (JSONObject) createSensitiveWorkspaceVariableJsonArray.get(i);
                workspaceVariableName = (String) ((JSONObject) ((JSONObject) createSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");
                sensitiveWorkspaceVariableValue = (String) ((JSONObject) ((JSONObject) createSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("propertyValue");
                hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) createSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("HCL"));
                workspaceVariableCategory = (String) ((JSONObject) ((JSONObject) createSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("Category");


                terraformWorkspaceVariableInputs = terraformWorkspaceVariableInputs.builder()
                        .workspaceVariableName(workspaceVariableName)
                        .sensitiveWorkspaceVariableValue(sensitiveWorkspaceVariableValue)
                        .workspaceVariableValue(EMPTY)
                        .workspaceVariableCategory(workspaceVariableCategory)
                        .hcl(hcl)
                        .workspaceId(terraformWorkspaceVariableInputs.getWorkspaceId())
                        .sensitive("true").build();
                httpClientInputs.setBody(createWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs));
                createWorkspaceVariableMap.put(workspaceVariableName, new HttpClientService().execute(httpClientInputs));

            }
            return createWorkspaceVariableMap;

        } else if (!terraformWorkspaceVariableInputs.getWorkspaceVariableJson().isEmpty()) {
            JSONArray createWorkspaceVariableJsonArray = (JSONArray) parser.parse(terraformWorkspaceVariableInputs.getWorkspaceVariableJson());
            JSONObject createWorkspaceVariableJson;
            for (int i = 0; i < createWorkspaceVariableJsonArray.size(); i++) {
                createWorkspaceVariableJson = (JSONObject) createWorkspaceVariableJsonArray.get(i);
                workspaceVariableName = (String) ((JSONObject) ((JSONObject) createWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");
                workspaceVariableValue = (String) ((JSONObject) ((JSONObject) createWorkspaceVariableJson.get("data")).get("attributes")).get("propertyValue");
                hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) createWorkspaceVariableJson.get("data")).get("attributes")).get("HCL"));
                workspaceVariableCategory = (String) ((JSONObject) ((JSONObject) createWorkspaceVariableJson.get("data")).get("attributes")).get("Category");


                terraformWorkspaceVariableInputs = terraformWorkspaceVariableInputs.builder()
                        .workspaceVariableName(workspaceVariableName)
                        .workspaceVariableValue(workspaceVariableValue)
                        .workspaceVariableCategory(workspaceVariableCategory)
                        .hcl(hcl)
                        .workspaceId(terraformWorkspaceVariableInputs.getWorkspaceId())
                        .sensitive("false").build();
                httpClientInputs.setBody(createWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs));
                createWorkspaceVariableMap.put(workspaceVariableName, new HttpClientService().execute(httpClientInputs));

            }

            return createWorkspaceVariableMap;
        } else {
            JSONArray createSensitiveWorkspaceVariableJsonArray = (JSONArray) parser.parse(terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableJson());
            JSONObject createSensitiveWorkspaceVariableJson;
            for (int i = 0; i < createSensitiveWorkspaceVariableJsonArray.size(); i++) {
                createSensitiveWorkspaceVariableJson = (JSONObject) createSensitiveWorkspaceVariableJsonArray.get(i);
                workspaceVariableName = (String) ((JSONObject) ((JSONObject) createSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");
                workspaceVariableValue = (String) ((JSONObject) ((JSONObject) createSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("propertyValue");
                hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) createSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("HCL"));
                workspaceVariableCategory = (String) ((JSONObject) ((JSONObject) createSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("Category");


                terraformWorkspaceVariableInputs = terraformWorkspaceVariableInputs.builder()

                        .workspaceVariableName(workspaceVariableName)
                        .sensitiveWorkspaceVariableValue(workspaceVariableValue)
                        .workspaceVariableValue(EMPTY)
                        .workspaceVariableCategory(workspaceVariableCategory)
                        .hcl(hcl)
                        .workspaceId(terraformWorkspaceVariableInputs.getWorkspaceId())
                        .sensitive("true").build();
                httpClientInputs.setBody(createWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs));
                createWorkspaceVariableMap.put(workspaceVariableName, new HttpClientService().execute(httpClientInputs));

            }

            return createWorkspaceVariableMap;
        }


    }

    @NotNull
    public static Map<String, Map<String, String>> updateWorkspaceVariables(@NotNull TerraformWorkspaceVariableInputs terraformWorkspaceVariableInputs) throws Exception {
        String workspaceVariableName;
        String workspaceVariableValue;
        String sensitiveWorkspaceVariableValue;
        String workspaceVariableId;
        String hcl = null;
        String sensitive = null;
        Map<String, Map<String, String>> updateWorkspaceVariableMap = new HashMap<>();
        Map<String, String> listWorkspaceVariablesResult = listWorkspaceVariables(terraformWorkspaceVariableInputs);
        JSONParser parser = new JSONParser();
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = terraformWorkspaceVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(createWorkspaceVariableUrl(terraformWorkspaceVariableInputs));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(PATCH);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        if (!terraformWorkspaceVariableInputs.getWorkspaceVariableJson().isEmpty() & !terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableJson().isEmpty()) {

            JSONArray updateWorkspaceVariableJsonArray = (JSONArray) parser.parse(terraformWorkspaceVariableInputs.getWorkspaceVariableJson());
            JSONArray updateSensitiveWorkspaceVariableJsonArray = (JSONArray) parser.parse(terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableJson());
            JSONObject updateWorkspaceVariableJson;
            JSONObject updateSensitiveWorkspaceVariableJson;
            for (int i = 0; i < updateWorkspaceVariableJsonArray.size(); i++) {
                updateWorkspaceVariableJson = (JSONObject) updateWorkspaceVariableJsonArray.get(i);
                workspaceVariableName = (String) ((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");
                workspaceVariableValue = (String) ((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).get("propertyValue");
                workspaceVariableId = (String) (((JSONObject) updateWorkspaceVariableJson.get("data")).get("id"));
                if (((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).containsKey("HCL")) {
                    hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).get("HCL"));
                }
                if (((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).containsKey("sensitive")) {
                    sensitive = Boolean.toString((boolean) ((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).get("sensitive"));
                }
                JSONObject workspaceVariableJsonObj = (JSONObject) parser.parse(listWorkspaceVariablesResult.get("returnResult"));
                JSONArray workspaceVariableJsonArray = (JSONArray) workspaceVariableJsonObj.get("data");
                for (int j = 0; j < workspaceVariableJsonArray.size(); j++) {
                    final String workspaceVariableID = JsonPath.read(workspaceVariableJsonArray.get(j), "id");
                    if (workspaceVariableId.equals(workspaceVariableID)) {
                        terraformWorkspaceVariableInputs = TerraformWorkspaceVariableInputs.builder()
                                .workspaceVariableName(workspaceVariableName)
                                .workspaceVariableValue(workspaceVariableValue)
                                .hcl(hcl)
                                .sensitive(sensitive)
                                .workspaceVariableId(workspaceVariableId)
                                .workspaceId(terraformWorkspaceVariableInputs.getWorkspaceId())
                                .build();
                        httpClientInputs.setBody(updateWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs));
                        httpClientInputs.setUrl(updateWorkspaceVariableUrl(terraformWorkspaceVariableInputs));
                        updateWorkspaceVariableMap.put(workspaceVariableName, new HttpClientService().execute(httpClientInputs));
                    }
                }
            }
            for (int i = 0; i < updateSensitiveWorkspaceVariableJsonArray.size(); i++) {
                updateSensitiveWorkspaceVariableJson = (JSONObject) updateSensitiveWorkspaceVariableJsonArray.get(i);
                workspaceVariableName = (String) updateSensitiveWorkspaceVariableJson.get("propertyName");
                sensitiveWorkspaceVariableValue = (String) updateSensitiveWorkspaceVariableJson.get("propertyValue");
                workspaceVariableId = (String) (((JSONObject) updateSensitiveWorkspaceVariableJson.get("data")).get("id"));
                JSONObject sensitiveWorkspaceVariableJsonObj = (JSONObject) parser.parse(listWorkspaceVariablesResult.get("returnResult"));
                JSONArray sensitiveWorkspaceVariableJsonArray = (JSONArray) sensitiveWorkspaceVariableJsonObj.get("data");
                for (int j = 0; j < sensitiveWorkspaceVariableJsonArray.size(); j++) {
                    final String workspaceVariableID = JsonPath.read(sensitiveWorkspaceVariableJsonArray.get(j), "id");
                    if (workspaceVariableId.equals(workspaceVariableID)) {
                        terraformWorkspaceVariableInputs = TerraformWorkspaceVariableInputs.builder()
                                .workspaceVariableName(workspaceVariableName)
                                .workspaceVariableValue(EMPTY)
                                .sensitiveWorkspaceVariableValue(sensitiveWorkspaceVariableValue)
                                .workspaceVariableId(workspaceVariableId)
                                .workspaceId(terraformWorkspaceVariableInputs.getWorkspaceId())
                                .build();
                        httpClientInputs.setBody(updateWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs));
                        httpClientInputs.setUrl(updateWorkspaceVariableUrl(terraformWorkspaceVariableInputs));
                        updateWorkspaceVariableMap.put(workspaceVariableName, new HttpClientService().execute(httpClientInputs));
                    }
                }
            }
            return updateWorkspaceVariableMap;
        } else if (!terraformWorkspaceVariableInputs.getWorkspaceVariableJson().isEmpty()) {
            JSONArray updateWorkspaceVariableJsonArray = (JSONArray) parser.parse(terraformWorkspaceVariableInputs.getWorkspaceVariableJson());
            JSONObject updateWorkspaceVariableJson;
            for (int i = 0; i < updateWorkspaceVariableJsonArray.size(); i++) {
                updateWorkspaceVariableJson = (JSONObject) updateWorkspaceVariableJsonArray.get(i);
                workspaceVariableName = (String) ((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");
                workspaceVariableValue = (String) ((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).get("propertyValue");
                workspaceVariableId = (String) (((JSONObject) updateWorkspaceVariableJson.get("data")).get("id"));
                if (((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).containsKey("HCL")) {
                    hcl = Boolean.toString((boolean) ((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).get("HCL"));
                }
                if (((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).containsKey("sensitive")) {
                    sensitive = Boolean.toString((boolean) ((JSONObject) ((JSONObject) updateWorkspaceVariableJson.get("data")).get("attributes")).get("sensitive"));
                }
                JSONObject workspaceVariableJsonObj = (JSONObject) parser.parse(listWorkspaceVariablesResult.get("returnResult"));
                JSONArray workspaceVariableJsonArray = (JSONArray) workspaceVariableJsonObj.get("data");
                for (int j = 0; j < workspaceVariableJsonArray.size(); j++) {
                    final String workspaceVariableID = JsonPath.read(workspaceVariableJsonArray.get(j), "id");
                    if (workspaceVariableId.equals(workspaceVariableID)) {
                        terraformWorkspaceVariableInputs = TerraformWorkspaceVariableInputs.builder()
                                .workspaceVariableName(workspaceVariableName)
                                .workspaceVariableValue(workspaceVariableValue)
                                .hcl(hcl)
                                .sensitive(sensitive)
                                .workspaceVariableId(workspaceVariableId)
                                .workspaceId(terraformWorkspaceVariableInputs.getWorkspaceId())
                                .build();
                        httpClientInputs.setBody(updateWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs));
                        httpClientInputs.setUrl(updateWorkspaceVariableUrl(terraformWorkspaceVariableInputs));
                        updateWorkspaceVariableMap.put(workspaceVariableName, new HttpClientService().execute(httpClientInputs));
                    }
                }
            }
            return updateWorkspaceVariableMap;
        } else {
            JSONArray updateSensitiveWorkspaceVariableJsonArray = (JSONArray) parser.parse(terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableJson());
            JSONObject updateSensitiveWorkspaceVariableJson;
            for (int i = 0; i < updateSensitiveWorkspaceVariableJsonArray.size(); i++) {
                updateSensitiveWorkspaceVariableJson = (JSONObject) updateSensitiveWorkspaceVariableJsonArray.get(i);
                workspaceVariableName = (String) ((JSONObject) ((JSONObject) updateSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");
                sensitiveWorkspaceVariableValue = (String) ((JSONObject) ((JSONObject) updateSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("propertyValue");
                workspaceVariableId = (String) (((JSONObject) updateSensitiveWorkspaceVariableJson.get("data")).get("id"));
                JSONObject sensitiveWorkspaceVariableJsonObj = (JSONObject) parser.parse(listWorkspaceVariablesResult.get("returnResult"));
                JSONArray sensitiveWorkspaceVariableJsonArray = (JSONArray) sensitiveWorkspaceVariableJsonObj.get("data");
                for (int j = 0; j < sensitiveWorkspaceVariableJsonArray.size(); j++) {
                    final String workspaceVariableID = JsonPath.read(updateSensitiveWorkspaceVariableJson.get(j), "id");
                    if (workspaceVariableId.equals(workspaceVariableID)) {
                        terraformWorkspaceVariableInputs = TerraformWorkspaceVariableInputs.builder()
                                .workspaceVariableName(workspaceVariableName)
                                .workspaceVariableValue(EMPTY)
                                .sensitiveWorkspaceVariableValue(sensitiveWorkspaceVariableValue)
                                .workspaceVariableId(workspaceVariableId)
                                .workspaceId(terraformWorkspaceVariableInputs.getWorkspaceId())
                                .build();
                        httpClientInputs.setBody(updateWorkspaceVariableRequestBody(terraformWorkspaceVariableInputs));
                        httpClientInputs.setUrl(updateWorkspaceVariableUrl(terraformWorkspaceVariableInputs));
                        updateWorkspaceVariableMap.put(workspaceVariableName, new HttpClientService().execute(httpClientInputs));
                    }
                }
            }

            return updateWorkspaceVariableMap;
        }


    }

    @NotNull
    public static Map<String, String> getWorkspaceVariablesOperationOutput(String workspaceVariablesJson, String sensitiveWorkspaceVariablesJson, Map<String, Map<String, String>> result) {
        try {
            final Map<String, String> results = new HashMap<>();
            JSONParser parser = new JSONParser();
            if (!workspaceVariablesJson.isEmpty() & !sensitiveWorkspaceVariablesJson.isEmpty()) {
                JSONArray createWorkspaceVariableJsonArray = (JSONArray) parser.parse(workspaceVariablesJson);
                JSONArray createSensitiveWorkspaceVariableJsonArray = (JSONArray) parser.parse(sensitiveWorkspaceVariablesJson);
                JSONObject createWorkspaceVariableJson;
                JSONObject createSensitiveWorkspaceVariableJson;
                String workspaceVariableName = EMPTY;
                String sensitiveWorkspaceVariableName = EMPTY;

                for (int i = 0; i < createWorkspaceVariableJsonArray.size(); i++) {
                    createWorkspaceVariableJson = (JSONObject) createWorkspaceVariableJsonArray.get(i);
                    workspaceVariableName = (String) ((JSONObject) ((JSONObject) createWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");


                    for (String workspaceVariableResult : result.keySet()) {

                        if (workspaceVariableName.equals(workspaceVariableResult)) {

                            results.put(RETURN_CODE, result.get(workspaceVariableResult).get(RETURN_CODE));
                            results.put(workspaceVariableName, result.get(workspaceVariableResult).get("returnResult"));
                            results.put(STATUS_CODE, result.get(workspaceVariableResult).get(STATUS_CODE));
                        }
                    }
                }
                for (int i = 0; i < createSensitiveWorkspaceVariableJsonArray.size(); i++) {
                    createSensitiveWorkspaceVariableJson = (JSONObject) createSensitiveWorkspaceVariableJsonArray.get(i);
                    sensitiveWorkspaceVariableName = (String) ((JSONObject) ((JSONObject) createSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");


                    for (String workspaceVariableResult : result.keySet()) {

                        if(sensitiveWorkspaceVariableName.equals(workspaceVariableResult)) {

                            results.put(RETURN_CODE, result.get(workspaceVariableResult).get(RETURN_CODE));
                            results.put(sensitiveWorkspaceVariableName, result.get(workspaceVariableResult).get("returnResult"));
                            results.put(STATUS_CODE, result.get(workspaceVariableResult).get(STATUS_CODE));
                        }
                    }
                }
                return results;

            } else if (!sensitiveWorkspaceVariablesJson.isEmpty()) {
                JSONArray createSensitiveWorkspaceVariableJsonArray = (JSONArray) parser.parse(sensitiveWorkspaceVariablesJson);
                JSONObject createSensitiveWorkspaceVariableJson;
                String sensitiveWorkspaceVariableName = EMPTY;
                for (int i = 0; i < createSensitiveWorkspaceVariableJsonArray.size(); i++) {
                    createSensitiveWorkspaceVariableJson = (JSONObject) createSensitiveWorkspaceVariableJsonArray.get(i);
                    sensitiveWorkspaceVariableName = (String) ((JSONObject) ((JSONObject) createSensitiveWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");


                    for (String workspaceVariableResult : result.keySet()) {

                        if(sensitiveWorkspaceVariableName.equals(workspaceVariableResult)) {

                            results.put(RETURN_CODE, result.get(workspaceVariableResult).get(RETURN_CODE));
                            results.put(sensitiveWorkspaceVariableName, result.get(workspaceVariableResult).get("returnResult"));
                            results.put(STATUS_CODE, result.get(workspaceVariableResult).get(STATUS_CODE));
                        }

                    }
                }
                return results;

            } else {
                JSONArray createWorkspaceVariableJsonArray = (JSONArray) parser.parse(workspaceVariablesJson);
                JSONObject createWorkspaceVariableJson;
                String workspaceVariableName = EMPTY;


                for (int i = 0; i < createWorkspaceVariableJsonArray.size(); i++) {
                    createWorkspaceVariableJson = (JSONObject) createWorkspaceVariableJsonArray.get(i);
                    workspaceVariableName = (String) ((JSONObject) ((JSONObject) createWorkspaceVariableJson.get("data")).get("attributes")).get("propertyName");


                    for (String workspaceVariableResult : result.keySet()) {

                        if(workspaceVariableName.equals(workspaceVariableResult)) {

                            results.put(RETURN_CODE, result.get(workspaceVariableResult).get(RETURN_CODE));
                            results.put(workspaceVariableName, result.get(workspaceVariableResult).get("returnResult"));
                            results.put(STATUS_CODE, result.get(workspaceVariableResult).get(STATUS_CODE));
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
    public static Map<String, String> listWorkspaceVariables(@NotNull final TerraformWorkspaceVariableInputs terraformWorkspaceVariableInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listWorkspaceVariablesUrl(terraformWorkspaceVariableInputs));
        setCommonHttpInputs(httpClientInputs, terraformWorkspaceVariableInputs.getCommonInputs());
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setResponseCharacterSet(terraformWorkspaceVariableInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(terraformWorkspaceVariableInputs.getCommonInputs().getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }


    @NotNull
    public static String listWorkspaceVariablesUrl(@NotNull final TerraformWorkspaceVariableInputs terraformWorkspaceVariableInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(WORKSPACE_PATH)
                .append(PATH_SEPARATOR)
                .append(terraformWorkspaceVariableInputs.getWorkspaceId())
                .append(WORKSPACE_VARIABLE_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String createWorkspaceVariableUrl(@NotNull final TerraformWorkspaceVariableInputs terraformWorkspaceVariableInputs) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getWorkspaceVariablePath(terraformWorkspaceVariableInputs));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getWorkspaceVariablePath(@NotNull final TerraformWorkspaceVariableInputs terraformWorkspaceVariableInputs) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(WORKSPACE_PATH)
                .append(PATH_SEPARATOR)
                .append(terraformWorkspaceVariableInputs.getWorkspaceId())
                .append(WORKSPACE_VARIABLE_PATH);
        return pathString.toString();
    }


    public static Map<String, String> updateWorkspaceVariable(@NotNull final TerraformWorkspaceVariableInputs updateWorkspaceVariableInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = updateWorkspaceVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(updateWorkspaceVariableUrl(updateWorkspaceVariableInputs));
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
    public static Map<String, String> deleteWorkspaceVariable(@NotNull final TerraformWorkspaceVariableInputs deleteWorkspaceVariableInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = deleteWorkspaceVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(deleteWorkspaceVariableUrl(deleteWorkspaceVariableInputs));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String updateWorkspaceVariableUrl(@NotNull final TerraformWorkspaceVariableInputs updateWorkspaceVariableInputs) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getWorkspaceVariablesPath(updateWorkspaceVariableInputs));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String deleteWorkspaceVariableUrl(@NotNull final TerraformWorkspaceVariableInputs deleteWorkspaceVariableInputs) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getWorkspaceVariablesPath(deleteWorkspaceVariableInputs));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getWorkspaceVariablesPath(@NotNull final TerraformWorkspaceVariableInputs updateWorkspaceVariableInputs) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(WORKSPACE_PATH)
                .append(PATH_SEPARATOR)
                .append(updateWorkspaceVariableInputs.getWorkspaceId())
                .append(WORKSPACE_VARIABLE_PATH)
                .append(PATH_SEPARATOR)
                .append(updateWorkspaceVariableInputs.getWorkspaceVariableId());
        return pathString.toString();
    }

    @NotNull
    public static String createWorkspaceVariableRequestBody(TerraformWorkspaceVariableInputs terraformWorkspaceVariableInputs) {
        String requestBody = EMPTY;
        ObjectMapper createWorkspaceVariableMapper = new ObjectMapper();
        CreateWorkspaceVariableRequestBody createWorkspaceVariableRequestBody = new CreateWorkspaceVariableRequestBody();
        CreateWorkspaceVariableRequestBody.CreateWorkspaceVariableData createWorkspaceVariableData = createWorkspaceVariableRequestBody.new CreateWorkspaceVariableData();
        createWorkspaceVariableData.setType(WORKSPACE_VARIABLE_TYPE);

        CreateWorkspaceVariableRequestBody.Attributes attributes = createWorkspaceVariableRequestBody.new Attributes();
        attributes.setKey(terraformWorkspaceVariableInputs.getWorkspaceVariableName());
        if (terraformWorkspaceVariableInputs.getWorkspaceVariableValue().isEmpty()) {
            attributes.setValue(terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableValue());
        } else {
            attributes.setValue(terraformWorkspaceVariableInputs.getWorkspaceVariableValue());
        }
        attributes.setCategory(terraformWorkspaceVariableInputs.getWorkspaceVariableCategory());
        attributes.setHcl(terraformWorkspaceVariableInputs.getHcl());
        attributes.setSensitive(terraformWorkspaceVariableInputs.getSensitive());
        createWorkspaceVariableData.setAttributes(attributes);
        createWorkspaceVariableRequestBody.setData(createWorkspaceVariableData);

        try {
            requestBody = createWorkspaceVariableMapper.writeValueAsString(createWorkspaceVariableRequestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;

    }

    @NotNull
    public static String updateWorkspaceVariableRequestBody(TerraformWorkspaceVariableInputs terraformWorkspaceVariableInputs) {
        String requestBody = EMPTY;
        ObjectMapper updateWorkspaceVariableMapper = new ObjectMapper();
        UpdateWorkspaceVariableRequestBody updateWorkspaceVariableRequestBody = new UpdateWorkspaceVariableRequestBody();
        UpdateWorkspaceVariableRequestBody.UpdateWorkspaceVariableData updateWorkspaceVariableData = updateWorkspaceVariableRequestBody.new UpdateWorkspaceVariableData();
        updateWorkspaceVariableData.setType(WORKSPACE_VARIABLE_TYPE);
        updateWorkspaceVariableData.setId(terraformWorkspaceVariableInputs.getWorkspaceVariableId());

        UpdateWorkspaceVariableRequestBody.Attributes attributes = updateWorkspaceVariableRequestBody.new Attributes();

        attributes.setKey(terraformWorkspaceVariableInputs.getWorkspaceVariableName());
        attributes.setHcl(terraformWorkspaceVariableInputs.getHcl());
        attributes.setSensitive(terraformWorkspaceVariableInputs.getSensitive());

        if (terraformWorkspaceVariableInputs.getWorkspaceVariableValue().isEmpty()) {
            attributes.setValue(terraformWorkspaceVariableInputs.getSensitiveWorkspaceVariableValue());
        } else {
            attributes.setValue(terraformWorkspaceVariableInputs.getWorkspaceVariableValue());
        }
        updateWorkspaceVariableData.setAttributes(attributes);
        updateWorkspaceVariableRequestBody.setData(updateWorkspaceVariableData);

        try {
            requestBody = updateWorkspaceVariableMapper.writeValueAsString(updateWorkspaceVariableRequestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;


    }
}
