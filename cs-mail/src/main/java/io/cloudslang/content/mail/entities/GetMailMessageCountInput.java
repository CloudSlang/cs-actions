package io.cloudslang.content.mail.entities;

public class GetMailMessageCountInput extends GetMailBaseInput {

    private String folder;
    private String trustKeystore;
    private String trustPassword;

    private GetMailMessageCountInput() {
    }

    public String getFolder() {
        return folder;
    }

    public String getTrustKeystore() {
        return trustKeystore;
    }

    public String getTrustPassword() {
        return trustPassword;
    }
    
    
    public static class Builder extends GetMailBaseInput.Builder{

        private String folder;
        private String trustKeystore;
        private String trustPassword;


        public Builder folder(String folder) {
            this.folder = folder;
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

        @Override
        public Builder hostname(String hostname) {
            return (Builder) super.hostname(hostname);
        }

        @Override
        public Builder port(String port) {
            return (Builder) super.port(port);
        }

        @Override
        public Builder protocol(String protocol) {
            return (Builder) super.protocol(protocol);
        }

        @Override
        public Builder username(String username) {
            return (Builder) super.username(username);
        }

        @Override
        public Builder password(String password) {
            return (Builder) super.password(password);
        }

        @Override
        public Builder trustAllRoots(String trustAllRoots) {
            return (Builder) super.trustAllRoots(trustAllRoots);
        }

        @Override
        public Builder enableSSL(String enableSSL) {
            return (Builder) super.enableSSL(enableSSL);
        }

        @Override
        public Builder enableTLS(String enableTLS) {
            return (Builder) super.enableTLS(enableTLS);
        }

        @Override
        public Builder keystore(String keystore) {
            return (Builder) super.keystore(keystore);
        }

        @Override
        public Builder keystorePassword(String keystorePassword) {
            return (Builder) super.keystorePassword(keystorePassword);
        }


        public GetMailMessageCountInput build() throws Exception {
            GetMailMessageCountInput input = new GetMailMessageCountInput();
            super.build(input);

            input.folder = folder;

            input.trustKeystore = trustKeystore;

            input.trustPassword = trustPassword;

            return input;
        }
    }
}
