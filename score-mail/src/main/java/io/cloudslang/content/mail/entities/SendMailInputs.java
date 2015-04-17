package io.cloudslang.content.mail.entities;

/**
 * Created by giloan on 10/30/2014.
 */
public class SendMailInputs {

    public static final String HOSTNAME = "hostname";
    public static final String PORT = "port";
    public static final String HTML_EMAIL = "htmlEmail";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String CC = "cc";
    public static final String BCC = "bcc";
    public static final String SUBJECT = "subject";
    public static final String BODY = "body";
    public static final String READ_RECEIPT = "readReceipt";
    public static final String ATTACHMENTS = "attachments";
    public static final String USER = "username";
    public static final String PASSWORD = "password";
    public static final String DELIMITER = "delimiter";
    public static final String CHARACTERSET = "characterSet";
    public static final String CONTENT_TRANSFER_ENCODING = "contentTransferEncoding";
    public static final String ENCRYPTION_KEYSTORE = "encryptionKeystore";
    public static final String ENCRYPTION_KEY_ALIAS = "encryptionKeyAlias";
    public static final String ENCRYPTION_KEYSTORE_PASSWORD = "encryptionKeystorePassword";
    public static final String ENABLE_TLS = "enableTLS";

    private String smtpHostname;
    private String port;
    private String htmlEmail;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private String read_receipt;
    private String attachments;
    private String user;
    private String password;
    private String delimiter;
    private String characterset;
    private String contentTransferEncoding;
    private String encryptionKeystore;
    private String encryptionKeyAlias;
    private String encryptionKeystorePassword;
    private String enableTLS;

    public String getSMTPHostname() {
        return smtpHostname;
    }

    public void setSmtpHostname(String smtpHostname) {
        this.smtpHostname = smtpHostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHtmlEmail() {
        return htmlEmail;
    }

    public void setHtmlEmail(String htmlEmail) {
        this.htmlEmail = htmlEmail;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReadReceipt() {
        return read_receipt;
    }

    public void setReadReceipt(String read_receipt) {
        this.read_receipt = read_receipt;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getCharacterset() {
        return characterset;
    }

    public void setCharacterset(String characterset) {
        this.characterset = characterset;
    }

    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }

    public void setContentTransferEncoding(String contentTransferEncoding) {
        this.contentTransferEncoding = contentTransferEncoding;
    }

    public String getEncryptionKeystore() {
        return encryptionKeystore;
    }

    public void setEncryptionKeystore(String encryptionKeystore) {
        this.encryptionKeystore = encryptionKeystore;
    }

    public String getEncryptionKeyAlias() {
        return encryptionKeyAlias;
    }

    public void setEncryptionKeyAlias(String encryptionKeyAlias) {
        this.encryptionKeyAlias = encryptionKeyAlias;
    }

    public String getEncryptionKeystorePassword() {
        return encryptionKeystorePassword;
    }

    public void setEncryptionKeystorePassword(String encryptionKeystorePassword) {
        this.encryptionKeystorePassword = encryptionKeystorePassword;
    }

    public String getEnableTLS() {
        return enableTLS;
    }

    public void setEnableTLS(String enableTLS) {
        this.enableTLS = enableTLS;
    }
}
