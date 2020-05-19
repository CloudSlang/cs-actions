package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixListVMdetailsInputs {

    private final String filter;
    private final String offset;
    private final String length;
    private final String sortorder;
    private final String sortattribute;
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

    public String getSortorder() {
        return sortorder;
    }

    public String getSortattribute() {
        return sortattribute;
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

    @java.beans.ConstructorProperties({"filter", "offset", "length", "sortorder", "sortattribute", "includeVMDiskConfigInfo", "includeVMNicConfigInfo", "commonInputs"})
    public NutanixListVMdetailsInputs(String filter, String offset, String length, String sortorder, String sortattribute, String includeVMDiskConfigInfo, String includeVMNicConfigInfo, NutanixCommonInputs commonInputs) {
        this.filter = filter;
        this.offset = offset;
        this.length = length;
        this.sortorder = sortorder;
        this.sortattribute = sortattribute;
        this.includeVMDiskConfigInfo = includeVMDiskConfigInfo;
        this.includeVMNicConfigInfo = includeVMNicConfigInfo;
        this.commonInputs = commonInputs;
    }

    public static NutanixListVMdetailsInputs.NutanixListVMdetailsInputsBuilder builder() {
        return new NutanixListVMdetailsInputs.NutanixListVMdetailsInputsBuilder();
    }

    public static class NutanixListVMdetailsInputsBuilder {
        private String filter = EMPTY;
        private String offset = EMPTY;
        private String length = EMPTY;
        private String sortorder = EMPTY;
        private String sortattribute = EMPTY;
        private String includeVMDiskConfigInfo = EMPTY;
        private String includeVMNicConfigInfo = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixListVMdetailsInputsBuilder() {
        }

        @NotNull
        public NutanixListVMdetailsInputs.NutanixListVMdetailsInputsBuilder filter(@NotNull final String filter) {
            this.filter = filter;
            return this;
        }

        @NotNull
        public NutanixListVMdetailsInputs.NutanixListVMdetailsInputsBuilder length(@NotNull final String length) {
            this.length = length;
            return this;
        }

        @NotNull
        public NutanixListVMdetailsInputs.NutanixListVMdetailsInputsBuilder sortorder(@NotNull final String sortorder) {
            this.sortorder = sortorder;
            return this;
        }

        @NotNull
        public NutanixListVMdetailsInputs.NutanixListVMdetailsInputsBuilder sortattribute(@NotNull final String sortattribute) {
            this.sortattribute = sortattribute;
            return this;
        }

        @NotNull
        public NutanixListVMdetailsInputs.NutanixListVMdetailsInputsBuilder offset(@NotNull final String offset) {
            this.offset = offset;
            return this;
        }

        @NotNull
        public NutanixListVMdetailsInputs.NutanixListVMdetailsInputsBuilder includeVMDiskConfigInfo(@NotNull final String includeVMDiskConfigInfo) {
            this.includeVMDiskConfigInfo = includeVMDiskConfigInfo;
            return this;
        }

        @NotNull
        public NutanixListVMdetailsInputs.NutanixListVMdetailsInputsBuilder includeVMNicConfigInfo(@NotNull final String includeVMNicConfigInfo) {
            this.includeVMNicConfigInfo = includeVMNicConfigInfo;
            return this;
        }

        @NotNull
        public NutanixListVMdetailsInputs.NutanixListVMdetailsInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixListVMdetailsInputs build() {
            return new NutanixListVMdetailsInputs(filter,offset,length,sortorder,sortattribute,includeVMDiskConfigInfo, includeVMNicConfigInfo, commonInputs);
        }
    }
}
