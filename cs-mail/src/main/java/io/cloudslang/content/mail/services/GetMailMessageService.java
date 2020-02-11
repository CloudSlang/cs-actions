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
package io.cloudslang.content.mail.services;

import com.sun.mail.util.ASCIIUtility;
import io.cloudslang.content.mail.entities.GetMailMessageInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.entities.StringOutputStream;
import io.cloudslang.content.mail.sslconfig.EasyX509TrustManager;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import org.bouncycastle.cms.PasswordRecipientId;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnveloped;

import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import static io.cloudslang.content.mail.utils.Constants.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.bouncycastle.mail.smime.SMIMEUtil.toMimeBodyPart;

public class GetMailMessageService {

    protected GetMailMessageInput input;
    private Store store;
    private RecipientId recId = null;
    private KeyStore ks = null;

    public Map<String, String> execute(GetMailMessageInput getMailMessageInput) throws Exception {
        Map<String, String> result = new HashMap<>();
        try {
            this.input = getMailMessageInput;
            Message message = getMessage();

            if (input.isEncryptedMessage()) {
                addDecryptionSettings();
            }

            //delete message
            if (input.isDeleteUponRetrieval()) {
                message.setFlag(Flags.Flag.DELETED, true);
            }
            if (input.isMarkMessageAsRead()) {
                message.setFlag(Flags.Flag.SEEN, true);
            }
            if (input.isSubjectOnly()) {
                String subject;
                // need to force the decode charset
                if ((input.getCharacterSet() != null) && (input.getCharacterSet().trim().length() > 0)) { 
                    subject = message.getHeader(SUBJECT_HEADER)[0];
                    subject = changeHeaderCharset(subject, input.getCharacterSet());
                    subject = MimeUtility.decodeText(subject);
                } else {
                    subject = message.getSubject();
                }
                if (subject == null) {
                    subject = Strings.EMPTY;
                }
                result.put(Outputs.SUBJECT, MimeUtility.decodeText(subject));
                result.put(Outputs.RETURN_RESULT, MimeUtility.decodeText(subject));
            } else {
                try {
                    // Get subject and attachedFileNames
                    if ((input.getCharacterSet() != null) && (input.getCharacterSet().trim().length() > 0)) {
                        //need to force the decode charset
                        String subject = message.getHeader(SUBJECT_HEADER)[0];
                        subject = changeHeaderCharset(subject, input.getCharacterSet());
                        result.put(Outputs.SUBJECT, MimeUtility.decodeText(subject));
                        String attachedFileNames = changeHeaderCharset(getAttachedFileNames(message), input.getCharacterSet());
                        result.put(Outputs.ATTACHED_FILE_NAMES_RESULT, decodeAttachedFileNames(attachedFileNames));
                    } else {
                        //let everything as the sender intended it to be :)
                        String subject = message.getSubject();
                        if (subject == null) {
                            subject = Strings.EMPTY;
                        }
                        result.put(Outputs.SUBJECT, MimeUtility.decodeText(subject));
                        result.put(Outputs.ATTACHED_FILE_NAMES_RESULT,
                                decodeAttachedFileNames((getAttachedFileNames(message))));
                    }
                    // Get the message body
                    Map<String, String> messageByTypes = getMessageByContentTypes(message, input.getCharacterSet());
                    String lastMessageBody = Strings.EMPTY;
                    if (!messageByTypes.isEmpty()) {
                        lastMessageBody = new LinkedList<>(messageByTypes.values()).getLast();
                    }
                    if (lastMessageBody == null) {
                        lastMessageBody = Strings.EMPTY;
                    }

                    result.put(Outputs.BODY_RESULT, MimeUtility.decodeText(lastMessageBody));

                    String plainTextBody = messageByTypes.containsKey(MimeTypes.TEXT_PLAIN) ? messageByTypes.get(MimeTypes.TEXT_PLAIN) : Strings.EMPTY;
                    result.put(Outputs.PLAIN_TEXT_BODY_RESULT, MimeUtility.decodeText(plainTextBody));

                    StringOutputStream stream = new StringOutputStream();
                    message.writeTo(stream);
                    result.put(Outputs.RETURN_RESULT, stream.toString().replaceAll(Strings.EMPTY + (char) 0, Strings.EMPTY));
                } catch (UnsupportedEncodingException except) {
                    throw new UnsupportedEncodingException("The given encoding (" + input.getCharacterSet() +
                            ") is invalid or not supported.");
                }
            }

            try {
                message.getFolder().close(true);
            } catch (Throwable ignore) {
            } finally {
                if (store != null)
                    store.close();
            }

            result.put(Outputs.RETURN_CODE, ReturnCodes.SUCCESS_RETURN_CODE);
        } catch (Exception e) {
            if (e.toString().contains(ExceptionMsgs.UNRECOGNIZED_SSL_MESSAGE)) {
                throw new Exception(ExceptionMsgs.UNRECOGNIZED_SSL_MESSAGE_PLAINTEXT_CONNECTION);
            } else {
                throw e;
            }
        }
        return result;
    }


