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
package io.cloudslang.content.mail.utils;

public final class Constants {

    public static final String FILE = "file:";
    public static final String HTTP = "http";
    public static final int ONE_SECOND = 1000;
    public static final String SUBJECT_HEADER = "Subject";
    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";
    public static final String ENCRYPT_RECID = "The encryption recId is: ";
    public static final String SSL = "SSL";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String DEFAULT_PASSWORD_FOR_STORE = "changeit";
    public static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    public static final String SECURE_SUFFIX_FOR_POP3_AND_IMAP = "s";
    public static final String PKCS_KEYSTORE_TYPE = "PKCS12";
    public static final String BOUNCY_CASTLE_PROVIDER = "BC";
    public static final String ENCRYPTED_CONTENT_TYPE = "application/pkcs7-mime; name=\"smime.p7m\"; " +
            "smime-type=enveloped-data";
    public static final String MAIL_WAS_SENT = "SentMailSuccessfully";


    public static final class Strings {
        public static final String FALSE = "false";
        public static final String TRUE = "true";
        public static final String COMMA = ",";
        public static final String COLON = ":";
        public static final String EMPTY = "";
    }


    public static final class Props {
        public static final String MAIL = "mail.";
        public static final String SSL_ENABLE = ".ssl.enable";
        public static final String START_TLS_ENABLE = ".starttls.enable";
        public static final String START_TLS_REQUIRED = ".starttls.required";
        public static final String SOCKET_FACTORY_CLASS = ".socketFactory.class";
        public static final String SOCKET_FACTORY_FALLBACK = ".socketFactory.fallback";
        public static final String SOCKET_FACTORY_PORT = ".socketFactory.port";
        public static final String HOST = ".host";
        public static final String PORT = ".port";
        public static final String TIMEOUT = ".timeout";
        public static final String JAVA_HOME = "java.home";
        public static final String FILE_SEPARATOR = "file.separator";
    }


    public static final class IMAPProps {
        private static final String MAIL_IMAPS = "mail.imaps.";

        public static final String IMAP = "imap";
        public static final String IMAP4 = "imap4";
        public static final String PORT = "143";
        public static final String PROXY_HOST = MAIL_IMAPS + "proxy.host";
        public static final String PROXY_PORT = MAIL_IMAPS + "proxy.port";
        public static final String PROXY_USER = MAIL_IMAPS + "proxy.user";
        public static final String PROXY_PASSWORD = MAIL_IMAPS + "proxy.password";
    }


    public static final class POPProps {
        private static final String MAIL_POP3 = "mail.pop3.";

        public static final String POP = "pop";
        public static final String POP3 = "pop3";
        public static final String POP3_PORT = "110";
        public static final String PROXY_HOST = MAIL_POP3 + "proxy.host";
        public static final String PROXY_PORT = MAIL_POP3 + "proxy.port";
        public static final String PROXY_USER = MAIL_POP3 + "proxy.user";
        public static final String PROXY_PASSWORD = MAIL_POP3 + "proxy.password";
    }


    public static final class SMTPProps {
        private static final String MAIL_SMTP = "mail.smtp.";

        public static final String SMTP = "smtp";
        public static final String HOST = MAIL_SMTP + "host";
        public static final String PORT = MAIL_SMTP + "port";
        public static final String USER = MAIL_SMTP + "user";
        public static final String PASSWORD = MAIL_SMTP + "password";
        public static final String AUTH = MAIL_SMTP + "auth";
        public static final String START_TLS_ENABLE = MAIL_SMTP + "starttls.enable";
        public static final String TIMEOUT = MAIL_SMTP + "timeout";
    }


    public static final class MimeTypes {
        public static final String MULTIPART_RELATED = "multipart/related";
        public static final String MULTIPART_ALTERNATIVE = "multipart/alternative";
        public static final String TEXT_PLAIN = "text/plain";
        public static final String TEXT_HTML = "text/html";
        public static final String IMAGE_PNG = "image/png;";
        public static final String MULTIPART_MIXED = "multipart/mixed";
    }


    public static final class Encodings {
        public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
        public static final String BASE64 = "base64";
        public static final String QUOTED_PRINTABLE = "quoted-printable";
    }


    public static final class Encryption {
        public static final String PKCS_KEYSTORE_TYPE = "PKCS12";
        public static final String BOUNCY_CASTLE_PROVIDER = "BC";
    }


