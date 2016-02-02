package io.cloudslang.content.vmware.entities;

import io.cloudslang.content.vmware.utils.InputUtils;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */

public class VmInputs {
    private static final int DEFAULT_CPU_COUNT = 1;
    private static final long DEFAULT_VM_DISK_SIZE_MB = 1024;
    private static final long DEFAULT_VM_MEMORY_SIZE_MB = 1024;

    private String dataCenterName;
    private String hostname;
    private String virtualMachineName;
    private String description;
    private String dataStore;
    private int intNumCPUs;
    private long longVmDiskSize;
    private long longVmMemorySize;
    private String guestOsId;
    private String operation;
    private String device;
    private String updateValue;
    private String vmDiskMode;

    public VmInputs(VmInputsBuilder builder) {
        this.dataCenterName = builder.dataCenterName;
        this.hostname = builder.hostname;
        this.virtualMachineName = builder.virtualMachineName;
        this.description = builder.description;
        this.dataStore = builder.dataStore;
        this.intNumCPUs = builder.intNumCPUs;
        this.longVmDiskSize = builder.longVmDiskSize;
        this.longVmMemorySize = builder.longVmMemorySize;
        this.guestOsId = builder.guestOsId;
        this.operation = builder.operation;
        this.device = builder.device;
        this.updateValue = builder.updateValue;
        this.vmDiskMode = builder.vmDiskMode;
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

    public int getIntNumCPUs() {
        return intNumCPUs;
    }

    public long getLongVmDiskSize() {
        return longVmDiskSize;
    }

    public long getLongVmMemorySize() {
        return longVmMemorySize;
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

    public static class VmInputsBuilder {
        private String dataCenterName;
        private String hostname;
        private String virtualMachineName;
        private String description;
        private String dataStore;
        private int intNumCPUs;
        private long longVmDiskSize;
        private long longVmMemorySize;
        private String guestOsId;
        private String operation;
        private String device;
        private String updateValue;
        private String vmDiskMode;

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

        public VmInputsBuilder withIntNumCPUs(String inputValue) {
            intNumCPUs = InputUtils.getIntInput(inputValue, DEFAULT_CPU_COUNT);
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
    }
}