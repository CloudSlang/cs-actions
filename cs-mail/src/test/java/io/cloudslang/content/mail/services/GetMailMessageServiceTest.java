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
import io.cloudslang.content.mail.entities.GetMailMessageInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.entities.StringOutputStream;
import io.cloudslang.content.mail.sslconfig.EasyX509TrustManager;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import io.cloudslang.content.mail.utils.Constants.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeUtility;
import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by giloan on 11/6/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetMailMessageService.class, MimeUtility.class, URLName.class, Session.class, System.class, SSLContext.class,
        SSLUtils.class, ASCIIUtility.class, ByteArrayInputStream.class})
public class GetMailMessageServiceTest {

    public static final int READ_ONLY = 1;
    public static final String SUBJECT_TEST = "subjectTest";
    private static final String FOLDER_DOES_NOT_EXIST = "The specified folder does not exist on the remote server.";
    // operation inputs
    private static final String HOST = "host";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPass";
    private static final String FOLDER = "INBOX";
    private static final String TRUST_ALL_ROOTS_TRUE = "true";
    private static final String TRUST_ALL_ROOTS_FALSE = "false";
    private static final String MESSAGE_NUMBER = "1";
    private static final String SUBJECT_ONLY_TRUE = "true";
    private static final String ENABLE_SSL_TRUE = "true";
    private static final String KEYSTORE = "HDD:\\keystore";
    private static final String KEYSTORE_PASSWORD = "keystorePass";
    private static final String TRUST_KEYSTORE = "HDD:\\trustore";
    private static final String TRUST_PASSWORD = "truststorePass";
    private static final String CHARACTERSET = "UTF-16";
    private static final String DEFAULT_CHARACTERSET = "UTF-8";
    private static final String DELETE_UPON_RETRIEVAL_TRUE = "true";
    // operation return codes
    private static final String RETURN_CODE = "returnCode";
    private static final String SUCCESS_RETURN_CODE = "0";
    //operation results
    private static final String RETURN_RESULT = "returnResult";
    private static final String SUBJECT_RESULT = "subject";
    private static final String BODY_RESULT = "body";
    private static final String ATTACHED_FILE_NAMES_RESULT = "attachedFileNames";
    private static final String BAD_CHARACTERSET = "badCharSet";
    private static final String STR_FALSE = "false";
    private static final String STR_TRUE = "true";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    public static final String SECURE_SUFFIX_FOR_POP3_AND_IMAP = "s";
    private String testSeparator = "\\";
    private String testJavaHome = "HDD:\\java";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEXT_HTML = "text/html";
    private String messageMockToString = "stringMessageMock";
    private String cmessageMock = "testcmeesage";

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Spy
    private GetMailMessageService serviceSpy = new GetMailMessageService();
    private GetMailMessageInput.Builder inputBuilder;
    @Spy
    private GetMailMessageInput.Builder inputBuilderSpy = new GetMailMessageInput.Builder();
    @Mock
    private Store storeMock;
    @Mock
    private Folder folderMock;
    @Mock
    private Message messageMock;
    @Mock
    private StringOutputStream stringOutputStreamMock;
    @Mock
    private Properties propertiesMock;
    @Mock
    private SimpleAuthenticator authenticatorMock;
    @Mock
    private URLName urlNameMock;
    @Mock
    private Session sessionMock;
    @Mock
    private Object objectMock;
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
    @Mock
    private EasyX509TrustManager easyX509TrustManagerMock;
    @Mock
    private Multipart multipartMock;
    @Mock
    private BodyPart partMock;
    @Mock
    private InputStream inputStreamMock;
    @Mock
    private ByteArrayInputStream byteArrayInputStreamMock;

    @Before
    public void setUp() {
        inputBuilder = new GetMailMessageInput.Builder();
        addRequiredInputs();
    }

    @After
    public void tearDown() {
        inputBuilder = null;
    }

