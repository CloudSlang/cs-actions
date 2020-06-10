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
