package io.cloudslang.content.jclouds.entities;

/**
 * Created by Mihai Tusa.
 * 6/8/2016.
 */
public enum NetworkInterfaceStatus {
    AVAILABLE("available"),
    IN_USE("in-use");

    private String value;

    NetworkInterfaceStatus(String value) {
        this.value = value;
    }

    public static String getValue(String input) {
        for (NetworkInterfaceStatus member : NetworkInterfaceStatus.values()) {
            if (member.value.equals(input.toLowerCase())) {
                return member.value;
            }
        }
        throw new RuntimeException("Unrecognized networkInterfaceStatus value: [" + input + "]. Valid values are: available, in-use.");
    }
}
