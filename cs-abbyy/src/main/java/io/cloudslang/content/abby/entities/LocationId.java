package io.cloudslang.content.abby.entities;

import io.cloudslang.content.abby.constants.ExceptionMsgs;

public enum LocationId {
    EU("cloud-eu"),
    WEST_US("cloud-westus");

    private final String str;


    LocationId(String str) {
        this.str = str;
    }


    public static LocationId fromString(String str) throws Exception {
        for (LocationId id : LocationId.values()) {
            if (id.name().equals(str)) {
                return id;
            }
        }
        throw new IllegalArgumentException(String.format(ExceptionMsgs.INVALID_LOCATION_ID, str));
    }
}
