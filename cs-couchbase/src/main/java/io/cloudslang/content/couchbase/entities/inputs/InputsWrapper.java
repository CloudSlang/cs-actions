/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.entities.inputs;

import io.cloudslang.content.httpclient.HttpClientInputs;

/**
 * Created by Mihai Tusa
 * 4/9/2017.
 */
public class InputsWrapper {
    private HttpClientInputs httpClientInputs;
    private CommonInputs commonInputs;
    private BucketInputs bucketInputs;

    private final String requestPayload;
    private final String headers;
    private final String queryParams;

    private InputsWrapper(Builder builder) {
        this.requestPayload = builder.requestPayload;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
    }

    public HttpClientInputs getHttpClientInputs() {
        return httpClientInputs;
    }

    public void setHttpClientInputs(HttpClientInputs httpClientInputs) {
        this.httpClientInputs = httpClientInputs;
    }

    public CommonInputs getCommonInputs() {
        return commonInputs;
    }

    public void setCommonInputs(CommonInputs commonInputs) {
        this.commonInputs = commonInputs;
    }

    public BucketInputs getBucketInputs() {
        return bucketInputs;
    }

    public void setBucketInputs(BucketInputs bucketInputs) {
        this.bucketInputs = bucketInputs;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public String getHeaders() {
        return headers;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public static class Builder {
        private String requestPayload;
        private String headers;
        private String queryParams;

        public InputsWrapper build() {
            return new InputsWrapper(this);
        }

        public Builder withRequestPayload(String inputValue) {
            requestPayload = inputValue;
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
    }
}