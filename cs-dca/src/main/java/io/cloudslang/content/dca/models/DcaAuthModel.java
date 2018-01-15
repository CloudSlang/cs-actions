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
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DcaAuthModel {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final String tenantName;
    private final Map<String, String> passwordCredentials;

    public DcaAuthModel(String tenantName) {
        this.tenantName = tenantName;
        this.passwordCredentials = new HashMap<>();
    }

    public String getTenantName() {
        return tenantName;
    }

    public Map<String, String> getPasswordCredentials() {
        return passwordCredentials;
    }

    public void setCredentials(@NotNull final String username, @NotNull final String password) {
        passwordCredentials.put(USERNAME, username);
        passwordCredentials.put(PASSWORD, password);
    }

    public String toJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
