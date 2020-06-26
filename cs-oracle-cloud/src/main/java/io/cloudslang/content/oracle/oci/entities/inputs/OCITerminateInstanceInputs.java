package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

public class OCITerminateInstanceInputs {
    private final String preserveBootVolume;
    private final OCICommonInputs commonInputs;

    @java.beans.ConstructorProperties({"preserveBootVolume", "commonInputs"})
    private OCITerminateInstanceInputs(String preserveBootVolume, OCICommonInputs commonInputs) {
        this.preserveBootVolume = preserveBootVolume;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCITerminateInstanceInputs.OCITerminateInstanceInputsBuilder builder() {
        return new OCITerminateInstanceInputs.OCITerminateInstanceInputsBuilder();
    }

    @NotNull
    public String getPreserveBootVolume() {
        return preserveBootVolume;
    }

    @NotNull
    public OCICommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static final class OCITerminateInstanceInputsBuilder {
        private String preserveBootVolume;
        private OCICommonInputs commonInputs;

        private OCITerminateInstanceInputsBuilder() {
        }


        public OCITerminateInstanceInputs.OCITerminateInstanceInputsBuilder preserveBootVolume(String preserveBootVolume) {
            this.preserveBootVolume = preserveBootVolume;
            return this;
        }

        public OCITerminateInstanceInputs.OCITerminateInstanceInputsBuilder commonInputs(OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCITerminateInstanceInputs build() {
            return new OCITerminateInstanceInputs(preserveBootVolume, commonInputs);

        }
    }
}
