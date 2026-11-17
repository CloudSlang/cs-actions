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
package io.cloudslang.content.nutanix.prism.services.models.disks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class DetachDisksRequestBody {
    @JsonProperty("uuid")
    String vmUUID;
    @JsonProperty("vm_disks")
    ArrayList vmDisks;

    public String getVmUUID() {
        return vmUUID;
    }

    public void setVmUUID(String vmUUID) {
        this.vmUUID = vmUUID;
    }

    public ArrayList getVmDisks() {
        return vmDisks;
    }

    public void setVmDisks(ArrayList vmDisks) {
        this.vmDisks = vmDisks;
    }

    public class VMDisks {
        @JsonProperty("disk_address")
        DiskAddress diskAddress;

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
        @JsonProperty("vmdisk_uuid")
        String vmDiskUUID;

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

        public String getVmDiskUUID() {
            return vmDiskUUID;
        }

        public void setVmDiskUUID(String vmDiskUUID) {
            this.vmDiskUUID = vmDiskUUID;
        }
    }


}
