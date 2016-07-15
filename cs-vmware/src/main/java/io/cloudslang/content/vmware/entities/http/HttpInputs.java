package io.cloudslang.content.vmware.entities.http;

import io.cloudslang.content.vmware.utils.InputUtils;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class HttpInputs {
    private static final int DEFAULT_HTTPS_PORT = 443;

    private String host;
    private int port;
    private String protocol;
    private String username;
    private String password;
    private boolean trustEveryone;

    public HttpInputs(HttpInputsBuilder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.protocol = builder.protocol;
        this.username = builder.username;
        this.password = builder.password;
        this.trustEveryone = builder.trustEveryone;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
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

    public Boolean isTrustEveryone() {
        return trustEveryone;
    }

    public static class HttpInputsBuilder {
        private String host;
        private int port;
        private String protocol;
        private String username;
        private String password;
        private boolean trustEveryone;

        public HttpInputs build() {
            return new HttpInputs(this);
        }

        public HttpInputsBuilder withHost(String inputValue) {
            host = inputValue;
            return this;
        }

        public HttpInputsBuilder withPort(String inputValue) {
            port = InputUtils.getIntInput(inputValue, DEFAULT_HTTPS_PORT);
            return this;
        }

        public HttpInputsBuilder withProtocol(String inputValue) throws Exception {
            protocol = Protocol.getValue(inputValue);
            return this;
        }

        public HttpInputsBuilder withUsername(String inputValue) throws Exception {
            username = inputValue;
            return this;
        }

        public HttpInputsBuilder withPassword(String inputValue) throws Exception {
            password = inputValue;
            return this;
        }

        public HttpInputsBuilder withTrustEveryone(String inputValue) throws Exception {
            trustEveryone = Boolean.parseBoolean(inputValue);
            return this;
        }
    }
}
