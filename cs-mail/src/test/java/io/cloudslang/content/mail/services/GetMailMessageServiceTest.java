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
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.mail.constants.ExceptionMsgs;
import io.cloudslang.content.mail.constants.MimeTypes;
import io.cloudslang.content.mail.constants.OutputNames;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.entities.GetMailInput;
import io.cloudslang.content.mail.entities.GetMailMessageInput;
import io.cloudslang.content.mail.entities.StringOutputStream;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by giloan on 11/6/2014.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({GetMailMessageService.class, MimeUtility.class, URLName.class, Session.class, System.class, SSLContext.class,
        SSLUtils.class, ASCIIUtility.class, ByteArrayInputStream.class, SSLUtils.class})
public class GetMailMessageServiceTest {

    public static final int READ_ONLY = 1;
    public static final String SUBJECT_TEST = "subjectTest";
    private static final String HOST = "host";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD = "testPass";
    private static final String FOLDER = "INBOX";
    private static final String MESSAGE_NUMBER = "1";
    private static final String CHARACTERSET = "UTF-16";
    private static final String BAD_CHARACTERSET = "badCharSet";
    private String messageMockToString = "stringMessageMock";
    private String cmessageMock = "testcmeesage";

    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Spy
    private GetMailMessageService serviceSpy = new GetMailMessageService();
    private GetMailMessageInput.Builder inputBuilder;
    @Mock
    private Store storeMock;
    @Mock
    private Folder folderMock;
    @Mock
    private Message messageMock;
    @Mock
    private StringOutputStream stringOutputStreamMock;
    @Mock
    private Object objectMock;
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
        PowerMockito.mockStatic(SSLUtils.class);
        when(SSLUtils.createMessageStore(any(GetMailInput.class))).thenReturn(storeMock);
        doReturn(folderMock).when(storeMock).getFolder(anyString());
        doReturn(false).when(folderMock).exists();
        serviceSpy.input = inputBuilder.build();

        exception.expect(Exception.class);
        exception.expectMessage(ExceptionMsgs.THE_SPECIFIED_FOLDER_DOES_NOT_EXIST_ON_THE_REMOTE_SERVER);
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
        PowerMockito.mockStatic(SSLUtils.class);
        when(SSLUtils.createMessageStore(any(GetMailInput.class))).thenReturn(storeMock);
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
        inputBuilder.subjectOnly(String.valueOf(true));
        inputBuilder.deleteUponRetrieval(String.valueOf(true));
        inputBuilder.characterSet(StringUtils.EMPTY);

        Map<String, String> result = serviceSpy.execute(inputBuilder.build());
        assertEquals(subjectTest, result.get(io.cloudslang.content.constants.OutputNames.RETURN_RESULT));
        assertEquals(subjectTest, result.get(OutputNames.SUBJECT));
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
        inputBuilder.subjectOnly(String.valueOf(true));
        inputBuilder.deleteUponRetrieval(String.valueOf(true));
        inputBuilder.characterSet(CHARACTERSET);

        Map<String, String> result = serviceSpy.execute(inputBuilder.build());
        assertEquals(SUBJECT_TEST, result.get(io.cloudslang.content.constants.OutputNames.RETURN_RESULT));
        assertEquals(SUBJECT_TEST, result.get(OutputNames.SUBJECT));
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
        inputBuilder.subjectOnly(StringUtils.EMPTY);
        inputBuilder.characterSet(CHARACTERSET);

        Map<String, String> result = serviceSpy.execute(inputBuilder.build());
        verify(serviceSpy).getMessage();
        verify(messageMock).getHeader("Subject");
        assertEquals(SUBJECT_TEST, result.get(OutputNames.SUBJECT));
        verify(serviceSpy).changeHeaderCharset("1", CHARACTERSET);
        verify(serviceSpy, times(2)).changeHeaderCharset(anyString(), anyString());
        verify(serviceSpy).decodeAttachedFileNames(attachedFileNames);
        assertEquals(attachedFileNames, result.get(OutputNames.ATTACHED_FILE_NAMES));

        assertEquals(messageContent, result.get(OutputNames.BODY));

