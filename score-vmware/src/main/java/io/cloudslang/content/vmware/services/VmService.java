package io.cloudslang.content.vmware.services;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.helpers.WaitForValues;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.VmConfigSpecs;
import io.cloudslang.content.vmware.utils.FindObjects;
import io.cloudslang.content.vmware.connection.ConnectionResources;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class VmService {

    private Map<String, String> results = new HashMap<>();

    public Map<String, String> createVirtualMachine(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference vmFolderMor = connectionResources.getVmFolderMor();
        ManagedObjectReference resourcePoolMor = connectionResources.getResourcePoolMor();
        ManagedObjectReference hostMor = connectionResources.getHostMor();
        VimPortType vimPort = connectionResources.getVimPortType();

        VmConfigSpecs vmConfigSpecs = new VmConfigSpecs();
        VirtualMachineConfigSpec vmConfigSpec = vmConfigSpecs.getVmConfigSpec(vmInputs, connectionResources);

        ManagedObjectReference taskMor = vimPort.createVMTask(vmFolderMor, vmConfigSpec, resourcePoolMor, hostMor);

        if (getTaskResultAfterDone(connectionResources, taskMor)) {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
            results.put(Outputs.RETURN_RESULT, "Success: Created [" + vmInputs.getVirtualMachineName()
                    + "] VM. The taskId is: " + taskMor.getValue());

        } else {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            results.put(Outputs.RETURN_RESULT, "Failure: Creating [" + vmInputs.getVirtualMachineName() + "] VM");
        }

        connectionResources.getConnection().disconnect();

        return results;
    }

    public Map<String, String> deleteVirtualMachine(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        VimPortType vimPort = connectionResources.getVimPortType();
        ServiceContent serviceContent = vimPort.retrieveServiceContent(serviceInstance);

        ManagedObjectReference vmMor = FindObjects.findObject(vimPort,
                serviceContent,
                Constants.VIRTUAL_MACHINE,
                vmInputs.getVirtualMachineName());

        if (vmMor != null) {
            ManagedObjectReference taskMor = vimPort.destroyTask(vmMor);

            if (getTaskResultAfterDone(connectionResources, taskMor)) {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
                results.put(Outputs.RETURN_RESULT, "Success: The [" + vmInputs.getVirtualMachineName()
                        + "] VM was deleted. The taskId is: " + taskMor.getValue());
            } else {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
                results.put(Outputs.RETURN_RESULT, "Failure: The [" + vmInputs.getVirtualMachineName()
                        + "] VM could not be deleted.");
            }

        } else {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            results.put(Outputs.RETURN_RESULT, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.");
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    public Map<String, String> powerOnVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        VimPortType vimPort = connectionResources.getVimPortType();
        ServiceContent serviceContent = vimPort.retrieveServiceContent(serviceInstance);

        ManagedObjectReference vmMor = FindObjects.findObject(vimPort,
                serviceContent,
                Constants.VIRTUAL_MACHINE,
                vmInputs.getVirtualMachineName());

        if (vmMor != null) {
            ManagedObjectReference taskMor = vimPort.powerOnVMTask(vmMor, null);

            if (getTaskResultAfterDone(connectionResources, taskMor)) {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
                results.put(Outputs.RETURN_RESULT, "Success: The [" + vmInputs.getVirtualMachineName()
                        + "] VM was successfully powered on. The taskId is: " + taskMor.getValue());
            } else {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
                results.put(Outputs.RETURN_RESULT, "Failure: The [" + vmInputs.getVirtualMachineName()
                        + "] VM could not be powered on.");
            }
        } else {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            results.put(Outputs.RETURN_RESULT, "Could not find the [ " + vmInputs.getVirtualMachineName() + "] VM.");
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    public Map<String, String> powerOffVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        VimPortType vimPort = connectionResources.getVimPortType();
        ServiceContent serviceContent = vimPort.retrieveServiceContent(serviceInstance);

        ManagedObjectReference vmMor = FindObjects.findObject(vimPort,
                serviceContent,
                Constants.VIRTUAL_MACHINE,
                vmInputs.getVirtualMachineName());

        if (vmMor != null) {
            ManagedObjectReference taskMor = vimPort.powerOffVMTask(vmMor);

            if (getTaskResultAfterDone(connectionResources, taskMor)) {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
                results.put(Outputs.RETURN_RESULT, "Success: The [" + vmInputs.getVirtualMachineName()
                        + "] VM was successfully powered off. The taskId is: " + taskMor.getValue());
            } else {
                results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
                results.put(Outputs.RETURN_RESULT, "Failure: The [" + vmInputs.getVirtualMachineName()
                        + "] VM could not be powered off.");
            }
        } else {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            results.put(Outputs.RETURN_RESULT, "Could not find the [ " + vmInputs.getVirtualMachineName() + "] VM.");
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    private boolean getTaskResultAfterDone(ConnectionResources connectionResources, ManagedObjectReference task)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {

        boolean retVal = Boolean.FALSE;

        WaitForValues wfv = new WaitForValues(connectionResources.getConnection());
        Object[] result = wfv.wait(task,
                new String[]{Constants.INFO_STATE, Constants.INFO_ERROR},
                new String[]{Constants.STATE},
                new Object[][]{new Object[]{TaskInfoState.SUCCESS, TaskInfoState.ERROR}});

        if (result[0].equals(TaskInfoState.SUCCESS)) {
            retVal = Boolean.TRUE;
        }
        if (result[1] instanceof LocalizedMethodFault) {
            throw new RuntimeException(((LocalizedMethodFault) result[1]).getLocalizedMessage());
        }
        return retVal;
    }
}