/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
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
import io.cloudslang.content.amazon.entities.inputs.EbsInputs;
import io.cloudslang.content.amazon.entities.inputs.IamInputs;
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.execute.QueryApiExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;

import java.util.Map;

import static io.cloudslang.content.amazon.utils.InputsUtil.getDefaultStringInput;

import static io.cloudslang.content.amazon.entities.constants.Constants.Apis.EC2_API;
import static io.cloudslang.content.amazon.entities.constants.Constants.AwsParams.HTTP_CLIENT_METHOD_GET;
import static io.cloudslang.content.amazon.entities.constants.Constants.DefaultApiVersion.INSTANCES_DEFAULT_API_VERSION;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;
import static io.cloudslang.content.amazon.entities.constants.Constants.Ec2QueryApiActions.MODIFY_INSTANCE_ATTRIBUTE;
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
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.INSTANCE_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.DELETE_ON_TERMINATIONS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.EBS_OPTIMIZED;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.NO_DEVICES_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.EbsInputs.VOLUME_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.IamInputs.SECURITY_GROUP_IDS_STRING;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.ATTRIBUTE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.ATTRIBUTE_VALUE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_DISABLE_API_TERMINATION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.ENA_SUPPORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_KERNEL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_RAMDISK;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.SOURCE_DESTINATION_CHECK;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.SRIOV_NET_SUPPORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.LOWER_CASE_USER_DATA;

/**
 * Created by TusaM
 * 10/24/2016.
 */
