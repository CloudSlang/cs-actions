package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/3/2016.
 */
public enum Hypervisor {
    OVM,
    XEN;

    public static String getValue(String input) throws RuntimeException {
        try {
            return Hypervisor.valueOf(input.toUpperCase()).toString().toLowerCase();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Invalid hypervisor value: [" + input + "]. Valid values: ovm, xen.");
        }
    }
}
