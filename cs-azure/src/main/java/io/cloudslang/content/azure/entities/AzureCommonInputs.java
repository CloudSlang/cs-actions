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
/*
 * (c) Copyright 2021 Micro Focus, L.P.
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

package io.cloudslang.content.azure.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AzureCommonInputs {

    private final String authToken;
    private final String resourceGroupName;
    private final String subscriptionId;
    private final String apiVersion;
    private final String location;
    private final String jobName;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String trustAllRoots;
    private final String x509HostnameVerifier;
    private final String trustKeystore;
    private final String trustPassword;

    @java.beans.ConstructorProperties({"authToken", "resourceGroupName", "subscriptionId", "apiVersion", "location", "jobName", "proxyHost", "proxyPort", "proxyUsername", "proxyPassword", "trustAllRoots", "x509HostnameVerifier", "trustKeystore", "trustPassword"})
    public AzureCommonInputs(String authToken, String resourceGroupName, String subscriptionId, String apiVersion, String location, String jobName,String proxyHost, String proxyPort, String proxyUsername, String proxyPassword, String trustAllRoots, String x509HostnameVerifier, String trustKeystore, String trustPassword) {
        this.authToken = authToken;
        this.resourceGroupName = resourceGroupName;
        this.subscriptionId = subscriptionId;
        this.apiVersion = apiVersion;
        this.location = location;
        this.jobName = jobName;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.trustAllRoots = trustAllRoots;
        this.x509HostnameVerifier = x509HostnameVerifier;
        this.trustKeystore = trustKeystore;
        this.trustPassword = trustPassword;
    }

    @NotNull
    public static AzureCommonInputsBuilder builder() {
        return new AzureCommonInputsBuilder();
    }

    @NotNull
    public String getAuthToken() {
        return authToken;
    }

    @NotNull
    public String getResourceGroupName() {
        return resourceGroupName;
    }

    @NotNull
    public String getSubscriptionId() {
        return subscriptionId;
    }

    @NotNull
    public String getApiVersion() {
        return apiVersion;
    }

    @NotNull
    public String getLocation() {
        return location;
    }

    @NotNull
    public String getJobName() {
        return jobName;
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


    public static final class AzureCommonInputsBuilder {
        private String authToken = EMPTY;
        private String resourceGroupName = EMPTY;
        private String subscriptionId = EMPTY;
        private String apiVersion = EMPTY;
        private String location = EMPTY;
        private String jobName = EMPTY;
        private String proxyHost = EMPTY;
        private String proxyPort = EMPTY;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;
        private String trustAllRoots = EMPTY;
        private String x509HostnameVerifier = EMPTY;
        private String trustKeystore = EMPTY;
        private String trustPassword = EMPTY;

        AzureCommonInputsBuilder() {
        }

        @NotNull
        public AzureCommonInputsBuilder authToken(@NotNull final String authToken) {
            this.authToken = authToken;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder resourceGroupName(@NotNull final String resourceGroupName) {
            this.resourceGroupName = resourceGroupName;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder subscriptionId(@NotNull final String subscriptionId) {
            this.subscriptionId = subscriptionId;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder apiVersion(@NotNull final String apiVersion) {
            this.apiVersion = apiVersion;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder location(@NotNull final String location) {
            this.location = location;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder jobName(@NotNull final String jobName) {
            this.jobName = jobName;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder proxyPort(@NotNull final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder trustAllRoots(@NotNull final String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder x509HostnameVerifier(@NotNull final String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder trustKeystore(@NotNull final String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        @NotNull
        public AzureCommonInputsBuilder trustPassword(@NotNull final String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        public AzureCommonInputs build() {
            return new AzureCommonInputs(authToken, resourceGroupName, subscriptionId, apiVersion, location, jobName, proxyHost, proxyPort, proxyUsername, proxyPassword, trustAllRoots, x509HostnameVerifier, trustKeystore, trustPassword);
        }
    }
}
