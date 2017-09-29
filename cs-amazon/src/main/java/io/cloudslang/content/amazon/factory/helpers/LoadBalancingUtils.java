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

import io.cloudslang.content.amazon.entities.aws.Scheme;
import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.utils.InputsUtil.getStringsArray;
import static io.cloudslang.content.amazon.utils.InputsUtil.getValidDelimiter;
import static io.cloudslang.content.amazon.utils.InputsUtil.getValidKeyOrValueTag;
import static io.cloudslang.content.amazon.utils.InputsUtil.getValidPageSizeInt;
import static io.cloudslang.content.amazon.utils.InputsUtil.setCommonQueryParamsMap;
import static io.cloudslang.content.amazon.utils.InputsUtil.setOptionalMapEntry;
import static io.cloudslang.content.amazon.utils.InputsUtil.setTypicalQueryParams;
import static io.cloudslang.content.amazon.utils.InputsUtil.validateAgainstDifferentArraysLength;

import static java.lang.String.valueOf;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.FIXED_PREFIX;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.KEY;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NAME;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SECURITY_GROUPS;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SUBNETS;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VALUE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KEY_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VALUE_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.LoadBalancerInputs.ARNS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.LoadBalancerInputs.MEMBER_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.SECURITY_GROUP_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.SUBNET_IDS_STRING;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public class LoadBalancingUtils {
    private static final String LOAD_BALANCER_ARN = "LoadBalancerArn";
    private static final String LOAD_BALANCER_ARNS = "LoadBalancerArns";
    private static final String DELIMITER_REGEX = "[^:\\-\\/]";
    private static final String MARKER = "Marker";
    private static final String NAMES = "Names";
    private static final String PAGE_SIZE = "PageSize";
    private static final String SCHEME = "Scheme";
    private static final String TAG_REGEX = "^([\\p{L}\\p{Z}\\p{N}_.:/=+\\-@]*)$";
    private static final String TAGS = "Tags";

    private static final int KEY_TAG_LENGTH_CONSTRAIN = 128;
    private static final int VALUE_TAG_LENGTH_CONSTRAIN = 256;

    public Map<String, String> getCreateLoadBalancerQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        queryParamsMap.put(NAME, wrapper.getLoadBalancerInputs().getLoadBalancerName());

        setOptionalMapEntry(queryParamsMap, SCHEME, wrapper.getLoadBalancerInputs().getScheme(),
                Scheme.INTERNAL.name().equalsIgnoreCase(wrapper.getLoadBalancerInputs().getScheme()));

        setTypicalQueryParams(queryParamsMap, wrapper.getNetworkInputs().getSubnetIdsString(), SUBNET_IDS_STRING,
                SUBNETS + FIXED_PREFIX, wrapper.getCommonInputs().getDelimiter());
        setTypicalQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(), SECURITY_GROUP_IDS_STRING,
                SECURITY_GROUPS + FIXED_PREFIX, wrapper.getCommonInputs().getDelimiter());

        setKeyAndValueTag(queryParamsMap, wrapper);

        return queryParamsMap;
    }

    public Map<String, String> getDeleteLoadBalancerQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(LOAD_BALANCER_ARN, wrapper.getLoadBalancerInputs().getLoadBalancerArn());

        return queryParamsMap;
    }

    public Map<String, String> getDescribeLoadBalancersQueryParamsMap(InputsWrapper wrapper) {
        String delimiter = getValidDelimiter(wrapper.getCommonInputs().getDelimiter(), DELIMITER_REGEX);
        int pageSize = getValidPageSizeInt(wrapper.getLoadBalancerInputs().getPageSize());

        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        setTypicalQueryParams(queryParamsMap, wrapper.getLoadBalancerInputs().getArnsString(), ARNS_STRING,
                LOAD_BALANCER_ARNS + FIXED_PREFIX, delimiter);
        setTypicalQueryParams(queryParamsMap, wrapper.getLoadBalancerInputs().getMemberNamesString(), MEMBER_NAMES_STRING,
                NAMES + FIXED_PREFIX, delimiter);

        setOptionalMapEntry(queryParamsMap, MARKER, wrapper.getLoadBalancerInputs().getMarker(),
                isNotBlank(wrapper.getLoadBalancerInputs().getMarker()));
        setOptionalMapEntry(queryParamsMap, PAGE_SIZE, valueOf(pageSize), pageSize > ONE);

        return queryParamsMap;
    }

    private void setKeyAndValueTag(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        String[] keyTagsArray = getStringsArray(wrapper.getCustomInputs().getKeyTagsString(), EMPTY,
                wrapper.getCommonInputs().getDelimiter());
        String[] valueTagsArray = getStringsArray(wrapper.getCustomInputs().getValueTagsString(), EMPTY,
                wrapper.getCommonInputs().getDelimiter());
        if (isNotEmpty(keyTagsArray) && isNotEmpty(valueTagsArray)) {
            validateAgainstDifferentArraysLength(keyTagsArray, valueTagsArray, KEY_TAGS_STRING, VALUE_TAGS_STRING);
            for (int index = START_INDEX; index < keyTagsArray.length; index++) {
                String currentTag = getValidKeyOrValueTag(keyTagsArray[index], TAG_REGEX, true, isBlank(keyTagsArray[index]), true,
                        KEY_TAG_LENGTH_CONSTRAIN, VALUE_TAG_LENGTH_CONSTRAIN);
                queryParamsMap.put(TAGS + FIXED_PREFIX + valueOf(index + ONE) + DOT + KEY, currentTag);

                String currentValue = getValidKeyOrValueTag(valueTagsArray[index], TAG_REGEX, false, false, true,
                        KEY_TAG_LENGTH_CONSTRAIN, VALUE_TAG_LENGTH_CONSTRAIN);
                queryParamsMap.put(TAGS + FIXED_PREFIX + valueOf(index + ONE) + DOT + VALUE, currentValue);
            }
        }
    }
}
