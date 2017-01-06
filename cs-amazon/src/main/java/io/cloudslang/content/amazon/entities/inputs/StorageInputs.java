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
    private final String encodingType;
    private final String maxKeys;

    public String getBucketName() {
        return bucketName;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public String getMaxKeys() {
        return maxKeys;
    }

    private StorageInputs(StorageInputs.Builder builder) {
        this.bucketName = builder.bucketName;
        this.encodingType = builder.encodingType;
        this.maxKeys = builder.maxKeys;
    }

    public static class Builder {
        private String bucketName;
        private String encodingType;
        private String maxKeys;

        public StorageInputs build() {
            return new StorageInputs(this);
        }

        public StorageInputs.Builder withBucketName(String inputValue) {
            bucketName = inputValue;
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
    }
}