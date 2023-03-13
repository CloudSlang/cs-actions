package io.cloudslang.content.sharepoint.entities;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class HttpInput {
    private String host;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String tlsVersion;
    private String allowedCyphers;
    private String trustAllRoots;
    private String x509HostnameVerifier;
    private String trustKeystore;
    private String trustPassword;
    private String keystore;
    private String keystorePassword;
    private String username;
    private String password;
    private String connectTimeout;
    private String executionTimeout;

    public HttpInput(String host, String proxyHost, String proxyPort, String proxyUsername, String proxyPassword,
                     String tlsVersion, String allowedCyphers, String trustAllRoots, String x509HostnameVerifier,
                     String trustKeystore, String trustPassword, String keystore, String keystorePassword,
                     String username, String password, String connectTimeout, String executionTimeout) {
        this.host = host;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.tlsVersion = tlsVersion;
        this.allowedCyphers = allowedCyphers;
        this.trustAllRoots = trustAllRoots;
        this.x509HostnameVerifier = x509HostnameVerifier;
        this.trustKeystore = trustKeystore;
        this.trustPassword = trustPassword;
        this.keystore = keystore;
        this.keystorePassword = keystorePassword;
        this.username = username;
        this.password = password;
        this.connectTimeout = connectTimeout;
        this.executionTimeout = executionTimeout;
    }

    public String getExecutionTimeout() {
        return executionTimeout;
    }

    public void setExecutionTimeout(String executionTimeout) {
        this.executionTimeout = executionTimeout;
    }

    public String getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(String connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getTlsVersion() {
        return tlsVersion;
    }

    public void setTlsVersion(String tlsVersion) {
        this.tlsVersion = tlsVersion;
    }

    public String getAllowedCyphers() {
        return allowedCyphers;
    }

    public void setAllowedCyphers(String allowedCyphers) {
        this.allowedCyphers = allowedCyphers;
    }

    public String getTrustAllRoots() {
        return trustAllRoots;
    }

    public void setTrustAllRoots(String trustAllRoots) {
        this.trustAllRoots = trustAllRoots;
    }

    public String getX509HostnameVerifier() {
        return x509HostnameVerifier;
    }

    public void setX509HostnameVerifier(String x509HostnameVerifier) {
        this.x509HostnameVerifier = x509HostnameVerifier;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public void setTrustKeystore(String trustKeystore) {
        this.trustKeystore = trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }

    public void setTrustPassword(String trustPassword) {
        this.trustPassword = trustPassword;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public static GetHttpInputBuilder builder() {
        return new GetHttpInputBuilder();
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public static class GetHttpInputBuilder {
        private String host = EMPTY;
        private String connectTimeout = EMPTY;
        private String proxyHost = EMPTY;
        private String proxyPort = EMPTY;
        private String proxyUsername = EMPTY;
        private String proxyPassword = EMPTY;
        private String tlsVersion = EMPTY;
        private String allowedCyphers = EMPTY;
        private String trustAllRoots = EMPTY;
        private String x509HostnameVerifier = EMPTY;
        private String trustKeystore = EMPTY;
        private String trustPassword = EMPTY;
        private String keystore = EMPTY;
        private String keystorePassword = EMPTY;
        private String username = EMPTY;
        private String password = EMPTY;
        private String executionTimeout = EMPTY;

        public GetHttpInputBuilder() {
        }

        public GetHttpInputBuilder host(final String host) {
            this.host = host;
            return this;
        }
        public GetHttpInputBuilder executionTimeout(final String executionTimeout) {
            this.executionTimeout = executionTimeout;
            return this;
        }
        public GetHttpInputBuilder connectTimeout(final String connectTimeout) {
            this.connectTimeout = connectTimeout;
            return this;
        }
        public GetHttpInputBuilder username(final String username) {
            this.username = username;
            return this;
        }
        public GetHttpInputBuilder password(final String password) {
            this.password = password;
            return this;
        }

        public GetHttpInputBuilder trustKeystore(final String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        public GetHttpInputBuilder keystorePassword(final String keystorePassword) {
            this.keystorePassword = keystorePassword;
            return this;
        }

        public GetHttpInputBuilder keystore(final String keystore) {
            this.keystore = keystore;
            return this;
        }

        public GetHttpInputBuilder trustPassword(final String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        public GetHttpInputBuilder proxyPassword(final String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public GetHttpInputBuilder tlsVersion(final String tlsVersion) {
            this.tlsVersion = tlsVersion;
            return this;
        }

        public GetHttpInputBuilder proxyUsername(final String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public GetHttpInputBuilder allowedCyphers(final String allowedCyphers) {
            this.allowedCyphers = allowedCyphers;
            return this;
        }

        public GetHttpInputBuilder trustAllRoots(final String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        public GetHttpInputBuilder x509HostnameVerifier(final String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        public GetHttpInputBuilder proxyHost(final String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public GetHttpInputBuilder proxyPort(final String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        public HttpInput build() {
            return new HttpInput(host,proxyHost,proxyPort,proxyUsername,proxyPassword,tlsVersion,allowedCyphers,
                    trustAllRoots, x509HostnameVerifier,trustKeystore,trustPassword,keystore,keystorePassword,username,password,connectTimeout,executionTimeout);
        }
    }
}
