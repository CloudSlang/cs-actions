/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
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
public enum Device {
    CPU,
    MEMORY,
    DISK,
    CD,
    NIC;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unrecognized device: [" + input + "]. Valid values are: cpu, memory, disk, cd, nic.");
        }
    }
}
