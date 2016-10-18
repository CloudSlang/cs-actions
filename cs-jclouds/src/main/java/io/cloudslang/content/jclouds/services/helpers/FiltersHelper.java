package io.cloudslang.content.jclouds.services.helpers;

import com.google.common.collect.Multimap;
import io.cloudslang.content.jclouds.entities.aws.*;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.ImageInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.jclouds.ContextBuilder;
import org.jclouds.ec2.EC2Api;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static io.cloudslang.content.jclouds.entities.constants.Constants.Values.START_INDEX;

/**
 * Created by Mihai Tusa.
 * 6/15/2016.
 */
public class FiltersHelper {
    private static final String NOT_RELEVANT_INT_CODE = "-1";
    private static final String TAG = "tag:";
    private static final String EQUAL = "=";
    private static final String TAG_KEYS_TAG_VALUES_MISMATCH = "Incorrect supplied values for: [keyTagsString] and/or " +
            "[valueTagsString] inputs. Number of tag keys should be the same with number of tag values.";
    private static final String MAXIMUM_TAGS_LIMIT_EXCEEDED = "Incorrect supplied values for: [keyTagsString] and/or " +
            "[valueTagsString] inputs. A resource can have a maximum of 10 tags.";

    private static final int MAXIMUM_TAGS_LIMIT = 10;

    public EC2Api getEC2Api(ContextBuilder contextBuilder) {
        return contextBuilder.buildApi(EC2Api.class);
    }

