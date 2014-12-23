package org.openscore.content.mail.services;

import org.openscore.content.mail.entities.SendMailInputs;
import com.sun.mail.smtp.SMTPMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openscore.content.mail.services.SendMail;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by giloan on 11/5/2014.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Transport.class, Session.class, SMTPMessage.class, SendMail.class})
public class SendMailTest {

    // operation inputs
    private static final String
            SMTP_HOSTANME = "127.0.0.1",
            PORT = "25",
            FROM = "from@test.com",
            TO = "to@test.com",
            TO2 = "to2@test.com",
            CC = "cc@test.com",
            BCC = "bcc@test.com",
            SUBJECT = "test subject",
            BODY = "test body",
            HTML_EMAIL_TRUE = "true",
            READ_RECEIPT_TRUE = "true",
            ATTACHMENTS = "HDD:\\FULL_PATH1;HDD:\\FULL_PATH2",
            USER = "user",
            PASSWORD = "pass",
            DELIMITER = ";";

    private static final int
            INT_PORT = 25;

    // operation return codes
    private static final String
            RETURN_CODE = "returnCode",
            SUCCESS_RETURN_CODE = "0";

    //operation results
    private static final String
            RETURN_RESULT = "returnResult",
            MAIL_WAS_SENT = "SentMailSuccessfully";

    private static final String
            SMTP_PROTOCOL = "smtp",
            SMTP_HOST_CONFIG = "mail.smtp.host",
            SMTP_PORT_CONFIG = "mail.smtp.port",
            SMTP_USER_CONFIG = "mail.smtp.user",
            SMTP_PASSWORD_CONFIG = "mail.smtp.password",
            SMTP_AUTH_CONFIG = "mail.smtp.auth",
            CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding",
            DEFAULT_CONTENT_TRANSFER_ENCODING = "quoted-printable",
            TEXT_HTML = "text/html",
            TEXT_PLAIN = "text/plain",
            CHARSET_CST = ";charset=",
            DEFAULT_CHARACTERSET = "UTF-8",
            DEFAULT_DELIMITER = ",",
            CANNOT_ATTACH = "Cannot attach";
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private SendMail sendMail;
    private SendMailInputs inputs;
    private Address[] addresses = new Address[1];
    @Mock
    private Session sessionMock;
    @Mock
    private Transport transportMock;
    @Mock
    private Properties propertiesMock;
    @Mock
    private SMTPMessage smtpMessageMock;
    @Mock
    private MimeMultipart mimeMultipartMock;
    @Mock
    private InternetAddress recipientMock;
    @Mock
    private MimeBodyPart mimeBodyPartMock;
    @Mock
    private FileDataSource fileDataSourceMock;
    @Mock
    private DataHandler dataHandlerMock;

