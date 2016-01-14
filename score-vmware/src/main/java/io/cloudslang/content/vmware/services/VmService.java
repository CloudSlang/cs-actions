package io.cloudslang.content.vmware.services;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.connection.helpers.WaitForValues;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.VmConfigSpecs;
import io.cloudslang.content.vmware.utils.FindObjects;
import io.cloudslang.content.vmware.utils.InputUtils;

import java.util.*;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class VmService {

    // Creates Virtual Machine
    public Map<String, String> createVirtualMachine(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        Map<String, String> results = new HashMap<>();
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference vmFolderMor = connectionResources.getVmFolderMor();
        ManagedObjectReference resourcePoolMor = connectionResources.getResourcePoolMor();
        ManagedObjectReference hostMor = connectionResources.getHostMor();
        VimPortType vimPort = connectionResources.getVimPortType();

        VmConfigSpecs vmConfigSpecs = new VmConfigSpecs();
        VirtualMachineConfigSpec vmConfigSpec = vmConfigSpecs.getVmConfigSpec(vmInputs, connectionResources);

        ManagedObjectReference taskMor = vimPort.createVMTask(vmFolderMor, vmConfigSpec, resourcePoolMor, hostMor);

        String successMessage = "Success: Created [" + vmInputs.getVirtualMachineName() + "] VM. The taskId is: " +
                taskMor.getValue();
        String failureMessage = "Failure: Creating [" + vmInputs.getVirtualMachineName() + "] VM";

        setTaskResults(results, connectionResources, taskMor, successMessage, failureMessage);
        connectionResources.getConnection().disconnect();

        return results;
    }

    // Deletes specified Virtual Machine
    public Map<String, String> deleteVirtualMachine(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        Map<String, String> results = new HashMap<>();
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

            String successMessage = "Success: The [" + vmInputs.getVirtualMachineName() +
                    "] VM was deleted. The taskId is: " + taskMor.getValue();
            String failureMessage = "Failure: The [" + vmInputs.getVirtualMachineName() +
                    "] VM could not be deleted.";

            setTaskResults(results, connectionResources, taskMor, successMessage, failureMessage);
        } else {
            setTaskNotFoundResults(results, vmInputs, Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    // Powers-On specified Virtual Machine
    public Map<String, String> powerOnVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        Map<String, String> results = new HashMap<>();
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

            String successMessage = "Success: The [" + vmInputs.getVirtualMachineName() +
                    "] VM was successfully powered on. The taskId is: " + taskMor.getValue();
            String failureMessage = "Failure: The [" + vmInputs.getVirtualMachineName() +
                    "] VM could not be powered on.";

            setTaskResults(results, connectionResources, taskMor, successMessage, failureMessage);
        } else {
            setTaskNotFoundResults(results, vmInputs, Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    // Powers-Off specified Virtual Machine
    public Map<String, String> powerOffVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        Map<String, String> results = new HashMap<>();
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

            String successMessage = "Success: The [" + vmInputs.getVirtualMachineName() +
                    "] VM was successfully powered off. The taskId is: " + taskMor.getValue();
            String failureMessage = "Failure: The [" + vmInputs.getVirtualMachineName() +
                    "] VM could not be powered off.";

            setTaskResults(results, connectionResources, taskMor, successMessage, failureMessage);
        } else {
            setTaskNotFoundResults(results, vmInputs, Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    // Gets all OS descriptors that are available
    public Map<String, String> getOsDescriptors(HttpInputs httpInputs, VmInputs vmInputs, String delimiter) throws Exception {
        Map<String, String> results = new HashMap<>();
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference environmentBrowserMor = (ManagedObjectReference) connectionResources.getGetMOREF()
                .entityProps(connectionResources.getComputeResourceMor(), new String[]{Constants.ENVIRONMENT_BROWSER})
                .get(Constants.ENVIRONMENT_BROWSER);

        VirtualMachineConfigOption configOptions = connectionResources.getVimPortType()
                .queryConfigOption(environmentBrowserMor, null, connectionResources.getHostMor());
        List<GuestOsDescriptor> guestOSDescriptors = configOptions.getGuestOSDescriptor();

        String osDescriptorIdsString = getResponseStringFromCollection(guestOSDescriptors, delimiter);

        results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
        results.put(Outputs.RETURN_RESULT, osDescriptorIdsString);

        connectionResources.getConnection().disconnect();

        return results;
    }

    // Gets all existing VMs or templates within specified datacenter
    public Map<String, String> listVirtualMachinesAndTemplates(HttpInputs httpInputs,
                                                               VmInputs vmInputs,
                                                               String delimiter) throws Exception {
        Map<String, String> results = new HashMap<>();
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        Set<String> virtualMachineNamesList = connectionResources.getGetMOREF()
                .inContainerByType(connectionResources.getMorRootFolder(), Constants.VIRTUAL_MACHINE).keySet();

        if (virtualMachineNamesList.size() > 0) {
            String virtualMachineNamesString = getResponseStringFromCollection(virtualMachineNamesList, delimiter);

            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
            results.put(Outputs.RETURN_RESULT, virtualMachineNamesString);
        } else {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            results.put(Outputs.RETURN_RESULT, "No VM found in: [" + vmInputs.getDataCenterName() + "] datacenter.");
        }
        connectionResources.getConnection().disconnect();

        return results;
    }


    private void setTaskResults(Map<String, String> results,
                                ConnectionResources connectionResources,
                                ManagedObjectReference taskMor,
                                String successMessage,
                                String failureMessage)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {

        if (getTaskResultAfterDone(connectionResources, taskMor)) {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_SUCCESS);
            results.put(Outputs.RETURN_RESULT, successMessage);
        } else {
            results.put(Outputs.RETURN_CODE, Outputs.RETURN_CODE_FAILURE);
            results.put(Outputs.RETURN_RESULT, failureMessage);
        }
    }

    private void setTaskNotFoundResults(Map<String, String> results, VmInputs vmInputs, String returnCodeFailure) {
        results.put(Outputs.RETURN_CODE, returnCodeFailure);
        results.put(Outputs.RETURN_RESULT, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.");
    }

    private <T> String getResponseStringFromCollection(Collection<T> collectionItems, String delimiter) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (T item : collectionItems) {
            String itemName = item instanceof GuestOsDescriptor ? ((GuestOsDescriptor) item).getId() : (String) item;
            sb.append(itemName);
            if (index < collectionItems.size() - 1) {
                sb.append(InputUtils.getDefaultDelimiter(delimiter, Constants.COMMA_DELIMITER));
            }
            index++;
        }

        return sb.toString();
    }

    protected boolean getTaskResultAfterDone(ConnectionResources connectionResources, ManagedObjectReference task)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {

        boolean retVal = Boolean.FALSE;

        WaitForValues wfv = new WaitForValues(connectionResources.getConnection());
        Object[] result = wfv.wait(task,
                new String[]{Constants.INFO_STATE, Constants.INFO_ERROR},
                new String[]{Constants.STATE},
                new Object[][]{new Object[]{TaskInfoState.SUCCESS, TaskInfoState.ERROR}});

        if (result[1] instanceof LocalizedMethodFault) {
            throw new RuntimeException(((LocalizedMethodFault) result[1]).getLocalizedMessage());
        }
        if (result[0].equals(TaskInfoState.SUCCESS)) {
            retVal = Boolean.TRUE;
        }

        return retVal;
    }
}