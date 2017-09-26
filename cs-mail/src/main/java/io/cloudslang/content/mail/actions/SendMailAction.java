/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.mail.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.mail.entities.SendMailInputs;
import io.cloudslang.content.mail.services.SendMail;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.mail.entities.SendMailInputs.ATTACHMENTS;
import static io.cloudslang.content.mail.entities.SendMailInputs.BCC;
import static io.cloudslang.content.mail.entities.SendMailInputs.BODY;
import static io.cloudslang.content.mail.entities.SendMailInputs.CC;
import static io.cloudslang.content.mail.entities.SendMailInputs.CHARACTERSET;
import static io.cloudslang.content.mail.entities.SendMailInputs.CONTENT_TRANSFER_ENCODING;
import static io.cloudslang.content.mail.entities.SendMailInputs.DELIMITER;
import static io.cloudslang.content.mail.entities.SendMailInputs.ENABLE_TLS;
import static io.cloudslang.content.mail.entities.SendMailInputs.ENCRYPTION_ALGORITHM;
import static io.cloudslang.content.mail.entities.SendMailInputs.ENCRYPTION_KEYSTORE;
import static io.cloudslang.content.mail.entities.SendMailInputs.ENCRYPTION_KEYSTORE_PASSWORD;
import static io.cloudslang.content.mail.entities.SendMailInputs.ENCRYPTION_KEY_ALIAS;
import static io.cloudslang.content.mail.entities.SendMailInputs.FROM;
import static io.cloudslang.content.mail.entities.SendMailInputs.HEADERS;
import static io.cloudslang.content.mail.entities.SendMailInputs.HEADERS_COLUMN_DELIMITER;
import static io.cloudslang.content.mail.entities.SendMailInputs.HEADERS_ROW_DELIMITER;
import static io.cloudslang.content.mail.entities.SendMailInputs.HOSTNAME;
import static io.cloudslang.content.mail.entities.SendMailInputs.HTML_EMAIL;
import static io.cloudslang.content.mail.entities.SendMailInputs.PASSWORD;
import static io.cloudslang.content.mail.entities.SendMailInputs.PORT;
import static io.cloudslang.content.mail.entities.SendMailInputs.READ_RECEIPT;
import static io.cloudslang.content.mail.entities.SendMailInputs.SUBJECT;
import static io.cloudslang.content.mail.entities.SendMailInputs.TIMEOUT;
import static io.cloudslang.content.mail.entities.SendMailInputs.TO;
import static io.cloudslang.content.mail.entities.SendMailInputs.USER;
import static io.cloudslang.content.mail.services.SendMail.EXCEPTION;
import static io.cloudslang.content.mail.services.SendMail.FAILURE;
import static io.cloudslang.content.mail.services.SendMail.FAILURE_RETURN_CODE;
import static io.cloudslang.content.mail.services.SendMail.RETURN_CODE;
import static io.cloudslang.content.mail.services.SendMail.RETURN_RESULT;
import static io.cloudslang.content.mail.services.SendMail.SUCCESS;
import static io.cloudslang.content.mail.services.SendMail.SUCCESS_RETURN_CODE;

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
     * @param readReceipt The value should be true if read receipt is required, else false.
     *                    Valid values: true, false.
     *                    Default value: false.
     * @param attachments A delimited separated list of files to attach (must be full path).
     * @param user If SMTP authentication is needed, the username to use.
     * @param password If SMTP authentication is needed, the password to use.
     * @param delimiter A delimiter to separate the email recipients and the attachments. Default value: ','.
     * @param characterSet The character set encoding for the entire email which includes subject, body,
     *                     attached file name and the attached file.
     *                     <br>Valid values: UTF-8, UTF-16, UTF-32, EUC-JP, ISO-2022-JP, Shift_JIS, Windows-31J.
     *                     Default value: UTF-8.
     * @param contentTransferEncoding The content transfer encoding scheme (such as 7bit, 8bit, base64, quoted-printable
     *                                etc) for the entire email which includes subject, body, attached file name and the
     *                                attached file.
     *                                Valid values: quoted-printable, base64, 7bit, 8bit, binary, x-token.
     *                                Default value: quoted-printable (or Q Encoding).
     * @param encryptionKeystore The path to the pks12 format keystore to use to encrypt the mail.
     * @param encryptionKeyAlias The alias of the key from the encryptionKeystore to use to encrypt the mail.
     * @param encryptionKeystorePassword The password for the encryptionKeystore.
     * @param timeout The timeout (seconds) for sending the mail messages.
     * @return a map containing the output of the operation. The keys present in the map are
     *      <br><b>returnResult</b> - that will contain the SentMailSuccessfully if the mail was sent successfully.
     *      <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the
     *      operation goes to failure.
     *      <br><b>exception</b> - the exception message if the operation goes to failure.
     * @throws Exception
     */
    @Action(name = "Send Mail",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
        )
    public Map<String, String> execute(
            @Param(value = HOSTNAME, required = true) String hostname,
            @Param(value = PORT, required = true) String port,
            @Param(value = HTML_EMAIL) String htmlEmail,
            @Param(value = FROM, required = true) String from,
            @Param(value = TO, required = true) String to,
            @Param(CC) String cc,
            @Param(BCC) String bcc,
            @Param(value = SUBJECT, required = true) String subject,
            @Param(value = BODY, required = true) String body,
            @Param(READ_RECEIPT) String readReceipt,
            @Param(ATTACHMENTS) String attachments,
            @Param(HEADERS) String headers,
            @Param(HEADERS_ROW_DELIMITER) String rowDelimiter,
            @Param(HEADERS_COLUMN_DELIMITER) String columnDelimiter,
            @Param(USER) String user,
            @Param(PASSWORD) String password,
            @Param(DELIMITER) String delimiter,
            @Param(CHARACTERSET) String characterSet,
            @Param(CONTENT_TRANSFER_ENCODING) String contentTransferEncoding,
            @Param(ENCRYPTION_KEYSTORE) String encryptionKeystore,
            @Param(ENCRYPTION_KEY_ALIAS) String encryptionKeyAlias,
            @Param(ENCRYPTION_KEYSTORE_PASSWORD) String encryptionKeystorePassword,
            @Param(ENABLE_TLS) String enableTLS,
            @Param(TIMEOUT) String timeout,
            @Param(ENCRYPTION_ALGORITHM) String encryptionAlgorithm
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
        sendMailInputs.setHeaders(headers);
        sendMailInputs.setRowDelimiter(rowDelimiter);
        sendMailInputs.setColumnDelimiter(columnDelimiter);
        sendMailInputs.setUser(user);
        sendMailInputs.setPassword(password);
        sendMailInputs.setDelimiter(delimiter);
        sendMailInputs.setCharacterset(characterSet);
        sendMailInputs.setContentTransferEncoding(contentTransferEncoding);
        sendMailInputs.setEncryptionKeystore(encryptionKeystore);
        sendMailInputs.setEncryptionKeyAlias(encryptionKeyAlias);
        sendMailInputs.setEncryptionKeystorePassword(encryptionKeystorePassword);
        sendMailInputs.setEnableTLS(enableTLS);
        sendMailInputs.setTimeout(timeout);
        sendMailInputs.setEncryptionAlgorithm(encryptionAlgorithm);

        try {
            return new SendMail().execute(sendMailInputs);
        } catch (Exception e) {
            return exceptionResult(e.getMessage(), e);
        }
    }

    private Map<String, String> exceptionResult(String message, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String exStr = writer.toString().replace("" + (char) 0x00, "");

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(RETURN_RESULT, message);
        returnResult.put(RETURN_CODE, FAILURE_RETURN_CODE);
        returnResult.put(EXCEPTION, exStr);
        return returnResult;
    }
}
