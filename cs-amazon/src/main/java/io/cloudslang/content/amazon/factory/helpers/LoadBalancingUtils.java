package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.AVAILABILITY_ZONES;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ZONE_NAMES_STRING;

/**
 * Created by TusaM
 * 11/10/2016.
 */
public class LoadBalancingUtils {
    public Map<String, String> getCreateLoadBalancerQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());

        String[] zonesArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getCustomInputs().getAvailabilityZonesString(),
                ZONE_NAMES_STRING, wrapper.getCommonInputs().getDelimiter());
        setSpecificQueryParamsMap(queryParamsMap, zonesArray, AVAILABILITY_ZONES);


        return queryParamsMap;
    }

    private void setSpecificQueryParamsMap(Map<String, String> queryParamsMap, String[] inputArray, String specificString) {
        if (inputArray != null && inputArray.length > START_INDEX) {
            for (int index = START_INDEX; index < inputArray.length; index++) {
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(specificString, index), inputArray[index]);
            }
        }
    }
}