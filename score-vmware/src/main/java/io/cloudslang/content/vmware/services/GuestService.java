package io.cloudslang.content.vmware.services;

import com.vmware.vim25.CustomizationSpec;
import com.vmware.vim25.ManagedObjectReference;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.VmParameter;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.MorHandler;
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

        MorHandler morHandler = new MorHandler();
        ManagedObjectReference vmMor = morHandler.getSpecificMorObject(connectionResources,
                VmParameter.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());

        Map<String, String> results = new HashMap<>();
        if (vmMor != null) {
            GuestConfigSpecs configHelper = new GuestConfigSpecs();
            CustomizationSpec customizationSpec = configHelper.getCustomizationSpec(guestInputs);

            connectionResources.getVimPortType().checkCustomizationSpec(vmMor, customizationSpec);

            ManagedObjectReference taskMor = connectionResources.getVimPortType().customizeVMTask(vmMor, customizationSpec);

            ResponseHelper responseHelper = new ResponseHelper();
            responseHelper.setTaskResults(results, connectionResources, taskMor, "Success: The [" +
                    vmInputs.getVirtualMachineName() + "] VM was successfully customized. The taskId is: " +
                    taskMor.getValue(), "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be customized.");
        } else {
            ResponseUtils.setResults(results, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.",
                    Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }
}