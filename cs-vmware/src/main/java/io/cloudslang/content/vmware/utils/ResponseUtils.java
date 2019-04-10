

package io.cloudslang.content.vmware.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.vim25.GuestOsDescriptor;
import com.vmware.vim25.VirtualMachineConfigSummary;
import com.vmware.vim25.VirtualMachineSummary;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.Inputs;
import io.cloudslang.content.vmware.constants.Outputs;
import io.cloudslang.content.vmware.entities.ManagedObjectType;
import io.cloudslang.content.vmware.entities.VmInputs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 1/19/2016.
 */
public class ResponseUtils {
    private static final String COMMA_DELIMITER = ",";

    public static Map<String, String> getResultsMap(String returnResultMessage, String returnCodeMessage) {
        Map<String, String> results = new HashMap<>();
        results.put(Outputs.RETURN_RESULT, returnResultMessage);
        results.put(Outputs.RETURN_CODE, returnCodeMessage);

        return results;
    }

    public static Map<String, String> getVmNotFoundResultsMap(VmInputs vmInputs) {
        return getResultsMap("Could not find the [" + vmInputs.getVirtualMachineName() + "] VM.", Outputs.RETURN_CODE_FAILURE);
    }

    public static <T> String getResponseStringFromCollection(Collection<T> collectionItems, String delimiter) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (T item : collectionItems) {
            String itemName = item instanceof GuestOsDescriptor ? ((GuestOsDescriptor) item).getId() : (String) item;
            sb.append(itemName);
            if (index < collectionItems.size() - 1) {
                sb.append(InputUtils.getDefaultDelimiter(delimiter, COMMA_DELIMITER));
            }
            index++;
        }

        return sb.toString();
    }

    public static void addDataToVmDetailsMap(Map<String, String> inputMap,
                                             VirtualMachineSummary virtualMachineSummary,
                                             VirtualMachineConfigSummary virtualMachineConfigSummary) {
        inputMap.put(ManagedObjectType.VM_ID.getValue(), virtualMachineSummary.getVm().getValue());
        inputMap.put(ManagedObjectType.VM_FULL_NAME.getValue(), virtualMachineConfigSummary.getGuestFullName());
        inputMap.put(ManagedObjectType.VM_UUID.getValue(), virtualMachineConfigSummary.getUuid());
        inputMap.put(Inputs.VM_CPU_COUNT, virtualMachineConfigSummary.getNumCpu().toString());
        inputMap.put(Inputs.VM_MEMORY_SIZE, virtualMachineConfigSummary.getMemorySizeMB().toString());
        inputMap.put(ManagedObjectType.VM_ETH_COUNT.getValue(), virtualMachineConfigSummary.getNumEthernetCards().toString());
        inputMap.put(ManagedObjectType.VM_DISK_COUNT.getValue(), virtualMachineConfigSummary.getNumVirtualDisks().toString());
        inputMap.put(Inputs.DATA_STORE, virtualMachineConfigSummary.getVmPathName()
                .substring(1, virtualMachineConfigSummary.getVmPathName().indexOf(Constants.RIGHT_SQUARE_BRACKET)));
        inputMap.put(ManagedObjectType.VM_PATH_NAME.getValue(), virtualMachineConfigSummary.getVmPathName());
        inputMap.put(ManagedObjectType.VM_IS_TEMPLATE.getValue(), Boolean.toString(virtualMachineConfigSummary.isTemplate()));

        if (virtualMachineSummary.getGuest() != null) {
            if (virtualMachineSummary.getGuest().getIpAddress() != null) {
                inputMap.put(ManagedObjectType.VM_IP_ADDRESS.getValue(), virtualMachineSummary.getGuest().getIpAddress());
            } else {
                inputMap.put(ManagedObjectType.VM_IP_ADDRESS.getValue(), Constants.EMPTY);
            }
        }
    }

    public static String getJsonString(Map<String, String> inputMap) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(inputMap);
    }
}
