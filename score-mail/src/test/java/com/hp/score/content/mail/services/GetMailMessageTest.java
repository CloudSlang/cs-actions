package com.hp.score.content.mail.services;

import com.hp.score.content.mail.entities.GetMailMessageInputs;
import com.hp.score.content.mail.services.GetMailMessage;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.mail.Folder;
import javax.mail.Store;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

/**
 * Created by giloan on 11/6/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetMailMessage.class})
public class GetMailMessageTest {

    public static final int READ_ONLY = 1;
    private static final String FOLDER_DOES_NOT_EXIST = "The specified folder does not exist on the remote server.";
    // operation inputs
    private static final String
            HOST = "host",
            POP3_PORT = "110",
            IMAP_PORT = "143",
            POP3_PROTOCOL = "POP3",
            IMAP_PROTOCOL = "IMAP",
            USERNAME = "testUser",
            PASSWORD = "testPass",
            FOLDER = "INBOX",
            TRUST_ALL_ROOTS_TRUE = "true",
            TRUST_ALL_ROOTS_FALSE = "false",
            MESSAGE_NUMBER = "1",
            SUBJECT_ONLY_TRUE = "true",
            ENABLE_SSL_TRUE = "true",
            KEYSTORE = "HDD:\\keystore",
            KEYSTORE_PASSWORD = "keystorePass",
            TRUST_KEYSTORE = "HDD:\\trustore",
            TRUST_PASSWORD = "truststorePass",
            CHARACTERSET = "UTF-16",
            DEFAULT_CHARACTERSET = "UTF-8",
            DELETE_UPON_RETRIEVAL_TRUE = "true";
    // operation return codes
    private static final String
            RETURN_CODE = "returnCode",
            SUCCESS_RETURN_CODE = "0";
    //operation results
    private static final String
            RETURN_RESULT = "returnResult",
            SUBJECT_RESULT = "Subject",
            BODY_RESULT = "Body",
            ATTACHED_FILE_NAMES_RESULT = "AttachedFileNames";
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
     * Test execute method throws folder not found exception.
     *
     * @throws Exception
     */
    @Test
    public void testExecuteThrowsFolderNotFoundException() throws Exception {
        doReturn(storeMock).when(getMailMessageSpy).configureStore();
        doNothing().when(getMailMessageSpy).processInputs(inputs);
        doReturn(folderMock).when(storeMock).getFolder(anyString());
        doReturn(false).when(folderMock).exists();

        exception.expect(Exception.class);
        exception.expectMessage(FOLDER_DOES_NOT_EXIST);
        getMailMessageSpy.execute(inputs);
    }

    /**
     * Test execute method throws exception because message number input is greater than the number
     * of messages contained in the folder.
     *
     * @throws Exception
     */
    @Test
    public void testExecuteThrowsMessageNumberException() throws Exception {
        doReturn(storeMock).when(getMailMessageSpy).configureStore();
        doReturn(folderMock).when(storeMock).getFolder(anyString());
        doReturn(true).when(folderMock).exists();
        doReturn(READ_ONLY).when(getMailMessageSpy).getFolderOpenMode();
        doNothing().when(folderMock).open(READ_ONLY);
        doReturn(0).when(folderMock).getMessageCount();

        inputs.setHostname(HOST);
        inputs.setPort(POP3_PORT);
        inputs.setProtocol(POP3_PROTOCOL);
        inputs.setMessageNumber(MESSAGE_NUMBER);

        exception.expect(Exception.class);
        exception.expectMessage("message value was: " + MESSAGE_NUMBER + " there are only " + 0 + " messages in folder");
        Map<String, String> result = getMailMessageSpy.execute(inputs);
    }

    /**
     * Test execute method with default values for trustAllRoots(true), enableSSL(false)
     * subjectOnly(false), deleteUponRetrieval(false).
     *
     * @throws Exception
     */
    @Test
    @Ignore
    public void testExecute2() throws Exception {
        doReturn(storeMock).when(getMailMessageSpy).configureStore();
//        doNothing().when(getMailMessageSpy).processInputs(inputs);
        doReturn(folderMock).when(storeMock).getFolder(anyString());
        doReturn(true).when(folderMock).exists();
        doReturn(READ_ONLY).when(getMailMessageSpy).getFolderOpenMode();
        doNothing().when(folderMock).open(READ_ONLY);
        doReturn(0).when(folderMock).getMessageCount();

        inputs.setHostname(HOST);
        inputs.setPort(POP3_PORT);
        inputs.setProtocol(POP3_PROTOCOL);
//        inputs.setUsername(USERNAME);
//        inputs.setPassword(PASSWORD);
//        inputs.setFolder(FOLDER);
//        inputs.setTrustAllRoots("");
//        inputs.setEnableSSL("");
        inputs.setMessageNumber(MESSAGE_NUMBER);
//        inputs.setSubjectOnly("");
//        inputs.setDeleteUponRetrieval("");
//        inputs.setCharacterSet("");

        exception.expect(Exception.class);
        exception.expectMessage("message value was: " + MESSAGE_NUMBER + " there are only " + 0 + " messages in folder");
        Map<String, String> result = getMailMessageSpy.execute(inputs);
//        assertEquals(GetMailMessage.SUCCESS_RETURN_CODE, result.get(RETURN_CODE));
    }
}
