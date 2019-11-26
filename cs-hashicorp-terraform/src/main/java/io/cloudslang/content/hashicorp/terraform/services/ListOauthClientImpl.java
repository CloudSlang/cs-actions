package io.cloudslang.content.hashicorp.terraform.services;



import java.util.Map;

import io.cloudslang.content.hashicorp.terraform.entities.ListOAuthClientInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import org.apache.http.client.utils.URIBuilder;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.jetbrains.annotations.NotNull;
import static io.cloudslang.content.hashicorp.terraform.services.HttpCommons.setCommonHttpInputs;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.*;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;

public class ListOauthClientImpl {
    @NotNull
    public static Map<String, String> listOAuthClient(@NotNull final ListOAuthClientInputs listOAuthInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = listOAuthInputs.getCommonInputs();
        httpClientInputs.setUrl(listOAuthClientUrl(commonInputs.getOrganizationName()));
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        setCommonHttpInputs(httpClientInputs, commonInputs);

        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String listOAuthClientUrl(@NotNull final String organizationName) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();
        uriBuilder.setPath(getListOAuthClientPath(organizationName));

        return uriBuilder.build().toURL().toString();
    }
}
