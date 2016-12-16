/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.mail.sslconfig;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * Created by giloan on 11/5/2014.
 */
public class EasyX509TrustManager implements X509TrustManager {
    private X509TrustManager standardTrustManager = null;

    public EasyX509TrustManager() throws NoSuchAlgorithmException, KeyStoreException {
        super();
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init((KeyStore) null);
        TrustManager[] trustManagers = factory.getTrustManagers();
        if (trustManagers.length == 0) throw new NoSuchAlgorithmException("SunX509 trust manager not supported");
        this.standardTrustManager = (X509TrustManager) trustManagers[0];
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) {
        return;
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) {
        return;
    }

    public X509Certificate[] getAcceptedIssuers() {
        if (standardTrustManager != null) return standardTrustManager.getAcceptedIssuers();
        return new X509Certificate[]{};
    }
}