    public static final class Inputs {
        // for both GetMailMessage and SendMail Actions
        public static final String HOST = "host";
        public static final String HOSTNAME = "hostname";
        public static final String PORT = "port";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String TIMEOUT = "timeout";
        public static final String PROXY_HOST = "proxyHost";
        public static final String PROXY_PORT = "proxyPort";
        public static final String PROXY_USERNAME = "proxyUsername";
        public static final String PROXY_PASSWORD = "proxyPassword";
        public static final String ENABLE_TLS = "enableTLS";
        public static final String CHARACTER_SET = "characterSet";
        // for GetMailMessage
        public static final String PROTOCOL = "protocol";
        public static final String FOLDER = "folder";
        public static final String TRUST_ALL_ROOTS = "trustAllRoots";
        public static final String MESSAGE_NUMBER = "messageNumber";
        public static final String SUBJECT_ONLY = "subjectOnly";
        public static final String ENABLE_SSL = "enableSSL";
        public static final String KEYSTORE = "keystore";
        public static final String KEYSTORE_PASSWORD = "keystorePassword";
        public static final String TRUST_KEYSTORE = "trustKeystore";
        public static final String TRUST_PASSWORD = "trustPassword";
        public static final String DELETE_UPON_RETRIVAL = "deleteUponRetrieval";
        public static final String DECRYPTION_KEYSTORE = "decryptionKeystore";
        public static final String DECRYPTION_KEY_ALIAS = "decryptionKeyAlias";
        public static final String DECRYPTION_KEYSTORE_PASSWORD = "decryptionKeystorePassword";
        public static final String MARK_MESSAGE_AS_READ = "markMessageAsRead";
        public static final String VERIFY_CERTIFICATE = "verifyCertificate";
        // for SendMail
        public static final String HTML_EMAIL = "htmlEmail";
        public static final String FROM = "from";
        public static final String TO = "to";
        public static final String CC = "cc";
        public static final String BCC = "bcc";
        public static final String SUBJECT = "subject";
        public static final String BODY = "body";
        public static final String READ_RECEIPT = "readReceipt";
        public static final String ATTACHMENTS = "attachments";
        public static final String HEADERS = "headers";
        public static final String HEADERS_ROW_DELIMITER = "rowDelimiter";
        public static final String HEADERS_COLUMN_DELIMITER = "columnDelimiter";
        public static final String DELIMITER = "delimiter";
        public static final String CONTENT_TRANSFER_ENCODING = "contentTransferEncoding";
        public static final String ENCRYPTION_KEYSTORE = "encryptionKeystore";
        public static final String ENCRYPTION_KEY_ALIAS = "encryptionKeyAlias";
        public static final String ENCRYPTION_KEYSTORE_PASSWORD = "encryptionKeystorePassword";
        public static final String ENCRYPTION_ALGORITHM = "encryptionAlgorithm";
        public static final String ATTACHMENT_NAME = "attachmentName";
        public static final String DESTINATION = "destination";
        public static final String OVERWRITE = "overwrite";
    }


    public static final class ExceptionMsgs {
        public static final String UNRECOGNIZED_SSL_MESSAGE = "Unrecognized SSL message";
        public static final String UNRECOGNIZED_SSL_MESSAGE_PLAINTEXT_CONNECTION = "Unrecognized SSL message, plaintext " +
                "connection?";
        public static final String MESSAGES_ARE_NUMBERED_STARTING_AT_1 = "Messages are numbered starting at 1 through " +
                "the total number of messages in the folder!";
        public static final String MESSAGE_NUMBER_NOT_SPECIFIED = "The required messageNumber input is not specified!";
        public static final String SPECIFY_PORT_OR_PROTOCOL_OR_BOTH = "Please specify the port, the protocol, or both.";
        public static final String SPECIFY_PROTOCOL_FOR_GIVEN_PORT = "Please specify the protocol for the indicated port.";
        public static final String SPECIFY_PORT_FOR_PROTOCOL = "Please specify the port for the indicated protocol.";
        public static final String THE_SPECIFIED_FOLDER_DOES_NOT_EXIST_ON_THE_REMOTE_SERVER = "The specified folder does " +
                "not exist on the remote server.";
        public static final String PRIVATE_KEY_ERROR_MESSAGE = "Can't find a private key!";
        public static final String PUBLIC_KEY_ERROR_MESSAGE = "Can't find a public key!";
        public static final String HOST_NOT_SPECIFIED = "The required host input is not specified!";
        public static final String USERNAME_NOT_SPECIFIED = "The required username input is not specified!";
        public static final String FOLDER_NOT_SPECIFIED = "The required folder input is not specified!";
        public static final String COUNT_MESSAGES_IN_FOLDER_ERROR_MESSAGE = " messages in folder";
        public static final String INVALID_DELIMITERS = "The columnDelimiter and rowDelimiter inputs have the " +
                "same value. They need to be different.";
        public static final String INVALID_ROW_DELIMITER = "The rowDelimiter can't be a substring of the columnDelimiter!";
        public static final String ROW_WITH_MULTIPLE_COLUMN_DELIMITERS_IN_HEADERS_INPUT = "Row #%d in the 'headers' " +
                "input has more than one column delimiter.";
        public static final String ROW_WITH_EMPTY_HEADERS_INPUT = "Row #%d in the 'headers' input does not contain " +
                "any values.";
        public static final String ROW_WITH_MISSING_VALUE_FOR_HEADER = "Row #%d in the 'headers' input is missing one " +
                "of the header values.";
        public static final String TIMEOUT_MUST_BE_POSITIVE = "timeout value must be a positive number";
        public static final String INVALID_PORT_NUMBER = "Invalid port number";
    }


    public static final class Outputs {
        public static final String EXCEPTION = "exception";
        public static final String RETURN_CODE = "returnCode";
        public static final String RETURN_RESULT = "returnResult";
        public static final String SUBJECT = "subject";
        public static final String BODY_RESULT = "body";
        public static final String PLAIN_TEXT_BODY_RESULT = "plainTextBody";
        public static final String ATTACHED_FILE_NAMES_RESULT = "attachedFileNames";
        public static final String TEMPORARY_FILE = "temporaryFile";
    }


    public static final class Responses {
        public static final String SUCCESS = "success";
        public static final String FAILURE = "failure";
    }


    public static final class ReturnCodes {
        public static final String SUCCESS_RETURN_CODE = "0";
        public static final String FAILURE_RETURN_CODE = "-1";
    }
}