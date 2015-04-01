package org.openscore.content.mail.services;

import net.suberic.crypto.EncryptionKeyManager;
import net.suberic.crypto.EncryptionManager;
import net.suberic.crypto.EncryptionUtils;
import org.openscore.content.mail.entities.GetMailMessageInputs;
import org.openscore.content.mail.entities.SimpleAuthenticator;
import org.openscore.content.mail.entities.StringOutputStream;
import org.openscore.content.mail.sslconfig.EasyX509TrustManager;
import org.openscore.content.mail.sslconfig.SSLUtils;
import com.sun.mail.util.ASCIIUtility;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
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
import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.*;

/**
 * Created by giloan on 11/3/2014.
 */
public class GetMailMessage {

    public static final String RETURN_RESULT = "returnResult";
    public static final String SUBJECT = "Subject";
    public static final String BODY_RESULT = "Body";
    public static final String PLAIN_TEXT_BODY_RESULT = "plainTextBody";
    public static final String ATTACHED_FILE_NAMES_RESULT = "AttachedFileNames";
    public static final String RETURN_CODE = "returnCode";
    public static final String EXCEPTION = "exception";

    public static final String SUCCESS = "success";
    public static final String FAILURE = "failure";

    public static final String SUCCESS_RETURN_CODE = "0";
    public static final String FAILURE_RETURN_CODE = "-1";
    public static final String FILE = "file:";
    public static final String HTTP = "http";
    public static final String DEFAULT_PASSWORD_FOR_STORE = "changeit";
    public static final String POP3 = "pop3";
    public static final String IMAP = "imap";
    public static final String IMAP_4 = "imap4";
    public static final String IMAP_PORT = "143";
    public static final String POP3_PORT = "110";
    public static final String PLEASE_SPECIFY_THE_PORT_FOR_THE_INDICATED_PROTOCOL = "Please specify the port for the indicated protocol.";
    public static final String PLEASE_SPECIFY_THE_PORT_THE_PROTOCOL_OR_BOTH = "Please specify the port, the protocol, or both.";
    public static final String PLEASE_SPECIFY_THE_PROTOCOL_FOR_THE_INDICATED_PORT = "Please specify the protocol for the indicated port.";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_HTML = "text/html";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String SSL = "SSL";
    public static final String STR_FALSE = "false";
    public static final String STR_TRUE = "true";
    public static final String MESSAGES_ARE_NUMBERED_STARTING_AT_1 = "Messages are numbered starting at 1 through the total number of messages in the folder!";
    public static final String STR_COMMA = ",";
    public static final String THE_SPECIFIED_FOLDER_DOES_NOT_EXIST_ON_THE_REMOTE_SERVER = "The specified folder does not exist on the remote server.";
    public static final String UNRECOGNIZED_SSL_MESSAGE = "Unrecognized SSL message";
    public static final String UNRECOGNIZED_SSL_MESSAGE_PLAINTEXT_CONNECTION = "Unrecognized SSL message, plaintext connection?";
    public static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private static final String HOST_NOT_SPECIFIED = "The required host input is not specified!";
    private static final String MESSAGE_NUMBER_NOT_SPECIFIED = "The required messageNumber input is not specified!";
    private static final String USERNAME_NOT_SPECIFIED = "The required username input is not specified!";
    private static final String FOLDER_NOT_SPECIFIED = "The required folder input is not specified!";

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
    private String decryptionKeystore;
    private String decryptionKeyAlias;
    private String decryptionKeystorePass;
    private boolean deleteUponRetrieval;
    private boolean decryptionMessage;

