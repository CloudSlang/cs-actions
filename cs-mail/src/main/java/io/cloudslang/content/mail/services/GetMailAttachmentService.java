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
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.entities.SimpleAuthenticator;
import io.cloudslang.content.mail.sslconfig.EasyX509TrustManager;
import io.cloudslang.content.mail.sslconfig.SSLUtils;
import io.cloudslang.content.mail.utils.Constants;
import io.cloudslang.content.mail.utils.Constants.*;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEUtil;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.*;

public class GetMailAttachmentService {

    public static final String SECURE_SUFFIX_FOR_POP3_AND_IMAP = "s";
    public static final String ENCRYPTED_CONTENT_TYPE = "application/pkcs7-mime; name=\"smime.p7m\"; smime-type=enveloped-data";

    private static final String SSLSOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

    protected GetMailAttachmentInput input;

    private Map<String, String> results = new HashMap<>();
    private RecipientId recId = null;
    private KeyStore ks = null;

    public Map<String, String> execute(GetMailAttachmentInput getMailAttachmentInput) {
        this.results = new HashMap<>();
        this.input = getMailAttachmentInput;

        try (Store store = createMessageStore()) {
            Folder folder = store.getFolder(input.getFolder());
            if (!folder.exists()) {
                throw new Exception(ExceptionMsgs.THE_SPECIFIED_FOLDER_DOES_NOT_EXIST_ON_THE_REMOTE_SERVER);
            }
            folder.open(Folder.READ_ONLY);

            if (input.getMessageNumber() > folder.getMessageCount()) {
                throw new IndexOutOfBoundsException("Message value was: " + input.getMessageNumber() + " there are only " +
                        folder.getMessageCount() + " messages in folder");
            }
            Message message = folder.getMessage(input.getMessageNumber());

            if (input.isEncryptedMessage()) {
                addDecryptionSettings();
            }

            try {
                if (StringUtils.isEmpty(input.getDestination())) {
                    readAttachment(message, input.getAttachmentName(), input.getCharacterSet());
                } else
                    downloadAttachment(message, input.getAttachmentName(), input.getCharacterSet(),
                            input.getDestination(), input.isOverwrite());

            } catch (UnsupportedEncodingException except) {
                throw new UnsupportedEncodingException("The given encoding (" + input.getCharacterSet() + ") is invalid or not supported.");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            results.put(Outputs.EXCEPTION, e.toString());
            String resultMessage = e.getMessage();
            if (e.toString().contains("Unrecognized SSL message")) {
                resultMessage = "Unrecognized SSL message, plaintext connection?";
            }
            results.put(Outputs.RETURN_RESULT, resultMessage);
            results.put(Outputs.RETURN_CODE, ReturnCodes.FAILURE_RETURN_CODE);
        }

        return results;
    }


    protected Store createMessageStore() throws Exception {
        Properties props = new Properties();
        Authenticator auth = new SimpleAuthenticator(input.getUsername(), input.getPassword());
        Store store;
        if (input.isEnableTLS() || input.isEnableSSL()) {
            addSSLSettings(input.getPort(), input.isTrustAllRoots(), input.getKeystore(), input.getKeystorePassword(),
                    Strings.EMPTY, Strings.EMPTY);
        }
        if (input.isEnableTLS()) {
            store = tryTLSOtherwiseTrySSL(auth, props);
        } else if (input.isEnableSSL()) {
            store = connectUsingSSL(props, auth);
        } else {
            props.put(Props.MAIL + input.getProtocol() + Props.HOST, input.getHostname());
            props.put(Props.MAIL + input.getProtocol() + Props.PORT, input.getPort());
            Session s = Session.getInstance(props, auth);
            store = s.getStore(input.getProtocol());
            store.connect();
        }
        return store;
    }


    private Store connectUsingSSL(Properties props, Authenticator auth) throws MessagingException {
        Store store = configureStoreWithSSL(props, auth);
        store.connect();
        return store;
    }


    protected Store configureStoreWithSSL(Properties props, Authenticator auth) throws NoSuchProviderException {
        props.setProperty(Props.MAIL + input.getProtocol() + Props.SOCKET_FACTORY_CLASS, SSLSOCKET_FACTORY);
        props.setProperty(Props.MAIL + input.getProtocol() + Props.SOCKET_FACTORY_FALLBACK, Strings.FALSE);
        props.setProperty(Props.MAIL + input.getProtocol() + Props.PORT, String.valueOf(input.getPort()));
        props.setProperty(Props.MAIL + input.getProtocol() + Props.SOCKET_FACTORY_PORT, String.valueOf(input.getPort()));
        URLName url = new URLName(input.getProtocol(), input.getHostname(), input.getPort(), Strings.EMPTY,
                input.getUsername(), input.getPassword());
        Session session = Session.getInstance(props, auth);
        return session.getStore(url);
    }


    public void addSSLSettings(int port, boolean trustAllRoots, String keystore,
                               String keystorePassword, String trustKeystore, String trustPassword) throws Exception {
        boolean useClientCert = false;
        boolean useTrustCert = false;

        String separator = System.getProperty(Props.FILE_SEPARATOR);
        String javaKeystore = System.getProperty(Props.JAVA_HOME) + separator + "lib" + separator + "security" + separator + "cacerts";
        if (keystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            keystore = (storeExists) ? Constants.FILE + javaKeystore : null;
            keystorePassword = (storeExists) ? ((keystorePassword.equals(Strings.EMPTY)) ? "changeit" : keystorePassword) : null;

            useClientCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!keystore.startsWith(Constants.HTTP))
                    keystore = Constants.FILE + keystore;
                useClientCert = true;
            }
        }
        if (trustKeystore.length() == 0 && !trustAllRoots) {
            boolean storeExists = new File(javaKeystore).exists();
            trustKeystore = (storeExists) ? Constants.FILE + javaKeystore : null;
            trustPassword = (storeExists) ? ((trustPassword.equals(Strings.EMPTY)) ? "changeit" : trustPassword) : null;

            useTrustCert = storeExists;
        } else {
            if (!trustAllRoots) {
                if (!trustKeystore.startsWith(Constants.HTTP))
                    trustKeystore = Constants.FILE + trustKeystore;
                useTrustCert = true;
            }

        }

