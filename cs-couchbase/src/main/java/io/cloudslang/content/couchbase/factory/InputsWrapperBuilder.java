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

import io.cloudslang.content.couchbase.entities.inputs.BucketInputs;
import io.cloudslang.content.couchbase.entities.inputs.CommonInputs;
import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;
import io.cloudslang.content.httpclient.HttpClientInputs;

import static io.cloudslang.content.couchbase.entities.constants.Constants.Values.INIT_INDEX;

/**
 * Created by Mihai Tusa
 * 4/9/2017.
 */
public class InputsWrapperBuilder {
    private static final String UNKNOWN_BUILDER_TYPE = "Unknown builder type.";

    private InputsWrapperBuilder() {
        // prevent instantiation
    }

    @SafeVarargs
    public static <T> InputsWrapper buildWrapper(HttpClientInputs httpClientInputs, T... builders) {
        InputsWrapper wrapper = new InputsWrapper.Builder().build();
        wrapper.setHttpClientInputs(httpClientInputs);

        if (builders.length > INIT_INDEX) {
            for (T builder : builders) {
                if (builder instanceof CommonInputs) {
                    wrapper.setCommonInputs((CommonInputs) builder);
                } else if (builder instanceof BucketInputs) {
                    wrapper.setBucketInputs((BucketInputs) builder);
                } else {
                    throw new RuntimeException(UNKNOWN_BUILDER_TYPE);
                }
            }
        }

        return wrapper;
    }
}