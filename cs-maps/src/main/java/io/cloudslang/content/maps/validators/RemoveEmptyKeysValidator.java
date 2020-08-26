package io.cloudslang.content.maps.validators;

import io.cloudslang.content.maps.entities.RemoveEmptyKeysInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.jetbrains.annotations.NotNull;

public class RemoveEmptyKeysValidator {

    public ValidationException validate(@NotNull RemoveEmptyKeysInput input) {
        try {
            FieldValidator.validatePairDelimiter(input.getPairDelimiter(), input.getEntryDelimiter());
            FieldValidator.validateEntryDelimiter(input.getEntryDelimiter(), input.getPairDelimiter());
            FieldValidator.validateElementWrapper(input.getElementWrapper(), input.getPairDelimiter(), input.getEntryDelimiter());
            FieldValidator.validateMap(input.getMap(), input.getMapStart(), input.getMapEnd());
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }
}
