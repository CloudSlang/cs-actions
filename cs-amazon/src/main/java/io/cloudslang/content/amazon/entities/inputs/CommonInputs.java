/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.entities.aws.AmazonApi;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.net.MalformedURLException;
import java.net.URL;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.COMMA_DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;

/**
 * Created by persdana
 * 5/27/2015.
 */
public class CommonInputs {
    private final String endpoint;
    private final String identity;
    private final String credential;
    private final String proxyHost;
    private final String proxyPort;
    private final String proxyUsername;
    private final String proxyPassword;
    private final String delimiter;
    private final String version;
    private final String headers;
    private final String queryParams;
    private final String apiService;
    private final String requestUri;
    private final String action;
    private final String requestPayload;
    private final String httpClientMethod;

    private CommonInputs(Builder builder) {
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

    public static class Builder {
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

        public CommonInputs build() {
            return new CommonInputs(this);
        }

        public Builder withEndpoint(String inputValue, String apiService) throws MalformedURLException {
            endpoint = new URL(InputsUtil.getUrlFromApiService(inputValue, apiService)).toString();
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
            delimiter = InputsUtil.getDefaultStringInput(inputValue, COMMA_DELIMITER);
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
            apiService = AmazonApi.getApiValue(inputValue);
            return this;
        }

        public Builder withRequestUri(String inputValue) {
            requestUri = InputsUtil.getDefaultStringInput(inputValue, EMPTY);
            return this;
        }

        public Builder withAction(String inputValue) {
            action = inputValue;
            return this;
        }

        public Builder withRequestPayload(String inputValue) {
            requestPayload = InputsUtil.getDefaultStringInput(inputValue, EMPTY);
            return this;
        }

        public Builder withHttpClientMethod(String inputValue) {
            httpClientMethod = InputsUtil.getDefaultStringInput(inputValue, HTTP_CLIENT_METHOD_GET);
            return this;
        }
    }
}
