package com.hp.score.content.mail.entities;

import com.hp.score.content.mail.actionsinp.GetMailMessageInputs;
import com.hp.score.content.mail.sslconfig.EasyX509TrustManager;
import com.hp.score.content.mail.sslconfig.SSLUtils;
import com.sun.mail.util.ASCIIUtility;

import javax.mail.*;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by giloan on 11/3/2014.
 */
public class GetMailMessage {

    public static final String RETURN_RESULT = "returnResult";
    public static final String SUBJECT_RESULT = "Subject";
    public static final String BODY_RESULT = "Body";
    public static final String ATTACHED_FILE_NAMES_RESULT = "AttachedFileNames";
    public static final String RETURN_CODE = "returnCode";
    public static final String EXCEPTION = "exception";

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    public static final String SUCCESS_RETURN_CODE = "0";
    public static final String FAILURE_RETURN_CODE = "-1";

    //Operation inputs
    private String host;
    private String port;
    private String protocol;
    private String username;
    private String password;
    private String folder;
    private boolean trustAllRoots;
    /**
     * The relative position of the message in the folder. Numbering starts from 1.
     */
    private int messageNumber;
    private boolean subjectOnly = true;
    private boolean enableSSL;
    private String keystore;
    private String keystorePassword;
    private String trustKeystoreFile;
    private String trustPassword;
    private String characterSet;
    private boolean deleteUponRetrieval;

    public Map<String, String> execute(GetMailMessageInputs getMailMessageInputs) throws Exception {
        Map<String, String> result = new HashMap<>();
        try {
            processInputs(getMailMessageInputs);
            Store store = configureStore();
            Folder f = store.getFolder(folder);
            if (!f.exists()) {
                throw new Exception("The specified folder does not exist on the remote server.");
            }
            f.open(getFolderOpenMode());
            if (messageNumber > f.getMessageCount())
                throw new IndexOutOfBoundsException("message value was: " + messageNumber + " there are only " + f.getMessageCount() + " messages in folder");
            Message message = f.getMessage(messageNumber);
            //delete message
            if (deleteUponRetrieval) {
                message.setFlag(Flags.Flag.DELETED, true);
            }
            if (subjectOnly) {
                String subject = "";
                if ((characterSet != null) && (characterSet.trim().length() > 0)) { //need to force the decode charset
                    subject = message.getHeader("Subject")[0];
                    subject = changeHeaderCharset(subject, characterSet);
                    subject = MimeUtility.decodeText(subject);
                } else {
                    subject = message.getSubject();
                }
                if (subject == null) {
                    subject = "";
                }
                result.put(RETURN_RESULT, MimeUtility.decodeText(subject));
            } else {
                try {
                    // Get subject and attachedFileNames
                    if ((characterSet != null) && (characterSet.trim().length() > 0)) { //need to force the decode charset
                        String subject = message.getHeader("Subject")[0];
                        subject = changeHeaderCharset(subject, characterSet);
                        result.put(SUBJECT_RESULT, MimeUtility.decodeText(subject));
                        String attachedFileNames = changeHeaderCharset(getAttachedFileNames(message), characterSet);
                        result.put(ATTACHED_FILE_NAMES_RESULT, decodeAttachedFileNames(attachedFileNames));
                    } else { //let everything as the sender intended it to be :)
                        String subject = message.getSubject();
                        if (subject == null)
                            subject = "";
                        result.put(SUBJECT_RESULT, MimeUtility.decodeText(subject));
                        result.put(ATTACHED_FILE_NAMES_RESULT, decodeAttachedFileNames((getAttachedFileNames(message))));
                    }
                    // Get the message body
                    result.put(BODY_RESULT, MimeUtility.decodeText(getMessageContent(message, characterSet)));
                    StringOutputStream stream = new StringOutputStream();
                    message.writeTo(stream);
                    result.put(RETURN_RESULT, stream.toString().replaceAll("" + (char) 0, ""));
                } catch (UnsupportedEncodingException except) {
                    throw new UnsupportedEncodingException("The given encoding (" + characterSet + ") is invalid or not supported.");
                }
            }
            result.put(RETURN_CODE, SUCCESS_RETURN_CODE);
        } catch (Exception e) {
            if (e.toString().contains("Unrecognized SSL message")) {
                throw new Exception("Unrecognized SSL message, plaintext connection?");
            } else {
                throw e;
            }
        }
        return result;
    }

