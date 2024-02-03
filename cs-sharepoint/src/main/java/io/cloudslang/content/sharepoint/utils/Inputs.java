/*
 * Copyright 2024 Open Text
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

package io.cloudslang.content.sharepoint.utils;

public class Inputs {
    public static class CommonInputs {
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String TRUST_ALL_ROOTS = "trustAllRoots";
        public static final String X509_HOSTNAME_VERIFIER = "x509HostnameVerifier";
        public static final String TRUST_KEYSTORE = "trustKeystore";
        public static final String TRUST_PASSWORD = "trustPassword";
        public static final String CONNECT_TIMEOUT = "connectTimeout";
        public static final String SOCKET_TIMEOUT = "socketTimeout";
        public static final String SESSION_COOKIES = "httpClientCookieSession";
        public static final String SESSION_CONNECTION_POOL = "httpClientPoolingConnectionManager";
        public static final String EXECUTION_TIMEOUT = "executionTimeout";
        public static final String TLS_VERSION = "tlsVersion";
        public static final String ALLOWED_CIPHERS = "allowedCiphers";
        public static final String AUTH_TOKEN = "authToken";
    }

    public static class AuthorizationInputs {
        public static final String LOGIN_TYPE = "loginType";
        public static final String CLIENT_ID = "clientId";
        public static final String CLIENT_SECRET = "clientSecret";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String LOGIN_AUTHORITY = "loginAuthority";
        public static final String RESOURCE = "resource";
        public static final String SCOPE = "scope";
    }

    public static class GetSiteIdByName {
        public static final String SITE_NAME = "siteName";
    }

    public static class GetRootSiteInputs {
        public static final String AUTH_TOKEN = "authToken";
    }

    public static class GetRootDrive {
        public static final String SITE_ID = "siteId";
    }

    public static class GetSiteNameById {
        public static final String SITE_ID = "siteId";
    }

    public static class GetEntitiesFromDrive {
        public static final String PATH = "path";
        public static final String ENTITIES_TYPE = "entitiesType";
    }

    public static class UploadFile {
        public static final String SITE_ID = "siteId";
        public static final String DRIVE_ID = "driveId";
        public static final String FOLDER_ID = "folderId";
        public static final String FILE_PATH = "filePath";
    }

    public static class CreateFolder {
        public static final String FOLDER_NAME = "folderName";
        public static final String JSON_BODY = "jsonBody";
        public static final String FOLDER_ID = "folderId";
    }
    public static class DeleteFolder {
        public static final String FOLDER_ID = "folderId";
    }

    public static class GetEntityShareLink {
        public static final String TYPE = "type";
        public static final String PASSWORD = "password";
        public static final String EXPIRATION_DATE_TIME = "expirationDateTime";
        public static final String RETAIN_INHERITED_PERMISSIONS = "retainInheritedPermissions";
        public static final String SCOPE = "scope";
    }

    public static class DownloadFile {
        public static final String PATH = "path";
        public static final String OVERWRITE = "overwrite";
    }

    public static class GetEntityIdByName {
        public static final String ENTITY_NAME = "entityName";
        public static final String PARENT_FOLDER = "parentFolder";
        public static final String ENTITY_PATH = "entityPath";
    }
    public static class SearchForEntities {
        public static final String SEARCH_TEXT = "searchText";
        public static final String OPTIONAL_PARAMETERS = "optionalParameters";
    }
}
