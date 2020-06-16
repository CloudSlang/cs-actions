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
    private final String deviceBus;
    private final String deviceIndex;
    private final String vmDisksize;
    private final String storagecontainerUUIDDisk;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "deviceBus", "deviceIndex", "vmDisksize", "storagecontainerUUIDDisk", "commonInputs"})
    public NutanixAttachDisksInputs(String vmUUID, String deviceBus, String deviceIndex, String vmDisksize, String storagecontainerUUIDDisk,
                                    NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.deviceBus = deviceBus;
        this.deviceIndex = deviceIndex;
        this.vmDisksize = vmDisksize;
        this.storagecontainerUUIDDisk = storagecontainerUUIDDisk;
        this.commonInputs = commonInputs;
    }

    public static NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder builder() {
        return new NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder();
    }

    @NotNull
    public String getVmUUID() {
        return vmUUID;
    }

    @NotNull
    public String getDeviceBus() {
        return deviceBus;
    }

    @NotNull
    public String getDeviceIndex() {
        return deviceIndex;
    }

    @NotNull
    public String getVmDisksize() {
        return vmDisksize;
    }

    @NotNull
    public String getStoragecontainerUUIDDisk() {
        return storagecontainerUUIDDisk;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixAttachDisksInputsBuilder {
        private String vmUUID = EMPTY;
        private String deviceBus = EMPTY;
        private String deviceIndex = EMPTY;
        private String vmDisksize = EMPTY;
        private String storagecontainerUUIDDisk = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixAttachDisksInputsBuilder() {

        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder deviceBus(@NotNull final String deviceBus) {
            this.deviceBus = deviceBus;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder deviceIndex(@NotNull final String deviceIndex) {
            this.deviceIndex = deviceIndex;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder vmDisksize(@NotNull final String vmDisksize) {
            this.vmDisksize = vmDisksize;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder storagecontainerUUIDDisk(@NotNull final String storagecontainerUUIDDisk) {
            this.storagecontainerUUIDDisk = storagecontainerUUIDDisk;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixAttachDisksInputs build() {
            return new NutanixAttachDisksInputs(vmUUID, deviceBus, deviceIndex, vmDisksize, storagecontainerUUIDDisk, commonInputs);
        }

    }


}
