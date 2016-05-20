package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.Providers;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.utils.InputsUtil;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by persdana on 5/27/2015.
 */
public class CommonInputs {
    private String provider;
    private String endpoint;
    private String identity;
    private String credential;
    private String proxyHost;
    private String proxyPort;
    private String delimiter;

    public CommonInputs(CommonInputsBuilder builder) {
        this.provider = builder.provider;
        this.endpoint = builder.endpoint;
        this.identity = builder.identity;
        this.credential = builder.credential;
        this.proxyHost = builder.proxyHost;
        this.proxyPort = builder.proxyPort;
        this.delimiter = builder.delimiter;
    }

    public String getProvider() {
        return provider;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getIdentity() {
        return identity;
    }

    public String getCredential() {
        return credential;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public static class CommonInputsBuilder {
        private String provider;
        private String endpoint;
        private String identity;
        private String credential;
        private String proxyHost;
        private String proxyPort;
        private String delimiter;

        public CommonInputs build() {
            return new CommonInputs(this);
        }

        public CommonInputsBuilder withProvider(String inputValue) throws Exception {
            provider = Providers.getValue(inputValue);
            return this;
        }

        public CommonInputsBuilder withEndpoint(String inputValue) throws MalformedURLException {
            URL url = new URL(inputValue.toLowerCase());
            InputsUtil.validateInput(url.toString(), Inputs.CommonInputs.ENDPOINT);
            endpoint = url.toString();
            return this;
        }

        public CommonInputsBuilder withIdentity(String inputValue) {
            identity = inputValue;
            return this;
        }

        public CommonInputsBuilder withCredential(String inputValue) {
            credential = inputValue;
            return this;
        }

        public CommonInputsBuilder withProxyHost(String inputValue) {
            proxyHost = inputValue;
            return this;
        }

        public CommonInputsBuilder withProxyPort(String inputValue) {
            proxyPort = inputValue;
            return this;
        }

        public CommonInputsBuilder withDelimiter(String inputValue) {
            delimiter = InputsUtil.getDefaultDelimiter(inputValue);
            return this;
        }
    }
}