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
package io.cloudslang.content.mail.entities;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.mail.utils.InputBuilderUtils.*;

public class GetMailMessageCountInput implements GetMailInput {

    private String hostname;
    private Short port;
    private String protocol;
    private String username;
    private String password;
    private boolean trustAllRoots;
    private boolean enableSSL;
    private boolean enableTLS;
    private String keystore;
    private String keystorePassword;
    private String proxyHost;
    private Short proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private int timeout = -1;
    private String folder;
    private String trustKeystore;
    private String trustPassword;
    private List<String> tlsVersions;
    private List<String> allowedCiphers;


    private GetMailMessageCountInput() {
    }


    public String getHostname() {
        return hostname;
    }


    public Short getPort() {
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


    public boolean isTrustAllRoots() {
        return trustAllRoots;
    }


    public boolean isEnableSSL() {
        return enableSSL;
    }


    public boolean isEnableTLS() {
        return enableTLS;
    }


    public String getKeystore() {
        return keystore;
    }


    public String getKeystorePassword() {
        return keystorePassword;
    }


    public String getProxyHost() {
        return proxyHost;
    }


    public Short getProxyPort() {
        return proxyPort;
    }


    public String getProxyUsername() {
        return proxyUsername;
    }


    public String getProxyPassword() {
        return proxyPassword;
    }


    public int getTimeout() {
        return timeout;
    }


    public String getFolder() {
        return folder;
    }


    public String getTrustKeystore() {
        return trustKeystore;
    }


    public String getTrustPassword() {
        return trustPassword;
    }


    public List<String> getTlsVersions() {
        return tlsVersions;
    }


    public List<String> getAllowedCiphers() {
        return allowedCiphers;
    }


    public static class Builder {

        private String hostname;
        private String port;
        private String protocol;
        private String username;
        private String password;
        private String trustAllRoots;
        private String enableSSL;
        private String enableTLS;
        private String keystore;
        private String keystorePassword;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private String timeout;
        private String folder;
        private String trustKeystore;
        private String trustPassword;
        private String tlsVersion;
        private String allowedCiphers;


        public Builder folder(String folder) {
            this.folder = folder;
            return this;
        }


        public Builder trustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }


        public Builder trustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }


        public Builder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }


        public Builder port(String port) {
            this.port = port;
            return this;
        }


        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }


        public Builder username(String username) {
            this.username = username;
            return this;
        }


        public Builder password(String password) {
            this.password = password;
            return this;
        }


        public Builder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }


        public Builder enableSSL(String enableSSL) {
            this.enableSSL = enableSSL;
            return this;
        }


        public Builder enableTLS(String enableTLS) {
            this.enableTLS = enableTLS;
            return this;
        }


        public Builder keystore(String keystore) {
            this.keystore = keystore;
            return this;
        }


        public Builder keystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
            return this;
        }


        public Builder proxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }


        public Builder proxyPort(String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }


        public Builder proxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }


        public Builder proxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }


        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }


        public Builder tlsVersion(String tlsVersion) {
            this.tlsVersion = tlsVersion;
            return this;
        }


        public Builder allowedCiphers(String allowedCiphers) {
            this.allowedCiphers = allowedCiphers;
            return this;
        }


        public GetMailMessageCountInput build() throws Exception {
            GetMailMessageCountInput input = new GetMailMessageCountInput();

            input.hostname = buildHostname(hostname);

            input.port = buildPort(port, false);

            Map<String, Object> portAndProtocol = buildPortAndProtocol(protocol, port);

            if (portAndProtocol.containsKey("port")) {
                input.port = (Short) portAndProtocol.get("port");
            }

            input.protocol = (String) portAndProtocol.get("protocol");

            input.username = buildUsername(username, true);

            input.password = buildPassword(password);

            input.trustAllRoots = buildTrustAllRoots(trustAllRoots);

            input.enableTLS = buildEnableTLS(enableTLS);

            input.enableSSL = buildEnableSSL(enableSSL);

            input.keystore = buildKeystore(keystore);

            input.keystorePassword = keystorePassword;

            input.proxyHost = StringUtils.defaultString(proxyHost);

            input.proxyPort = buildPort(proxyPort, false);

            input.proxyUsername = StringUtils.defaultString(proxyUsername);

            input.proxyPassword = StringUtils.defaultString(proxyPassword);

            input.timeout = buildTimeout(timeout);

            input.folder = StringUtils.defaultString(folder);

            input.trustKeystore = StringUtils.defaultString(trustKeystore);

            input.trustPassword = StringUtils.defaultString(trustPassword);

            input.tlsVersions = buildTlsVersions(tlsVersion);

            input.allowedCiphers = buildAllowedCiphers(allowedCiphers);

            return input;
        }
    }
}
