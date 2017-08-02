/*******************************************************************************
 * (c) Copyright 2017 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.vmware.entities;

import java.util.Locale;

import static io.cloudslang.content.vmware.utils.InputUtils.getIntInput;
import static io.cloudslang.content.vmware.utils.InputUtils.getLongInput;
import static java.lang.Boolean.parseBoolean;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * Created by Mihai Tusa.
 * 1/6/2016.
 */

public class VmInputs {
    private static final int DEFAULT_CPU_COUNT = 1;
    private static final long DEFAULT_VM_DISK_SIZE_MB = 1024;
    private static final long DEFAULT_VM_MEMORY_SIZE_MB = 1024;

    private final String cloneDataStore;
    private final String cloneHost;
    private final String cloneName;
    private final String cloneResourcePool;
    private final String coresPerSocket;
    private final String clusterName;
    private final String dataCenterName;
    private final String dataStore;
    private final String description;
    private final String device;
    private final String diskProvisioning;
    private final String folderName;
    private final String guestOsId;
    private final String hostGroupName;
    private final String hostname;
    private final String ipAllocScheme;
    private final String ipProtocol;
    private final String operation;
    private final String resourcePool;
    private final String ruleName;
    private final String snapshotDescription;
    private final String snapshotName;
    private final String updateValue;
    private final String virtualMachineId;
    private final String virtualMachineName;
    private final String vmDiskMode;
    private final String vmGroupName;

    private final int intNumCPUs;

    private final long longVmDiskSize;
    private final long longVmMemorySize;

    private final boolean quiesce;
    private final boolean template;
    private final boolean thickProvision;
    private final boolean withMemoryDump;

    private final Locale locale;

    private VmInputs(VmInputsBuilder builder) {
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
        this.resourcePool = builder.resourcePool;
        this.cloneName = builder.cloneName;
        this.cloneResourcePool = builder.cloneResourcePool;
        this.cloneHost = builder.cloneHost;
        this.cloneDataStore = builder.cloneDataStore;
        this.coresPerSocket = builder.coresPerSocket;
        this.intNumCPUs = builder.intNumCPUs;
        this.longVmDiskSize = builder.longVmDiskSize;
        this.longVmMemorySize = builder.longVmMemorySize;
        this.thickProvision = builder.thickProvision;
        this.template = builder.template;
        this.locale = builder.locale;
        this.ipProtocol = builder.ipProtocol;
        this.ipAllocScheme = builder.ipAllocScheme;
        this.diskProvisioning = builder.diskProvisioning;
        this.clusterName = builder.clusterName;
        this.vmGroupName = builder.vmGroupName;
        this.hostGroupName = builder.hostGroupName;
        this.ruleName = builder.ruleName;
        this.virtualMachineId = builder.virtualMachineId;
        this.snapshotName = builder.snapshotName;
        this.snapshotDescription = builder.snapshotDescription;
        this.withMemoryDump = builder.withMemoryDump;
        this.quiesce = builder.quiesce;
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

    public String getVirtualMachineId() {
        return virtualMachineId;
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

    public String getResourcePool() {
        return resourcePool;
    }

    public String getCloneName() {
        return cloneName;
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

    public int getIntNumCPUs() {
        return intNumCPUs;
    }

    public String getCoresPerSocket() {
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

    public Locale getLocale() {
        return this.locale;
    }

    public String getIpProtocol() {
        return this.ipProtocol;
    }

    public String getIpAllocScheme() {
        return this.ipAllocScheme;
    }

    public String getDiskProvisioning() {
        return this.diskProvisioning;
    }

    public String getClusterName() {
        return this.clusterName;
    }

    public String getVmGroupName() {
        return vmGroupName;
    }

    public String getHostGroupName() {
        return hostGroupName;
    }

    public String getRuleName() {
        return ruleName;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public String getSnapshotDescription() {
        return snapshotDescription;
    }

    public boolean isWithMemoryDump() {
        return withMemoryDump;
    }

    public boolean isQuiesce() {
        return quiesce;
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
        private String resourcePool;
        private String cloneName;
        private String cloneResourcePool;
        private String cloneHost;
        private String cloneDataStore;
        private String coresPerSocket;

        private int intNumCPUs;
        private long longVmDiskSize;
        private long longVmMemorySize;

        private boolean thickProvision;
        private boolean template;
        private Locale locale;
        private String ipProtocol;
        private String ipAllocScheme;
        private String diskProvisioning;
        private String clusterName;
        private String vmGroupName;
        private String hostGroupName;
        private String ruleName;
        private String virtualMachineId;

        private String snapshotName;
        private String snapshotDescription;
        private boolean withMemoryDump;
        private boolean quiesce;

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

        public VmInputsBuilder withResourcePool(String inputValue) {
            resourcePool = inputValue;
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
            intNumCPUs = getIntInput(inputValue, DEFAULT_CPU_COUNT);
            return this;
        }

        public VmInputsBuilder withCoresPerSocket(String inputValue) {
            coresPerSocket = inputValue;
            return this;
        }

        public VmInputsBuilder withLongVmDiskSize(String inputValue) {
            longVmDiskSize = getLongInput(inputValue, DEFAULT_VM_DISK_SIZE_MB);
            return this;
        }

        public VmInputsBuilder withLongVmMemorySize(String inputValue) {
            longVmMemorySize = getLongInput(inputValue, DEFAULT_VM_MEMORY_SIZE_MB);
            return this;
        }

        public VmInputsBuilder withThickProvision(String inputValue) {
            thickProvision = parseBoolean(inputValue);
            return this;
        }

        public VmInputsBuilder withTemplate(String inputValue) {
            template = parseBoolean(inputValue);
            return this;
        }

        public VmInputsBuilder withLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public VmInputsBuilder withIpProtocol(String ipProtocol) {
            this.ipProtocol = ipProtocol;
            return this;
        }

        public VmInputsBuilder withIpAllocScheme(String ipAllocScheme) {
            this.ipAllocScheme = ipAllocScheme;
            return this;
        }

        public VmInputsBuilder withDiskProvisioning(String diskProvisioning) {
            if (isBlank(diskProvisioning)) {
                this.diskProvisioning = null;
            } else {
                this.diskProvisioning = diskProvisioning;
            }
            return this;
        }

        public VmInputsBuilder withClusterName(String clusterName) {
            this.clusterName = clusterName;
            return this;
        }

        public VmInputsBuilder withVmGroupName(String vmGroupName) {
            this.vmGroupName = vmGroupName;
            return this;
        }

        public VmInputsBuilder withHostGroupName(String hostGroupName) {
            this.hostGroupName = hostGroupName;
            return this;
        }

        public VmInputsBuilder withRuleName(String ruleName) {
            this.ruleName = ruleName;
            return this;
        }

        public VmInputsBuilder withVirtualMachineId(String virtualMachineId) {
            this.virtualMachineId = virtualMachineId;
            return this;
        }

        public VmInputsBuilder withSnapshotName(String snapshotName) {
            this.snapshotName = snapshotName;
            return this;
        }

        public VmInputsBuilder withSnapshotDescription(String snapshotDescription) {
            this.snapshotDescription = snapshotDescription;
            return this;
        }

        public VmInputsBuilder withWithMemoryDump(String withMemoryDump) {
            this.withMemoryDump = parseBoolean(withMemoryDump);
            return this;
        }

        public VmInputsBuilder withQuiesce(String quiesce) {
            this.quiesce = parseBoolean(quiesce);
            return this;
        }
    }
}
