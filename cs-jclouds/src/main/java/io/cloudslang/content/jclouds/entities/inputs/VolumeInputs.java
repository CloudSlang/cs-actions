package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.utils.InputsUtil;

import static org.apache.commons.lang3.StringUtils.isBlank;

import static io.cloudslang.content.jclouds.entities.constants.Constants.Miscellaneous.NOT_RELEVANT;
/**
 * Created by Mihai Tusa.
 * 6/22/2016.
 */
public class VolumeInputs {
    private String snapshotId;
    private String size;
    private String iops;
    private String deviceName;
    private String description;

    private boolean encrypted;
    private boolean force;

    private VolumeInputs(Builder builder) {
        this.snapshotId = builder.snapshotId;
        this.size = builder.size;
        this.iops = builder.iops;
        this.deviceName = builder.deviceName;
        this.description = builder.description;

        this.encrypted = builder.encrypted;
        this.force = builder.force;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public String getSize() {
        return size;
    }

    public String getIops() {
        return iops;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDescription() {
        return description;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public boolean isForce() {
        return force;
    }

    public static class Builder {
        private String snapshotId;
        private String size;
        private String iops;
        private String deviceName;
        private String description;

        private boolean encrypted;
        private boolean force;

        public VolumeInputs build() {
            return new VolumeInputs(this);
        }

        public Builder withSnapshotId(String inputValue) {
            snapshotId = inputValue;
            return this;
        }

        public Builder withSize(String inputValue) {
            size = inputValue;
            return this;
        }

        public Builder withIops(String inputValue) {
            iops = isBlank(inputValue) ? NOT_RELEVANT : inputValue;
            return this;
        }

        public Builder withDeviceName(String inputValue) {
            deviceName = inputValue;
            return this;
        }

        public Builder withDescription(String inputValue) {
            description = inputValue;
            return this;
        }

        public Builder withEncrypted(String inputValue) {
            encrypted = InputsUtil.getEnforcedBooleanCondition(inputValue, false);
            return this;
        }

        public Builder withForce(String inputValue) {
            force = InputsUtil.getEnforcedBooleanCondition(inputValue, false);
            return this;
        }
    }
}