    /**
     * Test getMessage method throws folder not found exception.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessageThrowsFolderNotFoundException() throws Exception {
        doReturn(storeMock).when(serviceSpy).createMessageStore();
        doReturn(folderMock).when(storeMock).getFolder(anyString());
        doReturn(false).when(folderMock).exists();
        serviceSpy.input = inputBuilder.build();

        exception.expect(Exception.class);
        exception.expectMessage(FOLDER_DOES_NOT_EXIST);
        serviceSpy.getMessage();
    }

    /**
     * Test getMessage method throws exception because message number input is greater than the number
     * of messages contained in the folder.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessageThrowsMessageNumberException() throws Exception {
        doReturn(storeMock).when(serviceSpy).createMessageStore();
        doReturn(folderMock).when(storeMock).getFolder(anyString());
        doReturn(true).when(folderMock).exists();
        doReturn(READ_ONLY).when(serviceSpy).getFolderOpenMode();
        doNothing().when(folderMock).open(READ_ONLY);
        doReturn(0).when(folderMock).getMessageCount();

        addRequiredInputs();
        serviceSpy.input = inputBuilder.build();

        exception.expect(Exception.class);
        exception.expectMessage("message value was: " + MESSAGE_NUMBER + " there are only " + 0 +
                " messages in folder");
        serviceSpy.getMessage();
    }

    /**
     * Test execute method with default values for trustAllRoots(true), enableSSL(false)
     * subjectOnly(false), deleteUponRetrieval(false).
     *
     * @throws Exception
     */
    @Test
    public void testExecuteWithTrueSubjectOnlyAndDeleteUponRetrievalAndEmptyCharacterSet() throws Exception {
        doReturn(messageMock).when(serviceSpy).getMessage();
        doNothing().when(messageMock).setFlag(Flags.Flag.DELETED, true);
        doReturn(folderMock).when(messageMock).getFolder();
        String subjectTest = SUBJECT_TEST;
        doReturn(subjectTest).when(messageMock).getSubject();

        addRequiredInputs();
        inputBuilder.subjectOnly(STR_TRUE);
        inputBuilder.deleteUponRetrieval(STR_TRUE);
        inputBuilder.characterSet(Strings.EMPTY);

        Map<String, String> result = serviceSpy.execute(inputBuilder.build());
        assertEquals(subjectTest, result.get(RETURN_RESULT));
        assertEquals(subjectTest, result.get(SUBJECT_RESULT));
        verify(serviceSpy).getMessage();
        verify(messageMock).setFlag(Flags.Flag.DELETED, true);
        verify(messageMock).getSubject();
    }

    /**
     * Test execute method with default values for trustAllRoots(true), enableSSL(false)
     * subjectOnly(false), deleteUponRetrieval(false).
     *
     * @throws Exception
     */
    @Test
    public void testExecuteWithGivenCharacterSet() throws Exception {
        doReturn(messageMock).when(serviceSpy).getMessage();
        doNothing().when(messageMock).setFlag(Flags.Flag.DELETED, true);
        doReturn(folderMock).when(messageMock).getFolder();
        doReturn(new String[]{"1"}).when(messageMock).getHeader(anyString());
        doReturn(SUBJECT_TEST).when(serviceSpy).changeHeaderCharset("1", CHARACTERSET);

        addRequiredInputs();
        inputBuilder.subjectOnly(STR_TRUE);
        inputBuilder.deleteUponRetrieval(STR_TRUE);
        inputBuilder.characterSet(CHARACTERSET);

        Map<String, String> result = serviceSpy.execute(inputBuilder.build());
        assertEquals(SUBJECT_TEST, result.get(RETURN_RESULT));
        assertEquals(SUBJECT_TEST, result.get(SUBJECT_RESULT));
        verify(serviceSpy).getMessage();
        verify(messageMock).getHeader("Subject");
        verify(serviceSpy).changeHeaderCharset(anyString(), anyString());
    }