    protected Message getMessage() throws Exception {
        store = createMessageStore();
        Folder folder = store.getFolder(input.getFolder());
        if (!folder.exists()) {
            throw new Exception(ExceptionMsgs.THE_SPECIFIED_FOLDER_DOES_NOT_EXIST_ON_THE_REMOTE_SERVER);
        }
        folder.open(getFolderOpenMode());
        if (input.getMessageNumber() > folder.getMessageCount()) {
            throw new IndexOutOfBoundsException("message value was: " + input.getMessageNumber() + " there are only " +
                    folder.getMessageCount() + ExceptionMsgs.COUNT_MESSAGES_IN_FOLDER_ERROR_MESSAGE);
        }
        return folder.getMessage(input.getMessageNumber());
    }

    protected Store createMessageStore() throws Exception {
        Properties props = new Properties();
        if (input.getTimeout() > 0) {
            props.put(Props.MAIL + input.getProtocol() + Props.TIMEOUT, input.getTimeout());
            setPropertiesProxy(props);
        }
        Authenticator auth = new SimpleAuthenticator(input.getUsername(), input.getPassword());
        Store store;
        if (input.isEnableTLS() || input.isEnableSSL()) {
            addSSLSettings(input.isTrustAllRoots(), input.getKeystore(), input.getKeystorePassword(),
                    input.getTrustKeystore(), input.getTrustPassword());
        }
        if (input.isEnableTLS()) {
            store = tryTLSOtherwiseTrySSL(props, auth);
        } else if (input.isEnableSSL()) {
            store = connectUsingSSL(props, auth);
        } else {
            store = configureStoreWithoutSSL(props, auth);
            store.connect();
        }

        return store;
    }

    private Store tryTLSOtherwiseTrySSL(Properties props, Authenticator auth) throws MessagingException {
        Store store = configureStoreWithTLS(props, auth);
        try {
            store.connect(input.getHostname(), input.getUsername(), input.getPassword());
        } catch (Exception e) {
            if (input.isEnableSSL()) {
                clearTLSProperties(props);
                store = connectUsingSSL(props, auth);
            } else {
                throw e;
            }
        }
        return store;
    }

    private Store connectUsingSSL(Properties props, Authenticator auth) throws MessagingException {
        Store store = configureStoreWithSSL(props, auth);
        store.connect();
        return store;
    }

    private void clearTLSProperties(Properties props) {
        props.remove(Props.MAIL + input.getProtocol() + Props.SSL_ENABLE);
        props.remove(Props.MAIL + input.getProtocol() + Props.START_TLS_ENABLE);
        props.remove(Props.MAIL + input.getProtocol() + Props.START_TLS_REQUIRED);
    }


