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
package io.cloudslang.content.winrm.utils;

public class Descriptions {
    public static class WinRM {
        public static final String HOST_DESC = "The hostname or IP address of the host.";
        public static final String PORT_DESC = "The port number used to connect to the host. The default value for this input dependents on the protocol input. Valid values: 5985 for Http and 5986 for Https.\nDefault value: 5986";
        public static final String PROTOCOL_DESC = "Specifies what protocol is used to execute commands on the remote host. Valid values: http, https\nDefault value: https";
        public static final String USERNAME_DESC = "The username to use when connecting to the host.";
        public static final String PASSWORD_DESC = "The password to use when connecting to the host.";
        public static final String AUTH_TYPE_DESC = "The type of authentication used by this operation when trying to execute the request on the target WinRM service. The supported authentication types are: Basic, NTLM and Kerberos.\nDefault value: NTLM";
        public static final String PROXY_HOST_DESC = "The proxy server used to access the host.";
        public static final String PROXY_PORT_DESC = "The proxy server port.\n Default value:8080";
        public static final String PROXY_USERNAME_DESC = "The username used when connecting to the proxy.";
        public static final String PROXY_PASSWORD_DESC = "The proxy server password associated with the proxyUsername input value.";
        public static final String TRUST_ALL_ROOTS_DESC = " Specifies whether to enable weak security over SSL/TSL. A certificate is trusted even if no trusted certification authority issued it.\nValid values: true, false\nDefault value: false";
        public static final String X509_HOSTNAME_VERIFIER_DESC = "Specifies the way the server hostname must match a domain name" +
                " in the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. The hostname verification" +
                " system prevents communication with other hosts other than the ones you intended. This is done by checking that the" +
                " hostname is in the subject alternative name extension of the certificate. This system is designed to ensure that," +
                " if an attacker(Man In The Middle) redirects traffic to his machine, the client will not accept the connection." +
                " If you set this input to \"allow_all\", this verification is ignored and you become vulnerable to security attacks." +
                " For the value \"browser_compatible\" the hostname verifier works the same way as Curl and Firefox." +
                " The hostname must match either the first CN, or any of the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts." +
                " The only difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") with \"browser_compatible\"" +
                " matches all subdomains, including \"a.b.foo.com\". From the security perspective, to provide protection against possible" +
                " Man-In-The-Middle attacks, we strongly recommend to use \"strict\" option.\nValid values: strict, browser_compatible, allow_all\n Default: strict";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains certificates" +
                " from other parties that you expect to communicate with, or from Certificate Authorities that you trust to " +
                "identify other parties.  If the protocol (specified by the 'url') is not 'https' or if trustAllRoots is 'true' this input is ignored.\nDefault value: <OO_Home>/java/lib/security/cacerts\n" +
                "Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.\nDefault value: changeit";
        public static final String KEYSTORE_DESC= "The pathname of the Java KeyStore file. You only need this if the server requires client authentication." +
                " If the protocol (specified by the 'url') is not 'https' or if trustAllRoots is 'true' this input is ignored.\n Default value: <OO_Home>/java/lib/security/cacerts\nFormat: Java KeyStore (JKS)";
        public static final String KEYSTORE_PASSWORD_DESC= "The password associated with the KeyStore file. If trustAllRoots is false and keystore is empty, keystorePassword default will be supplied.\nDefault value: change it";
        public static final String OPERATION_TIMEOUT_DESC = "Defines the OperationTimeout value in seconds to indicate that the clients expect a response or a fault within the specified time.";
        public static final String SCRIPT_DESC = "The PowerShell script that will be executed on the remote shell. Check the notes section for security implications of using this input.";
        public static final String TLS_VERSION_DESC = "The version of TLS to use. By default, the operation tries to establish a secure connection over TLSv1.2.\n Valid values: TLSv1, TLSv1.1, TLSv1.2, TLSv1.3.\nDefault value: TLSv1.2";
        public static final String REQUEST_NEW_KERBEROS_TICKET_DESC = "Allows you to request a new ticket to the target computer specified by the service principal name (SPN).\nValid values: true, false.\nDefault value: true";
        public static final String WORKING_DIRECTORY_DESC = "The path of the directory where to be executed the PowerShell script.";
        public static final String CONFIGURATION_NAME_DESC = "The name of the PSSessionConfiguration to use. This can be used to target specific versions of PowerShell if the PSSessionConfiguration is properly configured on the target. By default, after PSRemoting is enabled on the target, the configuration name for PowerShell v5 or lower is 'microsoft.powershell', for PowerShell v6 is 'PowerShell.6', for PowerShell v7 is 'PowerShell.7'. Additional configurations can be created by the user on the target machines.\n" +
                "Valid values: any PSConfiguration that exists on the host.\n" +
                "Examples: 'microsoft.powershell', 'PowerShell.6', 'PowerShell.7'";

        public static final String SUCCESS_DESC = "The PowerShell script was executed successfully and the 'scriptExitCode' value is 0.";
        public static final String FAILURE_DESC = "The PowerShell script could not be executed or the value of the 'scriptExitCode' is different than 0.";

        public static final String RETURN_CODE_DESC = " The returnCode of the operation: 0 for success, -1 for failure.";
        public static final String RETURN_RESULT_DESC = "The result of the script execution written on the stdout stream of the opened shell in case of success or the error from stderr in case of failure.";
        public static final String SCRIPT_EXIT_CODE_DESC = "The exit code returned by the powershell script execution.";
        public static final String STDERR_DESC = "The error messages and other warnings written on the stderr stream.";
        public static final String STDOUT_DESC = "The result of the script execution written on the stdout stream of the opened shell.";
        public static final String EXCEPTION_DESC = " In case of failure response, this result contains the java stack trace of the runtime exception or fault details that the remote server generated throughout its communication with the client.";
    }
}
