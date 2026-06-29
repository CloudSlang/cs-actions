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

import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

/**
 * User: Adina Tusa
 * Date: 8/20/14
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({SSLContexts.class, System.class, SSLConnectionSocketFactoryBuilder.class,
        KeyStore.class, org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder.class})
public class SSLConnectionSocketFactoryBuilderTest {

    public static final String BAD_TRUST_KEYSTORE_ERROR = "The trust keystore provided in the 'trustKeystore' input is corrupted OR the password (in the 'trustPassword' input) is incorrect";
    public static final String BAD_KEYSTORE_ERROR = "The keystore provided in the 'keystore' input is corrupted OR the password (in the 'keystorePassword' input) is incorrect";
    public static final String PASSWORD = "password";
    public static final String KEYSTORE = "C:/keystore";
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Mock
    URL urlMock;
    private SSLConnectionSocketFactoryBuilder builder;
    @Mock
    private SSLContextBuilder sslContextBuilderMock;
    @Mock
    private SSLContext sslCtxMock;
    @Mock
    private SSLConnectionSocketFactory sslsfMock;
    @Mock
    private KeyStore keyStoreMock;
    @Mock
    private InputStream inputStreamMock;

    @Test
    public void build() throws Exception {
        builder = new SSLConnectionSocketFactoryBuilder() {
            protected void createTrustKeystore(SSLContextBuilder sslContextBuilder, boolean useTrustCert) {
            }

            protected void createKeystore(SSLContextBuilder sslContextBuilder, boolean useClientCert) {
            }

            @Override
            protected SSLConnectionSocketFactory buildSslConnectionSocketFactory(SSLContextBuilder sslCtxBuilder,
                                                                                  String[] protocols,
                                                                                  String[] ciphers,
                                                                                  HostnameVerifier hostnameVerifier) {
                return sslsfMock;
            }
        };

        mockStatic(SSLContexts.class);
        mockStatic(System.class);

        when(SSLContexts.custom()).thenReturn(sslContextBuilderMock);
        when(System.getProperty("java.home")).thenReturn("javaHome");
        whenNew(java.io.File.class).withArguments(anyString()).thenReturn(mock(java.io.File.class));

        SSLConnectionSocketFactory sslsf = builder.build();
        assertNotNull(sslsf);
        assertEquals(sslsfMock, sslsf);
    }

    @Test
    public void buildWithTrustAllRoots() throws Exception {
        builder = new SSLConnectionSocketFactoryBuilder() {
            @Override
            protected SSLConnectionSocketFactory buildSslConnectionSocketFactory(SSLContextBuilder sslCtxBuilder,
                                                                                  String[] protocols,
                                                                                  String[] ciphers,
                                                                                  HostnameVerifier hostnameVerifier) {
                return sslsfMock;
            }
        };
        builder.setTrustAllRoots("true");
        builder.setKeystore(System.getProperty("java.home") + "/lib/security/cacerts");
        builder.setKeystorePassword("changeit");

        mockStatic(SSLContexts.class);
        when(SSLContexts.custom()).thenReturn(sslContextBuilderMock);
        when(sslContextBuilderMock.loadTrustMaterial(isA(KeyStore.class), any())).thenReturn(sslContextBuilderMock);
        when(sslContextBuilderMock.build()).thenReturn(sslCtxMock);

        SSLConnectionSocketFactory sslsf = builder.build();
        assertNotNull(sslsf);
        assertEquals(sslsfMock, sslsf);
    }

    @Test
    public void createTrustKeystore() throws Exception {
        builder = new SSLConnectionSocketFactoryBuilder() {
            @Override
            protected KeyStore createKeyStore(final URL url, final String password) {
                return keyStoreMock;
            }
        };
        builder.setTrustKeystore("file:" + KEYSTORE)
                .setTrustPassword(PASSWORD);
        when(sslContextBuilderMock.loadTrustMaterial(keyStoreMock, null)).thenReturn(sslContextBuilderMock);

        builder.createTrustKeystore(sslContextBuilderMock, true);
        verify(sslContextBuilderMock).loadTrustMaterial(keyStoreMock, null);
    }

    @Test
    public void createTrustKeystoreWithException() throws Exception {
        builder = new SSLConnectionSocketFactoryBuilder();
        builder.setTrustKeystore(KEYSTORE);

        exception.expect(RuntimeException.class);
        exception.expectMessage(BAD_TRUST_KEYSTORE_ERROR);

        builder.createTrustKeystore(sslContextBuilderMock, true);
    }

    @Test
    public void createKeystore() throws Exception {
        builder = new SSLConnectionSocketFactoryBuilder() {

            @Override
            protected KeyStore createKeyStore(final URL url, final String password) {
                return keyStoreMock;
            }
        };
        builder.setKeystore("file:" + KEYSTORE)
                .setKeystorePassword(PASSWORD);
        when(sslContextBuilderMock.loadKeyMaterial(keyStoreMock, PASSWORD.toCharArray())).thenReturn(sslContextBuilderMock);

        builder.createKeystore(sslContextBuilderMock, true);
        verify(sslContextBuilderMock).loadKeyMaterial(keyStoreMock, PASSWORD.toCharArray());
    }

    @Test
    public void createKeystoreWithException() throws Exception {
        builder = new SSLConnectionSocketFactoryBuilder();
        builder.setKeystore(KEYSTORE);

        exception.expect(RuntimeException.class);
        exception.expectMessage(BAD_KEYSTORE_ERROR);

        builder.createKeystore(sslContextBuilderMock, true);
    }

    @Test
    public void createKeyStoreWithException() throws Exception {
        builder = new SSLConnectionSocketFactoryBuilder();

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Keystore url may not be null");

        builder.createKeyStore(null, null);
    }

    @Test
    public void createKeyStore() throws Exception {
        builder = new SSLConnectionSocketFactoryBuilder();
        mockStatic(KeyStore.class);
        when(KeyStore.getInstance("jks")).thenReturn(keyStoreMock);
        when(urlMock.openStream()).thenReturn(inputStreamMock);
        doNothing().when(keyStoreMock).load(inputStreamMock, PASSWORD.toCharArray());
        doNothing().when(inputStreamMock).close();

        KeyStore keystore = builder.createKeyStore(urlMock, PASSWORD);
        verify(keyStoreMock).load(inputStreamMock, PASSWORD.toCharArray());
        assertEquals(keystore, keyStoreMock);
    }
}