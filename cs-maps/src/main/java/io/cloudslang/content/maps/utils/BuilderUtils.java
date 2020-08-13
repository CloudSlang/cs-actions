package io.cloudslang.content.maps.utils;

import io.cloudslang.content.maps.constants.DefaultInputValues;
import io.cloudslang.content.maps.constants.ExceptionsMsgs;
import io.cloudslang.content.maps.constants.InputNames;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public final class BuilderUtils {
    private BuilderUtils () {

    }

    public static boolean parseStripWhitespaces(String stripWhitespaces) throws ValidationException {
        try {
            stripWhitespaces = StringUtils.defaultIfEmpty(stripWhitespaces, DefaultInputValues.STRIP_WHITESPACES);
            return BooleanUtils.toBoolean(stripWhitespaces.toLowerCase(), String.valueOf(true), String.valueOf(false));
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(String.format(ExceptionsMsgs.INVALID_VALUE_FOR_INPUT, InputNames.STRIP_WHITESPACES));
        }
    }
}
