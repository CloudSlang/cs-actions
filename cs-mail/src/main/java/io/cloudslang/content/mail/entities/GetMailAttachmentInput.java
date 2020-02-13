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


import io.cloudslang.content.mail.constants.Constants;
import org.apache.commons.lang3.StringUtils;

public class GetMailAttachmentInput extends GetMailBaseInput implements DecryptableMessageInput {

    private String folder;
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

    public static class Builder extends GetMailBaseInput.Builder {

        private String folder;
        private String messageNumber;
        private String attachmentName;
        private String characterSet;
        private String destination;
        private String overwrite;
        private String decryptionKeystore;
        private String decryptionKeyAlias;
        private String decryptionKeystorePassword;


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

        @Override
        public Builder proxyHost(String proxyHost) {
            return (Builder) super.proxyHost(proxyHost);
        }

        @Override
        public Builder proxyPort(String proxyPort) {
            return (Builder) super.proxyPort(proxyPort);
        }

        @Override
        public Builder proxyUsername(String proxyUsername) {
            return (Builder) super.proxyUsername(proxyUsername);
        }

        @Override
        public Builder proxyPassword(String proxyPassword) {
            return (Builder) super.proxyPassword(proxyPassword);
        }

        @Override
        public Builder timeout(String timeout) {
            return (Builder) super.timeout(timeout);
        }

        public GetMailAttachmentInput build() throws Exception {
            GetMailAttachmentInput input = new GetMailAttachmentInput();
            super.build(input);

            input.folder = folder;

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

            input.decryptionKeystore = decryptionKeystore;
            if (input.isEncryptedMessage()) {
                if (!decryptionKeystore.startsWith(Constants.HTTP)) {
                    decryptionKeystore = Constants.FILE + decryptionKeystore;
                }

                input.decryptionKeyAlias = (decryptionKeyAlias == null) ? StringUtils.EMPTY : decryptionKeyAlias;

                input.decryptionKeystorePassword = (decryptionKeystorePassword == null) ? StringUtils.EMPTY : decryptionKeystorePassword;
            }

            return input;
        }
    }
}