package io.cloudslang.content.dca.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class GetCredentialFromManagerController {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String KEY = "key";
    private static final String VALUE = "value";

    @NotNull
    public static String getUsernameFromDataArray(@NotNull final JsonNode dataArray) {
        return getValueFromDataArray(dataArray, USERNAME);
    }

    @NotNull
    public static String getPasswordFromDataArray(@NotNull final JsonNode dataArray) {
        return getValueFromDataArray(dataArray, PASSWORD);
    }

    @NotNull
    public static String getValueFromDataArray(@NotNull final JsonNode dataArray, @NotNull final String keyName) {
        if (dataArray.isArray()) {
            for (final JsonNode nodeElement : dataArray) {
                if (nodeElement.get(KEY).asText().equalsIgnoreCase(keyName)) {
                    return nodeElement.get(VALUE).asText(EMPTY);
                }
            }
        } else {
            return dataArray.get(keyName).asText(EMPTY);
        }
        return EMPTY;
    }
}
