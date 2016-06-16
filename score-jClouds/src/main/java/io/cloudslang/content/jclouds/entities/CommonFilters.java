package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/15/2016.
 */
public enum CommonFilters {
    ARCHITECTURE("architecture"),
    HYPERVISOR("hypervisor"),
    KERNEL_ID("kernel-id");

    private String value;

    CommonFilters(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}
