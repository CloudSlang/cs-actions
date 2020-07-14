package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

public class OCIVnicAttachmentInputs {
    private final String vnicAttachmentId;
    private final OCICommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vnicAttachmentId", "commonInputs"})
    private OCIVnicAttachmentInputs(String vnicAttachmentId, OCICommonInputs commonInputs) {
        this.vnicAttachmentId = vnicAttachmentId;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCIVnicAttachmentInputs.OCIVnicAttachmentInputsBuilder builder() {
        return new OCIVnicAttachmentInputs.OCIVnicAttachmentInputsBuilder();
    }

    @NotNull
    public String getVnicAttachmentId() {
        return vnicAttachmentId;
    }

    @NotNull
    public OCICommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static final class OCIVnicAttachmentInputsBuilder {
        private String vnicAttachmentId;
        private OCICommonInputs commonInputs;

        private OCIVnicAttachmentInputsBuilder() {
        }


        public OCIVnicAttachmentInputs.OCIVnicAttachmentInputsBuilder vnicAttachmentId(String vnicAttachmentId) {
            this.vnicAttachmentId = vnicAttachmentId;
            return this;
        }

        public OCIVnicAttachmentInputs.OCIVnicAttachmentInputsBuilder commonInputs(OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCIVnicAttachmentInputs build() {
            return new OCIVnicAttachmentInputs(vnicAttachmentId, commonInputs);

        }
    }
}
