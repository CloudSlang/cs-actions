package io.cloudslang.content.entities;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */
public class VmInputs {

    private String dataCenterName;
    private String hostname;
    private String virtualMachineName;
    private String description;
    private String dataStore;
    private Integer intNumCPUs;
    private Long longVmDiskSize;
    private Long longVmMemorySize;
    private String guestOsId;

    public VmInputs(String virtualMachineName) {
        this.virtualMachineName = virtualMachineName;
    }

    public VmInputs(String dataCenterName,
                    String hostname,
                    String virtualMachineName,
                    String description,
                    String dataStore,
                    Integer intNumCPUs,
                    Long longVmDiskSize,
                    Long longVmMemorySize,
                    String guestOsId) {
        this.dataCenterName = dataCenterName;
        this.hostname = hostname;
        this.virtualMachineName = virtualMachineName;
        this.description = description;
        this.dataStore = dataStore;
        this.intNumCPUs = intNumCPUs;
        this.longVmDiskSize = longVmDiskSize;
        this.longVmMemorySize = longVmMemorySize;
        this.guestOsId = guestOsId;
    }

    public String getDataCenterName() {
        return dataCenterName;
    }

    public String getHostname() {
        return hostname;
    }

    public String getVirtualMachineName() {
        return virtualMachineName;
    }

    public String getDescription() {
        return description;
    }

    public String getDataStore() {
        return dataStore;
    }

    public Integer getIntNumCPUs() {
        return intNumCPUs;
    }

    public Long getLongVmDiskSize() {
        return longVmDiskSize;
    }

    public Long getLongVmMemorySize() {
        return longVmMemorySize;
    }

    public String getGuestOsId() {
        return guestOsId;
    }
}