package io.cloudslang.content.vmware.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmware.vim25.GuestOsDescriptor;
import com.vmware.vim25.VirtualMachineConfigSummary;
import com.vmware.vim25.VirtualMachineSummary;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.Inputs;
import io.cloudslang.content.vmware.constants.Outputs;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 1/19/2016.
 */
public class ResponseUtils {
    private static final String VM_ID = "vmId";
    private static final String VM_FULL_NAME = "virtualMachineFullName";
    private static final String VM_UUID = "vmUuid";
    private static final String VM_ETH_COUNT = "numEths";
    private static final String VM_DISK_COUNT = "numDisks";
    private static final String VM_PATH_NAME = "vmPathName";
    private static final String VM_IS_TEMPLATE = "isTemplate";
    private static final String COMMA_DELIMITER = ",";

    public static void setResults(Map<String, String> results, String returnResultMessage, String returnCodeMessage) {
        results.put(Outputs.RETURN_RESULT, returnResultMessage);
        results.put(Outputs.RETURN_CODE, returnCodeMessage);
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

    public static Map<String, String> getVmDetailedMap(Map<String, String> inputMap,
                                                       VirtualMachineSummary virtualMachineSummary,
                                                       VirtualMachineConfigSummary virtualMachineConfigSummary) {
        inputMap.put(VM_ID, virtualMachineSummary.getVm().getValue());
        inputMap.put(VM_FULL_NAME, virtualMachineConfigSummary.getGuestFullName());
        inputMap.put(VM_UUID, virtualMachineConfigSummary.getUuid());
        inputMap.put(Inputs.VM_CPU_COUNT, virtualMachineConfigSummary.getNumCpu().toString());
        inputMap.put(Inputs.VM_MEMORY_SIZE, virtualMachineConfigSummary.getMemorySizeMB().toString());
        inputMap.put(VM_ETH_COUNT, virtualMachineConfigSummary.getNumEthernetCards().toString());
        inputMap.put(VM_DISK_COUNT, virtualMachineConfigSummary.getNumVirtualDisks().toString());
        inputMap.put(Inputs.DATA_STORE, virtualMachineConfigSummary.getVmPathName()
                .substring(1, virtualMachineConfigSummary.getVmPathName().indexOf(Constants.RIGHT_SQUARE_BRACKET)));
        inputMap.put(VM_PATH_NAME, virtualMachineConfigSummary.getVmPathName());
        inputMap.put(VM_IS_TEMPLATE, Boolean.toString(virtualMachineConfigSummary.isTemplate()));

        return inputMap;
    }

    public static String getJsonString(Map<String, String> inputMap) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(inputMap);
    }
}
