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

public class Constants {
    public static class Common {
        public static final String API = "/api";
        public static final String API_VERSION = "/v2";
        public static final String NEW_LINE = "\n";
        public static final String DEFAULT_PROXY_PORT = "8080";
        public static final String BOOLEAN_FALSE = "false";
        public static final String BOOLEAN_TRUE = "true";
        public static final String STRICT = "strict";
        public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
        public static final String EXCEPTION_INVALID_PROXY = "The %s is not a valid proxy details.";
        public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
        public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
        public static final String EXCEPTION_INVALID_NAME = "The %s can only contain letters, numbers, underscores, " +
                "and hyphens";
        public static final String ANONYMOUS = "anonymous";
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PATCH = "PATCH";
        public static final String DELETE = "DELETE";
        public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
        public static final String CHANGEIT = "changeit";
        public static final String ZERO = "0";
        public static final String CONNECT_TIMEOUT_CONST = "10000";
        public static final String UTF8 = "UTF-8";
        public static final String CONNECTIONS_MAX_PER_ROUTE_CONST = "2";
        public static final String CONNECTIONS_MAX_TOTAL_CONST = "20";
        public static final String AUTHORIZATION = "Authorization:";
        public static final String BEARER = "Bearer ";
        public static final String TERRAFORM_HOST = "app.terraform.io";
        public static final String TERRAFORM_VERSION_CONSTANT = "0.12.1";
        public static final String ORGANIZATION_PATH = "/organizations/";
        public static final String PATH_SEPARATOR = "/";
        public static final String AND = "&";
        public static final String QUERY = "?";
        public static final String ID = "id";
        public static final String HTTPS = "https";
        public static final String STATUS_CODE = "statusCode";
        public static final String APPLICATION_VND_API_JSON = "application/vnd.api+json";
        public static final String DELIMITER = ",";
        public static final String DEFAULT_PAGE_NUMBER = "1";
        public static final String DEFAULT_PAGE_SIZE = "100";
        public static final String PAGE_NUMBER = "page[number]=";
        public static final String PAGE_SIZE = "page[size]=";
        public static final String SESSION_TIMEOUT = "20160";
        public static final String SESSION_REMEMBER = "20160";
        public static final String COLLABORATOR_AUTH_POLICY = "password";
        public static final String ALLOWED_CYPHERS = "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256," +
                "TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384," +
                "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256," +
                "TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA256";

    }

    public static class CreateWorkspaceConstants {
        public static final String CREATE_WORKSPACE_OPERATION_NAME = "Create Workspace";
        public static final String WORKSPACE_PATH = "/workspaces";
        public static final String WORKSPACE_ID_JSON_PATH = "$.data.id";
        public static final String WORKSPACE_TYPE = "workspaces";
    }

    public static class CreateOrganizationConstants {
        public static final String CREATE_ORGANIZATION_OPERATION_NAME = "Create Organization";
        public static final String ORGANIZATION_ID_JSON_PATH = "$.data.id";
        public static final String ORGANIZATION_TYPE = "organizations";
    }

    public static class UpdateOrganizationConstants {
        public static final String UPDATE_ORGANIZATION_OPERATION_NAME = "Update Organization";
    }

    public static class ListOAuthClientConstants {
        public static final String LIST_OAUTH_CLIENT_OPERATION_NAME = "List OAuth Client";
        public static final String OAUTH_CLIENT_PATH = "/oauth-clients";
        public static final String OAUTH_TOKEN_LIST_JSON_PATH = "$.data[*].relationships.oauth-tokens.data[*].id";

    }

    public static class CreateRunConstants {
        public static final String CREATE_RUN_OPERATION_NAME = "Create Run";
        public static final String RUN_PATH = "/runs";
        public static final String RUN_TYPE = "runs";
        public static final String RUN_ID_PATH = "data.id";

    }

    public static class ApplyRunConstants {
        public static final String APPLY_RUN_OPERATION_NAME = "Apply Run";
        public static final String APPLY_RUN_PATH = "/actions/apply";
    }


    public static class ListRunsInWorkspaceConstants {
        public static final String LIST_RUNS_IN_WORKSPACE_OPERATION_NAME = "List Runs in a Workspace";
    }

    public static class CreateVariableConstants {
        public static final String CREATE_VARIABLE_OPERATION_NAME = "Create Variable";
        public static final String CREATE_VARIABLES_OPERATION_NAME = "Create Variables";
        public static final String VARIABLE_PATH = "/vars";
        public static final String VARIABLE_TYPE = "vars";
        public static final String VARIABLE_ID_JSON_PATH = "$.data.id";
    }