    private Store configureStore() throws Exception {
        Properties props = new Properties();
        Authenticator auth = new SimpleAuthenticator(username, password);
        Store store;
        if (!enableSSL) {
            props.put("mail." + protocol + ".host", host);
            props.put("mail." + protocol + ".port", port);
            Session s = Session.getInstance(props, auth);
            store = s.getStore(protocol);
            store.connect();


        } else {
            addSSLSettings(trustAllRoots, keystore, keystorePassword, trustKeystoreFile, trustPassword);

            String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            props.setProperty("mail." + protocol + ".socketFactory.class", SSL_FACTORY);
            props.setProperty("mail." + protocol + ".socketFactory.fallback", "false");
            props.setProperty("mail." + protocol + ".port", port);
            props.setProperty("mail." + protocol + ".socketFactory.port", port);
            URLName url = new URLName(protocol, host, Integer.parseInt(port), "", username, password);
            Session session = Session.getInstance(props, auth);
            store = session.getStore(url);
            store.connect();
        }
        return store;
    }

    public void addSSLSettings(boolean trustAllRoots, String keystore,
                               String keystorePassword, String trustKeystore, String trustPassword) throws Exception {
        boolean useClientCert = false;
        boolean useTrustCert = false;

        String separator = System.getProperty("file.separator");
        String javaKeystore = System.getProperty("java.home") + separator + "lib" + separator + "security" + separator + "cacerts";
        if (keystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            keystore = (storeExists) ? "file:" + javaKeystore : null;
            if (null != keystorePassword) {
                if (keystorePassword.equals("")) {
                    keystorePassword = "changeit";
                }
            }
            useClientCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!keystore.startsWith("http"))
                    keystore = "file:" + keystore;
                useClientCert = true;
            }
        }
        if (trustKeystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            trustKeystore = (storeExists) ? "file:" + javaKeystore : null;
            trustPassword = (storeExists) ? ((trustPassword.equals("")) ? "changeit" : trustPassword) : null;

            useTrustCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!trustKeystore.startsWith("http"))
                    trustKeystore = "file:" + trustKeystore;
                useTrustCert = true;
            }
        }

        SSLContext context = SSLContext.getInstance("SSL");
        TrustManager[] trustManagers = null;
        KeyManager[] keyManagers = null;

        if (trustAllRoots) {
            trustManagers = new TrustManager[]{new EasyX509TrustManager(null)};
        }

        if (useTrustCert) {
            KeyStore trustKeyStore = SSLUtils.createKeyStore(new URL(trustKeystore), trustPassword);
            trustManagers = SSLUtils.createAuthTrustManagers(trustKeyStore);
        }
        if (useClientCert) {
            KeyStore clientKeyStore = SSLUtils.createKeyStore(new URL(keystore), keystorePassword);
            keyManagers = SSLUtils.createKeyManagers(clientKeyStore, keystorePassword);
        }

        context.init(keyManagers, trustManagers, new SecureRandom());
        SSLContext.setDefault(context);
    }

    private void processInputs(GetMailMessageInputs getMailMessageInputs) throws Exception {
        host = getMailMessageInputs.getHostname();
        port = getMailMessageInputs.getPort();
        protocol = getMailMessageInputs.getProtocol();
        username = getMailMessageInputs.getUsername();
        password = getMailMessageInputs.getPassword();
        folder = getMailMessageInputs.getFolder();
        String trustAll = getMailMessageInputs.getTrustAllRoots();
        // Default value of trustAllRoots is true
        trustAllRoots = !(null != trustAll && trustAll.equalsIgnoreCase("false"));
        messageNumber = Integer.parseInt(getMailMessageInputs.getMessageNumber());
        String strSubOnly = getMailMessageInputs.getSubjectOnly();
        // Default value of subjectOnly is false
        subjectOnly = (strSubOnly != null && strSubOnly.equalsIgnoreCase("true"));
        String strEnableSSL = getMailMessageInputs.getEnableSSL();
        // Default value of enableSSL is false;
        enableSSL = (null != strEnableSSL && strEnableSSL.equalsIgnoreCase("true"));
        keystore = getMailMessageInputs.getKeystore();
        keystorePassword = getMailMessageInputs.getKeystorePassword();
        trustKeystoreFile = getMailMessageInputs.getTrustKeystore();
        trustPassword = getMailMessageInputs.getTrustPassword();
        characterSet = getMailMessageInputs.getCharacterSet();
        String strDeleteUponRetrieval = getMailMessageInputs.getDeleteUponRetrieval();
        // Default value for deleteUponRetrieval is false
        deleteUponRetrieval = (null != strDeleteUponRetrieval && strDeleteUponRetrieval.equalsIgnoreCase("true"));

        if (messageNumber < 1) {
            throw new Exception("Messages are numbered starting at 1 through the total number of messages in the folder!");
        }
        if ((protocol == null || protocol.equals("")) && (port == null || port.equals(""))) {
            throw new Exception("Please specify the port, the protocol, or both.");
        } else if ((protocol != null && !protocol.equals("")) && (!protocol.equalsIgnoreCase("imap")) && (!protocol.equalsIgnoreCase("pop3")) && (!protocol.equalsIgnoreCase("imap4"))
                && (port == null || port.equals(""))) {
            throw new Exception("Please specify the port for the indicated protocol.");
        } else if ((protocol == null || protocol.equals("")) && (port != null && !port.equals(""))
                && (!port.equalsIgnoreCase("143")) && (!port.equalsIgnoreCase("110"))) {
            throw new Exception("Please specify the protocol for the indicated port.");
        } else if ((protocol == null || protocol.equals("")) && (port.trim().equalsIgnoreCase("143"))) {
            protocol = "imap";
        } else if ((protocol == null || protocol.equals("")) && (port.trim().equalsIgnoreCase("110"))) {
            protocol = "pop3";
        } else if ((protocol.trim().equalsIgnoreCase("pop3")) && (port == null || port.equals(""))) {
            port = "110";
        } else if ((protocol.trim().equalsIgnoreCase("imap")) && (port == null || port.equals(""))) {
            port = "143";
        } else if ((protocol.trim().equalsIgnoreCase("imap4")) && (port == null || port.equals(""))) {
            port = "143";
        }

        //The protocol should be given in lowercase to be recognised.
        protocol = protocol.toLowerCase();
        if ((protocol != null) && protocol.trim().equalsIgnoreCase("imap4")) {
            protocol = "imap";
        }
    }

    public String getMessageContent(Message message, String characterSet) throws Exception {
        String cmessage = "";

        if (message.isMimeType("text/plain")) {
            cmessage = MimeUtility.decodeText(message.getContent().toString());
        } else if (message.isMimeType("text/html")) {
            cmessage = MimeUtility.decodeText(convertMessage(message.getContent().toString()));
        } else {
            Object obj = message.getContent();
            Multipart mpart = (Multipart) obj;
            for (int i = 0, n = mpart.getCount(); i < n; i++) {

                Part part = mpart.getBodyPart(i);
                String disposition = part.getDisposition();

                if (disposition == null) {
                    if (part.getContent() instanceof MimeMultipart) { // multipart with attachment
                        MimeMultipart mm = (MimeMultipart) part.getContent();
                        for (int j = 0; j < mm.getCount(); j++)
                            if (mm.getBodyPart(j).getContent() instanceof String) {
                                BodyPart bodyPart = mm.getBodyPart(j);
                                if ((characterSet != null) && (characterSet.trim().length() > 0)) {
                                    String contentType = bodyPart.getHeader("Content-Type")[0];
                                    contentType = contentType.replace(contentType.substring(contentType.indexOf("=") + 1), characterSet);
                                    bodyPart.setHeader("Content-Type", contentType);
                                }
                                cmessage += MimeUtility.decodeText(bodyPart.getContent().toString());
                            }
                    } else {//multipart - w/o attachment
                        //if the user has specified a certain characterSet we decode his way
                        if ((characterSet != null) && (characterSet.trim().length() > 0)) {
                            InputStream istream = part.getInputStream();
                            ByteArrayInputStream bis = new ByteArrayInputStream(ASCIIUtility.getBytes(istream));
                            int count = bis.available();
                            byte[] bytes = new byte[count];
                            count = bis.read(bytes, 0, count);
                            cmessage = MimeUtility.decodeText(new String(bytes, 0, count, characterSet));
                        } else
                            cmessage = MimeUtility.decodeText((String) part.getContent().toString());
                    }
                }
            }//for
        }//else
        return cmessage;
    }

    public String getAttachedFileNames(Part part) throws Exception {
        String fileNames = "";
        Object content = part.getContent();
        if (!(content instanceof Multipart)) {
            // non-Multipart MIME part ...
            // is the file name set for this MIME part? (i.e. is it an attachment?)
            if (part.getFileName() != null && !part.getFileName().equals("") && part.getInputStream() != null) {
                String fileName = part.getFileName();
                // is the file name encoded? (consider it is if it's in the =?charset?encoding?encoded text?= format)
                if (fileName.indexOf('?') == -1)
                    // not encoded  (i.e. a simple file name not containing '?')-> just return the file name
                    return fileName;
                // encoded file name -> remove any chars before the first "=?" and after the last "?="
                return fileName.substring(fileName.indexOf("=?"), fileName.length() -
                        ((new StringBuilder(fileName)).reverse()).indexOf("=?"));
            }
        } else {
            // a Multipart type of MIME part
            Multipart mpart = (Multipart) content;
            // iterate through all the parts in this Multipart ...
            for (int i = 0, n = mpart.getCount(); i < n; i++) {
                if (!fileNames.equals(""))
                    fileNames += ",";
                // to the list of attachments built so far append the list of attachments in the current MIME part ...
                fileNames += getAttachedFileNames(mpart.getBodyPart(i));
            }
        }
        return fileNames;
    }

    private String decodeAttachedFileNames(String attachedFileNames) throws Exception {
        StringBuilder sb = new StringBuilder();
        String delimiter = "";
        // splits the input into comma-separated chunks and decodes each chunk according to its encoding ...
        for (String fileName : attachedFileNames.split(",")) {
            sb.append(delimiter + MimeUtility.decodeText(fileName));
            delimiter = ",";
        }
        // return the concatenation of the decoded chunks ...
        return sb.toString();
    }

    public String convertMessage(String msg) throws Exception {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < msg.length(); i++) {
            char c = msg.charAt(i);
            if (c == '\n') {
                sb.append("<br>");
            } else {
                sb.append(c);
            }

        }
        String returnString = sb.toString();
        return returnString;
    }

    private int getFolderOpenMode() {
        return Folder.READ_ONLY;
    }

    /**
     * This method addresses the mail headers which contain encoded words. The syntax for an encoded word is defined
     * in RFC 2047 section 2: http://www.faqs.org/rfcs/rfc2047.html In some cases the header is marked as having a certain charset
     * but at decode not all the characters a properly decoded. This is why it can be useful to force it to decode the text with
     * a different charset.
     * For example when sending an email using Mozilla Thunderbird and JIS X 0213 characters the subject and attachment headers
     * are marked as =?Shift_JIS? but the JIS X 0213 characters are only supported in windows-31j.
     * <p/>
     * This method replaces the charset tag of the header with the new charset provided by the user.
     *
     * @param header     - The header in which the charset will be replaced.
     * @param newCharset - The new charset that will be replaced in the given header.
     * @return The header with the new charset.
     */
    public String changeHeaderCharset(String header, String newCharset) {
        return header.replaceAll("=\\?[^\\(\\)<>@,;:/\\[\\]\\?\\.= ]+\\?", "=?" + newCharset + "?"); //match for =?charset?
    }
}