package io.cloudslang.content.vmware.actions.vm;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import io.cloudslang.content.vmware.constants.Inputs;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.VmService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 1/7/2016.
 */
public class PowerOnVM {
    @Action(name = "Power On Virtual Machine",
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
    public Map<String, String> powerOnVM(@Param(value = Inputs.HOST, required = true) String host,
                                         @Param(Inputs.PORT) String port,
                                         @Param(Inputs.PROTOCOL) String protocol,
                                         @Param(Inputs.USERNAME) String username,
                                         @Param(value = Inputs.PASSWORD, encrypted = true) String password,
                                         @Param(Inputs.TRUST_EVERYONE) String trustEveryone,

                                         @Param(value = Inputs.VIRTUAL_MACHINE_NAME, required = true) String virtualMachineName) {

        Map<String, String> resultMap = new HashMap<>();

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
                    .withVirtualMachineName(virtualMachineName)
                    .build();

            resultMap = new VmService().powerOnVM(httpInputs, vmInputs);

        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ex.toString());
        }

        return resultMap;
    }
}
