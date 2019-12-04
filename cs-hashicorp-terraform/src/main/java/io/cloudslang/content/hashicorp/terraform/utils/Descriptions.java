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
                "A certificate is trusted even if no trusted certification authority issued it." +
                "Default: false";
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
        public static final String CONN_MAX_TOTAL_DESC = "The maximum limit of connections in total." +
                "Default: 20";
        public static final String CONN_MAX_ROUTE_DESC = "The maximum limit of connections on a per route basis." +
                "Default: 2";
        public static final String KEEP_ALIVE_DESC = "Specifies whether to create a shared connection that will be " +
                "used in subsequent calls. If keepAlive is false, the already open connection will be used and after" +
                " execution it will close it." +
                "Default: true";
        public static final String SOCKET_TIMEOUT_DESC = "The timeout for waiting for data (a maximum period " +
                "inactivity between two consecutive data packets), in seconds. A socketTimeout value of '0' " +
                "represents an infinite timeout.";
        public static final String POLLING_INTERVAL_DESC = "The time, in seconds, to wait before a new request that verifies if the operation finished\n" +
                "is executed." +
                "Default: '1000'";
        public static final String CONNECT_TIMEOUT_DESC = "The time to wait for a connection to be established, " +
                "in seconds. A timeout value of '0' represents an infinite timeout." +
                "Default: 10000";
        public static final String RESPONSE_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP response. " +
                "If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header will be used. " +
                "If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty, the " +
                "default value will be used. You should not use this for method=HEAD or OPTIONS." +
                "Default: UTF-8";
        public static final String AUTH_TOKEN_DESC = "The authorization token for terraform";
        public static final String ORGANIZATION_NAME_DESC = "The name of the organization";
        public static final String TERAAFORM_VERSION_DESC = "The version of Terraform to use for this workspace. Upon creating a workspace," +
                " the latest version is selected unless otherwise specified (e.g. \"0.11.1\")." +
                "Default: 0.12.1";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXECUTION_TIMEOUT_DESC = "The amount of time (in milliseconds) to allow the client to complete the execution " +
                "of an API call. A value of '0' disables this feature." +
                "Default: '60000'";
        public static final String ASYNC_DESC = "Whether to run the operation is async mode." +
                "Default: 'false'";
        public static final String STATUS_CODE_DESC = "The HTTP status code for Terraform API request.";
    }


    public static class ListOAuthClient {
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response containing the messages.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the request.";
        public static final String FAILURE_DESC = "There was an error while trying to get the messages.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
        public static final String MESSAGE_ID_LIST_DESC = "A comma-separated list of message IDs from the retrieved document.";
        public static final String DOCUMENT_DESC = "The full API response in case of success.";
        public static final String OAUTH_TOKEN_ID_DESCRIPTION="The VCS Connection (OAuth Connection + Token) to use. " +
                "This ID can be obtained from the oauth-tokens endpoint.";
    }

    public static class CreateWorkspace {
        public static final String CREATE_WORKSPACE_DESC = "Creates a workspace which represent running infrastructure managed by Terraform.";
        public static final String WORKSPACE_NAME_DESC = "The name of the workspace, which can only include letters, numbers, -, and _. " +
                "This will be used as an identifier and must be unique in the organization.";
        public static final String WORKSPACE_DESCRIPTION_DESC = "A description of the workspace to be created.";
        public static final String AUTO_APPLY_DESC = "Whether to automatically apply changes when a Terraform plan is successful, with some exceptions."+
                "Default: false";
        public static final String FILE_TRIGGERS_ENABLED_DESC = "Whether to filter runs based on the changed files in a VCS push. " +
                "If enabled, the working-directory and trigger-prefixes describe a set of paths which must contain changes for a VCS" +
                " push to trigger a run. If disabled, any push will trigger a run." +
                "Default: true";
        public static final String WORKING_DIRECTORY_DESC = "A relative path that Terraform will execute within. This defaults to the root of your repository" +
                " and is typically set to a subdirectory matching the environment when multiple environments exist within the same repository.";
        public static final String TRIGGER_PREFIXES_DESC = "List of repository-root-relative paths which should be tracked for changes, in addition to the working directory.";
        public static final String QUEUE_ALL_RUNS_DESC = "Whether runs should be queued immediately after workspace creation. When set to false, runs triggered by a VCS change " +
                "will not be queued until at least one run is manually queued." +
                "Default: false";
        public static final String SPECULATIVE_ENABLED_DESC = "Whether this workspace allows speculative plans. Setting this to false prevents Terraform Cloud from running plans " +
                "on pull requests, which can improve security if the VCS repository is public or includes untrusted contributors." +
                "Default: true";
        public static final String INGRESS_SUBMODULES_DESC = "Whether submodules should be fetched when cloning the VCS repository." +
                "Default: false";
        public static final String VCS_BRANCH_NAME_DESC = "The repository branch that Terraform will execute from. " +
                "If omitted or submitted as an empty string, this defaults to the repository's default branch (e.g. master) .";
        public static final String VCS_REPO_ID_DESC = "A reference to your VCS repository in the format :org/:repo where :org and :repo refer to the organization and " +
                "repository in your VCS provider.";
        public static final String WORKSPACE_REQUEST_BODY_DESC = "The request body of the workspace.";
        public static final String WORKSPACE_ID_DESC ="The Id of created workspace";
        public static final String CREATE_WORKSPACE_RETURN_RESULT_DESC = "The response of the workspace";
        public static final String CREATE_WORKSPACE_EXCEPTION_DESC = "An error message in case there was an error while creating the workspace.";
        public static final String FAILURE_DESC = "There was an error while creating workspace.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";

    }

    public static class CreateRun{
        public static  final String RUN_MESSAGE = "Specifies the message to be associated with this run";
        public static  final String IS_DESTROY = "Specifies if this plan is a destroy plan, which will destroy all provisioned resources.";


    }

}
