package io.cloudslang.content.httpclient.services;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.AuthScheme;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.impl.auth.DigestScheme;
import org.apache.hc.client5.http.impl.auth.NTLMScheme;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpHost;

import java.net.URI;

import static io.cloudslang.content.httpclient.utils.Constants.ANONYMOUS;

public class CustomHttpClientContext {

    public static HttpClientContext getHttpClientContext(HttpClientInputs httpClientInputs, CredentialsProvider credentialsProvider, URI uri) {

        HttpClientContext localContext = HttpClientContext.create();
        if (Boolean.parseBoolean(httpClientInputs.getPreemptiveAuth()) && !httpClientInputs.getAuthType().equalsIgnoreCase(ANONYMOUS)) {

            AuthCache authCache = new BasicAuthCache();
            HttpHost targetHost = new HttpHost(uri.getScheme(), uri.getHost(), uri.getPort() == -1 ? 443 : uri.getPort());

            authCache.put(targetHost, getAuthScheme(httpClientInputs.getAuthType()));

            localContext.setAuthCache(authCache);
            localContext.setCredentialsProvider(credentialsProvider);
            return localContext;
        }
        return localContext;
    }

    private static AuthScheme getAuthScheme(String authType) {
        switch (authType.toUpperCase()) {
            case "NTLM":
                return new NTLMScheme();
            case "DIGEST":
                return new DigestScheme();
            default:
                return new BasicScheme();
        }
    }
}
