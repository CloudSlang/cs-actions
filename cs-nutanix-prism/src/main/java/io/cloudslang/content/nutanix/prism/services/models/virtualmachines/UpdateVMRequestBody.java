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

package io.cloudslang.content.nutanix.prism.services.models.virtualmachines;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateVMRequestBody {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public class UpdateVMData {

        String name;
        String description;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int memory_mb;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int num_vcpus;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        int num_cores_per_vcpu;
        String timezone;
        @JsonProperty("vm_features")
        UpdateVMRequestBody.VMFeatures vmFeatures;
        @JsonProperty("affinity")
        UpdateVMRequestBody.Affinity affinity;


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

        public UpdateVMRequestBody.VMFeatures getVmFeatures() {
            return vmFeatures;
        }

        public void setVmFeatures(UpdateVMRequestBody.VMFeatures vmFeatures) {
            this.vmFeatures = vmFeatures;
        }

        public UpdateVMRequestBody.Affinity getAffinity() {
            return affinity;
        }

        public void setAffinity(UpdateVMRequestBody.Affinity affinity) {
            this.affinity = affinity;
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
