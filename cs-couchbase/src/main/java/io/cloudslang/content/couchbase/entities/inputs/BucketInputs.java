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

/**
 * Created by Mihai Tusa
 * 4/9/2017.
 */
public class BucketInputs {
    private final String bucketName;

    private BucketInputs(BucketInputs.Builder builder) {
        this.bucketName = builder.bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }

    public static class Builder {
        private String bucketName;

        public BucketInputs build() {
            return new BucketInputs(this);
        }

        public BucketInputs.Builder withBucketName(String inputValue) {
            bucketName = inputValue;
            return this;
        }
    }
}