/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.factory.helpers.StorageUtils;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.ErrorMessages.UNSUPPORTED_QUERY_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.S3QueryApiActions.GET_BUCKET;

/**
 * Created by TusaM
 * 12/23/2016.
 */
class S3QueryParamsMapBuilder {
    private S3QueryParamsMapBuilder() {
        // prevent instantiation
    }

    static Map<String, String> getS3QueryParamsMap(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case GET_BUCKET:
                return new StorageUtils().retrieveGetBucketQueryParamsMap(wrapper);
            default:
                throw new RuntimeException(UNSUPPORTED_QUERY_API);
        }
    }
}