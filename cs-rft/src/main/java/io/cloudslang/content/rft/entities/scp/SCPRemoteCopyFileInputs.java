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
package io.cloudslang.content.rft.entities.scp;

import org.jetbrains.annotations.NotNull;

public class SCPRemoteCopyFileInputs {
    private final String sourceHost;
    private final String sourcePort;
    private final String sourceUsername;
    private final String sourcePassword;
    private final String sourcePrivateKey;
    private final String sourcePath;
    private final String destinationHost;
    private final String destinationPort;
    private final String destinationUsername;
    private final String destinationPassword;
    private final String destinationPrivateKey;
    private final String destinationPath;
    private final String connectionTimeout;
    private final String executionTimeout;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String knownHostsPolicy;
    private final String knownHostsPath;

    public SCPRemoteCopyFileInputs(String sourceHost, String sourcePort, String sourceUsername, String sourcePassword,
                                   String sourcePrivateKey, String sourcePath, String destinationHost,
                                   String destinationPort, String destinationUsername, String destinationPassword,
                                   String destinationPrivateKey, String destinationPath, String connectionTimeout,
                                   String executionTimeout, String proxyHost, String proxyPort, String proxyUsername,
                                   String proxyPassword, String knownHostsPolicy, String knownHostsPath) {
        this.sourceHost = sourceHost;
        this.sourcePort = sourcePort;
        this.sourceUsername = sourceUsername;
        this.sourcePassword = sourcePassword;
        this.sourcePrivateKey = sourcePrivateKey;
        this.sourcePath = sourcePath;
        this.destinationHost = destinationHost;
        this.destinationPort = destinationPort;
        this.destinationUsername = destinationUsername;
        this.destinationPassword = destinationPassword;
        this.destinationPrivateKey = destinationPrivateKey;
        this.destinationPath = destinationPath;
        this.connectionTimeout = connectionTimeout;
        this.executionTimeout = executionTimeout;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.knownHostsPolicy = knownHostsPolicy;
        this.knownHostsPath = knownHostsPath;
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
    public String getKnownHostsPolicy() {
        return knownHostsPolicy;
    }

    @NotNull
    public String getKnownHostsPath() {
        return knownHostsPath;
    }

    @NotNull
    public String getSourceHost() {
        return sourceHost;
    }

    @NotNull
    public String getSourcePort() {
        return sourcePort;
    }

    @NotNull
    public String getSourceUsername() {
        return sourceUsername;
    }

    @NotNull
    public String getSourcePassword() {
        return sourcePassword;
    }

    @NotNull
    public String getSourcePrivateKey() {
        return sourcePrivateKey;
    }

    @NotNull
    public String getSourcePath() {
        return sourcePath;
    }

    @NotNull
    public String getDestinationHost() {
        return destinationHost;
    }

    @NotNull
    public String getDestinationPort() {
        return destinationPort;
    }

    @NotNull
    public String getDestinationUsername() {
        return destinationUsername;
    }

    @NotNull
    public String getDestinationPassword() {
        return destinationPassword;
    }

    @NotNull
    public String getDestinationPrivateKey() {
        return destinationPrivateKey;
    }

    @NotNull
    public String getDestinationPath() {
        return destinationPath;
    }

    @NotNull
    public String getConnectionTimeout() {
        return connectionTimeout;
    }

    @NotNull
    public String getExecutionTimeout() {
        return executionTimeout;
    }

    public static class SCPRemoteCopyFileInputsBuilder {
        private String sourceHost;
        private String sourcePort;
        private String sourceUsername;
        private String sourcePassword;
        private String sourcePrivateKey;
        private String sourcePath;
        private String destinationHost;
        private String destinationPort;
        private String destinationUsername;
        private String destinationPassword;
        private String destinationPrivateKey;
        private String destinationPath;
        private String connectionTimeout;
        private String executionTimeout;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private String knownHostsPolicy;
        private String knownHostsPath;

        public SCPRemoteCopyFileInputsBuilder() {
        }

        public SCPRemoteCopyFileInputsBuilder sourceHost(@NotNull final String sourceHost) {
            this.sourceHost = sourceHost;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder sourcePort(@NotNull final String sourcePort) {
            this.sourcePort = sourcePort;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder sourceUsername(@NotNull final String sourceUsername) {
            this.sourceUsername = sourceUsername;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder sourcePassword(@NotNull final String sourcePassword) {
            this.sourcePassword = sourcePassword;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder sourcePrivateKey(@NotNull final String sourcePrivateKey) {
            this.sourcePrivateKey = sourcePrivateKey;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder sourcePath(@NotNull final String sourcePath) {
            this.sourcePath = sourcePath;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder destinationHost(@NotNull final String destinationHost) {
            this.destinationHost = destinationHost;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder destinationPort(@NotNull final String destinationPort) {
            this.destinationPort = destinationPort;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder destinationUsername(@NotNull final String destinationUsername) {
            this.destinationUsername = destinationUsername;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder destinationPassword(@NotNull final String destinationPassword) {
            this.destinationPassword = destinationPassword;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder destinationPrivateKey(@NotNull final String destinationPrivateKey) {
            this.destinationPrivateKey = destinationPrivateKey;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder destinationPath(@NotNull final String destinationPath) {
            this.destinationPath = destinationPath;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder connectionTimeout(@NotNull final String connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder proxyPort(@NotNull final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        @NotNull
        public SCPRemoteCopyFileInputsBuilder proxyUsername(@NotNull final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        @NotNull
        public SCPRemoteCopyFileInputsBuilder proxyPassword(@NotNull final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder knownHostsPolicy(@NotNull final String knownHostsPolicy) {
            this.knownHostsPolicy = knownHostsPolicy;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder knownHostsPath(@NotNull final String knownHostsPath) {
            this.knownHostsPath = knownHostsPath;
            return this;
        }

        public SCPRemoteCopyFileInputsBuilder executionTimeout(@NotNull final String executionTimeout) {
            this.executionTimeout = executionTimeout;
            return this;
        }

        public SCPRemoteCopyFileInputs build() {
            return new SCPRemoteCopyFileInputs(sourceHost, sourcePort, sourceUsername, sourcePassword, sourcePrivateKey,
                    sourcePath, destinationHost, destinationPort, destinationUsername, destinationPassword,
                    destinationPrivateKey, destinationPath, connectionTimeout, executionTimeout, proxyHost,
                    proxyPort, proxyUsername, proxyPassword, knownHostsPolicy, knownHostsPath);
        }
    }
}
