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


package io.cloudslang.content.mail.entities;

import io.cloudslang.content.mail.constants.Constants;
import io.cloudslang.content.mail.constants.Encodings;
import io.cloudslang.content.mail.constants.TlsVersions;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static io.cloudslang.content.mail.utils.InputBuilderUtils.*;

/**
 * Created by giloan on 10/30/2014.
 */
public class SendMailInput implements MailInput {

    private String hostname;
    private Short port;
    private boolean htmlEmail;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String body;
    private boolean readReceipt;
    private String attachments;
    private List<String> headerNames;
    private List<String> headerValues;
    private String rowDelimiter;
    private String columnDelimiter;
    private String password;
    private String delimiter;
    private String characterSet;
    private String contentTransferEncoding;
    private String encodingScheme;
    private String encryptionKeystore;
    private String encryptionKeyAlias;
    private String encryptionKeystorePassword;
    private boolean enableTLS;
    private String user;
    private String encryptionAlgorithm;
    private String proxyHost;
    private String proxyPort;
    private String proxyUsername;
    private String proxyPassword;
    private int timeout = -1;
    private List<String> tlsVersions;
    private List<String> allowedCiphers;


    private SendMailInput() {
    }


    public String getHostname() {
        return hostname;
    }


    public short getPort() {
        return port;
    }


    public boolean isHtmlEmail() {
        return htmlEmail;
    }


    public String getFrom() {
        return from;
    }


    public String getTo() {
        return to;
    }


    public String getCc() {
        return cc;
    }


    public String getBcc() {
        return bcc;
    }


