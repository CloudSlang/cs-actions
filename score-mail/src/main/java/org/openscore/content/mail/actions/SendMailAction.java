package org.openscore.content.mail.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import org.openscore.content.mail.entities.SendMailInputs;
import org.openscore.content.mail.services.SendMail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giloan on 10/30/2014.
 */
public class SendMailAction {

    /**
     * The operation sends a smtp email.
     *
     * @param hostname The hostname or ip address of the smtp server.
     * @param port The port of the smtp service.
     * @param htmlEmail The value should be true if the email is in rich text/html format.
     *                  The value should be false if the email is in plain text format.
     *                  Valid values: true, false. Default value: true.
     * @param from From email address.
     * @param to A delimiter separated list of email address(es) or recipients where the email will be sent.
     * @param cc A delimiter separated list of email address(es) or recipients, to be placed in the CC.
     * @param bcc A delimiter separated list of email address(es) or recipients, to be placed in the BCC.
     * @param subject The email subject. If a subject spans on multiple lines, it is formatted to a single one.
     * @param body The body of the email.
     * @param readReceipt The value should be true if read receipt is required, else false. Valid values: true, false. Default value: false.
     * @param attachments A delimited separated list of files to attach (must be full path).
     * @param user If SMTP authentication is needed, the username to use.
     * @param password If SMTP authentication is needed, the password to use.
     * @param delimiter A delimiter to separate the email recipients and the attachments. Default value: ','.
     * @param characterSet The character set encoding for the entire email which includes subject, body,
     *                     attached file name and the attached file.
     *                     <br>Valid values: UTF-8, UTF-16, UTF-32, EUC-JP, ISO-2022-JP, Shift_JIS, Windows-31J. Default value: UTF-8.
     * @param contentTransferEncoding The content transfer encoding scheme (such as 7bit, 8bit, base64, quoted-printable etc)
     *                                for the entire email which includes subject, body, attached file name and the attached file. .
     *                                Valid values: quoted-printable, base64, 7bit, 8bit, binary, x-token. Default value: quoted-printable (or Q Encoding).
     * @param encryptionKeystore The path to the pks12 format keystore to use to encrypt the mail.
     * @param encryptionKeyAlias The alias of the key from the encryptionKeystore to use to encrypt the mail.
     * @param encryptionKeystorePassword The password for the encryptionKeystore.
     * @return a map containing the output of the operation. The keys present in the map are
     *      <br><b>returnResult</b> - that will contain the SentMailSuccessfully if the mail was sent successfully.
     *      <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     *      <br><b>exception</b> - the exception message if the operation goes to failure.
     * @throws Exception
     */
    @Action(name = "Send Mail",
            outputs = {
                    @Output(SendMail.RETURN_RESULT),
                    @Output(SendMail.RETURN_CODE),
                    @Output(SendMail.EXCEPTION)
            },
            responses = {
                    @Response(text = SendMail.SUCCESS, field = SendMail.RETURN_CODE, value = SendMail.SUCCESS_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = SendMail.FAILURE, field = SendMail.RETURN_CODE, value = SendMail.FAILURE_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
	public Map<String, String> execute(
            @Param(value = SendMailInputs.HOSTNAME, required = true) String hostname,
            @Param(value = SendMailInputs.PORT, required = true) String port,
            @Param(value = SendMailInputs.HTML_EMAIL) String htmlEmail,
            @Param(value = SendMailInputs.FROM, required = true) String from,
            @Param(value = SendMailInputs.TO, required = true) String to,
            @Param(SendMailInputs.CC) String cc,
            @Param(SendMailInputs.BCC) String bcc,
            @Param(value = SendMailInputs.SUBJECT, required = true) String subject,
            @Param(value = SendMailInputs.BODY, required = true) String body,
            @Param(SendMailInputs.READ_RECEIPT) String readReceipt,
            @Param(SendMailInputs.ATTACHMENTS) String attachments,
            @Param(SendMailInputs.USER) String user,
            @Param(SendMailInputs.PASSWORD) String password,
            @Param(SendMailInputs.DELIMITER) String delimiter,
            @Param(SendMailInputs.CHARACTERSET) String characterSet,
            @Param(SendMailInputs.CONTENT_TRANSFER_ENCODING) String contentTransferEncoding,
            @Param(SendMailInputs.ENCRYPTION_KEYSTORE) String encryptionKeystore,
            @Param(SendMailInputs.ENCRYPTION_KEY_ALIAS) String encryptionKeyAlias,
            @Param(SendMailInputs.ENCRYPTION_KEYSTORE_PASSWORD) String encryptionKeystorePassword
    ) throws Exception {

        SendMailInputs sendMailInputs = new SendMailInputs();
        sendMailInputs.setSmtpHostname(hostname);
        sendMailInputs.setPort(port);
        sendMailInputs.setHtmlEmail(htmlEmail);
        sendMailInputs.setFrom(from);
        sendMailInputs.setTo(to);
        sendMailInputs.setCc(cc);
        sendMailInputs.setBcc(bcc);
        sendMailInputs.setSubject(subject);
        sendMailInputs.setBody(body);
        sendMailInputs.setReadReceipt(readReceipt);
        sendMailInputs.setAttachments(attachments);
        sendMailInputs.setUser(user);
        sendMailInputs.setPassword(password);
        sendMailInputs.setDelimiter(delimiter);
        sendMailInputs.setCharacterset(characterSet);
        sendMailInputs.setContentTransferEncoding(contentTransferEncoding);
        sendMailInputs.setEncryptionKeystore(encryptionKeystore);
        sendMailInputs.setEncryptionKeyAlias(encryptionKeyAlias);
        sendMailInputs.setEncryptionKeystorePassword(encryptionKeystorePassword);

        try {
            return new SendMail().execute(sendMailInputs);
        } catch (Exception e) {
            return exceptionResult(e.getMessage(), e);
        }
	}

    private Map<String, String> exceptionResult(String message, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String eStr = writer.toString().replace("" + (char) 0x00, "");

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(SendMail.RETURN_RESULT, message);
        returnResult.put(SendMail.RETURN_CODE, SendMail.FAILURE_RETURN_CODE);
        returnResult.put(SendMail.EXCEPTION, eStr);
        return returnResult;
    }
}
