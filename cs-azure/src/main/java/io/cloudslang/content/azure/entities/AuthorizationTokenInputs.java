/*
 * (c) Copyright 2017 EntIT Software LLC, a Micro Focus company, L.P.
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

/**
 * Created by victor on 14.10.2016.
 */

public class AuthorizationTokenInputs {
    private final String username;
    private final String password;
    private final String clientId;
    private final String authority;
    private final String resource;
    private final String proxyHost;
    private final int proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;

    @java.beans.ConstructorProperties({"username", "password", "clientId", "authority", "resource", "proxyHost", "proxyPort", "proxyUsername", "proxyPassword"})
    private AuthorizationTokenInputs(final String username, final String password, final String clientId, final String authority,
                                     final String resource, final String proxyHost, final int proxyPort, final String proxyUsername, final String proxyPassword) {
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.authority = authority;
        this.resource = resource;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
    }

    @NotNull
    public static AuthorizationTokenInputsBuilder builder() {
        return new AuthorizationTokenInputsBuilder();
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
    public String getClientId() {
        return this.clientId;
    }

    @NotNull
    public String getAuthority() {
        return this.authority;
    }

    @NotNull
    public String getResource() {
        return this.resource;
    }

    @NotNull
    public String getProxyHost() {
        return this.proxyHost;
    }

    public int getProxyPort() {
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

    public static class AuthorizationTokenInputsBuilder {
        private String username = EMPTY;
        private String password = EMPTY;
        private String clientId = EMPTY;
        private String authority = EMPTY;
        private String resource = EMPTY;
        private String proxyHost = EMPTY;
        private int proxyPort;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;

        AuthorizationTokenInputsBuilder() {
        }

        @NotNull
        public AuthorizationTokenInputsBuilder username(@NotNull final String username) {
            this.username = username;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder password(@NotNull final String password) {
            this.password = password;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder clientId(@NotNull final String clientId) {
            this.clientId = clientId;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder authority(@NotNull final String authority) {
            this.authority = authority;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder resource(@NotNull final String resource) {
            this.resource = resource;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder proxyPort(final int proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputs build() {
            return new AuthorizationTokenInputs(username, password, clientId, authority, resource, proxyHost, proxyPort, proxyUsername, proxyPassword);
        }

        @Override
        public String toString() {
            return String.format("AuthorizationTokenInputsBuilder(username=%s, password=%s, clientId=%s, authority=%s," +
                            " resource=%s, proxyHost=%s, proxyPort=%s, proxyUsername=%s, proxyPassword=%s)",
                    this.username, this.password, this.clientId, this.authority, this.resource, this.proxyHost,
                    this.proxyPort, this.proxyUsername, this.proxyPassword);
        }
    }
}
