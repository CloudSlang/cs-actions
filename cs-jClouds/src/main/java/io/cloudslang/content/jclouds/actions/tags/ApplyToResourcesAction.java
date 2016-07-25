package io.cloudslang.content.jclouds.actions.tags;

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
import io.cloudslang.content.jclouds.execute.tags.ApplyToResourcesExecutor;
import io.cloudslang.content.jclouds.utils.ExceptionProcessor;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 7/21/2016.
 */
public class ApplyToResourcesAction {
    /**
     * Adds or overwrites one or more tags for the specified Amazon EC2 resource/resources.
     * Note: Each resource can have a maximum of 10 tags. Each tag consists of a key and optional value. Tag keys must be
     * unique per resource. For more information about tags, see Tagging Your Resources in the Amazon Elastic Compute Cloud
     * User Guide. For more information about creating IAM policies that control users' access to resources based on tags,
     * see Supported Resource-Level Permissions for Amazon EC2 API Actions in the Amazon Elastic Compute Cloud User Guide.
     *
     * @param provider          Cloud provider on which you have the resources - Valid values: "amazon" or "openstack".
     * @param endpoint          Endpoint to which first request will be sent. Ex: "https://ec2.amazonaws.com" for amazon or
     *                          "http://hostOrIp:5000/v2.0" for OpenStack.
     * @param identity          Optional - Username of your account or the Access Key ID. For OpenStack provider the required
     *                          format is 'alias:username'.
     * @param credential        Optional - Password of the user or the Secret Access Key that correspond to the identity
     *                          input.
     * @param proxyHost         Optional - Proxy server used to access the web site. If empty no proxy will be used.
     * @param proxyPort         Optional - Proxy server port.
     * @param debugMode         Optional - If "true" then the execution logs will be shown in CLI console.
     * @param delimiter         Optional - Delimiter that will be used - Default: ","
     * @param region            Optional - region where resources to tagged belongs. Ex: "RegionOne", "us-east-1".
     *                          ListRegionAction can be used in order to get all regions - Default: "us-east-1"
     * @param keyTagsString     String that contains one or more key tags separated by delimiter.
     * @param valueTagsString   String that contains one or more tag values separated by delimiter.
     * @param resourceIdsString String that contains Id's of one or more resources to tag.
     *                          Ex: "ami-1a2b3c4d"
     * @return A map with strings as keys and strings as values that contains: outcome of the action, returnCode of the
     * operation, or failure message and the exception if there is one
     */
    @Action(name = "Apply to Resources",
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
                                       @Param(Inputs.CommonInputs.DELIMITER) String delimiter,
                                       @Param(Inputs.CommonInputs.DEBUG_MODE) String debugMode,

                                       @Param(Inputs.CustomInputs.REGION) String region,
                                       @Param(value = Inputs.CustomInputs.KEY_TAGS_STRING, required = true) String keyTagsString,
                                       @Param(value = Inputs.CustomInputs.VALUE_TAGS_STRING, required = true) String valueTagsString,
                                       @Param(value = Inputs.CustomInputs.RESOURCE_IDS_STRING, required = true) String resourceIdsString)
            throws Exception {

        CommonInputs inputs = new CommonInputs.CommonInputsBuilder()
                .withProvider(provider)
                .withEndpoint(endpoint)
                .withIdentity(identity)
                .withCredential(credential)
                .withProxyHost(proxyHost)
                .withProxyPort(proxyPort)
                .withDelimiter(delimiter)
                .withDebugMode(debugMode)
                .build();

        CustomInputs customInputs = new CustomInputs.CustomInputsBuilder()
                .withRegion(region)
                .withKeyTagsString(keyTagsString)
                .withValueTagsString(valueTagsString)
                .withResourceIdsString(resourceIdsString)
                .build();

        try {
            return new ApplyToResourcesExecutor().execute(inputs, customInputs);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }
    }
}