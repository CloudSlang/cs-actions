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

import io.cloudslang.content.vmware.entities.VmInputs;
import io.cloudslang.content.vmware.entities.http.HttpInputs;

import java.net.URL;
import java.util.Locale;

import static io.cloudslang.content.utils.StringUtilities.isBlank;
import static io.cloudslang.content.vmware.constants.ErrorMessages.INVALID_VM_DISK_SIZE;
import static io.cloudslang.content.vmware.constants.ErrorMessages.NOT_ZERO_OR_POSITIVE_NUMBER;
import static io.cloudslang.content.vmware.entities.Operation.ADD;
import static io.cloudslang.content.vmware.entities.Operation.REMOVE;
import static io.cloudslang.content.vmware.entities.Operation.UPDATE;
import static io.cloudslang.content.vmware.entities.http.Protocol.getValue;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static org.apache.commons.lang3.LocaleUtils.isAvailableLocale;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


/**
 * Created by Mihai Tusa.
 * 10/21/2015.
 */
public class InputUtils {
    private static final String URI_PATH = "/sdk";
    private static final String VMDK_SUFFIX = ".vmdk";

    public static String getUrlString(HttpInputs httpInputs) throws Exception {
        String protocolString = getValue(httpInputs.getProtocol());
        String urlString = protocolString + "://" + httpInputs.getHost() + ":" + httpInputs.getPort() + URI_PATH;
        URL url = new URL(urlString.toLowerCase());

        return url.toString();
    }

    public static String getDiskFileNameString(String dataStoreName, String vmName, String updateValue) {
        return format("[%s] " + vmName + "/" + updateValue + VMDK_SUFFIX, dataStoreName);
    }

    public static boolean isUpdateOperation(VmInputs vmInputs) {
        return UPDATE.toString().equalsIgnoreCase(vmInputs.getOperation());
    }

    public static void checkValidOperation(VmInputs vmInputs, String device) {
        if (!isValidUpdateOperation(vmInputs)) {
            throw new RuntimeException(format("Invalid operation specified for %s device. This device can be only added or removed.", device));
        }
    }

    public static void validateDiskInputs(VmInputs vmInputs) {
        if (ADD.toString().equalsIgnoreCase(vmInputs.getOperation()) && vmInputs.getLongVmDiskSize() <= 0L) {
            throw new RuntimeException(INVALID_VM_DISK_SIZE);
        }
        if (REMOVE.toString().equalsIgnoreCase(vmInputs.getOperation()) && EMPTY.equals(vmInputs.getUpdateValue())) {
            throw new RuntimeException(format("The [%s] is not a valid disk label.", vmInputs.getUpdateValue()));
        }
    }

    public static int getIntInput(String input, int defaultValue) {
        int intInput;
        try {
            intInput = isBlank(input) ? defaultValue : parseInt(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(NOT_ZERO_OR_POSITIVE_NUMBER);
        }

        return intInput;
    }

    public static long getLongInput(String input, long defaultValue) {
        long longInput;
        try {
            longInput = isBlank(input) ? defaultValue : parseLong(input);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException(NOT_ZERO_OR_POSITIVE_NUMBER);
        }

        return longInput;
    }

    public static boolean getBooleanInput(String input, boolean defaultValue) {
        return isBlank(input) ? defaultValue : parseBoolean(input);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean isInt(String input) {
        try {
            parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }

        return true;
    }

    public static String getDefaultDelimiter(String input, String defaultValue) {
        return isBlank(input) ? defaultValue : input;
    }

    public static Locale getLocale(String localeLang, String localeCountry) throws Exception {
        Locale locale;
        if (isBlank(localeLang) && isBlank(localeCountry)) {
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
        if (isBlank(input1) == isBlank(input2)) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    public static void checkOptionalMutuallyExclusiveInputs(final String input1, final String input2, final String exceptionMessage) {
        if (isNotBlank(input1) && isNotBlank(input2)) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    private static boolean isValidUpdateOperation(VmInputs vmInputs) {
        return (ADD.toString().equalsIgnoreCase(vmInputs.getOperation()) || REMOVE.toString().equalsIgnoreCase(vmInputs.getOperation()));
    }
}
