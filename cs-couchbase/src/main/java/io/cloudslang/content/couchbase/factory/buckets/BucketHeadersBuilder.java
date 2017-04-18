/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.factory.buckets;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.CREATE_OR_EDIT_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.DELETE_BUCKET;

/**
 * Created by TusaM
 * 4/12/2017.
 */
public class BucketHeadersBuilder {
    private static final String APPLICATION_JSON = "application/json";
    private static final String CREATE_BUCKET_HEADER = "Accept:application/json, text/plain, */*";
    private static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded; charset=UTF-8";
    private static final String X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1 = "X-memcachekv-Store-Client-Specification-Version:0.1";

    private BucketHeadersBuilder() {
        // prevent instantiation
    }

    public static void setBucketHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case CREATE_OR_EDIT_BUCKET:
                wrapper.getHttpClientInputs().setHeaders(CREATE_BUCKET_HEADER);
                wrapper.getHttpClientInputs().setContentType(FORM_URL_ENCODED);
                break;
            case DELETE_BUCKET:
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON);
                break;
            default:
                wrapper.getHttpClientInputs().setHeaders(X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1);
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON);
        }
    }
}