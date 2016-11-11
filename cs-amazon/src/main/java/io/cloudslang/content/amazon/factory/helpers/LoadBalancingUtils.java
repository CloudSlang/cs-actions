package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.aws.Scheme;
import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
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
    private static final String NAME = "Name";
    private static final String SCHEME = "Scheme";
    private static final String TAGS = "Tags";

    public Map<String, String> getCreateLoadBalancerQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(NAME, wrapper.getLoadBalancerInputs().getLoadBalancerName());

        InputsUtil.setOptionalMapEntry(queryParamsMap, SCHEME, wrapper.getLoadBalancerInputs().getScheme(),
                Scheme.INTERNAL.name().equalsIgnoreCase(wrapper.getLoadBalancerInputs().getScheme()));

        setLoadBalancerSpecificQueryParams(wrapper, queryParamsMap);

        return queryParamsMap;
    }

    private void setLoadBalancerSpecificQueryParams(InputsWrapper wrapper, Map<String, String> queryParamsMap) {
        String[] subnetIdsArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getNetworkInputs().getSubnetIdsString(),
                SUBNET_IDS_STRING, wrapper.getCommonInputs().getDelimiter());
        String[] securityGroupsArray = InputsUtil.getValidStringArray(subnetIdsArray, wrapper.getIamInputs().getSecurityGroupIdsString(),
                EMPTY, wrapper.getCommonInputs().getDelimiter(), SUBNET_IDS_STRING, SECURITY_GROUP_IDS_STRING);

        for (int index = START_INDEX; index < subnetIdsArray.length; index++) {
            queryParamsMap.put(SUBNETS + DOT + MEMBER + DOT + String.valueOf(index + ONE), subnetIdsArray[index]);
            InputsUtil.setOptionalMapEntry(queryParamsMap, SECURITY_GROUPS + DOT + MEMBER + DOT + String.valueOf(index + ONE),
                    securityGroupsArray[index], securityGroupsArray.length > START_INDEX);
            setKeyAndValueTag(queryParamsMap, wrapper, subnetIdsArray, index);
        }
    }

    private void setKeyAndValueTag(Map<String, String> queryParamsMap, InputsWrapper wrapper, String[] referenceArray, int index) {
        String[] keyTagsArray = InputsUtil.getValidStringArray(referenceArray, wrapper.getCustomInputs().getKeyTagsString(),
                EMPTY, wrapper.getCommonInputs().getDelimiter(), SUBNET_IDS_STRING, KEY_TAGS_STRING);
        String[] valueTagsArray = InputsUtil.getValidStringArray(referenceArray, wrapper.getCustomInputs().getValueTagsString(),
                EMPTY, wrapper.getCommonInputs().getDelimiter(), SUBNET_IDS_STRING, VALUE_TAGS_STRING);
        InputsUtil.validateKeyOrValueString(keyTagsArray[index], true);
        InputsUtil.validateKeyOrValueString(valueTagsArray[index], false);
        queryParamsMap.put(TAGS + DOT + MEMBER + DOT + String.valueOf(index + ONE) + DOT + KEY, keyTagsArray[index]);
        queryParamsMap.put(TAGS + DOT + MEMBER + DOT + String.valueOf(index + ONE) + DOT + VALUE, valueTagsArray[index]);
    }
}