    /**
     * Test execute method with default value for subjectOnly input (false).
     *
     * @throws Exception
     */
    @Test
    public void testExecuteWithFalseSubjectOnly() throws Exception {
        doReturn(messageMock).when(serviceSpy).getMessage();
        doNothing().when(messageMock).setFlag(Flags.Flag.DELETED, true);
        doReturn(folderMock).when(messageMock).getFolder();
        doReturn(new String[]{"1"}).when(messageMock).getHeader(anyString());
        doReturn(SUBJECT_TEST).when(serviceSpy).changeHeaderCharset("1", CHARACTERSET);
        String attachedFileNames = "testAttachedFileNames";
        doReturn(attachedFileNames).when(serviceSpy).getAttachedFileNames(messageMock);
        doReturn(attachedFileNames).when(serviceSpy).changeHeaderCharset(attachedFileNames, CHARACTERSET);
        doReturn(attachedFileNames).when(serviceSpy).decodeAttachedFileNames(attachedFileNames);
        // Get the message body test
        String messageContent = "testMessageContent";
        Map<String, String> messageContentByType = new HashMap<>();
        messageContentByType.put("text/html", messageContent);
        doReturn(messageContentByType).when(serviceSpy).getMessageByContentTypes(messageMock, CHARACTERSET);
        PowerMockito.whenNew(StringOutputStream.class).withNoArguments().thenReturn(stringOutputStreamMock);
        doNothing().when(messageMock).writeTo(stringOutputStreamMock);
        String stringOutputStreamMockToString = "testStream";
        doReturn(stringOutputStreamMockToString).when(stringOutputStreamMock).toString();

        addRequiredInputs();
        inputBuilder.subjectOnly(Strings.EMPTY);
        inputBuilder.characterSet(CHARACTERSET);

        Map<String, String> result = serviceSpy.execute(inputBuilder.build());
        verify(serviceSpy).getMessage();
        verify(messageMock).getHeader("Subject");
        assertEquals(SUBJECT_TEST, result.get(SUBJECT_RESULT));
        verify(serviceSpy).changeHeaderCharset("1", CHARACTERSET);
        verify(serviceSpy, times(2)).changeHeaderCharset(anyString(), anyString());
        verify(serviceSpy).decodeAttachedFileNames(attachedFileNames);
        assertEquals(attachedFileNames, result.get(ATTACHED_FILE_NAMES_RESULT));

        assertEquals(messageContent, result.get(BODY_RESULT));

        verify(serviceSpy).getMessageByContentTypes(messageMock, CHARACTERSET);
        PowerMockito.verifyNew(StringOutputStream.class).withNoArguments();
        verify(messageMock).writeTo(stringOutputStreamMock);
        assertEquals(stringOutputStreamMockToString, result.get(RETURN_RESULT));
        assertEquals(SUCCESS_RETURN_CODE, result.get(RETURN_CODE));
    }

    /**
     * Test execute method with default value for subjectOnly input (false) and null character set.
     *
     * @throws Exception
     */
    @Test
    public void testExecuteWithFalseSubjectOnlyAndNullCharacterSet() throws Exception {
        doReturn(messageMock).when(serviceSpy).getMessage();
        doNothing().when(messageMock).setFlag(Flags.Flag.DELETED, true);
        doReturn(SUBJECT_TEST).when(messageMock).getSubject();
        doReturn(folderMock).when(messageMock).getFolder();
        String attachedFileNames = "testAttachedFileNames";
        doReturn(attachedFileNames).when(serviceSpy).getAttachedFileNames(messageMock);
        doReturn(attachedFileNames).when(serviceSpy).decodeAttachedFileNames(attachedFileNames);
        // Get the message body test
        Map<String, String> messageContentByType = new HashMap<>();
        String messageContent = "testMessageContent";
        messageContentByType.put("text/html", messageContent);
        doReturn(messageContentByType).when(serviceSpy).getMessageByContentTypes(messageMock, null);
        PowerMockito.whenNew(StringOutputStream.class).withNoArguments().thenReturn(stringOutputStreamMock);
        doNothing().when(messageMock).writeTo(stringOutputStreamMock);
        String stringOutputStreamMockToString = "testStream";
        String fiddledStringOutputStreamMockToString = (char) 0 + stringOutputStreamMockToString + (char) 0;
        doReturn(fiddledStringOutputStreamMockToString).when(stringOutputStreamMock).toString();

        addRequiredInputs();
        inputBuilder.subjectOnly(Strings.EMPTY);

        Map<String, String> result = serviceSpy.execute(inputBuilder.build());
        verify(serviceSpy).getMessage();
        assertEquals(SUBJECT_TEST, result.get(SUBJECT_RESULT));
        verify(serviceSpy).getAttachedFileNames(messageMock);
        verify(serviceSpy).decodeAttachedFileNames(attachedFileNames);
        assertEquals(attachedFileNames, result.get(ATTACHED_FILE_NAMES_RESULT));
        assertEquals(messageContent, result.get(BODY_RESULT));
        verify(serviceSpy).getMessageByContentTypes(messageMock, null);
        PowerMockito.verifyNew(StringOutputStream.class).withNoArguments();
        verify(messageMock).writeTo(stringOutputStreamMock);
        assertEquals(stringOutputStreamMockToString, result.get(RETURN_RESULT));
        assertEquals(SUCCESS_RETURN_CODE, result.get(RETURN_CODE));
    }

