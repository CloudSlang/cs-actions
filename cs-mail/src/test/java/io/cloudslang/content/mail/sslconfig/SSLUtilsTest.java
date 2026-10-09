/*
 * Copyright 2021-2024 Open Text
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





package io.cloudslang.content.mail.sslconfig;

import com.sun.mail.util.MailConnectException;
import io.cloudslang.content.mail.constants.Constants;
import io.cloudslang.content.mail.constants.PropNames;
import io.cloudslang.content.mail.constants.SecurityConstants;
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.entities.GetMailInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockedStatic;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.mail.*;
import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

/**
 * Created by persdana on 11/7/2014.
 */
@ExtendWith(MockitoExtension.class)
public class SSLUtilsTest {
    private static final String NULL_URL_EXCEPTION_MESSAGE = "Keystore url may not be null";
    private static final String NULL_KEYSTORE_EXCEPTION_MESSAGE = "Keystore may not be null";
    private String password = "";
    private static final String HOST = "host";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPass";
    private static final String FOLDER = "INBOX";
    @Mock
    private Properties propertiesMock;
    @Mock
    private Object objectMock;
    @Mock
    private Session sessionMock;
    @Mock
    private Store storeMock;
    @Mock
    private SimpleAuthenticator authenticatorMock;
    @Mock
    private EasyX509TrustManager easyX509TrustManagerMock;
    @Mock
    private File fileMock;
    @Mock
    private SSLContext sslContextMock;
    @Mock
    private URL urlMock;
    @Mock
    private KeyStore keyStoreMock;
    @Mock
    private SecureRandom secureRandomMock;
    private GetMailAttachmentInput.Builder inputBuilder;
    @Mock
    private InputStream isMock;
    @Mock
    private KeyStore keystoreMock;
    @Mock
    private KeyManagerFactory keyManagerFactoryMock;
    @Mock
    private TrustManagerFactory trustManagerFactoryMock;

    @BeforeEach
    public void setUp() {
        inputBuilder = new GetMailAttachmentInput.Builder();
        inputBuilder.hostname(HOST);
        inputBuilder.port(Constants.POP3_PORT);
        inputBuilder.protocol(Constants.POP3);
        inputBuilder.username(USERNAME);
        inputBuilder.password(PASSWORD);
        inputBuilder.folder(FOLDER);
        inputBuilder.messageNumber("1");
    }

    @Test
    public void testCreateKeyStore() throws Exception {
        when(urlMock.openStream()).thenReturn(isMock);

        try (MockedStatic<KeyStore> keyStoreStatic = Mockito.mockStatic(KeyStore.class)) {
            keyStoreStatic.when(() -> KeyStore.getInstance("jks")).thenReturn(keystoreMock);
            KeyStore result = SSLUtils.createKeyStore(urlMock, password);
            assertSame(keystoreMock, result);
        }
        //Mockito.verify(isMock).close();   //error
        //Mockito.verify(urlMock).openStream(); //error
        //Mockito.verify(keystoreMock).load(isMock, password != null ? password.toCharArray() : null);
        // cannot verify/stub final method
    }

    @Test
    public void testCreateKeyStoreWithNullUrl()
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException {
        final URL url = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> SSLUtils.createKeyStore(url, password));
        assertEquals(NULL_URL_EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    public void testCreateKeyManagers() throws Exception {
        String algorithm = KeyManagerFactory.getDefaultAlgorithm();
        try (MockedStatic<KeyManagerFactory> keyManagerFactoryStatic = Mockito.mockStatic(KeyManagerFactory.class)) {
            keyManagerFactoryStatic.when(KeyManagerFactory::getDefaultAlgorithm).thenReturn(algorithm);
            keyManagerFactoryStatic.when(() -> KeyManagerFactory.getInstance(algorithm)).thenReturn(keyManagerFactoryMock);
            KeyManager[] result = SSLUtils.createKeyManagers(keystoreMock, "");
            verify(keyManagerFactoryMock).init(keystoreMock, password != null ? password.toCharArray() : null);
            assertEquals(keyManagerFactoryMock.getKeyManagers(), result);
        }
    }

    @Test
    public void testCreateKeyManagersWithNullKeyStore() throws Exception {
        final KeyStore keyStore = null;
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> SSLUtils.createKeyManagers(keyStore, ""));
        assertEquals(NULL_KEYSTORE_EXCEPTION_MESSAGE, exception.getMessage());
    }

