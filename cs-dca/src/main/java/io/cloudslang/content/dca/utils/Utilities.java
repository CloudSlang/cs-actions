/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.dca.utils;

import io.cloudslang.content.dca.models.DcaAuthModel;
import io.cloudslang.content.httpclient.HttpClientInputs;
import org.apache.http.client.utils.URIBuilder;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import static io.cloudslang.content.constants.BooleanValues.TRUE;
import static io.cloudslang.content.dca.utils.Constants.APPLICATION_JSON;
import static io.cloudslang.content.dca.utils.Constants.POST;
import static java.nio.charset.StandardCharsets.UTF_8;

public class Utilities {

    @NotNull
    public static String getIdmUrl(@NotNull final String protocol,
                                   @NotNull final String idmHostInp,
                                   @NotNull final String idmPort) {
        final URIBuilder uriBuilder = new URIBuilder();
        uriBuilder.setHost(idmHostInp);
        uriBuilder.setPort(Integer.valueOf(idmPort));
        uriBuilder.setScheme(protocol);
        uriBuilder.setPath(Constants.IDM_TOKENS_PATH);

        try {
            return uriBuilder.build().toURL().toString();
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setProxy(@NotNull final HttpClientInputs httpClientInputs,
                                @NotNull final String proxyHost,
                                @NotNull final String proxyPort,
                                @NotNull final String proxyUsername,
                                @NotNull final String proxyPassword) {
        httpClientInputs.setProxyHost(proxyHost);
        httpClientInputs.setProxyPort(proxyPort);
        httpClientInputs.setProxyUsername(proxyUsername);
        httpClientInputs.setProxyPassword(proxyPassword);
    }

    public static void setSecurityInputs(@NotNull final HttpClientInputs httpClientInputs,
                                         @NotNull final String trustAllRoots,
                                         @NotNull final String x509HostnameVerifier,
                                         @NotNull final String trustKeystore,
                                         @NotNull final String trustPassword,
                                         @NotNull final String keystore,
                                         @NotNull final String keystorePassword) {
        httpClientInputs.setTrustAllRoots(trustAllRoots);
        httpClientInputs.setX509HostnameVerifier(x509HostnameVerifier);
        httpClientInputs.setTrustKeystore(trustKeystore);
        httpClientInputs.setTrustPassword(trustPassword);
        httpClientInputs.setKeystore(keystore);
        httpClientInputs.setKeystorePassword(keystorePassword);
    }

    public static void setDcaCredentials(@NotNull final HttpClientInputs httpClientInputs,
                                         @NotNull final String dcaUsername,
                                         @NotNull final String dcaPassword,
                                         @NotNull final String dcaTenant) {
        final DcaAuthModel dcaAuthModel = new DcaAuthModel(dcaTenant);
        dcaAuthModel.setCredentials(dcaUsername, dcaPassword);
        httpClientInputs.setBody(dcaAuthModel.toJson());

        httpClientInputs.setContentType(APPLICATION_JSON);
        httpClientInputs.setResponseCharacterSet(UTF_8.toString());
        httpClientInputs.setRequestCharacterSet(UTF_8.toString());
        httpClientInputs.setFollowRedirects(TRUE);
        httpClientInputs.setMethod(POST);
    }

    public static void setIdmAuthentication(@NotNull final HttpClientInputs httpClientInputs,
                                            @NotNull final String authType,
                                            @NotNull final String idmUsername,
                                            @NotNull final String idmPassword,
                                            @NotNull final String preemptiveAuth) {
        httpClientInputs.setAuthType(authType); // todo check if IDM supports other authType
        httpClientInputs.setPreemptiveAuth(preemptiveAuth);
        httpClientInputs.setUsername(idmUsername);
        httpClientInputs.setPassword(idmPassword);
    }

    public static void setConnectionParameters(HttpClientInputs httpClientInputs,
                                               @NotNull final String connectTimeout,
                                               @NotNull final String socketTimeout,
                                               @NotNull final String useCookies,
                                               @NotNull final String keepAlive,
                                               @NotNull final String connectionsMaxPerRoot,
                                               @NotNull final String connectionsMaxTotal) {
        httpClientInputs.setConnectTimeout(connectTimeout);
        httpClientInputs.setSocketTimeout(socketTimeout);
        httpClientInputs.setUseCookies(useCookies);
        httpClientInputs.setKeepAlive(keepAlive);
        httpClientInputs.setConnectionsMaxPerRoute(connectionsMaxPerRoot);
        httpClientInputs.setConnectionsMaxTotal(connectionsMaxTotal);
    }
}
