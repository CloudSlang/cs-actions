package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixCreateSnapshotInputs {
    private final String snapshotName;
    private final String vmUUID;
    private final String vmLogicalTimestamp;

    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"snapshotName", "vmUUID", "vmLogicalTimestamp", "commonInputs"})
    public NutanixCreateSnapshotInputs(String snapshotName, String vmUUID, String vmLogicalTimestamp,
                                       NutanixCommonInputs commonInputs) {
        this.snapshotName = snapshotName;
        this.vmUUID = vmUUID;
        this.vmLogicalTimestamp = vmLogicalTimestamp;
        this.commonInputs = commonInputs;
    }

    public static NutanixCreateSnapshotInputsBuilder builder() {
        return new NutanixCreateSnapshotInputsBuilder();
    }

    @NotNull
    public String getSnapshotName() {
        return snapshotName;
    }

    @NotNull
    public String getVmUUID() {
        return vmUUID;
    }

    @NotNull
    public String getVmLogicalTimestamp() {
        return vmLogicalTimestamp;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixCreateSnapshotInputsBuilder {
        private String snapshotName = EMPTY;
        private String vmUUID = EMPTY;
        private String vmLogicalTimestamp = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixCreateSnapshotInputsBuilder() {
        }

        @NotNull
        public NutanixCreateSnapshotInputsBuilder snapshotName(@NotNull final String snapshotName) {
            this.snapshotName = snapshotName;
            return this;
        }

        @NotNull
        public NutanixCreateSnapshotInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixCreateSnapshotInputsBuilder vmLogicalTimestamp(@NotNull final String vmLogicalTimestamp) {
            this.vmLogicalTimestamp = vmLogicalTimestamp;
            return this;
        }

        @NotNull
        public NutanixCreateSnapshotInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixCreateSnapshotInputs build() {
            return new NutanixCreateSnapshotInputs(snapshotName, vmUUID, vmLogicalTimestamp, commonInputs);
        }
    }
}

