package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/15/2016.
 */
public enum CommonFilters {
    ARCHITECTURE("architecture"),
    HYPERVISOR("hypervisor"),
    KERNEL_ID("kernel-id"),
    OWNER_ALIAS("owner-alias"),
    OWNER_ID("owner-id"),
    PLATFORM("platform"),
    PRODUCT_CODE("product-code"),
    PRODUCT_CODE_TYPE("product-code.type"),
    RAMDISK_ID("ramdiskId"),
    ROOT_DEVICE_NAME("root-device-name"),
    ROOT_DEVICE_TYPE("root-device-type");

    private String value;

    CommonFilters(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}
