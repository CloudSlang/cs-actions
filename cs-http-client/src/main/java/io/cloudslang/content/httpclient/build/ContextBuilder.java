/*
 * Copyright 2022-2024 Open Text
 * This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */





package io.cloudslang.content.httpclient.build;

import io.cloudslang.content.httpclient.build.auth.AuthTypes;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthCache;
import org.apache.hc.client5.http.auth.AuthSchemeFactory;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.impl.auth.BasicAuthCache;
import org.apache.hc.client5.http.impl.auth.BasicScheme;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.config.Lookup;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 10/7/14
 */
public class ContextBuilder {
    private Lookup<AuthSchemeFactory> authSchemeLookup;
    private URI uri;
    private AuthTypes authTypes;
    private CredentialsProvider credentialsProvider;
    private String preemptiveAuth;

    public ContextBuilder setAuthSchemeLookup(Lookup<AuthSchemeFactory> authSchemeLookup) {
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
        context.setCredentialsProvider(credentialsProvider);
        if (authTypes.size() == 1 && Boolean.parseBoolean(preemptiveAuth) && !authTypes.contains(AuthTypes.ANONYMOUS)) {
            try {
                AuthCache authCache = new BasicAuthCache();
                HttpHost targetHost = new HttpHost(uri.getScheme(), uri.getHost(), uri.getPort());
                AuthSchemeFactory factory = authSchemeLookup.lookup(authTypes.iterator().next());
                if (factory != null) {
                    authCache.put(targetHost, factory.create(context));
                    context.setAuthCache(authCache);
                }
            } catch (Exception ignored) {
                // Preemptive auth setup is best-effort; fall back to challenge-response
            }
        }
        return context;
    }
}
