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
import io.cloudslang.content.jclouds.execute.instances.RunInstancesExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/18/2016.
 */
public class RunInstancesAction {
    /**
     * Launches one ore more instances in a region based on specified "imageRef".
     *
     * @param provider         Cloud provider on which you launch the instances. Valid values: "amazon" or "openstack".
     * @param endpoint         Endpoint to which request will be sent. Example: "https://ec2.amazonaws.com"
     *                         for Amazon AWS or "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity         Optional - Username of your account or the Access Key ID. For OpenStack provider the required
     *                         format is 'alias:username'.
     * @param credential       Optional - Password of the user or the Secret Access Key that correspond to the identity
     *                         input.
     * @param proxyHost        Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort        Optional - Proxy server port.
     * @param debugMode        Optional - If "true" then the execution logs will be shown in CLI console.
     * @param region           Optional - the region where the server (instance) to be started can be found.
     *                         listRegionsAction can be used in order to get all regions - Default: 'us-east-1'
     * @param availabilityZone Optional - specifies the placement constraints for launching instance. Amazon automatically
     *                         selects an availability zone by default - Default: ''
     * @param imageId          ID of the AMI. For more information go to: http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ComponentsAMIs.html
     *                         Example: 'ami-fce3c696', 'ami-4b91bb21'
     * @param minCount         Optional - The minimum number of launched instances - Default: '1'
     * @param maxCount         Optional - The maximum number of launched instances - Default: '1'
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
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
    public Map<String, String> execute(@Param(value = Inputs.CommonInputs.PROVIDER, required = true) String provider,
                                       @Param(value = Inputs.CommonInputs.ENDPOINT, required = true) String endpoint,
                                       @Param(value = Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(value = Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(value = Inputs.CommonInputs.PROXY_PORT) String proxyPort,
                                       @Param(value = Inputs.CommonInputs.DEBUG_MODE) String debugMode,

                                       @Param(value = Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.IMAGE_ID, required = true) String imageId,
                                       @Param(value = Inputs.CustomInputs.AVAILABILITY_ZONE) String availabilityZone,
                                       @Param(value = Inputs.InstanceInputs.MIN_COUNT) String minCount,
                                       @Param(value = Inputs.InstanceInputs.MAX_COUNT) String maxCount) throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(endpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDebugMode(debugMode)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .withImageId(imageId)
                .withAvailabilityZone(availabilityZone)
                .build();

        InstanceInputs instanceInputs = new InstanceInputs.InstanceInputsBuilder()
                .withCustomInputs(customInputs)
                .withMinCount(minCount)
                .withMaxCount(maxCount)
                .build();

        try {
            return new RunInstancesExecutor().execute(inputs, instanceInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}