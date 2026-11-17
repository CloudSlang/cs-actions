package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixCreateSnapshotInputs {
    private final String vmUUID;
    private final String snapshotName;
    private final String snapshotUUID;
    private final String vmLogicalTimestamp;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vmUUID", "snapshotName", "snapshotUUID", "vmLogicalTimestamp", "commonInputs"})
    public NutanixCreateSnapshotInputs(String vmUUID, String snapshotName, String snapshotUUID,
                                       String vmLogicalTimestamp, NutanixCommonInputs commonInputs) {
        this.vmUUID = vmUUID;
        this.snapshotName = snapshotName;
        this.snapshotUUID = snapshotUUID;
        this.vmLogicalTimestamp = vmLogicalTimestamp;
        this.commonInputs = commonInputs;
    }

    public static NutanixCreateSnapshotInputsBuilder builder() {
        return new NutanixCreateSnapshotInputsBuilder();
    }

    @NotNull
    public String getVmUUID() {
        return vmUUID;
    }

    @NotNull
    public String getSnapshotName() {
        return snapshotName;
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

    public static class NutanixCreateSnapshotInputsBuilder {
        private String vmUUID = EMPTY;
        private String snapshotName = EMPTY;
        private String snapshotUUID = EMPTY;
        private String vmLogicalTimestamp = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixCreateSnapshotInputsBuilder() {
        }

        @NotNull
        public NutanixCreateSnapshotInputsBuilder vmUUID(@NotNull final String vmUUID) {
            this.vmUUID = vmUUID;
            return this;
        }

        @NotNull
        public NutanixCreateSnapshotInputsBuilder snapshotName(@NotNull final String snapshotName) {
            this.snapshotName = snapshotName;
            return this;
        }

        @NotNull
        public NutanixCreateSnapshotInputsBuilder snapshotUUID(@NotNull final String snapshotUUID) {
            this.snapshotUUID = snapshotUUID;
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
            return new NutanixCreateSnapshotInputs(vmUUID, snapshotName, snapshotUUID, vmLogicalTimestamp, commonInputs);
        }
    }
}
