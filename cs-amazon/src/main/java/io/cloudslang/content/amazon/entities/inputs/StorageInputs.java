/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
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

    public String getBucketName() {
        return bucketName;
    }

    private StorageInputs(StorageInputs.Builder builder) {
        this.bucketName = builder.bucketName;
    }

    public static class Builder {
        private String bucketName;

        public StorageInputs build() {
            return new StorageInputs(this);
        }

        public StorageInputs.Builder withBucketName(String inputValue) {
            bucketName = inputValue;
            return this;
        }
    }
}