package io.cloudslang.content.oracle.oci.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cloudslang.content.oracle.oci.services.models.instances.AttachVnicRequestBody;
import io.cloudslang.content.oracle.oci.services.models.instances.CreateInstanceRequestBody;
import io.cloudslang.content.oracle.oci.services.models.instances.UpdateInstanceRequestBody;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonObjectParserTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void parsesNestedJsonObjectsAndPreservesValues() throws Exception {
        String json = "{\"namespace\":{\"tag\":\"value\"},\"count\":2,\"enabled\":true,\"items\":[\"a\",\"b\"]}";

        ObjectNode result = JsonObjectParser.parse(objectMapper, json);

        assertEquals(objectMapper.readTree(json), result);
        assertEquals(objectMapper.readTree(json), objectMapper.readTree(objectMapper.writeValueAsString(result)));
    }

    @Test
    void emptyInputProducesAnEmptyObject() throws Exception {
        assertEquals(objectMapper.createObjectNode(), JsonObjectParser.parse(objectMapper, ""));
    }

    @Test
    void rejectsInvalidJsonAndNonObjectRoots() {
        assertThrows(JsonProcessingException.class, () -> JsonObjectParser.parse(objectMapper, "{\"broken\":"));
        assertThrows(JsonProcessingException.class, () -> JsonObjectParser.parse(objectMapper, "{} {}"));
        assertThrows(IllegalArgumentException.class, () -> JsonObjectParser.parse(objectMapper, "[1,2]"));
        assertThrows(IllegalArgumentException.class, () -> JsonObjectParser.parse(objectMapper, "null"));
    }

    @Test
    void requestBodiesSerializeJsonObjectsAsNestedValues() throws Exception {
        ObjectNode tags = JsonObjectParser.parse(objectMapper, "{\"namespace\":{\"tag\":\"value\"}}");
        ObjectNode metadata = JsonObjectParser.parse(objectMapper, "{\"count\":2,\"enabled\":true}");

        CreateInstanceRequestBody createBody = new CreateInstanceRequestBody();
        createBody.setDefinedTags(tags);
        createBody.setExtendedMetadata(metadata);
        CreateInstanceRequestBody.CreateVnicDetails createVnicDetails = createBody.new CreateVnicDetails();
        createVnicDetails.setFreeformTags(tags);
        createBody.setCreateVnicDetails(createVnicDetails);

        UpdateInstanceRequestBody updateBody = new UpdateInstanceRequestBody();
        updateBody.setDefinedTags(tags);
        updateBody.setFreeformTags(metadata);

        AttachVnicRequestBody attachBody = new AttachVnicRequestBody();
        AttachVnicRequestBody.CreateVnicDetails attachVnicDetails = attachBody.new CreateVnicDetails();
        attachVnicDetails.setDefinedTags(tags);
        attachBody.setCreateVnicDetails(attachVnicDetails);

        JsonNode createJson = objectMapper.readTree(objectMapper.writeValueAsString(createBody));
        JsonNode updateJson = objectMapper.readTree(objectMapper.writeValueAsString(updateBody));
        JsonNode attachJson = objectMapper.readTree(objectMapper.writeValueAsString(attachBody));

        assertEquals(tags, createJson.get("definedTags"));
        assertEquals(metadata, createJson.get("extendedMetadata"));
        assertEquals(tags, createJson.at("/createVnicDetails/freeformTags"));
        assertEquals(tags, updateJson.get("definedTags"));
        assertEquals(metadata, updateJson.get("freeformTags"));
        assertEquals(tags, attachJson.at("/createVnicDetails/definedTags"));

        CreateInstanceRequestBody emptyBody = new CreateInstanceRequestBody();
        emptyBody.setDefinedTags(objectMapper.createObjectNode());
        JsonNode emptyJson = objectMapper.readTree(objectMapper.writeValueAsString(emptyBody));
        assertFalse(emptyJson.has("definedTags"));
    }
}
