/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package org.openscore.content.httpclient.build.conn;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.*;
import org.openscore.content.httpclient.constants.Constants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

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
    private String trustAllRootsStr = "false";
    private String keystore;
    private String keystorePassword;
    private String trustKeystore;
    private String trustPassword;
    private String x509HostnameVerifierInputValue = "strict";

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
        if (!trustAllRoots) {
            boolean useClientCert = !StringUtils.isEmpty(keystore);
            //validate SSL certificates sent by the server
            boolean useTrustCert = !StringUtils.isEmpty(trustKeystore);

            String javaKeystore = System.getProperty("java.home") + "/lib/security/cacerts";
            boolean storeExists = new File(javaKeystore).exists();

            if (!useClientCert && storeExists) {
                keystore = "file:" + javaKeystore;
                keystorePassword = (StringUtils.isEmpty(keystorePassword)) ? "changeit" : keystorePassword;
                useClientCert = true;
            } else if (useClientCert && !keystore.startsWith("http")) {
                keystore = "file:" + keystore;
            }

            if (!useTrustCert && storeExists) {
                trustKeystore = "file:" + javaKeystore;
                trustPassword = (StringUtils.isEmpty(trustPassword)) ? "changeit" : trustPassword;
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
                sslContextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy() {
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

        SSLConnectionSocketFactory sslsf;
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
                    throw new IllegalArgumentException("Invalid value '"+ x509HostnameVerifierInputValue +"' for input 'x509HostnameVerifier'. Valid values: 'strict','browser_compatible','allow_all'.");
            }
            // Allow SSLv3, TLSv1, TLSv1.1 and TLSv1.2 protocols only. Client-server communication starts with TLSv1.2 and fallbacks to SSLv3 if needed.
            sslsf = new SSLConnectionSocketFactory(sslContextBuilder.build(), Constants.HttpsProtocols.SUPPORTED_PROTOCOLS, null, x509HostnameVerifier);
        } catch (Exception e) {
            if(e instanceof IllegalArgumentException){
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

    public SSLConnectionSocketFactoryBuilder setX509HostnameVerifier(String x509HostnameVerifier) {
        if (!StringUtils.isEmpty(x509HostnameVerifier)) {
            this.x509HostnameVerifierInputValue = x509HostnameVerifier;
        }
        return this;
    }
}
