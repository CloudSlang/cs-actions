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

import com.hp.score.content.httpclient.build.auth.AuthSchemeProviderLookupBuilder;
import com.hp.score.content.httpclient.build.auth.AuthTypes;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.config.Lookup;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/1/14
 */
public class AuthSchemeProviderLookupBuilderTest {

    @Test
    public void buildLookupWithBasicAuth(){
        AuthSchemeProvider provider = getAuthSchemeProvider(AuthSchemes.BASIC);
        assertThat(provider, instanceOf(BasicSchemeFactory.class));
        BasicScheme basicSchema = ((BasicScheme)provider.create(null));
        assertEquals("UTF-8", basicSchema.getCredentialsCharset().toString());
    }

    @Test
    public void buildLookupWithDigestAuth(){
        AuthSchemeProvider provider = getAuthSchemeProvider(AuthSchemes.DIGEST);
        assertThat(provider, instanceOf(DigestSchemeFactory.class));
    }

    @Test
    public void buildLookupWithKerberosAuth(){
        AuthTypes authTypes = new AuthTypes(AuthSchemes.KERBEROS);
        AuthSchemeProvider provider =  new AuthSchemeProviderLookupBuilder()
                .setAuthTypes(authTypes)
                .setHost("myweb.contoso.com").buildAuthSchemeProviderLookup().lookup(AuthSchemes.KERBEROS);
        assertThat(provider, instanceOf(KerberosSchemeFactory.class));
    }

    @Test
    public void buildLookupWithNtlmAuth(){
        AuthSchemeProvider provider = getAuthSchemeProvider(AuthSchemes.NTLM);
        assertThat(provider, instanceOf(AuthSchemeProvider.class));
    }


    private AuthSchemeProvider getAuthSchemeProvider(String authType) {
        AuthTypes authTypes = new AuthTypes(authType);
        Lookup<AuthSchemeProvider> lookup =  new AuthSchemeProviderLookupBuilder()
                .setAuthTypes(authTypes).buildAuthSchemeProviderLookup();
        return lookup.lookup(authType);
    }
}
