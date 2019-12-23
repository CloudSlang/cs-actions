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
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformRunInputs;
import io.cloudslang.content.hashicorp.terraform.services.models.runs.ApplyRunRequestBody;
import io.cloudslang.content.hashicorp.terraform.services.models.runs.CreateRunBody;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.ApplyRunConstants.APPLY_RUN_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateRunConstants.RUN_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateRunConstants.RUN_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspaceConstants.WORKSPACE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspaceConstants.WORKSPACE_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.GetApplyDetailsConstants.APPLY_DETAILS_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class RunImpl {
    @NotNull
    public static Map<String, String> createRunClient(@NotNull final TerraformRunInputs createRunInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = createRunInputs.getCommonInputs();
        httpClientInputs.setUrl(createRunClientUrl());
        if (commonInputs.getRequestBody().isEmpty()) {
            try {
                httpClientInputs.setBody(createRunBody(createRunInputs));
            } catch (JsonProcessingException e) {
                return getFailureResultsMap(e);
            }
        } else {
            httpClientInputs.setBody(commonInputs.getRequestBody());
        }
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> applyRunClient(@NotNull final TerraformRunInputs applyRunInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = applyRunInputs.getCommonInputs();
        httpClientInputs.setUrl(applyRunClientUrl(applyRunInputs.getRunId()));
        if (commonInputs.getRequestBody().isEmpty()) {
            try {
                httpClientInputs.setBody(applyRunBody(applyRunInputs));
            } catch (JsonProcessingException e) {
                return getFailureResultsMap(e);
            }
        } else {
            httpClientInputs.setBody(commonInputs.getRequestBody());
        }
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> listRunsInWorkspaceClient(@NotNull final TerraformRunInputs listRunsInWorkspaceInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = listRunsInWorkspaceInputs.getCommonInputs();
        httpClientInputs.setUrl(listRunsInWorkspaceClientUrl(listRunsInWorkspaceInputs.getWorkspaceId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setQueryParams(getQueryParams(listRunsInWorkspaceInputs.getCommonInputs().getPageNumber(),
                listRunsInWorkspaceInputs.getCommonInputs().getPageSize()));
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> getRunDetails(@NotNull final TerraformRunInputs getRunDetailsInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = getRunDetailsInputs.getCommonInputs();
        httpClientInputs.setUrl(getRunDetailsUrl(getRunDetailsInputs.getRunId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> getApplyDetails(@NotNull final TerraformRunInputs getApplyDetailsInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = getApplyDetailsInputs.getCommonInputs();
        httpClientInputs.setUrl(getApplyDetailsUrl(getApplyDetailsInputs.getApplyIdId()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String createRunClientUrl() throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(RUN_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getRunDetailsUrl(@NotNull final String runId) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(RUN_PATH)
                .append(PATH_SEPARATOR)
                .append(runId);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getApplyDetailsUrl(@NotNull final String applyId) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(APPLY_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(applyId);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String applyRunClientUrl(@NotNull String runId) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(RUN_PATH)
                .append(PATH_SEPARATOR)
                .append(runId)
                .append(APPLY_RUN_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }


    @NotNull
    public static String listRunsInWorkspaceClientUrl(@NotNull String workspaceId) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(WORKSPACE_PATH)
                .append(PATH_SEPARATOR)
                .append(workspaceId)
                .append(RUN_PATH);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String createRunBody(TerraformRunInputs createRunInputs) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CreateRunBody createBody = new CreateRunBody();
        CreateRunBody.CreateRunData createRundata = createBody.new CreateRunData();
        CreateRunBody.Attributes attributes = createBody.new Attributes();
        CreateRunBody.Relationships relationships = createBody.new Relationships();
        CreateRunBody.Workspace workspace = createBody.new Workspace();
        CreateRunBody.WorkspaceData workspaceData = createBody.new WorkspaceData();

        String requestBody = EMPTY;

        workspaceData.setId(createRunInputs.getWorkspaceId());
        workspaceData.setType(WORKSPACE_TYPE);

        attributes.setDestroy(Boolean.valueOf(createRunInputs.getIsDestroy()));
        attributes.setRunMessage(createRunInputs.getRunMessage());
        relationships.setWorkspace(workspace);
        workspace.setData(workspaceData);
        createRundata.setRelationships(relationships);
        createRundata.setAttributes(attributes);
        createRundata.setType(RUN_TYPE);

        createBody.setData(createRundata);

        requestBody = mapper.writeValueAsString(createBody);

        return requestBody;
    }

    @NotNull
    public static String applyRunBody(TerraformRunInputs applyRunInputs) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ApplyRunRequestBody applyRunBody = new ApplyRunRequestBody();
        applyRunBody.setRunComment(applyRunInputs.getRunComment());
        String requestBody = EMPTY;
        requestBody = mapper.writeValueAsString(applyRunBody);

        return requestBody;
    }
}
