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

import org.apache.hc.client5.http.auth.AuthSchemeFactory;
import org.apache.hc.client5.http.impl.auth.BasicSchemeFactory;
import org.apache.hc.client5.http.impl.auth.DigestSchemeFactory;
import org.apache.hc.client5.http.impl.auth.KerberosSchemeFactory;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.config.Lookup;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class AuthSchemeProviderLookupBuilderTest {

    @Test
    public void buildLookupWithBasicAuth() {
        AuthSchemeFactory factory = getAuthSchemeFactory("BASIC");
        assertThat(factory, instanceOf(BasicSchemeFactory.class));
    }

    @Test
    public void buildLookupWithDigestAuth() {
        AuthSchemeFactory factory = getAuthSchemeFactory("DIGEST");
        assertThat(factory, instanceOf(DigestSchemeFactory.class));
    }

    @Test
    public void buildLookupWithKerberosAuth() {
        AuthTypes authTypes = new AuthTypes("KERBEROS");
        AuthSchemeFactory factory = new AuthSchemeProviderLookupBuilder()
                .setAuthTypes(authTypes)
                .setHost("myweb.contoso.com").buildAuthSchemeProviderLookup().lookup("Kerberos");
        assertThat(factory, instanceOf(KerberosSchemeFactory.class));
    }

    @Test
    public void buildLookupWithNtlmAuth() {
        AuthSchemeFactory factory = getAuthSchemeFactory("NTLM");
        assertNotNull(factory);
    }

    private AuthSchemeFactory getAuthSchemeFactory(String authType) {
        AuthTypes authTypes = new AuthTypes(authType);
        Lookup<AuthSchemeFactory> lookup = new AuthSchemeProviderLookupBuilder()
                .setHeaders(new ArrayList<Header>())
                .setAuthTypes(authTypes)
                .buildAuthSchemeProviderLookup();
        // Registry keys are stored lowercase in HC5
        return lookup.lookup(authType.toLowerCase());
    }
}
