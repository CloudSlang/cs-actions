/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.factory.nodes;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.ALL_TYPE_HEADER;
import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.FORM_URL_ENCODED;
import static io.cloudslang.content.couchbase.entities.constants.Constants.NodeActions.FAIL_OVER_NODE;

/**
 * Created by TusaM
 * 5/9/2017.
 */
public class NodeHeadersBuilder {
    private NodeHeadersBuilder() {
        // prevent instantiation
    }

    public static void setNodeHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            default:
                wrapper.getHttpClientInputs().setHeaders(ALL_TYPE_HEADER);
                wrapper.getHttpClientInputs().setContentType(FORM_URL_ENCODED);
        }
    }
}