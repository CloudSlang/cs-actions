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

public class NutanixGetVMDetailsInputs {


    private final String vmUUID;
    private final String includeVMDiskConfigInfo;
    private final String includeVMNicConfigInfo;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "includeVMDiskConfigInfo", "includeVMNicConfigInfo", "commonInputs"})
    public NutanixGetVMDetailsInputs(String vmUUID, String includeVMDiskConfigInfo, String includeVMNicConfigInfo, NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.includeVMDiskConfigInfo = includeVMDiskConfigInfo;
        this.includeVMNicConfigInfo = includeVMNicConfigInfo;
        this.commonInputs = commonInputs;
    }

    public static NutanixGetVMDetailsInputs.NutanixGetVMDetailsInputsBuilder builder() {
        return new NutanixGetVMDetailsInputs.NutanixGetVMDetailsInputsBuilder();
    }

    @NotNull
    public String getVMUUID() {
        return vmUUID;
    }

    @NotNull
    public String getIncludeVMDiskConfigInfo() {
        return includeVMDiskConfigInfo;
    }

    @NotNull
    public String getIncludeVMNicConfigInfo() {
        return includeVMNicConfigInfo;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixGetVMDetailsInputsBuilder {
        private String vmUUID = EMPTY;
        private String includeVMDiskConfigInfo = EMPTY;
        private String includeVMNicConfigInfo = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixGetVMDetailsInputsBuilder() {
        }

        @NotNull
        public NutanixGetVMDetailsInputs.NutanixGetVMDetailsInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixGetVMDetailsInputs.NutanixGetVMDetailsInputsBuilder includeVMDiskConfigInfo(@NotNull final String includeVMDiskConfigInfo) {
            this.includeVMDiskConfigInfo = includeVMDiskConfigInfo;
            return this;
        }

        @NotNull
        public NutanixGetVMDetailsInputs.NutanixGetVMDetailsInputsBuilder includeVMNicConfigInfo(@NotNull final String includeVMNicConfigInfo) {
            this.includeVMNicConfigInfo = includeVMNicConfigInfo;
            return this;
        }

        @NotNull
        public NutanixGetVMDetailsInputs.NutanixGetVMDetailsInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixGetVMDetailsInputs build() {
            return new NutanixGetVMDetailsInputs(vmUUID, includeVMDiskConfigInfo, includeVMNicConfigInfo, commonInputs);
        }

    }
}
