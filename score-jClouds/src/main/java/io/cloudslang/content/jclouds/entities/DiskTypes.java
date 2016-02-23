package io.cloudslang.content.jclouds.entities;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 2/19/2016.
 */
public enum DiskTypes {
    GENERAL_PURPOSE_SSD("gp2"),
    PROVISIONED_IOPS_SSD("io1"),
    MAGNETIC("standard");

    private final String value;

    DiskTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getDiskType(String input) {
        if (StringUtils.isBlank(input)) {
            return GENERAL_PURPOSE_SSD.getValue().toLowerCase();
        }
        for (DiskTypes diskType : DiskTypes.values()) {
            if (diskType.getValue().toLowerCase().equals(input.toLowerCase())) {
                return diskType.getValue().toLowerCase();
            }
        }
        throw new IllegalArgumentException("Unrecognized disk type value: " + input + ". " +
                "Valid values are: general_purpose_ssd, provisioned_iops_ssd, standard.");
    }
}