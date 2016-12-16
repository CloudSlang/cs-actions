/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.mail.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.mail.entities.GetMailMessageInputs;
import io.cloudslang.content.mail.services.GetMailMessage;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by giloan on 11/3/2014.
 */
public class GetMailMessageAction {

    /**
     * This operation is used to get the contents of a mail message. Inline attachments are not supported by this operation.
     *
     * @param hostname            The email host.
     * @param port                The port to connect to on host (normally 110 for POP3, 143 for IMAP4).
     *                            This input can be left empty if the "protocol" value is "pop3" or "imap4": for "pop3" this input will
     *                            be completed by default with 110, for "imap4", this input will be completed by default with 143.
     * @param protocol            The protocol to connect with. This input can be left empty if the port value is provided:
     *                            if the provided port value is 110, the pop3 protocol will be used by default,
     *                            if the provided port value is 143, the imap4 protocol will be used by default.
     *                            For other values for the "port" input, the protocol should be also specified. Valid values: pop3, imap4, imap.
     * @param username            The username for the mail host. Use the full email address as username.
     * @param password            The password for the mail host.
     * @param folder              The folder to read the message from (NOTE: POP3 only supports "INBOX").
     * @param trustAllRoots       Specifies whether to trust all SSL certificate authorities. This input is ignored if the
     *                            enableSSL input is set to false. If false, make sure to have the certificate installed.
     *                            The steps are explained at the end of inputs description. Valid values: true, false. Default value: true.
     * @param messageNumber       The number (starting at 1) of the message to retrieve.  Email ordering is a server setting
     *                            that is independent of the client.
     * @param subjectOnly         A boolean value. If true, only subjects are retrieved instead of the entire message.
     *                            Valid values: true, false. Default value: false.
     * @param enableSSL           Specify if the connection should be SSL enabled or not. Valid values: true, false. Default value: false.
     * @param keystore            The path to the keystore to use for SSL Client Certificates.
     * @param keystorePassword    The password for the keystore.
     * @param trustKeystore       The path to the trustKeystore to use for SSL Server Certificates.
     * @param trustPassword       The password for the trustKeystore.
     * @param characterSet        The character set used to read the email. By default the operation uses the character set
     *                            with which the email is marked, in order to read its content. Because sometimes this character
     *                            set isn't accurate you can provide you own value for this property.
     *                            <br>Valid values: UTF-8, UTF-16, UTF-32, EUC-JP, ISO-2022-JP, Shift_JIS, Windows-31J. Default value: UTF-8.
     * @param deleteUponRetrieval If true the email which is retrived will be deleted. For any other values it will be just retrieved.
     *                            Valid values: true, false. Default value: false.
     * @param decryptionKeystore  The path to the pks12 format keystore to use to decrypt the mail.
     * @param decryptionKeyAlias  The alias of the key from the decryptionKeystore to use to decrypt the mail.
     * @param decryptionKeystorePassword The password for the decryptionKeystore.
     * @param timeout The timeout (seconds) for sending the mail messages.
     * @return a map containing the output of the operation. Keys present in the map are:
     * <br><b>returnCode</b> - This is the primary output. It is 0 if the operation succeeded and -1 for failure.
     * <br><b>Subject</b> - Subject of the email.
     * <br><b>Body</b> - Only the body contents of the email. This will not contain the attachment including inline attachments.
     * This is in HTML format, not plain text.
     * <br><b>AttachedFileNames</b> - Attached file names to the email.
     * <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     * <br><b>exception</b> - the exception message if the operation goes to failure.
     */
    @Action(name = "Get Mail Message",
            outputs = {
                    @Output(GetMailMessage.RETURN_CODE),
                    @Output(GetMailMessage.RETURN_RESULT),
                    @Output(GetMailMessage.SUBJECT),
                    @Output(GetMailMessage.BODY_RESULT),
                    @Output(GetMailMessage.PLAIN_TEXT_BODY_RESULT),
                    @Output(GetMailMessage.ATTACHED_FILE_NAMES_RESULT),
                    @Output(GetMailMessage.EXCEPTION)
            },
            responses = {
                    @Response(text = GetMailMessage.SUCCESS, field = GetMailMessage.RETURN_CODE, value = GetMailMessage.SUCCESS_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = GetMailMessage.FAILURE, field = GetMailMessage.RETURN_CODE, value = GetMailMessage.FAILURE_RETURN_CODE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = GetMailMessageInputs.HOSTNAME, required = true) String hostname,
            @Param(value = GetMailMessageInputs.PORT) String port,
            @Param(value = GetMailMessageInputs.PROTOCOL) String protocol,
            @Param(value = GetMailMessageInputs.USERNAME, required = true) String username,
            @Param(value = GetMailMessageInputs.PASSWORD, required = true) String password,
            @Param(value = GetMailMessageInputs.FOLDER, required = true) String folder,
            @Param(value = GetMailMessageInputs.TRUSTALLROOTS) String trustAllRoots,
            @Param(value = GetMailMessageInputs.MESSAGE_NUMBER, required = true) String messageNumber,
            @Param(value = GetMailMessageInputs.SUBJECT_ONLY) String subjectOnly,
            @Param(value = GetMailMessageInputs.ENABLETLS) String enableTLS,
            @Param(value = GetMailMessageInputs.ENABLESSL) String enableSSL,
            @Param(value = GetMailMessageInputs.KEYSTORE) String keystore,
            @Param(value = GetMailMessageInputs.KEYSTORE_PASSWORD) String keystorePassword,
            @Param(value = GetMailMessageInputs.TRUST_KEYSTORE) String trustKeystore,
            @Param(value = GetMailMessageInputs.TRUST_PASSWORD) String trustPassword,
            @Param(value = GetMailMessageInputs.CHARACTER_SET) String characterSet,
            @Param(value = GetMailMessageInputs.DELETE_UPON_RETRIVAL) String deleteUponRetrieval,
            @Param(value = GetMailMessageInputs.DECRYPTION_KEYSTORE) String decryptionKeystore,
            @Param(value = GetMailMessageInputs.DECRYPTION_KEY_ALIAS) String decryptionKeyAlias,
            @Param(value = GetMailMessageInputs.DECRYPTION_KEYSTORE_PASSWORD) String decryptionKeystorePassword,
            @Param(value = GetMailMessageInputs.TIMEOUT) String timeout,
            @Param(value = GetMailMessageInputs.VERIFY_CERTIFICATE) String verifyCertificate
    ) {
        GetMailMessageInputs getMailMessageInputs = new GetMailMessageInputs();
        getMailMessageInputs.setHostname(hostname);
        getMailMessageInputs.setPort(port);
        getMailMessageInputs.setProtocol(protocol);
        getMailMessageInputs.setUsername(username);
        getMailMessageInputs.setPassword(password);
        getMailMessageInputs.setFolder(folder);
        getMailMessageInputs.setTrustAllRoots(trustAllRoots);
        getMailMessageInputs.setMessageNumber(messageNumber);
        getMailMessageInputs.setSubjectOnly(subjectOnly);
        getMailMessageInputs.setEnableTLS(enableTLS);
        getMailMessageInputs.setEnableSSL(enableSSL);
        getMailMessageInputs.setKeystore(keystore);
        getMailMessageInputs.setKeystorePassword(keystorePassword);
        getMailMessageInputs.setTrustKeystore(trustKeystore);
        getMailMessageInputs.setTrustPassword(trustPassword);
        getMailMessageInputs.setCharacterSet(characterSet);
        getMailMessageInputs.setDeleteUponRetrieval(deleteUponRetrieval);
        getMailMessageInputs.setDecryptionKeystore(decryptionKeystore);
        getMailMessageInputs.setDecryptionKeyAlias(decryptionKeyAlias);
        getMailMessageInputs.setDecryptionKeystorePassword(decryptionKeystorePassword);
        getMailMessageInputs.setTimeout(timeout);
        getMailMessageInputs.setVerifyCertificate(verifyCertificate);

        try {
            return new GetMailMessage().execute(getMailMessageInputs);
        } catch (Exception e) {
            return exceptionResult(e.getMessage(), e);
        }
    }

    private Map<String, String> exceptionResult(String message, Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String eStr = writer.toString().replace("" + (char) 0x00, "");

        Map<String, String> returnResult = new HashMap<>();
        returnResult.put(GetMailMessage.RETURN_RESULT, message);
        returnResult.put(GetMailMessage.RETURN_CODE, GetMailMessage.FAILURE_RETURN_CODE);
        returnResult.put(GetMailMessage.EXCEPTION, eStr);
        return returnResult;
    }
}
