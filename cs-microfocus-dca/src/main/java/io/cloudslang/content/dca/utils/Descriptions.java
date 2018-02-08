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
package io.cloudslang.content.dca.utils;

public class Descriptions {

    public static class Common {
        // Inputs
        public static final String PREEMPTIVE_AUTH_DESC = "If this field is 'true' authentication info will be sent" +
                " in the first request. If this is 'false' a request with no authentication info will be made and " +
                "if server responds with 401 and a header like WWW-Authenticate: Basic realm=\"myRealm\" only then " +
                "the authentication info will be sent.";
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
        public static final String RETURN_CODE_DESC = "The return code of the operation, 0 in case of success, " +
                "-1 in case of failure";
        public static final String EXCEPTION_DESC = "In case of failure, the error message, otherwise empty.";
        public static final String SUCCESS_RESPONSE_DESC = "Operation succeeded, returnCode is '0'.";
        public static final String FAILURE_RESPONSE_DESC = "Operation failed, returnCode is '-1'.";
    }

    public static class GetAuthenticationToken {
        public static final String GET_AUTH_TOKEN_DESC = "This operation is used to retrieve an authentication token" +
                " from an IdM instance, in order to be used with the DCA operations and flows.";

        // Inputs
        public static final String IDM_HOST_DESC = "The hostname or IP of the IdM with which to authenticate.";
        public static final String IDM_PORT_DESC = "The port on which IdM is listening on the host.";
        public static final String PROTOCOL_DESC = "The protocol to use when connecting to IdM.";
        public static final String IDM_USERNAME_DESC = "The IdM username to use when authenticating.";
        public static final String IDM_PASSWORD_DESC = "The password of the IdM user.";
        public static final String DCA_USERNAME_DESC = "The DCA user to authenticate.";
        public static final String DCA_PASSWORD_DESC = "The password of the DCA user.";
        public static final String DCA_TENANT_DESC = "The tenant of the DCA user to authenticate.";

        // Outputs
        public static final String RETURN_RESULT_DESC = "The authentication token in case of success, or an error" +
                " message in case of failure.";
        public static final String AUTH_TOKEN_OUT_DESC = "The authentication token returned by the IdM service.";
        public static final String REFRESH_TOKEN_OUT_DESC = "The refresh token returned by the IdM service.";
    }

    public static class GetDeployment {
        public static final String GET_DEPLOYMENT_DESC = "This operation can be used to get information about " +
                "a DCA deployment.";

        public static final String DEPLOYMENT_UUID_DESC = "The UUID of the deployment for which the information " +
                "will be retrieved.";
        public static final String RETURN_RESULT_DESC = "In case of success, a JSON representation of the " +
                "deployment, otherwise an error message.";
        public static final String STATUS_DESC = "The status of the deployment.";
    }

    public static class GetResource {
        public static final String SUCCESS_MESSAGE = "Successfully retrieved the resource details.";
        public static final String GET_RESOURCE_DESC = "This operation can be used to get information about " +
                "a DCA resource.";
        public static final String RESOURCE_UUID_DESC = "The UUID of the resource for which the information " +
                "will be retrieved.";
        public static final String RETURN_RESULT_DESC = "In case of success, a successful message, " +
                "otherwise an error message.";
        public static final String NAME_DESC = "The name of the resource.";
        public static final String DNS_NAME_DESC = "The DNS name of the resource.";
        public static final String RESOURCE_JSON_DESC = "The JSON representation of the resource.";
        public static final String RESOURCE_TYPE_DESC = "The type of the resource.";
    }

    public static class DeployTemplate {
        public static final String DEPLOY_TEMPLATE_DESC = "This operation can be used to deploy a DCA template.";

        public static final String RESOURCES_DESC = "The JSON array with resources resulted from the Create Resource" +
                " JSON operation.";
        public static final String TEMPLATE_ID_DESC = "The UUID of the DCA template to deploy.";
        public static final String DESCRIPTION_DESC = "A description of the deployment.";
        public static final String NAME_DESC = "The display name of the deployment.";
        public static final String ASYNC_DESC = "Whether to run the operation is async mode. In async mode, the " +
                "deployment will be started, and the operation will exit. Otherwise the operation will wait for the" +
                " result of the deployment.";
        public static final String TIMEOUT_DESC = "The timeout in seconds, in case the operation runs in sync mode.";
        public static final String POLLING_INTERVAL_DESC = "The interval in seconds at which the deployment will be " +
                "queried in sync mode.";
    }

    public static class CreateResourceJSON {
        public static final String CREATE_RESOURCE_JSON_DESC = "This utility operation is used to create a JSON " +
                "representation of a DCA resource for use in the Deploy Template operation.";

        public static final String DELIMITER_DESC = "The delimiter used in the above lists.";
        public static final String TYPE_UUID_DESC = "The UUID of the resource type.";
        public static final String DEPLOY_SEQ_DESC = "A number (starting at 1) representing at which step the " +
                "resource deployment should occur.";
        public static final String BR_UUID_DESC = "The list of base resource UUIDs from uCMDB.";
        public static final String BR_CITYPE_DESC = "The list of uCMDB ciTypes of the base resources.";
        public static final String BR_TYPE_UUID_DESC = "The list of resource type UUIDs for the base resources.";
        public static final String DP_NAME_DESC = "List of deployment parameter names.";
        public static final String DP_VALUE_DESC = "List of deployment parameter values.";

        public static final String RETURN_RESULT_DESC = "A JSON representation of a DCA Resource, to use in the " +
                "Deploy Template Operation.";
    }

    public static class GetCredentialFromManager {
        public static final String GET_CREDENTIAL_FROM_MANAGER_DESC = "This operation can be used to retrieve a " +
                "credential from DCA credential manager.";

        public static final String CM_HOST_DESC = "The hostname of the DCA Credential Manager container.";
        public static final String CM_PORT_DESC = "The port of the DCA Credential Manager container.";
        public static final String PROTOCOL_DESC = "The protocol to use (HTTP, HTTPS) to connect to the DCA " +
                "Credential Manager.";
        public static final String CREDENTIAL_UUID_DESC = "The UUID of the credential for which to retrieve the " +
                "information.";
        public static final String USERNAME_DESC = "The username of the credential, empty if not found.";
        public static final String PASSWORD_DESC = "The password of the credential, empty if not found.";
        public static final String RETURN_RESULT_DESC = "JSON representation of the credential data.";
    }
}
