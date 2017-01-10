package io.cloudslang.content.database.utils;

import java.sql.ResultSet;

/**
 * Created by vranau on 1/21/2015.
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
