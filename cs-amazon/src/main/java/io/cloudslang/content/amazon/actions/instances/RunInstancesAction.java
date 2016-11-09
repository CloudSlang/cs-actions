package io.cloudslang.content.amazon.actions.instances;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.amazon.entities.inputs.CommonInputs;
import io.cloudslang.content.amazon.entities.inputs.CustomInputs;
import io.cloudslang.content.amazon.entities.inputs.EbsInputs;
import io.cloudslang.content.amazon.entities.inputs.ElasticIpInputs;
import io.cloudslang.content.amazon.entities.inputs.IamInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.entities.inputs.NetworkInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;
import io.cloudslang.content.amazon.utils.InputsUtil;
import io.cloudslang.content.constants.ReturnCodes;

import java.util.Map;

import static io.cloudslang.content.amazon.utils.OutputsUtil.putResponseIn;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.RUN_INSTANCES;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DELIMITER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.HEADERS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PASSWORD;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_USERNAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.QUERY_PARAMS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.VERSION;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.AVAILABILITY_ZONE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.HOST_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.IMAGE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.INSTANCE_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.KERNEL_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.RAMDISK_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.SUBNET_ID;

import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.DELETE_ON_TERMINATIONS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.EBS_OPTIMIZED;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.ENCRYPTED_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.IOPS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.SNAPSHOT_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.VOLUME_SIZES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.VOLUME_TYPES_STRING;

import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESSES_STRING;

import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.IAM_INSTANCE_PROFILE_ARN;
import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.IAM_INSTANCE_PROFILE_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.KEY_PAIR_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.SECURITY_GROUP_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.SECURITY_GROUP_NAMES_STRING;

import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.AFFINITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.CLIENT_TOKEN;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_DISABLE_API_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.MAX_COUNT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.MIN_COUNT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.MONITORING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.PLACEMENT_GROUP_NAME;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.TENANCY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_USER_DATA;

import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ASSOCIATE_PUBLIC_IP_ADDRESS;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DELETE_ON_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DESCRIPTION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_DEVICE_INDEX;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.NETWORK_INTERFACE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.NetworkInputs.SECONDARY_PRIVATE_IP_ADDRESS_COUNT;

import static io.cloudslang.content.amazon.entities.constants.Outputs.INSTANCE_ID_RESULT;

import static io.cloudslang.content.constants.OutputNames.EXCEPTION;
import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;

