

package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixSetVMPowerStateInputs {

    private final String vmUUID;
    private final String hostUUID;
    private final String transition;
    private final String vmLogicalTimestamp;
    private final NutanixCommonInputs commonInputs;

    @ConstructorProperties({"vmUUID", "hostUUID", "transition", "vmLogicalTimestamp", "commonInputs"})
    public NutanixSetVMPowerStateInputs(String vmUUID, String hostUUID, String transition, String vmLogicalTimestamp,
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

        public NutanixSetVMPowerStateInputs build() {
            return new NutanixSetVMPowerStateInputs(vmUUID, hostUUID, transition, vmLogicalTimestamp, commonInputs);
        }
    }
}
