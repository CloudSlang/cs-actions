package io.cloudslang.content.vmware.services;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.connection.helpers.WaitForValues;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.Device;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.VmParameter;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.FindObjects;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import io.cloudslang.content.vmware.services.utils.VmConfigSpecs;
import io.cloudslang.content.vmware.services.utils.VmUtils;
import io.cloudslang.content.vmware.utils.ResponseUtils;

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
     * Method used to connect to specified data center and create a virtual machine using the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to create a new virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> createVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        VmUtils utils = new VmUtils();

        ManagedObjectReference vmFolderMor = utils.getMorFolder(vmInputs.getFolderName(), connectionResources);
        ManagedObjectReference resourcePoolMor = utils.getMorResourcePool(vmInputs.getResourcePool(), connectionResources);
        ManagedObjectReference hostMor = utils.getMorHost(vmInputs.getHostname(), connectionResources, null);

        VmConfigSpecs vmConfigSpecs = new VmConfigSpecs();
        VirtualMachineConfigSpec vmConfigSpec = vmConfigSpecs.getVmConfigSpec(vmInputs, connectionResources);

        ManagedObjectReference taskMor = connectionResources.getVimPortType()
                .createVMTask(vmFolderMor, vmConfigSpec, resourcePoolMor, hostMor);

        Map<String, String> results = new HashMap<>();
        setTaskResults(results, connectionResources, taskMor, "Success: Created [" + vmInputs.getVirtualMachineName() +
                "] VM. The taskId is: " + taskMor.getValue(), "Failure: Creating [" + vmInputs.getVirtualMachineName() +
                "] VM");
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to specified data center and delete the virtual machine identified by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> deleteVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getSpecificMorObject(connectionResources, VmParameter.VIRTUAL_MACHINE.getValue(),
                vmInputs.getVirtualMachineName());

        Map<String, String> results = new HashMap<>();
        if (vmMor != null) {
            ManagedObjectReference taskMor = connectionResources.getVimPortType().destroyTask(vmMor);

            setTaskResults(results, connectionResources, taskMor, "Success: The [" + vmInputs.getVirtualMachineName() +
                    "] VM was deleted. The taskId is: " + taskMor.getValue(), "Failure: The [" +
                    vmInputs.getVirtualMachineName() + "] VM could not be deleted.");
        } else {
            ResponseUtils.setResults(results, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.",
                    Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to specified data center and power-on virtual machine identified by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> powerOnVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getSpecificMorObject(connectionResources, VmParameter.VIRTUAL_MACHINE.getValue(),
                vmInputs.getVirtualMachineName());

        Map<String, String> results = new HashMap<>();
        if (vmMor != null) {
            ManagedObjectReference taskMor = connectionResources.getVimPortType().powerOnVMTask(vmMor, null);

            setTaskResults(results, connectionResources, taskMor, "Success: The [" + vmInputs.getVirtualMachineName() +
                    "] VM was successfully powered on. The taskId is: " + taskMor.getValue(), "Failure: The [" +
                    vmInputs.getVirtualMachineName() + "] VM could not be powered on.");
        } else {
            ResponseUtils.setResults(results, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.",
                    Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to specified data center and power-off virtual machine identified by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> powerOffVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getSpecificMorObject(connectionResources, VmParameter.VIRTUAL_MACHINE.getValue(),
                vmInputs.getVirtualMachineName());

        Map<String, String> results = new HashMap<>();
        if (vmMor != null) {
            ManagedObjectReference taskMor = connectionResources.getVimPortType().powerOffVMTask(vmMor);

            setTaskResults(results, connectionResources, taskMor, "Success: The [" + vmInputs.getVirtualMachineName() +
                            "] VM was successfully powered off. The taskId is: " + taskMor.getValue(),
                    "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be powered off.");
        } else {
            ResponseUtils.setResults(results, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.",
                    Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to data center to retrieve a list with all the guest operating system descriptors
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
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference environmentBrowserMor = (ManagedObjectReference) connectionResources.getGetMOREF()
                .entityProps(connectionResources.getComputeResourceMor(), new String[]{VmParameter.ENVIRONMENT_BROWSER.getValue()})
                .get(VmParameter.ENVIRONMENT_BROWSER.getValue());

        VirtualMachineConfigOption configOptions = connectionResources.getVimPortType()
                .queryConfigOption(environmentBrowserMor, null, connectionResources.getHostMor());

        List<GuestOsDescriptor> guestOSDescriptors = configOptions.getGuestOSDescriptor();

        Map<String, String> results = new HashMap<>();
        ResponseUtils.setResults(results, ResponseUtils.getResponseStringFromCollection(guestOSDescriptors, delimiter),
                Outputs.RETURN_CODE_SUCCESS);
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to data center to retrieve a list with all the virtual machines and templates within.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted data center
     * @param delimiter  String that represents the delimiter needed in the result list
     * @return Map with String as key and value that contains returnCode of the operation, a list that contains all the
     * virtual machines and templates within the data center or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> listVMsAndTemplates(HttpInputs httpInputs, VmInputs vmInputs, String delimiter)
            throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        Set<String> virtualMachineNamesList = connectionResources.getGetMOREF()
                .inContainerByType(connectionResources.getMorRootFolder(), VmParameter.VIRTUAL_MACHINE.getValue())
                .keySet();

        Map<String, String> results = new HashMap<>();
        if (virtualMachineNamesList.size() > 0) {
            ResponseUtils.setResults(results,
                    ResponseUtils.getResponseStringFromCollection(virtualMachineNamesList, delimiter),
                    Outputs.RETURN_CODE_SUCCESS);
        } else {
            ResponseUtils.setResults(results, "No VM found in datacenter.", Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to data center to retrieve details of a virtual machine identified by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, a JSON formatted string that
     * contains details of the virtual machine or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> getVMDetails(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getSpecificMorObject(connectionResources, VmParameter.VIRTUAL_MACHINE.getValue(),
                vmInputs.getVirtualMachineName());

        ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, vmMor,
                new String[]{VmParameter.SUMMARY.getValue()});

        Map<String, String> results = new HashMap<>();
        if (objectContents != null) {
            Map<String, String> vmDetails = new HashMap<>();
            for (ObjectContent objectItem : objectContents) {
                List<DynamicProperty> vmProperties = objectItem.getPropSet();
                for (DynamicProperty propertyItem : vmProperties) {
                    VirtualMachineSummary virtualMachineSummary = (VirtualMachineSummary) propertyItem.getVal();
                    VirtualMachineConfigSummary virtualMachineConfigSummary = virtualMachineSummary.getConfig();

                    ResponseUtils.addDataToVmDetailsMap(vmDetails, virtualMachineSummary, virtualMachineConfigSummary);
                }
            }
            String responseJson = ResponseUtils.getJsonString(vmDetails);
            ResponseUtils.setResults(results, responseJson, Outputs.RETURN_CODE_SUCCESS);
        } else {
            ResponseUtils.setResults(results, "Could not retrieve the details for: [" +
                    vmInputs.getVirtualMachineName() + "] VM.", Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to data center to update existing devices of a virtual machine identified by the inputs
     * provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted device
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> updateVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getSpecificMorObject(connectionResources, VmParameter.VIRTUAL_MACHINE.getValue(),
                vmInputs.getVirtualMachineName());

        VmUtils utils = new VmUtils();
        Map<String, String> results = new HashMap<>();
        if (vmMor != null) {
            String device = Device.getValue(vmInputs.getDevice()).toLowerCase();
            VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
            if (Constants.CPU.equalsIgnoreCase(device) || Constants.MEMORY.equalsIgnoreCase(device)) {
                vmConfigSpec = utils.getUpdateConfigSpec(vmInputs, vmConfigSpec, device);
            } else {
                vmConfigSpec = utils.getAddOrRemoveSpecs(connectionResources, vmMor, vmInputs, vmConfigSpec, device);
            }

            ManagedObjectReference taskMor = connectionResources.getVimPortType().reconfigVMTask(vmMor, vmConfigSpec);

            setTaskResults(results, connectionResources, taskMor, "Success: The [" + vmInputs.getVirtualMachineName() +
                            "] VM was successfully reconfigured. The taskId is: " + taskMor.getValue(),
                    "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be reconfigured.");
        } else {
            ResponseUtils.setResults(results, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.",
                    Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to data center and clone a virtual machine identified by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the virtual machine that will be
     *                   cloned
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> cloneVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getSpecificMorObject(connectionResources, VmParameter.VIRTUAL_MACHINE.getValue(),
                vmInputs.getVirtualMachineName());

        Map<String, String> results = new HashMap<>();
        VmUtils utils = new VmUtils();
        if (vmMor != null) {
            ManagedObjectReference folder = utils.getMorFolder(vmInputs.getFolderName(), connectionResources);
            ManagedObjectReference resourcePool = utils.getMorResourcePool(vmInputs.getCloneResourcePool(), connectionResources);
            ManagedObjectReference host = utils.getMorHost(vmInputs.getHostname(), connectionResources, vmMor);
            ManagedObjectReference dataStore = utils.getMorDataStore(vmInputs.getCloneDataStore(), connectionResources, vmMor);

            VirtualMachineRelocateSpec vmRelocateSpec = utils.getVirtualMachineRelocateSpec(resourcePool, host, dataStore, vmInputs);

            VmConfigSpecs helper = new VmConfigSpecs();
            VirtualMachineCloneSpec cloneSpec = helper.getCloneSpec(vmInputs, vmRelocateSpec);

            ManagedObjectReference taskMor = connectionResources.getVimPortType()
                    .cloneVMTask(vmMor, folder, vmInputs.getCloneName(), cloneSpec);

            setTaskResults(results, connectionResources, taskMor, "Success: The [" + vmInputs.getVirtualMachineName() +
                            "] VM was successfully cloned. The taskId is: " + taskMor.getValue(),
                    "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be cloned.");
        } else {
            ResponseUtils.setResults(results, "Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.",
                    Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    private ManagedObjectReference getSpecificMorObject(ConnectionResources connectionResources, String objectType,
                                                        String objectName) throws Exception {
        ServiceContent serviceContent = getServiceContent(connectionResources);
        return FindObjects.findObject(connectionResources.getVimPortType(), serviceContent, objectType, objectName);
    }

    private ServiceContent getServiceContent(ConnectionResources connectionResources) throws RuntimeFaultFaultMsg {
        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        return connectionResources.getVimPortType().retrieveServiceContent(serviceInstance);
    }

    private void setTaskResults(Map<String, String> results, ConnectionResources connectionResources,
                                ManagedObjectReference taskMor, String successMessage, String failureMessage)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {

        if (getTaskResultAfterDone(connectionResources, taskMor)) {
            ResponseUtils.setResults(results, successMessage, Outputs.RETURN_CODE_SUCCESS);
        } else {
            ResponseUtils.setResults(results, failureMessage, Outputs.RETURN_CODE_FAILURE);
        }
    }

    protected boolean getTaskResultAfterDone(ConnectionResources connectionResources, ManagedObjectReference task)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg, InvalidCollectorVersionFaultMsg {
        WaitForValues waitForValues = new WaitForValues(connectionResources.getConnection());
        Object[] result = waitForValues.wait(task, new String[]{VmParameter.INFO_STATE.getValue(),
                        VmParameter.INFO_ERROR.getValue()}, new String[]{VmParameter.STATE.getValue()},
                new Object[][]{new Object[]{TaskInfoState.SUCCESS, TaskInfoState.ERROR}});

        if (result[1] instanceof LocalizedMethodFault) {
            throw new RuntimeException(((LocalizedMethodFault) result[1]).getLocalizedMessage());
        }

        return result[0].equals(TaskInfoState.SUCCESS);
    }
}