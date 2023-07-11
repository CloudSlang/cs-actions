

package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixGetVMDetailsInputs {


    private final String vmUUID;
    private final String includeVMDiskConfigInfo;
    private final String includeVMNicConfigInfo;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "includeVMDiskConfigInfo", "includeVMNicConfigInfo", "commonInputs"})
    public NutanixGetVMDetailsInputs(String vmUUID, String includeVMDiskConfigInfo, String includeVMNicConfigInfo,
                                     NutanixCommonInputs commonInputs) {
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
        public NutanixGetVMDetailsInputs.NutanixGetVMDetailsInputsBuilder includeVMDiskConfigInfo
                (@NotNull final String includeVMDiskConfigInfo) {
            this.includeVMDiskConfigInfo = includeVMDiskConfigInfo;
            return this;
        }

        @NotNull
        public NutanixGetVMDetailsInputs.NutanixGetVMDetailsInputsBuilder includeVMNicConfigInfo
                (@NotNull final String includeVMNicConfigInfo) {
            this.includeVMNicConfigInfo = includeVMNicConfigInfo;
            return this;
        }

        @NotNull
        public NutanixGetVMDetailsInputs.NutanixGetVMDetailsInputsBuilder commonInputs
                (@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixGetVMDetailsInputs build() {
            return new NutanixGetVMDetailsInputs(vmUUID, includeVMDiskConfigInfo, includeVMNicConfigInfo, commonInputs);
        }

    }
}
