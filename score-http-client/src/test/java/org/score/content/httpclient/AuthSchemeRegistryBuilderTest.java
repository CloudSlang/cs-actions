package org.score.content.httpclient;

import org.apache.http.client.config.AuthSchemes;
import org.score.content.httpclient.AuthSchemeProviderLookupBuilder;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.config.Lookup;
import org.apache.http.impl.auth.*;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * Created with IntelliJ IDEA.
 * User: tusaa
 * Date: 7/1/14
 */
public class AuthSchemeRegistryBuilderTest {

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
        AuthSchemeProvider provider = getAuthSchemeProvider(AuthSchemes.KERBEROS);
        assertThat(provider, instanceOf(KerberosSchemeFactory.class));
    }

    @Test
    public void buildLookupWithNtlmAuth(){
        AuthSchemeProvider provider = getAuthSchemeProvider(AuthSchemes.NTLM);
        assertThat(provider, instanceOf(AuthSchemeProvider.class));
    }


    private AuthSchemeProvider getAuthSchemeProvider(String authType) {
        Lookup<AuthSchemeProvider> lookup =  new AuthSchemeProviderLookupBuilder()
                .setAuthType(authType).buildAuthSchemeProviderLookup();
        return lookup.lookup(authType);
    }
}
