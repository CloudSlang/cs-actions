package io.cloudslang.content.json.validators;

import io.cloudslang.content.json.entities.AddToArrayInput;
import io.cloudslang.content.json.utils.Constants;
import io.cloudslang.content.json.utils.ExceptionMsgs;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AddToArrayValidator {
    public @NotNull List<RuntimeException> validate(@NotNull AddToArrayInput input) {
        List<RuntimeException> errs = new ArrayList<>();

        addErrsForArray(errs, input);
        addErrsForIndex(errs, input);

        return errs;
    }


    private void addErrsForArray(List<RuntimeException> errs, AddToArrayInput input) {
        if (input.getArray() == null) {
            String msg = String.format(ExceptionMsgs.NULL_INPUT, Constants.InputNames.ARRAY);
            errs.add(new IllegalArgumentException(msg));
        }
    }


    private void addErrsForIndex(List<RuntimeException> errs, AddToArrayInput input) {
        if (input.getArray() == null || input.getIndex() == null) {
            return;
        }

        if (input.getIndex() < -input.getArray().size() || input.getIndex() > input.getArray().size()) {
            errs.add(new IndexOutOfBoundsException());
        }
    }
}