        verify(serviceSpy).getMessageByContentTypes(messageMock, CHARACTERSET);
        PowerMockito.verifyNew(StringOutputStream.class).withNoArguments();
        verify(messageMock).writeTo(stringOutputStreamMock);
        assertEquals(stringOutputStreamMockToString, result.get(io.cloudslang.content.constants.OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.SUCCESS, result.get(io.cloudslang.content.constants.OutputNames.RETURN_CODE));
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
        inputBuilder.subjectOnly(StringUtils.EMPTY);

        Map<String, String> result = serviceSpy.execute(inputBuilder.build());
        verify(serviceSpy).getMessage();
        assertEquals(SUBJECT_TEST, result.get(OutputNames.SUBJECT));
        verify(serviceSpy).getAttachedFileNames(messageMock);
        verify(serviceSpy).decodeAttachedFileNames(attachedFileNames);
        assertEquals(attachedFileNames, result.get(OutputNames.ATTACHED_FILE_NAMES));
        assertEquals(messageContent, result.get(OutputNames.BODY));
        verify(serviceSpy).getMessageByContentTypes(messageMock, null);
        PowerMockito.verifyNew(StringOutputStream.class).withNoArguments();
        verify(messageMock).writeTo(stringOutputStreamMock);
        assertEquals(stringOutputStreamMockToString, result.get(io.cloudslang.content.constants.OutputNames.RETURN_RESULT));
        assertEquals(ReturnCodes.SUCCESS, result.get(io.cloudslang.content.constants.OutputNames.RETURN_CODE));
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
        PowerMockito.doThrow(new UnsupportedEncodingException(StringUtils.EMPTY)).when(MimeUtility.class, "decodeText", anyString());

        addRequiredInputs();
        inputBuilder.subjectOnly(StringUtils.EMPTY);
        inputBuilder.characterSet(BAD_CHARACTERSET);

        exception.expect(Exception.class);
        exception.expectMessage("The given encoding (" + BAD_CHARACTERSET + ") is invalid or not supported.");
        serviceSpy.execute(inputBuilder.build());
    }

    /**
     * Test getMessageContent method with text/plain message.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessageContentWithTextPlain() throws Exception {
        commonStubbingForGetMessageContentMethod(MimeTypes.TEXT_PLAIN);

        Map<String, String> messageByType = serviceSpy.getMessageByContentTypes(messageMock, CHARACTERSET);
        assertEquals(cmessageMock, messageByType.get(MimeTypes.TEXT_PLAIN));
        commonVerifiesForGetMessageContentMethod(messageByType, MimeTypes.TEXT_PLAIN);
    }

    /**
     * Test getMessageContent method with text/html message.
     *
     * @throws Exception
     */
    @Test
    public void testGetMessageContentWithTextHtml() throws Exception {
        commonStubbingForGetMessageContentMethod(MimeTypes.TEXT_HTML);
        doReturn(messageMockToString).when(serviceSpy).convertMessage(messageMockToString);

        Map<String, String> messageByType = serviceSpy.getMessageByContentTypes(messageMock, CHARACTERSET);
        assertEquals(cmessageMock, messageByType.get(MimeTypes.TEXT_HTML));
        commonVerifiesForGetMessageContentMethod(messageByType, MimeTypes.TEXT_HTML);
        verify(serviceSpy).convertMessage(messageMockToString);
    }

    /**
     * Test getMessageContent method with null disposition.
     */
    @Test
    public void testGetMessageContentWithNullDisposition() throws Exception {
        doReturn(false).when(messageMock).isMimeType(MimeTypes.TEXT_PLAIN);
        doReturn(false).when(messageMock).isMimeType(MimeTypes.TEXT_HTML);
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
        verify(messageMock).isMimeType(MimeTypes.TEXT_PLAIN);
        verify(messageMock).isMimeType(MimeTypes.TEXT_HTML);
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
        doReturn(false).when(messageMock).isMimeType(MimeTypes.TEXT_PLAIN);
        doReturn(false).when(messageMock).isMimeType(MimeTypes.TEXT_HTML);
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
        verify(messageMock).isMimeType(MimeTypes.TEXT_PLAIN);
        verify(messageMock).isMimeType(MimeTypes.TEXT_HTML);
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

    /**
     * Set the required inputs.
     */
    private void addRequiredInputs() {
        inputBuilder.hostname(HOST);
        inputBuilder.port(PopPropNames.POP3_PORT);
        inputBuilder.protocol(PopPropNames.POP3);
        inputBuilder.username(USERNAME);
        inputBuilder.password(PASSWORD);
        inputBuilder.folder(FOLDER);
        inputBuilder.messageNumber(MESSAGE_NUMBER);
    }
}

