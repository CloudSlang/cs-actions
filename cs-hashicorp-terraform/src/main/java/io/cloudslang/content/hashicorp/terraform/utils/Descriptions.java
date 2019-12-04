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
        public static final String EXECUTION_TIMEOUT_DESC = "The amount of time (in milliseconds) to allow the client to complete the execution of an API call. A value of '0' disables this feature";
        public static final String ASYN_DESC = "Whether to run the operation is async mode";
        public static final String POLLING_INTERVAL_DESC = "The time, in seconds, to wait before a new request that verifies if the operation finished is executed.";
        public static final String RESPONSE_CHARACTER_SET_DESC = "The character encoding to be used for the HTTP response. " +
                "If responseCharacterSet is empty, the charset from the 'Content-Type' HTTP response header will be used. " +
                "If responseCharacterSet is empty and the charset from the HTTP response Content-Type header is empty, the " +
                "default value will be used. You should not use this for method=HEAD or OPTIONS." +
                "Default value: UTF-8";
        public static final String AUTH_TOKEN_DESC = "The authorization token for terraform";
        public static final String ORGANIZATION_NAME_DESC = "Name of the organization";
        public static final String TERAAFORM_VERSION_DESC = "Terraform version.";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String STATUS_CODE_DESC = "The HTTP status code for Terraform API request.";

    }


    public static class ListOAuthClient {
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response containing the messages.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the request.";
        public static final String FAILURE_DESC = "There was an error while trying to get the messages.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
        public static final String MESSAGE_ID_LIST_DESC = "A comma-separated list of message IDs from the retrieved document.";
        public static final String DOCUMENT_DESC = "The full API response in case of success.";
        public static final String OAUTH_TOKEN_ID_DESCRIPTION = "Id of the oauthtoken";
        public static final String LIST_OAUTH_CLIENT_DESC = "List An OAuth Client Id";
    }

    public static class CreateWorkspace {
        public static final String CREATE_WORKSPACE_DESC = "Create a workspace in an organization.";
        public static final String WORKSPACE_NAME_DESC = "The name of the workspace, which can only include letters, numbers, -, and _. " +
                "This will be used as an identifier and must be unique in the organization.";
        public static final String WORKSPACE_DESCRIPTION_DESC = "The description of workspace to be created.";
        public static final String VCS_BRANCH_NAME_DESC = "VCS Branch name for given repo.";
        public static final String VCS_DEFAULT_BRANCH_DESC = "This repesents the VCS repo branch is default or not.";
        public static final String VCS_REPO_ID_DESC = "The ID of VCS repository +" +
                "example : username/repo_name";
        public static final String WORKSPACE_REQUEST_BODY_DESC = "The request body of the workspace.";
        public static final String WORKSPACE_ID_DESC = "The Id of created workspace";
        public static final String CREATE_WORKSPACE_RETURN_RESULT_DESC = "The response of the workspace";
        public static final String CREATE_WORKSPACE_EXCEPTION_DESC = "An error message in case there was an error while creating the message.";
        public static final String FAILURE_DESC = "There was an error while creating workspace.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";

    }


    public static class ApplyRun {
        public static final String APPLY_RUN_DESC = "Applies a run that is paused waiting for confirmation after a plan. This includes runs in the \"needs confirmation\" and \"policy checked\" states. This action is only required for runs that can't be auto-applied.";
        public static final String RUN_COMMENT_DESC = "Specifies the comment to be associated with this run";
        public static final String RUN_DESC = "Specifies the run";
        public static final String APPLY_RUN_RETURN_RESULT_DESC = "The response of the apply run.";
        public static final String APPLY_RUN_EXCEPTION_DESC = "An error message in case there was an error while apply run.";
        public static final String APPLY_RUN_REQUEST_BODY_DESC = "Request Body for the apply run.";

    }

    public static class CreateVariable {
        public static final String CREATE_VARIABLE_DESC = "Creates a variable in workspace.";
        public static final String VARIABLE_NAME_DESC = "The name of the variable.";
        public static final String VARIABLE_VALUE_DESC = "The value of the variable.";
        public static final String VARIABLE_CATEGORY_DESC = "Whether this is a Terraform or environment variable. Valid values are \"terraform\" or \"env\".";
        public static final String HCL_DESC = "Whether to evaluate the value of the variable as a string of HCL code. Has no effect for environment variables.";
        public static final String SENSITIVE_DESC = "Whether the value is sensitive. If true then the variable is written once and not visible thereafter.";
        public static final String VARIABLE_REQUEST_BODY_DESC = "Request Body for the Create Variable.";
        public static final String VARIABLE_ID_DESC = "The Id of created variable.";
        public static final String CREATE_VARIABLE_EXCEPTION_DESC = "An error message in case there was an error while creating the variable.";
        public static final String CREATE_VARIABLE_RETURN_RESULT_DESC = "The response of the apply run.";
    }
  
    public static class CreateRun{
        public static final String CREATE_RUN_DESC = "Creates a run in workspace.";
        public static final String CREATE_RUN_REQUEST_BODY_DESC = "The request body of the crate run.";
        public static  final String RUN_MESSAGE_DESC = "Specifies the message to be associated with this run";
        public static  final String IS_DESTROY_DESC = "Specifies if this plan is a destroy plan, which will destroy all provisioned resources.";
        public static final String RUN_ID_DESC="Id of the run.";


    }

}
