/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.utils;

import io.cloudslang.content.vmware.constants.Constants;
import io.cloudslang.content.vmware.constants.ErrorMessages;
import io.cloudslang.content.vmware.entities.Operation;
import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;
import io.cloudslang.content.vmware.entities.http.Protocol;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.Locale;

import static io.cloudslang.content.utils.StringUtilities.isBlank;
import static org.apache.commons.lang3.LocaleUtils.isAvailableLocale;

/**
 * Created by Mihai Tusa.
 * 10/21/2015.
 */
public class InputUtils {
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
        if (Operation.REMOVE.toString().equalsIgnoreCase(vmInputs.getOperation()) &&
                Constants.EMPTY.equals(vmInputs.getUpdateValue())) {
            throw new RuntimeException("The [" + vmInputs.getUpdateValue() + "] is not a valid disk label.");
        }
    }

    public static int getIntInput(String input, int defaultValue) {
        int intInput;
        try {
            intInput = StringUtils.isBlank(input) ? defaultValue : Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(ErrorMessages.NOT_ZERO_OR_POSITIVE_NUMBER);
        }

        return intInput;
    }

    public static long getLongInput(String input, long defaultValue) {
        long longInput;
        try {
            longInput = StringUtils.isBlank(input) ? defaultValue : Long.parseLong(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(ErrorMessages.NOT_ZERO_OR_POSITIVE_NUMBER);
        }

        return longInput;
    }

    public static boolean getBooleanInput(String input, boolean defaultValue) {
        return StringUtils.isBlank(input) ? defaultValue : Boolean.parseBoolean(input);
    }

    public static boolean isInt(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    public static byte getByteInput(String input, byte defaultValue) {
        byte byteInput;
        try {
            byteInput = StringUtils.isBlank(input) ? defaultValue : Byte.parseByte(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(ErrorMessages.NOT_BYTE);
        }

        return byteInput;
    }

    public static String getDefaultDelimiter(String input, String defaultValue) {
        return StringUtils.isBlank(input) ? defaultValue : input;
    }

    private static boolean isValidUpdateOperation(VmInputs vmInputs) {
        return (Operation.ADD.toString().equalsIgnoreCase(vmInputs.getOperation()) ||
                Operation.REMOVE.toString().equalsIgnoreCase(vmInputs.getOperation()));
    }

    public static Locale getLocale(String localeLang, String localeCountry) throws Exception {
        Locale locale;
        if (StringUtils.isEmpty(localeLang) && StringUtils.isEmpty(localeCountry)) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale(localeLang, localeCountry);
            if (!isAvailableLocale(locale)) {
                throw new Exception("Locale not found!");
            }
        }
        return locale;
    }

    public static void checkMutuallyExclusiveInputs(final String input1, final String input2, final String exceptionMessage) {
        if (!(isBlank(input1) ^ isBlank(input2))) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
