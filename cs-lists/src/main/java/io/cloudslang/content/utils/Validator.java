package io.cloudslang.content.utils;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static io.cloudslang.content.utils.Constants.EMPTY_STRING;
import static io.cloudslang.content.utils.Constants.METHOD_LIST;
import static io.cloudslang.content.utils.Constants.WHITESPACES;

public final class Validator {

    private Validator() {

    }

    public static boolean parseStripWhitespaces(String stripWhitespaces) throws Exception {
        try {
            stripWhitespaces = StringUtils.defaultIfEmpty(stripWhitespaces, DefaultInputValues.STRIP_WHITESPACES);
            return BooleanUtils.toBoolean(stripWhitespaces.toLowerCase(), String.valueOf(true), String.valueOf(false));
        } catch (IllegalArgumentException ex) {
            throw new Exception(String.format(ListExceptions.INVALID_VALUE_FOR_INPUT, InputNames.CS_STRIP_WHITESPACES));
        }
    }

    public static String parseMethod(String method) throws Exception {

        method = StringUtils.defaultIfEmpty(method, DefaultInputValues.METHOD);
        if (Arrays.asList(METHOD_LIST).contains(method.toLowerCase().replaceAll(WHITESPACES, EMPTY_STRING)))
            return method.toLowerCase().replaceAll(WHITESPACES, EMPTY_STRING);
        else
            throw new Exception(String.format(ListExceptions.INVALID_VALUE_FOR_INPUT, InputNames.METHOD));

    }
}
