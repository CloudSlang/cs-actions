package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixDeleteSnapshotInputs {
    private final String snapshotUUID;
    private final String vmLogicalTimestamp;

    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"snapshotUUID", "vmLogicalTimestamp", "commonInputs"})
    public NutanixDeleteSnapshotInputs(String snapshotUUID, String vmLogicalTimestamp,
                                       NutanixCommonInputs commonInputs) {
        this.snapshotUUID = snapshotUUID;
        this.vmLogicalTimestamp = vmLogicalTimestamp;
        this.commonInputs = commonInputs;
    }

    public static NutanixDeleteSnapshotInputsBuilder builder() {
        return new NutanixDeleteSnapshotInputsBuilder();
    }

    @NotNull
    public String getSnapshotUUID() {
        return snapshotUUID;
    }

    @NotNull
    public String getVmLogicalTimestamp() {
        return vmLogicalTimestamp;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixDeleteSnapshotInputsBuilder {
        private String snapshotUUID = EMPTY;
        private String vmLogicalTimestamp = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixDeleteSnapshotInputsBuilder() {
        }

        @NotNull
        public NutanixDeleteSnapshotInputsBuilder snapshotUUID(@NotNull final String snapshotUUID) {
            this.snapshotUUID = snapshotUUID;
            return this;
        }

        @NotNull
        public NutanixDeleteSnapshotInputsBuilder vmLogicalTimestamp(@NotNull final String vmLogicalTimestamp) {
            this.vmLogicalTimestamp = vmLogicalTimestamp;
            return this;
        }

        @NotNull
        public NutanixDeleteSnapshotInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixDeleteSnapshotInputs build() {
            return new NutanixDeleteSnapshotInputs(snapshotUUID, vmLogicalTimestamp, commonInputs);
        }
    }
}
