package io.cloudslang.content.azure.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by victor on 28.09.2016.
 */
public class InputsValidation {

    @NotNull
    public static List<String> verifyRequiredInputs(@Nullable final String identifier, @Nullable final String primaryOrSecondaryKey, @Nullable final String expiry) {
        final List<String> exceptionMessages = new ArrayList<>();
        if (isBlank(identifier)) {
            exceptionMessages.add("The identifier can't be null or empty.");
        }
        if (isBlank(primaryOrSecondaryKey)) {
            exceptionMessages.add("The primaryOrSecondaryKey can't be null or empty.");
        }
        if (isBlank(expiry)) {
            exceptionMessages.add("The expiry can't be null or empty.");
        }
        return exceptionMessages;
    }
}
