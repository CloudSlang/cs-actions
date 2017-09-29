/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.amazon.utils.InputsUtil.setOptionalMapEntry;
import static io.cloudslang.content.amazon.utils.InputsUtil.getS3HostHeaderValue;

import static io.cloudslang.content.amazon.entities.constants.Constants.Values.DEFAULT_MAX_KEYS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DELIMITER;

/**
 * Created by TusaM
 * 12/23/2016.
 */
public class StorageUtils {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTINUATION_TOKEN = "continuation-token";
    private static final String ENCODING_TYPE = "encoding-type";
    private static final String FETCH_OWNER = "fetch-owner";
    private static final String HOST = "Host";
    private static final String LIST_TYPE_KEY = "list-type";
    private static final String LIST_TYPE_VALUE = "2";
    private static final String MAX_KEYS = "max-keys";
    private static final String PREFIX = "prefix";
    private static final String START_AFTER = "start-after";
    private static final String TEXT_PLAIN = "text/plain";

    public void setS3ApiHeadersMap(Map<String, String> headersMap, InputsWrapper wrapper) {
        headersMap.put(CONTENT_TYPE, TEXT_PLAIN);
        headersMap.put(HOST, getS3HostHeaderValue(wrapper.getCommonInputs().getApiService(), wrapper.getStorageInputs().getBucketName()));
    }

    public Map<String, String> retrieveGetBucketQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(LIST_TYPE_KEY, LIST_TYPE_VALUE);
        setOptionalMapEntry(queryParamsMap, CONTINUATION_TOKEN, wrapper.getStorageInputs().getContinuationToken(), isNotBlank(wrapper.getStorageInputs().getContinuationToken()));
        setOptionalMapEntry(queryParamsMap, DELIMITER, wrapper.getCommonInputs().getDelimiter(), isNotBlank(wrapper.getCommonInputs().getDelimiter()));
        setOptionalMapEntry(queryParamsMap, ENCODING_TYPE, wrapper.getStorageInputs().getEncodingType(), isNotBlank(wrapper.getStorageInputs().getEncodingType()));
        setOptionalMapEntry(queryParamsMap, FETCH_OWNER, valueOf(wrapper.getStorageInputs().isFetchOwner()), wrapper.getStorageInputs().isFetchOwner());
        setOptionalMapEntry(queryParamsMap, MAX_KEYS, valueOf(wrapper.getStorageInputs().getMaxKeys()), wrapper.getStorageInputs().getMaxKeys() != DEFAULT_MAX_KEYS);
        setOptionalMapEntry(queryParamsMap, PREFIX, wrapper.getStorageInputs().getPrefix(), isNotBlank(wrapper.getStorageInputs().getPrefix()));
        setOptionalMapEntry(queryParamsMap, START_AFTER, wrapper.getStorageInputs().getStartAfter(), isNotBlank(wrapper.getStorageInputs().getStartAfter()));

        return queryParamsMap;
    }
}