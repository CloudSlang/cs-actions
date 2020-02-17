/*
 * (c) Copyright 2019 EntIT Software LLC, a Micro Focus company, L.P.
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


package io.cloudslang.content.httpclient.entities;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;

/**
 * User: davidmih
 * Date: 7/18/14
 */
public class HttpClientInputs {

    public static final String URL = "url";
    public static final String TLS_VERSION = "tlsVersion";
    public static final String ALLOWED_CYPHERS = "allowedCyphers";
    public static final String METHOD = "method";
    public static final String FOLLOW_REDIRECTS = "followRedirects";
    public static final String QUERY_PARAMS = "queryParams";
    public static final String QUERY_PARAMS_ARE_URLENCODED = "queryParamsAreURLEncoded";
    public static final String QUERY_PARAMS_ARE_FORM_ENCODED = "queryParamsAreFormEncoded";
    public static final String FORM_PARAMS = "formParams";
    public static final String FORM_PARAMS_ARE_URLENCODED = "formParamsAreURLEncoded";
    public static final String SOURCE_FILE = "sourceFile";
    public static final String REQUEST_CHARACTER_SET = "requestCharacterSet";
    public static final String BODY = "body";
    public static final String CONTENT_TYPE = "contentType";
    public static final String AUTH_TYPE = "authType";
    public static final String PREEMPTIVE_AUTH = "preemptiveAuth";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String KERBEROS_CONFIG_FILE = "kerberosConfFile";
    public static final String KERBEROS_LOGIN_CONFIG_FILE = "kerberosLoginConfFile";
    public static final String KERBEROS_SKIP_PORT_CHECK = "kerberosSkipPortForLookup";
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
    public static final String USE_COOKIES = "useCookies";
    public static final String KEEP_ALIVE = "keepAlive";
    public static final String CONNECTIONS_MAX_PER_ROUTE = "connectionsMaxPerRoute";
    public static final String CONNECTIONS_MAX_TOTAL = "connectionsMaxTotal";
    public static final String HEADERS = "headers";
    public static final String RESPONSE_CHARACTER_SET = "responseCharacterSet";
    public static final String DESTINATION_FILE = "destinationFile";
    public static final String MULTIPART_BODIES = "multipartBodies";
    public static final String MULTIPART_BODIES_CONTENT_TYPE = "multipartBodiesContentType";
    public static final String MULTIPART_FILES = "multipartFiles";
    public static final String MULTIPART_FILES_CONTENT_TYPE = "multipartFilesContentType";
    public static final String MULTIPART_VALUES_ARE_URLENCODED = "multipartValuesAreURLEncoded";
    public static final String CHUNKED_REQUEST_ENTITY = "chunkedRequestEntity";

    public final static String SESSION_CONNECTION_POOL = "httpClientPoolingConnectionManager";
    public final static String SESSION_COOKIES = "httpClientCookieSession";

    private String url;
    private String authType;
    private String preemptiveAuth;
    private String username;
    private String password;
    private String kerberosConfFile;
    private String kerberosLoginConfFile;
    private String kerberosSkipPortCheck;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String trustAllRoots;
    private String x509HostnameVerifier;
    private String trustKeystore;
    private String trustPassword;
    private String keystore;
    private String keystorePassword;
    private String connectTimeout;
    private String socketTimeout;
    private String useCookies;
    private String keepAlive;
    private String connectionsMaxPerRoute;
    private String connectionsMaxTotal;
    private String headers;
    private String responseCharacterSet;
    private String destinationFile;
    private String followRedirects;
    private String queryParams;
    private String queryParamsAreURLEncoded;
    private String formParams;
    private String formParamsAreURLEncoded;
    private String sourceFile;
    private String body;
    private String contentType;
    private String requestCharacterSet;
    private String multipartBodies;
    private String multipartFiles;
    private String multipartValuesAreURLEncoded;
    private String multipartBodiesContentType;
    private String multipartFilesContentType;
    private String chunkedRequestEntity;
    private String method;
    private String tlsVersion;
    private String allowedCyphers;

    private SerializableSessionObject cookieStoreSessionObject;
    private GlobalSessionObject connectionPoolSessionObject;
    private String queryParamsAreFormEncoded;

    public String getAllowedCyphers() {
        return allowedCyphers;
    }

