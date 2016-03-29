package io.cloudslang.content.vmware.services;

import com.vmware.vim25.CustomizationSpec;
import com.vmware.vim25.ManagedObjectReference;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.VmParameter;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;
import io.cloudslang.content.vmware.services.utils.GuestConfigSpecs;
import io.cloudslang.content.vmware.utils.ResponseUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 3/21/2016.
 */
public class GuestService {
    public Map<String, String> customizeWinVM(HttpInputs httpInputs, VmInputs vmInputs, GuestInputs guestInputs)
            throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference vmMor = new MorObjectHandler().getVmMor(connectionResources,
                VmParameter.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());

        Map<String, String> results;
        if (vmMor != null) {
            CustomizationSpec customizationSpec = new GuestConfigSpecs().getCustomizationSpec(guestInputs);

            connectionResources.getVimPortType().checkCustomizationSpec(vmMor, customizationSpec);

            ManagedObjectReference task = connectionResources.getVimPortType().customizeVMTask(vmMor, customizationSpec);

            results = new ResponseHelper().getResultsMap(connectionResources, task,
                    "Success: The [" + vmInputs.getVirtualMachineName() + "] VM was successfully customized. The taskId is: " +
                            task.getValue(), "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be customized.");
        } else {
            results = new HashMap<>();
            ResponseUtils.setResults(results, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.",
                    Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }
}