    public static class CreateWorkspaceVariableConstants {
        public static final String CREATE_WORKSPACE_VARIABLE_OPERATION_NAME = "Create Workspace Variable";
        public static final String CREATE_WORKSPACE_VARIABLES_OPERATION_NAME = "Create Workspace Variables";
        public static final String WORKSPACE_VARIABLE_PATH = "/vars";
        public static final String WORKSPACE_VARIABLE_TYPE = "vars";
        public static final String WORKSPACE_VARIABLE_ID_JSON_PATH = "$.data.id";
    }

    public static class ListVariableConstants {
        public static final String LIST_VARIABLE_OPERATION_NAME = "List Variable";
        public static final String ORGANIZATION_NAME = "filter[organization][name]=";
        public static final String WORKSPACE_NAME = "filter[workspace][name]=";
    }

    public static class ListWorkspaceVariableConstants {
        public static final String LIST_WORKSPACE_VARIABLE_OPERATION_NAME = "List Workspace Variable";
    }


    public static class UpdateVariableConstants {
        public static final String UPDATE_VARIABLE_OPERATION_NAME = "Update Variable";
        public static final String UPDATE_VARIABLES_OPERATION_NAME = "Update Variables";
        public static final String VARIABLE_KEY_JSON_PATH = "attributes.key";
    }

    public static class UpdateWorkspaceVariableConstants {
        public static final String UPDATE_WORKSPACE_VARIABLE_OPERATION_NAME = "Update Workspace Variable";
        public static final String UPDATE_WORKSPACE_VARIABLES_OPERATION_NAME = "Update Workspace Variables";
    }

    public static class DeleteVariableConstants {
        public static final String DELETE_VARIABLE_OPERATION_NAME = "Delete Variable";
    }

    public static class DeleteWorkspaceVariableConstants {
        public static final String DELETE_WORKSPACE_VARIABLE_OPERATION_NAME = "Delete Workspace Variable";
    }


    public static class GetWorkspaceDetailsConstants {
        public static final String GET_WORKSPACE_DETAILS_OPERATION_NAME = "Get Workspace Details";
    }

    public static class GetOrganizationDetailsConstants {
        public static final String GET_ORGANIZATION_DETAILS_OPERATION_NAME = "Get Organization Details";
    }


    public static class GetRunDetailsConstants {
        public static final String GET_RUN_OPERATION_NAME = "Get Run Details";
    }

    public static class PlanDetailsConstants {
        public static final String PLAN_DETAILS_OPERATION_NAME = "Get Plan Details";
        public static final String PLAN_DETAILS_PATH = "/plans";
    }

    public static class CancelRunConstants {
        public static final String CANCEL_RUN_OPERATION_NAME = "Cancel Run";
        public static final String CANCEL_RUN_PATH = "/actions/cancel";
    }


    public static class GetApplyDetailsConstants {
        public static final String GET_APPLY_DETAILS_OPERATION_NAME = "Get Apply Details";
        public static final String APPLY_DETAILS_PATH = "/applies";
    }

    public static class DeleteWorkspaceConstants {
        public static final String DELETE_WORKSPACE_OPERATION_NAME = "Delete Workspace";
    }

    public static class DeleteOrganizationConstants {
        public static final String DELETE_ORGANIZATION_OPERATION_NAME = "Delete Organization";
    }

    public static class ListWorkspacesConstants {
        public static final String LIST_WORKSPACES_OPERATION_NAME = "List Workspaces";
        public static final String WORKSPACES_LIST_JSON_PATH = "$.data[*].attributes.name";
    }

    public static class ListOrganizationsConstants {
        public static final String LIST_ORGANIZATIONS_OPERATION_NAME = "List Organizations";
        public static final String ORGANIZATIONS_LIST_JSON_PATH = "$.data[*].attributes.name";
    }


    public static class GetCurrentStateVersionConstants {
        public static final String GET_CURRENT_STATE_VERSION_OPERATION_NAME = "Get Current State Version";
        public static final String CURRENT_STATE_VERSION_PATH = "/current-state-version";
        public static final String STATE_VERSION_ID_JSON_PATH = "$.data.id";
        public static final String HOSTED_STATE_DOWNLOAD_URL_JSON_PATH = "$.data.attributes.hosted-state-download-url";
    }
    public static class CounterConstants{
        public static final String RESULT_STRING="resultString";
        public static final String INCREMENT_BY_DEFAULT_VALUE="1";
        public static final String RESULT="result";
        public static final String HASMORE="has more";
        public static final String NOMORE="no more";
        public static final String FAILURE="failure";
        public static final String EXCEPTION="exception";
        public static final String COUNTER_OPERATION_NAME="Counter";
        public static final String TO="to";
        public static final String FROM="from";
        public static final String INCREMENT_BY="incrementBy";
        public static final String SESSION_COUNTER="sessionCounter";
        public static final String RESET="reset";


    }
}
