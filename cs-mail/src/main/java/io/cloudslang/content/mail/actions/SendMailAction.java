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
import io.cloudslang.content.mail.entities.SendMailInput;
import io.cloudslang.content.mail.services.SendMailService;
import io.cloudslang.content.mail.utils.ResultUtils;

import java.util.Map;

import static io.cloudslang.content.mail.utils.Constants.*;


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
     * @return a map containing the output of the operation. The keys present in the map are
     * <br><b>returnResult</b> - that will contain the SentMailSuccessfully if the mail was sent successfully.
     * <br><b>returnCode</b> - the return code of the operation. 0 if the operation goes to success, -1 if the
     * operation goes to failure.
     * <br><b>exception</b> - the exception message if the operation goes to failure.
     * @throws Exception
     */
    @Action(name = "Send Mail",
            outputs = {
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.RETURN_CODE),
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
            @Param(value = Inputs.HOSTNAME, required = true) String hostname,
            @Param(value = Inputs.PORT, required = true) String port,
            @Param(value = Inputs.HTML_EMAIL) String htmlEmail,
            @Param(value = Inputs.FROM, required = true) String from,
            @Param(value = Inputs.TO, required = true) String to,
            @Param(Inputs.CC) String cc,
            @Param(Inputs.BCC) String bcc,
            @Param(value = Inputs.SUBJECT, required = true) String subject,
            @Param(value = Inputs.BODY, required = true) String body,
            @Param(Inputs.READ_RECEIPT) String readReceipt,
            @Param(Inputs.ATTACHMENTS) String attachments,
            @Param(Inputs.HEADERS) String headers,
            @Param(Inputs.HEADERS_ROW_DELIMITER) String rowDelimiter,
            @Param(Inputs.HEADERS_COLUMN_DELIMITER) String columnDelimiter,
            @Param(Inputs.USERNAME) String user,
            @Param(Inputs.PASSWORD) String password,
            @Param(Inputs.DELIMITER) String delimiter,
            @Param(Inputs.CHARACTER_SET) String characterSet,
            @Param(Inputs.CONTENT_TRANSFER_ENCODING) String contentTransferEncoding,
            @Param(Inputs.ENCRYPTION_KEYSTORE) String encryptionKeystore,
            @Param(Inputs.ENCRYPTION_KEY_ALIAS) String encryptionKeyAlias,
            @Param(Inputs.ENCRYPTION_KEYSTORE_PASSWORD) String encryptionKeystorePassword,
            @Param(Inputs.ENABLE_TLS) String enableTLS,
            @Param(Inputs.TIMEOUT) String timeout,
            @Param(Inputs.ENCRYPTION_ALGORITHM) String encryptionAlgorithm) {
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
                .encryptionAlgorithm(encryptionAlgorithm);
        try {
            return new SendMailService().execute(inputBuilder.build());
        } catch (Exception e) {
            return ResultUtils.fromException(e);
        }
    }
}
