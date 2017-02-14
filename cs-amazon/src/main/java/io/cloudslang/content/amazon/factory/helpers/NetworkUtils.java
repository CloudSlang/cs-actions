/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.aws.AttachmentStatus;
import io.cloudslang.content.amazon.entities.aws.NetworkFilter;
import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.ALLOCATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.CIDR_BLOCK;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DELETE_ON_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DESCRIPTION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DEVICE_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.FILTER;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.FORCE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NAME;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NETWORK_INTERFACE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NETWORK_INTERFACE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.PRIMARY;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.PRIVATE_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.PUBLIC_IP;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SECURITY_GROUP_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VALUE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VPC_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NETWORK;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.PIPE_DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.FILTER_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.FILTER_VALUES_STRING;
import static io.cloudslang.content.amazon.utils.InputsUtil.getArrayWithoutDuplicateEntries;
import static io.cloudslang.content.amazon.utils.InputsUtil.getQueryParamsSpecificString;
import static io.cloudslang.content.amazon.utils.InputsUtil.getStringsArray;
import static io.cloudslang.content.amazon.utils.InputsUtil.getStringsList;
import static io.cloudslang.content.amazon.utils.InputsUtil.getValidIPv4Address;
import static io.cloudslang.content.amazon.utils.InputsUtil.putCollectionInQueryMap;
import static io.cloudslang.content.amazon.utils.InputsUtil.setCommonQueryParamsMap;
import static io.cloudslang.content.amazon.utils.InputsUtil.setNetworkInterfaceSpecificQueryParams;
import static io.cloudslang.content.amazon.utils.InputsUtil.setOptionalMapEntry;
import static io.cloudslang.content.amazon.utils.InputsUtil.validateAgainstDifferentArraysLength;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by Mihai Tusa.
 * 9/9/2016.
 */
public class NetworkUtils {
    private static final String ALLOW_REASSOCIATION = "AllowReassociation";
    private static final String ASSOCIATION_ID = "AssociationId";
    private static final String ASSOCIATE_PUBLIC_IP_ADDRESS = "AssociatePublicIpAddress";
    private static final String ATTACHMENT_ID = "AttachmentId";
    private static final String AVAILABILITY_ZONE = "AvailabilityZone";
    private static final String INVALID_NUMBER_OF_ARGUMENTS = "Invalid number of arguments for network inputs.";
    private static final String PRIVATE_IP_ADDRESSES = "PrivateIpAddresses";
    private static final String SECONDARY_PRIVATE_IP_ADDRESS_COUNT = "SecondaryPrivateIpAddressCount";
    private static final String SUBNET_ID = "SubnetId";
    public static final String ATTACHMENT_STATUS = "attachment.status";

