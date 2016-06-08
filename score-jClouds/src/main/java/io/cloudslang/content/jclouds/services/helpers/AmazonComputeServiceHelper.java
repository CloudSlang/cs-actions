package io.cloudslang.content.jclouds.services.helpers;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.cloudslang.content.jclouds.entities.InstanceFilters;
import io.cloudslang.content.jclouds.entities.NetworkInterfaceFilters;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import org.apache.commons.lang3.StringUtils;
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

    public Multimap<String, String> getInstanceFilterMap(InstanceInputs instanceInputs) {
        Multimap<String, String> filtersMap = ArrayListMultimap.create();
        updateFiltersMap(instanceInputs, filtersMap);

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

    private void updateFiltersMapEntry(Multimap<String, String> map, String key, String value) {
        if (StringUtils.isNotBlank(value)) {
            map.put(key, value);
        }
    }

    private void updateFiltersMap(InstanceInputs instanceInputs, Multimap<String, String> filtersMap) {
        if (!Constants.Miscellaneous.NOT_RELEVANT.equalsIgnoreCase(instanceInputs.getDeleteOnTermination())) {
            updateFiltersMapEntry(filtersMap, InstanceFilters.BLOCK_DEVICE_MAPPING_DELETE_ON_TERMINATION.getValue(),
                    instanceInputs.getDeleteOnTermination());
        }
        if (!Constants.Miscellaneous.NOT_RELEVANT.equalsIgnoreCase(instanceInputs.getSourceDestinationCheck())) {
            updateFiltersMapEntry(filtersMap, InstanceFilters.SOURCE_DESTINATION_CHECK.getValue(),
                    instanceInputs.getSourceDestinationCheck());
        }

        updateFiltersMapEntry(filtersMap, InstanceFilters.AFFINITY.getValue(), instanceInputs.getAffinity());
        updateFiltersMapEntry(filtersMap, InstanceFilters.ARCHITECTURE.getValue(), instanceInputs.getArchitecture());
        updateFiltersMapEntry(filtersMap, InstanceFilters.AVAILABILITY_ZONE.getValue(), instanceInputs.getAvailabilityZone());
        updateFiltersMapEntry(filtersMap, InstanceFilters.BLOCK_DEVICE_MAPPING_ATTACH_TIME.getValue(), instanceInputs.getAttachTime());
        updateFiltersMapEntry(filtersMap, InstanceFilters.BLOCK_DEVICE_MAPPING_DEVICE_NAME.getValue(), instanceInputs.getDeviceName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.BLOCK_DEVICE_MAPPING_STATUS.getValue(), instanceInputs.getStatus());
        updateFiltersMapEntry(filtersMap, InstanceFilters.BLOCK_DEVICE_MAPPING_VOLUME_ID.getValue(), instanceInputs.getCustomInputs().getVolumeId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.CLIENT_TOKEN.getValue(), instanceInputs.getClientToken());
        updateFiltersMapEntry(filtersMap, InstanceFilters.DNS_NAME.getValue(), instanceInputs.getDnsName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.GROUP_ID.getValue(), instanceInputs.getCustomInputs().getGroupId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.GROUP_NAME.getValue(), instanceInputs.getGroupName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.HOST_ID.getValue(), instanceInputs.getCustomInputs().getHostId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.HYPERVISOR.getValue(), instanceInputs.getHypervisor());
        updateFiltersMapEntry(filtersMap, InstanceFilters.IAM_INSTANCE_PROFILE_ARN.getValue(), instanceInputs.getIamArn());
        updateFiltersMapEntry(filtersMap, InstanceFilters.IMAGE_ID.getValue(), instanceInputs.getCustomInputs().getImageId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_ID.getValue(), instanceInputs.getCustomInputs().getInstanceId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_LIFECYCLE.getValue(), instanceInputs.getInstanceLifecycle());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_STATE_CODE.getValue(), instanceInputs.getInstanceStateCode());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_STATE_NAME.getValue(), instanceInputs.getInstanceStateName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_TYPE.getValue(), instanceInputs.getInstanceType());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_GROUP_ID.getValue(), instanceInputs.getInstanceGroupId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.INSTANCE_GROUP_NAME.getValue(), instanceInputs.getInstanceGroupName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.IP_ADDRESS.getValue(), instanceInputs.getIpAddress());
        updateFiltersMapEntry(filtersMap, InstanceFilters.KERNEL_ID.getValue(), instanceInputs.getCustomInputs().getKernelId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.KEY_NAME.getValue(), instanceInputs.getKeyName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.LAUNCH_INDEX.getValue(), instanceInputs.getLaunchIndex());
        updateFiltersMapEntry(filtersMap, InstanceFilters.LAUNCH_TIME.getValue(), instanceInputs.getLaunchTime());
        updateFiltersMapEntry(filtersMap, InstanceFilters.MONITORING_STATE.getValue(), instanceInputs.getMonitoringState());
        updateFiltersMapEntry(filtersMap, InstanceFilters.OWNER_ID.getValue(), instanceInputs.getCustomInputs().getOwnerId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.PLACEMENT_GROUP_NAME.getValue(), instanceInputs.getPlacementGroupName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.PLATFORM.getValue(), instanceInputs.getPlatform());
        updateFiltersMapEntry(filtersMap, InstanceFilters.PRIVATE_DNS_NAME.getValue(), instanceInputs.getPrivateDnsName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.PRIVATE_IP_ADDRESS.getValue(), instanceInputs.getPrivateIpAddress());
        updateFiltersMapEntry(filtersMap, InstanceFilters.PRODUCT_CODE.getValue(), instanceInputs.getProductCode());
        updateFiltersMapEntry(filtersMap, InstanceFilters.PRODUCT_CODE_TYPE.getValue(), instanceInputs.getProductCodeType());
        updateFiltersMapEntry(filtersMap, InstanceFilters.RAMDISK_ID.getValue(), instanceInputs.getCustomInputs().getRamdiskId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.REASON.getValue(), instanceInputs.getReason());
        updateFiltersMapEntry(filtersMap, InstanceFilters.REQUESTER_ID.getValue(), instanceInputs.getRequesterId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.RESERVATION_ID.getValue(), instanceInputs.getCustomInputs().getReservationId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.ROOT_DEVICE_NAME.getValue(), instanceInputs.getRootDeviceName());
        updateFiltersMapEntry(filtersMap, InstanceFilters.ROOT_DEVICE_TYPE.getValue(), instanceInputs.getRootDeviceType());
        updateFiltersMapEntry(filtersMap, InstanceFilters.SPOT_INSTANCE_REQUEST_ID.getValue(), instanceInputs.getSpotInstanceRequestId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.STATE_REASON_CODE.getValue(), instanceInputs.getStateReasonCode());
        updateFiltersMapEntry(filtersMap, InstanceFilters.STATE_REASON_MESSAGE.getValue(), instanceInputs.getStateReasonMessage());
        updateFiltersMapEntry(filtersMap, InstanceFilters.SUBNET_ID.getValue(), instanceInputs.getCustomInputs().getSubnetId());
        updateFiltersMapEntry(filtersMap, InstanceFilters.TENANCY.getValue(), instanceInputs.getTenancy());
        updateFiltersMapEntry(filtersMap, InstanceFilters.VIRTUALIZATION_TYPE.getValue(), instanceInputs.getVirtualizationType());
        updateFiltersMapEntry(filtersMap, InstanceFilters.VPC_ID.getValue(), instanceInputs.getCustomInputs().getVpcId());

        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_DESCRIPTION.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceDescription());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_SUBNET_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceSubnetId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_VPC_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceVpcId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_OWNER_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceOwnerId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_AVAILABILITY_ZONE.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceAvailabilityZone());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_REQUESTER_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceRequesterId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_REQUESTER_MANAGED.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceRequesterManaged());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_STATUS.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceStatus());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_MAC_ADDRESS.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceMacAddress());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_PRIVATE_DNS_NAME.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfacePrivateDnsName());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_SOURCE_DESTINATION_CHECK.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceSourceDestinationCheck());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_GROUP_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceGroupId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_GROUP_NAME.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceGroupName());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_ATTACHMENT_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceAttachmentId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_INSTANCE_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceInstanceId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_INSTANCE_OWNER_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceInstanceOwnerId());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_PRIVATE_IP_ADDRESS.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfacePrivateIpAddress());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_DEVICE_INDEX.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceDeviceIndex());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_ATTACHMENT_STATUS.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceAttachmentStatus());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_ATTACH_TIME.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceAttachTime());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_DELETE_ON_TERMINATION.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceDeleteOnTermination());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_ADDRESSES_PRIMARY.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceAddressesPrimary());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_PUBLIC_IP.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfacePublicIp());
        updateFiltersMapEntry(filtersMap, NetworkInterfaceFilters.NETWORK_INTERFACE_IP_OWNER_ID.getValue(),
                instanceInputs.getNetworkInputs().getNetworkInterfaceIpOwnerId());
    }
}