    public String getSubject() {
        return subject;
    }


    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }


    public boolean isReadReceipt() {
        return readReceipt;
    }


    public String getAttachments() {
        return attachments;
    }


    public List<String> getHeaderNames() {
        return headerNames;
    }


    public List<String> getHeaderValues() {
        return headerValues;
    }


    public String getRowDelimiter() {
        return rowDelimiter;
    }


    public String getColumnDelimiter() {
        return columnDelimiter;
    }


    public String getPassword() {
        return password;
    }


    public String getDelimiter() {
        return delimiter;
    }


    public String getCharacterSet() {
        return characterSet;
    }


    public String getContentTransferEncoding() {
        return contentTransferEncoding;
    }


    public String getEncodingScheme() {
        return encodingScheme;
    }


    public boolean isEncryptedMessage() {
        return !StringUtils.isEmpty(encryptionKeystore);
    }


    public String getEncryptionKeystore() {
        return encryptionKeystore;
    }


    public String getEncryptionKeyAlias() {
        return encryptionKeyAlias;
    }


    public void setEncryptionKeyAlias(String encryptionKeyAlias) {
        this.encryptionKeyAlias = encryptionKeyAlias;
    }


    public String getEncryptionKeystorePassword() {
        return encryptionKeystorePassword;
    }


    public boolean isEnableTLS() {
        return enableTLS;
    }


    public String getUser() {
        return user;
    }


    public int getTimeout() {
        return timeout;
    }


    public String getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }


    public String getProtocol() {
        return StringUtils.EMPTY;
    }


    public String getProxyHost() {
        return proxyHost;
    }


    public String getProxyPort() {
        return proxyPort;
    }


    public String getProxyUsername() {
        return proxyUsername;
    }


    public String getProxyPassword() {
        return proxyPassword;
    }


    public List<String> getTlsVersions() {
        return tlsVersions;
    }


    public List<String> getAllowedCiphers() {
        return allowedCiphers;
    }


    public static class Builder {

        private String hostname;
        private String port;
        private String htmlEmail;
        private String from;
        private String to;
        private String cc;
        private String bcc;
        private String subject;
        private String body;
        private String readReceipt;
        private String attachments;
        private String headers;
        private String rowDelimiter;
        private String columnDelimiter;
        private String password;
        private String delimiter;
        private String characterSet;
        private String contentTransferEncoding;
        private String encryptionKeystore;
        private String encryptionKeyAlias;
        private String encryptionKeystorePassword;
        private String enableTLS;
        private String user;
        private String encryptionAlgorithm;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private String timeout;
        private String tlsVersion;
        private String allowedCiphers;


        public Builder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }


        public Builder port(String port) {
            this.port = port;
            return this;
        }


        public Builder htmlEmail(String htmlEmail) {
            this.htmlEmail = htmlEmail;
            return this;
        }


        public Builder from(String from) {
            this.from = from;
            return this;
        }


        public Builder to(String to) {
            this.to = to;
            return this;
        }


        public Builder cc(String cc) {
            this.cc = cc;
            return this;
        }


        public Builder bcc(String bcc) {
            this.bcc = bcc;
            return this;
        }


        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }


        public Builder body(String body) {
            this.body = body;
            return this;
        }


        public Builder readReceipt(String readReceipt) {
            this.readReceipt = readReceipt;
            return this;
        }


        public Builder attachments(String attachments) {
            this.attachments = attachments;
            return this;
        }


        public Builder headers(String headers) {
            this.headers = headers;
            return this;
        }


        public Builder rowDelimiter(String rowDelimiter) {
            this.rowDelimiter = rowDelimiter;
            return this;
        }


        public Builder columnDelimiter(String columnDelimiter) {
            this.columnDelimiter = columnDelimiter;
            return this;
        }


        public Builder password(String password) {
            this.password = password;
            return this;
        }


        public Builder delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }


        public Builder characterSet(String characterSet) {
            this.characterSet = characterSet;
            return this;
        }


        public Builder contentTransferEncoding(String contentTransferEncoding) {
            this.contentTransferEncoding = contentTransferEncoding;
            return this;
        }


        public Builder encryptionKeystore(String encryptionKeystore) {
            this.encryptionKeystore = encryptionKeystore;
            return this;
        }


        public Builder encryptionKeyAlias(String encryptionKeyAlias) {
            this.encryptionKeyAlias = encryptionKeyAlias;
            return this;
        }


        public Builder encryptionKeystorePassword(String encryptionKeystorePassword) {
            this.encryptionKeystorePassword = encryptionKeystorePassword;
            return this;
        }


        public Builder enableTLS(String enableTLS) {
            this.enableTLS = enableTLS;
            return this;
        }


        public Builder user(String user) {
            this.user = user;
            return this;
        }


        public Builder timeout(String timeout) {
            this.timeout = timeout;
            return this;
        }


        public Builder encryptionAlgorithm(String encryptionAlgorithm) {
            this.encryptionAlgorithm = encryptionAlgorithm;
            return this;
        }


        public Builder proxyHost(String proxyHost) {
            this.proxyHost = proxyHost;
            return this;
        }


        public Builder proxyPort(String proxyPort) {
            this.proxyPort = proxyPort;
            return this;
        }


        public Builder proxyUsername(String proxyUsername) {
            this.proxyUsername = proxyUsername;
            return this;
        }


        public Builder proxyPassword(String proxyPassword) {
            this.proxyPassword = proxyPassword;
            return this;
        }


        public Builder tlsVersion(String tlsVersion) {
            this.tlsVersion = tlsVersion;
            return this;
        }


        public Builder allowedCiphers(String allowedCiphers) {
            this.allowedCiphers = allowedCiphers;
            return this;
        }


        public SendMailInput build() throws Exception {
            SendMailInput input = new SendMailInput();

            try {
                input.htmlEmail = Boolean.parseBoolean(htmlEmail);
            } catch (Exception ex) {
                input.htmlEmail = false;
            }

            try {
                input.readReceipt = Boolean.parseBoolean(readReceipt);
            } catch (Exception ex) {
                input.readReceipt = false;
            }

            input.hostname = buildHostname(hostname);

            input.port = buildPort(port, true);

            input.from = StringUtils.defaultString(from);

            input.to = StringUtils.defaultString(to);

            input.cc = StringUtils.defaultString(cc);

            input.bcc = StringUtils.defaultString(bcc);

            input.subject = StringUtils.defaultString(subject);

            input.body = StringUtils.defaultString(body);

            input.attachments = (attachments != null) ? attachments : StringUtils.EMPTY;

            input.delimiter = StringUtils.isEmpty(delimiter) ? "," : delimiter;

            input.user = buildUsername(user, false);

            input.password = buildPassword(password);

            input.characterSet = StringUtils.isEmpty(characterSet) ? "UTF-8" : characterSet;

            input.contentTransferEncoding = StringUtils.isEmpty(contentTransferEncoding) ?
                    Encodings.QUOTED_PRINTABLE :
                    contentTransferEncoding;

            input.htmlEmail = (htmlEmail != null && htmlEmail.equalsIgnoreCase(String.valueOf(true)));

            input.readReceipt = (readReceipt != null && readReceipt.equalsIgnoreCase(String.valueOf(true)));

            input.encodingScheme = (contentTransferEncoding != null && contentTransferEncoding.equals(Encodings.QUOTED_PRINTABLE)) ? "Q" : "B";

            input.tlsVersions = buildTlsVersions(tlsVersion);

            if(input.tlsVersions.contains(TlsVersions.TLSv1_2)) {
                input.allowedCiphers = buildAllowedCiphers(allowedCiphers);

            } else {
                input.encryptionKeystore = encryptionKeystore;
                if (input.isEncryptedMessage()) {
                    if (!encryptionKeystore.startsWith(Constants.HTTP)) {
                        encryptionKeystore = Constants.FILE + encryptionKeystore;
                    }

                    input.encryptionKeyAlias = (encryptionKeyAlias == null) ? StringUtils.EMPTY : encryptionKeyAlias;

                    input.encryptionKeystorePassword = (encryptionKeystorePassword == null) ? StringUtils.EMPTY : encryptionKeystorePassword;

                    input.encryptionAlgorithm = EncryptionAlgorithmsEnum.getEncryptionAlgorithm(encryptionAlgorithm).getEncryptionOID();
                }
            }

            input.enableTLS = buildEnableTLS(enableTLS);

            input.rowDelimiter = StringUtils.isEmpty(rowDelimiter) ? "\n" : rowDelimiter;

            input.columnDelimiter = StringUtils.isEmpty(columnDelimiter) ? ":" : columnDelimiter;

            validateDelimiters(input.rowDelimiter, input.columnDelimiter);

            if (!isEmpty(headers)) {
                Object[] headersMap = extractHeaderNamesAndValues(headers, input.rowDelimiter, input.columnDelimiter);
                input.headerNames = (ArrayList<String>) headersMap[0];
                input.headerValues = (ArrayList<String>) headersMap[1];
            }

            input.timeout = buildTimeout(timeout);

            input.proxyHost = StringUtils.defaultString(proxyHost);

            input.proxyPort = StringUtils.defaultString(proxyPort);

            input.proxyUsername = StringUtils.defaultString(proxyUsername);

            input.proxyPassword = StringUtils.defaultString(proxyPassword);

            return input;
        }


        void validateDelimiters(String rowDelimiter, String columnDelimiter) throws Exception {
            if (rowDelimiter.equals(columnDelimiter)) {
                throw new Exception(io.cloudslang.content.mail.constants.ExceptionMsgs.INVALID_DELIMITERS);
            }
            if (StringUtils.contains(columnDelimiter, rowDelimiter)) {
                throw new Exception(io.cloudslang.content.mail.constants.ExceptionMsgs.INVALID_ROW_DELIMITER);
            }
        }


        Object[] extractHeaderNamesAndValues(String headersMap, String rowDelimiter, String columnDelimiter)
                throws Exception {
            String[] rows = headersMap.split(Pattern.quote(rowDelimiter));
            ArrayList<String> headerNames = new ArrayList<>();
            ArrayList<String> headerValues = new ArrayList<>();
            for (int i = 0; i < rows.length; i++) {
                if (isEmpty(rows[i])) {
                    continue;
                }
                if (validateRow(rows[i], columnDelimiter, i)) {
                    String[] headerNameAndValue = rows[i].split(Pattern.quote(columnDelimiter));
                    headerNames.add(i, headerNameAndValue[0].trim());
                    headerValues.add(i, headerNameAndValue[1].trim());
                }
            }
            return new Object[]{headerNames, headerValues};
        }


        boolean validateRow(String row, String columnDelimiter, int rowNumber) throws Exception {
            if (row.contains(columnDelimiter)) {
                if (row.equals(columnDelimiter)) {
                    throw new Exception(format(io.cloudslang.content.mail.constants.ExceptionMsgs.ROW_WITH_EMPTY_HEADERS_INPUT, rowNumber + 1));
                } else {
                    String[] headerNameAndValue = row.split(Pattern.quote(columnDelimiter));
                    if (StringUtils.countMatches(row, columnDelimiter) > 1) {
                        throw new Exception(format(io.cloudslang.content.mail.constants.ExceptionMsgs.ROW_WITH_MULTIPLE_COLUMN_DELIMITERS_IN_HEADERS_INPUT, rowNumber + 1));
                    } else {
                        if (headerNameAndValue.length == 1) {
                            throw new Exception(format(io.cloudslang.content.mail.constants.ExceptionMsgs.ROW_WITH_MISSING_VALUE_FOR_HEADER, rowNumber + 1));
                        } else {
                            return true;
                        }
                    }
                }
            } else {
                throw new Exception("Row #" + (rowNumber + 1) + " in the 'headers' input has no column delimiter.");
            }
        }
    }
}
