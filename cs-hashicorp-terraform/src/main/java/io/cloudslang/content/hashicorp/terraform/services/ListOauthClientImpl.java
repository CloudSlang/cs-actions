

package io.cloudslang.content.hashicorp.terraform.services;

import java.util.Map;

import io.cloudslang.content.hashicorp.terraform.entities.ListOAuthClientInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import org.apache.hc.core5.net.URIBuilder;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.jetbrains.annotations.NotNull;
import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.ListOAuthClientConstants.OAUTH_CLIENT_PATH;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;

public class ListOauthClientImpl {
    @NotNull
    public static Map<String, String> listOAuthClient(@NotNull final ListOAuthClientInputs listOAuthInputs) throws Exception {
        final HttpClientInputs.HttpClientInputsBuilder httpClientInputs = HttpClientInputs.builder();
        final TerraformCommonInputs commonInputs = listOAuthInputs.getCommonInputs();
        httpClientInputs.url(listOAuthClientUrl(commonInputs.getOrganizationName()));
        httpClientInputs.authType(ANONYMOUS);
        httpClientInputs.method(GET);
        httpClientInputs.headers(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.contentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);

        return HttpClientService.execute(httpClientInputs.build());
    }

    @NotNull
    private static String listOAuthClientUrl(@NotNull final String organizationName) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getListOAuthClientPath(organizationName));

        return uriBuilder.build().toURL().toString();
    }
    @NotNull
    public static String getListOAuthClientPath(@NotNull String organizationName) {
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(ORGANIZATION_PATH)
                .append(organizationName)
                .append(OAUTH_CLIENT_PATH);
        return pathString.toString();
    }
}
