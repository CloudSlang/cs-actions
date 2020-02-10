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
import io.cloudslang.content.mail.entities.GetMailMessageInput;
import io.cloudslang.content.mail.services.GetMailMessageService;
import io.cloudslang.content.mail.utils.ResultUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.mail.utils.Constants.*;

public class GetMailMessageAction {
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
    @Action(name = "Get Mail Message",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.SUBJECT),
                    @Output(Outputs.BODY_RESULT),
                    @Output(Outputs.PLAIN_TEXT_BODY_RESULT),
                    @Output(Outputs.ATTACHED_FILE_NAMES_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Responses.SUCCESS, field = Outputs.RETURN_CODE,
                            value = ReturnCodes.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Responses.FAILURE, field = Outputs.RETURN_CODE,
                            value = ReturnCodes.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(
            @Param(value = Inputs.HOSTNAME, required = true) String hostname,
            @Param(value = Inputs.PORT) String port,
            @Param(value = Inputs.PROTOCOL) String protocol,
            @Param(value = Inputs.USERNAME, required = true) String username,
            @Param(value = Inputs.PASSWORD, required = true) String password,
            @Param(value = Inputs.FOLDER, required = true) String folder,
            @Param(value = Inputs.TRUSTALLROOTS) String trustAllRoots,
            @Param(value = Inputs.MESSAGE_NUMBER, required = true) String messageNumber,
            @Param(value = Inputs.SUBJECT_ONLY) String subjectOnly,
            @Param(value = Inputs.ENABLE_TLS) String enableTLS,
            @Param(value = Inputs.ENABLE_SSL) String enableSSL,
            @Param(value = Inputs.KEYSTORE) String keystore,
            @Param(value = Inputs.KEYSTORE_PASSWORD) String keystorePassword,
            @Param(value = Inputs.TRUST_KEYSTORE) String trustKeystore,
            @Param(value = Inputs.TRUST_PASSWORD) String trustPassword,
            @Param(value = Inputs.CHARACTER_SET) String characterSet,
            @Param(value = Inputs.DELETE_UPON_RETRIVAL) String deleteUponRetrieval,
            @Param(value = Inputs.DECRYPTION_KEYSTORE) String decryptionKeystore,
            @Param(value = Inputs.DECRYPTION_KEY_ALIAS) String decryptionKeyAlias,
            @Param(value = Inputs.DECRYPTION_KEYSTORE_PASSWORD) String decryptionKeystorePassword,
            @Param(value = Inputs.PROXY_HOST) String proxyHost,
            @Param(value = Inputs.PROXY_PORT) String proxyPort,
            @Param(value = Inputs.PROXY_USERNAME) String proxyUsername,
            @Param(value = Inputs.PROXY_PASSWORD) String proxyPassword,
            @Param(value = Inputs.TIMEOUT) String timeout,
            @Param(value = Inputs.MARK_MESSAGE_AS_READ) String markAsRead,
            @Param(value = Inputs.VERIFY_CERTIFICATE) String verifyCertificate) {
        GetMailMessageInput.Builder inputBuilder = new GetMailMessageInput.Builder()
                .hostname(hostname)
                .port(port)
                .protocol(protocol)
                .username(username)
                .password(password)
                .folder(folder)
                .trustAllRoots(trustAllRoots)
                .messageNumber(messageNumber)
                .subjectOnly(subjectOnly)
                .enableSSL(enableSSL)
                .enableTLS(enableTLS)
                .keystore(keystore)
                .keystorePassword(keystorePassword)
                .trustKeystore(trustKeystore)
                .trustPassword(trustPassword)
                .characterSet(characterSet)
                .deleteUponRetrieval(deleteUponRetrieval)
                .decryptionKeystore(decryptionKeystore)
                .decryptionKeyAlias(decryptionKeyAlias)
                .decryptionKeystorePassword(decryptionKeystorePassword)
                .timeout(timeout)
                .verifyCertificate(verifyCertificate)
                .markMessageAsRead(markAsRead)
                .proxyHost(proxyHost)
                .proxyPort(proxyPort)
                .proxyUsername(proxyUsername)
                .proxyPassword(proxyPassword);
        try {
            return new GetMailMessageService().execute(inputBuilder.build());
        } catch (Exception e) {
            return ResultUtils.fromException(e);
        }
    }
}