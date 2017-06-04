/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.dropbox.entities.inputs;

import io.cloudslang.content.dropbox.entities.dropbox.ApiVersion;

/**
 * Created by TusaM
 * 5/26/2017.
 */
public class CommonInputs {
    private final String action;
    private final String accessToken;
    private final String api;
    private final String endpoint;
    private final String version;

    private CommonInputs(Builder builder) {
        this.action = builder.action;
        this.accessToken = builder.accessToken;
        this.api = builder.api;
        this.endpoint = builder.endpoint;
        this.version = builder.version;
    }

    public String getAction() {
        return action;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getApi() {
        return api;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getVersion() {
        return version;
    }

    public static class Builder {
        private String action;
        private String accessToken;
        private String api;
        private String endpoint;
        private String version;

        public CommonInputs build() {
            return new CommonInputs(this);
        }

        public Builder withAction(String inputValue) {
            action = inputValue;
            return this;
        }

        public Builder withAccessToken(String input) {
            accessToken = input;
            return this;
        }

        public Builder withApi(String inputValue) {
            api = inputValue;
            return this;
        }

        public Builder withEndpoint(String input) {
            endpoint = input;
            return this;
        }

        public Builder withVersion(String input) {
            version = ApiVersion.getValue(input);
            return this;
        }
    }
}