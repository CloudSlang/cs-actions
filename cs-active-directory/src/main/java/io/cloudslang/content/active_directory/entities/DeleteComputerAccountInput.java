/*
 * Copyright 2021-2024 Open Text
 * This program and the accompanying materials
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

package io.cloudslang.content.active_directory.entities;

import io.cloudslang.content.active_directory.constants.Constants;

import java.util.List;

import static io.cloudslang.content.active_directory.utils.InputBuilderUtils.*;
import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class DeleteComputerAccountInput implements DeleteComputerInterface {

    private String host;
    private String distinguishedName;
    private String computerCommonName;
    private String username;
    private String password;
    private String protocol;
    private String proxyHost;
    private int proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private String tlsVersion;
    private List<String> allowedCiphers;
    private String x509HostnameVerifier;
    private boolean trustAllRoots;
    private String trustKeystore;
    private String trustPassword;
    private boolean escapeChars;
    private String timeout;

    private DeleteComputerAccountInput() {
    }

    public String getHost() {
        return host;
    }

    public String getDistinguishedName() {
        return distinguishedName;
    }

    public String getComputerCommonName() {
        return computerCommonName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProtocol() {
        return protocol;
    }

    public Boolean getTrustAllRoots() {
        return trustAllRoots;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }

    public Boolean getEscapeChars() {
        return escapeChars;
    }

    public String getTimeout() {
        return timeout;
    }

    public String getTlsVersion() {
        return tlsVersion;
    }

    public List<String> getAllowedCiphers() {
        return allowedCiphers;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getX509HostnameVerifier() {
        return x509HostnameVerifier;
    }

    public static class Builder {

        private String host;
        private String distinguishedName;
        private String computerCommonName;
        private String username;
        private String password;
        private String protocol;
        private String trustAllRoots;
        private String trustKeystore;
        private String trustPassword;
        private String escapeChars;
        private String timeout;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private String x509HostnameVerifier;
        private String tlsVersion;
        private String allowedCiphers;


        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder distinguishedName(String distinguishedName) {
            this.distinguishedName = distinguishedName;
            return this;
        }

        public Builder computerCommonName(String computerCommonName) {
            this.computerCommonName = computerCommonName;
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

        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
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


        public Builder escapeChars(String escapeChars) {
            this.escapeChars = escapeChars;
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

        public Builder x509HostnameVerifier(String x509HostnameVerifier) {
            this.x509HostnameVerifier = x509HostnameVerifier;
            return this;
        }

        public DeleteComputerAccountInput build() throws Exception {
            DeleteComputerAccountInput input = new DeleteComputerAccountInput();

            input.host = buildHost(host, true);

            input.distinguishedName = buildDistinguishedName(distinguishedName, true);

            input.computerCommonName = buildComputerCommonName(computerCommonName, true);

            input.username = buildUsername(username);

            input.password = buildPassword(password);

            input.trustAllRoots = buildTrustAllRoots(trustAllRoots);

            input.protocol = buildProtocol(protocol);

            input.trustKeystore = defaultIfEmpty(trustKeystore, Constants.DEFAULT_JAVA_KEYSTORE);

            input.trustPassword = trustPassword;

            input.escapeChars = buildEscapeChars(escapeChars);

            input.timeout = timeout;

            input.proxyHost = proxyHost;

            input.proxyUsername = proxyUsername;

            input.proxyPassword = proxyPassword;

            input.x509HostnameVerifier = x509HostnameVerifier;

            input.tlsVersion = buildTlsVersions(tlsVersion);

            input.allowedCiphers = buildAllowedCiphers(allowedCiphers);

            input.proxyPort = Integer.parseInt(addVerifyPort(proxyPort));


            return input;
        }
    }
}
