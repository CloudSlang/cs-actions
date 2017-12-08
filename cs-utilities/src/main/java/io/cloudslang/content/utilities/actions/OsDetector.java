/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.utilities.actions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.constants.ResponseNames;
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.entities.InputDefaults;
import io.cloudslang.content.services.WSManRemoteShellService;
import io.cloudslang.content.ssh.services.actions.ScoreSSHShellCommand;
import io.cloudslang.content.utilities.entities.OperatingSystemDetails;
import io.cloudslang.content.utilities.entities.OsDetectorInputs;
import io.cloudslang.content.utilities.services.osdetector.LocalOsDetectorService;
import io.cloudslang.content.utilities.services.osdetector.NmapOsDetectorService;
import io.cloudslang.content.utilities.services.osdetector.OperatingSystemDetectorService;
import io.cloudslang.content.utilities.services.osdetector.OsDetectorHelperService;
import io.cloudslang.content.utilities.services.osdetector.PowerShellOsDetectorService;
import io.cloudslang.content.utilities.services.osdetector.SshOsDetectorService;

import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.entities.InputDefaults.X_509_HOSTNAME_VERIFIER;
import static io.cloudslang.content.httpclient.HttpClientInputs.AUTH_TYPE;
import static io.cloudslang.content.httpclient.HttpClientInputs.KERBEROS_CONFIG_FILE;
import static io.cloudslang.content.httpclient.HttpClientInputs.KERBEROS_LOGIN_CONFIG_FILE;
import static io.cloudslang.content.httpclient.HttpClientInputs.KERBEROS_SKIP_PORT_CHECK;
import static io.cloudslang.content.httpclient.HttpClientInputs.KEYSTORE;
import static io.cloudslang.content.httpclient.HttpClientInputs.KEYSTORE_PASSWORD;
import static io.cloudslang.content.httpclient.HttpClientInputs.PROXY_HOST;
import static io.cloudslang.content.httpclient.HttpClientInputs.PROXY_PASSWORD;
import static io.cloudslang.content.httpclient.HttpClientInputs.PROXY_PORT;
import static io.cloudslang.content.httpclient.HttpClientInputs.PROXY_USERNAME;
import static io.cloudslang.content.httpclient.HttpClientInputs.TRUST_ALL_ROOTS;
import static io.cloudslang.content.httpclient.HttpClientInputs.TRUST_KEYSTORE;
import static io.cloudslang.content.httpclient.HttpClientInputs.TRUST_PASSWORD;
import static io.cloudslang.content.httpclient.HttpClientInputs.X509_HOSTNAME_VERIFIER;
import static io.cloudslang.content.ssh.utils.Constants.ALLOWED_CIPHERS;
import static io.cloudslang.content.ssh.utils.Constants.DEFAULT_CONNECT_TIMEOUT;
import static io.cloudslang.content.ssh.utils.Constants.DEFAULT_KNOWN_HOSTS_PATH;
import static io.cloudslang.content.ssh.utils.Constants.DEFAULT_USE_AGENT_FORWARDING;
import static io.cloudslang.content.ssh.utils.Constants.InputNames.AGENT_FORWARDING;
import static io.cloudslang.content.ssh.utils.Constants.InputNames.PASSWORD;
import static io.cloudslang.content.ssh.utils.Constants.InputNames.PORT;
import static io.cloudslang.content.ssh.utils.Constants.InputNames.USERNAME;
import static io.cloudslang.content.ssh.utils.Constants.KNOWN_HOSTS_PATH;
import static io.cloudslang.content.ssh.utils.Constants.KNOWN_HOSTS_POLICY;
import static io.cloudslang.content.ssh.utils.Constants.PRIVATE_KEY_DATA;
import static io.cloudslang.content.ssh.utils.Constants.PRIVATE_KEY_FILE;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.BASIC_AUTH;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.DEFAULT_ALLOWED_CIPHERS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.DEFAULT_NMAP_ARGUMENTS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.DEFAULT_NMAP_PATH;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.DEFAULT_NMAP_TIMEOUT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.DEFAULT_POWER_SHELL_OP_TIMEOUT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.DEFAULT_PROXY_PORT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.DEFAULT_SSH_TIMEOUT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.HOST;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.KNOWN_HOSTS_STRICT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.NMAP_ARGUMENTS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.NMAP_PATH;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.NMAP_TIMEOUT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.NMAP_VALIDATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.OS_ARCHITECTURE;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.OS_COMMANDS;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.OS_FAMILY;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.OS_NAME;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.OS_VERSION;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.POWERSHELL_OPERATION_TIMEOUT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.RESTRICTIVE_NMAP_VALIDATOR;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.SSH_CONNECT_TIMEOUT;
import static io.cloudslang.content.utilities.entities.constants.OsDetectorConstants.SSH_TIMEOUT;
import static io.cloudslang.content.utils.Constants.InputNames.PROTOCOL;
import static io.cloudslang.content.utils.Constants.InputNames.WINRM_LOCALE;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.contains;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class OsDetector {
    /**
     * Attempts to detect the operating system of a machine by using system calls (if the machine is local), connecting
     * to the machine using SSH or PowerShell (and running specific commands) or by running a Nmap command.
     *
     * @param host                          The hostname or ip address of the remote host.
     * @param username                      The username used to connect to the remote machine (For SSH and PowerShell detection).
     * @param password                      The password used to connect to the remote machine (For SSH and PowerShell detection).
     * @param port                          The port to use when connecting to the remote server (For SSH and PowerShell detection).
     *                                      Default for SSH: 22
     *                                      Default for PowerShell: 5986
     *                                      Example: 22, 5985, 5986
     * @param proxyHost                     The proxy server used to access the remote host (For SSH, PowerShell and Nmap detection).
     * @param proxyPort                     The proxy server port (For SSH, PowerShell and Nmap detection).
     *                                      Default value: 8080
     * @param proxyUsername                 The username used when connecting to the proxy (For SSH and PowerShell detection).
     * @param proxyPassword                 The password used when connecting to the proxy (For SSH and PowerShell detection).
     * @param privateKeyFile                The path to the private key file (OpenSSH type) on the machine where is the worker
     *                                      (For SSH detection).
     * @param privateKeyData                A string representing the private key (OpenSSH type) used for authenticating the user.
     *                                      This string is usually the content of a private key file. The 'privateKeyData' and the
     *                                      'privateKeyFile' inputs are mutually exclusive. For security reasons it is recommend
     *                                      that the private key be protected by a passphrase that should be provided through the
     *                                      'password' input (For SSH detection).
     * @param knownHostsPolicy              The policy used for managing known_hosts file (For SSH detection).
     *                                      Valid values: allow, strict, add.
     *                                      Default value: strict
     * @param knownHostsPath                The path to the known hosts file. (For SSH detection).
     * @param allowedCiphers                A comma separated list of ciphers that will be used in the client-server handshake
     *                                      mechanism when the connection is created. Check the notes section for security concerns
     *                                      regarding your choice of ciphers. The default value will be used even if the input is not
     *                                      added to the operation (For SSH detection).
     *                                      Default value: aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-ctr,aes192-cbc,aes256-ctr,aes256-cbc
     * @param agentForwarding               Enables or disables the forwarding of the authentication agent connection (For SSH detection).
     *                                      Agent forwarding should be enabled with caution.
     * @param sshTimeout                    Time in milliseconds to wait for the command to complete (For SSH detection).
     *                                      Default value is 90000 (90 seconds)
     * @param sshConnectTimeout             Time in milliseconds to wait for the connection to be made (For SSH detection).
     *                                      Default value: 10000
     * @param protocol                      The protocol to use when connecting to the remote server (For PowerShell detection).
     *                                      Valid values are 'HTTP' and 'HTTPS'.
     *                                      Default value is 'HTTPS'.
     * @param authType:                     Type of authentication used to execute the request on the target server (For PowerShell detection).
     *                                      Valid: 'basic', 'form', 'springForm', 'digest', 'ntlm', 'kerberos', 'anonymous' (no authentication)
     *                                      Default: 'basic'
     * @param trustAllRoots                 Specifies whether to enable weak security over SSL/TSL. A certificate is trusted even
     *                                      if no trusted certification authority issued it (For PowerShell detection).
     *                                      Valid values are 'true' and 'false'.
     *                                      Default value: false
     * @param x509HostnameVerifier          Specifies the way the server hostname must match a domain name in the subject's Common
     *                                      Name (CN) or subjectAltName field of the X.509 certificate. The hostname verification
     *                                      system prevents communication with other hosts other than the ones you intended.
     *                                      This is done by checking that the hostname is in the subject alternative name extension
     *                                      of the certificate. This system is designed to ensure that, if an attacker(Man In The
     *                                      Middle) redirects traffic to his machine, the client will not accept the connection.
     *                                      If you set this input to "allow_all", this verification is ignored and you become
     *                                      vulnerable to security attacks. For the value "browser_compatible" the hostname verifier
     *                                      works the same way as Curl and Firefox. The hostname must match either the first CN,
     *                                      or any of the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts.
     *                                      The only difference between "browser_compatible" and "strict" is that a wildcard
     *                                      (such as "*.foo.com") with "browser_compatible" matches all subdomains, including "a.b.foo.com".
     *                                      From the security perspective, to provide protection against possible Man-In-The-Middle
     *                                      attacks, we strongly recommend to use "strict" option (For PowerShell detection).
     *                                      Valid values are 'strict', 'browser_compatible', 'allow_all'.
     *                                      Default value: strict
     * @param trustKeystore                 The pathname of the Java TrustStore file. This contains certificates from other parties
     *                                      that you expect to communicate with, or from Certificate Authorities that you trust to
     *                                      identify other parties.  If the protocol selected is not 'https' or if trustAllRoots
     *                                      is 'true' this input is ignored (For PowerShell detection).
     *                                      Format of the keystore is Java KeyStore (JKS).
     * @param trustPassword                 The password associated with the TrustStore file. If trustAllRoots is false and
     *                                      trustKeystore is empty, trustPassword default will be supplied (For PowerShell detection).
     * @param keystore                      The pathname of the Java KeyStore file. You only need this if the server requires client
     *                                      authentication. If the protocol selected is not 'https' or if trustAllRoots is 'true'
     *                                      this input is ignored (For PowerShell detection).
     *                                      Format of the keystore is Java KeyStore (JKS).
     * @param keystorePassword              The password associated with the KeyStore file. If trustAllRoots is false and keystore
     *                                      is empty, keystorePassword default will be supplied (For PowerShell detection).
     * @param kerberosConfFile              A krb5.conf file with content similar to the one in the examples (where you
     *                                      replace CONTOSO.COM with your domain and 'ad.contoso.com' with your kdc FQDN).
     *                                      This configures the Kerberos mechanism required by the Java GSS-API methods
     *                                      (For PowerShell detection).
     *                                      Example: http://web.mit.edu/kerberos/krb5-1.4/krb5-1.4.4/doc/krb5-admin/krb5.conf.html
     * @param kerberosLoginConfFile         A login.conf file needed by the JAAS framework with the content similar to the one in examples
     *                                      (For PowerShell detection).
     *                                      Example: http://docs.oracle.com/javase/7/docs/jre/api/security/jaas/spec/com/sun/security/auth/module/Krb5LoginModule.html
     *                                      Examples: com.sun.security.jgss.initiate {com.sun.security.auth.module.Krb5LoginModule
     *                                            required principal=Administrator doNotPrompt=true useKeyTab=true
     *                                            keyTab="file:/C:/Users/Administrator.CONTOSO/krb5.keytab";};
     * @param kerberosSkipPortForLookup     Do not include port in the key distribution center database lookup (For PowerShell detection).
     *                                      Default value: true
     *                                      Valid values: true, false
     * @param winrmLocale                   The WinRM locale to use (For PowerShell detection).
     *                                      Default value: en-US
     * @param powerShellTimeout             Defines the OperationTimeout value in seconds to indicate that the clients expect a
     *                                      response or a fault within the specified time (For PowerShell detection).
     *                                      Default value: 60000
     * @param nmapPath                      The absolute path to the Nmap executable or "nmap" if added in system path (For Nmap detection).
     *                                      Note: the path can be a network path.
     *                                      Example: //my-network-share//nmap, nmap, "C:\\Program Files (x86)\\Nmap\\nmap.exe"
     *                                      Default value: nmap
     * @param nmapArguments                 The Nmap arguments for operating system detection. (For Nmap detection).
     *                                      Refer to this document for more details: https://nmap.org/book/man.html
     *                                      Default value: -sS -sU -O -Pn --top-ports 20
     * @param nmapValidator                 The validation level for the Nmap arguments. It is recommended to use a
     *                                      restrictive validator (For Nmap detection).
     *                                      Valid values: restrictive, permissive
     *                                      Default value: restrictive
     * @param nmapTimeout                   Time in milliseconds to wait for the Nmap command to finish execution (For Nmap detection).
     *                                      Default value: 30000
     * @return                              A map containing the output of the operation. Keys present in the map are:
     * <br><b>returnResult</b>              The primary output, containing a success message or the exception message in case of failure.
     * <br><b>returnCode</b>                The return code of the operation. 0 if the operation goes to success, -1 if the operation goes to failure.
     * <br><b>osFamily</b>                  The operating system family in case of success. If the osFamily can not be determined
     *                                      from either direct outputs of the detection commands or osName the value will
     *                                      be "Other".
     * <br><b>osName</b>                    The operating system name in case of success. This result might not be present.
     * <br><b>osArchitecture</b>            The operating system architecture in case of success. This result might not be present.
     * <br><b>osVersion</b>                 The operating system version in case of success. This result might not be present.
     * <br><b>osCommands</b>                The output of the commands that were run in order to detect the operating system.
     * <br><b>exception</b>                 The stack trace of the exception in case an exception occurred.
     */
    @Action(name = "Operating System Detector",
            outputs = {
                    @Output(RETURN_RESULT),
                    @Output(RETURN_CODE),
                    @Output(OS_FAMILY),
                    @Output(OS_NAME),
                    @Output(OS_ARCHITECTURE),
                    @Output(OS_VERSION),
                    @Output(OS_COMMANDS),
                    @Output(EXCEPTION),
            },
            responses = {
                    @Response(text = ResponseNames.SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = ResponseNames.FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE, matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR),
            }
    )
    public Map<String, String> execute(@Param(value = HOST, required = true) String host,

                                       @Param(USERNAME) String username,
                                       @Param(PASSWORD) String password,
                                       @Param(PORT) String port,

                                       @Param(PROXY_HOST) String proxyHost,
                                       @Param(PROXY_PORT) String proxyPort,
                                       @Param(PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,

                                       @Param(PRIVATE_KEY_FILE) String privateKeyFile,
                                       @Param(value = PRIVATE_KEY_DATA, encrypted = true) String privateKeyData,
                                       @Param(KNOWN_HOSTS_POLICY) String knownHostsPolicy,
                                       @Param(KNOWN_HOSTS_PATH) String knownHostsPath,
                                       @Param(ALLOWED_CIPHERS) String allowedCiphers,
                                       @Param(value = AGENT_FORWARDING) String agentForwarding,
                                       @Param(SSH_TIMEOUT) String sshTimeout,
                                       @Param(SSH_CONNECT_TIMEOUT) String sshConnectTimeout,

                                       @Param(PROTOCOL) String protocol,
                                       @Param(AUTH_TYPE) String authType,
                                       @Param(TRUST_ALL_ROOTS) String trustAllRoots,
                                       @Param(X509_HOSTNAME_VERIFIER) String x509HostnameVerifier,
                                       @Param(TRUST_KEYSTORE) String trustKeystore,
                                       @Param(value = TRUST_PASSWORD, encrypted = true) String trustPassword,
                                       @Param(KEYSTORE) String keystore,
                                       @Param(value = KEYSTORE_PASSWORD, encrypted = true) String keystorePassword,
                                       @Param(KERBEROS_CONFIG_FILE) String kerberosConfFile,
                                       @Param(KERBEROS_LOGIN_CONFIG_FILE) String kerberosLoginConfFile,
                                       @Param(KERBEROS_SKIP_PORT_CHECK) String kerberosSkipPortForLookup,
                                       @Param(WINRM_LOCALE) String winrmLocale,
                                       @Param(POWERSHELL_OPERATION_TIMEOUT) String powerShellTimeout,

                                       @Param(NMAP_PATH) String nmapPath,
                                       @Param(NMAP_ARGUMENTS) String nmapArguments,
                                       @Param(NMAP_VALIDATOR) String nmapValidator,
                                       @Param(NMAP_TIMEOUT) String nmapTimeout) {

        try {
            OsDetectorInputs osDetectorInputs = new OsDetectorInputs.Builder()
                    .withHost(host)
                    .withPort(port)
                    .withUsername(username)
                    .withPassword(password)
                    .withSshTimeout(defaultIfEmpty(sshTimeout, DEFAULT_SSH_TIMEOUT))
                    .withPowerShellTimeout(defaultIfEmpty(powerShellTimeout, DEFAULT_POWER_SHELL_OP_TIMEOUT))
                    .withNmapTimeout(defaultIfEmpty(nmapTimeout, DEFAULT_NMAP_TIMEOUT))
                    .withSshConnectTimeout(defaultIfEmpty(sshConnectTimeout, String.valueOf(DEFAULT_CONNECT_TIMEOUT)))
                    .withNmapPath(defaultIfEmpty(nmapPath, DEFAULT_NMAP_PATH))
                    .withNmapArguments(defaultIfEmpty(nmapArguments, DEFAULT_NMAP_ARGUMENTS))
                    .withNmapValidator(defaultIfEmpty(nmapValidator, RESTRICTIVE_NMAP_VALIDATOR))
                    .withPrivateKeyFile(privateKeyFile)
                    .withPrivateKeyData(privateKeyData)
                    .withKnownHostsPolicy(defaultIfEmpty(knownHostsPolicy, KNOWN_HOSTS_STRICT))
                    .withKnownHostsPath(defaultIfEmpty(knownHostsPath, DEFAULT_KNOWN_HOSTS_PATH.toString()))
                    .withAllowedCiphers(defaultIfEmpty(allowedCiphers, DEFAULT_ALLOWED_CIPHERS))
                    .withAgentForwarding(defaultIfEmpty(agentForwarding, valueOf(DEFAULT_USE_AGENT_FORWARDING)))
                    .withProtocol(defaultIfEmpty(protocol, InputDefaults.PROTOCOL.getValue()))
                    .withAuthType(defaultIfEmpty(authType, BASIC_AUTH))
                    .withProxyHost(proxyHost)
                    .withProxyPort(defaultIfEmpty(proxyPort, DEFAULT_PROXY_PORT))
                    .withProxyUsername(proxyUsername)
                    .withProxyPassword(proxyPassword)
                    .withTrustAllRoots(defaultIfEmpty(trustAllRoots, valueOf(false)))
                    .withX509HostnameVerifier(defaultIfEmpty(x509HostnameVerifier, X_509_HOSTNAME_VERIFIER.getValue()))
                    .withTrustKeystore(trustKeystore)
                    .withTrustPassword(trustPassword)
                    .withKerberosConfFile(kerberosConfFile)
                    .withKerberosLoginConfFile(kerberosLoginConfFile)
                    .withKerberosSkipPortForLookup(kerberosSkipPortForLookup)
                    .withKeystore(keystore)
                    .withKeystorePassword(keystorePassword)
                    .withWinrmLocale(defaultIfEmpty(winrmLocale, InputDefaults.WINRM_LOCALE.getValue()))
                    .build();

            OsDetectorHelperService osDetectorHelperService = new OsDetectorHelperService();
            NmapOsDetectorService nmapOsDetectorService = new NmapOsDetectorService(osDetectorHelperService);
            OperatingSystemDetectorService service = new OperatingSystemDetectorService(new SshOsDetectorService(osDetectorHelperService, new ScoreSSHShellCommand()),
                    new PowerShellOsDetectorService(osDetectorHelperService, new WSManRemoteShellService()),
                    nmapOsDetectorService,
                    new LocalOsDetectorService(osDetectorHelperService), osDetectorHelperService);

            osDetectorHelperService.validateNmapInputs(osDetectorInputs, nmapOsDetectorService);

            OperatingSystemDetails os = service.detectOs(osDetectorInputs);
            Map<String, String> returnResult;
            if (osDetectorHelperService.foundOperatingSystem(os)) {
                returnResult = getSuccessResultsMap("Successfully detected the operating system.");
                returnResult.put(OS_FAMILY, os.getFamily());
                returnResult.put(OS_NAME, os.getName());
                returnResult.put(OS_ARCHITECTURE, os.getArchitecture());
                returnResult.put(OS_VERSION, os.getVersion());
            } else {
                returnResult = getFailureResultsMap("Unable to detect the operating system.");
            }
            returnResult.put(OS_COMMANDS, osDetectorHelperService.formatOsCommandsOutput(os.getCommandsOutput()));
            return returnResult;
        } catch (Exception e) {
            return getFailureResultsMap(e);
        }
    }
}
