package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.AwsInputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 9/9/2016.
 */
public class NetworkHelper {
    private static final String ATTACHMENT_ID = "AttachmentId";
    private static final String DEVICE_ID = "DeviceIndex";
    private static final String DOMAIN = "Domain";
    private static final String FORCE = "Force";
    private static final String NETWORK_INTERFACE_ID = "NetworkInterfaceId";

    public Map<String, String> getDeleteNetworkInterfaceQueryParamsMap(AwsInputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());

        return queryParamsMap;
    }

    public Map<String, String> getDetachNetworkInterfaceQueryParamsMap(AwsInputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(ATTACHMENT_ID, wrapper.getCustomInputs().getAttachmentId());

        InputsUtil.setOptionalMapEntry(queryParamsMap, FORCE, Constants.Miscellaneous.SET_FLAG,
                wrapper.getNetworkInputs().isForceDetach());

        return queryParamsMap;
    }

    public Map<String, String> getAttachNetworkInterfaceQueryParamsMap(AwsInputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());
        queryParamsMap.put(DEVICE_ID, wrapper.getNetworkInputs().getDeviceIndex());

        return queryParamsMap;
    }

    public Map<String, String> getAllocateAddressQueryParamsMap(AwsInputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(DOMAIN, wrapper.getCustomInputs().getDomain());

        return queryParamsMap;
    }
}