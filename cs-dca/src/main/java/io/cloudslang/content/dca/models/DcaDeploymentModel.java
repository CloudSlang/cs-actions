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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class DcaDeploymentModel {
    private String name;
    private String description;
    private String templateId;
    private List<DcaResourceModel> resources;

    public DcaDeploymentModel() {
    }

    public DcaDeploymentModel(String name, String description, String templateId, List<DcaResourceModel> resources) {
        this.name = name;
        this.description = description;
        this.templateId = templateId;
        this.resources = resources;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public List<DcaResourceModel> getResources() {
        return resources;
    }

    public void setResources(List<DcaResourceModel> resources) {
        this.resources = resources;
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
