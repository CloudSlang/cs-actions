/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.dropbox.entities.dropbox;

/**
 * Created by TusaM
 * 5/30/2017.
 */
public enum DropboxApi {
    FILES("/files");

    private final String value;

    DropboxApi(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}