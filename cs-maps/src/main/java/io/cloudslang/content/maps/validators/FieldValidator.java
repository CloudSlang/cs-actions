package io.cloudslang.content.maps.validators;

import io.cloudslang.content.maps.constants.ExceptionsMsgs;
import io.cloudslang.content.maps.entities.AddKeyInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;

final class FieldValidator {
    
    private FieldValidator() {
        
    }
    
    public static void validateMap(String map, String mapStart, String mapEnd) throws ValidationException {
        if (StringUtils.isEmpty(map) &&
                (StringUtils.isNotEmpty(mapStart) || StringUtils.isNotEmpty(mapEnd))) {
            throw new ValidationException(ExceptionsMsgs.NO_MAP_PROVIDED);
        }

        if (!map.startsWith(mapStart)) {
            throw new ValidationException(ExceptionsMsgs.MAP_EXPECTED_TO_START_WITH_MAP_START);
        }

        if (!map.endsWith(mapEnd)) {
            throw new ValidationException(ExceptionsMsgs.MAP_EXPECTED_TO_END_WITH_MAP_END);
        }
    }

    public static void validateKey(String key, String pairDelimiter, String entryDelimiter) throws ValidationException {
        if (StringUtils.isBlank(key)) {
            throw new ValidationException(ExceptionsMsgs.NULL_KEY);
        }

        if (key.contains(pairDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.KEY_CANNOT_CONTAIN_PAIR_DELIMITER);
        }

        if (key.contains(entryDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.KEY_CANNOT_CONTAIN_ENTRY_DELIMITER);
        }
    }

    public static void validateValue(String value, String pairDelimiter, String entryDelimiter) throws ValidationException {
        if (value == null) {
            return;
        }

        if (value.contains(pairDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.VALUE_CANNOT_CONTAIN_PAIR_DELIMITER);
        }

        if (value.contains(entryDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.VALUE_CANNOT_CONTAIN_ENTRY_DELIMITER);
        }
    }

    public static void validatePairDelimiter(String pairDelimiter, String entryDelimiter) throws ValidationException {
        if (StringUtils.isEmpty(pairDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.NULL_OR_EMPTY_PAIR_DELIMITER);
        }

        if(pairDelimiter.contains(entryDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.PAIR_DELIMITER_CANNOT_CONTAIN_ENTRY_DELIMITER);
        }
    }

    public static void validateEntryDelimiter(String entryDelimiter, String pairDelimiter) throws ValidationException {
        if (StringUtils.isEmpty(entryDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.NULL_OR_EMPTY_ENTRY_DELIMITER);
        }

        if(entryDelimiter.equals(pairDelimiter)) {
            throw new ValidationException(ExceptionsMsgs.ENTRY_AND_PAIR_DELIMITER_MUST_BE_DISTINCT);
        }
    }
}
