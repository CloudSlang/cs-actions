package io.cloudslang.content.vmware.entities;

/**
 * Created by Mihai Tusa.
 * 3/21/2016.
 */
public enum RebootOption {
    REBOOT("reboot"),
    NOREBOOT("noreboot"),
    SHUTDOWN("shutdown");

    private String value;

    /**
     * Retrieve reboot option.
     *
     * @param value the element value
     */
    RebootOption(String value) {
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