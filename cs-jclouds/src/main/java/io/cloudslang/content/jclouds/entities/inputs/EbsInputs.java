package io.cloudslang.content.jclouds.entities.inputs;

import io.cloudslang.content.jclouds.entities.constants.Constants;
import io.cloudslang.content.jclouds.utils.InputsUtil;

/**
 * Created by Mihai Tusa.
 * 9/15/2016.
 */
public class EbsInputs {
    private String blockDeviceMappingDeviceNamesString;
    private String blockDeviceMappingVirtualNamesString;
    private String deleteOnTerminationsString;
    private String encryptedString;
    private String iopsString;
    private String snapshotIdsString;
    private String volumeSizesString;
    private String volumeTypesString;

    private boolean ebsOptimized;

    private EbsInputs(EbsInputs.EbsInputsBuilder builder) {
        this.blockDeviceMappingDeviceNamesString = builder.blockDeviceMappingDeviceNamesString;
        this.blockDeviceMappingVirtualNamesString = builder.blockDeviceMappingVirtualNamesString;
        this.deleteOnTerminationsString = builder.deleteOnTerminationsString;
        this.encryptedString = builder.encryptedString;
        this.iopsString = builder.iopsString;
        this.snapshotIdsString = builder.snapshotIdsString;
        this.volumeSizesString = builder.volumeSizesString;
        this.volumeTypesString = builder.volumeTypesString;

        this.ebsOptimized = builder.ebsOptimized;
    }

    public String getBlockDeviceMappingDeviceNamesString() {
        return blockDeviceMappingDeviceNamesString;
    }

    public String getBlockDeviceMappingVirtualNamesString() {
        return blockDeviceMappingVirtualNamesString;
    }

    public String getDeleteOnTerminationsString() {
        return deleteOnTerminationsString;
    }

    public String getEncryptedString() {
        return encryptedString;
    }

    public String getIopsString() {
        return iopsString;
    }

    public String getSnapshotIdsString() {
        return snapshotIdsString;
    }

    public String getVolumeSizesString() {
        return volumeSizesString;
    }

    public String getVolumeTypesString() {
        return volumeTypesString;
    }

    public boolean isEbsOptimized() {
        return ebsOptimized;
    }

    public static class EbsInputsBuilder {
        private String blockDeviceMappingDeviceNamesString;
        private String blockDeviceMappingVirtualNamesString;
        private String deleteOnTerminationsString;
        private String encryptedString;
        private String iopsString;
        private String snapshotIdsString;
        private String volumeSizesString;
        private String volumeTypesString;

        private boolean ebsOptimized;

        public EbsInputs build() {
            return new EbsInputs(this);
        }

        public EbsInputs.EbsInputsBuilder withBlockDeviceMappingDeviceNamesString(String inputValue) {
            blockDeviceMappingDeviceNamesString = InputsUtil.getDefaultStringInput(inputValue, Constants.Miscellaneous.EMPTY);
            return this;
        }

        public EbsInputs.EbsInputsBuilder withBlockDeviceMappingVirtualNamesString(String inputValue) {
            blockDeviceMappingVirtualNamesString = InputsUtil.getDefaultStringInput(inputValue, Constants.Miscellaneous.EMPTY);
            return this;
        }

        public EbsInputs.EbsInputsBuilder withDeleteOnTerminationsString(String inputValue) {
            deleteOnTerminationsString = inputValue;
            return this;
        }

        public EbsInputs.EbsInputsBuilder withEncryptedString(String inputValue) {
            encryptedString = inputValue;
            return this;
        }

        public EbsInputs.EbsInputsBuilder withIopsString(String inputValue) {
            iopsString = inputValue;
            return this;
        }

        public EbsInputs.EbsInputsBuilder withSnapshotIdsString(String inputValue) {
            snapshotIdsString = inputValue;
            return this;
        }

        public EbsInputs.EbsInputsBuilder withVolumeSizesString(String inputValue) {
            volumeSizesString = inputValue;
            return this;
        }

        public EbsInputs.EbsInputsBuilder withVolumeTypesString(String inputValue) {
            volumeTypesString = inputValue;
            return this;
        }

        public EbsInputs.EbsInputsBuilder withEbsOptimized(String inputValue) {
            ebsOptimized = Boolean.parseBoolean(inputValue);
            return this;
        }
    }
}