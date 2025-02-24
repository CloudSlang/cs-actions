/*
 * Copyright 2021-2025 Open Text
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


package io.cloudslang.content.microsoftAD.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AzureActiveDirectoryCommonInputs {

    private final String authToken;
    private final String userPrincipalName;
    private final String userId;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String trustAllRoots;
    private final String x509HostnameVerifier;
    private final String trustKeystore;
    private final String trustPassword;
    private final String connectTimeout;
    private final String socketTimeout;
    private final String keepAlive;
    private final String connectionsMaxPerRoute;
    private final String connectionsMaxTotal;


    @java.beans.ConstructorProperties({"authToken", "userPrincipalName", "userId", "proxyHost", "proxyPort", "proxyUsername",
            "proxyPassword", "trustAllRoots", "x509HostnameVerifier", "trustKeystore", "trustPassword", "connectTimeout",
            "socketTimeout", "keepAlive", "connectionsMaxPerRoute", "connectionsMaxTotal"})
    private AzureActiveDirectoryCommonInputs(String authToken, String userPrincipalName, String userId, String proxyHost, String proxyPort,
                                             String proxyUsername, String proxyPassword, String trustAllRoots, String x509HostnameVerifier,
                                             String trustKeystore, String trustPassword, String connectTimeout, String socketTimeout,
                                             String keepAlive, String connectionsMaxPerRoute, String connectionsMaxTotal) {
        this.authToken = authToken;
        this.userPrincipalName = userPrincipalName;
        this.userId = userId;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.trustAllRoots = trustAllRoots;
        this.x509HostnameVerifier = x509HostnameVerifier;
        this.trustKeystore = trustKeystore;
        this.trustPassword = trustPassword;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.keepAlive = keepAlive;
        this.connectionsMaxPerRoute = connectionsMaxPerRoute;
        this.connectionsMaxTotal = connectionsMaxTotal;
    }

    @NotNull
    public static AzureActiveDirectoryCommonInputsBuilder builder() {
        return new AzureActiveDirectoryCommonInputsBuilder();
    }

    @NotNull
    public String getAuthToken() {
        return this.authToken;
    }

    @NotNull
    public String getUserPrincipalName() {
        return this.userPrincipalName;
    }

    @NotNull
    public String getProxyHost() {
        return this.proxyHost;
    }

    @NotNull
    public String getProxyPort() {
        return this.proxyPort;
    }

    @NotNull
    public String getProxyUsername() {
        return this.proxyUsername;
    }

    @NotNull
    public String getProxyPassword() {
        return this.proxyPassword;
    }

    @NotNull
    public String getUserId() {
        return this.userId;
    }

    @NotNull
    public String getTrustAllRoots() {
        return this.trustAllRoots;
    }

    @NotNull
    public String getX509HostnameVerifier() {
        return this.x509HostnameVerifier;
    }

    @NotNull
    public String getTrustKeystore() {
        return this.trustKeystore;
    }

    @NotNull
    public String getTrustPassword() {
        return this.trustPassword;
    }

    @NotNull
    public String getConnectTimeout() {
        return this.connectTimeout;
    }

    @NotNull
    public String getSocketTimeout() {
        return this.socketTimeout;
    }

    @NotNull
    public String getKeepAlive() {
        return this.keepAlive;
    }

    @NotNull
    public String getConnectionsMaxPerRoute() {
        return this.connectionsMaxPerRoute;
    }

    @NotNull
    public String getConnectionsMaxTotal() {
        return this.connectionsMaxTotal;
    }

    public static class AzureActiveDirectoryCommonInputsBuilder {
        private String authToken = EMPTY;
        private String userPrincipalName = EMPTY;
        private String userId = EMPTY;
        private String proxyHost = EMPTY;
        private String proxyPort = EMPTY;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;
        private String trustAllRoots = EMPTY;
        private String x509HostnameVerifier = EMPTY;
        private String trustKeystore = EMPTY;
        private String trustPassword = EMPTY;
        private String connectTimeout = EMPTY;
        private String socketTimeout = EMPTY;
        private String keepAlive = EMPTY;
        private String connectionsMaxPerRoute = EMPTY;
        private String connectionsMaxTotal = EMPTY;

        AzureActiveDirectoryCommonInputsBuilder() {
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder authToken(@NotNull final String authToken) {
            this.authToken = authToken;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder userPrincipalName(@NotNull final String userPrincipalName) {
            this.userPrincipalName = userPrincipalName;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder userId(@NotNull final String userId) {
            this.userId = userId;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder trustAllRoots(@NotNull final String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder x509HostnameVerifier(@NotNull final String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder trustKeystore(@NotNull final String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder trustPassword(@NotNull final String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder proxyPort(final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder connectTimeout(@NotNull final String connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder socketTimeout(@NotNull final String socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder keepAlive(@NotNull final String keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder connectionsMaxPerRoute(@NotNull final String connectionsMaxPerRoute) {
            this.connectionsMaxPerRoute = connectionsMaxPerRoute;
            return this;
        }

        @NotNull
        public AzureActiveDirectoryCommonInputs.AzureActiveDirectoryCommonInputsBuilder connectionsMaxTotal(@NotNull final String connectionsMaxTotal) {
            this.connectionsMaxTotal = connectionsMaxTotal;
            return this;
        }

        public AzureActiveDirectoryCommonInputs build() {
            return new AzureActiveDirectoryCommonInputs(authToken, userPrincipalName, userId, proxyHost, proxyPort, proxyUsername, proxyPassword,
                    trustAllRoots, x509HostnameVerifier, trustKeystore, trustPassword, connectTimeout,
                    socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal);
        }
    }
}
