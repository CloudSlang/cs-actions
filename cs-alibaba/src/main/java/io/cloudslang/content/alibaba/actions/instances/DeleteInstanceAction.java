package io.cloudslang.content.alibaba.actions.instances;

import com.aliyuncs.IAcsClient;
import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.alibaba.entities.constants.Outputs;
import io.cloudslang.content.alibaba.services.InstanceService;
import io.cloudslang.content.alibaba.utils.ClientUtil;
import io.cloudslang.content.alibaba.utils.Validator;

import java.util.Map;

import static io.cloudslang.content.alibaba.entities.constants.CommonInputs.*;
import static io.cloudslang.content.alibaba.utils.Descriptions.Common.*;
import static io.cloudslang.content.alibaba.utils.Descriptions.DeleteInstance.*;
import static io.cloudslang.content.utils.OutputUtilities.getFailureResultsMap;
import static io.cloudslang.content.utils.OutputUtilities.getSuccessResultsMap;

public class DeleteInstanceAction {

    @Action(name = "Delete Instance",
            description = DELTETE_INSTANCE_DESC,
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
    public Map<String, String> execute(@Param(value = REGION_ID, required = true, description = REGION_ID_DESC) String regionId,
                                       @Param(value = ACCESS_KEY_ID, required = true, description = ACCESS_KEY_ID_DESC) String accessKeyId,
                                       @Param(value = ACCESS_KEY_SECRET, required = true, encrypted = true, description = ACCESS_KEY_SECRET_ID_DESC) String accessKeySecret,
                                       @Param(value = PROXY_HOST, description = PROXY_HOST_DESC) String proxyHost,
                                       @Param(value = PROXY_PORT, description = PROXY_PORT_DESC) String proxyPort,
                                       @Param(value = PROXY_USERNAME, description = PROXY_USERNAME_DESC) String proxyUsername,
                                       @Param(value = PROXY_PASSWORD, encrypted = true, description = PROXY_PASSWORD_DESC) String proxyPassword,
                                       @Param(value = INSTANCE_ID, required = true, description = INSTANCE_ID_DESC) String instanceId) {
        //Validate Inputs
        Validator validator = new Validator();
        validator.validatePort(proxyPort, PROXY_PORT);

        if (validator.hasErrors()) {
            return getFailureResultsMap(validator.getErrors());
        }

        try{
            final IAcsClient client = ClientUtil.getClient(regionId, accessKeyId, accessKeySecret);
            final String result = InstanceService.deleteInstance(proxyHost, proxyPort, proxyUsername, proxyPassword, instanceId, client);

            return getSuccessResultsMap(result);
        } catch (Exception e){
            return getFailureResultsMap("Failed to delete instance. ", e);
        }
    }
}
