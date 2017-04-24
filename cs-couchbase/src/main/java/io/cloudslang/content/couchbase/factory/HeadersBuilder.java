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

import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.BUCKETS;
import static io.cloudslang.content.couchbase.entities.constants.Constants.Api.CLUSTER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.ErrorMessages.UNKNOWN_COUCHBASE_HEADER;
import static io.cloudslang.content.couchbase.factory.buckets.BucketHeadersBuilder.setBucketHeaders;
import static io.cloudslang.content.couchbase.factory.cluster.ClusterHeadersBuilder.setClusterHeaders;

/**
 * Created by TusaM
 * 4/14/2017.
 */
public class HeadersBuilder {
    private HeadersBuilder() {
        // prevent instantiation
    }

    public static void buildHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getApi()) {
            case BUCKETS:
                setBucketHeaders(wrapper);
                break;
            case CLUSTER:
                setClusterHeaders(wrapper);
                break;
            default:
                throw new RuntimeException(UNKNOWN_COUCHBASE_HEADER);
        }
    }
}