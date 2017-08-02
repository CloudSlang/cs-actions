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
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;
import io.cloudslang.content.vmware.services.utils.VmConfigSpecs;
import io.cloudslang.content.vmware.services.utils.VmUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.vmware.constants.Constants.CPU;
import static io.cloudslang.content.vmware.constants.Constants.MEMORY;
import static io.cloudslang.content.vmware.entities.Device.getValue;
import static io.cloudslang.content.vmware.entities.ManagedObjectType.ENVIRONMENT_BROWSER;
import static io.cloudslang.content.vmware.entities.ManagedObjectType.SUMMARY;
import static io.cloudslang.content.vmware.entities.ManagedObjectType.VIRTUAL_MACHINE;
import static io.cloudslang.content.vmware.services.helpers.ConnectionHandler.cleanupResources;
import static io.cloudslang.content.vmware.services.helpers.GetObjectProperties.getObjectProperties;
import static io.cloudslang.content.vmware.utils.ResponseUtils.addDataToVmDetailsMap;
import static io.cloudslang.content.vmware.utils.ResponseUtils.getJsonString;
import static io.cloudslang.content.vmware.utils.ResponseUtils.getResponseStringFromCollection;
import static io.cloudslang.content.vmware.utils.ResponseUtils.getResultsMap;
import static io.cloudslang.content.vmware.utils.ResponseUtils.getVmNotFoundResultsMap;
import static java.lang.String.format;

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
            ManagedObjectReference environmentBrowserMor = new MorObjectHandler().getEnvironmentBrowser(connectionResources, ENVIRONMENT_BROWSER.getValue());
            VirtualMachineConfigOption configOptions = connectionResources.getVimPortType().queryConfigOption(environmentBrowserMor, null, connectionResources.getHostMor());

            List<GuestOsDescriptor> guestOSDescriptors = configOptions.getGuestOSDescriptor();

            return getResultsMap(getResponseStringFromCollection(guestOSDescriptors, delimiter), SUCCESS);
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
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

            ManagedObjectReference task = connectionResources.getVimPortType().createVMTask(vmFolderMor, vmConfigSpec, resourcePoolMor, hostMor);

            return new ResponseHelper(connectionResources, task).getResultsMap(
                    format("Success: Created [%s] VM. The taskId is: %s.", vmInputs.getVirtualMachineName(), task.getValue()),
                    format("Failure: Could not create [%s] VM.", vmInputs.getVirtualMachineName()));
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
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
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources, VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                ManagedObjectReference task = connectionResources.getVimPortType().destroyTask(vmMor);

                return new ResponseHelper(connectionResources, task).getResultsMap(
                        format("Success: The [%s] VM was deleted. The taskId is: %s.", vmInputs.getVirtualMachineName(), task.getValue()),
                        format("Failure: The [%s] VM could not be deleted.", vmInputs.getVirtualMachineName()));
            } else {
                return getVmNotFoundResultsMap(vmInputs);
            }
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
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
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources, VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                ManagedObjectReference task = connectionResources.getVimPortType().powerOnVMTask(vmMor, null);

                return new ResponseHelper(connectionResources, task).getResultsMap(
                        format("Success: The [%s] VM was successfully powered on. The taskId is: %s.",
                                vmInputs.getVirtualMachineName(), task.getValue()),
                        format("Failure: The [%s] VM could not be powered on.", vmInputs.getVirtualMachineName()));
            } else {
                return getVmNotFoundResultsMap(vmInputs);
            }
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
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
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources, VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                ManagedObjectReference task = connectionResources.getVimPortType().powerOffVMTask(vmMor);

                return new ResponseHelper(connectionResources, task).getResultsMap(
                        format("Success: The [%s] VM was successfully powered off. The taskId is: %s.",
                                vmInputs.getVirtualMachineName(), task.getValue()),
                        format("Failure: The [%s] VM could not be powered off.", vmInputs.getVirtualMachineName()));
            } else {
                return getVmNotFoundResultsMap(vmInputs);
            }
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
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
            Map<String, ManagedObjectReference> virtualMachinesMorMap = new MorObjectHandler().getSpecificObjectsMap(connectionResources, VIRTUAL_MACHINE.getValue());
            Set<String> virtualMachineNamesList = virtualMachinesMorMap.keySet();

            return virtualMachineNamesList.size() > 0 ?
                    getResultsMap(getResponseStringFromCollection(virtualMachineNamesList, delimiter), SUCCESS) :
                    getResultsMap("No VM found in datacenter.", FAILURE);
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
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
                    VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            ObjectContent[] objectContents = getObjectProperties(connectionResources, vmMor, new String[]{SUMMARY.getValue()});

            if (objectContents != null) {
                Map<String, String> vmDetails = new HashMap<>();
                for (ObjectContent objectItem : objectContents) {
                    List<DynamicProperty> vmProperties = objectItem.getPropSet();
                    for (DynamicProperty propertyItem : vmProperties) {
                        VirtualMachineSummary virtualMachineSummary = (VirtualMachineSummary) propertyItem.getVal();
                        VirtualMachineConfigSummary virtualMachineConfigSummary = virtualMachineSummary.getConfig();
                        addDataToVmDetailsMap(vmDetails, virtualMachineSummary, virtualMachineConfigSummary);
                    }
                }

                return getResultsMap(getJsonString(vmDetails), SUCCESS);
            } else {
                return getResultsMap(format("Could not retrieve the details for: [%s] VM.", vmInputs.getVirtualMachineName()), FAILURE);
            }
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
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
                    VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                VirtualMachineConfigSpec vmConfigSpec = new VirtualMachineConfigSpec();
                String device = getValue(vmInputs.getDevice()).toLowerCase();

                vmConfigSpec = CPU.equalsIgnoreCase(device) || MEMORY.equalsIgnoreCase(device) ?
                        new VmUtils().getUpdateConfigSpec(vmInputs, vmConfigSpec, device) :
                        new VmUtils().getAddOrRemoveSpecs(connectionResources, vmMor, vmInputs, vmConfigSpec, device);

                ManagedObjectReference task = connectionResources.getVimPortType().reconfigVMTask(vmMor, vmConfigSpec);

                return new ResponseHelper(connectionResources, task).getResultsMap(
                        format("Success: The [%s] VM was successfully reconfigured. The taskId is: %s.",
                                vmInputs.getVirtualMachineName(), task.getValue()),
                        format("Failure: The [%s] VM could not be reconfigured.", vmInputs.getVirtualMachineName()));
            } else {
                return getVmNotFoundResultsMap(vmInputs);
            }
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
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
                    VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());

            if (vmMor != null) {
                VmUtils utils = new VmUtils();
                ManagedObjectReference folder = utils.getMorFolder(vmInputs.getFolderName(), connectionResources);
                ManagedObjectReference resourcePool = utils.getMorResourcePool(vmInputs.getCloneResourcePool(), connectionResources);
                ManagedObjectReference host = utils.getMorHost(vmInputs.getCloneHost(), connectionResources, vmMor);
                ManagedObjectReference dataStore = utils.getMorDataStore(vmInputs.getCloneDataStore(), connectionResources, vmMor, vmInputs);
                VirtualMachineRelocateSpec vmRelocateSpec = utils.getVirtualMachineRelocateSpec(resourcePool, host, dataStore, vmInputs);
                VirtualMachineCloneSpec cloneSpec = new VmConfigSpecs().getCloneSpec(vmInputs, vmRelocateSpec);

                ManagedObjectReference taskMor = connectionResources.getVimPortType().cloneVMTask(vmMor, folder, vmInputs.getCloneName(), cloneSpec);

                return new ResponseHelper(connectionResources, taskMor).getResultsMap(
                        format("Success: The [%s] VM was successfully cloned. The taskId is: %s.",
                                vmInputs.getVirtualMachineName(), taskMor.getValue()),
                        format("Failure: The [%s] VM could not be cloned.", vmInputs.getVirtualMachineName()));
            } else {
                return getVmNotFoundResultsMap(vmInputs);
            }
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
        }
    }

    public Map<String, String> createVMSnapshot(HttpInputs httpInputs, VmInputs vmInputs) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        String virtualMachineName = vmInputs.getVirtualMachineName();
        try {
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources, VIRTUAL_MACHINE.getValue(), virtualMachineName);
            if (vmMor != null) {
                ManagedObjectReference taskMor = connectionResources.getVimPortType().createSnapshotTask(vmMor, vmInputs.getSnapshotName(),
                        vmInputs.getSnapshotDescription(), vmInputs.isWithMemoryDump(), vmInputs.isQuiesce());

                return new ResponseHelper(connectionResources, taskMor).getResultsMap(
                        format("Success: The snapshot of [%s] VM was successfully created. The taskId is: %s.", virtualMachineName, taskMor.getValue()),
                        format("Failure: The snapshot of [%s] VM could not be created.", vmInputs.getVirtualMachineName()));
            } else {
                return getVmNotFoundResultsMap(vmInputs);
            }
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
        }
    }
}