    @Test
    public void testCreateAuthTrustManagers() throws Exception {
        TrustManager[] trustManagers = new TrustManager[3];
        X509TrustManager tm = Mockito.mock(X509TrustManager.class);
        trustManagers[1] = tm;

        String algorithm = TrustManagerFactory.getDefaultAlgorithm();
        try (MockedStatic<TrustManagerFactory> trustManagerFactoryStatic = Mockito.mockStatic(TrustManagerFactory.class)) {
            trustManagerFactoryStatic.when(TrustManagerFactory::getDefaultAlgorithm).thenReturn(algorithm);
            trustManagerFactoryStatic.when(() -> TrustManagerFactory.getInstance(algorithm)).thenReturn(trustManagerFactoryMock);
            Mockito.doReturn(trustManagers).when(trustManagerFactoryMock).getTrustManagers();
            TrustManager[] result = SSLUtils.createAuthTrustManagers(keystoreMock);
            assertEquals(trustManagers, result);
            verify(trustManagerFactoryMock).init(keystoreMock);
            assertTrue(result[1] instanceof AuthSSLX509TrustManager);
        }
    }

    @Test
    public void testConfigureStoreWithSSL() throws Exception {
        GetMailInput input = inputBuilder.build();
        doReturn(objectMock).when(propertiesMock)
                .setProperty(String.format(PropNames.MAIL_SOCKET_FACTORY_CLASS, Constants.POP3), SecurityConstants.SSL_SOCKET_FACTORY);
        doReturn(objectMock).when(propertiesMock)
                .setProperty(String.format(PropNames.MAIL_SOCKET_FACTORY_FALLBACK, Constants.POP3), String.valueOf(false));
        doReturn(objectMock).when(propertiesMock)
                .setProperty(String.format(PropNames.MAIL_PORT, Constants.POP3), Constants.POP3_PORT);
        doReturn(objectMock).when(propertiesMock)
                .setProperty(String.format(PropNames.MAIL_SOCKET_FACTORY_PORT, Constants.POP3), Constants.POP3_PORT);
        try (MockedStatic<Session> sessionStatic = Mockito.mockStatic(Session.class)) {
        sessionStatic.when(() -> Session.getInstance(any(Properties.class), any(Authenticator.class))).thenReturn(sessionMock);
        doReturn(storeMock).when(sessionMock).getStore(any(URLName.class));

        Store store = SSLUtils.configureStoreWithSSL(propertiesMock, authenticatorMock, input);

        assertEquals(storeMock, store);
        verify(propertiesMock).setProperty(String.format(PropNames.MAIL_SOCKET_FACTORY_CLASS, Constants.POP3), SecurityConstants.SSL_SOCKET_FACTORY);
        verify(propertiesMock).setProperty(String.format(PropNames.MAIL_SOCKET_FACTORY_FALLBACK, Constants.POP3), String.valueOf(false));
        verify(propertiesMock).setProperty(String.format(PropNames.MAIL_PORT, Constants.POP3), Constants.POP3_PORT);
        verify(propertiesMock).setProperty(String.format(PropNames.MAIL_SOCKET_FACTORY_PORT, Constants.POP3), Constants.POP3_PORT);
        sessionStatic.verify(() -> Session.getInstance(any(Properties.class), any(Authenticator.class)));
        verify(sessionMock).getStore(any(URLName.class));
        }
    }

