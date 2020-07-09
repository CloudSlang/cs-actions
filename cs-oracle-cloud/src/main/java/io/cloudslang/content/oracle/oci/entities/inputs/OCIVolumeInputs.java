package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

public class OCIVolumeInputs {
    private final String volumeAttachmentId;
    private final OCICommonInputs commonInputs;

    @java.beans.ConstructorProperties({"volumeAttachmentId", "commonInputs"})
    private OCIVolumeInputs(String volumeAttachmentId, OCICommonInputs commonInputs) {
        this.volumeAttachmentId = volumeAttachmentId;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCIVolumeInputs.OCIDetachVolumeAttachmentInputsBuilder builder() {
        return new OCIVolumeInputs.OCIDetachVolumeAttachmentInputsBuilder();
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


        public OCIVolumeInputs.OCIDetachVolumeAttachmentInputsBuilder volumeAttachmentId(String volumeAttachmentId) {
            this.volumeAttachmentId = volumeAttachmentId;
            return this;
        }

        public OCIVolumeInputs.OCIDetachVolumeAttachmentInputsBuilder commonInputs(OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCIVolumeInputs build() {
            return new OCIVolumeInputs(volumeAttachmentId, commonInputs);

        }
    }
}
