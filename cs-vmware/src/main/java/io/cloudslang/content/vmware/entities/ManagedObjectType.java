

package io.cloudslang.content.vmware.entities;

/**
 * Created by Mihai Tusa.
 * 3/10/2016.
 */
public enum ManagedObjectType {
    SERVICE_INSTANCE("ServiceInstance"),
    DATA_CENTER("Datacenter"),
    HOST_SYSTEM("HostSystem"),
    RESOURCE_POOL("ResourcePool"),
    DATA_STORE("datastore"),
    FOLDER("Folder"),
    VIRTUAL_MACHINE("VirtualMachine"),
    RESOURCES("Resources"),
    ENVIRONMENT_BROWSER("environmentBrowser"),
    CONTAINER_VIEW("ContainerView"),
    NAME("name"),
    VIEW("view"),
    SUMMARY("summary"),
    STATE("state"),
    PARENT("parent"),
    VM_FOLDER("vmFolder"),
    INFO_STATE("info.state"),
    INFO_ERROR("info.error"),
    VM_ID("vmId"),
    VM_FULL_NAME("virtualMachineFullName"),
    VM_UUID("vmUuid"),
    VM_ETH_COUNT("numEths"),
    VM_DISK_COUNT("numDisks"),
    VM_PATH_NAME("vmPathName"),
    VM_IS_TEMPLATE("isTemplate"),
    VM_IP_ADDRESS("ipAddress");

    private String parameter;

    /**
     * Instantiates a VM parameter.
     *
     * @param input the parameter
     */
    ManagedObjectType(String input) {
        this.parameter = input;
    }

    /**
     * Gets the parameter.
     *
     * @return the parameter
     */
    public String getValue() {
        return parameter;
    }
}