    /**
     * Initialize tested object and set up the mocks.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        sendMail = new SendMail();
        inputs = new SendMailInputs();

        PowerMockito.whenNew(Properties.class).withNoArguments().thenReturn(propertiesMock);
        Mockito.doReturn(SMTP_HOSTANME).when(propertiesMock).put(SMTP_HOST_CONFIG, SMTP_HOSTANME);
        PowerMockito.mockStatic(Session.class);
        PowerMockito.doReturn(sessionMock).when(Session.class, "getInstance", anyObject(),
                anyObject());
        PowerMockito.whenNew(SMTPMessage.class).withArguments(sessionMock).thenReturn(smtpMessageMock);
        PowerMockito.whenNew(MimeMultipart.class).withNoArguments().thenReturn(mimeMultipartMock);
        PowerMockito.whenNew(MimeBodyPart.class).withNoArguments().thenReturn(mimeBodyPartMock);
        Mockito.doNothing().when(mimeBodyPartMock).setHeader(anyString(), anyString());
        Mockito.doNothing().when(mimeMultipartMock).addBodyPart(mimeBodyPartMock);
        Mockito.doNothing().when(smtpMessageMock).setContent(mimeMultipartMock);
        Mockito.doNothing().when(smtpMessageMock).setFrom(Matchers.<InternetAddress>any());
        Mockito.doNothing().when(smtpMessageMock).setSubject(anyString());
        PowerMockito.whenNew(InternetAddress.class).withArguments(anyString()).thenReturn(recipientMock);
        Mockito.doNothing().when(smtpMessageMock).setRecipients(Matchers.<Message.RecipientType>any(), Matchers.<InternetAddress[]>any());
    }

    /**
     * Tear down objects after each test.
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        sendMail = null;
        inputs = null;
    }

    /**
     * Test Execute method with successful scenario,
     * (user not null, html input takes the default value(false)).
     *
     * @throws Exception
     */
    @Test
    public void testExecuteGoesToSuccessScenario1() throws Exception {
        Mockito.doReturn(transportMock).when(sessionMock).getTransport(SMTP_PROTOCOL);
        Mockito.doNothing().when(transportMock).connect(SMTP_HOSTANME, INT_PORT, USER, PASSWORD);
        Mockito.doNothing().when(transportMock).sendMessage(Matchers.<SMTPMessage>any(), Matchers.<Address[]>any());
        Mockito.doReturn(addresses).when(smtpMessageMock).getAllRecipients();
        Mockito.doNothing().when(mimeBodyPartMock).setContent(BODY, TEXT_PLAIN + CHARSET_CST + DEFAULT_CHARACTERSET);

        inputs.setSmtpHostname(SMTP_HOSTANME);
        inputs.setPort(PORT);
        inputs.setFrom(FROM);
        inputs.setTo(TO);
        inputs.setCc(CC);
        inputs.setBcc(BCC);
        inputs.setSubject(SUBJECT);
        inputs.setBody(BODY);
        inputs.setUser(USER);
        inputs.setPassword(PASSWORD);

        Map<String, String> result = sendMail.execute(inputs);
        assertEquals(MAIL_WAS_SENT, result.get(RETURN_RESULT));
        assertEquals(SUCCESS_RETURN_CODE, result.get(RETURN_CODE));
        verifyCommonMethodInvocations();
        verify(propertiesMock).put(eq(SMTP_USER_CONFIG), eq(USER));
        verify(propertiesMock).put(eq(SMTP_PASSWORD_CONFIG), eq(PASSWORD));
        verify(propertiesMock).put(eq(SMTP_AUTH_CONFIG), eq("true"));
        verify(sessionMock).getTransport(SMTP_PROTOCOL);
        verify(transportMock).connect(SMTP_HOSTANME, INT_PORT, USER, PASSWORD);
        verify(transportMock).sendMessage(Matchers.<SMTPMessage>any(), Matchers.<Address[]>any());
        verify(mimeBodyPartMock).setContent(BODY, TEXT_PLAIN + CHARSET_CST + DEFAULT_CHARACTERSET);
        verify(transportMock).close();
    }

    /**
     * Test Execute method with successful scenario,
     * (user is null, html inputs is true).
     *
     * @throws Exception
     */
    @Test
    public void testExecuteGoesToSuccessScenario2() throws Exception {
        prepareTransportClassForStaticMock();
        Mockito.doNothing().when(mimeBodyPartMock).setContent(BODY, TEXT_HTML + CHARSET_CST + DEFAULT_CHARACTERSET);

        inputs.setSmtpHostname(SMTP_HOSTANME);
        inputs.setPort(PORT);
        inputs.setFrom(FROM);
        inputs.setTo(TO);
        inputs.setCc(CC);
        inputs.setBcc(BCC);
        inputs.setSubject(SUBJECT);
        inputs.setBody(BODY);
        inputs.setHtmlEmail(HTML_EMAIL_TRUE);

        Map<String, String> result = sendMail.execute(inputs);
        assertEquals(MAIL_WAS_SENT, result.get(RETURN_RESULT));
        assertEquals(SUCCESS_RETURN_CODE, result.get(RETURN_CODE));
        verifyCommonMethodInvocations();
        verify(propertiesMock, never()).put(eq(SMTP_USER_CONFIG), eq(USER));
        verify(propertiesMock, never()).put(eq(SMTP_PASSWORD_CONFIG), eq(PASSWORD));
        verify(propertiesMock, never()).put(eq(SMTP_AUTH_CONFIG), eq("true"));
        verify(sessionMock, never()).getTransport(SMTP_PROTOCOL);
        verify(transportMock, never()).connect(SMTP_HOSTANME, INT_PORT, USER, PASSWORD);
        verify(transportMock, never()).sendMessage(Matchers.<SMTPMessage>any(), Matchers.<Address[]>any());
        PowerMockito.verifyStatic(Mockito.times(1));
        Transport.send(smtpMessageMock);
        verify(mimeBodyPartMock).setContent(BODY, TEXT_HTML + CHARSET_CST + DEFAULT_CHARACTERSET);
    }

