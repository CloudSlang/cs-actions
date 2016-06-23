package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.utils.InputsUtil;

/**
 * Created by Mihai Tusa.
 * 6/22/2016.
 */
public class VolumeInputs {
    private CustomInputs customInputs;

    private String snapshotId;

    private int size;
    private int iops;

    private boolean encrypted;

    private VolumeInputs(VolumeInputs.VolumeInputsBuilder builder) {
        this.customInputs = builder.customInputs;

        this.snapshotId = builder.snapshotId;

        this.size = builder.size;
        this.iops = builder.iops;

        this.encrypted = builder.encrypted;
    }

    public CustomInputs getCustomInputs() {
        return customInputs;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public int getSize() {
        return size;
    }

    public int getIops() {
        return iops;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public static class VolumeInputsBuilder {
        private CustomInputs customInputs;

        private String snapshotId;

        private int size;
        private int iops;

        private boolean encrypted;

        public VolumeInputs build() {
            return new VolumeInputs(this);
        }

        public VolumeInputsBuilder withCustomInputs(CustomInputs inputs) {
            customInputs = inputs;
            return this;
        }

        public VolumeInputsBuilder withSnapshotId(String inputValue) {
            snapshotId = inputValue;
            return this;
        }

        public VolumeInputsBuilder withSize(String inputValue) {
            size = InputsUtil.getValidSize(inputValue);
            return this;
        }

        public VolumeInputsBuilder withIops(String inputValue) {
            iops = InputsUtil.getValidIops(inputValue);
            return this;
        }

        public VolumeInputsBuilder withEncrypted(String inputValue) {
            encrypted = Boolean.parseBoolean(inputValue);
            return this;
        }
    }
}