    protected Store configureStoreWithSSL(Properties props, Authenticator auth) throws NoSuchProviderException {
        props.setProperty(Props.MAIL + input.getProtocol() + Props.SOCKET_FACTORY_CLASS, SSL_FACTORY);
        props.setProperty(Props.MAIL + input.getProtocol() + Props.SOCKET_FACTORY_FALLBACK, Strings.FALSE);
        props.setProperty(Props.MAIL + input.getProtocol() + Props.PORT, String.valueOf(input.getPort()));
        props.setProperty(Props.MAIL + input.getProtocol() + Props.SOCKET_FACTORY_PORT, String.valueOf(input.getPort()));
        URLName url = new URLName(input.getProtocol(), input.getHostname(), input.getPort(), Strings.EMPTY,
                input.getUsername(), input.getPassword());
        Session session = Session.getInstance(props, auth);
        return session.getStore(url);
    }

    protected Store configureStoreWithTLS(Properties props, Authenticator auth) throws NoSuchProviderException {
        props.setProperty(Props.MAIL + input.getProtocol() + Props.SSL_ENABLE, Strings.FALSE);
        props.setProperty(Props.MAIL + input.getProtocol() + Props.START_TLS_ENABLE, Strings.TRUE);
        props.setProperty(Props.MAIL + input.getProtocol() + Props.START_TLS_REQUIRED, Strings.TRUE);
        Session session = Session.getInstance(props, auth);
        return session.getStore(input.getProtocol() + SECURE_SUFFIX_FOR_POP3_AND_IMAP);
    }

    protected Store configureStoreWithoutSSL(Properties props, Authenticator auth) throws NoSuchProviderException {
        props.put(Props.MAIL + input.getProtocol() + Props.HOST, input.getHostname());
        props.put(Props.MAIL + input.getProtocol() + Props.PORT, input.getPort());
        Session session = Session.getInstance(props, auth);
        return session.getStore(input.getProtocol());
    }

