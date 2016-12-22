/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.factory;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.S3_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HEADER_DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.COLON;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by TusaM
 * 12/21/2016.
 */
public class HeadersMapBuilder {
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String HOST = "Host";
    private static final String TEXT_PLAIN = "text/plain";

    private HeadersMapBuilder() {
        // prevent instantiation
    }

    public static Map<String, String> getHeadersMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> headersMap = isBlank(wrapper.getCommonInputs().getHeaders()) ? new HashMap<String, String>() :
                InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(), wrapper.getCommonInputs().getHeaders(),
                        HEADER_DELIMITER, COLON, true);

        switch (wrapper.getCommonInputs().getApiService()) {
            case S3_API:
                headersMap.put(CONTENT_TYPE, TEXT_PLAIN);
                headersMap.put(HOST, InputsUtil.getS3HostHeaderValue(wrapper.getCommonInputs().getApiService(),
                        wrapper.getStorageInputs().getBucketName()));
                return headersMap;
            default:
                return headersMap;
        }
    }
}