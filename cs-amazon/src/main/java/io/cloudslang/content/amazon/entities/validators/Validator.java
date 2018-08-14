package io.cloudslang.content.amazon.entities.validators;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static io.cloudslang.content.utils.BooleanUtilities.isValid;
import static io.cloudslang.content.utils.NumberUtilities.isValidInt;
import static io.cloudslang.content.utils.OtherUtilities.isValidIpPort;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.join;

public class Validator {
    private final List<String> errorList;

    public Validator() {
        errorList = new ArrayList<>();
    }

    public boolean hasErrors() {
        return !errorList.isEmpty();
    }

    public String getErrors() {
        return getErrors(System.lineSeparator());
    }

    public String getErrors(@NotNull final String delimiter) {
        return join(errorList, delimiter);
    }

    public Validator validatePort(@NotNull final String value, @NotNull final String inputName) {
        if (!isValidIpPort(value)) {
            errorList.add(format("[%s]: Invalid port value [%s]!", inputName, value));
        }
        return this;
    }

    public Validator validateInt(@NotNull final String intValue, @NotNull final String inputName) {
        if (!isValidInt(intValue)) {
            errorList.add(format("[%s]: Invalid integer value.", inputName));
        }
        return this;
    }

    public Validator validateBoolean(@NotNull final String booleanValue, @NotNull final String inputName) {
        if (!isValid(booleanValue)) {
            errorList.add(format("[%s]: Invalid boolean value!", inputName));
        }
        return this;

    }
}
