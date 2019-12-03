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

package io.cloudslang.content.hashicorp.terraform.utils;

import io.cloudslang.content.constants.InputNames;
import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class Inputs extends InputNames {
    public static final String AUTH_TOKEN = "authToken";
    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";
    public static final String PROXY_USERNAME = "proxyUsername";
    public static final String PROXY_PASSWORD = "proxyPassword";
    public static final String REQUEST_BODY = "requestBody";
    public static final String EXECUTION_TIMEOUT = "executionTimeout";
    public static final String POLLING_INTERVAL = "pollingInterval";
    public static final String ASYNC = "async";


    public static final String PAGE_NUMBER = "page[number]";
    public static final String PAGE_SIZE = "page[size]";

    private final String authToken;
    private final String organizationName;
    private final String requestBody;
    private final String terraformVersion;
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
    private final String executionTimeout;
    private final String pollingInterval;
    private final String async;
    private final String keepAlive;
    private final String responseCharacterSet;
    private final String connectionsMaxPerRoot;
    private final String connectionsMaxTotal;

    @java.beans.ConstructorProperties({"authToken", "organizationName","requestBody","terraformVersion", "proxyHost", "proxyPort", "proxyUsername",
            "proxyPassword", "trustAllRoots", "x509HostnameVerifier", "trustKeystore", "trustPassword", "connectTimeout",
            "socketTimeout","executionTimeout","pollingInterval","async", "keepAlive", "responseCharacterSet", "connectionsMaxPerRoot", "connectionsMaxTotal"})
    private Inputs(String authToken, String organizationName,String requestBody, String terraformVersion, String proxyHost, String proxyPort,
                   String proxyUsername, String proxyPassword, String trustAllRoots, String x509HostnameVerifier,
                   String trustKeystore, String trustPassword, String connectTimeout, String socketTimeout,String executionTimeout, String pollingInterval,
                   String async, String keepAlive, String responseCharacterSet, String connectionsMaxPerRoot, String connectionsMaxTotal) {
        this.authToken = authToken;
        this.organizationName = organizationName;
        this.requestBody=requestBody;
        this.terraformVersion = terraformVersion;
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
        this.executionTimeout =executionTimeout;
        this.pollingInterval = pollingInterval;
        this.async = async;
        this.keepAlive = keepAlive;
        this.responseCharacterSet = responseCharacterSet;
        this.connectionsMaxPerRoot = connectionsMaxPerRoot;
        this.connectionsMaxTotal = connectionsMaxTotal;
    }

    @NotNull
    public static Inputs.InputsBuilder builder() {
        return new Inputs.InputsBuilder();
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
    public String getTerraformVersion() { return terraformVersion; }

    @NotNull
    public String getAuthToken() {
        return this.authToken;
    }

    @NotNull
    public String getOrganizationName() {
        return this.organizationName;
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
    public String getExecutionTimeout() {
        return this.executionTimeout;
    }

    @NotNull
    public String getPollingInterval() {
        return this.pollingInterval;
    }

    @NotNull
    public String getAsync() {
        return this.async;
    }

    @NotNull
    public String getKeepAlive() {
        return this.keepAlive;
    }

    @NotNull
    public String getResponseCharacterSet() {
        return this.responseCharacterSet;
    }

    public static class InputsBuilder {
        private String authToken = EMPTY;
        private String organizationName = EMPTY;
        private String requestBody = EMPTY;
        private String terraformVersion = EMPTY;
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
        private String executionTimeout = EMPTY;
        private String pollingInterval = EMPTY;
        private String async = EMPTY;
        private String keepAlive = EMPTY;
        private String responseCharacterSet = EMPTY;
        private String connectionsMaxPerRoot = EMPTY;
        private String connectionsMaxTotal = EMPTY;

        InputsBuilder() {
        }

        @NotNull
        public Inputs.InputsBuilder authToken(@NotNull final String authToken) {
            this.authToken = authToken;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder organizationName(@NotNull final String organizationName) {
            this.organizationName = organizationName;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder requestBody(@NotNull final String requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder terraformVersion(@NotNull final String terraformVersion) {
            this.terraformVersion = terraformVersion;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder trustAllRoots(@NotNull final String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder x509HostnameVerifier(@NotNull final String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder trustKeystore(@NotNull final String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder trustPassword(@NotNull final String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder proxyPort(final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder connectTimeout(@NotNull final String connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder socketTimeout(@NotNull final String socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder executionTimeout(@NotNull final String executionTimeout) {
            this.executionTimeout = executionTimeout;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder pollingInterval(@NotNull final String pollingInterval) {
            this.pollingInterval = pollingInterval;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder async(@NotNull final String async) {
            this.async = async;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder keepAlive(@NotNull final String keepAlive) {
            this.keepAlive = keepAlive;
            return this;
        }


        @NotNull
        public Inputs.InputsBuilder responseCharacterSet(@NotNull final String responseCharacterSet) {
            this.responseCharacterSet = responseCharacterSet;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder connectionsMaxPerRoot(@NotNull final String connectionsMaxPerRoot) {
            this.connectionsMaxPerRoot = connectionsMaxPerRoot;
            return this;
        }

        @NotNull
        public Inputs.InputsBuilder connectionsMaxTotal(@NotNull final String connectionsMaxTotal) {
            this.connectionsMaxTotal = connectionsMaxTotal;
            return this;
        }

        public Inputs build() {
            return new Inputs(authToken, organizationName,requestBody, terraformVersion, proxyHost, proxyPort, proxyUsername, proxyPassword,
                    trustAllRoots, x509HostnameVerifier, trustKeystore, trustPassword, connectTimeout,
                    socketTimeout,executionTimeout,pollingInterval,async, keepAlive, responseCharacterSet, connectionsMaxPerRoot, connectionsMaxTotal);
        }
    }

}
