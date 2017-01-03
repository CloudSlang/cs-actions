/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.aws;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by TusaM
 * 11/1/2016.
 */
public enum BlockRootDeviceType {
    EBS("ebs"),
    INSTANCE_STORE("instance-store");

    private final String value;

    BlockRootDeviceType(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return NOT_RELEVANT;
        }

        for (BlockRootDeviceType member : BlockRootDeviceType.values()) {
            if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }
        throw new RuntimeException("Unrecognized block root device type value: [" + input + "]. Valid values are: ebs, instance-store.");
    }
}