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

import com.vmware.vim25.CustomizationSpec;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.VimPortType;
import io.cloudslang.content.vmware.connection.ConnectionResources;
import io.cloudslang.content.vmware.entities.GuestInputs;
import io.cloudslang.content.vmware.entities.ManagedObjectType;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.services.helpers.MorObjectHandler;
import io.cloudslang.content.vmware.services.helpers.ResponseHelper;
import io.cloudslang.content.vmware.services.utils.GuestConfigSpecs;

import java.util.Map;

import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.constants.ReturnCodes.SUCCESS;
import static io.cloudslang.content.vmware.services.helpers.ConnectionHandler.cleanupResources;
import static io.cloudslang.content.vmware.utils.ResponseUtils.getResultsMap;
import static io.cloudslang.content.vmware.utils.ResponseUtils.getVmNotFoundResultsMap;
import static java.lang.String.format;

/**
 * Created by Mihai Tusa.
 * 3/21/2016.
 */
public class GuestService {
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
    public Map<String, String> customizeVM(HttpInputs httpInputs, VmInputs vmInputs, GuestInputs guestInputs, boolean isWin) throws Exception {
        ConnectionResources connectionResources = new ConnectionResources(httpInputs, vmInputs);
        try {
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources, ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                VimPortType vimPortType = connectionResources.getVimPortType();
                CustomizationSpec customizationSpec = isWin ?
                        new GuestConfigSpecs().getWinCustomizationSpec(guestInputs) :
                        new GuestConfigSpecs().getLinuxCustomizationSpec(guestInputs);
                vimPortType.checkCustomizationSpec(vmMor, customizationSpec);

                ManagedObjectReference task = vimPortType.customizeVMTask(vmMor, customizationSpec);

                return new ResponseHelper(connectionResources, task).getResultsMap(
                        format("Success: The [%s] VM was successfully customized. The taskId is: %s.",
                                vmInputs.getVirtualMachineName(), task.getValue()),
                        format("Failure: The [%s] VM could not be customized.", vmInputs.getVirtualMachineName()));
            }
            return getVmNotFoundResultsMap(vmInputs);
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
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
            ManagedObjectReference vmMor = new MorObjectHandler().getMor(connectionResources, ManagedObjectType.VIRTUAL_MACHINE.getValue(), vmInputs.getVirtualMachineName());
            if (vmMor != null) {
                connectionResources.getVimPortType().mountToolsInstaller(vmMor);

                return getResultsMap(format("Initiated VMware Tools Installer Mount on: %s.", vmInputs.getVirtualMachineName()), SUCCESS);
            }
            return getVmNotFoundResultsMap(vmInputs);
        } catch (Exception ex) {
            return getResultsMap(ex.toString(), FAILURE);
        } finally {
            if (httpInputs.isCloseSession()) {
                cleanupResources(httpInputs, connectionResources);
            }
        }
    }
}
