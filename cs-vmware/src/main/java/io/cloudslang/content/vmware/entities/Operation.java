

package io.cloudslang.content.vmware.entities;

/**
 * Created by Mihai Tusa.
 * 1/22/2016.
 */
public enum Operation {
    CREATE,
    ADD,
    REMOVE,
    UPDATE;

    public static String getValue(String input) throws Exception {
        try {
            return valueOf(input.toUpperCase()).toString();
        } catch (IllegalArgumentException iae) {
            throw new RuntimeException("Unsupported operation value: [" + input + "]. Valid values are: " +
                    "create, add, remove, update.");
        }
    }
}
