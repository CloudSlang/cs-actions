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
import io.cloudslang.content.amazon.entities.inputs.InstanceInputs;
import io.cloudslang.content.amazon.execute.instances.UpdateInstanceTypeExecutor;
import io.cloudslang.content.amazon.utils.ExceptionProcessor;

import java.util.Map;

import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.CREDENTIAL;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.DEBUG_MODE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.ENDPOINT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.IDENTITY;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROVIDER;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_HOST;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CommonInputs.PROXY_PORT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.INSTANCE_ID;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.INSTANCE_TYPE;
import static io.cloudslang.content.amazon.entities.constants.Inputs.CustomInputs.REGION;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.OPERATION_TIMEOUT;
import static io.cloudslang.content.amazon.entities.constants.Inputs.InstanceInputs.POOLING_INTERVAL;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class UpdateInstanceTypeAction {
    /**
     * Updates (changes) instance (identified by "instanceId") type with the new one specified by "serverType".
     *
     * @param provider         Cloud provider on which you have the instance - Valid values: "amazon" or "openstack".
     * @param endpoint         Optional - Endpoint to which request will be sent. Example: "https://ec2.amazonaws.com" for Amazon AWS
     *                         or "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity         Username of your account or the Access Key ID. For OpenStack provider the required
     *                         format is 'alias:username'.
     * @param credential       Password of the user or the Secret Access Key that correspond to the identity
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
    public Map<String, String> execute(@Param(value = PROVIDER, required = true) String provider,
                                       @Param(value = ENDPOINT) String endpoint,
                                       @Param(value = IDENTITY) String identity,
                                       @Param(value = CREDENTIAL, encrypted = true) String credential,
                                       @Param(value = PROXY_HOST) String proxyHost,
                                       @Param(value = PROXY_PORT) String proxyPort,
                                       @Param(value = DEBUG_MODE) String debugMode,

                                       @Param(value = REGION) String region,
                                       @Param(value = INSTANCE_ID, required = true) String instanceId,
                                       @Param(value = INSTANCE_TYPE) String instanceType,

                                       @Param(value = OPERATION_TIMEOUT) String operationTimeout,
                                       @Param(value = POOLING_INTERVAL) String poolingInterval)
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
                .withInstanceId(instanceId)
                .withInstanceType(instanceType)
                .build();

        InstanceInputs instanceInputs = new InstanceInputs.Builder()
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