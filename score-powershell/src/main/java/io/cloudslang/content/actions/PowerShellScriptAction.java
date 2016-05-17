package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.entities.WSManRequestInputs;
import io.cloudslang.content.services.WSManRemoteShellService;
import io.cloudslang.content.utils.Constants;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.httpclient.HttpClientInputs.*;
import static io.cloudslang.content.utils.Constants.InputNames.*;
import static io.cloudslang.content.utils.Constants.OutputNames.*;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_FAILURE;
import static io.cloudslang.content.utils.Constants.ReturnCodes.RETURN_CODE_SUCCESS;

/**
 * Created by giloan on 3/26/2016.
 */
public class PowerShellScriptAction {

    /**
     * Executes a PowerShell script on a remote host.
     *
     * @param host                 The hostname or ip address of the remote host.
     * @param port                 The port to use when connecting to the remote WinRM server.
     * @param protocol             The protocol to use when connecting to the remote server.
     *                             Valid values are 'HTTP' and 'HTTPS'.
     *                             Default value is 'HTTPS'.
     * @param username             The username used to connect to the remote machine.
     * @param password             The password used to connect to the remote machine.
     * @param proxyHost            The proxy server used to access the remote host.
     * @param proxyPort            The proxy server port.
     * @param proxyUsername        The username used when connecting to the proxy.
     * @param proxyPassword        The password used when connecting to the proxy.
     * @param trustAllRoots        Specifies whether to enable weak security over SSL/TSL. A certificate is trusted even if no trusted certification authority issued it.
     *                             Default value is 'false'.
     *                             Valid values are 'true' and 'false'.
     * @param x509HostnameVerifier Specifies the way the server hostname must match a domain name in the subject's Common Name (CN) or subjectAltName field of the
     *                             X.509 certificate. The hostname verification system prevents communication with other hosts other than the ones you intended.
     *                             This is done by checking that the hostname is in the subject alternative name extension of the certificate. This system is
     *                             designed to ensure that, if an attacker(Man In The Middle) redirects traffic to his machine, the client will not accept the
     *                             connection. If you set this input to "allow_all", this verification is ignored and you become vulnerable to security attacks.
     *                             For the value "browser_compatible" the hostname verifier works the same way as Curl and Firefox. The hostname must match
     *                             either the first CN, or any of the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only
     *                             difference between "browser_compatible" and "strict" is that a wildcard (such as "*.foo.com") with "browser_compatible" matches
     *                             all subdomains, including "a.b.foo.com". From the security perspective, to provide protection against possible Man-In-The-Middle
     *                             attacks, we strongly recommend to use "strict" option.
     *                             Valid values are 'strict', 'browser_compatible', 'allow_all'.
     *                             Default value is 'strict'.
     * @param trustKeystore        The pathname of the Java TrustStore file. This contains certificates from other parties that you expect to communicate with, or from
     *                             Certificate Authorities that you trust to identify other parties.  If the protocol selected is not 'https' or if trustAllRoots
     *                             is 'true' this input is ignored.
     *                             Format of the keystore is Java KeyStore (JKS).
     * @param trustPassword        The password associated with the TrustStore file. If trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.
     *                             Default value is 'changeit'.
     * @param keystore             The pathname of the Java KeyStore file. You only need this if the server requires client authentication. If the protocol selected is not
     *                             'https' or if trustAllRoots is 'true' this input is ignored.
     *                             Format of the keystore is Java KeyStore (JKS).
     * @param keystorePassword     The password associated with the KeyStore file. If trustAllRoots is false and keystore is empty, keystorePassword default will be supplied.
     *                             Default value is 'changeit'.
     * @param maxEnvelopeSize      The maximum size of a SOAP packet in bytes for all stream content.
     *                             Default value is '153600'.
     * @param script               The PowerShell script that will be executed on the remote shell.
     * @param winrmLocale          The WinRM locale to use.
     *                             Default value is 'en-US'.
     * @param operationTimeout     Defines the OperationTimeout value in seconds to indicate that the clients expect a response or a fault within the specified time.
     *                             Default value is '60'.
     * @return
     */
    @Action(name = "PowerShell Script Action",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(SCRIPT_EXIT_CODE),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = RETURN_CODE, value = RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = RETURN_CODE, value = RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            }
    )
    public Map<String, String> execute(
            @Param(value = INPUT_HOST, required = true) String host,
            @Param(value = INPUT_PORT) String port,
            @Param(value = PROTOCOL) String protocol,
            @Param(value = USERNAME, required = true) String username,
            @Param(value = PASSWORD, required = true, encrypted = true) String password,
            @Param(value = PROXY_HOST) String proxyHost,
            @Param(value = PROXY_PORT) String proxyPort,
            @Param(value = PROXY_USERNAME) String proxyUsername,
            @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
            @Param(value = TRUST_ALL_ROOTS) String trustAllRoots,
            @Param(value = X509_HOSTNAME_VERIFIER) String x509HostnameVerifier,
            @Param(value = TRUST_KEYSTORE) String trustKeystore,
            @Param(value = TRUST_PASSWORD, encrypted = true) String trustPassword,
            @Param(value = KEYSTORE) String keystore,
            @Param(value = KEYSTORE_PASSWORD, encrypted = true) String keystorePassword,
            @Param(value = MAX_ENVELOP_SIZE) String maxEnvelopeSize,
            @Param(value = INPUT_SCRIPT, required = true) String script,
            @Param(value = WINRM_LOCALE) String winrmLocale,
            @Param(value = OPERATION_TIMEOUT) String operationTimeout
    ) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            WSManRemoteShellService wsManRemoteShellService = new WSManRemoteShellService();

            WSManRequestInputs wsManRequestInputs = new WSManRequestInputs.WSManRequestInputsBuilder()
                    .withHost(host)
                    .withPort(port)
                    .withProtocol(protocol)
                    .withUsername(username)
                    .withPassword(password)
                    .withProxyHost(proxyHost)
                    .withProxyPort(proxyPort)
                    .withProxyUsername(proxyUsername)
                    .withProxyPassword(proxyPassword)
                    .withMaxEnvelopeSize(maxEnvelopeSize)
                    .withTrustAllRoots(trustAllRoots)
                    .withX509HostnameVerifier(x509HostnameVerifier)
                    .withKeystore(keystore)
                    .withKeystorePassword(keystorePassword)
                    .withTrustKeystore(trustKeystore)
                    .withTrustPassword(trustPassword)
                    .withScript(script)
                    .withWinrmLocale(winrmLocale)
                    .withOperationTimeout(operationTimeout)
                    .build();

            resultMap = wsManRemoteShellService.runCommand(wsManRequestInputs);
            resultMap.put(RETURN_CODE, RETURN_CODE_SUCCESS);
        } catch (NumberFormatException nfe) {
            resultMap.put(EXCEPTION, ExceptionUtils.getStackTrace(nfe));
            resultMap.put(RETURN_CODE, RETURN_CODE_FAILURE);
        } catch (Exception e) {
            resultMap.put(EXCEPTION, ExceptionUtils.getStackTrace(e));
            resultMap.put(RETURN_CODE, RETURN_CODE_FAILURE);
        }
        return resultMap;
    }
}
