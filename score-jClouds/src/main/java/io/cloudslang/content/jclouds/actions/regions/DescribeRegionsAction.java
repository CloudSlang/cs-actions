package io.cloudslang.content.jclouds.actions.regions;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.jclouds.entities.constants.Inputs;
import io.cloudslang.content.jclouds.entities.constants.Outputs;
import io.cloudslang.content.jclouds.entities.inputs.CommonInputs;
import io.cloudslang.content.jclouds.execute.regions.DescribeRegionsExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by persdana on 6/23/2015.
 */
public class DescribeRegionsAction {
    /**
     * Lists the regions from a cloud endpoint.
     *
     * @param provider         The cloud provider on which you have the instance. Valid values: "amazon" or "openstack".
     * @param identityEndpoint The endpoint to which first request will be sent. Example: "https://ec2.amazonaws.com" for amazon or "http://hostOrIp:5000/v2.0" for openstack.
     * @param identity         The username of your account or the Access Key ID. For openstack provider the required format is 'alias:username'.
     * @param credential       The password of the user or the Secret Access Key that correspond to the identity input.
     * @param proxyHost        The proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort        The proxy server port.
     * @param delimiter        A delimiter separating the region elements.
     * @return
     */
    @Action(name = "Describe Regions",
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
                                       @Param(Inputs.CommonInputs.DELIMITER) String delimiter) throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(identityEndpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDelimiter(delimiter)
                .build();

        try {
            return new DescribeRegionsExecutor().execute(inputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}