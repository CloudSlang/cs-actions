package org.openscore.content.mail.entities;

/**
 * Created by giloan on 11/3/2014.
 */
public class GetMailMessageInputs {

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
    public static final String KEYSTORE = "keystore";
    public static final String KEYSTORE_PASSWORD = "keystorePassword";
    public static final String TRUST_KEYSTORE = "trustKeystore";
    public static final String TRUST_PASSWORD = "trustPassword";
    public static final String CHARACTER_SET = "characterSet";
    public static final String DELETE_UPON_RETRIVAL = "deleteUponRetrieval";
    public static final String DECRYPTION_KEYSTORE = "decryptionKeystore";
    public static final String DECRYPTION_KEY_ALIAS = "decryptionKeyAlias";
    public static final String DECRYPTION_KEYSTORE_PASSWORD = "decryptionKeystorePassword";

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
    private String keystore;
    private String keystorePassword;
    private String trustKeystore;
    private String trustPassword;
    private String characterSet;
    private String deleteUponRetrieval;
    private String decryptionKeystore;
    private String decryptionKeyAlias;
    private String decryptionKeystorePassword;

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
