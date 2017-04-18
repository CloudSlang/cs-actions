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

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by TusaM
 * 4/14/2017.
 */
public enum BucketType {
    COUCHBASE("couchbase"),
    MEMCACHED("membase");

    private final String value;

    BucketType(String value) {
        this.value = value;
    }

    public static String getBucketType(String input) throws RuntimeException {
        if (isBlank(input)) {
            return MEMCACHED.getValue();
        }

        for (BucketType bucket : BucketType.values()) {
            if (bucket.getValue().equalsIgnoreCase(input)) {
                return bucket.getValue();
            }
        }

        throw new RuntimeException("Invalid Couchbase bucket type value: [" + input + "]. Valid values: couchbase, memcached.");
    }

    private String getValue() {
        return value;
    }
}