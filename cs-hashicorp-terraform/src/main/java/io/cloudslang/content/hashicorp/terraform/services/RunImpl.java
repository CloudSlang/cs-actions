

package io.cloudslang.content.hashicorp.terraform.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformRunInputs;
import io.cloudslang.content.hashicorp.terraform.services.models.runs.ApplyRunRequestBody;
import io.cloudslang.content.hashicorp.terraform.services.models.runs.CreateRunBody;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.ApplyRunConstants.APPLY_RUN_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CancelRunConstants.CANCEL_RUN_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateRunConstants.RUN_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateRunConstants.RUN_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspaceConstants.WORKSPACE_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateWorkspaceConstants.WORKSPACE_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.GetApplyDetailsConstants.APPLY_DETAILS_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.PlanDetailsConstants.PLAN_DETAILS_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class RunImpl {
    @NotNull
    public static Map<String, String> createRunClient(@NotNull final TerraformRunInputs createRunInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final TerraformCommonInputs commonInputs = createRunInputs.getCommonInputs();
        httpClientInputs.url(createRunClientUrl());
        if (commonInputs.getRequestBody().isEmpty()) {
            try {
                httpClientInputs.body(createRunBody(createRunInputs));
            } catch (JsonProcessingException e) {
                return getFailureResultsMap(e);
            }
        } else {
            httpClientInputs.body(commonInputs.getRequestBody());
        }
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(POST);
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> applyRunClient(@NotNull final TerraformRunInputs applyRunInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final TerraformCommonInputs commonInputs = applyRunInputs.getCommonInputs();
        httpClientInputs.url(applyRunClientUrl(applyRunInputs.getRunId()));
        if (commonInputs.getRequestBody().isEmpty()) {
            try {
                httpClientInputs.body(applyRunBody(applyRunInputs));
            } catch (JsonProcessingException e) {
                return getFailureResultsMap(e);
            }
        } else {
            httpClientInputs.body(commonInputs.getRequestBody());
        }
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(POST);
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> listRunsInWorkspaceClient(@NotNull final TerraformRunInputs listRunsInWorkspaceInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final TerraformCommonInputs commonInputs = listRunsInWorkspaceInputs.getCommonInputs();
        httpClientInputs.url(listRunsInWorkspaceClientUrl(listRunsInWorkspaceInputs.getWorkspaceId()));
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        httpClientInputs.queryParams(getQueryParams(listRunsInWorkspaceInputs.getCommonInputs().getPageNumber(),
                listRunsInWorkspaceInputs.getCommonInputs().getPageSize()));
        httpClientInputs.responseCharacterSet(commonInputs.getResponseCharacterSet());
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> getRunDetails(@NotNull final TerraformRunInputs getRunDetailsInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final TerraformCommonInputs commonInputs = getRunDetailsInputs.getCommonInputs();
        httpClientInputs.url(getRunDetailsUrl(getRunDetailsInputs.getRunId()));
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return HttpClientService.execute(httpClientInputs.build());
    }

  @NotNull
    public static Map<String, String> planDetails(@NotNull final TerraformRunInputs planDetailsInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final TerraformCommonInputs commonInputs = planDetailsInputs.getCommonInputs();
        httpClientInputs.url(planDetailsUrl(planDetailsInputs.getPlanId()));
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return HttpClientService.execute(httpClientInputs.build());
    }
  
    public static Map<String, String> cancelRun(@NotNull final TerraformRunInputs cancelRunInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final TerraformCommonInputs commonInputs = cancelRunInputs.getCommonInputs();
        httpClientInputs.url(cancelRunUrl(cancelRunInputs.getRunId()));
        if (commonInputs.getRequestBody().isEmpty()) {
            try {
                httpClientInputs.body(applyRunBody(cancelRunInputs));
            } catch (JsonProcessingException e) {
                return getFailureResultsMap(e);
            }
        } else {
            httpClientInputs.body(commonInputs.getRequestBody());
        }
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(POST);
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> getApplyDetails(@NotNull final TerraformRunInputs getApplyDetailsInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final TerraformCommonInputs commonInputs = getApplyDetailsInputs.getCommonInputs();
        httpClientInputs.url(getApplyDetailsUrl(getApplyDetailsInputs.getApplyIdId()));
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);
        return HttpClientService.execute(httpClientInputs.build());
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
    public static String planDetailsUrl(@NotNull final String planId) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(PLAN_DETAILS_PATH)
                .append(PATH_SEPARATOR)
                .append(planId);
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    public static String cancelRunUrl(@NotNull final String runId) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(RUN_PATH)
                .append(PATH_SEPARATOR)
                .append(runId)
                .append(CANCEL_RUN_PATH);
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
