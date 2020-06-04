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

package io.cloudslang.content.nutanix.prism.service.models.virtualmachines;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CreateVMRequestBody {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class CreateVMData {
        String name;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String description;
        int memory_mb;
        int num_vcpus;
        int num_cores_per_vcpu;
        String timezone;
        String hypervisor_type;
        @JsonProperty("vm_disks")
        ArrayList vmDisks;
        @JsonProperty("vm_nics")
        ArrayList vmNics;
        @JsonProperty("vm_features")
        VMFeatures vmFeatures;
        @JsonProperty("affinity")
        Affinity affinity;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getMemory_mb() {
            return memory_mb;
        }

        public void setMemory_mb(int memory_mb) {
            this.memory_mb = memory_mb;
        }

        public int getNum_vcpus() {
            return num_vcpus;
        }

        public void setNum_vcpus(int num_vcpus) {
            this.num_vcpus = num_vcpus;
        }

        public int getNum_cores_per_vcpu() {
            return num_cores_per_vcpu;
        }

        public void setNum_cores_per_vcpu(int num_cores_per_vcpu) {
            this.num_cores_per_vcpu = num_cores_per_vcpu;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getHypervisor_type() {
            return hypervisor_type;
        }

        public void setHypervisor_type(String getHypervisor_type) {
            this.hypervisor_type = hypervisor_type;
        }

        public ArrayList getVmDisks() {
            return vmDisks;
        }

        public void setVmDisks(ArrayList vmDisks) {
            this.vmDisks = vmDisks;
        }

        public ArrayList getVmNics() {
            return vmNics;
        }

        public void setVmNics(ArrayList vmNics) {
            this.vmNics = vmNics;
        }

        public VMFeatures getVmFeatures() {
            return vmFeatures;
        }

        public void setVmFeatures(VMFeatures vmFeatures) {
            this.vmFeatures = vmFeatures;
        }

        public Affinity getAffinity() {
            return affinity;
        }

        public void setAffinity(Affinity affinity) {
            this.affinity = affinity;
        }

    }

    public class VMDisks {
        DiskAddress disk_address;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        VMDiskClone vm_disk_clone;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        VMDiskCloneExternal vm_disk_clone_external;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        VMDiskCreate vm_disk_create;
        boolean flash_mode_enabled;
        boolean is_cdrom;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        boolean is_empty;
        boolean is_scsi_pass_through;
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

        public VMDiskCloneExternal getVm_disk_clone_external() {
            return vm_disk_clone_external;
        }

        public void setVm_disk_clone_external(VMDiskCloneExternal vm_disk_clone_external) {
            this.vm_disk_clone_external = vm_disk_clone_external;
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

    public class DiskAddress {
        String device_bus;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String disk_label;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int device_index;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String ndfs_filepath;

        public String getDevice_bus() {
            return device_bus;
        }

        public void setDevice_bus(String device_bus) {
            this.device_bus = device_bus;
        }

        public String getDisk_label() {
            return disk_label;
        }

        public void setDisk_label(String disk_label) {
            this.disk_label = disk_label;
        }

        public int getDevice_index() {
            return device_index;
        }

        public void setDevice_index(int device_index) {
            this.device_index = device_index;
        }

        public String getNdfs_filepath() {
            return ndfs_filepath;
        }

        public void setNdfs_filepath(String ndfs_filepath) {
            this.ndfs_filepath = ndfs_filepath;
        }
    }
    public class VMDiskClone {
        CloneDiskAddress disk_address;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int minimum_size;

        public CloneDiskAddress getDisk_address() {
            return disk_address;
        }

        public void setDisk_address(CloneDiskAddress disk_address) {
            this.disk_address = disk_address;
        }

        public int getMinimum_size() {
            return minimum_size;
        }

        @NotNull
        public void setMinimum_size(int minimum_size) {
            this.minimum_size = minimum_size;
        }
    }
    public class CloneDiskAddress {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String vmdisk_uuid;

        public String getVmdisk_uuid() {
            return vmdisk_uuid;
        }

        public void setVmdisk_uuid(String vmdisk_uuid) {
            this.vmdisk_uuid = vmdisk_uuid;
        }
    }
    public class VMDiskCloneExternal {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String external_disk_url;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int size;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String storage_container_uuid;

        public String getExternal_disk_url() {
            return external_disk_url;
        }

        public void setExternal_disk_url(String external_disk_url) {
            this.external_disk_url = external_disk_url;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getStorage_container_uuid() {
            return storage_container_uuid;
        }

        public void setStorage_container_uuid(String storage_container_uuid) {
            this.storage_container_uuid = storage_container_uuid;
        }
    }

    public class VMDiskCreate {
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
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

    public class VMNics {
        boolean is_connected;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String requested_ip_address;
        String network_uuid;

        public boolean isIs_connected() {
            return is_connected;
        }

        public void setIs_connected(boolean is_connected) {
            this.is_connected = is_connected;
        }

        public String getRequested_ip_address() {
            return requested_ip_address;
        }

        public void setRequested_ip_address(String requested_ip_address) {
            this.requested_ip_address = requested_ip_address;
        }

        public String getNetwork_uuid() {
            return network_uuid;
        }

        public void setNetwork_uuid(String network_uuid) {
            this.network_uuid = network_uuid;
        }
    }

    public class VMFeatures {
        boolean AGENT_VM;

        public boolean isAGENT_VM() {
            return AGENT_VM;
        }

        public void setAGENT_VM(boolean AGENT_VM) {
            this.AGENT_VM = AGENT_VM;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public class Affinity {
        String policy;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        List<String> host_uuids;

        public String getPolicy() {
            return policy;
        }

        public void setPolicy(String policy) {
            this.policy = policy;
        }

        public List<String> getHost_uuids() {
            return host_uuids;
        }

        public void setHost_uuids(List<String> host_uuids) {
            this.host_uuids = host_uuids;
        }
    }
}
