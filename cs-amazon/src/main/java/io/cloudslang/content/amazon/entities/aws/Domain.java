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

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 9/5/2016.
 */
public enum Domain {
    STANDARD,
    VPC;

    public static String getValue(String input) throws RuntimeException {
        if (isBlank(input)) {
            return STANDARD.name().toLowerCase();
        }

        for (Domain member : Domain.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Invalid Amazon domain: [" + input + "]. Valid values: standard, vpc.");
    }
}