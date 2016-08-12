package io.cloudslang.content.jclouds.services.helpers;

import io.cloudslang.content.jclouds.entities.aws.AWSApiAction;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AWSInputsWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 8/12/2016.
 */
public class AWSApiNetworkServiceHelper {
    private static final String NETWORK_INTERFACE_ID = "NetworkInterfaceId";
    private static final String DEVICE_ID = "DeviceIndex";

    public Map<String, String> getAttachNetworkInterfaceQueryParamsMap(AWSInputsWrapper inputs) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AWSParams.ACTION, AWSApiAction.ATTACH_NETWORK_INTERFACE.getValue());
        queryParamsMap.put(Constants.AWSParams.VERSION, inputs.getVersion());
        queryParamsMap.put(NETWORK_INTERFACE_ID, inputs.getNetworkInterfaceId());
        queryParamsMap.put(Constants.AWSParams.INSTANCE_ID, inputs.getCustomInputs().getInstanceId());
        queryParamsMap.put(DEVICE_ID, inputs.getDeviceIndex());

        return queryParamsMap;
    }
}