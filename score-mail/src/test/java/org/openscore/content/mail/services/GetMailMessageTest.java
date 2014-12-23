package org.openscore.content.mail.services;

import org.openscore.content.mail.entities.GetMailMessageInputs;
import org.openscore.content.mail.entities.SimpleAuthenticator;
import org.openscore.content.mail.entities.StringOutputStream;
import org.openscore.content.mail.services.GetMailMessage;
import org.openscore.content.mail.sslconfig.EasyX509TrustManager;
import org.openscore.content.mail.sslconfig.SSLUtils;
import com.sun.mail.util.ASCIIUtility;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.*;
import javax.mail.internet.MimeUtility;
//import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by giloan on 11/6/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetMailMessage.class, MimeUtility.class, URLName.class, Session.class, System.class, SSLContext.class, SSLUtils.class, ASCIIUtility.class, ByteArrayInputStream.class})
public class GetMailMessageTest {

    public static final int READ_ONLY = 1;
    public static final String SUBJECT_TEST = "subjectTest";
    private static final String FOLDER_DOES_NOT_EXIST = "The specified folder does not exist on the remote server.";
    // operation inputs
    private static final String HOST = "host";
    private static final String POP3_PORT = "110";
    private static final String IMAP_PORT = "143";
    private static final String POP3_PROTOCOL = "pop3";
    private static final String IMAP_PROTOCOL = "imap";
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
    private static final String SUBJECT_RESULT = "Subject";
    private static final String BODY_RESULT = "Body";
    private static final String ATTACHED_FILE_NAMES_RESULT = "AttachedFileNames";
    private static final String BAD_CHARACTERSET = "badCharSet";
    private static final String STR_FALSE = "false";
    private static final String STR_TRUE = "true";
    private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private String testSeparator = "\\";
    private String testJavaHome = "HDD:\\java";
    private static final String TEXT_PLAIN = "text/plain";
    private static final String TEXT_HTML = "text/html";
    private String messageMockToString = "stringMessageMock";
    private String cmessageMock = "testcmeesage";

    @Rule
    public ExpectedException exception = ExpectedException.none();
    private GetMailMessage getMailMessage;
    private GetMailMessageInputs inputs;
    @Spy
    private GetMailMessage getMailMessageSpy = new GetMailMessage();
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
    public void setUp() throws Exception {
        getMailMessage = new GetMailMessage();
        inputs = new GetMailMessageInputs();
    }

    @After
    public void tearDown() throws Exception {
        getMailMessage = null;
        inputs = null;
    }

