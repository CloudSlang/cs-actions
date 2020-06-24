/*
 * (c) Copyright 2020 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.abbyy.entities.requests;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;

import java.nio.file.Path;

public class HttpClientRequest implements HttpRequest {
    private final String url;
    private final String tlsVersion;
    private final String allowedCyphers;
    private final String authType;
    private final boolean preemptiveAuth;
    private final String username;
    private final String password;
    private final String kerberosConfigFile;
    private final String kerberosLoginConfFile;
    private final String kerberosSkipPortForLookup;
    private final String proxyHost;
    private final short proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final boolean trustAllRoots;
    private final String x509HostnameVerifier;
    private final String trustKeystore;
    private final String trustPassword;
    private final String keystore;
    private final String keystorePassword;
    private final int connectTimeout;
    private final int socketTimeout;
    private final boolean useCookies;
    private final boolean keepAlive;
    private final int connectionsMaxPerRoute;
    private final int connectionsMaxTotal;
    private final String headers;
    private final String responseCharacterSet;
    private final Path destinationFile;
    private final boolean followRedirects;
    private final String queryParams;
    private final boolean queryParamsAreURLEncoded;
    private final boolean queryParamsAreFormEncoded;
    private final String formParams;
    private final boolean formParamsAreURLEncoded;
    private final Path sourceFile;
    private final String body;
    private final String contentType;
    private final String requestCharacterSet;
    private final String multipartBodies;
    private final String multipartBodiesContentType;
    private final String multipartFiles;
    private final String multipartFilesContentType;
    private final boolean multipartValuesAreURLEncoded;
    private final boolean chunkedRequestEntity;
    private final String method;
    private final SerializableSessionObject httpClientCookieSession;
    private final GlobalSessionObject httpClientPoolingConnectionManager;


    private HttpClientRequest(Builder builder) {
        this.url = builder.url;
        this.tlsVersion = builder.tlsVersion;
        this.allowedCyphers = builder.allowedCyphers;
        this.authType = builder.authType;
        this.preemptiveAuth = builder.preemptiveAuth;
        this.username = builder.username;
        this.password = builder.password;
        this.kerberosConfigFile = builder.kerberosConfigFile;
        this.kerberosLoginConfFile = builder.kerberosLoginConfFile;
        this.kerberosSkipPortForLookup = builder.kerberosSkipPortForLookup;
        this.proxyHost = builder.proxyHost;
        this.proxyPort = builder.proxyPort;
        this.proxyUsername = builder.proxyUsername;
        this.proxyPassword = builder.proxyPassword;
        this.trustAllRoots = builder.trustAllRoots;
        this.x509HostnameVerifier = builder.x509HostnameVerifier;
        this.trustKeystore = builder.trustKeystore;
        this.trustPassword = builder.trustPassword;
        this.keystore = builder.keystore;
        this.keystorePassword = builder.keystorePassword;
        this.connectTimeout = builder.connectTimeout;
        this.socketTimeout = builder.socketTimeout;
        this.useCookies = builder.useCookies;
        this.keepAlive = builder.keepAlive;
        this.connectionsMaxPerRoute = builder.connectionsMaxPerRoute;
        this.connectionsMaxTotal = builder.connectionsMaxTotal;
        this.headers = builder.headers;
        this.responseCharacterSet = builder.responseCharacterSet;
        this.destinationFile = builder.destinationFile;
        this.followRedirects = builder.followRedirects;
        this.queryParams = builder.queryParams;
        this.queryParamsAreURLEncoded = builder.queryParamsAreURLEncoded;
        this.queryParamsAreFormEncoded = builder.queryParamsAreFormEncoded;
        this.formParams = builder.formParams;
        this.formParamsAreURLEncoded = builder.formParamsAreURLEncoded;
        this.sourceFile = builder.sourceFile;
        this.body = builder.body;
        this.contentType = builder.contentType;
        this.requestCharacterSet = builder.requestCharacterSet;
        this.multipartBodies = builder.multipartBodies;
        this.multipartBodiesContentType = builder.multipartBodiesContentType;
        this.multipartFiles = builder.multipartFiles;
        this.multipartFilesContentType = builder.multipartFilesContentType;
        this.multipartValuesAreURLEncoded = builder.multipartValuesAreURLEncoded;
        this.chunkedRequestEntity = builder.chunkedRequestEntity;
        this.method = builder.method;
        this.httpClientCookieSession = builder.httpClientCookieSession;
        this.httpClientPoolingConnectionManager = builder.httpClientPoolingConnectionManager;
    }


    public String getUrl() {
        return url;
    }


    public String getTlsVersion() {
        return tlsVersion;
    }


    public String getAllowedCyphers() {
        return allowedCyphers;
    }


    public String getAuthType() {
        return authType;
    }


    public boolean isPreemptiveAuth() {
        return preemptiveAuth;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getKerberosConfigFile() {
        return kerberosConfigFile;
    }


    public String getKerberosLoginConfFile() {
        return kerberosLoginConfFile;
    }


    public String getKerberosSkipPortForLookup() {
        return kerberosSkipPortForLookup;
    }


    public String getProxyHost() {
        return proxyHost;
    }


    public short getProxyPort() {
        return proxyPort;
    }


    public String getProxyUsername() {
        return proxyUsername;
    }


    public String getProxyPassword() {
        return proxyPassword;
    }


    public boolean isTrustAllRoots() {
        return trustAllRoots;
    }


    public String getX509HostnameVerifier() {
        return x509HostnameVerifier;
    }


    public String getTrustKeystore() {
        return trustKeystore;
    }


    public String getTrustPassword() {
        return trustPassword;
    }


    public String getKeystore() {
        return keystore;
    }


    public String getKeystorePassword() {
        return keystorePassword;
    }


    public int getConnectTimeout() {
        return connectTimeout;
    }


    public int getSocketTimeout() {
        return socketTimeout;
    }


    public boolean isUseCookies() {
        return useCookies;
    }


    public boolean isKeepAlive() {
        return keepAlive;
    }


    public int getConnectionsMaxPerRoute() {
        return connectionsMaxPerRoute;
    }


    public int getConnectionsMaxTotal() {
        return connectionsMaxTotal;
    }


    public String getHeaders() {
        return headers;
    }


    public String getResponseCharacterSet() {
        return responseCharacterSet;
    }


    public Path getDestinationFile() {
        return destinationFile;
    }


    public boolean isFollowRedirects() {
        return followRedirects;
    }


    public String getQueryParams() {
        return queryParams;
    }


    public boolean isQueryParamsAreURLEncoded() {
        return queryParamsAreURLEncoded;
    }


    public boolean isQueryParamsAreFormEncoded() {
        return queryParamsAreFormEncoded;
    }


    public String getFormParams() {
        return formParams;
    }


    public boolean isFormParamsAreURLEncoded() {
        return formParamsAreURLEncoded;
    }


    public Path getSourceFile() {
        return sourceFile;
    }


    public String getBody() {
        return body;
    }


    public String getContentType() {
        return contentType;
    }


    public String getRequestCharacterSet() {
        return requestCharacterSet;
    }


    public String getMultipartBodies() {
        return multipartBodies;
    }


    public String getMultipartBodiesContentType() {
        return multipartBodiesContentType;
    }


    public String getMultipartFiles() {
        return multipartFiles;
    }


    public String getMultipartFilesContentType() {
        return multipartFilesContentType;
    }


    public boolean isMultipartValuesAreURLEncoded() {
        return multipartValuesAreURLEncoded;
    }


    public boolean isChunkedRequestEntity() {
        return chunkedRequestEntity;
    }


    public String getMethod() {
        return method;
    }


    public SerializableSessionObject getHttpClientCookieSession() {
        return httpClientCookieSession;
    }


    public GlobalSessionObject getHttpClientPoolingConnectionManager() {
        return httpClientPoolingConnectionManager;
    }


    public static class Builder {
        private String url;
        private String tlsVersion;
        private String allowedCyphers;
        private String authType;
        private boolean preemptiveAuth;
        private String username;
        private String password;
        private String kerberosConfigFile;
        private String kerberosLoginConfFile;
        private String kerberosSkipPortForLookup;
        private String proxyHost;
        private short proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private boolean trustAllRoots;
        private String x509HostnameVerifier;
        private String trustKeystore;
        private String trustPassword;
        private String keystore;
        private String keystorePassword;
        private int connectTimeout;
        private int socketTimeout;
        private boolean useCookies;
        private boolean keepAlive;
        private int connectionsMaxPerRoute;
        private int connectionsMaxTotal;
        private String headers;
        private String responseCharacterSet;
        private Path destinationFile;
        private boolean followRedirects;
        private String queryParams;
        private boolean queryParamsAreURLEncoded;
        private boolean queryParamsAreFormEncoded;
        private String formParams;
        private boolean formParamsAreURLEncoded;
        private Path sourceFile;
        private String body;
        private String contentType;
        private String requestCharacterSet;
        private String multipartBodies;
        private String multipartBodiesContentType;
        private String multipartFiles;
        private String multipartFilesContentType;
        private boolean multipartValuesAreURLEncoded;
        private boolean chunkedRequestEntity;
        private String method;
        private SerializableSessionObject httpClientCookieSession;
        private GlobalSessionObject httpClientPoolingConnectionManager;


        public Builder url(String url) {
            this.url = url;
            return this;
        }


        public Builder tlsVersion(String tlsVersion) {
            this.tlsVersion = tlsVersion;
            return this;
        }


        public Builder allowedCyphers(String allowedCyphers) {
            this.allowedCyphers = allowedCyphers;
            return this;
        }


        public Builder authType(String authType) {
            this.authType = authType;
            return this;
        }


        public Builder preemptiveAuth(boolean preemptiveAuth) {
            this.preemptiveAuth = preemptiveAuth;
            return this;
        }


        public Builder username(String username) {
            this.username = username;
            return this;
        }


        public Builder password(String password) {
            this.password = password;
            return this;
        }


        public Builder kerberosConfigFile(String kerberosConfigFile) {
            this.kerberosConfigFile = kerberosConfigFile;
            return this;
        }


        public Builder kerberosLoginConfFile(String kerberosLoginConfFile) {
            this.kerberosLoginConfFile = kerberosLoginConfFile;
            return this;
        }


        public Builder kerberosSkipPortForLookup(String kerberosSkipPortForLookup) {
            this.kerberosSkipPortForLookup = kerberosSkipPortForLookup;
            return this;
        }


        public Builder proxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }


        public Builder proxyPort(short proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }


        public Builder proxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }


        public Builder proxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }


        public Builder trustAllRoots(boolean trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }


        public Builder x509HostnameVerifier(String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }


        public Builder trustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }


        public Builder trustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }


        public Builder keystore(String keystore) {
            this.keystore = keystore;
            return this;
        }


        public Builder keystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
            return this;
        }


        public Builder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }


        public Builder socketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }


        public Builder useCookies(boolean useCookies) {
            this.useCookies = useCookies;
            return this;
        }


        public Builder keepAlive(boolean keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }


        public Builder connectionsMaxPerRoute(int connectionsMaxPerRoute) {
            this.connectionsMaxPerRoute = connectionsMaxPerRoute;
            return this;
        }


        public Builder connectionsMaxTotal(int connectionsMaxTotal) {
            this.connectionsMaxTotal = connectionsMaxTotal;
            return this;
        }


        public Builder headers(String headers) {
            this.headers = headers;
            return this;
        }


        public Builder responseCharacterSet(String responseCharacterSet) {
            this.responseCharacterSet = responseCharacterSet;
            return this;
        }


        public Builder destinationFile(Path destinationFile) {
            this.destinationFile = destinationFile;
            return this;
        }


        public Builder followRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }


        public Builder queryParams(String queryParams) {
            this.queryParams = queryParams;
            return this;
        }


        public Builder queryParamsAreURLEncoded(boolean queryParamsAreURLEncoded) {
            this.queryParamsAreURLEncoded = queryParamsAreURLEncoded;
            return this;
        }


        public Builder queryParamsAreFormEncoded(boolean queryParamsAreFormEncoded) {
            this.queryParamsAreFormEncoded = queryParamsAreFormEncoded;
            return this;
        }


        public Builder formParams(String formParams) {
            this.formParams = formParams;
            return this;
        }


        public Builder formParamsAreURLEncoded(boolean formParamsAreURLEncoded) {
            this.formParamsAreURLEncoded = formParamsAreURLEncoded;
            return this;
        }


        public Builder sourceFile(Path sourceFile) {
            this.sourceFile = sourceFile;
            return this;
        }


        public Builder body(String body) {
            this.body = body;
            return this;
        }


        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }


        public Builder requestCharacterSet(String requestCharacterSet) {
            this.requestCharacterSet = requestCharacterSet;
            return this;
        }


        public Builder multipartBodies(String multipartBodies) {
            this.multipartBodies = multipartBodies;
            return this;
        }


        public Builder multipartBodiesContentType(String multipartBodiesContentType) {
            this.multipartBodiesContentType = multipartBodiesContentType;
            return this;
        }


        public Builder multipartFiles(String multipartFiles) {
            this.multipartFiles = multipartFiles;
            return this;
        }


        public Builder multipartFilesContentType(String multipartFilesContentType) {
            this.multipartFilesContentType = multipartFilesContentType;
            return this;
        }


        public Builder multipartValuesAreURLEncoded(boolean multipartValuesAreURLEncoded) {
            this.multipartValuesAreURLEncoded = multipartValuesAreURLEncoded;
            return this;
        }


        public Builder chunkedRequestEntity(boolean chunkedRequestEntity) {
            this.chunkedRequestEntity = chunkedRequestEntity;
            return this;
        }


        public Builder method(String method) {
            this.method = method;
            return this;
        }


        public Builder httpClientCookieSession(SerializableSessionObject httpClientCookieSession) {
            this.httpClientCookieSession = httpClientCookieSession;
            return this;
        }


        public Builder httpClientPoolingConnectionManager(GlobalSessionObject httpClientPoolingConnectionManager) {
            this.httpClientPoolingConnectionManager = httpClientPoolingConnectionManager;
            return this;
        }


        public HttpClientRequest build() {
            return new HttpClientRequest(this);
        }
    }
}
