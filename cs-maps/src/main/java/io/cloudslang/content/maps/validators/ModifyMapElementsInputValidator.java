package io.cloudslang.content.maps.validators;

import io.cloudslang.content.maps.entities.ModifyMapElementsInput;
import io.cloudslang.content.maps.exceptions.ValidationException;
import org.jetbrains.annotations.NotNull;

public class ModifyMapElementsInputValidator {

    public ValidationException validate(@NotNull ModifyMapElementsInput input) {
        try {
            FieldValidator.validateMap(input.getMap(), input.getMapStart(), input.getMapEnd());
            FieldValidator.validateMethod(input.getMethod());
            FieldValidator.validateElements(input.getElements());
            FieldValidator.validatePairDelimiter(input.getPairDelimiter(), input.getEntryDelimiter());
            FieldValidator.validateEntryDelimiter(input.getEntryDelimiter(), input.getPairDelimiter());
            FieldValidator.validateElementWrapper(input.getElementWrapper(), input.getPairDelimiter(), input.getEntryDelimiter());
            return null;
        } catch (ValidationException ex) {
            return ex;
        }
    }
}
