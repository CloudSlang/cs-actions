/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
*/
package io.cloudslang.content.utilities.entities;

/**
 * Created by Tirla Florin-Alin on 28/11/2017.
 **/
public class OsDetectorInputs {
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final String nmapTimeout;
    private final String nmapPath;
    private final String nmapArguments;
    private final String nmapValidator;
    private final String privateKeyFile;
    private final String privateKeyData;
    private final String knownHostsPolicy;
    private final String knownHostsPath;
    private final String allowedCiphers;
    private final String agentForwarding;
    private final String protocol;
    private final String authType;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String trustAllRoots;
    private final String x509HostnameVerifier;
    private final String trustKeystore;
    private final String trustPassword;
    private final String kerberosConfFile;
    private final String kerberosLoginConfFile;
    private final String kerberosSkipPortForLookup;
    private final String keystore;
    private final String keystorePassword;
    private final String winrmLocale;
    private final String powerShellTimeout;
    private final String sshTimeout;
    private final String sshConnectTimeout;

    private OsDetectorInputs(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;
        this.nmapTimeout = builder.nmapTimeout;
        this.sshTimeout = builder.sshTimeout;
        this.sshConnectTimeout = builder.sshConnectTimeout;
        this.powerShellTimeout = builder.powerShellTimeout;
        this.nmapPath = builder.nmapPath;
        this.nmapArguments = builder.nmapArguments;
        this.nmapValidator = builder.nmapValidator;
        this.privateKeyFile = builder.privateKeyFile;
        this.privateKeyData = builder.privateKeyData;
        this.knownHostsPolicy = builder.knownHostsPolicy;
        this.knownHostsPath = builder.knownHostsPath;
        this.allowedCiphers = builder.allowedCiphers;
        this.agentForwarding = builder.agentForwarding;
        this.protocol = builder.protocol;
        this.authType = builder.authType;
        this.proxyHost = builder.proxyHost;
        this.proxyPort = builder.proxyPort;
        this.proxyUsername = builder.proxyUsername;
        this.proxyPassword = builder.proxyPassword;
        this.trustAllRoots = builder.trustAllRoots;
        this.x509HostnameVerifier = builder.x509HostnameVerifier;
        this.trustKeystore = builder.trustKeystore;
        this.trustPassword = builder.trustPassword;
        this.kerberosConfFile = builder.kerberosConfFile;
        this.kerberosLoginConfFile = builder.kerberosLoginConfFile;
        this.kerberosSkipPortForLookup = builder.kerberosSkipPortForLookup;
        this.keystore = builder.keystore;
        this.keystorePassword = builder.keystorePassword;
        this.winrmLocale = builder.winrmLocale;
    }

