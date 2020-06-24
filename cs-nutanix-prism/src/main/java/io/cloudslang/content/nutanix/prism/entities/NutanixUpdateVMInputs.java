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

public class NutanixUpdateVMInputs {

    private final String vmUUID;
    private final String vmName;
    private final String description;
    private final String vmMemorySize;
    private final String numVCPUs;
    private final String numCoresPerVCPU;
    private final String timeZone;
    private final String hostUUIDs;
    private final String agentVM;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "vmName", "description", "vmMemorySize", "numVCPUs", "numCoresPerVCPU"
            , "timeZone", "hostUUIDs", "agentVM", "commonInputs"})
    public NutanixUpdateVMInputs(String vmUUID, String vmName, String description, String vmMemorySize, String numVCPUs,
                                 String numCoresPerVCPU, String timeZone,
                                 String hostUUIDs, String agentVM, NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.vmName = vmName;
        this.description = description;
        this.vmMemorySize = vmMemorySize;
        this.numVCPUs = numVCPUs;
        this.numCoresPerVCPU = numCoresPerVCPU;
        this.timeZone = timeZone;
        this.hostUUIDs = hostUUIDs;
        this.agentVM = agentVM;
        this.commonInputs = commonInputs;
    }

    public static NutanixUpdateVMInputsBuilder builder() {
        return new NutanixUpdateVMInputsBuilder();
    }

    @NotNull
    public String getVmUUID() {
        return vmUUID;
    }

    @NotNull
    public String getVmName() {
        return vmName;
    }

    @NotNull
    public String getDescription() {
        return description;
    }

    @NotNull
    public String getVmMemorySize() {
        return vmMemorySize;
    }

    @NotNull
    public String getNumVCPUs() {
        return numVCPUs;
    }

    @NotNull
    public String getNumCoresPerVCPU() {
        return numCoresPerVCPU;
    }

    @NotNull
    public String getTimeZone() {
        return timeZone;
    }


    @NotNull
    public String getHostUUIDs() {
        return hostUUIDs;
    }

    @NotNull
    public String getAgentVM() {
        return agentVM;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixUpdateVMInputsBuilder {
        private String vmUUID = EMPTY;
        private String vmName = EMPTY;
        private String description = EMPTY;
        private String vmMemorySize = EMPTY;
        private String numVCPUs = EMPTY;
        private String numCoresPerVCPU = EMPTY;
        private String timeZone = EMPTY;
        private String agentVM = EMPTY;
        private String hostUUIDs = EMPTY;

        private NutanixCommonInputs commonInputs;

        NutanixUpdateVMInputsBuilder() {
        }

        @NotNull
        public NutanixUpdateVMInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixUpdateVMInputsBuilder vmName(@NotNull final String vmName) {
            this.vmName = vmName;
            return this;
        }

        @NotNull
        public NutanixUpdateVMInputsBuilder description(@NotNull final String description) {
            this.description = description;
            return this;
        }

        @NotNull
        public NutanixUpdateVMInputsBuilder vmMemorySize(@NotNull final String vmMemorySize) {
            this.vmMemorySize = vmMemorySize;
            return this;
        }

        @NotNull
        public NutanixUpdateVMInputsBuilder numVCPUs(@NotNull final String numVCPUs) {
            this.numVCPUs = numVCPUs;
            return this;
        }

        @NotNull
        public NutanixUpdateVMInputsBuilder numCoresPerVCPU(@NotNull final String numCoresPerVCPU) {
            this.numCoresPerVCPU = numCoresPerVCPU;
            return this;
        }

        @NotNull
        public NutanixUpdateVMInputsBuilder timeZone(@NotNull final String timeZone) {
            this.timeZone = timeZone;
            return this;
        }

        @NotNull
        public NutanixUpdateVMInputsBuilder hostUUIDs(@NotNull final String hostUUIDs) {
            this.hostUUIDs = hostUUIDs;
            return this;
        }

        @NotNull
        public NutanixUpdateVMInputsBuilder agentVM(@NotNull final String agentVM) {
            this.agentVM = agentVM;
            return this;
        }

        @NotNull
        public NutanixUpdateVMInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixUpdateVMInputs build() {
            return new NutanixUpdateVMInputs(vmUUID, vmName, description, vmMemorySize, numVCPUs, numCoresPerVCPU
                    , timeZone, hostUUIDs, agentVM, commonInputs);
        }
    }
}
