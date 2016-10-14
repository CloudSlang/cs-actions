package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.NETWORK_INTERFACE;
import static io.cloudslang.content.jclouds.entities.constants.Constants.AwsParams.SECURITY_GROUP_ID;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.EMPTY;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Values.ONE;

/**
 * Created by Mihai Tusa.
 * 9/23/2016.
 */
class IamUtils {
    void setSecurityGroupQueryParams(Map<String, String> queryParamsMap, String inputString, String prefix,
                                     String suffix, String delimiter) {
        if (StringUtils.isNotBlank(inputString)) {
            String[] securityGroupsArray = InputsUtil.getStringsArray(inputString, EMPTY, delimiter);
            if (securityGroupsArray != null && securityGroupsArray.length > START_INDEX) {
                for (int index = START_INDEX; index < securityGroupsArray.length; index++) {
                    queryParamsMap.put(prefix + DOT + String.valueOf(index + ONE) + suffix, securityGroupsArray[index]);
                }
            }
        }
    }

    void setNetworkSecurityGroupsQueryParams(Map<String, String> queryParamsMap, String inputIdsString, String delimiter) {
        if (StringUtils.isNotBlank(inputIdsString)) {
            String[] networkInterfaceSecurityGroupIdsArray = InputsUtil.getStringsArray(inputIdsString, EMPTY, delimiter);
            if (networkInterfaceSecurityGroupIdsArray != null && networkInterfaceSecurityGroupIdsArray.length > START_INDEX) {
                for (int index = START_INDEX; index < networkInterfaceSecurityGroupIdsArray.length; index++) {
                    queryParamsMap.put(NETWORK_INTERFACE + DOT + String.valueOf(index + ONE) + DOT + SECURITY_GROUP_ID,
                            networkInterfaceSecurityGroupIdsArray[index]);
                }
            }
        }
    }
}