package io.cloudslang.content.jclouds.actions.snapshots;

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
import io.cloudslang.content.jclouds.execute.snapshots.CreateSnapshotInRegionExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 6/28/2016.
 */
public class CreateSnapshotInRegionAction {
    /**
     * Creates a snapshot of an EBS volume and stores it in Amazon S3. You can use snapshots for backups, to make copies
     * of EBS volumes, and to save data before shutting down an instance.
     * Note: When a snapshot is created, any AWS Marketplace product codes that are associated with the source volume are
     * propagated to the snapshot. You can take a snapshot of an attached volume that is in use. However, snapshots only
     * capture data that has been written to your EBS volume at the time the snapshot command is issued; this may exclude
     * any data that has been cached by any applications or the operating system. If you can pause any file systems on the
     * volume long enough to take a snapshot, your snapshot should be complete. However, if you cannot pause all file writes
     * to the volume, you should un-mount the volume from within the instance, issue the snapshot command, and then remount
     * the volume to ensure a consistent and complete snapshot. You may remount and use your volume while the snapshot
     * status is pending. To create a snapshot for EBS volumes that serve as root devices, you should stop the instance
     * before taking the snapshot. Snapshots that are taken from encrypted volumes are automatically encrypted. Volumes
     * that are created from encrypted snapshots are also automatically encrypted. Your encrypted volumes and any associated
     * snapshots always remain protected. For more information, see Amazon Elastic Block Store and Amazon EBS Encryption
     * in the Amazon Elastic Compute Cloud User Guide.
     *
     * @param provider   Cloud provider on which you have the instance - Valid values: "amazon" or "openstack".
     * @param endpoint   Endpoint to which first request will be sent. Ex: "https://ec2.amazonaws.com" for amazon or
     *                   "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity   Optional - Username of your account or the Access Key ID. For OpenStack provider the required
     *                   format is 'alias:username'.
     * @param credential Optional - Password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost  Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort  Optional - Proxy server port.
     * @param region     Optional - region where volume, to make snapshot for, belongs. Ex: "RegionOne", "us-east-1".
     *                   ListRegionAction can be used in order to get all regions - Default: "us-east-1"
     * @param volumeId   ID of the EBS volume.
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Create Snapshot in Region",
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

                                       @Param(Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.VOLUME_ID, required = true) String volumeId,
                                       @Param(Inputs.VolumeInputs.SNAPSHOT_DESCRIPTION) String snapshotDescription)
            throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(endpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .withVolumeId(volumeId)
                .build();

        VolumeInputs volumeInputs = new VolumeInputs.VolumeInputsBuilder()
                .withCustomInputs(customInputs)
                .withDescription(snapshotDescription)
                .build();

        try {
            return new CreateSnapshotInRegionExecutor().execute(inputs, volumeInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}
