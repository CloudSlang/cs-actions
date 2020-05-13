/*
 * (c) Copyright 2020 Micro Focus, L.P.
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

package io.cloudslang.content.nutanix.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixCommonInputs {

    private final String protocol;
    private final String hostname;
    private final String port;
    private final String username;
    private final String password;
    private final String nutanixAPIVersion;
    private final String requestBody;
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
    private final String responseCharacterSet;
    private final String connectionsMaxPerRoot;
    private final String connectionsMaxTotal;
    private final String pageNumber;
    private final String pageSize;


    @java.beans.ConstructorProperties({"protocol","hostname","port","username", "password","nutanixAPIVersion", "requestBody", "proxyHost", "proxyPort", "proxyUsername",
            "proxyPassword", "trustAllRoots", "x509HostnameVerifier", "trustKeystore", "trustPassword", "connectTimeout",
            "socketTimeout", "keepAlive", "responseCharacterSet", "connectionsMaxPerRoot", "connectionsMaxTotal",
            "pageNumber", "pageSize"})
    private NutanixCommonInputs(String protocol,String hostname,String port, String username, String password,String nutanixAPIVersion,String requestBody, String proxyHost, String proxyPort,
                                String proxyUsername, String proxyPassword, String trustAllRoots, String x509HostnameVerifier,
                                String trustKeystore, String trustPassword, String connectTimeout, String socketTimeout, String keepAlive, String responseCharacterSet, String connectionsMaxPerRoot, String connectionsMaxTotal, String pageNumber, String pageSize) {
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
        this.username = username;
        this.password=password;
        this.nutanixAPIVersion = nutanixAPIVersion;
        this.requestBody = requestBody;
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
        this.responseCharacterSet = responseCharacterSet;
        this.connectionsMaxPerRoot = connectionsMaxPerRoot;
        this.connectionsMaxTotal = connectionsMaxTotal;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @NotNull
    public static NutanixCommonInputs.NutanixCommonInputsBuilder builder() {
        return new NutanixCommonInputs.NutanixCommonInputsBuilder();
    }

    @NotNull
    public String getConnectionsMaxPerRoot() {
        return connectionsMaxPerRoot;
    }

    @NotNull
    public String getConnectionsMaxTotal() {
        return connectionsMaxTotal;
    }

    @NotNull
    public String getRequestBody() {
        return requestBody;
    }

    @NotNull
    public String getNutanixAPIVersion() {
        return nutanixAPIVersion;
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
    public String getPort() {
        return this.port;
    }

    @NotNull
    public String getPassword() {
        return this.password;
    }
    @NotNull
    public String getHostname() {
        return this.hostname;
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
    public String getResponseCharacterSet() {
        return this.responseCharacterSet;
    }

    public String getPageNumber() {
        return this.pageNumber;
    }

    public String getPageSize() {
        return this.pageSize;
    }

    public static class NutanixCommonInputsBuilder {

        private String protocol = EMPTY;
        private String hostname = EMPTY;
        private String port = EMPTY;
        private String username = EMPTY;
        private String password = EMPTY;
        private String nutanixAPIVersion = EMPTY;
        private String requestBody = EMPTY;
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
        private String responseCharacterSet = EMPTY;
        private String connectionsMaxPerRoot = EMPTY;
        private String connectionsMaxTotal = EMPTY;
        private String pageNumber = EMPTY;
        private String pageSize = EMPTY;

        NutanixCommonInputsBuilder() {
        }
        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder protocol(@NotNull final String protocol) {
            this.protocol = protocol;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder hostname(@NotNull final String hostname) {
            this.hostname = hostname;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder port(@NotNull final String port) {
            this.port = port;
            return this;
        }
        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder username(@NotNull final String username) {
            this.username = username;
            return this;
        }
        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder password(@NotNull final String password) {
            this.password = password;
            return this;
        }
        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder nutanixAPIVersion(@NotNull final String nutanixAPIVersion) {
            this.nutanixAPIVersion = nutanixAPIVersion;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder requestBody(@NotNull final String requestBody) {
            this.requestBody = requestBody;
            return this;
        }


        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder trustAllRoots(@NotNull final String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder x509HostnameVerifier(@NotNull final String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder trustKeystore(@NotNull final String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder trustPassword(@NotNull final String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder proxyPort(final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder connectTimeout(@NotNull final String connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder socketTimeout(@NotNull final String socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder keepAlive(@NotNull final String keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }


        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder responseCharacterSet(@NotNull final String responseCharacterSet) {
            this.responseCharacterSet = responseCharacterSet;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder connectionsMaxPerRoot(@NotNull final String connectionsMaxPerRoot) {
            this.connectionsMaxPerRoot = connectionsMaxPerRoot;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder connectionsMaxTotal(@NotNull final String connectionsMaxTotal) {
            this.connectionsMaxTotal = connectionsMaxTotal;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder pageNumber(@NotNull final String pageNumber) {
            this.pageNumber = pageNumber;
            return this;
        }

        @NotNull
        public NutanixCommonInputs.NutanixCommonInputsBuilder pageSize(@NotNull final String pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public NutanixCommonInputs build() {
            return new NutanixCommonInputs(protocol,hostname,port, username,password,nutanixAPIVersion, requestBody, proxyHost, proxyPort, proxyUsername, proxyPassword,
                    trustAllRoots, x509HostnameVerifier, trustKeystore, trustPassword, connectTimeout,
                    socketTimeout, keepAlive, responseCharacterSet, connectionsMaxPerRoot, connectionsMaxTotal, pageNumber, pageSize);
        }
    }
}
