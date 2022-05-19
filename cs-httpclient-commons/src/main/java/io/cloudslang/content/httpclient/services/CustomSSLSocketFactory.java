package io.cloudslang.content.httpclient.services;

import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.hc.client5.http.ssl.*;
import org.apache.hc.core5.ssl.SSLContextBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;

import static io.cloudslang.content.httpclient.utils.Constants.*;

public class CustomSSLSocketFactory {

    private static SSLContext createSSLContext(HttpClientInputs httpClientInputs) {


        if (Boolean.parseBoolean(httpClientInputs.getTrustAllRoots())) {
            try {
                return new SSLContextBuilder().loadTrustMaterial(null, new TrustAllStrategy()).build();
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        } else {
            try {
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                try (InputStream in = new FileInputStream(httpClientInputs.getKeystore())) {
                    keyStore.load(in, httpClientInputs.getKeystorePassword().toCharArray());
                }
                SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
                sslContextBuilder.loadTrustMaterial(new File(httpClientInputs.getTrustKeystore()), httpClientInputs.getTrustPassword().toCharArray());
                sslContextBuilder.loadKeyMaterial(keyStore, httpClientInputs.getKeystorePassword().toCharArray());

                return sslContextBuilder.build();
            }catch(FileNotFoundException exception){
                throw new RuntimeException(EXCEPTION_CERTIFICATE_NOT_FOUND);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    private static HostnameVerifier createHostnameVerifier(String hostnameVerifier) {
        if (hostnameVerifier.equalsIgnoreCase(ALLOW_ALL))
            return new NoopHostnameVerifier();
        else
            return new DefaultHostnameVerifier();
    }

    public static SSLConnectionSocketFactory createSSLSocketFactory(HttpClientInputs httpClientInputs) {
        SSLConnectionSocketFactoryBuilder builder = SSLConnectionSocketFactoryBuilder.create();
        builder.setHostnameVerifier(createHostnameVerifier(httpClientInputs.getX509HostnameVerifier()));
        builder.setCiphers(httpClientInputs.getAllowedCiphers().split(COMMA));
        builder.setTlsVersions(httpClientInputs.getTlsVersion().split(COMMA));
        builder.setSslContext(createSSLContext(httpClientInputs));
        return builder.build();
    }
}