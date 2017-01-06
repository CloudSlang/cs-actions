/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.services;

import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.GuestOsDescriptor;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;
import com.vmware.vim25.VirtualMachineCloneSpec;
import com.vmware.vim25.VirtualMachineConfigOption;
import com.vmware.vim25.VirtualMachineConfigSpec;
import com.vmware.vim25.VirtualMachineConfigSummary;
import com.vmware.vim25.VirtualMachineRelocateSpec;
import com.vmware.vim25.VirtualMachineSummary;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.Device;
import io.cloudslang.content.vmware.entities.ManagedObjectType;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.GetObjectProperties;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;
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
        try {
            ManagedObjectReference environmentBrowserMor = new MorObjectHandler()
                    .getEnvironmentBrowser(connectionResources, ManagedObjectType.ENVIRONMENT_BROWSER.getValue());
            VirtualMachineConfigOption configOptions = connectionResources.getVimPortType()
                    .queryConfigOption(environmentBrowserMor, null, connectionResources.getHostMor());

            List<GuestOsDescriptor> guestOSDescriptors = configOptions.getGuestOSDescriptor();

            return ResponseUtils.getResultsMap(ResponseUtils.getResponseStringFromCollection(guestOSDescriptors, delimiter),
                    Outputs.RETURN_CODE_SUCCESS);
        } catch (Exception ex) {
            return ResponseUtils.getResultsMap(ex.toString(), Outputs.RETURN_CODE_FAILURE);
        } finally {
            connectionResources.getConnection().disconnect();
        }
    }

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
        try {
            VmUtils utils = new VmUtils();
            ManagedObjectReference vmFolderMor = utils.getMorFolder(vmInputs.getFolderName(), connectionResources);
            ManagedObjectReference resourcePoolMor = utils.getMorResourcePool(vmInputs.getResourcePool(), connectionResources);
            ManagedObjectReference hostMor = utils.getMorHost(vmInputs.getHostname(), connectionResources, null);
            VirtualMachineConfigSpec vmConfigSpec = new VmConfigSpecs().getVmConfigSpec(vmInputs, connectionResources);

            ManagedObjectReference task = connectionResources.getVimPortType()
                    .createVMTask(vmFolderMor, vmConfigSpec, resourcePoolMor, hostMor);

            return new ResponseHelper(connectionResources, task).getResultsMap("Success: Created [" +
                            vmInputs.getVirtualMachineName() + "] VM. The taskId is: " + task.getValue(),
                    "Failure: Could not create [" + vmInputs.getVirtualMachineName() + "] VM");
        } catch (Exception ex) {
            return ResponseUtils.getResultsMap(ex.toString(), Outputs.RETURN_CODE_FAILURE);
        } finally {
            connectionResources.getConnection().disconnect();
        }

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
        try {
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources,
                    ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                ManagedObjectReference task = connectionResources.getVimPortType().destroyTask(vmMor);

                return new ResponseHelper(connectionResources, task).getResultsMap("Success: The [" +
                                vmInputs.getVirtualMachineName() + "] VM was deleted. The taskId is: " + task.getValue(),
                        "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be deleted.");
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
        try {
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources,
                    ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                ManagedObjectReference task = connectionResources.getVimPortType().powerOnVMTask(vmMor, null);

                return new ResponseHelper(connectionResources, task).getResultsMap("Success: The [" +
                        vmInputs.getVirtualMachineName() + "] VM was successfully powered on. The taskId is: " +
                        task.getValue(), "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be powered on.");
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
        try {
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources,
                    ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                ManagedObjectReference task = connectionResources.getVimPortType().powerOffVMTask(vmMor);

                return new ResponseHelper(connectionResources, task).getResultsMap("Success: The [" +
                        vmInputs.getVirtualMachineName() + "] VM was successfully powered off. The taskId is: " +
                        task.getValue(), "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be powered off.");
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
     * Method used to connect to data center to retrieve a list with all the virtual machines and templates within.
     *
     * @param httpInputs Object that has all the inputs necessary to made a connection to data center
     * @param vmInputs   Object that has all the specific inputs necessary to identify the targeted data center
     * @param delimiter  String that represents the delimiter needed in the result list
     * @return Map with String as key and value that contains returnCode of the operation, a list that contains all the
     * virtual machines and templates within the data center or failure message and the exception if there is one
     * @throws Exception
     */
    public Map<String, String> listVMsAndTemplates(HttpInputs httpInputs, VmInputs vmInputs, String delimiter) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        try {
            Map<String, ManagedObjectReference> virtualMachinesMorMap = new MorObjectHandler()
                    .getSpecificObjectsMap(connectionResources, ManagedObjectType.VIRTUAL_MACHINE.getValue());
            Set<String> virtualMachineNamesList = virtualMachinesMorMap.keySet();

            if (virtualMachineNamesList.size() > 0) {
                return ResponseUtils.getResultsMap(ResponseUtils
                        .getResponseStringFromCollection(virtualMachineNamesList, delimiter), Outputs.RETURN_CODE_SUCCESS);
            } else {
                return ResponseUtils.getResultsMap("No VM found in datacenter.", Outputs.RETURN_CODE_FAILURE);
            }
        } catch (Exception ex) {
            return ResponseUtils.getResultsMap(ex.toString(), Outputs.RETURN_CODE_FAILURE);
        } finally {
            connectionResources.getConnection().disconnect();
        }
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
        try {
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources,
                    ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            ObjectContent[] objectContents = GetObjectProperties.getObjectProperties(connectionResources, vmMor,
                    new String[]{ManagedObjectType.SUMMARY.getValue()});

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
                return ResponseUtils.getResultsMap(responseJson, Outputs.RETURN_CODE_SUCCESS);
            } else {
                return ResponseUtils.getResultsMap("Could not retrieve the details for: [" +
                        vmInputs.getVirtualMachineName() + "] VM.", Outputs.RETURN_CODE_FAILURE);
            }
        } catch (Exception ex) {
            return ResponseUtils.getResultsMap(ex.toString(), Outputs.RETURN_CODE_FAILURE);
        } finally {
            connectionResources.getConnection().disconnect();
        }
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
        try {
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources,
                    ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
                String device = Device.getValue(vmInputs.getDevice()).toLowerCase();
                if (Constants.CPU.equalsIgnoreCase(device) || Constants.MEMORY.equalsIgnoreCase(device)) {
                    vmConfigSpec = new VmUtils().getUpdateConfigSpec(vmInputs, vmConfigSpec, device);
                } else {
                    vmConfigSpec = new VmUtils().getAddOrRemoveSpecs(connectionResources, vmMor, vmInputs, vmConfigSpec, device);
                }

                ManagedObjectReference task = connectionResources.getVimPortType().reconfigVMTask(vmMor, vmConfigSpec);

                return new ResponseHelper(connectionResources, task).getResultsMap("Success: The [" +
                        vmInputs.getVirtualMachineName() + "] VM was successfully reconfigured. The taskId is: " +
                        task.getValue(), "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be reconfigured.");
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
        try {
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources,
                    ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());

            if (vmMor != null) {
                VmUtils utils = new VmUtils();
                ManagedObjectReference folder = utils.getMorFolder(vmInputs.getFolderName(), connectionResources);
                ManagedObjectReference resourcePool = utils.getMorResourcePool(vmInputs.getCloneResourcePool(), connectionResources);
                ManagedObjectReference host = utils.getMorHost(vmInputs.getCloneHost(), connectionResources, vmMor);
                ManagedObjectReference dataStore = utils.getMorDataStore(vmInputs.getCloneDataStore(), connectionResources,
                        vmMor, vmInputs);

                VirtualMachineRelocateSpec vmRelocateSpec = utils.getVirtualMachineRelocateSpec(resourcePool, host, dataStore, vmInputs);

                VirtualMachineCloneSpec cloneSpec = new VmConfigSpecs().getCloneSpec(vmInputs, vmRelocateSpec);

                ManagedObjectReference taskMor = connectionResources.getVimPortType()
                        .cloneVMTask(vmMor, folder, vmInputs.getCloneName(), cloneSpec);

                return new ResponseHelper(connectionResources, taskMor).getResultsMap("Success: The [" +
                        vmInputs.getVirtualMachineName() + "] VM was successfully cloned. The taskId is: " +
                        taskMor.getValue(), "Failure: The [" + vmInputs.getVirtualMachineName() + "] VM could not be cloned.");
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
