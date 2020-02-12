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
package io.cloudslang.content.mail.services;

import com.sun.mail.util.ASCIIUtility;
import com.sun.mail.util.MailConnectException;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.entities.GetMailBaseInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.utils.MessageStoreUtils;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MimeUtility.class, URLName.class, Session.class, System.class, SSLContext.class, ASCIIUtility.class, ByteArrayInputStream.class, MessageStoreUtils.class})
public class GetMailAttachmentServiceTest {

    private static final String HOST = "host";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPass";
    private static final String FOLDER = "INBOX";
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
    private GetMailAttachmentInput.Builder inputBuilder;
    @Mock
    private MessageStoreUtils storeUtilsMock;

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
    public void executeMessageNumberGreaterThanFolderMessageCountThrowsException() throws Exception {
        doReturn(folderMock).when(storeMock).getFolder(Matchers.anyString());
        doReturn(1).when(folderMock).getMessageCount();
        doReturn(true).when(folderMock).exists();
        when(MessageStoreUtils.createMessageStore(any(GetMailBaseInput.class))).thenReturn(storeMock);
        inputBuilder.messageNumber("2");

        try {
            serviceSpy.execute(inputBuilder.build());
        } catch (Exception ex) {
            if (!(ex instanceof IndexOutOfBoundsException)) {
                fail();
            }
        }
    }

    @Test
    public void testCreateMessageStoreWithEnableTLSInputTrueAndEnableSSLInputFalse() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        when(MessageStoreUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString())).thenReturn(null);
        when(MessageStoreUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailBaseInput.class))).thenReturn(storeMock);
        doNothing().when(storeMock).connect(HOST, USERNAME, PASSWORD);
        inputBuilder.enableTLS(String.valueOf(true));
        inputBuilder.enableSSL(String.valueOf(false));

        try {
            storeUtilsMock.createMessageStore(inputBuilder.build());
            fail();
        } catch (MailConnectException ex) {

        } finally {
            PowerMockito.verifyStatic(times(1));
            verify(storeUtilsMock).addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
            PowerMockito.verifyStatic(times(1));
            verify(storeUtilsMock).configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailBaseInput.class));
        }
    }

    @Test
    public void testCreateMessageStoreWithEnableTLSInputTrueExceptionAndEnableSSLInputTrue() throws Exception {
        when(MessageStoreUtils.addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString())).thenReturn(null);
        when(MessageStoreUtils.configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailBaseInput.class))).thenReturn(storeMock);
        doThrow(AuthenticationFailedException.class).when(storeMock).connect(HOST, USERNAME, PASSWORD);
        when(MessageStoreUtils.configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailBaseInput.class))).thenReturn(storeMock);
        doNothing().when(storeMock).connect();
        inputBuilder.enableSSL(String.valueOf(true));
        inputBuilder.enableTLS(String.valueOf(true));

        Store store = MessageStoreUtils.createMessageStore(inputBuilder.build());

        assertEquals(storeMock, store);
        verify(storeUtilsMock).addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        verify(storeUtilsMock).configureStoreWithTLS(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailBaseInput.class));
        verify(storeUtilsMock).configureStoreWithSSL(any(Properties.class), any(SimpleAuthenticator.class), any(GetMailBaseInput.class));
    }

}
