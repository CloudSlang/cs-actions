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

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Created by TusaM
 * 5/9/2017.
 */
public enum NodesUri {
    FAIL_OVER_NODE("FailOverNode", "/controller/failOver"),
    GRACEFUL_FAIL_OVER_NODE("GracefulFailOverNode", "/controller/startGracefulFailover"),
    SET_RECOVERY_TYPE("SetRecoveryType", "/controller/setRecoveryType");

    private final String key;
    private final String value;

    NodesUri(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public static String getValue(String key) {
        for (NodesUri uri : NodesUri.values()) {
            if (uri.getKey().equalsIgnoreCase(key)) {
                return uri.getValue();
            }
        }

        return EMPTY;
    }

    private String getKey() {
        return key;
    }

    private String getValue() {
        return value;
    }
}