    void updateInstanceFiltersMap(InstanceInputs instanceInputs, Multimap<String, String> filtersMap, String delimiter) {
        setInstanceRelevantFilters(instanceInputs, filtersMap);
        setTags(instanceInputs, filtersMap, delimiter);

        updateFiltersMapEntry(filtersMap, CommonFilter.KERNEL_ID.getValue(), instanceInputs.getCustomInputs().getKernelId());
        updateFiltersMapEntry(filtersMap, CommonFilter.OWNER_ID.getValue(), instanceInputs.getCustomInputs().getOwnerId());
        updateFiltersMapEntry(filtersMap, CommonFilter.RAMDISK_ID.getValue(), instanceInputs.getCustomInputs().getRamdiskId());
        updateFiltersMapEntry(filtersMap, CommonFilter.ROOT_DEVICE_NAME.getValue(), instanceInputs.getCustomInputs().getRootDeviceName());
        updateFiltersMapEntry(filtersMap, CommonFilter.STATE_REASON_CODE.getValue(), instanceInputs.getCustomInputs().getStateReasonCode());
        updateFiltersMapEntry(filtersMap, CommonFilter.STATE_REASON_MESSAGE.getValue(), instanceInputs.getCustomInputs().getStateReasonMessage());

        updateFiltersMapEntry(filtersMap, InstanceFilter.AFFINITY.getValue(), instanceInputs.getAffinity());
        updateFiltersMapEntry(filtersMap, InstanceFilter.AVAILABILITY_ZONE.getValue(), instanceInputs.getCustomInputs().getAvailabilityZone());
        updateFiltersMapEntry(filtersMap, InstanceFilter.CLIENT_TOKEN.getValue(), instanceInputs.getClientToken());
        updateFiltersMapEntry(filtersMap, InstanceFilter.DNS_NAME.getValue(), instanceInputs.getDnsName());
        updateFiltersMapEntry(filtersMap, InstanceFilter.GROUP_ID.getValue(), instanceInputs.getCustomInputs().getGroupId());
        updateFiltersMapEntry(filtersMap, InstanceFilter.GROUP_NAME.getValue(), instanceInputs.getGroupName());
        updateFiltersMapEntry(filtersMap, InstanceFilter.HOST_ID.getValue(), instanceInputs.getCustomInputs().getHostId());
        updateFiltersMapEntry(filtersMap, InstanceFilter.IAM_INSTANCE_PROFILE_ARN.getValue(), instanceInputs.getIamArn());
        updateFiltersMapEntry(filtersMap, InstanceFilter.IMAGE_ID.getValue(), instanceInputs.getCustomInputs().getImageId());
        updateFiltersMapEntry(filtersMap, InstanceFilter.INSTANCE_ID.getValue(), instanceInputs.getCustomInputs().getInstanceId());
        updateFiltersMapEntry(filtersMap, InstanceFilter.INSTANCE_LIFECYCLE.getValue(), instanceInputs.getInstanceLifecycle());
        updateFiltersMapEntry(filtersMap, InstanceFilter.INSTANCE_GROUP_ID.getValue(), instanceInputs.getInstanceGroupId());
        updateFiltersMapEntry(filtersMap, InstanceFilter.INSTANCE_GROUP_NAME.getValue(), instanceInputs.getInstanceGroupName());
        updateFiltersMapEntry(filtersMap, InstanceFilter.IP_ADDRESS.getValue(), instanceInputs.getIpAddress());
        updateFiltersMapEntry(filtersMap, InstanceFilter.KEY_NAME.getValue(), instanceInputs.getKeyName());
        updateFiltersMapEntry(filtersMap, InstanceFilter.LAUNCH_INDEX.getValue(), instanceInputs.getLaunchIndex());
        updateFiltersMapEntry(filtersMap, InstanceFilter.LAUNCH_TIME.getValue(), instanceInputs.getLaunchTime());
        updateFiltersMapEntry(filtersMap, InstanceFilter.PLACEMENT_GROUP_NAME.getValue(), instanceInputs.getPlacementGroupName());
        updateFiltersMapEntry(filtersMap, InstanceFilter.PRIVATE_DNS_NAME.getValue(), instanceInputs.getPrivateDnsName());
        updateFiltersMapEntry(filtersMap, InstanceFilter.PRIVATE_IP_ADDRESS.getValue(), instanceInputs.getPrivateIpAddress());
        updateFiltersMapEntry(filtersMap, InstanceFilter.REASON.getValue(), instanceInputs.getReason());
        updateFiltersMapEntry(filtersMap, InstanceFilter.REQUESTER_ID.getValue(), instanceInputs.getRequesterId());
        updateFiltersMapEntry(filtersMap, InstanceFilter.SUBNET_ID.getValue(), instanceInputs.getCustomInputs().getSubnetId());
        updateFiltersMapEntry(filtersMap, InstanceFilter.VPC_ID.getValue(), instanceInputs.getCustomInputs().getVpcId());
        updateFiltersMapEntry(filtersMap, InstanceFilter.RESERVATION_ID.getValue(), instanceInputs.getCustomInputs().getReservationId());
        updateFiltersMapEntry(filtersMap, InstanceFilter.SPOT_INSTANCE_REQUEST_ID.getValue(), instanceInputs.getSpotInstanceRequestId());

        updateFiltersMapEntry(filtersMap, AssociationFilter.PUBLIC_IP.getValue(), instanceInputs.getPublicIp());
        updateFiltersMapEntry(filtersMap, AssociationFilter.IP_OWNER_ID.getValue(), instanceInputs.getIpOwnerId());
        updateFiltersMapEntry(filtersMap, AssociationFilter.ALLOCATION_ID.getValue(), instanceInputs.getCustomInputs().getAllocationId());
        updateFiltersMapEntry(filtersMap, AssociationFilter.ASSOCIATION_ID.getValue(), instanceInputs.getCustomInputs().getAssociationId());

        updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilter.ATTACH_TIME.getValue(), instanceInputs.getCustomInputs().getAttachTime());
        updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilter.DEVICE_NAME.getValue(), instanceInputs.getCustomInputs().getBlockMappingDeviceName());
        updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilter.VOLUME_ID.getValue(), instanceInputs.getCustomInputs().getVolumeId());

        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.DESCRIPTION.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceDescription());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.SUBNET_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceSubnetId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.VPC_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceVpcId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.INTERFACE_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.OWNER_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceOwnerId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.AVAILABILITY_ZONE.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceAvailabilityZone());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.REQUESTER_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceRequesterId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.REQUESTER_MANAGED.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceRequesterManaged());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.MAC_ADDRESS.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceMacAddress());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.PRIVATE_DNS_NAME.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfacePrivateDnsName());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.SOURCE_DEST_CHECK.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceSourceDestinationCheck());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.GROUP_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceGroupId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.GROUP_NAME.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceGroupName());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.ATTACHMENT_ATTACHMENT_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceAttachmentId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.ATTACHMENT_INSTANCE_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceInstanceId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.ATTACHMENT_INSTANCE_OWNER_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceInstanceOwnerId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.ADDRESSES_PRIVATE_IP_ADDRESS.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfacePrivateIpAddress());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.ATTACHMENT_DEVICE_INDEX.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceDeviceIndex());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.ATTACHMENT_ATTACH_TIME.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceAttachTime());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.ATTACHMENT_DELETE_ON_TERMINATION.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceDeleteOnTermination());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.ADDRESSES_PRIMARY.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceAddressesPrimary());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.ADDRESSES_ASSOCIATION_PUBLIC_IP.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfacePublicIp());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilter.ADDRESSES_ASSOCIATION_IP_OWNER_ID.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceIpOwnerId());
    }

    private void setInstanceRelevantFilters(InstanceInputs instanceInputs, Multimap<String, String> filtersMap) {
        if (!NOT_RELEVANT_INT_CODE.equals(instanceInputs.getInstanceStateCode())) {
            updateFiltersMapEntry(filtersMap, InstanceFilter.INSTANCE_STATE_CODE.getValue(), instanceInputs.getInstanceStateCode());
        }

        addFiltersMapRelevantEntry(filtersMap, CommonFilter.ARCHITECTURE.getValue(), instanceInputs.getCustomInputs().getArchitecture());
        addFiltersMapRelevantEntry(filtersMap, CommonFilter.HYPERVISOR.getValue(), instanceInputs.getCustomInputs().getHypervisor());
        addFiltersMapRelevantEntry(filtersMap, CommonFilter.PLATFORM.getValue(), instanceInputs.getCustomInputs().getPlatform());
        addFiltersMapRelevantEntry(filtersMap, CommonFilter.VIRTUALIZATION_TYPE.getValue(), instanceInputs.getCustomInputs().getVirtualizationType());
        addFiltersMapRelevantEntry(filtersMap, CommonFilter.ROOT_DEVICE_TYPE.getValue(), instanceInputs.getCustomInputs().getRootDeviceType());
        addFiltersMapRelevantEntry(filtersMap, CommonFilter.PRODUCT_CODE.getValue(), instanceInputs.getCustomInputs().getProductCode());
        addFiltersMapRelevantEntry(filtersMap, CommonFilter.PRODUCT_CODE_TYPE.getValue(), instanceInputs.getCustomInputs().getProductCodeType());
        addFiltersMapRelevantEntry(filtersMap, CommonFilter.INSTANCE_TYPE.getValue(), instanceInputs.getCustomInputs().getInstanceType());

        addFiltersMapRelevantEntry(filtersMap, BlockDeviceMappingFilter.STATUS.getValue(), instanceInputs.getCustomInputs().getBlockDeviceMappingStatus());
        addFiltersMapRelevantEntry(filtersMap, BlockDeviceMappingFilter.DELETE_ON_TERMINATION.getValue(), instanceInputs.getCustomInputs().getDeleteOnTermination());

        addFiltersMapRelevantEntry(filtersMap, InstanceFilter.TENANCY.getValue(), instanceInputs.getTenancy());
        addFiltersMapRelevantEntry(filtersMap, InstanceFilter.INSTANCE_STATE_NAME.getValue(), instanceInputs.getInstanceStateName());
        addFiltersMapRelevantEntry(filtersMap, InstanceFilter.MONITORING_STATE.getValue(), instanceInputs.getMonitoringState());
        addFiltersMapRelevantEntry(filtersMap, InstanceFilter.SOURCE_DESTINATION_CHECK.getValue(),
                instanceInputs.getSourceDestinationCheck());

        addFiltersMapRelevantEntry(filtersMap, NetworkInterfaceFilter.ATTACHMENT_STATUS.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceAttachmentStatus());
        addFiltersMapRelevantEntry(filtersMap, NetworkInterfaceFilter.STATUS.getValue(), instanceInputs.getNetworkInputs().getNetworkInterfaceStatus());
    }

    private void addFiltersMapRelevantEntry(Multimap<String, String> filtersMap, String filterKey, String filterValue) {
        if (isNotBlank(filterValue) && !NOT_RELEVANT.equalsIgnoreCase(filterValue)) {
            filtersMap.put(filterKey, filterValue);
        }
    }

    private void updateFiltersMapEntry(Multimap<String, String> map, String key, String value) {
        if (isNotBlank(value)) {
            map.put(key, value);
        }
    }

    @SuppressWarnings("unchecked")
    private <K, V> void setTags(K inputs, V multipurposeMap, String delimiter) {
        String[] tagKeys;
        String[] tagValues;

        if (inputs instanceof InstanceInputs) {
            tagKeys = InputsUtil.getStringsArray(((InstanceInputs) inputs).getCustomInputs().getKeyTagsString(), EMPTY, delimiter);
            tagValues = InputsUtil.getStringsArray(((InstanceInputs) inputs).getCustomInputs().getValueTagsString(), EMPTY, delimiter);

            updateTagFiltersMap((Multimap<String, String>) multipurposeMap, tagKeys, tagValues);
        } else if (inputs instanceof ImageInputs) {
            tagKeys = InputsUtil.getStringsArray(((ImageInputs) inputs).getCustomInputs().getKeyTagsString(), EMPTY, delimiter);
            tagValues = InputsUtil.getStringsArray(((ImageInputs) inputs).getCustomInputs().getValueTagsString(), EMPTY, delimiter);

            updateTagFiltersMap((Multimap<String, String>) multipurposeMap, tagKeys, tagValues);
        } else if (inputs instanceof CustomInputs) {
            tagKeys = InputsUtil.getStringsArray(((CustomInputs) inputs).getKeyTagsString(), EMPTY, delimiter);
            tagValues = InputsUtil.getStringsArray(((CustomInputs) inputs).getValueTagsString(), EMPTY, delimiter);

            updateTagsMap((Map<String, String>) multipurposeMap, tagKeys, tagValues);
        }

    }

    private void updateTagFiltersMap(Multimap<String, String> filtersMap, String[] tagKeys, String[] tagValues) {
        if (tagKeys != null && tagValues != null) {
            validateArraysLength(tagKeys, tagValues);

            for (int counter = START_INDEX; counter < tagKeys.length; counter++) {
                filtersMap.put(TAG, tagKeys[counter] + EQUAL + tagValues[counter]);
            }
        }
    }

    private void updateTagsMap(Map<String, String> tagsMap, String[] tagKeys, String[] tagValues) {
        if (tagKeys != null && tagValues != null) {
            validateArraysLength(tagKeys, tagValues);

            if (tagKeys.length > MAXIMUM_TAGS_LIMIT) {
                throw new RuntimeException(MAXIMUM_TAGS_LIMIT_EXCEEDED);
            }

            for (int counter = START_INDEX; counter < tagKeys.length; counter++) {
                tagsMap.put(tagKeys[counter], tagValues[counter]);
            }
        }

    }

    private void validateArraysLength(String[] tagKeys, String[] tagValues) {
        if (tagKeys.length != tagValues.length) {
            throw new RuntimeException(TAG_KEYS_TAG_VALUES_MISMATCH);
        }
    }
}