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
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.mail.constants.OutputNames;
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.services.GetMailAttachmentService;
import io.cloudslang.content.mail.constants.InputNames;
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
     * @param proxyHost                  The proxy server used.
     * @param proxyPort                  The proxy server port.
     * @param proxyUsername              The user name used when connecting to the proxy.
     * @param proxyPassword              The proxy server password associated with the proxyUsername input value.
     * @param timeout                    The timeout (seconds) for retrieving the mail message attachment.
     * @param tlsVersion                 The version of TLS to use. The value of this input will be ignored if
     *                                   'enableTLS' / 'enableSSL' is set to 'false'.
     *                                   Valid values: 'SSLv3', 'TLSv1.0', 'TLSv1.1', 'TLSv1.2'.
     *                                   Default value: TLSv1.2.
     * @param encryptionAlgorithm        A list of ciphers to use. The value of this input will be ignored if
     *                                   'tlsVersion' does not contain 'TLSv1.2'.
     *                                   Default value is TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
     *                                   TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384, TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
     *                                   TLS_DHE_RSA_WITH_AES_256_CBC_SHA256, TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,
     *                                   TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,
     *                                   TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256, TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
     *                                   TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384, TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
     *                                   TLS_RSA_WITH_AES_256_GCM_SHA384, TLS_RSA_WITH_AES_256_CBC_SHA256,
     *                                   TLS_RSA_WITH_AES_128_CBC_SHA256.
     * @return a map containing the output of the operations. Keys present in the map are:
     * <br><b>returnResult</b> - The text content of the attachment, if the attachment is in plain text format.
     * <br><b>temporaryFile</b> - The path to the temporary file where the attachment was saved.
     * <br><b>exception</b> - the exception message if the operation goes to failure.
     */
    @Action(name = "Get Mail Attachment",
            outputs = {
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_RESULT),
                    @Output(OutputNames.TEMPORARY_FILE),
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_CODE),
                    @Output(io.cloudslang.content.constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = io.cloudslang.content.constants.OutputNames.RETURN_CODE,
                            value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = io.cloudslang.content.constants.OutputNames.RETURN_CODE,
                            value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            })
    public Map<String, String> execute(
            @Param(value = InputNames.HOST, required = true) String hostname,
            @Param(value = InputNames.PORT) String port,
            @Param(value = InputNames.PROTOCOL) String protocol,
            @Param(value = InputNames.USERNAME, required = true) String username,
            @Param(value = InputNames.PASSWORD, required = true, encrypted = true) String password,
            @Param(value = InputNames.FOLDER, required = true) String folder,
            @Param(value = InputNames.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(value = InputNames.ENABLE_TLS) String enableTLS,
            @Param(value = InputNames.ENABLE_SSL) String enableSSL,
            @Param(value = InputNames.KEYSTORE) String keystore,
            @Param(value = InputNames.KEYSTORE_PASSWORD, encrypted = true) String keystorePassword,
            @Param(value = InputNames.MESSAGE_NUMBER, required = true) String messageNumber,
            @Param(value = InputNames.ATTACHMENT_NAME, required = true) String attachmentName,
            @Param(value = InputNames.CHARACTER_SET) String characterSet,
            @Param(value = InputNames.DESTINATION) String destination,
            @Param(value = InputNames.OVERWRITE) String overwrite,
            @Param(value = InputNames.DECRYPTION_KEYSTORE) String decryptionKeystore,
            @Param(value = InputNames.DECRYPTION_KEY_ALIAS) String decryptionKeyAlias,
            @Param(value = InputNames.DECRYPTION_KEYSTORE_PASSWORD, encrypted = true) String decryptionKeystorePassword,
            @Param(value = InputNames.PROXY_HOST) String proxyHost,
            @Param(value = InputNames.PROXY_PORT) String proxyPort,
            @Param(value = InputNames.PROXY_USERNAME) String proxyUsername,
            @Param(value = InputNames.PROXY_PASSWORD) String proxyPassword,
            @Param(value = InputNames.TIMEOUT) String timeout,
            @Param(value = InputNames.TLS_VERSION) String tlsVersion,
            @Param(value = InputNames.ENCRYPTION_ALGORITHM) String encryptionAlgorithm) {
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
                .decryptionKeystorePassword(decryptionKeystorePassword)
                .proxyHost(proxyHost)
                .proxyPort(proxyPort)
                .proxyUsername(proxyUsername)
                .proxyPassword(proxyPassword)
                .timeout(timeout)
                .tlsVersion(tlsVersion)
                .allowedCiphers(encryptionAlgorithm);
        try {
            return new GetMailAttachmentService().execute(inputBuilder.build());
        } catch (Exception ex) {
            return ResultUtils.fromException(ex);
        }
    }
}