    public String getSshTimeout() {
        return sshTimeout;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getNmapTimeout() {
        return nmapTimeout;
    }

    public String getNmapPath() {
        return nmapPath;
    }

    public String getNmapArguments() {
        return nmapArguments;
    }

    public String getNmapValidator() {
        return nmapValidator;
    }

    public String getPrivateKeyFile() {
        return privateKeyFile;
    }

    public String getPrivateKeyData() {
        return privateKeyData;
    }

    public String getKnownHostsPolicy() {
        return knownHostsPolicy;
    }

    public String getKnownHostsPath() {
        return knownHostsPath;
    }

    public String getAllowedCiphers() {
        return allowedCiphers;
    }

    public String getAgentForwarding() {
        return agentForwarding;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getAuthType() {
        return authType;
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

    public String getTrustAllRoots() {
        return trustAllRoots;
    }

    public String getX509HostnameVerifier() {
        return x509HostnameVerifier;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }

    public String getKerberosConfFile() {
        return kerberosConfFile;
    }

    public String getKerberosLoginConfFile() {
        return kerberosLoginConfFile;
    }

    public String getKerberosSkipPortForLookup() {
        return kerberosSkipPortForLookup;
    }

    public String getKeystore() {
        return keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getWinrmLocale() {
        return winrmLocale;
    }

    public String getPassword() {
        return password;
    }

    public String getPowerShellTimeout() {
        return powerShellTimeout;
    }

    public String getSshConnectTimeout() {
        return sshConnectTimeout;
    }

    public static class Builder {
        private String host;
        private String port;
        private String username;
        private String password;
        private String nmapTimeout;
        private String nmapPath;
        private String nmapArguments;
        private String nmapValidator;
        private String privateKeyFile;
        private String privateKeyData;
        private String knownHostsPolicy;
        private String knownHostsPath;
        private String allowedCiphers;
        private String agentForwarding;
        private String protocol;
        private String authType;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private String trustAllRoots;
        private String x509HostnameVerifier;
        private String trustKeystore;
        private String trustPassword;
        private String kerberosConfFile;
        private String kerberosLoginConfFile;
        private String kerberosSkipPortForLookup;
        private String keystore;
        private String keystorePassword;
        private String winrmLocale;
        private String sshTimeout;
        private String powerShellTimeout;
        private String sshConnectTimeout;

        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        public Builder withPort(String port) {
            this.port = port;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withNmapTimeout(String nmapTimeout) {
            this.nmapTimeout = nmapTimeout;
            return this;
        }

        public Builder withNmapPath(String nmapPath) {
            this.nmapPath = nmapPath;
            return this;
        }

        public Builder withNmapArguments(String nmapArguments) {
            this.nmapArguments = nmapArguments;
            return this;
        }

        public Builder withNmapValidator(String nmapValidator) {
            this.nmapValidator = nmapValidator;
            return this;
        }

        public Builder withPrivateKeyFile(String privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
            return this;
        }

        public Builder withPrivateKeyData(String privateKeyData) {
            this.privateKeyData = privateKeyData;
            return this;
        }

        public Builder withKnownHostsPolicy(String knownHostsPolicy) {
            this.knownHostsPolicy = knownHostsPolicy;
            return this;
        }

        public Builder withKnownHostsPath(String knownHostsPath) {
            this.knownHostsPath = knownHostsPath;
            return this;
        }

        public Builder withAllowedCiphers(String allowedCiphers) {
            this.allowedCiphers = allowedCiphers;
            return this;
        }

        public Builder withAgentForwarding(String agentForwarding) {
            this.agentForwarding = agentForwarding;
            return this;
        }

        public Builder withProtocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder withAuthType(String authType) {
            this.authType = authType;
            return this;
        }

        public Builder withProxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public Builder withProxyPort(String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        public Builder withProxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public Builder withProxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public Builder withTrustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        public Builder withX509HostnameVerifier(String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        public Builder withTrustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        public Builder withTrustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        public Builder withKerberosConfFile(String kerberosConfFile) {
            this.kerberosConfFile = kerberosConfFile;
            return this;
        }

        public Builder withKerberosLoginConfFile(String kerberosLoginConfFile) {
            this.kerberosLoginConfFile = kerberosLoginConfFile;
            return this;
        }

        public Builder withKerberosSkipPortForLookup(String kerberosSkipPortForLookup) {
            this.kerberosSkipPortForLookup = kerberosSkipPortForLookup;
            return this;
        }

        public Builder withKeystore(String keystore) {
            this.keystore = keystore;
            return this;
        }

        public Builder withKeystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
            return this;
        }

        public Builder withWinrmLocale(String winrmLocale) {
            this.winrmLocale = winrmLocale;
            return this;
        }

        public OsDetectorInputs build() {
            return new OsDetectorInputs(this);
        }

        public Builder withSshTimeout(String sshTimeout) {
            this.sshTimeout = sshTimeout;
            return this;
        }

        public Builder withPowerShellTimeout(String powerShellTimeout) {
            this.powerShellTimeout = powerShellTimeout;
            return this;
        }

        public Builder withSshConnectTimeout(String sshConnectTimeout) {
            this.sshConnectTimeout = sshConnectTimeout;
            return this;
        }
    }
}
