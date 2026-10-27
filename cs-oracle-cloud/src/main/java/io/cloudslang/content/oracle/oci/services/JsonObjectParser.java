package io.cloudslang.content.oracle.oci.services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

final class JsonObjectParser {

    private JsonObjectParser() {
    }

    static ObjectNode parse(ObjectMapper objectMapper, String property) throws IOException {
        if (isEmpty(property)) {
            return objectMapper.createObjectNode();
        }

        JsonNode parsed = objectMapper.reader()
                .with(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
                .readTree(property);
        if (parsed == null || !parsed.isObject()) {
            throw new IllegalArgumentException("Expected a JSON object");
        }
        return (ObjectNode) parsed;
    }
}
