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
    private final String vmDisksizeList;
    private final String storagecontainerUUIDDiskList;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "deviceBusList", "deviceIndexList", "vmDisksizeList", "storagecontainerUUIDDiskList", "commonInputs"})
    public NutanixAttachDisksInputs(String vmUUID, String deviceBusList, String deviceIndexList, String vmDisksizeList, String storagecontainerUUIDDiskList,
                                    NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.deviceBusList = deviceBusList;
        this.deviceIndexList = deviceIndexList;
        this.vmDisksizeList = vmDisksizeList;
        this.storagecontainerUUIDDiskList = storagecontainerUUIDDiskList;
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
        return deviceBusList;
    }

    @NotNull
    public String getDeviceIndex() {
        return deviceIndexList;
    }

    @NotNull
    public String getVmDisksize() {
        return vmDisksizeList;
    }

    @NotNull
    public String getStoragecontainerUUIDDisk() {
        return storagecontainerUUIDDiskList;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixAttachDisksInputsBuilder {
        private String vmUUID = EMPTY;
        private String deviceBusList = EMPTY;
        private String deviceIndexList = EMPTY;
        private String vmDisksizeList = EMPTY;
        private String storagecontainerUUIDDiskList = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixAttachDisksInputsBuilder() {

        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder deviceBusList(@NotNull final String deviceBusList) {
            this.deviceBusList = deviceBusList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder deviceIndexList(@NotNull final String deviceIndexList) {
            this.deviceIndexList = deviceIndexList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder vmDisksizeList(@NotNull final String vmDisksizeList) {
            this.vmDisksizeList = vmDisksizeList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder storagecontainerUUIDDiskList(@NotNull final String storagecontainerUUIDDiskList) {
            this.storagecontainerUUIDDiskList = storagecontainerUUIDDiskList;
            return this;
        }

        @NotNull
        public NutanixAttachDisksInputs.NutanixAttachDisksInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixAttachDisksInputs build() {
            return new NutanixAttachDisksInputs(vmUUID, deviceBusList, deviceIndexList, vmDisksizeList, storagecontainerUUIDDiskList, commonInputs);
        }

    }


}
