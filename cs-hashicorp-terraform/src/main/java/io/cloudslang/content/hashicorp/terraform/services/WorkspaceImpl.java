/*
 * (c) Copyright 2022 Micro Focus, L.P.
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
import io.cloudslang.content.hashicorp.terraform.entities.TerraformWorkspaceInputs;
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
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspaceConstants.WORKSPACE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspaceConstants.WORKSPACE_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class WorkspaceImpl {

    @NotNull
    public static Map<String, String> createWorkspace(@NotNull final TerraformWorkspaceInputs createWorkspaceInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(createWorkspaceUrl(createWorkspaceInputs.getCommonInputs().getOrganizationName()));
        setCommonHttpInputs(httpClientInputs, createWorkspaceInputs.getCommonInputs());
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        if (createWorkspaceInputs.getCommonInputs().getRequestBody().equals(EMPTY)) {
            httpClientInputs.setBody(createWorkspaceBody(createWorkspaceInputs, DELIMITER));
        } else {
            httpClientInputs.setBody(createWorkspaceInputs.getCommonInputs().getRequestBody());
        }
        httpClientInputs.setResponseCharacterSet(createWorkspaceInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(createWorkspaceInputs.getCommonInputs().getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> deleteWorkspace(@NotNull final TerraformWorkspaceInputs deleteWorkspaceInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getWorkspaceDetailsUrl(deleteWorkspaceInputs.getCommonInputs().getOrganizationName(),
                deleteWorkspaceInputs.getWorkspaceName()));
        setCommonHttpInputs(httpClientInputs, deleteWorkspaceInputs.getCommonInputs());
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(DELETE);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setResponseCharacterSet(deleteWorkspaceInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(deleteWorkspaceInputs.getCommonInputs().getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> listWorkspaces(@NotNull final TerraformCommonInputs listWorkspacesInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(createWorkspaceUrl(listWorkspacesInputs.getOrganizationName()));
        setCommonHttpInputs(httpClientInputs, listWorkspacesInputs);
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        httpClientInputs.setQueryParams(getQueryParams(listWorkspacesInputs.getPageNumber(),
                listWorkspacesInputs.getPageSize()));
        httpClientInputs.setResponseCharacterSet(listWorkspacesInputs.getResponseCharacterSet());
        httpClientInputs.setHeaders(getAuthHeaders(listWorkspacesInputs.getAuthToken()));
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static Map<String, String> getWorkspaceDetails(@NotNull final TerraformWorkspaceInputs
                                                                  getWorkspaceDetailsInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getWorkspaceDetailsUrl(getWorkspaceDetailsInputs.getCommonInputs().getOrganizationName()
                , getWorkspaceDetailsInputs.getWorkspaceName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setHeaders(getAuthHeaders(getWorkspaceDetailsInputs.getCommonInputs().getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, getWorkspaceDetailsInputs.getCommonInputs());
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
                .append(WORKSPACE_PATH)
                .append(PATH_SEPARATOR)
                .append(workspaceName);
        return pathString.toString();
    }

    @NotNull
    public static String createWorkspaceBody(TerraformWorkspaceInputs createWorkspaceInputs, String delimiter) {
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
