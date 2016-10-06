package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.aws.HttpClientMethod;
import io.cloudslang.content.jclouds.entities.aws.Providers;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

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
    private String proxyUsername;
    private String proxyPassword;
    private String delimiter;
    private String version;
    private String headers;
    private String queryParams;
    private String apiService;
    private String requestUri;
    private String action;
    private String requestPayload;
    private String httpClientMethod;

    private boolean debugMode;

    private CommonInputs(Builder builder) {
        this.provider = builder.provider;
        this.endpoint = builder.endpoint;
        this.identity = builder.identity;
        this.credential = builder.credential;
        this.proxyHost = builder.proxyHost;
        this.proxyPort = builder.proxyPort;
        this.proxyUsername = builder.proxyUsername;
        this.proxyPassword = builder.proxyPassword;
        this.delimiter = builder.delimiter;
        this.version = builder.version;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
        this.apiService = builder.apiService;
        this.requestUri = builder.requestUri;
        this.action = builder.action;
        this.requestPayload = builder.requestPayload;
        this.httpClientMethod = builder.httpClientMethod;

        this.debugMode = builder.debugMode;
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

    public String getProxyUsername() {
        return proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public String getVersion() {
        return version;
    }

    public String getHeaders() {
        return headers;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public String getApiService() {
        return apiService;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public String getAction() {
        return action;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public String getHttpClientMethod() {
        return httpClientMethod;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public static class Builder {
        private String provider;
        private String endpoint;
        private String identity;
        private String credential;
        private String proxyHost;
        private String proxyPort;
        private String proxyUsername;
        private String proxyPassword;
        private String delimiter;
        private String version;
        private String headers;
        private String queryParams;
        private String apiService;
        private String requestUri;
        private String action;
        private String requestPayload;
        private String httpClientMethod;

        private boolean debugMode;

        public CommonInputs build() {
            return new CommonInputs(this);
        }

        public Builder withProvider(String inputValue) throws Exception {
            provider = Providers.getValue(inputValue);
            return this;
        }

        public Builder withEndpoint(String inputValue) throws MalformedURLException {
            if (StringUtils.isBlank(inputValue)) {
                throw new RuntimeException("The required " + inputValue + " input is not specified!");
            }
            endpoint = new URL(inputValue.toLowerCase()).toString();
            return this;
        }

        public Builder withIdentity(String inputValue) {
            identity = inputValue;
            return this;
        }

        public Builder withCredential(String inputValue) {
            credential = inputValue;
            return this;
        }

        public Builder withProxyHost(String inputValue) {
            proxyHost = inputValue;
            return this;
        }

        public Builder withProxyPort(String inputValue) {
            proxyPort = inputValue;
            return this;
        }

        public Builder withProxyUsername(String inputValue) {
            proxyUsername = inputValue;
            return this;
        }

        public Builder withProxyPassword(String inputValue) {
            proxyPassword = inputValue;
            return this;
        }

        public Builder withDelimiter(String inputValue) {
            delimiter = InputsUtil.getDefaultStringInput(inputValue, Constants.Miscellaneous.COMMA_DELIMITER);
            return this;
        }

        public Builder withVersion(String inputValue) {
            version = inputValue;
            return this;
        }

        public Builder withHeaders(String inputValue) {
            headers = inputValue;
            return this;
        }

        public Builder withQueryParams(String inputValue) {
            queryParams = inputValue;
            return this;
        }

        public Builder withApiService(String inputValue) {
            apiService = InputsUtil.getDefaultStringInput(inputValue, Constants.Apis.AMAZON_EC2_API);
            return this;
        }

        public Builder withRequestUri(String inputValue) {
            requestUri = InputsUtil.getDefaultStringInput(inputValue, Constants.Miscellaneous.EMPTY);
            return this;
        }

        public Builder withAction(String inputValue) {
            action = inputValue;
            return this;
        }

        public Builder withRequestPayload(String inputValue) {
            requestPayload = InputsUtil.getDefaultStringInput(inputValue, Constants.Miscellaneous.EMPTY);
            return this;
        }

        public Builder withHttpClientMethod(String inputValue) {
            httpClientMethod = InputsUtil.getDefaultStringInput(inputValue, HttpClientMethod.GET.toString());
            return this;
        }

        public Builder withDebugMode(String inputValue) {
            debugMode = Boolean.parseBoolean(inputValue);
            return this;
        }
    }
}