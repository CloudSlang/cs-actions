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
package io.cloudslang.content.abbyy.entities.inputs;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import com.hp.oo.sdk.content.plugin.SerializableSessionObject;
import io.cloudslang.content.abbyy.constants.*;
import io.cloudslang.content.abbyy.entities.others.LocationId;
import io.cloudslang.content.abbyy.entities.requests.HttpRequest;
import io.cloudslang.content.abbyy.utils.InputParser;
import io.cloudslang.content.httpclient.entities.HttpClientInputs;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.URISyntaxException;

public abstract class AbbyyInput implements HttpRequest {
    private final LocationId locationId;
    private final String applicationId;
    private final String password;
    private final String proxyHost;
    private final Short proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final Boolean trustAllRoots;
    private final String x509HostnameVerifier;
    private final String trustKeystore;
    private final String trustPassword;
    private final Integer connectTimeout;
    private final Integer socketTimeout;
    private final Boolean keepAlive;
    private final Integer connectionsMaxPerRoute;
    private final Integer connectionsMaxTotal;
    private final String responseCharacterSet;
    private final File destinationFile;
    private final File sourceFile;


    AbbyyInput(Builder builder) {
        this.locationId = builder.locationId;
        this.applicationId = builder.applicationId;
        this.password = builder.password;
        this.proxyHost = builder.proxyHost;
        this.proxyPort = builder.proxyPort;
        this.proxyUsername = builder.proxyUsername;
        this.proxyPassword = builder.proxyPassword;
        this.trustAllRoots = builder.trustAllRoots;
        this.x509HostnameVerifier = builder.x509HostnameVerifier;
        this.trustKeystore = builder.trustKeystore;
        this.trustPassword = builder.trustPassword;
        this.connectTimeout = builder.connectTimeout;
        this.socketTimeout = builder.socketTimeout;
        this.keepAlive = builder.keepAlive;
        this.connectionsMaxPerRoute = builder.connectionsMaxPerRoute;
        this.connectionsMaxTotal = builder.connectionsMaxTotal;
        this.responseCharacterSet = builder.responseCharacterSet;
        this.destinationFile = builder.destinationFile;
        this.sourceFile = builder.sourceFile;
    }


    public LocationId getLocationId() {
        return this.locationId;
    }


    public String getApplicationId() {
        return this.applicationId;
    }


    @Override
    public String getPassword() {
        return this.password;
    }


    @Override
    public String getProxyHost() {
        return this.proxyHost;
    }


    @Override
    public Short getProxyPort() {
        return this.proxyPort;
    }


    @Override
    public String getProxyUsername() {
        return this.proxyUsername;
    }


    @Override
    public String getProxyPassword() {
        return this.proxyPassword;
    }


    @Override
    public Boolean isTrustAllRoots() {
        return this.trustAllRoots;
    }


    @Override
    public String getX509HostnameVerifier() {
        return this.x509HostnameVerifier;
    }


    @Override
    public String getTrustKeystore() {
        return this.trustKeystore;
    }


    @Override
    public String getTrustPassword() {
        return this.trustPassword;
    }


    @Override
    public Integer getConnectTimeout() {
        return this.connectTimeout;
    }


    @Override
    public Integer getSocketTimeout() {
        return this.socketTimeout;
    }


    @Override
    public Boolean isKeepAlive() {
        return this.keepAlive;
    }


    @Override
    public Integer getConnectionsMaxPerRoute() {
        return this.connectionsMaxPerRoute;
    }


    @Override
    public Integer getConnectionsMaxTotal() {
        return this.connectionsMaxTotal;
    }


    @Override
    public String getResponseCharacterSet() {
        return this.responseCharacterSet;
    }


    @Override
    public @Nullable File getDestinationFile() {
        return this.destinationFile;
    }


    @Override
    public File getSourceFile() {
        return this.sourceFile;
    }


    public abstract @NotNull String getUrl() throws URISyntaxException;


    @Override
    public String getTlsVersion() {
        return SecurityConstants.TLSv1_2;
    }


    @Override
    public String getAllowedCyphers() {
        return SecurityConstants.ALLOWED_CYPHERS;
    }


    @Override
    public String getAuthType() {
        return Headers.AUTH_TYPE_BASIC;
    }


    @Override
    public String getContentType() {
        return Headers.CONTENT_TYPE_OCTET_STREAM;
    }


    @Override
    public String getMethod() {
        return HttpMethods.POST;
    }


    @Override
    public Boolean isPreemptiveAuth() {
        return true;
    }


    @Override
    public String getUsername() {
        return this.getApplicationId();
    }


    @Override
    public String getKerberosConfigFile() {
        return StringUtils.EMPTY;
    }


    @Override
    public String getKerberosLoginConfFile() {
        return StringUtils.EMPTY;
    }


    @Override
    public String getKerberosSkipPortForLookup() {
        return StringUtils.EMPTY;
    }


    @Override
    public String getKeystore() {
        return StringUtils.EMPTY;
    }


    @Override
    public String getKeystorePassword() {
        return StringUtils.EMPTY;
    }


    @Override
    public Boolean isUseCookies() {
        return false;
    }


    @Override
    public String getHeaders() {
        return StringUtils.EMPTY;
    }


