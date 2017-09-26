/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
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
    public static final String HEADERS = "headers";
    public static final String HEADERS_ROW_DELIMITER = "rowDelimiter";
    public static final String HEADERS_COLUMN_DELIMITER = "columnDelimiter";
    public static final String USER = "username";
    public static final String PASSWORD = "password";
    public static final String DELIMITER = "delimiter";
    public static final String CHARACTERSET = "characterSet";
    public static final String CONTENT_TRANSFER_ENCODING = "contentTransferEncoding";
    public static final String ENCRYPTION_KEYSTORE = "encryptionKeystore";
    public static final String ENCRYPTION_KEY_ALIAS = "encryptionKeyAlias";
    public static final String ENCRYPTION_KEYSTORE_PASSWORD = "encryptionKeystorePassword";
    public static final String ENABLE_TLS = "enableTLS";
    public static final String TIMEOUT = "timeout";
    public static final String ENCRYPTION_ALGORITHM = "encryptionAlgorithm";

    private String smtpHostname;
    private String port;
    private String htmlEmail;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private String readReceipt;
    private String attachments;
    private String headers;
    private String rowDelimiter;
    private String columnDelimiter;
    private String password;
    private String delimiter;
    private String characterset;
    private String contentTransferEncoding;
    private String encryptionKeystore;
    private String encryptionKeyAlias;
    private String encryptionKeystorePassword;
    private String enableTLS;
    private String user;
    private String timeout;
    private String encryptionAlgorithm;

    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(String encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getRowDelimiter() {
        return rowDelimiter;
    }

    public void setRowDelimiter(String rowDelimiter) {
        this.rowDelimiter = rowDelimiter;
    }

    public String getColumnDelimiter() {
        return columnDelimiter;
    }

    public void setColumnDelimiter(String columnDelimiter) {
        this.columnDelimiter = columnDelimiter;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

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
        return readReceipt;
    }

    public void setReadReceipt(String readReceipt) {
        this.readReceipt = readReceipt;
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
