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

import io.cloudslang.content.couchbase.entities.inputs.InputsWrapper;
import io.cloudslang.content.httpclient.CSHttpClient;
import io.cloudslang.content.httpclient.HttpClientInputs;

import java.net.MalformedURLException;
import java.util.Map;

import static io.cloudslang.content.couchbase.factory.InputsWrapperBuilder.buildWrapper;
import static io.cloudslang.content.couchbase.factory.UriFactory.getUri;
import static io.cloudslang.content.couchbase.utils.InputsUtil.getUrl;

/**
 * Created by Mihai Tusa
 * 3/26/2017.
 */
public class CouchbaseService {
    private static final String APPLICATION_JSON = "application/json";
    private static final String COUCHBASE_HEADER = "X-memcachekv-Store-Client-Specification-Version:0.1";

    @SafeVarargs
    public final <T> Map<String, String> execute(HttpClientInputs httpClientInputs, T... builders) throws MalformedURLException {
        InputsWrapper wrapper = buildWrapper(builders);

        httpClientInputs.setUrl(buildUrl(wrapper));
        httpClientInputs.setContentType(APPLICATION_JSON);
        httpClientInputs.setHeaders(COUCHBASE_HEADER);

        return new CSHttpClient().execute(httpClientInputs);
    }

    private String buildUrl(InputsWrapper wrapper) throws MalformedURLException {
        return getUrl(wrapper.getCommonInputs().getEndpoint()) + getUri(wrapper);
    }
}