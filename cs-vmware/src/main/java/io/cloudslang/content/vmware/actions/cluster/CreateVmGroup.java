package io.cloudslang.content.vmware.actions.cluster;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.utils.CollectionUtilities;
import io.cloudslang.content.utils.OutputUtilities;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.ClusterComputeResourceService;
import io.cloudslang.content.vmware.utils.InputUtils;

import java.util.Map;

import static io.cloudslang.content.constants.InputNames.DELIMITER;
import static io.cloudslang.content.constants.OtherValues.COMMA_DELIMITER;
import static io.cloudslang.content.vmware.constants.Inputs.CLUSTER_NAME;
import static io.cloudslang.content.vmware.constants.Inputs.HOST;
import static io.cloudslang.content.vmware.constants.Inputs.PASSWORD;
import static io.cloudslang.content.vmware.constants.Inputs.PORT;
import static io.cloudslang.content.vmware.constants.Inputs.PROTOCOL;
import static io.cloudslang.content.vmware.constants.Inputs.TRUST_EVERYONE;
import static io.cloudslang.content.vmware.constants.Inputs.USERNAME;
import static io.cloudslang.content.vmware.constants.Inputs.VM_GROUP_NAME;
import static io.cloudslang.content.vmware.constants.Inputs.VM_LIST;

/**
 * Created by pinteae on 9/27/2016.
 */
public class CreateVmGroup {

    @Action(name = "Create DRS VM Group",
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
    public Map<String, String> createVmGroup(@Param(value = HOST, required = true) String host,
                                             @Param(value = PORT) String port,
                                             @Param(value = PROTOCOL) String protocol,
                                             @Param(value = USERNAME, required = true) String username,
                                             @Param(value = PASSWORD, encrypted = true) String password,
                                             @Param(value = TRUST_EVERYONE) String trustEveryone,
                                             @Param(value = VM_GROUP_NAME, required = true) String vmGroupName,
                                             @Param(value = CLUSTER_NAME, required = true) String clusterName,
                                             @Param(value = VM_LIST, required = true) String vmList,
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
            VmInputs vmInputs = new VmInputs.VmInputsBuilder()
                    .withClusterName(clusterName)
                    .withVmGroupName(vmGroupName)
                    .build();

            return new ClusterComputeResourceService()
                    .createVmGroup(httpInputs, vmInputs, CollectionUtilities.toList(vmList, InputUtils.getDefaultDelimiter(delimiter, COMMA_DELIMITER)));
        } catch (Exception ex) {
            return OutputUtilities.getFailureResultsMap(ex);
        }
    }
}
