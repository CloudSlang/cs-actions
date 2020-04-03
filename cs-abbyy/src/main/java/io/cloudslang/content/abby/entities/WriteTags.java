package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public enum WriteTags {
    AUTO("auto"),
    WRITE("write"),
    DONT_WRITE("dontWrite");

    private final String str;


    WriteTags(String str) {
        this.str = str;
    }


    @Override
    public String toString() {
        return this.str;
    }


    public static WriteTags fromString(String str) throws Exception {
        for (WriteTags wt : WriteTags.values()) {
            if (wt.name().equals(str)) {
                return wt;
            }
        }
        throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_WRITE_TAGS, str));
    }
}
