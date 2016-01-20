package io.cloudslang.content.vmware.services;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.connection.helpers.WaitForValues;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.VmConfigSpecs;
import io.cloudslang.content.vmware.services.helpers.VmUtils;
import io.cloudslang.content.vmware.utils.FindObjects;
import io.cloudslang.content.vmware.utils.GetObjectProperties;
import io.cloudslang.content.vmware.utils.InputUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class VmService {

    /**
     * Method used to connect to a specified data center and creates a virtual machine using the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to create a new virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
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

        setTaskResults(results, connectionResources, taskMor, "Success: Created [" + vmInputs.getVirtualMachineName() +
                "] VM. The taskId is: " + taskMor.getValue(), "Failure: Creating [" + vmInputs.getVirtualMachineName() +
                "] VM");
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to a specified data center and deletes the virtual machine identified by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> deleteVirtualMachine(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        Map<String, String> results = new HashMap<>();
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ServiceContent serviceContent = getServiceContent(connectionResources);

        ManagedObjectReference vmMor = FindObjects.findObject(connectionResources.getVimPortType(),
                serviceContent, Constants.VIRTUAL_MACHINE, vmInputs.getVirtualMachineName());

        if (vmMor != null) {
            ManagedObjectReference taskMor = connectionResources.getVimPortType().destroyTask(vmMor);

            setTaskResults(results, connectionResources, taskMor, "Success: The [" + vmInputs.getVirtualMachineName() +
                    "] VM was deleted. The taskId is: " + taskMor.getValue(), "Failure: The [" +
                    vmInputs.getVirtualMachineName() + "] VM could not be deleted.");
        } else {
            setResults(results, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.", Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to a specified data center and powers-on the virtual machine identified by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> powerOnVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        Map<String, String> results = new HashMap<>();
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ServiceContent serviceContent = getServiceContent(connectionResources);

        ManagedObjectReference vmMor = FindObjects.findObject(connectionResources.getVimPortType(),
                serviceContent, Constants.VIRTUAL_MACHINE, vmInputs.getVirtualMachineName());

        if (vmMor != null) {
            ManagedObjectReference taskMor = connectionResources.getVimPortType().powerOnVMTask(vmMor, null);

            setTaskResults(results, connectionResources, taskMor, "Success: The [" + vmInputs.getVirtualMachineName() +
                    "] VM was successfully powered on. The taskId is: " + taskMor.getValue(), "Failure: The [" +
                    vmInputs.getVirtualMachineName() + "] VM could not be powered on.");
        } else {
            setResults(results, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.", Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to a specified data center and powers-off the virtual machine identified by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> powerOffVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        Map<String, String> results = new HashMap<>();
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ServiceContent serviceContent = getServiceContent(connectionResources);

        ManagedObjectReference vmMor = FindObjects.findObject(connectionResources.getVimPortType(),
                serviceContent, Constants.VIRTUAL_MACHINE, vmInputs.getVirtualMachineName());

        if (vmMor != null) {
            ManagedObjectReference taskMor = connectionResources.getVimPortType().powerOffVMTask(vmMor);

            setTaskResults(results, connectionResources, taskMor, "Success: The [" + vmInputs.getVirtualMachineName() +
                            "] VM was successfully powered off. The taskId is: " + taskMor.getValue(),
                    "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be powered off.");
        } else {
            setResults(results, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.", Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to a data center to retrieve a list with all the guest operating system descriptors
     * supported by the host system.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted host system
     * @param delimiter  String that represents the delimiter needed in the result list
     * @return Map with String as key and value that contains returnCode of the operation, a list that contains all the
     * guest operating system descriptors supported by the host system or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> getOsDescriptors(HttpInputs httpInputs, VmInputs vmInputs, String delimiter) throws Exception {

        Map<String, String> results = new HashMap<>();
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference environmentBrowserMor = (ManagedObjectReference) connectionResources.getGetMOREF()
                .entityProps(connectionResources.getComputeResourceMor(), new String[]{Constants.ENVIRONMENT_BROWSER})
                .get(Constants.ENVIRONMENT_BROWSER);

        VirtualMachineConfigOption configOptions = connectionResources.getVimPortType()
                .queryConfigOption(environmentBrowserMor, null, connectionResources.getHostMor());

        List<GuestOsDescriptor> guestOSDescriptors = configOptions.getGuestOSDescriptor();

        setResults(results, VmUtils.getResponseStringFromCollection(guestOSDescriptors, delimiter), Outputs.RETURN_CODE_SUCCESS);
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to a data center to retrieve a list with all the virtual machines and templates within.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted data center
     * @param delimiter  String that represents the delimiter needed in the result list
     * @return Map with String as key and value that contains returnCode of the operation, a list that contains all the
     * virtual machines and templates within the data center or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> listVirtualMachinesAndTemplates(HttpInputs httpInputs,
                                                               VmInputs vmInputs,
                                                               String delimiter) throws Exception {
        Map<String, String> results = new HashMap<>();
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        Set<String> virtualMachineNamesList = connectionResources.getGetMOREF()
                .inContainerByType(connectionResources.getMorRootFolder(), Constants.VIRTUAL_MACHINE).keySet();

        if (virtualMachineNamesList.size() > 0) {
            setResults(results, VmUtils.getResponseStringFromCollection(virtualMachineNamesList, delimiter),
                    Outputs.RETURN_CODE_SUCCESS);
        } else {
            setResults(results, "No VM found in datacenter.", Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to a data center to retrieve details of a specified virtual machine.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, a JSON formatted string that
     * contains details of the virtual machine or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> getVMDetails(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        Map<String, String> results = new HashMap<>();
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ServiceContent serviceContent = getServiceContent(connectionResources);

        ManagedObjectReference vmMor = FindObjects.findObject(connectionResources.getVimPortType(),
                serviceContent, Constants.VIRTUAL_MACHINE, vmInputs.getVirtualMachineName());

        ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, vmMor,
                new String[]{Constants.SUMMARY});

        if (objectContents != null) {
            Map<String, String> vmDetails = new HashMap<>();
            for (ObjectContent objectItem : objectContents) {
                List<DynamicProperty> vmProperties = objectItem.getPropSet();
                for (DynamicProperty propertyItem : vmProperties) {
                    VirtualMachineSummary virtualMachineSummary = (VirtualMachineSummary) propertyItem.getVal();
                    VirtualMachineConfigSummary virtualMachineConfigSummary = virtualMachineSummary.getConfig();

                    vmDetails = VmUtils.getVmDetailedMap(vmDetails, virtualMachineSummary, virtualMachineConfigSummary);
                }
            }

            setResults(results, InputUtils.getJsonString(vmDetails), Outputs.RETURN_CODE_SUCCESS);
        } else {
            setResults(results,
                    "Could not retrieve the details for: [" + vmInputs.getVirtualMachineName() + "] VM.",
                    Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    private ServiceContent getServiceContent(ConnectionResources connectionResources) throws RuntimeFaultFaultMsg {
        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        VimPortType vimPort = connectionResources.getVimPortType();

        return vimPort.retrieveServiceContent(serviceInstance);
    }

    private void setResults(Map<String, String> results, String returnResultMessage, String returnCodeMessage) {
        results.put(Outputs.RETURN_RESULT, returnResultMessage);
        results.put(Outputs.RETURN_CODE, returnCodeMessage);
    }

    private void setTaskResults(Map<String, String> results,
                                ConnectionResources connectionResources,
                                ManagedObjectReference taskMor,
                                String successMessage,
                                String failureMessage)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {

        if (getTaskResultAfterDone(connectionResources, taskMor)) {
            setResults(results, successMessage, Outputs.RETURN_CODE_SUCCESS);
        } else {
            setResults(results, failureMessage, Outputs.RETURN_CODE_FAILURE);
        }
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