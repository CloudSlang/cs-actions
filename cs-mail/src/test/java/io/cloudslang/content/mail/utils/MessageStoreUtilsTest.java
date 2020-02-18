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
package io.cloudslang.content.mail.utils;

import com.sun.mail.util.MailConnectException;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.constants.PropNames;
import io.cloudslang.content.mail.constants.SecurityConstants;
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.entities.GetMailInput;
import io.cloudslang.content.mail.entities.MailInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.sslconfig.EasyX509TrustManager;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.*;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Session.class, MessageStoreUtils.class, SSLContext.class, SSLUtils.class})
public class MessageStoreUtilsTest {

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

    @Before
    public void setUp() {
        PowerMockito.mockStatic(MessageStoreUtils.class);

        inputBuilder = new GetMailAttachmentInput.Builder();
        inputBuilder.hostname(HOST);
        inputBuilder.port(PopPropNames.POP3_PORT);
        inputBuilder.protocol(PopPropNames.POP3);
        inputBuilder.username(USERNAME);
        inputBuilder.password(PASSWORD);
        inputBuilder.folder(FOLDER);
        inputBuilder.messageNumber("1");
    }

    @Test
    public void testConfigureStoreWithSSL() throws Exception {
        GetMailInput input = inputBuilder.build();
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_CLASS, SecurityConstants.SSL_SOCKET_FACTORY);
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_FALLBACK, String.valueOf(false));
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.PORT, PopPropNames.POP3_PORT);
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_PORT, PopPropNames.POP3_PORT);
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock).when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(any(URLName.class));
        when(MessageStoreUtils.configureStoreWithSSL(propertiesMock, authenticatorMock, input)).thenCallRealMethod();

        Store store = MessageStoreUtils.configureStoreWithSSL(propertiesMock, authenticatorMock, input);

        assertEquals(storeMock, store);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_CLASS, SecurityConstants.SSL_SOCKET_FACTORY);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_FALLBACK, String.valueOf(false));
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.PORT, PopPropNames.POP3_PORT);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_PORT, PopPropNames.POP3_PORT);
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(any(URLName.class));
    }

    @Test
    public void testConfigureStoreWithTLS() throws Exception {
        GetMailInput input = inputBuilder.build();
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SSL_ENABLE, String.valueOf(false));
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_ENABLE, String.valueOf(true));
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_REQUIRED, String.valueOf(true));
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock).when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(any(String.class));
        when(MessageStoreUtils.configureStoreWithTLS(propertiesMock, authenticatorMock, input)).thenCallRealMethod();
        PowerMockito.doCallRealMethod()
                .when(MessageStoreUtils.class, "configureWithTLS", any(Properties.class), any(MailInput.class));

        Store store = MessageStoreUtils.configureStoreWithTLS(propertiesMock, authenticatorMock, input);

        assertEquals(storeMock, store);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SSL_ENABLE, String.valueOf(false));
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_ENABLE, String.valueOf(true));
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_REQUIRED, String.valueOf(true));
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(PopPropNames.POP3 + SecurityConstants.SECURE_SUFFIX_FOR_POP3_AND_IMAP);
    }

    /**
     * Test configureStoreWithoutSSL method.
     *
     * @throws Exception
     */
    @Test
    public void testConfigureStoreWithoutSSL() throws Exception {
        GetMailInput input = inputBuilder.build();
        doReturn(objectMock).when(propertiesMock).put(PropNames.MAIL + PopPropNames.POP3 + PropNames.HOST, HOST);
        doReturn(objectMock).when(propertiesMock).put(PropNames.MAIL + PopPropNames.POP3 + PropNames.PORT, Short.parseShort(PopPropNames.POP3_PORT));
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock)
                .when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(PopPropNames.POP3);
        when(MessageStoreUtils.configureStoreWithoutSSL(propertiesMock, authenticatorMock, input)).thenCallRealMethod();

        Store store = MessageStoreUtils.configureStoreWithoutSSL(propertiesMock, authenticatorMock, input);

        assertEquals(storeMock, store);
        verify(propertiesMock).put(PropNames.MAIL + PopPropNames.POP3 + PropNames.HOST, HOST);
        verify(propertiesMock).put(PropNames.MAIL + PopPropNames.POP3 + PropNames.PORT, Short.parseShort(PopPropNames.POP3_PORT));
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(PopPropNames.POP3);
    }

    /**
     * Test method assSSLSettings with false trustAllRoots.
     *
     * @throws Exception
     */
    @Test
    public void testAddSSLSettingsWithFalseTrustAllRoots() throws Exception {
        PowerMockito.whenNew(File.class)
                .withArguments(anyString())
                .thenReturn(fileMock);
        doReturn(true).when(fileMock).exists();
        PowerMockito.mockStatic(SSLContext.class);
        PowerMockito.doReturn(sslContextMock).when(SSLContext.class, "getInstance", anyString());
        PowerMockito.mockStatic(SSLUtils.class);
        PowerMockito.whenNew(URL.class).withArguments(anyString()).thenReturn(urlMock);
        PowerMockito.doReturn(keyStoreMock).when(SSLUtils.class, "createKeyStore", anyObject(), anyString());
        //can't mock TrustManager[] and KeyManager[] objects.
        PowerMockito.doReturn(null).when(SSLUtils.class, "createAuthTrustManagers", anyObject());
        PowerMockito.doReturn(null).when(SSLUtils.class, "createKeyManagers", anyObject(), anyObject());
        PowerMockito.whenNew(SecureRandom.class).withNoArguments().thenReturn(secureRandomMock);
        doNothing().when(sslContextMock).init(null, null, secureRandomMock);
        PowerMockito.doNothing().when(SSLContext.class, "setDefault", sslContextMock);
        PowerMockito.doCallRealMethod().when(MessageStoreUtils.class, "addSSLSettings", anyBoolean(), anyString(), anyString(), anyString(), anyString());

        MessageStoreUtils.addSSLSettings(false, StringUtils.EMPTY, StringUtils.EMPTY,
                StringUtils.EMPTY, StringUtils.EMPTY);

        PowerMockito.verifyNew(File.class, times(2))
                .withArguments(anyString());
        verify(fileMock, times(2)).exists();
        PowerMockito.verifyStatic();
        SSLContext.getInstance(anyString());
        SSLContext.setDefault(sslContextMock);
        SSLUtils.createKeyStore(Matchers.<URL>any(), anyString());
        SSLUtils.createAuthTrustManagers(Matchers.<KeyStore>anyObject());
        SSLUtils.createKeyManagers(Matchers.<KeyStore>any(), anyString());
        SSLContext.setDefault(sslContextMock);
        PowerMockito.verifyNew(SecureRandom.class).withNoArguments();
        PowerMockito.verifyNew(URL.class, times(2)).withArguments(anyString());
    }

    /**
     * Test method assSSLSettings with default keystore file, keystore password,
     * trustKeyStore and trustPassword.
     *
     * @throws Exception
     */
    @Test
    public void testAddSSLSettings() throws Exception {
        PowerMockito.mockStatic(SSLContext.class);
        PowerMockito.doReturn(sslContextMock).when(SSLContext.class, "getInstance", anyString());
        PowerMockito.whenNew(EasyX509TrustManager.class).withNoArguments().thenReturn(easyX509TrustManagerMock);
        PowerMockito.mockStatic(SSLUtils.class);
        PowerMockito.whenNew(URL.class).withArguments(anyString()).thenReturn(urlMock);
        PowerMockito.doReturn(keyStoreMock).when(SSLUtils.class, "createKeyStore", anyObject(), anyString());
        //can't mock TrustManager[] and KeyManager[] objects.
        PowerMockito.doReturn(null).when(SSLUtils.class, "createAuthTrustManagers", anyObject());
        PowerMockito.doReturn(null).when(SSLUtils.class, "createKeyManagers", anyObject(), anyObject());
        PowerMockito.whenNew(SecureRandom.class).withNoArguments().thenReturn(secureRandomMock);
        doNothing().when(sslContextMock).init(null, null, secureRandomMock);
        PowerMockito.doNothing().when(SSLContext.class, "setDefault", sslContextMock);
        PowerMockito.doCallRealMethod().when(MessageStoreUtils.class, "addSSLSettings", anyBoolean(), anyString(), anyString(), anyString(), anyString());

        MessageStoreUtils.addSSLSettings(true, "HDD:\\keystore", "keystorePass",
                "HDD:\\trustore", "truststorePass");

        PowerMockito.verifyStatic();
        SSLContext.getInstance(anyString());
        PowerMockito.verifyNew(EasyX509TrustManager.class).withNoArguments();
        PowerMockito.verifyStatic();
        SSLContext.getInstance(anyString());
        SSLContext.setDefault(sslContextMock);
        SSLUtils.createKeyStore(Matchers.<URL>any(), anyString());
        SSLUtils.createAuthTrustManagers(Matchers.<KeyStore>anyObject());
        SSLUtils.createKeyManagers(Matchers.<KeyStore>any(), anyString());
        SSLContext.setDefault(sslContextMock);
        PowerMockito.verifyNew(SecureRandom.class).withNoArguments();
    }

    @Test
    public void testCreateMessageStoreWithEnableTLSInputTrueAndEnableSSLInputFalse() throws Exception {
        when(MessageStoreUtils.createMessageStore(any(GetMailInput.class))).thenCallRealMethod();
        when(MessageStoreUtils.tryTLSOtherwiseTrySSL(any(SimpleAuthenticator.class), any(Properties.class), any(GetMailInput.class))).thenCallRealMethod();
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        PowerMockito.doNothing().when(MessageStoreUtils.class, "addSSLSettings", anyBoolean(), anyString(), anyString(), anyString(), anyString());
        when(MessageStoreUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
        doNothing().when(storeMock).connect(HOST, USERNAME, PASSWORD);
        inputBuilder.enableTLS(String.valueOf(true));
        inputBuilder.enableSSL(String.valueOf(false));

        try {
            MessageStoreUtils.createMessageStore(inputBuilder.build());
        } catch (Exception ex) {
            if (!(ex instanceof MailConnectException)) {
                fail();
            }
        } finally {
            PowerMockito.verifyStatic(times(1));
            MessageStoreUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
            PowerMockito.verifyStatic(times(1));
            MessageStoreUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class));
        }
    }

    @Test
    public void testCreateMessageStoreWithEnableTLSInputTrueExceptionAndEnableSSLInputTrue() throws Exception {
        when(MessageStoreUtils.createMessageStore(any(GetMailInput.class))).thenCallRealMethod();
        when(MessageStoreUtils.tryTLSOtherwiseTrySSL(any(SimpleAuthenticator.class), any(Properties.class), any(GetMailInput.class))).thenCallRealMethod();
        when(MessageStoreUtils.connectUsingSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenCallRealMethod();
        PowerMockito.doNothing().when(MessageStoreUtils.class, "addSSLSettings", anyBoolean(), anyString(), anyString(), anyString(), anyString());
        doThrow(AuthenticationFailedException.class).when(storeMock).connect(HOST, USERNAME, PASSWORD);
        when(MessageStoreUtils.configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
        when(MessageStoreUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
        doNothing().when(storeMock).connect();
        inputBuilder.enableSSL(String.valueOf(true));
        inputBuilder.enableTLS(String.valueOf(true));

        Store store = MessageStoreUtils.createMessageStore(inputBuilder.build());

        assertEquals(storeMock, store);
        PowerMockito.verifyStatic(times(1));
        MessageStoreUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        PowerMockito.verifyStatic(times(1));
        MessageStoreUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class));
        PowerMockito.verifyStatic(times(1));
        MessageStoreUtils.configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailInput.class));
    }

    /**
     * Test createMessageStore method with enableSSL input false.
     */
    @Test
    public void testCreateMessageStoreWithEnableSSLInputFalse() throws Exception {
        inputBuilder.enableSSL(String.valueOf(false));
        GetMailInput input = inputBuilder.build();
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        when(MessageStoreUtils.createMessageStore(input)).thenCallRealMethod();
        when(MessageStoreUtils.configureStoreWithoutSSL(any(Properties.class), any(Authenticator.class), any(GetMailInput.class))).thenReturn(storeMock);
        doNothing().when(storeMock).connect();

        assertEquals(storeMock, MessageStoreUtils.createMessageStore(input));

        PowerMockito.verifyNew(Properties.class).withNoArguments();
        PowerMockito.verifyNew(SimpleAuthenticator.class).withArguments(anyString(), anyString());
        PowerMockito.verifyStatic(times(1));
        MessageStoreUtils.configureStoreWithoutSSL(propertiesMock, authenticatorMock, input);
        verify(storeMock).connect();
    }

    /**
     * Test createMessageStore method with enableSSL input true and enableTLS input false.
     */
    @Test
    public void testCreateMessageStoreWithEnableSSLInputTrue() throws Exception {
        inputBuilder.enableSSL(String.valueOf(true));
        inputBuilder.enableTLS(String.valueOf(false));
        GetMailInput input = inputBuilder.build();
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        when(MessageStoreUtils.createMessageStore(input)).thenCallRealMethod();
        when(MessageStoreUtils.connectUsingSSL(propertiesMock, authenticatorMock, input)).thenCallRealMethod();
        PowerMockito.doNothing().when(MessageStoreUtils.class, "addSSLSettings", anyBoolean(), anyString(), anyString(), anyString(), anyString());
        when(MessageStoreUtils.configureStoreWithSSL(propertiesMock, authenticatorMock, input)).thenReturn(storeMock);
        doNothing().when(storeMock).connect();

        assertEquals(storeMock, MessageStoreUtils.createMessageStore(input));

        PowerMockito.verifyNew(Properties.class).withNoArguments();
        PowerMockito.verifyNew(SimpleAuthenticator.class).withArguments(anyString(), anyString());
        PowerMockito.verifyStatic(times(1));
        MessageStoreUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        PowerMockito.verifyStatic(times(1));
        MessageStoreUtils.configureStoreWithSSL(propertiesMock, authenticatorMock, input);
    }

}