    /**
     * Test execute method throws UnsupportedEncodingException.
     *
     * @throws Exception
     */
    @Test
    public void testExecuteUnsupportedEncodingException() throws Exception {
        doReturn(messageMock).when(serviceSpy).getMessage();
        doNothing().when(messageMock).setFlag(Flags.Flag.DELETED, true);
        doReturn(new String[]{"1"}).when(messageMock).getHeader(anyString());
        doReturn(SUBJECT_TEST).when(serviceSpy).changeHeaderCharset("1", CHARACTERSET);
        PowerMockito.mockStatic(MimeUtility.class);
        PowerMockito.doThrow(new UnsupportedEncodingException(Strings.EMPTY)).when(MimeUtility.class, "decodeText", anyString());

        addRequiredInputs();
        inputBuilder.subjectOnly(Strings.EMPTY);
        inputBuilder.characterSet(BAD_CHARACTERSET);

        exception.expect(Exception.class);
        exception.expectMessage("The given encoding (" + BAD_CHARACTERSET + ") is invalid or not supported.");
        serviceSpy.execute(inputBuilder.build());
    }

    /**
     * Test createMessageStore method with enableSSL input false.
     */
    @Test
    public void testCreateMessageStoreWithEnableSSLInputFalse() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        doReturn(storeMock).when(serviceSpy).configureStoreWithoutSSL(propertiesMock, authenticatorMock);
        doNothing().when(storeMock).connect();

        addRequiredInputs();
        inputBuilder.enableSSL(STR_FALSE);
        serviceSpy.input = inputBuilder.build();

