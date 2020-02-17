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
import io.cloudslang.content.constants.OutputNames;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.mail.entities.SendMailInput;
import io.cloudslang.content.mail.services.SendMailService;
import io.cloudslang.content.mail.constants.InputNames;
import io.cloudslang.content.mail.utils.ResultUtils;

import java.util.Map;


/**
 * Created by giloan on 10/30/2014.
 */
public class SendMailAction {

    /**
     * The operation sends a smtp email.
     *
     * @param hostname                   The hostname or ip address of the smtp server.
     * @param port                       The port of the smtp service.
     * @param htmlEmail                  The value should be true if the email is in rich text/html format.
     *                                   The value should be false if the email is in plain text format.
     *                                   Valid values: true, false. Default value: true.
     * @param from                       From email address.
     * @param to                         A delimiter separated list of email address(es) or recipients where the email will be sent.
     * @param cc                         A delimiter separated list of email address(es) or recipients, to be placed in the CC.
     * @param bcc                        A delimiter separated list of email address(es) or recipients, to be placed in the BCC.
     * @param subject                    The email subject. If a subject spans on multiple lines, it is formatted to a single one.
     * @param body                       The body of the email.
     * @param readReceipt                The value should be true if read receipt is required, else false.
     *                                   Valid values: true, false.
     *                                   Default value: false.
     * @param attachments                A delimited separated list of files to attach (must be full path).
     * @param user                       If SMTP authentication is needed, the username to use.
     * @param password                   If SMTP authentication is needed, the password to use.
     * @param delimiter                  A delimiter to separate the email recipients and the attachments. Default value: ','.
     * @param characterSet               The character set encoding for the entire email which includes subject, body,
     *                                   attached file name and the attached file.
     *                                   <br>Valid values: UTF-8, UTF-16, UTF-32, EUC-JP, ISO-2022-JP, Shift_JIS, Windows-31J.
     *                                   Default value: UTF-8.
     * @param contentTransferEncoding    The content transfer encoding scheme (such as 7bit, 8bit, base64, quoted-printable
     *                                   etc) for the entire email which includes subject, body, attached file name and the
     *                                   attached file.
     *                                   Valid values: quoted-printable, base64, 7bit, 8bit, binary, x-token.
     *                                   Default value: quoted-printable (or Q Encoding).
     * @param encryptionKeystore         The path to the pks12 format keystore to use to encrypt the mail.
     * @param encryptionKeyAlias         The alias of the key from the encryptionKeystore to use to encrypt the mail.
     * @param encryptionKeystorePassword The password for the encryptionKeystore.
     * @param timeout                    The timeout (seconds) for sending the mail messages.
     * @param proxyHost                  The proxy server used.
     * @param proxyPort                  The proxy server port.
     * @param proxyUsername              The user name used when connecting to the proxy.
     * @param proxyPassword              The proxy server password associated with the proxyUsername input value.
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
     * @return a map containing the output of the operation. The keys present in the map are
     * <br><b>returnResult</b> - that will contain the SentMailSuccessfully if the mail was sent successfully.
     * <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the
     * operation goes to failure.
     * <br><b>exception</b> - the exception message if the operation goes to failure.
     */
    @Action(name = "Send Mail",
            outputs = {
                    @Output(OutputNames.RETURN_RESULT),
                    @Output(OutputNames.RETURN_CODE),
                    @Output(OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = OutputNames.RETURN_CODE,
                            value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = OutputNames.RETURN_CODE,
                            value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            })
    public Map<String, String> execute(
            @Param(value = InputNames.HOSTNAME, required = true) String hostname,
            @Param(value = InputNames.PORT, required = true) String port,
            @Param(value = InputNames.HTML_EMAIL) String htmlEmail,
            @Param(value = InputNames.FROM, required = true) String from,
            @Param(value = InputNames.TO, required = true) String to,
            @Param(value = InputNames.CC) String cc,
            @Param(value = InputNames.BCC) String bcc,
            @Param(value = InputNames.SUBJECT, required = true) String subject,
            @Param(value = InputNames.BODY, required = true) String body,
            @Param(value = InputNames.READ_RECEIPT) String readReceipt,
            @Param(value = InputNames.ATTACHMENTS) String attachments,
            @Param(value = InputNames.HEADERS) String headers,
            @Param(value = InputNames.HEADERS_ROW_DELIMITER) String rowDelimiter,
            @Param(value = InputNames.HEADERS_COLUMN_DELIMITER) String columnDelimiter,
            @Param(value = InputNames.USERNAME) String user,
            @Param(value = InputNames.PASSWORD) String password,
            @Param(value = InputNames.DELIMITER) String delimiter,
            @Param(value = InputNames.CHARACTER_SET) String characterSet,
            @Param(value = InputNames.CONTENT_TRANSFER_ENCODING) String contentTransferEncoding,
            @Param(value = InputNames.ENCRYPTION_KEYSTORE) String encryptionKeystore,
            @Param(value = InputNames.ENCRYPTION_KEY_ALIAS) String encryptionKeyAlias,
            @Param(value = InputNames.ENCRYPTION_KEYSTORE_PASSWORD) String encryptionKeystorePassword,
            @Param(value = InputNames.ENABLE_TLS) String enableTLS,
            @Param(value = InputNames.TIMEOUT) String timeout,
            @Param(value = InputNames.ENCRYPTION_ALGORITHM) String encryptionAlgorithm,
            @Param(value = InputNames.PROXY_HOST) String proxyHost,
            @Param(value = InputNames.PROXY_PORT) String proxyPort,
            @Param(value = InputNames.PROXY_USERNAME) String proxyUsername,
            @Param(value = InputNames.PROXY_PASSWORD) String proxyPassword,
            @Param(value = InputNames.TLS_VERSION) String tlsVersion) {
        SendMailInput.Builder inputBuilder = new SendMailInput.Builder()
                .hostname(hostname)
                .port(port)
                .htmlEmail(htmlEmail)
                .from(from)
                .to(to)
                .cc(cc)
                .bcc(bcc)
                .subject(subject)
                .body(body)
                .readReceipt(readReceipt)
                .attachments(attachments)
                .headers(headers)
                .rowDelimiter(rowDelimiter)
                .columnDelimiter(columnDelimiter)
                .user(user)
                .password(password)
                .delimiter(delimiter)
                .characterSet(characterSet)
                .contentTransferEncoding(contentTransferEncoding)
                .encryptionKeystore(encryptionKeystore)
                .encryptionKeyAlias(encryptionKeyAlias)
                .encryptionKeystorePassword(encryptionKeystorePassword)
                .enableTLS(enableTLS)
                .timeout(timeout)
                .encryptionAlgorithm(encryptionAlgorithm)
                .proxyHost(proxyHost)
                .proxyPort(proxyPort)
                .proxyUsername(proxyUsername)
                .proxyPassword(proxyPassword)
                .tlsVersion(tlsVersion)
                .allowedCiphers(encryptionAlgorithm);
        try {
            return new SendMailService().execute(inputBuilder.build());
        } catch (Exception e) {
            return ResultUtils.fromException(e);
        }
    }
}
