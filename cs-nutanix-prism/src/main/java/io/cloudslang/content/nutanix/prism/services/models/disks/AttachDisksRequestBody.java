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
package io.cloudslang.content.nutanix.prism.services.models.disks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class AttachDisksRequestBody {
    @JsonProperty("vm_disks")
    ArrayList vmDisks;


    public ArrayList getVmDisks() {
        return vmDisks;
    }

    public void setVmDisks(ArrayList vmDisks) {
        this.vmDisks = vmDisks;
    }

    public class VMDisks {
        @JsonProperty("disk_address")
        AttachDisksRequestBody.DiskAddress diskAddress;

        @JsonProperty("vm_disk_create")
        AttachDisksRequestBody.VMDiskCreate vmDiskCreate;

        public VMDiskCreate getVmDiskCreate() {
            return vmDiskCreate;
        }

        public void setVmDiskCreate(VMDiskCreate vmDiskCreate) {
            this.vmDiskCreate = vmDiskCreate;
        }

        public DiskAddress getDiskAddress() {
            return diskAddress;
        }

        public void setDiskAddress(DiskAddress diskAddress) {
            this.diskAddress = diskAddress;
        }

    }

    public class DiskAddress {
        @JsonProperty("device_bus")
        String deviceBus;
        @JsonProperty("device_index")
        String deviceIndex;

        public String getDeviceBus() {
            return deviceBus;
        }

        public void setDeviceBus(String deviceBus) {
            this.deviceBus = deviceBus;
        }

        public String getDeviceIndex() {
            return deviceIndex;
        }

        public void setDeviceIndex(String deviceIndex) {
            this.deviceIndex = deviceIndex;
        }

    }

    public class VMDiskCreate {

        @JsonProperty("size")
        long size;

        @JsonProperty("storage_container_uuid")
        String storage_container_uuid;

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }


        public String getStorage_container_uuid() {
            return storage_container_uuid;
        }

        public void setStorage_container_uuid(String storage_container_uuid) {
            this.storage_container_uuid = storage_container_uuid;
        }

    }
}
