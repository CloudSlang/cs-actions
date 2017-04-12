/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.entities.constants;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */
public class Inputs {
    private Inputs() {
        // prevent instantiation
    }

    public static class CommonInputs {
        public static final String ENDPOINT = "endpoint";
    }

    public static class BucketInputs {
        public static final String BUCKET_NAME = "bucketName";
    }
}