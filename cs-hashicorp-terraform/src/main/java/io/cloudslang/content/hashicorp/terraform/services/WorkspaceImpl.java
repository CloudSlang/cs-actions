

package io.cloudslang.content.hashicorp.terraform.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformWorkspaceInputs;
import io.cloudslang.content.hashicorp.terraform.services.models.workspace.CreateWorkspaceRequestBody;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.hc.core5.net.URIBuilder;
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
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        httpClientInputs.url(createWorkspaceUrl(createWorkspaceInputs.getCommonInputs().getOrganizationName()));
        setCommonHttpInputs(httpClientInputs, createWorkspaceInputs.getCommonInputs());
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(POST);
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        if (createWorkspaceInputs.getCommonInputs().getRequestBody().equals(EMPTY)) {
            httpClientInputs.body(createWorkspaceBody(createWorkspaceInputs, DELIMITER));
        } else {
            httpClientInputs.body(createWorkspaceInputs.getCommonInputs().getRequestBody());
        }
        httpClientInputs.responseCharacterSet(createWorkspaceInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(createWorkspaceInputs.getCommonInputs().getAuthToken()));
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> deleteWorkspace(@NotNull final TerraformWorkspaceInputs deleteWorkspaceInputs)
            throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        httpClientInputs.url(getWorkspaceDetailsUrl(deleteWorkspaceInputs.getCommonInputs().getOrganizationName(),
                deleteWorkspaceInputs.getWorkspaceName()));
        setCommonHttpInputs(httpClientInputs, deleteWorkspaceInputs.getCommonInputs());
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(DELETE);
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        httpClientInputs.responseCharacterSet(deleteWorkspaceInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(deleteWorkspaceInputs.getCommonInputs().getAuthToken()));
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> listWorkspaces(@NotNull final TerraformCommonInputs listWorkspacesInputs)
            throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        httpClientInputs.url(createWorkspaceUrl(listWorkspacesInputs.getOrganizationName()));
        setCommonHttpInputs(httpClientInputs, listWorkspacesInputs);
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        httpClientInputs.queryParams(getQueryParams(listWorkspacesInputs.getPageNumber(),
                listWorkspacesInputs.getPageSize()));
        httpClientInputs.responseCharacterSet(listWorkspacesInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(listWorkspacesInputs.getAuthToken()));
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> getWorkspaceDetails(@NotNull final TerraformWorkspaceInputs
                                                                  getWorkspaceDetailsInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        httpClientInputs.url(getWorkspaceDetailsUrl(getWorkspaceDetailsInputs.getCommonInputs().getOrganizationName()
                , getWorkspaceDetailsInputs.getWorkspaceName()));
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.headers(getAuthHeaders(getWorkspaceDetailsInputs.getCommonInputs().getAuthToken()));
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, getWorkspaceDetailsInputs.getCommonInputs());
        return HttpClientService.execute(httpClientInputs.build());
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
