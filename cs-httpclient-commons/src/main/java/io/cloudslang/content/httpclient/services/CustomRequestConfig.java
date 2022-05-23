package io.cloudslang.content.httpclient.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;

import java.util.Collections;

import static io.cloudslang.content.httpclient.utils.Constants.ANONYMOUS;
import static org.apache.hc.client5.http.auth.StandardAuthScheme.*;

public class CustomRequestConfig {

    private static String getAuthType(String authType) {
        switch (authType.toUpperCase()) {
            case "NTLM":
                return NTLM;
            case "DIGEST":
                return DIGEST;
            case "ANONYMOUS":
                return ANONYMOUS;
            default:
                return BASIC;
        }
    }

    public static RequestConfig getDefaultRequestConfig(HttpClientInputs httpClientInputs) {
        RequestConfig.Builder requestConfigBuilder;
        String authType = getAuthType(httpClientInputs.getAuthType());

        requestConfigBuilder = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(Long.parseLong((httpClientInputs.getConnectTimeout()))))
                .setResponseTimeout(Timeout.ofSeconds(Long.parseLong((httpClientInputs.getResponseTimeout()))))
                .setRedirectsEnabled(Boolean.parseBoolean(httpClientInputs.getFollowRedirects()));

        if (Boolean.parseBoolean(httpClientInputs.getKeepAlive()))
            requestConfigBuilder.setConnectionKeepAlive((TimeValue.ofSeconds(-1)));

        if (!authType.equalsIgnoreCase(ANONYMOUS))
            requestConfigBuilder.setTargetPreferredAuthSchemes(Collections.singletonList(authType));

        if (!httpClientInputs.getProxyHost().isEmpty()) {
            requestConfigBuilder.setProxyPreferredAuthSchemes(Collections.singletonList(BASIC));
            requestConfigBuilder.setProxy(new HttpHost(httpClientInputs.getProxyHost(), Integer.parseInt(httpClientInputs.getProxyPort())));
        }
        return requestConfigBuilder.build();
    }
}
