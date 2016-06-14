package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/14/2016.
 */
public enum Association {
    PUBLIC_IP("association.public-ip"),
    IP_OWNER_ID("association.ip-owner-id"),
    ALLOCATION_ID("association.allocation-id"),
    ASSOCIATION_ID("association.association-id");

    private String value;

    Association(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}
