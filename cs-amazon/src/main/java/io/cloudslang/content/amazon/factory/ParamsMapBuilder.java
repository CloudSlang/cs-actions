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

import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.LOAD_BALANCING_API;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.AMPERSAND;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EQUAL;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public class ParamsMapBuilder {
    private static final String UNSUPPORTED_AWS_API = "Unsupported Amazon AWS API.";

    private ParamsMapBuilder() {
        // prevent instantiation
    }

    public static Map<String, String> getParamsMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> queryParamsMap;
        if (isBlank(wrapper.getCommonInputs().getQueryParams())) {
            switch (wrapper.getCommonInputs().getApiService()) {
                case EC2_API:
                    return Ec2QueryParamsMapBuilder.getEc2QueryParamsMap(wrapper);
                case LOAD_BALANCING_API:
                    return LoadBalancingQueryParamsMapBuilder.getLoadBalancingQueryParamsMap(wrapper);
                default:
                    throw new RuntimeException(UNSUPPORTED_AWS_API);
            }
        } else {
            queryParamsMap = InputsUtil.getHeadersOrQueryParamsMap(new HashMap<String, String>(),
                    wrapper.getCommonInputs().getQueryParams(), AMPERSAND, EQUAL, false);
        }

        return queryParamsMap;
    }
}
