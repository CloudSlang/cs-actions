package com.hp.score.content.mail.sslconfig;

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

    public EasyX509TrustManager(KeyStore keystore) throws NoSuchAlgorithmException, KeyStoreException {
        super();
        TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        factory.init(keystore);
        TrustManager[] trustmanagers = factory.getTrustManagers();
        if (trustmanagers.length == 0) throw new NoSuchAlgorithmException("SunX509 trust manager not supported");
        this.standardTrustManager = (X509TrustManager)trustmanagers[0];
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
