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

public class NutanixDetachDisksInputs {
    private final String vmUUID;
    private final String vmDiskUUIDList;
    private final String deviceBusList;
    private final String deviceIndexList;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "vmDiskUUIDList", "deviceBusList", "deviceIndexList", "commonInputs"})
    public NutanixDetachDisksInputs(String vmUUID, String vmDiskUUIDList, String deviceBusList, String deviceIndexList,
                                    NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.vmDiskUUIDList = vmDiskUUIDList;
        this.deviceBusList = deviceBusList;
        this.deviceIndexList = deviceIndexList;
        this.commonInputs = commonInputs;
    }

    public static NutanixDetachDisksInputs.NutanixDetachDisksInputsBuilder builder() {
        return new NutanixDetachDisksInputs.NutanixDetachDisksInputsBuilder();
    }

    @NotNull
    public String getVMUUID() {
        return vmUUID;
    }

    @NotNull
    public String getVmDiskUUIDList() {
        return vmDiskUUIDList;
    }

    @NotNull
    public String getDeviceBusList() {
        return deviceBusList;
    }

    @NotNull
    public String getDeviceIndexList() { return deviceIndexList; }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixDetachDisksInputsBuilder {
        private String vmUUID = EMPTY;
        private String vmDiskUUIDList = EMPTY;
        private String deviceBusList = EMPTY;
        private String deviceIndexList = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixDetachDisksInputsBuilder() {
        }

        @NotNull
        public NutanixDetachDisksInputs.NutanixDetachDisksInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixDetachDisksInputs.NutanixDetachDisksInputsBuilder vmDiskUUIDList
                (@NotNull final String vmDiskUUIDList) {
            this.vmDiskUUIDList = vmDiskUUIDList;
            return this;
        }

        @NotNull
        public NutanixDetachDisksInputs.NutanixDetachDisksInputsBuilder deviceBusList
                (@NotNull final String deviceBusList) {
            this.deviceBusList = deviceBusList;
            return this;
        }

        @NotNull
        public NutanixDetachDisksInputs.NutanixDetachDisksInputsBuilder deviceIndexList
                (@NotNull final String deviceIndexList){
            this.deviceIndexList = deviceIndexList;
            return this;
        }

        @NotNull
        public NutanixDetachDisksInputs.NutanixDetachDisksInputsBuilder commonInputs
                (@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixDetachDisksInputs build() {
            return new NutanixDetachDisksInputs(vmUUID, vmDiskUUIDList, deviceBusList, deviceIndexList,commonInputs);
        }

    }
}
