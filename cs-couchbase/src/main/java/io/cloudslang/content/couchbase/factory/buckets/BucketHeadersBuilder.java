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
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.APPLICATION_JSON;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.ALL_TYPE_HEADER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.FORM_URL_ENCODED;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1;

/**
 * Created by TusaM
 * 4/12/2017.
 */
public class BucketHeadersBuilder {
    private BucketHeadersBuilder() {
        // prevent instantiation
    }

    public static void setBucketHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case CREATE_OR_EDIT_BUCKET:
                wrapper.getHttpClientInputs().setHeaders(ALL_TYPE_HEADER);
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