    private void prepareTransportClassForStaticMock() throws Exception {
        PowerMockito.mockStatic(Transport.class);
        PowerMockito.doNothing().when(Transport.class, "send", smtpMessageMock);
    }

    /**
     * Test Execute method with successful scenario,
     * (attachments not empty and readReceipt is true).
     *
     * @throws Exception
     */
    @Test
    public void testExecuteGoesToSuccessScenario3() throws Exception {
        prepareTransportClassForStaticMock();
        Mockito.doNothing().when(mimeBodyPartMock).setContent(BODY, TEXT_PLAIN + CHARSET_CST + DEFAULT_CHARACTERSET);
        PowerMockito.whenNew(FileDataSource.class).withArguments(anyString()).thenReturn(fileDataSourceMock);
        PowerMockito.whenNew(DataHandler.class).withArguments(fileDataSourceMock).thenReturn(dataHandlerMock);
        doNothing().when(mimeBodyPartMock).setDataHandler(dataHandlerMock);
        doNothing().when(mimeBodyPartMock).setFileName(anyString());
        doNothing().when(mimeMultipartMock).addBodyPart(mimeBodyPartMock);

        inputs.setSmtpHostname(SMTP_HOSTANME);
        inputs.setPort(PORT);
        inputs.setFrom(FROM);
        inputs.setTo(TO);
        inputs.setCc(CC);
        inputs.setBcc(BCC);
        inputs.setSubject(SUBJECT);
        inputs.setBody(BODY);
        inputs.setAttachments(ATTACHMENTS);
        inputs.setDelimiter(DELIMITER);
        inputs.setReadReceipt(READ_RECEIPT_TRUE);

        Map<String, String> result = sendMail.execute(inputs);
        assertEquals(MAIL_WAS_SENT, result.get(RETURN_RESULT));
        assertEquals(SUCCESS_RETURN_CODE, result.get(RETURN_CODE));
        // 3 invocations, one for html setting and one for each of the attachments
        PowerMockito.verifyNew(MimeBodyPart.class, times(3)).withNoArguments();
        verify(mimeBodyPartMock, times(3)).setHeader(CONTENT_TRANSFER_ENCODING, DEFAULT_CONTENT_TRANSFER_ENCODING);
        PowerMockito.verifyNew(FileDataSource.class, times(2)).withArguments(anyString());
        verify(mimeBodyPartMock, times(2)).setDataHandler(dataHandlerMock);
        verify(mimeBodyPartMock, times(2)).setFileName(anyString());
        verify(mimeMultipartMock, times(3)).addBodyPart(mimeBodyPartMock);
        verify(smtpMessageMock).setNotifyOptions(SMTPMessage.NOTIFY_DELAY + SMTPMessage.NOTIFY_FAILURE + SMTPMessage.NOTIFY_SUCCESS);
    }

    /**
     * Test Execute method with successful scenario,
     * (multiple destination recipients)
     *
     * @throws Exception
     */
    @Test
    public void testExecuteGoesToSuccessScenario4() throws Exception {
        prepareTransportClassForStaticMock();

        inputs.setSmtpHostname(SMTP_HOSTANME);
        inputs.setPort(PORT);
        inputs.setFrom(FROM);
        inputs.setTo(TO + DEFAULT_DELIMITER + TO2);
        inputs.setCc(CC);
        inputs.setBcc(BCC);
        inputs.setSubject(SUBJECT);
        inputs.setBody(BODY);

        Map<String, String> result = sendMail.execute(inputs);
        assertEquals(MAIL_WAS_SENT, result.get(RETURN_RESULT));
        assertEquals(SUCCESS_RETURN_CODE, result.get(RETURN_CODE));
        PowerMockito.verifyNew(InternetAddress.class).withArguments(TO);
        PowerMockito.verifyNew(InternetAddress.class).withArguments(TO2);
        verify(smtpMessageMock, times(3)).setRecipients(Matchers.<Message.RecipientType>any(), Matchers.<InternetAddress[]>any());
    }

