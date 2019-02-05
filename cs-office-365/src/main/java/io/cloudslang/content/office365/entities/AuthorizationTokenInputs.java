/*
 * (c) Copyright 2019 Micro Focus, L.P.
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
package io.cloudslang.content.office365.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class AuthorizationTokenInputs {
    private final String loginType;
    private final String clientId;
    private final String clientSecret;
    private final String username;
    private final String password;
    private final String authority;
    private final String resource;
    private final String proxyHost;
    private final int proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String certificatePath;
    private final String certificatePassword;

    @java.beans.ConstructorProperties({"loginType", "clientId", "clientSecret", "username", "password", "authority", "resource", "proxyHost", "proxyPort", "proxyUsername", "proxyPassword", "certificatePath", "certificatePassword"})
    private AuthorizationTokenInputs(final String loginType, final String clientId, final String clientSecret, final String username, final String password, final String authority,
                                     final String resource, final String proxyHost, final int proxyPort, final String proxyUsername, final String proxyPassword, final String certificatePath, final String certificatePassword) {
        this.loginType = loginType;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.username = username;
        this.password = password;
        this.authority = authority;
        this.resource = resource;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.certificatePath = certificatePath;
        this.certificatePassword = certificatePassword;
    }

    @NotNull
    public static AuthorizationTokenInputsBuilder builder() {
        return new AuthorizationTokenInputsBuilder();
    }

    @NotNull
    public String getLoginType() {
        return this.loginType;
    }

    @NotNull
    public String getClientId() {
        return this.clientId;
    }

    @NotNull
    public String getClientSecret() {
        return this.clientSecret;
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

    @NotNull
    public String getCertificatePath() {
        return this.certificatePath;
    }

    @NotNull
    public String getCertificatePassword() {
        return this.certificatePassword;
    }

    public static class AuthorizationTokenInputsBuilder {
        private String loginType = EMPTY;
        private String clientId = EMPTY;
        private String clientSecret = EMPTY;
        private String username = EMPTY;
        private String password = EMPTY;
        private String authority = EMPTY;
        private String resource = EMPTY;
        private String proxyHost = EMPTY;
        private int proxyPort;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;
        private String certificatePath = EMPTY;
        private String certificatePassword = EMPTY;

        AuthorizationTokenInputsBuilder() {
        }

        @NotNull
        public AuthorizationTokenInputsBuilder loginType(@NotNull final String loginType) {
            this.loginType = loginType;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder clientId(@NotNull final String clientId) {
            this.clientId = clientId;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder clientSecret(@NotNull final String clientSecret) {
            this.clientSecret = clientSecret;
            return this;
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
        public AuthorizationTokenInputsBuilder certificatePath(@NotNull final String certificatePath) {
            this.certificatePath = certificatePath;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputsBuilder certificatePassword(@NotNull final String certificatePassword) {
            this.certificatePassword = certificatePassword;
            return this;
        }

        @NotNull
        public AuthorizationTokenInputs build() {
            return new AuthorizationTokenInputs(loginType, clientId, clientSecret, username, password, authority, resource, proxyHost, proxyPort, proxyUsername, proxyPassword, certificatePath, certificatePassword);
        }

        @Override
        public String toString() {
            return String.format("AuthorizationTokenInputsBuilder(loginType=%s, clientId=%s, clientSecret=%s," +
                            " username=%s, password=%s, clientId=%s, authority=%s, resource=%s, proxyHost=%s," +
                            " proxyPort=%s, proxyUsername=%s, proxyPassword=%s, certificatePath=%s, certificatePassword=%s)",
                    this.loginType, this.clientId, this.clientSecret, this.username, this.password, this.authority,
                    this.resource, this.proxyHost, this.proxyPort, this.proxyUsername, this.proxyPassword, this.certificatePath, this.certificatePassword);
        }
    }
}