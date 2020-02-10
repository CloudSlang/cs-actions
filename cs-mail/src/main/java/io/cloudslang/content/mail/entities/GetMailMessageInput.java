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

import static io.cloudslang.content.mail.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.*;

public class GetMailMessageInput {

    private String hostname;
    private short port;
    private String protocol;
    private String username;
    private String password;
    private String folder;
    private boolean trustAllRoots;
    private int messageNumber;
    private boolean subjectOnly;
    private boolean enableSSL;
    private boolean enableTLS;
    private String keystore;
    private String keystorePassword;
    private String trustKeystore;
    private String trustPassword;
    private String characterSet;
    private boolean deleteUponRetrieval;
    private boolean decryptMessage;
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


    public boolean isEnableTLS() {
        return enableTLS;
    }


    public String getHostname() {
        return hostname;
    }


    public short getPort() {
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


    public int getMessageNumber() {
        return messageNumber;
    }


    public boolean isSubjectOnly() {
        return subjectOnly;
    }


    public boolean isEnableSSL() {
        return enableSSL;
    }


    public String getKeystore() {
        return keystore;
    }


    public String getKeystorePassword() {
        return keystorePassword;
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


    public boolean isDecryptMessage() {
        return decryptMessage;
    }


    public static class Builder {
        private String hostname;
        private String port;
        private String protocol;
        private String username;
        private String password;
        private String folder;
        private String trustAllRoots;
        private String messageNumber;
        private String subjectOnly;
        private String enableSSL;
        private String enableTLS;
        private String keystore;
        private String keystorePassword;
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


        public Builder messageNumber(String messageNumber) {
            this.messageNumber = messageNumber;
            return this;
        }


        public Builder subjectOnly(String subjectOnly) {
            this.subjectOnly = subjectOnly;
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


        public GetMailMessageInput build() throws Exception {
            GetMailMessageInput input = new GetMailMessageInput();

            if (isEmpty(hostname)) {
                throw new Exception(ExceptionMsgs.HOST_NOT_SPECIFIED);
            }
            input.hostname = hostname.trim();

            if(!isEmpty(port)) {
                input.port = Short.parseShort(port);
                if(input.port < 1) {
                    throw new Exception(ExceptionMsgs.INVALID_PORT_NUMBER);
                }
            }

            input.protocol = protocol;

            if (isEmpty(username)) {
                throw new Exception(ExceptionMsgs.USERNAME_NOT_SPECIFIED);
            }
            input.username = username.trim();

            input.password = (password == null) ? Strings.EMPTY : password.trim();

            if (isEmpty(folder)) {
                throw new Exception(ExceptionMsgs.FOLDER_NOT_SPECIFIED);
            }
            input.folder = folder.trim();

            if (trustAllRoots != null && trustAllRoots.equalsIgnoreCase(Strings.FALSE)) {
                input.trustAllRoots = false;
            } else {
                input.trustAllRoots = true;
            }

            if (isEmpty(messageNumber)) {
                throw new Exception(ExceptionMsgs.MESSAGE_NUMBER_NOT_SPECIFIED);
            }
            input.messageNumber = Integer.parseInt(messageNumber);
            if (input.messageNumber < 1) {
                throw new Exception(ExceptionMsgs.MESSAGES_ARE_NUMBERED_STARTING_AT_1);
            }

            if (subjectOnly != null && subjectOnly.equalsIgnoreCase(Strings.TRUE)) {
                input.subjectOnly = true;
            } else {
                input.subjectOnly = false;
            }

            if (enableSSL != null && enableSSL.equalsIgnoreCase(Strings.TRUE)) {
                input.enableSSL = true;
            } else {
                input.enableSSL = false;
            }

            if (enableTLS != null && enableTLS.equalsIgnoreCase(Strings.TRUE)) {
                input.enableTLS = true;
            } else {
                input.enableTLS = false;
            }

            input.keystore = defaultIfEmpty(keystore, DEFAULT_JAVA_KEYSTORE);

            input.keystorePassword = keystorePassword;

            input.trustKeystore = defaultIfEmpty(trustKeystore, DEFAULT_JAVA_KEYSTORE);

            input.trustPassword = trustPassword;

            input.characterSet = characterSet;

            if (deleteUponRetrieval != null && deleteUponRetrieval.equalsIgnoreCase(Strings.TRUE)) {
                input.deleteUponRetrieval = true;
            } else {
                input.deleteUponRetrieval = false;
            }

            if (markMessageAsRead != null && markMessageAsRead.equalsIgnoreCase(Strings.TRUE)) {
                input.markMessageAsRead = true;
            } else {
                input.markMessageAsRead = false;
            }


            if (isEmpty(protocol) && isEmpty(port)) {
                throw new Exception(ExceptionMsgs.SPECIFY_PORT_OR_PROTOCOL_OR_BOTH);
            } else if ((protocol != null && !protocol.isEmpty()) && (!protocol.equalsIgnoreCase(IMAPProps.IMAP)) &&
                    (!protocol.equalsIgnoreCase(POPProps.POP3)) && (!protocol.equalsIgnoreCase(IMAPProps.IMAP4)) &&
                    isEmpty(port)) {
                throw new Exception(ExceptionMsgs.SPECIFY_PORT_FOR_PROTOCOL);
            } else if (isEmpty(protocol) && !isEmpty(port) &&
                    (!port.equalsIgnoreCase(IMAPProps.PORT)) && (!port.equalsIgnoreCase(POPProps.PORT))) {
                throw new Exception(ExceptionMsgs.SPECIFY_PROTOCOL_FOR_GIVEN_PORT);
            } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(IMAPProps.PORT)) {
                input.protocol = IMAPProps.IMAP;
            } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(POPProps.PORT)) {
                input.protocol = POPProps.POP3;
            } else if (protocol.trim().equalsIgnoreCase(POPProps.POP3) && isEmpty(port)) {
                input.port = Short.parseShort(POPProps.PORT);
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

            input.decryptionKeystore = decryptionKeystore;
            if (isNotEmpty(input.decryptionKeystore)) {
                if (!input.decryptionKeystore.startsWith(HTTP)) {
                    input.decryptionKeystore = FILE + decryptionKeystore;
                }

                input.decryptMessage = true;

                input.decryptionKeyAlias = decryptionKeyAlias;
                if (null == decryptionKeyAlias) {
                    decryptionKeyAlias = Strings.EMPTY;
                }

                input.decryptionKeystorePassword = decryptionKeystorePassword;
                if (null == input.decryptionKeystorePassword) {
                    input.decryptionKeystorePassword = Strings.EMPTY;
                }
            } else {
                input.decryptMessage = false;
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
