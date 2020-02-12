package io.cloudslang.content.mail.utils;

import com.sun.mail.util.ASCIIUtility;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.constants.PropNames;
import io.cloudslang.content.mail.constants.SecurityConstants;
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Session.class})
public class MessageStoreUtilsTest {

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

    private GetMailAttachmentInput.Builder inputBuilder;

    @Before
    public void setUp() {
        inputBuilder = new GetMailAttachmentInput.Builder();
        inputBuilder.hostname("host");
        inputBuilder.port(PopPropNames.POP3_PORT);
        inputBuilder.protocol(PopPropNames.POP3);
        inputBuilder.username("testUser");
        inputBuilder.password("testPass");
        inputBuilder.folder("INBOX");
        inputBuilder.messageNumber("1");
    }

    @Test
    public void testConfigureStoreWithSSL() throws Exception {
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_CLASS, SecurityConstants.SSL_SOCKET_FACTORY);
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_FALLBACK, String.valueOf(false));
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.PORT, PopPropNames.POP3_PORT);
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SOCKET_FACTORY_PORT, PopPropNames.POP3_PORT);
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock).when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(any(URLName.class));

        Store store = MessageStoreUtils.configureStoreWithSSL(propertiesMock, authenticatorMock, inputBuilder.build());

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
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SSL_ENABLE, String.valueOf(false));
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_ENABLE, String.valueOf(true));
        doReturn(objectMock).when(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_REQUIRED, String.valueOf(true));
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock).when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(any(String.class));

        Store store = MessageStoreUtils.configureStoreWithTLS(propertiesMock, authenticatorMock, inputBuilder.build());

        assertEquals(storeMock, store);
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.SSL_ENABLE, String.valueOf(false));
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_ENABLE, String.valueOf(true));
        verify(propertiesMock).setProperty(PropNames.MAIL + PopPropNames.POP3 + PropNames.START_TLS_REQUIRED, String.valueOf(true));
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(PopPropNames.POP3 + SecurityConstants.SECURE_SUFFIX_FOR_POP3_AND_IMAP);
    }
}
