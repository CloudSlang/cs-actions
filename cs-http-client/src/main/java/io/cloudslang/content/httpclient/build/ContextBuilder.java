/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.httpclient.build;

import io.cloudslang.content.httpclient.build.auth.AuthTypes;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.impl.client.BasicAuthCache;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 10/7/14
 */
public class ContextBuilder {
    private Lookup<AuthSchemeProvider> authSchemeLookup;
    private URI uri;
    private AuthTypes authTypes;
    private CredentialsProvider credentialsProvider;
    private String preemptiveAuth;

    public ContextBuilder setAuthSchemeLookup(Lookup<AuthSchemeProvider> authSchemeLookup) {
        this.authSchemeLookup = authSchemeLookup;
        return this;
    }

    public ContextBuilder setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public ContextBuilder setAuthTypes(AuthTypes authTypes) {
        this.authTypes = authTypes;
        return this;
    }

    public ContextBuilder setCredentialsProvider(CredentialsProvider credentialsProvider) {
        this.credentialsProvider = credentialsProvider;
        return this;
    }

    public ContextBuilder setPreemptiveAuth(String preemptiveAuth) {
        this.preemptiveAuth = preemptiveAuth;
        return this;
    }

    public HttpClientContext build() {
        if (StringUtils.isEmpty(preemptiveAuth)) {
            preemptiveAuth = "true";
        }
        HttpClientContext context = HttpClientContext.create();
        if (authTypes.size() == 1 && Boolean.parseBoolean(preemptiveAuth) && !authTypes.contains(AuthTypes.ANONYMOUS)) {
            AuthCache authCache = new BasicAuthCache();
            authCache.put(new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()),
                    authSchemeLookup.lookup(authTypes.iterator().next()).create(context));
            context.setCredentialsProvider(credentialsProvider);
            context.setAuthCache(authCache);
        }
        return context;
    }
}
