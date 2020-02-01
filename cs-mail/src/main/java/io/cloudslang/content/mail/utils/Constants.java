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

public class Constants {
    public static final String DEFAULT_JAVA_KEYSTORE = System.getProperty("java.home") + "/lib/security/cacerts";

    public static final String HOSTNAME = "host";
    public static final String PORT = "port";
    public static final String PROTOCOL = "protocol";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String FOLDER = "folder";
    public static final String TRUSTALLROOTS = "trustAllRoots";
    public static final String MESSAGE_NUMBER = "messageNumber";
    public static final String SUBJECT_ONLY = "subjectOnly";
    public static final String ENABLESSL = "enableSSL";
    public static final String ENABLETLS = "enableTLS";
    public static final String KEYSTORE = "keystore";
    public static final String KEYSTORE_PASSWORD = "keystorePassword";
    public static final String TRUST_KEYSTORE = "trustKeystore";
    public static final String TRUST_PASSWORD = "trustPassword";
    public static final String CHARACTER_SET = "characterSet";
    public static final String DELETE_UPON_RETRIVAL = "deleteUponRetrieval";
    public static final String DECRYPTION_KEYSTORE = "decryptionKeystore";
    public static final String DECRYPTION_KEY_ALIAS = "decryptionKeyAlias";
    public static final String DECRYPTION_KEYSTORE_PASSWORD = "decryptionKeystorePassword";
    public static final String TIMEOUT = "timeout";
    public static final String VERIFY_CERTIFICATE = "verifyCertificate";
    public static final String PROXY_HOST = "proxyHost";
    public static final String PROXY_PORT = "proxyPort";
    public static final String PROXY_USERNAME = "proxyUsername";
    public static final String PROXY_PASSWORD = "proxyPassword";

    public static final String POP3 = "pop3";
    public static final String IMAP = "imap";
    public static final String IMAP_4 = "imap4";
    public static final String IMAP_PORT = "143";
    public static final String POP3_PORT = "110";
    public static final String FILE = "file:";
    public static final String HTTP = "http";
    public static final int ONE_SECOND = 1000;
    public static final String RETURN_RESULT = "returnResult";
    public static final String SUBJECT_HEADER = "Subject";
    public static final String SUBJECT = "subject";
    public static final String SUCCESS_RETURN_CODE = "0";
    public static final String FAILURE_RETURN_CODE = "-1";
    public static final String UNRECOGNIZED_SSL_MESSAGE = "Unrecognized SSL message";
    public static final String UNRECOGNIZED_SSL_MESSAGE_PLAINTEXT_CONNECTION = "Unrecognized SSL message, plaintext " +
            "connection?";

    public static final String ENCRYPT_RECID = "The encryption recId is: ";


    public static final String BODY_RESULT = "body";
    public static final String PLAIN_TEXT_BODY_RESULT = "plainTextBody";
    public static final String ATTACHED_FILE_NAMES_RESULT = "attachedFileNames";
    public static final String SSL = "SSL";


    public static final String HOST_NOT_SPECIFIED = "The required host input is not specified!";
    public static final String USERNAME_NOT_SPECIFIED = "The required username input is not specified!";
    public static final String FOLDER_NOT_SPECIFIED = "The required folder input is not specified!";
    public static final String STR_FALSE = "false";
    public static final String STR_TRUE = "true";
    public static final String STR_COMMA = ",";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String DEFAULT_PASSWORD_FOR_STORE = "changeit";

    public static final String MULTIPART_MIXED = "multipart/mixed";
    public static final String MULTIPART_RELATED = "multipart/related";
    public static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    public static final String RETURN_CODE = "returnCode";
    public static final String MESSAGES_ARE_NUMBERED_STARTING_AT_1 = "Messages are numbered starting at 1 through " +
            "the total number of messages in the folder!";
    public static final String MESSAGE_NUMBER_NOT_SPECIFIED = "The required messageNumber input is not specified!";
    public static final String SPECIFY_PORT_OR_PROTOCOL_OR_BOTH = "Please specify the port, the protocol, or both.";
    public static final String SPECIFY_PROTOCOL_FOR_GIVEN_PORT = "Please specify the protocol for the indicated port.";
    public static final String SPECIFY_PORT_FOR_PROTOCOL = "Please specify the port for the indicated protocol.";
    public static final String THE_SPECIFIED_FOLDER_DOES_NOT_EXIST_ON_THE_REMOTE_SERVER = "The specified folder does " +
            "not exist on the remote server.";
    public static final String SECURE_SUFFIX_FOR_POP3_AND_IMAP = "s";
    public static final String COUNT_MESSAGES_IN_FOLDER_ERROR_MESSAGE = " messages in folder";
    public static final String PKCS_KEYSTORE_TYPE = "PKCS12";
    public static final String BOUNCY_CASTLE_PROVIDER = "BC";
    public static final String PRIVATE_KEY_ERROR_MESSAGE = "Can't find a private key!";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_HTML = "text/html";
    public static final String ENCRYPTED_CONTENT_TYPE = "application/pkcs7-mime; name=\"smime.p7m\"; " +
            "smime-type=enveloped-data";
    public Constants() {
    }
}
