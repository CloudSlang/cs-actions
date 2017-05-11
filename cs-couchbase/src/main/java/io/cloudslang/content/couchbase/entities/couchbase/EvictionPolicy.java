/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.couchbase.entities.couchbase;

import static io.cloudslang.content.couchbase.utils.InputsUtil.getEnumValidValuesString;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by TusaM
 * 4/14/2017.
 */
public enum EvictionPolicy {
    FULL_EVICTION("fullEviction"),
    VALUE_ONLY("valueOnly");

    private final String value;

    EvictionPolicy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static String getValue(String input) {
        if (isBlank(input)) {
            return VALUE_ONLY.getValue();
        }

        for (EvictionPolicy policy : EvictionPolicy.values()) {
            if (policy.getValue().equalsIgnoreCase(input)) {
                return policy.getValue();
            }
        }

        throw new RuntimeException(format("Invalid Couchbase eviction policy value: '%s'. Valid values: '%s'.",
                input, getEnumValidValuesString(EvictionPolicy.class)));
    }
}