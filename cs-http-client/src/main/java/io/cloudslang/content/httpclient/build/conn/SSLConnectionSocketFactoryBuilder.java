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


package io.cloudslang.content.httpclient.build.conn;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: davidmih
 * Date: 6/27/14
 */
public class SSLConnectionSocketFactoryBuilder {
    public static final String TRUST_ALL_ROOTS_ERROR = "Could not use trustAllRoots=";
    public static final String SSL_CONNECTION_ERROR = "Could not create SSL connection. Invalid keystore or trustKeystore certificates.";
    public static final String BAD_KEYSTORE_ERROR = "The keystore provided in the 'keystore' input is corrupted OR the password (in the 'keystorePassword' input) is incorrect";
    public static final String INVALID_KEYSTORE_ERROR = "A keystore could not be found or it does not contain the needed certificate";
    public static final String BAD_TRUST_KEYSTORE_ERROR = "The trust keystore provided in the 'trustKeystore' input is corrupted OR the password (in the 'trustPassword' input) is incorrect";
    public static final String INVALID_TRUST_KEYSTORE_ERROR = "A trust keystore could not be found or it does not contain the needed certificate";
    public static final String SSLv3 = "SSLv3";
    public static final String TLSv10 = "TLSv1";
    public static final String TLSv11 = "TLSv1.1";
    public static final String TLSv12 = "TLSv1.2";
    public static final String[] ARRAY_TLSv12 = new String[]{"TLSv1.2"};
    public static final String[] array = new String[0];
    public static final String[] SUPPORTED_PROTOCOLS = new String[]{SSLv3, TLSv10, TLSv11, TLSv12};
    public static final String[] SUPPORTED_CYPHERS = new String[]{"TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "THS_DHE_RSA_WITH_AES_256_CBC_SHA256", "THS_DHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256",
            "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_WITH_AES_256_CBC_SHA384", "TLS_ECDHE_WITH_AES_256_GCM_SHA384", "TLS_RSA_WITH_AES_256_GCM_SHA384", "TLS_RSA_WITH_AES_256_CBC_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA256"};
    private static boolean checkArray = false;
    public String[] cypherArray;
    private String trustAllRootsStr = "false";
    private String keystore;
    private String inputTLS;
    private String keystorePassword;
    private String trustKeystore;
    private String trustPassword;
    private String inputCyphers;
    private String x509HostnameVerifierInputValue = "strict";
    private boolean flag = false;
    private boolean hasTLS2;

    public static boolean checkEquality(String[] subArray, String[] largeArray) {

        for (String aLargeArray : largeArray)
            for (int j = 0; j < subArray.length; j++)
                if ((aLargeArray.toUpperCase()).equals((subArray[j]).toUpperCase())) {
                    subArray[j] = aLargeArray;
                }

        return Arrays.asList(largeArray).containsAll(Arrays.asList(subArray));
    }

    public static boolean checkIfTLS2(String[] arr, String targetValue) {
        return Arrays.asList(arr).contains(targetValue);
    }

