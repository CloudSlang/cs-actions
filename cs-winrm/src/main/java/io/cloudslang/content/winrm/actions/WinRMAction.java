/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.winrm.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.utils.NumberUtilities;
import io.cloudslang.content.utils.StringUtilities;
import io.cloudslang.content.winrm.entities.WinRMInputs;
import io.cloudslang.content.winrm.service.WinRMService;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType.COMPARE_EQUAL;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.ERROR;
import static com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType.RESOLVED;
import static io.cloudslang.content.constants.OutputNames.*;
import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.winrm.utils.Constants.*;
import static io.cloudslang.content.winrm.utils.Descriptions.WinRM.*;
import static io.cloudslang.content.winrm.utils.Inputs.WinRMInputs.*;
import static io.cloudslang.content.winrm.utils.Inputs.WinRMInputs.TLS_VERSION;
import static io.cloudslang.content.winrm.utils.InputsValidation.verifyWinRMInputs;
import static io.cloudslang.content.winrm.utils.Outputs.WinRMOutputs.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class WinRMAction {
    @Action(name = "WinRM Command",
            outputs = {
                    @Output(value = RETURN_CODE, description = RETURN_CODE_DESC),
                    @Output(value = RETURN_RESULT, description = RETURN_RESULT_DESC),
                    @Output(value = STDERR, description = STDERR_DESC),
                    @Output(value = COMMAND_EXIT_CODE, description = COMMAND_EXIT_CODE_DESC),
                    @Output(value = EXCEPTION, description = EXCEPTION_DESC),
                    @Output(value = STDOUT, description = STDOUT_DESC)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = COMPARE_EQUAL, responseType = RESOLVED, description = SUCCESS_DESC),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = COMPARE_EQUAL, responseType = ERROR, description = FAILURE_DESC)
            }
    )
    public Map<String, String> execute(
            @Param(value = HOST, description = HOST_DESC, required = true) String host,
            @Param(value = COMMAND, description = COMMAND_DESC, required = true) String command,
            @Param(value = PORT, description = PORT_DESC) String port,
            @Param(value = PROTOCOL, description = PROTOCOL_DESC) String protocol,
            @Param(value = USERNAME, description = USERNAME_DESC) String username,
            @Param(value = PASSWORD, description = PASSWORD_DESC, encrypted = true) String password,
            @Param(value = AUTH_TYPE, description = AUTH_TYPE_DESC) String authType,
            @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
            @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
            @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
            @Param(value = PROXY_PASSWORD, description = PROXY_PASSWORD_DESC, encrypted = true) String proxyPassword,
            @Param(value = TRUST_ALL_ROOTS, description = TRUST_ALL_ROOTS_DESC) String trustAllRoots,
            @Param(value = X509_HOSTNAME_VERIFIER, description = X509_HOSTNAME_VERIFIER_DESC) String x509HostnameVerifier,
            @Param(value = TRUST_KEYSTORE, description = TRUST_KEYSTORE_DESC) String trustKeystore,
            @Param(value = TRUST_PASSWORD, description = TRUST_PASSWORD_DESC, encrypted = true) String trustPassword,
            @Param(value = KEYSTORE, description = KEYSTORE_DESC) String keystore,
            @Param(value = KEYSTORE_PASSWORD, description = KEYSTORE_PASSWORD_DESC, encrypted = true) String keystorePassword,
            @Param(value = OPERATION_TIMEOUT, description = OPERATION_TIMEOUT_DESC) String operationTimeout,
            @Param(value = TLS_VERSION, description = TLS_VERSION_DESC) String tlsVersion,
            @Param(value = REQUEST_NEW_KERBEROS_TICKET, description = REQUEST_NEW_KERBEROS_TICKET_DESC) String requestNewKerberosToken,
            @Param(value = WORKING_DIRECTORY, description = WORKING_DIRECTORY_DESC) String workingDirectory,
            @Param(value = CONFIGURATION_NAME, description = CONFIGURATION_NAME_DESC) String configurationName,
            @Param(value = COMMAND_TYPE, description = COMMAND_TYPE_DESC) String commandType
    ) {
        host = defaultIfEmpty(host, EMPTY);
        command = defaultIfEmpty(command, EMPTY);
        port = defaultIfEmpty(port, DEFAULT_PORT);
        protocol = defaultIfEmpty(protocol, HTTPS);
        username = defaultIfEmpty(username, EMPTY);
        password = defaultIfEmpty(password, EMPTY);
        authType = defaultIfEmpty(authType, NTLM);
        proxyHost = defaultIfEmpty(proxyHost, EMPTY);
        proxyPort = defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT);
        proxyUsername = defaultIfEmpty(proxyUsername, EMPTY);
        proxyPassword = defaultIfEmpty(proxyPassword, EMPTY);
        trustAllRoots = defaultIfEmpty(trustAllRoots, BOOLEAN_FALSE);
        x509HostnameVerifier = defaultIfEmpty(x509HostnameVerifier, STRICT);
        trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);
        trustPassword = defaultIfEmpty(trustPassword, CHANGE_IT);
        keystore = defaultIfEmpty(keystore, DEFAULT_JAVA_KEYSTORE);
        keystorePassword = defaultIfEmpty(keystorePassword, CHANGE_IT);
        operationTimeout = defaultIfEmpty(operationTimeout, DEFAULT_TIMEOUT);
        tlsVersion = defaultIfEmpty(tlsVersion, TLSv12);
        requestNewKerberosToken = defaultIfEmpty(requestNewKerberosToken, BOOLEAN_TRUE);
        workingDirectory = defaultIfEmpty(workingDirectory, EMPTY);
        configurationName = defaultIfEmpty(configurationName, EMPTY);
        commandType = defaultIfEmpty(commandType, DEFAULT_COMMAND_TYPE);

        final List<String> exceptionMessages = verifyWinRMInputs(proxyPort, trustAllRoots, operationTimeout, requestNewKerberosToken,
                authType, x509HostnameVerifier, trustKeystore, keystore, port, tlsVersion, protocol, commandType);
        if (!exceptionMessages.isEmpty()) {
            return getFailureResultsMap(StringUtilities.join(exceptionMessages, NEW_LINE));
        }

        WinRMInputs winRMInputs = new WinRMInputs.WinRMBuilder()
                .host(host)
                .port(port)
                .protocol(protocol)
                .username(username)
                .password(password)
                .authType(authType)
                .command(command)
                .proxyHost(proxyHost)
                .proxyPort(proxyPort)
                .proxyUsername(proxyUsername)
                .proxyPassword(proxyPassword)
                .trustAllRoots(trustAllRoots)
                .x509HostnameVerifier(x509HostnameVerifier)
                .keystore(keystore)
                .keystorePassword(keystorePassword)
                .trustKeystore(trustKeystore)
                .trustPassword(trustPassword)
                .tlsVersion(tlsVersion)
                .operationTimeout(NumberUtilities.toInteger(operationTimeout))
                .requestNewKerberosToken(requestNewKerberosToken)
                .workingDirectory(workingDirectory)
                .configurationName(configurationName)
                .commandType(commandType)
                .build();
        try {
            return WinRMService.execute(winRMInputs);
        } catch (Exception exception) {
            Map<String, String> results = new HashMap();
            results.put("returnCode", "-1");
            try{
                results.put("returnResult", ExceptionUtils.getRootCause(exception).toString());
            }catch (NullPointerException e){
                results.put("returnResult", exception.getMessage());
            }
            results.put("exception", ExceptionUtils.getStackTrace(exception));
            return results;
        }
    }
}
