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
 * 3/3/2016.
 */
public class CloneVM {
    @Action(name = "Clone Virtual Machine",
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
    public Map<String, String> cloneVM(@Param(value = Inputs.HOST, required = true) String host,
                                       @Param(Inputs.PORT) String port,
                                       @Param(Inputs.PROTOCOL) String protocol,
                                       @Param(value = Inputs.USERNAME, required = true) String username,
                                       @Param(value = Inputs.PASSWORD, encrypted = true) String password,
                                       @Param(Inputs.TRUST_EVERYONE) String trustEveryone,

                                       @Param(value = Inputs.VM_NAME, required = true) String virtualMachineName,
                                       @Param(value = Inputs.CLONE_NAME, required = true) String cloneName,
                                       @Param(Inputs.FOLDER_NAME) String folderName,
                                       @Param(Inputs.CLONE_HOST) String cloneHost,
                                       @Param(Inputs.CLONE_RESOURCE_POOL) String cloneResourcePool,
                                       @Param(Inputs.CLONE_DATA_STORE) String cloneDataStore,
                                       @Param(Inputs.THIN_PROVISION) String thickProvision,
                                       @Param(Inputs.IS_TEMPLATE) String isTemplate,
                                       @Param(Inputs.CPU_NUM) String cpuNum,
                                       @Param(Inputs.CORES_PER_SOCKET) String coresPerSocket,
                                       @Param(Inputs.MEMORY) String memory,
                                       @Param(Inputs.CLONE_DESCRIPTION) String cloneDescription) {

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
                    .withCloneName(cloneName)
                    .withFolderName(folderName)
                    .withCloneHost(cloneHost)
                    .withCloneResourcePool(cloneResourcePool)
                    .withCloneDataStore(cloneDataStore)
                    .withThickProvision(thickProvision)
                    .withTemplate(isTemplate)
                    .withIntNumCPUs(cpuNum)
                    .withCoresPerSocket(coresPerSocket)
                    .withLongVmMemorySize(memory)
                    .withDescription(cloneDescription)
                    .build();

            resultMap = new VmService().cloneVM(httpInputs, vmInputs);

        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ex.toString());
        }

        return resultMap;
    }
}