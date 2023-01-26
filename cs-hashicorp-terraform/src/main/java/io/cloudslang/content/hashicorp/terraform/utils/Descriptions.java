/*
 * (c) Copyright 2022 Micro Focus, L.P.
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
        public static final String AUTH_TOKEN_DESC = "The authorization token for terraform.";
        public static final String ORGANIZATION_NAME_DESC = "The name of the organization.";
        public static final String TERRAFORM_VERSION_DESC = "The version of Terraform to use for this workspace. " +
                "Upon creating a workspace,the latest version is selected unless otherwise specified (e.g. \"0.11.1\")." +
                "Default: '0.12.1'";
        public static final String RETURN_CODE_DESC = "0 if success, -1 otherwise.";
        public static final String EXECUTION_TIMEOUT_DESC = "The amount of time (in milliseconds) to allow the client " +
                "to complete the execution of an API call. A value of '0' disables this feature." +
                "Default: '60000'";
        public static final String ASYNC_DESC = "Whether to run the operation is async mode." +
                "Default: 'false'";
        public static final String STATUS_CODE_DESC = "The HTTP status code for Terraform API request.";
        public static final String PAGE_NUMBER_DESC = "Optional. If omitted, the endpoint will return the first page.";
        public static final String PAGE_SIZE_DESC = "Optional. If omitted, the endpoint will return 20 items per page. " +
                "The maximum page size is 150.";
        public static final String EXCEPTION_DESC = "An error message in case there was an error while executing the request.";
        public static final String FAILURE_DESC = "There was an error while executing the request.";
        public static final String SUCCESS_DESC = "The request was successfully executed.";
        public static final String RETURN_RESULT_DESC = "If successful, returns the complete API response. In case of an error this output will contain the error message.";
    }


    public static class ListOAuthClient {
        public static final String DOCUMENT_DESC = "The full API response in case of success.";
        public static final String OAUTH_TOKEN_ID_DESCRIPTION = "The VCS Connection (OAuth Connection + Token) to use. " +
                "This ID can be obtained from the oauth-tokens endpoint.";
        public static final String LIST_OAUTH_CLIENT_DESC = "List An OAuth Client Id";
    }

    public static class CreateWorkspace {
        public static final String CREATE_WORKSPACE_DESC = "Creates a workspace which represent running infrastructure " +
                "managed by Terraform.";
        public static final String WORKSPACE_NAME_DESC = "The name of the workspace, which can only include letters, " +
                "numbers, -, and _.This will be used as an identifier and must be unique in the organization.";
        public static final String WORKSPACE_DESCRIPTION_DESC = "A description of the workspace to be created.";
        public static final String AUTO_APPLY_DESC = "Whether to automatically apply changes when a Terraform plan " +
                "is successful, with some exceptions." +
                "Default: 'false'";
        public static final String FILE_TRIGGERS_ENABLED_DESC = "Whether to filter runs based on the changed files " +
                "in a VCS push. If enabled, the working-directory and trigger-prefixes describe a set of paths which " +
                "must contain changes for a VCS push to trigger a run. If disabled, any push will trigger a run." +
                "Default: 'true'";
        public static final String WORKING_DIRECTORY_DESC = "A relative path that Terraform will execute within. " +
                "This defaults to the root of your repository and is typically set to a subdirectory matching the " +
                "environment when multiple environments exist within the same repository.";
        public static final String TRIGGER_PREFIXES_DESC = "List of repository-root-relative paths which should be " +
                "tracked for changes, in addition to the working directory.";
        public static final String QUEUE_ALL_RUNS_DESC = "Whether runs should be queued immediately after workspace " +
                "creation. When set to false, runs triggered by a VCS change will not be queued until at least " +
                "one run is manually queued." +
                "Default: false";
        public static final String SPECULATIVE_ENABLED_DESC = "Whether this workspace allows speculative plans. " +
                "Setting this to false prevents Terraform Cloud from running plans on pull requests, which can " +
                "improve security if the VCS repository is public or includes untrusted contributors." +
                "Default: 'true'";
        public static final String INGRESS_SUBMODULES_DESC = "Whether submodules should be fetched when cloning the " +
                "VCS repository." +
                "Default: 'false'";
        public static final String VCS_BRANCH_NAME_DESC = "The repository branch that Terraform will execute from. " +
                "If omitted or submitted as an empty string, this defaults to the repository's default branch " +
                "(e.g. master) .";
        public static final String VCS_REPO_ID_DESC = "A reference to your VCS repository in the format :org/:repo " +
                "where :org and :repo refer to the organization and " +
                "repository in your VCS provider.";
        public static final String WORKSPACE_REQUEST_BODY_DESC = "The request body of the workspace.";
        public static final String WORKSPACE_ID_DESC = "The Id of the workspace";
    }

    public static class CreateOrganization {
        public static final String CREATE_ORGANIZATION_DESC = "Creates an organization which is " +
                "managed by Terraform.";
        public static final String ORGANIZATION_DESCRIPTION_DESC = "A description of the organization to be created.";
        public static final String ORGANIZATION_REQUEST_BODY_DESC = "The request body of the organization.";
        public static final String ORGANIZATION_ID_DESC = "The Id of the organization";
        public static final String EMAIL_DESC = "The email ID of the Admin";
        public static final String SESSION_TIMEOUT_DESC = "Session timeout after inactivity in minutes";
        public static final String SESSION_REMEMBER_DESC = "Session expiration in minutes";
        public static final String COLLABORATOR_AUTH_POLICY_DESC = "Authentication policy (password or two_factor_mandatory)";
        public static final String COST_ESTIMATION_ENABLED_DESC = "Whether or not the cost estimation feature is enabled for all workspaces in the organization";
        public static final String OWNERS_TEAM_SAML_ID_DESC = "Optional. SAML only The name of the owners team";
    }

    public static class CreateRun {
        public static final String CREATE_RUN_DESC = "Creates a run in workspace.";
        public static final String CREATE_RUN_REQUEST_BODY_DESC = "The request body of the crate run.";
        public static final String RUN_MESSAGE_DESC = "Specifies the message to be associated with this run";
        public static final String IS_DESTROY_DESC = "Specifies if this plan is a destroy plan, which will destroy " +
                "all provisioned resources.";
        public static final String RUN_ID_DESC = "Id of the run.";
    }

    public static class ListRunsInWorkspace {
        public static final String LIST_RUNS_IN_WORKSPACE_DESC = "Lists the runs in a workspace.";
    }

    public static class ApplyRun {
        public static final String APPLY_RUN_DESC = "Applies a run that is paused waiting for confirmation after a " +
                "plan. This includes runs in the \"needs confirmation\" and \"policy checked\" states. This action is " +
                "only required for runs that can't be auto-applied.";
        public static final String RUN_COMMENT_DESC = "Specifies the comment to be associated with this run";
        public static final String RUN_DESC = "Specifies the run";
        public static final String APPLY_RUN_REQUEST_BODY_DESC = "Request Body for the apply run.";
    }

    public static class CreateVariable {
        public static final String CREATE_VARIABLE_DESC = "Create either a sensitive or a non-sensitive variable in a given workspace.";
        public static final String CREATE_VARIABLES_DESC = "Creates  multiple sensitive and non-sensitive variables or both in a workspace.";
        public static final String VARIABLE_NAME_DESC = "The name of the variable.";
        public static final String VARIABLE_VALUE_DESC = "The value of the variable.";
        public static final String SENSITIVE_VARIABLE_NAME_DESC = "The name of the sensitive variable.";
        public static final String SENSITIVE_VARIABLE_VALUE_DESC = "The value of the sensitive variable.";
        public static final String VARIABLE_CATEGORY_DESC = "Whether this is a Terraform or environment variable. Valid " +
                "values are \"terraform\" or \"env\".";
        public static final String HCL_DESC = "Whether to evaluate the value of the variable as a string of HCL code." +
                " Has no effect for environment variables.";
        public static final String SENSITIVE_DESC = "Whether the value is sensitive. If true then the variable is " +
                "written once and not visible thereafter.";

        public static final String VARIABLE_REQUEST_BODY_DESC = "Request Body for the Create Variable.";
        public static final String VARIABLE_SENSITIVE_REQUEST_BODY_DESC = "Request Body for the Create Sensitive Variable.";
        public static final String VARIABLES_JSON_DESC = "List of variables in json format." +
                                        "Examples : [{\"propertyName\":\"test1\",\"propertyValue\":\"1\",\"HCL\":false,\"Category\":\"terraform\"},{\"propertyName\":\"test2\",\"propertyValue\":\"1\",\"HCL\":false,\"Category\":\"env\"}]";
        public static final String SENSITIVE_VARIABLES_JSON_DESC = "List of sensitive variables in json format.";
        public static final String VARIABLE_ID_DESC = "The Id of created variable.";
        public static final String CREATE_VARIABLE_EXCEPTION_DESC = "An error message in case there was an error while " +
                "creating the variable.";
        public static final String CREATE_VARIABLE_RETURN_RESULT_DESC = "The response of the apply run.";
    }

    public static class CreateWorkspaceVariable {
        public static final String CREATE_WORKSPACE_VARIABLE_DESC = "Create either a sensitive or a non-sensitive variable in a given workspace.";
        public static final String CREATE_WORKSPACE_VARIABLES_DESC = "Creates  multiple sensitive and non-sensitive variables or both in a workspace.";
        public static final String WORKSPACE_VARIABLE_NAME_DESC = "The name of the workspace variable.";
        public static final String WORKSPACE_VARIABLE_VALUE_DESC = "The value of the workspace variable.";
        public static final String SENSITIVE_WORKSPACE_VARIABLE_NAME_DESC = "The name of the sensitive workspace variable.";
        public static final String SENSITIVE_WORKSPACE_VARIABLE_VALUE_DESC = "The value of the sensitive workspace variable.";
        public static final String WORKSPACE_VARIABLE_CATEGORY_DESC = "Whether this is a Terraform or environment workspace variable. Valid " +
                "values are \"terraform\" or \"env\".";
        public static final String HCL_DESC = "Whether to evaluate the value of the variable as a string of HCL code." +
                " Has no effect for environment variables.";
        public static final String SENSITIVE_DESC = "Whether the value is sensitive. If true then the variable is " +
                "written once and not visible thereafter.";

        public static final String WORKSPACE_VARIABLE_REQUEST_BODY_DESC = "Request Body for the Create Workspace Variable.";
        public static final String WORKSPACE_VARIABLE_SENSITIVE_REQUEST_BODY_DESC = "Request Body for the Create Workspace Sensitive Variable.";
        public static final String WORKSPACE_VARIABLES_JSON_DESC = "List of workspace variables in json format." +
                "Examples : [{\"propertyName\":\"test1\",\"propertyValue\":\"1\",\"HCL\":false,\"Category\":\"terraform\"},{\"propertyName\":\"test2\",\"propertyValue\":\"1\",\"HCL\":false,\"Category\":\"env\"}]";
        public static final String SENSITIVE_WORKSPACE_VARIABLES_JSON_DESC = "List of sensitive workspace variables in json format.";
        public static final String WORKSPACE_VARIABLE_ID_DESC = "The Id of created workspace variable.";
        public static final String CREATE_WORKSPACE_VARIABLE_EXCEPTION_DESC = "An error message in case there was an error while " +
                "creating the workspace variable.";
        public static final String CREATE_WORKSPACE_VARIABLE_RETURN_RESULT_DESC = "The response of the apply run.";
    }


    public static class ListVariables {
        public static final String LIST_VARIABLE_DESC = "List all the variables in a workspace.";
    }

    public static class ListWorkspaceVariables {
        public static final String LIST_WORKSPACE_VARIABLE_DESC = "List all the variables in a workspace.";
    }

    public static class GetWorkspaceDetails {
        public static final String GET_WORKSPACE_DETAILS_DESC = "Get details of workspace in an organization.";
        public static final String WORKSPACE_NAME_DESC = "The name of workspace whose description is to be fetched.";
        public static final String WORKSPACE_ID_DESC = "The Id of the workspace";
    }

    public static class GetOrganizationDetails {
        public static final String GET_ORGANIZATION_DETAILS_DESC = "Get details of the organization.";
        public static final String ORGANIZATION_ID_DESC = "The Id of the organization";
    }

    public static class GetRunDetails {
        public static final String GET_RUN_DETAILS_DESC = "Getting details about a run";
    }

  public static class PlanDetails {
        public static final String PLAN_DETAILS_DESC = "Getting details about plan";
    }
  
    public static class CancelRun {
        public static final String CANCEL_RUN_DESC = "Cancel the run";
        public static final String CANCEL_RUN_REQUEST_BODY_DESC = "Request Body for the cancel run.";
        public static final String CANCEL_RUN_SUCCESS_DESC = "The run is cancelled successfully.";
    }

    public static class GetApplyDetails {
        public static final String GET_APPLY_DETAILS_DESC = "Getting details about a apply id";
        public static final String APPLY_ID_DESC = "The ID of the apply to show";

    }

    public static class DeleteWorkspace {
        public static final String DELETE_WORKSPACE_DESC = "Deletes the workspace from an organization using workspace " +
                "name and organization name";
        public static final String DELETE_WORKSPACE_SUCCESS_DESC = "The workspace is deleted successfully.";
    }

    public static class DeleteOrganization{
        public static final String DELETE_ORGANIZATION_DESC = "Deletes the organization";
        public static final String DELETE_ORGANIZATION_SUCCESS_DESC = "The organization is deleted successfully.";
    }

    public static class UpdateOrganization{
        public static final String UPDATE_ORGANIZATION_DESC = "Updates the organization";
    }

    public static class ListWorkspaces {
        public static final String LIST_WORKSPACES_OPERATION_DESC = "List of workspaces present in given Organization";
        public static final String WORKSPACE_LIST_DESC = "List of all workspaces under the organization.";
    }

    public static class ListOrganizations {
        public static final String LIST_ORGANIZATIONS_OPERATION_DESC = "List of organizations present";
        public static final String ORGANIZATION_LIST_DESC = "List of all organizations";
    }

    public static class UpdateVariables {
        public static final String UPDATE_VARIABLES_DESC = "Updates  multiple sensitive and non-sensitive variables or both in a workspace.";
        public static final String UPDATE_VARIABLE_DESC = "Updates  multiple sensitive and non-sensitive variable or both in a workspace.";
    }

    public static class UpdateWorkspaceVariables {
        public static final String WORKSPACE_VARIABLE_REQUEST_BODY_DESC = "Request Body for the Update Workspace Variable.";
        public static final String WORKSPACE_VARIABLES_JSON_DESC = "List of workspace variables in json format." +
                "Examples : [{\"propertyName\":\"test1\",\"propertyValue\":\"1\",\"HCL\":false,\"Category\":\"terraform\"},{\"propertyName\":\"test2\",\"propertyValue\":\"1\",\"HCL\":false,\"Category\":\"env\"}]";
        public static final String UPDATE_WORKSPACE_VARIABLES_DESC = "Updates  multiple sensitive and non-sensitive variables or both in a workspace.";
        public static final String UPDATE_WORKSPACE_VARIABLE_DESC = "Updates  multiple sensitive and non-sensitive variable or both in a workspace.";
    }

    public static class DeleteVariable {
        public static final String DELETE_VAR_SUCCESS_DESC = "The variable deleted successfully.";
        public static final String DELETE_VARIABLE_DESC = "Deletes a variable from workspace.";
    }

    public static class DeleteWorkspaceVariable {
        public static final String DELETE_WORKSPACE_VAR_SUCCESS_DESC = "The workspace variable deleted successfully.";
        public static final String DELETE_WORKSPACE_VARIABLE_DESC = "Deletes a variable from workspace.";
    }


    public static class GetCurrentStateVersion {
        public static final String GET_CURRENT_STATE_VERSION_DESC = "Fetches the current state version for the " +
                "given workspace.";
        public static final String STATE_VERSION_ID_DESC = "The ID of the desired state version.";
        public static final String HOSTED_STATE_DOWNLOAD_URL_DESC = "A url from which you can download the raw state ";
    }

    public static class Counter{
        public static final String FAILURE_MESSAGE = "Something went wrong";
        public static final String COUNTER_DESC = "Counts from one number to another number.";
        public static final String RESULT_STRING_DESC = "The primary result is resultString, Result can also be used. result (All lower case) should not be used as it is the response code.";
        public static final String RESULT_DESC = "If successful, returns the complete API response. In case of an error this output will contain the error message.";
        public static final String FROM_DESC = "The number to start counting at.";
        public static final String TO_DESC = "The number to count to.";
        public static final String RESET_DESC = "If true, then the counter will restart counting from the beginning.";
        public static final String INCREMENT_BY_DESC = "The number to increment by while counting. If unspecified this is 1. If you wanted to count 2,4,6,8 this would be 2.";


    }
}
