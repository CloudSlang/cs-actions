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

import static io.cloudslang.content.couchbase.entities.constants.Constants.Miscellaneous.COMMA;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getInputWithDefaultValue;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */
public class CommonInputs {
    private final String action;
    private final String api;
    private final String endpoint;
    private final String delimiter;

    private CommonInputs(Builder builder) {
        this.action = builder.action;
        this.api = builder.api;
        this.endpoint = builder.endpoint;
        this.delimiter = builder.delimiter;
    }

    public String getAction() {
        return action;
    }

    public String getApi() {
        return api;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public static class Builder {
        private String action;
        private String api;
        private String endpoint;
        private String delimiter;

        public CommonInputs build() {
            return new CommonInputs(this);
        }

        public Builder withAction(String inputValue) {
            action = inputValue;
            return this;
        }

        public Builder withApi(String inputValue) {
            api = inputValue;
            return this;
        }

        public Builder withEndpoint(String inputValue) {
            endpoint = inputValue;
            return this;
        }

        public Builder withDelimiter(String inputValue) {
            delimiter = getInputWithDefaultValue(inputValue, COMMA);
            return this;
        }
    }
}