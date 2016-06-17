package io.cloudslang.content.jclouds.services.helpers;

import com.google.common.collect.Multimap;
import io.cloudslang.content.jclouds.entities.*;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;
import org.jclouds.ContextBuilder;

import java.io.Closeable;

/**
 * Created by Mihai Tusa.
 * 6/15/2016.
 */
public class Utils {
    public <T extends Closeable> T getApi(ContextBuilder contextBuilder, Class<T> valueType) {
        return contextBuilder.buildApi(valueType);
    }

    void updateInstanceFiltersMap(InstanceInputs instanceInputs, Multimap<String, String> filtersMap, String delimiter) {
        setInstanceRelevantFilters(instanceInputs, filtersMap);
        setTagFilters(instanceInputs, filtersMap, delimiter);

        updateFiltersMapEntry(filtersMap, CommonFilters.KERNEL_ID.getValue(), instanceInputs.getCustomInputs().getKernelId());
        updateFiltersMapEntry(filtersMap, CommonFilters.OWNER_ID.getValue(), instanceInputs.getCustomInputs().getOwnerId());
        updateFiltersMapEntry(filtersMap, CommonFilters.PRODUCT_CODE.getValue(), instanceInputs.getCustomInputs().getProductCode());
        updateFiltersMapEntry(filtersMap, CommonFilters.PRODUCT_CODE_TYPE.getValue(), instanceInputs.getCustomInputs().getProductCodeType());
        updateFiltersMapEntry(filtersMap, CommonFilters.RAMDISK_ID.getValue(), instanceInputs.getCustomInputs().getRamdiskId());
        updateFiltersMapEntry(filtersMap, CommonFilters.ROOT_DEVICE_NAME.getValue(), instanceInputs.getCustomInputs().getRootDeviceName());
        updateFiltersMapEntry(filtersMap, CommonFilters.ROOT_DEVICE_TYPE.getValue(), instanceInputs.getCustomInputs().getRootDeviceType());
        updateFiltersMapEntry(filtersMap, CommonFilters.STATE_REASON_CODE.getValue(), instanceInputs.getCustomInputs().getStateReasonCode());
        updateFiltersMapEntry(filtersMap, CommonFilters.STATE_REASON_MESSAGE.getValue(), instanceInputs.getCustomInputs().getStateReasonMessage());

        updateFiltersMapEntry(filtersMap, InstanceFilters.AFFINITY.getValue(), instanceInputs.getAffinity());
        updateFiltersMapEntry(filtersMap, InstanceFilters.AVAILABILITY_ZONE.getValue(), instanceInputs.getAvailabilityZone());
        updateFiltersMapEntry(filtersMap, InstanceFilters.CLIENT_TOKEN.getValue(), instanceInputs.getClientToken());
        updateFiltersMapEntry(filtersMap, InstanceFilters.DNS_NAME.getValue(), instanceInputs.getDnsName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.GROUP_ID.getValue(), instanceInputs.getCustomInputs().getGroupId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.GROUP_NAME.getValue(), instanceInputs.getGroupName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.HOST_ID.getValue(), instanceInputs.getCustomInputs().getHostId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.IAM_INSTANCE_PROFILE_ARN.getValue(), instanceInputs.getIamArn());
        updateFiltersMapEntry(filtersMap, InstanceFilters.IMAGE_ID.getValue(), instanceInputs.getCustomInputs().getImageId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_ID.getValue(), instanceInputs.getCustomInputs().getInstanceId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_LIFECYCLE.getValue(), instanceInputs.getInstanceLifecycle());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_TYPE.getValue(), instanceInputs.getInstanceType());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_GROUP_ID.getValue(), instanceInputs.getInstanceGroupId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_GROUP_NAME.getValue(), instanceInputs.getInstanceGroupName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.IP_ADDRESS.getValue(), instanceInputs.getIpAddress());
        updateFiltersMapEntry(filtersMap, InstanceFilters.KEY_NAME.getValue(), instanceInputs.getKeyName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.LAUNCH_INDEX.getValue(), instanceInputs.getLaunchIndex());
        updateFiltersMapEntry(filtersMap, InstanceFilters.LAUNCH_TIME.getValue(), instanceInputs.getLaunchTime());
        updateFiltersMapEntry(filtersMap, InstanceFilters.PLACEMENT_GROUP_NAME.getValue(), instanceInputs.getPlacementGroupName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.PRIVATE_DNS_NAME.getValue(), instanceInputs.getPrivateDnsName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.PRIVATE_IP_ADDRESS.getValue(), instanceInputs.getPrivateIpAddress());
        updateFiltersMapEntry(filtersMap, InstanceFilters.REASON.getValue(), instanceInputs.getReason());
        updateFiltersMapEntry(filtersMap, InstanceFilters.REQUESTER_ID.getValue(), instanceInputs.getRequesterId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.SUBNET_ID.getValue(), instanceInputs.getCustomInputs().getSubnetId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.VPC_ID.getValue(), instanceInputs.getCustomInputs().getVpcId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.RESERVATION_ID.getValue(), instanceInputs.getCustomInputs().getReservationId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.SPOT_INSTANCE_REQUEST_ID.getValue(), instanceInputs.getSpotInstanceRequestId());

        updateFiltersMapEntry(filtersMap, Association.PUBLIC_IP.getValue(), instanceInputs.getPublicIp());
        updateFiltersMapEntry(filtersMap, Association.IP_OWNER_ID.getValue(), instanceInputs.getIpOwnerId());
        updateFiltersMapEntry(filtersMap, Association.ALLOCATION_ID.getValue(), instanceInputs.getCustomInputs().getAllocationId());
        updateFiltersMapEntry(filtersMap, Association.ASSOCIATION_ID.getValue(), instanceInputs.getCustomInputs().getAssociationId());

        updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.ATTACH_TIME.getValue(), instanceInputs.getCustomInputs().getAttachTime());
        updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.DEVICE_NAME.getValue(), instanceInputs.getCustomInputs().getBlockMappingDeviceName());
        updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.VOLUME_ID.getValue(), instanceInputs.getCustomInputs().getVolumeId());

        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.DESCRIPTION.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceDescription());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.SUBNET_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceSubnetId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.VPC_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceVpcId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.INTERFACE_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.OWNER_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceOwnerId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.AVAILABILITY_ZONE.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceAvailabilityZone());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.REQUESTER_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceRequesterId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.REQUESTER_MANAGED.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceRequesterManaged());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.MAC_ADDRESS.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceMacAddress());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.PRIVATE_DNS_NAME.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfacePrivateDnsName());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.SOURCE_DEST_CHECK.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceSourceDestinationCheck());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.GROUP_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceGroupId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.GROUP_NAME.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceGroupName());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_ATTACHMENT_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceAttachmentId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_INSTANCE_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceInstanceId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_INSTANCE_OWNER_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceInstanceOwnerId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ADDRESSES_PRIVATE_IP_ADDRESS.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfacePrivateIpAddress());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_DEVICE_INDEX.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceDeviceIndex());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_ATTACH_TIME.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceAttachTime());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_DELETE_ON_TERMINATION.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceDeleteOnTermination());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ADDRESSES_PRIMARY.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceAddressesPrimary());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ADDRESSES_ASSOCIATION_PUBLIC_IP.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfacePublicIp());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ADDRESSES_ASSOCIATION_IP_OWNER_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceIpOwnerId());
    }

    private void setInstanceRelevantFilters(InstanceInputs instanceInputs, Multimap<String, String> filtersMap) {
        if (!Constants.Miscellaneous.NOT_RELEVANT_INT_CODE.equals(instanceInputs.getInstanceStateCode())) {
            updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_STATE_CODE.getValue(), instanceInputs.getInstanceStateCode());
        }

        addFiltersMapRelevantEntry(filtersMap, CommonFilters.ARCHITECTURE.getValue(), instanceInputs.getCustomInputs().getArchitecture());
        addFiltersMapRelevantEntry(filtersMap, CommonFilters.HYPERVISOR.getValue(), instanceInputs.getCustomInputs().getHypervisor());
        addFiltersMapRelevantEntry(filtersMap, CommonFilters.PLATFORM.getValue(), instanceInputs.getCustomInputs().getPlatform());
        addFiltersMapRelevantEntry(filtersMap, CommonFilters.VIRTUALIZATION_TYPE.getValue(), instanceInputs.getCustomInputs().getVirtualizationType());

        addFiltersMapRelevantEntry(filtersMap, BlockDeviceMappingFilters.STATUS.getValue(), instanceInputs.getCustomInputs().getBlockDeviceMappingStatus());
        addFiltersMapRelevantEntry(filtersMap, BlockDeviceMappingFilters.DELETE_ON_TERMINATION.getValue(), instanceInputs.getCustomInputs().getDeleteOnTermination());

        addFiltersMapRelevantEntry(filtersMap, InstanceFilters.TENANCY.getValue(), instanceInputs.getTenancy());
        addFiltersMapRelevantEntry(filtersMap, InstanceFilters.INSTANCE_STATE_NAME.getValue(), instanceInputs.getInstanceStateName());
        addFiltersMapRelevantEntry(filtersMap, InstanceFilters.MONITORING_STATE.getValue(), instanceInputs.getMonitoringState());
        addFiltersMapRelevantEntry(filtersMap, InstanceFilters.SOURCE_DESTINATION_CHECK.getValue(),
                instanceInputs.getSourceDestinationCheck());

        addFiltersMapRelevantEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_STATUS.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceAttachmentStatus());
        addFiltersMapRelevantEntry(filtersMap, NetworkInterfaceFilters.STATUS.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceStatus());
    }

    void updateImageFiltersMap(ImageInputs imageInputs, Multimap<String, String> filtersMap, String delimiter) {
        setImageRelevantFilters(imageInputs, filtersMap);
        setTagFilters(imageInputs, filtersMap, delimiter);

        updateFiltersMapEntry(filtersMap, CommonFilters.HYPERVISOR.getValue(), imageInputs.getCustomInputs().getHypervisor());
        updateFiltersMapEntry(filtersMap, CommonFilters.KERNEL_ID.getValue(), imageInputs.getCustomInputs().getKernelId());
        updateFiltersMapEntry(filtersMap, CommonFilters.OWNER_ALIAS.getValue(), imageInputs.getCustomInputs().getOwnerAlias());
        updateFiltersMapEntry(filtersMap, CommonFilters.OWNER_ID.getValue(), imageInputs.getCustomInputs().getOwnerId());
        updateFiltersMapEntry(filtersMap, CommonFilters.PLATFORM.getValue(), imageInputs.getCustomInputs().getPlatform());
        updateFiltersMapEntry(filtersMap, CommonFilters.PRODUCT_CODE.getValue(), imageInputs.getCustomInputs().getProductCode());
        updateFiltersMapEntry(filtersMap, CommonFilters.RAMDISK_ID.getValue(), imageInputs.getCustomInputs().getRamdiskId());
        updateFiltersMapEntry(filtersMap, CommonFilters.ROOT_DEVICE_NAME.getValue(), imageInputs.getCustomInputs().getRootDeviceName());
        updateFiltersMapEntry(filtersMap, CommonFilters.STATE_REASON_CODE.getValue(), imageInputs.getCustomInputs().getStateReasonCode());
        updateFiltersMapEntry(filtersMap, CommonFilters.STATE_REASON_MESSAGE.getValue(), imageInputs.getCustomInputs().getStateReasonMessage());

        updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.DELETE_ON_TERMINATION.getValue(), imageInputs.getCustomInputs().getDeleteOnTermination());
        updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.DEVICE_NAME.getValue(), imageInputs.getCustomInputs().getBlockMappingDeviceName());
        updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.SNAPSHOT_ID.getValue(), imageInputs.getCustomInputs().getBlockDeviceMappingSnapshotId());

        updateFiltersMapEntry(filtersMap, ImageFilters.DESCRIPTION.getValue(), imageInputs.getDescription());
        updateFiltersMapEntry(filtersMap, ImageFilters.ID.getValue(), imageInputs.getCustomInputs().getImageId());
        updateFiltersMapEntry(filtersMap, ImageFilters.MANIFEST_LOCATION.getValue(), imageInputs.getManifestLocation());
        updateFiltersMapEntry(filtersMap, ImageFilters.NAME.getValue(), imageInputs.getImageName());
    }

    private void setImageRelevantFilters(ImageInputs imageInputs, Multimap<String, String> filtersMap) {
        addFiltersMapRelevantEntry(filtersMap, CommonFilters.ARCHITECTURE.getValue(), imageInputs.getCustomInputs().getArchitecture());
        addFiltersMapRelevantEntry(filtersMap, CommonFilters.PRODUCT_CODE_TYPE.getValue(), imageInputs.getCustomInputs().getProductCodeType());
        addFiltersMapRelevantEntry(filtersMap, CommonFilters.ROOT_DEVICE_TYPE.getValue(), imageInputs.getCustomInputs().getRootDeviceType());

        addFiltersMapRelevantEntry(filtersMap, BlockDeviceMappingFilters.VOLUME_SIZE.getValue(), imageInputs.getCustomInputs().getVolumeSize());
        addFiltersMapRelevantEntry(filtersMap, BlockDeviceMappingFilters.VOLUME_TYPE.getValue(), imageInputs.getCustomInputs().getVolumeType());

        addFiltersMapRelevantEntry(filtersMap, ImageFilters.TYPE.getValue(), imageInputs.getCustomInputs().getVolumeType());
        addFiltersMapRelevantEntry(filtersMap, ImageFilters.IS_PUBLIC.getValue(), imageInputs.getIsPublic());
        addFiltersMapRelevantEntry(filtersMap, ImageFilters.STATE.getValue(), imageInputs.getState());
    }

    private void addFiltersMapRelevantEntry(Multimap<String, String> filtersMap, String filterKey, String filterValue) {
        if (StringUtils.isNotBlank(filterValue) && !Constants.Miscellaneous.NOT_RELEVANT.equalsIgnoreCase(filterValue)) {
            filtersMap.put(filterKey, filterValue);
        }
    }

    private void updateFiltersMapEntry(Multimap<String, String> map, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            map.put(key, value);
        }
    }

    private <T> void setTagFilters(T inputs, Multimap<String, String> filtersMap, String delimiter) {
        String[] tagKeys = null;
        String[] tagValues = null;

        if (inputs instanceof InstanceInputs) {
            tagKeys = InputsUtil.getStringsArray(((InstanceInputs) inputs).getCustomInputs().getKeyTagsString(), Constants.Miscellaneous.EMPTY, delimiter);
            tagValues = InputsUtil.getStringsArray(((InstanceInputs) inputs).getCustomInputs().getValueTagsString(), Constants.Miscellaneous.EMPTY, delimiter);
        } else if (inputs instanceof ImageInputs) {
            tagKeys = InputsUtil.getStringsArray(((ImageInputs) inputs).getCustomInputs().getKeyTagsString(), Constants.Miscellaneous.EMPTY, delimiter);
            tagValues = InputsUtil.getStringsArray(((ImageInputs) inputs).getCustomInputs().getValueTagsString(), Constants.Miscellaneous.EMPTY, delimiter);
        }

        updateTagFilters(filtersMap, tagKeys, tagValues);
    }

    private void updateTagFilters(Multimap<String, String> filtersMap, String[] tagKeys, String[] tagValues) {
        if (tagKeys != null && tagValues != null) {
            if (tagKeys.length != tagValues.length) {
                throw new RuntimeException(Constants.ErrorMessages.TAG_KEYS_TAG_VALUES_MISMATCH);
            }

            for (int counter = 0; counter < tagKeys.length - 1; counter++) {
                filtersMap.put(Constants.Miscellaneous.TAG, tagKeys[counter] + Constants.Miscellaneous.EQUAL + tagValues[counter]);
            }
        }
    }
}