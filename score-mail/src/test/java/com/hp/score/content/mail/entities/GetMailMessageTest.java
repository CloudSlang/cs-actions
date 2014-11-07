package com.hp.score.content.mail.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import javax.mail.Folder;
import javax.mail.Store;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;

/**
 * Created by giloan on 11/6/2014.
 */
public class GetMailMessageTest {

    private GetMailMessage getMailMessage;
    private GetMailMessageInputs inputs;

    @Mock
    private GetMailMessage getMailMessageMock;
    @Mock
    private Store storeMock;
    @Mock
    private Folder folderMock;

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
     * Test execute method with default values for trustAllRoots(true), enableSSL(false)
     * subjectOnly(false), deleteUponRetrieval(false).
     * @throws Exception
     */
    @Test
    @Ignore
    public void testExecute1() throws Exception {
        doReturn(storeMock).when(getMailMessageMock).configureStore();
        doReturn(folderMock).when(storeMock).getFolder(FOLDER);

        inputs.setHostname(HOST);
        inputs.setPort(POP3_PORT);
        inputs.setProtocol(POP3_PROTOCOL);
        inputs.setUsername(USERNAME);
        inputs.setPassword(PASSWORD);
        inputs.setFolder(FOLDER);
        inputs.setTrustAllRoots("");
        inputs.setEnableSSL("");
        inputs.setMessageNumber(MESSAGE_NUMBER);
        inputs.setSubjectOnly("");
        inputs.setDeleteUponRetrieval("");
        inputs.setCharacterSet("");

        Map<String, String> result = getMailMessage.execute(inputs);
        assertEquals(GetMailMessage.SUCCESS_RETURN_CODE, result.get(RETURN_CODE));
    }
}
