package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InputsWrapper;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 9/9/2016.
 */
public class NetworkHelper {
    private static final String ATTACHMENT_ID = "AttachmentId";
    private static final String DEVICE_ID = "DeviceIndex";
    private static final String DESCRIPTION = "Description";
    private static final String DOMAIN = "Domain";
    private static final String FORCE = "Force";
    private static final String NETWORK_INTERFACE_ID = "NetworkInterfaceId";
    private static final String PRIMARY = "Primary";
    private static final String PRIVATE_IP_ADDRESS = "PrivateIpAddress";
    private static final String PRIVATE_IP_ADDRESSES = "PrivateIpAddresses";
    private static final String SECONDARY_PRIVATE_IP_ADDRESS_COUNT = "SecondaryPrivateIpAddressCount";
    private static final String SECURITY_GROUP_ID = "SecurityGroupId";
    private static final String SUBNET_ID = "SubnetId";

    private static final int INITIAL_INDEX = 0;

    public Map<String, String> getAllocateAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(DOMAIN, wrapper.getCustomInputs().getDomain());

        return queryParamsMap;
    }

    public Map<String, String> getAttachNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());
        queryParamsMap.put(DEVICE_ID, wrapper.getNetworkInputs().getDeviceIndex());

        return queryParamsMap;
    }

    public Map<String, String> getCreateNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(SUBNET_ID, wrapper.getNetworkInputs().getNetworkInterfaceSubnetId());

        InputsUtil.setOptionalMapEntry(queryParamsMap, DESCRIPTION, wrapper.getNetworkInputs().getNetworkInterfaceDescription(),
                StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDescription()));

        if (StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfacePrivateIpAddress())
                && !InputsUtil.isValidIPv4Address(wrapper.getNetworkInputs().getNetworkInterfacePrivateIpAddress())) {
            throw new RuntimeException("The input value: " + wrapper.getNetworkInputs().getNetworkInterfacePrivateIpAddress() +
                    " is not a valid IPv4 address.");
        } else if (StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfacePrivateIpAddress())) {
            queryParamsMap.put(getCommonString(INITIAL_INDEX) + PRIMARY, Boolean.TRUE.toString().toLowerCase());
            queryParamsMap.put(getCommonString(INITIAL_INDEX) + PRIVATE_IP_ADDRESS, wrapper.getNetworkInputs().getNetworkInterfacePrivateIpAddress());
        }

        if (StringUtils.isNotBlank(wrapper.getNetworkInputs().getPrivateIpAddressesString())) {
            String[] privateIpAddressesArray = InputsUtil.getStringsArray(wrapper.getNetworkInputs().getPrivateIpAddressesString(),
                    Constants.Miscellaneous.EMPTY, wrapper.getCommonInputs().getDelimiter());
            if (privateIpAddressesArray != null && privateIpAddressesArray.length > 0) {
                for (int index = 0; index < privateIpAddressesArray.length; index++) {
                    if (!InputsUtil.isValidIPv4Address(privateIpAddressesArray[index])) {
                        throw new RuntimeException("The string: " + privateIpAddressesArray[index] + " is not a valid IPv4 address.");
                    } else if (index == 0 && !queryParamsMap.containsKey(getCommonString(INITIAL_INDEX) + PRIMARY)
                            && !queryParamsMap.containsValue(Boolean.TRUE.toString().toLowerCase())) {
                        queryParamsMap.put(getCommonString(index + Constants.ValidationValues.ONE) + PRIMARY,
                                Boolean.TRUE.toString().toLowerCase());
                        queryParamsMap.put(getCommonString(index + Constants.ValidationValues.ONE) + PRIVATE_IP_ADDRESS,
                                privateIpAddressesArray[index]);
                    } else {
                        queryParamsMap.put(getCommonString(index + Constants.ValidationValues.ONE) + PRIMARY,
                                Boolean.FALSE.toString().toLowerCase());
                        queryParamsMap.put(getCommonString(index + Constants.ValidationValues.ONE) + PRIVATE_IP_ADDRESS,
                                privateIpAddressesArray[index]);
                    }
                }
            }
        }

        if (!queryParamsMap.containsKey(getCommonString(Constants.ValidationValues.ONE) + PRIMARY)
                && !queryParamsMap.containsValue(Boolean.FALSE.toString().toLowerCase())) {
            InputsUtil.setOptionalMapEntry(queryParamsMap, SECONDARY_PRIVATE_IP_ADDRESS_COUNT,
                    wrapper.getNetworkInputs().getSecondaryPrivateIpAddressCount(),
                    StringUtils.isNotBlank(wrapper.getNetworkInputs().getSecondaryPrivateIpAddressCount()));
        }

        if (StringUtils.isNotBlank(wrapper.getNetworkInputs().getSecurityGroupIdsString())) {
            String[] securityGroupIdsArray = InputsUtil.getStringsArray(wrapper.getNetworkInputs().getSecurityGroupIdsString(),
                    Constants.Miscellaneous.EMPTY, wrapper.getCommonInputs().getDelimiter());
            if (securityGroupIdsArray != null && securityGroupIdsArray.length > 0) {
                for (int index = 0; index < securityGroupIdsArray.length; index++) {
                    queryParamsMap.put(SECURITY_GROUP_ID + Constants.Miscellaneous.DOT +
                            String.valueOf(index + Constants.ValidationValues.ONE), securityGroupIdsArray[index]);
                }
            }
        }

        return queryParamsMap;
    }

    public Map<String, String> getDeleteNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());

        return queryParamsMap;
    }

    public Map<String, String> getDetachNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        queryParamsMap.put(Constants.AwsParams.ACTION, wrapper.getAction());
        queryParamsMap.put(Constants.AwsParams.VERSION, wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(ATTACHMENT_ID, wrapper.getCustomInputs().getAttachmentId());

        InputsUtil.setOptionalMapEntry(queryParamsMap, FORCE, Constants.Miscellaneous.SET_FLAG,
                wrapper.getNetworkInputs().isForceDetach());

        return queryParamsMap;
    }

    private String getCommonString(int index) {
        return PRIVATE_IP_ADDRESSES + Constants.Miscellaneous.DOT + String.valueOf(index + Constants.ValidationValues.ONE) +
                Constants.Miscellaneous.DOT;
    }
}