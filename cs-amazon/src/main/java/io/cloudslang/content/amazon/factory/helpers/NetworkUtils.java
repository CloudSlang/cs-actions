package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.ALLOCATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DESCRIPTION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DEVICE_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.FORCE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NETWORK_INTERFACE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NETWORK_INTERFACE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.PRIMARY;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.PRIVATE_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.PUBLIC_IP;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SECURITY_GROUP_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SUBNET_ID;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NETWORK;

import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;

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
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        InputsUtil.setOptionalMapEntry(queryParamsMap, ALLOCATION_ID, wrapper.getCustomInputs().getAllocationId(),
                isNotBlank(wrapper.getCustomInputs().getAllocationId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, ALLOW_REASSOCIATION, valueOf(wrapper.getElasticIpInputs().isAllowReassociation()),
                wrapper.getElasticIpInputs().isAllowReassociation());
        InputsUtil.setOptionalMapEntry(queryParamsMap, PRIVATE_IP_ADDRESS, wrapper.getElasticIpInputs().getPrivateIpAddress(),
                isNotBlank(wrapper.getElasticIpInputs().getPrivateIpAddress()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PUBLIC_IP, wrapper.getElasticIpInputs().getPublicIp(),
                isNotBlank(wrapper.getElasticIpInputs().getPublicIp()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId(),
                isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceId()));

        return queryParamsMap;
    }

    public Map<String, String> getAttachNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());
        queryParamsMap.put(DEVICE_INDEX, wrapper.getNetworkInputs().getDeviceIndex());

        return queryParamsMap;
    }

    public Map<String, String> getCreateNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(SUBNET_ID, wrapper.getCustomInputs().getSubnetId());
        InputsUtil.setOptionalMapEntry(queryParamsMap, DESCRIPTION, wrapper.getNetworkInputs().getNetworkInterfaceDescription(),
                isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDescription()));

        String privateIpAddress = InputsUtil.getValidIPv4Address(wrapper.getElasticIpInputs().getPrivateIpAddress());
        InputsUtil.setOptionalMapEntry(queryParamsMap, InputsUtil.getQueryParamsSpecificString(NETWORK, START_INDEX) + PRIMARY,
                Boolean.TRUE.toString().toLowerCase(), isNotBlank(privateIpAddress));
        InputsUtil.setOptionalMapEntry(queryParamsMap, InputsUtil.getQueryParamsSpecificString(NETWORK, START_INDEX) + PRIVATE_IP_ADDRESS,
                privateIpAddress, isNotBlank(privateIpAddress));
        InputsUtil.setOptionalMapEntry(queryParamsMap, InputsUtil.getQueryParamsSpecificString(NETWORK, START_INDEX) + PRIVATE_IP_ADDRESS,
                privateIpAddress, isNotBlank(privateIpAddress));

        setPrivateIpAddressesQueryParams(queryParamsMap, wrapper, NETWORK, wrapper.getCommonInputs().getDelimiter());
        setSecondaryPrivateIpAddressCountQueryParams(queryParamsMap, wrapper.getNetworkInputs().getSecondaryPrivateIpAddressCount());
        new IamUtils().setSecurityGroupsRelatedQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                SECURITY_GROUP_ID, EMPTY, wrapper.getCommonInputs().getDelimiter());

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

        InputsUtil.setOptionalMapEntry(queryParamsMap, FORCE, valueOf(ONE), wrapper.getNetworkInputs().isForceDetach());

        return queryParamsMap;
    }

    public Map<String, String> getDisassociateAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        InputsUtil.setOptionalMapEntry(queryParamsMap, ASSOCIATION_ID, wrapper.getCustomInputs().getAssociationId(),
                isNotBlank(wrapper.getCustomInputs().getAssociationId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PUBLIC_IP, wrapper.getElasticIpInputs().getPublicIp(),
                isNotBlank(wrapper.getElasticIpInputs().getPublicIp()));

        return queryParamsMap;
    }

    void setPrivateIpAddressesQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper, String specificArea, String delimiter) {
        if (isNotBlank(wrapper.getElasticIpInputs().getPrivateIpAddressesString())) {
            String[] privateIpAddressesArray = InputsUtil.getStringsArray(wrapper.getElasticIpInputs().getPrivateIpAddressesString(), EMPTY, delimiter);
            InputsUtil.validateArrayAgainstDuplicateElements(privateIpAddressesArray, wrapper.getElasticIpInputs().getPrivateIpAddressesString(),
                    wrapper.getCommonInputs().getDelimiter(), PRIVATE_IP_ADDRESSES_STRING);
            if (privateIpAddressesArray != null && privateIpAddressesArray.length > START_INDEX) {
                for (int index = START_INDEX; index < privateIpAddressesArray.length; index++) {
                    privateIpAddressesArray[index] = InputsUtil.getValidIPv4Address(privateIpAddressesArray[index]);
                    if (index == START_INDEX
                            && !queryParamsMap.containsKey(InputsUtil.getQueryParamsSpecificString(specificArea, START_INDEX) + PRIMARY)
                            && !queryParamsMap.containsValue(Boolean.TRUE.toString().toLowerCase())) {
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(specificArea, index) + PRIMARY, Boolean.TRUE.toString().toLowerCase());
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(specificArea, index) + PRIVATE_IP_ADDRESS, privateIpAddressesArray[index]);
                    } else {
                        int startIndex = NETWORK_INTERFACE.equalsIgnoreCase(specificArea) ? index : index + ONE;
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(specificArea, startIndex) + PRIMARY, Boolean.FALSE.toString().toLowerCase());
                        queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(specificArea, startIndex) + PRIVATE_IP_ADDRESS, privateIpAddressesArray[index]);
                    }
                    if (NETWORK_INTERFACE.equalsIgnoreCase(specificArea)) {
                        InputsUtil.setNetworkInterfaceSpecificQueryParams(queryParamsMap, wrapper, privateIpAddressesArray, index);
                    }
                }
            }
        }
    }

    void setSecondaryPrivateIpAddressCountQueryParams(Map<String, String> queryParamsMap, String inputString) {
        if (!queryParamsMap.containsKey(InputsUtil.getQueryParamsSpecificString(NETWORK, ONE) + PRIMARY)
                && !queryParamsMap.containsValue(Boolean.FALSE.toString().toLowerCase())) {
            InputsUtil.setOptionalMapEntry(queryParamsMap, SECONDARY_PRIVATE_IP_ADDRESS_COUNT, inputString,
                    isNotBlank(inputString));
        }
    }
}