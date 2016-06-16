package io.cloudslang.content.jclouds.services.helpers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.cloudslang.content.jclouds.entities.*;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.jclouds.ec2.domain.InstanceState;
import org.jclouds.ec2.domain.Reservation;
import org.jclouds.ec2.domain.RunningInstance;
import org.jclouds.ec2.features.InstanceApi;

import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 2/23/2016.
 */
public class AmazonComputeServiceHelper {
    private static final String SERVER_NOT_FOUND = "Server not found.";

    public InstanceState getInstanceState(InstanceApi instanceApi, String region, String serverId) throws Exception {
        Set<? extends Reservation<? extends RunningInstance>> reservationSet = instanceApi
                .describeInstancesInRegion(region, serverId);
        if (reservationSet.iterator().hasNext()) {
            Reservation<? extends RunningInstance> reservation = reservationSet.iterator().next();
            if (reservation.iterator().hasNext()) {
                RunningInstance runningInstance = reservation.iterator().next();
                return runningInstance.getInstanceState();
            }
        }
        throw new Exception(SERVER_NOT_FOUND);
    }

    public void stopAndWaitToStopInstance(InstanceApi instanceApi, InstanceState instanceState, String region,
                                          String serverId, long checkStateTimeout, long polingInterval) throws Exception {
        if (!InstanceState.STOPPED.equals(instanceState)) {
            instanceApi.stopInstancesInRegion(region, true, serverId);
            waitLoop(instanceApi, instanceState, region, serverId, checkStateTimeout, polingInterval);
        }
    }

    public Multimap<String, String> getInstanceFiltersMap(InstanceInputs instanceInputs, String delimiter) {
        Multimap<String, String> filtersMap = ArrayListMultimap.create();
        updateFiltersMap(instanceInputs, filtersMap, delimiter);

        return filtersMap;
    }

    private void waitLoop(InstanceApi instanceApi, InstanceState instanceState, String region,
                          String serverId, long checkStateTimeout, long polingInterval) throws Exception {
        long waitTime = 0;
        while (!InstanceState.STOPPED.equals(instanceState) && waitTime <= checkStateTimeout) {
            Thread.sleep(polingInterval);
            waitTime += 4000;
            instanceState = getInstanceState(instanceApi, region, serverId);
        }
    }

