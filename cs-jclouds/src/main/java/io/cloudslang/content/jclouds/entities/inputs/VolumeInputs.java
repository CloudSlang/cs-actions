package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.utils.InputsUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mihai Tusa.
 * 6/22/2016.
 */
public class VolumeInputs {
    private CustomInputs customInputs;

    private String snapshotId;
    private String size;
    private String iops;
    private String deviceName;
    private String description;

    private boolean encrypted;
    private boolean force;

    private VolumeInputs(VolumeInputs.VolumeInputsBuilder builder) {
        this.customInputs = builder.customInputs;

        this.snapshotId = builder.snapshotId;
        this.size = builder.size;
        this.iops = builder.iops;
        this.deviceName = builder.deviceName;
        this.description = builder.description;

        this.encrypted = builder.encrypted;
        this.force = builder.force;
    }

    public CustomInputs getCustomInputs() {
        return customInputs;
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

    public static class VolumeInputsBuilder {
        private CustomInputs customInputs;

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

        public VolumeInputsBuilder withCustomInputs(CustomInputs inputs) {
            customInputs = inputs;
            return this;
        }

        public VolumeInputsBuilder withSnapshotId(String inputValue) {
            snapshotId = inputValue;
            return this;
        }

        public VolumeInputsBuilder withSize(String inputValue) {
            size = inputValue;
            return this;
        }

        public VolumeInputsBuilder withIops(String inputValue) {
            iops = StringUtils.isBlank(inputValue) ? Constants.Miscellaneous.NOT_RELEVANT : inputValue;
            return this;
        }

        public VolumeInputsBuilder withDeviceName(String inputValue) {
            deviceName = inputValue;
            return this;
        }

        public VolumeInputsBuilder withDescription(String inputValue) {
            description = inputValue;
            return this;
        }

        public VolumeInputsBuilder withEncrypted(String inputValue) {
            encrypted = InputsUtil.getEnforcedBooleanCondition(inputValue, false);
            return this;
        }

        public VolumeInputsBuilder withForce(String inputValue) {
            force = InputsUtil.getEnforcedBooleanCondition(inputValue, false);
            return this;
        }
    }
}
