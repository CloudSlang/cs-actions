/*
 * (c) Copyright 2018 Micro Focus, L.P.
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
package io.cloudslang.content.hcm.utils;

public class Descriptions {

    public static class Common {
        // Inputs
        public static final String PROXY_HOST_DESC = "The proxy server used to access the web site.";
        public static final String PROXY_PORT_DESC = "The proxy server port. Default value: 8080. Valid values: -1 " +
                "and integer values greater than 0. The value '-1' indicates that the proxy port is not set and the " +
                "protocol default port will be used. If the protocol is 'http' and the 'proxyPort' is set to '-1'" +
                " then port '80' will be used.";
        public static final String PROXY_USER_DESC = "The user name used when connecting to the proxy.";
        public static final String PROXY_PASS_DESC = "The proxy server password associated with the proxyUsername " +
                "input value.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it.";
        public static final String X509_DESC = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\".";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String KEYSTORE_DESC = "The pathname of the Java KeyStore file. You only need this if " +
                "the server requires client authentication. If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String KEYSTORE_PASS_DESC = "The password associated with the KeyStore file. If " +
                "trustAllRoots is false and keystore is empty, keystorePassword default will be supplied.";
        public static final String KEEP_ALIVE_DESC = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                " execution it will close it.";
        public static final String USE_COOKIES_DESC = "Specifies whether to enable cookie tracking or not. Cookies " +
                "are stored between consecutive calls in a serializable session object therefore they will be " +
                "available on a branch level. If you specify a non-boolean value, the default value is used.";
        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period " +
                "inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' " +
                "represents an infinite timeout.";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout.";

        // Outputs
        public static final String RETURN_CODE_DESC = "The return code of the operation, 0 in case of success, " +
                "-1 in case of failure";
        public static final String EXCEPTION_DESC = "In case of failure, the error message, otherwise empty.";
        public static final String PARAM_LIST_DESC = "The final list which is composed of all properties name and " +
                "their values ";
    }


    public static class GetSubscriptionParams {
        public static final String GET_SUBSCRIPTION_PARAMS_DESC = "This operation can be used to retrieve a list of" +
                " all properties that contain in the property name the string \"param_\" and all the values " +
                "associated with the selected items";
        public static final String URL_DESC = "The web address to make the request to";
        public static final String AUTH_DESC = " The type of authentication used by this operation when trying to" +
                "execute the request on the target server. The authentication is not preemptive: a plain request not " +
                "including authentication info will be made and only when the server responds with a" +
                " 'WWW-Authenticate' header, the client sends the required headers. If the server needs no" +
                " authentication but you specify one in this input the request will still be valid but the client " +
                "cannot choose the authentication method and there is no fallback so you have to know which one you" +
                " need. If the web application and proxy use different authentication types, these must be specified" +
                " as shown in the example." +
                "Default value: Basic" +
                "Valid values: Basic, digest, ntlm, kerberos, any, anonymous, or a list of valid values separated" +
                " by comma." +
                "Example: Basic,digest";
        public static final String USERNAME_DESC = "The user name used for authentication. For NTLM authentication, " +
                "the required format is \"domain\\user\". If you only specify the user, a period is added in the " +
                "format \".\\user\" so that a local user on the target machine can be used. The username is required " +
                "for all authentication schemes except Kerberos.";
        public static final String PASSWORD_DESC = "The password used for authentication.";
        public static final String QUERY_PARAMS_DESC = "The list containing query parameters to append to the URL." +
                " The names and the values must not be URL encoded unless you specify queryParamsAreURLEncoded=true " +
                "because if they are encoded and queryParamsAreURLEncoded =false they will get double encoded.The " +
                "separator between name-value pairs is &. The query name will be separated from query value by =. " +
                "Note that you need to URL encode at least & to %26 and = to %3D and set queryParamsAreURLEncoded=" +
                " true if you leave the other special URL characters un-encoded they will be encoded by the HTTP " +
                "Client. Examples: parameterName1=parameterValue1&parameterName2=parameterValue2;";
    }
}
