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
import io.cloudslang.content.mail.constants.SecurityConstants;
import io.cloudslang.content.mail.constants.TlsVersions;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

import static io.cloudslang.content.mail.constants.Constants.*;
import static io.cloudslang.content.mail.utils.InputBuilderUtils.*;
import static org.apache.commons.lang3.StringUtils.*;

public class GetMailMessageInput implements GetMailInput, DecryptableMailInput {

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
    private String folder;
    private int messageNumber;
    private boolean subjectOnly;
    private String trustKeystore;
    private String trustPassword;
    private String characterSet;
    private boolean deleteUponRetrieval;
    private String decryptionKeystore;
    private String decryptionKeyAlias;
    private String decryptionKeystorePassword;
    private boolean verifyCertificate;
    private boolean markMessageAsRead;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private int timeout = -1;
    private List<String> tlsVersions;
    private List<String> cipherSuites;

    private GetMailMessageInput() {
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


    public boolean isMarkMessageAsRead() {
        return markMessageAsRead;
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


    public boolean isVerifyCertificate() {
        return verifyCertificate;
    }


    public int getTimeout() {
        return timeout;
    }


    public String getFolder() {
        return folder;
    }


    public int getMessageNumber() {
        return messageNumber;
    }


    public boolean isSubjectOnly() {
        return subjectOnly;
    }


    public String getTrustKeystore() {
        return trustKeystore;
    }


    public String getTrustPassword() {
        return trustPassword;
    }


    public String getCharacterSet() {
        return characterSet;
    }


    public boolean isDeleteUponRetrieval() {
        return deleteUponRetrieval;
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


    public List<String> getTlsVersions() {
        return tlsVersions;
    }


    public List<String> getAllowedCiphers() {
        return cipherSuites;
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
        private String subjectOnly;
        private String trustKeystore;
        private String trustPassword;
        private String characterSet;
        private String deleteUponRetrieval;
        private String decryptionKeystore;
        private String decryptionKeyAlias;
        private String decryptionKeystorePassword;
        private String verifyCertificate;
        private String markMessageAsRead;
        private String tlsVersion;
        private String allowedCiphers;


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


        public Builder folder(String folder) {
            this.folder = folder;
            return this;
        }


        public Builder messageNumber(String messageNumber) {
            this.messageNumber = messageNumber;
            return this;
        }


        public Builder subjectOnly(String subjectOnly) {
            this.subjectOnly = subjectOnly;
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


        public Builder characterSet(String characterSet) {
            this.characterSet = characterSet;
            return this;
        }


        public Builder deleteUponRetrieval(String deleteUponRetrieval) {
            this.deleteUponRetrieval = deleteUponRetrieval;
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


        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }


        public Builder verifyCertificate(String verifyCertificate) {
            this.verifyCertificate = verifyCertificate;
            return this;
        }


        public Builder markMessageAsRead(String markMessageAsRead) {
            this.markMessageAsRead = markMessageAsRead;
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


        public Builder tlsVersion(String tlsVersion) {
            this.tlsVersion = tlsVersion;
            return this;
        }


        public Builder allowedCiphers(String allowedCiphers) {
            this.allowedCiphers = allowedCiphers;
            return this;
        }


        public GetMailMessageInput build() throws Exception {
            GetMailMessageInput input = new GetMailMessageInput();

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

            input.proxyPort = StringUtils.defaultString(proxyPort);

            input.proxyUsername = StringUtils.defaultString(proxyUsername);

            input.proxyPassword = StringUtils.defaultString(proxyPassword);

            input.timeout = buildTimeout(timeout);

            if (isEmpty(folder)) {
                throw new Exception(ExceptionMsgs.FOLDER_NOT_SPECIFIED);
            }
            input.folder = folder.trim();

            if (isEmpty(messageNumber)) {
                throw new Exception(ExceptionMsgs.MESSAGE_NUMBER_NOT_SPECIFIED);
            }
            input.messageNumber = Integer.parseInt(messageNumber);
            if (input.messageNumber < 1) {
                throw new Exception(ExceptionMsgs.MESSAGES_ARE_NUMBERED_STARTING_AT_1);
            }

            if (subjectOnly != null && subjectOnly.equalsIgnoreCase(String.valueOf(true))) {
                input.subjectOnly = true;
            } else {
                input.subjectOnly = false;
            }

            input.trustKeystore = defaultIfEmpty(trustKeystore, SecurityConstants.DEFAULT_JAVA_KEYSTORE);

            input.trustPassword = trustPassword;

            input.characterSet = characterSet;

            if (deleteUponRetrieval != null && deleteUponRetrieval.equalsIgnoreCase(String.valueOf(true))) {
                input.deleteUponRetrieval = true;
            } else {
                input.deleteUponRetrieval = false;
            }

            if (markMessageAsRead != null && markMessageAsRead.equalsIgnoreCase(String.valueOf(true))) {
                input.markMessageAsRead = true;
            } else {
                input.markMessageAsRead = false;
            }

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

            if (!isEmpty(verifyCertificate)) {
                input.verifyCertificate = Boolean.parseBoolean(verifyCertificate);
            }

            input.tlsVersions = buildTlsVersions(tlsVersion);

            input.cipherSuites = buildAllowedCiphers(allowedCiphers);

            return input;
        }
    }
}
