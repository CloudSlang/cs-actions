/*******************************************************************************
 * (c) Copyright 2016 Hewlett-Packard Development Company, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *******************************************************************************/
package io.cloudslang.content.amazon.entities.inputs;

import io.cloudslang.content.amazon.utils.InputsUtil;

import static io.cloudslang.content.amazon.entities.constants.Constants.Miscellaneous.EMPTY;

/**
 * Created by Mihai Tusa.
 * 9/15/2016.
 */
public class EbsInputs {
    private final String blockDeviceMappingDeviceNamesString;
    private final String blockDeviceMappingVirtualNamesString;
    private final String deleteOnTerminationsString;
    private final String encryptedString;
    private final String iopsString;
    private final String noDevicesString;
    private final String snapshotIdsString;
    private final String volumeIdsString;
    private final String volumeSizesString;
    private final String volumeTypesString;

    private final boolean ebsOptimized;

    private EbsInputs(Builder builder) {
        this.blockDeviceMappingDeviceNamesString = builder.blockDeviceMappingDeviceNamesString;
        this.blockDeviceMappingVirtualNamesString = builder.blockDeviceMappingVirtualNamesString;
        this.deleteOnTerminationsString = builder.deleteOnTerminationsString;
        this.encryptedString = builder.encryptedString;
        this.iopsString = builder.iopsString;
        this.noDevicesString = builder.noDevicesString;
        this.snapshotIdsString = builder.snapshotIdsString;
        this.volumeIdsString = builder.volumeIdsString;
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

    public String getNoDevicesString() {
        return noDevicesString;
    }

    public String getSnapshotIdsString() {
        return snapshotIdsString;
    }

    public String getVolumeIdsString() {
        return volumeIdsString;
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

    public static class Builder {
        private String blockDeviceMappingDeviceNamesString;
        private String blockDeviceMappingVirtualNamesString;
        private String deleteOnTerminationsString;
        private String encryptedString;
        private String iopsString;
        private String noDevicesString;
        private String snapshotIdsString;
        private String volumeIdsString;
        private String volumeSizesString;
        private String volumeTypesString;

        private boolean ebsOptimized;

        public EbsInputs build() {
            return new EbsInputs(this);
        }

        public Builder withBlockDeviceMappingDeviceNamesString(String inputValue) {
            blockDeviceMappingDeviceNamesString = InputsUtil.getDefaultStringInput(inputValue, EMPTY);
            return this;
        }

        public Builder withBlockDeviceMappingVirtualNamesString(String inputValue) {
            blockDeviceMappingVirtualNamesString = InputsUtil.getDefaultStringInput(inputValue, EMPTY);
            return this;
        }

        public Builder withDeleteOnTerminationsString(String inputValue) {
            deleteOnTerminationsString = inputValue;
            return this;
        }

        public Builder withEncryptedString(String inputValue) {
            encryptedString = inputValue;
            return this;
        }

        public Builder withIopsString(String inputValue) {
            iopsString = inputValue;
            return this;
        }

        public Builder withNoDevicesString(String inputValue) {
            noDevicesString = inputValue;
            return this;
        }

        public Builder withSnapshotIdsString(String inputValue) {
            snapshotIdsString = inputValue;
            return this;
        }

        public Builder withVolumeIdsString(String inputValue) {
            volumeIdsString = inputValue;
            return this;
        }

        public Builder withVolumeSizesString(String inputValue) {
            volumeSizesString = inputValue;
            return this;
        }

        public Builder withVolumeTypesString(String inputValue) {
            volumeTypesString = inputValue;
            return this;
        }

        public Builder withEbsOptimized(String inputValue) {
            ebsOptimized = Boolean.parseBoolean(inputValue);
            return this;
        }
    }
}