        assertEquals(storeMock, serviceSpy.createMessageStore());
        PowerMockito.verifyNew(Properties.class).withNoArguments();
        PowerMockito.verifyNew(SimpleAuthenticator.class).withArguments(anyString(), anyString());
        verify(serviceSpy).configureStoreWithoutSSL(propertiesMock, authenticatorMock);
        verify(storeMock).connect();
    }

    /**
     * Test createMessageStore method with enableSSL input true and enableTLS input false.
     */
    @Test
    public void testCreateMessageStoreWithEnableSSLInputTrue() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        doNothing().when(serviceSpy).addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        doReturn(storeMock).when(serviceSpy).configureStoreWithSSL(propertiesMock, authenticatorMock);
        doNothing().when(storeMock).connect();

        addRequiredInputs();
        inputBuilder.enableSSL(STR_TRUE);
        inputBuilder.enableTLS(STR_FALSE);
        serviceSpy.input = inputBuilder.build();

        assertEquals(storeMock, serviceSpy.createMessageStore());
        PowerMockito.verifyNew(Properties.class).withNoArguments();
        PowerMockito.verifyNew(SimpleAuthenticator.class).withArguments(anyString(), anyString());
        verify(serviceSpy).addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        verify(serviceSpy).configureStoreWithSSL(propertiesMock, authenticatorMock);
    }

    @Test
    public void testCreateMessageStoreWithEnableTLSInputTrueAndEnableSSLInputFalse() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        doNothing().when(serviceSpy).addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        doReturn(storeMock).when(serviceSpy).configureStoreWithTLS(propertiesMock, authenticatorMock);
        doNothing().when(storeMock).connect(HOST, USERNAME, PASSWORD);

        addRequiredInputs();
        inputBuilder.enableSSL(STR_FALSE);
        inputBuilder.enableTLS(STR_TRUE);
        serviceSpy.input = inputBuilder.build();

        assertEquals(storeMock, serviceSpy.createMessageStore());
        PowerMockito.verifyNew(Properties.class).withNoArguments();
        PowerMockito.verifyNew(SimpleAuthenticator.class).withArguments(anyString(), anyString());
        verify(serviceSpy).addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        verify(serviceSpy).configureStoreWithTLS(propertiesMock, authenticatorMock);
    }

    @Test
    public void testCreateMessageStoreWithEnableTLSInputTrueExceptionAndEnableSSLInputTrue() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        doNothing().when(serviceSpy).addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        doReturn(storeMock).when(serviceSpy).configureStoreWithTLS(propertiesMock, authenticatorMock);
        doThrow(AuthenticationFailedException.class).when(storeMock).connect(HOST, USERNAME, PASSWORD);
        doReturn(storeMock).when(serviceSpy).configureStoreWithSSL(propertiesMock, authenticatorMock);
        doNothing().when(storeMock).connect();

        addRequiredInputs();
        inputBuilder.enableSSL(STR_TRUE);
        inputBuilder.enableTLS(STR_TRUE);
        serviceSpy.input = inputBuilder.build();

        assertEquals(storeMock, serviceSpy.createMessageStore());
        PowerMockito.verifyNew(Properties.class).withNoArguments();
        PowerMockito.verifyNew(SimpleAuthenticator.class).withArguments(anyString(), anyString());
        verify(serviceSpy).addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        verify(serviceSpy).configureStoreWithTLS(propertiesMock, authenticatorMock);
        verify(serviceSpy).configureStoreWithSSL(propertiesMock, authenticatorMock);
    }

    /**
     * Test configureStoreWithSSL method.
     *
     * @throws Exception
     */
    @Test
    public void testConfigureStoreWithSSL() throws Exception {
        doReturn(objectMock).when(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.SOCKET_FACTORY_CLASS, SSL_FACTORY);
        doReturn(objectMock).when(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.SOCKET_FACTORY_FALLBACK, STR_FALSE);
        doReturn(objectMock).when(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.PORT, POPProps.PORT);
        doReturn(objectMock).when(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.SOCKET_FACTORY_PORT, POPProps.PORT);
        PowerMockito.whenNew(URLName.class)
                .withArguments(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString())
                .thenReturn(urlNameMock);
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock)
                .when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(urlNameMock);

        addRequiredInputs();
        serviceSpy.input = inputBuilder.build();

        Store store = serviceSpy.configureStoreWithSSL(propertiesMock, authenticatorMock);
        assertEquals(storeMock, store);
        verify(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.SOCKET_FACTORY_CLASS, SSL_FACTORY);
        verify(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.SOCKET_FACTORY_FALLBACK, STR_FALSE);
        verify(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.PORT, POPProps.PORT);
        verify(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.SOCKET_FACTORY_PORT, POPProps.PORT);
        PowerMockito.verifyNew(URLName.class)
                .withArguments(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString());
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(urlNameMock);
    }

    @Test
    public void testConfigureStoreWithTLS() throws Exception {
        doReturn(objectMock).when(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.SSL_ENABLE, STR_FALSE);
        doReturn(objectMock).when(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.START_TLS_ENABLE, STR_TRUE);
        doReturn(objectMock).when(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.START_TLS_REQUIRED, STR_TRUE);

        PowerMockito.whenNew(URLName.class)
                .withArguments(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString())
                .thenReturn(urlNameMock);
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock)
                .when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(any(String.class));

        addRequiredInputs();
        serviceSpy.input = inputBuilder.build();

        Store store = serviceSpy.configureStoreWithTLS(propertiesMock, authenticatorMock);
        assertEquals(storeMock, store);
        verify(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.SSL_ENABLE, STR_FALSE);
        verify(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.START_TLS_ENABLE, STR_TRUE);
        verify(propertiesMock).setProperty(Props.MAIL + POPProps.POP3 + Props.START_TLS_REQUIRED, STR_TRUE);
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(POPProps.POP3 + SECURE_SUFFIX_FOR_POP3_AND_IMAP);
    }

    /**
     * Test configureStoreWithoutSSL method.
     *
     * @throws Exception
     */
    @Test
    public void testConfigureStoreWithoutSSL() throws Exception {
        doReturn(objectMock).when(propertiesMock).put(Props.MAIL + POPProps.POP3 + Props.HOST, HOST);
        doReturn(objectMock).when(propertiesMock).put(Props.MAIL + POPProps.POP3 + Props.PORT, Short.parseShort(POPProps.PORT));
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock)
                .when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(POPProps.POP3);

        addRequiredInputs();
        serviceSpy.input = inputBuilder.build();

        Store store = serviceSpy.configureStoreWithoutSSL(propertiesMock, authenticatorMock);
        assertEquals(storeMock, store);
        verify(propertiesMock).put(Props.MAIL + POPProps.POP3 + Props.HOST, HOST);
        verify(propertiesMock).put(Props.MAIL + POPProps.POP3 + Props.PORT, Short.parseShort(POPProps.PORT));
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(POPProps.POP3);
    }

    /**
     * Test method assSSLSettings with false trustAllRoots.
     *
     * @throws Exception
     */
    @Test
    public void testAddSSLSettingsWithFalseTrustAllRoots() throws Exception {
        mockGetSystemFileSeparatorAndGetSystemJavaHomeMethods();
        PowerMockito.whenNew(File.class)
                .withArguments(testJavaHome + testSeparator + "lib" + testSeparator + "security" + testSeparator + "cacerts")
                .thenReturn(fileMock);
        doReturn(true).when(fileMock).exists();

        PowerMockito.mockStatic(SSLContext.class);
        PowerMockito.doReturn(sslContextMock).when(SSLContext.class, "getInstance", anyString());
        commonStubbedMethodsForAddSSLSettings();

        serviceSpy.addSSLSettings(Boolean.parseBoolean(TRUST_ALL_ROOTS_FALSE), Strings.EMPTY, Strings.EMPTY, Strings.EMPTY, Strings.EMPTY);
        verifyGetSystemFileSeparatorAndGetSystemJavaHomeInvocation();
        PowerMockito.verifyNew(File.class, times(2))
                .withArguments(testJavaHome + testSeparator + "lib" + testSeparator + "security" + testSeparator + "cacerts");
        verify(fileMock, times(2)).exists();
        verifyCommonStubbedMethodsForAddSSLSettingsMethod();
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
        mockGetSystemFileSeparatorAndGetSystemJavaHomeMethods();
        PowerMockito.mockStatic(SSLContext.class);
        PowerMockito.doReturn(sslContextMock).when(SSLContext.class, "getInstance", anyString());
        PowerMockito.whenNew(EasyX509TrustManager.class).withNoArguments().thenReturn(easyX509TrustManagerMock);
        commonStubbedMethodsForAddSSLSettings();

        serviceSpy.addSSLSettings(Boolean.parseBoolean(TRUST_ALL_ROOTS_TRUE), KEYSTORE, KEYSTORE_PASSWORD,
                TRUST_KEYSTORE, TRUST_PASSWORD);
        verifyGetSystemFileSeparatorAndGetSystemJavaHomeInvocation();
        PowerMockito.verifyStatic();
        SSLContext.getInstance(anyString());
        PowerMockito.verifyNew(EasyX509TrustManager.class).withNoArguments();
        verifyCommonStubbedMethodsForAddSSLSettingsMethod();
    }

    /**
     * Test getMessageContent method with text/plain message.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessageContentWithTextPlain() throws Exception {
        commonStubbingForGetMessageContentMethod(TEXT_PLAIN);

        Map<String, String> messageByType = serviceSpy.getMessageByContentTypes(messageMock, CHARACTERSET);
        assertEquals(cmessageMock, messageByType.get(TEXT_PLAIN));
        commonVerifiesForGetMessageContentMethod(messageByType, TEXT_PLAIN);
    }

    /**
     * Test getMessageContent method with text/html message.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessageContentWithTextHtml() throws Exception {
        commonStubbingForGetMessageContentMethod(TEXT_HTML);
        doReturn(messageMockToString).when(serviceSpy).convertMessage(messageMockToString);

        Map<String, String> messageByType = serviceSpy.getMessageByContentTypes(messageMock, CHARACTERSET);
        assertEquals(cmessageMock, messageByType.get(TEXT_HTML));
        commonVerifiesForGetMessageContentMethod(messageByType, TEXT_HTML);
        verify(serviceSpy).convertMessage(messageMockToString);
    }

    /**
     * Test getMessageContent method with null disposition.
     */
    @Test
    public void testGetMessageContentWithNullDisposition() throws Exception {
        doReturn(false).when(messageMock).isMimeType(TEXT_PLAIN);
        doReturn(false).when(messageMock).isMimeType(TEXT_HTML);
        doReturn(multipartMock).when(messageMock).getContent();
        int one = 1;
        doReturn(one).when(multipartMock).getCount();
        doReturn(partMock).when(multipartMock).getBodyPart(anyInt());
        // return null disposition
        doReturn(null).when(partMock).getDisposition();
        doReturn("text/plain; charset=utf-8; format=flowed").when(partMock).getContentType();
        doReturn(inputStreamMock).when(partMock).getInputStream();
        byte[] bytes = {1};
        PowerMockito.mockStatic(ASCIIUtility.class);
        PowerMockito.doReturn(bytes).when(ASCIIUtility.class, "getBytes", inputStreamMock);
        PowerMockito.whenNew(ByteArrayInputStream.class).withArguments(bytes).thenReturn(byteArrayInputStreamMock);
        int byteArrayInputStreamMockCount = 1;
        doReturn(byteArrayInputStreamMockCount).when(byteArrayInputStreamMock).available();
        doReturn(byteArrayInputStreamMockCount)
                .when(byteArrayInputStreamMock)
                .read(new byte[byteArrayInputStreamMockCount], 0, byteArrayInputStreamMockCount);
        String testCMessage = "testCMessage";
        PowerMockito.mockStatic(MimeUtility.class);
        PowerMockito.doReturn(testCMessage).when(MimeUtility.class, "decodeText", anyString());
        serviceSpy.input = inputBuilder.build();

        Map<String, String> contentMessage = serviceSpy.getMessageByContentTypes(messageMock, CHARACTERSET);
        assertEquals(testCMessage, contentMessage.get("text/plain"));
        verify(messageMock).isMimeType(TEXT_PLAIN);
        verify(messageMock).isMimeType(TEXT_HTML);
        verify(messageMock).getContent();
        verify(multipartMock).getCount();
        verify(partMock).getDisposition();
        verify(partMock).getInputStream();
        PowerMockito.verifyStatic();
        ASCIIUtility.getBytes(inputStreamMock);
        PowerMockito.verifyNew(ByteArrayInputStream.class).withArguments(bytes);
        verify(byteArrayInputStreamMock).available();
        verify(byteArrayInputStreamMock).read(new byte[byteArrayInputStreamMockCount], 0, byteArrayInputStreamMockCount);
        PowerMockito.verifyStatic();
        MimeUtility.decodeText(anyString());
    }

    /**
     * Test getMessageContent method with null disposition.
     */
    @Test
    public void testGetMessageContentWithoutANullDisposition() throws Exception {
        doReturn(false).when(messageMock).isMimeType(TEXT_PLAIN);
        doReturn(false).when(messageMock).isMimeType(TEXT_HTML);
        doReturn(multipartMock).when(messageMock).getContent();
        int one = 1;
        doReturn(one).when(multipartMock).getCount();
        doReturn(partMock).when(multipartMock).getBodyPart(anyInt());
        String testDisposition = "testDisposition";
        doReturn(testDisposition).when(partMock).getDisposition();
        doReturn("text/plain; charset=utf-8; format=flowed").when(partMock).getContentType();
        serviceSpy.input = inputBuilder.build();

        Map<String, String> contentMessage = serviceSpy.getMessageByContentTypes(messageMock, CHARACTERSET);
        assertEquals(0, contentMessage.size());
        verify(messageMock).isMimeType(TEXT_PLAIN);
        verify(messageMock).isMimeType(TEXT_HTML);
        verify(messageMock).getContent();
        verify(multipartMock).getCount();
        verify(partMock).getDisposition();
    }

    /**
     * Test getAttachedFileNames method when content is not multipart.
     *
     * @throws Exception
     */
    @Test
    public void testGetAttachedFileNamesWhereContentIsNotMultiPart() throws Exception {
        doReturn(objectMock).when(partMock).getContent();
        String testFileName = "testFileName";
        doReturn(testFileName).when(partMock).getFileName();
        doReturn(inputStreamMock).when(partMock).getInputStream();
        serviceSpy.input = inputBuilder.build();

        String fileNames = serviceSpy.getAttachedFileNames(partMock);
        assertEquals(testFileName, fileNames);
    }

    @Test
    public void testConvertMessageSimple() throws Exception {
        final String expectedMessage = "message";
        final String message = serviceSpy.convertMessage(expectedMessage);
        assertEquals(expectedMessage, message);
    }

    @Test
    public void testConvertMessageWithBr() throws Exception {
        final String expectedMessage = "mes sage<br>";
        final String message = serviceSpy.convertMessage(expectedMessage);
        assertEquals(expectedMessage, message);
    }

    @Test
    public void testConvertMessageWithEoL() throws Exception {
        final String expectedMessage = "message\n";
        final String message = serviceSpy.convertMessage(expectedMessage);
        assertEquals("message<br>", message);
    }

    @Test
    public void testGetFolderOpenMode() throws Exception {
        final int folderOpenMode = serviceSpy.getFolderOpenMode();
        assertEquals(2, folderOpenMode);
    }

    @Test
    public void testDecodeAttachedFileNameSimple() throws Exception {
        final String expectedFileName = "fileName";
        final String message = serviceSpy.decodeAttachedFileNames(expectedFileName);
        assertEquals(expectedFileName, message);
    }

    @Test
    public void testDecodeAttachedFileNameWithSpaces() throws Exception {
        final String fileName = "file Name";
        final String message = serviceSpy.decodeAttachedFileNames(fileName);
        assertEquals("file Name", message);
    }

    @Test
    public void testDecodeAttachedFileNames() throws Exception {
        final String fileNames = "file Name, fileName1, fileName2 ";
        final String message = serviceSpy.decodeAttachedFileNames(fileNames);
        assertEquals("file Name, fileName1, fileName2 ", message);
    }

    @Test
    public void testDecodeAttachedFileNamesEncoded() throws Exception {
        final String fileNames = "?\u009D??\u009D???? ?????????,?\u009D??\u009D????";
        final String message = serviceSpy.decodeAttachedFileNames(fileNames);
        assertEquals("?\u009D??\u009D???? ?????????,?\u009D??\u009D????", message);
    }



    private void commonVerifiesForGetMessageContentMethod(Map<String, String> messageByType, String messageType)
            throws MessagingException, IOException {
        verify(messageMock).isMimeType(messageType);
        verify(messageMock).getContent();
        PowerMockito.verifyStatic();
        MimeUtility.decodeText(messageMockToString);
    }

    private void commonStubbingForGetMessageContentMethod(String messageType) throws Exception {
        doReturn(true).when(messageMock).isMimeType(messageType);
        doReturn(objectMock).when(messageMock).getContent();
        doReturn(messageMockToString).when(objectMock).toString();
        PowerMockito.mockStatic(MimeUtility.class);
        PowerMockito.doReturn(cmessageMock).when(MimeUtility.class, "decodeText", messageMockToString);
    }

    private void commonStubbedMethodsForAddSSLSettings() throws Exception {
        PowerMockito.mockStatic(SSLUtils.class);
        PowerMockito.whenNew(URL.class).withArguments(anyString()).thenReturn(urlMock);
        PowerMockito.doReturn(keyStoreMock).when(SSLUtils.class, "createKeyStore", anyObject(), anyString());
        //can't mock TrustManager[] and KeyManager[] objects.
        PowerMockito.doReturn(null).when(SSLUtils.class, "createAuthTrustManagers", anyObject());
        PowerMockito.doReturn(null).when(SSLUtils.class, "createKeyManagers", anyObject(), anyObject());
        PowerMockito.whenNew(SecureRandom.class).withNoArguments().thenReturn(secureRandomMock);
        doNothing().when(sslContextMock).init(null, null, secureRandomMock);
        PowerMockito.doNothing().when(SSLContext.class, "setDefault", sslContextMock);
    }

    private void verifyCommonStubbedMethodsForAddSSLSettingsMethod() throws Exception {
        PowerMockito.verifyStatic();
        SSLContext.getInstance(anyString());
        SSLContext.setDefault(sslContextMock);
        SSLUtils.createKeyStore(Matchers.<URL>any(), anyString());
        SSLUtils.createAuthTrustManagers(Matchers.<KeyStore>anyObject());
        SSLUtils.createKeyManagers(Matchers.<KeyStore>any(), anyString());
        SSLContext.setDefault(sslContextMock);
        PowerMockito.verifyNew(SecureRandom.class).withNoArguments();
    }

    private void verifyGetSystemFileSeparatorAndGetSystemJavaHomeInvocation() {
        verify(serviceSpy).getSystemFileSeparator();
        verify(serviceSpy).getSystemJavaHome();
    }

    private void mockGetSystemFileSeparatorAndGetSystemJavaHomeMethods() {
        doReturn(testSeparator).when(serviceSpy).getSystemFileSeparator();
        doReturn(testJavaHome).when(serviceSpy).getSystemJavaHome();
    }

    /**
     * Set the required inputs.
     */
    private void addRequiredInputs() {
        inputBuilder.hostname(HOST);
        inputBuilder.port(POPProps.PORT);
        inputBuilder.protocol(POPProps.POP3);
        inputBuilder.username(USERNAME);
        inputBuilder.password(PASSWORD);
        inputBuilder.folder(FOLDER);
        inputBuilder.messageNumber(MESSAGE_NUMBER);
    }
}

