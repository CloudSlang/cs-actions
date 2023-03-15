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
    public static class GetSiteIdByName{
        public static final String SITE_NAME = "siteName";
    }
    public static class GetRootSiteInputs{
        public static final String AUTH_TOKEN = "authToken";
    }

    public static class GetRootDrive{
        public static final String SITE_ID = "siteId";
    }
    public static class GetSiteNameById{
        public static final String SITE_ID = "siteId";
    }
}
