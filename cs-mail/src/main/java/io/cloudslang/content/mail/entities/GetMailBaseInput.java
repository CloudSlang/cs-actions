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

import io.cloudslang.content.mail.constants.ExceptionMsgs;
import io.cloudslang.content.mail.constants.ImapPropNames;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.constants.SecurityConstants;
import org.apache.commons.lang3.StringUtils;

import static io.cloudslang.content.mail.constants.Constants.ONE_SECOND;
import static org.apache.commons.lang3.StringUtils.*;

public abstract class GetMailBaseInput implements ProxyInput {

    protected String hostname;
    protected Short port;
    protected String protocol;
    protected String username;
    protected String password;
    protected boolean trustAllRoots;
    protected boolean enableSSL;
    protected boolean enableTLS;
    protected String keystore;
    protected String keystorePassword;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private int timeout = -1;

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

    public String getProxyPort() {
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

    public static abstract class Builder {

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


        protected abstract GetMailBaseInput build() throws Exception;


        protected void build(GetMailBaseInput input) throws Exception {
            if (isEmpty(hostname)) {
                throw new Exception(ExceptionMsgs.HOST_NOT_SPECIFIED);
            }
            input.hostname = hostname.trim();

            if (!StringUtils.isEmpty(port)) {
                input.port = Short.parseShort(port);
                if(input.port < 1) {
                    throw new Exception(ExceptionMsgs.INVALID_PORT_NUMBER);
                }
            }

            input.protocol = protocol;
            if (isEmpty(protocol) && isEmpty(port)) {
                throw new Exception(io.cloudslang.content.mail.constants.ExceptionMsgs.SPECIFY_PORT_OR_PROTOCOL_OR_BOTH);
            } else if ((protocol != null && !protocol.isEmpty()) && (!protocol.equalsIgnoreCase(ImapPropNames.IMAP)) &&
                    (!protocol.equalsIgnoreCase(PopPropNames.POP3)) && (!protocol.equalsIgnoreCase(ImapPropNames.IMAP4)) &&
                    isEmpty(port)) {
                throw new Exception(ExceptionMsgs.SPECIFY_PORT_FOR_PROTOCOL);
            } else if (isEmpty(protocol) && !isEmpty(port) &&
                    (!port.equalsIgnoreCase(ImapPropNames.PORT)) && (!port.equalsIgnoreCase(PopPropNames.POP3_PORT))) {
                throw new Exception(io.cloudslang.content.mail.constants.ExceptionMsgs.SPECIFY_PROTOCOL_FOR_GIVEN_PORT);
            } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(ImapPropNames.PORT)) {
                input.protocol = ImapPropNames.IMAP;
            } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(PopPropNames.POP3_PORT)) {
                input.protocol = PopPropNames.POP3;
            } else if (protocol.trim().equalsIgnoreCase(PopPropNames.POP3) && isEmpty(port)) {
                input.port = Short.parseShort(PopPropNames.POP3_PORT);
            } else if (protocol.trim().equalsIgnoreCase(ImapPropNames.IMAP) && isEmpty(port)) {
                input.port = Short.parseShort(ImapPropNames.PORT);
            } else if (protocol.trim().equalsIgnoreCase(ImapPropNames.IMAP4) && (isEmpty(port))) {
                input.port = Short.parseShort(ImapPropNames.PORT);
            }
            //The protocol should be given in lowercase to be recognised.
            input.protocol = input.protocol.toLowerCase();
            if (input.protocol.trim().equalsIgnoreCase(ImapPropNames.IMAP4)) {
                input.protocol = ImapPropNames.IMAP;
            }

            if (isEmpty(username)) {
                throw new Exception(ExceptionMsgs.USERNAME_NOT_SPECIFIED);
            }
            input.username = username;

            input.password = password;

            input.trustAllRoots = StringUtils.isEmpty(trustAllRoots) ? true : Boolean.parseBoolean(trustAllRoots);

            input.enableTLS = enableTLS != null && enableTLS.equalsIgnoreCase(String.valueOf(true));

            input.enableSSL = enableSSL != null && enableSSL.equalsIgnoreCase(String.valueOf(true));

            input.keystore = defaultIfEmpty(keystore, SecurityConstants.DEFAULT_JAVA_KEYSTORE);

            input.keystorePassword = keystorePassword;

            input.proxyHost = proxyHost;

            input.proxyPort = proxyPort;

            input.proxyUsername = proxyUsername;

            input.proxyPassword = proxyPassword;

            if (isNotEmpty(timeout)) {
                input.timeout = Integer.parseInt(timeout);
                if (input.timeout <= 0) {
                    throw new Exception(ExceptionMsgs.TIMEOUT_MUST_BE_POSITIVE);
                }
                input.timeout *= ONE_SECOND; //timeouts in seconds
            }
        }
    }
}
