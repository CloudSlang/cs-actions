package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
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
public class NetworkUtils {
    private static final String ALLOW_REASSOCIATION = "AllowReassociation";
    private static final String ASSOCIATION_ID = "AssociationId";
    private static final String ATTACHMENT_ID = "AttachmentId";
    private static final String SECONDARY_PRIVATE_IP_ADDRESS_COUNT = "SecondaryPrivateIpAddressCount";

    public Map<String, String> getAssociateAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());

        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.ALLOCATION_ID,
                wrapper.getCustomInputs().getAllocationId(),
                StringUtils.isNotBlank(wrapper.getCustomInputs().getAllocationId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, ALLOW_REASSOCIATION,
                String.valueOf(wrapper.getElasticIpInputs().isAllowReassociation()),
                wrapper.getElasticIpInputs().isAllowReassociation());
        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.PRIVATE_IP_ADDRESS,
                wrapper.getElasticIpInputs().getPrivateIpAddress(),
                StringUtils.isNotBlank(wrapper.getElasticIpInputs().getPrivateIpAddress()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.PUBLIC_IP,
                wrapper.getElasticIpInputs().getPublicIp(),
                StringUtils.isNotBlank(wrapper.getElasticIpInputs().getPublicIp()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.NETWORK_INTERFACE_ID,
                wrapper.getNetworkInputs().getNetworkInterfaceId(),
                StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceId()));

        return queryParamsMap;
    }

    public Map<String, String> getAttachNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        queryParamsMap.put(Constants.AwsParams.NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());
        queryParamsMap.put(Constants.AwsParams.DEVICE_INDEX, wrapper.getNetworkInputs().getDeviceIndex());

        return queryParamsMap;
    }

    public Map<String, String> getCreateNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.SUBNET_ID, wrapper.getCustomInputs().getSubnetId());
        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.DESCRIPTION,
                wrapper.getNetworkInputs().getNetworkInterfaceDescription(),
                StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDescription()));

        String privateIpAddress = InputsUtil.getValidIPv4Address(wrapper.getElasticIpInputs().getPrivateIpAddress());
        InputsUtil.setOptionalMapEntry(queryParamsMap,
                InputsUtil.getQueryParamsSpecificString(Constants.Miscellaneous.NETWORK, Constants.Values.START_INDEX) +
                        Constants.AwsParams.PRIMARY, Boolean.TRUE.toString().toLowerCase(), StringUtils.isNotBlank(privateIpAddress));
        InputsUtil.setOptionalMapEntry(queryParamsMap,
                InputsUtil.getQueryParamsSpecificString(Constants.Miscellaneous.NETWORK, Constants.Values.START_INDEX) +
                        Constants.AwsParams.PRIVATE_IP_ADDRESS, privateIpAddress, StringUtils.isNotBlank(privateIpAddress));
        InputsUtil.setOptionalMapEntry(queryParamsMap,
                InputsUtil.getQueryParamsSpecificString(Constants.Miscellaneous.NETWORK, Constants.Values.START_INDEX) +
                        Constants.AwsParams.PRIVATE_IP_ADDRESS, privateIpAddress, StringUtils.isNotBlank(privateIpAddress));

        setPrivateIpAddressesQueryParams(queryParamsMap, wrapper, Constants.Miscellaneous.NETWORK, wrapper.getCommonInputs().getDelimiter());
        setSecondaryPrivateIpAddressCountQueryParams(queryParamsMap, wrapper.getNetworkInputs().getSecondaryPrivateIpAddressCount());
        new IamUtils().setSecurityGroupQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                Constants.AwsParams.SECURITY_GROUP_ID, Constants.Miscellaneous.EMPTY, wrapper.getCommonInputs().getDelimiter());

        return queryParamsMap;
    }

    public Map<String, String> getDeleteNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(Constants.AwsParams.NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());

        return queryParamsMap;
    }

    public Map<String, String> getDetachNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(),
                wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(ATTACHMENT_ID, wrapper.getCustomInputs().getAttachmentId());

        InputsUtil.setOptionalMapEntry(queryParamsMap, Constants.AwsParams.FORCE, String.valueOf(Constants.Values.ONE),
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

    void setPrivateIpAddressesQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper, String specificArea,
                                          String delimiter) {
        if (StringUtils.isNotBlank(wrapper.getElasticIpInputs().getPrivateIpAddressesString())) {
            String[] privateIpAddressesArray = InputsUtil.getStringsArray(wrapper.getElasticIpInputs().getPrivateIpAddressesString(),
                    Constants.Miscellaneous.EMPTY, delimiter);
            InputsUtil.validateArrayAgainstDuplicateElements(privateIpAddressesArray, wrapper.getElasticIpInputs().getPrivateIpAddressesString(),
                    wrapper.getCommonInputs().getDelimiter(), Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING);
            if (privateIpAddressesArray != null && privateIpAddressesArray.length > Constants.Values.START_INDEX) {
                for (int index = Constants.Values.START_INDEX; index < privateIpAddressesArray.length; index++) {
                    privateIpAddressesArray[index] = InputsUtil.getValidIPv4Address(privateIpAddressesArray[index]);
                    if (index == Constants.Values.START_INDEX
                            && !queryParamsMap.containsKey(InputsUtil.getQueryParamsSpecificString(specificArea,
                            Constants.Values.START_INDEX) + Constants.AwsParams.PRIMARY)
                            && !queryParamsMap.containsValue(Boolean.TRUE.toString().toLowerCase())) {
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(specificArea, index) +
                                Constants.AwsParams.PRIMARY, Boolean.TRUE.toString().toLowerCase());
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(specificArea, index) +
                                Constants.AwsParams.PRIVATE_IP_ADDRESS, privateIpAddressesArray[index]);
                    } else {
                        int startIndex = Constants.AwsParams.NETWORK_INTERFACE.equalsIgnoreCase(specificArea) ?
                                index : index + Constants.Values.ONE;
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(specificArea, startIndex) +
                                Constants.AwsParams.PRIMARY, Boolean.FALSE.toString().toLowerCase());
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(specificArea, startIndex) +
                                Constants.AwsParams.PRIVATE_IP_ADDRESS, privateIpAddressesArray[index]);
                    }
                    if (Constants.AwsParams.NETWORK_INTERFACE.equalsIgnoreCase(specificArea)) {
                        InputsUtil.setNetworkInterfaceSpecificQueryParams(queryParamsMap, wrapper, privateIpAddressesArray, index);
                    }
                }
            }
        }
    }

    void setSecondaryPrivateIpAddressCountQueryParams(Map<String, String> queryParamsMap, String inputString) {
        if (!queryParamsMap.containsKey(InputsUtil.getQueryParamsSpecificString(Constants.Miscellaneous.NETWORK, Constants.Values.ONE) +
                Constants.AwsParams.PRIMARY)
                && !queryParamsMap.containsValue(Boolean.FALSE.toString().toLowerCase())) {
            InputsUtil.setOptionalMapEntry(queryParamsMap, SECONDARY_PRIVATE_IP_ADDRESS_COUNT, inputString,
                    StringUtils.isNotBlank(inputString));
        }
    }
}