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

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.ssl.*;
import org.apache.hc.core5.ssl.SSLContextBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Arrays;

import static io.cloudslang.content.httpclient.utils.Constants.*;

public class CustomSSLSocketFactory {

    private static SSLContext createSSLContext(HttpClientInputs httpClientInputs) {
        SSLContextBuilder sslContextBuilder = new SSLContextBuilder();

        try {
            boolean trustAllRoots = Boolean.parseBoolean(httpClientInputs.getTrustAllRoots());

            //load keystore if trustAllRoots is false
            if (!trustAllRoots) {
                String keystorePath = httpClientInputs.getKeystore();
                String keystorePassword = httpClientInputs.getKeystorePassword();

                if (StringUtils.isEmpty(keystorePath))
                    throw new RuntimeException(EXCEPTION_KEYSTORE_NOT_FOUND);

                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                try (InputStream in = new FileInputStream(keystorePath)) {
                    keyStore.load(in, keystorePassword.toCharArray());
                }
                sslContextBuilder.loadKeyMaterial(keyStore, keystorePassword.toCharArray());

                String trustStorePath = httpClientInputs.getTrustKeystore();
                String trustStorePassword = httpClientInputs.getTrustPassword();

                if (StringUtils.isEmpty(trustStorePath)) {
                    trustStorePath = System.getProperty(TRUSTSTORE_PROPERTY);
                    trustStorePassword = System.getProperty(TRUSTSTORE_PASSWORD_PROPERTY);
                }

                if (trustStorePath == null || trustStorePassword == null)
                    throw new RuntimeException(EXCEPTION_TRUSTSTORE_NOT_FOUND);

                sslContextBuilder.loadTrustMaterial(new File(trustStorePath), trustStorePassword.toCharArray());

            } else {
                sslContextBuilder.loadTrustMaterial(new TrustAllStrategy());
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
        builder.setCiphers(
                Arrays.stream(httpClientInputs.getAllowedCiphers().split(COMMA))
                        .map(String::trim)
                        .toArray(String[]::new)
        );
        builder.setTlsVersions(
                Arrays.stream(httpClientInputs.getTlsVersion().split(COMMA))
                        .map(String::trim)
                        .toArray(String[]::new)
        );
        builder.setSslContext(createSSLContext(httpClientInputs));
        return builder.build();
    }
}