    /**
     * Test Execute method with successful scenario,
     * (cc and bcc not null)
     *
     * @throws Exception
     */
    @Test
    public void testExecuteGoesToSuccessScenario5() throws Exception {
        prepareTransportClassForStaticMock();

        inputs.setSmtpHostname(SMTP_HOSTANME);
        inputs.setPort(PORT);
        inputs.setFrom(FROM);
        inputs.setTo(TO);
        inputs.setCc(CC);
        inputs.setBcc(BCC);
        inputs.setSubject(SUBJECT);
        inputs.setBody(BODY);

        Map<String, String> result = sendMail.execute(inputs);
        assertEquals(MAIL_WAS_SENT, result.get(RETURN_RESULT));
        assertEquals(SUCCESS_RETURN_CODE, result.get(RETURN_CODE));
        PowerMockito.verifyNew(InternetAddress.class).withArguments(CC);
        PowerMockito.verifyNew(InternetAddress.class).withArguments(BCC);
    }

    /**
     * Test Execute method with exception thrown when attachment files are not found
     *
     * @throws Exception
     */
    @Test
    public void testExecuteThrowsException() throws Exception {
        prepareTransportClassForStaticMock();

        Mockito.doNothing().when(mimeBodyPartMock).setContent(BODY, TEXT_PLAIN + CHARSET_CST + DEFAULT_CHARACTERSET);
        PowerMockito.whenNew(FileDataSource.class).withArguments(anyString()).thenReturn(fileDataSourceMock);
        PowerMockito.whenNew(DataHandler.class).withArguments(fileDataSourceMock).thenReturn(dataHandlerMock);
        doNothing().when(mimeBodyPartMock).setDataHandler(dataHandlerMock);

        doThrow(new MessagingException("IOException")).when(mimeBodyPartMock).setDataHandler(dataHandlerMock);

        inputs.setSmtpHostname(SMTP_HOSTANME);
        inputs.setPort(PORT);
        inputs.setFrom(FROM);
        inputs.setTo(TO + DEFAULT_DELIMITER + TO2);
        inputs.setCc(CC);
        inputs.setBcc(BCC);
        inputs.setSubject(SUBJECT);
        inputs.setBody(BODY);
        inputs.setAttachments(ATTACHMENTS);

        exception.expect(Exception.class);
        exception.expectMessage(CANNOT_ATTACH);
        sendMail.execute(inputs);
    }

    /**
     * Verify the stubbed method invocations.
     */
    private void verifyCommonMethodInvocations() throws Exception {
        verify(propertiesMock).put(eq(SMTP_HOST_CONFIG), eq(SMTP_HOSTANME));
        verify(propertiesMock).put(eq(SMTP_PORT_CONFIG), eq(PORT));
        PowerMockito.verifyStatic(Mockito.times(1));
        Session.getInstance(propertiesMock, null);
        PowerMockito.verifyNew(MimeBodyPart.class).withNoArguments();
        verify(mimeBodyPartMock).setHeader(CONTENT_TRANSFER_ENCODING, DEFAULT_CONTENT_TRANSFER_ENCODING);
        verify(mimeMultipartMock).addBodyPart(mimeBodyPartMock);
        verify(smtpMessageMock).setContent(mimeMultipartMock);
        verify(smtpMessageMock).setFrom(Matchers.<InternetAddress>any());
        verify(smtpMessageMock).setSubject(anyString());
        PowerMockito.verifyNew(Properties.class).withNoArguments();
        PowerMockito.verifyNew(SMTPMessage.class).withArguments(sessionMock);
        PowerMockito.verifyNew(MimeMultipart.class).withNoArguments();
        PowerMockito.verifyNew(InternetAddress.class, atLeastOnce()).withArguments(anyString());
    }
}
