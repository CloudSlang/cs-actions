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
import io.cloudslang.content.abbyy.constants.SecurityConstants;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class HttpClientRequest implements HttpRequest {
    private String url;
    private String tlsVersion;
    private String allowedCyphers;
    private String authType;
    private String preemptiveAuth;
    private String username;
    private String password;
    private String kerberosConfigFile;
    private String kerberosLoginConfFile;
    private String kerberosSkipPortForLookup;
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
    private String queryParamsAreFormEncoded;
    private String formParams;
    private String formParamsAreURLEncoded;
    private String sourceFile;
    private String body;
    private String contentType;
    private String requestCharacterSet;
    private String multipartBodies;
    private String multipartBodiesContentType;
    private String multipartFiles;
    private String multipartFilesContentType;
    private String multipartValuesAreURLEncoded;
    private String chunkedRequestEntity;
    private String method;
    private SerializableSessionObject httpClientCookieSession;
    private GlobalSessionObject httpClientPoolingConnectionManager;


    private HttpClientRequest() {

    }


    public @Nullable String getUrl() {
        return url;
    }


    public @Nullable String getTlsVersion() {
        return tlsVersion;
    }


    public @Nullable String getAllowedCyphers() {
        return allowedCyphers;
    }


    public @Nullable String getAuthType() {
        return authType;
    }


    public @Nullable Boolean isPreemptiveAuth() {
        return Boolean.parseBoolean(preemptiveAuth);
    }


    public @Nullable String getUsername() {
        return username;
    }


    public @Nullable String getPassword() {
        return password;
    }


    public @Nullable String getKerberosConfigFile() {
        return kerberosConfigFile;
    }


    public @Nullable String getKerberosLoginConfFile() {
        return kerberosLoginConfFile;
    }


    public @Nullable String getKerberosSkipPortForLookup() {
        return kerberosSkipPortForLookup;
    }


    public @Nullable String getProxyHost() {
        return proxyHost;
    }


    public @Nullable Short getProxyPort() {
        return Short.parseShort(proxyPort);
    }


    public @Nullable String getProxyUsername() {
        return proxyUsername;
    }


    public @Nullable String getProxyPassword() {
        return proxyPassword;
    }


    public @Nullable Boolean getTrustAllRoots() {
        return Boolean.parseBoolean(trustAllRoots);
    }


    public @Nullable String getX509HostnameVerifier() {
        return x509HostnameVerifier;
    }


    public @Nullable String getTrustKeystore() {
        return trustKeystore;
    }


    public @Nullable String getTrustPassword() {
        return trustPassword;
    }


    public @Nullable String getKeystore() {
        return keystore;
    }


    public @Nullable String getKeystorePassword() {
        return keystorePassword;
    }


    public @Nullable Integer getConnectTimeout() {
        return Integer.parseInt(connectTimeout);
    }


    public @Nullable Integer getSocketTimeout() {
        return Integer.parseInt(socketTimeout);
    }


    public @Nullable Boolean isUseCookies() {
        return Boolean.parseBoolean(useCookies);
    }


    public @Nullable Boolean getKeepAlive() {
        return Boolean.parseBoolean(keepAlive);
    }


    public @Nullable Integer getConnectionsMaxPerRoute() {
        return Integer.parseInt(connectionsMaxPerRoute);
    }


    public @Nullable Integer getConnectionsMaxTotal() {
        return Integer.parseInt(connectionsMaxTotal);
    }


    public @Nullable String getHeaders() {
        return headers;
    }


    public @Nullable String getResponseCharacterSet() {
        return responseCharacterSet;
    }


    public @Nullable File getDestinationFile() {
        return StringUtils.isNotBlank(destinationFile) ? new File(destinationFile) : null;
    }


    public @Nullable Boolean isFollowRedirects() {
        return Boolean.parseBoolean(followRedirects);
    }


    public @Nullable String getQueryParams() {
        return queryParams;
    }


    public @Nullable Boolean isQueryParamsAreURLEncoded() {
        return Boolean.parseBoolean(queryParamsAreURLEncoded);
    }


    public @Nullable Boolean isQueryParamsAreFormEncoded() {
        return Boolean.parseBoolean(queryParamsAreFormEncoded);
    }


    public @Nullable String getFormParams() {
        return formParams;
    }


    public @Nullable Boolean isFormParamsAreURLEncoded() {
        return Boolean.parseBoolean(formParamsAreURLEncoded);
    }


    public @Nullable File getSourceFile() {
        return StringUtils.isNotBlank(sourceFile) ? new File(sourceFile) : null;
    }


    public @Nullable String getBody() {
        return body;
    }


    public @Nullable String getContentType() {
        return contentType;
    }


    public @Nullable String getRequestCharacterSet() {
        return requestCharacterSet;
    }


    public @Nullable String getMultipartBodies() {
        return multipartBodies;
    }


    public @Nullable String getMultipartBodiesContentType() {
        return multipartBodiesContentType;
    }


    public @Nullable String getMultipartFiles() {
        return multipartFiles;
    }


    public @Nullable String getMultipartFilesContentType() {
        return multipartFilesContentType;
    }


    public @Nullable Boolean isMultipartValuesAreURLEncoded() {
        return Boolean.parseBoolean(multipartValuesAreURLEncoded);
    }


    public @Nullable Boolean isChunkedRequestEntity() {
        return Boolean.parseBoolean(chunkedRequestEntity);
    }


    public @Nullable String getMethod() {
        return method;
    }


    public @Nullable SerializableSessionObject getHttpClientCookieSession() {
        return httpClientCookieSession;
    }


    public @Nullable GlobalSessionObject getHttpClientPoolingConnectionManager() {
        return httpClientPoolingConnectionManager;
    }


    public static class Builder {
        private String url;
        private String authType;
        private String username;
        private String password;
        private String proxyHost;
        private short proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private boolean trustAllRoots;
        private String x509HostnameVerifier;
        private String trustKeystore;
        private String trustPassword;
        private int connectTimeout;
        private int socketTimeout;
        private boolean keepAlive;
        private int connectionsMaxPerRoute;
        private int connectionsMaxTotal;
        private String responseCharacterSet;
        private String destinationFile;
        private String sourceFile;
        private String contentType;
        private String method;
        private String headers;


        public Builder url(String url) {
            this.url = url;
            return this;
        }


        public Builder authType(String authType) {
            this.authType = authType;
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


        public Builder connectTimeout(int connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }


        public Builder socketTimeout(int socketTimeout) {
            this.socketTimeout = socketTimeout;
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


        public Builder responseCharacterSet(String responseCharacterSet) {
            this.responseCharacterSet = responseCharacterSet;
            return this;
        }


        public Builder destinationFile(String destinationFile) {
            this.destinationFile = destinationFile;
            return this;
        }


        public Builder sourceFile(String sourceFile) {
            this.sourceFile = sourceFile;
            return this;
        }


        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }


        public Builder method(String method) {
            this.method = method;
            return this;
        }


        public Builder headers(String headers) {
            this.headers = headers;
            return this;
        }


        public HttpClientRequest build() {
            HttpClientRequest request = new HttpClientRequest();

            request.url = StringUtils.defaultString(this.url);
            request.tlsVersion = SecurityConstants.TLSv1_2;
            request.allowedCyphers = SecurityConstants.ALLOWED_CYPHERS;
            request.authType = StringUtils.defaultString(this.authType);
            request.preemptiveAuth = String.valueOf(false);
            request.username = StringUtils.defaultString(this.username);
            request.password = StringUtils.defaultString(this.password);
            request.kerberosConfigFile = StringUtils.EMPTY;
            request.kerberosLoginConfFile = StringUtils.EMPTY;
            request.kerberosSkipPortForLookup = StringUtils.EMPTY;
            request.proxyHost = StringUtils.defaultString(this.proxyHost);
            request.proxyPort = String.valueOf(this.proxyPort);
            request.proxyUsername = StringUtils.defaultString(this.proxyUsername);
            request.proxyPassword = StringUtils.defaultString(this.proxyPassword);
            request.trustAllRoots = String.valueOf(this.trustAllRoots);
            request.x509HostnameVerifier = StringUtils.defaultString(this.x509HostnameVerifier);
            request.trustKeystore = StringUtils.defaultString(this.trustKeystore);
            request.trustPassword = StringUtils.defaultString(this.trustPassword);
            request.keystore = StringUtils.EMPTY;
            request.keystorePassword = StringUtils.EMPTY;
            request.connectTimeout = String.valueOf(this.connectTimeout);
            request.socketTimeout = String.valueOf(this.socketTimeout);
            request.useCookies = String.valueOf(false);
            request.keepAlive = String.valueOf(this.keepAlive);
            request.connectionsMaxPerRoute = String.valueOf(this.connectionsMaxPerRoute);
            request.connectionsMaxTotal = String.valueOf(this.connectionsMaxTotal);
            request.headers = StringUtils.defaultString(headers);
            request.responseCharacterSet = StringUtils.defaultString(this.responseCharacterSet);
            request.destinationFile = StringUtils.defaultString(this.destinationFile);
            request.followRedirects = String.valueOf(false);
            request.queryParams = StringUtils.EMPTY;
            request.queryParamsAreURLEncoded = String.valueOf(false);
            request.queryParamsAreFormEncoded = String.valueOf(false);
            request.formParams = StringUtils.EMPTY;
            request.formParamsAreURLEncoded = String.valueOf(false);
            request.sourceFile = StringUtils.defaultString(this.sourceFile);
            request.body = StringUtils.EMPTY;
            request.contentType = StringUtils.defaultString(this.contentType);
            request.requestCharacterSet = StringUtils.EMPTY;
            request.multipartBodies = StringUtils.EMPTY;
            request.multipartBodiesContentType = StringUtils.EMPTY;
            request.multipartFiles = StringUtils.EMPTY;
            request.multipartFilesContentType = StringUtils.EMPTY;
            request.multipartValuesAreURLEncoded = String.valueOf(false);
            request.chunkedRequestEntity = StringUtils.EMPTY;
            request.method = StringUtils.defaultString(this.method);
            request.httpClientCookieSession = null;
            request.httpClientPoolingConnectionManager = null;

            return request;
        }
    }
}
