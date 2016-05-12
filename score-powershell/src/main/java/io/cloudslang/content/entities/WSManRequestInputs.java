package io.cloudslang.content.entities;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by giloan on 5/12/2016.
 */
public class WSManRequestInputs {

    private String host;
    private String port;
    private String protocol;
    private String username;
    private String password;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private int maxEnvelopeSize;
    private String trustAllRoots;
    private String x509HostnameVerifier;
    private String keystore;
    private String keystorePassword;
    private String trustKeystore;
    private String trustPassword;
    private String script;
    private String winrmLocale;
    private int operationTimeout;

    public WSManRequestInputs(WSManRequestInputsBuilder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.protocol = builder.protocol;
        this.username = builder.username;
        this.password = builder.password;
        this.proxyHost = builder.proxyHost;
        this.proxyPort = builder.proxyPort;
        this.proxyUsername = builder.proxyUsername;
        this.proxyPassword = builder.proxyPassword;
        this.maxEnvelopeSize = builder.maxEnvelopeSize;
        this.trustAllRoots = builder.trustAllRoots;
        this.x509HostnameVerifier = builder.x509HostnameVerifier;
        this.keystore = builder.keystore;
        this.keystorePassword = builder.keystorePassword;
        this.trustKeystore = builder.trustKeystore;
        this.trustPassword = builder.trustPassword;
        this.script = builder.script;
        this.winrmLocale = builder.winrmLocale;
        this.operationTimeout = builder.operationTimeout;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public int getMaxEnvelopeSize() {
        return maxEnvelopeSize;
    }

    public String getTrustAllRoots() {
        return trustAllRoots;
    }

    public String getX509HostnameVerifier() {
        return x509HostnameVerifier;
    }

    public String getKeystore() {
        return keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }

    public String getScript() {
        return script;
    }

    public String getWinrmLocale() {
        return winrmLocale;
    }

    public int getOperationTimeout() {
        return operationTimeout;
    }

    public static class WSManRequestInputsBuilder {
        private String host;
        private String port;
        private String protocol;
        private String username;
        private String password;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private int maxEnvelopeSize;
        private String trustAllRoots;
        private String x509HostnameVerifier;
        private String keystore;
        private String keystorePassword;
        private String trustKeystore;
        private String trustPassword;
        private String script;
        private String winrmLocale;
        private int operationTimeout;

        public WSManRequestInputs build() {
            return new WSManRequestInputs(this);
        }

        public WSManRequestInputsBuilder withHost(String host) {
            this.host = host;
            return this;
        }

        public WSManRequestInputsBuilder withPort(String port) {
            this.port = StringUtils.isBlank(port) ? InputDefaults.PORT.getValue() : port;
            return this;
        }

        public WSManRequestInputsBuilder withProtocol(String protocol) {
            this.protocol = StringUtils.isBlank(protocol) ? InputDefaults.PROTOCOL.getValue() : protocol;
            return this;
        }

        public WSManRequestInputsBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public WSManRequestInputsBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public WSManRequestInputsBuilder withProxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public WSManRequestInputsBuilder withProxyPort(String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        public WSManRequestInputsBuilder withProxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public WSManRequestInputsBuilder withProxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public WSManRequestInputsBuilder withMaxEnvelopeSize(String maxEnvelopeSize) {
            maxEnvelopeSize = StringUtils.isBlank(maxEnvelopeSize) ? InputDefaults.MAX_ENVELOPE_SIZE.getValue() : maxEnvelopeSize;
            this.maxEnvelopeSize = Integer.parseInt(maxEnvelopeSize);
            return this;
        }

        public WSManRequestInputsBuilder withTrustAllRoots(String trustAllRoots) {
            this.trustAllRoots = StringUtils.isBlank(trustAllRoots) ? InputDefaults.TRUST_ALL_ROOTS.getValue() : trustAllRoots;
            return this;
        }

        public WSManRequestInputsBuilder withX509HostnameVerifier(String x509HostnameVerifier) {
            this.x509HostnameVerifier = StringUtils.isBlank(x509HostnameVerifier) ? InputDefaults.X_509_HOSTNAME_VERIFIER.getValue() : x509HostnameVerifier;
            return this;
        }

        public WSManRequestInputsBuilder withKeystore(String keystore) {
            this.keystore = keystore;
            return this;
        }

        public WSManRequestInputsBuilder withKeystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
            return this;
        }

        public WSManRequestInputsBuilder withTrustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        public WSManRequestInputsBuilder withTrustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        public WSManRequestInputsBuilder withScript(String script) {
            this.script = script;
            return this;
        }

        public WSManRequestInputsBuilder withWinrmLocale(String winrmLocale) {
            this.winrmLocale = StringUtils.isBlank(winrmLocale) ? InputDefaults.WINRM_LOCALE.getValue() : winrmLocale;
            return this;
        }

        public WSManRequestInputsBuilder withOperationTimeout(String operationTimeout) {
            operationTimeout = StringUtils.isBlank(operationTimeout) ? InputDefaults.OPERATION_TIMEOUT.getValue() : operationTimeout;
            this.operationTimeout = Integer.parseInt(operationTimeout);
            return this;
        }
    }
}