    /**
     * Test getMessage method throws folder not found exception.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessageThrowsFolderNotFoundException() throws Exception {
        doReturn(storeMock).when(getMailMessageSpy).createMessageStore();
        doNothing().when(getMailMessageSpy).processInputs(inputs);
        doReturn(folderMock).when(storeMock).getFolder(anyString());
        doReturn(false).when(folderMock).exists();

        exception.expect(Exception.class);
        exception.expectMessage(FOLDER_DOES_NOT_EXIST);
        getMailMessageSpy.getMessage();
    }

    /**
     * Test getMessage method throws exception because message number input is greater than the number
     * of messages contained in the folder.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessageThrowsMessageNumberException() throws Exception {
        doReturn(storeMock).when(getMailMessageSpy).createMessageStore();
        doReturn(folderMock).when(storeMock).getFolder(anyString());
        doReturn(true).when(folderMock).exists();
        doReturn(READ_ONLY).when(getMailMessageSpy).getFolderOpenMode();
        doNothing().when(folderMock).open(READ_ONLY);
        doReturn(0).when(folderMock).getMessageCount();

        addRequiredInputs();
        getMailMessageSpy.processInputs(inputs);
        exception.expect(Exception.class);
        exception.expectMessage("message value was: " + MESSAGE_NUMBER + " there are only " + 0 + " messages in folder");
        getMailMessageSpy.getMessage();
    }

    /**
     * Test execute method with default values for trustAllRoots(true), enableSSL(false)
     * subjectOnly(false), deleteUponRetrieval(false).
     *
     * @throws Exception
     */
    @Test
    public void testExecuteWithTrueSubjectOnlyAndDeleteUponRetrievalAndEmptyCharacterSet() throws Exception {
        doReturn(messageMock).when(getMailMessageSpy).getMessage();
        doNothing().when(messageMock).setFlag(Flags.Flag.DELETED, true);
        String subjectTest = SUBJECT_TEST;
        doReturn(subjectTest).when(messageMock).getSubject();

        addRequiredInputs();
        inputs.setSubjectOnly(STR_TRUE);
        inputs.setDeleteUponRetrieval(STR_TRUE);
        inputs.setCharacterSet("");

        Map<String, String> result = getMailMessageSpy.execute(inputs);
        assertEquals(subjectTest, result.get(RETURN_RESULT));
        verify(getMailMessageSpy).getMessage();
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
        doReturn(messageMock).when(getMailMessageSpy).getMessage();
        doNothing().when(messageMock).setFlag(Flags.Flag.DELETED, true);
        doReturn(new String[]{"1"}).when(messageMock).getHeader(anyString());
        doReturn(SUBJECT_TEST).when(getMailMessageSpy).changeHeaderCharset("1", CHARACTERSET);

        addRequiredInputs();
        inputs.setSubjectOnly(STR_TRUE);
        inputs.setDeleteUponRetrieval(STR_TRUE);
        inputs.setCharacterSet(CHARACTERSET);

        Map<String, String> result = getMailMessageSpy.execute(inputs);
        assertEquals(SUBJECT_TEST, result.get(RETURN_RESULT));
        verify(getMailMessageSpy).getMessage();
        verify(messageMock).getHeader("Subject");
        verify(getMailMessageSpy).changeHeaderCharset(anyString(), anyString());
    }

