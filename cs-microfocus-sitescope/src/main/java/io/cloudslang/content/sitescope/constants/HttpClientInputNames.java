/*
 * Copyright 2020-2025 Open Text
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

package io.cloudslang.content.sitescope.constants;

public final class HttpClientInputNames {
    public static final String AUTH_TYPE = "authType";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";
    public static final String PROXY_USERNAME = "proxyUsername";
    public static final String PROXY_PASSWORD = "proxyPassword";
    public static final String TRUST_ALL_ROOTS = "trustAllRoots";
    public static final String X509_HOSTNAME_VERIFIER = "x509HostnameVerifier";
    public static final String TRUST_KEYSTORE = "trustKeystore";
    public static final String TRUST_PASSWORD = "trustPassword";
    public static final String KEYSTORE = "keystore";
    public static final String KEYSTORE_PASSWORD = "keystorePassword";
    public static final String CONNECT_TIMEOUT = "connectTimeout";
    public static final String SOCKET_TIMEOUT = "socketTimeout";
    public static final String KEEP_ALIVE = "keepAlive";
    public static final String CONNECTIONS_MAX_PER_ROUTE = "connectionsMaxPerRoute";
    public static final String CONNECTIONS_MAX_TOTAL = "connectionsMaxTotal";
    public static final String RESPONSE_CHARACTER_SET = "responseCharacterSet";

    private HttpClientInputNames() {
    }
}
