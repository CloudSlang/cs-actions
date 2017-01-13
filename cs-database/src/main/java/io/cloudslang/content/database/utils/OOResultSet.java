/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.database.utils;

import java.sql.ResultSet;

/**
 * Created by victor on 13.01.2017.
 */
public enum OOResultSet {

    CONCUR_READ_ONLY(ResultSet.CONCUR_READ_ONLY, "CONCUR_READ_ONLY"),
    CONCUR_UPDATABLE(ResultSet.CONCUR_UPDATABLE, "CONCUR_UPDATABLE"),
    TYPE_FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY, "TYPE_FORWARD_ONLY"),
    TYPE_SCROLL_INSENSITIVE(ResultSet.TYPE_FORWARD_ONLY, "TYPE_SCROLL_INSENSITIVE"),
    TYPE_SCROLL_SENSITIVE(ResultSet.TYPE_FORWARD_ONLY, "TYPE_SCROLL_SENSITIVE"),
    NO_RESULT_SET(-1000000, "NO_RESULT_SET");
    private final int value;
    private final String toString;

    private OOResultSet(int value, String toString) {
        this.value = value;
        this.toString = toString;
    }

    public int getValue() {
        return value;
    }

    public String toString() {
        return toString;
    }
}
