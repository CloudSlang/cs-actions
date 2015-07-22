package io.cloudslang.content.mail.services;

import com.sun.mail.smtp.SMTPMessage;
import io.cloudslang.content.mail.entities.SendMailInputs;
import io.cloudslang.content.mail.utils.HtmlImageNodeVisitor;
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
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

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

    public static final String MAIL_WAS_SENT = "SentMailSuccessfully";
    public static final String FILE = "file:";
    public static final String HTTP = "http";

    public static final String PKCS_KEYSTORE_TYPE = "PKCS12";
    public static final String BOUNCY_CASTLE_PROVIDER = "BC";
    public static final String THE_COLUMN_DELIMITER_AND_ROW_DELIMITER_INPUTS_HAVE_THE_SAME_VALUE = "The columnDelimiter and rowDelimiter inputs have the same value. They need to be different.";
    public static final String THE_ROW_DELIMITER_CAN_T_BE_A_SUBSTRING_OF_THE_COLUMN_DELIMITER = "The rowDelimiter can't be a substring of the columnDelimiter!";

    //Operation inputs
    String attachments;
    String smtpHost;
    String from;
    String to;
    String cc;
    String bcc;
    String subject;
    String body;
    String delimiter;
    String user;
    String password;
    String charset;
    String transferEncoding;
    String encodingScheme;
    String keystoreFile;
    String keyAlias;
    String keystorePass;
    List<String> headerNames;
    List<String> headerValues;
    String rowDelimiter;
    String columnDelimiter;
    int smtpPort;
    int timeout = -1;
    boolean html;
    boolean readReceipt;
    boolean encryptMessage;
    boolean enableTLS;
    SMIMEEnvelopedGenerator gen;

    public Map<String, String> execute(SendMailInputs sendMailInputs) throws Exception {
        Map<String, String> result = new HashMap<>();
        Transport transport = null;

        try {
            processInputs(sendMailInputs);

            String[] recipients = to.split(Pattern.quote(delimiter));
            // Create a mail session
            java.util.Properties props = new java.util.Properties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", "" + smtpPort);

            if (null != user && user.length() > 0) {
                props.put("mail.smtp.user", user);
                props.put("mail.smtp.password", password);
                props.put("mail.smtp.auth", "true");
            }
            if(enableTLS) {
                props.put("mail.smtp.starttls.enable", "true");
            }
            if(timeout > 0) {
                props.put("mail.smtp.timeout",timeout);
            }


            Session session = Session.getInstance(props, null);

            //construct encryption SMIMEEnvelopedGenerator
            if(encryptMessage) {
                addEncryptionSettings();
            }

            // Construct the message
            SMTPMessage msg = new SMTPMessage(session);
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
                    if(!source.getFile().exists()) {
                        throw new FileNotFoundException("Cannot attach " + attachment);
                    }

                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setHeader("Content-Transfer-Encoding", transferEncoding);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName((MimeUtility.encodeText(attachment.substring(attachment.lastIndexOf(java.io.File.separator) + 1), charset,
                            encodingScheme)));
                    messageBodyPart = encryptMimeBodyPart(messageBodyPart);
                    multipart.addBodyPart(messageBodyPart);
                }
            }
            msg.setContent(multipart);
            msg.setFrom(new InternetAddress(from));
            msg.setSubject(MimeUtility.encodeText(subject.replaceAll("[\\n\\r]", " "), charset, encodingScheme));

            if (readReceipt) {
                msg.setNotifyOptions(SMTPMessage.NOTIFY_DELAY + SMTPMessage.NOTIFY_FAILURE + SMTPMessage.NOTIFY_SUCCESS);
            }

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
            }
            else {
                Transport.send(msg);
            }
            result.put(RETURN_RESULT, MAIL_WAS_SENT);
            result.put(RETURN_CODE, SUCCESS_RETURN_CODE);
        }  catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("IOException")) {
                throw new Exception("Cannot attach '" + attachments + "' Check if the location exists and is accessible.");
            } else {
                throw e;
            }
        } finally {
            if (null != transport) transport.close();
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

    private MimeBodyPart getImageMimeBodyPart(Map<String, String> base64ImagesMap, String contentId) throws MessagingException {
        MimeBodyPart imagePart = new MimeBodyPart();
        imagePart.setContentID(contentId);
        imagePart.setHeader("Content-Transfer-Encoding", "base64");
        imagePart.setDataHandler(new DataHandler(Base64.decode(base64ImagesMap.get(contentId)), "image/png;"));
        return imagePart;
    }

    private MimeBodyPart encryptMimeBodyPart(MimeBodyPart mimeBodyPart) throws NoSuchAlgorithmException,
            NoSuchProviderException, SMIMEException {
        if(encryptMessage) {
            mimeBodyPart = gen.generate(mimeBodyPart, SMIMEEnvelopedGenerator.RC2_CBC, BOUNCY_CASTLE_PROVIDER);
        }
        return mimeBodyPart;
    }

    private void addEncryptionSettings() throws  Exception{
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

        if(keyAlias.equals("")) {
            Enumeration e = ks.aliases();
            while (e.hasMoreElements()) {
                String alias = (String) e.nextElement();

                if (ks.isKeyEntry(alias)) {
                    keyAlias = alias;
                }
            }
        }

        if (keyAlias.equals(""))
        {
            throw new Exception("Can't find a public key!");
        }

        Certificate[]   chain = ks.getCertificateChain(keyAlias);

        if(chain == null) {
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
        delimiter = (strDelimiter == null || strDelimiter.equals("")) ? "," : strDelimiter;
        user = sendMailInputs.getUser();
        String pass = sendMailInputs.getPassword();
        password = (null == pass) ? "" : pass;
        charset = sendMailInputs.getCharacterset();
        transferEncoding = sendMailInputs.getContentTransferEncoding();
        String strHtml = sendMailInputs.getHtmlEmail();
        // Default value for html is false
        html = ((null != strHtml) && (strHtml.equalsIgnoreCase("true")));
        String strReadReceipt = sendMailInputs.getReadReceipt();
        // Default value for readReceipt is false
        readReceipt = ((null != strReadReceipt) && (strReadReceipt.equalsIgnoreCase("true")));
        // By default the charset will be UTF-8
        if (charset == null || charset.equals("")) {
            charset = "UTF-8";
        }
        // By default the filename encoding scheme will be Q
        encodingScheme = "Q";
        // By default the content transfer encoding for body and attachment will be quoted-printable
        if (transferEncoding == null || transferEncoding.equals("")) {
            transferEncoding = "quoted-printable";
        }
        // Encoding for filename is either Q or B, so if the transferEncoding is not quoted-printable then it will be B encoding.
        if (!transferEncoding.equals("quoted-printable")) {
            encodingScheme = "B";
        }
        this.keystoreFile = sendMailInputs.getEncryptionKeystore();
        if(this.keystoreFile != null && !this.keystoreFile.equals("")) {
            if (!keystoreFile.startsWith(HTTP)) {
                keystoreFile = FILE + keystoreFile;
            }

            encryptMessage = true;
            keyAlias = sendMailInputs.getEncryptionKeyAlias();
            if(null == keyAlias) {
                keyAlias = "";
            }
            keystorePass = sendMailInputs.getEncryptionKeystorePassword();
            if(null == keystorePass) {
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
        if(StringUtils.isEmpty(rowDelimiterInput)) {
            rowDelimiter = "\n";
        } else {
            rowDelimiter = rowDelimiterInput;
        }
        String columnDelimiterInput = sendMailInputs.getColumnDelimiter();
        if(StringUtils.isEmpty(columnDelimiterInput)) {
            columnDelimiter = ":";
        } else {
            columnDelimiter = columnDelimiterInput;
        }

        validateDelimiters(rowDelimiter, columnDelimiter);

        String headersMap = sendMailInputs.getHeaders();
        if(!StringUtils.isEmpty(headersMap)) {
            Object[] headers = extractHeaderNamesAndValues(headersMap, rowDelimiter, columnDelimiter);
            headerNames = (ArrayList<String>) headers[0];
            headerValues = (ArrayList<String>) headers[1];
        }

        String timeout = sendMailInputs.getTimeout();
        if(timeout != null && !timeout.isEmpty()) {
            this.timeout = Integer.parseInt(timeout);
            if(this.timeout <= 0) {
                throw new Exception("timeout value must a positive number");
            }
            this.timeout *= 1000; //timeouts in seconds
        } else {
            this.timeout = -1;
        }

    }

    /**
     * This method checks if the delimiters are equal and if the row delimiter is a substring of the column delimiter and throws an exception with the apropriate message.
     * @param rowDelimiter
     * @param columnDelimiter
     * @throws Exception
     */
    protected void validateDelimiters(String rowDelimiter, String columnDelimiter) throws Exception {
        if(rowDelimiter.equals(columnDelimiter)) {
            throw new Exception(THE_COLUMN_DELIMITER_AND_ROW_DELIMITER_INPUTS_HAVE_THE_SAME_VALUE);
        }
        if(StringUtils.contains(columnDelimiter, rowDelimiter)) {
            throw new Exception(THE_ROW_DELIMITER_CAN_T_BE_A_SUBSTRING_OF_THE_COLUMN_DELIMITER);
        }
    }

    /**
     * This method extracts and returns an object containing two Lists. A list with the header names and a list with the header values.
     * Values found on same position in the two lists correspond to each other.
     * @param headersMap
     * @param rowDelimiter
     * @param columnDelimiter
     * @return
     * @throws Exception
     */
    protected Object[] extractHeaderNamesAndValues(String headersMap, String rowDelimiter, String columnDelimiter) throws Exception {
        String[] rows = headersMap.split(Pattern.quote(rowDelimiter));
        ArrayList<String> headerNames = new ArrayList<>();
        ArrayList<String> headerValues = new ArrayList<>();
        for (int i = 0; i < rows.length; i++) {
            if (StringUtils.isEmpty(rows[i])) {
                continue;
            }
            else {
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
                throw new Exception("Row #" + (rowNumber + 1) + " in the 'headers' input does not contain any values.");
            } else {
                String[] headerNameAndValue = row.split(Pattern.quote(columnDelimiter));
                if (StringUtils.countMatches(row, columnDelimiter) > 1) {
                    throw new Exception("Row #" + (rowNumber + 1) + " in the 'headers' input has more than one column delimiter.");
                } else {
                    if (headerNameAndValue.length == 1) {
                        throw new Exception("Row #" + (rowNumber + 1) + " in the 'headers' input is missing one of the header values.");
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
     * The method creates a copy of the SMTPMessage object passed through the arguments list and adds the headers to the copied object then returns it. If the header is already present in the message then its values list will be updated with the given header value.
     * @param message The SMTPMessage object to which the headers are added or updated.
     * @param headerNames A list of strings containing the header names that need to be added or updated.
     * @param headerValues A list of strings containing the header values that need to be added.
     * @return The method returns the message with the headers added.
     * @throws MessagingException
     */
    protected SMTPMessage addHeadersToSMTPMessage(SMTPMessage message, List<String> headerNames, List<String> headerValues) throws MessagingException {
        SMTPMessage msg = new SMTPMessage(message);
        Iterator namesIter = headerNames.iterator();
        Iterator valuesIter = headerValues.iterator();
        while (namesIter.hasNext() && valuesIter.hasNext()) {
            String headerName = (String) namesIter.next();
            String headerValue = (String) valuesIter.next();
            if (msg.getHeader(headerName) != null) { // then a header with this name already exists, add the headerValue to the existing values list.
                msg.addHeader(headerName, headerValue);
            } else {
                msg.setHeader(headerName, headerValue);
            }
        }
        return msg;
    }
}
