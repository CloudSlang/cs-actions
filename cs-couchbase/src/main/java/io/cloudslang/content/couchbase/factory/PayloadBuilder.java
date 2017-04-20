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
import io.cloudslang.content.couchbase.factory.buckets.BucketsHelper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.BucketActions.CREATE_OR_EDIT_BUCKET;

/**
 * Created by TusaM
 * 4/14/2017.
 */
public class PayloadBuilder {
    private PayloadBuilder() {
        // prevent instantiation
    }

    public static void buildPayload(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case CREATE_OR_EDIT_BUCKET:
                String payload = new BucketsHelper().getCreateBucketPayload(wrapper);
                wrapper.getHttpClientInputs().setBody(payload);
                break;
            default:
                break;
        }
    }
}