    public void setAllowedCyphers(String allowedCyphers) {
        this.allowedCyphers = allowedCyphers;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getPreemptiveAuth() {
        return preemptiveAuth;
    }

    public void setPreemptiveAuth(String preemptiveAuth) {
        this.preemptiveAuth = preemptiveAuth;
    }

    public String getTlsVersion() {
        return tlsVersion;
    }

    public void setTlsVersion(String tlsVersion) {
        this.tlsVersion = tlsVersion;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKerberosConfFile() {
        return kerberosConfFile;
    }

    public void setKerberosConfFile(String kerberosConfFile) {
        this.kerberosConfFile = kerberosConfFile;
    }

    public String getKerberosLoginConfFile() {
        return kerberosLoginConfFile;
    }

    public void setKerberosLoginConfFile(String kerberosLoginConfFile) {
        this.kerberosLoginConfFile = kerberosLoginConfFile;
    }

    public String getKerberosSkipPortCheck() {
        return kerberosSkipPortCheck;
    }

    public void setKerberosSkipPortCheck(String kerberosSkipPortCheck) {
        this.kerberosSkipPortCheck = kerberosSkipPortCheck;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getTrustAllRoots() {
        return trustAllRoots;
    }

    public void setTrustAllRoots(String trustAllRoots) {
        this.trustAllRoots = trustAllRoots;
    }

    public String getX509HostnameVerifier() {
        return x509HostnameVerifier;
    }

    public void setX509HostnameVerifier(String x509HostnameVerifier) {
        this.x509HostnameVerifier = x509HostnameVerifier;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public void setTrustKeystore(String trustKeystore) {
        this.trustKeystore = trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }

    public void setTrustPassword(String trustPassword) {
        this.trustPassword = trustPassword;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(String connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(String socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public String getUseCookies() {
        return useCookies;
    }

    public void setUseCookies(String useCookies) {
        this.useCookies = useCookies;
    }

    public String getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(String keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getConnectionsMaxPerRoute() {
        return connectionsMaxPerRoute;
    }

    public void setConnectionsMaxPerRoute(String connectionsMaxPerRoute) {
        this.connectionsMaxPerRoute = connectionsMaxPerRoute;
    }

    public String getConnectionsMaxTotal() {
        return connectionsMaxTotal;
    }

    public void setConnectionsMaxTotal(String connectionsMaxTotal) {
        this.connectionsMaxTotal = connectionsMaxTotal;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getResponseCharacterSet() {
        return responseCharacterSet;
    }

    public void setResponseCharacterSet(String responseCharacterSet) {
        this.responseCharacterSet = responseCharacterSet;
    }

    public String getDestinationFile() {
        return destinationFile;
    }

    public void setDestinationFile(String destinationFile) {
        this.destinationFile = destinationFile;
    }

    public String getFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(String followRedirects) {
        this.followRedirects = followRedirects;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getFormParams() {
        return formParams;
    }

    public void setFormParams(String formParams) {
        this.formParams = formParams;
    }

    public String getQueryParamsAreURLEncoded() {
        return queryParamsAreURLEncoded;
    }

    public void setQueryParamsAreURLEncoded(String queryParamsAreURLEncoded) {
        this.queryParamsAreURLEncoded = queryParamsAreURLEncoded;
    }

    public String getFormParamsAreURLEncoded() {
        return formParamsAreURLEncoded;
    }

    public void setFormParamsAreURLEncoded(String formParamsAreURLEncoded) {
        this.formParamsAreURLEncoded = formParamsAreURLEncoded;
    }

    public String getSourceFile() {
        return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
        this.sourceFile = sourceFile;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getRequestCharacterSet() {
        return requestCharacterSet;
    }

    public void setRequestCharacterSet(String requestCharacterSet) {
        this.requestCharacterSet = requestCharacterSet;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMultipartFiles() {
        return multipartFiles;
    }

    public void setMultipartFiles(String multipartFiles) {
        this.multipartFiles = multipartFiles;
    }

    public String getMultipartBodies() {
        return multipartBodies;
    }

    public void setMultipartBodies(String multipartBodies) {
        this.multipartBodies = multipartBodies;
    }

    public String getMultipartValuesAreURLEncoded() {
        return multipartValuesAreURLEncoded;
    }

    public void setMultipartValuesAreURLEncoded(String multipartValuesAreURLEncoded) {
        this.multipartValuesAreURLEncoded = multipartValuesAreURLEncoded;
    }

    public String getMultipartBodiesContentType() {
        return multipartBodiesContentType;
    }

    public void setMultipartBodiesContentType(String multipartBodiesContentType) {
        this.multipartBodiesContentType = multipartBodiesContentType;
    }

    public String getMultipartFilesContentType() {
        return multipartFilesContentType;
    }

    public void setMultipartFilesContentType(String multipartFilesContentType) {
        this.multipartFilesContentType = multipartFilesContentType;
    }

    public String getChunkedRequestEntity() {
        return chunkedRequestEntity;
    }

    public void setChunkedRequestEntity(String chunkedRequestEntity) {
        this.chunkedRequestEntity = chunkedRequestEntity;
    }

    public SerializableSessionObject getCookieStoreSessionObject() {
        return cookieStoreSessionObject;
    }

    public void setCookieStoreSessionObject(SerializableSessionObject cookieStoreSessionObject) {
        this.cookieStoreSessionObject = cookieStoreSessionObject;
    }

    public GlobalSessionObject getConnectionPoolSessionObject() {
        return connectionPoolSessionObject;
    }

    public void setConnectionPoolSessionObject(GlobalSessionObject connectionPoolSessionObject) {
        this.connectionPoolSessionObject = connectionPoolSessionObject;
    }

    public String getQueryParamsAreFormEncoded() {
        return queryParamsAreFormEncoded;
    }

    public void setQueryParamsAreFormEncoded(String queryParamsAreFormEncoded) {
        this.queryParamsAreFormEncoded = queryParamsAreFormEncoded;
    }
}
