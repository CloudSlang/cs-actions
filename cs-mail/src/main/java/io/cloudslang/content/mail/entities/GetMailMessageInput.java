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
import org.apache.commons.lang3.StringUtils;

import static io.cloudslang.content.mail.constants.Constants.*;
import static org.apache.commons.lang3.StringUtils.*;

public class GetMailMessageInput extends GetMailBaseInput implements DecryptableMessageInput {

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
    private int timeout = -1;
    private boolean verifyCertificate;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private boolean markMessageAsRead;


    private GetMailMessageInput(){
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


    public static class Builder extends GetMailBaseInput.Builder {

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
        private String timeout;
        private String verifyCertificate;
        private String markMessageAsRead;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;


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

        @Override
        public Builder hostname(String hostname) {
            return (Builder) super.hostname(hostname);
        }

        @Override
        public Builder port(String port) {
            return (Builder) super.port(port);
        }

        @Override
        public Builder protocol(String protocol) {
            return (Builder) super.protocol(protocol);
        }

        @Override
        public Builder username(String username) {
            return (Builder) super.username(username);
        }

        @Override
        public Builder password(String password) {
            return (Builder) super.password(password);
        }

        @Override
        public Builder trustAllRoots(String trustAllRoots) {
            return (Builder) super.trustAllRoots(trustAllRoots);
        }

        @Override
        public Builder enableSSL(String enableSSL) {
            return (Builder) super.enableSSL(enableSSL);
        }

        @Override
        public Builder enableTLS(String enableTLS) {
            return (Builder) super.enableTLS(enableTLS);
        }

        @Override
        public Builder keystore(String keystore) {
            return (Builder) super.keystore(keystore);
        }

        @Override
        public Builder keystorePassword(String keystorePassword) {
            return (Builder) super.keystorePassword(keystorePassword);
        }


        public GetMailMessageInput build() throws Exception {
            GetMailMessageInput input = new GetMailMessageInput();
            super.build(input);

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

            input.decryptionKeystore = decryptionKeystore;
            if (input.isEncryptedMessage()) {
                if (!input.decryptionKeystore.startsWith(HTTP)) {
                    input.decryptionKeystore = FILE + decryptionKeystore;
                }

                input.decryptionKeyAlias = decryptionKeyAlias;
                if (null == decryptionKeyAlias) {
                    decryptionKeyAlias = StringUtils.EMPTY;
                }

                input.decryptionKeystorePassword = decryptionKeystorePassword;
                if (null == input.decryptionKeystorePassword) {
                    input.decryptionKeystorePassword = StringUtils.EMPTY;
                }
            }

            if (isNotEmpty(timeout)) {
                input.timeout = Integer.parseInt(timeout);
                if (input.timeout <= 0) {
                    throw new Exception(ExceptionMsgs.TIMEOUT_MUST_BE_POSITIVE);
                }
                input.timeout *= ONE_SECOND; //timeouts in seconds
            }

            if (!isEmpty(verifyCertificate)) {
                input.verifyCertificate = Boolean.parseBoolean(verifyCertificate);
            }

            input.proxyHost = proxyHost;

            input.proxyPort = proxyPort;

            input.proxyUsername = proxyUsername;

            input.proxyPassword = proxyPassword;

            return input;
        }
    }
}