    @Test
    public void testConfigureStoreWithTLS() throws Exception {
        GetMailInput input = inputBuilder.build();
        doReturn(objectMock).when(propertiesMock)
                .setProperty(String.format(PropNames.MAIL_SSL_ENABLE, Constants.POP3), String.valueOf(false));
        doReturn(objectMock).when(propertiesMock)
                .setProperty(String.format(PropNames.MAIL_STARTTLS_ENABLE, Constants.POP3), String.valueOf(true));
        doReturn(objectMock).when(propertiesMock)
                .setProperty(String.format(PropNames.MAIL_STARTTLS_REQUIRED, Constants.POP3), String.valueOf(true));
        try (MockedStatic<Session> sessionStatic = Mockito.mockStatic(Session.class)) {
        sessionStatic.when(() -> Session.getInstance(any(Properties.class), any(Authenticator.class))).thenReturn(sessionMock);
        doReturn(storeMock).when(sessionMock).getStore(any(String.class));

        Store store = SSLUtils.configureStoreWithTLS(propertiesMock, authenticatorMock, input);

        assertEquals(storeMock, store);
        verify(propertiesMock).setProperty(String.format(PropNames.MAIL_SSL_ENABLE, Constants.POP3), String.valueOf(false));
        verify(propertiesMock).setProperty(String.format(PropNames.MAIL_STARTTLS_ENABLE, Constants.POP3), String.valueOf(true));
        verify(propertiesMock).setProperty(String.format(PropNames.MAIL_STARTTLS_REQUIRED, Constants.POP3), String.valueOf(true));
        sessionStatic.verify(() -> Session.getInstance(any(Properties.class), any(Authenticator.class)));
        verify(sessionMock).getStore(Constants.POP3);
        }
    }

    /**
     * Test configureStoreWithoutSSL method.
     *
     * @throws Exception
     */
    @Test
    public void testConfigureStoreWithoutSSL() throws Exception {
        GetMailInput input = inputBuilder.build();
        doReturn(objectMock).when(propertiesMock).put(String.format(PropNames.MAIL_HOST, Constants.POP3), HOST);
        doReturn(objectMock).when(propertiesMock).put(String.format(PropNames.MAIL_PORT, Constants.POP3), Short.parseShort(Constants.POP3_PORT));
        try (MockedStatic<Session> sessionStatic = Mockito.mockStatic(Session.class);
             MockedStatic<SSLUtils> sslUtilsStatic = Mockito.mockStatic(SSLUtils.class)) {
            sessionStatic.when(() -> Session.getInstance(any(Properties.class), any(Authenticator.class))).thenReturn(sessionMock);
            doReturn(storeMock).when(sessionMock).getStore(any(URLName.class));
            sslUtilsStatic.when(() -> SSLUtils.configureStoreWithoutSSL(any(Properties.class), any(Authenticator.class), any(GetMailInput.class))).thenCallRealMethod();

            Store store = SSLUtils.configureStoreWithoutSSL(propertiesMock, authenticatorMock, input);

            assertEquals(storeMock, store);
            verify(propertiesMock).put(String.format(PropNames.MAIL_HOST, Constants.POP3), HOST);
            verify(propertiesMock).put(String.format(PropNames.MAIL_PORT, Constants.POP3), Short.parseShort(Constants.POP3_PORT));
            sessionStatic.verify(() -> Session.getInstance(any(Properties.class), any(Authenticator.class)));
            verify(sessionMock).getStore(any(URLName.class));
        }
    }

