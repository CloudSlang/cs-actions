package io.cloudslang.content.utils;

import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;

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
        } else if (!NumberUtilities.isValidInt(expiry)) {
            exceptionMessages.add("The expiry has to be a number less than 2^31-1 that signifies the number of seconds that the token will be valid.");
        }
        return exceptionMessages;
    }
}
