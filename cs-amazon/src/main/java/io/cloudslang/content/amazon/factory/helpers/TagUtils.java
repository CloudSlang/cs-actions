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
import io.cloudslang.content.amazon.entities.validators.TagFilterValidator;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.KEY;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.RESOURCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VALUE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KEY_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.RESOURCE_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VALUE_TAGS_STRING;
import static io.cloudslang.content.amazon.factory.helpers.FilterUtils.getFiltersQueryMap;
import static io.cloudslang.content.amazon.utils.InputsUtil.getArrayWithoutDuplicateEntries;
import static io.cloudslang.content.amazon.utils.InputsUtil.getQueryParamsSpecificString;
import static io.cloudslang.content.amazon.utils.InputsUtil.getStringsArray;
import static io.cloudslang.content.amazon.utils.InputsUtil.getValidKeyOrValueTag;
import static io.cloudslang.content.amazon.utils.InputsUtil.setCommonQueryParamsMap;
import static io.cloudslang.content.amazon.utils.InputsUtil.setOptionalMapEntry;
import static io.cloudslang.content.amazon.utils.InputsUtil.validateAgainstDifferentArraysLength;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by TusaM
 * 10/12/2016.
 */
public class TagUtils {
    private static final String EXCEPTED_KEY_STRING = "aws:";
    private static final String MORE_THAN_50_TAGS_ERROR_MESSAGE = "The resources cannot be tagged with more than 50 tags!";

    private static final int KEY_TAG_LENGTH_CONSTRAIN = 127;
    private static final int MAXIMUM_TAGS_ALLOWED = 50;
    private static final int VALUE_TAG_LENGTH_CONSTRAIN = 255;
    private static final String NEXT_TOKEN = "NextToken";
    private static final String MAX_RESULTS = "MaxResults";

    public Map<String, String> getCreateTagsQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        String[] resourceIdsArray = getArrayWithoutDuplicateEntries(wrapper.getCustomInputs().getResourceIdsString(),
                RESOURCE_IDS_STRING, wrapper.getCommonInputs().getDelimiter());

        if (isNotEmpty(resourceIdsArray)) {
            for (int index = START_INDEX; index < resourceIdsArray.length; index++) {
                queryParamsMap.put(getQueryParamsSpecificString(RESOURCE_ID, index), resourceIdsArray[index]);
            }
        }
        setResourcesTags(queryParamsMap, wrapper.getCustomInputs().getKeyTagsString(), wrapper.getCustomInputs().getValueTagsString(),
                wrapper.getCommonInputs().getDelimiter());

        return queryParamsMap;
    }

    private void setResourcesTags(Map<String, String> queryParamsMap, String keyTagsString, String valueTagsString, String delimiter) {
        String[] keyTagsStringArray = getStringsArray(keyTagsString, EMPTY, delimiter);
        String[] valueTagsStringArray = getStringsArray(valueTagsString, EMPTY, delimiter);
        validateAgainstDifferentArraysLength(keyTagsStringArray, valueTagsStringArray, KEY_TAGS_STRING, VALUE_TAGS_STRING);

        if (isNotEmpty(keyTagsStringArray) && isNotEmpty(valueTagsStringArray)) {

            if (keyTagsStringArray.length > MAXIMUM_TAGS_ALLOWED) {
                throw new RuntimeException(MORE_THAN_50_TAGS_ERROR_MESSAGE);
            }

            for (int index = START_INDEX; index < keyTagsStringArray.length; index++) {
                String currentKey = getValidKeyOrValueTag(keyTagsStringArray[index], EMPTY, true, keyTagsStringArray[index].startsWith(EXCEPTED_KEY_STRING),
                        false, KEY_TAG_LENGTH_CONSTRAIN, VALUE_TAG_LENGTH_CONSTRAIN);
                queryParamsMap.put(getQueryParamsSpecificString(KEY, index), currentKey);

                String emptyOrRelevant = NOT_RELEVANT.equalsIgnoreCase(valueTagsStringArray[index]) ? EMPTY : valueTagsStringArray[index];
                String currentValue = getValidKeyOrValueTag(emptyOrRelevant, EMPTY, false, false, false,
                        KEY_TAG_LENGTH_CONSTRAIN, VALUE_TAG_LENGTH_CONSTRAIN);
                queryParamsMap.put(getQueryParamsSpecificString(VALUE, index), currentValue);
            }
        }
    }

    @NotNull
    public Map<String, String> getDescribeTagsQueryParamsMap(@NotNull InputsWrapper wrapper) {
        final Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        setOptionalMapEntry(queryParamsMap, MAX_RESULTS, wrapper.getFilterInputs().getMaxResults(),
                !NOT_RELEVANT.equalsIgnoreCase(wrapper.getFilterInputs().getMaxResults()));
        setOptionalMapEntry(queryParamsMap, NEXT_TOKEN, wrapper.getFilterInputs().getNextToken(),
                isNotBlank(wrapper.getFilterInputs().getNextToken()));

        final TagFilterValidator tagFilterValidator = new TagFilterValidator();

        final Map<String, String> filterQueryMap = getFiltersQueryMap(wrapper.getFilterInputs(), tagFilterValidator);
        queryParamsMap.putAll(filterQueryMap);

        return queryParamsMap;
    }
}
