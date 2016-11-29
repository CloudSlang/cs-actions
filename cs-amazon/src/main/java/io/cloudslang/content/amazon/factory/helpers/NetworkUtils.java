package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.ALLOCATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.CIDR_BLOCK;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DELETE_ON_TERMINATION;
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
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VPC_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NETWORK;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.PIPE_DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING;
import static io.cloudslang.content.amazon.utils.InputsUtil.getStringsList;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Created by Mihai Tusa.
 * 9/9/2016.
 */
public class NetworkUtils {
    public static final String ASSOCIATE_PUBLIC_IP_ADDRESS = "AssociatePublicIpAddress";
    public static final String PRIVATE_IP_ADDRESSES = "PrivateIpAddresses";
    private static final String ALLOW_REASSOCIATION = "AllowReassociation";
    private static final String ASSOCIATION_ID = "AssociationId";
    private static final String ATTACHMENT_ID = "AttachmentId";
    private static final String AVAILABILITY_ZONE = "AvailabilityZone";
    private static final String SECONDARY_PRIVATE_IP_ADDRESS_COUNT = "SecondaryPrivateIpAddressCount";
    private static final String SUBNET_ID = "SubnetId";

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
        new CommonUtils().setPrefixedAndSuffixedCommonQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                SECURITY_GROUP_ID, EMPTY, wrapper.getCommonInputs().getDelimiter());

        return queryParamsMap;
    }

    public Map<String, String> getCreateSubnetQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(CIDR_BLOCK, wrapper.getNetworkInputs().getCidrBlock());
        queryParamsMap.put(VPC_ID, wrapper.getCustomInputs().getVpcId());
        InputsUtil.setOptionalMapEntry(queryParamsMap, AVAILABILITY_ZONE, wrapper.getCustomInputs().getAvailabilityZone(),
                isNotBlank(wrapper.getCustomInputs().getAvailabilityZone()));

        return queryParamsMap;
    }

    public Map<String, String> getDeleteNetworkInterfaceQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(NETWORK_INTERFACE_ID, wrapper.getNetworkInputs().getNetworkInterfaceId());

        return queryParamsMap;
    }

    public Map<String, String> getDeleteSubnetQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(SUBNET_ID, wrapper.getCustomInputs().getSubnetId());

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

    void setNetworkInterfaceQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) throws Exception {
        int alignmentValue = 0;
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
        if (alignmentValue != 0) {
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
                for (int step = 0; step < currentPrivateIpAddresses.size(); step++) {
                    String currentIpKey = key + DOT + valueOf(step + 1) + DOT + PRIVATE_IP_ADDRESS;
                    String currentIpTypeKey = key + DOT + valueOf(step + 1) + DOT + PRIMARY;
                    String currentIpValue = currentPrivateIpAddresses.get(step);
                    String currentIpTypeValue = step == 0 ? valueOf(true) : valueOf(false);

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
                for (int step = 0; step < instanceNetworkIds.size(); step++) {
                    String currentKey = key + DOT + valueOf(step + 1);
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
            if (alignmentValue != 0 && paramList.size() != alignmentValue) {
                throw new Exception("Invalid number of arguments for network inputs.");
            } else {
                return paramList.size();
            }
        }
        return alignmentValue;
    }

    void setPrivateIpAddressesQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper, String specificArea, String delimiter) {
        if (isNotBlank(wrapper.getElasticIpInputs().getPrivateIpAddressesString())) {
            String[] privateIpAddressesArray = InputsUtil
                    .getArrayWithoutDuplicateEntries(wrapper.getElasticIpInputs().getPrivateIpAddressesString(),
                            PRIVATE_IP_ADDRESSES_STRING, delimiter);

            if (isNotEmpty(privateIpAddressesArray)) {
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