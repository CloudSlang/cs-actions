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

public class NutanixListVMsInputs {

    private final String filter;
    private final String offset;
    private final String length;
    private final String sortOrder;
    private final String sortAttribute;
    private final String includeVMDiskConfigInfo;
    private final String includeVMNicConfigInfo;

    private final NutanixCommonInputs commonInputs;

    public String getFilter() {
        return filter;
    }

    public String getOffset() {
        return offset;
    }

    public String getLength() {
        return length;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public String getSortAttribute() {
        return sortAttribute;
    }

    public String getIncludeVMDiskConfigInfo() {
        return includeVMDiskConfigInfo;
    }

    public String getIncludeVMNicConfigInfo() {
        return includeVMNicConfigInfo;
    }

    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    @java.beans.ConstructorProperties({"filter", "offset", "length", "sortOrder", "sortAttribute",
            "includeVMDiskConfigInfo", "includeVMNicConfigInfo", "commonInputs"})
    public NutanixListVMsInputs(String filter, String offset, String length, String sortOrder,
                                String sortAttribute, String includeVMDiskConfigInfo,
                                String includeVMNicConfigInfo, NutanixCommonInputs commonInputs) {
        this.filter = filter;
        this.offset = offset;
        this.length = length;
        this.sortOrder = sortOrder;
        this.sortAttribute = sortAttribute;
        this.includeVMDiskConfigInfo = includeVMDiskConfigInfo;
        this.includeVMNicConfigInfo = includeVMNicConfigInfo;
        this.commonInputs = commonInputs;
    }

    public static NutanixListVMsInputs.NutanixListVMdetailsInputsBuilder builder() {
        return new NutanixListVMsInputs.NutanixListVMdetailsInputsBuilder();
    }

    public static class NutanixListVMdetailsInputsBuilder {
        private String filter = EMPTY;
        private String offset = EMPTY;
        private String length = EMPTY;
        private String sortOrder = EMPTY;
        private String sortAttribute = EMPTY;
        private String includeVMDiskConfigInfo = EMPTY;
        private String includeVMNicConfigInfo = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixListVMdetailsInputsBuilder() {
        }

        @NotNull
        public NutanixListVMsInputs.NutanixListVMdetailsInputsBuilder filter(@NotNull final String filter) {
            this.filter = filter;
            return this;
        }

        @NotNull
        public NutanixListVMsInputs.NutanixListVMdetailsInputsBuilder length(@NotNull final String length) {
            this.length = length;
            return this;
        }

        @NotNull
        public NutanixListVMsInputs.NutanixListVMdetailsInputsBuilder sortOrder(@NotNull final String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        @NotNull
        public NutanixListVMsInputs.NutanixListVMdetailsInputsBuilder sortAttribute
                (@NotNull final String sortAttribute) {
            this.sortAttribute = sortAttribute;
            return this;
        }

        @NotNull
        public NutanixListVMsInputs.NutanixListVMdetailsInputsBuilder offset(@NotNull final String offset) {
            this.offset = offset;
            return this;
        }

        @NotNull
        public NutanixListVMsInputs.NutanixListVMdetailsInputsBuilder includeVMDiskConfigInfo
                (@NotNull final String includeVMDiskConfigInfo) {
            this.includeVMDiskConfigInfo = includeVMDiskConfigInfo;
            return this;
        }

        @NotNull
        public NutanixListVMsInputs.NutanixListVMdetailsInputsBuilder includeVMNicConfigInfo
                (@NotNull final String includeVMNicConfigInfo) {
            this.includeVMNicConfigInfo = includeVMNicConfigInfo;
            return this;
        }

        @NotNull
        public NutanixListVMsInputs.NutanixListVMdetailsInputsBuilder commonInputs
                (@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixListVMsInputs build() {
            return new NutanixListVMsInputs(filter, offset, length, sortOrder, sortAttribute, includeVMDiskConfigInfo,
                    includeVMNicConfigInfo, commonInputs);
        }
    }
}
