/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.entities;

/**
 * Created by Mihai Tusa.
 * 1/22/2016.
 */
public enum DiskMode {
    PERSISTENT,
    INDEPENDENT_PERSISTENT,
    INDEPENDENT_NONPERSISTENT;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized disk mode value: [" + input + "]. " +
                    "Valid values are: persistent, independent_persistent, independent_nonpersistent.");
        }
    }
}
