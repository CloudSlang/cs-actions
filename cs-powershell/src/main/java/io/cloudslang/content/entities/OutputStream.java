/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.entities;

/**
 * Created by giloan on 6/7/2016.
 */
public enum OutputStream {
    STDOUT("stdout"),
    STDERR("stderr");

    private String value;

    OutputStream(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
