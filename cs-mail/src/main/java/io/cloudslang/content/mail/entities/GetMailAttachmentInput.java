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

public class GetMailAttachmentInput implements GetMailInput, DecryptableMailInput {

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
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private int timeout = -1;
    private String folder;
    private int messageNumber;
    private String attachmentName;
    private String characterSet;
    private String destination;
    private boolean overwrite;
    private String decryptionKeystore;
    private String decryptionKeyAlias;
    private String decryptionKeystorePassword;
    private List<String> tlsVersions;
    private List<String> allowedCiphers;


    private GetMailAttachmentInput() {
    }


    public String getFolder() {
        return folder;
    }


    public int getMessageNumber() {
        return messageNumber;
    }


    public String getAttachmentName() {
        return attachmentName;
    }


    public String getCharacterSet() {
        return characterSet;
    }


    public String getDestination() {
        return destination;
    }


    public boolean isOverwrite() {
        return overwrite;
    }


    public String getDecryptionKeystore() {
        return decryptionKeystore;
    }


    public String getDecryptionKeyAlias() {
        return decryptionKeyAlias;
    }


    public void setDecryptionKeyAlias(String value) {
        this.decryptionKeyAlias = value;
    }


    public String getDecryptionKeystorePassword() {
        return decryptionKeystorePassword;
    }


    public boolean isEncryptedMessage() {
        return !StringUtils.isEmpty(decryptionKeystore);
    }


    @Override
    public boolean isVerifyCertificate() {
        return false;
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
        private String messageNumber;
        private String attachmentName;
        private String characterSet;
        private String destination;
        private String overwrite;
        private String decryptionKeystore;
        private String decryptionKeyAlias;
        private String decryptionKeystorePassword;
        private String tlsVersion;
        private String allowedCiphers;


        public Builder folder(String folder) {
            this.folder = folder;
            return this;
        }


        public Builder messageNumber(String messageNumber) {
            this.messageNumber = messageNumber;
            return this;
        }


        public Builder attachmentName(String attachmentName) {
            this.attachmentName = attachmentName;
            return this;
        }


        public Builder characterSet(String characterSet) {
            this.characterSet = characterSet;
            return this;
        }


        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }


        public Builder overwrite(String overwrite) {
            this.overwrite = overwrite;
            return this;
        }


        public Builder decryptionKeystore(String decryptionKeystore) {
            this.decryptionKeystore = decryptionKeystore;
            return this;
        }


        public Builder decryptionKeyAlias(String decryptionKeyAlias) {
            this.decryptionKeyAlias = decryptionKeyAlias;
            return this;
        }


        public Builder decryptionKeystorePassword(String decryptionKeystorePassword) {
            this.decryptionKeystorePassword = decryptionKeystorePassword;
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


        public GetMailAttachmentInput build() throws Exception {
            GetMailAttachmentInput input = new GetMailAttachmentInput();

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

            input.keystorePassword = StringUtils.defaultString(keystorePassword);

            input.proxyHost = StringUtils.defaultString(proxyHost);

            input.proxyPort = StringUtils.defaultString(proxyPort);

            input.proxyUsername = StringUtils.defaultString(proxyUsername);

            input.proxyPassword = StringUtils.defaultString(proxyPassword);

            input.timeout = buildTimeout(timeout);

            input.folder = StringUtils.defaultString(folder);

            if (StringUtils.isEmpty(messageNumber)) {
                throw new Exception(io.cloudslang.content.mail.constants.ExceptionMsgs.MESSAGE_NUMBER_NOT_SPECIFIED);
            }
            input.messageNumber = Integer.parseInt(messageNumber);
            if (input.messageNumber < 1) {
                throw new Exception(io.cloudslang.content.mail.constants.ExceptionMsgs.MESSAGES_ARE_NUMBERED_STARTING_AT_1);
            }

            input.attachmentName = attachmentName;

            input.characterSet = characterSet;

            input.destination = destination;

            input.overwrite = !StringUtils.isEmpty(overwrite) && Boolean.parseBoolean(overwrite.trim());

            Map<String, String> decryption = buildDecryptionKeystore(decryptionKeystore, decryptionKeyAlias,
                    decryptionKeystorePassword);

            if (decryption.containsKey("decryptionKeystore")) {
                input.decryptionKeystore = decryption.get("decryptionKeystore");
            }

            if (decryption.containsKey("decryptionKeyAlias")) {
                input.decryptionKeyAlias = decryption.get("decryptionKeyAlias");
            }

            if (decryption.containsKey("decryptionKeystorePassword")) {
                input.decryptionKeystorePassword = decryption.get("decryptionKeystorePassword");
            }

            input.tlsVersions = buildTlsVersions(tlsVersion);

            input.allowedCiphers = buildAllowedCiphers(allowedCiphers);

            return input;
        }
    }
}