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

import java.util.List;

public class UpdateDiskRequestBody {
    @JsonProperty("vm_disks")
    private List<VMDisk> vmDisks;

    public void setVmDisks(List<VMDisk> vmDisks) {
        this.vmDisks = vmDisks;
    }

    public static class VMDisk {
        @JsonProperty("disk_address")
        private DiskAddress diskAddress;
        @JsonProperty("vm_disk_create")
        private VMDiskCreate vmDiskCreate;

        public void setDiskAddress(DiskAddress diskAddress) {
            this.diskAddress = diskAddress;
        }

        public void setVmDiskCreate(VMDiskCreate vmDiskCreate) {
            this.vmDiskCreate = vmDiskCreate;
        }
    }

    public static class DiskAddress {
        @JsonProperty("vmdisk_uuid")
        private String vmDiskUUID;

        public void setVmDiskUUID(String vmDiskUUID) {
            this.vmDiskUUID = vmDiskUUID;
        }
    }

    public static class VMDiskCreate {
        @JsonProperty("size")
        private long size;

        public void setSize(long size) {
            this.size = size;
        }
    }
}
