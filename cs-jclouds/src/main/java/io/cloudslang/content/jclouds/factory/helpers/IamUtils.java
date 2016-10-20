package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.utils.InputsUtil;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.EMPTY;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Values.ONE;

/**
 * Created by Mihai Tusa.
 * 9/23/2016.
 */
class IamUtils {
    void setSecurityGroupsRelatedQueryParams(Map<String, String> queryParamsMap, String inputString, String prefix,
                                             String suffix, String delimiter) {
        if (isNotBlank(inputString)) {
            String[] securityGroupsRelatedArray = InputsUtil.getStringsArray(inputString, EMPTY, delimiter);
            if (securityGroupsRelatedArray != null && securityGroupsRelatedArray.length > START_INDEX) {
                for (int index = START_INDEX; index < securityGroupsRelatedArray.length; index++) {
                    queryParamsMap.put(prefix + DOT + String.valueOf(index + ONE) + suffix, securityGroupsRelatedArray[index]);
                }
            }
        }
    }
}