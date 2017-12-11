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

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by TusaM
 * 5/30/2017.
 */
public enum DropboxApi {
    FILES("Files", "/files");

    private final String key;
    private final String value;

    DropboxApi(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getApi(String input) {
        for (DropboxApi api : values()) {
            if (api.getKey().equalsIgnoreCase(input)) {
                return api.getValue();
            }
        }

        return EMPTY;
    }

    private String getValue() {
        return value;
    }

    private String getKey() {
        return key;
    }
}