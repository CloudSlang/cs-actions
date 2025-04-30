/*
 * Copyright 2022-2025 Open Text
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


package io.cloudslang.content.httpclient.entities;

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
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();

        KeyStore keyStore;

        try {
            if (!httpClientInputs.getKeystore().isEmpty()) {
                keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                try (InputStream in = new FileInputStream(httpClientInputs.getKeystore())) {
                    keyStore.load(in, httpClientInputs.getKeystorePassword().toCharArray());
                }
                sslContextBuilder.loadKeyMaterial(keyStore, httpClientInputs.getKeystorePassword().toCharArray());
            }

            if (Boolean.parseBoolean(httpClientInputs.getTrustAllRoots())) {
                sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
            } else {
                String trustStorePath = httpClientInputs.getTrustKeystore();
                String trustStorePassword = httpClientInputs.getTrustPassword();
                // Fallback to system properties if values are empty
                if (trustStorePath == null || trustStorePath.isEmpty()) {
                    trustStorePath = System.getProperty(TRUSTSTORE_PROPERTY);
                    trustStorePassword = System.getProperty(TRUSTSTORE_PASSWORD_PROPERTY);
                }
                if (trustStorePath != null && trustStorePassword != null)
                    sslContextBuilder.loadTrustMaterial(new File(trustStorePath), trustStorePassword.toCharArray());
                else
                    throw new RuntimeException(EXCEPTION_TRUSTSTORE_NOT_FOUND);
            }

            return sslContextBuilder.build();
        } catch (FileNotFoundException exception) {
            throw new RuntimeException(EXCEPTION_CERTIFICATE_NOT_FOUND);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
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