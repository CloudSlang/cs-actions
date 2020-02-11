package io.cloudslang.content.mail.entities;

import io.cloudslang.content.mail.utils.Constants;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class GetMailMessageCountInput {

    private String hostname;
    private Short port;
    private String protocol;
    private String username;
    private String password;
    private String folder;
    private boolean trustAllRoots;
    private boolean enableSSL;
    private boolean enableTLS;
    private String keystore;
    private String keystorePassword;
    private String trustKeystore;
    private String trustPassword;

    private GetMailMessageCountInput() {
    }

    public String getHostname() {
        return hostname;
    }

    public Short getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFolder() {
        return folder;
    }

    public boolean isTrustAllRoots() {
        return trustAllRoots;
    }

    public boolean isEnableSSL() {
        return enableSSL;
    }

    public boolean isEnableTLS() {
        return enableTLS;
    }

    public String getKeystore() {
        return keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }
    
    
    public static class Builder {

        private String hostname;
        private String port;
        private String protocol;
        private String username;
        private String password;
        private String folder;
        private String trustAllRoots;
        private String enableSSL;
        private String enableTLS;
        private String keystore;
        private String keystorePassword;
        private String trustKeystore;
        private String trustPassword;

        public Builder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }


        public Builder port(String port) {
            this.port = port;
            return this;
        }


        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }


        public Builder username(String username) {
            this.username = username;
            return this;
        }


        public Builder password(String password) {
            this.password = password;
            return this;
        }


        public Builder folder(String folder) {
            this.folder = folder;
            return this;
        }


        public Builder trustAllRoots(String trustAllRoots) {
            this.trustAllRoots = trustAllRoots;
            return this;
        }


        public Builder enableSSL(String enableSSL) {
            this.enableSSL = enableSSL;
            return this;
        }


        public Builder enableTLS(String enableTLS) {
            this.enableTLS = enableTLS;
            return this;
        }


        public Builder keystore(String keystore) {
            this.keystore = keystore;
            return this;
        }


        public Builder keystorePassword(String keystorePassword) {
            this.keystorePassword = keystorePassword;
            return this;
        }


        public Builder trustKeystore(String trustKeystore) {
            this.trustKeystore = trustKeystore;
            return this;
        }


        public Builder trustPassword(String trustPassword) {
            this.trustPassword = trustPassword;
            return this;
        }


        public GetMailMessageCountInput build() throws Exception {
            GetMailMessageCountInput input = new GetMailMessageCountInput();

            input.hostname = hostname;

            if (!StringUtils.isEmpty(port)) {
                input.port = Short.parseShort(port);
            }

            input.protocol = protocol;
            if (isEmpty(protocol) && isEmpty(port)) {
                throw new Exception(Constants.ExceptionMsgs.SPECIFY_PORT_OR_PROTOCOL_OR_BOTH);
            } else if ((protocol != null && !protocol.isEmpty()) && (!protocol.equalsIgnoreCase(Constants.IMAPProps.IMAP)) &&
                    (!protocol.equalsIgnoreCase(Constants.POPProps.POP3)) && (!protocol.equalsIgnoreCase(Constants.IMAPProps.IMAP4)) &&
                    isEmpty(port)) {
                throw new Exception(Constants.ExceptionMsgs.SPECIFY_PORT_FOR_PROTOCOL);
            } else if (isEmpty(protocol) && !isEmpty(port) &&
                    (!port.equalsIgnoreCase(Constants.IMAPProps.PORT)) && (!port.equalsIgnoreCase(Constants.POPProps.POP3_PORT))) {
                throw new Exception(Constants.ExceptionMsgs.SPECIFY_PROTOCOL_FOR_GIVEN_PORT);
            } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(Constants.IMAPProps.PORT)) {
                input.protocol = Constants.IMAPProps.IMAP;
            } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(Constants.POPProps.POP3_PORT)) {
                input.protocol = Constants.POPProps.POP3;
            } else if (protocol.trim().equalsIgnoreCase(Constants.POPProps.POP3) && isEmpty(port)) {
                input.port = Short.parseShort(Constants.POPProps.POP3_PORT);
            } else if (protocol.trim().equalsIgnoreCase(Constants.IMAPProps.IMAP) && isEmpty(port)) {
                input.port = Short.parseShort(Constants.IMAPProps.PORT);
            } else if (protocol.trim().equalsIgnoreCase(Constants.IMAPProps.IMAP4) && (isEmpty(port))) {
                input.port = Short.parseShort(Constants.IMAPProps.PORT);
            }
            //The protocol should be given in lowercase to be recognised.
            input.protocol = input.protocol.toLowerCase();
            if (input.protocol.trim().equalsIgnoreCase(Constants.IMAPProps.IMAP4)) {
                input.protocol = Constants.IMAPProps.IMAP;
            }

            input.username = username;

            input.password = password;

            input.folder = folder;

            input.trustAllRoots = StringUtils.isEmpty(trustAllRoots) ? true : Boolean.parseBoolean(trustAllRoots);

            input.enableTLS = enableTLS != null && enableTLS.equalsIgnoreCase(Constants.Strings.TRUE);

            input.enableSSL = enableSSL != null && enableSSL.equalsIgnoreCase(Constants.Strings.TRUE);

            input.keystore = keystore;

            input.keystorePassword = keystorePassword;

            input.trustKeystore = trustKeystore;

            input.trustPassword = trustPassword;

            return input;
        }
    }
}
