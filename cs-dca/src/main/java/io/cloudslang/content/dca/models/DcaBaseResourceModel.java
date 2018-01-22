/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
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
