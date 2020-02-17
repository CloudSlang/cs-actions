/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
 * All rights reserved. This program and the accompanying materials
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
package io.cloudslang.content.mail.utils;

import io.cloudslang.content.mail.constants.*;
import io.cloudslang.content.mail.entities.GetMailInput;
import io.cloudslang.content.mail.entities.MailInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.sslconfig.EasyX509TrustManager;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.File;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class MessageStoreUtils {

    public static Store createMessageStore(GetMailInput input) throws Exception {
        Properties props = new Properties();
        if (input.getTimeout() > 0) {
            props.put(PropNames.MAIL + input.getProtocol() + PropNames.TIMEOUT, input.getTimeout());
            ProxyUtils.setPropertiesProxy(props, input);
        }
        Authenticator auth = new SimpleAuthenticator(input.getUsername(), input.getPassword());
        Store store;
        if (input.isEnableTLS() || input.isEnableSSL()) {
            addSSLSettings(input.isTrustAllRoots(), input.getKeystore(), input.getKeystorePassword(),
                    StringUtils.EMPTY, StringUtils.EMPTY);
        }
        if (input.isEnableTLS()) {
            store = tryTLSOtherwiseTrySSL(auth, props, input);
        } else if (input.isEnableSSL()) {
            store = connectUsingSSL(props, auth, input);
        } else {
            store = configureStoreWithoutSSL(props, auth, input);
            store.connect();
        }

        return store;
    }


    static Store connectUsingSSL(Properties props, Authenticator auth, GetMailInput input) throws MessagingException {
        Store store = configureStoreWithSSL(props, auth, input);
        store.connect();
        return store;
    }


    public static Store configureStoreWithSSL(Properties props, Authenticator auth, GetMailInput input) throws NoSuchProviderException {
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SOCKET_FACTORY_CLASS, SecurityConstants.SSL_SOCKET_FACTORY);
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SOCKET_FACTORY_FALLBACK, String.valueOf(false));
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.PORT, String.valueOf(input.getPort()));
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SOCKET_FACTORY_PORT, String.valueOf(input.getPort()));
        URLName url = new URLName(input.getProtocol(), input.getHostname(), input.getPort(), StringUtils.EMPTY,
                input.getUsername(), input.getPassword());
        Session session = Session.getInstance(props, auth);
        return session.getStore(url);
    }


    public static void addSSLSettings(boolean trustAllRoots, String keystore,
                                      String keystorePassword, String trustKeystore, String trustPassword) throws Exception {
        boolean useClientCert = false;
        boolean useTrustCert = false;

        String separator = System.getProperty(PropNames.FILE_SEPARATOR);
        String javaKeystore = System.getProperty(PropNames.JAVA_HOME) + separator + "lib" + separator + "security" + separator + "cacerts";

        if (keystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            keystore = (storeExists) ? Constants.FILE + javaKeystore : null;
            if (null != keystorePassword) {
                if (StringUtils.EMPTY.equals(keystorePassword)) {
                    keystorePassword = SecurityConstants.DEFAULT_PASSWORD_FOR_STORE;
                }
            }
            useClientCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!keystore.startsWith(Constants.HTTP))
                    keystore = Constants.FILE + keystore;
                useClientCert = true;
            }
        }

        if (trustKeystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            trustKeystore = (storeExists) ? Constants.FILE + javaKeystore : null;
            if (storeExists) {
                if (isEmpty(trustPassword)) {
                    trustPassword = SecurityConstants.DEFAULT_PASSWORD_FOR_STORE;
                }
            } else {
                trustPassword = null;
            }
            useTrustCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!trustKeystore.startsWith(Constants.HTTP))
                    trustKeystore = Constants.FILE + trustKeystore;
                useTrustCert = true;
            }

        }

        TrustManager[] trustManagers = null;
        KeyManager[] keyManagers = null;

        if (trustAllRoots) {
            trustManagers = new TrustManager[]{new EasyX509TrustManager()};
        }

        if (useTrustCert) {
            KeyStore trustKeyStore = SSLUtils.createKeyStore(new URL(trustKeystore), trustPassword);
            trustManagers = SSLUtils.createAuthTrustManagers(trustKeyStore);
        }
        if (useClientCert) {
            KeyStore clientKeyStore = SSLUtils.createKeyStore(new URL(keystore), keystorePassword);
            keyManagers = SSLUtils.createKeyManagers(clientKeyStore, keystorePassword);
        }

        SSLContext context = SSLContext.getInstance(SecurityConstants.SSL);
        context.init(keyManagers, trustManagers, new SecureRandom());
        SSLContext.setDefault(context);
    }


    public static Store tryTLSOtherwiseTrySSL(Authenticator auth, Properties props, GetMailInput input) throws MessagingException {
        Store store = configureStoreWithTLS(props, auth, input);
        try {
            store.connect(input.getHostname(), input.getUsername(), input.getPassword());
        } catch (Exception e) {
            if (input.isEnableSSL()) {
                clearTLSProperties(props, input);
                store = connectUsingSSL(props, auth, input);
            } else {
                throw e;
            }
        }
        return store;
    }


    public static Store configureStoreWithTLS(Properties props, Authenticator auth, MailInput input) throws NoSuchProviderException {
        configureWithTLS(props, input);
        Session session = Session.getInstance(props, auth);
        return session.getStore(input.getProtocol() + SecurityConstants.SECURE_SUFFIX_FOR_POP3_AND_IMAP);
    }


    public static void configureWithTLS(Properties props, MailInput input) {
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SSL_ENABLE, String.valueOf(false));

        if (!input.getTlsVersions().isEmpty()) {
            props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SSL_PROTOCOLS,
                    StringUtils.join(input.getTlsVersions(), ' '));

            if (input.getTlsVersions().contains(TlsVersions.TLSv1_2) && !input.getAllowedCiphers().isEmpty()) {
                props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SSL_CIPHER_SUITES,
                        StringUtils.join(input.getAllowedCiphers(), ' '));
            }
        }

        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.START_TLS_ENABLE, String.valueOf(true));

        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.START_TLS_REQUIRED, String.valueOf(true));
    }


    private static void clearTLSProperties(Properties props, GetMailInput input) {
        props.remove(PropNames.MAIL + input.getProtocol() + PropNames.SSL_ENABLE);
        props.remove(PropNames.MAIL + input.getProtocol() + PropNames.SSL_PROTOCOLS);
        props.remove(PropNames.MAIL + input.getProtocol() + PropNames.SSL_CIPHER_SUITES);
        props.remove(PropNames.MAIL + input.getProtocol() + PropNames.START_TLS_ENABLE);
        props.remove(PropNames.MAIL + input.getProtocol() + PropNames.START_TLS_REQUIRED);
    }


    public static Store configureStoreWithoutSSL(Properties props, Authenticator auth, GetMailInput input) throws NoSuchProviderException {
        props.put(PropNames.MAIL + input.getProtocol() + PropNames.HOST, input.getHostname());
        props.put(PropNames.MAIL + input.getProtocol() + PropNames.PORT, input.getPort());
        Session session = Session.getInstance(props, auth);
        return session.getStore(input.getProtocol());
    }
}
