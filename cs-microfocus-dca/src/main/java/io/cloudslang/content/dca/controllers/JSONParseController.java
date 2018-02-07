package io.cloudslang.content.dca.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;

import static io.cloudslang.content.dca.utils.Constants.VALUE;
import static io.cloudslang.content.dca.utils.OutputNames.DNS_NAME;
import static io.cloudslang.content.dca.utils.OutputNames.NAME;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class JSONParseController {
    @NotNull
    public static String getDnsNameFromArrayNode(@NotNull final JsonNode node) {
        if (node.isArray()) {
            for (final JsonNode attribute : node) {
                if (attribute.get(NAME).asText().equalsIgnoreCase(DNS_NAME)) {
                    return attribute.get(VALUE).asText();
                }
            }
        }
        return EMPTY;
    }
}
