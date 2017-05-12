/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.factory.views;

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;

import static io.cloudslang.content.couchbase.entities.constants.Constants.HttpClientInputsValues.APPLICATION_JSON;

/**
 * Created by TusaM
 * 4/28/2017.
 */
public class ViewHeadersBuilder {
    private ViewHeadersBuilder() {
        // prevent instantiation
    }

    public static void setViewHeaders(InputsWrapper wrapper) {
        switch (wrapper.getCommonInputs().getAction()) {
            default:
                wrapper.getHttpClientInputs().setContentType(APPLICATION_JSON);
        }
    }
}