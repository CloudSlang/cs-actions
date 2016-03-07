package io.cloudslang.content.vmware.entities;

import io.cloudslang.content.vmware.utils.InputUtils;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */

public class VmInputs {
    private static final int DEFAULT_CPU_COUNT = 1;
    private static final int DEFAULT_CORES_PER_SOCKET = 1;
    private static final long DEFAULT_VM_DISK_SIZE_MB = 1024;
    private static final long DEFAULT_VM_MEMORY_SIZE_MB = 1024;

    private String dataCenterName;
    private String hostname;
    private String virtualMachineName;
    private String description;
    private String dataStore;
    private String guestOsId;
    private String operation;
    private String device;
    private String updateValue;
    private String vmDiskMode;
    private String folderName;
    private String cloneName;
    private String cloneResourcePool;
    private String cloneHost;
    private String cloneDataStore;

    private int intNumCPUs;
    private int coresPerSocket;
    private long longVmDiskSize;
    private long longVmMemorySize;

    private boolean thickProvision;
    private boolean template;

    public VmInputs(VmInputsBuilder builder) {
        this.dataCenterName = builder.dataCenterName;
        this.hostname = builder.hostname;
        this.virtualMachineName = builder.virtualMachineName;
        this.description = builder.description;
        this.dataStore = builder.dataStore;
        this.guestOsId = builder.guestOsId;
        this.operation = builder.operation;
        this.device = builder.device;
        this.updateValue = builder.updateValue;
        this.vmDiskMode = builder.vmDiskMode;
        this.folderName = builder.folderName;
        this.cloneName = builder.cloneName;
        this.cloneResourcePool = builder.cloneResourcePool;
        this.cloneHost = builder.cloneHost;
        this.cloneDataStore = builder.cloneDataStore;

        this.intNumCPUs = builder.intNumCPUs;
        this.coresPerSocket = builder.coresPerSocket;
        this.longVmDiskSize = builder.longVmDiskSize;
        this.longVmMemorySize = builder.longVmMemorySize;

        this.thickProvision = builder.thickProvision;
        this.template = builder.template;
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

    public String getGuestOsId() {
        return guestOsId;
    }

    public String getOperation() {
        return operation;
    }

    public String getDevice() {
        return device;
    }

    public String getUpdateValue() {
        return updateValue;
    }

    public String getVmDiskMode() {
        return vmDiskMode;
    }

    public String getFolderName() {
        return folderName;
    }

    public String getCloneName() {
        return cloneName;
    }

    public int getIntNumCPUs() {
        return intNumCPUs;
    }

    public int getCoresPerSocket() {
        return coresPerSocket;
    }

    public long getLongVmDiskSize() {
        return longVmDiskSize;
    }

    public long getLongVmMemorySize() {
        return longVmMemorySize;
    }

    public boolean isThickProvision() {
        return thickProvision;
    }

    public boolean isTemplate() {
        return template;
    }

    public String getCloneResourcePool() {
        return cloneResourcePool;
    }

    public String getCloneHost() {
        return cloneHost;
    }

    public String getCloneDataStore() {
        return cloneDataStore;
    }

    public static class VmInputsBuilder {
        private String dataCenterName;
        private String hostname;
        private String virtualMachineName;
        private String description;
        private String dataStore;
        private String guestOsId;
        private String operation;
        private String device;
        private String updateValue;
        private String vmDiskMode;
        private String folderName;
        private String cloneName;
        private String cloneResourcePool;
        private String cloneHost;
        private String cloneDataStore;

        private int intNumCPUs;
        private int coresPerSocket;
        private long longVmDiskSize;
        private long longVmMemorySize;

        private boolean thickProvision;
        private boolean template;

        public VmInputs build() {
            return new VmInputs(this);
        }

        public VmInputsBuilder withDataCenterName(String inputValue) {
            dataCenterName = inputValue;
            return this;
        }

        public VmInputsBuilder withHostname(String inputValue) {
            hostname = inputValue;
            return this;
        }

        public VmInputsBuilder withVirtualMachineName(String inputValue) {
            virtualMachineName = inputValue;
            return this;
        }

        public VmInputsBuilder withDescription(String inputValue) {
            description = inputValue;
            return this;
        }

        public VmInputsBuilder withDataStore(String inputValue) {
            dataStore = inputValue;
            return this;
        }

        public VmInputsBuilder withGuestOsId(String inputValue) {
            guestOsId = inputValue;
            return this;
        }

        public VmInputsBuilder withOperation(String inputValue) throws Exception {
            operation = Operation.getValue(inputValue);
            return this;
        }

        public VmInputsBuilder withDevice(String inputValue) throws Exception {
            device = Device.getValue(inputValue);
            return this;
        }

        public VmInputsBuilder withUpdateValue(String inputValue) throws Exception {
            updateValue = inputValue;
            return this;
        }

        public VmInputsBuilder withDiskMode(String inputValue) throws Exception {
            vmDiskMode = inputValue;
            return this;
        }

        public VmInputsBuilder withFolderName(String inputValue) {
            folderName = inputValue;
            return this;
        }

        public VmInputsBuilder withCloneName(String inputValue) {
            cloneName = inputValue;
            return this;
        }

        public VmInputsBuilder withCloneResourcePool(String inputValue) {
            cloneResourcePool = inputValue;
            return this;
        }

        public VmInputsBuilder withCloneHost(String inputValue) {
            cloneHost = inputValue;
            return this;
        }

        public VmInputsBuilder withCloneDataStore(String inputValue) {
            cloneDataStore = inputValue;
            return this;
        }

        public VmInputsBuilder withIntNumCPUs(String inputValue) {
            intNumCPUs = InputUtils.getIntInput(inputValue, DEFAULT_CPU_COUNT);
            return this;
        }

        public VmInputsBuilder withCoresPerSocket(String inputValue) {
            coresPerSocket = InputUtils.getIntInput(inputValue, DEFAULT_CORES_PER_SOCKET);
            return this;
        }

        public VmInputsBuilder withLongVmDiskSize(String inputValue) {
            longVmDiskSize = InputUtils.getLongInput(inputValue, DEFAULT_VM_DISK_SIZE_MB);
            return this;
        }

        public VmInputsBuilder withLongVmMemorySize(String inputValue) {
            longVmMemorySize = InputUtils.getLongInput(inputValue, DEFAULT_VM_MEMORY_SIZE_MB);
            return this;
        }

        public VmInputsBuilder withThickProvision(String inputValue){
            thickProvision = Boolean.parseBoolean(inputValue);
            return this;
        }

        public VmInputsBuilder withTemplate(String inputValue) {
            template = Boolean.parseBoolean(inputValue);
            return this;
        }
    }
}