    protected void addSSLSettings(boolean trustAllRoots, String keystore, String keystorePassword,
                                  String trustKeystore, String trustPassword) throws Exception {
        boolean useClientCert = false;
        boolean useTrustCert = false;

        String separator = getSystemFileSeparator();
        String javaKeystore = getSystemJavaHome() + separator + "lib" + separator + "security" + separator + "cacerts";
        if (keystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            keystore = (storeExists) ? FILE + javaKeystore : null;
            if (null != keystorePassword) {
                if (Strings.EMPTY.equals(keystorePassword)) {
                    keystorePassword = DEFAULT_PASSWORD_FOR_STORE;
                }
            }
            useClientCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!keystore.startsWith(HTTP)) {
                    keystore = FILE + keystore;
                }
                useClientCert = true;
            }
        }
        if (trustKeystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            trustKeystore = (storeExists) ? FILE + javaKeystore : null;
            if (storeExists) {
                if (isEmpty(trustPassword)) {
                    trustPassword = DEFAULT_PASSWORD_FOR_STORE;
                }
            } else {
                trustPassword = null;
            }

            useTrustCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!trustKeystore.startsWith(HTTP)) {
                    trustKeystore = FILE + trustKeystore;
                }
                useTrustCert = true;
            }
        }

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

        SSLContext context = SSLContext.getInstance(SSL);
        context.init(keyManagers, trustManagers, new SecureRandom());
        SSLContext.setDefault(context);
    }


    private void addDecryptionSettings() throws Exception {
        char[] smimePw = new String(input.getDecryptionKeystorePassword()).toCharArray();

        Security.addProvider(new BouncyCastleProvider());
        ks = KeyStore.getInstance(Encryption.PKCS_KEYSTORE_TYPE, Encryption.BOUNCY_CASTLE_PROVIDER);

        InputStream decryptionStream = new URL(input.getDecryptionKeystore()).openStream();
        try {
            ks.load(decryptionStream, smimePw);
        } finally {
            decryptionStream.close();
        }

        if (Strings.EMPTY.equals(input.getDecryptionKeyAlias())) {
            Enumeration aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = (String) aliases.nextElement();

                if (ks.isKeyEntry(alias)) {
                   input.setDecryptionKeyAlias(alias);
                }
            }

            if (Strings.EMPTY.equals(input.getDecryptionKeyAlias())) {
                throw new Exception(ExceptionMsgs.PRIVATE_KEY_ERROR_MESSAGE);
            }
        }

        //
        // find the certificate for the private key and generate a
        // suitable recipient identifier.
        //
        X509Certificate cert = (X509Certificate) ks.getCertificate(input.getDecryptionKeyAlias());
        if (null == cert) {
            throw new Exception("Can't find a key pair with alias \"" + input.getDecryptionKeyAlias() +
                    "\" in the given keystore");
        }
        if (input.isVerifyCertificate()) {
            cert.checkValidity();
        }

        recId = new PasswordRecipientId();
        recId.setSerialNumber(cert.getSerialNumber());
        recId.setIssuer(cert.getIssuerX500Principal().getEncoded());
    }


    protected String getSystemFileSeparator() {
        return System.getProperty(Props.FILE_SEPARATOR);
    }


    protected String getSystemJavaHome() {
        return System.getProperty(Props.JAVA_HOME);
    }


    protected Map<String, String> getMessageByContentTypes(Message message, String characterSet) throws Exception {

        Map<String, String> messageMap = new HashMap<>();

        if (message.isMimeType(MimeTypes.TEXT_PLAIN)) {
            messageMap.put(MimeTypes.TEXT_PLAIN, MimeUtility.decodeText(message.getContent().toString()));
        } else if (message.isMimeType(MimeTypes.TEXT_HTML)) {
            messageMap.put(MimeTypes.TEXT_HTML, MimeUtility.decodeText(convertMessage(message.getContent().toString())));
        } else if (message.isMimeType(MimeTypes.MULTIPART_MIXED) || message.isMimeType(MimeTypes.MULTIPART_RELATED)) {
            messageMap.put(MimeTypes.MULTIPART_MIXED, extractMultipartMixedMessage(message, characterSet));
        } else {
            Object obj = message.getContent();
            Multipart mpart = (Multipart) obj;

            for (int i = 0, n = mpart.getCount(); i < n; i++) {

                Part part = mpart.getBodyPart(i);

                if (input.isEncryptedMessage() && part.getContentType() != null &&
                        part.getContentType().equals(ENCRYPTED_CONTENT_TYPE)) {
                    part = decryptPart((MimeBodyPart) part);
                }

                String disposition = part.getDisposition();
                String partContentType = part.getContentType().substring(0, part.getContentType().indexOf(";"));
                if (disposition == null) {
                    if (part.getContent() instanceof MimeMultipart) {
                        // multipart with attachment
                        MimeMultipart mm = (MimeMultipart) part.getContent();
                        for (int j = 0; j < mm.getCount(); j++) {
                            if (mm.getBodyPart(j).getContent() instanceof String) {
                                BodyPart bodyPart = mm.getBodyPart(j);
                                if ((characterSet != null) && (characterSet.trim().length() > 0)) {
                                    String contentType = bodyPart.getHeader(CONTENT_TYPE)[0];
                                    contentType = contentType
                                            .replace(contentType.substring(contentType.indexOf("=") + 1), characterSet);
                                    bodyPart.setHeader(CONTENT_TYPE, contentType);
                                }
                                String partContentType1 = bodyPart
                                        .getContentType().substring(0, bodyPart.getContentType().indexOf(";"));
                                messageMap.put(partContentType1,
                                        MimeUtility.decodeText(bodyPart.getContent().toString()));
                            }
                        }
                    } else {
                        //multipart - w/o attachment
                        //if the user has specified a certain characterSet we decode his way
                        if ((characterSet != null) && (characterSet.trim().length() > 0)) {
                            InputStream istream = part.getInputStream();
                            ByteArrayInputStream bis = new ByteArrayInputStream(ASCIIUtility.getBytes(istream));
                            int count = bis.available();
                            byte[] bytes = new byte[count];
                            count = bis.read(bytes, 0, count);
                            messageMap.put(partContentType,
                                    MimeUtility.decodeText(new String(bytes, 0, count, characterSet)));
                        } else {
                            messageMap.put(partContentType, MimeUtility.decodeText(part.getContent().toString()));
                        }
                    }
                }
            } //for
        } //else

        return messageMap;
    }


    private String extractMultipartMixedMessage(Message message, String characterSet) throws Exception {

        Object obj = message.getContent();
        Multipart mpart = (Multipart) obj;

        for (int i = 0, n = mpart.getCount(); i < n; i++) {

            Part part = mpart.getBodyPart(i);
            if (input.isEncryptedMessage() && part.getContentType() != null &&
                    part.getContentType().equals(ENCRYPTED_CONTENT_TYPE)) {
                part = decryptPart((MimeBodyPart) part);
            }
            String disposition = part.getDisposition();

            if (disposition != null) {
                // this means the part is not an inline image or attached file.
                continue;
            }

            if (part.isMimeType(MimeTypes.MULTIPART_RELATED)) {
                // if related content then check it's parts

                String content = processMultipart(part);

                if (content != null) {
                    return content;
                }

            }

            if (part.isMimeType(MimeTypes.MULTIPART_ALTERNATIVE)) {
                return extractAlternativeContent(part);
            }

            if (part.isMimeType(MimeTypes.TEXT_PLAIN) || part.isMimeType(MimeTypes.TEXT_HTML)) {
                return part.getContent().toString();
            }

        }

        return null;
    }


    private String processMultipart(Part part) throws IOException,
            MessagingException {
        Multipart relatedparts = (Multipart) part.getContent();

        for (int j = 0; j < relatedparts.getCount(); j++) {

            Part rel = relatedparts.getBodyPart(j);

            if (rel.getDisposition() == null) {
                // again, if it's not an image or attachment(only those have disposition not null)

                if (rel.isMimeType(MimeTypes.MULTIPART_ALTERNATIVE)) {
                    // last crawl through the alternative formats.
                    return extractAlternativeContent(rel);
                }
            }
        }

        return null;
    }


    private String extractAlternativeContent(Part part) throws IOException, MessagingException {
        Multipart alternatives = (Multipart) part.getContent();

        Object content = Strings.EMPTY;

        for (int k = 0; k < alternatives.getCount(); k++) {
            Part alternative = alternatives.getBodyPart(k);
            if (alternative.getDisposition() == null) {
                content = alternative.getContent();
            }
        }

        return content.toString();
    }


    private MimeBodyPart decryptPart(MimeBodyPart part) throws Exception {

        SMIMEEnveloped smimeEnveloped = new SMIMEEnveloped(part);
        RecipientInformationStore recipients = smimeEnveloped.getRecipientInfos();
        RecipientInformation recipient = recipients.get(recId);

        if (null == recipient) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("This email wasn't encrypted with \"" + recId.toString() + "\".\n");
            errorMessage.append(ENCRYPT_RECID);

            for (Object rec : recipients.getRecipients()) {
                if (rec instanceof RecipientInformation) {
                    RecipientId recipientId = ((RecipientInformation) rec).getRID();
                    errorMessage.append("\"" + recipientId.toString() + "\"\n");
                }
            }
            throw new Exception(errorMessage.toString());
        }

        return toMimeBodyPart(recipient.getContent(
                ks.getKey(input.getDecryptionKeyAlias(), null),
                BOUNCY_CASTLE_PROVIDER));
    }


    protected String getAttachedFileNames(Part part) throws Exception {
        String fileNames = Strings.EMPTY;
        Object content = part.getContent();
        if (!(content instanceof Multipart)) {
            if (input.isEncryptedMessage() && part.getContentType() != null &&
                    part.getContentType().equals(ENCRYPTED_CONTENT_TYPE)) {
                part = decryptPart((MimeBodyPart) part);
            }
            // non-Multipart MIME part ...
            // is the file name set for this MIME part? (i.e. is it an attachment?)
            if (part.getFileName() != null && !part.getFileName().equals(Strings.EMPTY) && part.getInputStream() != null) {
                String fileName = part.getFileName();
                // is the file name encoded? (consider it is if it's in the =?charset?encoding?encoded text?= format)
                if (fileName.indexOf('?') == -1) {
                    // not encoded  (i.e. a simple file name not containing '?')-> just return the file name
                    return fileName;
                }
                // encoded file name -> remove any chars before the first "=?" and after the last "?="
                return fileName.substring(fileName.indexOf("=?"), fileName.length() -
                        ((new StringBuilder(fileName)).reverse()).indexOf("=?"));
            }
        } else {
            // a Multipart type of MIME part
            Multipart mpart = (Multipart) content;
            // iterate through all the parts in this Multipart ...
            for (int i = 0, n = mpart.getCount(); i < n; i++) {
                if (!Strings.EMPTY.equals(fileNames)) {
                    fileNames += Strings.COMMA;
                }
                // to the list of attachments built so far append the list of attachments in the current MIME part ...
                fileNames += getAttachedFileNames(mpart.getBodyPart(i));
            }
        }
        return fileNames;
    }


    protected String decodeAttachedFileNames(String attachedFileNames) throws Exception {
        StringBuilder sb = new StringBuilder();
        String delimiter = Strings.EMPTY;
        // splits the input into comma-separated chunks and decodes each chunk according to its encoding ...
        for (String fileName : attachedFileNames.split(Strings.COMMA)) {
            sb.append(delimiter).append(MimeUtility.decodeText(fileName));
            delimiter = Strings.COMMA;
        }
        // return the concatenation of the decoded chunks ...
        return sb.toString();
    }


    protected String convertMessage(String msg) throws Exception {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < msg.length(); i++) {
            char currentChar = msg.charAt(i);
            if (currentChar == '\n') {
                sb.append("<br>");
            } else {
                sb.append(currentChar);
            }

        }
        return sb.toString();
    }

    protected int getFolderOpenMode() {
        return Folder.READ_WRITE;
    }

    /**
     * This method addresses the mail headers which contain encoded words. The syntax for an encoded word is defined in
     * RFC 2047 section 2: http://www.faqs.org/rfcs/rfc2047.html In some cases the header is marked as having a certain
     * charset but at decode not all the characters a properly decoded. This is why it can be useful to force it to
     * decode the text with a different charset.
     * For example when sending an email using Mozilla Thunderbird and JIS X 0213 characters the subject and attachment
     * headers are marked as =?Shift_JIS? but the JIS X 0213 characters are only supported in windows-31j.
     * <p/>
     * This method replaces the charset tag of the header with the new charset provided by the user.
     *
     * @param header     - The header in which the charset will be replaced.
     * @param newCharset - The new charset that will be replaced in the given header.
     * @return The header with the new charset.
     */
    public String changeHeaderCharset(String header, String newCharset) {
        //match for =?charset?
        return header.replaceAll("=\\?[^\\(\\)<>@,;:/\\[\\]\\?\\.= ]+\\?", "=?" + newCharset + "?");
    }

    protected void setPropertiesProxy(Properties prop) {
        if (input.getProtocol().contains(IMAPProps.IMAP)) {
            prop.setProperty(IMAPProps.PROXY_HOST, input.getProxyHost());
            prop.setProperty(IMAPProps.PROXY_PORT, input.getProxyPort());
            prop.setProperty(IMAPProps.PROXY_USER, input.getProxyUsername());
            prop.setProperty(IMAPProps.PROXY_PASSWORD, input.getProxyPassword());

        }
        if (input.getProtocol().contains(POPProps.POP)) {
            prop.setProperty(POPProps.PROXY_HOST, input.getProxyHost());
            prop.setProperty(POPProps.PROXY_PORT, input.getProxyPort());
            prop.setProperty(POPProps.PROXY_USER, input.getProxyUsername());
            prop.setProperty(POPProps.PROXY_PASSWORD, input.getProxyPassword());
        }
    }
}