        SSLContext context = SSLContext.getInstance("SSL");
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


    private Store tryTLSOtherwiseTrySSL(Authenticator auth, Properties props) throws MessagingException {
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


    protected Store configureStoreWithTLS(Properties props, Authenticator auth) throws NoSuchProviderException {
        props.setProperty(Props.MAIL + input.getProtocol() + Props.SSL_ENABLE, Strings.FALSE);
        props.setProperty(Props.MAIL + input.getProtocol() + Props.START_TLS_ENABLE, Strings.TRUE);
        props.setProperty(Props.MAIL + input.getProtocol() + Props.START_TLS_REQUIRED, Strings.TRUE);
        Session session = Session.getInstance(props, auth);
        return session.getStore(input.getProtocol() + SECURE_SUFFIX_FOR_POP3_AND_IMAP);
    }


    private void clearTLSProperties(Properties props) {
        props.remove(Props.MAIL + input.getProtocol() + Props.SSL_ENABLE);
        props.remove(Props.MAIL + input.getProtocol() + Props.START_TLS_ENABLE);
        props.remove(Props.MAIL + input.getProtocol() + Props.START_TLS_REQUIRED);
    }


    private void addDecryptionSettings() throws Exception {
        char[] smimePw = input.getDecryptionKeystorePassword().toCharArray();

        Security.addProvider(new BouncyCastleProvider());
        ks = KeyStore.getInstance(Encryption.PKCS_KEYSTORE_TYPE, Encryption.BOUNCY_CASTLE_PROVIDER);

        InputStream decryptionStream = new URL(input.getDecryptionKeystore()).openStream();
        try {
            ks.load(decryptionStream, smimePw);
        } finally {
            decryptionStream.close();
        }

        if (input.getDecryptionKeyAlias().equals(Strings.EMPTY)) {
            Enumeration e = ks.aliases();
            while (e.hasMoreElements()) {
                String alias = (String) e.nextElement();

                if (ks.isKeyEntry(alias)) {
                    input.setDecryptionKeyAlias(alias);
                }
            }

            if (input.getDecryptionKeyAlias().equals(Strings.EMPTY)) {
                throw new Exception(ExceptionMsgs.PRIVATE_KEY_ERROR_MESSAGE);
            }
        }

        // find the certificate for the private key and generate a
        // suitable recipient identifier.
        X509Certificate cert = (X509Certificate) ks.getCertificate(input.getDecryptionKeyAlias());
        if (null == cert) {
            throw new Exception("Can't find a key pair with alias \"" + input.getDecryptionKeyAlias() + "\" in the given keystore");
        }

        recId = new KeyTransRecipientId(cert.getIssuerX500Principal().getEncoded());
        recId.setSerialNumber(cert.getSerialNumber());
        recId.setIssuer(cert.getIssuerX500Principal().getEncoded());
    }


    private String readAttachment(Message message, String attachment, String characterSet) throws Exception {
        if (!message.isMimeType(MimeTypes.TEXT_PLAIN) && !message.isMimeType(MimeTypes.TEXT_HTML)) {

            Multipart mpart = (Multipart) message.getContent();

            if (mpart != null) {
                for (int i = 0, n = mpart.getCount(); i < n; i++) {
                    Part part = mpart.getBodyPart(i);

                    if (input.isEncryptedMessage() && part.getContentType() != null && part.getContentType().equals(ENCRYPTED_CONTENT_TYPE)) {
                        part = decryptPart((MimeBodyPart) part);
                    }

                    if (part != null && part.getFileName() != null
                            && (MimeUtility.decodeText(part.getFileName()).equalsIgnoreCase(attachment) || (part.getFileName().equalsIgnoreCase(attachment)))) {
                        String disposition = part.getDisposition();
                        if (disposition != null && disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                            String partPrefix = part.getContentType().toLowerCase();
                            if (partPrefix.startsWith(MimeTypes.TEXT_PLAIN)) {
                                String attachmentContent = attachmentToString(part, characterSet);
                                results.put(Outputs.RETURN_RESULT, MimeUtility.decodeText(attachmentContent));
                                results.put(Outputs.TEMPORARY_FILE, writeTextToTempFile(attachmentContent));
                            } else
                                results.put(Outputs.TEMPORARY_FILE, writeToTempFile(part));
                        }
                        return MimeUtility.decodeText(part.getFileName());
                    }
                }
            }
        }
        throw new Exception("The email does not have an attached file by the name " + attachment + ".");
    }


    private MimeBodyPart decryptPart(MimeBodyPart part) throws Exception {

        SMIMEEnveloped m = new SMIMEEnveloped(part);

        RecipientInformationStore recipients = m.getRecipientInfos();
        RecipientInformation recipient = null;

        Collection recipientsColection = recipients.getRecipients();
        Iterator recipientsIterator = recipientsColection.iterator();
        while (recipientsIterator.hasNext()) {
            Object nextObj = recipientsIterator.next();
            if (nextObj instanceof RecipientInformation) {
                RecipientInformation testRecipient = (RecipientInformation) nextObj;

                RecipientId recipientId = testRecipient.getRID();

                if (recipientId.getSerialNumber().equals(recId.getSerialNumber()) &&
                        recipientId.getIssuerAsString().equals(recId.getIssuerAsString())) {
                    recipient = testRecipient;
                    break;
                }
            }
        }

        if (null == recipient) {
            StringBuilder errorMessage = new StringBuilder();
            errorMessage.append("This email wasn't encrypted with \"" + recId.toString() + "\".\n");
            errorMessage.append("The encryption recId is: ");

            for (Object rec : recipients.getRecipients()) {
                if (rec instanceof RecipientInformation) {
                    RecipientId recipientId = ((RecipientInformation) rec).getRID();
                    errorMessage.append("\"" + recipientId.toString() + "\"\n");
                }
            }
            throw new Exception(errorMessage.toString());
        }

        return SMIMEUtil.toMimeBodyPart(recipient.getContent(
                ks.getKey(input.getDecryptionKeyAlias(), null),
                Encryption.BOUNCY_CASTLE_PROVIDER)
        );
    }


    private static String attachmentToString(Part part, String characterSet) throws IOException, MessagingException {
        String content;
        if ((characterSet != null) && (characterSet.trim().length() > 0)) {
            InputStream istream = part.getInputStream();
            ByteArrayInputStream bis = new ByteArrayInputStream(ASCIIUtility.getBytes(istream));
            int count = bis.available();
            byte[] bytes = new byte[count];
            count = bis.read(bytes, 0, count);
            content = new String(bytes, 0, count, characterSet);
        } else
            content = (String) part.getContent().toString();

        return content;
    }


    private static String writeTextToTempFile(String text) throws IOException {
        File f = File.createTempFile("attachment", null);
        FileWriter fw = null;

        try {
            fw = new FileWriter(f);
            fw.write(text);
        } finally {
            if (fw != null) {
                fw.close();
            }
        }

        return f.getCanonicalPath();
    }


    private static String writeToTempFile(Part part) throws IOException, MessagingException {
        InputStream is = part.getInputStream();
        File f = File.createTempFile("attachment", null);

        OutputStream os = null;
        try {
            os = new FileOutputStream(f);
            int readChar;
            while ((readChar = is.read()) != -1) {
                os.write(readChar);
            }
        } finally {
            if (os != null) {
                os.close();
            }
        }

        return f.getCanonicalPath();
    }


    private String downloadAttachment(Message message, String attachment, String characterSet, String path, boolean overwrite) throws Exception {
        if (!message.isMimeType(MimeTypes.TEXT_PLAIN) && !message.isMimeType(MimeTypes.TEXT_HTML)) {

            Multipart mpart = (Multipart) message.getContent();
            File f = new File(path);
            if (f.isDirectory()) {
                if (mpart != null) {
                    for (int i = 0, n = mpart.getCount(); i < n; i++) {
                        Part part = mpart.getBodyPart(i);

                        if (input.isEncryptedMessage() && part.getContentType() != null &&
                                part.getContentType().equals(ENCRYPTED_CONTENT_TYPE)) {
                            part = decryptPart((MimeBodyPart) part);
                        }

                        if (part != null && part.getFileName() != null &&
                                (MimeUtility.decodeText(part.getFileName()).equalsIgnoreCase(attachment) ||
                                        (part.getFileName().equalsIgnoreCase(attachment)))) {
                            String disposition = part.getDisposition();
                            if (disposition != null && disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                                String partPrefix = part.getContentType().toLowerCase();
                                if (partPrefix.startsWith(MimeTypes.TEXT_PLAIN)) {
                                    String attachmentContent = attachmentToString(part, characterSet);
                                    writeTextToNewFile(path + "/" + attachment, attachmentContent, overwrite);
                                    results.put(Outputs.RETURN_RESULT, MimeUtility.decodeText(attachmentContent));
                                } else {
                                    writeNewFile(path + "/" + attachment, part, overwrite);
                                }
                            }
                            return MimeUtility.decodeText(part.getFileName());
                        }
                    }
                }
            } else throw new Exception("The Folder " + path + " does not exist.");
        }
        throw new Exception("The email does not have an attached file by the name " + attachment + ".");
    }


    private static String writeNewFile(String path, Part part, boolean overwrite) throws Exception {

        InputStream is = null;
        OutputStream os = null;

        try {
            is = part.getInputStream();

            File f = new File(path);
            if (!f.exists() || overwrite) {
                if (!f.createNewFile()) {
                    throw new IOException("Could not create file at path: " + path);
                }

                os = new FileOutputStream(f);

                int readChar;
                while ((readChar = is.read()) != -1) {
                    os.write(readChar);
                }
                return f.getCanonicalPath();
            } else {//return writeToTempFile(part);
                throw new Exception("The file " + path + " already exists.");
            }
        } finally {
            if (is != null)
                is.close();

            if (os != null)
                os.close();
        }
    }


    private static String writeTextToNewFile(String path, String text, boolean overwrite) throws Exception {
        File f = new File(path);
        FileWriter fw = null;

        try {
            if (!f.exists() || overwrite) {
                if (!f.createNewFile()) {
                    throw new IOException("Could not create file at path: " + path);
                }
                fw = new FileWriter(f);
                fw.write(text);
                return f.getCanonicalPath();
            }
            throw new Exception("The file " + path + " already exists. ");
        } finally {
            if (fw != null)
                fw.close();
        }
    }
}