    @Override
    public Boolean isFollowRedirects() {
        return true;
    }


    @Override
    public String getQueryParams() {
        return StringUtils.EMPTY;
    }


    @Override
    public Boolean isQueryParamsAreURLEncoded() {
        return false;
    }


    @Override
    public Boolean isQueryParamsAreFormEncoded() {
        return false;
    }


    @Override
    public String getFormParams() {
        return StringUtils.EMPTY;
    }


    @Override
    public Boolean isFormParamsAreURLEncoded() {
        return false;
    }


    @Override
    public String getBody() {
        return StringUtils.EMPTY;
    }


    @Override
    public String getRequestCharacterSet() {
        return StringUtils.EMPTY;
    }


    @Override
    public String getMultipartBodies() {
        return StringUtils.EMPTY;
    }


    @Override
    public String getMultipartBodiesContentType() {
        return StringUtils.EMPTY;
    }


    @Override
    public String getMultipartFiles() {
        return StringUtils.EMPTY;
    }


    @Override
    public String getMultipartFilesContentType() {
        return StringUtils.EMPTY;
    }


    @Override
    public Boolean isMultipartValuesAreURLEncoded() {
        return false;
    }


    @Override
    public Boolean isChunkedRequestEntity() {
        return false;
    }


    @Override
    public SerializableSessionObject getHttpClientCookieSession() {
        return null;
    }


    @Override
    public GlobalSessionObject getHttpClientPoolingConnectionManager() {
        return new GlobalSessionObject();
    }


    public static abstract class Builder {
        private LocationId locationId;
        private String applicationId;
        private String password;
        private String proxyHost;
        private Short proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private Boolean trustAllRoots;
        private String x509HostnameVerifier;
        private String trustKeystore;
        private String trustPassword;
        private Integer connectTimeout;
        private Integer socketTimeout;
        private Boolean keepAlive;
        private Integer connectionsMaxPerRoute;
        private Integer connectionsMaxTotal;
        private String responseCharacterSet;
        private File destinationFile;
        private File sourceFile;


        public Builder locationId(String locationId) {
            this.locationId = InputParser.parseEnum(locationId, LocationId.class, InputNames.LOCATION_ID);
            return this;
        }


        public Builder applicationId(String applicationId) {
            this.applicationId = applicationId;
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


        public Builder proxyPort(String proxyPort) {
            this.proxyPort = InputParser.parseShort(
                    StringUtils.defaultString(proxyPort, DefaultInputValues.PROXY_PORT),
                    InputNames.PROXY_PORT);
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


        public Builder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = InputParser.parseBoolean(
                    StringUtils.defaultString(trustAllRoots, DefaultInputValues.TRUST_ALL_ROOTS),
                    HttpClientInputs.TRUST_ALL_ROOTS);
            return this;
        }


        public Builder x509HostnameVerifier(String x509HostnameVerifier) {
            this.x509HostnameVerifier = StringUtils.defaultString(x509HostnameVerifier, DefaultInputValues.X_509_HOSTNAME_VERIFIER);
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


        public Builder connectTimeout(String connectTimeout) {
            this.connectTimeout = InputParser.parseInt(
                    StringUtils.defaultString(connectTimeout, DefaultInputValues.CONNECT_TIMEOUT),
                    HttpClientInputs.CONNECT_TIMEOUT);
            return this;
        }


        public Builder socketTimeout(String socketTimeout) {
            this.socketTimeout = InputParser.parseInt(
                    StringUtils.defaultString(socketTimeout, DefaultInputValues.SOCKET_TIMEOUT),
                    HttpClientInputs.SOCKET_TIMEOUT);
            return this;
        }


        public Builder keepAlive(String keepAlive) {
            this.keepAlive = InputParser.parseBoolean(
                    StringUtils.defaultString(keepAlive, DefaultInputValues.KEEP_ALIVE),
                    HttpClientInputs.KEEP_ALIVE);
            return this;
        }


        public Builder connectionsMaxPerRoute(String connectionsMaxPerRoute) {
            this.connectionsMaxPerRoute = InputParser.parseInt(
                    StringUtils.defaultString(connectionsMaxPerRoute, DefaultInputValues.CONNECTIONS_MAX_PER_ROUTE),
                    HttpClientInputs.CONNECTIONS_MAX_PER_ROUTE);
            return this;
        }


        public Builder connectionsMaxTotal(String connectionsMaxTotal) {
            this.connectionsMaxTotal = InputParser.parseInt(
                    StringUtils.defaultString(connectionsMaxTotal, DefaultInputValues.CONNECTIONS_MAX_TOTAL),
                    HttpClientInputs.CONNECTIONS_MAX_TOTAL);
            return this;
        }


        public Builder responseCharacterSet(String responseCharacterSet) {
            this.responseCharacterSet = StringUtils.defaultString(responseCharacterSet, DefaultInputValues.RESPONSE_CHARACTER_SET);
            return this;
        }


        public Builder destinationFile(String destinationFile) {
            if (StringUtils.isNotEmpty(destinationFile)) {
                this.destinationFile = new File(destinationFile);
            }
            return this;
        }


        public Builder sourceFile(String sourceFile) {
            this.sourceFile = new File(sourceFile);
            return this;
        }


        public abstract Object build();
    }
}
