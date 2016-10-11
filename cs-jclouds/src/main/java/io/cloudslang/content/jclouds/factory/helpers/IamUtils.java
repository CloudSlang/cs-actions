package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 9/23/2016.
 */
class IamUtils {
    void setSecurityGroupQueryParams(Map<String, String> queryParamsMap, String inputString, String prefix,
                                     String suffix, String delimiter) {
        if (StringUtils.isNotBlank(inputString)) {
            String[] securityGroupsArray = InputsUtil.getStringsArray(inputString, Constants.Miscellaneous.EMPTY, delimiter);
            if (securityGroupsArray != null && securityGroupsArray.length > Constants.Values.START_INDEX) {
                for (int index = Constants.Values.START_INDEX; index < securityGroupsArray.length; index++) {
                    queryParamsMap.put(prefix + Constants.Miscellaneous.DOT + String.valueOf(index + Constants.Values.ONE) +
                            suffix, securityGroupsArray[index]);
                }
            }
        }
    }

    void setNetworkSecurityGroupsQueryParams(Map<String, String> queryParamsMap, String inputIdsString, String delimiter) {
        if (StringUtils.isNotBlank(inputIdsString)) {
            String[] networkInterfaceSecurityGroupIdsArray = InputsUtil.getStringsArray(inputIdsString,
                    Constants.Miscellaneous.EMPTY, delimiter);
            if (networkInterfaceSecurityGroupIdsArray != null
                    && networkInterfaceSecurityGroupIdsArray.length > Constants.Values.START_INDEX) {
                for (int index = Constants.Values.START_INDEX; index < networkInterfaceSecurityGroupIdsArray.length; index++) {
                    queryParamsMap.put(Constants.AwsParams.NETWORK_INTERFACE + Constants.Miscellaneous.DOT +
                            String.valueOf(index + Constants.Values.ONE) + Constants.Miscellaneous.DOT +
                            Constants.AwsParams.SECURITY_GROUP_ID, networkInterfaceSecurityGroupIdsArray[index]);
                }
            }
        }
    }
}