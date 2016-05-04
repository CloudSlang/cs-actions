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
import io.cloudslang.content.jclouds.execute.instances.UpdateServerTypeExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 2/24/2016.
 */
public class UpdateServerTypeAction {
    /**
     * Updates (changes) instance (identified by "serverId") type with the new one specified by "serverType".
     *
     * @param provider         The cloud provider on which you have the instance. Valid values: "amazon" or "openstack".
     * @param identityEndpoint The endpoint to which first request will be sent. Example: "https://ec2.amazonaws.com"
     *                         for amazon or "http://hostOrIp:5000/v2.0" for openstack.
     * @param identity         The username of your account or the Access Key ID. For openstack provider the required
     *                         format is 'alias:username'.
     * @param credential       The password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost        The proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort        The proxy server port.
     * @param region           Optional - the region where the server (instance) to be started can be found.
     *                         listRegionsAction can be used in order to get all regions - Default: 'us-east-1'
     * @param serverId         The ID of the server (instance) you want to update
     * @param serverType       The new server type to be used when updating the instance. The complete list of instance
     *                         types can be found at: http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/instance-types.html
     *                         - Example: 't2.medium', 'm3.large' - Default: 't2.micro'
     * @param operationTimeout Optional - the total time (in milliseconds) that operation will wait to complete the execution
     * @param poolingInterval  Optional - the time (in milliseconds) that operation will wait until next check of the instance
     *                         state
     * @return
     */
    @Action(name = "Update Server Type",
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
                                       @Param(value = Inputs.CommonInputs.IDENTITY) String identity,
                                       @Param(value = Inputs.CommonInputs.CREDENTIAL, encrypted = true) String credential,
                                       @Param(Inputs.CommonInputs.PROXY_HOST) String proxyHost,
                                       @Param(Inputs.CommonInputs.PROXY_PORT) String proxyPort,

                                       @Param(Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.SERVER_ID, required = true) String serverId,
                                       @Param(Inputs.CustomInputs.SERVER_TYPE) String serverType,
                                       @Param(Inputs.CustomInputs.OPERATION_TIMEOUT) String operationTimeout,
                                       @Param(Inputs.CustomInputs.POOLING_INTERVAL) String poolingInterval) throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(identityEndpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .withServerId(serverId)
                .withInstanceType(serverType)
                .withCheckStateTimeout(operationTimeout)
                .withPolingInterval(poolingInterval)
                .build();

        try {
            return new UpdateServerTypeExecutor().execute(inputs, customInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}