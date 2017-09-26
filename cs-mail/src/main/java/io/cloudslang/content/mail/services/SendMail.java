/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.mail.services;

import com.sun.mail.smtp.SMTPMessage;
import io.cloudslang.content.mail.entities.EncryptionAlgorithmsEnum;
import io.cloudslang.content.mail.entities.SendMailInputs;
import io.cloudslang.content.mail.utils.HtmlImageNodeVisitor;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.util.encoders.Base64;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.security.InvalidParameterException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.sun.mail.smtp.SMTPMessage.NOTIFY_DELAY;
import static com.sun.mail.smtp.SMTPMessage.NOTIFY_FAILURE;
import static com.sun.mail.smtp.SMTPMessage.NOTIFY_SUCCESS;
import static io.cloudslang.content.mail.entities.EncryptionAlgorithmsEnum.getEncryptionAlgorithm;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Created by giloan on 10/30/2014.
 */
public class SendMail {

    public static final String RETURN_RESULT = "returnResult";
    public static final String RETURN_CODE = "returnCode";
    public static final String EXCEPTION = "exception";

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    public static final String SUCCESS_RETURN_CODE = "0";
    public static final String FAILURE_RETURN_CODE = "-1";

    private static final String MAIL_WAS_SENT = "SentMailSuccessfully";
    private static final String FILE = "file:";
    private static final String HTTP = "http";

    private static final String PKCS_KEYSTORE_TYPE = "PKCS12";
    private static final String BOUNCY_CASTLE_PROVIDER = "BC";
    private static final String INVALID_DELIMITERS = "The columnDelimiter and rowDelimiter inputs have the " +
            "same value. They need to be different.";
    private static final String INVALID_ROW_DELIMITER = "The rowDelimiter can't be a substring of the columnDelimiter!";
    public static final int ONE_SECOND = 1000;
    public static final String ROW_WITH_MULTIPLE_COLUMN_DELIMITERS_IN_HEADERS_INPUT = "Row #%d in the 'headers' " +
            "input has more than one column delimiter.";
    public static final String ROW_WITH_EMPTY_HEADERS_INPUT = "Row #%d in the 'headers' input does not contain " +
            "any values.";
    public static final String ROW_WITH_MISSING_VALUE_FOR_HEADER = "Row #%d in the 'headers' input is missing one " +
            "of the header values.";

    //Operation inputs
    private String attachments;
    private String smtpHost;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private String delimiter;
    private String user;
    private String password;
    private String charset;
    private String transferEncoding;
    private String encodingScheme;
    private String keystoreFile;
    private String keyAlias;
    private String keystorePass;
    private List<String> headerNames;
    private List<String> headerValues;
    private String rowDelimiter;
    private String columnDelimiter;
    private int smtpPort;
    private int timeout = -1;
    private boolean html;
    private boolean readReceipt;
    private boolean encryptMessage;
    private boolean enableTLS;
    private SMIMEEnvelopedGenerator gen;
    private String encryptionOID;

