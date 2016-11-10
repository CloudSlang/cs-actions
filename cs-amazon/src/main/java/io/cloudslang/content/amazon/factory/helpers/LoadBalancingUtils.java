package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.AVAILABILITY_ZONES;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ZONE_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.LoadBalancingInputs.LISTENER_INSTANCE_PORTS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.LoadBalancingInputs.LISTENER_INSTANCE_PROTOCOLS_STRING;

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
        String[] listenerInstancePortsArray = InputsUtil.getStringsArray(wrapper.getLoadBalancerInputs().getListenerInstancePortsString(),
                LISTENER_INSTANCE_PORTS_STRING, wrapper.getCommonInputs().getDelimiter());
        String[] listenerInstanceProtocolsArray = InputsUtil.getStringsArray(wrapper.getLoadBalancerInputs().getListenerInstanceProtocolsString(),
                LISTENER_INSTANCE_PROTOCOLS_STRING, wrapper.getCommonInputs().getDelimiter());

        InputsUtil.validateAgainstDifferentArraysLength(zonesArray, listenerInstancePortsArray, ZONE_NAMES_STRING, LISTENER_INSTANCE_PORTS_STRING);
        InputsUtil.validateAgainstDifferentArraysLength(zonesArray, listenerInstanceProtocolsArray, ZONE_NAMES_STRING, LISTENER_INSTANCE_PROTOCOLS_STRING);

        if (zonesArray != null && zonesArray.length > START_INDEX) {
            for (int index = START_INDEX; index < zonesArray.length; index++) {
                queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(AVAILABILITY_ZONES, index), zonesArray[index]);
            }
        }

        return queryParamsMap;
    }
}