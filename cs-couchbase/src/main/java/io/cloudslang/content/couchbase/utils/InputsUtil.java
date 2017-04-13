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

import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.START_INDEX;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.http.client.config.AuthSchemes.BASIC;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */

public class InputsUtil {
    private InputsUtil() {
        // prevent instantiation
    }

    public static HttpClientInputs getHttpClientInputs(String username, String password, String proxyHost, String proxyPort,
                                                       String proxyUsername, String proxyPassword, String trustAllRoots,
                                                       String x509HostnameVerifier, String trustKeystore, String trustPassword,
                                                       String keystore, String keystorePassword, String connectTimeout,
                                                       String socketTimeout, String useCookies, String keepAlive, String method) {
        HttpClientInputs httpClientInputs = new HttpClientInputs();
        httpClientInputs.setMethod(method);
        httpClientInputs.setAuthType(BASIC);
        httpClientInputs.setQueryParamsAreURLEncoded(Boolean.FALSE.toString());
        httpClientInputs.setUsername(username);
        httpClientInputs.setPassword(password);

        httpClientInputs.setTrustAllRoots(valueOf(getEnforcedBooleanCondition(trustAllRoots, true)));
        httpClientInputs.setKeepAlive(valueOf(getEnforcedBooleanCondition(keepAlive, true)));
        httpClientInputs.setUseCookies(valueOf(getEnforcedBooleanCondition(useCookies, true)));

        if (isNotBlank(proxyHost) && isNotBlank(proxyPort)) {
            httpClientInputs.setProxyHost(proxyHost);
            httpClientInputs.setProxyPort(proxyPort);
        }

        if (isNotBlank(proxyUsername) && isNotBlank(proxyPassword)) {
            httpClientInputs.setProxyUsername(proxyUsername);
            httpClientInputs.setProxyPassword(proxyPassword);
        }

        if (isNotBlank(trustKeystore) && isNotBlank(trustPassword)) {
            httpClientInputs.setTrustKeystore(trustKeystore);
            httpClientInputs.setTrustPassword(trustPassword);
        }

        if (isNotBlank(keystore) && isNotBlank(keystorePassword)) {
            httpClientInputs.setKeystore(keystore);
            httpClientInputs.setKeystorePassword(keystorePassword);
        }

        httpClientInputs.setConnectTimeout(getDefaultStringInput(connectTimeout, valueOf(START_INDEX)));
        httpClientInputs.setSocketTimeout(getDefaultStringInput(socketTimeout, valueOf(START_INDEX)));

        if (isBlank(x509HostnameVerifier) || (!"strict".equalsIgnoreCase(x509HostnameVerifier) && !"browser_compatible".equalsIgnoreCase(x509HostnameVerifier))) {
            httpClientInputs.setX509HostnameVerifier("allow_all");
        } else {
            httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        }

        return httpClientInputs;
    }

    public static String getUrl(String input) throws MalformedURLException {
        return new URL(input).toString();
    }

    public static String toAppend(String prefix, String suffix, String action) {
        if (isBlank(suffix)) {
            return prefix;
        }

        return prefix + "/" + suffix + UriSuffix.getUriSuffix(action);
    }

    private static String getDefaultStringInput(String input, String defaultValue) {
        return isBlank(input) ? defaultValue : input;
    }

    /**
     * If enforcedBoolean is "true" and string input is: null, empty, many empty chars, TrUe, tRuE... but not "false"
     * then returns "true".
     * If enforcedBoolean is "false" and string input is: null, empty, many empty chars, FaLsE, fAlSe... but not "true"
     * then returns "false"
     * This behavior is needed for inputs like: "imageNoReboot" when we want them to be set to "true" disregarding the
     * value provided (null, empty, many empty chars, TrUe, tRuE) except the case when is "false"
     *
     * @param input           String to be evaluated.
     * @param enforcedBoolean Enforcement boolean.
     * @return A boolean according with above description.
     */
    private static boolean getEnforcedBooleanCondition(String input, boolean enforcedBoolean) {
        return (enforcedBoolean) ? isTrueOrFalse(input) == Boolean.parseBoolean(input) : Boolean.parseBoolean(input);
    }

    private static boolean isTrueOrFalse(String input) {
        return Boolean.FALSE.toString().equalsIgnoreCase(input) || Boolean.TRUE.toString().equalsIgnoreCase(input);
    }
}