    /**
     * Test execute method with default value for subjectOnly input (false).
     *
     * @throws Exception
     */
    @Test
    public void testExecuteWithFalseSubjectOnly() throws Exception {
        doReturn(messageMock).when(getMailMessageSpy).getMessage();
        doNothing().when(messageMock).setFlag(Flags.Flag.DELETED, true);
        doReturn(new String[]{"1"}).when(messageMock).getHeader(anyString());
        doReturn(SUBJECT_TEST).when(getMailMessageSpy).changeHeaderCharset("1", CHARACTERSET);
        String attachedFileNames = "testAttachedFileNames";
        doReturn(attachedFileNames).when(getMailMessageSpy).getAttachedFileNames(messageMock);
        doReturn(attachedFileNames).when(getMailMessageSpy).changeHeaderCharset(attachedFileNames, CHARACTERSET);
        doReturn(attachedFileNames).when(getMailMessageSpy).decodeAttachedFileNames(attachedFileNames);
        // Get the message body test
        String messageContent = "testMessageContent";
        doReturn(messageContent).when(getMailMessageSpy).getMessageContent(messageMock, CHARACTERSET);
        PowerMockito.whenNew(StringOutputStream.class).withNoArguments().thenReturn(stringOutputStreamMock);
        doNothing().when(messageMock).writeTo(stringOutputStreamMock);
        String stringOutputStreamMockToString = "testStream";
        doReturn(stringOutputStreamMockToString).when(stringOutputStreamMock).toString();

        addRequiredInputs();
        inputs.setSubjectOnly("");
        inputs.setCharacterSet(CHARACTERSET);

        Map<String, String> result = getMailMessageSpy.execute(inputs);
        verify(getMailMessageSpy).getMessage();
        verify(messageMock).getHeader("Subject");
        assertEquals(SUBJECT_TEST, result.get(SUBJECT_RESULT));
        verify(getMailMessageSpy).changeHeaderCharset("1", CHARACTERSET);
        verify(getMailMessageSpy, times(2)).changeHeaderCharset(anyString(), anyString());
        verify(getMailMessageSpy).decodeAttachedFileNames(attachedFileNames);
        assertEquals(attachedFileNames, result.get(ATTACHED_FILE_NAMES_RESULT));
        assertEquals(messageContent, result.get(BODY_RESULT));
        verify(getMailMessageSpy).getMessageContent(messageMock, CHARACTERSET);
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
        doReturn(messageMock).when(getMailMessageSpy).getMessage();
        doNothing().when(messageMock).setFlag(Flags.Flag.DELETED, true);
        doReturn(SUBJECT_TEST).when(messageMock).getSubject();
        String attachedFileNames = "testAttachedFileNames";
        doReturn(attachedFileNames).when(getMailMessageSpy).getAttachedFileNames(messageMock);
        doReturn(attachedFileNames).when(getMailMessageSpy).decodeAttachedFileNames(attachedFileNames);
        // Get the message body test
        String messageContent = "testMessageContent";
        doReturn(messageContent).when(getMailMessageSpy).getMessageContent(messageMock, null);
        PowerMockito.whenNew(StringOutputStream.class).withNoArguments().thenReturn(stringOutputStreamMock);
        doNothing().when(messageMock).writeTo(stringOutputStreamMock);
        String stringOutputStreamMockToString = "testStream";
        String fiddledStringOutputStreamMockToString = (char) 0 + stringOutputStreamMockToString + (char) 0;
        doReturn(fiddledStringOutputStreamMockToString).when(stringOutputStreamMock).toString();

        addRequiredInputs();
        inputs.setSubjectOnly("");

        Map<String, String> result = getMailMessageSpy.execute(inputs);
        verify(getMailMessageSpy).getMessage();
        assertEquals(SUBJECT_TEST, result.get(SUBJECT_RESULT));
        verify(getMailMessageSpy).getAttachedFileNames(messageMock);
        verify(getMailMessageSpy).decodeAttachedFileNames(attachedFileNames);
        assertEquals(attachedFileNames, result.get(ATTACHED_FILE_NAMES_RESULT));
        assertEquals(messageContent, result.get(BODY_RESULT));
        verify(getMailMessageSpy).getMessageContent(messageMock, null);
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
        doReturn(messageMock).when(getMailMessageSpy).getMessage();
        doNothing().when(messageMock).setFlag(Flags.Flag.DELETED, true);
        doReturn(new String[]{"1"}).when(messageMock).getHeader(anyString());
        doReturn(SUBJECT_TEST).when(getMailMessageSpy).changeHeaderCharset("1", CHARACTERSET);
        PowerMockito.mockStatic(MimeUtility.class);
        PowerMockito.doThrow(new UnsupportedEncodingException("")).when(MimeUtility.class, "decodeText", anyString());

        addRequiredInputs();
        inputs.setSubjectOnly("");
        inputs.setCharacterSet(BAD_CHARACTERSET);

        exception.expect(Exception.class);
        exception.expectMessage("The given encoding (" + BAD_CHARACTERSET + ") is invalid or not supported.");
        getMailMessageSpy.execute(inputs);
    }

    /**
     * Test createMessageStore method with enableSSL input false.
     */
    @Test
    public void testCreateMessageStoreWithEnableSSLInputFalse() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        doReturn(storeMock).when(getMailMessageSpy).configureStoreWithoutSSL(propertiesMock, authenticatorMock);
        doNothing().when(storeMock).connect();

