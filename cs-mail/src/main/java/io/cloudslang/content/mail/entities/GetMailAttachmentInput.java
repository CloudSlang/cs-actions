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


import io.cloudslang.content.mail.utils.Constants;
import io.cloudslang.content.mail.utils.Constants.*;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class GetMailAttachmentInput {

    private String hostname;
    private Short port;
    private String protocol;
    private String username;
    private String password;
    private String folder;
    private boolean trustAllRoots;
    private boolean enableSSL;
    private boolean enableTLS;
    private String keystore;
    private String keystorePassword;
    private int messageNumber;
    private String attachmentName;
    private String characterSet;
    private String destination;
    private boolean overwrite;
    private String decryptionKeystore;
    private String decryptionKeyAlias;
    private String decryptionKeystorePassword;

    private GetMailAttachmentInput() {
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

    public String getFolder() {
        return folder;
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

    public static class Builder {

        private String hostname;
        private String port;
        private String protocol;
        private String username;
        private String password;
        private String folder;
        private String trustAllRoots;
        private String enableSSL;
        private String enableTLS;
        private String keystore;
        private String keystorePassword;
        private String messageNumber;
        private String attachmentName;
        private String characterSet;
        private String destination;
        private String overwrite;
        private String decryptionKeystore;
        private String decryptionKeyAlias;
        private String decryptionKeystorePassword;

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


        public Builder folder(String folder) {
            this.folder = folder;
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


        public GetMailAttachmentInput build() throws Exception {
            GetMailAttachmentInput input = new GetMailAttachmentInput();

            input.hostname = hostname;

            if (!StringUtils.isEmpty(port)) {
                input.port = Short.parseShort(port);
            }

            input.protocol = protocol;
            if (isEmpty(protocol) && isEmpty(port)) {
                throw new Exception(ExceptionMsgs.SPECIFY_PORT_OR_PROTOCOL_OR_BOTH);
            } else if ((protocol != null && !protocol.isEmpty()) && (!protocol.equalsIgnoreCase(IMAPProps.IMAP)) &&
                    (!protocol.equalsIgnoreCase(POPProps.POP3)) && (!protocol.equalsIgnoreCase(IMAPProps.IMAP4)) &&
                    isEmpty(port)) {
                throw new Exception(ExceptionMsgs.SPECIFY_PORT_FOR_PROTOCOL);
            } else if (isEmpty(protocol) && !isEmpty(port) &&
                    (!port.equalsIgnoreCase(IMAPProps.PORT)) && (!port.equalsIgnoreCase(POPProps.POP3_PORT))) {
                throw new Exception(ExceptionMsgs.SPECIFY_PROTOCOL_FOR_GIVEN_PORT);
            } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(IMAPProps.PORT)) {
                input.protocol = IMAPProps.IMAP;
            } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(POPProps.POP3_PORT)) {
                input.protocol = POPProps.POP3;
            } else if (protocol.trim().equalsIgnoreCase(POPProps.POP3) && isEmpty(port)) {
                input.port = Short.parseShort(POPProps.POP3_PORT);
            } else if (protocol.trim().equalsIgnoreCase(IMAPProps.IMAP) && isEmpty(port)) {
                input.port = Short.parseShort(IMAPProps.PORT);
            } else if (protocol.trim().equalsIgnoreCase(IMAPProps.IMAP4) && (isEmpty(port))) {
                input.port = Short.parseShort(IMAPProps.PORT);
            }
            //The protocol should be given in lowercase to be recognised.
            input.protocol = input.protocol.toLowerCase();
            if (input.protocol.trim().equalsIgnoreCase(IMAPProps.IMAP4)) {
                input.protocol = IMAPProps.IMAP;
            }

            input.username = username;

            input.password = password;

            input.folder = folder;

            input.trustAllRoots = StringUtils.isEmpty(trustAllRoots) ? true : Boolean.parseBoolean(trustAllRoots);

            input.enableTLS = enableTLS != null && enableTLS.equalsIgnoreCase(Strings.TRUE);

            input.enableSSL = enableSSL != null && enableSSL.equalsIgnoreCase(Strings.TRUE);

            input.keystore = keystore;

            input.keystorePassword = keystorePassword;

            if (StringUtils.isEmpty(messageNumber)) {
                throw new Exception(ExceptionMsgs.MESSAGE_NUMBER_NOT_SPECIFIED);
            }
            input.messageNumber = Integer.parseInt(messageNumber);
            if (input.messageNumber < 1) {
                throw new Exception(ExceptionMsgs.MESSAGES_ARE_NUMBERED_STARTING_AT_1);
            }

            input.attachmentName = attachmentName;

            input.characterSet = characterSet;

            input.destination = destination;

            input.overwrite = !StringUtils.isEmpty(overwrite) && Boolean.parseBoolean(overwrite.trim());

            input.decryptionKeystore = decryptionKeystore;
            if (input.isEncryptedMessage()) {
                if (!decryptionKeystore.startsWith(Constants.HTTP)) {
                    decryptionKeystore = Constants.FILE + decryptionKeystore;
                }

                input.decryptionKeyAlias = (decryptionKeyAlias == null) ? Strings.EMPTY : decryptionKeyAlias;

                input.decryptionKeystorePassword = (decryptionKeystorePassword == null) ? Strings.EMPTY : decryptionKeystorePassword;
            }

            return input;
        }
    }
}