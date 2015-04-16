package io.cloudslang.content.mail.services;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import com.sun.mail.smtp.SMTPMessage;
import io.cloudslang.content.mail.entities.SendMailInputs;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

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
    int smtpPort;
    boolean html;
    boolean readReceipt;
    boolean encryptMessage;
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
                mimeBodyPart.setContent(body, "text/html" + ";charset=" + charset);
            } else {
                mimeBodyPart.setContent(body, "text/plain" + ";charset=" + charset);
            }
            mimeBodyPart.setHeader("Content-Transfer-Encoding", transferEncoding);
            if(encryptMessage) {
                mimeBodyPart = gen.generate(mimeBodyPart, SMIMEEnvelopedGenerator.RC2_CBC, BOUNCY_CASTLE_PROVIDER);
            }
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
                    if(encryptMessage) {
                        messageBodyPart = gen.generate(messageBodyPart, SMIMEEnvelopedGenerator.RC2_CBC, BOUNCY_CASTLE_PROVIDER);
                    }
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

    private void processInputs(SendMailInputs sendMailInputs) {
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
    }
}
