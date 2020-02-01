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
package io.cloudslang.content.mail.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.mail.entities.IMAPGetMailMessageInput;
import io.cloudslang.content.mail.services.GetMessageImapServices;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.mail.services.GetMailMessage.*;
import static io.cloudslang.content.mail.services.GetMailMessage.FAILURE_RETURN_CODE;
import static io.cloudslang.content.mail.utils.Constants.ATTACHED_FILE_NAMES_RESULT;
import static io.cloudslang.content.mail.utils.Constants.BODY_RESULT;
import static io.cloudslang.content.mail.utils.Constants.*;
import static io.cloudslang.content.mail.utils.Constants.PLAIN_TEXT_BODY_RESULT;
import static io.cloudslang.content.mail.utils.Constants.RETURN_CODE;
import static io.cloudslang.content.mail.utils.Constants.RETURN_RESULT;
import static io.cloudslang.content.mail.utils.Constants.SUBJECT;
import static io.cloudslang.content.mail.utils.Constants.SUCCESS_RETURN_CODE;

public class IMAPGetMailMessageAction {
    /**
     * This operation is used to get the contents of a mail message. Inline attachments are not supported by this
     * operation.
     *
     * @param hostname                   The email host.
     * @param port                       The port to connect to on host (normally 110 for POP3, 143 for IMAP4).
     *                                   This input can be left empty if the "protocol" value is "pop3" or "imap4": for "pop3"
     *                                   this input will be completed by default with 110, for "imap4", this input will be
     *                                   completed by default with 143.
     * @param protocol                   The protocol to connect with. This input can be left empty if the port value is
     *                                   provided:
     *                                   if the provided port value is 110, the pop3 protocol will be used by default,
     *                                   if the provided port value is 143, the imap4 protocol will be used by default.
     *                                   For other values for the "port" input, the protocol should be also specified.
     *                                   Valid values: pop3, imap4, imap.
     * @param username                   The username for the mail host. Use the full email address as username.
     * @param password                   The password for the mail host.
     * @param folder                     The folder to read the message from (NOTE: POP3 only supports "INBOX").
     * @param trustAllRoots              Specifies whether to trust all SSL certificate authorities. This input is ignored if
     *                                   the enableSSL input is set to false. If false, make sure to have the certificate
     *                                   installed. The steps are explained at the end of inputs description.
     *                                   Valid values: true, false.
     *                                   Default value: true.
     * @param messageNumber              The number (starting at 1) of the message to retrieve.  Email ordering is a server
     *                                   setting that is independent of the client.
     * @param subjectOnly                A boolean value. If true, only subjects are retrieved instead of the entire message.
     *                                   Valid values: true, false. Default value: false.
     * @param enableSSL                  Specify if the connection should be SSL enabled or not. Valid values: true, false.
     *                                   Default value: false.
     * @param keystore                   The path to the keystore to use for SSL Client Certificates.
     * @param keystorePassword           The password for the keystore.
     * @param trustKeystore              The path to the trustKeystore to use for SSL Server Certificates.
     * @param trustPassword              The password for the trustKeystore.
     * @param characterSet               The character set used to read the email. By default the operation uses the character
     *                                   set with which the email is marked, in order to read its content. Because sometimes
     *                                   this character set isn't accurate you can provide you own value for this property.
     *                                   <br>Valid values: UTF-8, UTF-16, UTF-32, EUC-JP, ISO-2022-JP, Shift_JIS, Windows-31J.
     *                                   Default value: UTF-8.
     * @param deleteUponRetrieval        If true the email which is retrived will be deleted. For any other values it will be
     *                                   just retrieved.
     *                                   Valid values: true, false. Default value: false.
     * @param decryptionKeystore         The path to the pks12 format keystore to use to decrypt the mail.
     * @param decryptionKeyAlias         The alias of the key from the decryptionKeystore to use to decrypt the mail.
     * @param decryptionKeystorePassword The password for the decryptionKeystore.
     * @param timeout                    The timeout (seconds) for sending the mail messages.
     * @return a map containing the output of the operation. Keys present in the map are:
     * <br><b>returnCode</b> - This is the primary output. It is 0 if the operation succeeded and -1 for failure.
     * <br><b>subject</b> - Subject of the email.
     * <br><b>body</b> - Only the body contents of the email. This will not contain the attachment including inline
     * attachments. This is in HTML format, not plain text.
     * <br><b>attachedFileNames</b> - Attached file names to the email.
     * <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the operation
     * goes to failure.
     * <br><b>exception</b> - the exception message if the operation goes to failure.
     */
    @Action(name = "Get Mail Message using IMAP",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(SUBJECT),
                    @Output(BODY_RESULT),
                    @Output(PLAIN_TEXT_BODY_RESULT),
                    @Output(ATTACHED_FILE_NAMES_RESULT),
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
            @Param(value = PORT) String port,
            @Param(value = PROTOCOL) String protocol,
            @Param(value = USERNAME, required = true) String username,
            @Param(value = PASSWORD, required = true) String password,
            @Param(value = FOLDER, required = true) String folder,
            @Param(value = TRUSTALLROOTS) String trustAllRoots,
            @Param(value = MESSAGE_NUMBER, required = true) String messageNumber,
            @Param(value = SUBJECT_ONLY) String subjectOnly,
            @Param(value = ENABLETLS) String enableTLS,
            @Param(value = ENABLESSL) String enableSSL,
            @Param(value = KEYSTORE) String keystore,
            @Param(value = KEYSTORE_PASSWORD) String keystorePassword,
            @Param(value = TRUST_KEYSTORE) String trustKeystore,
            @Param(value = TRUST_PASSWORD) String trustPassword,
            @Param(value = CHARACTER_SET) String characterSet,
            @Param(value = DELETE_UPON_RETRIVAL) String deleteUponRetrieval,
            @Param(value = DECRYPTION_KEYSTORE) String decryptionKeystore,
            @Param(value = DECRYPTION_KEY_ALIAS) String decryptionKeyAlias,
            @Param(value = DECRYPTION_KEYSTORE_PASSWORD) String decryptionKeystorePassword,
            @Param(value = PROXY_HOST) String proxyHost,
            @Param(value = PROXY_PORT) String proxyPort,
            @Param(value = PROXY_USERNAME) String proxyUsername,
            @Param(value = PROXY_PASSWORD) String proxyPassword,
            @Param(value = TIMEOUT) String timeout,
            @Param(value = VERIFY_CERTIFICATE) String verifyCertificate
    ) {
        IMAPGetMailMessageInput getMailMessageInputs = new IMAPGetMailMessageInput();
        getMailMessageInputs.setHostname(hostname);
        getMailMessageInputs.setProxyHost(proxyHost);
        getMailMessageInputs.setProxyPassword(proxyPassword);
        getMailMessageInputs.setProxyPort(proxyPort);
        getMailMessageInputs.setProxyUsername(proxyUsername);
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
            return new GetMessageImapServices().execute(getMailMessageInputs);
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