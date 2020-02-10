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

import com.sun.mail.smtp.SMTPMessage;
import io.cloudslang.content.mail.entities.SendMailInput;
import io.cloudslang.content.mail.utils.Constants;
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
import javax.mail.internet.*;
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static io.cloudslang.content.mail.utils.Constants.*;
import static com.sun.mail.smtp.SMTPMessage.NOTIFY_DELAY;
import static com.sun.mail.smtp.SMTPMessage.NOTIFY_FAILURE;
import static com.sun.mail.smtp.SMTPMessage.NOTIFY_SUCCESS;

/**
 * Created by giloan on 10/30/2014.
 */
public class SendMailService {

    //Operation inputs
    private SMIMEEnvelopedGenerator gen;
    protected SendMailInput input;

    public Map<String, String> execute(SendMailInput sendMailInput) throws Exception {
        Map<String, String> result = new HashMap<>();
        Transport transport = null;
        try {
            this.input = sendMailInput;
            // Create a mail session
            java.util.Properties props = new java.util.Properties();
            props.put(SMTPProps.HOST, input.getHostname());
            props.put(SMTPProps.PORT, Strings.EMPTY + input.getPort());

            if (null != input.getUser() && input.getUser().length() > 0) {
                props.put(SMTPProps.USER, input.getUser());
                props.put(SMTPProps.PASSWORD, input.getPassword());
                props.put(SMTPProps.AUTH, Strings.TRUE);
            }
            if (!input.isEnableTLS()) {
                props.put(SMTPProps.START_TLS_ENABLE, Strings.FALSE);
            }
            if (input.getTimeout() > 0) {
                props.put(SMTPProps.TIMEOUT, input.getTimeout());
            }

            //construct encryption SMIMEEnvelopedGenerator
            if (input.isEncryptedMessage()) {
                addEncryptionSettings();
            }

            // Construct the message
            MimeMultipart multipart = new MimeMultipart();

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            if (input.isHtmlEmail()) {
                processHTMLBodyWithBASE64Images(multipart);
                mimeBodyPart.setContent(input.getBody(), MimeTypes.TEXT_HTML + ";charset=" + input.getCharacterSet());
            } else {
                mimeBodyPart.setContent(input.getBody(), MimeTypes.TEXT_PLAIN + ";charset=" + input.getCharacterSet());
            }
            mimeBodyPart.setHeader(Encodings.CONTENT_TRANSFER_ENCODING, input.getContentTransferEncoding());
            mimeBodyPart = encryptMimeBodyPart(mimeBodyPart);

            multipart.addBodyPart(mimeBodyPart);

            if (null != input.getAttachments() && input.getAttachments().length() > 0) {
                for (String attachment : input.getAttachments().split(Pattern.quote(input.getDelimiter()))) {
                    FileDataSource source = new FileDataSource(attachment);
                    if (!source.getFile().exists()) {
                        throw new FileNotFoundException("Cannot attach " + attachment);
                    }

                    if (!Files.isReadable(source.getFile().toPath())) {
                        throw new InvalidParameterException(attachment + " don't have read permision");
                    }

                    MimeBodyPart messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setHeader(Encodings.CONTENT_TRANSFER_ENCODING, input.getContentTransferEncoding());
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(MimeUtility.encodeText(
                            attachment.substring(attachment.lastIndexOf(java.io.File.separator) + 1),
                            input.getCharacterSet(),
                            input.getEncodingScheme())
                    );
                    messageBodyPart = encryptMimeBodyPart(messageBodyPart);
                    multipart.addBodyPart(messageBodyPart);
                }
            }

            Session session = Session.getInstance(props, null);
            SMTPMessage msg = new SMTPMessage(session);
            msg.setContent(multipart);
            msg.setFrom(new InternetAddress(input.getFrom()));
            msg.setSubject(MimeUtility.encodeText(
                    input.getSubject().replaceAll("[\\n\\r]", " "),
                    input.getCharacterSet(),
                    input.getEncodingScheme())
            );

            if (input.isReadReceipt()) {
                msg.setNotifyOptions(NOTIFY_DELAY + NOTIFY_FAILURE + NOTIFY_SUCCESS);
            }

            String[] recipients = input.getTo().split(Pattern.quote(input.getDelimiter()));
            InternetAddress[] toRecipients = new InternetAddress[recipients.length];
            for (int count = 0; count < recipients.length; count++) {
                toRecipients[count] = new InternetAddress(recipients[count]);
            }
            msg.setRecipients(Message.RecipientType.TO, toRecipients);

            if (input.getCc() != null && input.getCc().trim().length() > 0) {
                recipients = input.getCc().split(Pattern.quote(input.getDelimiter()));
                if (recipients.length > 0) {
                    InternetAddress[] ccRecipients = new InternetAddress[recipients.length];
                    for (int count = 0; count < recipients.length; count++) {
                        ccRecipients[count] = new InternetAddress(recipients[count]);
                    }
                    msg.setRecipients(Message.RecipientType.CC, ccRecipients);
                }
            }

            if (input.getBcc() != null && input.getBcc().trim().length() > 0) {
                recipients = input.getBcc().split(Pattern.quote(input.getDelimiter()));
                if (recipients.length > 0) {
                    InternetAddress[] bccRecipients = new InternetAddress[recipients.length];
                    for (int count = 0; count < recipients.length; count++) {
                        bccRecipients[count] = new InternetAddress(recipients[count]);
                    }
                    msg.setRecipients(Message.RecipientType.BCC, bccRecipients);
                }
            }

            if (input.getHeaderNames() != null && !input.getHeaderNames().isEmpty()) {
                msg = addHeadersToSMTPMessage(msg, input.getHeaderNames(), input.getHeaderValues());
            }

            msg.saveChanges();

            if(!StringUtils.isEmpty(input.getUser())) {
                transport = session.getTransport(SMTPProps.SMTP);
                transport.connect(input.getHostname(), input.getPort(), input.getUser(), input.getPassword());
                transport.sendMessage(msg, msg.getAllRecipients());
            } else {
                Transport.send(msg);
            }
            result.put(Constants.Outputs.RETURN_RESULT, Constants.MAIL_WAS_SENT);
            result.put(Constants.Outputs.RETURN_CODE, Constants.ReturnCodes.SUCCESS_RETURN_CODE);
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
        return result;
    }

    private void processHTMLBodyWithBASE64Images(MimeMultipart multipart) throws ParserException,
            MessagingException, NoSuchAlgorithmException, SMIMEException, java.security.NoSuchProviderException {
        if (null != input.getBody() && input.getBody().contains(Encodings.BASE64)) {
            Parser parser = new Parser(input.getBody());
            NodeList nodeList = parser.parse(null);
            HtmlImageNodeVisitor htmlImageNodeVisitor = new HtmlImageNodeVisitor();
            nodeList.visitAllNodesWith(htmlImageNodeVisitor);
            input.setBody(nodeList.toHtml());

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
        imagePart.setHeader(Encodings.CONTENT_TRANSFER_ENCODING, Encodings.BASE64);
        imagePart.setDataHandler(new DataHandler(Base64.decode(base64ImagesMap.get(contentId)), MimeTypes.IMAGE_PNG));
        return imagePart;
    }

    private MimeBodyPart encryptMimeBodyPart(MimeBodyPart mimeBodyPart) throws NoSuchAlgorithmException,
            NoSuchProviderException, SMIMEException {
        if (input.isEncryptedMessage()) {
            mimeBodyPart = gen.generate(mimeBodyPart, input.getEncryptionAlgorithm(), Constants.BOUNCY_CASTLE_PROVIDER);
        }
        return mimeBodyPart;
    }

    private void addEncryptionSettings() throws Exception {
        URL keystoreUrl = new URL(input.getEncryptionKeystore());
        InputStream publicKeystoreInputStream = keystoreUrl.openStream();
        char[] smimePw = new String(input.getEncryptionKeystorePassword()).toCharArray();


        gen = new SMIMEEnvelopedGenerator();
        Security.addProvider(new BouncyCastleProvider());
        KeyStore ks = KeyStore.getInstance(Constants.PKCS_KEYSTORE_TYPE, Constants.BOUNCY_CASTLE_PROVIDER);
        try {
            ks.load(publicKeystoreInputStream, smimePw);
        } finally {
            publicKeystoreInputStream.close();
        }

        if (Strings.EMPTY.equals(input.getEncryptionKeyAlias())) {
            Enumeration aliases = ks.aliases();
            while (aliases.hasMoreElements()) {
                String alias = (String) aliases.nextElement();

                if (ks.isKeyEntry(alias)) {
                    input.setEncryptionKeyAlias(alias);
                }
            }
        }

        if (Strings.EMPTY.equals(input.getEncryptionKeyAlias())) {
            throw new Exception(ExceptionMsgs.PUBLIC_KEY_ERROR_MESSAGE);
        }

        Certificate[] chain = ks.getCertificateChain(input.getEncryptionKeyAlias());

        if (chain == null) {
            throw new Exception("The key with alias \"" + input.getEncryptionKeyAlias() + "\" can't be found in given keystore.");
        }

        //
        // create the generator for creating an smime/encrypted message
        //
        gen.addKeyTransRecipient((X509Certificate) chain[0]);
    }


    /**
     * The method creates a copy of the SMTPMessage object passed through the arguments list and adds the headers to the
     * copied object then returns it. If the header is already present in the message then its values list will be
     * updated with the given header value.
     *
     * @param message      The SMTPMessage object to which the headers are added or updated.
     * @param headerNames  A list of strings containing the header names that need to be added or updated.
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
