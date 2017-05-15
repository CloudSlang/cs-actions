/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.factory.cluster;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.ClusterActions.REBALANCING_NODES;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.ALL_TYPE_HEADER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.APPLICATION_JSON;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.FORM_URL_ENCODED;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1;

/**
 * Created by TusaM
 * 4/20/2017.
 */
public class ClusterHeadersBuilder {
    private ClusterHeadersBuilder() {
        // prevent instantiation
    }

    public static void setClusterHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            case REBALANCING_NODES:
                wrapper.getHttpClientInputs().setContentType(FORM_URL_ENCODED);
                wrapper.getHttpClientInputs().setHeaders(ALL_TYPE_HEADER);
                break;
            default:
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON);
                wrapper.getHttpClientInputs().setHeaders(X_MEMCACHEKV_STORE_CLIENT_SPECIFICATION_VERSION_0_1);
        }
    }
}