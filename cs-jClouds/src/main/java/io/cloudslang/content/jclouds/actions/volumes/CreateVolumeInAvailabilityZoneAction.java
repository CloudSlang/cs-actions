package io.cloudslang.content.jclouds.actions.volumes;

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
import io.cloudslang.content.jclouds.entities.inputs.VolumeInputs;
import io.cloudslang.content.jclouds.execute.volumes.CreateVolumeInAvailabilityZoneExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 6/22/2016.
 */
public class CreateVolumeInAvailabilityZoneAction {
    /**
     * Creates an EBS volume that can be attached to an instance in the same Availability Zone. The volume is created in
     * the regional endpoint that you send the HTTP request to.
     *
     * @param provider          Cloud provider on which you have the instance - Valid values: "amazon" or "openstack".
     * @param endpoint          Endpoint to which first request will be sent. Ex: "https://ec2.amazonaws.com" for amazon or
     *                          "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity          Optional - Username of your account or the Access Key ID. For OpenStack provider the required
     *                          format is 'alias:username'.
     * @param credential        Optional - Password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost         Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort         Optional - Proxy server port.
     * @param withExecutionLogs Optional - If "true" then the execution logs will be shown in CLI console.
     * @param region            Optional - region where volume will reside. Ex: "RegionOne", "us-east-1".
     *                          ListRegionAction can be used in order to get all regions - Default: "us-east-1"
     * @param availabilityZone  Specifies the placement constraints for launching instance. Amazon automatically
     *                          selects an availability zone by default - Default: ""
     * @param snapshotId        Optional - Snapshot from which to create the volume - Default: ""
     * @param volumeType        Optional - Volume type of the Amazon EBS volume - Valid values: "gp2" (for General
     *                          Purpose SSD volumes), "io1" (for Provisioned IOPS SSD volumes), "st1" (for Throughput
     *                          Optimized HDD), "sc1" (for Cold HDD) and "standard" (for Magnetic volumes).
     *                          Default: "standard"
     * @param size              Optional - size of the volume, in GiBs. Constraints: 1-16384 for gp2, 4-16384 for io1,
     *                          500-16384 for st1, 500-16384 for sc1, and 1-1024 for standard. If you specify a snapshot,
     *                          the volume size must be equal to or larger than the snapshot size. If you're creating the
     *                          volume from a snapshot and don't specify a volume size, the default is the snapshot size.
     * @param iops              Optional - Only valid for Provisioned IOPS SSD volumes. The number of I/O operations per
     *                          second (IOPS) to provision for the volume, with a maximum ratio of 30 IOPS/GiB. Constraint:
     *                          Range is 100 to 20000 for Provisioned IOPS SSD volumes.
     * @param encrypted         Specifies whether the volume should be encrypted. Encrypted Amazon EBS volumes may only be
     *                          attached to instances that support Amazon EBS encryption. Volumes that are created from
     *                          encrypted snapshots are automatically encrypted. There is no way to create an encrypted
     *                          volume from an unencrypted snapshot or vice versa. If your AMI uses encrypted volumes, you
     *                          can only launch it on supported instance types. For more information, see Amazon EBS Encryption
     *                          in the Amazon Elastic Compute Cloud User Guide - Valid values: "false", "true". Any other
     *                          but valid values provided will be ignored - Default: "false"
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Create Volume in Availability Zone",
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
                                       @Param(Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                       @Param(Inputs.CommonInputs.WITH_EXECUTION_LOGS) String withExecutionLogs,

                                       @Param(Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.AVAILABILITY_ZONE, required = true) String availabilityZone,
                                       @Param(Inputs.CustomInputs.VOLUME_TYPE) String volumeType,

                                       @Param(Inputs.VolumeInputs.SNAPSHOT_ID) String snapshotId,
                                       @Param(Inputs.VolumeInputs.SIZE) String size,
                                       @Param(Inputs.VolumeInputs.IOPS) String iops,
                                       @Param(Inputs.VolumeInputs.ENCRYPTED) String encrypted) throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(endpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withExecutionLogs(withExecutionLogs)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .withAvailabilityZone(availabilityZone)
                .withVolumeType(volumeType)
                .build();

        VolumeInputs volumeInputs = new VolumeInputs.VolumeInputsBuilder()
                .withCustomInputs(customInputs)
                .withSnapshotId(snapshotId)
                .withSize(size)
                .withIops(iops)
                .withEncrypted(encrypted)
                .build();

        try {
            return new CreateVolumeInAvailabilityZoneExecutor().execute(inputs, volumeInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}
