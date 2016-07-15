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
import io.cloudslang.content.jclouds.execute.volumes.AttachVolumeInRegionExecutor;
import io.cloudslang.content.jclouds.execute.volumes.DetachVolumeInRegionExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 6/24/2016.
 */
public class DetachVolumeInRegionAction {
    /**
     * Detaches an EBS volume from an instance.
     * <p>
     * Note: Make sure to un-mount any file systems on the device within your operating system before detaching the volume.
     * Failure to do so results in the volume being stuck in a busy state while detaching. If an Amazon EBS volume is the
     * root device of an instance, it can't be detached while the instance is running. To detach the root volume, stop the
     * instance first. When a volume with an AWS Marketplace product code is detached from an instance, the product code
     * is no longer associated with the instance. For more information, see Detaching an Amazon EBS Volume in the Amazon
     * Elastic Compute Cloud User Guide.
     *
     * @param provider   Cloud provider on which you have the instance - Valid values: "amazon" or "openstack".
     * @param endpoint   Endpoint to which first request will be sent. Ex: "https://ec2.amazonaws.com" for amazon or
     *                   "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity   Optional - Username of your account or the Access Key ID. For OpenStack provider the required
     *                   format is 'alias:username'.
     * @param credential Optional - Password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost  Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort  Optional - Proxy server port.
     * @param region     Optional - region where volume belongs. Ex: "RegionOne", "us-east-1".
     *                   ListRegionAction can be used in order to get all regions - Default: "us-east-1"
     * @param volumeId   ID of the EBS volume. The volume and instance must be within the same Availability Zone.
     * @param instanceId Optional - ID of the instance.
     * @param deviceName Optional - Device name.
     * @param force      Optional - Forces detachment if the previous detachment attempt did not occur cleanly (for example,
     *                   logging into an instance, un-mounting the volume, and detaching normally). This option can lead
     *                   to data loss or a corrupted file system. Use this option only as a last resort to detach a volume
     *                   from a failed instance. The instance won't have an opportunity to flush file system caches or file
     *                   system metadata. If you use this option, you must perform file system check and repair procedures.
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Detach Volume in Region",
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
                                       @Param(Inputs.CustomInputs.INSTANCE_ID) String instanceId,

                                       @Param(Inputs.VolumeInputs.DEVICE_NAME) String deviceName,
                                       @Param(Inputs.VolumeInputs.FORCE) String force) throws Exception {

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
                .withInstanceId(instanceId)
                .build();

        VolumeInputs volumeInputs = new VolumeInputs.VolumeInputsBuilder()
                .withCustomInputs(customInputs)
                .withDeviceName(deviceName)
                .withForce(force)
                .build();

        try {
            return new DetachVolumeInRegionExecutor().execute(inputs, volumeInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}
