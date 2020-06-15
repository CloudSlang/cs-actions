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

public class NutanixDeleteVMInputs {

    private final String vmUUID;
    private final String deleteSnapshots;
    private final String logicalTimestamp;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "deleteSnapshots", "logicalTimestamp", "commonInputs"})
    public NutanixDeleteVMInputs(String vmUUID, String deleteSnapshots, String logicalTimestamp,
                                 NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.deleteSnapshots = deleteSnapshots;
        this.logicalTimestamp = logicalTimestamp;
        this.commonInputs = commonInputs;
    }

    public static NutanixDeleteVMInputsBuilder builder() {
        return new NutanixDeleteVMInputsBuilder();
    }

    @NotNull
    public String getVMUUID() {
        return vmUUID;
    }

    @NotNull
    public String getDeleteSnapshots() {
        return deleteSnapshots;
    }

    @NotNull
    public String getLogicalTimestamp() {
        return logicalTimestamp;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }


    public static final class NutanixDeleteVMInputsBuilder {
        private String vmUUID = EMPTY;
        private String deleteSnapshots = EMPTY;
        private String logicalTimestamp = EMPTY;
        private NutanixCommonInputs commonInputs;

        private NutanixDeleteVMInputsBuilder() {
        }

        @NotNull
        public NutanixDeleteVMInputsBuilder vmUUID(String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixDeleteVMInputsBuilder deleteSnapshots(String deleteSnapshots) {
            this.deleteSnapshots = deleteSnapshots;
            return this;
        }

        @NotNull
        public NutanixDeleteVMInputsBuilder logicalTimestamp(String logicalTimestamp) {
            this.logicalTimestamp = logicalTimestamp;
            return this;
        }

        @NotNull
        public NutanixDeleteVMInputsBuilder commonInputs(NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixDeleteVMInputs build() {
            return new NutanixDeleteVMInputs(vmUUID, deleteSnapshots, logicalTimestamp, commonInputs);
        }
    }
}