    public Map<String, String> execute(GetMailMessageInputs getMailMessageInputs) throws Exception {
        Map<String, String> result = new HashMap<>();
        try {
            processInputs(getMailMessageInputs);
            Message message = getMessage();

            if(decryptionMessage) {
                message = decryptMessage(message);
            }

            //delete message
            if (deleteUponRetrieval) {
                message.setFlag(Flags.Flag.DELETED, true);
            }
            if (subjectOnly) {
                String subject;
                if ((characterSet != null) && (characterSet.trim().length() > 0)) { //need to force the decode charset
                    subject = message.getHeader(SUBJECT)[0];
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
                        String subject = message.getHeader(SUBJECT)[0];
                        subject = changeHeaderCharset(subject, characterSet);
                        result.put(SUBJECT, MimeUtility.decodeText(subject));
                        String attachedFileNames = changeHeaderCharset(getAttachedFileNames(message), characterSet);
                        result.put(ATTACHED_FILE_NAMES_RESULT, decodeAttachedFileNames(attachedFileNames));
                    } else { //let everything as the sender intended it to be :)
                        String subject = message.getSubject();
                        if (subject == null)
                            subject = "";
                        result.put(SUBJECT, MimeUtility.decodeText(subject));
                        result.put(ATTACHED_FILE_NAMES_RESULT, decodeAttachedFileNames((getAttachedFileNames(message))));
                    }
                    // Get the message body
                    Map<String, String> messageByTypes = getMessageByContentTypes(message, characterSet);
                    String lastMessageBody = new LinkedList<>(messageByTypes.values()).getLast();

                    result.put(BODY_RESULT, MimeUtility.decodeText(lastMessageBody));

                    String plainTextBody = messageByTypes.containsKey(TEXT_PLAIN) ? messageByTypes.get(TEXT_PLAIN) : "";
                    result.put(PLAIN_TEXT_BODY_RESULT, MimeUtility.decodeText(plainTextBody));

                    StringOutputStream stream = new StringOutputStream();
                    message.writeTo(stream);
                    result.put(RETURN_RESULT, stream.toString().replaceAll("" + (char) 0, ""));
                } catch (UnsupportedEncodingException except) {
                    throw new UnsupportedEncodingException("The given encoding (" + characterSet + ") is invalid or not supported.");
                }
            }
            result.put(RETURN_CODE, SUCCESS_RETURN_CODE);
        } catch (Exception e) {
            if (e.toString().contains(UNRECOGNIZED_SSL_MESSAGE)) {
                throw new Exception(UNRECOGNIZED_SSL_MESSAGE_PLAINTEXT_CONNECTION);
            } else {
                throw e;
            }
        }
        return result;
    }

    protected Message getMessage() throws Exception {
        Store store = createMessageStore();
        Folder f = store.getFolder(folder);
        if (!f.exists()) {
            throw new Exception(THE_SPECIFIED_FOLDER_DOES_NOT_EXIST_ON_THE_REMOTE_SERVER);
        }
        f.open(getFolderOpenMode());
        if (messageNumber > f.getMessageCount())
            throw new IndexOutOfBoundsException("message value was: " + messageNumber + " there are only " + f.getMessageCount() + " messages in folder");
        return f.getMessage(messageNumber);
    }

    protected MimeMessage decryptMessage(Message message) throws Exception {
        MimeMessage pop_message = (MimeMessage)message;

        String cryptotype = EncryptionManager.checkEncryptionType(pop_message);
        EncryptionUtils cryptoUtils = EncryptionManager.getEncryptionUtils(cryptotype);

        char[] smimePw = new String(decryptionKeystorePass).toCharArray();
        EncryptionKeyManager keyMgr = cryptoUtils.createKeyManager();
        keyMgr.loadPrivateKeystore(new URL(decryptionKeystore).openStream(), smimePw);
        Key privateKey = keyMgr.getPrivateKey(decryptionKeyAlias, smimePw);

        MimeMessage decryptedMsg = null;
        try
        {
            decryptedMsg = cryptoUtils.decryptMessage(null, pop_message, privateKey);
        }
        catch (Exception e)
        {
            Object o = message.getContent();

            if(o != null) {
                String exceptionMessage = "";
                exceptionMessage = "msg.getContent() = " + o + ", a " + o.getClass().getName();
                exceptionMessage += "\nerror decrypting message with key " + privateKey + ":  " + e;
                exceptionMessage += "\n";
                throw new Exception(exceptionMessage, e);
            }
            throw e;
        }

        return decryptedMsg;
    }

    protected Store createMessageStore() throws Exception {
        Properties props = new Properties();
        Authenticator auth = new SimpleAuthenticator(username, password);
        Store store;
        if (!enableSSL) {
            store = configureStoreWithoutSSL(props, auth);
            store.connect();
        } else {
            addSSLSettings(trustAllRoots, keystore, keystorePassword, trustKeystoreFile, trustPassword);
            store = configureStoreWithSSL(props, auth);
            store.connect();
        }
        return store;
    }

    protected Store configureStoreWithSSL(Properties props, Authenticator auth) throws NoSuchProviderException {
        props.setProperty("mail." + protocol + ".socketFactory.class", SSL_FACTORY);
        props.setProperty("mail." + protocol + ".socketFactory.fallback", STR_FALSE);
        props.setProperty("mail." + protocol + ".port", port);
        props.setProperty("mail." + protocol + ".socketFactory.port", port);
        URLName url = new URLName(protocol, host, Integer.parseInt(port), "", username, password);
        Session session = Session.getInstance(props, auth);
        return session.getStore(url);
    }

    protected Store configureStoreWithoutSSL(Properties props, Authenticator auth) throws NoSuchProviderException {
        props.put("mail." + protocol + ".host", host);
        props.put("mail." + protocol + ".port", port);
        Session s = Session.getInstance(props, auth);
        return s.getStore(protocol);
    }

    protected void addSSLSettings(boolean trustAllRoots, String keystore,
                                  String keystorePassword, String trustKeystore, String trustPassword) throws Exception {
        boolean useClientCert = false;
        boolean useTrustCert = false;

        String separator = getSystemFileSeparator();
        String javaKeystore = getSystemJavaHome() + separator + "lib" + separator + "security" + separator + "cacerts";
        if (keystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            keystore = (storeExists) ? FILE + javaKeystore : null;
            if (null != keystorePassword) {
                if (keystorePassword.equals("")) {
                    keystorePassword = DEFAULT_PASSWORD_FOR_STORE;
                }
            }
            useClientCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!keystore.startsWith(HTTP))
                    keystore = FILE + keystore;
                useClientCert = true;
            }
        }
        if (trustKeystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            trustKeystore = (storeExists) ? FILE + javaKeystore : null;
            trustPassword = (storeExists) ? ((trustPassword.equals("")) ? DEFAULT_PASSWORD_FOR_STORE : trustPassword) : null;

            useTrustCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!trustKeystore.startsWith(HTTP))
                    trustKeystore = FILE + trustKeystore;
                useTrustCert = true;
            }
        }

        SSLContext context = SSLContext.getInstance(SSL);
        TrustManager[] trustManagers = null;
        KeyManager[] keyManagers = null;

        if (trustAllRoots) {
            trustManagers = new TrustManager[]{new EasyX509TrustManager()};
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

    protected String getSystemFileSeparator() {
        return System.getProperty("file.separator");
    }

    protected String getSystemJavaHome() {
        return System.getProperty("java.home");
    }

    protected void processInputs(GetMailMessageInputs getMailMessageInputs) throws Exception {

        String strHost = getMailMessageInputs.getHostname();
        if(null == strHost || strHost.equals("")) {
            throw new Exception(HOST_NOT_SPECIFIED);
        } else {
            host = strHost.trim();
        }
        port = getMailMessageInputs.getPort();
        protocol = getMailMessageInputs.getProtocol();
        String strUsername = getMailMessageInputs.getUsername();
        if(null == strUsername || strUsername.equals("")) {
            throw new Exception(USERNAME_NOT_SPECIFIED);
        } else {
            username = strUsername.trim();
        }
        String strPassword = getMailMessageInputs.getPassword();
        if(null == strPassword ) {
            password = "";
        } else {
            password = strPassword.trim();
        }
        String strFolder = getMailMessageInputs.getFolder();
        if(null == strFolder || strFolder.equals("")) {
            throw new Exception(FOLDER_NOT_SPECIFIED);
        } else {
            folder = strFolder.trim();
        }
        String trustAll = getMailMessageInputs.getTrustAllRoots();
        // Default value of trustAllRoots is true
        trustAllRoots = !(null != trustAll && trustAll.equalsIgnoreCase(STR_FALSE));
        String strMessageNumber = getMailMessageInputs.getMessageNumber();
        if(strMessageNumber == null || strMessageNumber.equals("")) {
            throw new Exception(MESSAGE_NUMBER_NOT_SPECIFIED);
        } else {
            messageNumber = Integer.parseInt(strMessageNumber);
        }
        String strSubOnly = getMailMessageInputs.getSubjectOnly();
        // Default value of subjectOnly is false
        subjectOnly = (strSubOnly != null && strSubOnly.equalsIgnoreCase(STR_TRUE));
        String strEnableSSL = getMailMessageInputs.getEnableSSL();
        // Default value of enableSSL is false;
        enableSSL = (null != strEnableSSL && strEnableSSL.equalsIgnoreCase(STR_TRUE));
        keystore = getMailMessageInputs.getKeystore();
        keystorePassword = getMailMessageInputs.getKeystorePassword();
        trustKeystoreFile = getMailMessageInputs.getTrustKeystore();
        trustPassword = getMailMessageInputs.getTrustPassword();
        characterSet = getMailMessageInputs.getCharacterSet();
        String strDeleteUponRetrieval = getMailMessageInputs.getDeleteUponRetrieval();
        // Default value for deleteUponRetrieval is false
        deleteUponRetrieval = (null != strDeleteUponRetrieval && strDeleteUponRetrieval.equalsIgnoreCase(STR_TRUE));

        if (messageNumber < 1) {
            throw new Exception(MESSAGES_ARE_NUMBERED_STARTING_AT_1);
        }
        if ((protocol == null || protocol.equals("")) && (port == null || port.equals(""))) {
            throw new Exception(PLEASE_SPECIFY_THE_PORT_THE_PROTOCOL_OR_BOTH);
        } else if ((protocol != null && !protocol.equals("")) && (!protocol.equalsIgnoreCase(IMAP)) && (!protocol.equalsIgnoreCase(POP3)) && (!protocol.equalsIgnoreCase(IMAP_4))
                && (port == null || port.equals(""))) {
            throw new Exception(PLEASE_SPECIFY_THE_PORT_FOR_THE_INDICATED_PROTOCOL);
        } else if ((protocol == null || protocol.equals("")) && (port != null && !port.equals(""))
                && (!port.equalsIgnoreCase(IMAP_PORT)) && (!port.equalsIgnoreCase(POP3_PORT))) {
            throw new Exception(PLEASE_SPECIFY_THE_PROTOCOL_FOR_THE_INDICATED_PORT);
        } else if ((protocol == null || protocol.equals("")) && (port.trim().equalsIgnoreCase(IMAP_PORT))) {
            protocol = IMAP;
        } else if ((protocol == null || protocol.equals("")) && (port.trim().equalsIgnoreCase(POP3_PORT))) {
            protocol = POP3;
        } else if ((protocol.trim().equalsIgnoreCase(POP3)) && (port == null || port.equals(""))) {
            port = POP3_PORT;
        } else if ((protocol.trim().equalsIgnoreCase(IMAP)) && (port == null || port.equals(""))) {
            port = IMAP_PORT;
        } else if ((protocol.trim().equalsIgnoreCase(IMAP_4)) && (port == null || port.equals(""))) {
            port = IMAP_PORT;
        }

        //The protocol should be given in lowercase to be recognised.
        protocol = protocol.toLowerCase();
        if (protocol.trim().equalsIgnoreCase(IMAP_4)) {
            protocol = IMAP;
        }

        this.decryptionKeystore = getMailMessageInputs.getDecryptionKeystore();
        if(this.decryptionKeystore != null && !this.decryptionKeystore.equals("")) {
            if(!decryptionKeystore.startsWith(HTTP)) {
                decryptionKeystore = FILE + decryptionKeystore;
            }

            decryptionMessage = true;
            decryptionKeyAlias = getMailMessageInputs.getDecryptionKeyAlias();
            if(null == decryptionKeyAlias) {
                decryptionKeyAlias = "";
            }
            decryptionKeystorePass = getMailMessageInputs.getDecryptionKeystorePassword();
            if(null == decryptionKeystorePass) {
                decryptionKeystorePass = "";
            }

        } else {
            decryptionMessage = false;
        }
    }

    protected Map<String, String> getMessageByContentTypes(Message message, String characterSet) throws Exception {

        Map<String, String> messageMap = new HashMap<>();

        Object obj = message.getContent();
        if(obj instanceof Multipart) {
            Multipart mpart = (Multipart) obj;

            for (int i = 0, n = mpart.getCount(); i < n; i++) {

                Part part = mpart.getBodyPart(i);
                String disposition = part.getDisposition();
                String partContentType = new String(part.getContentType().substring(0, part.getContentType().indexOf(";")));
                if (disposition == null) {
                    if (part.getContent() instanceof MimeMultipart) { // multipart with attachment
                        MimeMultipart mm = (MimeMultipart) part.getContent();
                        for (int j = 0; j < mm.getCount(); j++) {
                            if (mm.getBodyPart(j).getContent() instanceof String) {
                                BodyPart bodyPart = mm.getBodyPart(j);
                                if ((characterSet != null) && (characterSet.trim().length() > 0)) {
                                    String contentType = bodyPart.getHeader(CONTENT_TYPE)[0];
                                    contentType = contentType.replace(contentType.substring(contentType.indexOf("=") + 1), characterSet);
                                    bodyPart.setHeader(CONTENT_TYPE, contentType);
                                }
                                String partContentType1 = new String(bodyPart.getContentType().substring(0, bodyPart.getContentType().indexOf(";")));
                                messageMap.put(partContentType1, MimeUtility.decodeText(bodyPart.getContent().toString()));
                            }
                        }
                    } else {//multipart - w/o attachment
                        //if the user has specified a certain characterSet we decode his way
                        if ((characterSet != null) && (characterSet.trim().length() > 0)) {
                            InputStream istream = part.getInputStream();
                            ByteArrayInputStream bis = new ByteArrayInputStream(ASCIIUtility.getBytes(istream));
                            int count = bis.available();
                            byte[] bytes = new byte[count];
                            count = bis.read(bytes, 0, count);
                            messageMap.put(partContentType, MimeUtility.decodeText(new String(bytes, 0, count, characterSet)));
                        } else {
                            messageMap.put(partContentType, MimeUtility.decodeText(part.getContent().toString()));
                        }
                    }
                }
            }//for
        } else if (message.isMimeType(TEXT_PLAIN)) {
            messageMap.put(TEXT_PLAIN, MimeUtility.decodeText(obj.toString()));
        } else if (message.isMimeType(TEXT_HTML)) {
            messageMap.put(TEXT_HTML, MimeUtility.decodeText(convertMessage(obj.toString())));
        }

        return messageMap;
    }

    protected String getAttachedFileNames(Part part) throws Exception {
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
                    fileNames += STR_COMMA;
                // to the list of attachments built so far append the list of attachments in the current MIME part ...
                fileNames += getAttachedFileNames(mpart.getBodyPart(i));
            }
        }
        return fileNames;
    }

    protected String decodeAttachedFileNames(String attachedFileNames) throws Exception {
        StringBuilder sb = new StringBuilder();
        String delimiter = "";
        // splits the input into comma-separated chunks and decodes each chunk according to its encoding ...
        for (String fileName : attachedFileNames.split(STR_COMMA)) {
            sb.append(delimiter).append(MimeUtility.decodeText(fileName));
            delimiter = STR_COMMA;
        }
        // return the concatenation of the decoded chunks ...
        return sb.toString();
    }

    protected String convertMessage(String msg) throws Exception {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < msg.length(); i++) {
            char c = msg.charAt(i);
            if (c == '\n') {
                sb.append("<br>");
            } else {
                sb.append(c);
            }

        }
        return sb.toString();
    }

    protected int getFolderOpenMode() {
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