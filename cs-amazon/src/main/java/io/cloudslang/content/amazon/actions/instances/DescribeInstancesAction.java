package io.cloudslang.content.amazon.actions.instances;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Inputs;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.instances.DescribeInstancesExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;

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
     * @param provider                               Cloud provider on which you have the instances.
     *                                               Valid values: "amazon" or "openstack".
     * @param endpoint                               Endpoint to which request will be sent.
     *                                               Example: "https://ec2.amazonaws.com" for Amazon AWS or
     *                                               "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity                               Optional - username of your account or the Access Key ID. For OpenStack
     *                                               provider the required format is 'alias:username'.
     * @param credential                             Optional - password of the user or the Secret Access Key that corresponds
     *                                               to the identity input.
     * @param proxyHost                              Optional - proxy server used to access the web site. If empty no proxy
     *                                               will be used.
     * @param proxyPort                              Optional - proxy server port.
     * @param delimiter                              Optional - delimiter that will be used - Default: ","
     * @param debugMode                              Optional - If "true" then the execution logs will be shown in CLI console.
     * @param region                                 Optional - region from which to list servers. Example: "RegionOne", "us-east-1".
     *                                               ListRegionAction can be used in order to get all regions.
     *                                               Default: "us-east-1"
     * @param volumeId                               Optional - volume ID of the EBS volume.
     * @param groupId                                Optional - ID of the security group for the instance. EC2-Classic only.
     * @param hostId                                 Optional - ID of the Dedicated host on which the instance is running,
     *                                               if applicable.
     * @param imageId                                Optional - ID of the image used to launch the instance.
     * @param instanceId                             Optional - ID of the instance.
     * @param kernelId                               Optional - kernel ID.
     * @param ownerId                                Optional - AWS account ID of the instance owner.
     * @param ramdiskId                              Optional - RAM disk ID.
     * @param reservationId                          Optional - ID of the instance's reservation. A reservation ID is created
     *                                               any time you launch an instance. A reservation ID has a one-to-one
     *                                               relationship with an instance launch request, but can be associated
     *                                               with more than one instance if you launch multiple instances using
     *                                               the same launch request. For example, if we launch one instance, we'll
     *                                               get one reservation ID. If will launch ten instances using the same
     *                                               launch request, we'll also get one reservation ID.
     * @param subnetId                               Optional - ID of the subnet for the instance.
     * @param vpcId                                  Optional - ID of the VPC that the instance is running in.
     * @param allocationId                           Optional - allocation ID returned when you allocated the Elastic IP
     *                                               address for your network interface.
     * @param associationId                          Optional - association ID returned when the network interface was
     *                                               associated with an IP address.
     * @param architecture                           Optional - instance architecture - Valid values: "i386" or "x86_64"
     * @param blockDeviceMappingStatus               Optional - status for the EBS volume.
     *                                               Valid values: "attaching", "attached", "detaching", "detached"
     * @param deleteOnTermination                    Optional - a boolean that indicates whether the EBS volume is deleted
     *                                               on instance termination.
     * @param blockMappingDeviceName                 Optional - device name for the EBS volume. Example: "/dev/sdh" or "xvdh"
     * @param hypervisor                             Optional - hypervisor type of the instance - Valid values: "ovm", "xen"
     * @param platform                               Optional - platform. Use "windows" if you have Windows instances;
     *                                               otherwise, leave blank - Valid values: "", "windows"
     * @param productCode                            Optional - product code associated with the AMI used to launch the instance.
     * @param productCodeType                        Optional - type of product code - Valid values: "devpay", "marketplace"
     * @param rootDeviceName                         Optional - name of the root device for the instance.
     *                                               Example: "/dev/sda1", "/dev/xvda".
     * @param rootDeviceType                         Optional - type of root device that the instance uses.
     *                                               Valid values: "ebs", "instance-store".
     * @param stateReasonCode                        Optional - reason code for the state change.
     * @param stateReasonMessage                     Optional - a message that describes the state change.
     * @param keyTagsString                          Optional - A string that contains: none, one or more key tags
     *                                               separated by delimiter - Default: ""
     * @param valueTagsString                        Optional - A string that contains: none, one or more tag values
     *                                               separated by delimiter - Default: ""
     * @param virtualizationType                     Optional - virtualization type of the instance.
     *                                               Valid values: "paravirtual", "hvm".
     * @param affinity                               Optional - affinity setting for an instance running on a dedicated
     *                                               host - Valid values: "default" or "host".
     * @param availabilityZone                       Optional - availability zone of the instance.
     * @param attachTime                             Optional - attach time for an EBS volume mapped to the instance.
     *                                               Example: "2010-09-15T17:15:20.000Z"
     * @param clientToken                            Optional - idem-potency token that was provided when the instance
     *                                               was launched.
     * @param dnsName                                Optional - public DNS name of the instance.
     * @param groupName                              Optional - name of the security group for the instance.
     *                                               EC2-Classic only.
     * @param iamArn                                 Optional - instance profile associated with the instance. Specified
     *                                               as an IAM_INSTANCE_PROFILE_ARN.
     * @param instanceLifecycle                      Optional - indicates whether this is a Spot Instance or a Scheduled
     *                                               Instance - Valid values: "spot", "scheduled".
     * @param instanceStateCode                      Optional - state of the instance, as a 16-bit unsigned integer. The
     *                                               high byte is an opaque internal value and should be ignored. The low
     *                                               byte is set based on the state represented - Valid values: "0" (pending),
     *                                               "16" (running), "32" (shutting-down), "48" (terminated), "64" (stopping)
     *                                               and "80" (stopped).
     * @param instanceStateName                      Optional - state of the instance - Valid values: "pending", "running",
     *                                               "shutting-down", "terminated", "stopping", "stopped".
     * @param instanceType                           Optional - new server type to be used when updating the instance.
     *                                               The complete list of instance types can be found at:
     *                                               http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/instance-types.html
     *                                               Example: "t2.micro", "t2.medium", "m3.large".
     * @param instanceGroupId                        Optional - ID of the security group for the instance.
     * @param instanceGroupName                      Optional - name of the security group for the instance.
     * @param ipAddress                              Optional - public IP address of the instance.
     * @param keyName                                Optional - name of the key pair used when the instance was launched.
     * @param launchIndex                            Optional - When launching multiple instances, this is the index for
     *                                               the instance in the launch group. Example: 0, 1, 2, and so on.
     * @param launchTime                             Optional - time when the instance was launched.
     * @param monitoringState                        Optional - Indicates whether monitoring is enabled for the instance.
     *                                               Valid values: "disabled", "enabled".
     * @param placementGroupName                     Optional - name of the placement group for the instance.
     * @param privateDnsName                         Optional - private DNS name of the instance.
     * @param privateIpAddress                       Optional - private IP address of the instance.
     * @param reason                                 Optional - reason for the current state of the instance. For e.g.:
     *                                               shows "User Initiated [date]" when user stops or terminates the instance.
     *                                               Similar to the state-reason-code filter.
     * @param requesterId                            Optional - ID of the entity that launched the instance on your behalf
     *                                               (for e.g.: "AWS Management Console", "Auto Scaling", and so on).
     * @param sourceDestinationCheck                 Optional - Indicates whether the instance performs source/destination
     *                                               checking. A value of "true" means that checking is enabled, and "false"
     *                                               means checking is disabled. The value must be "false" for the instance
     *                                               to perform network address translation (NAT) in your VPC.
     * @param spotInstanceRequestId                  Optional - ID of the Spot instance request.
     * @param tenancy                                Optional - tenancy of an instance - Valid values: "dedicated", "default", "host".
     * @param publicIp                               Optional - address of the Elastic IP address bound to the network interface.
     * @param ipOwnerId                              Optional - owner of the Elastic IP address associated with the network interface.
     * @param networkInterfaceDescription            Optional - description of the network interface.
     * @param networkInterfaceSubnetId               Optional - ID of the subnet for the network interface.
     * @param networkInterfaceVpcId                  Optional - ID of the VPC for the network interface.
     * @param networkInterfaceId                     Optional - ID of the network interface.
     * @param networkInterfaceOwnerId                Optional - ID of the owner of the network interface.
     * @param networkInterfaceAvailabilityZone       Optional - availability Zone for the network interface.
     * @param networkInterfaceRequesterId            Optional - requester ID for the network interface.
     * @param networkInterfaceRequesterManaged       Optional - indicates whether the network interface is being managed by AWS.
     * @param networkInterfaceStatus                 Optional - status of the network interface. Valid values: "available", "in-use".
     * @param networkInterfaceMacAddress             Optional - MAC address of the network interface.
     * @param networkInterfacePrivateDnsName         Optional - private DNS name of the network interface.
     * @param networkInterfaceSourceDestinationCheck Optional - whether the network interface performs source/destination
     *                                               checking. A value of true means checking is enabled, and false means
     *                                               checking is disabled. The value must be false for the network interface
     *                                               to perform network address translation (NAT) in your VPC.
     * @param networkInterfaceGroupId                Optional - ID of a security group associated with the network interface.
     * @param networkInterfaceGroupName              Optional - name of a security group associated with the network interface.
     * @param networkInterfaceAttachmentId           Optional - ID of the interface attachment.
     * @param networkInterfaceInstanceId             Optional - ID of the instance to which the network interface is attached.
     * @param networkInterfaceInstanceOwnerId        Optional - owner ID of the instance to which the network interface
     *                                               is attached.
     * @param networkInterfacePrivateIpAddress       Optional - private IP address associated with the network interface.
     * @param networkInterfaceDeviceIndex            Optional - device index to which the network interface is attached.
     * @param networkInterfaceAttachmentStatus       Optional - status of the attachment.
     *                                               Valid values: "attaching", "attached", "detaching", "detached".
     * @param networkInterfaceAttachTime             Optional - time that the network interface was attached to an instance.
     * @param networkInterfaceDeleteOnTermination    Optional - specifies whether the attachment is deleted when an instance
     *                                               is terminated.
     * @param networkInterfaceAddressesPrimary       Optional - Specifies whether the IP address of the network interface
     *                                               is the primary private IP address.
     * @param networkInterfacePublicIp               Optional - ID of the association of an Elastic IP address with a network
     *                                               interface.
     * @param networkInterfaceIpOwnerId              Optional - owner ID of the private IP address associated with the network
     *                                               interface.
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
                                       @Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String endpoint,
                                       @Param(value = Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(value = Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(value = Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                       @Param(value = Inputs.CommonInputs.DELIMITER) String delimiter,
                                       @Param(value = Inputs.CommonInputs.DEBUG_MODE) String debugMode,

                                       @Param(value = Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.VOLUME_ID) String volumeId,
                                       @Param(value = Inputs.CustomInputs.GROUP_ID) String groupId,
                                       @Param(value = Inputs.CustomInputs.HOST_ID) String hostId,
                                       @Param(value = Inputs.CustomInputs.IMAGE_ID) String imageId,
                                       @Param(value = Inputs.CustomInputs.INSTANCE_ID) String instanceId,
                                       @Param(value = Inputs.CustomInputs.KERNEL_ID) String kernelId,
                                       @Param(value = Inputs.CustomInputs.OWNER_ID) String ownerId,
                                       @Param(value = Inputs.CustomInputs.RAMDISK_ID) String ramdiskId,
                                       @Param(value = Inputs.CustomInputs.RESERVATION_ID) String reservationId,
                                       @Param(value = Inputs.CustomInputs.SUBNET_ID) String subnetId,
                                       @Param(value = Inputs.CustomInputs.VPC_ID) String vpcId,
                                       @Param(value = Inputs.CustomInputs.ALLOCATION_ID) String allocationId,
                                       @Param(value = Inputs.CustomInputs.ASSOCIATION_ID) String associationId,
                                       @Param(value = Inputs.CustomInputs.ARCHITECTURE) String architecture,
                                       @Param(value = Inputs.CustomInputs.BLOCK_DEVICE_MAPPING_STATUS) String blockDeviceMappingStatus,
                                       @Param(value = Inputs.CustomInputs.DELETE_ON_TERMINATION) String deleteOnTermination,
                                       @Param(value = Inputs.CustomInputs.BLOCK_MAPPING_DEVICE_NAME) String blockMappingDeviceName,
                                       @Param(value = Inputs.CustomInputs.HYPERVISOR) String hypervisor,
                                       @Param(value = Inputs.CustomInputs.PLATFORM) String platform,
                                       @Param(value = Inputs.CustomInputs.PRODUCT_CODE) String productCode,
                                       @Param(value = Inputs.CustomInputs.PRODUCT_CODE_TYPE) String productCodeType,
                                       @Param(value = Inputs.CustomInputs.ROOT_DEVICE_NAME) String rootDeviceName,
                                       @Param(value = Inputs.CustomInputs.ROOT_DEVICE_TYPE) String rootDeviceType,
                                       @Param(value = Inputs.CustomInputs.STATE_REASON_CODE) String stateReasonCode,
                                       @Param(value = Inputs.CustomInputs.STATE_REASON_MESSAGE) String stateReasonMessage,
                                       @Param(value = Inputs.CustomInputs.KEY_TAGS_STRING) String keyTagsString,
                                       @Param(value = Inputs.CustomInputs.VALUE_TAGS_STRING) String valueTagsString,
                                       @Param(value = Inputs.CustomInputs.VIRTUALIZATION_TYPE) String virtualizationType,
                                       @Param(value = Inputs.CustomInputs.AVAILABILITY_ZONE) String availabilityZone,
                                       @Param(value = Inputs.CustomInputs.INSTANCE_TYPE) String instanceType,

                                       @Param(value = Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESS) String privateIpAddress,
                                       @Param(value = Inputs.ElasticIpInputs.PUBLIC_IP) String publicIp,

                                       @Param(value = Inputs.InstanceInputs.ATTACH_TIME) String attachTime,
                                       @Param(value = Inputs.InstanceInputs.CLIENT_TOKEN) String clientToken,
                                       @Param(value = Inputs.InstanceInputs.DNS_NAME) String dnsName,
                                       @Param(value = Inputs.InstanceInputs.GROUP_NAME) String groupName,
                                       @Param(value = Inputs.InstanceInputs.IAM_ARN) String iamArn,
                                       @Param(value = Inputs.InstanceInputs.INSTANCE_LIFECYCLE) String instanceLifecycle,
                                       @Param(value = Inputs.InstanceInputs.INSTANCE_STATE_CODE) String instanceStateCode,
                                       @Param(value = Inputs.InstanceInputs.INSTANCE_STATE_NAME) String instanceStateName,
                                       @Param(value = Inputs.InstanceInputs.INSTANCE_GROUP_ID) String instanceGroupId,
                                       @Param(value = Inputs.InstanceInputs.INSTANCE_GROUP_NAME) String instanceGroupName,
                                       @Param(value = Inputs.InstanceInputs.IP_ADDRESS) String ipAddress,
                                       @Param(value = Inputs.InstanceInputs.KEY_NAME) String keyName,
                                       @Param(value = Inputs.InstanceInputs.LAUNCH_INDEX) String launchIndex,
                                       @Param(value = Inputs.InstanceInputs.LAUNCH_TIME) String launchTime,
                                       @Param(value = Inputs.InstanceInputs.MONITORING_STATE) String monitoringState,
                                       @Param(value = Inputs.InstanceInputs.AFFINITY) String affinity,
                                       @Param(value = Inputs.InstanceInputs.PLACEMENT_GROUP_NAME) String placementGroupName,
                                       @Param(value = Inputs.InstanceInputs.PRIVATE_DNS_NAME) String privateDnsName,
                                       @Param(value = Inputs.InstanceInputs.REASON) String reason,
                                       @Param(value = Inputs.InstanceInputs.REQUESTER_ID) String requesterId,
                                       @Param(value = Inputs.InstanceInputs.SOURCE_DESTINATION_CHECK) String sourceDestinationCheck,
                                       @Param(value = Inputs.InstanceInputs.SPOT_INSTANCE_REQUEST_ID) String spotInstanceRequestId,
                                       @Param(value = Inputs.InstanceInputs.TENANCY) String tenancy,
                                       @Param(value = Inputs.InstanceInputs.IP_OWNER_ID) String ipOwnerId,

                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_DESCRIPTION) String networkInterfaceDescription,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_SUBNET_ID) String networkInterfaceSubnetId,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_VPC_ID) String networkInterfaceVpcId,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_ID) String networkInterfaceId,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_OWNER_ID) String networkInterfaceOwnerId,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_AVAILABILITY_ZONE) String networkInterfaceAvailabilityZone,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_REQUESTER_ID) String networkInterfaceRequesterId,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_REQUESTER_MANAGED) String networkInterfaceRequesterManaged,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_STATUS) String networkInterfaceStatus,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_MAC_ADDRESS) String networkInterfaceMacAddress,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_PRIVATE_DNS_NAME) String networkInterfacePrivateDnsName,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_SOURCE_DESTINATION_CHECK) String networkInterfaceSourceDestinationCheck,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_GROUP_ID) String networkInterfaceGroupId,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_GROUP_NAME) String networkInterfaceGroupName,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_ATTACHMENT_ID) String networkInterfaceAttachmentId,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_INSTANCE_ID) String networkInterfaceInstanceId,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_INSTANCE_OWNER_ID) String networkInterfaceInstanceOwnerId,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_PRIVATE_IP_ADDRESS) String networkInterfacePrivateIpAddress,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_DEVICE_INDEX) String networkInterfaceDeviceIndex,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_ATTACHMENT_STATUS) String networkInterfaceAttachmentStatus,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_ATTACH_TIME) String networkInterfaceAttachTime,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_DELETE_ON_TERMINATION) String networkInterfaceDeleteOnTermination,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_ADDRESSES_PRIMARY) String networkInterfaceAddressesPrimary,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_PUBLIC_IP) String networkInterfacePublicIp,
                                       @Param(value = Inputs.NetworkInputs.NETWORK_INTERFACE_IP_OWNER_ID) String networkInterfaceIpOwnerId)
            throws Exception {

        CommonInputs inputs = new CommonInputs.Builder()
                .withProvider(provider)
                .withEndpoint(endpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDelimiter(delimiter)
                .withDebugMode(debugMode)
                .build();

        CustomInputs customInputs = new CustomInputs.Builder()
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
                .withStateReasonCode(stateReasonCode)
                .withStateReasonMessage(stateReasonMessage)
                .withKeyTagsString(keyTagsString)
                .withValueTagsString(valueTagsString)
                .withVirtualizationType(virtualizationType)
                .withAvailabilityZone(availabilityZone)
                .withInstanceType(instanceType)
                .build();

        NetworkInputs networkInputs = new NetworkInputs.Builder()
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

        InstanceInputs instanceInputs = new InstanceInputs.Builder()
                .withCustomInputs(customInputs)
                .withNetworkInputs(networkInputs)
                .withAffinity(affinity)
                .withClientToken(clientToken)
                .withDnsName(dnsName)
                .withGroupName(groupName)
                .withIamArn(iamArn)
                .withInstanceLifecycle(instanceLifecycle)
                .withInstanceStateCode(instanceStateCode)
                .withInstanceStateName(instanceStateName)
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
                .withTenancy(tenancy)
                .withPublicIp(publicIp)
                .withIpOwnerId(ipOwnerId)
                .build();

        try {
            return new DescribeInstancesExecutor().execute(inputs, instanceInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}