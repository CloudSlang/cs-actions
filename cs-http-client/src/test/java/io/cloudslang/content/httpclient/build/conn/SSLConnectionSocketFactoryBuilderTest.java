/*
 * Copyright 2022-2024 Open Text
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

package io.cloudslang.content.httpclient.build.conn;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.net.URL;
import java.security.KeyStore;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SSLConnectionSocketFactoryBuilderTest {
    private static final String PASSWORD = "password";
    private static final String KEYSTORE = "C:/keystore";

    @Mock
    private SSLContextBuilder sslContextBuilder;
    @Mock
    private KeyStore keyStore;

    @Test
    void build() {
        SSLConnectionSocketFactoryBuilder builder = new SSLConnectionSocketFactoryBuilder() {
            @Override
            protected void createTrustKeystore(SSLContextBuilder contextBuilder, boolean useTrustCert) {
            }

            @Override
            protected void createKeystore(SSLContextBuilder contextBuilder, boolean useClientCert) {
            }
        };

        assertNotNull(builder.build());
    }

    @Test
    void buildWithTrustAllRoots() {
        String javaHome = System.getProperty("java.home");
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactoryBuilder()
                .setTrustAllRoots("true")
                .setKeystore(javaHome + "/lib/security/cacerts")
                .setKeystorePassword("changeit")
                .build();

        assertNotNull(socketFactory);
    }

    @Test
    void createTrustKeystore() throws Exception {
        SSLConnectionSocketFactoryBuilder builder = new SSLConnectionSocketFactoryBuilder() {
            @Override
            protected KeyStore createKeyStore(URL url, String password) {
                return keyStore;
            }
        };
        builder.setTrustKeystore("file:" + KEYSTORE).setTrustPassword(PASSWORD);
        when(sslContextBuilder.loadTrustMaterial(keyStore)).thenReturn(sslContextBuilder);

        builder.createTrustKeystore(sslContextBuilder, true);

        verify(sslContextBuilder).loadTrustMaterial(keyStore);
    }

    @Test
    void createTrustKeystoreWithException() {
        SSLConnectionSocketFactoryBuilder builder = new SSLConnectionSocketFactoryBuilder()
                .setTrustKeystore(KEYSTORE);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> builder.createTrustKeystore(sslContextBuilder, true));
        assertTrue(exception.getMessage().contains(SSLConnectionSocketFactoryBuilder.BAD_TRUST_KEYSTORE_ERROR));
    }

    @Test
    void createKeystore() throws Exception {
        SSLConnectionSocketFactoryBuilder builder = new SSLConnectionSocketFactoryBuilder() {
            @Override
            protected KeyStore createKeyStore(URL url, String password) {
                return keyStore;
            }
        };
        builder.setKeystore("file:" + KEYSTORE).setKeystorePassword(PASSWORD);
        when(sslContextBuilder.loadKeyMaterial(keyStore, PASSWORD.toCharArray())).thenReturn(sslContextBuilder);

        builder.createKeystore(sslContextBuilder, true);

        verify(sslContextBuilder).loadKeyMaterial(keyStore, PASSWORD.toCharArray());
    }

    @Test
    void createKeystoreWithException() {
        SSLConnectionSocketFactoryBuilder builder = new SSLConnectionSocketFactoryBuilder()
                .setKeystore(KEYSTORE);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> builder.createKeystore(sslContextBuilder, true));
        assertTrue(exception.getMessage().contains(SSLConnectionSocketFactoryBuilder.BAD_KEYSTORE_ERROR));
    }

    @Test
    void createKeyStoreWithException() {
        SSLConnectionSocketFactoryBuilder builder = new SSLConnectionSocketFactoryBuilder();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> builder.createKeyStore(null, null));
        assertEquals("Keystore url may not be null", exception.getMessage());
    }

    @Test
    void createKeyStore() throws Exception {
        File cacertsFile = new File(System.getProperty("java.home"), "lib/security/cacerts");
        KeyStore loadedKeyStore = new SSLConnectionSocketFactoryBuilder()
                .createKeyStore(cacertsFile.toURI().toURL(), "changeit");

        assertTrue(loadedKeyStore.size() > 0);
    }
}
