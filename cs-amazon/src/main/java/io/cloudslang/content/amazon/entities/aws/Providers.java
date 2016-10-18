package io.cloudslang.content.amazon.entities.aws;

/**
 * Created by persdana on 7/14/2015.
 */
public enum Providers {
    OPENSTACK,
    AMAZON;

    public static String getValue(String input) throws Exception {
        for (Providers member : Providers.values()) {
            if (member.name().equalsIgnoreCase(input)) {
                return member.name().toLowerCase();
            }
        }

        throw new RuntimeException("Unrecognized provider value: [" + input + "]. Valid values are: openstack, amazon.");
    }
}