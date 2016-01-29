package io.cloudslang.content.vmware.services;

import com.vmware.vim25.*;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.connection.helpers.WaitForValues;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.Device;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.FindObjects;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import io.cloudslang.content.vmware.services.utils.VmConfigSpecs;
import io.cloudslang.content.vmware.services.utils.VmUtils;
import io.cloudslang.content.vmware.utils.InputUtils;
import io.cloudslang.content.vmware.utils.ResponseUtils;

import java.util.*;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class VmService {
    private static final String VIRTUAL_MACHINE = "VirtualMachine";
    private static final String CPU = "cpu";
    private static final String MEMORY = "memory";
    private static final String DISK = "disk";
    private static final String CD = "cd";
    private static final String NIC = "nic";
    private static final String STATE = "state";
    private static final String INFO_STATE = "info.state";
    private static final String INFO_ERROR = "info.error";

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

        VmConfigSpecs vmConfigSpecs = new VmConfigSpecs();
        VirtualMachineConfigSpec vmConfigSpec = vmConfigSpecs.getVmConfigSpec(vmInputs, connectionResources);

        ManagedObjectReference taskMor = connectionResources.getVimPortType().createVMTask(vmFolderMor, vmConfigSpec,
                resourcePoolMor, hostMor);

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
        ManagedObjectReference vmMor = getVmMor(connectionResources, vmInputs);

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
        ManagedObjectReference vmMor = getVmMor(connectionResources, vmInputs);

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
        ManagedObjectReference vmMor = getVmMor(connectionResources, vmInputs);

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

        ResponseUtils.setResults(results, ResponseUtils.getResponseStringFromCollection(guestOSDescriptors, delimiter),
                Outputs.RETURN_CODE_SUCCESS);
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
                .inContainerByType(connectionResources.getMorRootFolder(), VIRTUAL_MACHINE).keySet();

        if (virtualMachineNamesList.size() > 0) {
            ResponseUtils.setResults(results, ResponseUtils.getResponseStringFromCollection(virtualMachineNamesList, delimiter),
                    Outputs.RETURN_CODE_SUCCESS);
        } else {
            ResponseUtils.setResults(results, "No VM found in datacenter.", Outputs.RETURN_CODE_FAILURE);
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
        ManagedObjectReference vmMor = getVmMor(connectionResources, vmInputs);

        ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, vmMor,
                new String[]{Constants.SUMMARY});

        if (objectContents != null) {
            Map<String, String> vmDetails = new HashMap<>();
            for (ObjectContent objectItem : objectContents) {
                List<DynamicProperty> vmProperties = objectItem.getPropSet();
                for (DynamicProperty propertyItem : vmProperties) {
                    VirtualMachineSummary virtualMachineSummary = (VirtualMachineSummary) propertyItem.getVal();
                    VirtualMachineConfigSummary virtualMachineConfigSummary = virtualMachineSummary.getConfig();

                    vmDetails = ResponseUtils.getVmDetailedMap(vmDetails, virtualMachineSummary, virtualMachineConfigSummary);
                }
            }
            String responseJson = ResponseUtils.getJsonString(vmDetails);
            ResponseUtils.setResults(results, responseJson, Outputs.RETURN_CODE_SUCCESS);
        } else {
            ResponseUtils.setResults(results,
                    "Could not retrieve the details for: [" + vmInputs.getVirtualMachineName() + "] VM.",
                    Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    public Map<String, String> updateVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        Map<String, String> results = new HashMap<>();
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getVmMor(connectionResources, vmInputs);

        if (vmMor != null) {
            String device = Device.getValue(vmInputs.getDevice()).toLowerCase();
            VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
            if (CPU.equalsIgnoreCase(device) || MEMORY.equalsIgnoreCase(device)) {
                vmConfigSpec = getUpdateConfigSpec(vmInputs, vmConfigSpec, device);
            } else {
                vmConfigSpec = getAddOrRemoveSpecs(connectionResources, vmMor, vmInputs, vmConfigSpec, device);
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

    private ServiceContent getServiceContent(ConnectionResources connectionResources) throws RuntimeFaultFaultMsg {
        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        VimPortType vimPort = connectionResources.getVimPortType();

        return vimPort.retrieveServiceContent(serviceInstance);
    }

    private ManagedObjectReference getVmMor(ConnectionResources connectionResources, VmInputs vmInputs) throws Exception {
        ServiceContent serviceContent = getServiceContent(connectionResources);

        return FindObjects.findObject(connectionResources.getVimPortType(), serviceContent, VIRTUAL_MACHINE,
                vmInputs.getVirtualMachineName());
    }

    private VirtualMachineConfigSpec getUpdateConfigSpec(VmInputs vmInputs, VirtualMachineConfigSpec vmConfigSpec,
                                                         String device) throws Exception {
        if (!InputUtils.isUpdateOperation(vmInputs)) {
            throw new RuntimeException(ErrorMessages.CPU_OR_MEMORY_INVALID_OPERATION);
        }
        ResourceAllocationInfo resourceAllocationInfo = getResourceAllocationInfo(vmInputs);
        if (CPU.equalsIgnoreCase(device)) {
            vmConfigSpec.setCpuAllocation(resourceAllocationInfo);
        } else {
            vmConfigSpec.setMemoryAllocation(resourceAllocationInfo);
        }

        return vmConfigSpec;
    }

    private VirtualMachineConfigSpec getAddOrRemoveSpecs(ConnectionResources connectionResources,
                                                         ManagedObjectReference vmMor, VmInputs vmInputs,
                                                         VirtualMachineConfigSpec vmConfigSpec, String device) throws Exception {
        VmConfigSpecs vmConfigSpecs = new VmConfigSpecs();
        VirtualDeviceConfigSpec deviceConfigSpec = new VirtualDeviceConfigSpec();
        switch (device) {
            case DISK:
                checkValidOperation(vmInputs, device);
                InputUtils.validateDiskInputs(vmInputs);
                deviceConfigSpec = vmConfigSpecs.getDiskDeviceConfigSpec(connectionResources, vmMor, vmInputs);
                break;
            case CD:
                checkValidOperation(vmInputs, device);
                deviceConfigSpec = vmConfigSpecs.getCDDeviceConfigSpec(connectionResources, vmMor, vmInputs);
                break;
            case NIC:
                checkValidOperation(vmInputs, device);
                deviceConfigSpec = vmConfigSpecs.getNICDeviceConfigSpec(connectionResources, vmMor, vmInputs);
                break;
        }
        List<VirtualDeviceConfigSpec> specs = new ArrayList<>();
        specs.add(deviceConfigSpec);
        vmConfigSpec.getDeviceChange().addAll(specs);

        return vmConfigSpec;
    }

    private ResourceAllocationInfo getResourceAllocationInfo(VmInputs vmInputs) throws Exception {
        VmUtils vmUtils = new VmUtils();
        SharesInfo sharesInfo = vmUtils.getSharesInfo(vmInputs.getUpdateValue());
        ResourceAllocationInfo resourceAllocationInfo = new ResourceAllocationInfo();
        resourceAllocationInfo.setShares(sharesInfo);

        return resourceAllocationInfo;
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
        Object[] result = waitForValues.wait(task, new String[]{INFO_STATE, INFO_ERROR}, new String[]{STATE},
                new Object[][]{new Object[]{TaskInfoState.SUCCESS, TaskInfoState.ERROR}});

        if (result[1] instanceof LocalizedMethodFault) {
            throw new RuntimeException(((LocalizedMethodFault) result[1]).getLocalizedMessage());
        }

        return result[0].equals(TaskInfoState.SUCCESS);
    }

    private void checkValidOperation(VmInputs vmInputs, String device) {
        if (!InputUtils.isValidUpdateOperation(vmInputs)) {
            throw new RuntimeException("Invalid operation specified for " + device + " device. " +
                    "The " + device + " device can be only added or removed.");
        }
    }
}