    /**
     * Test method assSSLSettings with false trustAllRoots.
     *
     * @throws Exception
     */
    @Test
    public void testAddSSLSettingsWithFalseTrustAllRoots() throws Exception {
        try (MockedConstruction<File> files = Mockito.mockConstruction(File.class,
                    (mock, context) -> when(mock.exists()).thenReturn(true));
             MockedConstruction<URL> urls = Mockito.mockConstruction(URL.class);
             MockedConstruction<SecureRandom> randoms = Mockito.mockConstruction(SecureRandom.class);
             MockedStatic<SSLContext> sslContextStatic = Mockito.mockStatic(SSLContext.class);
             MockedStatic<SSLUtils> sslUtilsStatic = Mockito.mockStatic(SSLUtils.class)) {
            sslContextStatic.when(() -> SSLContext.getInstance(anyString())).thenReturn(sslContextMock);
            sslUtilsStatic.when(() -> SSLUtils.createKeyStore(any(URL.class), anyString())).thenReturn(keyStoreMock);
            sslUtilsStatic.when(() -> SSLUtils.createAuthTrustManagers(any(KeyStore.class))).thenReturn(null);
            sslUtilsStatic.when(() -> SSLUtils.createKeyManagers(any(KeyStore.class), anyString())).thenReturn(null);
            sslUtilsStatic.when(() -> SSLUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString())).thenCallRealMethod();

            SSLUtils.addSSLSettings(false, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY);

            assertEquals(2, files.constructed().size());
            files.constructed().forEach(file -> verify(file).exists());
            sslContextStatic.verify(() -> SSLContext.getInstance(anyString()), times(1));
            sslContextStatic.verify(() -> SSLContext.setDefault(sslContextMock), times(1));
            sslUtilsStatic.verify(() -> SSLUtils.createKeyStore(any(URL.class), anyString()), times(2));
            sslUtilsStatic.verify(() -> SSLUtils.createAuthTrustManagers(any(KeyStore.class)));
            sslUtilsStatic.verify(() -> SSLUtils.createKeyManagers(any(KeyStore.class), anyString()));
            assertEquals(1, randoms.constructed().size());
            assertEquals(2, urls.constructed().size());
        }
    }

    /**
     * Test method assSSLSettings with default keystore file, keystore password,
     * trustKeyStore and trustPassword.
     *
     * @throws Exception
     */
    @Test
    public void testAddSSLSettings() throws Exception {
        try (MockedConstruction<EasyX509TrustManager> trustManagers = Mockito.mockConstruction(EasyX509TrustManager.class);
             MockedConstruction<URL> urls = Mockito.mockConstruction(URL.class);
             MockedConstruction<SecureRandom> randoms = Mockito.mockConstruction(SecureRandom.class);
             MockedStatic<SSLContext> sslContextStatic = Mockito.mockStatic(SSLContext.class);
             MockedStatic<SSLUtils> sslUtilsStatic = Mockito.mockStatic(SSLUtils.class)) {
            sslContextStatic.when(() -> SSLContext.getInstance(anyString())).thenReturn(sslContextMock);
            sslUtilsStatic.when(() -> SSLUtils.createKeyStore(any(URL.class), anyString())).thenReturn(keyStoreMock);
            sslUtilsStatic.when(() -> SSLUtils.createAuthTrustManagers(any(KeyStore.class))).thenReturn(null);
            sslUtilsStatic.when(() -> SSLUtils.createKeyManagers(any(KeyStore.class), anyString())).thenReturn(null);
            sslUtilsStatic.when(() -> SSLUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString())).thenCallRealMethod();
            doNothing().when(sslContextMock).init(any(), any(), any());

            SSLUtils.addSSLSettings(true, "HDD:\\keystore", "keystorePass", "HDD:\\trustore", "truststorePass");

            assertEquals(1, trustManagers.constructed().size());
            sslContextStatic.verify(() -> SSLContext.getInstance(anyString()), times(1));
            sslContextStatic.verify(() -> SSLContext.setDefault(sslContextMock), times(1));
            sslUtilsStatic.verify(() -> SSLUtils.createKeyStore(any(URL.class), anyString()), times(0));
            sslUtilsStatic.verify(() -> SSLUtils.createAuthTrustManagers(any(KeyStore.class)), times(0));
            sslUtilsStatic.verify(() -> SSLUtils.createKeyManagers(any(KeyStore.class), anyString()), times(0));
            assertEquals(1, randoms.constructed().size());
            assertEquals(0, urls.constructed().size());
        }
    }

    @Test
    public void testCreateMessageStoreWithEnableTLSInputTrueAndEnableSSLInputFalse() throws Exception {
        try (MockedStatic<SSLUtils> sslUtilsStatic = Mockito.mockStatic(SSLUtils.class)) {
            sslUtilsStatic.when(() -> SSLUtils.createMessageStore(any(GetMailInput.class))).thenCallRealMethod();
            sslUtilsStatic.when(() -> SSLUtils.tryTLSOtherwiseTrySSL(any(SimpleAuthenticator.class), any(Properties.class), any(GetMailInput.class))).thenCallRealMethod();
            sslUtilsStatic.when(() -> SSLUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString())).thenAnswer(invocation -> null);
            sslUtilsStatic.when(() -> SSLUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
            doNothing().when(storeMock).connect(HOST, USERNAME, PASSWORD);
            inputBuilder.enableTLS(String.valueOf(true));
            inputBuilder.enableSSL(String.valueOf(false));

            try {
                SSLUtils.createMessageStore(inputBuilder.build());
            } catch (Exception ex) {
                if (!(ex instanceof MailConnectException)) {
                    fail(ex);
                }
            }
            sslUtilsStatic.verify(() -> SSLUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString()), times(1));
            sslUtilsStatic.verify(() -> SSLUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class)), times(1));
        }
    }

    @Test
    public void testCreateMessageStoreWithEnableTLSInputTrueExceptionAndEnableSSLInputTrue() throws Exception {
        try (MockedStatic<SSLUtils> sslUtilsStatic = Mockito.mockStatic(SSLUtils.class)) {
            sslUtilsStatic.when(() -> SSLUtils.createMessageStore(any(GetMailInput.class))).thenCallRealMethod();
            sslUtilsStatic.when(() -> SSLUtils.tryTLSOtherwiseTrySSL(any(SimpleAuthenticator.class), any(Properties.class), any(GetMailInput.class))).thenCallRealMethod();
            sslUtilsStatic.when(() -> SSLUtils.connectUsingSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenCallRealMethod();
            sslUtilsStatic.when(() -> SSLUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString())).thenAnswer(invocation -> null);
            doThrow(AuthenticationFailedException.class).when(storeMock).connect(HOST, USERNAME, PASSWORD);
            sslUtilsStatic.when(() -> SSLUtils.configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
            sslUtilsStatic.when(() -> SSLUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
            doNothing().when(storeMock).connect();
            inputBuilder.enableSSL(String.valueOf(true));
            inputBuilder.enableTLS(String.valueOf(true));

            Store store = SSLUtils.createMessageStore(inputBuilder.build());

            assertEquals(storeMock, store);
            sslUtilsStatic.verify(() -> SSLUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString()), times(1));
            sslUtilsStatic.verify(() -> SSLUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class)), times(1));
            sslUtilsStatic.verify(() -> SSLUtils.configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class)), times(1));
        }
    }

    /**
     * Test createMessageStore method with enableSSL input false.
     */
    @Test
    public void testCreateMessageStoreWithEnableSSLInputFalse() throws Exception {
        inputBuilder.enableSSL(String.valueOf(false));
        GetMailInput input = inputBuilder.build();
        try (MockedStatic<SSLUtils> sslUtilsStatic = Mockito.mockStatic(SSLUtils.class)) {
            sslUtilsStatic.when(() -> SSLUtils.createMessageStore(any(GetMailInput.class))).thenCallRealMethod();
            sslUtilsStatic.when(() -> SSLUtils.configureStoreWithoutSSL(any(Properties.class), any(Authenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
            doNothing().when(storeMock).connect();

            assertEquals(storeMock, SSLUtils.createMessageStore(input));

            sslUtilsStatic.verify(() -> SSLUtils.configureStoreWithoutSSL(any(Properties.class), any(Authenticator.class), any(GetMailInput.class)), times(1));
            verify(storeMock).connect();
        }
    }

    /**
     * Test createMessageStore method with enableSSL input true and enableTLS input false.
     */
    @Test
    public void testCreateMessageStoreWithEnableSSLInputTrue() throws Exception {
        inputBuilder.enableSSL(String.valueOf(true));
        inputBuilder.enableTLS(String.valueOf(false));
        GetMailInput input = inputBuilder.build();
        try (MockedStatic<SSLUtils> sslUtilsStatic = Mockito.mockStatic(SSLUtils.class)) {
            sslUtilsStatic.when(() -> SSLUtils.createMessageStore(any(GetMailInput.class))).thenCallRealMethod();
            sslUtilsStatic.when(() -> SSLUtils.connectUsingSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenCallRealMethod();
            sslUtilsStatic.when(() -> SSLUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString())).thenAnswer(invocation -> null);
            sslUtilsStatic.when(() -> SSLUtils.configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
            doNothing().when(storeMock).connect();

            assertEquals(storeMock, SSLUtils.createMessageStore(input));

            sslUtilsStatic.verify(() -> SSLUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString()), times(1));
            sslUtilsStatic.verify(() -> SSLUtils.configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class)), times(1));
        }
    }
}
