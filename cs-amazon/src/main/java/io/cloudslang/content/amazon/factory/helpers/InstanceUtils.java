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

import io.cloudslang.content.amazon.entities.aws.Affinity;
import io.cloudslang.content.amazon.entities.aws.Architecture;
import io.cloudslang.content.amazon.entities.aws.BlockDeviceMappingStatus;
import io.cloudslang.content.amazon.entities.aws.BlockRootDeviceType;
import io.cloudslang.content.amazon.entities.aws.Hypervisor;
import io.cloudslang.content.amazon.entities.aws.InstanceLifecycle;
import io.cloudslang.content.amazon.entities.aws.InstanceFilter;
import io.cloudslang.content.amazon.entities.aws.InstanceInitiatedShutdownBehavior;
import io.cloudslang.content.amazon.entities.aws.InstanceState;
import io.cloudslang.content.amazon.entities.aws.InstanceType;
import io.cloudslang.content.amazon.entities.aws.MonitoringState;
import io.cloudslang.content.amazon.entities.aws.NetworkInterfaceAttachmentStatus;
import io.cloudslang.content.amazon.entities.aws.NetworkInterfaceStatus;
import io.cloudslang.content.amazon.entities.aws.ProductCodeType;
import io.cloudslang.content.amazon.entities.aws.Tenancy;
import io.cloudslang.content.amazon.entities.aws.VolumeType;
import io.cloudslang.content.amazon.entities.aws.VirtualizationType;

import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static io.cloudslang.content.amazon.utils.InputsUtil.getArrayWithoutDuplicateEntries;
import static io.cloudslang.content.amazon.utils.InputsUtil.getEnforcedBooleanCondition;
import static io.cloudslang.content.amazon.utils.InputsUtil.getQueryParamsSpecificString;
import static io.cloudslang.content.amazon.utils.InputsUtil.getStringsArray;
import static io.cloudslang.content.amazon.utils.InputsUtil.getValidEbsSize;
import static io.cloudslang.content.amazon.utils.InputsUtil.setCommonQueryParamsMap;
import static io.cloudslang.content.amazon.utils.InputsUtil.setOptionalMapEntry;
import static io.cloudslang.content.amazon.utils.InputsUtil.validateAgainstDifferentArraysLength;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static java.lang.String.valueOf;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.BLOCK_DEVICE_MAPPING;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DELETE_ON_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.ENCRYPTED;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.FORCE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.FILTER;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.IMAGE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.IOPS;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NAME;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NETWORK_INTERFACE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SECURITY_GROUP;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SECURITY_GROUP_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SNAPSHOT_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.STANDARD;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SUBNET_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VALUE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VOLUME_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VOLUME_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EBS;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.PIPE_DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.DELETE_ON_TERMINATIONS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.ENCRYPTED_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.IOPS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.NO_DEVICES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.SNAPSHOT_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.VOLUME_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.VOLUME_SIZES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.VOLUME_TYPES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.FILTER_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.FILTER_VALUES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.INSTANCE_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_DISABLE_API_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_KERNEL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_RAMDISK;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_USER_DATA;

/**
 * Created by Mihai Tusa.
 * 9/15/2016.
 */
