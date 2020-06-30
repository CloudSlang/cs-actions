package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

public class OCIDetachVolumeAttachmentInputs {
    private final String volumeAttachmentId;
    private final OCICommonInputs commonInputs;

    @java.beans.ConstructorProperties({"volumeAttachmentId", "commonInputs"})
    private OCIDetachVolumeAttachmentInputs(String volumeAttachmentId, OCICommonInputs commonInputs) {
        this.volumeAttachmentId = volumeAttachmentId;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCIDetachVolumeAttachmentInputs.OCIDetachVolumeAttachmentInputsBuilder builder() {
        return new OCIDetachVolumeAttachmentInputs.OCIDetachVolumeAttachmentInputsBuilder();
    }

    @NotNull
    public String getVolumeAttachmentId() {
        return volumeAttachmentId;
    }

    @NotNull
    public OCICommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static final class OCIDetachVolumeAttachmentInputsBuilder {
        private String volumeAttachmentId;
        private OCICommonInputs commonInputs;

        private OCIDetachVolumeAttachmentInputsBuilder() {
        }


        public OCIDetachVolumeAttachmentInputs.OCIDetachVolumeAttachmentInputsBuilder volumeAttachmentId(String volumeAttachmentId) {
            this.volumeAttachmentId = volumeAttachmentId;
            return this;
        }

        public OCIDetachVolumeAttachmentInputs.OCIDetachVolumeAttachmentInputsBuilder commonInputs(OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCIDetachVolumeAttachmentInputs build() {
            return new OCIDetachVolumeAttachmentInputs(volumeAttachmentId, commonInputs);

        }
    }
}
