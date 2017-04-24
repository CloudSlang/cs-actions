/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.execute;

import io.cloudslang.content.couchbase.entities.inputs.CommonInputs;
import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;
import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.HttpClientInputs;

import java.net.MalformedURLException;
import java.util.Map;

import static io.cloudslang.content.couchbase.factory.HeadersBuilder.buildHeaders;
import static io.cloudslang.content.couchbase.factory.InputsWrapperBuilder.buildWrapper;
import static io.cloudslang.content.couchbase.factory.PayloadBuilder.buildPayload;
import static io.cloudslang.content.couchbase.utils.InputsUtil.buildUrl;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */
public class CouchbaseService {
    @SafeVarargs
    public final <T> Map<String, String> execute(HttpClientInputs httpClientInputs, CommonInputs commonInputs, T... builders)
            throws MalformedURLException {
        InputsWrapper wrapper = buildWrapper(httpClientInputs, commonInputs, builders);

        httpClientInputs.setUrl(buildUrl(wrapper));

        buildHeaders(wrapper);
        buildPayload(wrapper);

        return new CSHttpClient().execute(httpClientInputs);
    }
}