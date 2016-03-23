package io.cloudslang.content.vmware.entities;

/**
 * Created by Mihai Tusa.
 * 3/22/2016.
 */
public enum LicenseDataMode {
    PERSERVER("perServer"),
    PERSEAT("perSeat");

    private String value;

    /**
     * Retrieve license data mode.
     *
     * @param value the element value
     */
    LicenseDataMode(String value) {
        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return value
     */
    public String getValue() {
        return value;
    }
}