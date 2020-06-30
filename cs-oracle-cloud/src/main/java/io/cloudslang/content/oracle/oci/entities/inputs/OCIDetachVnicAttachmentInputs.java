package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

public class OCIDetachVnicAttachmentInputs {
    private final String vnicAttachmentId;
    private final OCICommonInputs commonInputs;

    @java.beans.ConstructorProperties({"vnicAttachmentId", "commonInputs"})
    private OCIDetachVnicAttachmentInputs(String vnicAttachmentId, OCICommonInputs commonInputs) {
        this.vnicAttachmentId = vnicAttachmentId;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCIDetachVnicAttachmentInputs.OCIDetachVnicAttachmentInputsBuilder builder() {
        return new OCIDetachVnicAttachmentInputs.OCIDetachVnicAttachmentInputsBuilder();
    }

    @NotNull
    public String getVnicAttachmentId() {
        return vnicAttachmentId;
    }

    @NotNull
    public OCICommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static final class OCIDetachVnicAttachmentInputsBuilder {
        private String vnicAttachmentId;
        private OCICommonInputs commonInputs;

        private OCIDetachVnicAttachmentInputsBuilder() {
        }


        public OCIDetachVnicAttachmentInputs.OCIDetachVnicAttachmentInputsBuilder vnicAttachmentId(String vnicAttachmentId) {
            this.vnicAttachmentId = vnicAttachmentId;
            return this;
        }

        public OCIDetachVnicAttachmentInputs.OCIDetachVnicAttachmentInputsBuilder commonInputs(OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCIDetachVnicAttachmentInputs build() {
            return new OCIDetachVnicAttachmentInputs(vnicAttachmentId, commonInputs);

        }
    }
}