    public Map<String, String> execute(SendMailInputs sendMailInputs) throws Exception {
        Map<String, String> result = new HashMap<>();
        Transport transport = null;
        try {
            processInputs(sendMailInputs);

            // Create a mail session
            java.util.Properties props = new java.util.Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", "" + smtpPort);

            if (null != user && user.length() > 0) {
                props.put("mail.smtp.user", user);
                props.put("mail.smtp.password", password);
                props.put("mail.smtp.auth", "true");
            }
            if (enableTLS) {
                props.put("mail.smtp.starttls.enable", "true");
            }
            if (timeout > 0) {
                props.put("mail.smtp.timeout", timeout);
            }

            //construct encryption SMIMEEnvelopedGenerator
            if (encryptMessage) {
                addEncryptionSettings();
            }

            // Construct the message
            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            if (html) {
                processHTMLBodyWithBASE64Images(multipart);
                mimeBodyPart.setContent(body, "text/html" + ";charset=" + charset);
            } else {
                mimeBodyPart.setContent(body, "text/plain" + ";charset=" + charset);
            }
            mimeBodyPart.setHeader("Content-Transfer-Encoding", transferEncoding);
            mimeBodyPart = encryptMimeBodyPart(mimeBodyPart);

            multipart.addBodyPart(mimeBodyPart);

            if (null != attachments && attachments.length() > 0) {
                for (String attachment : attachments.split(Pattern.quote(delimiter))) {
                    FileDataSource source = new FileDataSource(attachment);
                    if (!source.getFile().exists()) {
                        throw new FileNotFoundException("Cannot attach " + attachment);
                    }

                    if (!Files.isReadable(source.getFile().toPath())) {
                        throw new InvalidParameterException(attachment + " don't have read permision");
                    }

                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setHeader("Content-Transfer-Encoding", transferEncoding);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName((MimeUtility.encodeText(attachment
                            .substring(attachment.lastIndexOf(java.io.File.separator) + 1), charset, encodingScheme)));
                    messageBodyPart = encryptMimeBodyPart(messageBodyPart);
                    multipart.addBodyPart(messageBodyPart);
                }
            }

            Session session = Session.getInstance(props, null);
            SMTPMessage msg = new SMTPMessage(session);
            msg.setContent(multipart);
            msg.setFrom(new InternetAddress(from));
            msg.setSubject(MimeUtility.encodeText(subject.replaceAll("[\\n\\r]", " "), charset, encodingScheme));

            if (readReceipt) {
                msg.setNotifyOptions(NOTIFY_DELAY + NOTIFY_FAILURE + NOTIFY_SUCCESS);
            }

            String[] recipients = to.split(Pattern.quote(delimiter));
            InternetAddress[] toRecipients = new InternetAddress[recipients.length];
            for (int count = 0; count < recipients.length; count++) {
                toRecipients[count] = new InternetAddress(recipients[count]);
            }
            msg.setRecipients(Message.RecipientType.TO, toRecipients);

            if (cc != null && cc.trim().length() > 0) {
                recipients = cc.split(Pattern.quote(delimiter));
                if (recipients.length > 0) {
                    InternetAddress[] ccRecipients = new InternetAddress[recipients.length];
                    for (int count = 0; count < recipients.length; count++) {
                        ccRecipients[count] = new InternetAddress(recipients[count]);
                    }
                    msg.setRecipients(Message.RecipientType.CC, ccRecipients);
                }
            }

            if (bcc != null && bcc.trim().length() > 0) {
                recipients = bcc.split(Pattern.quote(delimiter));
                if (recipients.length > 0) {
                    InternetAddress[] bccRecipients = new InternetAddress[recipients.length];
                    for (int count = 0; count < recipients.length; count++) {
                        bccRecipients[count] = new InternetAddress(recipients[count]);
                    }
                    msg.setRecipients(Message.RecipientType.BCC, bccRecipients);
                }
            }

            if (headerNames != null && !headerNames.isEmpty()) {
                msg = addHeadersToSMTPMessage(msg, headerNames, headerValues);
            }

            msg.saveChanges();

            if (null != user && user.length() > 0) {
                transport = session.getTransport("smtp");
                transport.connect(smtpHost, smtpPort, user, password);
                transport.sendMessage(msg, msg.getAllRecipients());
            } else {
                Transport.send(msg);
            }
            result.put(RETURN_RESULT, MAIL_WAS_SENT);
            result.put(RETURN_CODE, SUCCESS_RETURN_CODE);
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
        return result;
    }

    private void processHTMLBodyWithBASE64Images(MimeMultipart multipart) throws ParserException,
            MessagingException, NoSuchAlgorithmException, SMIMEException, java.security.NoSuchProviderException {
        if (null != body && body.contains("base64")) {
            Parser parser = new Parser(body);
            NodeList nodeList = parser.parse(null);
            HtmlImageNodeVisitor htmlImageNodeVisitor = new HtmlImageNodeVisitor();
            nodeList.visitAllNodesWith(htmlImageNodeVisitor);
            body = nodeList.toHtml();

            addAllBase64ImagesToMimeMultipart(multipart, htmlImageNodeVisitor.getBase64Images());
        }
    }

    private void addAllBase64ImagesToMimeMultipart(MimeMultipart multipart, Map<String, String> base64ImagesMap)
            throws MessagingException, NoSuchAlgorithmException, NoSuchProviderException, SMIMEException {
        for (String contentId : base64ImagesMap.keySet()) {
            MimeBodyPart imagePart = getImageMimeBodyPart(base64ImagesMap, contentId);
            imagePart = encryptMimeBodyPart(imagePart);
            multipart.addBodyPart(imagePart);
        }
    }

    private MimeBodyPart getImageMimeBodyPart(Map<String, String> base64ImagesMap, String contentId)
            throws MessagingException {
        MimeBodyPart imagePart = new MimeBodyPart();
        imagePart.setContentID(contentId);
        imagePart.setHeader("Content-Transfer-Encoding", "base64");
        imagePart.setDataHandler(new DataHandler(Base64.decode(base64ImagesMap.get(contentId)), "image/png;"));
        return imagePart;
    }

    private MimeBodyPart encryptMimeBodyPart(MimeBodyPart mimeBodyPart) throws NoSuchAlgorithmException,
            NoSuchProviderException, SMIMEException {
        if (encryptMessage) {
            mimeBodyPart = gen.generate(mimeBodyPart, encryptionOID, BOUNCY_CASTLE_PROVIDER);
        }
        return mimeBodyPart;
    }

    private void addEncryptionSettings() throws  Exception {
        URL keystoreUrl = new URL(keystoreFile);
        InputStream publicKeystoreInputStream = keystoreUrl.openStream();
        char[] smimePw = new String(keystorePass).toCharArray();


        gen = new SMIMEEnvelopedGenerator();
        Security.addProvider(new BouncyCastleProvider());
        KeyStore ks = KeyStore.getInstance(PKCS_KEYSTORE_TYPE, BOUNCY_CASTLE_PROVIDER);
        try {
            ks.load(publicKeystoreInputStream, smimePw);
        } finally {
            publicKeystoreInputStream.close();
        }

        if ("".equals(keyAlias)) {
            Enumeration aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = (String) aliases.nextElement();

                if (ks.isKeyEntry(alias)) {
                    keyAlias = alias;
                }
            }
        }

        if ("".equals(keyAlias)) {
            throw new Exception("Can't find a public key!");
        }

        Certificate[]   chain = ks.getCertificateChain(keyAlias);

        if (chain == null) {
            throw new Exception("The key with alias \"" + keyAlias + "\" can't be fount in given keystore.");
        }

        //
        // create the generator for creating an smime/encrypted message
        //
        gen.addKeyTransRecipient((X509Certificate)chain[0]);
    }

