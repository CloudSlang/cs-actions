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
import io.cloudslang.content.jclouds.execute.instances.UpdateInstanceTypeExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class UpdateInstanceTypeAction {
    /**
     * Updates (changes) instance (identified by "instanceId") type with the new one specified by "serverType".
     *
     * @param provider         Cloud provider on which you have the instance - Valid values: "amazon" or "openstack".
     * @param endpoint         Endpoint to which request will be sent. Example: "https://ec2.amazonaws.com" for Amazon AWS
     *                         or "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity         Optional - username of your account or the Access Key ID. For OpenStack provider the required
     *                         format is 'alias:username'.
     * @param credential       Optional - password of the user or the Secret Access Key that correspond to the identity
     *                         input.
     * @param proxyHost        Optional - proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort        Optional - proxy server port.
     * @param debugMode        Optional - if "true" then the execution logs will be shown in CLI console.
     * @param region           Optional - region where the server (instance) to be started can be found.
     *                         listRegionsAction operation can be used in order to get all regions - Default: 'us-east-1'
     * @param instanceId       ID of the server (instance) you want to update.
     * @param instanceType     Optional - new server type to be used when updating the instance. The complete list of instance
     *                         types can be found at: http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/instance-types.html
     *                         - Example: 't2.medium', 'm3.large' - Default: 't2.micro'
     * @param operationTimeout Optional - total time (in milliseconds) that operation will wait to complete the execution
     * @param poolingInterval  Optional - time (in milliseconds) that operation will wait until next check of the instance
     *                         state
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Update Instance Type",
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
                                       @Param(value = Inputs.CustomInputs.INSTANCE_ID, required = true) String instanceId,
                                       @Param(value = Inputs.CustomInputs.INSTANCE_TYPE) String instanceType,

                                       @Param(value = Inputs.InstanceInputs.OPERATION_TIMEOUT) String operationTimeout,
                                       @Param(value = Inputs.InstanceInputs.POOLING_INTERVAL) String poolingInterval)
            throws Exception {

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
                .withInstanceId(instanceId)
                .withInstanceType(instanceType)
                .build();

        InstanceInputs instanceInputs = new InstanceInputs.InstanceInputsBuilder()
                .withCustomInputs(customInputs)
                .withCheckStateTimeout(operationTimeout)
                .withPolingInterval(poolingInterval)
                .build();

        try {
            return new UpdateInstanceTypeExecutor().execute(inputs, instanceInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}