package io.cloudslang.content.jclouds.actions.instances;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.*;
import io.cloudslang.content.jclouds.execute.queries.QueryApiExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class RunInstancesAction {
    /**
     * Launches the specified number of instances using an AMI for which you have permissions.
     * Notes: When you launch an instance, it enters the pending state. After the instance is ready for you, it enters
     * the running state. To check the state of your instance, call DescribeInstances.
     * <p>
     * To ensure faster instance launches, break up large requests into smaller batches. For example, create five
     * separate launch requests for 100 instances each instead of one launch request for 500 instances.
     * <p>
     * To tag your instance, ensure that it is running as CreateTags requires a resource ID. For more information
     * about tagging, see Tagging Your Amazon EC2 Resources.
     * <p>
     * If you don't specify a security group when launching an instance, Amazon EC2 uses the default security group.
     * For more information, see Security Groups in the Amazon Elastic Compute Cloud User Guide.
     * <p>
     * [EC2-VPC only accounts] If you don't specify a subnet in the request, we choose a default subnet from your
     * default VPC for you.
     * <p>
     * [EC2-Classic accounts] If you're launching into EC2-Classic and you don't specify an Availability Zone, we
     * choose one for you.
     * <p>
     * Linux instances have access to the public key of the key pair at boot. You can use this key to provide secure
     * access to the instance. Amazon EC2 public images use this feature to provide secure access without passwords.
     * For more information, see Key Pairs in the Amazon Elastic Compute Cloud User Guide.
     * <p>
     * You can provide optional user data when launching an instance. For more information, see Instance Metadata
     * in the Amazon Elastic Compute Cloud User Guide.
     * <p>
     * If any of the AMIs have a product code attached for which the user has not subscribed, RunInstances fails.
     * <p>
     * Some instance types can only be launched into a VPC. If you do not have a default VPC, or if you do not
     * specify a subnet ID in the request, RunInstances fails. For more information, see Instance Types Available
     * Only in a VPC.
     * <p>
     * For more information about troubleshooting, see What To Do If An Instance Immediately Terminates, and
     * Troubleshooting Connecting to Your Instance in the Amazon Elastic Compute Cloud User Guide.
     *
     * @param endpoint                             Endpoint to which request will be sent.
     *                                             Default: "https://ec2.amazonaws.com"
     * @param identity                             ID of the secret access key associated with your Amazon AWS or IAM
     *                                             account.
     *                                             Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential                           Secret access key associated with your Amazon AWS or IAM account.
     *                                             Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost                            Optional - proxy server used to connect to Amazon API. If empty no
     *                                             proxy will be used.
     *                                             Default: ""
     * @param proxyPort                            Optional - proxy server port. You must either specify values for both
     *                                             <proxyHost> and <proxyPort> inputs or leave them both empty.
     *                                             Default: ""
     * @param proxyUsername                        Optional - proxy server user name.
     *                                             Default: ""
     * @param proxyPassword                        Optional - proxy server password associated with the <proxyUsername>
     *                                             input value.
     * @param headers                              Optional - string containing the headers to use for the request separated
     *                                             by new line (CRLF). The header name-value pair will be separated by ":".
     *                                             Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                             Examples: Accept:text/plain
     *                                             Default: ""
     * @param queryParams                          Optional - string containing query parameters that will be appended to
     *                                             the URL. The names and the values must not be URL encoded because if
     *                                             they are encoded then a double encoded will occur. The separator between
     *                                             name-value pairs is "&" symbol. The query name will be separated from
     *                                             query value by "=".
     *                                             Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                                             Default: ""
     * @param delimiter                            Optional - delimiter that will be used.
     *                                             Default: ","
     * @param version                              Version of the web service to made the call against it.
     *                                             Example: "2014-06-15"
     *                                             Default: ""
     * @param availabilityZone                     Optional - availability zone of the instance.
     *                                             Default: ""
     * @param hostId                               Optional - The ID of the Dedicted host on which the instance resides.
     *                                             This parameter is not support for the <ImportInstance> command.
     *                                             Default: ""
     * @param imageId                              ID of the AMI, which you can get by calling <DescribeImages>.
     *                                             For more information go to: http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ComponentsAMIs.html
     *                                             Example: "ami-abcdef12"
     * @param instanceType                         Optional - Instance type. For more information, see Instance Types in
     *                                             the Amazon Elastic Compute Cloud User Guide.
     *                                             Default: "m1.small"
     * @param kernelId                             Optional - ID of the kernel.
     *                                             Important: We recommend that you use PV-GRUB instead of kernels and RAM
     *                                             disks. For more information, see PV-GRUB in the Amazon Elastic Compute
     *                                             Cloud User Guide.
     *                                             Default: ""
     * @param privateIpAddress                     Optional - [EC2-VPC] The primary IP address. You must specify a value
     *                                             from the IP address range of the subnet. Only one private IP address
     *                                             can be designated as primary. Therefore, you can't specify this parameter
     *                                             if <PrivateIpAddresses.n.Primary> is set to "true" and
     *                                             <PrivateIpAddresses.n.PrivateIpAddress> is set to an IP address.
     *                                             Default: We select an IP address from the IP address range of the subnet.
     * @param ramdiskId                            Optional - ID of the RAM disk.
     *                                             Important: We recommend that you use PV-GRUB instead of kernels and RAM
     *                                             disks. For more information, see PV-GRUB in the Amazon Elastic Compute
     *                                             Cloud User Guide.
     *                                             Default: ""
     * @param subnetId                             Optional - [EC2-VPC] ID of the subnet to launch the instance into.
     *                                             Default: ""
     * @param blockDeviceMappingDeviceNamesString  Optional - String that contains one or more device names, exposed to
     *                                             the instance, separated by <delimiter>. If you want to suppress the
     *                                             specified device included in the block device mapping
     *                                             of the AMI then supply "NoDevice" in string.
     *                                             Examples: "/dev/sdc,/dev/sdd", "/dev/sdh", "xvdh" or "NoDevice"
     *                                             Default: ""
     * @param blockDeviceMappingVirtualNamesString Optional - String that contains one or more virtual names separated
     *                                             by <delimiter>. Virtual device name is "ephemeralN". Instance store
     *                                             volumes are numbered starting from 0. An instance type with 2 available
     *                                             instance store volumes can specify mappings for ephemeral0 and ephemeral1.
     *                                             The number of available instance store volumes depends on the instance
     *                                             type. After you connect to the instance, you must mount the volume.
     *                                             Constraints: For M3 instances, you must specify instance store volumes
     *                                             in the block device mapping for the instance. When you launch an M3
     *                                             instance, we ignore any instance store volumes specified in the block
     *                                             device mapping for the AMI.
     *                                             Example: "ephemeral0,ephemeral1,Not relevant"
     *                                             Default: ""
     * @param deleteOnTerminationsString           Optional - String that contains one or more values that indicates
     *                                             whether a specific EBS volume will be deleted on instance termination.
     *                                             Example: For a third EBS device (from existing 5 devices), that should
     *                                             not be terminated when the attached instance will be, the string will
     *                                             be: "true,true,false,true,true". If no value provided the the default
     *                                             value of "true" will be considered for all EBS specified devices.
     *                                             Default: ""
     * @param ebsOptimized                         Optional - Indicates whether the instance is optimized for EBS I/O.
     *                                             This optimization provides dedicated throughput to Amazon EBS and an
     *                                             optimized configuration stack to provide optimal EBS I/O performance.
     *                                             This optimization isn't available with all instance types. Additional
     *                                             usage charges apply when using an EBS-optimized instance.
     *                                             Valid values: "true", "false"
     *                                             Default: "false"
     * @param encryptedString                      Optional - String that contains one or more values that indicates
     *                                             whether a specific EBS volume will be encrypted. Encrypted Amazon EBS
     *                                             volumes may only be attached to instances that support Amazon EBS encryption.
     *                                             Example: For a second EBS device (from existing 4 devices), that should
     *                                             be encrypted, the string will be: "0,1,0,0". If no value provided the
     *                                             the default value of not encrypted will be considered for all EBS specified
     *                                             devices.
     *                                             Default: ""
     * @param iopsString                           Optional - String that contains one or more values that specifies the
     *                                             number of I/O operations per second (IOPS) that the volume supports.
     *                                             For "io1", this represents the number of IOPS that are provisioned for
     *                                             the volume. For "gp2", this represents the baseline performance of the
     *                                             volume and the rate at which the volume accumulates I/O credits for
     *                                             bursting. For more information about General Purpose SSD baseline
     *                                             performance, I/O credits, and bursting, see Amazon EBS Volume Types in
     *                                             the Amazon Elastic Compute Cloud User Guide.
     *                                             Constraint: Range is 100-20000 IOPS for "io1" volumes and 100-10000 IOPS
     *                                             for "gp2" volumes.
     *                                             Condition: This parameter is required for requests to create "io1" volumes;
     *                                             it is not used in requests to create "gp2", "st1", "sc1", or "standard"
     *                                             volumes.
     *                                             Example: For a first EBS device (from existing 3 devices), with type
     *                                             "io1" that should have 5000 IOPS as value the string will be: "5000,,".
     *                                             If no value provided the the default value for every single EBS device
     *                                             will be considered.
     *                                             Default: ""
     * @param volumeTypesString                    Optional - String that contains one or more values that specifies the
     *                                             volume types: "gp2", "io1", "st1", "sc1", or "standard". If no value
     *                                             provided then the default value for every single EBS device type will
     *                                             be considered.
     *                                             Default: ""
     * @param snapshotIdsString                    Optional - String that contains one or more values of the snapshot ID
     *                                             to be used when creating the EBS device.
     *                                             Example: For a last EBS device (from existing 3 devices), to be created
     *                                             using a snapshot as image the string will be:
     *                                             "Not relevant,Not relevant,snap-abcdef12".
     *                                             If no value provided then no snapshot will be used when creating EBS
     *                                             device.
     *                                             Default: ""
     * @param volumeSizesString                    Optional - String that contains one or more values of the sizes
     *                                             (in GiB) for EBS devices.
     *                                             Constraints: 1-16384 for General Purpose SSD ("gp2"), 4-16384 for
     *                                             Provisioned IOPS SSD ("io1"), 500-16384 for Throughput Optimized HDD
     *                                             ("st1"), 500-16384 for Cold HDD ("sc1"), and 1-1024 for Magnetic
     *                                             ("standard") volumes. If you specify a snapshot, the volume size must
     *                                             be equal to or larger than the snapshot size. If you're creating the
     *                                             volume from a snapshot and don't specify a volume size, the default is
     *                                             the snapshot size.
     *                                             Examples: "Not relevant,Not relevant,100"
     *                                             Default: ""
     * @param iamInstanceProfileArn                Optional - Amazon Resource Name (IAM_INSTANCE_PROFILE_ARN) of the instance
     *                                             profile.
     *                                             Default: ""
     * @param iamInstanceProfileName               Optional - Name of the instance profile.
     *                                             Default: ""
     * @param keyPairName                          Optional - Name of the key pair. You can create a key pair using
     *                                             <CreateKeyPair> or <ImportKeyPair>.
     *                                             Important: If you do not specify a key pair, you can't connect to the
     *                                             instance unless you choose an AMI that is configured to allow users
     *                                             another way to log in.
     * @param clientToken                          Optional - Unique, case-sensitive identifier you provide to ensure the
     *                                             idempotency of the request. For more information, see Ensuring Idempotency.
     *                                             Constraints: Maximum 64 ASCII characters
     * @param disableApiTermination                Optional - If you set this parameter to "true", you can't terminate the
     *                                             instance using the Amazon EC2 console, CLI, or API; otherwise, you can.
     *                                             If you set this parameter to "true" and then later want to be able to
     *                                             terminate the instance, you must first change the value of the
     *                                             <disableApiTermination> attribute to "false" using <ModifyInstanceAttribute>.
     *                                             Alternatively, if you set <InstanceInitiatedShutdownBehavior> to "terminate",
     *                                             you can terminate the instance by running the shutdown command from the
     *                                             instance.
     *                                             Valid values: "true", "false"
     *                                             Default: "false"
     * @param instanceInitiatedShutdownBehavior    Optional - Indicates whether an instance stops or terminates when you
     *                                             initiate shutdown from the instance (using the operating system command
     *                                             for system shutdown).
     *                                             Valid values: "stop", "terminate"
     *                                             Default: "stop"
     * @param maxCount                             Maximum number of instances to launch. If you specify more instances
     *                                             than Amazon EC2 can launch in the target Availability Zone, Amazon EC2
     *                                             launches the largest possible number of instances above MinCount.
     *                                             Constraints: Between 1 and the maximum number you're allowed for the
     *                                             specified instance type. For more information about the default limits,
     *                                             and how to request an increase, see:
     *                                             https://aws.amazon.com/ec2/faqs/#How_many_instances_can_I_run_in_Amazon_EC2
     *                                             Default: "1"
     * @param minCount                             Minimum number of instances to launch. If you specify a minimum that is
     *                                             more instances than Amazon EC2 can launch in the target Availability
     *                                             Zone, Amazon EC2 launches no instances.
     *                                             Constraints: Between 1 and the maximum number you're allowed for the
     *                                             specified instance type. For more information about the default limits,
     *                                             and how to request an increase, see:
     *                                             https://aws.amazon.com/ec2/faqs/#How_many_instances_can_I_run_in_Amazon_EC2
     *                                             Default: "1"
     * @param monitoring                           Optional - monitoring for the instance.
     *                                             Default: "false"
     * @param affinity                             Optional - Affinity setting for the instance on the Dedicated Host.
     *                                             This parameter is not supported for the <ImportInstance> command.
     *                                             Default: ""
     * @param placementGroupName                   Optional - Name of the placement group for the instance.
     *                                             Default: ""
     * @param tenancy                              Optional - Tenancy of an instance (if the instance is running in a VPC).
     *                                             An instance with a tenancy of dedicated runs on single-tenant hardware.
     *                                             The host tenancy is not supported for the ImportInstance command.
     *                                             Valid values: "dedicated", "default", "host".
     * @param associatePublicIpAddress             Optional - Indicates whether to assign a public IP address to an instance
     *                                             you launch in a VPC. The public IP address can only be assigned to a
     *                                             network interface for eth0, and can only be assigned to a new network
     *                                             interface, not an existing one. You cannot specify more than one network
     *                                             interface in the request. If launching into a default subnet, the default
     *                                             value is "true".
     *                                             Valid values: "true", "false"
     *                                             Default: "true"
     * @return A map with strings as keys and strings as values that contains: outcome of the action (or failure message
     * and the exception if there is one), returnCode of the operation and the ID of the request
     */
    @Action(name = "Run Instances",
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
    public Map<String, String> runInstances(@Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String endpoint,
                                            @Param(value = Inputs.CommonInputs.IDENTITY, required = true) String identity,
                                            @Param(value = Inputs.CommonInputs.CREDENTIAL, required = true, encrypted = true) String credential,
                                            @Param(value = Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                            @Param(value = Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                            @Param(value = Inputs.CommonInputs.PROXY_USERNAME) String proxyUsername,
                                            @Param(value = Inputs.CommonInputs.PROXY_PASSWORD, encrypted = true) String proxyPassword,
                                            @Param(value = Inputs.CommonInputs.HEADERS) String headers,
                                            @Param(value = Inputs.CommonInputs.QUERY_PARAMS) String queryParams,
                                            @Param(value = Inputs.CommonInputs.VERSION, required = true) String version,
                                            @Param(value = Inputs.CommonInputs.DELIMITER) String delimiter,

                                            @Param(value = Inputs.CustomInputs.AVAILABILITY_ZONE) String availabilityZone,
                                            @Param(value = Inputs.CustomInputs.HOST_ID) String hostId,
                                            @Param(value = Inputs.CustomInputs.IMAGE_ID, required = true) String imageId,
                                            @Param(value = Inputs.CustomInputs.INSTANCE_TYPE) String instanceType,
                                            @Param(value = Inputs.CustomInputs.KERNEL_ID) String kernelId,
                                            @Param(value = Inputs.CustomInputs.RAMDISK_ID) String ramdiskId,
                                            @Param(value = Inputs.CustomInputs.SUBNET_ID) String subnetId,

                                            @Param(value = Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING) String blockDeviceMappingDeviceNamesString,
                                            @Param(value = Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING) String blockDeviceMappingVirtualNamesString,
                                            @Param(value = Inputs.EbsInputs.DELETE_ON_TERMINATIONS_STRING) String deleteOnTerminationsString,
                                            @Param(value = Inputs.EbsInputs.EBS_OPTIMIZED) String ebsOptimized,
                                            @Param(value = Inputs.EbsInputs.ENCRYPTED_STRING) String encryptedString,
                                            @Param(value = Inputs.EbsInputs.IOPS_STRING) String iopsString,
                                            @Param(value = Inputs.EbsInputs.SNAPSHOT_IDS_STRING) String snapshotIdsString,
                                            @Param(value = Inputs.EbsInputs.VOLUME_SIZES_STRING) String volumeSizesString,
                                            @Param(value = Inputs.EbsInputs.VOLUME_TYPES_STRING) String volumeTypesString,

                                            @Param(value = Inputs.ElasticIpInputs.PRIVATE_IP_ADDRESS) String privateIpAddress,

                                            @Param(value = Inputs.IamInputs.IAM_INSTANCE_PROFILE_ARN) String iamInstanceProfileArn,
                                            @Param(value = Inputs.IamInputs.IAM_INSTANCE_PROFILE_NAME) String iamInstanceProfileName,
                                            @Param(value = Inputs.IamInputs.KEY_PAIR_NAME) String keyPairName,

                                            @Param(value = Inputs.InstanceInputs.AFFINITY) String affinity,
                                            @Param(value = Inputs.InstanceInputs.CLIENT_TOKEN) String clientToken,
                                            @Param(value = Inputs.InstanceInputs.DISABLE_API_TERMINATION) String disableApiTermination,
                                            @Param(value = Inputs.InstanceInputs.INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR) String instanceInitiatedShutdownBehavior,
                                            @Param(value = Inputs.InstanceInputs.MAX_COUNT) String maxCount,
                                            @Param(value = Inputs.InstanceInputs.MIN_COUNT) String minCount,
                                            @Param(value = Inputs.InstanceInputs.MONITORING) String monitoring,
                                            @Param(value = Inputs.InstanceInputs.PLACEMENT_GROUP_NAME) String placementGroupName,
                                            @Param(value = Inputs.InstanceInputs.TENANCY) String tenancy,
                                            @Param(value = Inputs.InstanceInputs.USER_DATA) String userData,

                                            @Param(value = Inputs.NetworkInputs.ASSOCIATE_PUBLIC_IP_ADDRESS) String associatePublicIpAddress) {
        try {
            CommonInputs commonInputs = new CommonInputs.CommonInputsBuilder()
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
                    .withAction(Constants.QueryApiActions.RUN_INSTANCES)
                    .withApiService(Constants.Apis.AMAZON_EC2_API)
                    .withRequestUri(Constants.Miscellaneous.EMPTY)
                    .withRequestPayload(Constants.Miscellaneous.EMPTY)
                    .withHttpClientMethod(Constants.AwsParams.HTTP_CLIENT_METHOD_GET)
                    .build();

            CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                    .withAvailabilityZone(availabilityZone)
                    .withHostId(hostId)
                    .withImageId(imageId)
                    .withInstanceType(instanceType)
                    .withKernelId(kernelId)
                    .withRamdiskId(ramdiskId)
                    .withSubnetId(subnetId)
                    .build();

            EbsInputs ebsInputs = new EbsInputs.EbsInputsBuilder()
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

            ElasticIpInputs elasticIpInputs = new ElasticIpInputs.ElasticIpInputsBuilder()
                    .withPrivateIpAddress(privateIpAddress)
                    .build();

            IamInputs iamInputs = new IamInputs.IamInputsBuilder()
                    .withIamInstanceProfileArn(iamInstanceProfileArn)
                    .withIamInstanceProfileName(iamInstanceProfileName)
                    .withKeyPairName(keyPairName)
                    .build();

            InstanceInputs instanceInputs = new InstanceInputs.InstanceInputsBuilder()
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

            NetworkInputs networkInputs = new NetworkInputs.NetworkInputsBuilder()
                    .withAssociatePublicIpAddress(associatePublicIpAddress)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, ebsInputs, elasticIpInputs, iamInputs,
                    instanceInputs, networkInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}