package io.cloudslang.content.oracle.oci.entities.inputs;

import org.jetbrains.annotations.NotNull;

public class OCIInstanceActionInputs {
    private final String actionName;
    private final OCICommonInputs commonInputs;

    @java.beans.ConstructorProperties({"actionName", "commonInputs"})
    private OCIInstanceActionInputs(String actionName, OCICommonInputs commonInputs) {
        this.actionName = actionName;
        this.commonInputs = commonInputs;
    }

    @NotNull
    public static OCIInstanceActionInputs.OCIInstanceActionInputsBuilder builder() {
        return new OCIInstanceActionInputs.OCIInstanceActionInputsBuilder();
    }

    @NotNull
    public String getActionName() {
        return actionName;
    }

    @NotNull
    public OCICommonInputs getCommonInputs() {
        return commonInputs;
    }

    public static final class OCIInstanceActionInputsBuilder {
        private String actionName;
        private OCICommonInputs commonInputs;

        private OCIInstanceActionInputsBuilder() {
        }


        public OCIInstanceActionInputs.OCIInstanceActionInputsBuilder actionName(String actionName) {
            this.actionName = actionName;
            return this;
        }

        public OCIInstanceActionInputs.OCIInstanceActionInputsBuilder commonInputs(OCICommonInputs commonInputs) {
            this.commonInputs = commonInputs;
            return this;
        }

        public OCIInstanceActionInputs build() {
            return new OCIInstanceActionInputs(actionName, commonInputs);

        }
    }
}
