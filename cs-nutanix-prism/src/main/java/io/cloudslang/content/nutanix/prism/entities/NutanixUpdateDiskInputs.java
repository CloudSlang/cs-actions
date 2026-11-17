/*
 * Copyright 2026-2027 Open Text
 * This program and the accompanying materials
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

public class NutanixUpdateDiskInputs {
    private final String vmUUID;
    private final String vmDiskUUID;
    private final String deviceBus;
    private final String deviceIndex;
    private final String storageContainerUUID;
    private final String vmDiskSizeGiB;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "vmDiskUUID", "deviceBus", "deviceIndex", "storageContainerUUID",
            "vmDiskSizeGiB", "commonInputs"})
    public NutanixUpdateDiskInputs(String vmUUID, String vmDiskUUID, String deviceBus, String deviceIndex,
                                  String storageContainerUUID, String vmDiskSizeGiB,
                                  NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.vmDiskUUID = vmDiskUUID;
        this.deviceBus = deviceBus;
        this.deviceIndex = deviceIndex;
        this.storageContainerUUID = storageContainerUUID;
        this.vmDiskSizeGiB = vmDiskSizeGiB;
        this.commonInputs = commonInputs;
    }

    public static NutanixUpdateDiskInputsBuilder builder() {
        return new NutanixUpdateDiskInputsBuilder();
    }

    @NotNull
    public String getVmUUID() {
        return vmUUID;
    }

    @NotNull
    public String getVmDiskUUID() {
        return vmDiskUUID;
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
    public String getStorageContainerUUID() {
        return storageContainerUUID;
    }

    @NotNull
    public String getVmDiskSizeGiB() {
        return vmDiskSizeGiB;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixUpdateDiskInputsBuilder {
        private String vmUUID = EMPTY;
        private String vmDiskUUID = EMPTY;
        private String deviceBus = EMPTY;
        private String deviceIndex = EMPTY;
        private String storageContainerUUID = EMPTY;
        private String vmDiskSizeGiB = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixUpdateDiskInputsBuilder() {
        }

        @NotNull
        public NutanixUpdateDiskInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixUpdateDiskInputsBuilder vmDiskUUID(@NotNull final String vmDiskUUID) {
            this.vmDiskUUID = vmDiskUUID;
            return this;
        }

        @NotNull
        public NutanixUpdateDiskInputsBuilder deviceBus(@NotNull final String deviceBus) {
            this.deviceBus = deviceBus;
            return this;
        }

        @NotNull
        public NutanixUpdateDiskInputsBuilder deviceIndex(@NotNull final String deviceIndex) {
            this.deviceIndex = deviceIndex;
            return this;
        }

        @NotNull
        public NutanixUpdateDiskInputsBuilder storageContainerUUID(@NotNull final String storageContainerUUID) {
            this.storageContainerUUID = storageContainerUUID;
            return this;
        }

        @NotNull
        public NutanixUpdateDiskInputsBuilder vmDiskSizeGiB(@NotNull final String vmDiskSizeGiB) {
            this.vmDiskSizeGiB = vmDiskSizeGiB;
            return this;
        }

        @NotNull
        public NutanixUpdateDiskInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixUpdateDiskInputs build() {
            return new NutanixUpdateDiskInputs(vmUUID, vmDiskUUID, deviceBus, deviceIndex, storageContainerUUID,
                    vmDiskSizeGiB, commonInputs);
        }
    }
}
