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
import io.cloudslang.content.mail.constants.MimeTypes;
import io.cloudslang.content.mail.constants.SecurityConstants;
import io.cloudslang.content.mail.constants.ExceptionMsgs;
import io.cloudslang.content.mail.constants.OutputNames;
import io.cloudslang.content.mail.entities.GetMailAttachmentInput;
import io.cloudslang.content.mail.utils.MessageStoreUtils;
import io.cloudslang.content.mail.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEUtil;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.security.KeyStore;
import java.util.*;

public class GetMailAttachmentService {

    protected GetMailAttachmentInput input;

    private Map<String, String> results = new HashMap<>();
    private RecipientId recId = null;
    private KeyStore ks = null;

    public Map<String, String> execute(GetMailAttachmentInput getMailAttachmentInput) throws Exception {
        this.results = new HashMap<>();
        this.input = getMailAttachmentInput;

        try (Store store = MessageStoreUtils.createMessageStore(input)) {
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
                ks = KeyStore.getInstance(SecurityConstants.PKCS_KEYSTORE_TYPE, SecurityConstants.BOUNCY_CASTLE_PROVIDER);
                recId = new KeyTransRecipientId(new byte[]{});
                SecurityUtils.addDecryptionSettings(ks, recId, input);
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

            return results;
        }
    }


    private String readAttachment(Message message, String attachment, String characterSet) throws Exception {
        if (!message.isMimeType(MimeTypes.TEXT_PLAIN) && !message.isMimeType(MimeTypes.TEXT_HTML)) {

            Multipart mpart = (Multipart) message.getContent();

            if (mpart != null) {
                for (int i = 0, n = mpart.getCount(); i < n; i++) {
                    Part part = mpart.getBodyPart(i);

                    if (input.isEncryptedMessage() && part.getContentType() != null &&
                            part.getContentType().equals(SecurityConstants.ENCRYPTED_CONTENT_TYPE)) {
                        part = decryptPart((MimeBodyPart) part);
                    }

                    if (part != null && part.getFileName() != null &&
                            (MimeUtility.decodeText(part.getFileName()).equalsIgnoreCase(attachment) ||
                                    part.getFileName().equalsIgnoreCase(attachment))
                    ) {
                        String disposition = part.getDisposition();
                        if (disposition != null && disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                            String partPrefix = part.getContentType().toLowerCase();
                            if (partPrefix.startsWith(MimeTypes.TEXT_PLAIN)) {
                                String attachmentContent = attachmentToString(part, characterSet);
                                results.put(io.cloudslang.content.constants.OutputNames.RETURN_RESULT, MimeUtility.decodeText(attachmentContent));
                                results.put(OutputNames.TEMPORARY_FILE, writeTextToTempFile(attachmentContent));
                            } else
                                results.put(OutputNames.TEMPORARY_FILE, writeToTempFile(part));
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
                SecurityConstants.BOUNCY_CASTLE_PROVIDER)
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

        try (FileWriter fw = new FileWriter(f)) {
            fw.write(text);
        }

        return f.getCanonicalPath();
    }


    private static String writeToTempFile(Part part) throws IOException, MessagingException {
        InputStream is = part.getInputStream();
        File f = File.createTempFile("attachment", null);

        try (OutputStream os = new FileOutputStream(f)) {
            int readChar;
            while ((readChar = is.read()) != -1) {
                os.write(readChar);
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
                                part.getContentType().equals(SecurityConstants.ENCRYPTED_CONTENT_TYPE)) {
                            part = decryptPart((MimeBodyPart) part);
                        }

                        if (part != null && part.getFileName() != null &&
                                (MimeUtility.decodeText(part.getFileName()).equalsIgnoreCase(attachment) ||
                                        part.getFileName().equalsIgnoreCase(attachment))
                        ) {
                            String disposition = part.getDisposition();
                            if (disposition != null && disposition.equalsIgnoreCase(Part.ATTACHMENT)) {
                                String partPrefix = part.getContentType().toLowerCase();
                                if (partPrefix.startsWith(io.cloudslang.content.mail.constants.MimeTypes.TEXT_PLAIN)) {
                                    String attachmentContent = attachmentToString(part, characterSet);
                                    writeTextToNewFile(path + "/" + attachment, attachmentContent, overwrite);
                                    results.put(io.cloudslang.content.constants.OutputNames.RETURN_RESULT, MimeUtility.decodeText(attachmentContent));
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
