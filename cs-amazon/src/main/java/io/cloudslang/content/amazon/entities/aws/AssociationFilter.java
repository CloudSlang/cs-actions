package io.cloudslang.content.amazon.entities.aws;

/**
 * Created by Mihai Tusa.
 * 6/14/2016.
 */
public enum AssociationFilter {
    PUBLIC_IP("association.public-ip"),
    IP_OWNER_ID("association.ip-owner-id"),
    ALLOCATION_ID("association.allocation-id"),
    ASSOCIATION_ID("association.association-id");

    private String value;

    AssociationFilter(String input) {
        this.value = input;
    }

    public String getValue() {
        return value;
    }
}
