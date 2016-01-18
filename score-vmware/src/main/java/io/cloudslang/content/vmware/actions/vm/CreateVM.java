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
 * 10/18/2015.
 */
public class CreateVM {

    @Action(name = "Create Virtual Machine",
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
    public Map<String, String> createVM(@Param(value = Inputs.HOST, required = true) String host,
                                        @Param(Inputs.PORT) String port,
                                        @Param(Inputs.PROTOCOL) String protocol,
                                        @Param(Inputs.USERNAME) String username,
                                        @Param(value = Inputs.PASSWORD, encrypted = true) String password,
                                        @Param(Inputs.TRUST_EVERYONE) String trustEveryone,

                                        @Param(value = Inputs.DATA_CENTER_NAME, required = true) String dataCenterName,
                                        @Param(value = Inputs.HOSTNAME, required = true) String hostname,
                                        @Param(value = Inputs.VM_NAME, required = true) String virtualMachineName,
                                        @Param(Inputs.VM_DESCRIPTION) String description,
                                        @Param(value = Inputs.DATA_STORE, required = true) String dataStore,
                                        @Param(Inputs.VM_CPU_COUNT) String numCPUs,
                                        @Param(Inputs.VM_DISK_SIZE) String vmDiskSize,
                                        @Param(Inputs.VM_MEMORY_SIZE) String vmMemorySize,
                                        @Param(value = Inputs.GUEST_OS_ID, required = true) String guestOsId) {

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
                    .withDataCenterName(dataCenterName)
                    .withHostname(hostname)
                    .withVirtualMachineName(virtualMachineName)
                    .withDescription(description)
                    .withDataStore(dataStore)
                    .withIntNumCPUs(numCPUs)
                    .withLongVmDiskSize(vmDiskSize)
                    .withLongVmMemorySize(vmMemorySize)
                    .withGuestOsId(guestOsId)
                    .build();

            resultMap = new VmService().createVirtualMachine(httpInputs, vmInputs);

        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ex.toString());
        }

        return resultMap;
    }
}