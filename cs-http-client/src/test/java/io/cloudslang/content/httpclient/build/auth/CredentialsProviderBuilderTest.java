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





package io.cloudslang.content.httpclient.build.auth;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.NTCredentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class CredentialsProviderBuilderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void createNtlmCredentialsProvider() {
        BasicCredentialsProvider credentialsProvider = getCredentialsProvider("NTLM");
        Credentials credentials = credentialsProvider.getCredentials(new AuthScope("host", 80), null);

        assertThat(credentials, instanceOf(NTCredentials.class));
        NTCredentials ntCredentials = (NTCredentials) credentials;
        assertEquals("DOMAIN", ntCredentials.getDomain());
        assertEquals("HOST", ntCredentials.getWorkstation());
        assertEquals("pass", new String(ntCredentials.getPassword()));
        Credentials proxyCredentials = credentialsProvider.getCredentials(new AuthScope("proxy", 8080), null);
        assertThat(proxyCredentials, instanceOf(UsernamePasswordCredentials.class));
        UsernamePasswordCredentials userCredentials = (UsernamePasswordCredentials) proxyCredentials;
        assertEquals("proxyUsername", userCredentials.getUserName());
    }

    @Test
    public void createKerberosCredentialsProvider() {
        BasicCredentialsProvider credentialsProvider = getCredentialsProvider("KERBEROS");
        Credentials credentials = credentialsProvider.getCredentials(new AuthScope("host", 80), null);

        assertThat(credentials, instanceOf(Credentials.class));
    }

    @Test
    public void createDefaultCredentialsProvider() {
        BasicCredentialsProvider credentialsProvider = getCredentialsProvider("");
        Credentials credentials = credentialsProvider.getCredentials(new AuthScope("host", 80), null);

        assertThat(credentials, instanceOf(UsernamePasswordCredentials.class));
        UsernamePasswordCredentials userCredentials = (UsernamePasswordCredentials) credentials;
        assertEquals("pass", new String(userCredentials.getPassword()));
    }

    @Test
    public void createCredentialProviderWithInvalidProxyPort() {
        final String invalidProxyPort = "invalidProxyPort";
        final String expectedExceptionMessage = "Invalid value '" + invalidProxyPort + "' for input 'proxyPort'. Valid Values: -1 and integer values greater than 0";
        final AuthTypes authTypes = new AuthTypes("");

        CredentialsProviderBuilder builder = new CredentialsProviderBuilder()
                .setAuthTypes(authTypes)
                .setProxyPort(invalidProxyPort)
                .setProxyUsername("proxyUsername");

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedExceptionMessage);
        builder.buildCredentialsProvider();
    }

    @Test
    public void createCredentialProviderWithNegativeProxyPort() {
        final String invalidProxyPort = "-2";
        final String expectedExceptionMessage = "Invalid value '" + invalidProxyPort + "' for input 'proxyPort'. Valid Values: -1 and integer values greater than 0";
        final AuthTypes authTypes = new AuthTypes("");

        CredentialsProviderBuilder builder = new CredentialsProviderBuilder()
                .setAuthTypes(authTypes)
                .setProxyPort(invalidProxyPort)
                .setProxyUsername("proxyUsername");

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage(expectedExceptionMessage);
        builder.buildCredentialsProvider();
    }

    private BasicCredentialsProvider getCredentialsProvider(String authType) {
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
