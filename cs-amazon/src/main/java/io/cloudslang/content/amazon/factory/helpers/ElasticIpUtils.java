/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.ALLOCATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.PUBLIC_IP;

/**
 * Created by Mihai Tusa.
 * 9/14/2016.
 */
public class ElasticIpUtils {
    private static final String DOMAIN = "Domain";

    public Map<String, String> getAllocateAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(DOMAIN, wrapper.getCustomInputs().getDomain());

        return queryParamsMap;
    }

    public Map<String, String> getReleaseAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());

        InputsUtil.setOptionalMapEntry(queryParamsMap, ALLOCATION_ID, wrapper.getCustomInputs().getAllocationId(),
                isNotBlank(wrapper.getCustomInputs().getAllocationId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PUBLIC_IP, wrapper.getElasticIpInputs().getPublicIp(),
                isNotBlank(wrapper.getElasticIpInputs().getPublicIp()));

        return queryParamsMap;
    }
}
