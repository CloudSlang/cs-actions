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
import io.cloudslang.content.hashicorp.terraform.entities.CreateVariableInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.services.models.variables.CreateVariableRequestBody;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateRunConstants.CREATE_RUN_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateVariableConstants.VARIABLE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateVariableConstants.VARIABLE_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspace.WORKSPACE_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getUriBuilder;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class VariableImpl {

    @NotNull
    public static Map<String, String> createVariable(@NotNull final CreateVariableInputs createVariableInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = createVariableInputs.getCommonInputs();
        httpClientInputs.setUrl(createVariableUrl());
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        if (commonInputs.getRequestBody().equals(EMPTY)) {
            httpClientInputs.setBody(createVariableRequestBody(createVariableInputs));
        } else {
            httpClientInputs.setBody(commonInputs.getRequestBody());
        }
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> listVariables(@NotNull final TerraformCommonInputs commonInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(listVariablesUrl());
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }


    @NotNull
    public static String listVariablesUrl() throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(VARIABLE_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String createVariableUrl() throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getVariablePath());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getVariablePath() {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(VARIABLE_PATH);
        return pathString.toString();
    }

    @NotNull
    public static String createVariableRequestBody(CreateVariableInputs createVariableInputs) {
        String requestBody = EMPTY;
        ObjectMapper createVariableMapper = new ObjectMapper();
        CreateVariableRequestBody createVariableRequestBody = new CreateVariableRequestBody();
        CreateVariableRequestBody.CreateVariableData createVariableData = createVariableRequestBody.new CreateVariableData();
        createVariableData.setType(VARIABLE_TYPE);

        CreateVariableRequestBody.Attributes attributes = createVariableRequestBody.new Attributes();
        attributes.setKey(createVariableInputs.getVariableName());
        attributes.setValue(createVariableInputs.getVariableValue());
        attributes.setCategory(createVariableInputs.getVariableCategory());
        attributes.setHcl(createVariableInputs.getHcl());
        attributes.setSensitive(createVariableInputs.getSensitive());

        CreateVariableRequestBody.Data data = createVariableRequestBody.new Data();
        data.setId(createVariableInputs.getWorkspaceId());
        data.setType(WORKSPACE_TYPE);
        CreateVariableRequestBody.Workspace workspace = createVariableRequestBody.new Workspace();
        workspace.setData(data);
        CreateVariableRequestBody.Relationships relationships = createVariableRequestBody.new Relationships();
        relationships.setWorkspace(workspace);

        createVariableData.setRelationships(relationships);
        createVariableData.setAttributes(attributes);
        createVariableRequestBody.setData(createVariableData);

        try {
            requestBody = createVariableMapper.writeValueAsString(createVariableRequestBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;

    }
}
