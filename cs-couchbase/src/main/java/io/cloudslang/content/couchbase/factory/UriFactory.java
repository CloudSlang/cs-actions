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

import io.cloudslang.content.couchbase.entities.couchbase.CouchbaseApi;
import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.BUCKETS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ErrorMessages.UNSUPPORTED_COUCHBASE_API;
import static io.cloudslang.content.couchbase.factory.buckets.BucketsUriFactory.getBucketsUri;
import static io.cloudslang.content.couchbase.utils.InputsUtil.appendTo;

/**
 * Created by Mihai Tusa
 * 4/5/2017.
 */
public class UriFactory {
    private UriFactory() {
        // prevent instantiation
    }

    public static String getUri(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getApi()) {
            case BUCKETS:
                return appendTo(CouchbaseApi.BUCKETS.getValue(), getBucketsUri(wrapper), wrapper.getCommonInputs().getAction());
            default:
                throw new RuntimeException(UNSUPPORTED_COUCHBASE_API);
        }
    }
}