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

public final class Constants {
    public static final String API = "api";
    public static final String NATIVE = "native";
    public static final String NEW_LINE = "\n";
    public static final String DEFAULT_LOGIN_TYPE = "API";
    public static final String DEFAULT_RESOURCE = "https://graph.microsoft.com";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String BOOLEAN_FALSE = "false";
    public static final String BOOLEAN_TRUE = "true";
    public static final String STRICT = "strict";
    public static final String EXCEPTION_INVALID_LOGIN_TYPE = "The %s must be either 'API' or 'Native'.";
    public static final String EXCEPTION_INVALID_LOGIN_TYPE_REST = "The %s or %s is required for login.";
    public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    public static final String EXCEPTION_INVALID_PROXY = "The %s is not a valid port.";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
    public static final String ANONYMOUS = "anonymous";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PATCH = "PATCH";
    public static final String DELETE = "DELETE";
    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String CHANGEIT = "changeit";
    public static final String ZERO = "0";
    public static final String UTF8 = "UTF-8";
    public static final String CONNECTIONS_MAX_PER_ROUTE_CONST = "2";
    public static final String CONNECTIONS_MAX_TOTAL_CONST = "20";
    public static final String AUTHORIZATION = "Authorization:";
    public static final String BEARER = "Bearer ";
    public static final String GRAPH_HOST = "graph.microsoft.com";
    public static final String BASE_GRAPH_PATH = "/v1.0/users/";
    public static final String MESSAGES_PATH = "/messages/";
    public static final String MAIL_FOLDERS_PATH = "/mailFolders/";
    public static final String DATA_QUERY = "$select=";
    public static final String HTTPS = "https";
    public static final String CONTENT_LENGTH = "Content-Length:0";
    public static final String HEADERS_DELIMITER = "\r\n";
    public static final String STATUS_CODE = "statusCode";
    public static final String APPLICATION_JSON = "application/json";
    public static final String DEFAULT_IMPORTANCE = "low";
    public static final String DEFAULT_INFERENCE_CLASSIFICATION = "other";
    public static final String DELIMITER = ",";
    public static final String CONTENT_TYPE = "HTML";
    public static final String SEND = "/send";
    public static final String MOVE = "/move";
    public static final String SEND_MESSAGE = "Email sent. For the delivery status please check the Delivery Report Email.";
    public static final String UPDATE_USER = "User updated successfully.";
    public static final String ID = "id";
    public static final String COMMA = ",";
    public static final String NAME = "name";
    public static final String SIZE = "size";
    public static final String MANAGE_USER_REQUEST_URL = "https://graph.microsoft.com/v1.0/users";
    public static final String DELETE_USER_REQUEST_URL = "https://graph.microsoft.com/v1.0/users/";
    public static final String GET_USER_REQUEST_URL = "https://graph.microsoft.com/v1.0/users/";
    public static final String ACCOUNT_ENABLED_BODY = "accountEnabled";
    public static final String PASSWORD_PROFILE_BODY = "passwordProfile";
    public static final String MAIL_NICKNAME_BODY = "mailNickname";
    public static final String DISPLAY_NAME_BODY = "displayName";
    public static final String USER_PRINCIPAL_NAME_BODY = "userPrincipalName";
    public static final String ON_PREMISES_IMMUTABLE_ID_BODY = "onPremisesImmutableId";
    public static final String FORCE_CHANGE_PASSWORD_NEXT_SIGN_IN_BODY = "forceChangePasswordNextSignIn";
    public static final String PASSWORD_BODY = "password";
    public static final String SELECT_PATH = "?$select=";
    public static final String ATTACHMENTS = "/attachments";
    public static final String ATTACHMENTS_PATH = "/attachments/";
    public static final String VALUE= "value";
    public static final String MESSAGE_ID_LIST_JSON_PATH = "$.value[*].id";
}