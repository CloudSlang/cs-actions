/*
 * Copyright 2026-2027 Open Text
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

package io.cloudslang.content.winrm.client.kerberos;

import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Produces Kerberos tokens (sent with the {@code Negotiate} HTTP scheme) for the WinRM HTTP service principal.
 */
public final class KerberosAuthenticator {

    private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";
    private static final Configuration JAAS_KERBEROS_LOGIN_CONFIGURATION = new KerberosJaasConfiguration();

    private final String host;
    private final GSSCredential credential;

    /**
     * @param host                     the WinRM host, used to build the {@code HTTP@host} service principal
     * @param username                 the user principal, used only when a new ticket is requested
     * @param password                 the user password, used only when a new ticket is requested
     * @param requestNewKerberosTicket when {@code true} a new TGT is obtained from the KDC with the given credentials,
     *                                 otherwise the default JAAS/GSS configuration (ticket cache, keytab) is used
     */
    public KerberosAuthenticator(final String host, final String username, final String password,
                                 final boolean requestNewKerberosTicket) {
        this.host = host;
        this.credential = requestNewKerberosTicket ? createCredential(username, password) : null;
    }

    /**
     * Creates a new security context and returns its initial token.
     */
    public byte[] createToken() {
        try {
            final GSSManager manager = GSSManager.getInstance();
            final Oid kerberosOid = new Oid(KERBEROS_OID);
            final GSSName serverName = manager.createName("HTTP@" + resolveCanonicalHostname(host), GSSName.NT_HOSTBASED_SERVICE);
            final GSSContext context = manager.createContext(serverName.canonicalize(kerberosOid), kerberosOid, credential,
                    GSSContext.DEFAULT_LIFETIME);
            try {
                context.requestMutualAuth(true);
                return context.initSecContext(new byte[0], 0, 0);
            } finally {
                context.dispose();
            }
        } catch (GSSException e) {
            throw new IllegalStateException("Unable to generate the Kerberos token for host \"" + host + "\": " + e.getMessage(), e);
        }
    }

    private static String resolveCanonicalHostname(final String host) {
        try {
            final InetAddress address = InetAddress.getByName(host);
            final String canonicalName = address.getCanonicalHostName();
            if (address.getHostAddress().contentEquals(canonicalName)) {
                return host;
            }
            return canonicalName;
        } catch (UnknownHostException e) {
            return host;
        }
    }

    private static GSSCredential createCredential(final String username, final String password) {
        // An upper case UPN avoids "Message stream modified" errors when the realm is upper case
        final String canonicalUsername = username.trim().toUpperCase(Locale.ROOT);
        final Subject subject = login(canonicalUsername, password);
        try {
            return Subject.doAs(subject, (PrivilegedExceptionAction<GSSCredential>) () -> {
                final GSSManager manager = GSSManager.getInstance();
                final GSSName principal = manager.createName(canonicalUsername, null);
                return manager.createCredential(principal, GSSContext.DEFAULT_LIFETIME, new Oid(KERBEROS_OID),
                        GSSCredential.INITIATE_ONLY);
            });
        } catch (Exception e) {
            throw new IllegalStateException("Unable to create credential for user \"" + username + "\" after login", e);
        }
    }

    private static Subject login(final String username, final String password) {
        try {
            final LoginContext loginContext = new LoginContext("", null, new NamePasswordCallbackHandler(username, password),
                    JAAS_KERBEROS_LOGIN_CONFIGURATION);
            loginContext.login();
            return loginContext.getSubject();
        } catch (LoginException e) {
            throw new IllegalStateException("Exception occurred while authenticating the user \"" + username + "\" on the KDC", e);
        }
    }

    /**
     * Forces a fresh TGT to be obtained from the KDC with the supplied credentials, ignoring the ticket cache.
     */
    private static final class KerberosJaasConfiguration extends Configuration {
        private final AppConfigurationEntry[] entries;

        KerberosJaasConfiguration() {
            final Map<String, String> options = new HashMap<>();
            options.put("client", "true");
            options.put("isInitiator", "true");
            options.put("useTicketCache", "false");
            entries = new AppConfigurationEntry[]{new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule",
                    AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, options)};
        }

        @Override
        public AppConfigurationEntry[] getAppConfigurationEntry(final String name) {
            return entries;
        }
    }

    private static final class NamePasswordCallbackHandler implements CallbackHandler {
        private final String username;
        private final String password;

        NamePasswordCallbackHandler(final String username, final String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public void handle(final Callback[] callbacks) throws UnsupportedCallbackException {
            for (Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    ((NameCallback) callback).setName(username);
                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(password == null ? new char[0] : password.toCharArray());
                } else {
                    throw new UnsupportedCallbackException(callback);
                }
            }
        }
    }
}
