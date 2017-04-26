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
        public static final String BUCKET_TYPE = "bucketType";
        public static final String CONFLICT_RESOLUTION_TYPE = "conflictResolutionType";
        public static final String COUCHBASE_PROXY_PORT = "couchbaseProxyPort";
        public static final String EVICTION_POLICY = "evictionPolicy";
        public static final String FLUSH_ENABLED = "flushEnabled";
        public static final String PARALLEL_DB_VIEW_COMPACTION = "parallelDBAndViewCompaction";
        public static final String RAM_QUOTA_DB = "ramQuotaMB";
        public static final String REPLICA_INDEX = "replicaIndex";
        public static final String REPLICA_NUMBER = "replicaNumber";
        public static final String SASL_PASSWORD = "saslPassword";
        public static final String THREADS_NUMBER = "threadsNumber";
    }
}