    protected KeyStore createKeyStore(final URL url, final String password)
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
            if (is != null) is.close();
        }
        return keystore;
    }

    public SSLConnectionSocketFactory build() {
        if (!"true".equalsIgnoreCase(trustAllRootsStr) && !"false".equalsIgnoreCase(trustAllRootsStr)) {
            throw new IllegalArgumentException("'trustAllRoots' can only be 'true' or 'false'");
        }
        boolean trustAllRoots = Boolean.parseBoolean(trustAllRootsStr);

        SSLContextBuilder sslContextBuilder = SSLContexts.custom();
        String changeit = "changeit";
        String javaKeystore = System.getProperty("java.home") + "/lib/security/cacerts";
        if (!trustAllRoots) {
            boolean useClientCert = StringUtils.isNotEmpty(keystore);
            //validate SSL certificates sent by the server
            boolean useTrustCert = StringUtils.isNotEmpty(trustKeystore);

            boolean storeExists = new File(javaKeystore).exists();

            if (!useClientCert && storeExists) {
                keystore = "file:" + javaKeystore;
                keystorePassword = StringUtils.isNotEmpty(keystorePassword) ? keystorePassword : changeit;
                useClientCert = true;
            } else if (useClientCert && !keystore.startsWith("http")) {
                keystore = "file:" + keystore;
            }

            if (!useTrustCert && storeExists) {
                trustKeystore = "file:" + javaKeystore;
                trustPassword = StringUtils.isNotEmpty(trustPassword) ? trustPassword : changeit;
                useTrustCert = true;
            } else if (useTrustCert && !trustKeystore.startsWith("http")) {
                trustKeystore = "file:" + trustKeystore;
            }
            createTrustKeystore(sslContextBuilder, useTrustCert);
            //todo client key authentication should not depend on 'trustAllRoots'
            createKeystore(sslContextBuilder, useClientCert);
        } else {
            try {
                //need to override isTrusted() method to accept CA certs because the Apache HTTP Client ver.4.3 will only accepts self-signed certificates
                KeyStore keyStore = createKeyStore(new URL("file:" + keystore), keystorePassword);
                sslContextBuilder.loadKeyMaterial(keyStore, keystorePassword.toCharArray());

                String internalJavaKeystoreUri = "file:" + javaKeystore;
                KeyStore javaTrustStore = createKeyStore(new URL(internalJavaKeystoreUri), changeit);
                sslContextBuilder.loadTrustMaterial(javaTrustStore, new TrustSelfSignedStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] chain, String authType)
                            throws CertificateException {
                        return true;
                    }
                });
            } catch (Exception e) {
                throw new IllegalArgumentException(e.getMessage() + ". " + TRUST_ALL_ROOTS_ERROR + trustAllRoots, e);
            }
        }

        sslContextBuilder.useSSL();
        sslContextBuilder.useTLS();

        SSLConnectionSocketFactory sslsf = null;
        try {
            String x509HostnameVerifierStr = x509HostnameVerifierInputValue.toLowerCase();

            X509HostnameVerifier x509HostnameVerifier;
            switch (x509HostnameVerifierStr) {
                case "strict":
                    x509HostnameVerifier = SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER;
                    break;
                case "browser_compatible":
                    x509HostnameVerifier = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
                    break;
                case "allow_all":
                    x509HostnameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid value '" + x509HostnameVerifierInputValue + "' for input 'x509HostnameVerifier'. Valid values: 'strict','browser_compatible','allow_all'.");
            }

            // Allow SSLv3, TLSv1, TLSv1.1 and TLSv1.2 protocols only. Client-server communication starts with TLSv1.2 and fallbacks to SSLv3 if needed.
            if (!StringUtils.isEmpty(inputTLS)) {
                Set<String> protocolSet = new HashSet<>(Arrays.asList(inputTLS.trim().split(",")));
                String[] protocolArray = protocolSet.toArray(new String[0]);

                if (!checkEquality(protocolArray, SUPPORTED_PROTOCOLS)) {
                    throw new IllegalArgumentException("Protocol not supported");
                }

                if (checkIfTLS2(protocolArray, TLSv12))
                    flag = true;

                if (!StringUtils.isEmpty(inputCyphers))
                    cypherArray = inputCyphers.trim().split(",");

                if (flag) {
                    if (cypherArray != null) {
                        sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(), ARRAY_TLSv12, cypherArray, x509HostnameVerifier);
                    } else {
                        sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(), protocolArray, null, x509HostnameVerifier);
                    }
                } else {
                    sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(), protocolArray, null, x509HostnameVerifier);
                }
            } else {
                sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(), SUPPORTED_PROTOCOLS, null, x509HostnameVerifier);
            }

        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw new IllegalArgumentException(e.getMessage());
            }
            throw new RuntimeException(e.getMessage() + ". " + SSL_CONNECTION_ERROR, e);
        }
        return sslsf;
    }

    protected void createKeystore(SSLContextBuilder sslContextBuilder, boolean useClientCert) {
        if (useClientCert) {
            KeyStore clientKeyStore;
            try {
                clientKeyStore = createKeyStore(new URL(keystore), keystorePassword);
                sslContextBuilder.loadKeyMaterial(clientKeyStore, keystorePassword.toCharArray());
            } catch (UnrecoverableKeyException | IOException ue) {
                throw new IllegalArgumentException(ue.getMessage() + ". " + BAD_KEYSTORE_ERROR, ue);
            } catch (GeneralSecurityException gse) {
                throw new IllegalArgumentException(gse.getMessage() + ". " + INVALID_KEYSTORE_ERROR, gse);
            }
        }
    }

    protected void createTrustKeystore(SSLContextBuilder sslContextBuilder, boolean useTrustCert) {
        if (useTrustCert) {
            KeyStore trustKeyStore;
            try {
                //todo should we do this 'create' in each and every step?
                trustKeyStore = createKeyStore(new URL(trustKeystore), trustPassword);
                sslContextBuilder.loadTrustMaterial(trustKeyStore);
            } catch (IOException ioe) {
                throw new IllegalArgumentException(ioe.getMessage() + ". " + BAD_TRUST_KEYSTORE_ERROR, ioe);
            } catch (GeneralSecurityException gse) {
                throw new IllegalArgumentException(gse.getMessage() + ". " + INVALID_TRUST_KEYSTORE_ERROR, gse);
            }
        }
    }

    public SSLConnectionSocketFactoryBuilder setTrustAllRoots(String trustAllRoots) {
        if (!StringUtils.isEmpty(trustAllRoots)) {
            this.trustAllRootsStr = trustAllRoots;
        }
        return this;
    }


    public SSLConnectionSocketFactoryBuilder setInputTLS(String inputTLSversion) {
        this.inputTLS = inputTLSversion;
        return this;
    }

    public SSLConnectionSocketFactoryBuilder setKeystore(String keystore) {
        this.keystore = keystore;
        return this;
    }

    public SSLConnectionSocketFactoryBuilder setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
        return this;
    }

    public SSLConnectionSocketFactoryBuilder setTrustKeystore(String trustKeystore) {
        this.trustKeystore = trustKeystore;
        return this;
    }

    public SSLConnectionSocketFactoryBuilder setTrustPassword(String trustPassword) {
        this.trustPassword = trustPassword;
        return this;
    }

    public SSLConnectionSocketFactoryBuilder setallowedCyphers(String allowedCyphers) {
        this.inputCyphers = allowedCyphers;
        return this;
    }

    public SSLConnectionSocketFactoryBuilder setX509HostnameVerifier(String x509HostnameVerifier) {
        if (!StringUtils.isEmpty(x509HostnameVerifier)) {
            this.x509HostnameVerifierInputValue = x509HostnameVerifier;
        }
        return this;
    }
}
