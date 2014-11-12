package com.hp.score.content.mail.services;

import com.hp.score.content.mail.entities.GetMailMessageInputs;
import com.hp.score.content.mail.entities.StringOutputStream;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by giloan on 11/6/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetMailMessage.class, MimeUtility.class})
public class GetMailMessageTest {

    public static final int READ_ONLY = 1;
    public static final String SUBJECT_TEST = "subjectTest";
    private static final String FOLDER_DOES_NOT_EXIST = "The specified folder does not exist on the remote server.";
    // operation inputs
    private static final String HOST = "host";
    private static final String POP3_PORT = "110";
    private static final String IMAP_PORT = "143";
    private static final String POP3_PROTOCOL = "POP3";
    private static final String IMAP_PROTOCOL = "IMAP";
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

    @Before
    public void setUp() throws Exception {
//        getMailMessage = new GetMailMessage();
        inputs = new GetMailMessageInputs();
    }

    @After
    public void tearDown() throws Exception {
//        getMailMessage = null;
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

        inputs.setHostname(HOST);
        inputs.setPort(POP3_PORT);
        inputs.setProtocol(POP3_PROTOCOL);
        inputs.setMessageNumber(MESSAGE_NUMBER);

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

        inputs.setHostname(HOST);
        inputs.setPort(POP3_PORT);
        inputs.setProtocol(POP3_PROTOCOL);
        inputs.setMessageNumber(MESSAGE_NUMBER);
        inputs.setSubjectOnly("true");
        inputs.setDeleteUponRetrieval("true");
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

        inputs.setHostname(HOST);
        inputs.setPort(POP3_PORT);
        inputs.setProtocol(POP3_PROTOCOL);
        inputs.setMessageNumber(MESSAGE_NUMBER);
        inputs.setSubjectOnly("true");
        inputs.setDeleteUponRetrieval("true");
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

        inputs.setHostname(HOST);
        inputs.setPort(POP3_PORT);
        inputs.setProtocol(POP3_PROTOCOL);
        inputs.setMessageNumber(MESSAGE_NUMBER);
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

        inputs.setHostname(HOST);
        inputs.setPort(POP3_PORT);
        inputs.setProtocol(POP3_PROTOCOL);
        inputs.setMessageNumber(MESSAGE_NUMBER);
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

        inputs.setHostname(HOST);
        inputs.setPort(POP3_PORT);
        inputs.setProtocol(POP3_PROTOCOL);
        inputs.setMessageNumber(MESSAGE_NUMBER);
        inputs.setSubjectOnly("");
        inputs.setCharacterSet(BAD_CHARACTERSET);

        exception.expect(Exception.class);
        exception.expectMessage("The given encoding (" + BAD_CHARACTERSET + ") is invalid or not supported.");
        getMailMessageSpy.execute(inputs);
    }

    /**
     * Test createMessageStore method.
     */
    @Test
    @Ignore
    public void testCreateMessageStore() {

    }
}

