package io.cloudslang.content.maps.validators;

import io.cloudslang.content.maps.constants.ExceptionsMsgs;
import io.cloudslang.content.maps.entities.AddKeyInput;
import io.cloudslang.content.maps.entities.MergeMapsInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class MergeMapsInputValidator {

    public ValidationException validate(@NotNull MergeMapsInput input) {
        try {
            validateMap(input,input.getMap1());
            validateMap(input,input.getMap2());
            validatePairDelimiter(input);
            validateEntryDelimiter(input);
            validateMapStart(input);
            validateMapEnd(input);
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }


    private void validateMap(MergeMapsInput input,String map) throws ValidationException {
        if (StringUtils.isBlank(map)) {
            throw new ValidationException(ExceptionsMsgs.NO_MAP_PROVIDED);
        }

        if (!map.startsWith(input.getMapStart())) {
            throw new ValidationException(ExceptionsMsgs.MAP_EXPECTED_TO_START_WITH_MAP_START);
        }

        if (!map.endsWith(input.getMapEnd())) {
            throw new ValidationException(ExceptionsMsgs.MAP_EXPECTED_TO_END_WITH_MAP_END);
        }

        if (map.length() < input.getMapStart().length() + input.getMapEnd().length()) {
            throw new ValidationException(ExceptionsMsgs.INVALID_MAP);
        }
    }


//    private void validateKey(MergeMapsInput input) throws ValidationException {
//        if (input.getKey() == null) {
//            return;
//        }
//
//        if (input.getKey().contains(input.getPairDelimiter())) {
//            throw new ValidationException(ExceptionsMsgs.KEY_CANNOT_CONTAIN_PAIR_DELIMITER);
//        }
//
//        if (input.getKey().contains(input.getEntryDelimiter())) {
//            throw new ValidationException(ExceptionsMsgs.KEY_CANNOT_CONTAIN_ENTRY_DELIMITER);
//        }
//    }


//    private void validateValue(MergeMapsInput input) throws ValidationException {
//        if (input.getValue() == null) {
//            return;
//        }
//
//        if (input.getValue().contains(input.getPairDelimiter())) {
//            throw new ValidationException(ExceptionsMsgs.VALUE_CANNOT_CONTAIN_PAIR_DELIMITER);
//        }
//
//        if (input.getValue().contains(input.getEntryDelimiter())) {
//            throw new ValidationException(ExceptionsMsgs.VALUE_CANNOT_CONTAIN_ENTRY_DELIMITER);
//        }
//    }


    private void validatePairDelimiter(MergeMapsInput input) throws ValidationException {
        if (StringUtils.isEmpty(input.getPairDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.NULL_OR_EMPTY_PAIR_DELIMITER);
        }

        if(input.getPairDelimiter().contains(input.getEntryDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.PAIR_DELIMITER_CANNOT_CONTAIN_ENTRY_DELIMITER);
        }
    }


    private void validateEntryDelimiter(MergeMapsInput input) throws ValidationException {
        if (StringUtils.isEmpty(input.getEntryDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.NULL_OR_EMPTY_ENTRY_DELIMITER);
        }

        if(input.getEntryDelimiter().equals(input.getPairDelimiter())) {
            throw new ValidationException(ExceptionsMsgs.ENTRY_AND_PAIR_DELIMITER_MUST_BE_DISTINCT);
        }
    }


    private void validateMapStart(MergeMapsInput input) throws ValidationException {
        if (input.getMapStart() == null) {
            throw new ValidationException(ExceptionsMsgs.NULL_MAP_START);
        }
    }


    private void validateMapEnd(MergeMapsInput input) throws ValidationException {
        if (input.getMapEnd() == null) {
            throw new ValidationException(ExceptionsMsgs.NULL_MAP_END);
        }
    }
}
