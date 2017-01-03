/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * Created by moldovas on 7/13/2016.
 */
public class InputsUtils {
    public static String getInputDefaultValue(String input, String defaultValue) {
        return (StringUtils.isEmpty(input)) ? defaultValue : input;
    }

    public static Boolean toBoolean(String value, Boolean defaultValue, String inputName) throws Exception {
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        switch (value.toLowerCase(Locale.ENGLISH)) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                throw new Exception(String.format(Constants.INPUT_NOT_BOOLEAN, inputName));
        }
    }
}
