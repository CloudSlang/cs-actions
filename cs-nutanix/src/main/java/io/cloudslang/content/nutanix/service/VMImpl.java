package io.cloudslang.content.nutanix.service;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import io.cloudslang.content.httpclient.services.HttpClientService;
import io.cloudslang.content.nutanix.entities.NutanixCommonInputs;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static io.cloudslang.content.nutanix.service.HttpCommons.setCommonHttpInputs;

import static io.cloudslang.content.nutanix.utils.Constants.Common.*;

public class VMImpl {

    @NotNull
    public static Map<String, String> listVMs(@NotNull final NutanixCommonInputs nutanixCommonInputs)
            throws Exception {
        final HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUrl(getListVMsURL(nutanixCommonInputs));
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setMethod(GET);
        httpClientInputs.setUsername(nutanixCommonInputs.getUsername());
        httpClientInputs.setPassword(nutanixCommonInputs.getPassword());
        httpClientInputs.setContentType(APPLICATION_API_JSON);
        setCommonHttpInputs(httpClientInputs, nutanixCommonInputs);
        return new HttpClientService().execute(httpClientInputs);
    }

    @NotNull
    public static String getListVMsURL(NutanixCommonInputs nutanixCommonInputs) throws Exception {

        final URIBuilder uriBuilder = getUriBuilder(nutanixCommonInputs);
        StringBuilder pathString = new StringBuilder()
                .append(API)
                .append(API_VERSION)
                .append(PATH_SEPARATOR)
                .append("vms");
        uriBuilder.setPath(pathString.toString());
        return uriBuilder.build().toURL().toString();
    }

    @NotNull
    public static URIBuilder getUriBuilder(NutanixCommonInputs nutanixCommonInputs) {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(nutanixCommonInputs.getHostname());
        uriBuilder.setPort(Integer.parseInt(nutanixCommonInputs.getPort()));
        uriBuilder.setScheme(nutanixCommonInputs.getProtocol());
        return uriBuilder;
    }
}
