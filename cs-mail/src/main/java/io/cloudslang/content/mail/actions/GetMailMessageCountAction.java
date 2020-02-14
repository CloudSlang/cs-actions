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
import io.cloudslang.content.mail.entities.GetMailMessageCountInput;
import io.cloudslang.content.mail.services.GetMailMessageCountService;
import io.cloudslang.content.mail.constants.InputNames;
import io.cloudslang.content.mail.utils.ResultUtils;

import java.util.Map;

public class GetMailMessageCountAction {

    /**
     * Gets the total number of messages in a folder via POP3 or IMAP4.
     *
     * @param hostname         The email host.
     * @param username         The username for email host.
     * @param password         The password for email host.
     * @param folder           The folder to read the message from (NOTE: POP3 only supports 'INBOX').
     * @param port             The port to connect to host on (normally 110 for POP3, 143 for IMAP4).
     *                         This input can be left empty if the protocol value is 'pop3' or 'imap4':
     *                         for 'pop3' this input will be completed by default with 110, for 'imap4',
     *                         this input will be completed by default with 143.
     * @param protocol         The protocol to connect with. This input can be left empty if the port value is provided:
     *                         if the provided port value is 110, the pop3 protocol will be used by default,
     *                         if the provided port value is 143, the imap4 protocol will be used by default.
     *                         For other values for the port input, the protocol should be also specified.
     *                         Valid values: pop3, imap4, imap.
     * @param trustAllRoots    Specifies whether to trust all SSL certificate authorities.
     *                         This input is ignored if the enableSSL input is set to false.
     *                         If false, make sure to have the certificate installed.
     *                         The steps are explained at the end of inputs description.
     *                         Valid values: true, false.
     *                         Default value: true.
     * @param enableTLS        Specify if the connection should be TLS enabled or not.
     *                         Valid values: true, false.
     * @param enableSSL        Specify if the connection should be SSL enabled or not.
     * @param keystore         The path to the keystore to use for SSL Client Certificates.
     * @param keystorePassword The path to the keystore to use for SSL Client Certificates.
     * @param trustKeystore    The path to the trustKeystore to use for SSL Server Certificates.
     * @param trustPassword    The password for the trustKeystore.
     * @param proxyHost        The proxy server used.
     * @param proxyPort        The proxy server port.
     * @param proxyUsername    The user name used when connecting to the proxy.
     * @param proxyPassword    The proxy server password associated with the proxyUsername input value.
     * @param timeout          The timeout (seconds) for retrieving the mail message count.
     * @return a map containing the output of the operations. Keys present in the map are:
     * <br><b>returnResult</b> - The text content of the attachment, if the attachment is in plain text format.
     * <br><b>exception</b> - the exception message if the operation goes to failure.
     */
    @Action(name = "Get Mail Message Count",
            outputs = {
                    @Output(io.cloudslang.content.constants.OutputNames.RETURN_RESULT),
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
            @Param(value = InputNames.HOST, required = true) String hostname,
            @Param(value = InputNames.USERNAME, required = true) String username,
            @Param(value = InputNames.PASSWORD, required = true, encrypted = true) String password,
            @Param(value = InputNames.FOLDER, required = true) String folder,
            @Param(value = InputNames.PORT) String port,
            @Param(value = InputNames.PROTOCOL) String protocol,
            @Param(value = InputNames.TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(value = InputNames.ENABLE_TLS) String enableTLS,
            @Param(value = InputNames.ENABLE_SSL) String enableSSL,
            @Param(value = InputNames.KEYSTORE) String keystore,
            @Param(value = InputNames.KEYSTORE_PASSWORD, encrypted = true) String keystorePassword,
            @Param(value = InputNames.TRUST_KEYSTORE) String trustKeystore,
            @Param(value = InputNames.TRUST_PASSWORD, encrypted = true) String trustPassword,
            @Param(value = InputNames.PROXY_HOST) String proxyHost,
            @Param(value = InputNames.PROXY_PORT) String proxyPort,
            @Param(value = InputNames.PROXY_USERNAME) String proxyUsername,
            @Param(value = InputNames.PROXY_PASSWORD) String proxyPassword,
            @Param(value = InputNames.TIMEOUT) String timeout) {
        GetMailMessageCountInput.Builder inputBuilder = new GetMailMessageCountInput.Builder()
                .hostname(hostname)
                .username(username)
                .password(password)
                .folder(folder)
                .port(port)
                .protocol(protocol)
                .trustAllRoots(trustAllRoots)
                .enableTLS(enableTLS)
                .enableSSL(enableSSL)
                .keystore(keystore)
                .keystorePassword(keystorePassword)
                .trustKeystore(trustKeystore)
                .trustPassword(trustPassword)
                .proxyHost(proxyHost)
                .proxyPort(proxyPort)
                .proxyUsername(proxyUsername)
                .proxyPassword(proxyPassword)
                .timeout(timeout);
        try {
            return new GetMailMessageCountService().execute(inputBuilder.build());
        } catch (Exception ex) {
            return ResultUtils.fromException(ex);
        }
    }
}