import static io.cloudslang.content.constants.ResponseNames.FAILURE;
import static io.cloudslang.content.constants.ResponseNames.SUCCESS;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class RunInstancesAction {
    private static final String INSTANCE_ID_X_PATH_QUERY = "/RunInstancesResponse/instancesSet/item/instanceId";

    /**
     * Launches the specified number of instances using an AMI (Amazon Image) for which you have permissions.
     * Notes: When you launch an instance, it enters the pending state. After the instance is ready for you, it enters
     * the running state. To check the state of your instance, call DescribeInstances.
     * To ensure faster instance launches, break up large requests into smaller batches. For example, create five
     * separate launch requests for 100 instances each instead of one launch request for 500 instances.
     * To tag your instance, ensure that it is running as CreateTags requires a resource ID. For more information
     * about tagging, see Tagging Your Amazon EC2 Resources.
     * If you don't specify a security group when launching an instance, Amazon EC2 uses the default security group.
     * For more information, see Security Groups in the Amazon Elastic Compute Cloud User Guide.
     * [EC2-VPC only accounts] If you don't specify a subnet in the request, we choose a default subnet from your
     * default VPC for you.
     * [EC2-Classic accounts] If you're launching into EC2-Classic and you don't specify an Availability Zone,
     * we choose one for you.
     * Linux instances have access to the public key of the key pair at boot. You can use this key to provide
     * secure access to the instance. Amazon EC2 public images use this feature to provide secure access without
     * passwords.
     * For more information, see Key Pairs in the Amazon Elastic Compute Cloud User Guide.
     * You can provide optional user data when launching an instance. For more information, see Instance Metadata
     * in the Amazon Elastic Compute Cloud User Guide.
     * If any of the AMIs have a product code attached for which the user has not subscribed, RunInstances fails.
     * Some instance types can only be launched into a VPC. If you do not have a default VPC, or if you do not
     * specify a subnet ID in the request, RunInstances fails. For more information, see Instance Types Available
     * Only in a VPC.
     * For more information about troubleshooting, see What To Do If An Instance Immediately Terminates, and
     * Troubleshooting Connecting to Your Instance in the Amazon Elastic Compute Cloud User Guide.
     *
     * @param endpoint                                  Optional - Endpoint to which request will be sent.
     *                                                  Default: "https://ec2.amazonaws.com"
     * @param identity                                  ID of the secret access key associated with your Amazon AWS or IAM
     *                                                  account.
     *                                                  Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential                                Secret access key associated with your Amazon AWS or IAM account.
     *                                                  Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost                                 Optional - proxy server used to connect to Amazon API. If empty no
     *                                                  proxy will be used.
     *                                                  Default: ""
     * @param proxyPort                                 Optional - proxy server port. You must either specify values for both
     *                                                  <proxyHost> and <proxyPort> inputs or leave them both empty.
     *                                                  Default: ""
     * @param proxyUsername                             Optional - proxy server user name.
     *                                                  Default: ""
     * @param proxyPassword                             Optional - proxy server password associated with the <proxyUsername>
     *                                                  input value.
     * @param headers                                   Optional - string containing the headers to use for the request separated
     *                                                  by new line (CRLF). The header name-value pair will be separated by ":".
     *                                                  Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                                  Examples: Accept:text/plain
     *                                                  Default: ""
     * @param queryParams                               Optional - string containing query parameters that will be appended to
     *                                                  the URL. The names and the values must not be URL encoded because if
     *                                                  they are encoded then a double encoded will occur. The separator between
     *                                                  name-value pairs is "&" symbol. The query name will be separated from
     *                                                  query value by "=".
     *                                                  Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                                                  Default: ""
     * @param version                                   Optional - Version of the web service to made the call against it.
     *                                                  Example: "2016-04-01"
     *                                                  Default: "2016-04-01"
     * @param delimiter                                 Optional - delimiter that will be used.
     *                                                  Default: ","
     * @param availabilityZone                          Optional - availability zone of the instance (as part of Placement).
     *                                                  Default: ""
     * @param hostId                                    Optional - ID of the dedicated host on which the instance resides
     *                                                  (as part of Placement). This parameter is not support for the
     *                                                  <ImportInstance> command.
     *                                                  Default: ""
     * @param imageId                                   ID of the AMI, which you can get by calling <DescribeImages>.
     *                                                  For more information go to: http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ComponentsAMIs.html
     *                                                  Example: "ami-abcdef12"
     * @param instanceType                              Optional - Instance type. For more information, see Instance Types
     *                                                  in the Amazon Elastic Compute Cloud User Guide.
     *                                                  Valid values: t1.micro | t2.nano | t2.micro | t2.small | t2.medium |
     *                                                  t2.large | m1.small | m1.medium | m1.large | m1.xlarge | m3.medium |
     *                                                  m3.large | m3.xlarge | m3.2xlarge | m4.large | m4.xlarge | m4.2xlarge |
     *                                                  m4.4xlarge | m4.10xlarge | m2.xlarge | m2.2xlarge | m2.4xlarge |
     *                                                  cr1.8xlarge | r3.large | r3.xlarge | r3.2xlarge | r3.4xlarge |
     *                                                  r3.8xlarge | x1.4xlarge | x1.8xlarge | x1.16xlarge | x1.32xlarge |
     *                                                  i2.xlarge | i2.2xlarge | i2.4xlarge | i2.8xlarge | hi1.4xlarge |
     *                                                  hs1.8xlarge | c1.medium | c1.xlarge | c3.large | c3.xlarge |
     *                                                  c3.2xlarge | c3.4xlarge | c3.8xlarge | c4.large | c4.xlarge |
     *                                                  c4.2xlarge | c4.4xlarge | c4.8xlarge | cc1.4xlarge | cc2.8xlarge |
     *                                                  g2.2xlarge | g2.8xlarge | cg1.4xlarge | d2.xlarge | d2.2xlarge |
     *                                                  d2.4xlarge | d2.8xlarge"
     *                                                  Default: "m1.small"
     * @param kernelId                                  Optional - ID of the kernel.
     *                                                  Important: We recommend that you use PV-GRUB instead of kernels
     *                                                  and RAM disks. For more information, see PV-GRUB in the Amazon
     *                                                  Elastic Compute Cloud User Guide.
     *                                                  Default: ""
     * @param ramdiskId                                 Optional - ID of the RAM disk.
     *                                                  Important: We recommend that you use PV-GRUB instead of kernels
     *                                                  and RAM disks. For more information, see PV-GRUB in the Amazon
     *                                                  Elastic Compute Cloud User Guide.
     *                                                  Default: ""
     * @param subnetId                                  Optional - String that contains one or more subnet IDs. If you
     *                                                  launch into EC2 Classic then supply values for this input and
     *                                                  don't supply values for Private IP Addresses string.
     *                                                  [EC2-VPC] The ID of the subnet to launch the instance into.
     *                                                  Default: ""
     * @param blockDeviceMappingDeviceNamesString       Optional - String that contains one or more device names, exposed
     *                                                  to the instance, separated by <delimiter>. If you want to suppress
     *                                                  the specified device included in the block device mapping of the
     *                                                  AMI then supply "NoDevice" in string.
     *                                                  Examples: "/dev/sdc,/dev/sdd", "/dev/sdh", "xvdh" or "NoDevice"
     *                                                  Default: ""
     * @param blockDeviceMappingVirtualNamesString      Optional - String that contains one or more virtual names separated
     *                                                  by <delimiter>. Virtual device name is "ephemeralN". Instance store
     *                                                  volumes are numbered starting from 0. An instance type with 2 available
     *                                                  instance store volumes can specify mappings for ephemeral0 and ephemeral1.
     *                                                  The number of available instance store volumes depends on the instance
     *                                                  type. After you connect to the instance, you must mount the volume.
     *                                                  Constraints: For M3 instances, you must specify instance store volumes
     *                                                  in the block device mapping for the instance. When you launch an M3
     *                                                  instance, we ignore any instance store volumes specified in the block
     *                                                  device mapping for the AMI.
     *                                                  Example: "ephemeral0,ephemeral1,Not relevant"
     *                                                  Default: ""
     * @param deleteOnTerminationsString                Optional - String that contains one or more values that indicates
     *                                                  whether a specific EBS volume will be deleted on instance termination.
     *                                                  Example: For a second EBS device (from existing 4 devices), that
     *                                                  should be deleted, the string will be: "false,true,false,false".
     *                                                  Valid values: "true", "false"
     *                                                  Default: ""
     * @param ebsOptimized                              Optional - Indicates whether the instance is optimized for EBS I/O.
     *                                                  This optimization provides dedicated throughput to Amazon EBS and an
     *                                                  optimized configuration stack to provide optimal EBS I/O performance.
     *                                                  This optimization isn't available with all instance types. Additional
     *                                                  usage charges apply when using an EBS-optimized instance.
     *                                                  Valid values: "true", "false"
     *                                                  Default: "false"
     * @param encryptedString                           Optional - String that contains one or more values that indicates
     *                                                  whether a specific EBS volume will be encrypted. Encrypted Amazon
     *                                                  EBS volumes may only be attached to instances that support Amazon
     *                                                  EBS encryption.
     *                                                  Example: For a second EBS device (from existing 4 devices), that
     *                                                  should be encrypted, the string will be: "0,1,0,0". If no value
     *                                                  provided the the default value of not encrypted will be considered
     *                                                  for all EBS specified devices.
     *                                                  Default: ""
     * @param iopsString                                Optional - String that contains one or more values that specifies
     *                                                  the number of I/O operations per second (IOPS) that the volume supports.
     *                                                  For "io1", this represents the number of IOPS that are provisioned
     *                                                  for the volume. For "gp2", this represents the baseline performance
     *                                                  of the volume and the rate at which the volume accumulates I/O
     *                                                  credits for bursting. For more information about General Purpose
     *                                                  SSD baseline performance, I/O credits, and bursting, see Amazon
     *                                                  EBS Volume Types in the Amazon Elastic Compute Cloud User Guide.
     *                                                  Constraint: Range is 100-20000 IOPS for "io1" volumes and 100-10000
     *                                                  IOPS for "gp2" volumes.
     *                                                  Condition: This parameter is required for requests to create "io1"
     *                                                  volumes; it is not used in requests to create "gp2", "st1", "sc1",
     *                                                  or "standard" volumes.
     *                                                  Example: For a first EBS device (from existing 3 devices), with
     *                                                  type "io1" that should have 5000 IOPS as value the string will
     *                                                  be: "5000,,". If no value provided then the default value for every
     *                                                  single EBS device will be used.
     *                                                  Default: ""
     * @param snapshotIdsString                         Optional - String that contains one or more values of the snapshot
     *                                                  IDs to be used when creating the EBS device.
     *                                                  Example: For a last EBS device (from existing 3 devices), to be
     *                                                  created using a snapshot as image the string will be:
     *                                                  "Not relevant,Not relevant,snap-abcdef12". If no value provided
     *                                                  then no snapshot will be used when creating EBS device.
     *                                                  Default: ""
     * @param volumeSizesString                         Optional - String that contains one or more values of the sizes
     *                                                  (in GiB) for EBS devices.
     *                                                  Constraints: 1-16384 for General Purpose SSD ("gp2"), 4-16384 for
     *                                                  Provisioned IOPS SSD ("io1"), 500-16384 for Throughput Optimized
     *                                                  HDD ("st1"), 500-16384 for Cold HDD ("sc1"), and 1-1024 for Magnetic
     *                                                  ("standard") volumes. If you specify a snapshot, the volume size
     *                                                  must be equal to or larger than the snapshot size. If you're creating
     *                                                  the volume from a snapshot and don't specify a volume size, the
     *                                                  default is the snapshot size.
     *                                                  Examples: "Not relevant,Not relevant,100"
     *                                                  Default: ""
     * @param volumeTypesString                         Optional - String that contains one or more values that specifies
     *                                                  the volume types: "gp2", "io1", "st1", "sc1", or "standard". If
     *                                                  no value provided then the default value of "standard" for every
     *                                                  single EBS device type will be considered.
     *                                                  Default: ""
     * @param privateIpAddress                          Optional - [EC2-VPC] The primary IP address. You must specify a
     *                                                  value from the IP address range of the subnet. Only one private
     *                                                  IP address can be designated as primary. Therefore, you can't
     *                                                  specify this parameter if <PrivateIpAddresses.n.Primary> is set
     *                                                  to "true" and <PrivateIpAddresses.n.PrivateIpAddress> is set to
     *                                                  an IP address.
     *                                                  Default: We select an IP address from the IP address range of the
     *                                                  subnet.
     * @param privateIpAddressesString                  Optional - String that contains one or more private IP addresses
     *                                                  to assign to the network interface. Only one private IP address
     *                                                  can be designated as primary. Use this if you want to launch instances
     *                                                  with many NICs attached.
     *                                                  Default: ""
     * @param iamInstanceProfileArn                     Optional - Amazon Resource Name (IAM_INSTANCE_PROFILE_ARN) of the
     *                                                  instance profile.
     *                                                  Example: "arn:aws:iam::123456789012:user/some_user"
     *                                                  Default: ""
     * @param iamInstanceProfileName                    Optional - Name of the instance profile.
     *                                                  Default: ""
     * @param keyPairName                               Optional - Name of the key pair. You can create a key pair using
     *                                                  <CreateKeyPair> or <ImportKeyPair>.
     *                                                  Important: If you do not specify a key pair, you can't connect to
     *                                                  the instance unless you choose an AMI that is configured to allow
     *                                                  users another way to log in.
     *                                                  Default: ""
     * @param securityGroupIdsString                    Optional - IDs of the security groups for the network interface.
     *                                                  Applies only if creating a network interface when launching an
     *                                                  instance.
     *                                                  Default: ""
     * @param securityGroupNamesString                  Optional - String that contains one or more IDs of the security
     *                                                  groups for the network interface. Applies only if creating a network
     *                                                  interface when launching an instance.
     *                                                  Default: ""
     * @param affinity                                  Optional - Affinity setting for the instance on the Dedicated Host
     *                                                  (as part of Placement). This parameter is not supported for the
     *                                                  <ImportInstance> command.
     *                                                  Default: ""
     * @param clientToken                               Optional - Unique, case-sensitive identifier you provide to ensure
     *                                                  the idem-potency of the request. For more information, see Ensuring
     *                                                  Idempotency.
     *                                                  Constraints: Maximum 64 ASCII characters
     *                                                  Default: ""
     * @param disableApiTermination                     Optional - If you set this parameter to "true", you can't terminate
     *                                                  the instance using the Amazon EC2 console, CLI, or API; otherwise,
     *                                                  you can. If you set this parameter to "true" and then later want
     *                                                  to be able to terminate the instance, you must first change the
     *                                                  value of the <disableApiTermination> attribute to "false" using
     *                                                  <ModifyInstanceAttribute>. Alternatively, if you set <InstanceInitiatedShutdownBehavior>
     *                                                  to "terminate", you can terminate the instance by running the shutdown
     *                                                  command from the instance.
     *                                                  Valid values: "true", "false"
     *                                                  Default: "false"
     * @param instanceInitiatedShutdownBehavior         Optional - Indicates whether an instance stops or terminates when
     *                                                  you initiate shutdown from the instance (using the operating system
     *                                                  command for system shutdown).
     *                                                  Valid values: "stop", "terminate"
     *                                                  Default: "stop"
     * @param maxCount                                  Maximum number of instances to launch. If you specify more instances
     *                                                  than Amazon EC2 can launch in the target Availability Zone, Amazon
     *                                                  EC2 launches the largest possible number of instances above MinCount.
     *                                                  Constraints: Between 1 and the maximum number you're allowed for
     *                                                  the specified instance type. For more information about the default
     *                                                  limits, and how to request an increase, see:
     *                                                  https://aws.amazon.com/ec2/faqs/#How_many_instances_can_I_run_in_Amazon_EC2
     *                                                  Default: "1"
     * @param minCount                                  Minimum number of instances to launch. If you specify a minimum
     *                                                  that is more instances than Amazon EC2 can launch in the target
     *                                                  Availability Zone, Amazon EC2 launches no instances.
     *                                                  Constraints: Between 1 and the maximum number you're allowed for
     *                                                  the specified instance type. For more information about the default
     *                                                  limits, and how to request an increase, see:
     *                                                  https://aws.amazon.com/ec2/faqs/#How_many_instances_can_I_run_in_Amazon_EC2
     *                                                  Default: "1"
     * @param monitoring                                Optional - whether to enable or not monitoring for the instance.
     *                                                  Default: "false"
     * @param placementGroupName                        Optional - Name of the placement group for the instance (as part
     *                                                  of Placement).
     *                                                  Default: ""
     * @param tenancy                                   Optional - Tenancy of an instance (if the instance is running in
     *                                                  a VPC - as part of Placement).
     *                                                  An instance with a tenancy of dedicated runs on single-tenant hardware.
     *                                                  The host tenancy is not supported for the ImportInstance command.
     *                                                  Valid values: "dedicated", "default", "host".
     * @param userData                                  Optional - The user data to make available to the instance. For
     *                                                  more information, see Running Commands on Your Linux Instance at
     *                                                  Launch (Linux) and Adding User Data (Windows). If you are using
     *                                                  an AWS SDK or command line tool, Base64-encoding is performed for
     *                                                  you, and you can load the text from a file. Otherwise, you must
     *                                                  provide Base64-encoded text.
     *                                                  Default: ""
     * @param networkInterfaceAssociatePublicIpAddress  Optional - String that contains one or more values that indicates
     *                                                  whether to assign a public IP address or not when you launch in
     *                                                  a VPC. The public IP address can only be assigned to a network
     *                                                  interface for eth0, and can only be assigned to a new network
     *                                                  interface, not an existing one. You cannot specify more than one
     *                                                  network interface in the request. If launching into a default subnet,
     *                                                  the default value is "true".
     *                                                  Valid values: "true", "false"
     *                                                  Default: ""
     * @param networkInterfaceDeleteOnTerminationString Optional - String that contains one or more values that indicates
     *                                                  that the interface is deleted when the instance is terminated.
     *                                                  You can specify true only if creating a new network interface when
     *                                                  launching an instance.
     *                                                  Valid values: "true", "false"
     *                                                  Default: ""
     * @param networkInterfaceDescription               Optional - String that contains one or more values that describe
     *                                                  the network interfaces. Applies only if creating a network interfaces
     *                                                  when launching an instance.
     *                                                  Default: ""
     * @param networkInterfaceDeviceIndex               Optional - String that contains one or more values that are indexes
     *                                                  of the device on the instance for the network interface attachment.
     *                                                  If you are specifying a network interface in a RunInstances request,
     *                                                  you should provide the device index. If not provided then we supply
     *                                                  the automatic index starting from 0.
     *                                                  Default: ""
     * @param networkInterfaceId                        Optional - String that contains one or more values that are IDs
     *                                                  of the network interfaces.
     *                                                  Default: ""
     * @param secondaryPrivateIpAddressCount            Optional - The number of secondary private IP addresses. You can't
     *                                                  specify this option and specify more than one private IP address
     *                                                  using the private IP addresses option. Minimum valid number is 2.
     *                                                  Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Run Instances",
            outputs = {
                    @Output(RETURN_CODE),
                    @Output(RETURN_RESULT),
                    @Output(INSTANCE_ID_RESULT),
                    @Output(EXCEPTION)
            },
            responses = {
                    @Response(text = SUCCESS, field = RETURN_CODE, value = ReturnCodes.SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = FAILURE, field = RETURN_CODE, value = ReturnCodes.FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR)
            }
    )
    public Map<String, String> execute(@Param(value = ENDPOINT) String endpoint,
                                       @Param(value = IDENTITY, required = true) String identity,
                                       @Param(value = CREDENTIAL, required = true, encrypted = true) String credential,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = PROXY_USERNAME) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                       @Param(value = HEADERS) String headers,
                                       @Param(value = QUERY_PARAMS) String queryParams,
                                       @Param(value = VERSION) String version,
                                       @Param(value = DELIMITER) String delimiter,
                                       @Param(value = AVAILABILITY_ZONE) String availabilityZone,
                                       @Param(value = HOST_ID) String hostId,
                                       @Param(value = IMAGE_ID, required = true) String imageId,
                                       @Param(value = INSTANCE_TYPE) String instanceType,
                                       @Param(value = KERNEL_ID) String kernelId,
                                       @Param(value = RAMDISK_ID) String ramdiskId,
                                       @Param(value = SUBNET_ID) String subnetId,
                                       @Param(value = BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING) String blockDeviceMappingDeviceNamesString,
                                       @Param(value = BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING) String blockDeviceMappingVirtualNamesString,
                                       @Param(value = DELETE_ON_TERMINATIONS_STRING) String deleteOnTerminationsString,
                                       @Param(value = EBS_OPTIMIZED) String ebsOptimized,
                                       @Param(value = ENCRYPTED_STRING) String encryptedString,
                                       @Param(value = IOPS_STRING) String iopsString,
                                       @Param(value = SNAPSHOT_IDS_STRING) String snapshotIdsString,
                                       @Param(value = VOLUME_SIZES_STRING) String volumeSizesString,
                                       @Param(value = VOLUME_TYPES_STRING) String volumeTypesString,
                                       @Param(value = PRIVATE_IP_ADDRESS) String privateIpAddress,
                                       @Param(value = PRIVATE_IP_ADDRESSES_STRING) String privateIpAddressesString,
                                       @Param(value = IAM_INSTANCE_PROFILE_ARN) String iamInstanceProfileArn,
                                       @Param(value = IAM_INSTANCE_PROFILE_NAME) String iamInstanceProfileName,
                                       @Param(value = KEY_PAIR_NAME) String keyPairName,
                                       @Param(value = SECURITY_GROUP_IDS_STRING) String securityGroupIdsString,
                                       @Param(value = SECURITY_GROUP_NAMES_STRING) String securityGroupNamesString,
                                       @Param(value = AFFINITY) String affinity,
                                       @Param(value = CLIENT_TOKEN) String clientToken,
                                       @Param(value = LOWER_CASE_DISABLE_API_TERMINATION) String disableApiTermination,
                                       @Param(value = LOWER_CASE_INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR) String instanceInitiatedShutdownBehavior,
                                       @Param(value = MAX_COUNT) String maxCount,
                                       @Param(value = MIN_COUNT) String minCount,
                                       @Param(value = MONITORING) String monitoring,
                                       @Param(value = PLACEMENT_GROUP_NAME) String placementGroupName,
                                       @Param(value = TENANCY) String tenancy,
                                       @Param(value = LOWER_CASE_USER_DATA) String userData,
                                       @Param(value = NETWORK_INTERFACE_ASSOCIATE_PUBLIC_IP_ADDRESS) String networkInterfaceAssociatePublicIpAddress,
                                       @Param(value = NETWORK_INTERFACE_DELETE_ON_TERMINATION) String networkInterfaceDeleteOnTerminationString,
                                       @Param(value = NETWORK_INTERFACE_DESCRIPTION) String networkInterfaceDescription,
                                       @Param(value = NETWORK_INTERFACE_DEVICE_INDEX) String networkInterfaceDeviceIndex,
                                       @Param(value = NETWORK_INTERFACE_ID) String networkInterfaceId,
                                       @Param(value = SECONDARY_PRIVATE_IP_ADDRESS_COUNT) String secondaryPrivateIpAddressCount) {
        try {
            version = InputsUtil.getDefaultStringInput(version, "2016-04-01");
            CommonInputs commonInputs = new CommonInputs.Builder()
                    .withEndpoint(endpoint)
                    .withIdentity(identity)
                    .withCredential(credential)
                    .withProxyHost(proxyHost)
                    .withProxyPort(proxyPort)
                    .withProxyUsername(proxyUsername)
                    .withProxyPassword(proxyPassword)
                    .withHeaders(headers)
                    .withQueryParams(queryParams)
                    .withVersion(version)
                    .withDelimiter(delimiter)
                    .withAction(RUN_INSTANCES)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.Builder()
                    .withAvailabilityZone(availabilityZone)
                    .withHostId(hostId)
                    .withImageId(imageId)
                    .withInstanceType(instanceType)
                    .withKernelId(kernelId)
                    .withRamdiskId(ramdiskId)
                    .withSubnetId(subnetId)
                    .build();

            EbsInputs ebsInputs = new EbsInputs.Builder()
                    .withBlockDeviceMappingDeviceNamesString(blockDeviceMappingDeviceNamesString)
                    .withBlockDeviceMappingVirtualNamesString(blockDeviceMappingVirtualNamesString)
                    .withDeleteOnTerminationsString(deleteOnTerminationsString)
                    .withEbsOptimized(ebsOptimized)
                    .withEncryptedString(encryptedString)
                    .withIopsString(iopsString)
                    .withSnapshotIdsString(snapshotIdsString)
                    .withVolumeSizesString(volumeSizesString)
                    .withVolumeTypesString(volumeTypesString)
                    .build();

            ElasticIpInputs elasticIpInputs = new ElasticIpInputs.Builder()
                    .withPrivateIpAddress(privateIpAddress)
                    .withPrivateIpAddressesString(privateIpAddressesString)
                    .build();

            IamInputs iamInputs = new IamInputs.Builder()
                    .withIamInstanceProfileArn(iamInstanceProfileArn)
                    .withIamInstanceProfileName(iamInstanceProfileName)
                    .withKeyPairName(keyPairName)
                    .withSecurityGroupIdsString(securityGroupIdsString)
                    .withSecurityGroupNamesString(securityGroupNamesString)
                    .build();

            InstanceInputs instanceInputs = new InstanceInputs.Builder()
                    .withAffinity(affinity)
                    .withClientToken(clientToken)
                    .withDisableApiTermination(disableApiTermination)
                    .withInstanceInitiatedShutdownBehavior(instanceInitiatedShutdownBehavior)
                    .withMaxCount(maxCount)
                    .withMinCount(minCount)
                    .withMonitoring(monitoring)
                    .withPlacementGroupName(placementGroupName)
                    .withTenancy(tenancy)
                    .withUserData(userData)
                    .build();

            NetworkInputs networkInputs = new NetworkInputs.Builder()
                    .withNetworkInterfacesAssociatePublicIpAddressesString(networkInterfaceAssociatePublicIpAddress)
                    .withNetworkInterfaceDeleteOnTermination(networkInterfaceDeleteOnTerminationString)
                    .withNetworkInterfaceDescription(networkInterfaceDescription)
                    .withNetworkInterfaceDeviceIndex(networkInterfaceDeviceIndex)
                    .withNetworkInterfaceId(networkInterfaceId)
                    .withSecondaryPrivateIpAddressCount(secondaryPrivateIpAddressCount)
                    .build();

            Map<String, String> queryMapResult = new QueryApiExecutor()
                    .execute(commonInputs, customInputs, ebsInputs, elasticIpInputs, iamInputs, instanceInputs, networkInputs);

            if ((ReturnCodes.SUCCESS).equals(queryMapResult.get(RETURN_CODE))) {
                putResponseIn(queryMapResult, INSTANCE_ID_RESULT, INSTANCE_ID_X_PATH_QUERY);
            }

            return queryMapResult;
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}