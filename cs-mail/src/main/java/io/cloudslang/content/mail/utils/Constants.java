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

    public Constants() {
    }
}
