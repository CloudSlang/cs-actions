package io.cloudslang.content.vmware.services.helpers;

import com.vmware.vim25.GuestOsDescriptor;
import com.vmware.vim25.VirtualMachineConfigSummary;
import com.vmware.vim25.VirtualMachineSummary;
import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.Inputs;
import io.cloudslang.content.vmware.utils.InputUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Mihai Tusa.
 * 1/19/2016.
 */
public class VmUtils {

    public static <T> String getResponseStringFromCollection(Collection<T> collectionItems, String delimiter) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (T item : collectionItems) {
            String itemName = item instanceof GuestOsDescriptor ? ((GuestOsDescriptor) item).getId() : (String) item;
            sb.append(itemName);
            if (index < collectionItems.size() - 1) {
                sb.append(InputUtils.getDefaultDelimiter(delimiter, Constants.COMMA_DELIMITER));
            }
            index++;
        }

        return sb.toString();
    }

    public static Map<String, String> getVmDetailedMap(Map<String, String> inputMap,
                                                       VirtualMachineSummary virtualMachineSummary,
                                                       VirtualMachineConfigSummary virtualMachineConfigSummary) {
        inputMap.put(Inputs.VM_ID, virtualMachineSummary.getVm().getValue());
        inputMap.put(Inputs.VM_FULL_NAME, virtualMachineConfigSummary.getGuestFullName());
        inputMap.put(Inputs.VM_UUID, virtualMachineConfigSummary.getUuid());
        inputMap.put(Inputs.VM_CPU_COUNT, virtualMachineConfigSummary.getNumCpu().toString());
        inputMap.put(Inputs.VM_MEMORY_SIZE, virtualMachineConfigSummary.getMemorySizeMB().toString());
        inputMap.put(Inputs.VM_ETH_COUNT, virtualMachineConfigSummary.getNumEthernetCards().toString());
        inputMap.put(Inputs.VM_DISK_COUNT, virtualMachineConfigSummary.getNumVirtualDisks().toString());
        inputMap.put(Inputs.DATA_STORE, virtualMachineConfigSummary.getVmPathName()
                .substring(1, virtualMachineConfigSummary.getVmPathName().indexOf(Constants.RIGHT_SQUARE_BRACKET)));
        inputMap.put(Inputs.VM_PATH_NAME, virtualMachineConfigSummary.getVmPathName());
        inputMap.put(Inputs.VM_IS_TEMPLATE, Boolean.toString(virtualMachineConfigSummary.isTemplate()));

        return inputMap;
    }
}
