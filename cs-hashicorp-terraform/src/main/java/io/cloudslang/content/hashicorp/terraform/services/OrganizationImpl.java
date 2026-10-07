

package io.cloudslang.content.hashicorp.terraform.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformOrganizationInputs;
import io.cloudslang.content.hashicorp.terraform.services.models.organization.CreateOrganizationRequestBody;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.CreateOrganizationConstants.ORGANIZATION_TYPE;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;


public class OrganizationImpl {

    @NotNull
    public static Map<String, String> createOrganization(@NotNull final TerraformOrganizationInputs createOrganizationInputs)
            throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        httpClientInputs.url(createOrganizationUrl(createOrganizationInputs.getCommonInputs().getOrganizationName()));
        setCommonHttpInputs(httpClientInputs, createOrganizationInputs.getCommonInputs());
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(POST);
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        if (createOrganizationInputs.getCommonInputs().getRequestBody().equals(EMPTY)) {
            httpClientInputs.body(createOrganizationBody(createOrganizationInputs, DELIMITER));
        } else {
            httpClientInputs.body(createOrganizationInputs.getCommonInputs().getRequestBody());
        }
        httpClientInputs.responseCharacterSet(createOrganizationInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(createOrganizationInputs.getCommonInputs().getAuthToken()));
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> updateOrganization(@NotNull final TerraformOrganizationInputs createOrganizationInputs)
            throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        httpClientInputs.url(getOrganizationDetailsUrl(createOrganizationInputs.getCommonInputs().getOrganizationName()));
        setCommonHttpInputs(httpClientInputs, createOrganizationInputs.getCommonInputs());
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(PATCH);
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        if (createOrganizationInputs.getCommonInputs().getRequestBody().equals(EMPTY)) {
            httpClientInputs.body(createOrganizationBody(createOrganizationInputs, DELIMITER));
        } else {
            httpClientInputs.body(createOrganizationInputs.getCommonInputs().getRequestBody());
        }
        httpClientInputs.responseCharacterSet(createOrganizationInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(createOrganizationInputs.getCommonInputs().getAuthToken()));
        return HttpClientService.execute(httpClientInputs.build());
    }


    @NotNull
    public static Map<String, String> deleteOrganization(@NotNull final TerraformOrganizationInputs deleteOrganizationInputs)
            throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        httpClientInputs.url(getOrganizationDetailsUrl(deleteOrganizationInputs.getCommonInputs().getOrganizationName()));
        setCommonHttpInputs(httpClientInputs, deleteOrganizationInputs.getCommonInputs());
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(DELETE);
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        httpClientInputs.responseCharacterSet(deleteOrganizationInputs.getCommonInputs().getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(deleteOrganizationInputs.getCommonInputs().getAuthToken()));
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> listOrganizations(@NotNull final TerraformCommonInputs listOrganizationsInputs)
            throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        httpClientInputs.url(createOrganizationUrl(listOrganizationsInputs.getOrganizationName()));
        setCommonHttpInputs(httpClientInputs, listOrganizationsInputs);
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        httpClientInputs.queryParams(getQueryParams(listOrganizationsInputs.getPageNumber(),
                listOrganizationsInputs.getPageSize()));
        httpClientInputs.responseCharacterSet(listOrganizationsInputs.getResponseCharacterSet());
        httpClientInputs.headers(getAuthHeaders(listOrganizationsInputs.getAuthToken()));
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    public static Map<String, String> getOrganizationDetails(@NotNull final TerraformOrganizationInputs
                                                                     getOrganizationDetailsInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        httpClientInputs.url(getOrganizationDetailsUrl(getOrganizationDetailsInputs.getCommonInputs().getOrganizationName()));
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.headers(getAuthHeaders(getOrganizationDetailsInputs.getCommonInputs().getAuthToken()));
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, getOrganizationDetailsInputs.getCommonInputs());
        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    private static String createOrganizationUrl(@NotNull final String organizationName) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getOrganizationPath(organizationName));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getOrganizationPath(@NotNull final String organizationName) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(ORGANIZATION_PATH);
        return pathString.toString();
    }

    @NotNull
    private static String getOrganizationDetailsUrl(@NotNull final String organizationName) throws Exception {
        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getOrganizationDetailsPath(organizationName));
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static String getOrganizationDetailsPath(@NotNull final String organizationName) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(ORGANIZATION_PATH)
                .append(organizationName);
        return pathString.toString();
    }


    @NotNull
    public static String createOrganizationBody(TerraformOrganizationInputs createOrganizationInputs, String delimiter) {
        String requestBody = EMPTY;
        ObjectMapper createOrganizationMapper = new ObjectMapper();
        CreateOrganizationRequestBody createBody = new CreateOrganizationRequestBody();
        CreateOrganizationRequestBody.CreateOrganizationData createOrganizationData = createBody.new CreateOrganizationData();
        CreateOrganizationRequestBody.Attributes attributes = createBody.new Attributes();
        attributes.setName(createOrganizationInputs.getCommonInputs().getOrganizationName());
        attributes.setDescription(createOrganizationInputs.getOrganizationDescription());
        attributes.setEmail(createOrganizationInputs.getEmail());
        if (!EMPTY.equals(createOrganizationInputs.getSessionTimeout())) {
            attributes.setSessionTimeout(createOrganizationInputs.getSessionTimeout());
        } else {
            attributes.setSessionTimeout(SESSION_TIMEOUT);
        }
        if (!EMPTY.equals(createOrganizationInputs.getSessionRemember())) {
            attributes.setSessionRemember(createOrganizationInputs.getSessionRemember());
        } else {
            attributes.setSessionRemember(SESSION_REMEMBER);
        }
        if (!EMPTY.equals(createOrganizationInputs.getCollaboratorAuthPolicy())){
            attributes.setCollaboratorAuthPolicy(createOrganizationInputs.getCollaboratorAuthPolicy());
        } else {
            attributes.setCollaboratorAuthPolicy(COLLABORATOR_AUTH_POLICY);
        }
        attributes.setCostEstimationEnabled(Boolean.parseBoolean(createOrganizationInputs.getCostEstimationEnabled()));
        attributes.setOwnersTeamSamlRoleId(createOrganizationInputs.getOwnersTeamSamlRoleId());

        createOrganizationData.setAttributes(attributes);
        createOrganizationData.setType(ORGANIZATION_TYPE);

        createBody.setData(createOrganizationData);


        try {
            requestBody = createOrganizationMapper.writeValueAsString(createBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return requestBody;

    }
}

