package io.cloudslang.content.actions.vm;

import com.hp.oo.sdk.content.annotations.Action;
import com.hp.oo.sdk.content.annotations.Output;
import com.hp.oo.sdk.content.annotations.Param;
import com.hp.oo.sdk.content.annotations.Response;
import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import com.hp.oo.sdk.content.plugin.ActionMetadata.ResponseType;
import com.vmware.vim25.ManagedObjectReference;
import io.cloudslang.content.constants.Constants;
import io.cloudslang.content.constants.Inputs;
import io.cloudslang.content.constants.Outputs;
import io.cloudslang.content.services.VmService;
import io.cloudslang.content.utils.InputUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 10/18/2015.
 */
public class CreateVM {

    private VmService service = new VmService();

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
                                        @Param(Inputs.X509_HOSTNAME_VERIFIER) String x509HostnameVerifier,

                                        @Param(value = Inputs.DATA_CENTER_NAME, required = true) String dataCenterName,
                                        @Param(Inputs.HOSTNAME) String hostname,
                                        @Param(value = Inputs.VIRTUAL_MACHINE_NAME, required = true) String virtualMachineName,
                                        @Param(Inputs.VIRTUAL_MACHINE_DESCRIPTION) String description,
                                        @Param(value = Inputs.DATA_STORE, required = true) String dataStore,
                                        @Param(Constants.RESOURCE_POOL) String resourcePool,
                                        @Param(Inputs.CPU_COUNT) String numCPUs,
                                        @Param(Inputs.VM_DISK_SIZE) String vmDiskSize,
                                        @Param(Inputs.VM_MEMORY_SIZE) String vmMemorySize,
                                        @Param(value = Inputs.GUEST_OS_ID, required = true) String guestOsId) {

        Map<String, String> resultMap = new HashMap<>();

        int intPort = InputUtils.getIntInput(port, Inputs.DEFAULT_HTTPS_PORT);
        boolean booleanX509HostnameVerifier = InputUtils.getBooleanInput(x509HostnameVerifier);
        int intNumCPUs = InputUtils.getIntInput(numCPUs, Constants.DEFAULT_CPU_COUNT);
        long longVmDiskSize = InputUtils.getLongInput(vmDiskSize, Constants.DEFAULT_VM_DISK_SIZE_MB);
        long longVmMemorySize = InputUtils.getLongInput(vmMemorySize, Constants.DEFAULT_VM_MEMORY_SIZE_MB);

        try {

            ManagedObjectReference managedObjectReference = service.createVirtualMachine(host,
                    intPort,
                    protocol,
                    username,
                    password,
                    booleanX509HostnameVerifier,
                    dataCenterName,
                    hostname,
                    virtualMachineName,
                    description,
                    dataStore,
                    resourcePool,
                    intNumCPUs,
                    longVmDiskSize,
                    longVmMemorySize,
                    guestOsId);

            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
            resultMap.put(Outputs.RETURN_RESULT, managedObjectReference.getValue());
        } catch (Exception ex) {
            resultMap.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            resultMap.put(Outputs.RETURN_RESULT, ex.getMessage());
            resultMap.put(Outputs.EXCEPTION, ex.toString());
        }

        return resultMap;
    }
}