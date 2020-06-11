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

import java.beans.ConstructorProperties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixSetPowerStateInputs {

    private final String vmUUID;
    private final String hostUUID;
    private final String transition;
    private final String vmLogicalTimestamp;
    private final NutanixCommonInputs commonInputs;

    @ConstructorProperties({"vmUUID", "hostUUID", "transition", "vmLogicalTimestamp", "commonInputs"})
    public NutanixSetPowerStateInputs(String vmUUID, String hostUUID, String transition, String vmLogicalTimestamp,
                                      NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.hostUUID = hostUUID;
        this.transition = transition;
        this.vmLogicalTimestamp = vmLogicalTimestamp;
        this.commonInputs = commonInputs;
    }

    public static NutanixSetPowerStateInputsBuilder builder() {
        return new NutanixSetPowerStateInputsBuilder();
    }

    @NotNull
    public String getVMUUID() {
        return vmUUID;
    }

    @NotNull
    public String getHostUUID() {
        return hostUUID;
    }

    @NotNull
    public String getTransition() {
        return transition;
    }

    @NotNull
    public String getVmLogicalTimestamp() {
        return vmLogicalTimestamp;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }


    public static final class NutanixSetPowerStateInputsBuilder {
        private String vmUUID = EMPTY;
        private String hostUUID = EMPTY;
        private String transition = EMPTY;
        private String vmLogicalTimestamp = EMPTY;
        private NutanixCommonInputs commonInputs;


        private NutanixSetPowerStateInputsBuilder() {
        }

        @NotNull
        public NutanixSetPowerStateInputsBuilder vmUUID(String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixSetPowerStateInputsBuilder hostUUID(String hostUUID) {
            this.hostUUID = hostUUID;
            return this;
        }

        @NotNull
        public NutanixSetPowerStateInputsBuilder transition(String transition) {
            this.transition = transition;
            return this;
        }

        @NotNull
        public NutanixSetPowerStateInputsBuilder vmLogicalTimestamp(String vmLogicalTimestamp) {
            this.vmLogicalTimestamp = vmLogicalTimestamp;
            return this;
        }

        @NotNull
        public NutanixSetPowerStateInputsBuilder commonInputs(NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixSetPowerStateInputs build() {
            return new NutanixSetPowerStateInputs(vmUUID, hostUUID, transition, vmLogicalTimestamp, commonInputs);
        }
    }
}
