package io.cloudslang.content.dca.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DcaBaseResourceModel {
    private String resourceUuid;
    private String resourceCiType;
    private String typeUuid;

    public DcaBaseResourceModel() {
    }

    public DcaBaseResourceModel(String resourceUuid, String resourceCiType, String typeUuid) {
        this.resourceUuid = resourceUuid;
        this.resourceCiType = resourceCiType;
        this.typeUuid = typeUuid;
    }

    public String getResourceUuid() {
        return resourceUuid;
    }

    public void setResourceUuid(String resourceUuid) {
        this.resourceUuid = resourceUuid;
    }

    public String getResourceCiType() {
        return resourceCiType;
    }

    public void setResourceCiType(String resourceCiType) {
        this.resourceCiType = resourceCiType;
    }

    @JsonProperty("TypeUuid")
    public String getTypeUuid() {
        return typeUuid;
    }

    public void setTypeUuid(String typeUuid) {
        this.typeUuid = typeUuid;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
