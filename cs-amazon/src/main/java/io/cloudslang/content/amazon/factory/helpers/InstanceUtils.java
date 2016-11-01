package io.cloudslang.content.amazon.factory.helpers;

import io.cloudslang.content.amazon.entities.aws.InstanceInitiatedShutdownBehavior;
import io.cloudslang.content.amazon.entities.aws.InstanceType;
import io.cloudslang.content.amazon.entities.aws.VolumeType;
import io.cloudslang.content.amazon.entities.constants.Inputs;
import io.cloudslang.content.amazon.entities.inputs.InputsWrapper;
import io.cloudslang.content.amazon.utils.InputsUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SUBNET_ID;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.BLOCK_DEVICE_MAPPING;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.DELETE_ON_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.ENCRYPTED;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.FORCE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.IMAGE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.IOPS;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.NETWORK_INTERFACE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SECURITY_GROUP;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SECURITY_GROUP_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.SNAPSHOT_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.STANDARD;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VALUE;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VOLUME_ID;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.VOLUME_TYPE;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EBS;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.DOT;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;

import static io.cloudslang.content.amazon.entities.constants.Constants.Values.START_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Constants.Values.ONE;

/**
 * Created by Mihai Tusa.
 * 9/15/2016.
 */
public class InstanceUtils {
    private static final String ATTRIBUTE = "Attribute";
    private static final String CLIENT_TOKEN = "ClientToken";
    private static final String BLOCK_DEVICE_MAPPING_DEVICE_NAME = "DeviceName";
    private static final String DISABLE_API_TERMINATION = "DisableApiTermination";
    private static final String EBS_OPTIMIZED = "EbsOptimized";
    private static final String ENA_SUPPORT = "EnaSupport";
    private static final String GROUP_ID = "GroupId";
    private static final String IAM_INSTANCE_PROFILE_ARN = "IamInstanceProfile.Arn";
    private static final String IAM_INSTANCE_PROFILE_NAME = "IamInstanceProfile.Name";
    private static final String INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR = "InstanceInitiatedShutdownBehavior";
    private static final String INSTANCE_TYPE = "InstanceType";
    private static final String KERNEL = "Kernel";
    private static final String KERNEL_ID = "KernelId";
    private static final String KEY_NAME = "KeyName";
    private static final String MAX_COUNT = "MaxCount";
    private static final String MIN_COUNT = "MinCount";
    private static final String MONITORING_ENABLED = "Monitoring.Enabled";
    private static final String NO_DEVICE = "NoDevice";
    private static final String PLACEMENT_AFFINITY = "Placement.Affinity";
    private static final String PLACEMENT_AVAILABILITY_ZONE = "Placement.AvailabilityZone";
    private static final String PLACEMENT_GROUP_NAME = "Placement.GroupName";
    private static final String PLACEMENT_HOST_ID = "Placement.HostId";
    private static final String PLACEMENT_TENANCY = "Placement.Tenancy";
    private static final String RAMDISK = "Ramdisk";
    private static final String RAMDISK_ID = "RamdiskId";
    private static final String SOURCE_DEST_CHECK = "SourceDestCheck";
    private static final String SRIOV_NET_SUPPORT = "SriovNetSupport";
    private static final String USER_DATA = "UserData";
    private static final String VOLUME_SIZE = "VolumeSize";
    private static final String VIRTUAL_NAME = "VirtualName";

