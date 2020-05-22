/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.oracle.oci.utils;

public class Descriptions {
    public static class Common {
        public static final String TENANCY_OCID_DESC =  "Oracle creates a tenancy for your company, which is a secure and isolated partition where you can create, organize, and administer your cloud resources. This is ID of the tenancy.";
        public static final String USER_OCID_DESC = "ID of an individual employee or system that needs to manage or use your company’s Oracle Cloud Infrastructure resources.";
        public static final String FINGER_PRINT_DESC = "Finger print of the public key generated for OCI account.";
        public static final String PRIVATE_KEY_FILE_DESC = "Location of the private key pem file generated for OCI account.";
        public static final String API_VERSION_DESC = "Version of the API of OCI."+
                "Default: '20160918'";
        public static final String REGION_DESC = "Region in OCI.";
        public static final String PROXY_HOST_DESC = "Proxy server used to access the OCI.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the OCI." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username " +
                "input value.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it." +
                "Default: 'false'";
        public static final String X509_DESC = "Specifies the way the server hostname must match a domain name in " +
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to " +
                "\"allow_all\" to skip any checking. For the value \"browser_compatible\" the hostname verifier " +
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between \"browser_compatible\" and \"strict\" is that a wildcard (such as \"*.foo.com\") " +
                "with \"browser_compatible\" matches all subdomains, including \"a.b.foo.com\"." +
                "Default: 'strict'";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String KEYSTORE_DESC ="The pathname of the Java KeyStore file. You only need this if the" +
                "server requires client authentication. If the protocol (specified by the 'url') is not 'https' or if " +
                "trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)" +
                "Default: <OO_Home>/java/lib/security/cacerts";
        public static final String KEYSTORE_PASSWORD_DESC = "The password associated with the KeyStore file. If "+
                "trustAllRoots is false and keystore is empty, keystorePassword default will be supplied." +
                "Default: changeit";
        public static final String CONN_MAX_TOTAL_DESC = "The maximum limit of connections in total." +
                "Default: '20'";
        public static final String CONN_MAX_ROUTE_DESC = "The maximum limit of connections on a per route basis." +
                "Default: '2'";
        public static final String KEEP_ALIVE_DESC = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                " execution it will close it." +
                "Default: 'true'";
        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period " +
                "inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' " +
                "represents an infinite timeout.";
        public static final String POLLING_INTERVAL_DESC = "The time, in seconds, to wait before a new request that " +
                "verifies if the operation finished is executed." +
                "Default: '1000'";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout." +
                "Default: '10000'";
        public static final String RESPONSE_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP " +
                "response. If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header " +
                "will be used. If responseCharacterSet is empty and the charset from the HTTP response Content-Type " +
                "header is empty, the default value will be used. You should not use this for method=HEAD or OPTIONS." +
                "Default: 'UTF-8'";
        public static final String AUTH_TOKEN_DESC = "The authorization token for OCI.";
        public static final String ORGANIZATION_NAME_DESC = "The name of the organization.";
        public static final String OCI_VERSION_DESC = "The version of OCI to use for this workspace. " +
                "Upon creating a workspace,the latest version is selected unless otherwise specified (e.g. \"0.11.1\")." +
                "Default: '0.12.1'";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXECUTION_TIMEOUT_DESC = "The amount of time (in milliseconds) to allow the client " +
                "to complete the execution of an API call. A value of '0' disables this feature." +
                "Default: '60000'";
        public static final String ASYNC_DESC = "Whether to run the operation is async mode." +
                "Default: 'false'";
        public static final String STATUS_CODE_DESC = "The HTTP status code for OCI API request.";
        public static final String PAGE_NUMBER_DESC = "Optional. If omitted, the endpoint will return the first page.";
        public static final String PAGE_SIZE_DESC = "Optional. If omitted, the endpoint will return 20 items per page. " +
                "The maximum page size is 150.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the request.";
        public static final String FAILURE_DESC = "There was an error while executing the request.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response. In case of an error this output will contain the error message.";
    }

    public static class ListInstances {
        public static final String LIST_INSTANCES_OPERATION_DESC = "List of organizations present";
        public static final String INSTANCE_LIST_DESC = "List of all organizations";
        public static final String COMPARTMENT_OCID_DESC = "Compartments are a fundamental component of Oracle Cloud Infrastructure for organizing and isolating your cloud resources. This is ID of the compartment.";
    }
}
