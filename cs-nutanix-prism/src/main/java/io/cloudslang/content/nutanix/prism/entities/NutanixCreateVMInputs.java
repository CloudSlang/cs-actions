/*
 * (c) Copyright 2020 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixCreateVMInputs {

    private final String vmName;
    private final String description;
    private final String vmMemorySize;
    private final String numVCPUs;
    private final String numCoresPerVCPU;
    private final String timeZone;
    private final String hypervisorType;
    private final String flashModeEnabled;
    private final String isSCSIPassThrough;
    private final String isThinProvisioned;
    private final String isCDROM;
    private final String isEmpty;
    private final String deviceBus;
    private final String diskLabel;
    private final String deviceIndex;
    private final String ndfsFilepath;
    private final String sourceVMDiskUUID;
    private final String vmDiskMinimumSize;
    private final String externalDiskUrl;
    private final String externalDiskSize;
    private final String storageContainerUUID;
    private final String vmDiskSize;
    private final String networkUUID;
    private final String requestedIPAddress;
    private final String isConnected;
    private final String hostUUIDs;
    private final String agentVM;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmName", "description", "vmMemorySize", "numVCPUs", "numCoresPerVCPU"
            , "timeZone", "hypervisorType", "flashModeEnabled", "isSCSIPassThrough", "isThinProvisioned", "isCDROM",
            "isEmpty", "deviceBus", "diskLabel", "deviceIndex", "ndsfFilepath",
            "sourceVMDiskUUID", "vmDiskMinimumSize", "externalDiskUrl", "externalDiskSize", "storageContainerUUID",
            "vmDiskSize", "networkUUID", "requestedIPAddress", "isConnected", "hostUUIDs", "agentVM", "commonInputs"})
    public NutanixCreateVMInputs(String vmName, String description, String vmMemorySize, String numVCPUs,
                                 String numCoresPerVCPU, String timeZone, String hypervisorType,
                                 String flashModeEnabled, String isSCSIPassThrough, String isThinProvisioned,
                                 String isCDROM, String isEmpty, String deviceBus, String diskLabel, String deviceIndex,
                                 String ndfsFilepath, String sourceVMDiskUUID, String vmDiskMinimumSize,
                                 String externalDiskUrl, String externalDiskSize, String storageContainerUUID,
                                 String vmDiskSize, String networkUUID, String requestedIPAddress, String isConnected,
                                 String hostUUIDs, String agentVM, NutanixCommonInputs commonInputs) {
        this.vmName = vmName;
        this.description = description;
        this.vmMemorySize = vmMemorySize;
        this.numVCPUs = numVCPUs;
        this.numCoresPerVCPU = numCoresPerVCPU;
        this.timeZone = timeZone;
        this.hypervisorType = hypervisorType;
        this.flashModeEnabled = flashModeEnabled;
        this.isSCSIPassThrough = isSCSIPassThrough;
        this.isThinProvisioned = isThinProvisioned;
        this.isCDROM = isCDROM;
        this.isEmpty = isEmpty;
        this.deviceBus = deviceBus;
        this.diskLabel = diskLabel;
        this.deviceIndex = deviceIndex;
        this.ndfsFilepath = ndfsFilepath;
        this.sourceVMDiskUUID = sourceVMDiskUUID;
        this.vmDiskMinimumSize = vmDiskMinimumSize;
        this.externalDiskUrl = externalDiskUrl;
        this.externalDiskSize = externalDiskSize;
        this.storageContainerUUID = storageContainerUUID;
        this.vmDiskSize = vmDiskSize;
        this.networkUUID = networkUUID;
        this.requestedIPAddress = requestedIPAddress;
        this.isConnected = isConnected;
        this.hostUUIDs = hostUUIDs;
        this.agentVM = agentVM;
        this.commonInputs = commonInputs;
    }

    public static NutanixCreateVMInputsBuilder builder() {
        return new NutanixCreateVMInputsBuilder();
    }

    @NotNull
    public String getVmName() {
        return vmName;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public String getVmMemorySize() {
        return vmMemorySize;
    }

    @NotNull
    public String getNumVCPUs() {
        return numVCPUs;
    }

    @NotNull
    public String getNumCoresPerVCPU() {
        return numCoresPerVCPU;
    }

    @NotNull
    public String getTimeZone() {
        return timeZone;
    }

    @NotNull
    public String getFlashModeEnabled() {
        return flashModeEnabled;
    }

    @NotNull
    public String getIsSCSIPassThrough() {
        return isSCSIPassThrough;
    }

    @NotNull
    public String getIsThinProvisioned() {
        return isThinProvisioned;
    }

    @NotNull
    public String getDiskLabel() {
        return diskLabel;
    }

    @NotNull
    public String getDeviceIndex() {
        return deviceIndex;
    }

    @NotNull
    public String getNdfsFilepath() {
        return ndfsFilepath;
    }

    @NotNull
    public String getExternalDiskUrl() {
        return externalDiskUrl;
    }

    @NotNull
    public String getExternalDiskSize() {
        return externalDiskSize;
    }

    @NotNull
    public String getStorageContainerUUID() {
        return storageContainerUUID;
    }

    @NotNull
    public String getVmDiskSize() {
        return vmDiskSize;
    }

    @NotNull
    public String getDeviceBus() {
        return deviceBus;
    }

    @NotNull
    public String getSourceVMDiskUUID() {
        return sourceVMDiskUUID;
    }

    @NotNull
    public String getIsCDROM() {
        return isCDROM;
    }

    @NotNull
    public String getIsEmpty() {
        return isEmpty;
    }

    @NotNull
    public String getVmDiskMinimumSize() {
        return vmDiskMinimumSize;
    }

    @NotNull
    public String getNetworkUUID() {
        return networkUUID;
    }

    @NotNull
    public String getRequestedIPAddress() {
        return requestedIPAddress;
    }

    @NotNull
    public String getIsConnected() {
        return isConnected;
    }

    @NotNull
    public String getHypervisorType() {
        return hypervisorType;
    }

    @NotNull
    public String getHostUUIDs() {
        return hostUUIDs;
    }

    @NotNull
    public String getAgentVM() {
        return agentVM;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixCreateVMInputsBuilder {
        private String vmName = EMPTY;
        private String description = EMPTY;
        private String vmMemorySize = EMPTY;
        private String numVCPUs = EMPTY;
        private String numCoresPerVCPU = EMPTY;
        private String timeZone = EMPTY;
        private String hypervisorType = EMPTY;
        private String flashModeEnabled = EMPTY;
        private String isSCSIPassThrough = EMPTY;
        private String isThinProvisioned = EMPTY;
        private String isCDROM = EMPTY;
        private String isEmpty = EMPTY;
        private String deviceBus = EMPTY;
        private String diskLabel = EMPTY;
        private String deviceIndex = EMPTY;
        private String ndfsFilepath = EMPTY;
        private String sourceVMDiskUUID = EMPTY;
        private String vmDiskMinimumSize = EMPTY;
        private String externalDiskUrl = EMPTY;
        private String externalDiskSize = EMPTY;
        private String storageContainerUUID = EMPTY;
        private String vmDiskSize = EMPTY;
        private String networkUUID = EMPTY;
        private String requestedIPAddress = EMPTY;
        private String isConnected = EMPTY;
        private String agentVM = EMPTY;
        private String hostUUIDs = EMPTY;

        private NutanixCommonInputs commonInputs;

        NutanixCreateVMInputsBuilder() {
        }

        @NotNull
        public NutanixCreateVMInputsBuilder vmName(@NotNull final String vmName) {
            this.vmName = vmName;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder description(@NotNull final String description) {
            this.description = description;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder vmMemorySize(@NotNull final String vmMemorySize) {
            this.vmMemorySize = vmMemorySize;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder numVCPUs(@NotNull final String numVCPUs) {
            this.numVCPUs = numVCPUs;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder numCoresPerVCPU(@NotNull final String numCoresPerVCPU) {
            this.numCoresPerVCPU = numCoresPerVCPU;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder timeZone(@NotNull final String timeZone) {
            this.timeZone = timeZone;
            return this;
        }


        @NotNull
        public NutanixCreateVMInputsBuilder hypervisorType(@NotNull final String hypervisorType) {
            this.hypervisorType = hypervisorType;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder flashModeEnabled(@NotNull final String flashModeEnabled) {
            this.flashModeEnabled = flashModeEnabled;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder isSCSIPassThrough(@NotNull final String isSCSIPassThrough) {
            this.isSCSIPassThrough = isSCSIPassThrough;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder isThinProvisioned(@NotNull final String isThinProvisioned) {
            this.isThinProvisioned = isThinProvisioned;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder isCDROM(@NotNull final String isCDROM) {
            this.isCDROM = isCDROM;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder isEmpty(@NotNull final String isEmpty) {
            this.isEmpty = isEmpty;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder deviceBus(@NotNull final String deviceBus) {
            this.deviceBus = deviceBus;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder diskLabel(@NotNull final String diskLabel) {
            this.diskLabel = diskLabel;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder deviceIndex(@NotNull final String deviceIndex) {
            this.deviceIndex = deviceIndex;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder ndfsFilepath(@NotNull final String ndfsFilepath) {
            this.ndfsFilepath = ndfsFilepath;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder sourceVMDiskUUID(@NotNull final String sourceVMDiskUUID) {
            this.sourceVMDiskUUID = sourceVMDiskUUID;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder vmDiskMinimumSize(@NotNull final String vmDiskMinimumSize) {
            this.vmDiskMinimumSize = vmDiskMinimumSize;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder externalDiskUrl(@NotNull final String externalDiskUrl) {
            this.externalDiskUrl = externalDiskUrl;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder externalDiskSize(@NotNull final String externalDiskSize) {
            this.externalDiskSize = externalDiskSize;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder storageContainerUUID(@NotNull final String storageContainerUUID) {
            this.storageContainerUUID = storageContainerUUID;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder vmDiskSize(@NotNull final String vmDiskSize) {
            this.vmDiskSize = vmDiskSize;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder networkUUID(@NotNull final String networkUUID) {
            this.networkUUID = networkUUID;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder requestedIPAddress(@NotNull final String requestedIPAddress) {
            this.requestedIPAddress = requestedIPAddress;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder isConnected(@NotNull final String isConnected) {
            this.isConnected = isConnected;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder hostUUIDs(@NotNull final String hostUUIDs) {
            this.hostUUIDs = hostUUIDs;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder agentVM(@NotNull final String agentVM) {
            this.agentVM = agentVM;
            return this;
        }

        @NotNull
        public NutanixCreateVMInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixCreateVMInputs build() {
            return new NutanixCreateVMInputs(vmName, description, vmMemorySize, numVCPUs, numCoresPerVCPU
                    , timeZone, hypervisorType, flashModeEnabled, isSCSIPassThrough, isThinProvisioned,
                    isCDROM, isEmpty, deviceBus, diskLabel, deviceIndex, ndfsFilepath, sourceVMDiskUUID,
                    vmDiskMinimumSize, externalDiskUrl, externalDiskSize, storageContainerUUID, vmDiskSize,
                    networkUUID, requestedIPAddress, isConnected, hostUUIDs, agentVM, commonInputs);
        }
    }
}
