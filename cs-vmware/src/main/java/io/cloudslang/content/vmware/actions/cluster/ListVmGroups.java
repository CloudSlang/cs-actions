package io.cloudslang.content.vmware.actions.cluster;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import io.cloudslang.content.vmware.utils.InputUtils;

import java.util.Map;

import static io.cloudslang.content.constants.OtherValues.COMMA_DELIMITER;
import static io.cloudslang.content.vmware.constants.Inputs.CLUSTER_NAME;
import static io.cloudslang.content.vmware.constants.Inputs.DELIMITER;
import static io.cloudslang.content.vmware.constants.Inputs.HOST;
import static io.cloudslang.content.vmware.constants.Inputs.PASSWORD;
import static io.cloudslang.content.vmware.constants.Inputs.PORT;
import static io.cloudslang.content.vmware.constants.Inputs.PROTOCOL;
import static io.cloudslang.content.vmware.constants.Inputs.TRUST_EVERYONE;
import static io.cloudslang.content.vmware.constants.Inputs.USERNAME;

/**
 * Created by pinteae on 10/5/2016.
 */
public class ListVmGroups {

    @Action(name = "List DRS VM Groups",
            outputs = {
                    @Output(Outputs.RETURN_CODE),
                    @Output(Outputs.RETURN_RESULT),
                    @Output(Outputs.EXCEPTION)
            },
            responses = {
                    @Response(text = Outputs.SUCCESS, field = Outputs.RETURN_CODE, value = Outputs.RETURN_CODE_SUCCESS,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.RESOLVED),
                    @Response(text = Outputs.FAILURE, field = Outputs.RETURN_CODE, value = Outputs.RETURN_CODE_FAILURE,
                            matchType = MatchType.COMPARE_EQUAL, responseType = ResponseType.ERROR, isOnFail = true)
            })
    public Map<String, String> listVmGroups(@Param(value = HOST, required = true) String host,
                                            @Param(value = PORT) String port,
                                            @Param(value = PROTOCOL) String protocol,
                                            @Param(value = USERNAME, required = true) String username,
                                            @Param(value = PASSWORD, encrypted = true) String password,
                                            @Param(value = TRUST_EVERYONE) String trustEveryone,
                                            @Param(value = CLUSTER_NAME, required = true) String clusterName,
                                            @Param(value = DELIMITER) String delimiter) {
        try {
            HttpInputs httpInputs = new HttpInputs.HttpInputsBuilder()
                    .withHost(host)
                    .withPort(port)
                    .withProtocol(protocol)
                    .withUsername(username)
                    .withPassword(password)
                    .withTrustEveryone(trustEveryone)
                    .build();
            return OutputUtilities.getSuccessResultsMap(
                    new ClusterComputeResourceService().
                            listVmGroups(httpInputs, clusterName, InputUtils.getDefaultDelimiter(delimiter, COMMA_DELIMITER)));
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
