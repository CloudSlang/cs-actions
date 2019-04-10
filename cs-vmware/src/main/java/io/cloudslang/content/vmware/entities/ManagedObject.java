

package io.cloudslang.content.vmware.entities;

public enum ManagedObject {
    NETWORK("Network");

    private String name;

    /**
     * Instantiates a ManagedObject name.
     *
     * @param input the name
     */
    ManagedObject(String input) {
        this.name = input;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
