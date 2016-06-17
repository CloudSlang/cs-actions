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
    ROOT_DEVICE_TYPE("root-device-type"),
    STATE_REASON_CODE("state-reason-code"),
    STATE_REASON_MESSAGE("state-reason-message"),
    VIRTUALIZATION_TYPE("virtualization-type");

    private String value;

    CommonFilters(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}
