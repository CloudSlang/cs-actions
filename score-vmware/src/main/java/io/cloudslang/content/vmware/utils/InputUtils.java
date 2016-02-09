package io.cloudslang.content.vmware.utils;

import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.Operation;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.entities.http.Protocol;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * Created by Mihai Tusa.
 * 10/21/2015.
 */
public class InputUtils {
    private static final String NOT_INTEGER = "The input value must be a int number.";
    private static final String NOT_LONG = "The input value must be a long number.";
    private static final String URI_PATH = "/sdk";
    private static final String VMDK_SUFFIX = ".vmdk";

    public static String getUrlString(HttpInputs httpInputs) throws Exception {
        String protocolString = Protocol.getValue(httpInputs.getProtocol());
        String urlString = protocolString + "://" + httpInputs.getHost() + ":" + httpInputs.getPort() + URI_PATH;
        URL url = new URL(urlString.toLowerCase());

        return url.toString();
    }

    public static String getDiskFileNameString(String dataStoreName, String vmName, String updateValue) {
        return "[" + dataStoreName + "] " + vmName + "/" + updateValue + VMDK_SUFFIX;
    }

    public static boolean isUpdateOperation(VmInputs vmInputs) {
        return Operation.UPDATE.toString().equalsIgnoreCase(vmInputs.getOperation());
    }

    public static boolean isValidUpdateOperation(VmInputs vmInputs) {
        return (Operation.ADD.toString().equalsIgnoreCase(vmInputs.getOperation())
                || Operation.REMOVE.toString().equalsIgnoreCase(vmInputs.getOperation()));
    }

    public static void checkValidOperation(VmInputs vmInputs, String device) {
        if (!InputUtils.isValidUpdateOperation(vmInputs)) {
            throw new RuntimeException("Invalid operation specified for " + device + " device. " +
                    "The " + device + " device can be only added or removed.");
        }
    }

    public static void validateDiskInputs(VmInputs vmInputs) {
        if (Operation.ADD.toString().equalsIgnoreCase(vmInputs.getOperation()) && vmInputs.getLongVmDiskSize() <= 0L) {
            throw new RuntimeException(ErrorMessages.INVALID_VM_DISK_SIZE);
        }
        if (Operation.REMOVE.toString().equalsIgnoreCase(vmInputs.getOperation())
                && Constants.EMPTY.equals(vmInputs.getUpdateValue())) {
            throw new RuntimeException("The [" + vmInputs.getUpdateValue() + "] is not a valid disk label.");
        }
    }

    public static int getIntInput(String input, int defaultValue) {
        int intInput;
        try {
            intInput = StringUtils.isBlank(input) ? defaultValue : Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(NOT_INTEGER);
        }

        return intInput;
    }

    public static long getLongInput(String input, long defaultValue) {
        long longInput;
        try {
            longInput = StringUtils.isBlank(input) ? defaultValue : Long.parseLong(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(NOT_LONG);
        }

        return longInput;
    }

    public static String getDefaultDelimiter(String input, String defaultValue) {
        return StringUtils.isBlank(input) ? defaultValue : input;
    }

    public static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }
}