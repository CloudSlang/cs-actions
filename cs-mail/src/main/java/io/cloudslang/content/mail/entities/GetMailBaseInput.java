package io.cloudslang.content.mail.entities;

import io.cloudslang.content.mail.constants.ExceptionMsgs;
import io.cloudslang.content.mail.constants.ImapPropNames;
import io.cloudslang.content.mail.constants.PopPropNames;
import io.cloudslang.content.mail.constants.SecurityConstants;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public abstract class GetMailBaseInput {

    protected String hostname;
    protected Short port;
    protected String protocol;
    protected String username;
    protected String password;
    protected boolean trustAllRoots;
    protected boolean enableSSL;
    protected boolean enableTLS;
    protected String keystore;
    protected String keystorePassword;

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


    public static abstract class Builder {

        private String hostname;
        private String port;
        private String protocol;
        private String username;
        private String password;
        private String trustAllRoots;
        private String enableSSL;
        private String enableTLS;
        private String keystore;
        private String keystorePassword;

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


        protected abstract GetMailBaseInput build() throws Exception;


        protected void build(GetMailBaseInput input) throws Exception {
            if (isEmpty(hostname)) {
                throw new Exception(ExceptionMsgs.HOST_NOT_SPECIFIED);
            }
            input.hostname = hostname.trim();

            if (!StringUtils.isEmpty(port)) {
                input.port = Short.parseShort(port);
                if(input.port < 1) {
                    throw new Exception(ExceptionMsgs.INVALID_PORT_NUMBER);
                }
            }

            input.protocol = protocol;
            if (isEmpty(protocol) && isEmpty(port)) {
                throw new Exception(io.cloudslang.content.mail.constants.ExceptionMsgs.SPECIFY_PORT_OR_PROTOCOL_OR_BOTH);
            } else if ((protocol != null && !protocol.isEmpty()) && (!protocol.equalsIgnoreCase(ImapPropNames.IMAP)) &&
                    (!protocol.equalsIgnoreCase(PopPropNames.POP3)) && (!protocol.equalsIgnoreCase(ImapPropNames.IMAP4)) &&
                    isEmpty(port)) {
                throw new Exception(ExceptionMsgs.SPECIFY_PORT_FOR_PROTOCOL);
            } else if (isEmpty(protocol) && !isEmpty(port) &&
                    (!port.equalsIgnoreCase(ImapPropNames.PORT)) && (!port.equalsIgnoreCase(PopPropNames.POP3_PORT))) {
                throw new Exception(io.cloudslang.content.mail.constants.ExceptionMsgs.SPECIFY_PROTOCOL_FOR_GIVEN_PORT);
            } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(ImapPropNames.PORT)) {
                input.protocol = ImapPropNames.IMAP;
            } else if (isEmpty(protocol) && port.trim().equalsIgnoreCase(PopPropNames.POP3_PORT)) {
                input.protocol = PopPropNames.POP3;
            } else if (protocol.trim().equalsIgnoreCase(PopPropNames.POP3) && isEmpty(port)) {
                input.port = Short.parseShort(PopPropNames.POP3_PORT);
            } else if (protocol.trim().equalsIgnoreCase(ImapPropNames.IMAP) && isEmpty(port)) {
                input.port = Short.parseShort(ImapPropNames.PORT);
            } else if (protocol.trim().equalsIgnoreCase(ImapPropNames.IMAP4) && (isEmpty(port))) {
                input.port = Short.parseShort(ImapPropNames.PORT);
            }
            //The protocol should be given in lowercase to be recognised.
            input.protocol = input.protocol.toLowerCase();
            if (input.protocol.trim().equalsIgnoreCase(ImapPropNames.IMAP4)) {
                input.protocol = ImapPropNames.IMAP;
            }

            if (isEmpty(username)) {
                throw new Exception(ExceptionMsgs.USERNAME_NOT_SPECIFIED);
            }
            input.username = username;

            input.password = password;

            input.trustAllRoots = StringUtils.isEmpty(trustAllRoots) ? true : Boolean.parseBoolean(trustAllRoots);

            input.enableTLS = enableTLS != null && enableTLS.equalsIgnoreCase(String.valueOf(true));

            input.enableSSL = enableSSL != null && enableSSL.equalsIgnoreCase(String.valueOf(true));

            input.keystore = defaultIfEmpty(keystore, SecurityConstants.DEFAULT_JAVA_KEYSTORE);

            input.keystorePassword = keystorePassword;
        }
    }
}
