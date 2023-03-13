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
    public static final String DEFAULT_LOGIN_TYPE = "Native";
    public static final String DEFAULT_RESOURCE = "https://graph.microsoft.com";
    public static final String DEFAULT_PROXY_PORT = "8080";
    public static final String DEFAULT_SCOPE = "https://graph.microsoft.com/.default";
    public static final String BOOLEAN_FALSE = "false";
    public static final String BOOLEAN_TRUE = "true";
    public static final String STRICT = "strict";
    public static final String TLS = "TLS";
    public static final String STATUS_CODE_201 = "201";
    public static final String ATTACHMENTS_SPLIT = "Attachments";
    public static final int ATTACHMENT_SIZE_THRESHOLD = 3000000;
    public static final String EXCEPTION_ACQUIRE_TOKEN_FAILED = "Request to acquire token failed.";
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
    public static final String HOST = "host";
    public static final String TLS_VERSION = "tlsVersion";
    public static final String ALLOWED_CIPHERS = "allowedCiphers";
    public static final String TRUST_ALL_ROOTS = "trustAllRoots";
    public static final String X509_HOSTNAME_VERIFIER = "x509HostnameVerifier";
    public static final String TRUST_KEYSTORE = "trustKeystore";
    public static final String TRUST_PASSWORD = "trustPassword";
    public static final String KEYSTORE = "keystore";
    public static final String KEYSTORE_PASSWORD = "keystorePassword";
    public static final String CONNECT_TIMEOUT = "connectTimeout";
    public static final String EXECUTION_TIMEOUT = "executionTimeout";
    public static final String CONNECTIONS_MAX_PER_ROUTE = "connectionsMaxPerRoute";
    public static final String CONNECTIONS_MAX_TOTAL = "connectionsMaxTotal";
    public static final String SESSION_COOKIES = "httpClientCookieSession";
    public static final String SESSION_CONNECTION_POOL = "httpClientPoolingConnectionManager";
    public static final String APPLICATION_JSON = "application/json";
    public static final String CONTENT_TYPE = "Content-Type:";
    public static final String AUTHORIZATION_BEARER = "Authorization: Bearer ";
    public static final String CONNECTIONS_MAX_PER_ROUTE_VALUE = "20";
    public static final String CONNECTIONS_MAX_TOTAL_VALUE = "200";
    public static final String NEGATIVE_RETURN_CODE = "-1";
    public static final String EXCEPTION = "exception";
}