    private void processInputs(SendMailInputs sendMailInputs) throws Exception {
        try {
            html = Boolean.parseBoolean(sendMailInputs.getHtmlEmail());
        } catch (Exception e) {
            html = false;
        }
        try {
            readReceipt = Boolean.parseBoolean(sendMailInputs.getReadReceipt());
        } catch (Exception e) {
            readReceipt = false;
        }
        smtpHost = sendMailInputs.getSMTPHostname();
        smtpPort = Integer.parseInt(sendMailInputs.getPort());
        from = sendMailInputs.getFrom();
        to = sendMailInputs.getTo();
        cc = sendMailInputs.getCc();
        bcc = sendMailInputs.getBcc();
        subject = sendMailInputs.getSubject();
        body = sendMailInputs.getBody();
        String strAttachments = sendMailInputs.getAttachments();
        attachments = (null != strAttachments) ? strAttachments : "";
        String strDelimiter = sendMailInputs.getDelimiter();
        delimiter = (isEmpty(strDelimiter)) ? "," : strDelimiter;
        user = sendMailInputs.getUser();
        String pass = sendMailInputs.getPassword();
        password = (null == pass) ? "" : pass;
        charset = sendMailInputs.getCharacterset();
        transferEncoding = sendMailInputs.getContentTransferEncoding();
        String strHtml = sendMailInputs.getHtmlEmail();
        // Default value for html is false
        html = ((null != strHtml) && ("true".equalsIgnoreCase(strHtml)));
        String strReadReceipt = sendMailInputs.getReadReceipt();
        // Default value for readReceipt is false
        readReceipt = ((null != strReadReceipt) && ("true".equalsIgnoreCase(strReadReceipt)));
        // By default the charset will be UTF-8
        if (isEmpty(charset)) {
            charset = "UTF-8";
        }
        // By default the filename encoding scheme will be Q
        encodingScheme = "Q";
        // By default the content transfer encoding for body and attachment will be quoted-printable
        if (isEmpty(transferEncoding)) {
            transferEncoding = "quoted-printable";
        }
        // Encoding for filename is either Q or B, so if the transferEncoding is not quoted-printable then it will be B
        // encoding.
        if (!"quoted-printable".equals(transferEncoding)) {
            encodingScheme = "B";
        }
        this.keystoreFile = sendMailInputs.getEncryptionKeystore();
        if (isNotEmpty(keystoreFile)) {
            if (!keystoreFile.startsWith(HTTP)) {
                keystoreFile = FILE + keystoreFile;
            }

            encryptMessage = true;
            keyAlias = sendMailInputs.getEncryptionKeyAlias();
            if (null == keyAlias) {
                keyAlias = "";
            }
            keystorePass = sendMailInputs.getEncryptionKeystorePassword();
            if (null == keystorePass) {
                keystorePass = "";
            }
        } else {
            encryptMessage = false;
        }

        try {
            enableTLS = Boolean.parseBoolean(sendMailInputs.getEnableTLS());
        } catch (Exception e) {
            enableTLS = false;
        }

        String rowDelimiterInput = sendMailInputs.getRowDelimiter();
        if (isEmpty(rowDelimiterInput)) {
            rowDelimiter = "\n";
        } else {
            rowDelimiter = rowDelimiterInput;
        }
        String columnDelimiterInput = sendMailInputs.getColumnDelimiter();
        if (isEmpty(columnDelimiterInput)) {
            columnDelimiter = ":";
        } else {
            columnDelimiter = columnDelimiterInput;
        }

        validateDelimiters(rowDelimiter, columnDelimiter);

        String headersMap = sendMailInputs.getHeaders();
        if (!isEmpty(headersMap)) {
            Object[] headers = extractHeaderNamesAndValues(headersMap, rowDelimiter, columnDelimiter);
            headerNames = (ArrayList<String>) headers[0];
            headerValues = (ArrayList<String>) headers[1];
        }

        String timeout = sendMailInputs.getTimeout();
        if (timeout != null && !timeout.isEmpty()) {
            this.timeout = Integer.parseInt(timeout);
            if (this.timeout <= 0) {
                throw new Exception("timeout value must be a positive number");
            }
            this.timeout *= ONE_SECOND; //timeouts in seconds
        }

        encryptionOID = encryptionAlgorithmToEncryptionOID(sendMailInputs.getEncryptionAlgorithm());
    }

