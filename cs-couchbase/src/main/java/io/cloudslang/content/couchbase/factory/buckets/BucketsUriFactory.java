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

import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_BUCKET;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.GET_BUCKET_STATISTICS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.DELETE_BUCKET;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by Mihai Tusa
 * 4/8/2017.
 */
public class BucketsUriFactory {
    private BucketsUriFactory() {
        // prevent instantiation
    }

    public static String getBucketsUri(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case GET_BUCKET:
                return wrapper.getBucketInputs().getBucketName();
            case GET_BUCKET_STATISTICS:
                return wrapper.getBucketInputs().getBucketName();
            case DELETE_BUCKET:
                return wrapper.getBucketInputs().getBucketName();
            default:
                return EMPTY;
        }
    }
}