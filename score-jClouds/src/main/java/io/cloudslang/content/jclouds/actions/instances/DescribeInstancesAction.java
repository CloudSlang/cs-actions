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
     * @param provider            The cloud provider on which you have the instance. Valid values: "amazon" or "openstack".
     * @param identityEndpoint    The endpoint to which first request will be sent. Example: "https://ec2.amazonaws.com"
     *                            for Amazon AWS or "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity            The username of your account or the Access Key ID. For openstack provider the required
     *                            format is 'alias:username'.
     * @param credential          The password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost           The proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort           The proxy server port.
     * @param region              The region from which to list servers. Ex: "RegionOne", "us-east-1". ListRegionAction can
     *                            be used in order to get all regions.
     * @param volumeId            The volume ID of the EBS volume.
     * @param groupId             The ID of the security group for the instance. EC2-Classic only.
     * @param affinity            The affinity setting for an instance running on a dedicated host.
     *                            Valid values: "default" or "host".
     * @param architecture        The instance architecture. Valid values: "i386" or "x86_64".
     * @param availabilityZone    The Availability Zone of the instance.
     * @param attachTime          The attach time for an EBS volume mapped to the instance. Ex: "2010-09-15T17:15:20.000Z"
     * @param deleteOnTermination A Boolean that indicates whether the EBS volume is deleted on instance termination.
     * @param deviceName          The device name for the EBS volume. Ex: "/dev/sdh" or "xvdh".
     * @param status              The status for the EBS volume. Valid values: "attaching", "attached", "detaching", "detached".
     * @param clientToken         The idem-potency token that was provided when the instance was launched.
     * @param dnsName             The public DNS name of the instance.
     * @param groupName           The name of the security group for the instance. EC2-Classic only.
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
                                       @Param(Inputs.InstanceInputs.AFFINITY) String affinity,
                                       @Param(Inputs.InstanceInputs.ARCHITECTURE) String architecture,
                                       @Param(Inputs.InstanceInputs.AVAILABILITY_ZONE) String availabilityZone,
                                       @Param(Inputs.InstanceInputs.ATTACH_TIME) String attachTime,
                                       @Param(Inputs.InstanceInputs.DELETE_ON_TERMINATION) String deleteOnTermination,
                                       @Param(Inputs.InstanceInputs.DEVICE_NAME) String deviceName,
                                       @Param(Inputs.InstanceInputs.STATUS) String status,
                                       @Param(Inputs.InstanceInputs.CLIENT_TOKEN) String clientToken,
                                       @Param(Inputs.InstanceInputs.DNS_NAME) String dnsName,
                                       @Param(Inputs.InstanceInputs.GROUP_NAME) String groupName) throws Exception {

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
                .build();

        InstanceInputs instanceInputs = new InstanceInputs.InstanceInputsBuilder()
                .withCustomInputs(customInputs)
                .withAffinity(affinity)
                .withArchitecture(architecture)
                .withAvailabilityZone(availabilityZone)
                .withAttachTime(attachTime)
                .withDeleteOnTermination(deleteOnTermination)
                .withDeviceName(deviceName)
                .withStatus(status)
                .withClientToken(clientToken)
                .withDnsName(dnsName)
                .withGroupName(groupName)
                .build();

        try {
            return new DescribeInstancesExecutor().execute(inputs, instanceInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}
