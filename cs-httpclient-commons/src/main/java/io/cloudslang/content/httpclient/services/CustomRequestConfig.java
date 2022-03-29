package io.cloudslang.content.httpclient.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.cookie.StandardCookieSpec;
import org.apache.hc.core5.http.HttpHost;

import java.util.Collections;

import static org.apache.commons.lang3.StringUtils.EMPTY;
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
            case "ANONYMOUS":
                return EMPTY;
            default:
                throw new IllegalStateException("Unexpected value: " + authType.toUpperCase());
        }
    }

    public static RequestConfig getDefaultRequestConfig(HttpClientInputs httpClientInputs) {
        String authType = getAuthType(httpClientInputs.getAuthType());
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom()
                .setCookieSpec(StandardCookieSpec.STRICT)
                .setExpectContinueEnabled(true);
//                .setConnectTimeout()
//                .setRedirectsEnabled()
//                .setResponseTimeout()
//                .setAuthenticationEnabled()



        if (!authType.isEmpty()) {
            requestConfigBuilder.setTargetPreferredAuthSchemes(Collections.singletonList(authType));
        }
        if (!httpClientInputs.getProxyHost().isEmpty()) {
            requestConfigBuilder.setProxyPreferredAuthSchemes(Collections.singletonList(BASIC));
            requestConfigBuilder.setProxy(new HttpHost("myproxy", 8080));
        }


        return requestConfigBuilder.build();
    }
}
