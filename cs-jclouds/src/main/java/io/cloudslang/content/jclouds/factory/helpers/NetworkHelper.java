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
    private static final String ALLOW_REASSOCIATION = "AllowReassociation";
    private static final String ATTACHMENT_ID = "AttachmentId";
    private static final String ASSOCIATION_ID = "AssociationId";
    private static final String DEVICE_ID = "DeviceIndex";
    private static final String DESCRIPTION = "Description";
    private static final String FORCE = "Force";
    private static final String INSTANCE_ID = "InstanceId";
    private static final String NETWORK_INTERFACE_ID = "NetworkInterfaceId";
    private static final String SECONDARY_PRIVATE_IP_ADDRESS_COUNT = "SecondaryPrivateIpAddressCount";
    private static final String SECURITY_GROUP_ID = "SecurityGroupId";

    public Map<String, String> getAssociateAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.ALLOCATION_ID, wrapper.getCustomInputs().getAllocationId(),
                StringUtils.isNotBlank(wrapper.getCustomInputs().getAllocationId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, ALLOW_REASSOCIATION, String.valueOf(wrapper.getElasticIpInputs().isAllowReassociation()),
                wrapper.getElasticIpInputs().isAllowReassociation());
        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.PRIVATE_IP_ADDRESS, wrapper.getElasticIpInputs().getPrivateIpAddress(),
                StringUtils.isNotBlank(wrapper.getElasticIpInputs().getPrivateIpAddress()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.PUBLIC_IP, wrapper.getElasticIpInputs().getPublicIp(),
                StringUtils.isNotBlank(wrapper.getElasticIpInputs().getPublicIp()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId(),
                StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceId()));

        return queryParamsMap;
    }

    public Map<String, String> getAttachNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());
        queryParamsMap.put(DEVICE_ID, wrapper.getNetworkInputs().getDeviceIndex());

        return queryParamsMap;
    }

    public Map<String, String> getCreateNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.SUBNET_ID, wrapper.getCustomInputs().getSubnetId());

        InputsUtil.setOptionalMapEntry(queryParamsMap, DESCRIPTION, wrapper.getNetworkInputs().getNetworkInterfaceDescription(),
                StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDescription()));

        String privateIpAddress = InputsUtil.getValidIPv4Address(wrapper.getElasticIpInputs().getPrivateIpAddress());
        if (StringUtils.isNotBlank(privateIpAddress)) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(Constants.Values.START_INDEX, Constants.Miscellaneous.NETWORK) +
                    Constants.AwsParams.PRIMARY, Boolean.TRUE.toString().toLowerCase());
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(Constants.Values.START_INDEX, Constants.Miscellaneous.NETWORK) +
                    Constants.AwsParams.PRIVATE_IP_ADDRESS, privateIpAddress);
        }

        if (StringUtils.isNotBlank(wrapper.getElasticIpInputs().getPrivateIpAddressesString())) {
            String[] privateIpAddressesArray = InputsUtil.getStringsArray(wrapper.getElasticIpInputs().getPrivateIpAddressesString(),
                    Constants.Miscellaneous.EMPTY, wrapper.getCommonInputs().getDelimiter());
            if (privateIpAddressesArray != null && privateIpAddressesArray.length > 0) {
                for (int index = 0; index < privateIpAddressesArray.length; index++) {
                    privateIpAddressesArray[index] = InputsUtil.getValidIPv4Address(privateIpAddressesArray[index]);
                    if (index == 0
                            && !queryParamsMap.containsKey(InputsUtil.getQueryParamsSpecificString(Constants.Values.START_INDEX,
                            Constants.Miscellaneous.NETWORK) + Constants.AwsParams.PRIMARY)
                            && !queryParamsMap.containsValue(Boolean.TRUE.toString().toLowerCase())) {
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(index + Constants.Values.ONE,
                                Constants.Miscellaneous.NETWORK) + Constants.AwsParams.PRIMARY, Boolean.TRUE.toString().toLowerCase());
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(index + Constants.Values.ONE,
                                Constants.Miscellaneous.NETWORK) + Constants.AwsParams.PRIVATE_IP_ADDRESS, privateIpAddressesArray[index]);
                    } else {
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(index + Constants.Values.ONE,
                                Constants.Miscellaneous.NETWORK) + Constants.AwsParams.PRIMARY, Boolean.FALSE.toString().toLowerCase());
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(index + Constants.Values.ONE,
                                Constants.Miscellaneous.NETWORK) + Constants.AwsParams.PRIVATE_IP_ADDRESS, privateIpAddressesArray[index]);
                    }
                }
            }
        }

        if (!queryParamsMap.containsKey(InputsUtil.getQueryParamsSpecificString(Constants.Values.ONE, Constants.Miscellaneous.NETWORK) + Constants.AwsParams.PRIMARY)
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
                            String.valueOf(index + Constants.Values.ONE), securityGroupIdsArray[index]);
                }
            }
        }

        return queryParamsMap;
    }

    public Map<String, String> getDeleteNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());

        return queryParamsMap;
    }

    public Map<String, String> getDetachNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(ATTACHMENT_ID, wrapper.getCustomInputs().getAttachmentId());

        InputsUtil.setOptionalMapEntry(queryParamsMap, FORCE, String.valueOf(Constants.Values.ONE),
                wrapper.getNetworkInputs().isForceDetach());

        return queryParamsMap;
    }

    public Map<String, String> getDisassociateAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        InputsUtil.setOptionalMapEntry(queryParamsMap, ASSOCIATION_ID, wrapper.getCustomInputs().getAssociationId(),
                StringUtils.isNotBlank(wrapper.getCustomInputs().getAssociationId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.PUBLIC_IP, wrapper.getElasticIpInputs().getPublicIp(),
                StringUtils.isNotBlank(wrapper.getElasticIpInputs().getPublicIp()));

        return queryParamsMap;
    }
}