public class InstanceUtils {
    private static final String AFFINITY_FILTER = "affinity";
    private static final String ARCHITECTURE_FILTER = "architecture";
    private static final String ATTRIBUTE = "Attribute";
    private static final String CLIENT_TOKEN = "ClientToken";
    private static final String BLOCK_DEVICE_MAPPING_DEVICE_NAME = "DeviceName";
    private static final String BLOCK_DEVICE_MAPPING_STATUS_FILTER = "block-device-mapping.status";
    private static final String DISABLE_API_TERMINATION = "DisableApiTermination";
    private static final String EBS_OPTIMIZED = "EbsOptimized";
    private static final String ENA_SUPPORT = "EnaSupport";
    private static final String GROUP_ID = "GroupId";
    private static final String HYPERVISOR_FILTER = "hypervisor";
    private static final String IAM_INSTANCE_PROFILE_ARN = "IamInstanceProfile.Arn";
    private static final String IAM_INSTANCE_PROFILE_NAME = "IamInstanceProfile.Name";
    private static final String INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR = "InstanceInitiatedShutdownBehavior";
    private static final String INSTANCE_LIFECYCLE_FILTER = "instance-lifecycle";
    private static final String INSTANCE_STATE_CODE_FILTER = "instance-state-code";
    private static final String INSTANCE_STATE_NAME_FILTER = "instance-state-name";
    private static final String INSTANCE_TYPE = "InstanceType";
    private static final String INSTANCE_TYPE_FILTER = "instance-type";
    private static final String KERNEL = "Kernel";
    private static final String KERNEL_ID = "KernelId";
    private static final String KEY_NAME = "KeyName";
    private static final String MAX_COUNT = "MaxCount";
    private static final String MAX_RESULTS = "MaxResults";
    private static final String MIN_COUNT = "MinCount";
    private static final String MONITORING_ENABLED = "Monitoring.Enabled";
    private static final String MONITORING_STATE_FILTER = "monitoring-state";
    private static final String NEXT_TOKEN = "NextToken";
    private static final String NETWORK_INTERFACE_ATTACHMENT_STATUS_FILTER = "network-interface.attachment.status";
    private static final String NETWORK_INTERFACE_STATUS_FILTER = "network-interface.status";
    private static final String NO_DEVICE = "NoDevice";
    private static final String NOT_RELEVANT_KEY_STRING = "-1";
    private static final String PLACEMENT_AFFINITY = "Placement.Affinity";
    private static final String PLACEMENT_AVAILABILITY_ZONE = "Placement.AvailabilityZone";
    private static final String PLACEMENT_GROUP_NAME = "Placement.GroupName";
    private static final String PLACEMENT_HOST_ID = "Placement.HostId";
    private static final String PLACEMENT_TENANCY = "Placement.Tenancy";
    private static final String PRODUCT_CODE_TYPE_FILTER = "product-code.type";
    private static final String RAMDISK = "Ramdisk";
    private static final String RAMDISK_ID = "RamdiskId";
    private static final String ROOT_DEVIOCE_TYPE_FILTER = "root-device-type";
    private static final String SOURCE_DEST_CHECK = "SourceDestCheck";
    private static final String SRIOV_NET_SUPPORT = "SriovNetSupport";
    private static final String TENANCY_FILTER = "tenancy";
    private static final String USER_DATA = "UserData";
    private static final String VOLUME_SIZE = "VolumeSize";
    private static final String VIRTUAL_NAME = "VirtualName";
    private static final String VIRTUALIZATION_TYPE_FILTER = "virtualization-type";

    public Map<String, String> getDescribeInstancesQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        setInstanceIdsQueryParams(wrapper, queryParamsMap);
        setOptionalMapEntry(queryParamsMap, MAX_RESULTS, wrapper.getInstanceInputs().getMaxResults(),
                !NOT_RELEVANT.equalsIgnoreCase(wrapper.getInstanceInputs().getMaxResults()));
        setOptionalMapEntry(queryParamsMap, NEXT_TOKEN, wrapper.getInstanceInputs().getNextToken(),
                isNotBlank(wrapper.getInstanceInputs().getNextToken()));
        setDescribeInstancesQueryParamsFilter(queryParamsMap, wrapper);

