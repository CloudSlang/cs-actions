package org.score.content.httpclient;

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
        //assertNull(credentials.getPassword());
        //assertNull(credentials.getUserPrincipal());
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
        CredentialsProviderBuilder builder = new CredentialsProviderBuilder()
                .setAuthType(authType)
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
