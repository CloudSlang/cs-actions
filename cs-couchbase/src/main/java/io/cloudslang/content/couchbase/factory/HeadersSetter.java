/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.factory;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_ALL_BUCKETS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_BUCKET_STATISTICS;

/**
 * Created by TusaM
 * 4/12/2017.
 */
public class HeadersSetter {
    private static final String APPLICATION_JSON = "application/json";
    private static final String X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1 = "X-memcachekv-Store-Client-Specification-Version:0.1";

    private HeadersSetter() {
        // prevent instantiation
    }

    public static void setHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case GET_ALL_BUCKETS:
                setBucketCommonHeaders(wrapper);
                break;
            case GET_BUCKET:
                setBucketCommonHeaders(wrapper);
                break;
            case GET_BUCKET_STATISTICS:
                setBucketCommonHeaders(wrapper);
                break;
            default:
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON);
        }
    }

    private static void setBucketCommonHeaders(InputsWrapper wrapper) {
        wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON);
        wrapper.getHttpClientInputs().setHeaders(X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1);
    }
}