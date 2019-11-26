package io.cloudslang.content.hashicorp.terraform.services;

import com.sun.istack.internal.NotNull;
import io.cloudslang.content.hashicorp.terraform.entities.CreateRunInputs;
import io.cloudslang.content.hashicorp.terraform.entities.TerraformCommonInputs;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import org.apache.http.client.utils.URIBuilder;

import java.util.Map;

import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getAuthHeaders;
import static io.cloudslang.content.hashicorp.terraform.utils.HttpUtils.getUriBuilder;
import static io.cloudslang.content.hashicorp.terraform.utils.Constants.Common.*;

public class CreateRunImpl {
    @NotNull
    public static Map<String, String> createRunClient(@NotNull final CreateRunInputs createRunInputs) throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        final TerraformCommonInputs commonInputs = createRunInputs.getCommonInputs();
        httpClientInputs.setUrl(createRunClientUrl(createRunInputs.getWorkspaceId(), createRunInputs.getRunMessage(), createRunInputs.getIsDestroy()));
        httpClientInputs.setBody("{}");
        httpClientInputs.setAuthType(ANONYMOUS);
        httpClientInputs.setMethod(POST);
        httpClientInputs.setHeaders(getAuthHeaders(commonInputs.getAuthToken()));
        httpClientInputs.setContentType(APPLICATION_VND_API_JSON);
        System.out.println(httpClientInputs.getUrl());
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    private static String createRunClientUrl(@NotNull final String workspaceId, @NotNull final String runMessage, @NotNull final String isDestroy) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder();

        return uriBuilder.build().toURL().toString() + workspaceId + "/oauth-clients";
    }
}
