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
package com.hp.score.content.httpclient.build.auth;

import com.hp.score.content.httpclient.build.auth.AuthTypes;
import com.hp.score.content.httpclient.build.auth.CredentialsProviderBuilder;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/2/14
 */
public class CredentialsProviderBuilderTest {

    @Test
    public void createNtlmCredentialsProvider() {
        CredentialsProvider credentialsProvider = getCredentialsProvider(AuthSchemes.NTLM);
        Credentials credentials = credentialsProvider.getCredentials(new AuthScope("host", 80));

        assertThat(credentials, instanceOf(NTCredentials.class));
        NTCredentials ntCredentials = (NTCredentials) credentials;
        assertEquals("DOMAIN", ntCredentials.getDomain());
        assertEquals("HOST", ntCredentials.getWorkstation());
        assertEquals("pass", ntCredentials.getPassword());
        Credentials proxyCredentials = credentialsProvider.getCredentials(new AuthScope("proxy", 8080));
        assertThat(proxyCredentials, instanceOf(UsernamePasswordCredentials.class));
        UsernamePasswordCredentials userCredentials = (UsernamePasswordCredentials) proxyCredentials;
        assertEquals("proxyUsername", userCredentials.getUserName());
    }

    @Test
    public void createKerberosCredentialsProvider() {
        CredentialsProvider credentialsProvider = getCredentialsProvider(AuthSchemes.KERBEROS);
        Credentials credentials = credentialsProvider.getCredentials(new AuthScope("host", 80));

        assertThat(credentials, instanceOf(Credentials.class));
    }

    @Test
    public void createDefaultCredentialsProvider() {
        CredentialsProvider credentialsProvider = getCredentialsProvider("");
        Credentials credentials = credentialsProvider.getCredentials(new AuthScope("host", 80));

        assertThat(credentials, instanceOf(UsernamePasswordCredentials.class));
        UsernamePasswordCredentials userCredentials = (UsernamePasswordCredentials) credentials;
        assertEquals("pass", userCredentials.getPassword());
    }

    private CredentialsProvider getCredentialsProvider(String authType) {
        AuthTypes authTypes = new AuthTypes(authType);
        CredentialsProviderBuilder builder = new CredentialsProviderBuilder()
                .setAuthTypes(authTypes)
                .setUsername("DOMAIN\\username")
                .setPassword("pass")
                .setHost("host")
                .setPort("80")
                .setProxyHost("proxy")
                .setProxyPort("8080")
                .setProxyPassword("proxyPass")
                .setProxyUsername("proxyUsername");

        return builder.buildCredentialsProvider();
    }
}