    public Map<String, String> getModifyInstanceAttributeQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());
        queryParamsMap.put(INSTANCE_ID, wrapper.getCustomInputs().getInstanceId());
        setAttribute(queryParamsMap, wrapper.getInstanceInputs().getAttribute());
        new IamUtils().setSecurityGroupsRelatedQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                GROUP_ID, EMPTY, wrapper.getCommonInputs().getDelimiter());
        setModifyInstanceAttributeEbsSpecs(queryParamsMap, wrapper);

        InputsUtil.setOptionalMapEntry(queryParamsMap, VALUE, wrapper.getInstanceInputs().getAttributeValue(),
                isNotBlank(wrapper.getInstanceInputs().getAttributeValue()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, DISABLE_API_TERMINATION + DOT + VALUE,
                valueOf(wrapper.getInstanceInputs().isDisableApiTermination()), wrapper.getInstanceInputs().isDisableApiTermination());
        InputsUtil.setOptionalMapEntry(queryParamsMap, EBS_OPTIMIZED + DOT + VALUE,
                valueOf(wrapper.getEbsInputs().isEbsOptimized()), wrapper.getEbsInputs().isEbsOptimized());
        InputsUtil.setOptionalMapEntry(queryParamsMap, ENA_SUPPORT + DOT + VALUE,
                valueOf(wrapper.getInstanceInputs().isEnaSupport()), wrapper.getInstanceInputs().isEnaSupport());
        InputsUtil.setOptionalMapEntry(queryParamsMap, INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR + DOT + VALUE,
                wrapper.getInstanceInputs().getInstanceInitiatedShutdownBehavior(),
                (isNotBlank(wrapper.getInstanceInputs().getInstanceInitiatedShutdownBehavior()))
                        && !NOT_RELEVANT.equalsIgnoreCase(wrapper.getInstanceInputs().getInstanceInitiatedShutdownBehavior()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, INSTANCE_TYPE + DOT + VALUE, wrapper.getCustomInputs().getInstanceType(),
                (isNotBlank(wrapper.getCustomInputs().getInstanceType())
                        && !NOT_RELEVANT.equalsIgnoreCase(wrapper.getCustomInputs().getInstanceType())));
        InputsUtil.setOptionalMapEntry(queryParamsMap, KERNEL + DOT + VALUE, wrapper.getInstanceInputs().getKernel(),
                isNotBlank(wrapper.getInstanceInputs().getKernel()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, RAMDISK + DOT + VALUE, wrapper.getInstanceInputs().getRamdisk(),
                isNotBlank(wrapper.getInstanceInputs().getRamdisk()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, SOURCE_DEST_CHECK + DOT + VALUE, wrapper.getInstanceInputs().getSourceDestinationCheck(),
                (isNotBlank(wrapper.getInstanceInputs().getSourceDestinationCheck()) && !NOT_RELEVANT.equalsIgnoreCase(wrapper.getInstanceInputs().getSourceDestinationCheck())));
        InputsUtil.setOptionalMapEntry(queryParamsMap, SRIOV_NET_SUPPORT + DOT + VALUE, wrapper.getInstanceInputs().getSriovNetSupport(),
                isNotBlank(wrapper.getInstanceInputs().getSriovNetSupport()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, USER_DATA + DOT + VALUE, wrapper.getInstanceInputs().getUserData(),
                isNotBlank(wrapper.getInstanceInputs().getUserData()));

        return queryParamsMap;
    }

    private void setModifyInstanceAttributeEbsSpecs(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        String delimiter = wrapper.getCommonInputs().getDelimiter();

        String[] deviceNamesArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getBlockDeviceMappingDeviceNamesString(),
                Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, delimiter);
        String[] deleteOnTerminationsArray = InputsUtil.getStringsArray(wrapper.getEbsInputs().getDeleteOnTerminationsString(), EMPTY, delimiter);
        String[] volumeIdsArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getVolumeIdsString(),
                Inputs.EbsInputs.VOLUME_IDS_STRING, delimiter);
        String[] noDevicesArray = InputsUtil.getStringsArray(wrapper.getEbsInputs().getNoDevicesString(), EMPTY, delimiter);
        String[] virtualNamesArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getBlockDeviceMappingVirtualNamesString(),
                Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING, delimiter);

        InputsUtil.validateAgainstDifferentArraysLength(deviceNamesArray, deleteOnTerminationsArray,
                Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, Inputs.EbsInputs.DELETE_ON_TERMINATIONS_STRING);
        InputsUtil.validateAgainstDifferentArraysLength(deviceNamesArray, volumeIdsArray,
                Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, Inputs.EbsInputs.VOLUME_IDS_STRING);
        InputsUtil.validateAgainstDifferentArraysLength(deviceNamesArray, noDevicesArray,
                Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, Inputs.EbsInputs.NO_DEVICES_STRING);
        InputsUtil.validateAgainstDifferentArraysLength(deviceNamesArray, virtualNamesArray,
                Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING);

        setEbsOptionalQueryParams(queryParamsMap, deviceNamesArray, deleteOnTerminationsArray, volumeIdsArray, noDevicesArray, virtualNamesArray);
    }

    public Map<String, String> getRebootInstancesQueryParamsMap(InputsWrapper wrapper) {
        return getRebootStartStopTerminateCommonQueryParamsMap(wrapper);
    }

    public Map<String, String> getRunInstancesQueryParamsMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

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

        InputsUtil.setOptionalMapEntry(queryParamsMap, CLIENT_TOKEN, wrapper.getInstanceInputs().getClientToken(),
                isNotBlank(wrapper.getInstanceInputs().getClientToken()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, IAM_INSTANCE_PROFILE_ARN, wrapper.getIamInputs().getIamInstanceProfileArn(),
                isNotBlank(wrapper.getIamInputs().getIamInstanceProfileArn()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, IAM_INSTANCE_PROFILE_NAME, wrapper.getIamInputs().getIamInstanceProfileName(),
                isNotBlank(wrapper.getIamInputs().getIamInstanceProfileName()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, KERNEL_ID, wrapper.getCustomInputs().getKernelId(),
                isNotBlank(wrapper.getCustomInputs().getKernelId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, KEY_NAME, wrapper.getIamInputs().getKeyPairName(),
                isNotBlank(wrapper.getIamInputs().getKeyPairName()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PLACEMENT_AFFINITY, wrapper.getInstanceInputs().getAffinity(),
                isNotBlank(wrapper.getInstanceInputs().getAffinity()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PLACEMENT_AVAILABILITY_ZONE, wrapper.getCustomInputs().getAvailabilityZone(),
                isNotBlank(wrapper.getCustomInputs().getAvailabilityZone()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PLACEMENT_GROUP_NAME, wrapper.getInstanceInputs().getPlacementGroupName(),
                isNotBlank(wrapper.getInstanceInputs().getPlacementGroupName()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PLACEMENT_HOST_ID, wrapper.getCustomInputs().getHostId(),
                isNotBlank(wrapper.getCustomInputs().getHostId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PLACEMENT_TENANCY, wrapper.getInstanceInputs().getTenancy(),
                !NOT_RELEVANT.equalsIgnoreCase(wrapper.getInstanceInputs().getTenancy()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, RAMDISK_ID, wrapper.getCustomInputs().getRamdiskId(),
                isNotBlank(wrapper.getCustomInputs().getRamdiskId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, USER_DATA, wrapper.getInstanceInputs().getUserData(),
                isNotBlank(wrapper.getInstanceInputs().getUserData()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, SUBNET_ID, wrapper.getCustomInputs().getSubnetId(),
                isNotBlank(wrapper.getCustomInputs().getSubnetId()));

        setBlockDeviceMappingQueryParams(queryParamsMap, wrapper);
        setNetworkInterfaceQueryParams(queryParamsMap, wrapper);

        if (!queryParamsMap.keySet().toString().contains(NETWORK_INTERFACE)) {
            setSecurityGroupQueryParams(queryParamsMap, wrapper);
        }

        return queryParamsMap;
    }

    public Map<String, String> getStartInstancesQueryParamsMap(InputsWrapper wrapper) {
        return getRebootStartStopTerminateCommonQueryParamsMap(wrapper);
    }

    public Map<String, String> getStopInstancesQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = getRebootStartStopTerminateCommonQueryParamsMap(wrapper);
        InputsUtil.setOptionalMapEntry(queryParamsMap, FORCE, valueOf(wrapper.getInstanceInputs().isForceStop()),
                wrapper.getInstanceInputs().isForceStop());

        return queryParamsMap;
    }

    public Map<String, String> getTerminateInstancesQueryParamsMap(InputsWrapper wrapper) {
        return getRebootStartStopTerminateCommonQueryParamsMap(wrapper);
    }

    private Map<String, String> getRebootStartStopTerminateCommonQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        String[] instanceIdsArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getCustomInputs().getInstanceId(),
                Inputs.CustomInputs.INSTANCE_ID, wrapper.getCommonInputs().getDelimiter());
        if (instanceIdsArray != null && instanceIdsArray.length > START_INDEX) {
            for (int index = START_INDEX; index < instanceIdsArray.length; index++) {
                queryParamsMap.put(INSTANCE_ID + DOT + valueOf(index + ONE), instanceIdsArray[index]);
            }
        }
        return queryParamsMap;
    }

    private void setSecurityGroupQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        IamUtils helper = new IamUtils();
        helper.setSecurityGroupsRelatedQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupNamesString(),
                SECURITY_GROUP, EMPTY, wrapper.getCommonInputs().getDelimiter());
        helper.setSecurityGroupsRelatedQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                SECURITY_GROUP_ID, EMPTY, wrapper.getCommonInputs().getDelimiter());
    }

    private void setNetworkInterfaceQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        NetworkUtils helper = new NetworkUtils();
        helper.setPrivateIpAddressesQueryParams(queryParamsMap, wrapper, NETWORK_INTERFACE, wrapper.getCommonInputs().getDelimiter());
        helper.setSecondaryPrivateIpAddressCountQueryParams(queryParamsMap, wrapper.getNetworkInputs().getSecondaryPrivateIpAddressCount());
        if (isNotBlank(wrapper.getNetworkInputs().getNetworkInterfacePrivateIpAddress())) {
            new IamUtils().setSecurityGroupsRelatedQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                    NETWORK_INTERFACE, DOT + SECURITY_GROUP_ID, wrapper.getCommonInputs().getDelimiter());
        }
    }

    private void setBlockDeviceMappingQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) throws Exception {
        String[] deviceNamesArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getBlockDeviceMappingDeviceNamesString(),
                Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, wrapper.getCommonInputs().getDelimiter());
        String[] virtualNamesArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getBlockDeviceMappingVirtualNamesString(),
                Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING, wrapper.getCommonInputs().getDelimiter());

        if (deviceNamesArray != null && deviceNamesArray.length > START_INDEX
                && virtualNamesArray != null && virtualNamesArray.length > START_INDEX) {
            InputsUtil.validateAgainstDifferentArraysLength(deviceNamesArray, virtualNamesArray,
                    Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING);

            for (int index = START_INDEX; index < deviceNamesArray.length; index++) {
                if (NO_DEVICE.equalsIgnoreCase(deviceNamesArray[index])) {
                    queryParamsMap.put(NO_DEVICE, EMPTY);
                } else {
                    queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(BLOCK_DEVICE_MAPPING, index) +
                            BLOCK_DEVICE_MAPPING_DEVICE_NAME, deviceNamesArray[index]);
                    InputsUtil.setOptionalMapEntry(queryParamsMap, InputsUtil.getQueryParamsSpecificString(BLOCK_DEVICE_MAPPING, index) +
                            VIRTUAL_NAME, virtualNamesArray[index], !NOT_RELEVANT.equalsIgnoreCase(virtualNamesArray[index]));

                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getDeleteOnTerminationsString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.DELETE_ON_TERMINATIONS_STRING, DELETE_ON_TERMINATION, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getEncryptedString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.ENCRYPTED_STRING, ENCRYPTED, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getSnapshotIdsString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.SNAPSHOT_IDS_STRING, SNAPSHOT_ID, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getVolumeTypesString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.VOLUME_TYPES_STRING, VOLUME_TYPE, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getIopsString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.IOPS_STRING, IOPS, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getVolumeSizesString(), EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.VOLUME_SIZES_STRING, VOLUME_SIZE, deviceNamesArray, index);
                }
            }
        }
    }

    private void setOptionalQueryParam(Map<String, String> queryParamsMap, String inputString, String toTest,
                                       String delimiter, String firstInputName, String secondInputName, String customKey,
                                       String[] referenceArray, int index) throws Exception {
        String[] inputsArray = InputsUtil.getStringsArray(inputString, toTest, delimiter);
        boolean setOptionalQueryParamEntryFlag = getOptionalsSetterFlag(referenceArray, inputsArray, firstInputName, secondInputName);
        setOptionalQueryParamEntry(queryParamsMap, inputsArray, customKey, index, setOptionalQueryParamEntryFlag);
    }

    private void setOptionalQueryParamEntry(Map<String, String> queryParamsMap, String[] inputArray, String customKey,
                                            int index, boolean condition) {
        if (condition && ENCRYPTED.equalsIgnoreCase(customKey) && valueOf(ONE).equalsIgnoreCase(inputArray[index])) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(EBS, index) + customKey, inputArray[index]);
        } else if (condition && VOLUME_TYPE.equalsIgnoreCase(customKey)) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(EBS, index) + customKey, VolumeType.getValue(inputArray[index]));
        } else if (condition && IOPS.equalsIgnoreCase(customKey)
                && queryParamsMap.containsValue(VolumeType.IO1.toString())) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(EBS, index) + customKey, inputArray[index]);
        } else if (condition && VOLUME_SIZE.equalsIgnoreCase(customKey) && queryParamsMap.keySet().toString().contains(BLOCK_DEVICE_MAPPING)) {
            String currentVolumeType = getCurrentVolumeType(queryParamsMap, index);
            String currentValidSize = isBlank(currentVolumeType) ? InputsUtil.getValidEbsSize(inputArray[index], STANDARD) :
                    InputsUtil.getValidEbsSize(inputArray[index], currentVolumeType);
            InputsUtil.setOptionalMapEntry(queryParamsMap,
                    InputsUtil.getQueryParamsSpecificString(EBS, index) + customKey, currentValidSize,
                    !NOT_RELEVANT.equalsIgnoreCase(currentValidSize));
        } else if (condition && DELETE_ON_TERMINATION.equalsIgnoreCase(customKey)
                && InputsUtil.getEnforcedBooleanCondition(inputArray[index], true) == Boolean.FALSE) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(EBS, index) + customKey, inputArray[index]);
        } else if (condition && SNAPSHOT_ID.equalsIgnoreCase(customKey)) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(EBS, index) + customKey, inputArray[index]);
        }
    }

    private String getCurrentVolumeType(Map<String, String> queryParamsMap, int index) {
        return queryParamsMap.get(BLOCK_DEVICE_MAPPING + DOT + (index + ONE) + DOT + EBS + VOLUME_TYPE);
    }

    private boolean getOptionalsSetterFlag(String[] validationArray, String[] toBeValidatedArray, String firstInput, String secondInput) {
        if (toBeValidatedArray != null) {
            InputsUtil.validateAgainstDifferentArraysLength(validationArray, toBeValidatedArray, firstInput, secondInput);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private void setEbsOptionalQueryParams(Map<String, String> queryParamsMap, String[] deviceNamesArray, String[] deleteOnTerminationsArray,
                                           String[] volumeIdsArray, String[] noDevicesArray, String[] virtualNamesArray) {
        boolean setNoDevice = noDevicesArray != null && noDevicesArray.length > START_INDEX;
        boolean setDeleteOnTermination = deleteOnTerminationsArray != null && deleteOnTerminationsArray.length > START_INDEX;

        if (deviceNamesArray != null && deviceNamesArray.length > START_INDEX) {
            for (int index = START_INDEX; index < deviceNamesArray.length; index++) {
                InputsUtil.setOptionalMapEntry(queryParamsMap, InputsUtil.getQueryParamsSpecificString(BLOCK_DEVICE_MAPPING, index) +
                        BLOCK_DEVICE_MAPPING_DEVICE_NAME, deviceNamesArray[index], isNotBlank(deviceNamesArray[index]));
                InputsUtil.setOptionalMapEntry(queryParamsMap, InputsUtil.getQueryParamsSpecificString(BLOCK_DEVICE_MAPPING, index) +
                        VIRTUAL_NAME, virtualNamesArray[index], (virtualNamesArray.length > START_INDEX
                        && isNotBlank(virtualNamesArray[index]) && !NOT_RELEVANT.equalsIgnoreCase(virtualNamesArray[index])));
                InputsUtil.setOptionalMapEntry(queryParamsMap, InputsUtil.getQueryParamsSpecificString(EBS, index) + VOLUME_ID,
                        volumeIdsArray[index], isNotBlank(volumeIdsArray[index]));

                if (setNoDevice) {
                    InputsUtil.setOptionalMapEntry(queryParamsMap, InputsUtil.getQueryParamsSpecificString(BLOCK_DEVICE_MAPPING, index) +
                            NO_DEVICE, noDevicesArray[index], NO_DEVICE.equalsIgnoreCase(noDevicesArray[index]));
                }
                if (setDeleteOnTermination) {
                    InputsUtil.setOptionalMapEntry(queryParamsMap, InputsUtil.getQueryParamsSpecificString(EBS, index) + DELETE_ON_TERMINATION,
                            deleteOnTerminationsArray[index], Boolean.FALSE == InputsUtil.getEnforcedBooleanCondition(deleteOnTerminationsArray[index], true));
                }
            }
        }
    }

    private void setAttribute(Map<String, String> queryParamsMap, String attribute) {
        List<String> validAttributesList = Arrays.asList(Inputs.InstanceInputs.KERNEL, Inputs.InstanceInputs.RAMDISK,
                Inputs.InstanceInputs.USER_DATA, Inputs.InstanceInputs.DISABLE_API_TERMINATION,
                Inputs.InstanceInputs.INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR);

        if (isNotBlank(attribute) && !NOT_RELEVANT.equalsIgnoreCase(attribute) && !validAttributesList.contains(attribute)) {
            throw new RuntimeException("Unsupported attribute: [" + attribute + "]. " +
                    "Valid values: | kernel | ramdisk | userData | disableApiTermination | instanceInitiatedShutdownBehavior |");
        }

        InputsUtil.setOptionalMapEntry(queryParamsMap, ATTRIBUTE, attribute, !NOT_RELEVANT.equalsIgnoreCase(attribute));
    }
}