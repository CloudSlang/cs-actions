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
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.services.GetMailAttachmentService;
import io.cloudslang.content.mail.utils.Constants.*;
import io.cloudslang.content.mail.utils.ResultUtils;

import java.util.Map;

public class GetMailAttachmentAction {

    /**
     * This operation downloads an email attachment to a specific directory or as a temporary file.
     * If the attachment is in plain text format, it also reads the text content of the attachment in a result.
     * Inline attachments are not supported by this operation.
     *
     * @param hostname                   The email host.
     * @param port                       The port to connect to on host (normally 110 for POP3, 143 for IMAP4).
     *                                   This input can be left empty if the "protocol" value is "pop3" or "imap4":
     *                                   for "pop3" this input will be completed by default with 110, for "imap4",
     *                                   this input will be completed by default with 143.
     * @param protocol                   The protocol to connect with. This input can be left empty if the port value is provided:
     *                                   if the provided port value is 110, the pop3 protocol will be used by default,
     *                                   if the provided port value is 143, the imap4 protocol will be used by default.
     *                                   For other values for the "port" input, the protocol should be also specified.
     *                                   Valid values: pop3, imap4, imap.
     * @param username                   The username for the mail host.  Use full email address as username.
     * @param password                   The password for the mail host.
     * @param folder                     The folder that contains the email message that includes the attachment to be
     *                                   read/downloaded (NOTE: POP3 only supports "INBOX").
     * @param trustAllRoots              Specifies whether to trust all SSL certificate authorities.
     *                                   This input is ignored if the enableSSL input is set to false.
     *                                   If false, make sure to have the certificate installed.
     *                                   The steps are explained at the end of inputs description.
     *                                   Valid values: true, false.
     *                                   Default value: true.
     * @param enableTLS                  Specify if the connection should be TLS enabled or not.
     *                                   Valid values: true, false.
     *                                   Default value: false.
     * @param enableSSL                  Specify if the connection should be SSL enabled or not.
     *                                   If not specified, the default value is used.
     *                                   Valid values: true, false.
     *                                   Default value: false.
     * @param keystore                   The path to the keystore to use for SSL Client Certificates.
     * @param keystorePassword           The password for the keystore.
     * @param messageNumber              The number (starting at 1) of the email message.
     *                                   Email ordering is a server setting that is independent of the client.
     * @param attachmentName             The name of the attachment in the email that should be read/downloaded.
     * @param characterSet               The character set used to read the email.
     *                                   By default the operation uses the character set with which the email is marked,
     *                                   in order to read its content. Because sometimes this character set isn't accurate
     *                                   you can provide you own value for this property.
     *                                   Valid values: UTF-8, UTF-16, UTF-32, EUC-JP, ISO-2022-JP, Shift_JIS, Windows-31J.
     *                                   Default value: UTF-8.
     * @param destination                The folder where the attachment will be saved.
     *                                   If this input is empty the attachment will be saved as a temporary file.
     *                                   Examples: C:\Folder Name, \\<computerName>\<Shared Folder>.
     * @param overwrite                  If true the attachment will overwrite any existing file with the same name
     *                                   in destination.
     *                                   Valid values: true, false.
     *                                   Default value: false.
     * @param decryptionKeystore         The path to the pks12 formatted keystore used to decrypt the mail.
     * @param decryptionKeyAlias         The alias of a RSA key pair from the decryptionKeystore.
     *                                   The private key from the pair will be used to decrypt the mail.
     *                                   The key pair must not have password.
     * @param decryptionKeystorePassword The password for the decryptionKeystore.
     * @return a map containing the output of the operations. Keys present in the map are:
     * <br><b>returnResult</b> - The text content of the attachment, if the attachment is in plain text format.
     * <br><b>temporaryFile</b> - The path to the temporary file where the attachment was saved.
     * <br><b>exception</b> - the exception message if the operation goes to failure.
     */
    @Action(name = "Get Mail Attachment",
            outputs = {
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.TEMPORARY_FILE),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Responses.SUCCESS, field = Outputs.RETURN_CODE,
                            value = ReturnCodes.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Responses.FAILURE, field = Outputs.RETURN_CODE,
                            value = ReturnCodes.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            })
    public Map<String, String> execute(
            @Param(value = Inputs.HOST, required = true) String hostname,
            @Param(value = Inputs.PORT) String port,
            @Param(value = Inputs.PROTOCOL) String protocol,
            @Param(value = Inputs.USERNAME, required = true) String username,
            @Param(value = Inputs.PASSWORD, required = true, encrypted = true) String password,
            @Param(value = Inputs.FOLDER, required = true) String folder,
            @Param(value = Inputs.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(value = Inputs.ENABLE_TLS) String enableTLS,
            @Param(value = Inputs.ENABLE_SSL) String enableSSL,
            @Param(value = Inputs.KEYSTORE) String keystore,
            @Param(value = Inputs.KEYSTORE_PASSWORD, encrypted = true) String keystorePassword,
            @Param(value = Inputs.MESSAGE_NUMBER, required = true) String messageNumber,
            @Param(value = Inputs.ATTACHMENT_NAME, required = true) String attachmentName,
            @Param(value = Inputs.CHARACTER_SET) String characterSet,
            @Param(value = Inputs.DESTINATION) String destination,
            @Param(value = Inputs.OVERWRITE) String overwrite,
            @Param(value = Inputs.DECRYPTION_KEYSTORE) String decryptionKeystore,
            @Param(value = Inputs.DECRYPTION_KEY_ALIAS) String decryptionKeyAlias,
            @Param(value = Inputs.DECRYPTION_KEYSTORE_PASSWORD, encrypted = true) String decryptionKeystorePassword
//        @Param(value = Inputs.PROXY_PORT) String proxyPort,
//        @Param(value = Inputs.PROXY_USERNAME) String proxyUsername,
//        @Param(value = Inputs.PROXY_PASSWORD) String proxyPassword,
//        @Param(value = Inputs.TIMEOUT) String timeout,
    ) {
        GetMailAttachmentInput.Builder inputBuilder = new GetMailAttachmentInput.Builder()
                .hostname(hostname)
                .port(port)
                .protocol(protocol)
                .username(username)
                .password(password)
                .folder(folder)
                .trustAllRoots(trustAllRoots)
                .enableTLS(enableTLS)
                .enableSSL(enableSSL)
                .keystore(keystore)
                .keystorePassword(keystorePassword)
                .messageNumber(messageNumber)
                .attachmentName(attachmentName)
                .characterSet(characterSet)
                .destination(destination)
                .overwrite(overwrite)
                .decryptionKeystore(decryptionKeystore)
                .decryptionKeyAlias(decryptionKeyAlias)
                .decryptionKeystorePassword(decryptionKeystorePassword);
        try {
            return new GetMailAttachmentService().execute(inputBuilder.build());
        } catch (Exception ex) {
            return ResultUtils.fromException(ex);
        }
    }
}
