/*
 * Copyright 2022-2025 Open Text
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



package io.cloudslang.content.httpclient.entities;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpClientInputs {
    private final String host;
    private final String authType;
    private final String preemptiveAuth;
    private final String username;
    private final String password;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String trustAllRoots;
    private final String x509HostnameVerifier;
    private final String trustKeystore;
    private final String trustPassword;
    private final String keystore;
    private final String keystorePassword;
    private final String connectTimeout;
    private final String responseTimeout;
    private final String executionTimeout;
    private final String useCookies;
    private final String keepAlive;
    private final String connectionsMaxPerRoute;
    private final String connectionsMaxTotal;
    private final String headers;
    private final String responseCharacterSet;
    private final String destinationFile;
    private final String followRedirects;
    private final String queryParams;
    private final String queryParamsAreURLEncoded;
    private final String formParams;
    private final String formParamsAreURLEncoded;
    private final String sourceFile;
    private final String body;
    private final String contentType;
    private final String requestCharacterSet;
    private final String multipartBodies;
    private final String multipartFiles;
    private final String multipartValuesAreURLEncoded;
    private final String multipartBodiesContentType;
    private final String multipartFilesContentType;
    private final String method;
    private final String tlsVersion;
    private final String allowedCiphers;

    private final SerializableSessionObject cookieStoreSessionObject;
    private final GlobalSessionObject<?> connectionPoolSessionObject;
    private final String queryParamsAreFormEncoded;

    @NotNull
    public static HttpClientInputsBuilder builder(){return new HttpClientInputsBuilder();}

    @java.beans.ConstructorProperties({"host","authType","preemptiveAuth","username","password","proxyHost","proxyPort",
            "proxyUsername", "proxyPassword", "trustAllRoots","x509HostnameVerifier", "trustKeystore", "trustPassword",
            "keystore","keystorePassword","connectTimeout","responseTimeout","executionTimeout","useCookies","keepAlive",
            "connectionsMaxPerRoute", "connectionsMaxTotal", "headers", "responseCharacterSet", "destinationFile",
            "followRedirects", "queryParams", "queryParamsAreURLEncoded", "formParams", "formParamsAreURLEncoded",
            "sourceFile", "body", "contentType", "requestCharacterSet", "multipartBodies", "multipartFiles",
            "multipartValuesAreURLEncoded", "multipartBodiesContentType", "multipartFilesContentType",
            "method", "tlsVersion", "allowedCiphers", "cookieStoreSessionObject",
            "connectionPoolSessionObject", "queryParamsAreFormEncoded"})
    public HttpClientInputs(String host, String authType, String preemptiveAuth, String username, String password,
                            String proxyHost, String proxyPort, String proxyUsername, String proxyPassword,
                            String trustAllRoots, String x509HostnameVerifier, String trustKeystore, String trustPassword,
                            String keystore, String keystorePassword, String connectTimeout, String responseTimeout,
                            String executionTimeout, String useCookies, String keepAlive, String connectionsMaxPerRoute,
                            String connectionsMaxTotal, String headers, String responseCharacterSet, String destinationFile,
                            String followRedirects, String queryParams, String queryParamsAreURLEncoded, String formParams,
                            String formParamsAreURLEncoded, String sourceFile, String body, String contentType,
                            String requestCharacterSet, String multipartBodies, String multipartFiles,
                            String multipartValuesAreURLEncoded, String multipartBodiesContentType,
                            String multipartFilesContentType, String method,
                            String tlsVersion, String allowedCiphers, SerializableSessionObject cookieStoreSessionObject,
                            GlobalSessionObject<?> connectionPoolSessionObject, String queryParamsAreFormEncoded) {
        this.host = host;
        this.authType = authType;
        this.preemptiveAuth = preemptiveAuth;
        this.username = username;
        this.password = password;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.trustAllRoots = trustAllRoots;
        this.x509HostnameVerifier = x509HostnameVerifier;
        this.trustKeystore = trustKeystore;
        this.trustPassword = trustPassword;
        this.keystore = keystore;
        this.keystorePassword = keystorePassword;
        this.connectTimeout = connectTimeout;
        this.responseTimeout = responseTimeout;
        this.executionTimeout = executionTimeout;
        this.useCookies = useCookies;
        this.keepAlive = keepAlive;
        this.connectionsMaxPerRoute = connectionsMaxPerRoute;
        this.connectionsMaxTotal = connectionsMaxTotal;
        this.headers = headers;
        this.responseCharacterSet = responseCharacterSet;
        this.destinationFile = destinationFile;
        this.followRedirects = followRedirects;
        this.queryParams = queryParams;
        this.queryParamsAreURLEncoded = queryParamsAreURLEncoded;
        this.formParams = formParams;
        this.formParamsAreURLEncoded = formParamsAreURLEncoded;
        this.sourceFile = sourceFile;
        this.body = body;
        this.contentType = contentType;
        this.requestCharacterSet = requestCharacterSet;
        this.multipartBodies = multipartBodies;
        this.multipartFiles = multipartFiles;
        this.multipartValuesAreURLEncoded = multipartValuesAreURLEncoded;
        this.multipartBodiesContentType = multipartBodiesContentType;
        this.multipartFilesContentType = multipartFilesContentType;
        this.method = method;
        this.tlsVersion = tlsVersion;
        this.allowedCiphers = allowedCiphers;
        this.cookieStoreSessionObject = cookieStoreSessionObject;
        this.connectionPoolSessionObject = connectionPoolSessionObject;
        this.queryParamsAreFormEncoded = queryParamsAreFormEncoded;
    }

    @NotNull
    public String getAllowedCiphers() {
        return allowedCiphers;
    }

    @NotNull
    public String getHost() {
        return host;
    }

    @NotNull
    public String getAuthType() {
        return authType;
    }

    @NotNull
    public String getPreemptiveAuth() {
        return preemptiveAuth;
    }

    @NotNull
    public String getTlsVersion() {
        return tlsVersion;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    @NotNull
    public String getPassword() {
        return password;
    }

    @NotNull
    public String getProxyHost() {
        return proxyHost;
    }

    @NotNull
    public String getProxyPort() {
        return proxyPort;
    }

    @NotNull
    public String getProxyUsername() {
        return proxyUsername;
    }

    @NotNull
    public String getProxyPassword() {
        return proxyPassword;
    }

    @NotNull
    public String getTrustAllRoots() {
        return trustAllRoots;
    }

    @NotNull
    public String getX509HostnameVerifier() {
        return x509HostnameVerifier;
    }

    @NotNull
    public String getTrustKeystore() {
        return trustKeystore;
    }

    @NotNull
    public String getTrustPassword() {
        return trustPassword;
    }

    @NotNull
    public String getKeystore() {
        return keystore;
    }

    @NotNull
    public String getKeystorePassword() {
        return keystorePassword;
    }

    @NotNull
    public String getConnectTimeout() {
        return connectTimeout;
    }

    @NotNull
    public String getResponseTimeout() {
        return responseTimeout;
    }

    @NotNull
    public String getExecutionTimeout() {
        return executionTimeout;
    }

    @NotNull
    public String getUseCookies() {
        return useCookies;
    }

    @NotNull
    public String getKeepAlive() {
        return keepAlive;
    }

    @NotNull
    public String getConnectionsMaxPerRoute() {
        return connectionsMaxPerRoute;
    }

    @NotNull
    public String getConnectionsMaxTotal() {
        return connectionsMaxTotal;
    }

    @NotNull
    public String getHeaders() {
        return headers;
    }

    @NotNull
    public String getResponseCharacterSet() {
        return responseCharacterSet;
    }

    @NotNull
    public String getDestinationFile() {
        return destinationFile;
    }

    @NotNull
    public String getFollowRedirects() {
        return followRedirects;
    }

    @NotNull
    public String getQueryParams() {
        return queryParams;
    }

    @NotNull
    public String getFormParams() {
        return formParams;
    }

    @NotNull
    public String getQueryParamsAreURLEncoded() {
        return queryParamsAreURLEncoded;
    }

    @NotNull
    public String getFormParamsAreURLEncoded() {
        return formParamsAreURLEncoded;
    }

    @NotNull
    public String getSourceFile() {
        return sourceFile;
    }

    @NotNull
    public String getBody() {
        return body;
    }

    @NotNull
    public String getContentType() {
        return contentType;
    }

    @NotNull
    public String getRequestCharacterSet() {
        return requestCharacterSet;
    }

    @NotNull
    public String getMethod() {
        return method;
    }

    @NotNull
    public String getMultipartFiles() {
        return multipartFiles;
    }

    @NotNull
    public String getMultipartBodies() {
        return multipartBodies;
    }

    @NotNull
    public String getMultipartValuesAreURLEncoded() {
        return multipartValuesAreURLEncoded;
    }

    @NotNull
    public String getMultipartBodiesContentType() {
        return multipartBodiesContentType;
    }

    @NotNull
    public String getMultipartFilesContentType() {
        return multipartFilesContentType;
    }

    @NotNull
    public SerializableSessionObject getCookieStoreSessionObject() {
        return cookieStoreSessionObject;
    }

    @NotNull
    public GlobalSessionObject<?> getConnectionPoolSessionObject() {
        return connectionPoolSessionObject;
    }

    @NotNull
    public String getQueryParamsAreFormEncoded() {
        return queryParamsAreFormEncoded;
    }

    public static class HttpClientInputsBuilder {
        private String host = EMPTY;
        private String authType = EMPTY;
        private String preemptiveAuth = EMPTY;
        private String username = EMPTY;
        private String password = EMPTY;
        private String proxyHost = EMPTY;
        private String proxyPort = EMPTY;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;
        private String trustAllRoots = EMPTY;
        private String x509HostnameVerifier = EMPTY;
        private String trustKeystore = EMPTY;
        private String trustPassword = EMPTY;
        private String keystore = EMPTY;
        private String keystorePassword = EMPTY;
        private String connectTimeout = EMPTY;
        private String responseTimeout = EMPTY;
        private String executionTimeout = EMPTY;
        private String useCookies = EMPTY;
        private String keepAlive = EMPTY;
        private String connectionsMaxPerRoute = EMPTY;
        private String connectionsMaxTotal = EMPTY;
        private String headers = EMPTY;
        private String responseCharacterSet = EMPTY;
        private String destinationFile = EMPTY;
        private String followRedirects = EMPTY;
        private String queryParams = EMPTY;
        private String queryParamsAreURLEncoded = EMPTY;
        private String formParams = EMPTY;
        private String formParamsAreURLEncoded = EMPTY;
        private String sourceFile = EMPTY;
        private String body = EMPTY;
        private String contentType = EMPTY;
        private String requestCharacterSet = EMPTY;
        private String multipartBodies = EMPTY;
        private String multipartFiles = EMPTY;
        private String multipartValuesAreURLEncoded = EMPTY;
        private String multipartBodiesContentType = EMPTY;
        private String multipartFilesContentType = EMPTY;
        private String method = EMPTY;
        private String tlsVersion = EMPTY;
        private String allowedCiphers = EMPTY;

        private SerializableSessionObject cookieStoreSessionObject;
        private GlobalSessionObject<?> connectionPoolSessionObject;
        private String queryParamsAreFormEncoded = EMPTY;

        HttpClientInputsBuilder() {
        }

        @NotNull
        public HttpClientInputsBuilder host(@NotNull final String host){
            this.host = host;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder authType(@NotNull final String authType){
            this.authType = authType;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder preemptiveAuth(@NotNull final String preemptiveAuth){
            this.preemptiveAuth = preemptiveAuth;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder username(@NotNull final String username){
            this.username = username;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder password(@NotNull final String password){
            this.password = password;
            return this;
        }


        @NotNull
        public HttpClientInputsBuilder proxyHost(@NotNull final String proxyHost){
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder proxyPort(@NotNull final String proxyPort){
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder proxyUsername(@NotNull final String proxyUsername){
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder proxyPassword(@NotNull final String proxyPassword){
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder trustAllRoots(@NotNull final String trustAllRoots){
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder x509HostnameVerifier(@NotNull final String x509HostnameVerifier){
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder trustKeystore(@NotNull final String trustKeystore){
            this.trustKeystore = trustKeystore;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder trustPassword(@NotNull final String trustPassword){
            this.trustPassword = trustPassword;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder keystore(@NotNull final String keystore){
            this.keystore = keystore;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder keystorePassword(@NotNull final String keystorePassword){
            this.keystorePassword = keystorePassword;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder connectTimeout(@NotNull final String connectTimeout){
            this.connectTimeout = connectTimeout;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder responseTimeout(@NotNull final String responseTimeout){
            this.responseTimeout = responseTimeout;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder executionTimeout(@NotNull final String executionTimeout){
            this.executionTimeout = executionTimeout;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder useCookies(@NotNull final String useCookies){
            this.useCookies = useCookies;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder keepAlive(@NotNull final String keepAlive){
            this.keepAlive = keepAlive;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder connectionsMaxPerRoute(@NotNull final String connectionsMaxPerRoute){
            this.connectionsMaxPerRoute = connectionsMaxPerRoute;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder connectionsMaxTotal(@NotNull final String connectionsMaxTotal){
            this.connectionsMaxTotal = connectionsMaxTotal;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder headers(@NotNull final String headers){
            this.headers = headers;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder responseCharacterSet(@NotNull final String responseCharacterSet){
            this.responseCharacterSet = responseCharacterSet;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder destinationFile(@NotNull final String destinationFile){
            this.destinationFile = destinationFile;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder followRedirects(@NotNull final String followRedirects){
            this.followRedirects = followRedirects;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder queryParams(@NotNull final String queryParams){
            this.queryParams = queryParams;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder queryParamsAreURLEncoded(@NotNull final String queryParamsAreURLEncoded){
            this.queryParamsAreURLEncoded = queryParamsAreURLEncoded;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder formParams(@NotNull final String formParams){
            this.formParams = formParams;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder formParamsAreURLEncoded(@NotNull final String formParamsAreURLEncoded){
            this.formParamsAreURLEncoded = formParamsAreURLEncoded;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder sourceFile(@NotNull final String sourceFile){
            this.sourceFile = sourceFile;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder body(@NotNull final String body){
            this.body = body;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder contentType(@NotNull final String contentType){
            this.contentType = contentType;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder requestCharacterSet(@NotNull final String requestCharacterSet){
            this.requestCharacterSet = requestCharacterSet;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder multipartBodies(@NotNull final String multipartBodies){
            this.multipartBodies = multipartBodies;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder multipartFiles(@NotNull final String multipartFiles){
            this.multipartFiles = multipartFiles;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder multipartValuesAreURLEncoded(@NotNull final String multipartValuesAreURLEncoded){
            this.multipartValuesAreURLEncoded = multipartValuesAreURLEncoded;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder multipartBodiesContentType(@NotNull final String multipartBodiesContentType){
            this.multipartBodiesContentType = multipartBodiesContentType;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder multipartFilesContentType(@NotNull final String multipartFilesContentType){
            this.multipartFilesContentType = multipartFilesContentType;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder method(@NotNull final String method){
            this.method = method;
            return this;
        }
        @NotNull
        public HttpClientInputsBuilder tlsVersion(@NotNull final String tlsVersion){
            this.tlsVersion = tlsVersion;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder allowedCiphers(@NotNull final String allowedCiphers){
            this.allowedCiphers = allowedCiphers;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder cookieStoreSessionObject(@NotNull final SerializableSessionObject cookieStoreSessionObject){
            this.cookieStoreSessionObject = cookieStoreSessionObject;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder connectionPoolSessionObject(@NotNull final GlobalSessionObject<?> connectionPoolSessionObject){
            this.connectionPoolSessionObject = connectionPoolSessionObject;
            return this;
        }

        @NotNull
        public HttpClientInputsBuilder queryParamsAreFormEncoded(@NotNull final String queryParamsAreFormEncoded){
            this.queryParamsAreFormEncoded = queryParamsAreFormEncoded;
            return this;
        }

        public HttpClientInputs build(){
            return new HttpClientInputs(host, authType, preemptiveAuth, username, password, proxyHost, proxyPort, proxyUsername,
                    proxyPassword, trustAllRoots, x509HostnameVerifier, trustKeystore, trustPassword, keystore, keystorePassword,
                    connectTimeout, responseTimeout, executionTimeout, useCookies, keepAlive, connectionsMaxPerRoute,
                    connectionsMaxTotal, headers, responseCharacterSet, destinationFile, followRedirects, queryParams,
                    queryParamsAreURLEncoded, formParams, formParamsAreURLEncoded, sourceFile, body, contentType,
                    requestCharacterSet, multipartBodies, multipartFiles, multipartValuesAreURLEncoded, multipartBodiesContentType,
                    multipartFilesContentType, method, tlsVersion, allowedCiphers, cookieStoreSessionObject,
                    connectionPoolSessionObject, queryParamsAreFormEncoded);
        }

    }
}
