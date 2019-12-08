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
import io.cloudslang.content.hashicorp.terraform.entities.CreateWorkspaceInputs;
import io.cloudslang.content.hashicorp.terraform.entities.GetWorkspaceDetailsInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.services.models.workspace.CreateWorkspaceRequestBody;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspace.WORKSPACE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspace.WORKSPACE_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.GetWorkspaceDetails.GET_WORKSPACE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class WorkspaceImpl {

    @NotNull
    public static Map<String, String> createWorkspace(@NotNull final CreateWorkspaceInputs createWorkspaceInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = createWorkspaceInputs.getCommonInputs();
        httpClientInputs.setUrl(createWorkspaceUrl(createWorkspaceInputs.getCommonInputs().getOrganizationName()));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        if (commonInputs.getRequestBody().equals(EMPTY)) {
            httpClientInputs.setBody(createWorkspaceBody(createWorkspaceInputs, DELIMITER));
        } else {
            httpClientInputs.setBody(commonInputs.getRequestBody());
        }
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> deleteWorkspace(@NotNull final GetWorkspaceDetailsInputs deleteWorkspaceInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = deleteWorkspaceInputs.getCommonInputs();
        httpClientInputs.setUrl(getWorkspaceDetailsUrl(deleteWorkspaceInputs.getCommonInputs().getOrganizationName(),
                deleteWorkspaceInputs.getWorkspaceName()));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> listWorkspaces(@NotNull final GetWorkspaceDetailsInputs listWorkspacesInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = listWorkspacesInputs.getCommonInputs();
        httpClientInputs.setUrl(createWorkspaceUrl(listWorkspacesInputs.getCommonInputs().getOrganizationName()));
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setQueryParams(getQueryParams(listWorkspacesInputs.getCommonInputs().getPageNumber(),
                listWorkspacesInputs.getCommonInputs().getPageSize()));
        httpClientInputs.setResponseCharacterSet(commonInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> getWorkspaceDetails(@NotNull final GetWorkspaceDetailsInputs
                                                                  getWorkspaceDetailsInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = getWorkspaceDetailsInputs.getCommonInputs();
        httpClientInputs.setUrl(getWorkspaceDetailsUrl(getWorkspaceDetailsInputs.getCommonInputs().getOrganizationName()
                , getWorkspaceDetailsInputs.getWorkspaceName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String createWorkspaceUrl(@NotNull final String organizationName) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getWorkspacePath(organizationName));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    private static String getWorkspaceDetailsUrl(@NotNull final String organizationName, @NotNull final
    String workspaceName) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getWorkspaceDetailsPath(organizationName, workspaceName));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getWorkspacePath(@NotNull final String organizationName) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(ORGANIZATION_PATH)
                .append(organizationName)
                .append(WORKSPACE_PATH);
        return pathString.toString();
    }

    @NotNull
    public static String getWorkspaceDetailsPath(@NotNull final String organizationName,
                                                 @NotNull final String workspaceName) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(ORGANIZATION_PATH)
                .append(organizationName)
                .append(GET_WORKSPACE_PATH)
                .append(workspaceName);
        return pathString.toString();
    }

    @NotNull
    public static String createWorkspaceBody(CreateWorkspaceInputs createWorkspaceInputs, String delimiter) {
        String requestBody = EMPTY;
        final List<String> triggerPrefixesList = new ArrayList<>();
        ObjectMapper createWorkspaceMapper = new ObjectMapper();
        CreateWorkspaceRequestBody createBody = new CreateWorkspaceRequestBody();
        CreateWorkspaceRequestBody.CreateWorkspaceData createWorkspaceData = createBody.new CreateWorkspaceData();
        CreateWorkspaceRequestBody.Attributes attributes = createBody.new Attributes();
        attributes.setName(createWorkspaceInputs.getWorkspaceName());
        attributes.setTerraform_version(createWorkspaceInputs.getCommonInputs().getTerraformVersion());
        attributes.setDescription(createWorkspaceInputs.getWorkspaceDescription());
        attributes.setAutoApply(Boolean.parseBoolean(createWorkspaceInputs.getAutoApply()));
        attributes.setFileTriggersEnabled(Boolean.parseBoolean(createWorkspaceInputs.getFileTriggersEnabled()));
        attributes.setWorkingDirectory(createWorkspaceInputs.getWorkingDirectory());
        attributes.setQueueAllRuns(Boolean.parseBoolean(createWorkspaceInputs.getQueueAllRuns()));
        attributes.setSpeculativeEnabled(Boolean.parseBoolean(createWorkspaceInputs.getSpeculativeEnabled()));
        String[] triggerPrefixes = createWorkspaceInputs.getTriggerPrefixes().split(delimiter);
        Collections.addAll(triggerPrefixesList, triggerPrefixes);
        attributes.setTriggerPrefixes(triggerPrefixesList);

        CreateWorkspaceRequestBody.VCSRepo vcsRepo = createBody.new VCSRepo();

        vcsRepo.setIdentifier(createWorkspaceInputs.getVcsRepoId());
        vcsRepo.setOauthTokenId(createWorkspaceInputs.getOauthTokenId());
        vcsRepo.setBranch(createWorkspaceInputs.getVcsBranch());
        vcsRepo.setIngressSubmodules(Boolean.parseBoolean(createWorkspaceInputs.getIngressSubmodules()));

        attributes.setVcsRepo(vcsRepo);

        createWorkspaceData.setAttributes(attributes);
        createWorkspaceData.setType(WORKSPACE_TYPE);

        createBody.setData(createWorkspaceData);


        try {
            requestBody = createWorkspaceMapper.writeValueAsString(createBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return requestBody;

    }
}
