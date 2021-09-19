package io.cloudslang.content.rft.entities.scp;

import org.jetbrains.annotations.NotNull;

public class SCPCopyFileInputs {

    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String localFile;
    private final String copyAction;
    private final String remoteFile;
    private final String connectionTimeout;
    private final String executionTimeout;
    private final String privateKey;
    private final String proxyHost;
    private final String proxyPort;
    private final String knownHostsPolicy;
    private final String knownHostsPath;

    public SCPCopyFileInputs(String host, String port, String username, String password, String localFile,
                             String copyAction, String remoteFile, String connectionTimeout, String executionTimeout,
                             String privateKey, String proxyHost, String proxyPort, String knownHostsPolicy, String knownHostsPath) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.localFile = localFile;
        this.copyAction = copyAction;
        this.remoteFile = remoteFile;
        this.connectionTimeout = connectionTimeout;
        this.executionTimeout = executionTimeout;
        this.privateKey = privateKey;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
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
    public String getKnownHostsPolicy() {
        return knownHostsPolicy;
    }

    @NotNull
    public String getKnownHostsPath() {
        return knownHostsPath;
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
    public String getLocalFile() {
        return localFile;
    }

    @NotNull
    public String getCopyAction() {
        return copyAction;
    }

    @NotNull
    public String getRemoteFile() {
        return remoteFile;
    }

    @NotNull
    public String getConnectionTimeout() {
        return connectionTimeout;
    }

    @NotNull
    public String getExecutionTimeout() {
        return executionTimeout;
    }

    @NotNull
    public String getPrivateKey() {
        return privateKey;
    }

    public static class SCPCopyFileInputsBuilder {
        private String host;
        private String port;
        private String username;
        private String password;
        private String localFile;
        private String copyAction;
        private String remoteFile;
        private String connectionTimeout;
        private String executionTimeout;
        private String privateKey;
        private String proxyHost;
        private String proxyPort;
        private String knownHostsPolicy;
        private String knownHostsPath;

        public SCPCopyFileInputsBuilder() {
        }

        @NotNull
        public SCPCopyFileInputsBuilder host(@NotNull final String host) {
            this.host = host;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder port(@NotNull final String port) {
            this.port = port;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder username(@NotNull final String username) {
            this.username = username;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder password(@NotNull final String password) {
            this.password = password;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder localFile(@NotNull final String localFile) {
            this.localFile = localFile;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder copyAction(@NotNull final String copyAction) {
            this.copyAction = copyAction;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder remoteFile(@NotNull final String remoteFile) {
            this.remoteFile = remoteFile;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder connectionTimeout(@NotNull final String connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder executionTimeout(@NotNull final String executionTimeout) {
            this.executionTimeout = executionTimeout;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder proxyHost(@NotNull final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder proxyPort(@NotNull final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }


        @NotNull
        public SCPCopyFileInputsBuilder knownHostsPolicy(@NotNull final String knownHostsPolicy) {
            this.knownHostsPolicy = knownHostsPolicy;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder knownHostsPath(@NotNull final String knownHostsPath) {
            this.knownHostsPath = knownHostsPath;
            return this;
        }

        @NotNull
        public SCPCopyFileInputsBuilder privateKey(@NotNull final String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public SCPCopyFileInputs build() {
            return new SCPCopyFileInputs(host, port, username, password, localFile, copyAction, remoteFile,
                    connectionTimeout, executionTimeout, privateKey,proxyHost,proxyHost,knownHostsPolicy,knownHostsPath);
        }
    }
}
