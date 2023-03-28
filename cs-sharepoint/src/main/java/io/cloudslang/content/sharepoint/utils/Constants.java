/*
 * (c) Copyright 2023 Micro Focus, L.P.
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
package io.cloudslang.content.sharepoint.utils;

public class Constants {
    public static final String EMPTY = "";
    public static final String API = "api";
    public static final String NATIVE = "native";
    public static final String NEW_LINE = "\n";
    public static final String SLASH ="/";
    public static final String ZERO = "0";
    public static final String DEFAULT_TIMEOUT = "60";
    public static final String SEARCH = "search";
    public static final String NEGATIVE_RETURN_CODE = "-1";
    public static final String UTF_8 ="UTF-8";
    public static final String CONTENT_TYPE_TEXT_PLAIN ="text/plain";
    public static final String APPLICATION_JSON = "application/json";
    public static final String ID = "id";
    public static final String CONNECTIONS_MAX_PER_ROUTE_CONST = "2";
    public static final String CONNECTIONS_MAX_TOTAL_CONST = "20";
    public static final String AUTHORIZATION_BEARER = "Authorization: Bearer ";
    public static final String DEFAULT_LOGIN_TYPE = "Native";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String DEFAULT_SCOPE = "https://graph.microsoft.com/.default";
    public static final String BOOLEAN_FALSE = "false";
    public static final String BOOLEAN_TRUE = "true";
    public static final String STRICT = "strict";
    public static final String ALLOW_ALL = "allow_all";
    public static final String TLS = "TLS";
    public static final String STATUS_CODE_201 = "201";
    public static final String ATTACHMENTS_SPLIT = "Attachments";
    public static final int ATTACHMENT_SIZE_THRESHOLD = 3000000;
    public static final String PARENT_REFERENCE = "parentReference";
    public static final String NAME = "name";
    public static final String DISPLAY_NAME = "displayName";
    public static final String VALUE = "value";
    public static final String FOLDER = "folder";
    public static final String TYPE = "type";
    public static final String FILE = "file";
    public static final String MIME_TYPE = "mimeType";
    public static final String FOLDERS = "folders";
    public static final String FILES = "files";
    public static final String ALL = "all";
    public static final String LINK = "link";

    public static final String GRAPH_API_ENDPOINT = "https://graph.microsoft.com/v1.0";
    public static final String SITES_ENDPOINT = "/sites/";
    public static final String PATH_ENDPOINT =":/";
    public static final String CONTENT_ENDPOINT= ":/content";
    public static final String ROOT_DRIVE_ENDPOINT = "/drive/root";
    public static final String ROOT_CHILDREN_ENDPOINT = "/root/children";
    public static final String ROOT_PATH_ENDPOINT = "/root:/";
    public static final String ROOT_PATH_ENDPOINT_2 = "root:/";
    public static final String CHILDREN_PATH_ENDPOINT = ":/children";
    public static final String DRIVE_ENDPOINT = "/drive";
    public static final String DRIVES_ENDPOINT = "/drives/";
    public static final String DRIVE_ITEMS_ENDPOINT = "/drive/items/";
    public static final String ITEMS_ENDPOINT = "/items/";
    public static final String CREATE_LINK_ENDPOINT = "/createLink";
    public static final String EXCEPTION_ACQUIRE_TOKEN_FAILED = "Request to acquire token failed.";
    public static final String EXCEPTION_INVALID_LOGIN_TYPE = "The %s must be either 'API' or 'Native'.";
    public static final String EXCEPTION_INVALID_LOGIN_TYPE_REST = "The %s or %s is required for login.";
    public static final String EXCEPTION_NULL_EMPTY = "The %s can't be null or empty.";
    public static final String EXCEPTION_INVALID_PROXY = "The proxy port %s is not a valid port.";
    public static final String EXCEPTION_INVALID_BOOLEAN = "The %s for %s input is not a valid boolean value.";
    public static final String EXCEPTION_INVALID_NUMBER = "The %s for %s input is not a valid number value.";
    public static final String EXCEPTION_INVALID_HOSTNAME_VERIFIER = "%s for %s input is not a valid x509HostnameVerifier value. The valid values are: 'strict','allow_all'.";
    public static final String EXCEPTION_SITE_ID = "Site id was not found in the JSON response.";
    public static final String EXCEPTION_SITE_NAME = "Site display name was not found in the JSON response.";
    public static final String EXCEPTION_INVALID_ENTITIES_TYPE = "The %s for %s input is not a valid entities type value. The valid values are 'folders', 'files', 'all'.";
   public static final String ANONYMOUS = "anonymous";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PATCH = "PATCH";
    public static final String DELETE = "DELETE";

    public static class Endpoints{
        public static String GET_ROOT_SITE = "https://graph.microsoft.com/v1.0/sites/root";
        public static String GET_ALL_SITES = "https://graph.microsoft.com/v1.0/sites/";
    }
}
