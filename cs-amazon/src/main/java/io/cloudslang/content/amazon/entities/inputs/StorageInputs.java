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

/**
 * Created by TusaM
 * 12/21/2016.
 */
public class StorageInputs {
    private final String bucketName;
    private final String continuationToken;
    private final String encodingType;
    private final String maxKeys;
    private final String prefix;

    public String getBucketName() {
        return bucketName;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public String getMaxKeys() {
        return maxKeys;
    }

    public String getPrefix() {
        return prefix;
    }

    private StorageInputs(StorageInputs.Builder builder) {
        this.bucketName = builder.bucketName;
        this.continuationToken = builder.continuationToken;
        this.encodingType = builder.encodingType;
        this.maxKeys = builder.maxKeys;
        this.prefix = builder.prefix;
    }

    public static class Builder {
        private String bucketName;
        private String continuationToken;
        private String encodingType;
        private String maxKeys;
        private String prefix;

        public StorageInputs build() {
            return new StorageInputs(this);
        }

        public StorageInputs.Builder withBucketName(String inputValue) {
            bucketName = inputValue;
            return this;
        }

        public StorageInputs.Builder withContinuationToken(String inputValue) {
            continuationToken = inputValue;
            return this;
        }

        public StorageInputs.Builder withEncodingType(String inputValue) {
            encodingType = inputValue;
            return this;
        }

        public StorageInputs.Builder withMaxKeys(String inputValue) {
            maxKeys = inputValue;
            return this;
        }

        public StorageInputs.Builder withPrefix(String inputValue) {
            prefix = inputValue;
            return this;
        }
    }
}