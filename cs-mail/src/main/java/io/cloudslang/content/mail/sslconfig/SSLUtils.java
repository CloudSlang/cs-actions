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


package io.cloudslang.content.mail.sslconfig;

import com.sun.mail.util.MailSSLSocketFactory;
import io.cloudslang.content.mail.constants.Constants;
import io.cloudslang.content.mail.constants.PropNames;
import io.cloudslang.content.mail.constants.SecurityConstants;
import io.cloudslang.content.mail.constants.TlsVersions;
import io.cloudslang.content.mail.entities.GetMailInput;
import io.cloudslang.content.mail.entities.MailInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.utils.ProxyUtils;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.mail.NoSuchProviderException;
import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Created by giloan on 11/5/2014.
 */
public class SSLUtils {

    public static KeyStore createKeyStore(final URL url, final String password)
            throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        if (url == null) {
            throw new IllegalArgumentException("Keystore url may not be null");
        }
        KeyStore keystore = KeyStore.getInstance("jks");
        InputStream is = null;
        try {
            is = url.openStream();
            keystore.load(is, password != null ? password.toCharArray() : null);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return keystore;
    }

    public static KeyManager[] createKeyManagers(final KeyStore keystore, final String password)
            throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, password != null ? password.toCharArray() : null);
        return kmfactory.getKeyManagers();
    }

    public static TrustManager[] createAuthTrustManagers(final KeyStore keystore)
            throws KeyStoreException, NoSuchAlgorithmException {
        if (keystore == null) {
            throw new IllegalArgumentException("Keystore may not be null");
        }
        TrustManagerFactory tmfactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        tmfactory.init(keystore);
        TrustManager[] trustmanagers = tmfactory.getTrustManagers();
        for (int i = 0; i < trustmanagers.length; i++) {
            if (trustmanagers[i] instanceof X509TrustManager) {
                trustmanagers[i] = new AuthSSLX509TrustManager(
                        (X509TrustManager) trustmanagers[i]);
            }
        }
        return trustmanagers;
    }

    public static Store createMessageStore(GetMailInput input) throws Exception {
        Properties props = new Properties();
        if (input.getTimeout() > 0) {
            props.put(PropNames.MAIL + input.getProtocol() + PropNames.TIMEOUT, input.getTimeout());
        }
        if(StringUtils.isNotEmpty(input.getProxyHost())) {
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


    static Store connectUsingSSL(Properties props, Authenticator auth, GetMailInput input) throws MessagingException {
        Store store = configureStoreWithSSL(props, auth, input);
        store.connect();
        return store;
    }


    public static Store configureStoreWithSSL(Properties props, Authenticator auth, MailInput input) throws NoSuchProviderException {
        configureWithSSL(props, input);
        URLName url = new URLName(input.getProtocol(), input.getHostname(), input.getPort(), StringUtils.EMPTY,
                input.getUsername(), input.getPassword());
        Session session = Session.getInstance(props, auth);
        return session.getStore(url);
    }

    private static void configureWithSSL(Properties props, MailInput input) {
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SOCKET_FACTORY_CLASS, SecurityConstants.SSL_SOCKET_FACTORY);
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SOCKET_FACTORY_FALLBACK, String.valueOf(false));
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.PORT, String.valueOf(input.getPort()));
        props.setProperty(PropNames.MAIL + input.getProtocol() + PropNames.SOCKET_FACTORY_PORT, String.valueOf(input.getPort()));
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
        return session.getStore(input.getProtocol() + SecurityConstants.SECURE_SUFFIX);
    }


    public static void configureWithTLS(Properties props, final MailInput input) {
        final String mailSecureSSLEnable = String.format(PropNames.MAIL_SSL_ENABLE, input.getProtocol() + SecurityConstants.SECURE_SUFFIX);
        final String mailSecureStartTLSEnable = String.format(PropNames.MAIL_STARTTLS_ENABLE, input.getProtocol() + SecurityConstants.SECURE_SUFFIX);
        final String mailSecureStartTLSRequired = String.format(PropNames.MAIL_STARTTLS_REQUIRED, input.getProtocol() + SecurityConstants.SECURE_SUFFIX);
        final String mailSecureSocketFactory = String.format(PropNames.MAIL_SOCKET_FACTORY, input.getProtocol() + SecurityConstants.SECURE_SUFFIX);
        final String mailSecureSocketFactoryFallback = String.format(PropNames.MAIL_SOCKET_FACTORY_FALLBACK, input.getProtocol() + SecurityConstants.SECURE_SUFFIX);

        final boolean sslEnable = false;
        final boolean startTlsEnable = true;
        final boolean startTlsRequired = true;
        final SSLSocketFactory socketFactory = new TLSSocketFactory(input.getTlsVersions(), input.getAllowedCiphers());
        final boolean socketFactoryFallback = false;

        if (!input.getTlsVersions().isEmpty()) {
            props.setProperty(mailSecureSSLEnable, String.valueOf(sslEnable));
            props.setProperty(mailSecureStartTLSEnable, String.valueOf(startTlsEnable));
            props.setProperty(mailSecureStartTLSRequired, String.valueOf(startTlsRequired));
            props.put(mailSecureSocketFactory, socketFactory);
            props.setProperty(mailSecureSocketFactoryFallback, String.valueOf(socketFactoryFallback));
        } else {
            props.setProperty(mailSecureSSLEnable, String.valueOf(sslEnable));
            props.setProperty(mailSecureStartTLSEnable, String.valueOf(startTlsEnable));
            props.setProperty(mailSecureStartTLSRequired, String.valueOf(startTlsRequired));
        }
    }


    public static void clearTLSProperties(Properties props, MailInput input) {
        props.remove(String.format(PropNames.MAIL_SSL_ENABLE, input.getProtocol() + SecurityConstants.SECURE_SUFFIX));
        props.remove(String.format(PropNames.MAIL_STARTTLS_ENABLE, input.getProtocol() + SecurityConstants.SECURE_SUFFIX));
        props.remove(String.format(PropNames.MAIL_STARTTLS_REQUIRED, input.getProtocol() + SecurityConstants.SECURE_SUFFIX));
        props.remove(String.format(PropNames.MAIL_SOCKET_FACTORY, input.getProtocol() + SecurityConstants.SECURE_SUFFIX));

        props.remove(String.format(PropNames.MAIL_SSL_ENABLE, input.getProtocol()));
        props.remove(String.format(PropNames.MAIL_STARTTLS_ENABLE, input.getProtocol()));
        props.remove(String.format(PropNames.MAIL_STARTTLS_REQUIRED, input.getProtocol()));
    }


    static Store configureStoreWithoutSSL(Properties props, Authenticator auth, GetMailInput input) throws NoSuchProviderException {
        props.put(PropNames.MAIL + input.getProtocol() + PropNames.HOST, input.getHostname());
        props.put(PropNames.MAIL + input.getProtocol() + PropNames.PORT, input.getPort());
        Session session = Session.getInstance(props, auth);
        return session.getStore(input.getProtocol());
    }
}
