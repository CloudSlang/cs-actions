/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.vim25.GuestOsDescriptor;
import com.vmware.vim25.VirtualMachineConfigSummary;
import com.vmware.vim25.VirtualMachineSummary;
import io.cloudslang.content.vmware.entities.ManagedObjectType;
import io.cloudslang.content.vmware.entities.VmInputs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static io.cloudslang.content.constants.OutputNames.RETURN_CODE;
import static io.cloudslang.content.constants.OutputNames.RETURN_RESULT;
import static io.cloudslang.content.constants.ReturnCodes.FAILURE;
import static io.cloudslang.content.vmware.constants.Constants.RIGHT_SQUARE_BRACKET;
import static io.cloudslang.content.vmware.constants.Inputs.DATA_STORE;
import static io.cloudslang.content.vmware.constants.Inputs.VM_CPU_COUNT;
import static io.cloudslang.content.vmware.constants.Inputs.VM_MEMORY_SIZE;
import static io.cloudslang.content.vmware.utils.InputUtils.getDefaultDelimiter;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by Mihai Tusa.
 * 1/19/2016.
 */
public class ResponseUtils {
    private static final String COMMA_DELIMITER = ",";

    public static Map<String, String> getResultsMap(String returnResultMessage, String returnCodeMessage) {
        Map<String, String> results = new HashMap<>();
        results.put(RETURN_RESULT, returnResultMessage);
        results.put(RETURN_CODE, returnCodeMessage);

        return results;
    }

    public static Map<String, String> getVmNotFoundResultsMap(VmInputs vmInputs) {
        return getResultsMap(format("Could not find the [%s] VM.", vmInputs.getVirtualMachineName()), FAILURE);
    }

    public static <T> String getResponseStringFromCollection(Collection<T> collectionItems, String delimiter) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (T item : collectionItems) {
            String itemName = item instanceof GuestOsDescriptor ? ((GuestOsDescriptor) item).getId() : (String) item;
            sb.append(itemName);
            if (index < collectionItems.size() - 1) {
                sb.append(getDefaultDelimiter(delimiter, COMMA_DELIMITER));
            }
            index++;
        }

        return sb.toString();
    }

    public static void addDataToVmDetailsMap(Map<String, String> inputMap, VirtualMachineSummary virtualMachineSummary,
                                             VirtualMachineConfigSummary virtualMachineConfigSummary) {
        inputMap.put(ManagedObjectType.VM_ID.getValue(), virtualMachineSummary.getVm().getValue());
        inputMap.put(ManagedObjectType.VM_FULL_NAME.getValue(), virtualMachineConfigSummary.getGuestFullName());
        inputMap.put(ManagedObjectType.VM_UUID.getValue(), virtualMachineConfigSummary.getUuid());
        inputMap.put(VM_CPU_COUNT, virtualMachineConfigSummary.getNumCpu().toString());
        inputMap.put(VM_MEMORY_SIZE, virtualMachineConfigSummary.getMemorySizeMB().toString());
        inputMap.put(ManagedObjectType.VM_ETH_COUNT.getValue(), virtualMachineConfigSummary.getNumEthernetCards().toString());
        inputMap.put(ManagedObjectType.VM_DISK_COUNT.getValue(), virtualMachineConfigSummary.getNumVirtualDisks().toString());
        inputMap.put(DATA_STORE, virtualMachineConfigSummary.getVmPathName().substring(1, virtualMachineConfigSummary.getVmPathName().indexOf(RIGHT_SQUARE_BRACKET)));
        inputMap.put(ManagedObjectType.VM_PATH_NAME.getValue(), virtualMachineConfigSummary.getVmPathName());
        inputMap.put(ManagedObjectType.VM_IS_TEMPLATE.getValue(), Boolean.toString(virtualMachineConfigSummary.isTemplate()));

        if (virtualMachineSummary.getGuest() != null && virtualMachineSummary.getGuest().getIpAddress() != null) {
            inputMap.put(ManagedObjectType.VM_IP_ADDRESS.getValue(), virtualMachineSummary.getGuest().getIpAddress());
        } else if (virtualMachineSummary.getGuest() != null) {
            inputMap.put(ManagedObjectType.VM_IP_ADDRESS.getValue(), EMPTY);
        }
    }

    public static String getJsonString(Map<String, String> inputMap) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(inputMap);
    }
}
