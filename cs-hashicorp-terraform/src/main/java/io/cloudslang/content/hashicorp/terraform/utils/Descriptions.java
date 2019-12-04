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

package io.cloudslang.content.hashicorp.terraform.utils;


public class Descriptions {
    public static class Common {
        public static final String PROXY_HOST_DESC = "Proxy server used to access the Terraform service.";
        public static final String PROXY_PORT_DESC = "Proxy server port used to access the Terraform service." +
                "Default: '8080'";
        public static final String PROXY_USERNAME_DESC = "Proxy server user name.";
        public static final String PROXY_PASSWORD_DESC = "Proxy server password associated with the proxy_username input value.";
        public static final String TRUST_ALL_ROOTS_DESC = "Specifies whether to enable weak security over SSL/TSL. " +
                "A certificate is trusted even if no trusted certification authority issued it.";
        public static final String X509_DESC = "Specifies the way the server hostname must match a domain name in "+
                "the subject's Common Name (CN) or subjectAltName field of the X.509 certificate. Set this to "+
                "allow_all to skip any checking. For the value browser_compatible the hostname verifier "+
                "works the same way as Curl and Firefox. The hostname must match either the first CN, or any of " +
                "the subject-alts. A wildcard can occur in the CN, and in any of the subject-alts. The only " +
                "difference between browser_compatible and strict is that a wildcard (such as *.foo.com) " +
                "with browser_compatible matches all subdomains, including a.b.foo.com.";
        public static final String TRUST_KEYSTORE_DESC = "The pathname of the Java TrustStore file. This contains " +
                "certificates from other parties that you expect to communicate with, or from Certificate Authorities" +
                " that you trust to identify other parties.  If the protocol (specified by the 'url') is not 'https' " +
                "or if trustAllRoots is 'true' this input is ignored. Format: Java KeyStore (JKS)";
        public static final String TRUST_PASSWORD_DESC = "The password associated with the TrustStore file. If " +
                "trustAllRoots is false and trustKeystore is empty, trustPassword default will be supplied.";
        public static final String CONN_MAX_TOTAL_DESC = "The maximum limit of connections in total.";
        public static final String CONN_MAX_ROUTE_DESC = "The maximum limit of connections on a per route basis.";
        public static final String KEEP_ALIVE_DESC = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                " execution it will close it.";
        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period " +
                "inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' " +
                "represents an infinite timeout.";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout.";
        public static final String EXECUTION_TIMEOUT_DESC="The amount of time (in milliseconds) to allow the client to complete the execution of an API call. A value of '0' disables this feature";
        public static final String ASYN_DESC="Whether to run the operation is async mode";
        public static final String POLLING_INTERVAL_DESC="The time, in seconds, to wait before a new request that verifies if the operation finished is executed.";
        public static final String RESPONSE_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP response,If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header will be used.If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty, the default value will be used. You should not use this for method=HEAD or OPTIONS";
        public static final String AUTH_TOKEN_DESC = "The authorization token for terraform";
        public static final String ORGANIZATION_NAME_DESC = "Name of the organization";
        public static final String TERAAFORM_VERSION_DESC = "Terraform version.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for Terraform API request.";
    }

    public static class GetWorkspaceDetails {
        public static final String GET_WORKSPACE_DETAILS_DESC = "Get details of workspace in an organization.";
        public static final String WORKSPACE_NAME_DESC = "The name of workspace whose description is to be fetched.";
        public static final String WORKSPACE_ID_DESC ="The Id of the workspace";
        public static final String GET_WORKSPACE_DETAILS_RETURN_RESULT_DESC = "Workspace details in response";
        public static final String GET_WORKSPACE_DETAILS_EXCEPTION_DESC = "An error message in case there was an error while creating the message.";
        public static final String FAILURE_DESC = "There was an error while creating workspace.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
    }

    public static class GetRunDetails {
        public static final String GET_RUN_DETAILS_DESC = "Getting details about a run";


    }



}
