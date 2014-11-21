/*
 * Licensed to Hewlett-Packard Development Company, L.P. under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
*/
package org.eclipse.score.content.httpclient.build;

import org.eclipse.score.content.httpclient.build.auth.AuthTypes;
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
        if (authTypes.size() == 1 && Boolean.parseBoolean(preemptiveAuth)) {
            AuthCache authCache = new BasicAuthCache();
            authCache.put(new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()),
                    authSchemeLookup.lookup(authTypes.iterator().next()).create(context));
            context.setCredentialsProvider(credentialsProvider);
            context.setAuthCache(authCache);
        }
        return context;
    }
}
