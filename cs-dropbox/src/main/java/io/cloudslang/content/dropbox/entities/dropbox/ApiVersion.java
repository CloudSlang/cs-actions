/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.dropbox.entities.dropbox;

/**
 * Created by TusaM
 * 5/30/2017.
 */
public enum ApiVersion {
    NONE(""),
    ONE("/1"),
    TWO("/2");

    private final String value;

    ApiVersion(String value) {
        this.value = value;
    }

    public static String getApiVersion(String key) {
        for (ApiVersion version : ApiVersion.values()) {
            if (version.name().equalsIgnoreCase(key)) {
                return version.getValue();
            }
        }

        return TWO.getValue();
    }

    private String getValue() {
        return value;
    }
}