/*
 * (c) Copyright 2021 EntIT Software LLC, a Micro Focus company, L.P.
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
package io.cloudslang.content.winrm.entities;

public class WinRMInputs {
    private final String host;
    private final String port;
    private final String protocol;
    private final String username;
    private final String password;
    private final String command;
    private final String authType;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String tlsVersion;
    private final String trustAllRoots;
    private final String x509HostnameVerifier;
    private final String trustKeystore;
    private final String trustPassword;
    private final String keystore;
    private final String keystorePassword;
    private final int operationTimeout;
    private final String requestNewKerberosTicket;
    private final String workingDirectory;
    private final String configurationName;
    private final String commandType;
    private final String kerberosConfFile;
    private final String kerberosLoginConfFile;
    private final String domain;
    private final String useSubjectCredsOnly;


    private WinRMInputs(String host, String port, String protocol, String username, String password, String command,
                        String authType, String proxyHost, String proxyPort, String proxyUsername, String proxyPassword,
                        String tlsVersion, String trustAllRoots, String x509HostnameVerifier, String trustKeystore, String trustPassword,
                        String keystore, String keystorePassword, int operationTimeout, String requestNewKerberosTicket,
                        String workingDirectory, String configurationName, String commandType, String kerberosConfFile,
                        String kerberosLoginConfFile, String domain, String useSubjectCredsOnly) {
        this.host = host;
        this.port = port;
        this.command = command;
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.authType = authType;
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyUsername = proxyUsername;
        this.proxyPassword = proxyPassword;
        this.tlsVersion = tlsVersion;
        this.trustAllRoots = trustAllRoots;
        this.x509HostnameVerifier = x509HostnameVerifier;
        this.trustKeystore = trustKeystore;
        this.trustPassword = trustPassword;
        this.keystore = keystore;
        this.keystorePassword = keystorePassword;
        this.operationTimeout = operationTimeout;
        this.requestNewKerberosTicket = requestNewKerberosTicket;
        this.workingDirectory = workingDirectory;
        this.configurationName = configurationName;
        this.commandType = commandType;
        this.kerberosConfFile = kerberosConfFile;
        this.kerberosLoginConfFile = kerberosLoginConfFile;
        this.domain = domain;
        this.useSubjectCredsOnly = useSubjectCredsOnly;
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

    public String getCommand() {
        return command;
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

    public String getTlsVersion() { return tlsVersion; }

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

    public String getKeystore() {
        return keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public int getOperationTimeout() {
        return operationTimeout;
    }

    public String getRequestNewKerberosTicket() { return requestNewKerberosTicket; }

    public String getWorkingDirectory() { return workingDirectory; }

    public String getConfigurationName(){ return configurationName; }

    public String getCommandType(){return commandType; }

    public String getKerberosConfFile(){return kerberosConfFile; }

    public String getKerberosLoginConfFile(){return kerberosLoginConfFile; }

    public String getDomain(){return domain; }

    public String getUseSubjectCredsOnly(){return useSubjectCredsOnly; }

    public static class WinRMBuilder {
        private String host;
        private String port;
        private String protocol;
        private String username;
        private String password;
        private String command;
        private String authType;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private String tlsVersion;
        private String trustAllRoots;
        private String x509HostnameVerifier;
        private String trustKeystore;
        private String trustPassword;
        private String keystore;
        private String keystorePassword;
        private String requestNewKerberosTicket;
        private int operationTimeout;
        private String workingDirectory;
        private String configurationName;
        private String commandType;
        private String kerberosConfFile;
        private String kerberosLoginConfFile;
        private String domain;
        private String useSubjectCredsOnly;

        public WinRMBuilder() {
        }

        public WinRMInputs build() {
            return new WinRMInputs(host, port, protocol, username, password, command, authType, proxyHost, proxyPort,
                    proxyUsername, proxyPassword, tlsVersion, trustAllRoots, x509HostnameVerifier, trustKeystore, trustPassword,
                    keystore, keystorePassword, operationTimeout, requestNewKerberosTicket, workingDirectory,
                    configurationName, commandType, kerberosConfFile, kerberosLoginConfFile, domain, useSubjectCredsOnly);
        }

        public WinRMBuilder host(String host) {
            this.host = host;
            return this;
        }

        public WinRMBuilder port(String port) {
            this.port = port;
            return this;
        }

        public WinRMBuilder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public WinRMBuilder username(String username) {
            this.username = username;
            return this;
        }

        public WinRMBuilder password(String password) {
            this.password = password;
            return this;
        }

        public WinRMBuilder command(String command) {
            this.command = command;
            return this;
        }

        public WinRMBuilder authType(String authType) {
            this.authType = authType;
            return this;
        }

        public WinRMBuilder proxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }

        public WinRMBuilder proxyPort(String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }

        public WinRMBuilder proxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }

        public WinRMBuilder proxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }

        public WinRMBuilder tlsVersion(String tlsVersion) {
            this.tlsVersion = tlsVersion;
            return this;
        }

        public WinRMBuilder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }

        public WinRMBuilder x509HostnameVerifier(String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        public WinRMBuilder trustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }

        public WinRMBuilder trustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }

        public WinRMBuilder keystore(String keystore) {
            this.keystore = keystore;
            return this;
        }

        public WinRMBuilder keystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
            return this;
        }

        public WinRMBuilder operationTimeout(int operationTimeout) {
            this.operationTimeout = operationTimeout;
            return this;
        }

        public WinRMBuilder requestNewKerberosToken(String requestNewKerberosToken) {
            this.requestNewKerberosTicket = requestNewKerberosToken;
            return this;
        }

        public WinRMBuilder workingDirectory(String workingDirectory) {
            this.workingDirectory = workingDirectory;
            return this;
        }

        public WinRMBuilder configurationName(String configurationName){
            this.configurationName = configurationName;
            return this;
        }

        public WinRMBuilder commandType(String commandType){
            this.commandType = commandType;
            return this;
        }

        public WinRMBuilder kerberosConfFile(String kerberosConfFile){
            this.kerberosConfFile = kerberosConfFile;
            return this;
        }

        public WinRMBuilder kerberosLoginConfFile(String kerberosLoginConfFile){
            this.kerberosLoginConfFile = kerberosLoginConfFile;
            return this;
        }

        public WinRMBuilder domain(String domain){
            this.domain = domain;
            return this;
        }

        public WinRMBuilder useSubjectCredsOnly(String useSubjectCredsOnly){
            this.useSubjectCredsOnly = useSubjectCredsOnly;
            return this;
        }
    }
}
