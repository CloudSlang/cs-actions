package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

public class OCIAttachVolumeInputs {
    private final String volumeType;
    private final String deviceName;
    private final String displayName;
    private final String isReadOnly;
    private final String isShareable;
    private final OCICommonInputs commonInputs;

    @java.beans.ConstructorProperties({"volumeType", "deviceName", "displayName", "isReadOnly", "isShareable", "commonInputs"})
    private OCIAttachVolumeInputs(String volumeType, String deviceName, String displayName, String isReadOnly,
                                  String isShareable, OCICommonInputs commonInputs) {
        this.volumeType = volumeType;
        this.deviceName = deviceName;
        this.displayName = displayName;
        this.isReadOnly = isReadOnly;
        this.isShareable = isShareable;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCIAttachVolumeInputsBuilder builder() {
        return new OCIAttachVolumeInputsBuilder();
    }

    @NotNull
    public String getVolumeType() {
        return volumeType;
    }

    @NotNull
    public String getDeviceName() {
        return deviceName;
    }

    @NotNull
    public String getDisplayName() {
        return displayName;
    }

    @NotNull
    public String getIsReadOnly() {
        return isReadOnly;
    }

    @NotNull
    public String getIsShareable() {
        return isShareable;
    }

    @NotNull
    public OCICommonInputs getCommonInputs() {
        return commonInputs;
    }


    public static final class OCIAttachVolumeInputsBuilder {
        private String volumeType;
        private String deviceName;
        private String displayName;
        private String isReadOnly;
        private String isShareable;
        private OCICommonInputs commonInputs;

        private OCIAttachVolumeInputsBuilder() {
        }

        public static OCIAttachVolumeInputsBuilder anOCIAttachVolumeInputs() {
            return new OCIAttachVolumeInputsBuilder();
        }

        public OCIAttachVolumeInputsBuilder volumeType(String volumeType) {
            this.volumeType = volumeType;
            return this;
        }

        public OCIAttachVolumeInputsBuilder deviceName(String deviceName) {
            this.deviceName = deviceName;
            return this;
        }

        public OCIAttachVolumeInputsBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public OCIAttachVolumeInputsBuilder isReadOnly(String isReadOnly) {
            this.isReadOnly = isReadOnly;
            return this;
        }

        public OCIAttachVolumeInputsBuilder isShareable(String isShareable) {
            this.isShareable = isShareable;
            return this;
        }

        public OCIAttachVolumeInputsBuilder commonInputs(OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCIAttachVolumeInputs build() {
            return new OCIAttachVolumeInputs(volumeType, deviceName, displayName, isReadOnly, isShareable, commonInputs);
        }
    }
}
