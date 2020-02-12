package io.cloudslang.content.mail.services;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.MailConnectException;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.constants.PropNames;
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.constants.Constants;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.*;
import javax.mail.internet.MimeUtility;
import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MimeUtility.class, URLName.class, Session.class, System.class, SSLContext.class, ASCIIUtility.class, ByteArrayInputStream.class})
public class GetMailAttachmentServiceTest {

    public static final String SECURE_SUFFIX_FOR_POP3_AND_IMAP = "s";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final String HOST = "host";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPass";
    private static final String FOLDER = "INBOX";
    private static final String STR_FALSE = "false";
    private static final String STR_TRUE = "true";
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Spy
    private GetMailAttachmentService serviceSpy = new GetMailAttachmentService();
    @Mock
    private Store storeMock;
    @Mock
    private Folder folderMock;
    @Mock
    private Properties propertiesMock;
    @Mock
    private SimpleAuthenticator authenticatorMock;
    @Mock
    private Session sessionMock;
    @Mock
    private Object objectMock;
    private GetMailAttachmentInput.Builder inputBuilder;

    @Before
    public void setUp() {
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
    public void executeMessageNumberGreaterThanFolderMessageCountThrowsException() throws Exception {
        doReturn(folderMock).when(storeMock).getFolder(Matchers.anyString());
        doReturn(1).when(folderMock).getMessageCount();
        doReturn(true).when(folderMock).exists();
        doReturn(storeMock).when(serviceSpy).createMessageStore();
        inputBuilder.messageNumber("2");

        try {
            serviceSpy.execute(inputBuilder.build());
        } catch (Exception ex) {
            if(!(ex instanceof IndexOutOfBoundsException)){
                fail();
            }
        }
    }

    @Test
    public void testConfigureStoreWithSSL() throws Exception {
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_CLASS, SSL_FACTORY);
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_FALLBACK, Constants.Strings.FALSE);
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.PORT, PopPropNames.POP3_PORT);
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_PORT, PopPropNames.POP3_PORT);
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock).when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(any(URLName.class));

        serviceSpy.input = inputBuilder.build();

        Store store = serviceSpy.configureStoreWithSSL(propertiesMock, authenticatorMock);
        assertEquals(storeMock, store);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_CLASS, SSL_FACTORY);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_FALLBACK, Constants.Strings.FALSE);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.PORT, PopPropNames.POP3_PORT);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_PORT, PopPropNames.POP3_PORT);
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(any(URLName.class));
    }

    @Test
    public void testConfigureStoreWithTLS() throws Exception {
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SSL_ENABLE, Constants.Strings.FALSE);
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_ENABLE, Constants.Strings.TRUE);
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_REQUIRED, Constants.Strings.TRUE);

        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock).when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(any(String.class));

        serviceSpy.input = inputBuilder.build();

        Store store = serviceSpy.configureStoreWithTLS(propertiesMock, authenticatorMock);
        assertEquals(storeMock, store);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SSL_ENABLE, STR_FALSE);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_ENABLE, STR_TRUE);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_REQUIRED, STR_TRUE);
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(PopPropNames.POP3 + SECURE_SUFFIX_FOR_POP3_AND_IMAP);
    }

    @Test
    public void testCreateMessageStoreWithEnableTLSInputTrueAndEnableSSLInputFalse() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        doNothing().when(serviceSpy).addSSLSettings(anyInt(), anyBoolean(), anyString(), anyString(), anyString(), anyString());
        doReturn(storeMock).when(serviceSpy).configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class));
        doNothing().when(storeMock).connect(HOST, USERNAME, PASSWORD);


        inputBuilder.enableTLS(Constants.Strings.TRUE);
        inputBuilder.enableSSL(Constants.Strings.FALSE);
        serviceSpy.input = inputBuilder.build();

        try {
            serviceSpy.createMessageStore();
        } catch (MailConnectException ex) {
            verify(serviceSpy).addSSLSettings(anyInt(), anyBoolean(), anyString(), anyString(), anyString(), anyString());
            verify(serviceSpy).configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class));
        }
    }

    @Test
    public void testCreateMessageStoreWithEnableTLSInputTrueExceptionAndEnableSSLInputTrue() throws Exception {
        doNothing().when(serviceSpy).addSSLSettings(anyInt(), anyBoolean(), anyString(), anyString(), anyString(), anyString());
        doReturn(storeMock).when(serviceSpy).configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class));
        doThrow(AuthenticationFailedException.class).when(storeMock).connect(HOST, USERNAME, PASSWORD);
        doReturn(storeMock).when(serviceSpy).configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class));
        doNothing().when(storeMock).connect();

        inputBuilder.enableSSL(Constants.Strings.TRUE);
        inputBuilder.enableTLS(Constants.Strings.TRUE);
        serviceSpy.input = inputBuilder.build();

        assertEquals(storeMock, serviceSpy.createMessageStore());
        verify(serviceSpy).addSSLSettings(anyInt(), anyBoolean(), anyString(), anyString(), anyString(), anyString());
        verify(serviceSpy).configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class));
        verify(serviceSpy).configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class));
    }

}
