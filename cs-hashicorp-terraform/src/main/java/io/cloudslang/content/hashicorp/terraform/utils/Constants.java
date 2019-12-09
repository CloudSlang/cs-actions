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

public class Constants {
    public static class Common {
        public static final String API = "/api";
        public static final String API_VERSION = "/v2";
        public static final String NATIVE = "native";
        public static final String NEW_LINE = "\n";
        public static final String DEFAULT_PROXY_PORT = "8080";
        public static final String BOOLEAN_FALSE = "false";
        public static final String BOOLEAN_TRUE = "true";
        public static final boolean TRUE = true;
        public static final String STRICT = "strict";
        public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
        public static final String EXCEPTION_INVALID_PROXY = "The %s is not a valid proxy details.";
        public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
        public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
        public static final String EXCEPTION_EMPTY_FILE_PATH_AND_CONTENT_BYTES = "The filePath or both contentName and contentBytes inputs are required.";
        public static final String ANONYMOUS = "anonymous";
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PATCH = "PATCH";
        public static final String DELETE = "DELETE";
        public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
        public static final String CHANGEIT = "changeit";
        public static final String ZERO = "0";
        public static final String CONNECT_TIMEOUT_CONST = "10000";
        public static final String POLLING_INTERVAL_DEFAULT = "1000";
        public static final String EXEC_TIMEOUT = "600000";
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
        public static final String HTTPS = "https";
        public static final String CONTENT_LENGTH = "Content-Length:0";
        public static final String HEADERS_DELIMITER = "\r\n";
        public static final String STATUS_CODE = "statusCode";
        public static final String APPLICATION_VND_API_JSON = "application/vnd.api+json";
        public static final String DEFAULT_IMPORTANCE = "low";
        public static final String DEFAULT_INFERENCE_CLASSIFICATION = "other";
        public static final String DELIMITER = ",";
        public static final String CONTENT_TYPE = "HTML";
        public static final String ID = "id";
        public static final String COMMA = ",";
        public static final String NAME = "name";
        public static final String SIZE = "size";
        public static final String PASSWORD_BODY = "password";
        public static final String VALUE = "value";
        public static final String DEFAULT_PAGE_NUMBER = "1";
        public static final String DEFAULT_PAGE_SIZE = "100";
        public static final String PAGE_NUMBER = "page[number]=";
        public static final String PAGE_SIZE = "page[size]=";
    }

    public static class CreateWorkspace {
        public static final String CREATE_WORKSPACE_OPERATION_NAME = "Create Workspace";
        public static final String WORKSPACE_PATH = "/workspaces";
        public static final String WORKSPACE_ID_JSON_PATH = "$.data.id";
        public static final String WORKSPACE_TYPE = "workspaces";
    }

    public static class ListOAuthClientConstants {
        public static final String LIST_OAUTH_CLIENT_OPERATION_NAME = "List OAuth Client";
        public static final String OAUTH_CLIENT_PATH = "/oauth-clients";
        public static final String OAUTH_TOKEN_LIST_JSON_PATH = "$.data[*].relationships.oauth-tokens.data[*].id";

    }

    public static class CreateRunConstants {
        public static final String CREATE_RUN_OPERATION_NAME = "Create Run";
        public static final String CREATE_RUN_PATH = "/runs";
        public static final String RUN_TYPE = "runs";
        public static final String RUN_ID_PATH = "$.data[*].relationships.run-events.data[*].id";

    }

    public static class ApplyRunConstants {
        public static final String APPLY_RUN_OPERATION_NAME = "Apply Run";
        public static final String RUN_PATH = "/runs/";
        public static final String APPLY_RUN_PATH = "/actions/apply";
    }

    public static class ListRunsInWorkspaceConstants {
        public static final String LIST_RUNS_IN_WORKSPACE_OPERATION_NAME = "List Runs in a Workspace";
    }

    public static class CreateVariableConstants {
        public static final String CREATE_VARIABLE_OPERATION_NAME = "Create Variable";
        public static final String VARIABLE_PATH = "/vars";
        public static final String VARIABLE_TYPE = "vars";
        public static final String VARIABLE_ID_JSON_PATH = "$.data.id";

    }

    public static class GetWorkspaceDetails {
        public static final String GET_WORKSPACE_DETAILS_OPERATION_NAME = "Get Workspace Details";
        public static final String GET_WORKSPACE_PATH = "/workspaces/";
        public static final String WORKSPACE_ID_JSON_PATH = "data.id";
    }

    public static class GetRunDetailsConstants {
        public static final String GET_RUN_OPERATION_NAME = "Get Run Details";
        public static final String GET_RUN_DETAILS_PATH = "/runs/";

    }
}
