package io.cloudslang.content.httpclient.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.Timeout;
import java.util.Arrays;
import java.util.Collections;

import static io.cloudslang.content.httpclient.utils.Constants.ANONYMOUS;
import static io.cloudslang.content.httpclient.utils.Constants.ANY;
import static org.apache.hc.client5.http.auth.StandardAuthScheme.*;

public class CustomRequestConfig {

    private static String getAuthType(String authType) {
        switch (authType.toUpperCase()) {
            case "BASIC":
                return BASIC;
            case "NTLM":
                return NTLM;
            case "DIGEST":
                return DIGEST;
            case "ANY":
                return ANY;
            case "ANONYMOUS":
                return ANONYMOUS;
            default:
                throw new IllegalStateException("Unexpected value: " + authType.toUpperCase());
        }
    }

    public static RequestConfig getDefaultRequestConfig(HttpClientInputs httpClientInputs){
        RequestConfig.Builder requestConfigBuilder;
        String authType = getAuthType(httpClientInputs.getAuthType());

        requestConfigBuilder = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(Long.parseLong((httpClientInputs.getConnectTimeout()))))
                .setResponseTimeout(Timeout.ofSeconds(Long.parseLong((httpClientInputs.getResponseTimeout()))))
                .setRedirectsEnabled(Boolean.parseBoolean(httpClientInputs.getFollowRedirects()));

        if (!authType.equalsIgnoreCase(ANONYMOUS))
            if (authType.equalsIgnoreCase(ANY))
                requestConfigBuilder.setTargetPreferredAuthSchemes(Arrays.asList(BASIC, NTLM, DIGEST));
            else
                requestConfigBuilder.setTargetPreferredAuthSchemes(Collections.singletonList(authType));

        if (!httpClientInputs.getProxyHost().isEmpty()) {
            requestConfigBuilder.setProxyPreferredAuthSchemes(Collections.singletonList(BASIC));
            requestConfigBuilder.setProxy(new HttpHost(httpClientInputs.getProxyHost(), httpClientInputs.getProxyPort()));
        }
        return requestConfigBuilder.build();
    }
}