        addRequiredInputs();
        inputs.setEnableSSL(STR_FALSE);
        getMailMessageSpy.processInputs(inputs);
        assertEquals(storeMock, getMailMessageSpy.createMessageStore());
        PowerMockito.verifyNew(Properties.class).withNoArguments();
        PowerMockito.verifyNew(SimpleAuthenticator.class).withArguments(anyString(), anyString());
        verify(getMailMessageSpy).configureStoreWithoutSSL(propertiesMock, authenticatorMock);
        verify(storeMock).connect();
    }

    /**
     * Test createMessageStore method with enableSSL input true.
     */
    @Test
    public void testCreateMessageStoreWithEnableSSLInputTrue() throws Exception {
        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        PowerMockito.whenNew(SimpleAuthenticator.class).withArguments(anyString(), anyString()).thenReturn(authenticatorMock);
        doNothing().when(getMailMessageSpy).addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        doReturn(storeMock).when(getMailMessageSpy).configureStoreWithSSL(propertiesMock, authenticatorMock);
        doNothing().when(storeMock).connect();

        addRequiredInputs();
        inputs.setEnableSSL(STR_TRUE);
        getMailMessageSpy.processInputs(inputs);
        assertEquals(storeMock, getMailMessageSpy.createMessageStore());
        PowerMockito.verifyNew(Properties.class).withNoArguments();
        PowerMockito.verifyNew(SimpleAuthenticator.class).withArguments(anyString(), anyString());
        verify(getMailMessageSpy).addSSLSettings(anyBoolean(), anyString(), anyString(), anyString(), anyString());
        verify(getMailMessageSpy).configureStoreWithSSL(propertiesMock, authenticatorMock);
    }

    /**
     * Test configureStoreWithSSL method.
     *
     * @throws Exception
     */
    @Test
    public void testConfigureStoreWithSSL() throws Exception {
        doReturn(objectMock).when(propertiesMock).setProperty("mail." + POP3_PROTOCOL + ".socketFactory.class", SSL_FACTORY);
        doReturn(objectMock).when(propertiesMock).setProperty("mail." + POP3_PROTOCOL + ".socketFactory.fallback", STR_FALSE);
        doReturn(objectMock).when(propertiesMock).setProperty("mail." + POP3_PROTOCOL + ".port", POP3_PORT);
        doReturn(objectMock).when(propertiesMock).setProperty("mail." + POP3_PROTOCOL + ".socketFactory.port", POP3_PORT);
        PowerMockito.whenNew(URLName.class).withArguments(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString()).thenReturn(urlNameMock);
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock).when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(urlNameMock);

        addRequiredInputs();
        getMailMessageSpy.processInputs(inputs);

        Store store = getMailMessageSpy.configureStoreWithSSL(propertiesMock, authenticatorMock);
        assertEquals(storeMock, store);
        verify(propertiesMock).setProperty("mail." + POP3_PROTOCOL + ".socketFactory.class", SSL_FACTORY);
        verify(propertiesMock).setProperty("mail." + POP3_PROTOCOL + ".socketFactory.fallback", STR_FALSE);
        verify(propertiesMock).setProperty("mail." + POP3_PROTOCOL + ".port", POP3_PORT);
        verify(propertiesMock).setProperty("mail." + POP3_PROTOCOL + ".socketFactory.port", POP3_PORT);
        PowerMockito.verifyNew(URLName.class).withArguments(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString());
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(urlNameMock);
    }

    /**
     * Test configureStoreWithoutSSL method.
     * @throws Exception
     */
    @Test
    public void testConfigureStoreWithoutSSL() throws Exception {
        doReturn(objectMock).when(propertiesMock).put("mail." + POP3_PROTOCOL + ".host", HOST);
        doReturn(objectMock).when(propertiesMock).put("mail." + POP3_PROTOCOL + ".port", POP3_PORT);
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock).when(Session.class, "getInstance", Matchers.<Properties>any(), Matchers.<Authenticator>any());
        doReturn(storeMock).when(sessionMock).getStore(POP3_PROTOCOL);

        addRequiredInputs();
        getMailMessageSpy.processInputs(inputs);

        Store store = getMailMessageSpy.configureStoreWithoutSSL(propertiesMock, authenticatorMock);
        assertEquals(storeMock, store);
        verify(propertiesMock).put("mail." + POP3_PROTOCOL + ".host", HOST);
        verify(propertiesMock).put("mail." + POP3_PROTOCOL + ".port", POP3_PORT);
        PowerMockito.verifyStatic();
        Session.getInstance(Matchers.<Properties>any(), Matchers.<Authenticator>any());
        verify(sessionMock).getStore(POP3_PROTOCOL);
    }

    /**
     * Test method assSSLSettings with false trustAllRoots.
     * @throws Exception
     */
    @Test
    public void testAddSSLSettingsWithFalseTrustAllRoots() throws Exception {
        mockGetSystemFileSeparatorAndGetSystemJavaHomeMethods();
        PowerMockito.whenNew(File.class).withArguments(testJavaHome + testSeparator + "lib" + testSeparator + "security" + testSeparator + "cacerts").thenReturn(fileMock);
        doReturn(true).when(fileMock).exists();

        PowerMockito.mockStatic(SSLContext.class);
        PowerMockito.doReturn(sslContextMock).when(SSLContext.class, "getInstance", anyString());
        commonStubbedMethodsForAddSSLSettings();

        getMailMessageSpy.addSSLSettings(Boolean.parseBoolean(TRUST_ALL_ROOTS_FALSE), "", "", "", "");
        verifyGetSystemFileSeparatorAndGetSystemJavaHomeInvocation();
        PowerMockito.verifyNew(File.class, times(2)).withArguments(testJavaHome + testSeparator + "lib" + testSeparator + "security" + testSeparator + "cacerts");
        verify(fileMock, times(2)).exists();
        verifyCommonStubbedMethodsForAddSSLSettingsMethod();
        PowerMockito.verifyNew(URL.class, times(2)).withArguments(anyString());
    }

    /**
     * Test method assSSLSettings with default keystore file, keystore password,
     * trustKeyStore and trustPassword.
     * @throws Exception
     */
    @Test
    public void testAddSSLSettings() throws Exception {
        mockGetSystemFileSeparatorAndGetSystemJavaHomeMethods();
        PowerMockito.mockStatic(SSLContext.class);
        PowerMockito.doReturn(sslContextMock).when(SSLContext.class, "getInstance", anyString());
        PowerMockito.whenNew(EasyX509TrustManager.class).withArguments(null).thenReturn(easyX509TrustManagerMock);
        commonStubbedMethodsForAddSSLSettings();

        getMailMessageSpy.addSSLSettings(Boolean.parseBoolean(TRUST_ALL_ROOTS_TRUE), KEYSTORE, KEYSTORE_PASSWORD, TRUST_KEYSTORE, TRUST_PASSWORD);
        verifyGetSystemFileSeparatorAndGetSystemJavaHomeInvocation();
        PowerMockito.verifyStatic();
        SSLContext.getInstance(anyString());
        PowerMockito.verifyNew(EasyX509TrustManager.class).withArguments(null);
        verifyCommonStubbedMethodsForAddSSLSettingsMethod();
    }

    /**
     * Test getMessageContent method with text/plain message.
     * @throws Exception
     */
    @Test
    public void testGetMessageContentWithTextPlain() throws Exception {
        commonStubbingForGetMessageContentMethod(TEXT_PLAIN);

        String message = getMailMessageSpy.getMessageContent(messageMock, CHARACTERSET);
        commonVerifiesForGetMessageContentMethod(message, TEXT_PLAIN);
    }

    /**
     * Test getMessageContent method with text/html message.
     * @throws Exception
     */
    @Test
    public void testGetMessageContentWithTextHtml() throws Exception {
        commonStubbingForGetMessageContentMethod(TEXT_HTML);
        doReturn(messageMockToString).when(getMailMessageSpy).convertMessage(messageMockToString);

        String message = getMailMessageSpy.getMessageContent(messageMock, CHARACTERSET);
        commonVerifiesForGetMessageContentMethod(message, TEXT_HTML);
        verify(getMailMessageSpy).convertMessage(messageMockToString);
    }

    /**
     * Test getMessageContent method with null disposition.
     */
    @Test
    public void testGetMessageContentWithNullDisposition() throws Exception {
        doReturn(false).when(messageMock).isMimeType(TEXT_PLAIN);
        doReturn(false).when(messageMock).isMimeType(TEXT_HTML);
        doReturn(multipartMock).when(messageMock).getContent();
        int n = 1;
        doReturn(n).when(multipartMock).getCount();
        doReturn(partMock).when(multipartMock).getBodyPart(anyInt());
        // return null disposition
        doReturn(null).when(partMock).getDisposition();
        doReturn(inputStreamMock).when(partMock).getInputStream();
        byte[] bytes = {1};
        PowerMockito.mockStatic(ASCIIUtility.class);
        PowerMockito.doReturn(bytes).when(ASCIIUtility.class, "getBytes", inputStreamMock);
        PowerMockito.whenNew(ByteArrayInputStream.class).withArguments(bytes).thenReturn(byteArrayInputStreamMock);
        int byteArrayInputStreamMockCount = 1;
        doReturn(byteArrayInputStreamMockCount).when(byteArrayInputStreamMock).available();
        doReturn(byteArrayInputStreamMockCount).when(byteArrayInputStreamMock).read(new byte[byteArrayInputStreamMockCount], 0, byteArrayInputStreamMockCount);
        String testCMessage = "testCMessage";
        PowerMockito.mockStatic(MimeUtility.class);
        PowerMockito.doReturn(testCMessage).when(MimeUtility.class, "decodeText", anyString());

        String cMessage = getMailMessageSpy.getMessageContent(messageMock, CHARACTERSET);
        assertEquals(testCMessage, cMessage);
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
        int n = 1;
        doReturn(n).when(multipartMock).getCount();
        doReturn(partMock).when(multipartMock).getBodyPart(anyInt());
        String testDisposition = "testDisposition";
        doReturn(testDisposition).when(partMock).getDisposition();

        String cMessage = getMailMessageSpy.getMessageContent(messageMock, CHARACTERSET);
        assertEquals("", cMessage);
        verify(messageMock).isMimeType(TEXT_PLAIN);
        verify(messageMock).isMimeType(TEXT_HTML);
        verify(messageMock).getContent();
        verify(multipartMock).getCount();
        verify(partMock).getDisposition();
    }

    /**
     * Test getAttachedFileNames method when content is not multipart.
     * @throws Exception
     */
    @Test
    public void testGetAttachedFileNamesWhereContentIsNotMultiPart() throws Exception {
        doReturn(objectMock).when(partMock).getContent();
        String testFileName = "testFileName";
        doReturn(testFileName).when(partMock).getFileName();
        doReturn(inputStreamMock).when(partMock).getInputStream();

        String fileNames= getMailMessageSpy.getAttachedFileNames(partMock);
        assertEquals(testFileName, fileNames);
    }

    private void commonVerifiesForGetMessageContentMethod(String message, String messageType) throws MessagingException, IOException {
        assertEquals(cmessageMock, message);
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
        verify(getMailMessageSpy).getSystemFileSeparator();
        verify(getMailMessageSpy).getSystemJavaHome();
    }

    private void mockGetSystemFileSeparatorAndGetSystemJavaHomeMethods() {
        doReturn(testSeparator).when(getMailMessageSpy).getSystemFileSeparator();
        doReturn(testJavaHome).when(getMailMessageSpy).getSystemJavaHome();
    }

    /**
     * Set the required inputs.
     */
    private void addRequiredInputs() {
        inputs.setHostname(HOST);
        inputs.setPort(POP3_PORT);
        inputs.setProtocol(POP3_PROTOCOL);
        inputs.setUsername(USERNAME);
        inputs.setPassword(PASSWORD);
        inputs.setFolder(FOLDER);
        inputs.setMessageNumber(MESSAGE_NUMBER);
    }
}

