package io.cloudslang.content.entities;

/**
 * Created by giloan on 5/3/2016.
 */
public enum InputDefaults {
    PROTOCOL("https"),
    PORT("5986"),
    TRUST_ALL_ROOTS("false"),
    MAX_ENVELOPE_SIZE("153600"),
    X_509_HOSTNAME_VERIFIER("strict"),
    WINRM_LOCALE("en-US"),
    OPERATION_TIMEOUT("60");

    private String defaultValue;

    /**
     * Instantiates a new input.
     *
     * @param defaultValue the default value of the input.
     */
    InputDefaults(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the default value.
     *
     * @return the default value.
     */
    public String getValue() {
        return defaultValue;
    }
}
