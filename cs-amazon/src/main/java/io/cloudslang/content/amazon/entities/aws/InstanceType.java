/*
 * (c) Copyright 2017 Hewlett-Packard Enterprise Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package io.cloudslang.content.amazon.entities.aws;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;

/**
 * Created by Mihai Tusa.
 * 6/30/2016.
 */
public enum InstanceType {
    // Actual Generation Instances
    // General purpose
    T2_NANO("t2.nano"),
    T2_MICRO("t2.micro"),
    T2_SMALL("t2.small"),
    T2_MEDIUM("t2.medium"),
    T2_LARGE("t2.large"),
    M4_LARGE("m4.large"),
    M4_XLARGE("m4.xlarge"),
    M4_2XLARGE("m4.2xlarge"),
    M4_4XLARGE("m4.4xlarge"),
    M4_10XLARGE("m4.10xlarge"),
    M3_MEDIUM("m3.medium"),
    M3_LARGE("m3.large"),
    M3_XLARGE("m3.xlarge"),
    M3_2XLARGE("m3.2xlarge"),

    // Compute optimized
    C4_LARGE("c4.large"),
    C4_XLARGE("c4.xlarge"),
    C4_2XLARGE("c4.2xlarge"),
    C4_4XLARGE("c4.4xlarge"),
    C4_8XLARGE("c4.8xlarge"),
    C3_LARGE("c3.large"),
    C3_XLARGE("c3.xlarge"),
    C3_2XLARGE("c3.2xlarge"),
    C3_4XLARGE("c3.4xlarge"),
    C3_8XLARGE("c3.8xlarge"),

    // Memory optimized
    R3_LARGE("r3.large"),
    R3_XLARGE("r3.xlarge"),
    R3_2XLARGE("r3.2xlarge"),
    R3_4XLARGE("r3.4xlarge"),
    R3_8XLARGE("r3.8xlarge"),

    // Storage optimized
    I2_XLARGE("i2.xlarge"),
    I2_2XLARGE("i2.2xlarge"),
    I2_4XLARGE("i2.4xlarge"),
    I2_8XLARGE("i2.8xlarge"),
    D2_XLARGE("d2.xlarge"),
    D2_2XLARGE("d2.2xlarge"),
    D2_4XLARGE("d2.4xlarge"),
    D2_8XLARGE("d2.8xlarge"),

    // GPU instances
    G2_2XLARGE("g2.2xlarge"),
    G2_8XLARGE("g2.8xlarge"),

    // Previous Generation Instances
    // General purpose
    M1_SMALL("m1.small"),
    M1_MEDIUM("m1.medium"),
    M1_LARGE("m1.large"),
    M1_XLARGE("m1.xlarge"),

    // Compute optimized
    C1_MEDIUM("c1.medium"),
    C1_XLARGE("c1.xlarge"),
    CC2_8XLARGE("Cc2.8xlarge"),

    // Memory optimized
    M2_XLARGE("m2.xlarge"),
    M2_2XLARGE("m2.2xlarge"),
    M2_4XLARGE("m2.4xlarge"),
    CR1_8XLARGE("cr1.8xlarge"),

    // Storage optimized
    HI1_4XLARGE("hi1.4xlarge"),
    HS1_8XLARGE("hs1.8xlarge"),

    // GPU instances
    CG1_4XLARGE("cg1.4xlarge"),

    // Micro instances
    T1_MICRO("t1.micro");

    private final String value;

    InstanceType(String value) {
        this.value = value;
    }

    private String getValue() {
        return value;
    }

    public static String getInstanceType(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }
        for (InstanceType type : InstanceType.values()) {
            if (type.getValue().equals(input.toLowerCase())) {
                return type.getValue();
            }
        }
        throw new IllegalArgumentException("Invalid instance type value: [" + input + "]. Valid values: t1.micro | " +
                "t2.nano | t2.micro | t2.small | t2.medium | t2.large | m1.small | m1.medium | m1.large | m1.xlarge | " +
                "m3.medium | m3.large | m3.xlarge | m3.2xlarge | m4.large | m4.xlarge | m4.2xlarge | m4.4xlarge | " +
                "m4.10xlarge | m2.xlarge | m2.2xlarge | m2.4xlarge | cr1.8xlarge | r3.large | r3.xlarge | r3.2xlarge | " +
                "r3.4xlarge | r3.8xlarge | x1.4xlarge | x1.8xlarge | x1.16xlarge | x1.32xlarge | i2.xlarge | i2.2xlarge | " +
                "i2.4xlarge | i2.8xlarge | hi1.4xlarge | hs1.8xlarge | c1.medium | c1.xlarge | c3.large | c3.xlarge | " +
                "c3.2xlarge | c3.4xlarge | c3.8xlarge | c4.large | c4.xlarge | c4.2xlarge | c4.4xlarge | c4.8xlarge | " +
                "cc1.4xlarge | cc2.8xlarge | g2.2xlarge | g2.8xlarge | cg1.4xlarge | d2.xlarge | d2.2xlarge | d2.4xlarge | " +
                "d2.8xlarge");
    }
}
