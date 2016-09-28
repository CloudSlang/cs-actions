package io.cloudslang.content.jclouds.factory.helpers;

import io.cloudslang.content.jclouds.entities.aws.VolumeType;
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
 * 9/15/2016.
 */
public class InstanceHelper {
    private static final String CLIENT_TOKEN = "ClientToken";
    private static final String BLOCK_DEVICE_MAPPING_DEVICE_NAME = "DeviceName";
    private static final String DISABLE_API_TERMINATION = "DisableApiTermination";
    private static final String EBS_OPTIMIZED = "EbsOptimized";
    private static final String IAM_INSTANCE_PROFILE_ARN = "IamInstanceProfile.Arn";
    private static final String IAM_INSTANCE_PROFILE_NAME = "IamInstanceProfile.Name";
    private static final String INSTANCE_ID = "InstanceId";
    private static final String INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR = "InstanceInitiatedShutdownBehavior";
    private static final String INSTANCE_TYPE = "InstanceType";
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
    private static final String RAMDISK_ID = "RamdiskId";
    private static final String USER_DATA = "UserData";
    private static final String VOLUME_SIZE = "VolumeSize";
    private static final String VIRTUAL_NAME = "VirtualName";

    public Map<String, String> getRunInstancesQueryParamsMap(InputsWrapper wrapper) throws Exception {
        Map<String, String> queryParamsMap = new LinkedHashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        queryParamsMap.put(DISABLE_API_TERMINATION, String.valueOf(wrapper.getInstanceInputs().isDisableApiTermination()));
        queryParamsMap.put(EBS_OPTIMIZED, String.valueOf(wrapper.getEbsInputs().isEbsOptimized()));
        queryParamsMap.put(Constants.AwsParams.IMAGE_ID, wrapper.getCustomInputs().getImageId());
        queryParamsMap.put(INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR, wrapper.getInstanceInputs().getInstanceInitiatedShutdownBehavior());
        queryParamsMap.put(INSTANCE_TYPE, wrapper.getCustomInputs().getInstanceType());
        queryParamsMap.put(MAX_COUNT, String.valueOf(wrapper.getInstanceInputs().getMaxCount()));
        queryParamsMap.put(MIN_COUNT, String.valueOf(wrapper.getInstanceInputs().getMinCount()));
        queryParamsMap.put(MONITORING_ENABLED, String.valueOf(wrapper.getInstanceInputs().isMonitoring()));

        InputsUtil.setOptionalMapEntry(queryParamsMap, CLIENT_TOKEN, wrapper.getInstanceInputs().getClientToken(),
                StringUtils.isNotBlank(wrapper.getInstanceInputs().getClientToken()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, IAM_INSTANCE_PROFILE_ARN, wrapper.getIamInputs().getIamInstanceProfileArn(),
                StringUtils.isNotBlank(wrapper.getIamInputs().getIamInstanceProfileArn()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, IAM_INSTANCE_PROFILE_NAME, wrapper.getIamInputs().getIamInstanceProfileName(),
                StringUtils.isNotBlank(wrapper.getIamInputs().getIamInstanceProfileName()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, KERNEL_ID, wrapper.getCustomInputs().getKernelId(),
                StringUtils.isNotBlank(wrapper.getCustomInputs().getKernelId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, KEY_NAME, wrapper.getIamInputs().getKeyPairName(),
                StringUtils.isNotBlank(wrapper.getIamInputs().getKeyPairName()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PLACEMENT_AFFINITY, wrapper.getInstanceInputs().getAffinity(),
                StringUtils.isNotBlank(wrapper.getInstanceInputs().getAffinity()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PLACEMENT_AVAILABILITY_ZONE, wrapper.getCustomInputs().getAvailabilityZone(),
                StringUtils.isNotBlank(wrapper.getCustomInputs().getAvailabilityZone()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PLACEMENT_GROUP_NAME, wrapper.getInstanceInputs().getPlacementGroupName(),
                StringUtils.isNotBlank(wrapper.getInstanceInputs().getPlacementGroupName()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PLACEMENT_HOST_ID, wrapper.getCustomInputs().getHostId(),
                StringUtils.isNotBlank(wrapper.getCustomInputs().getHostId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, PLACEMENT_TENANCY, wrapper.getInstanceInputs().getTenancy(),
                !Constants.Miscellaneous.NOT_RELEVANT.equalsIgnoreCase(wrapper.getInstanceInputs().getTenancy()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, RAMDISK_ID, wrapper.getCustomInputs().getRamdiskId(),
                StringUtils.isNotBlank(wrapper.getCustomInputs().getRamdiskId()));
        InputsUtil.setOptionalMapEntry(queryParamsMap, USER_DATA, wrapper.getInstanceInputs().getUserData(),
                StringUtils.isNotBlank(wrapper.getInstanceInputs().getUserData()));

        setBlockDeviceMappingQueryParams(queryParamsMap, wrapper);
        setNetworkInterfaceQueryParams(queryParamsMap, wrapper);

        if (!queryParamsMap.keySet().toString().contains(Constants.AwsParams.NETWORK_INTERFACE)) {
            setSecurityGroupQueryParams(queryParamsMap, wrapper);
        }

        return queryParamsMap;
    }

    public Map<String, String> getStartInstancesQueryParamsMap(InputsWrapper wrapper) {
        Map<String, String> queryParamsMap = new HashMap<>();
        InputsUtil.setCommonQueryParamsMap(queryParamsMap, wrapper.getCommonInputs().getAction(), wrapper.getCommonInputs().getVersion());

        String[] instanceIdsArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getCustomInputs().getInstanceId(),
                Inputs.CustomInputs.INSTANCE_ID, wrapper.getCommonInputs().getDelimiter());
        if (instanceIdsArray != null && instanceIdsArray.length > Constants.Values.START_INDEX){
            for (int index = Constants.Values.START_INDEX; index < instanceIdsArray.length; index++) {
                queryParamsMap.put(INSTANCE_ID + Constants.Miscellaneous.DOT + String.valueOf(index + Constants.Values.ONE),
                        instanceIdsArray[index]);
            }
        }

        return queryParamsMap;
    }

    private void setSecurityGroupQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        IamHelper helper = new IamHelper();
        helper.setSecurityGroupQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupNamesString(),
                Constants.AwsParams.SECURITY_GROUP, Constants.Miscellaneous.EMPTY, wrapper.getCommonInputs().getDelimiter());
        helper.setSecurityGroupQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                Constants.AwsParams.SECURITY_GROUP_ID, Constants.Miscellaneous.EMPTY, wrapper.getCommonInputs().getDelimiter());
    }

    private void setNetworkInterfaceQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) {
        NetworkHelper helper = new NetworkHelper();
        helper.setPrivateIpAddressesQueryParams(queryParamsMap, wrapper, Constants.AwsParams.NETWORK_INTERFACE,
                wrapper.getCommonInputs().getDelimiter());
        helper.setSecondaryPrivateIpAddressCountQueryParams(queryParamsMap, wrapper.getNetworkInputs().getSecondaryPrivateIpAddressCount());
        if (StringUtils.isNotBlank(wrapper.getNetworkInputs().getNetworkInterfacePrivateIpAddress())) {
            new IamHelper().setNetworkSecurityGroupsQueryParams(queryParamsMap, wrapper.getIamInputs().getSecurityGroupIdsString(),
                    wrapper.getCommonInputs().getDelimiter());
        }
    }

    private void setBlockDeviceMappingQueryParams(Map<String, String> queryParamsMap, InputsWrapper wrapper) throws Exception {
        String[] deviceNamesArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getBlockDeviceMappingDeviceNamesString(),
                Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, wrapper.getCommonInputs().getDelimiter());
        String[] virtualNamesArray = InputsUtil.getArrayWithoutDuplicateEntries(wrapper.getEbsInputs().getBlockDeviceMappingVirtualNamesString(),
                Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING, wrapper.getCommonInputs().getDelimiter());

        if (deviceNamesArray != null && deviceNamesArray.length > Constants.Values.START_INDEX
                && virtualNamesArray != null && virtualNamesArray.length > Constants.Values.START_INDEX) {
            InputsUtil.validateAgainstDifferentArraysLength(deviceNamesArray, virtualNamesArray,
                    Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING, Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING);

            for (int index = Constants.Values.START_INDEX; index < deviceNamesArray.length; index++) {
                if (NO_DEVICE.equalsIgnoreCase(deviceNamesArray[index])) {
                    queryParamsMap.put(NO_DEVICE, Constants.Miscellaneous.EMPTY);
                } else {
                    queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(Constants.AwsParams.BLOCK_DEVICE_MAPPING, index) +
                            BLOCK_DEVICE_MAPPING_DEVICE_NAME, deviceNamesArray[index]);
                    InputsUtil.setOptionalMapEntry(queryParamsMap, InputsUtil.getQueryParamsSpecificString(Constants.AwsParams.BLOCK_DEVICE_MAPPING, index) +
                            VIRTUAL_NAME, virtualNamesArray[index], !Constants.Miscellaneous.NOT_RELEVANT.equalsIgnoreCase(virtualNamesArray[index]));

                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getDeleteOnTerminationsString(), Constants.Miscellaneous.EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.DELETE_ON_TERMINATIONS_STRING, Constants.AwsParams.DELETE_ON_TERMINATION, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getEncryptedString(), Constants.Miscellaneous.EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.ENCRYPTED_STRING, Constants.AwsParams.ENCRYPTED, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getSnapshotIdsString(), Constants.Miscellaneous.EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.SNAPSHOT_IDS_STRING, Constants.AwsParams.SNAPSHOT_ID, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getVolumeTypesString(), Constants.Miscellaneous.EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.VOLUME_TYPES_STRING, Constants.AwsParams.VOLUME_TYPE, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getIopsString(), Constants.Miscellaneous.EMPTY,
                            wrapper.getCommonInputs().getDelimiter(), Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING,
                            Inputs.EbsInputs.IOPS_STRING, Constants.AwsParams.IOPS, deviceNamesArray, index);
                    setOptionalQueryParam(queryParamsMap, wrapper.getEbsInputs().getVolumeSizesString(), Constants.Miscellaneous.EMPTY,
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
                                            int index, boolean condition) throws Exception {
        if (condition && Constants.AwsParams.ENCRYPTED.equalsIgnoreCase(customKey)
                && String.valueOf(Constants.Values.ONE).equalsIgnoreCase(inputArray[index])) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(Constants.Miscellaneous.EBS, index) + customKey,
                    inputArray[index]);
        } else if (condition && Constants.AwsParams.VOLUME_TYPE.equalsIgnoreCase(customKey)) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(Constants.Miscellaneous.EBS, index) + customKey,
                    VolumeType.getValue(inputArray[index]));
        } else if (condition && Constants.AwsParams.IOPS.equalsIgnoreCase(customKey)
                && queryParamsMap.containsValue(VolumeType.IO1.toString())) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(Constants.Miscellaneous.EBS, index) + customKey,
                    inputArray[index]);
        } else if (condition && VOLUME_SIZE.equalsIgnoreCase(customKey)
                && queryParamsMap.keySet().toString().contains(Constants.AwsParams.BLOCK_DEVICE_MAPPING)) {
            String currentVolumeType = getCurrentVolumeType(queryParamsMap, index);
            String currentValidSize = StringUtils.isBlank(currentVolumeType) ?
                    InputsUtil.getValidEbsSize(inputArray[index], Constants.AwsParams.STANDARD) :
                    InputsUtil.getValidEbsSize(inputArray[index], currentVolumeType);
            InputsUtil.setOptionalMapEntry(queryParamsMap,
                    InputsUtil.getQueryParamsSpecificString(Constants.Miscellaneous.EBS, index) + customKey, currentValidSize,
                    !Constants.Miscellaneous.NOT_RELEVANT.equalsIgnoreCase(currentValidSize));
        } else if (condition && Constants.AwsParams.DELETE_ON_TERMINATION.equalsIgnoreCase(customKey)
                && Boolean.FALSE == InputsUtil.getEnforcedBooleanCondition(inputArray[index], true)) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(Constants.Miscellaneous.EBS, index) + customKey,
                    inputArray[index]);
        } else if (condition && Constants.AwsParams.SNAPSHOT_ID.equalsIgnoreCase(customKey)) {
            queryParamsMap.put(InputsUtil.getQueryParamsSpecificString(Constants.Miscellaneous.EBS, index) + customKey,
                    inputArray[index]);
        }
    }

    private String getCurrentVolumeType(Map<String, String> queryParamsMap, int index) {
        return queryParamsMap.get(Constants.AwsParams.BLOCK_DEVICE_MAPPING + Constants.Miscellaneous.DOT +
                (index + Constants.Values.ONE) + Constants.Miscellaneous.DOT + Constants.Miscellaneous.EBS +
                Constants.AwsParams.VOLUME_TYPE);
    }

    private boolean getOptionalsSetterFlag(String[] validationArray, String[] toBeValidatedArray, String firstInput,
                                           String secondInput) {
        if (toBeValidatedArray != null) {
            InputsUtil.validateAgainstDifferentArraysLength(validationArray, toBeValidatedArray, firstInput, secondInput);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}