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
import io.cloudslang.content.constants.ReturnCodes;
import io.cloudslang.content.mail.constants.*;
import io.cloudslang.content.mail.entities.GetMailMessageInput;
import io.cloudslang.content.mail.entities.StringOutputStream;
import io.cloudslang.content.mail.utils.MessageStoreUtils;
import io.cloudslang.content.mail.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cms.PasswordRecipientId;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.mail.smime.SMIMEEnveloped;

import java.io.*;
import java.security.KeyStore;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import static io.cloudslang.content.mail.constants.Constants.*;
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
                ks = KeyStore.getInstance(SecurityConstants.PKCS_KEYSTORE_TYPE, SecurityConstants.BOUNCY_CASTLE_PROVIDER);
                recId = new PasswordRecipientId();
                SecurityUtils.addDecryptionSettings(ks, recId, input);
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
                    subject = StringUtils.EMPTY;
                }
                result.put(OutputNames.SUBJECT, MimeUtility.decodeText(subject));
                result.put(io.cloudslang.content.constants.OutputNames.RETURN_RESULT, MimeUtility.decodeText(subject));
            } else {
                try {
                    // Get subject and attachedFileNames
                    if ((input.getCharacterSet() != null) && (input.getCharacterSet().trim().length() > 0)) {
                        //need to force the decode charset
                        String subject = message.getHeader(SUBJECT_HEADER)[0];
                        subject = changeHeaderCharset(subject, input.getCharacterSet());
                        result.put(OutputNames.SUBJECT, MimeUtility.decodeText(subject));
                        String attachedFileNames = changeHeaderCharset(getAttachedFileNames(message), input.getCharacterSet());
                        result.put(OutputNames.ATTACHED_FILE_NAMES, decodeAttachedFileNames(attachedFileNames));
                    } else {
                        //let everything as the sender intended it to be :)
                        String subject = message.getSubject();
                        if (subject == null) {
                            subject = StringUtils.EMPTY;
                        }
                        result.put(OutputNames.SUBJECT, MimeUtility.decodeText(subject));
                        result.put(OutputNames.ATTACHED_FILE_NAMES,
                                decodeAttachedFileNames((getAttachedFileNames(message))));
                    }
                    // Get the message body
                    Map<String, String> messageByTypes = getMessageByContentTypes(message, input.getCharacterSet());
                    String lastMessageBody = StringUtils.EMPTY;
                    if (!messageByTypes.isEmpty()) {
                        lastMessageBody = new LinkedList<>(messageByTypes.values()).getLast();
                    }
                    if (lastMessageBody == null) {
                        lastMessageBody = StringUtils.EMPTY;
                    }

                    result.put(OutputNames.BODY, MimeUtility.decodeText(lastMessageBody));

                    String plainTextBody = messageByTypes.containsKey(MimeTypes.TEXT_PLAIN) ?
                            messageByTypes.get(MimeTypes.TEXT_PLAIN) :
                            StringUtils.EMPTY;
                    result.put(OutputNames.PLAIN_TEXT_BODY, MimeUtility.decodeText(plainTextBody));

                    StringOutputStream stream = new StringOutputStream();
                    message.writeTo(stream);
                    result.put(io.cloudslang.content.constants.OutputNames.RETURN_RESULT,
                            stream.toString().replaceAll(StringUtils.EMPTY + (char) 0, StringUtils.EMPTY));
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

            result.put(io.cloudslang.content.constants.OutputNames.RETURN_CODE, ReturnCodes.SUCCESS);
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
        store = MessageStoreUtils.createMessageStore(input);
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
                        part.getContentType().equals(SecurityConstants.ENCRYPTED_CONTENT_TYPE)) {
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
                    part.getContentType().equals(SecurityConstants.ENCRYPTED_CONTENT_TYPE)) {
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

        Object content = StringUtils.EMPTY;

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
            errorMessage.append(SecurityConstants.ENCRYPT_RECID);

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
                SecurityConstants.BOUNCY_CASTLE_PROVIDER));
    }


    protected String getAttachedFileNames(Part part) throws Exception {
        String fileNames = StringUtils.EMPTY;
        Object content = part.getContent();
        if (!(content instanceof Multipart)) {
            if (input.isEncryptedMessage() && part.getContentType() != null &&
                    part.getContentType().equals(SecurityConstants.ENCRYPTED_CONTENT_TYPE)) {
                part = decryptPart((MimeBodyPart) part);
            }
            // non-Multipart MIME part ...
            // is the file name set for this MIME part? (i.e. is it an attachment?)
            if (part.getFileName() != null && !part.getFileName().equals(StringUtils.EMPTY) && part.getInputStream() != null) {
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
                if (!StringUtils.EMPTY.equals(fileNames)) {
                    fileNames += ",";
                }
                // to the list of attachments built so far append the list of attachments in the current MIME part ...
                fileNames += getAttachedFileNames(mpart.getBodyPart(i));
            }
        }
        return fileNames;
    }


    protected String decodeAttachedFileNames(String attachedFileNames) throws Exception {
        StringBuilder sb = new StringBuilder();
        String delimiter = StringUtils.EMPTY;
        // splits the input into comma-separated chunks and decodes each chunk according to its encoding ...
        for (String fileName : attachedFileNames.split(",")) {
            sb.append(delimiter).append(MimeUtility.decodeText(fileName));
            delimiter = ",";
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

    int getFolderOpenMode() {
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
    String changeHeaderCharset(String header, String newCharset) {
        //match for =?charset?
        return header.replaceAll("=\\?[^\\(\\)<>@,;:/\\[\\]\\?\\.= ]+\\?", "=?" + newCharset + "?");
    }
}