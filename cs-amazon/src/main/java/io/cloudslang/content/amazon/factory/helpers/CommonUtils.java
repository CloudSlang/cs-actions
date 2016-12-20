/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.Map;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;

import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;

/**
 * Created by Mihai Tusa.
 * 9/23/2016.
 */
class CommonUtils {
    void setPrefixedAndSuffixedCommonQueryParams(Map<String, String> queryParamsMap, String inputString, String prefix,
                              String suffix, String delimiter) {
        if (isNotBlank(inputString)) {
            String[] securityGroupsRelatedArray = InputsUtil.getStringsArray(inputString, EMPTY, delimiter);
            if (isNotEmpty(securityGroupsRelatedArray)) {
                for (int index = START_INDEX; index < securityGroupsRelatedArray.length; index++) {
                    queryParamsMap.put(prefix + DOT + String.valueOf(index + ONE) + suffix, securityGroupsRelatedArray[index]);
                }
            }
        }
    }
}