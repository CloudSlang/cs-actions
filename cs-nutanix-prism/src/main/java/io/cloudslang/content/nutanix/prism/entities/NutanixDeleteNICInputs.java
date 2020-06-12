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

public class NutanixDeleteNICInputs {
    private final String vmUUID;
    private final String nicMacAddress;
    private final String vmLogicalTimestamp;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "nicMacAddress", "vmLogicalTimestamp", "commonInputs"})
    public NutanixDeleteNICInputs(String vmUUID, String nicMacAddress, String vmLogicalTimestamp,
                                  NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.nicMacAddress = nicMacAddress;
        this.vmLogicalTimestamp = vmLogicalTimestamp;
        this.commonInputs = commonInputs;
    }

    public static NutanixDeleteNICInputs.NutanixDeleteNICInputsBuilder builder() {
        return new NutanixDeleteNICInputs.NutanixDeleteNICInputsBuilder();
    }

    @NotNull
    public String getVMUUID() {
        return vmUUID;
    }

    @NotNull
    public String getNicMacAddress() {
        return nicMacAddress;
    }

    @NotNull
    public String getVmLogicalTimestamp() {
        return vmLogicalTimestamp;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixDeleteNICInputsBuilder {
        private String vmUUID = EMPTY;
        private String nicMacAddress = EMPTY;
        private String vmLogicalTimestamp = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixDeleteNICInputsBuilder() {
        }

        @NotNull
        public NutanixDeleteNICInputs.NutanixDeleteNICInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixDeleteNICInputs.NutanixDeleteNICInputsBuilder nicMacAddress
                (@NotNull final String nicMacAddress) {
            this.nicMacAddress = nicMacAddress;
            return this;
        }

        @NotNull
        public NutanixDeleteNICInputs.NutanixDeleteNICInputsBuilder vmLogicalTimestamp
                (@NotNull final String vmLogicalTimestamp) {
            this.vmLogicalTimestamp = vmLogicalTimestamp;
            return this;
        }

        @NotNull
        public NutanixDeleteNICInputs.NutanixDeleteNICInputsBuilder commonInputs
                (@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixDeleteNICInputs build() {
            return new NutanixDeleteNICInputs(vmUUID, nicMacAddress, vmLogicalTimestamp, commonInputs);
        }

    }
}
