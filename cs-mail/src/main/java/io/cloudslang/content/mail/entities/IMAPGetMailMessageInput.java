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

public class IMAPGetMailMessageInput {

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
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;


    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }

    public String getVerifyCertificate() {
        return verifyCertificate;
    }

    public void setVerifyCertificate(String verifyCertificate) {
        this.verifyCertificate = verifyCertificate;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getEnableTLS() {
        return enableTLS;
    }

    public void setEnableTLS(String enableTLS) {
        this.enableTLS = enableTLS;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getTrustAllRoots() {
        return trustAllRoots;
    }

    public void setTrustAllRoots(String trustAllRoots) {
        this.trustAllRoots = trustAllRoots;
    }

    public String getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getSubjectOnly() {
        return subjectOnly;
    }

    public void setSubjectOnly(String subjectOnly) {
        this.subjectOnly = subjectOnly;
    }

    public String getEnableSSL() {
        return enableSSL;
    }

    public void setEnableSSL(String enableSSL) {
        this.enableSSL = enableSSL;
    }

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public void setTrustKeystore(String trustKeystore) {
        this.trustKeystore = trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }

    public void setTrustPassword(String trustPassword) {
        this.trustPassword = trustPassword;
    }

    public String getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(String characterSet) {
        this.characterSet = characterSet;
    }

    public String getDeleteUponRetrieval() {
        return deleteUponRetrieval;
    }

    public void setDeleteUponRetrieval(String deleteUponRetrieval) {
        this.deleteUponRetrieval = deleteUponRetrieval;
    }

    public String getDecryptionKeystore() {
        return decryptionKeystore;
    }

    public void setDecryptionKeystore(String decryptionKeystore) {
        this.decryptionKeystore = decryptionKeystore;
    }

    public String getDecryptionKeyAlias() {
        return decryptionKeyAlias;
    }

    public void setDecryptionKeyAlias(String decryptionKeyAlias) {
        this.decryptionKeyAlias = decryptionKeyAlias;
    }

    public String getDecryptionKeystorePassword() {
        return decryptionKeystorePassword;
    }

    public void setDecryptionKeystorePassword(String decryptionKeystorePassword) {
        this.decryptionKeystorePassword = decryptionKeystorePassword;
    }
}
