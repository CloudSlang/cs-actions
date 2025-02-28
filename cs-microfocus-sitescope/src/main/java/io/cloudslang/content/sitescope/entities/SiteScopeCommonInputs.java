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



package io.cloudslang.content.sitescope.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SiteScopeCommonInputs {
    private final String host;
    private final String port;
    private final String protocol;
    private final String username;
    private final String password;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String trustAllRoots;
    private final String x509HostnameVerifier;
    private final String keystore;
    private final String keystorePassword;
    private final String trustKeystore;
    private final String trustPassword;
    private final String connectTimeout;
    private final String socketTimeout;
    private final String keepAlive;
    private final String connectionsMaxPerRoute;
    private final String connectionsMaxTotal;
    private final String responseCharacterSet;


    @java.beans.ConstructorProperties({"host", "port", "protocol", "username", "password","proxyHost", "proxyPort",
            "proxyUsername", "proxyPassword", "trustAllRoots", "x509HostnameVerifier", "keystore", "keystorePassword",
            "trustKeystore", "trustPassword", "connectTimeout", "socketTimeout", "keepAlive", "connectionsMaxPerRoute",
            "connectionsMaxTotal", "responseCharacterSet"})
    private SiteScopeCommonInputs(String host, String port, String protocol, String username, String password,
                                  String proxyHost, String proxyPort, String proxyUsername, String proxyPassword, String trustAllRoots,
                                  String x509HostnameVerifier, String keystore, String keystorePassword, String trustKeystore,
                                  String trustPassword, String connectTimeout, String socketTimeout, String keepAlive,
                                  String connectionsMaxPerRoute, String connectionsMaxTotal, String responseCharacterSet) {

        this.host = host;
        this.port = port;
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.trustAllRoots = trustAllRoots;
        this.x509HostnameVerifier = x509HostnameVerifier;
        this.keystore = keystore;
        this.keystorePassword = keystorePassword;
        this.trustKeystore = trustKeystore;
        this.trustPassword = trustPassword;
        this.connectTimeout = connectTimeout;
        this.socketTimeout = socketTimeout;
        this.keepAlive = keepAlive;
        this.connectionsMaxPerRoute = connectionsMaxPerRoute;
        this.connectionsMaxTotal = connectionsMaxTotal;
        this.responseCharacterSet = responseCharacterSet;
    }

    @NotNull
    public static SiteScopeCommonInputsBuilder builder() {
        return new SiteScopeCommonInputsBuilder();
    }

    @NotNull
    public String getHost() {
        return this.host;
    }

    @NotNull
    public String getPort() {
        return this.port;
    }

    @NotNull
    public String getProtocol() {
        return this.protocol;
    }

    @NotNull
    public String getUsername() {
        return this.username;
    }

    @NotNull
    public String getPassword() {
        return this.password;
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
    public String getTrustAllRoots() {
        return this.trustAllRoots;
    }

    @NotNull
    public String getX509HostnameVerifier() {
        return this.x509HostnameVerifier;
    }

    @NotNull
    public String getKeystore() {
        return this.keystore;
    }

    @NotNull
    public String getKeystorePassword() {
        return this.keystorePassword;
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

    @NotNull
    public String getResponseCharacterSet() {
        return this.responseCharacterSet;
    }

    public static class SiteScopeCommonInputsBuilder {
        private String host = EMPTY;
        private String port = EMPTY;
        private String protocol = EMPTY;
        private String username = EMPTY;
        private String password = EMPTY;
        private String proxyHost = EMPTY;
        private String proxyPort = EMPTY;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;
        private String trustAllRoots = EMPTY;
        private String x509HostnameVerifier = EMPTY;
        private String trustKeystore = EMPTY;
        private String keystore = EMPTY;
        private String keystorePassword = EMPTY;
        private String trustPassword = EMPTY;
        private String connectTimeout = EMPTY;
        private String socketTimeout = EMPTY;
        private String keepAlive = EMPTY;
        private String connectionsMaxPerRoute = EMPTY;
        private String connectionsMaxTotal = EMPTY;
        private String responseCharacterSet = EMPTY;

        SiteScopeCommonInputsBuilder() {
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder host(@NotNull final String host) {
            this.host = host;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder port(@NotNull final String port) {
            this.port = port;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder protocol(@NotNull final String protocol) {
            this.protocol = protocol;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder username(@NotNull final String username) {
            this.username = username;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder password(@NotNull final String password) {
            this.password = password;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder trustAllRoots(@NotNull final String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder x509HostnameVerifier(@NotNull final String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder keystore(@NotNull final String keystore) {
            this.keystore = keystore;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder keystorePassword(@NotNull final String keystorePassword) {
            this.keystorePassword = keystorePassword;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder trustKeystore(@NotNull final String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder trustPassword(@NotNull final String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder proxyPort(final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder connectTimeout(@NotNull final String connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder socketTimeout(@NotNull final String socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder keepAlive(@NotNull final String keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder connectionsMaxPerRoute(@NotNull final String connectionsMaxPerRoute) {
            this.connectionsMaxPerRoute = connectionsMaxPerRoute;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder connectionsMaxTotal(@NotNull final String connectionsMaxTotal) {
            this.connectionsMaxTotal = connectionsMaxTotal;
            return this;
        }

        @NotNull
        public SiteScopeCommonInputs.SiteScopeCommonInputsBuilder responseCharacterSet(@NotNull final String responseCharacterSet) {
            this.responseCharacterSet = responseCharacterSet;
            return this;
        }

        public SiteScopeCommonInputs build() {
            return new SiteScopeCommonInputs(host, port, protocol, username, password, proxyHost, proxyPort, proxyUsername,
                    proxyPassword, trustAllRoots, x509HostnameVerifier, keystore, keystorePassword, trustKeystore,
                    trustPassword, connectTimeout, socketTimeout, keepAlive, connectionsMaxPerRoute, connectionsMaxTotal,
                    responseCharacterSet);
        }
    }
}