public class ModifyInstanceAttributeAction {
    /**
     * Modifies the specified attribute of the specified instance.
     * Note: You can specify only one attribute at a time. To modify some attributes, the instance must be stopped.
     * For more information, see Modifying Attributes of a Stopped Instance in the Amazon Elastic Compute Cloud User Guide:
     * http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/Stop_Start.html
     *
     * @param endpoint                             Endpoint to which request will be sent.
     *                                             Default: "https://ec2.amazonaws.com"
     * @param identity                             ID of the secret access key associated with your Amazon AWS or IAM account.
     *                                             Example: "AKIAIOSFODNN7EXAMPLE"
     * @param credential                           Secret access key associated with your Amazon AWS or IAM account.
     *                                             Example: "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY"
     * @param proxyHost                            Optional - proxy server used to connect to Amazon API. If empty no proxy
     *                                             will be used.
     *                                             Default: ""
     * @param proxyPort                            Optional - proxy server port. You must either specify values for both
     *                                             proxyHost and proxyPort inputs or leave them both empty.
     *                                             Default: ""
     * @param proxyUsername                        Optional - proxy server user name.
     *                                             Default: ""
     * @param proxyPassword                        Optional - proxy server password associated with the proxyUsername
     *                                             input value.
     *                                             Default: ""
     * @param headers                              Optional - string containing the headers to use for the request separated
     *                                             by new line (CRLF). The header name-value pair will be separated by ":".
     *                                             Format: Conforming with HTTP standard for headers (RFC 2616)
     *                                             Examples: "Accept:text/plain"
     *                                             Default: ""
     * @param queryParams                          Optional - string containing query parameters that will be appended to
     *                                             the URL. The names and the values must not be URL encoded because if
     *                                             they are encoded then a double encoded will occur. The separator between
     *                                             name-value pairs is "&" symbol. The query name will be separated from
     *                                             query value by "=".
     *                                             Examples: "parameterName1=parameterValue1&parameterName2=parameterValue2"
     *                                             Default: ""
     * @param version                              Version of the web service to made the call against it.
     *                                             Example: "2016-11-15"
     *                                             Default: "2016-11-15"
     * @param delimiter                            Optional - Delimiter that will be used.
     *                                             Default: ","
     * @param attribute                            Optional - name of the attribute.
     *                                             Valid values: "instanceType | kernel | ramdisk | userData | disableApiTermination |
     *                                             instanceInitiatedShutdownBehavior | rootDeviceName | blockDeviceMapping |
     *                                             productCodes | sourceDestCheck | groupSet | ebsOptimized | sriovNetSupport |
     *                                             enaSupport"
     * @param attributeValue                       Optional - A new value for the attribute. Use only with: "kernel", "ramdisk",
     *                                             "userData", "disableApiTermination", or "instanceInitiatedShutdownBehavior"
     *                                             attributes.
     *                                             Default: ""
     * @param blockDeviceMappingDeviceNamesString  Optional - String that contains one or more device names, exposed to
     *                                             the instance, separated by delimiter.
     *                                             Examples: "/dev/sdh,xvdh"
     *                                             Default: ""
     * @param blockDeviceMappingVirtualNamesString Optional - String that contains one or more virtual names separated
     *                                             by delimiter. Virtual device name is "ephemeralN". Instance store
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
     *                                             Example: For a third EBS device (from existing 4 devices), that
     *                                             should be deleted, the string will be: "false,false,true,false".
     *                                             Valid values: "true", "false"
     *                                             Default: ""
     * @param volumeIdsString                      Optional - String that contains one or more values that indicates
     *                                             volume Ids.
     *                                             Default: ""
     * @param noDevicesString                      Optional - String that contains one or more values that indicates
     *                                             if a certain specified device included in the block device mapping
     *                                             will be suppressed.
     *                                             Example: For a second EBS device (from existing 4 devices), that
     *                                             should be suppressed, the string will be: ",No device,,".
     *                                             Default: ""
     * @param disableApiTermination                Optional - If the value is "true", you can't terminate the instance
     *                                             using the Amazon EC2 console, CLI, or API; otherwise, you can. You
     *                                             cannot use this paramater for Spot Instances.
     *                                             Valid values: "true", "false"
     *                                             Default: "false"
     * @param ebsOptimized                         Optional - Specifies whether the instance is optimized for EBS I/O. This
     *                                             optimization provides dedicated throughput to Amazon EBS and an optimized
     *                                             configuration stack to provide optimal EBS I/O performance. This optimization
     *                                             isn't available with all instance types. Additional usage charges apply
     *                                             when using an EBS Optimized instance.
     *                                             Valid values: "true", "false"
     *                                             Default: "false"
     * @param enaSupport                           Optional - Set to "true" to enable enhanced networking with ENA for the
     *                                             instance. This option is supported only for HVM instances. Specifying
     *                                             this option with a PV instance can make it unreachable.
     *                                             Valid values: "true", "false"
     *                                             Default: "false"
     * @param securityGroupIdsString               Optional - [EC2-VPC] Changes the security groups of the instance. You
     *                                             must specify at least one security group, even if it's just the default
     *                                             security group for the VPC. You must specify the security group IDs,
     *                                             not the security group names.
     *                                             Default: ""
     * @param instanceId                           ID of the instance.
     *                                             Example: "i-12345678"
     * @param instanceInitiatedShutdownBehavior    Optional - Specifies whether an instance stops or terminates when you
     *                                             initiate shutdown from the instance (using the operating system command
     *                                             for system shutdown).
     *                                             Valid values: "stop", "terminate"
     *                                             Default: "stop"
     * @param instanceType                         Optional - Changes the instance type to the specified value. If the
     *                                             instance type is not valid, the error returned is InvalidInstanceAttributeValue.
     *                                             For more information, see Instance Types in the Amazon Elastic Compute
     *                                             Cloud User Guide.
     *                                             Valid values: t1.micro | t2.nano | t2.micro | t2.small | t2.medium |
     *                                             t2.large | m1.small | m1.medium | m1.large | m1.xlarge | m3.medium |
     *                                             m3.large | m3.xlarge | m3.2xlarge | m4.large | m4.xlarge | m4.2xlarge |
     *                                             m4.4xlarge | m4.10xlarge | m2.xlarge | m2.2xlarge | m2.4xlarge |
     *                                             cr1.8xlarge | r3.large | r3.xlarge | r3.2xlarge | r3.4xlarge |
     *                                             r3.8xlarge | x1.4xlarge | x1.8xlarge | x1.16xlarge | x1.32xlarge |
     *                                             i2.xlarge | i2.2xlarge | i2.4xlarge | i2.8xlarge | hi1.4xlarge |
     *                                             hs1.8xlarge | c1.medium | c1.xlarge | c3.large | c3.xlarge |
     *                                             c3.2xlarge | c3.4xlarge | c3.8xlarge | c4.large | c4.xlarge |
     *                                             c4.2xlarge | c4.4xlarge | c4.8xlarge | cc1.4xlarge | cc2.8xlarge |
     *                                             g2.2xlarge | g2.8xlarge | cg1.4xlarge | d2.xlarge | d2.2xlarge |
     *                                             d2.4xlarge | d2.8xlarge"
     *                                             Default: "m1.small"
     * @param kernel                               Optional - Changes the instance's kernel to the specified value. We
     *                                             recommend that you use PV-GRUB instead of kernels and RAM disks. For
     *                                             more information, see PV-GRUB: http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/UserProvidedKernels.html
     *                                             Default: ""
     * @param sourceDestinationCheck               Optional - Specifies whether source/destination checking is enabled.
     *                                             A value of "true" means that checking is enabled, and "false" means
     *                                             checking is disabled. This value must be "false" for a NAT instance to
     *                                             perform NAT.
     *                                             Valid values: "true", "false"
     *                                             Default: "false"
     * @param sriovNetSupport                      Optional - Set to "simple" to enable enhanced networking with the Intel
     *                                             82599 Virtual Function interface for the instance. There is no way to
     *                                             disable enhanced networking with the Intel 82599 Virtual Function interface
     *                                             at this time. This option is supported only for HVM instances. Specifying
     *                                             this option with a PV instance can make it unreachable.
     *                                             Default: ""
     * @param userData                             Optional - Changes the instance's user data to the specified value.
     *                                             If you are using an AWS SDK or command line tool, Base64-encoding is
     *                                             performed for you, and you can load the text from a file. Otherwise,
     *                                             you must provide Base64-encoded text.
     *                                             Default: ""
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     *         operation, or failure message and the exception if there is one
     */
    @Action(name = "Modify Instance Attribute",
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
    public Map<String, String> execute(@Param(value = ENDPOINT, required = true) String endpoint,
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
                                       @Param(value = ATTRIBUTE) String attribute,
                                       @Param(value = ATTRIBUTE_VALUE) String attributeValue,
                                       @Param(value = BLOCK_DEVICE_MAPPING_DEVICE_NAMES_STRING) String blockDeviceMappingDeviceNamesString,
                                       @Param(value = BLOCK_DEVICE_MAPPING_VIRTUAL_NAMES_STRING) String blockDeviceMappingVirtualNamesString,
                                       @Param(value = DELETE_ON_TERMINATIONS_STRING) String deleteOnTerminationsString,
                                       @Param(value = VOLUME_IDS_STRING) String volumeIdsString,
                                       @Param(value = NO_DEVICES_STRING) String noDevicesString,
                                       @Param(value = LOWER_CASE_DISABLE_API_TERMINATION) String disableApiTermination,
                                       @Param(value = EBS_OPTIMIZED) String ebsOptimized,
                                       @Param(value = ENA_SUPPORT) String enaSupport,
                                       @Param(value = SECURITY_GROUP_IDS_STRING) String securityGroupIdsString,
                                       @Param(value = INSTANCE_ID, required = true) String instanceId,
                                       @Param(value = LOWER_CASE_INSTANCE_INITIATED_SHUTDOWN_BEHAVIOR) String instanceInitiatedShutdownBehavior,
                                       @Param(value = INSTANCE_TYPE) String instanceType,
                                       @Param(value = LOWER_CASE_KERNEL) String kernel,
                                       @Param(value = LOWER_CASE_RAMDISK) String ramdisk,
                                       @Param(value = SOURCE_DESTINATION_CHECK) String sourceDestinationCheck,
                                       @Param(value = SRIOV_NET_SUPPORT) String sriovNetSupport,
                                       @Param(value = LOWER_CASE_USER_DATA) String userData) {
        try {
            version = getDefaultStringInput(version, INSTANCES_DEFAULT_API_VERSION);

            final CommonInputs commonInputs = new CommonInputs.Builder()
                    .withEndpoint(endpoint, EC2_API, EMPTY)
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
                    .withAction(MODIFY_INSTANCE_ATTRIBUTE)
                    .withApiService(EC2_API)
                    .withRequestUri(EMPTY)
                    .withRequestPayload(EMPTY)
                    .withHttpClientMethod(HTTP_CLIENT_METHOD_GET)
                    .build();

            final CustomInputs customInputs = new CustomInputs.Builder()
                    .withInstanceId(instanceId)
                    .withInstanceType(instanceType)
                    .build();

            EbsInputs ebsInputs = new EbsInputs.Builder()
                    .withEbsOptimized(ebsOptimized)
                    .withBlockDeviceMappingDeviceNamesString(blockDeviceMappingDeviceNamesString)
                    .withBlockDeviceMappingVirtualNamesString(blockDeviceMappingVirtualNamesString)
                    .withDeleteOnTerminationsString(deleteOnTerminationsString)
                    .withVolumeIdsString(volumeIdsString)
                    .withNoDevicesString(noDevicesString)
                    .build();

            IamInputs iamInputs = new IamInputs.Builder().withSecurityGroupIdsString(securityGroupIdsString).build();

            InstanceInputs instanceInputs = new InstanceInputs.Builder()
                    .withAttribute(attribute)
                    .withAttributeValue(attributeValue)
                    .withDisableApiTermination(disableApiTermination)
                    .withEnaSupport(enaSupport)
                    .withInstanceInitiatedShutdownBehavior(instanceInitiatedShutdownBehavior)
                    .withKernel(kernel)
                    .withRamdisk(ramdisk)
                    .withSourceDestinationCheck(sourceDestinationCheck)
                    .withSriovNetSupport(sriovNetSupport)
                    .withUserData(userData)
                    .build();

            return new QueryApiExecutor().execute(commonInputs, customInputs, ebsInputs, iamInputs, instanceInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}
