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
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 6/24/2016.
 */
public class AttachVolumeInRegionAction {
    /**
     * Attaches an EBS volume to a running or stopped instance and exposes it to the instance with the specified device
     * name.
     * <p>
     * Note: Encrypted EBS volumes may only be attached to instances that support Amazon EBS encryption. For more information,
     * see Amazon EBS Encryption in the Amazon Elastic Compute Cloud User Guide. For a list of supported device names, see
     * Attaching an EBS Volume to an Instance. Any device names that aren't reserved for instance store volumes can be used
     * for EBS volumes. For more information, see Amazon EC2 Instance Store in the Amazon Elastic Compute Cloud User Guide.
     * <p>
     * If a volume has an AWS Marketplace product code:
     * - The volume can be attached only to a stopped instance.
     * - AWS Marketplace product codes are copied from the volume to the instance.
     * - You must be subscribed to the product.
     * - The instance type and operating system of the instance must support the product. For example, you can't detach
     * a volume from a Windows instance and attach it to a Linux instance.
     * For more information about EBS volumes, see Attaching Amazon EBS Volumes in the Amazon Elastic Compute Cloud User
     * Guide.
     *
     * @param provider   Cloud provider on which you have the instance - Valid values: "amazon" or "openstack".
     * @param endpoint   Endpoint to which request will be sent. Ex: "https://ec2.amazonaws.com" for Amazon AWS or
     *                   "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity   Optional - username of your account or the Access Key ID. For OpenStack provider the required
     *                   format is 'alias:username'.
     * @param credential Optional - password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost  Optional - proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort  Optional - proxy server port.
     * @param debugMode  Optional - if "true" then the execution logs will be shown in CLI console.
     * @param region     Optional - region where volume belongs. Ex: "RegionOne", "us-east-1".
     *                   ListRegionAction can be used in order to get all regions - Default: "us-east-1"
     * @param volumeId   ID of the EBS volume. The volume and instance must be within the same Availability Zone.
     * @param instanceId ID of the instance.
     * @param deviceName Device name.
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Attach Volume in Region",
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
                                       @Param(value = Inputs.CommonInputs.DEBUG_MODE) String debugMode,

                                       @Param(value = Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.VOLUME_ID, required = true) String volumeId,
                                       @Param(value = Inputs.CustomInputs.INSTANCE_ID, required = true) String instanceId,

                                       @Param(value = Inputs.VolumeInputs.DEVICE_NAME, required = true) String deviceName)
            throws Exception {

        CommonInputs inputs = new CommonInputs.Builder()
                .withProvider(provider)
                .withEndpoint(endpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDebugMode(debugMode)
                .build();

        CustomInputs customInputs = new CustomInputs.Builder()
                .withRegion(region)
                .withVolumeId(volumeId)
                .withInstanceId(instanceId)
                .build();

        VolumeInputs volumeInputs = new VolumeInputs.VolumeInputsBuilder()
                .withCustomInputs(customInputs)
                .withDeviceName(deviceName)
                .build();

        try {
            return new AttachVolumeInRegionExecutor().execute(inputs, volumeInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}