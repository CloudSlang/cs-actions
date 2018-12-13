/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.office365.utils;

public class Descriptions {
    public static class Common {
        public static final String PROXY_HOST_DESC = "Proxy server used to access the Office 365 service.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the Office 365 service." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username input value.";
        public static final String PREEMPTIVE_AUTH_DESC = "If this field is 'true' authentication info will be sent" +
                " in the first request. If this is 'false' a request with no authentication info will be made and " +
                "if server responds with 401 and a header like WWW-Authenticate: Basic realm=\"myRealm\" only then " +
                "the authentication info will be sent.";
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
        public static final String CONN_MAX_TOTAL_DESC = "The maximum limit of connections in total.";
        public static final String CONN_MAX_ROUTE_DESC = "The maximum limit of connections on a per route basis.";
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
        public static final String DCA_HOST_DESC = "The hostname or IP of the DCA environment.";
        public static final String DCA_PORT_DESC = "The port on which the DCA environment is listening.";
        public static final String DCA_PROTOCOL_DESC = "The protocol with which to connect to the DCA environment";
        public static final String AUTH_TOKEN_DESC = "The authentication token from the Get Authentication " +
                "Token operation.";
        public static final String REFRESH_TOKEN_DESC = "The refresh token from the Get Authentication Token" +
                " operation. This can be used to extend the default lifetime of the authentication token.";

        // Outputs
        public static final String SUCCESS_RESPONSE_DESC = "Operation succeeded, returnCode is '0'.";
        public static final String FAILURE_RESPONSE_DESC = "Operation failed, returnCode is '-1'.";

    }

    public static class GetAuthorizationToken {
        public static final String LOGIN_TYPE_DESC = "Login method according to application type\n" +
                "Valid values: 'API', 'Native'\n" +
                "Default: 'API'";
        public static final String CLIENT_ID_DESC = "Service Client ID";
        public static final String CLIENT_SECRET_DESC = "Service Client Secret";
        public static final String USERNAME_DESC = "Office 365 username";
        public static final String PASSWORD_DESC = "Office 365 password";
        public static final String LOGIN_AUTHORITY_DESC = "The authority URL. Usually, the format for this input is:\n" +
                "'https://login.windows.net/TENANT_NAME/oauth2/token' where TENANT_NAME is your application\n" +
                "tenant.";
        public static final String RESOURCES_DESC = "The resource URL\n" +
                "Default: 'https://graph.microsoft.com'";

        public static final String RETURN_RESULT_DESC = "The authorization token for Office 365.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while generating the token.";

        public static final String SUCCESS_DESC = "Token generated successfully.";
        public static final String FAILURE_DESC = "There was an error while trying to retrieve token.";
    }
}