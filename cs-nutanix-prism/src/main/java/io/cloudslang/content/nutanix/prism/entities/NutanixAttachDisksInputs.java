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

public class NutanixAttachDisksInputs {
    private final String vmUUID;
    private final String deviceBusList;
    private final String deviceIndexList;
    private final String isCDROMList;
    private final String isEmptyDiskList;
    private final String sourceVMDiskUUIDList;
    private final String vmDiskMinimumSizeList;
    private final String ndfsFilepathList;
    private final String vmDiskSizeList;
    private final String storageContainerUUIDList;
    private final String isSCSIPassThroughList;
    private final String isThinProvisionedList;
    private final String isFlashModeEnabledList;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "deviceBusList", "deviceIndexList", "isCDROMList", "isEmptyDiskList",
            "sourceVMDiskUUIDList", "vmDiskMinimumSizeList", "ndfsFilepathList", "vmDiskSizeList",
            "storageContainerUUIDList", "isSCSIPassThroughList", "isThinProvisionedList", "isFlashModeEnabledList",
            "commonInputs"})
    public NutanixAttachDisksInputs(String vmUUID, String deviceBusList, String deviceIndexList, String isCDROMList,
                                    String isEmptyDiskList, String sourceVMDiskUUIDList, String vmDiskMinimumSizeList,
                                    String ndfsFilepathList, String vmDiskSizeList, String storageContainerUUIDList,
                                    String isSCSIPassThroughList, String isThinProvisionedList,
                                    String isFlashModeEnabledList, NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.deviceBusList = deviceBusList;
        this.deviceIndexList = deviceIndexList;
        this.isCDROMList = isCDROMList;
        this.isEmptyDiskList = isEmptyDiskList;
        this.sourceVMDiskUUIDList = sourceVMDiskUUIDList;
        this.vmDiskMinimumSizeList = vmDiskMinimumSizeList;
        this.ndfsFilepathList = ndfsFilepathList;
        this.vmDiskSizeList = vmDiskSizeList;
        this.storageContainerUUIDList = storageContainerUUIDList;
        this.isSCSIPassThroughList = isSCSIPassThroughList;
        this.isThinProvisionedList = isThinProvisionedList;
        this.isFlashModeEnabledList = isFlashModeEnabledList;
        this.commonInputs = commonInputs;
    }

    public static NutanixAttachDisksInputsBuilder builder() {
        return new NutanixAttachDisksInputsBuilder();
    }

    @NotNull
    public String getVmUUID() {
        return vmUUID;
    }

    @NotNull
    public String getDeviceBusList() {
        return deviceBusList;
    }

    @NotNull
    public String getDeviceIndexList() {
        return deviceIndexList;
    }

    @NotNull
    public String getVmDiskSizeList() {
        return vmDiskSizeList;
    }

    @NotNull
    public String getStorageContainerUUIDList() {
        return storageContainerUUIDList;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    @NotNull
    public String getIsCDROMList() {
        return isCDROMList;
    }

    @NotNull
    public String getIsEmptyList() {
        return isEmptyDiskList;
    }

    @NotNull
    public String getSourceVMDiskUUIDList() {
        return sourceVMDiskUUIDList;
    }

    @NotNull
    public String getVmDiskMinimumSizeList() {
        return vmDiskMinimumSizeList;
    }

    @NotNull
    public String getNdfsFilepathList() {
        return ndfsFilepathList;
    }

    @NotNull
    public String getIsSCSIPassThroughList() {
        return isSCSIPassThroughList;
    }

    @NotNull
    public String getIsThinProvisionedList() {
        return isThinProvisionedList;
    }

    @NotNull
    public String getIsFlashModeEnabledList() {
        return isFlashModeEnabledList;
    }

    public static class NutanixAttachDisksInputsBuilder {
        private String vmUUID = EMPTY;
        private String deviceBusList = EMPTY;
        private String deviceIndexList = EMPTY;
        private String isCDROMList = EMPTY;
        private String isEmptyDiskList = EMPTY;
        private String sourceVMDiskUUIDList = EMPTY;
        private String vmDiskMinimumSizeList = EMPTY;
        private String ndfsFilepathList = EMPTY;
        private String vmDiskSizeList = EMPTY;
        private String storageContainerUUIDList = EMPTY;
        private String isSCSIPassThroughList = EMPTY;
        private String isThinProvisionedList = EMPTY;
        private String isFlashModeEnabledList = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixAttachDisksInputsBuilder() {

        }

        @NotNull
        public NutanixAttachDisksInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder deviceBusList(@NotNull final String deviceBusList) {
            this.deviceBusList = deviceBusList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder deviceIndexList(@NotNull final String deviceIndexList) {
            this.deviceIndexList = deviceIndexList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder isCDROMList(@NotNull final String isCDROMList) {
            this.isCDROMList = isCDROMList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder isEmptyDiskList(@NotNull final String isEmptyDiskList) {
            this.isEmptyDiskList = isEmptyDiskList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder sourceVMDiskUUIDList(@NotNull final String sourceVMDiskUUIDList) {
            this.sourceVMDiskUUIDList = sourceVMDiskUUIDList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder vmDiskMinimumSizeList(@NotNull final String vmDiskMinimumSizeList) {
            this.vmDiskMinimumSizeList = vmDiskMinimumSizeList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder ndfsFilepathList(@NotNull final String ndfsFilepathList) {
            this.ndfsFilepathList = ndfsFilepathList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder vmDiskSizeList(@NotNull final String vmDiskSizeList) {
            this.vmDiskSizeList = vmDiskSizeList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder storageContainerUUIDList(@NotNull final String storageContainerUUIDList) {
            this.storageContainerUUIDList = storageContainerUUIDList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder isSCSIPassThroughList(@NotNull final String isSCSIPassThroughList) {
            this.isSCSIPassThroughList = isSCSIPassThroughList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder isThinProvisionedList(@NotNull final String isThinProvisionedList) {
            this.isThinProvisionedList = isThinProvisionedList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder isFlashModeEnabledList(@NotNull final String isFlashModeEnabledList) {
            this.isFlashModeEnabledList = isFlashModeEnabledList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixAttachDisksInputs build() {
            return new NutanixAttachDisksInputs(vmUUID, deviceBusList, deviceIndexList, isCDROMList, isEmptyDiskList,
                    sourceVMDiskUUIDList, vmDiskMinimumSizeList, ndfsFilepathList, vmDiskSizeList,
                    storageContainerUUIDList, isSCSIPassThroughList, isThinProvisionedList, isFlashModeEnabledList,
                    commonInputs);
        }

    }


}
