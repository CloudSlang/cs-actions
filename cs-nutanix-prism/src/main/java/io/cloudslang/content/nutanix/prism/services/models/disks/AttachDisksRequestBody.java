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

import com.fasterxml.jackson.annotation.JsonInclude;
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
        @JsonInclude(JsonInclude.Include.NON_NULL)
        DiskAddress disk_address;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        VMDiskClone vm_disk_clone;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        VMDiskCreate vm_disk_create;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        boolean flash_mode_enabled;
        boolean is_cdrom;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        boolean is_empty;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        boolean is_scsi_pass_through;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        boolean is_thin_provisioned;

        public DiskAddress getDisk_address() {
            return disk_address;
        }

        public void setDisk_address(DiskAddress disk_address) {
            this.disk_address = disk_address;
        }

        public VMDiskCreate getVm_disk_create() {
            return vm_disk_create;
        }

        public void setVm_disk_create(VMDiskCreate vm_disk_create) {
            this.vm_disk_create = vm_disk_create;
        }

        public VMDiskClone getVm_disk_clone() {
            return vm_disk_clone;
        }

        public void setVm_disk_clone(VMDiskClone vm_disk_clone) {
            this.vm_disk_clone = vm_disk_clone;
        }

        public boolean isFlash_mode_enabled() {
            return flash_mode_enabled;
        }

        public void setFlash_mode_enabled(boolean flash_mode_enabled) {
            this.flash_mode_enabled = flash_mode_enabled;
        }

        public boolean isIs_cdrom() {
            return is_cdrom;
        }

        public void setIs_cdrom(boolean is_cdrom) {
            this.is_cdrom = is_cdrom;
        }

        public boolean isIs_empty() {
            return is_empty;
        }

        public void setIs_empty(boolean is_empty) {
            this.is_empty = is_empty;
        }

        public boolean isIs_scsi_pass_through() {
            return is_scsi_pass_through;
        }

        public void setIs_scsi_pass_through(boolean is_scsi_pass_through) {
            this.is_scsi_pass_through = is_scsi_pass_through;
        }

        public boolean isIs_thin_provisioned() {
            return is_thin_provisioned;
        }

        public void setIs_thin_provisioned(boolean is_thin_provisioned) {
            this.is_thin_provisioned = is_thin_provisioned;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class DiskAddress {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String device_bus;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int device_index;

        public String getDevice_bus() {
            return device_bus;
        }

        public void setDevice_bus(String device_bus) {
            this.device_bus = device_bus;
        }

        public int getDevice_index() {
            return device_index;
        }

        public void setDevice_index(int device_index) {
            this.device_index = device_index;
        }
    }

    public class VMDiskCreate {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        long size;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
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

    public class VMDiskClone {
        CloneDiskAddress disk_address;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        long minimum_size;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String storage_container_uuid;

        public CloneDiskAddress getDisk_address() {
            return disk_address;
        }

        public void setDisk_address(CloneDiskAddress disk_address) {
            this.disk_address = disk_address;
        }

        public long getMinimum_size() {
            return minimum_size;
        }

        public void setMinimum_size(long minimum_size) {
            this.minimum_size = minimum_size;
        }

        public String getStorage_container_uuid() {
            return storage_container_uuid;
        }

        public void setStorage_container_uuid(String storage_container_uuid) {
            this.storage_container_uuid = storage_container_uuid;
        }
    }

    public class CloneDiskAddress {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String vmdisk_uuid;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String ndfs_filepath;

        public String getVmdisk_uuid() {
            return vmdisk_uuid;
        }

        public void setVmdisk_uuid(String vmdisk_uuid) {
            this.vmdisk_uuid = vmdisk_uuid;
        }

        public String getNdfs_filepath() {
            return ndfs_filepath;
        }

        public void setNdfs_filepath(String ndfs_filepath) {
            this.ndfs_filepath = ndfs_filepath;
        }

    }
}
