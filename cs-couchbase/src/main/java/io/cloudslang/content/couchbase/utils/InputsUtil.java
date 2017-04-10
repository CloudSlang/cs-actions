/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.utils;

import io.cloudslang.content.couchbase.entities.couchbase.UriSuffix;
import io.cloudslang.content.httpclient.HttpClientInputs;

import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.http.client.config.AuthSchemes.BASIC;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.SLASH;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */

public class InputsUtil {
    private InputsUtil() {
        // prevent instantiation
    }

    public static HttpClientInputs getHttpClientInputs(String username, String password, String proxyHost, String proxyPort,
                                                       String proxyUsername, String proxyPassword, String method) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);
        httpClientInputs.setProxyHost(proxyHost);
        httpClientInputs.setProxyPort(proxyPort);
        httpClientInputs.setProxyUsername(proxyUsername);
        httpClientInputs.setProxyPassword(proxyPassword);
        httpClientInputs.setMethod(method);
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setQueryParamsAreURLEncoded(Boolean.FALSE.toString());

        return httpClientInputs;
    }

    public static String getUrl(String input) throws MalformedURLException {
        return new URL(input).toString();
    }

    public static String toAppend(String prefix, String suffix, String action) {
        if (isBlank(suffix)) {
            return prefix;
        }

        return prefix + SLASH + suffix + UriSuffix.getUriSuffix(action);
    }
}