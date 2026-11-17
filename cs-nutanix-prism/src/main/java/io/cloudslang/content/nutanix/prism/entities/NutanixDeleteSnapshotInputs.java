package io.cloudslang.content.nutanix.prism.entities;

import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public class NutanixDeleteSnapshotInputs {
    private final String snapshotUUID;
    private final String logicalTimestamp;
    private final NutanixCommonInputs commonInputs;

    @java.beans.ConstructorProperties({"snapshotUUID", "logicalTimestamp", "commonInputs"})
    public NutanixDeleteSnapshotInputs(String snapshotUUID, String logicalTimestamp,
                                       NutanixCommonInputs commonInputs) {
        this.snapshotUUID = snapshotUUID;
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
    public String getLogicalTimestamp() {
        return logicalTimestamp;
    }

    @NotNull
    public NutanixCommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static class NutanixDeleteSnapshotInputsBuilder {
        private String snapshotUUID = EMPTY;
        private String logicalTimestamp = EMPTY;
        private NutanixCommonInputs commonInputs;

        NutanixDeleteSnapshotInputsBuilder() {
        }

        @NotNull
        public NutanixDeleteSnapshotInputsBuilder snapshotUUID(@NotNull final String snapshotUUID) {
            this.snapshotUUID = snapshotUUID;
            return this;
        }

        @NotNull
        public NutanixDeleteSnapshotInputsBuilder logicalTimestamp(@NotNull final String logicalTimestamp) {
            this.logicalTimestamp = logicalTimestamp;
            return this;
        }

        @NotNull
        public NutanixDeleteSnapshotInputsBuilder commonInputs(@NotNull final NutanixCommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public NutanixDeleteSnapshotInputs build() {
            return new NutanixDeleteSnapshotInputs(snapshotUUID, logicalTimestamp, commonInputs);
        }
    }
}
