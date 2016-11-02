package io.cloudslang.content.amazon.actions.instances;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.constants.Outputs;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.instances.DescribeInstancesExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DEBUG_MODE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROVIDER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ALLOCATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ARCHITECTURE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ASSOCIATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.AVAILABILITY_ZONE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.BLOCK_DEVICE_MAPPING_STATUS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.BLOCK_MAPPING_DEVICE_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.DELETE_ON_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.GROUP_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.HOST_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.HYPERVISOR;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.IMAGE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.INSTANCE_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KERNEL_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KEY_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.OWNER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.PLATFORM;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.PRODUCT_CODE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.PRODUCT_CODE_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.RAMDISK_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.REGION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.RESERVATION_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ROOT_DEVICE_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.ROOT_DEVICE_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.STATE_REASON_CODE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.STATE_REASON_MESSAGE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.SUBNET_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VALUE_TAGS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VIRTUALIZATION_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VOLUME_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.VPC_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PUBLIC_IP;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.AFFINITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.ATTACH_TIME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.CLIENT_TOKEN;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.DNS_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.GROUP_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.IAM_ARN;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.INSTANCE_GROUP_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.INSTANCE_GROUP_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.INSTANCE_LIFECYCLE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.INSTANCE_STATE_CODE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.INSTANCE_STATE_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.IP_OWNER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.KEY_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LAUNCH_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LAUNCH_TIME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.MONITORING_STATE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.PLACEMENT_GROUP_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.PRIVATE_DNS_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.REASON;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.REQUESTER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.SOURCE_DESTINATION_CHECK;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.SPOT_INSTANCE_REQUEST_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.TENANCY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ADDRESSES_PRIMARY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ATTACHMENT_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ATTACHMENT_STATUS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ATTACH_TIME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_AVAILABILITY_ZONE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DELETE_ON_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DESCRIPTION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DEVICE_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_GROUP_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_GROUP_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_INSTANCE_OWNER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_IP_OWNER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_MAC_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_OWNER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_PRIVATE_DNS_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_PRIVATE_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_PUBLIC_IP;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_REQUESTER_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_REQUESTER_MANAGED;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_SOURCE_DESTINATION_CHECK;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_STATUS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_SUBNET_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_VPC_ID;

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
     * @param endpoint                               Optional - Endpoint to which request will be sent.
     *                                               Example: "https://ec2.amazonaws.com" for Amazon AWS or
     *                                               "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity                               Username of your account or the Access Key ID. For OpenStack
     *                                               provider the required format is 'alias:username'.
     * @param credential                             Password of the user or the Secret Access Key that corresponds
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
    public Map<String, String> execute(@Param(value = PROVIDER, required = true) String provider,
                                       @Param(value = ENDPOINT) String endpoint,
                                       @Param(value = IDENTITY, required = true) String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = DEBUG_MODE) String debugMode,

                                       @Param(value = REGION) String region,
                                       @Param(value = VOLUME_ID) String volumeId,
                                       @Param(value = GROUP_ID) String groupId,
                                       @Param(value = HOST_ID) String hostId,
                                       @Param(value = IMAGE_ID) String imageId,
                                       @Param(value = INSTANCE_ID) String instanceId,
                                       @Param(value = KERNEL_ID) String kernelId,
                                       @Param(value = OWNER_ID) String ownerId,
                                       @Param(value = RAMDISK_ID) String ramdiskId,
                                       @Param(value = RESERVATION_ID) String reservationId,
                                       @Param(value = SUBNET_ID) String subnetId,
                                       @Param(value = VPC_ID) String vpcId,
                                       @Param(value = ALLOCATION_ID) String allocationId,
                                       @Param(value = ASSOCIATION_ID) String associationId,
                                       @Param(value = ARCHITECTURE) String architecture,
                                       @Param(value = BLOCK_DEVICE_MAPPING_STATUS) String blockDeviceMappingStatus,
                                       @Param(value = DELETE_ON_TERMINATION) String deleteOnTermination,
                                       @Param(value = BLOCK_MAPPING_DEVICE_NAME) String blockMappingDeviceName,
                                       @Param(value = HYPERVISOR) String hypervisor,
                                       @Param(value = PLATFORM) String platform,
                                       @Param(value = PRODUCT_CODE) String productCode,
                                       @Param(value = PRODUCT_CODE_TYPE) String productCodeType,
                                       @Param(value = ROOT_DEVICE_NAME) String rootDeviceName,
                                       @Param(value = ROOT_DEVICE_TYPE) String rootDeviceType,
                                       @Param(value = STATE_REASON_CODE) String stateReasonCode,
                                       @Param(value = STATE_REASON_MESSAGE) String stateReasonMessage,
                                       @Param(value = KEY_TAGS_STRING) String keyTagsString,
                                       @Param(value = VALUE_TAGS_STRING) String valueTagsString,
                                       @Param(value = VIRTUALIZATION_TYPE) String virtualizationType,
                                       @Param(value = AVAILABILITY_ZONE) String availabilityZone,
                                       @Param(value = INSTANCE_TYPE) String instanceType,

                                       @Param(value = PRIVATE_IP_ADDRESS) String privateIpAddress,
                                       @Param(value = PUBLIC_IP) String publicIp,

                                       @Param(value = ATTACH_TIME) String attachTime,
                                       @Param(value = CLIENT_TOKEN) String clientToken,
                                       @Param(value = DNS_NAME) String dnsName,
                                       @Param(value = GROUP_NAME) String groupName,
                                       @Param(value = IAM_ARN) String iamArn,
                                       @Param(value = INSTANCE_LIFECYCLE) String instanceLifecycle,
                                       @Param(value = INSTANCE_STATE_CODE) String instanceStateCode,
                                       @Param(value = INSTANCE_STATE_NAME) String instanceStateName,
                                       @Param(value = INSTANCE_GROUP_ID) String instanceGroupId,
                                       @Param(value = INSTANCE_GROUP_NAME) String instanceGroupName,
                                       @Param(value = IP_ADDRESS) String ipAddress,
                                       @Param(value = KEY_NAME) String keyName,
                                       @Param(value = LAUNCH_INDEX) String launchIndex,
                                       @Param(value = LAUNCH_TIME) String launchTime,
                                       @Param(value = MONITORING_STATE) String monitoringState,
                                       @Param(value = AFFINITY) String affinity,
                                       @Param(value = PLACEMENT_GROUP_NAME) String placementGroupName,
                                       @Param(value = PRIVATE_DNS_NAME) String privateDnsName,
                                       @Param(value = REASON) String reason,
                                       @Param(value = REQUESTER_ID) String requesterId,
                                       @Param(value = SOURCE_DESTINATION_CHECK) String sourceDestinationCheck,
                                       @Param(value = SPOT_INSTANCE_REQUEST_ID) String spotInstanceRequestId,
                                       @Param(value = TENANCY) String tenancy,
                                       @Param(value = IP_OWNER_ID) String ipOwnerId,

                                       @Param(value = NETWORK_INTERFACE_DESCRIPTION) String networkInterfaceDescription,
                                       @Param(value = NETWORK_INTERFACE_SUBNET_ID) String networkInterfaceSubnetId,
                                       @Param(value = NETWORK_INTERFACE_VPC_ID) String networkInterfaceVpcId,
                                       @Param(value = NETWORK_INTERFACE_ID) String networkInterfaceId,
                                       @Param(value = NETWORK_INTERFACE_OWNER_ID) String networkInterfaceOwnerId,
                                       @Param(value = NETWORK_INTERFACE_AVAILABILITY_ZONE) String networkInterfaceAvailabilityZone,
                                       @Param(value = NETWORK_INTERFACE_REQUESTER_ID) String networkInterfaceRequesterId,
                                       @Param(value = NETWORK_INTERFACE_REQUESTER_MANAGED) String networkInterfaceRequesterManaged,
                                       @Param(value = NETWORK_INTERFACE_STATUS) String networkInterfaceStatus,
                                       @Param(value = NETWORK_INTERFACE_MAC_ADDRESS) String networkInterfaceMacAddress,
                                       @Param(value = NETWORK_INTERFACE_PRIVATE_DNS_NAME) String networkInterfacePrivateDnsName,
                                       @Param(value = NETWORK_INTERFACE_SOURCE_DESTINATION_CHECK) String networkInterfaceSourceDestinationCheck,
                                       @Param(value = NETWORK_INTERFACE_GROUP_ID) String networkInterfaceGroupId,
                                       @Param(value = NETWORK_INTERFACE_GROUP_NAME) String networkInterfaceGroupName,
                                       @Param(value = NETWORK_INTERFACE_ATTACHMENT_ID) String networkInterfaceAttachmentId,
                                       @Param(value = NETWORK_INTERFACE_INSTANCE_ID) String networkInterfaceInstanceId,
                                       @Param(value = NETWORK_INTERFACE_INSTANCE_OWNER_ID) String networkInterfaceInstanceOwnerId,
                                       @Param(value = NETWORK_INTERFACE_PRIVATE_IP_ADDRESS) String networkInterfacePrivateIpAddress,
                                       @Param(value = NETWORK_INTERFACE_DEVICE_INDEX) String networkInterfaceDeviceIndex,
                                       @Param(value = NETWORK_INTERFACE_ATTACHMENT_STATUS) String networkInterfaceAttachmentStatus,
                                       @Param(value = NETWORK_INTERFACE_ATTACH_TIME) String networkInterfaceAttachTime,
                                       @Param(value = NETWORK_INTERFACE_DELETE_ON_TERMINATION) String networkInterfaceDeleteOnTermination,
                                       @Param(value = NETWORK_INTERFACE_ADDRESSES_PRIMARY) String networkInterfaceAddressesPrimary,
                                       @Param(value = NETWORK_INTERFACE_PUBLIC_IP) String networkInterfacePublicIp,
                                       @Param(value = NETWORK_INTERFACE_IP_OWNER_ID) String networkInterfaceIpOwnerId)
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