    private void updateFiltersMap(InstanceInputs instanceInputs, Multimap<String, String> filtersMap, String delimiter) {
        setRelevantFilters(instanceInputs, filtersMap);
        setInstanceTagFilters(instanceInputs, filtersMap, delimiter);

        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.AFFINITY.getValue(), instanceInputs.getAffinity());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.AVAILABILITY_ZONE.getValue(), instanceInputs.getAvailabilityZone());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.CLIENT_TOKEN.getValue(), instanceInputs.getClientToken());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.DNS_NAME.getValue(), instanceInputs.getDnsName());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.GROUP_ID.getValue(), instanceInputs.getCustomInputs().getGroupId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.GROUP_NAME.getValue(), instanceInputs.getGroupName());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.HOST_ID.getValue(), instanceInputs.getCustomInputs().getHostId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.IAM_INSTANCE_PROFILE_ARN.getValue(), instanceInputs.getIamArn());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.IMAGE_ID.getValue(), instanceInputs.getCustomInputs().getImageId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_ID.getValue(), instanceInputs.getCustomInputs().getInstanceId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_LIFECYCLE.getValue(), instanceInputs.getInstanceLifecycle());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_TYPE.getValue(), instanceInputs.getInstanceType());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_GROUP_ID.getValue(), instanceInputs.getInstanceGroupId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_GROUP_NAME.getValue(), instanceInputs.getInstanceGroupName());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.IP_ADDRESS.getValue(), instanceInputs.getIpAddress());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.KERNEL_ID.getValue(), instanceInputs.getCustomInputs().getKernelId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.KEY_NAME.getValue(), instanceInputs.getKeyName());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.LAUNCH_INDEX.getValue(), instanceInputs.getLaunchIndex());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.LAUNCH_TIME.getValue(), instanceInputs.getLaunchTime());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.OWNER_ID.getValue(), instanceInputs.getCustomInputs().getOwnerId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.PLACEMENT_GROUP_NAME.getValue(), instanceInputs.getPlacementGroupName());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.PRIVATE_DNS_NAME.getValue(), instanceInputs.getPrivateDnsName());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.PRIVATE_IP_ADDRESS.getValue(), instanceInputs.getPrivateIpAddress());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.PRODUCT_CODE.getValue(), instanceInputs.getProductCode());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.PRODUCT_CODE_TYPE.getValue(), instanceInputs.getProductCodeType());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.RAMDISK_ID.getValue(), instanceInputs.getCustomInputs().getRamdiskId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.REASON.getValue(), instanceInputs.getReason());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.REQUESTER_ID.getValue(), instanceInputs.getRequesterId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.ROOT_DEVICE_NAME.getValue(), instanceInputs.getRootDeviceName());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.ROOT_DEVICE_TYPE.getValue(), instanceInputs.getRootDeviceType());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.STATE_REASON_CODE.getValue(), instanceInputs.getStateReasonCode());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.STATE_REASON_MESSAGE.getValue(), instanceInputs.getStateReasonMessage());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.SUBNET_ID.getValue(), instanceInputs.getCustomInputs().getSubnetId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.VPC_ID.getValue(), instanceInputs.getCustomInputs().getVpcId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.RESERVATION_ID.getValue(),
                instanceInputs.getCustomInputs().getReservationId());
        Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.SPOT_INSTANCE_REQUEST_ID.getValue(),
                instanceInputs.getSpotInstanceRequestId());

        Utils.updateFiltersMapEntry(filtersMap, Association.PUBLIC_IP.getValue(), instanceInputs.getPublicIp());
        Utils.updateFiltersMapEntry(filtersMap, Association.IP_OWNER_ID.getValue(), instanceInputs.getIpOwnerId());
        Utils.updateFiltersMapEntry(filtersMap, Association.ALLOCATION_ID.getValue(), instanceInputs.getCustomInputs().getAllocationId());
        Utils.updateFiltersMapEntry(filtersMap, Association.ASSOCIATION_ID.getValue(), instanceInputs.getCustomInputs().getAssociationId());

        Utils.updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.ATTACH_TIME.getValue(), instanceInputs.getCustomInputs().getAttachTime());
        Utils.updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.DEVICE_NAME.getValue(), instanceInputs.getCustomInputs().getBlockMappingDeviceName());
        Utils.updateFiltersMapEntry(filtersMap, BlockDeviceMappingFilters.VOLUME_ID.getValue(), instanceInputs.getCustomInputs().getVolumeId());

        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.DESCRIPTION.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceDescription());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.SUBNET_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceSubnetId());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.VPC_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceVpcId());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.INTERFACE_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceId());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.OWNER_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceOwnerId());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.AVAILABILITY_ZONE.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceAvailabilityZone());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.REQUESTER_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceRequesterId());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.REQUESTER_MANAGED.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceRequesterManaged());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.MAC_ADDRESS.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceMacAddress());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.PRIVATE_DNS_NAME.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfacePrivateDnsName());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.SOURCE_DEST_CHECK.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceSourceDestinationCheck());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.GROUP_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceGroupId());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.GROUP_NAME.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceGroupName());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_ATTACHMENT_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceAttachmentId());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_INSTANCE_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceInstanceId());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_INSTANCE_OWNER_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceInstanceOwnerId());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ADDRESSES_PRIVATE_IP_ADDRESS.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfacePrivateIpAddress());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_DEVICE_INDEX.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceDeviceIndex());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_ATTACH_TIME.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceAttachTime());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_DELETE_ON_TERMINATION.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceDeleteOnTermination());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ADDRESSES_PRIMARY.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceAddressesPrimary());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ADDRESSES_ASSOCIATION_PUBLIC_IP.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfacePublicIp());
        Utils.updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.ADDRESSES_ASSOCIATION_IP_OWNER_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceIpOwnerId());
    }

    private void setRelevantFilters(InstanceInputs instanceInputs, Multimap<String, String> filtersMap) {
        if (!Constants.Miscellaneous.NOT_RELEVANT_INT_CODE.equals(instanceInputs.getInstanceStateCode())) {
            Utils.updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_STATE_CODE.getValue(), instanceInputs.getInstanceStateCode());
        }

        Utils.addFiltersMapRelevantEntry(filtersMap, CommonFilters.ARCHITECTURE.getValue(), instanceInputs.getCustomInputs().getArchitecture());

        Utils.addFiltersMapRelevantEntry(filtersMap, BlockDeviceMappingFilters.STATUS.getValue(),
                instanceInputs.getCustomInputs().getBlockDeviceMappingStatus());
        Utils.addFiltersMapRelevantEntry(filtersMap, BlockDeviceMappingFilters.DELETE_ON_TERMINATION.getValue(),
                instanceInputs.getCustomInputs().getDeleteOnTermination());

        Utils.addFiltersMapRelevantEntry(filtersMap, InstanceFilters.HYPERVISOR.getValue(), instanceInputs.getHypervisor());
        Utils.addFiltersMapRelevantEntry(filtersMap, InstanceFilters.PLATFORM.getValue(), instanceInputs.getPlatform());
        Utils.addFiltersMapRelevantEntry(filtersMap, InstanceFilters.TENANCY.getValue(), instanceInputs.getTenancy());
        Utils.addFiltersMapRelevantEntry(filtersMap, InstanceFilters.VIRTUALIZATION_TYPE.getValue(), instanceInputs.getVirtualizationType());
        Utils.addFiltersMapRelevantEntry(filtersMap, InstanceFilters.INSTANCE_STATE_NAME.getValue(), instanceInputs.getInstanceStateName());
        Utils.addFiltersMapRelevantEntry(filtersMap, InstanceFilters.MONITORING_STATE.getValue(), instanceInputs.getMonitoringState());
        Utils.addFiltersMapRelevantEntry(filtersMap, InstanceFilters.SOURCE_DESTINATION_CHECK.getValue(),
                instanceInputs.getSourceDestinationCheck());

        Utils.addFiltersMapRelevantEntry(filtersMap, NetworkInterfaceFilters.ATTACHMENT_STATUS.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceAttachmentStatus());
        Utils.addFiltersMapRelevantEntry(filtersMap, NetworkInterfaceFilters.STATUS.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceStatus());
    }

    private void setInstanceTagFilters(InstanceInputs instanceInputs, Multimap<String, String> filtersMap, String delimiter) {
        String[] tagKeys = InputsUtil.getStringsArray(instanceInputs.getKeyTagsString(), Constants.Miscellaneous.EMPTY, delimiter);
        String[] tagValues = InputsUtil.getStringsArray(instanceInputs.getValueTagsString(), Constants.Miscellaneous.EMPTY, delimiter);

        Utils.setTagFilters(filtersMap, tagKeys, tagValues);
    }
}