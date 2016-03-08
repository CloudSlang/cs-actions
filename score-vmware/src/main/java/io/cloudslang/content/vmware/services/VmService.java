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
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class VmService {
    private static final String VIRTUAL_MACHINE = "VirtualMachine";
    private static final String FOLDER = "Folder";
    private static final String RESOURCE_POOL = "ResourcePool";
    private static final String RESOURCES = "Resources";
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
    public Map<String, String> createVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference vmFolderMor = connectionResources.getVmFolderMor();
        ManagedObjectReference resourcePoolMor = connectionResources.getResourcePoolMor();
        ManagedObjectReference hostMor = connectionResources.getHostMor();

        VmConfigSpecs vmConfigSpecs = new VmConfigSpecs();
        VirtualMachineConfigSpec vmConfigSpec = vmConfigSpecs.getVmConfigSpec(vmInputs, connectionResources);

        ManagedObjectReference taskMor = connectionResources.getVimPortType().createVMTask(vmFolderMor, vmConfigSpec,
                resourcePoolMor, hostMor);

        Map<String, String> results = new HashMap<>();
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
    public Map<String, String> deleteVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getMorObject(connectionResources, VIRTUAL_MACHINE, vmInputs.getVirtualMachineName());

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
     * Method used to connect to a specified data center and powers-on the virtual machine identified by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> powerOnVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getMorObject(connectionResources, VIRTUAL_MACHINE, vmInputs.getVirtualMachineName());

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
     * Method used to connect to a specified data center and powers-off the virtual machine identified by the inputs provided.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted virtual machine
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> powerOffVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getMorObject(connectionResources, VIRTUAL_MACHINE, vmInputs.getVirtualMachineName());

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
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);

        ManagedObjectReference environmentBrowserMor = (ManagedObjectReference) connectionResources.getGetMOREF()
                .entityProps(connectionResources.getComputeResourceMor(), new String[]{Constants.ENVIRONMENT_BROWSER})
                .get(Constants.ENVIRONMENT_BROWSER);

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
     * Method used to connect to a data center to retrieve a list with all the virtual machines and templates within.
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
                .inContainerByType(connectionResources.getMorRootFolder(), VIRTUAL_MACHINE).keySet();

        Map<String, String> results = new HashMap<>();
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
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getMorObject(connectionResources, VIRTUAL_MACHINE, vmInputs.getVirtualMachineName());

        ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, vmMor,
                new String[]{Constants.SUMMARY});

        Map<String, String> results = new HashMap<>();
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
            ResponseUtils.setResults(results, "Could not retrieve the details for: [" +
                    vmInputs.getVirtualMachineName() + "] VM.", Outputs.RETURN_CODE_FAILURE);
        }
        connectionResources.getConnection().disconnect();

        return results;
    }

    /**
     * Method used to connect to a data center to update existing devices of a specified virtual machine.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted device
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> updateVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getMorObject(connectionResources, VIRTUAL_MACHINE, vmInputs.getVirtualMachineName());

        Map<String, String> results = new HashMap<>();
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

    /**
     * Method used to connect to a data center to clone an existing virtual machine.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the virtual machine that will be cloned
     * @return Map with String as key and value that contains returnCode of the operation, success message with task id
     * of the execution or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> cloneVM(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        ManagedObjectReference vmMor = getMorObject(connectionResources, VIRTUAL_MACHINE, vmInputs.getVirtualMachineName());

        Map<String, String> results = new HashMap<>();
        VmUtils utils = new VmUtils();
        if (vmMor != null) {
            ManagedObjectReference folder = getMorFolder(vmInputs, connectionResources, utils);
            ManagedObjectReference resourcePool = getMorResourcePool(vmInputs, connectionResources, utils);
            ManagedObjectReference host = getMorHost(vmInputs, connectionResources, vmMor, utils);
            ManagedObjectReference dataStore = getMorDataStore(vmInputs, connectionResources, vmMor);

            VirtualMachineRelocateSpec vmRelocateSpec = getVirtualMachineRelocateSpec(resourcePool, host, dataStore, vmInputs);

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

    private ManagedObjectReference getMorDataStore(VmInputs vmInputs, ConnectionResources connectionResources,
                                                   ManagedObjectReference vmMor) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference dataStore = null;
        if (StringUtils.isNotBlank(vmInputs.getCloneDataStore())) {
            dataStore = getDataStore(vmInputs.getCloneDataStore(), connectionResources, vmMor);
            if (dataStore == null) {
                throw new RuntimeException(ErrorMessages.DATA_STORE_NOT_FOUND);
            }
        } else {
            ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, vmMor,
                    new String[]{Constants.SUMMARY});

            for (ObjectContent objectItem : objectContents) {
                List<DynamicProperty> vmProperties = objectItem.getPropSet();
                for (DynamicProperty propertyItem : vmProperties) {
                    VirtualMachineSummary virtualMachineSummary = (VirtualMachineSummary) propertyItem.getVal();
                    String vmPathName = virtualMachineSummary.getConfig().getVmPathName();
                    String dataStoreName = vmPathName.substring(1, vmPathName.indexOf(Constants.RIGHT_SQUARE_BRACKET));
                    dataStore = getDataStore(dataStoreName, connectionResources, vmMor);
                    break;
                }
                break;
            }
        }
        return dataStore;
    }

    private ManagedObjectReference getMorHost(VmInputs vmInputs, ConnectionResources connectionResources, ManagedObjectReference vmMor,
                                              VmUtils utils) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference host = null;
        if (StringUtils.isNotBlank(vmInputs.getCloneHost())) {
            Map<String, ManagedObjectReference> hostsMap = connectionResources.getGetMOREF()
                    .inContainerByType(connectionResources.getMorRootFolder(), Constants.HOST_SYSTEM, new RetrieveOptions());
            host = utils.getMorObject(hostsMap, vmInputs.getCloneHost());
            if (host == null) {
                throw new RuntimeException(ErrorMessages.HOST_NOT_FOUND);
            }
        } else {
            ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, vmMor,
                    new String[]{Constants.SUMMARY});

            for (ObjectContent objectItem : objectContents) {
                List<DynamicProperty> vmProperties = objectItem.getPropSet();
                for (DynamicProperty propertyItem : vmProperties) {
                    VirtualMachineSummary virtualMachineSummary = (VirtualMachineSummary) propertyItem.getVal();
                    host = virtualMachineSummary.getRuntime().getHost();
                    break;
                }
                break;
            }
        }
        return host;
    }

    private ManagedObjectReference getMorResourcePool(VmInputs vmInputs, ConnectionResources connectionResources, VmUtils utils)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference resourcePool;
        if (StringUtils.isNotBlank(vmInputs.getCloneResourcePool())) {
            Map<String, ManagedObjectReference> poolsMap = connectionResources.getGetMOREF()
                    .inContainerByType(connectionResources.getMorRootFolder(), RESOURCE_POOL, new RetrieveOptions());
            resourcePool = utils.getMorObject(poolsMap, vmInputs.getCloneResourcePool());
            if (resourcePool == null) {
                throw new RuntimeException(ErrorMessages.RESOURCE_POOL_NOT_FOUND);
            }
        } else {
            Map<String, ManagedObjectReference> poolsMap = connectionResources.getGetMOREF()
                    .inContainerByType(connectionResources.getMorRootFolder(), RESOURCE_POOL, new RetrieveOptions());
            resourcePool = utils.getMorObject(poolsMap, RESOURCES);
        }
        return resourcePool;
    }

    private ManagedObjectReference getMorFolder(VmInputs vmInputs, ConnectionResources connectionResources, VmUtils utils)
            throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        ManagedObjectReference folder;
        if (StringUtils.isNotBlank(vmInputs.getFolderName())) {
            Map<String, ManagedObjectReference> foldersMap = connectionResources.getGetMOREF()
                    .inContainerByType(connectionResources.getMorRootFolder(), FOLDER, new RetrieveOptions());
            folder = utils.getMorObject(foldersMap, vmInputs.getFolderName());
            if (folder == null) {
                throw new RuntimeException(ErrorMessages.FOLDER_NOT_FOUND);
            }
        } else {
            folder = connectionResources.getMorRootFolder();
        }
        return folder;
    }

    private ManagedObjectReference getDataStore(String dataStoreName, ConnectionResources connectionResources,
                                                ManagedObjectReference vmMor) throws InvalidPropertyFaultMsg, RuntimeFaultFaultMsg {
        List<ManagedObjectReference> dataStores = ((ArrayOfManagedObjectReference) connectionResources.getGetMOREF()
                .entityProps(vmMor, new String[]{Constants.DATASTORE}).get(Constants.DATASTORE)).getManagedObjectReference();

        for (ManagedObjectReference dataStore : dataStores) {
            DatastoreSummary datastoreSummary = (DatastoreSummary) connectionResources.getGetMOREF()
                    .entityProps(dataStore, new String[]{Constants.SUMMARY}).get(Constants.SUMMARY);
            if (dataStoreName.equalsIgnoreCase(datastoreSummary.getName())) {
                return dataStore;
            }
        }
        return null;
    }

    private VirtualMachineRelocateSpec getVirtualMachineRelocateSpec(ManagedObjectReference resourcePool,
                                                                     ManagedObjectReference host,
                                                                     ManagedObjectReference dataStore,
                                                                     VmInputs vmInputs) {
        VirtualMachineRelocateSpec vmRelocateSpec = new VirtualMachineRelocateSpec();
        vmRelocateSpec.setPool(resourcePool);
        vmRelocateSpec.setHost(host);
        vmRelocateSpec.setDatastore(dataStore);

        if (vmInputs.isThickProvision()) {
            vmRelocateSpec.setTransform(VirtualMachineRelocateTransformation.FLAT);
        } else {
            vmRelocateSpec.setTransform(VirtualMachineRelocateTransformation.SPARSE);
        }

        return vmRelocateSpec;
    }

    private ServiceContent getServiceContent(ConnectionResources connectionResources) throws RuntimeFaultFaultMsg {
        ManagedObjectReference serviceInstance = connectionResources.getServiceInstance();
        return connectionResources.getVimPortType().retrieveServiceContent(serviceInstance);
    }

    private ManagedObjectReference getMorObject(ConnectionResources connectionResources, String objectType, String objectName)
            throws Exception {
        ServiceContent serviceContent = getServiceContent(connectionResources);
        return FindObjects.findObject(connectionResources.getVimPortType(), serviceContent, objectType, objectName);
    }

    private VirtualMachineConfigSpec getUpdateConfigSpec(VmInputs vmInputs, VirtualMachineConfigSpec vmConfigSpec,
                                                         String device) throws Exception {
        if (!InputUtils.isUpdateOperation(vmInputs)) {
            throw new RuntimeException(ErrorMessages.CPU_OR_MEMORY_INVALID_OPERATION);
        }
        VmConfigSpecs specs = new VmConfigSpecs();
        ResourceAllocationInfo resourceAllocationInfo = specs.getResourceAllocationInfo(vmInputs.getUpdateValue());
        if (CPU.equalsIgnoreCase(device)) {
            vmConfigSpec.setCpuAllocation(resourceAllocationInfo);
        } else {
            vmConfigSpec.setMemoryAllocation(resourceAllocationInfo);
        }

        return vmConfigSpec;
    }

    private VirtualMachineConfigSpec getAddOrRemoveSpecs(ConnectionResources connectionResources, ManagedObjectReference vmMor,
                                                         VmInputs vmInputs, VirtualMachineConfigSpec vmConfigSpec, String device)
            throws Exception {
        VmConfigSpecs vmConfigSpecs = new VmConfigSpecs();
        VirtualDeviceConfigSpec deviceConfigSpec = new VirtualDeviceConfigSpec();
        switch (device) {
            case DISK:
                InputUtils.checkValidOperation(vmInputs, device);
                InputUtils.validateDiskInputs(vmInputs);
                deviceConfigSpec = vmConfigSpecs.getDiskDeviceConfigSpec(connectionResources, vmMor, vmInputs);
                break;
            case CD:
                InputUtils.checkValidOperation(vmInputs, device);
                deviceConfigSpec = vmConfigSpecs.getCDDeviceConfigSpec(connectionResources, vmMor, vmInputs);
                break;
            case NIC:
                InputUtils.checkValidOperation(vmInputs, device);
                deviceConfigSpec = vmConfigSpecs.getNICDeviceConfigSpec(connectionResources, vmMor, vmInputs);
                break;
        }
        List<VirtualDeviceConfigSpec> specs = new ArrayList<>();
        specs.add(deviceConfigSpec);
        vmConfigSpec.getDeviceChange().addAll(specs);

        return vmConfigSpec;
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
}