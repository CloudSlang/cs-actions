package io.cloudslang.content.jclouds.actions.instances;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.entities.inputs.CustomInputs;
import io.cloudslang.content.jclouds.entities.inputs.InstanceInputs;
import io.cloudslang.content.jclouds.entities.inputs.NetworkInputs;
import io.cloudslang.content.jclouds.execute.instances.DescribeInstancesExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 6/1/2016.
 */
public class DescribeInstancesAction {
    /**
     * Describes one or more instances.
     * Note: If you specify one or more instance IDs, Amazon EC2 returns information for those instances.
     * If you do not specify instance IDs, Amazon EC2 returns information for all relevant instances.
     * If you specify an instance that you do not own, it's not included in the output.
     * Recently terminated instances might appear in the output. This interval is usually less than one hour.
     *
     * @param provider                               The cloud provider on which you have the instance. Valid values:
     *                                               "amazon" or "openstack".
     * @param identityEndpoint                       The endpoint to which first request will be sent.
     *                                               Ex: "https://ec2.amazonaws.com" for Amazon AWS or "http://hostOrIp:5000/v2.0"
     *                                               for OpenStack.
     * @param identity                               The username of your account or the Access Key ID. For openstack
     *                                               provider the required format is 'alias:username'.
     * @param credential                             The password of the user or the Secret Access Key that correspond to
     *                                               the identity input.
     * @param proxyHost                              The proxy server used to access the web site. If empty no proxy will
     *                                               be used.
     * @param proxyPort                              The proxy server port.
     * @param delimiter                              The delimiter that will be used - Default: ","
     * @param region                                 The region from which to list servers. Ex: "RegionOne", "us-east-1".
     *                                               ListRegionAction can be used in order to get all regions.
     * @param volumeId                               The volume ID of the EBS volume.
     * @param groupId                                The ID of the security group for the instance. EC2-Classic only.
     * @param hostId                                 The ID of the Dedicated host on which the instance is running, if
     *                                               applicable.
     * @param imageId                                The ID of the image used to launch the instance.
     * @param instanceId                             The ID of the instance.
     * @param kernelId                               The kernel ID.
     * @param ownerId                                The AWS account ID of the instance owner.
     * @param ramdiskId                              The RAM disk ID.
     * @param reservationId                          The ID of the instance's reservation. A reservation ID is created
     *                                               any time you launch an instance. A reservation ID has a one-to-one
     *                                               relationship with an instance launch request, but can be associated
     *                                               with more than one instance if you launch multiple instances using
     *                                               the same launch request. For example, if we launch one instance, we'll
     *                                               get one reservation ID. If will launch ten instances using the same
     *                                               launch request, we'll also get one reservation ID.
     * @param subnetId                               The ID of the subnet for the instance.
     * @param vpcId                                  The ID of the VPC that the instance is running in.
     * @param allocationId                           The allocation ID returned when you allocated the Elastic IP address
     *                                               for your network interface.
     * @param associationId                          The association ID returned when the network interface was associated
     *                                               with an IP address.
     * @param affinity                               The affinity setting for an instance running on a dedicated host.
     *                                               Valid values: "default" or "host".
     * @param architecture                           The instance architecture. Valid values: "i386" or "x86_64".
     * @param availabilityZone                       The Availability Zone of the instance.
     * @param attachTime                             The attach time for an EBS volume mapped to the instance.
     *                                               Ex: "2010-09-15T17:15:20.000Z"
     * @param deleteOnTermination                    A Boolean that indicates whether the EBS volume is deleted on instance
     *                                               termination.
     * @param blockMappingDeviceName                 The device name for the EBS volume. Ex: "/dev/sdh" or "xvdh".
     * @param blockDeviceMappingStatus               The status for the EBS volume. Valid values: "attaching", "attached",
     *                                               "detaching", "detached".
     * @param clientToken                            The idem-potency token that was provided when the instance was launched.
     * @param dnsName                                The public DNS name of the instance.
     * @param groupName                              The name of the security group for the instance. EC2-Classic only.
     * @param hypervisor                             The hypervisor type of the instance. Valid values: "ovm", "xen".
     * @param iamArn                                 The instance profile associated with the instance. Specified as an ARN.
     * @param instanceLifecycle                      Indicates whether this is a Spot Instance or a Scheduled Instance.
     *                                               Valid values: "spot", "scheduled".
     * @param instanceStateCode                      The state of the instance, as a 16-bit unsigned integer. The high byte
     *                                               is
     *                                               an opaque internal value and should be ignored. The low byte is set based
     *                                               on the state represented. Valid values: "0" (pending), "16" (running),
     *                                               "32" (shutting-down), "48" (terminated), "64" (stopping) and "80" (stopped).
     * @param instanceStateName                      The state of the instance. Valid values: "pending", "running",
     *                                               "shutting-down", "terminated", "stopping", "stopped".
     * @param instanceType                           The new server type to be used when updating the instance. The complete
     *                                               list of instance types can be found at: http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/instance-types.html
     *                                               Ex: "t2.micro", "t2.medium", "m3.large".
     * @param instanceGroupId                        The ID of the security group for the instance.
     * @param instanceGroupName                      The name of the security group for the instance.
     * @param ipAddress                              The public IP address of the instance.
     * @param keyName                                The name of the key pair used when the instance was launched.
     * @param launchIndex                            When launching multiple instances, this is the index for the instance
     *                                               in the launch group. Ex: 0, 1, 2, and so on.
     * @param launchTime                             The time when the instance was launched.
     * @param monitoringState                        Indicates whether monitoring is enabled for the instance.
     *                                               Valid values: "disabled", "enabled".
     * @param placementGroupName                     The name of the placement group for the instance.
     * @param platform                               The platform. Use "windows" if you have Windows instances; otherwise,
     *                                               leave blank. Valid values: "", "windows".
     * @param privateDnsName                         The private DNS name of the instance.
     * @param privateIpAddress                       The private IP address of the instance.
     * @param productCode                            The product code associated with the AMI used to launch the instance.
     * @param productCodeType                        The type of product code. Valid values: "devpay", "marketplace".
     * @param reason                                 The reason for the current state of the instance. For e.g.: shows
     *                                               "User Initiated [date]" when user stops or terminates the instance.
     *                                               Similar to the state-reason-code filter.
     * @param requesterId                            The ID of the entity that launched the instance on your behalf
     *                                               (for e.g.: "AWS Management Console", "Auto Scaling", and so on).
     * @param rootDeviceName                         The name of the root device for the instance. Ex: "/dev/sda1", "/dev/xvda".
     * @param rootDeviceType                         The type of root device that the instance uses. Valid values: "ebs",
     *                                               "instance-store".
     * @param sourceDestinationCheck                 Indicates whether the instance performs source/destination checking.
     *                                               A value of "true" means that checking is enabled, and "false" means
     *                                               checking is disabled. The value must be "false" for the instance to
     *                                               perform network address translation (NAT) in your VPC.
     * @param spotInstanceRequestId                  The ID of the Spot instance request.
     * @param stateReasonCode                        The reason code for the state change.
     * @param stateReasonMessage                     A message that describes the state change.
     * @param tenancy                                The tenancy of an instance. Valid values: "dedicated", "default", "host".
     * @param virtualizationType                     The virtualization type of the instance. Valid values: "paravirtual", "hvm".
     * @param publicIp                               The address of the Elastic IP address bound to the network interface.
     * @param ipOwnerId                              The owner of the Elastic IP address associated with the network interface.
     * @param networkInterfaceDescription            The description of the network interface.
     * @param networkInterfaceSubnetId               The ID of the subnet for the network interface.
     * @param networkInterfaceVpcId                  The ID of the VPC for the network interface.
     * @param networkInterfaceId                     The ID of the network interface.
     * @param networkInterfaceOwnerId                The ID of the owner of the network interface.
     * @param networkInterfaceAvailabilityZone       The Availability Zone for the network interface.
     * @param networkInterfaceRequesterId            The requester ID for the network interface.
     * @param networkInterfaceRequesterManaged       Indicates whether the network interface is being managed by AWS.
     * @param networkInterfaceStatus                 The status of the network interface. Valid values: "available", "in-use".
     * @param networkInterfaceMacAddress             The MAC address of the network interface.
     * @param networkInterfacePrivateDnsName         The private DNS name of the network interface.
     * @param networkInterfaceSourceDestinationCheck Whether the network interface performs source/destination checking.
     *                                               A value of true means checking is enabled, and false means checking
     *                                               is disabled. The value must be false for the network interface to
     *                                               perform network address translation (NAT) in your VPC.
     * @param networkInterfaceGroupId                The ID of a security group associated with the network interface.
     * @param networkInterfaceGroupName              The name of a security group associated with the network interface.
     * @param networkInterfaceAttachmentId           The ID of the interface attachment.
     * @param networkInterfaceInstanceId             The ID of the instance to which the network interface is attached.
     * @param networkInterfaceInstanceOwnerId        The owner ID of the instance to which the network interface is attached.
     * @param networkInterfacePrivateIpAddress       The private IP address associated with the network interface.
     * @param networkInterfaceDeviceIndex            The device index to which the network interface is attached.
     * @param networkInterfaceAttachmentStatus       The status of the attachment. Valid values: "attaching", "attached",
     *                                               "detaching", "detached".
     * @param networkInterfaceAttachTime             The time that the network interface was attached to an instance.
     * @param networkInterfaceDeleteOnTermination    Specifies whether the attachment is deleted when an instance is terminated.
     * @param networkInterfaceAddressesPrimary       Specifies whether the IP address of the network interface is the primary
     *                                               private IP address.
     * @param networkInterfacePublicIp               The ID of the association of an Elastic IP address with a network interface.
     * @param networkInterfaceIpOwnerId              The owner ID of the private IP address associated with the network interface.
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Describe Instances",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.SUCCESS_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.FAILURE_RETURN_CODE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(@Param(value = Inputs.CommonInputs.PROVIDER, required = true) String provider,
                                       @Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String identityEndpoint,
                                       @Param(Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                       @Param(Inputs.CommonInputs.DELIMITER) String delimiter,

                                       @Param(Inputs.CustomInputs.REGION) String region,
                                       @Param(Inputs.CustomInputs.VOLUME_ID) String volumeId,
                                       @Param(Inputs.CustomInputs.GROUP_ID) String groupId,
                                       @Param(Inputs.CustomInputs.HOST_ID) String hostId,
                                       @Param(Inputs.CustomInputs.IMAGE_ID) String imageId,
                                       @Param(Inputs.CustomInputs.INSTANCE_ID) String instanceId,
                                       @Param(Inputs.CustomInputs.KERNEL_ID) String kernelId,
                                       @Param(Inputs.CustomInputs.OWNER_ID) String ownerId,
                                       @Param(Inputs.CustomInputs.RAMDISK_ID) String ramdiskId,
                                       @Param(Inputs.CustomInputs.RESERVATION_ID) String reservationId,
                                       @Param(Inputs.CustomInputs.SUBNET_ID) String subnetId,
                                       @Param(Inputs.CustomInputs.VPC_ID) String vpcId,
                                       @Param(Inputs.CustomInputs.ALLOCATION_ID) String allocationId,
                                       @Param(Inputs.CustomInputs.ASSOCIATION_ID) String associationId,
                                       @Param(Inputs.CustomInputs.ARCHITECTURE) String architecture,
                                       @Param(Inputs.CustomInputs.BLOCK_DEVICE_MAPPING_STATUS) String blockDeviceMappingStatus,
                                       @Param(Inputs.CustomInputs.DELETE_ON_TERMINATION) String deleteOnTermination,
                                       @Param(Inputs.CustomInputs.BLOCK_DEVICE_MAPPING_SNAPSHOT_ID) String blockMappingDeviceName,
                                       @Param(Inputs.CustomInputs.HYPERVISOR) String hypervisor,
                                       @Param(Inputs.CustomInputs.PLATFORM) String platform,
                                       @Param(Inputs.CustomInputs.PRODUCT_CODE) String productCode,
                                       @Param(Inputs.CustomInputs.PRODUCT_CODE_TYPE) String productCodeType,
                                       @Param(Inputs.CustomInputs.ROOT_DEVICE_NAME) String rootDeviceName,
                                       @Param(Inputs.CustomInputs.ROOT_DEVICE_TYPE) String rootDeviceType,

                                       @Param(Inputs.InstanceInputs.AFFINITY) String affinity,
                                       @Param(Inputs.InstanceInputs.AVAILABILITY_ZONE) String availabilityZone,
                                       @Param(Inputs.InstanceInputs.ATTACH_TIME) String attachTime,
                                       @Param(Inputs.InstanceInputs.CLIENT_TOKEN) String clientToken,
                                       @Param(Inputs.InstanceInputs.DNS_NAME) String dnsName,
                                       @Param(Inputs.InstanceInputs.GROUP_NAME) String groupName,
                                       @Param(Inputs.InstanceInputs.IAM_ARN) String iamArn,
                                       @Param(Inputs.InstanceInputs.INSTANCE_LIFECYCLE) String instanceLifecycle,
                                       @Param(Inputs.InstanceInputs.INSTANCE_STATE_CODE) String instanceStateCode,
                                       @Param(Inputs.InstanceInputs.INSTANCE_STATE_NAME) String instanceStateName,
                                       @Param(Inputs.InstanceInputs.INSTANCE_TYPE) String instanceType,
                                       @Param(Inputs.InstanceInputs.INSTANCE_GROUP_ID) String instanceGroupId,
                                       @Param(Inputs.InstanceInputs.INSTANCE_GROUP_NAME) String instanceGroupName,
                                       @Param(Inputs.InstanceInputs.IP_ADDRESS) String ipAddress,
                                       @Param(Inputs.InstanceInputs.KEY_NAME) String keyName,
                                       @Param(Inputs.InstanceInputs.LAUNCH_INDEX) String launchIndex,
                                       @Param(Inputs.InstanceInputs.LAUNCH_TIME) String launchTime,
                                       @Param(Inputs.InstanceInputs.MONITORING_STATE) String monitoringState,
                                       @Param(Inputs.InstanceInputs.PLACEMENT_GROUP_NAME) String placementGroupName,
                                       @Param(Inputs.InstanceInputs.PRIVATE_DNS_NAME) String privateDnsName,
                                       @Param(Inputs.InstanceInputs.PRIVATE_IP_ADDRESS) String privateIpAddress,
                                       @Param(Inputs.InstanceInputs.REASON) String reason,
                                       @Param(Inputs.InstanceInputs.REQUESTER_ID) String requesterId,
                                       @Param(Inputs.InstanceInputs.SOURCE_DESTINATION_CHECK) String sourceDestinationCheck,
                                       @Param(Inputs.InstanceInputs.SPOT_INSTANCE_REQUEST_ID) String spotInstanceRequestId,
                                       @Param(Inputs.InstanceInputs.STATE_REASON_CODE) String stateReasonCode,
                                       @Param(Inputs.InstanceInputs.STATE_REASON_MESSAGE) String stateReasonMessage,
                                       @Param(Inputs.InstanceInputs.TENANCY) String tenancy,
                                       @Param(Inputs.InstanceInputs.VIRTUALIZATION_TYPE) String virtualizationType,
                                       @Param(Inputs.InstanceInputs.PUBLIC_IP) String publicIp,
                                       @Param(Inputs.InstanceInputs.IP_OWNER_ID) String ipOwnerId,
                                       @Param(Inputs.InstanceInputs.KEY_TAGS_STRING) String keyTagsString,
                                       @Param(Inputs.InstanceInputs.VALUE_TAGS_STRING) String valueTagsString,

                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_DESCRIPTION) String networkInterfaceDescription,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_SUBNET_ID) String networkInterfaceSubnetId,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_VPC_ID) String networkInterfaceVpcId,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_ID) String networkInterfaceId,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_OWNER_ID) String networkInterfaceOwnerId,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_AVAILABILITY_ZONE) String networkInterfaceAvailabilityZone,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_REQUESTER_ID) String networkInterfaceRequesterId,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_REQUESTER_MANAGED) String networkInterfaceRequesterManaged,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_STATUS) String networkInterfaceStatus,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_MAC_ADDRESS) String networkInterfaceMacAddress,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_PRIVATE_DNS_NAME) String networkInterfacePrivateDnsName,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_SOURCE_DESTINATION_CHECK) String networkInterfaceSourceDestinationCheck,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_GROUP_ID) String networkInterfaceGroupId,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_GROUP_NAME) String networkInterfaceGroupName,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_ATTACHMENT_ID) String networkInterfaceAttachmentId,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_INSTANCE_ID) String networkInterfaceInstanceId,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_INSTANCE_OWNER_ID) String networkInterfaceInstanceOwnerId,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_PRIVATE_IP_ADDRESS) String networkInterfacePrivateIpAddress,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_DEVICE_INDEX) String networkInterfaceDeviceIndex,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_ATTACHMENT_STATUS) String networkInterfaceAttachmentStatus,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_ATTACH_TIME) String networkInterfaceAttachTime,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_DELETE_ON_TERMINATION) String networkInterfaceDeleteOnTermination,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_ADDRESSES_PRIMARY) String networkInterfaceAddressesPrimary,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_PUBLIC_IP) String networkInterfacePublicIp,
                                       @Param(Inputs.NetworkInputs.NETWORK_INTERFACE_IP_OWNER_ID) String networkInterfaceIpOwnerId)
            throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(identityEndpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDelimiter(delimiter)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .withVolumeId(volumeId)
                .withGroupId(groupId)
                .withHostId(hostId)
                .withImageId(imageId)
                .withInstanceId(instanceId)
                .withKernelId(kernelId)
                .withOwnerId(ownerId)
                .withRamdiskId(ramdiskId)
                .withReservationId(reservationId)
                .withSubnetId(subnetId)
                .withVpcId(vpcId)
                .withAllocationId(allocationId)
                .withAssociationId(associationId)
                .withArchitecture(architecture)
                .withDeleteOnTermination(deleteOnTermination)
                .withBlockMappingDeviceName(blockMappingDeviceName)
                .withBlockDeviceMappingStatus(blockDeviceMappingStatus)
                .withAttachTime(attachTime)
                .withHypervisor(hypervisor)
                .withPlatform(platform)
                .withProductCode(productCode)
                .withProductCodeType(productCodeType)
                .withRootDeviceName(rootDeviceName)
                .withRootDeviceType(rootDeviceType)
                .build();

        NetworkInputs networkInputs = new NetworkInputs.NetworkInputsBuilder()
                .withNetworkInterfaceDescription(networkInterfaceDescription)
                .withNetworkInterfaceSubnetId(networkInterfaceSubnetId)
                .withNetworkInterfaceVpcId(networkInterfaceVpcId)
                .withNetworkInterfaceId(networkInterfaceId)
                .withNetworkInterfaceOwnerId(networkInterfaceOwnerId)
                .withNetworkInterfaceAvailabilityZone(networkInterfaceAvailabilityZone)
                .withNetworkInterfaceRequesterId(networkInterfaceRequesterId)
                .withNetworkInterfaceRequesterManaged(networkInterfaceRequesterManaged)
                .withNetworkInterfaceStatus(networkInterfaceStatus)
                .withNetworkInterfaceMacAddress(networkInterfaceMacAddress)
                .withNetworkInterfacePrivateDnsName(networkInterfacePrivateDnsName)
                .withNetworkInterfaceSourceDestinationCheck(networkInterfaceSourceDestinationCheck)
                .withNetworkInterfaceGroupId(networkInterfaceGroupId)
                .withNetworkInterfaceGroupName(networkInterfaceGroupName)
                .withNetworkInterfaceAttachmentId(networkInterfaceAttachmentId)
                .withNetworkInterfaceInstanceId(networkInterfaceInstanceId)
                .withNetworkInterfaceInstanceOwnerId(networkInterfaceInstanceOwnerId)
                .withNetworkInterfacePrivateIpAddress(networkInterfacePrivateIpAddress)
                .withNetworkInterfaceDeviceIndex(networkInterfaceDeviceIndex)
                .withNetworkInterfaceAttachmentStatus(networkInterfaceAttachmentStatus)
                .withNetworkInterfaceAttachTime(networkInterfaceAttachTime)
                .withNetworkInterfaceDeleteOnTermination(networkInterfaceDeleteOnTermination)
                .withNetworkInterfaceAddressesPrimary(networkInterfaceAddressesPrimary)
                .withNetworkInterfacePublicIp(networkInterfacePublicIp)
                .withNetworkInterfaceIpOwnerId(networkInterfaceIpOwnerId)
                .build();

        InstanceInputs instanceInputs = new InstanceInputs.InstanceInputsBuilder()
                .withCustomInputs(customInputs)
                .withNetworkInputs(networkInputs)
                .withAffinity(affinity)
                .withAvailabilityZone(availabilityZone)
                .withClientToken(clientToken)
                .withDnsName(dnsName)
                .withGroupName(groupName)
                .withIamArn(iamArn)
                .withInstanceLifecycle(instanceLifecycle)
                .withInstanceStateCode(instanceStateCode)
                .withInstanceStateName(instanceStateName)
                .withInstanceType(instanceType)
                .withInstanceGroupId(instanceGroupId)
                .withInstanceGroupName(instanceGroupName)
                .withIpAddress(ipAddress)
                .withKeyName(keyName)
                .withLaunchIndex(launchIndex)
                .withLaunchTime(launchTime)
                .withMonitoringState(monitoringState)
                .withPlacementGroupName(placementGroupName)
                .withPrivateDnsName(privateDnsName)
                .withPrivateIpAddress(privateIpAddress)
                .withReason(reason)
                .withRequesterId(requesterId)
                .withSourceDestinationCheck(sourceDestinationCheck)
                .withSpotInstanceRequestId(spotInstanceRequestId)
                .withStateReasonCode(stateReasonCode)
                .withStateReasonMessage(stateReasonMessage)
                .withTenancy(tenancy)
                .withVirtualizationType(virtualizationType)
                .withPublicIp(publicIp)
                .withIpOwnerId(ipOwnerId)
                .withKeyTagsString(keyTagsString)
                .withValueTagsString(valueTagsString)
                .build();

        try {
            return new DescribeInstancesExecutor().execute(inputs, instanceInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}