        return queryParamsMap;
    }

    public Map<String, String> getModifyInstanceAttributeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        setAttribute(queryParamsMap, wrapper.getInstanceInputs().getAttribute());
        new CommonUtils().setPrefixedAndSuffixedCommonQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                GROUP_ID, EMPTY, wrapper.getCommonInputs().getDelimiter());
        setModifyInstanceAttributeEbsSpecs(queryParamsMap, wrapper);
        setOptionalMapEntry(queryParamsMap, VALUE, wrapper.getInstanceInputs().getAttributeValue(),
                isNotBlank(wrapper.getInstanceInputs().getAttributeValue()));
        setOptionalMapEntry(queryParamsMap, DISABLE_API_TERMINATION + DOT + VALUE,
                valueOf(wrapper.getInstanceInputs().isDisableApiTermination()), wrapper.getInstanceInputs().isDisableApiTermination());
        setOptionalMapEntry(queryParamsMap, EBS_OPTIMIZED + DOT + VALUE,
                valueOf(wrapper.getEbsInputs().isEbsOptimized()), wrapper.getEbsInputs().isEbsOptimized());
        setOptionalMapEntry(queryParamsMap, ENA_SUPPORT + DOT + VALUE,
                valueOf(wrapper.getInstanceInputs().isEnaSupport()), wrapper.getInstanceInputs().isEnaSupport());
        setOptionalMapEntry(queryParamsMap, INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR + DOT + VALUE,
                wrapper.getInstanceInputs().getInstanceInitiatedShutdownBehavior(),
                (isNotBlank(wrapper.getInstanceInputs().getInstanceInitiatedShutdownBehavior()))
                        && !NOT_RELEVANT.equalsIgnoreCase(wrapper.getInstanceInputs().getInstanceInitiatedShutdownBehavior()));
        setOptionalMapEntry(queryParamsMap, INSTANCE_TYPE + DOT + VALUE, wrapper.getCustomInputs().getInstanceType(),
                (isNotBlank(wrapper.getCustomInputs().getInstanceType())
                        && !NOT_RELEVANT.equalsIgnoreCase(wrapper.getCustomInputs().getInstanceType())));
        setOptionalMapEntry(queryParamsMap, KERNEL + DOT + VALUE, wrapper.getInstanceInputs().getKernel(),
                isNotBlank(wrapper.getInstanceInputs().getKernel()));
        setOptionalMapEntry(queryParamsMap, RAMDISK + DOT + VALUE, wrapper.getInstanceInputs().getRamdisk(),
                isNotBlank(wrapper.getInstanceInputs().getRamdisk()));
        setOptionalMapEntry(queryParamsMap, SOURCE_DEST_CHECK + DOT + VALUE, wrapper.getInstanceInputs().getSourceDestinationCheck(),
                (isNotBlank(wrapper.getInstanceInputs().getSourceDestinationCheck()) &&
                        !NOT_RELEVANT.equalsIgnoreCase(wrapper.getInstanceInputs().getSourceDestinationCheck())));
        setOptionalMapEntry(queryParamsMap, SRIOV_NET_SUPPORT + DOT + VALUE, wrapper.getInstanceInputs().getSriovNetSupport(),
                isNotBlank(wrapper.getInstanceInputs().getSriovNetSupport()));
        setOptionalMapEntry(queryParamsMap, USER_DATA + DOT + VALUE, wrapper.getInstanceInputs().getUserData(),
                isNotBlank(wrapper.getInstanceInputs().getUserData()));

        return queryParamsMap;
    }

    public Map<String, String> getRebootInstancesQueryParamsMap(InputsWrapper wrapper) {
        return getRebootStartStopTerminateCommonQueryParamsMap(wrapper);
    }

    public Map<String, String> getRunInstancesQueryParamsMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(DISABLE_API_TERMINATION, valueOf(wrapper.getInstanceInputs().isDisableApiTermination()));
        queryParamsMap.put(EBS_OPTIMIZED, valueOf(wrapper.getEbsInputs().isEbsOptimized()));
        queryParamsMap.put(IMAGE_ID, wrapper.getCustomInputs().getImageId());
        queryParamsMap.put(MAX_COUNT, valueOf(wrapper.getInstanceInputs().getMaxCount()));
        queryParamsMap.put(MIN_COUNT, valueOf(wrapper.getInstanceInputs().getMinCount()));
        queryParamsMap.put(MONITORING_ENABLED, valueOf(wrapper.getInstanceInputs().isMonitoring()));

        String instanceType = NOT_RELEVANT.equalsIgnoreCase(wrapper.getCustomInputs().getInstanceType()) ?
                InstanceType.M1_SMALL.name().toLowerCase() : InstanceType.getInstanceType(wrapper.getCustomInputs().getInstanceType());
        queryParamsMap.put(INSTANCE_TYPE, instanceType);
        String iisbvalue = NOT_RELEVANT.equalsIgnoreCase(wrapper.getInstanceInputs().getInstanceInitiatedShutdownBehavior()) ?
                InstanceInitiatedShutdownBehavior.STOP.name().toLowerCase() :
                InstanceInitiatedShutdownBehavior.getValue(wrapper.getInstanceInputs().getInstanceInitiatedShutdownBehavior());
        queryParamsMap.put(INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR, iisbvalue);

        setOptionalMapEntry(queryParamsMap, CLIENT_TOKEN, wrapper.getInstanceInputs().getClientToken(),
                isNotBlank(wrapper.getInstanceInputs().getClientToken()));
        setOptionalMapEntry(queryParamsMap, IAM_INSTANCE_PROFILE_ARN, wrapper.getIamInputs().getIamInstanceProfileArn(),
                isNotBlank(wrapper.getIamInputs().getIamInstanceProfileArn()));
        setOptionalMapEntry(queryParamsMap, IAM_INSTANCE_PROFILE_NAME, wrapper.getIamInputs().getIamInstanceProfileName(),
                isNotBlank(wrapper.getIamInputs().getIamInstanceProfileName()));
        setOptionalMapEntry(queryParamsMap, KERNEL_ID, wrapper.getCustomInputs().getKernelId(),
                isNotBlank(wrapper.getCustomInputs().getKernelId()));
        setOptionalMapEntry(queryParamsMap, KEY_NAME, wrapper.getIamInputs().getKeyPairName(),
                isNotBlank(wrapper.getIamInputs().getKeyPairName()));
        setOptionalMapEntry(queryParamsMap, PLACEMENT_AFFINITY, wrapper.getInstanceInputs().getAffinity(),
                isNotBlank(wrapper.getInstanceInputs().getAffinity()));
        setOptionalMapEntry(queryParamsMap, PLACEMENT_AVAILABILITY_ZONE, wrapper.getCustomInputs().getAvailabilityZone(),
                isNotBlank(wrapper.getCustomInputs().getAvailabilityZone()));
        setOptionalMapEntry(queryParamsMap, PLACEMENT_GROUP_NAME, wrapper.getInstanceInputs().getPlacementGroupName(),
                isNotBlank(wrapper.getInstanceInputs().getPlacementGroupName()));
        setOptionalMapEntry(queryParamsMap, PLACEMENT_HOST_ID, wrapper.getCustomInputs().getHostId(),
                isNotBlank(wrapper.getCustomInputs().getHostId()));
        setOptionalMapEntry(queryParamsMap, PLACEMENT_TENANCY, wrapper.getInstanceInputs().getTenancy(),
                !NOT_RELEVANT.equalsIgnoreCase(wrapper.getInstanceInputs().getTenancy()));
        setOptionalMapEntry(queryParamsMap, RAMDISK_ID, wrapper.getCustomInputs().getRamdiskId(),
                isNotBlank(wrapper.getCustomInputs().getRamdiskId()));
        setOptionalMapEntry(queryParamsMap, USER_DATA, wrapper.getInstanceInputs().getUserData(),
                isNotBlank(wrapper.getInstanceInputs().getUserData()));

        setBlockDeviceMappingQueryParams(queryParamsMap, wrapper);
        new NetworkUtils().setNetworkInterfaceQueryParams(queryParamsMap, wrapper);

        if (!queryParamsMap.keySet().toString().contains(NETWORK_INTERFACE)) {
            setOptionalMapEntry(queryParamsMap, SUBNET_ID, wrapper.getCustomInputs().getSubnetId(),
                    isNotBlank(wrapper.getCustomInputs().getSubnetId()));
            setSecurityGroupQueryParams(queryParamsMap, wrapper);
        }

        return queryParamsMap;
    }

    public Map<String, String> getStartInstancesQueryParamsMap(InputsWrapper wrapper) {
        return getRebootStartStopTerminateCommonQueryParamsMap(wrapper);
    }

    public Map<String, String> getStopInstancesQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = getRebootStartStopTerminateCommonQueryParamsMap(wrapper);
        setOptionalMapEntry(queryParamsMap, FORCE, valueOf(wrapper.getInstanceInputs().isForceStop()), wrapper.getInstanceInputs().isForceStop());

        return queryParamsMap;
    }

    public Map<String, String> getTerminateInstancesQueryParamsMap(InputsWrapper wrapper) {
        return getRebootStartStopTerminateCommonQueryParamsMap(wrapper);
    }

    private Map<String, String> getRebootStartStopTerminateCommonQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        setInstanceIdsQueryParams(wrapper, queryParamsMap);

        return queryParamsMap;
    }

    private String getCurrentVolumeType(Map<String, String> queryParamsMap, int index) {
        return queryParamsMap.get(BLOCK_DEVICE_MAPPING + DOT + (index + ONE) + DOT + EBS + VOLUME_TYPE);
    }

    private boolean getOptionalsSetterFlag(String[] validationArray, String[] toBeValidatedArray, String firstInput, String secondInput) {
        if (toBeValidatedArray != null) {
            validateAgainstDifferentArraysLength(validationArray, toBeValidatedArray, firstInput, secondInput);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private String getFilterValue(String filterName, String filterValue) {
        switch (filterName) {
            case AFFINITY_FILTER:
                return Affinity.getValue(filterValue);
            case ARCHITECTURE_FILTER:
                return Architecture.getValue(filterValue);
            case BLOCK_DEVICE_MAPPING_STATUS_FILTER:
                return BlockDeviceMappingStatus.getValue(filterValue);
            case HYPERVISOR_FILTER:
                return Hypervisor.getValue(filterValue);
            case INSTANCE_LIFECYCLE_FILTER:
                return InstanceLifecycle.getValue(filterValue);
            case INSTANCE_STATE_CODE_FILTER:
                return valueOf(InstanceState.getKey(filterValue));
            case INSTANCE_STATE_NAME_FILTER:
                return InstanceState.getValue(filterValue);
            case INSTANCE_TYPE_FILTER:
                return InstanceType.getInstanceType(filterValue);
            case MONITORING_STATE_FILTER:
                return MonitoringState.getValue(filterValue);
            case PRODUCT_CODE_TYPE_FILTER:
                return ProductCodeType.getValue(filterValue);
            case ROOT_DEVIOCE_TYPE_FILTER:
                return BlockRootDeviceType.getValue(filterValue);
            case TENANCY_FILTER:
                return Tenancy.getValue(filterValue);
            case VIRTUALIZATION_TYPE_FILTER:
                return VirtualizationType.getValue(filterValue);
            case NETWORK_INTERFACE_STATUS_FILTER:
                return NetworkInterfaceStatus.getValue(filterValue);
            case NETWORK_INTERFACE_ATTACHMENT_STATUS_FILTER:
                return NetworkInterfaceAttachmentStatus.getValue(filterValue);
            default:
                return filterValue;
        }
    }

    private void setDescribeInstancesQueryParamsFilter(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        String[] filterNamesArray = getArrayWithoutDuplicateEntries(wrapper.getInstanceInputs().getFilterNamesString(),
                FILTER_NAMES_STRING, wrapper.getCommonInputs().getDelimiter());
        String[] filterValuesArray = getStringsArray(wrapper.getInstanceInputs().getFilterValuesString(), EMPTY,
                wrapper.getCommonInputs().getDelimiter());
        validateAgainstDifferentArraysLength(filterNamesArray, filterValuesArray, FILTER_NAMES_STRING, FILTER_VALUES_STRING);
        if (isNotEmpty(filterNamesArray) && isNotEmpty(filterValuesArray)) {
            for (int index = START_INDEX; index < filterNamesArray.length; index++) {
                String filterName = InstanceFilter.getInstanceFilter(filterNamesArray[index]);
                queryParamsMap.put(getFilterNameKey(index), filterName);
                setFilterValues(queryParamsMap, filterName, filterValuesArray[index], index);
            }
        }
    }

    private void setFilterValues(Map<String, String> queryParamsMap, String filterName, String filterValues, int index) {
        String[] valuesArray = getStringsArray(filterValues, EMPTY, PIPE_DELIMITER);
        if (isNotEmpty(valuesArray)) {
            for (int counter = START_INDEX; counter < valuesArray.length; counter++) {
                if (!NOT_RELEVANT.equalsIgnoreCase(getFilterValue(filterName, valuesArray[counter]))
                        || !NOT_RELEVANT_KEY_STRING.equals(getFilterValue(filterName, valuesArray[counter]))) {
                    queryParamsMap.put(getFilterValueKey(index, counter), getFilterValue(filterName, valuesArray[counter].toLowerCase()));
                    if (!NOT_RELEVANT.equalsIgnoreCase(getFilterValue(filterName, valuesArray[counter])) ||
                            !NOT_RELEVANT_KEY_STRING.equals(getFilterValue(filterName, valuesArray[counter]))) {
                        queryParamsMap.put(getFilterValueKey(index, counter),
                                getFilterValue(filterName, valuesArray[counter].toLowerCase()));
                    }
                }
            }
        }
    }

    private String getFilterNameKey(int index) {
        return FILTER + DOT + valueOf(index + ONE) + DOT + NAME;
    }

    private String getFilterValueKey(int index, int counter) {
        return FILTER + DOT + valueOf(index + ONE) + DOT + VALUE + DOT + valueOf(counter + ONE);
    }

    private void setModifyInstanceAttributeEbsSpecs(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        String delimiter = wrapper.getCommonInputs().getDelimiter();
        String[] deviceNamesArray = getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getBlockDeviceMappingDeviceNamesString(),
                BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, delimiter);
        String[] deleteOnTerminationsArray = getStringsArray(wrapper.getEbsInputs().getDeleteOnTerminationsString(), EMPTY, delimiter);
        String[] volumeIdsArray = getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getVolumeIdsString(),
                VOLUME_IDS_STRING, delimiter);
        String[] noDevicesArray = getStringsArray(wrapper.getEbsInputs().getNoDevicesString(), EMPTY, delimiter);
        String[] virtualNamesArray = getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getBlockDeviceMappingVirtualNamesString(),
                BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING, delimiter);

        validateAgainstDifferentArraysLength(deviceNamesArray, deleteOnTerminationsArray,
                BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, DELETE_ON_TERMINATIONS_STRING);
        validateAgainstDifferentArraysLength(deviceNamesArray, volumeIdsArray,
                BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, VOLUME_IDS_STRING);
        validateAgainstDifferentArraysLength(deviceNamesArray, noDevicesArray,
                BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, NO_DEVICES_STRING);
        validateAgainstDifferentArraysLength(deviceNamesArray, virtualNamesArray,
                BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING);
        setEbsOptionalQueryParams(queryParamsMap, deviceNamesArray, deleteOnTerminationsArray, volumeIdsArray, noDevicesArray, virtualNamesArray);
    }

    private void setInstanceIdsQueryParams(InputsWrapper wrapper, Map<String, String> queryParamsMap) {
        String[] instanceIdsArray = getArrayWithoutDuplicateEntries(wrapper.getInstanceInputs().getInstanceIdsString(),
                INSTANCE_IDS_STRING, wrapper.getCommonInputs().getDelimiter());
        if (isNotEmpty(instanceIdsArray)) {
            for (int index = START_INDEX; index < instanceIdsArray.length; index++) {
                setOptionalMapEntry(queryParamsMap, INSTANCE_ID + DOT + valueOf(index + ONE), instanceIdsArray[index],
                        isNotBlank(instanceIdsArray[index]) && !NOT_RELEVANT.equalsIgnoreCase(instanceIdsArray[index])
                                && !EMPTY.equals(instanceIdsArray[index]));
                setOptionalMapEntry(queryParamsMap, INSTANCE_ID + DOT + valueOf(index + ONE), instanceIdsArray[index],
                        isNotBlank(instanceIdsArray[index]) && !NOT_RELEVANT.equalsIgnoreCase(instanceIdsArray[index]) &&
                                !EMPTY.equals(instanceIdsArray[index]));
            }
        }
    }

    private void setSecurityGroupQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        CommonUtils helper = new CommonUtils();
        helper.setPrefixedAndSuffixedCommonQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupNamesString(),
                SECURITY_GROUP, EMPTY, wrapper.getCommonInputs().getDelimiter());
        helper.setPrefixedAndSuffixedCommonQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                SECURITY_GROUP_ID, EMPTY, wrapper.getCommonInputs().getDelimiter());
    }

    private void setBlockDeviceMappingQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        String[] deviceNamesArray = getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getBlockDeviceMappingDeviceNamesString(),
                BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, wrapper.getCommonInputs().getDelimiter());
        String[] virtualNamesArray = getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getBlockDeviceMappingVirtualNamesString(),
                BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING, wrapper.getCommonInputs().getDelimiter());

        if (deviceNamesArray != null && deviceNamesArray.length > START_INDEX
                && virtualNamesArray != null && virtualNamesArray.length > START_INDEX) {
            validateAgainstDifferentArraysLength(deviceNamesArray, virtualNamesArray,
                    BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING);

            for (int index = START_INDEX; index < deviceNamesArray.length; index++) {
                if (NO_DEVICE.equalsIgnoreCase(deviceNamesArray[index])) {
                    queryParamsMap.put(NO_DEVICE, EMPTY);
                } else {
                    queryParamsMap.put(getQueryParamsSpecificString(BLOCK_DEVICE_MAPPING, index) +
                            BLOCK_DEVICE_MAPPING_DEVICE_NAME, deviceNamesArray[index]);
                    setOptionalMapEntry(queryParamsMap, getQueryParamsSpecificString(BLOCK_DEVICE_MAPPING, index) +
                            VIRTUAL_NAME, virtualNamesArray[index], !NOT_RELEVANT.equalsIgnoreCase(virtualNamesArray[index]));
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getDeleteOnTerminationsString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            DELETE_ON_TERMINATIONS_STRING, DELETE_ON_TERMINATION, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getEncryptedString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            ENCRYPTED_STRING, ENCRYPTED, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getSnapshotIdsString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            SNAPSHOT_IDS_STRING, SNAPSHOT_ID, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getVolumeTypesString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            VOLUME_TYPES_STRING, VOLUME_TYPE, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getIopsString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            IOPS_STRING, IOPS, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getVolumeSizesString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            VOLUME_SIZES_STRING, VOLUME_SIZE, deviceNamesArray, index);
                }
            }
        }
    }

    private void setOptionalQueryParam(Map<String, String> queryParamsMap, String inputString, String toTest,
                                       String delimiter, String firstInputName, String secondInputName, String customKey,
                                       String[] referenceArray, int index) {
        String[] inputsArray = getStringsArray(inputString, toTest, delimiter);
        boolean setOptionalQueryParamEntryFlag = getOptionalsSetterFlag(referenceArray, inputsArray, firstInputName, secondInputName);
        setOptionalQueryParamEntry(queryParamsMap, inputsArray, customKey, index, setOptionalQueryParamEntryFlag);
    }

    private void setOptionalQueryParamEntry(Map<String, String> queryParamsMap, String[] inputArray, String customKey,
                                            int index, boolean condition) {
        if (condition && ENCRYPTED.equalsIgnoreCase(customKey) && valueOf(ONE).equalsIgnoreCase(inputArray[index])) {
            queryParamsMap.put(getQueryParamsSpecificString(EBS, index) + customKey, inputArray[index]);
        } else if (condition && VOLUME_TYPE.equalsIgnoreCase(customKey)) {
            queryParamsMap.put(getQueryParamsSpecificString(EBS, index) + customKey, VolumeType.getValue(inputArray[index]));
        } else if (condition && IOPS.equalsIgnoreCase(customKey)
                && queryParamsMap.containsValue(VolumeType.IO1.toString())) {
            queryParamsMap.put(getQueryParamsSpecificString(EBS, index) + customKey, inputArray[index]);
        } else if (condition && VOLUME_SIZE.equalsIgnoreCase(customKey) && queryParamsMap.keySet().toString().contains(BLOCK_DEVICE_MAPPING)) {
            String currentVolumeType = getCurrentVolumeType(queryParamsMap, index);
            String currentValidSize = isBlank(currentVolumeType) ? getValidEbsSize(inputArray[index], STANDARD) :
                    getValidEbsSize(inputArray[index], currentVolumeType);
            setOptionalMapEntry(queryParamsMap, getQueryParamsSpecificString(EBS, index) + customKey, currentValidSize,
                    !NOT_RELEVANT.equalsIgnoreCase(currentValidSize));
        } else if (condition && DELETE_ON_TERMINATION.equalsIgnoreCase(customKey)
                && getEnforcedBooleanCondition(inputArray[index], true) == Boolean.FALSE) {
            queryParamsMap.put(getQueryParamsSpecificString(EBS, index) + customKey, inputArray[index]);
        } else if (condition && SNAPSHOT_ID.equalsIgnoreCase(customKey)) {
            queryParamsMap.put(getQueryParamsSpecificString(EBS, index) + customKey, inputArray[index]);
        }
    }

    private void setEbsOptionalQueryParams(Map<String, String> queryParamsMap, String[] deviceNamesArray, String[] deleteOnTerminationsArray,
                                           String[] volumeIdsArray, String[] noDevicesArray, String[] virtualNamesArray) {
        boolean setNoDevice = noDevicesArray != null && noDevicesArray.length > START_INDEX;
        boolean setDeleteOnTermination = deleteOnTerminationsArray != null && deleteOnTerminationsArray.length > START_INDEX;

        if (isNotEmpty(deviceNamesArray)) {
            for (int index = START_INDEX; index < deviceNamesArray.length; index++) {
                setOptionalMapEntry(queryParamsMap, getQueryParamsSpecificString(BLOCK_DEVICE_MAPPING, index) +
                        BLOCK_DEVICE_MAPPING_DEVICE_NAME, deviceNamesArray[index], isNotBlank(deviceNamesArray[index]));
                setOptionalMapEntry(queryParamsMap, getQueryParamsSpecificString(BLOCK_DEVICE_MAPPING, index) +
                        VIRTUAL_NAME, virtualNamesArray[index], (virtualNamesArray.length > START_INDEX
                        && isNotBlank(virtualNamesArray[index]) && !NOT_RELEVANT.equalsIgnoreCase(virtualNamesArray[index])));
                setOptionalMapEntry(queryParamsMap, getQueryParamsSpecificString(EBS, index) + VOLUME_ID,
                        volumeIdsArray[index], isNotBlank(volumeIdsArray[index]));

                if (setNoDevice) {
                    setOptionalMapEntry(queryParamsMap, getQueryParamsSpecificString(BLOCK_DEVICE_MAPPING, index) +
                            NO_DEVICE, noDevicesArray[index], NO_DEVICE.equalsIgnoreCase(noDevicesArray[index]));
                }
                if (setDeleteOnTermination) {
                    setOptionalMapEntry(queryParamsMap, getQueryParamsSpecificString(EBS, index) + DELETE_ON_TERMINATION,
                            deleteOnTerminationsArray[index], Boolean.FALSE == getEnforcedBooleanCondition(deleteOnTerminationsArray[index], true));
                }
            }
        }
    }

    private void setAttribute(Map<String, String> queryParamsMap, String attribute) {
        validateAttribute(attribute);
        setOptionalMapEntry(queryParamsMap, ATTRIBUTE, attribute, !NOT_RELEVANT.equalsIgnoreCase(attribute));
    }

    private void validateAttribute(String attribute) {
        List<String> validAttributesList = Arrays.asList(LOWER_CASE_KERNEL, LOWER_CASE_RAMDISK, LOWER_CASE_USER_DATA,
                LOWER_CASE_DISABLE_API_TERMINATION, LOWER_CASE_INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR);

        if (isNotBlank(attribute) && !NOT_RELEVANT.equalsIgnoreCase(attribute) && !validAttributesList.contains(attribute)) {
            throw new RuntimeException("Unsupported attribute: [" + attribute + "]. " +
                    "Valid values: | kernel | ramdisk | userData | disableApiTermination | instanceInitiatedShutdownBehavior |");
        }
    }
}