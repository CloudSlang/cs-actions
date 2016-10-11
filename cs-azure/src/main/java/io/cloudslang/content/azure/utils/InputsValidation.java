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
    public static List<String> verifyRequiredInputs(@Nullable final String username, @Nullable final String password, @Nullable final String clientId) {
        final List<String> exceptionMessages = new ArrayList<>();
        if (isBlank(username)) {
            exceptionMessages.add("The username can't be null or empty.");
        }
        if (isBlank(password)) {
            exceptionMessages.add("The password can't be null or empty.");
        }
        if (isBlank(clientId)) {
            exceptionMessages.add("The clientId can't be null or empty.");
        }
        return exceptionMessages;
    }


}
