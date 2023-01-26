/*
 * (c) Copyright 2021 Micro Focus
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
package io.cloudslang.content.rft.entities.sftp;

import com.hp.oo.sdk.content.plugin.GlobalSessionObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class SFTPCommonInputs implements IHasFTPOperation {

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String connectionTimeout;
    private final String executionTimeout;
    private final String privateKey;
    private final String characterSet;
    private final String closeSession;
    private GlobalSessionObject<Map<String, SFTPConnection>> globalSessionObject;


    @java.beans.ConstructorProperties({"host", "port", "username", "password", "proxyHost", "proxyPort", "proxyUsername", "proxyPassword", "connectionTimeout", "executionTimeout","privateKey", "globalSessionObject", "characterSet", "closeSession"})
    public SFTPCommonInputs(String host, String port, String username, String password, String proxyHost, String proxyPort, String proxyUsername, String proxyPassword, String connectionTimeout, String executionTimeout, String privateKey, GlobalSessionObject<Map<String, SFTPConnection>> globalSessionObject, String characterSet, String closeSession) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.connectionTimeout = connectionTimeout;
        this.executionTimeout = executionTimeout;
        this.privateKey = privateKey;
        this.globalSessionObject = globalSessionObject;
        this.characterSet = characterSet;
        this.closeSession = closeSession;

    }

    @NotNull
    public static SFTPCommonInputsBuilder builder() {
        return new SFTPCommonInputsBuilder();
    }

    @NotNull
    public String getHost() {
        return host;
    }

    @NotNull
    public String getPort() {
        return port;
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
    public String getConnectionTimeout() {
        return connectionTimeout;
    }

    @NotNull
    public String getExecutionTimeout() {
        return executionTimeout;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    @NotNull
    public String getCharacterSet() {
        return characterSet;
    }

    @NotNull
    public String getCloseSession() {
        return closeSession;
    }


    @Nullable
    public GlobalSessionObject<Map<String, SFTPConnection>> getGlobalSessionObject() {
        return globalSessionObject;
    }

    public void setGlobalSessionObject(GlobalSessionObject<Map<String, SFTPConnection>> globalSessionObject) {
        this.globalSessionObject = globalSessionObject;
    }

    @Override
    public @NotNull SFTPCommonInputs getSftpCommonInputs() {
        return this;
    }

    public static class SFTPCommonInputsBuilder {
        private String host = EMPTY;
        private String port = EMPTY;
        private String username = EMPTY;
        private String password = EMPTY;
        private String proxyHost = EMPTY;
        private String proxyPort = EMPTY;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;
        private String connectionTimeout = EMPTY;
        private String executionTimeout = EMPTY;
        private String privateKey = EMPTY;
        private String characterSet = EMPTY;
        private String closeSession = EMPTY;
        private GlobalSessionObject<Map<String, SFTPConnection>> globalSessionObject;

        SFTPCommonInputsBuilder() {
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder host(@NotNull final String host) {
            this.host = host;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder port(@NotNull final String port) {
            this.port = port;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder username(@NotNull final String username) {
            this.username = username;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder password(@NotNull final String password) {
            this.password = password;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder proxyPort(@NotNull final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder connectionTimeout(@NotNull final String connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder executionTimeout(@NotNull final String executionTimeout) {
            this.executionTimeout = executionTimeout;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder privateKey(@NotNull final String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder characterSet(@NotNull final String characterSet) {
            this.characterSet = characterSet;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder closeSession(@NotNull final String closeSession) {
            this.closeSession = closeSession;
            return this;
        }

        @NotNull
        public SFTPCommonInputs.SFTPCommonInputsBuilder globalSessionObject(@Nullable final GlobalSessionObject<Map<String, SFTPConnection>> globalSessionObject) {
            if (globalSessionObject != null)
                this.globalSessionObject = globalSessionObject;
            return this;
        }

        public SFTPCommonInputs build() {
            return new SFTPCommonInputs(host, port, username, password, proxyHost, proxyPort, proxyUsername, proxyPassword, connectionTimeout, executionTimeout, privateKey, globalSessionObject, characterSet, closeSession);
        }
    }
}
