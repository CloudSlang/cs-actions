package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.aws.Scheme;
import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.KEY;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.MEMBER;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SECURITY_GROUPS;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SUBNETS;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VALUE;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KEY_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.SECURITY_GROUP_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.SUBNET_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VALUE_TAGS_STRING;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public class LoadBalancingUtils {
    private static final String LOAD_BALANCER_ARN = "LoadBalancerArn";
    private static final String NAME = "Name";
    private static final String REGEX = "^([\\p{L}\\p{Z}\\p{N}_.:/=+\\-@]*)$";
    private static final String SCHEME = "Scheme";
    private static final String TAGS = "Tags";

    private static final int KEY_TAG_LENGTH_CONSTRAIN = 128;
    private static final int VALUE_TAG_LENGTH_CONSTRAIN = 256;

    public Map<String, String> getCreateLoadBalancerQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(NAME, wrapper.getLoadBalancerInputs().getLoadBalancerName());

        InputsUtil.setOptionalMapEntry(queryParamsMap, SCHEME, wrapper.getLoadBalancerInputs().getScheme(),
                Scheme.INTERNAL.name().equalsIgnoreCase(wrapper.getLoadBalancerInputs().getScheme()));

        setSubnetIdQueryParams(queryParamsMap, wrapper);
        setSecurityGroupQueryParams(queryParamsMap, wrapper);
        setKeyAndValueTag(queryParamsMap, wrapper);

        return queryParamsMap;
    }

    public Map<String, String> getDeleteLoadBalancerQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(LOAD_BALANCER_ARN, wrapper.getLoadBalancerInputs().getLoadBalancerArn());

        return queryParamsMap;
    }

    private void setSubnetIdQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        String[] subnetIdsArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getNetworkInputs().getSubnetIdsString(),
                SUBNET_IDS_STRING, wrapper.getCommonInputs().getDelimiter());
        for (int index = START_INDEX; index < subnetIdsArray.length; index++) {
            queryParamsMap.put(SUBNETS + DOT + MEMBER + DOT + String.valueOf(index + ONE), subnetIdsArray[index]);
        }
    }

    private void setSecurityGroupQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        String[] securityGroupsArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getIamInputs().getSecurityGroupIdsString(),
                SECURITY_GROUP_IDS_STRING, wrapper.getCommonInputs().getDelimiter());
        if (isNotEmpty(securityGroupsArray)) {
            for (int index = START_INDEX; index < securityGroupsArray.length; index++) {
                queryParamsMap.put(SECURITY_GROUPS + DOT + MEMBER + DOT + String.valueOf(index + ONE), securityGroupsArray[index]);
            }
        }
    }

    private void setKeyAndValueTag(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        String[] keyTagsArray = InputsUtil.getStringsArray(wrapper.getCustomInputs().getKeyTagsString(), EMPTY,
                wrapper.getCommonInputs().getDelimiter());
        String[] valueTagsArray = InputsUtil.getStringsArray(wrapper.getCustomInputs().getValueTagsString(), EMPTY,
                wrapper.getCommonInputs().getDelimiter());
        if (isNotEmpty(keyTagsArray) && isNotEmpty(valueTagsArray)) {
            InputsUtil.validateAgainstDifferentArraysLength(keyTagsArray, valueTagsArray, KEY_TAGS_STRING, VALUE_TAGS_STRING);
            for (int index = START_INDEX; index < keyTagsArray.length; index++) {
                String currentTag = InputsUtil.getValidKeyOrValueTag(keyTagsArray[index], REGEX, true,
                        isBlank(keyTagsArray[index]), true, KEY_TAG_LENGTH_CONSTRAIN, VALUE_TAG_LENGTH_CONSTRAIN);
                queryParamsMap.put(TAGS + DOT + MEMBER + DOT + String.valueOf(index + ONE) + DOT + KEY, currentTag);

                String currentValue = InputsUtil.getValidKeyOrValueTag(valueTagsArray[index], REGEX, false,
                        false, true, KEY_TAG_LENGTH_CONSTRAIN, VALUE_TAG_LENGTH_CONSTRAIN);
                queryParamsMap.put(TAGS + DOT + MEMBER + DOT + String.valueOf(index + ONE) + DOT + VALUE, currentValue);
            }
        }
    }
}