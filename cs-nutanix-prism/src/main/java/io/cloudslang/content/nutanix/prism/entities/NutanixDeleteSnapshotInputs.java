package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixDeleteSnapshotInputs {

    private final String snapshotUUID;
    private final String deleteSnapshots;
    private final String logicalTimestamp;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"snapshotUUID", "deleteSnapshots", "logicalTimestamp", "commonInputs"})
    public NutanixDeleteSnapshotInputs(String snapshotUUID,
                                       String deleteSnapshots,
                                       String logicalTimestamp,
                                       NutanixCommonInputs commonInputs) {
        this.snapshotUUID = snapshotUUID;
        this.deleteSnapshots = deleteSnapshots;
        this.logicalTimestamp = logicalTimestamp;
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

    public static final class NutanixDeleteSnapshotInputsBuilder {
        private String snapshotUUID = EMPTY;
        private String deleteSnapshots = EMPTY;
        private String logicalTimestamp = EMPTY;
        private NutanixCommonInputs commonInputs;

        private NutanixDeleteSnapshotInputsBuilder() {
        }

        @NotNull
        public NutanixDeleteSnapshotInputsBuilder snapshotUUID(String snapshotUUID) {
            this.snapshotUUID = snapshotUUID;
            return this;
        }

        @NotNull
        public NutanixDeleteSnapshotInputsBuilder deleteSnapshots(String deleteSnapshots) {
            this.deleteSnapshots = deleteSnapshots;
            return this;
        }

        @NotNull
        public NutanixDeleteSnapshotInputsBuilder logicalTimestamp(String logicalTimestamp) {
            this.logicalTimestamp = logicalTimestamp;
            return this;
        }

        @NotNull
        public NutanixDeleteSnapshotInputsBuilder commonInputs(NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixDeleteSnapshotInputs build() {
            return new NutanixDeleteSnapshotInputs(snapshotUUID, deleteSnapshots, logicalTimestamp, commonInputs);
        }
    }
}
