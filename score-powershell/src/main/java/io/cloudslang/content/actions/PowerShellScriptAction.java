package io.cloudslang.content.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.entities.PowerShellActionInputs;
import io.cloudslang.content.services.PowerShellScriptService;
import io.cloudslang.content.services.PowerShellScriptServiceImpl;
import io.cloudslang.content.utils.Constants;

import java.util.Map;

/**
 * User: bancl
 * Date: 10/6/2015
 */
public class PowerShellScriptAction {
    /**
     * Executes a PowerShell command(s) on the remote machine.
     *
     * @param host The hostname or ip address of the source remote machine.
     * @param port The port to use when connecting to the remote machine.
     *             The interpretation and the default value for this connection option depend on the protocol that is used.
     * @param userName The username used to connect to the the remote machine.
     * @param password The password used to connect to the remote machine.
     * @param script The PowerShell script that will run on the remote machine.
     * @param connectionType Specifies what protocol is used to execute commands on the remote hosts.
     *                       One of the following values must be set:
     *                       WINRM_INTERNAL - uses WinRM over HTTP(S) to execute remote commands.
     *                          The default value for the port is 5985 for HTTP and 5986 for HTTPS.
     *                          A Java implementation of WinRM used.
     *                       WINRM_NATIVE - uses WinRM over HTTP(S) to execute remote commands.
     *                          The default value for the port is 5985 for HTTP and 5986 for HTTPS.
     *                          The native Windows implementation of WinRM is used, i.e. the winrs command.
     *                       TELNET - uses Telnet to execute remote commands. The port connection option
     *                          specifies the Telnet port to connect to. The default value is 23.
     * @param winrmContext The context used by the WinRM server. The default value is /wsman.
     *                     This connection option is only applicable for the WINRM_INTERNAL connection type.
     * @param winrmEnableHTTPS If set to true, HTTPS is used to connect to the WinRM server. Otherwise HTTP is used.
     *                         The default value is false. This connection option is only applicable for
     *                         the WINRM_INTERNAL and WINRM_NATIVE connection types.
     * @param winrmEnvelopSize The WinRM envelop size in bytes to use. The default value is 153600.
     *                         This connection option is only applicable for the WINRM_INTERNAL connection type.
     * @param winrmHttpsCertificateTrustStrategy The certificate trust strategy for WinRM HTTPS connections.
     *                                           One of the following values can be set:
     *                                              STRICT (default) - use Java's trusted certificate chains.
     *                                              SELF_SIGNED - self-signed certificates are allowed
     *                                              ALLOW_ALL - trust all certificates.
     *                                           This connection option is only applicable for the WINRM_INTERNAL
     *                                           connection type, when winrmEnableHttps is set to true.
     * @param winrmHttpsHostnameVerificationStrategy The hostname verification strategy for WinRM HTTPS connections.
     *                                               One of the following values can be set:
     *                                                  STRICT - strict verification
     *                                                  BROWSER_COMPATIBLE (default) - wilcards in certificates are matched.
     *                                                  ALLOW_ALL - trust all hostnames
     *                                               See the Apache HttpComponent HttpClient documentation for more
     *                                               information about the hostname verifications strategies.
     *                                               This connection option is only applicable for the WINRM_INTERNAL
     *                                               connection type, when winrmEnableHttps is set to true.
     * @param winrmKerberosAddPortToSpn If set to true, the port number (e.g. 5985) will be added to the service
     *                                  principal name (SPN) for which a Kerberos ticket is requested. The default
     *                                  value is false. This connection option is only applicable for the
     *                                  WINRM_INTERNAL connection type, when a Windows domain account is used.
     * @param winrmKerberosTicketCache If set to true, enables the use of the Kerberos ticket cache for use in
     *                                 authentication. When enabled, if a password is not specified the system ticket
     *                                 cache will be used. The default value is false. This connection option is only
     *                                 applicable for the WINRM_INTERNAL connection type, when a Windows domain
     *                                 account is used.
     * @param winrmKerberosUseHttpSpn If set to true, the protocol HTTP will be used in the service principal name (SPN)
     *                                for which a Kerberos ticket is requested. Otherwise the protocol WSMAN is used.
     *                                The default value is false. This connection option is only applicable for
     *                                the WINRM_INTERNAL connection type, when a Windows domain account is used.
     * @param winrmLocale The WinRM locale to use. The default value is en-US. This connection option is only
     *                    applicable for the WINRM_INTERNAL connection type.
     * @param winrmTimeout The WinRM timeout to use in XML schema duration format. The default value is PT60.000S.
     *                     This connection option is only applicable for the WINRM_INTERNAL connection type.
     *
     */
    @Action(name = "PowerShell Script Action",
            outputs = {
                    @Output(Constants.OutputNames.RETURN_CODE),
                    @Output(Constants.OutputNames.RETURN_RESULT),
                    @Output(Constants.OutputNames.EXCEPTION)
            },
            responses = {
                    @Response(text = Constants.ResponseNames.SUCCESS, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Constants.ResponseNames.FAILURE, field = Constants.OutputNames.RETURN_CODE, value = Constants.ReturnCodes.RETURN_CODE_FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            }
    )
    public Map<String, String> execute(
            @Param(value = Constants.InputNames.INPUT_HOST, required = true) String host,
            @Param(value = Constants.InputNames.INPUT_PORT) String port,
            @Param(value = Constants.InputNames.INPUT_USERNAME, required = true) String userName,
            @Param(value = Constants.InputNames.INPUT_PASSWORD, required = true, encrypted = true) String password,
            @Param(value = Constants.InputNames.INPUT_SCRIPT, required = true) String script,
            @Param(value = Constants.InputNames.CONNECTION_TYPE, required = true) String connectionType,
            @Param(value = Constants.InputNames.WINRM_CONTEXT) String winrmContext,
            @Param(value = Constants.InputNames.WINRM_ENABLE_HTTPS) String winrmEnableHTTPS,
            @Param(value = Constants.InputNames.WINRM_ENVELOP_SIZE) String winrmEnvelopSize,
            @Param(value = Constants.InputNames.WINRM_HTTPS_CERTIFICATE_TRUST_STRATEGY) String winrmHttpsCertificateTrustStrategy,
            @Param(value = Constants.InputNames.WINRM_HTTPS_HOSTNAME_VERIFICATION_STRATEGY) String winrmHttpsHostnameVerificationStrategy,
            @Param(value = Constants.InputNames.WINRM_KERBEROS_ADD_PORT_TO_SPN) String winrmKerberosAddPortToSpn,
            @Param(value = Constants.InputNames.WINRM_KERBEROS_TICKET_CACHE) String winrmKerberosTicketCache,
            @Param(value = Constants.InputNames.WINRM_KERBEROS_USE_HTTP_SPN) String winrmKerberosUseHttpSpn,
            @Param(value = Constants.InputNames.WINRM_LOCALE) String winrmLocale,
            @Param(value = Constants.InputNames.WINRM_TIMEMOUT) String winrmTimeout) {

        PowerShellActionInputs inputs = new PowerShellActionInputs();
        inputs.setHost(host);
        inputs.setPort(port);
        inputs.setUsername(userName);
        inputs.setPassword(password);
        inputs.setScript(script);
        inputs.setConnectionType(connectionType);
        inputs.setWinrmContext(winrmContext);
        inputs.setWinrmEnableHTTPS(winrmEnableHTTPS);
        inputs.setWinrmEnvelopSize(winrmEnvelopSize);
        inputs.setWinrmHttpsCertificateTrustStrategy(winrmHttpsCertificateTrustStrategy);
        inputs.setWinrmHttpsHostnameVerificationStrategy(winrmHttpsHostnameVerificationStrategy);
        inputs.setWinrmKerberosAddPortToSpn(winrmKerberosAddPortToSpn);
        inputs.setWinrmKerberosTicketCache(winrmKerberosTicketCache);
        inputs.setWinrmKerberosUseHttpSpn(winrmKerberosUseHttpSpn);
        inputs.setWinrmLocale(winrmLocale);
        inputs.setWinrmTimeout(winrmTimeout);

        PowerShellScriptService service = new PowerShellScriptServiceImpl();
        return service.execute(inputs);
    }
}
