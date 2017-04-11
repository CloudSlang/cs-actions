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
        public static final String GET_ALL_BUCKETS = "GetAllBuckets";
        public static final String GET_BUCKET_STATISTICS = "GetBucketStatistics";
    }

    public static class Miscellaneous {
        public static final String SLASH = "/";
    }

    public static class Values {
        public static final int START_INDEX = 0;
    }
}