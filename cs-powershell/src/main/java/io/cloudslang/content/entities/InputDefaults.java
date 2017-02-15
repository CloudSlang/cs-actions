/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.entities;

/**
 * Created by giloan on 5/3/2016.
 */
public enum InputDefaults {
    PROTOCOL("https"),
    PORT("5986"),
    TRUST_ALL_ROOTS("false"),
    MAX_ENVELOPE_SIZE("153600"),
    X_509_HOSTNAME_VERIFIER("strict"),
    WINRM_LOCALE("en-US"),
    OPERATION_TIMEOUT("60"),
    AUTH_TYPE("Basic");

    private String defaultValue;

    /**
     * Instantiates a new input.
     *
     * @param defaultValue the default value of the input.
     */
    InputDefaults(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the default value.
     *
     * @return the default value.
     */
    public String getValue() {
        return defaultValue;
    }
}
