/*
 * Copyright 2021-2025 Open Text
 * This program and the accompanying materials
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


package io.cloudslang.content.microsoftAD.utils;

public final class Constants {
    public static final String API = "api";
    public static final String NATIVE = "native";
    public static final String NEW_LINE = "\n";
    public static final String DEFAULT_LOGIN_TYPE = "Native";
    public static final String DEFAULT_SCOPE = "https://graph.microsoft.com/.default";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String BOOLEAN_TRUE = "true";
    public static final String BOOLEAN_FALSE = "false";
    public static final String EXCEPTION_ACQUIRE_TOKEN_FAILED = "Request to acquire token failed.";
    public static final String EXCEPTION_INVALID_LOGIN_TYPE = "The %s must be either 'API' or 'Native'.";
    public static final String EXCEPTION_INVALID_LOGIN_TYPE_REST = "The %s or %s is required for login.";
    public static final String EXCEPTION_USER_IDENTIFIER_NOT_SPEC = "Fields %s and %s cannot both be empty.";
    public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    public static final String EXCEPTION_INVALID_PROXY = "The %s is not a valid port.";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
    public static final String EXCEPTION_NO_PROPERTIES_SET = "You must provide at least one property in order to update " +
            "the user's details.";
    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String FORWARD_SLASH = "/";
    public static final String CODE_404 = "404";
    public static final String CHANGEIT = "changeit";
    public static final String ZERO = "0";
    public static final String UTF8 = "UTF-8";
    public static final String CONNECTIONS_MAX_PER_ROUTE_CONST = "2";
    public static final String CONNECTIONS_MAX_TOTAL_CONST = "20";
    public static final String AUTHORIZATION = "Authorization:";
    public static final String BEARER = "Bearer ";
    public static final String GRAPH_HOST = "graph.microsoft.com";
    public static final String BASE_GRAPH_PATH = "/v1.0/users/";
    public static final String MESSAGES_PATH = "/messages";
    public static final String PATH_SEPARATOR = "/";
    public static final String MAIL_FOLDERS_PATH = "/mailFolders/";
    public static final String TOP_QUERY = "$top=";
    public static final String SELECT_PATH = "$select=";
    public static final String AND = "&";
    public static final String QUERY = "?";

    public static final String COMMA = "\",\"";
    public static final String SIMPLE_COMMA = ",";
    public static final String RIGHT_PARANTHESIS = "[\"";
    public static final String LEFT_PARANTHESIS = "\"]";
    public static final String EMPTY_STRING = "";

    public static final String HTTPS = "https";
    public static final String STATUS_CODE = "statusCode";
    public static final String APPLICATION_JSON = "application/json";
    public static final String SEND = "/send";
    public static final String MOVE = "/move";
    public static final String ID = "id";
    public static final String ERROR = "error";
    public static final String MESSAGE = "message";
    public static final String ADD_LICENSES = "addLicenses";
    public static final String REMOVE_LICENSES = "removeLicenses";

    public static final String ATTACHMENTS = "/attachments";
    public static final String ATTACHMENTS_PATH = "/attachments/";
    public static final String IS_USER_ENABLED_ODATA_QUERY = "$select=accountEnabled";
    public static final String USER_ID = "userId";
    public static final String ACCOUNT_ENABLED = "accountEnabled";
    public static final String SKUID = "skuId";

    public static final String VALUE = "value";
    public static final String GET_MEMBER_GROUPS = "/getMemberGroups";
    public static final String LICENSE_DETAILS = "/licenseDetails";
    public static final String SECURITY_ENABLED_GROUPS = "securityEnabledOnly";

    public static final String QUERY_PARAMS_SELECT = "$select=";
    public static final String QUERY_PARAMS = "queryParams";

    public static final String STRICT = "strict";
    public static final String ALLOW_ALL = "allow_all";
    public static final String BROWSER_COMPATIBLE = "browser_compatible";

    public static final String TLSV1 = "tlsv1";
    public static final String TLSV11 = "tlsv1.1";
    public static final String TLSV12 = "tlsv1.2";
    public static final String TLSV13 = "tlsv1.3";

    //API URLs
    public static final String GRAPH_URL = "https://graph.microsoft.com/v1.0";
    public static final String USERS_URL = GRAPH_URL + "/users";
    public static final String ASSIGN_LICENSE_URL = "/assignLicense";
    public static final String CHANGE_USER_PASSWORD_URL = GRAPH_URL + "/me/changePassword";
    public static final String LIST_SUBSCRIBED_SKUS_URL = GRAPH_URL + "/subscribedSkus";
}