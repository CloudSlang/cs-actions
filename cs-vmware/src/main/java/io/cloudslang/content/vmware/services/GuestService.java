package io.cloudslang.content.vmware.services;

import com.vmware.vim25.CustomizationSpec;
import com.vmware.vim25.ManagedObjectReference;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.ManagedObjectType;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;
import io.cloudslang.content.vmware.services.utils.GuestConfigSpecs;
import io.cloudslang.content.vmware.utils.ResponseUtils;

import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 3/21/2016.
 */
public class GuestService {
    private static final String INITIATED_TOOLS_INSTALLER_MOUNT = "Initiated VMware Tools Installer Mount on: ";

    /**
     * Method used to connect to specified data center and customize the windows OS based virtual machine identified
     * by the inputs provided.
     *
     * @param httpInputs  Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs    Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @param guestInputs Object that has all specific inputs necessary to customize specified virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> customizeVM(HttpInputs httpInputs, VmInputs vmInputs, GuestInputs guestInputs, boolean isWin)
            throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        try {
            ManagedObjectReference vmMor = new MorObjectHandler()
                    .getVmMor(connectionResources, ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                CustomizationSpec customizationSpec = isWin ? new GuestConfigSpecs().getWinCustomizationSpec(guestInputs)
                        : new GuestConfigSpecs().getLinuxCustomizationSpec(guestInputs);

                connectionResources.getVimPortType().checkCustomizationSpec(vmMor, customizationSpec);
                ManagedObjectReference task = connectionResources.getVimPortType().customizeVMTask(vmMor, customizationSpec);

                return new ResponseHelper(connectionResources, task).getResultsMap("Success: The [" +
                        vmInputs.getVirtualMachineName() + "] VM was successfully customized. The taskId is: " +
                        task.getValue(), "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be customized.");
            } else {
                return ResponseUtils.getVmNotFoundResultsMap(vmInputs);
            }
        } catch (Exception ex) {
            return ResponseUtils.getResultsMap(ex.toString(), Outputs.RETURN_CODE_FAILURE);
        } finally {
            connectionResources.getConnection().disconnect();
        }
    }

    /**
     * Method used to connect to specified data center and start the Install Tools process on virtual machine identified
     * by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message or failure
     * message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> mountTools(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        try {
            ManagedObjectReference vmMor = new MorObjectHandler()
                    .getVmMor(connectionResources, ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                connectionResources.getVimPortType().mountToolsInstaller(vmMor);
                return ResponseUtils.getResultsMap(INITIATED_TOOLS_INSTALLER_MOUNT + vmInputs.getVirtualMachineName(),
                        Outputs.RETURN_CODE_SUCCESS);
            } else {
                return ResponseUtils.getVmNotFoundResultsMap(vmInputs);
            }
        } catch (Exception ex) {
            return ResponseUtils.getResultsMap(ex.toString(), Outputs.RETURN_CODE_FAILURE);
        } finally {
            connectionResources.getConnection().disconnect();
        }
    }
}