    public Map<String, String> getAssociateAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        setOptionalMapEntry(queryParamsMap, ALLOCATION_ID, wrapper.getCustomInputs().getAllocationId(),
                isNotBlank(wrapper.getCustomInputs().getAllocationId()));
        setOptionalMapEntry(queryParamsMap, ALLOW_REASSOCIATION, valueOf(wrapper.getElasticIpInputs().isAllowReassociation()),
                wrapper.getElasticIpInputs().isAllowReassociation());
        setOptionalMapEntry(queryParamsMap, PRIVATE_IP_ADDRESS, wrapper.getElasticIpInputs().getPrivateIpAddress(),
                isNotBlank(wrapper.getElasticIpInputs().getPrivateIpAddress()));
        setOptionalMapEntry(queryParamsMap, PUBLIC_IP, wrapper.getElasticIpInputs().getPublicIp(),
                isNotBlank(wrapper.getElasticIpInputs().getPublicIp()));
        setOptionalMapEntry(queryParamsMap, NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId(),
                isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceId()));

        return queryParamsMap;
    }

    public Map<String, String> getAttachNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());
        queryParamsMap.put(DEVICE_INDEX, wrapper.getNetworkInputs().getDeviceIndex());

        return queryParamsMap;
    }

    public Map<String, String> getCreateNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(SUBNET_ID, wrapper.getCustomInputs().getSubnetId());
        setOptionalMapEntry(queryParamsMap, DESCRIPTION, wrapper.getNetworkInputs().getNetworkInterfaceDescription(),
                isNotBlank(wrapper.getNetworkInputs().getNetworkInterfaceDescription()));

        String privateIpAddress = getValidIPv4Address(wrapper.getElasticIpInputs().getPrivateIpAddress());
        setOptionalMapEntry(queryParamsMap, getQueryParamsSpecificString(NETWORK, START_INDEX) + PRIMARY,
                Boolean.TRUE.toString().toLowerCase(), isNotBlank(privateIpAddress));
        setOptionalMapEntry(queryParamsMap, getQueryParamsSpecificString(NETWORK, START_INDEX) + PRIVATE_IP_ADDRESS,
                privateIpAddress, isNotBlank(privateIpAddress));
        setOptionalMapEntry(queryParamsMap, getQueryParamsSpecificString(NETWORK, START_INDEX) + PRIVATE_IP_ADDRESS,
                privateIpAddress, isNotBlank(privateIpAddress));

        setPrivateIpAddressesQueryParams(queryParamsMap, wrapper, NETWORK, wrapper.getCommonInputs().getDelimiter());
        setSecondaryPrivateIpAddressCountQueryParams(queryParamsMap, wrapper.getNetworkInputs().getSecondaryPrivateIpAddressCount());
        new CommonUtils().setPrefixedAndSuffixedCommonQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                SECURITY_GROUP_ID, EMPTY, wrapper.getCommonInputs().getDelimiter());

        return queryParamsMap;
    }

    public Map<String, String> getCreateSubnetQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(CIDR_BLOCK, wrapper.getNetworkInputs().getCidrBlock());
        queryParamsMap.put(VPC_ID, wrapper.getCustomInputs().getVpcId());
        setOptionalMapEntry(queryParamsMap, AVAILABILITY_ZONE, wrapper.getCustomInputs().getAvailabilityZone(),
                isNotBlank(wrapper.getCustomInputs().getAvailabilityZone()));

        return queryParamsMap;
    }

    public Map<String, String> getDeleteNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());

        return queryParamsMap;
    }

    public Map<String, String> getDeleteSubnetQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(SUBNET_ID, wrapper.getCustomInputs().getSubnetId());

        return queryParamsMap;
    }

    public Map<String, String> getDetachNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(ATTACHMENT_ID, wrapper.getCustomInputs().getAttachmentId());
        setOptionalMapEntry(queryParamsMap, FORCE, valueOf(ONE), wrapper.getNetworkInputs().isForceDetach());

        return queryParamsMap;
    }

    public Map<String, String> getDisassociateAddressQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        setOptionalMapEntry(queryParamsMap, ASSOCIATION_ID, wrapper.getCustomInputs().getAssociationId(),
                isNotBlank(wrapper.getCustomInputs().getAssociationId()));
        setOptionalMapEntry(queryParamsMap, PUBLIC_IP, wrapper.getElasticIpInputs().getPublicIp(),
                isNotBlank(wrapper.getElasticIpInputs().getPublicIp()));

        return queryParamsMap;
    }

    void setNetworkInterfaceQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) throws Exception {
        int alignmentValue = START_INDEX;
        List<String> associatePublicIpAddressList = getStringsList(wrapper.getNetworkInputs().getNetworkInterfacesAssociatePublicIpAddressesString(), wrapper.getCommonInputs().getDelimiter());
        alignmentValue = validateParamsCount(alignmentValue, associatePublicIpAddressList);
        List<String> deleteOnTerminationList = getStringsList(wrapper.getNetworkInputs().getNetworkInterfaceDeleteOnTermination(), wrapper.getCommonInputs().getDelimiter());
        alignmentValue = validateParamsCount(alignmentValue, deleteOnTerminationList);
        List<String> descriptionList = getStringsList(wrapper.getNetworkInputs().getNetworkInterfaceDescription(), wrapper.getCommonInputs().getDelimiter());
        alignmentValue = validateParamsCount(alignmentValue, descriptionList);
        List<String> deviceIndexList = getStringsList(wrapper.getNetworkInputs().getNetworkInterfaceDeviceIndex(), wrapper.getCommonInputs().getDelimiter());
        alignmentValue = validateParamsCount(alignmentValue, deviceIndexList);
        List<String> interfaceIdList = getStringsList(wrapper.getNetworkInputs().getNetworkInterfaceId(), wrapper.getCommonInputs().getDelimiter());
        alignmentValue = validateParamsCount(alignmentValue, interfaceIdList);
        List<String> privateIpAddressList = getStringsList(wrapper.getNetworkInputs().getNetworkInterfacePrivateIpAddress(), wrapper.getCommonInputs().getDelimiter());
        alignmentValue = validateParamsCount(alignmentValue, privateIpAddressList);
        List<String> privateIpAddressesList = getStringsList(wrapper.getElasticIpInputs().getPrivateIpAddressesString(), wrapper.getCommonInputs().getDelimiter());
        alignmentValue = validateParamsCount(alignmentValue, privateIpAddressesList);
        if (alignmentValue != START_INDEX) {
            List<String> secondaryPrivateIpAddressCountList = getStringsList(wrapper.getNetworkInputs().getSecondaryPrivateIpAddressCount(), wrapper.getCommonInputs().getDelimiter());
            alignmentValue = validateParamsCount(alignmentValue, secondaryPrivateIpAddressCountList);
            List<String> securityGroupIdsList = getStringsList(wrapper.getIamInputs().getSecurityGroupIdsString(), wrapper.getCommonInputs().getDelimiter());
            alignmentValue = validateParamsCount(alignmentValue, securityGroupIdsList);
            List<String> subnetIdList = getStringsList(wrapper.getCustomInputs().getSubnetId(), wrapper.getCommonInputs().getDelimiter());
            alignmentValue = validateParamsCount(alignmentValue, subnetIdList);
            for (int index = 0; index < alignmentValue; index++) {
                String commonKey = NETWORK_INTERFACE + DOT + valueOf(index + 1);
                putSimpleNetworkParams(queryParamsMap, commonKey, ASSOCIATE_PUBLIC_IP_ADDRESS, associatePublicIpAddressList, index);
                putSimpleNetworkParams(queryParamsMap, commonKey, DELETE_ON_TERMINATION, deleteOnTerminationList, index);
                putSimpleNetworkParams(queryParamsMap, commonKey, DESCRIPTION, descriptionList, index);
                putSimpleNetworkParams(queryParamsMap, commonKey, DEVICE_INDEX, deviceIndexList, index);
                putSimpleNetworkParams(queryParamsMap, commonKey, NETWORK_INTERFACE_ID, interfaceIdList, index);
                putSimpleNetworkParams(queryParamsMap, commonKey, PRIVATE_IP_ADDRESS, privateIpAddressList, index);
                putNetworkPrivateIpAddresses(queryParamsMap, commonKey, PRIVATE_IP_ADDRESSES, privateIpAddressesList, index);
                putSimpleNetworkParams(queryParamsMap, commonKey, SECONDARY_PRIVATE_IP_ADDRESS_COUNT, secondaryPrivateIpAddressCountList, index);
                putNetworkSecurityGroupIds(queryParamsMap, commonKey, SECURITY_GROUP_ID, securityGroupIdsList, index);
                putSimpleNetworkParams(queryParamsMap, commonKey, SUBNET_ID, subnetIdList, index);
            }
        }
    }

    private void putNetworkPrivateIpAddresses(Map<String, String> queryParamsMap, String commonKey, String specificKey, List<String> paramsList, int index) {
        if (paramsList != null) {
            String key = commonKey + DOT + specificKey;
            String currentValues = paramsList.get(index);
            List<String> currentPrivateIpAddresses = getStringsList(currentValues, PIPE_DELIMITER);
            if (currentPrivateIpAddresses != null) {
                for (int step = START_INDEX; step < currentPrivateIpAddresses.size(); step++) {
                    String currentIpKey = key + DOT + valueOf(step + ONE) + DOT + PRIVATE_IP_ADDRESS;
                    String currentIpTypeKey = key + DOT + valueOf(step + ONE) + DOT + PRIMARY;
                    String currentIpValue = currentPrivateIpAddresses.get(step);
                    String currentIpTypeValue = step == START_INDEX ? valueOf(true) : valueOf(false);

                    queryParamsMap.put(currentIpKey, currentIpValue);
                    queryParamsMap.put(currentIpTypeKey, currentIpTypeValue);
                }
            }
        }
    }

    private void putNetworkSecurityGroupIds(Map<String, String> queryParamsMap, String commonKey, String specificKey, List<String> paramsList, int index) {
        if (paramsList != null) {
            String key = commonKey + DOT + specificKey;
            String currentValues = paramsList.get(index);
            List<String> instanceNetworkIds = getStringsList(currentValues, PIPE_DELIMITER);
            if (instanceNetworkIds != null) {
                for (int step = START_INDEX; step < instanceNetworkIds.size(); step++) {
                    String currentKey = key + DOT + valueOf(step + ONE);
                    String currentValue = instanceNetworkIds.get(step);
                    queryParamsMap.put(currentKey, currentValue);
                }
            }
        }
    }

    private void putSimpleNetworkParams(Map<String, String> queryParamsMap, String commonKey, String specificKey, List<String> paramList, int index) {
        if (paramList != null) {
            String currentKey = commonKey + DOT + specificKey;
            String currentValue = paramList.get(index);
            queryParamsMap.put(currentKey, currentValue);
        }
    }


    private int validateParamsCount(int alignmentValue, List<String> paramList) throws Exception {
        if (paramList != null) {
            if (alignmentValue != START_INDEX && paramList.size() != alignmentValue) {
                throw new Exception(INVALID_NUMBER_OF_ARGUMENTS);
            } else {
                return paramList.size();
            }
        }

        return alignmentValue;
    }

    private void setPrivateIpAddressesQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper, String specificArea, String delimiter) {
        if (isNotBlank(wrapper.getElasticIpInputs().getPrivateIpAddressesString())) {
            String[] privateIpAddressesArray = getArrayWithoutDuplicateEntries(wrapper.getElasticIpInputs().getPrivateIpAddressesString(),
                    PRIVATE_IP_ADDRESSES_STRING, delimiter);

            if (isNotEmpty(privateIpAddressesArray)) {
                for (int index = START_INDEX; index < privateIpAddressesArray.length; index++) {
                    privateIpAddressesArray[index] = getValidIPv4Address(privateIpAddressesArray[index]);
                    if (index == START_INDEX
                            && !queryParamsMap.containsKey(getQueryParamsSpecificString(specificArea, START_INDEX) + PRIMARY)
                            && !queryParamsMap.containsValue(Boolean.TRUE.toString().toLowerCase())) {
                        queryParamsMap.put(getQueryParamsSpecificString(specificArea, index) + PRIMARY, Boolean.TRUE.toString().toLowerCase());
                        queryParamsMap.put(getQueryParamsSpecificString(specificArea, index) + PRIVATE_IP_ADDRESS, privateIpAddressesArray[index]);
                    } else {
                        int startIndex = NETWORK_INTERFACE.equalsIgnoreCase(specificArea) ? index : index + ONE;
                        queryParamsMap.put(getQueryParamsSpecificString(specificArea, startIndex) + PRIMARY, Boolean.FALSE.toString().toLowerCase());
                        queryParamsMap.put(getQueryParamsSpecificString(specificArea, startIndex) + PRIVATE_IP_ADDRESS, privateIpAddressesArray[index]);
                    }
                    if (NETWORK_INTERFACE.equalsIgnoreCase(specificArea)) {
                        setNetworkInterfaceSpecificQueryParams(queryParamsMap, wrapper, privateIpAddressesArray, index);
                    }
                }
            }
        }
    }

    private void setSecondaryPrivateIpAddressCountQueryParams(Map<String, String> queryParamsMap, String inputString) {
        if (!queryParamsMap.containsKey(getQueryParamsSpecificString(NETWORK, ONE) + PRIMARY)
                && !queryParamsMap.containsValue(Boolean.FALSE.toString().toLowerCase())) {
            setOptionalMapEntry(queryParamsMap, SECONDARY_PRIVATE_IP_ADDRESS_COUNT, inputString, isNotBlank(inputString));
        }
    }

    private void setFilterValues(Map<String, String> queryParamsMap, String filterName, String filterValues, int index) {
        String[] valuesArray = getStringsArray(filterValues, EMPTY, PIPE_DELIMITER);
        if (isNotEmpty(valuesArray)) {
            for (int counter = START_INDEX; counter < valuesArray.length; counter++) {
                if (!NOT_RELEVANT.equalsIgnoreCase(getFilterValue(filterName, valuesArray[counter]))
                        || !"-1".equals(getFilterValue(filterName, valuesArray[counter]))) {
                    queryParamsMap.put(getFilterValueKey(index, counter), getFilterValue(filterName, valuesArray[counter].toLowerCase()));
                    if (!NOT_RELEVANT.equalsIgnoreCase(getFilterValue(filterName, valuesArray[counter])) ||
                            !"-1".equals(getFilterValue(filterName, valuesArray[counter]))) {
                        queryParamsMap.put(getFilterValueKey(index, counter),
                                getFilterValue(filterName, valuesArray[counter].toLowerCase()));
                    }
                }
            }
        }
    }

    private String getFilterValue(String filterName, String filterValue) {
        switch (filterName) {
            case ATTACHMENT_STATUS:
                return AttachmentStatus.getValue(filterValue);
            default:
                return filterValue;
        }
    }
    
    private String getFilterNameKey(int index) {
        return FILTER + DOT + valueOf(index + ONE) + DOT + NAME;
    }

    private String getFilterValueKey(int index, int counter) {
        return FILTER + DOT + valueOf(index + ONE) + DOT + VALUE + DOT + valueOf(counter + ONE);
    }

    private void setDescribeNetworkInterfacesFilters(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        String[] filterNamesArray = getArrayWithoutDuplicateEntries(wrapper.getInstanceInputs().getFilterNamesString(),
                FILTER_NAMES_STRING, wrapper.getCommonInputs().getDelimiter());
        String[] filterValuesArray = getStringsArray(wrapper.getInstanceInputs().getFilterValuesString(), EMPTY,
                wrapper.getCommonInputs().getDelimiter());
        validateAgainstDifferentArraysLength(filterNamesArray, filterValuesArray, FILTER_NAMES_STRING, FILTER_VALUES_STRING);
        if (isNotEmpty(filterNamesArray) && isNotEmpty(filterValuesArray)) {
            for (int index = START_INDEX; index < filterNamesArray.length; index++) {
                String filterName = NetworkFilter.getNetworkFilter(filterNamesArray[index]);
                queryParamsMap.put(getFilterNameKey(index), filterName);
                setFilterValues(queryParamsMap, filterName, filterValuesArray[index], index);
            }
        }
    }

    public Map<String, String> getDescribeNetworkInterfacesQueryParamsMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        List<String> networkInterfaceIds = getStringsList(wrapper.getNetworkInputs().getNetworkInterfaceId(), wrapper.getCommonInputs().getDelimiter());
        putCollectionInQueryMap(queryParamsMap, NETWORK_INTERFACE_ID, networkInterfaceIds);

        setDescribeNetworkInterfacesFilters(queryParamsMap, wrapper);

        return queryParamsMap;
    }
}