    private String encryptionAlgorithmToEncryptionOID(String encryptionAlgorithmStr) throws Exception {
        EncryptionAlgorithmsEnum encryptionAlgorithm = getEncryptionAlgorithm(encryptionAlgorithmStr);
        return encryptionAlgorithm.getEncryptionOID();
    }

    /**
     * This method checks if the delimiters are equal and if the row delimiter is a substring of the column delimiter
     * and throws an exception with the appropriate message.
     * @param rowDelimiter
     * @param columnDelimiter
     * @throws Exception
     */
    protected void validateDelimiters(String rowDelimiter, String columnDelimiter) throws Exception {
        if (rowDelimiter.equals(columnDelimiter)) {
            throw new Exception(INVALID_DELIMITERS);
        }
        if (StringUtils.contains(columnDelimiter, rowDelimiter)) {
            throw new Exception(INVALID_ROW_DELIMITER);
        }
    }

    /**
     * This method extracts and returns an object containing two Lists. A list with the header names and a list with the
     * header values. Values found on same position in the two lists correspond to each other.
     * @param headersMap
     * @param rowDelimiter
     * @param columnDelimiter
     * @return
     * @throws Exception
     */
    protected Object[] extractHeaderNamesAndValues(String headersMap, String rowDelimiter, String columnDelimiter)
            throws Exception {
        String[] rows = headersMap.split(Pattern.quote(rowDelimiter));
        ArrayList<String> headerNames = new ArrayList<>();
        ArrayList<String> headerValues = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            if (isEmpty(rows[i])) {
                continue;
            } else {
                if (validateRow(rows[i], columnDelimiter, i)) {
                    String[] headerNameAndValue = rows[i].split(Pattern.quote(columnDelimiter));
                    headerNames.add(i, headerNameAndValue[0].trim());
                    headerValues.add(i, headerNameAndValue[1].trim());
                }
            }
        }
        return new Object[]{headerNames, headerValues};
    }

    /**
     * This method validates a row contained in the 'headers' input of the operation.
     * @param row The value of the row to be validated.
     * @param columnDelimiter The delimiter that separates the header name from the header value.
     * @param rowNumber The row number inside the 'headers' input.
     * @return This method returns true if the row contains a header name and a header value.
     * @throws Exception
     */
    protected boolean validateRow(String row, String columnDelimiter, int rowNumber) throws Exception {
        if (row.contains(columnDelimiter)) {
            if (row.equals(columnDelimiter)) {
                throw new Exception(format(ROW_WITH_EMPTY_HEADERS_INPUT, rowNumber + 1));
            } else {
                String[] headerNameAndValue = row.split(Pattern.quote(columnDelimiter));
                if (StringUtils.countMatches(row, columnDelimiter) > 1) {
                    throw new Exception(format(ROW_WITH_MULTIPLE_COLUMN_DELIMITERS_IN_HEADERS_INPUT, rowNumber + 1));
                } else {
                    if (headerNameAndValue.length == 1) {
                        throw new Exception(format(ROW_WITH_MISSING_VALUE_FOR_HEADER, rowNumber + 1));
                    } else {
                        return true;
                    }
                }
            }
        } else {
            throw new Exception("Row #" + (rowNumber + 1) + " in the 'headers' input has no column delimiter.");
        }
    }

    /**
     * The method creates a copy of the SMTPMessage object passed through the arguments list and adds the headers to the
     * copied object then returns it. If the header is already present in the message then its values list will be
     * updated with the given header value.
     * @param message The SMTPMessage object to which the headers are added or updated.
     * @param headerNames A list of strings containing the header names that need to be added or updated.
     * @param headerValues A list of strings containing the header values that need to be added.
     * @return The method returns the message with the headers added.
     * @throws MessagingException
     */
    protected SMTPMessage addHeadersToSMTPMessage(SMTPMessage message, List<String> headerNames,
                                                  List<String> headerValues) throws MessagingException {
        SMTPMessage msg = new SMTPMessage(message);
        Iterator namesIter = headerNames.iterator();
        Iterator valuesIter = headerValues.iterator();
        while (namesIter.hasNext() && valuesIter.hasNext()) {
            String headerName = (String) namesIter.next();
            String headerValue = (String) valuesIter.next();
            if (msg.getHeader(headerName) != null) {
                // then a header with this name already exists, add the headerValue to the existing values list.
                msg.addHeader(headerName, headerValue);
            } else {
                msg.setHeader(headerName, headerValue);
            }
        }
        return msg;
    }
}
