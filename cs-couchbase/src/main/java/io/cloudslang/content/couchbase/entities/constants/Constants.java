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
public class Constants {
    private Constants() {
        // prevent instantiation
    }

    public static class Api {
        public static final String BUCKETS = "buckets";
    }

    public static class BucketActions {
        public static final String CREATE_OR_EDIT_BUCKET = "CreateOrEditBucket";
        public static final String DELETE_BUCKET = "DeleteBucket";
        public static final String GET_ALL_BUCKETS = "GetAllBuckets";
        public static final String GET_BUCKET = "GetBucket";
        public static final String GET_BUCKET_STATISTICS = "GetBucketStatistics";
    }

    public static class ErrorMessages {
        public static final String CONSTRAINS_ERROR_MESSAGE = "doesn't meet conditions for general purpose usage. " +
                "See operation inputs description section for details.";
    }

    public static class Miscellaneous {
        public static final String AMPERSAND = "&";
        public static final String EQUAL = "=";
    }

    public static class Values {
        public static final int INIT_INDEX = 0;
        public static final int COUCHBASE_DEFAULT_PROXY_PORT = 11215;
        public static final int DEFAULT_REPLICA_NUMBER = 1;
        public static final int DEFAULT_THREADS_NUMBER = 2;
        public static final int MAXIMUM_REPLICA_NUMBER = 3;
        public static final int MAXIMUM_THREADS_NUMBER = 8;
        public static final int MINIMUM_RAM_QUOTA_AMOUNT = 100;
        public static final int MINIMUM_THREADS_NUMBER = 2;
    }
}